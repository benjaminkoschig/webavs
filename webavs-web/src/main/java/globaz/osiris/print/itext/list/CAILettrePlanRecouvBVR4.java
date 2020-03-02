package globaz.osiris.print.itext.list;

import ch.globaz.common.document.reference.ReferenceQR;
import ch.globaz.common.properties.CommonProperties;
import globaz.aquila.print.COParameter;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.application.CAParametres;
import globaz.osiris.db.access.recouvrement.CAEcheancePlan;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import ch.globaz.common.document.reference.ReferenceBVR;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.application.TIApplication;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * @author Alexandre Cuva, 13-mai-2005
 */
public class CAILettrePlanRecouvBVR4 extends CADocumentManager {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final String NUMERO_REFERENCE_INFOROM = "0043GCA";
    /** Le nom du modèle */
    private static final String TEMPLATE_NAME = "CAIEcheancierBVR4_QR";

    private ReferenceBVR bvr = null;
    private String centimes;
    private double cumulSolde = 0;
    private String dateRef = "";
    private boolean factureAvecMontantMinime = false;
    private int factureImpressionNo = 0;
    private String montantSansCentime;
    /** Données du formulaire */
    private CAPlanRecouvrement plan = new CAPlanRecouvrement();

    /**
     * Initialise le document
     * 
     * @param parent
     *            Le processus parent
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CAILettrePlanRecouvBVR4(BProcess parent) throws FWIException {
        super(parent, parent.getSession().getLabel("CABVRFILENAME"));
    }

    /**
     * Initialise le document
     * 
     * @param parent
     *            La session parente
     * @throws FWIException
     *             En cas de problème d'initialisaion
     */
    public CAILettrePlanRecouvBVR4(BSession parent) throws FWIException {
        super(parent, parent.getLabel("CABVRFILENAME"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.print.itext.list.CADocumentManager#beforeBuildReport()
     */
    @Override
    public void beforeBuildReport() throws FWIException {
        super.beforeBuildReport();

        if ((getPlanRecouvrement() != null) && (getPlanRecouvrement() instanceof CAPlanRecouvrement)) {
            if (!JadeStringUtil.isBlank((getPlanRecouvrement()).getCompteAnnexe().getId())) {
                String numAff = (getPlanRecouvrement()).getCompteAnnexe().getIdExterneRole();
                getDocumentInfo().setDocumentProperty("numero.affilie.formatte", numAff);
                try {
                    IFormatData affilieFormater = ((TIApplication) GlobazServer.getCurrentSystem().getApplication(
                            TIApplication.DEFAULT_APPLICATION_PYXIS)).getAffileFormater();
                    getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte",
                            affilieFormater.unformat(numAff));
                    TIDocumentInfoHelper.fill(getDocumentInfo(),
                            (getPlanRecouvrement()).getCompteAnnexe().getIdTiers(), getSession(), ITIRole.CS_AFFILIE,
                            numAff, affilieFormater.unformat(numAff));
                } catch (Exception e) {
                    getDocumentInfo().setDocumentProperty("numero.affilie.non.formatte", numAff);
                }
            }
        }
        getDocumentInfo().setPublishDocument(true);
        getDocumentInfo().setArchiveDocument(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.osiris.print.itext.list.CADocumentManager#beforeExecuteReport()
     */
    @Override
    public void beforeExecuteReport() throws FWIException {
        setNumeroReferenceInforom(CAILettrePlanRecouvBVR4.NUMERO_REFERENCE_INFOROM);
    }

    /**
     * @see globaz.framework.printing.itext.api.FWIDocumentInterface#createDataSource()
     */
    @Override
    public void createDataSource() throws Exception {
        String compteCADesc = "";
        // Récupération des données
        echeance = (CAEcheancePlan) currentEntity();
        // Sette la langue selon le tier.
        _setLangueFromTiers(getPlanRecouvrement().getCompteAnnexe().getTiers());

        // this.setNumeroReferenceInforom(CAILettrePlanRecouvBVR4.NUMERO_REFERENCE_INFOROM);

        if (!JadeStringUtil.isBlank(echeance.getId()) && !JadeStringUtil.isBlank(getPlanRecouvrement().getId())
                && !JadeStringUtil.isBlank(getPlanRecouvrement().getCompteAnnexe().getId())) {
            ++factureImpressionNo;
            if (!JadeStringUtil.isBlank(getPlanRecouvrement().getCompteAnnexe().getTiers().getId())) {
                compteCADesc = getPlanRecouvrement().getCompteAnnexe().getIdExterneRole() + " - "
                        + getPlanRecouvrement().getCompteAnnexe().getTiers().getNom();
            } else {
                compteCADesc = getPlanRecouvrement().getCompteAnnexe().getDescription();
            }
            // Gestion du modèle et du titre
            setTemplateFile(CAILettrePlanRecouvBVR4.TEMPLATE_NAME);
            setDocumentTitle(getSession().getLabel("OSIRIS_LETTRE_PLAN_RECOUV_BVR") + " " + compteCADesc);
            // Gestion de l'en-tête/pied de page/signature
            this._handleHeaders(getPlanRecouvrement(), true, false, false, getDateRef());
            setMontantMinime(echeance);
            initMontant(echeance);
            // Renseigne les paramètres du document

            if (CommonProperties.QR_FACTURE.getBooleanValue()) {
                // -- QR
                qrFacture = new ReferenceQR();
                qrFacture.setSession(getSession());
                // Initialisation des variables du document
                initVariableQR(new FWCurrency(echeance.getMontant()), getPlanRecouvrement().getCompteAnnexe().getIdTiers());

                // Génération du document QR
                qrFacture.initQR(this);
            } else {
                fillBVR();
            }

            setColumnHeader(1, _getProperty(CADocumentManager.JASP_PROP_BODY_CACLIBELLE, ""));
            setColumnHeader(6, _getProperty(CADocumentManager.JASP_PROP_BODY_CACMONTANT, ""));
            String textMontant = FWMessageFormat.format(_getProperty(CADocumentManager.JASP_PROP_BODY_CACREPORT, ""),
                    JACalendar.format(echeance.getDateExigibilite(), _getLangue()));
            if (isFactureAvecMontantMinime()) {
                textMontant = _getProperty(CADocumentManager.JASP_PROP_BODY_CACTEXT_MINIMEPOS, "");
            }
            if (isMontantZero()) {
                textMontant = "";
            }
            this.setParametres(CAILettrePlanRecouvParam.P_TEXT, textMontant);
            String numImpression = _getProperty(CADocumentManager.JASP_PROP_BODY_LABEL_CACPAGE, "") + " ";
            numImpression += getPlanRecouvrement().getIdPlanRecouvrement();
            // description du décompte
            this.setParametres(CAILettrePlanRecouvParam.getParamP(8), numImpression);
            // Renseigne les lignes dans le tableau du document
            ArrayList liste = new ArrayList();
            liste.add(newMap(CADocumentManager.JASP_PROP_BODY_COL_LIBELLE_CACPAIEMENT, new Double(new FWCurrency(
                    echeance.getMontant()).doubleValue())));
            this.setDataSource(liste);
            this.setParametres(CAILettrePlanRecouvParam.P_TOTAL, new Double(echeance.getMontant()));
        }
    }

    /**
     * Method _fillBVR.
     */
    private void fillBVR() {
        // commencer à écrire les paramètres
        String adresseDebiteur = "";
        try {
            // prendre l'adresse de courier de la caisse
            // rechercher l'adresse de paiement
            adresseDebiteur = getPlanRecouvrement().getAdressePaiementTiers();
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }

        if (!isFactureAvecMontantMinime()) {
            this.setParametres(CAILettrePlanRecouvParam.P_FRANC, montantSansCentime);
            this.setParametres(CAILettrePlanRecouvParam.P_CENTIME, centimes);
        } else {
            this.setParametres(CAILettrePlanRecouvParam.P_FRANC, "");
            this.setParametres(CAILettrePlanRecouvParam.P_CENTIME, "");
        }
        try {
            getBvr().setSession(getSession());
            getBvr().setBVR(echeance);

            // Modification suite à QR-Facture. Choix du footer
            super.setParametres(COParameter.P_SUBREPORT_QR, getImporter().getImportPath() + "BVR_TEMPLATE.jasper");

            this.setParametres(CAILettrePlanRecouvParam.P_ADRESSE, getBvr().getAdresse());
            this.setParametres(CAILettrePlanRecouvParam.P_ADRESSECOPY, getBvr().getAdresse());
            this.setParametres(CAILettrePlanRecouvParam.P_COMPTE, getBvr().getNumeroCC());

            this.setParametres(CAILettrePlanRecouvParam.P_VERSE, getBvr().getLigneReference() + "\n" + adresseDebiteur);
            this.setParametres(CAILettrePlanRecouvParam.P_PAR, adresseDebiteur);
            this.setParametres(FWIImportParametre.PARAM_REFERENCE, bvr.getLigneReference());
            this.setParametres(CAILettrePlanRecouvParam.P_OCR, getBvr().getOcrb());
        } catch (Exception e) {
            JadeLogger.error(this, e);
        }
    }

    /**
     * Renvoie la référence BVR.
     * 
     * @return la référence BVR.
     */
    public ReferenceBVR getBvr() {
        if (bvr == null) {
            bvr = new ReferenceBVR();
        }
        return bvr;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public double getCumulSolde() {
        return cumulSolde;
    }

    /**
     * @return the dateRef
     */
    public String getDateRef() {
        return dateRef;
    }

    /**
     * @return La valeur courante de la propriété
     */
    public int getFactureImpressionNo() {
        return factureImpressionNo;
    }

    /**
     * @param echeance
     * @return
     */
    public String getMontantFormatted(CAEcheancePlan echeance) {
        FWCurrency montant = new FWCurrency(echeance.getMontant());
        return JANumberFormatter.fmt(JANumberFormatter.deQuote(montant.toStringFormat()), true, true, false, 2);
    }

    /**
     * @return La valeur courante de la propriété
     */
    public CAPlanRecouvrement getPlanRecouvrement() {
        return plan;
    }

    /**
     * @param textString
     * @param delim
     * @return
     */
    public final String getTextWithoutDelimiter(String textString, String delim) {
        StringTokenizer st = new java.util.StringTokenizer(textString, delim);
        StringBuffer sb = new StringBuffer();
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
        }
        return sb.toString();
    }

    /**
     * @param echeance
     */
    public void initMontant(CAEcheancePlan echeance) {
        String montantFacture = getMontantFormatted(echeance);
        montantFacture = JANumberFormatter.deQuote(montantFacture);
        // convertir le montant en entier (BigInteger)
        montantSansCentime = JAUtil.createBigDecimal(montantFacture).toBigInteger().toString();
        java.math.BigDecimal montantSansCentimeBig = JAUtil.createBigDecimal(montantSansCentime);
        // convertir le montant avec centimes en BigDecimal
        java.math.BigDecimal montantAvecCentimeBig = JAUtil.createBigDecimal(montantFacture);
        // les centimes représentés en entier
        try {
            centimes = montantAvecCentimeBig.subtract(montantSansCentimeBig).toString().substring(2, 4);
        } catch (Exception e) {
            JadeLogger.error(this, e);
            centimes = "";
        }
    }

    /**
     * Returns the factureAvecMontantMinime.
     * 
     * @return boolean
     */
    public boolean isFactureAvecMontantMinime() {
        return factureAvecMontantMinime;
    }

    /**
     * @return
     */
    private boolean isMontantZero() {
        if (new BigDecimal(montantSansCentime).compareTo(new BigDecimal("0")) == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param libelleCourantProperty
     * @param valeurCourante
     * @return
     */
    private HashMap newMap(String libelleCourantProperty, Double valeurCourante) {
        HashMap map = new HashMap();
        // Libelle courant
        map.put(FWIImportParametre.getCol(1),
                _getProperty(libelleCourantProperty, String.valueOf(getFactureImpressionNo())));
        // Valeur courante
        map.put(FWIImportParametre.getCol(6), valeurCourante);

        return map;
    }

    /**
     * @param d
     *            La nouvelle valeur de la propriété
     */
    public void setCumulSolde(double d) {
        cumulSolde = d;
    }

    /**
     * @param dateRef
     *            the dateRef to set
     */
    public void setDateRef(String dateRef) {
        this.dateRef = dateRef;
    }

    /**
     * Sets the factureAvecMontantMinime.
     * 
     * @param factureAvecMontantMinime
     *            The factureAvecMontantMinime to set
     */
    public void setFactureAvecMontantMinime(boolean factureAvecMontantMinime) {
        this.factureAvecMontantMinime = factureAvecMontantMinime;
    }

    /**
     * @param langueDoc
     *            La nouvelle valeur de la propriété
     */
    public void setLangueDocument(String langueDoc) {
        _setLangueDocument(langueDoc);
    }

    /**
     * @author: sel Créé le : 18 janv. 07
     * @param echeance
     */
    public void setMontantMinime(CAEcheancePlan echeance) {
        FWCurrency montantFacCur = new FWCurrency(echeance.getMontant());
        if (montantFacCur.compareTo(new FWCurrency(CAParametres.getMontantMinime(getTransaction()))) < 0) {
            setFactureAvecMontantMinime(true);
        } else {
            setFactureAvecMontantMinime(false);
        }
    }

    /**
     * @param planRecouvrement
     *            La nouvelle valeur de la propriété
     */
    public void setPlanRecouvrement(CAPlanRecouvrement planRecouvrement) {
        plan = planRecouvrement;
    }
}
