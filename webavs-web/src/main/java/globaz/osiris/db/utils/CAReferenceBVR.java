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
    // Variables nécessaire pour récupérer le noAdhérent dans le catalogue de
    // texte
    public static final String IDENTIFIANT_REF_IDCOMPTEANNEXE = "99";
    private static final String ERROR_MONTANT_NEGATIF = "Error : Montant négatif !";
    private static int lengthIdRole = Integer.MIN_VALUE;
    private static int lengthRefDebiteur = Integer.MIN_VALUE;
    private static int lengthRefFacture = Integer.MIN_VALUE;
    private static int lengthTypeFacture = Integer.MIN_VALUE;
    private static final int MAX_LENGTH_NUM_AFFILIE = 13;
    private static final int MAX_LENGTH_REFERENCE = 26; // +1 du modulo de contrôle = les 27 positions

    public static final String OCRB_NON_FACTURABLE = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";

    private Boolean forcerBV = false;

    private String ocrb = CAReferenceBVR.OCRB_NON_FACTURABLE;
    private BSession session = null;

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
        return this.genererNumReferenceBVR(section.getCompteAnnexe().getIdRole(), section.getCompteAnnexe()
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
        return genererNumReferenceBVR(idRole, idExterneRole, isPlanPaiement, typeSection, idExterneSection, null);
    }

    /**
     * Permet de générer un numéro de référence pour un BVR.
     * 
     * @param idRole
     * @param idExterneRole
     * @param isPlanPaiement
     * @param typeSection
     * @param idExterneSection
     * @param idCompteAnnexe
     * @return
     * @throws Exception
     */
    private String genererNumReferenceBVR(String idRole, String idExterneRole, boolean isPlanPaiement,
            String typeSection, String idExterneSection, String idCompteAnnexe) throws Exception {
        // Valeur forcée dans les paramètres pour idRole
        int valIdRole = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                IntReferenceBVRParser.VAL_ID_ROLE));

        String idExterneRoleUnformatted = removeNotLetterNotDigit(idExterneRole);
        String role = JadeStringUtil.rightJustifyInteger(idRole, getLengthIdRole());
        String refDebiteur = JadeStringUtil.rightJustifyInteger(idExterneRoleUnformatted, getLengthRefDebiteur());
        String refFacture = JadeStringUtil.rightJustifyInteger(idExterneSection, getLengthRefFacture());
        String idTypeFacture = "";

        // idRole sur 2 positions
        if (role.length() > getLengthIdRole()) {
            role = role.substring(4, 6);
        }

        // Si l'id du rôle est forcé
        if (valIdRole != 0) {
            role = JadeStringUtil.rightJustifyInteger(String.valueOf(valIdRole), getLengthIdRole());
        }

        if (isPlanPaiement) {
            idTypeFacture = JadeStringUtil.rightJustifyInteger(
                    CAApplication.getApplicationOsiris().getProperty(IntReferenceBVRParser.IDENT_PLAN, ""),
                    getLengthTypeFacture());
        } else {
            idTypeFacture = JadeStringUtil.rightJustifyInteger(typeSection, getLengthTypeFacture());
        }

        // Si le numéro d'affilié non formatté comporte plus de 13 caractères, on utilise l'id du compte annexe pour le
        // numéro de référence. On utilise le numéro 99 à la place du rôle pour indiquer que le numéro de référence
        // utilise l'idCompteAnnexe au lieu du numéro d'affilié
        if (refDebiteur.length() > MAX_LENGTH_NUM_AFFILIE) {
            role = IDENTIFIANT_REF_IDCOMPTEANNEXE;
            if (idCompteAnnexe == null) {
                CACompteAnnexe compteAnnexe = findCompteAnnexeForIdRoleAndIdExterneRole(idRole, idExterneRole);
                refDebiteur = compteAnnexe.getIdCompteAnnexe();
            } else {
                refDebiteur = idCompteAnnexe;
            }
        }

        StringBuffer ref = new StringBuffer();
        ref.append(role);
        ref.append(JadeStringUtil.rightJustifyInteger(refDebiteur, getLengthRefDebiteur()));
        ref.append(idTypeFacture);
        ref.append(refFacture); // idPlan = reference facture

        if (ref.length() > CAReferenceBVR.MAX_LENGTH_REFERENCE) {
            throw new Exception(CAReferenceBVR.class.getName() + ": " + getSession().getLabel("ERREUR_REFERENCEBVR"));
        }

        return ref.toString();
    }

    /**
     * Retourne le compte annexe correspondant à l'idRole et l'idExterneRole
     * 
     * @param idRole
     * @param idExterneRole
     * @return
     * @throws Exception si aucun résultat
     */
    private CACompteAnnexe findCompteAnnexeForIdRoleAndIdExterneRole(String idRole, String idExterneRole)
            throws Exception {
        CACompteAnnexeManager compteAnnexeManager = new CACompteAnnexeManager();
        compteAnnexeManager.setSession(getSession());
        compteAnnexeManager.setForIdRole(idRole);
        compteAnnexeManager.setForIdExterneRole(idExterneRole);
        compteAnnexeManager.find();

        if (compteAnnexeManager.size() > 0) {
            return (CACompteAnnexe) compteAnnexeManager.getFirstEntity();
        } else {
            throw new Exception(CAReferenceBVR.class.getName() + ": unable to find compte annexe for idRole" + idRole
                    + " and idExterneRole" + idExterneRole);
        }
    }

    /**
     * @return the forcerBV
     */
    public Boolean getForcerBV() {
        return forcerBV;
    }

    /**
     * @author: sel Créé le : 18 déc. 06
     * @return la longueur de id role
     */
    private int getLengthIdRole() {
        if (CAReferenceBVR.lengthIdRole == Integer.MIN_VALUE) {
            CAReferenceBVR.lengthIdRole = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_ID_ROLE));
        }
        return CAReferenceBVR.lengthIdRole;
    }

    /**
     * @author: sel Créé le : 18 déc. 06
     * @return la longueur de l'id externe role
     */
    private int getLengthRefDebiteur() {
        if (CAReferenceBVR.lengthRefDebiteur == Integer.MIN_VALUE) {
            CAReferenceBVR.lengthRefDebiteur = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_ID_EXTERNE_ROLE));
        }
        return CAReferenceBVR.lengthRefDebiteur;
    }

    /**
     * @author: sel Créé le : 18 déc. 06
     * @return la longueur de l'id plan
     */
    private int getLengthRefFacture() {
        if (CAReferenceBVR.lengthRefFacture == Integer.MIN_VALUE) {
            CAReferenceBVR.lengthRefFacture = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_ID_PLAN));
        }
        return CAReferenceBVR.lengthRefFacture;
    }

    /**
     * @author: sel Créé le : 18 déc. 06
     * @return la longueur de type plan
     */
    private int getLengthTypeFacture() {
        if (CAReferenceBVR.lengthTypeFacture == Integer.MIN_VALUE) {
            CAReferenceBVR.lengthTypeFacture = Integer.parseInt(CAApplication.getApplicationOsiris().getProperty(
                    IntReferenceBVRParser.LEN_TYPE_PLAN));
        }
        return CAReferenceBVR.lengthTypeFacture;
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
     * @return the session
     */
    public BSession getSession() {
        return session;
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
     * Supprime tout caractère qui n'est pas une lettre ou un chiffre
     * 
     * @author: sel Créé le : 19 déc. 06
     * @param val
     * @return une chaine contenant que des lettres et des chiffres
     */
    private String removeNotLetterNotDigit(String val) {
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < val.length(); i++) {
            char c = val.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                strBuf.append(c);
            }
        }
        return strBuf.toString();
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

        String refBVR = this.genererNumReferenceBVR(idRole, idExterneRole, true, "", idPlan, plan.getIdCompteAnnexe());

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
