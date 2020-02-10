package globaz.osiris.db.utils;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JABVR;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.access.recouvrement.CAEcheancePlan;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.parser.IntReferenceBVRParser;

/**
 * @author sel Créé le : 6 déc. 06
 */
public class CAReferenceBVR extends AbstractCAReference{

    private static final String ERROR_MONTANT_NEGATIF = "Error : Montant négatif !";

    public static final String OCRB_NON_FACTURABLE = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

    private Boolean forcerBV = false;

    private String ocrb = CAReferenceBVR.OCRB_NON_FACTURABLE;


    /**
     *
     *            nécessaire pour aller chercher le no d'adhérent et le no de compte dans le catalogue de textes de
     *            MUSCA.
     * @throws Exception
     *             si erreur lors du chargement des textes de Babel
     */
    public CAReferenceBVR() {
        super();
    }

    /**
     * Permet de générer un numéro de référence pour un BVR.
     * 
     * @param section
     * @return
     * @throws Exception
     */
    private String genererNumReferenceBVR(APISection section) throws Exception {
        return this.genererNumReference(section.getCompteAnnexe().getIdRole(), section.getCompteAnnexe()
                .getIdExterneRole(), false, section.getIdTypeSection(), section.getIdExterne(), section
                .getIdCompteAnnexe());
    }

    /**
     * Permet de générer un numéro de référence pour un BVR.
     * 
     * @param idRole
     * @param idExterneRole
     * @param isPlanPaiement
     * @param typeSection
     * @param idExterneSection
     * @return
     * @throws Exception
     */
    private String genererNumReferenceBVR(String idRole, String idExterneRole, boolean isPlanPaiement,
            String typeSection, String idExterneSection) throws Exception {
        return genererNumReference(idRole, idExterneRole, isPlanPaiement, typeSection, idExterneSection, null);
    }


    /**
     * @return the forcerBV
     */
    public Boolean getForcerBV() {
        return forcerBV;
    }

    /**
     * Va chercher le numéro de l'adherent dans Babel
     * 
     * @author: sel Créé le : 28 nov. 06
     * @return le N° adherent (ex: 010123451)
     * @throws Exception
     */
    public String getNoAdherent() throws Exception {
        String res = "";
        res = getTexteBabel(2, 2);
        return res;
    }

    /**
     * @return the ocrb
     */
    public String getOcrb() {
        return ocrb;
    }

    /**
     * @author: sel Créé le : 22 janv. 07
     * @return le numéro de référence sans espace
     */
    public String getRefNoSpace() {
        return removeNotLetterNotDigit(getLigneReference());
    }


    /**
     * Retourne l'adresse pour le BVR (va rechercher dans le catalogue de textes MUSCA)
     * 
     * @return
     */
    public String getTextBVRNeutre(BSession session, String langueIso) {
        StringBuffer text = new StringBuffer("");
        try {
            // va rechercher les textes qui sont au niveau 1
            if (this.getCurrentDocument(session, langueIso) == null) {
                text.append(TEXTE_INTROUVABLE);
            } else {
                this.dumpNiveau(3, text, RETOUR_LIGNE, langueIso);
            }
        } catch (Exception e3) {
            text.append(TEXTE_INTROUVABLE);
        }
        return text.toString();
    }

    /**
     * @author: sel Créé le : 18 janv. 07
     * @param transaction
     * @param montant
     * @return
     */
    private boolean isFactureAvecMontantMinime(BTransaction transaction, String montant) throws Exception {
        if (getForcerBV()) {
            return false;
        }

        String montantMinimeNeg = "";
        String montantMinimePos = "";

        BSession sessionMusca = new BSession("MUSCA");
        getSession().connectSession(sessionMusca);

        montantMinimeNeg = sessionMusca.getApplication().getProperty(FAApplication.MONTANT_MINIMENEG);
        montantMinimePos = sessionMusca.getApplication().getProperty(FAApplication.MONTANT_MINIMEPOS);

        FWCurrency montantFacCur = new FWCurrency(montant);
        if ((montantFacCur.compareTo(new FWCurrency(montantMinimeNeg)) >= 0)
                && (montantFacCur.compareTo(new FWCurrency(montantMinimePos)) <= 0)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Vérifie le montant minime féfinit dans Musca
     * 
     * @author: sel Créé le : 20 déc. 06
     * @param section
     *            APISection
     * @param montant
     *            si montant = null, utilise <code>section.getSolde()</code>
     * @throws Exception
     */
    public void setBVR(APISection section, String montant) throws Exception {
        this.setBVR(section, montant, false);
    }

    /**
     * Vérifie le montant minime féfinit dans Musca
     * 
     * @author: sel Créé le : 20 déc. 06
     * @param section
     *            APISection
     * @param montant
     *            si montant = null, utilise <code>section.getSolde()</code>
     * @param isFactureMontantReport
     * @throws Exception
     */
    public void setBVR(APISection section, String montant, boolean isFactureMontantReport) throws Exception {
        JABVR bvr = null;
        String refBVR = this.genererNumReferenceBVR(section);

        montant = JANumberFormatter.deQuote(montant);

        if (JadeStringUtil.isDecimalEmpty(montant)) {
            montant = section.getSolde();
        }

        if (!isFactureAvecMontantMinime(getSession().getCurrentThreadTransaction(), montant) && !isFactureMontantReport) {
            if (new FWCurrency(montant).isPositive()) {
                bvr = new JABVR(montant, refBVR, getNoAdherent());

                setLigneReference(bvr.get_ligneReference());
                setOcrb(bvr.get_ocrb());
            }
        }
    }

    /**
     * @author: sel Créé le : 19 déc. 06
     * @param echeance
     * @throws Exception
     */
    public void setBVR(CAEcheancePlan echeance) throws Exception {
        JABVR bvr = null;

        String montantTMP = echeance.getMontant();
        Float montantF = Float.valueOf(montantTMP);
        CAPlanRecouvrement plan = echeance.getPlanRecouvrement();
        String idRole = plan.getCompteAnnexe().getIdRole();
        String idExterneRole = plan.getCompteAnnexe().getIdExterneRole();
        String idPlan = plan.getIdPlanRecouvrement();

        String refBVR = this.genererNumReference(idRole, idExterneRole, true, "", idPlan, plan.getIdCompteAnnexe());

        if (new FWCurrency(montantF.floatValue()).isPositive()) {
            bvr = new JABVR(JANumberFormatter.deQuote(montantF.toString()), refBVR, getNoAdherent());
            setLigneReference(bvr.get_ligneReference());
            setOcrb(bvr.get_ocrb());
        } else {
            throw new Exception(ERROR_MONTANT_NEGATIF);
        }
    }

    /**
     * Aucun contrôle du montant minime
     * 
     * @author: sel <br/>
     *          Créé le : 24.10.2008
     * @param section
     *            CASection
     * @param montant
     *            si montant = null, utilise <code>section.getSolde()</code>
     * @throws Exception
     */
    public void setBVR(CASection section, String montant) throws Exception {
        JABVR bvr = null;
        String refBVR = this.genererNumReferenceBVR(section);

        montant = JANumberFormatter.deQuote(montant);

        if (JadeStringUtil.isDecimalEmpty(montant)) {
            montant = section.getSolde();
        }

        if (new FWCurrency(montant).isPositive()) {
            bvr = new JABVR(montant, refBVR, getNoAdherent());

            setLigneReference(bvr.get_ligneReference());
            setOcrb(bvr.get_ocrb());
        }
    }

    /**
     * @author: sel Créé le : 20 déc. 06
     * @param entity
     * @param isFactureAvecMontantMinime
     * @throws Exception
     */
    public void setBVR(FAEnteteFacture entity, boolean isFactureAvecMontantMinime, boolean isFactureMontantReport)
            throws NumberFormatException, Exception {
        JABVR bvr = null;

        String refBVR = this.genererNumReferenceBVR(entity.getIdRole(), entity.getIdExterneRole(), false,
                JadeStringUtil.rightJustifyInteger(entity.getIdTypeFacture(), 2), entity.getIdExterneFacture());

        if (new FWCurrency(entity.getTotalFacture()).isPositive()
                && !entity.getIdModeRecouvrement().equals(FAEnteteFacture.CS_MODE_RECOUVREMENT_DIRECT)) {
            bvr = new JABVR(JANumberFormatter.deQuote(entity.getTotalFacture()), refBVR, getNoAdherent());
        }

        if (!(new FWCurrency(entity.getTotalFacture()).isZero() || isFactureAvecMontantMinime || isFactureMontantReport)
                && (bvr != null)) {
            if (!entity.getIdModeRecouvrement().equals(FAEnteteFacture.CS_MODE_RECOUVREMENT_DIRECT)) {
                setLigneReference(bvr.get_ligneReference());
                setOcrb(bvr.get_ocrb());
            } else {
                setLigneReference(CAReferenceBVR.REFERENCE_NON_FACTURABLE);
                setOcrb(CAReferenceBVR.OCRB_NON_FACTURABLE);
            }
        } else {
            setLigneReference(CAReferenceBVR.REFERENCE_NON_FACTURABLE);
            setOcrb(CAReferenceBVR.OCRB_NON_FACTURABLE);
        }
    }

    /**
     * Aucun contrôle du montant minime
     * 
     * @author: sel <br/>
     *          Créé le : 13.07.2010
     * @param section
     *            CASection
     * @throws Exception
     */
    public void setBVRNeutre(CASection section) throws Exception {
        JABVR bvr = null;
        String refBVR = this.genererNumReferenceBVR(section);

        bvr = new JABVR("0", refBVR, getNoAdherent());

        setLigneReference(bvr.get_ligneReference());
        setOcrb(bvr.get_ocrb());
    }

    /**
     * @author: MAR 25.05.2009 Numéro OCRB pour les bulletins neutres.
     * @param entity
     * @param isFactureAvecMontantMinime
     * @throws Exception
     */
    public void setBVRNeutre(FAEnteteFacture entity, boolean isFactureAvecMontantMinime, boolean isFactureMontantReport)
            throws NumberFormatException, Exception {
        JABVR bvr = null;

        String refBVR = this.genererNumReferenceBVR(entity.getIdRole(),
                removeNotLetterNotDigit(entity.getIdExterneRole()), false,
                JadeStringUtil.rightJustifyInteger(entity.getIdTypeFacture(), 2), entity.getIdExterneFacture());

        bvr = new JABVR(null, refBVR, getNoAdherent());

        setLigneReference(bvr.get_ligneReference());
        setOcrb(bvr.get_ocrb());
    }

    /**
     * @param forcerBV
     *            the forcerBV to set
     */
    public void setForcerBV(Boolean forcerBV) {
        this.forcerBV = forcerBV;
    }


    /**
     * @param ocrb
     *            the ocrb to set
     */
    public void setOcrb(String ocrb) {
        this.ocrb = ocrb;
    }


}
