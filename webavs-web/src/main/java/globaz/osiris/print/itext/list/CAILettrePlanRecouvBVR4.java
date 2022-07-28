package globaz.osiris.print.itext.list;

import ch.globaz.common.document.reference.ReferenceQR;
import ch.globaz.common.properties.CommonProperties;
import globaz.aquila.print.COParameter;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.printing.itext.fill.FWIImportParametre;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.musca.api.musca.PaireIdEcheanceParDateExigibilite;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.osiris.application.CAApplication;
import globaz.osiris.application.CAParametres;
import globaz.osiris.db.access.recouvrement.CACouvertureSection;
import globaz.osiris.db.access.recouvrement.CACouvertureSectionManager;
import globaz.osiris.db.access.recouvrement.CAEcheancePlan;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import ch.globaz.common.document.reference.ReferenceBVR;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.process.ebill.EBillHelper;
import globaz.osiris.process.ebill.EBillSftpProcessor;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.application.TIApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    private static final int NUMBER_MAX_OF_ECHEANCE=100;

    private ReferenceBVR bvr = null;
    private String centimes;
    private double cumulSolde = 0;
    private String dateRef = "";
    private boolean factureAvecMontantMinime = false;
    private String montantSansCentime;
    private JadePublishDocument decisionFusionee;

    /* eBill fields */
    public Map<PaireIdEcheanceParDateExigibilite, List<Map>> lignesSursis = new LinkedHashMap();
    public Map<PaireIdEcheanceParDateExigibilite, String> referencesSursis = new LinkedHashMap();
    private static final Logger LOGGER = LoggerFactory.getLogger(CAILettrePlanRecouvBVR4.class);
    private EBillHelper eBillHelper = new EBillHelper();
    private int factureEBill = 0;

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
        computeTotalPage();
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

    @Override
    public String getJasperTemplate() {
        return TEMPLATE_NAME;
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
                initVariableQR(_getLangue(), new FWCurrency(echeance.getMontant()), getPlanRecouvrement().getCompteAnnexe().getIdTiers());

                // Génération du document QR
                qrFacture.initQR(this, qrFactures);
                referencesSursis.put(new PaireIdEcheanceParDateExigibilite(echeance.getIdEcheancePlan(), echeance.getDateExigibilite()), qrFacture.getReference());
            } else {
                fillBVR();
                referencesSursis.put(new PaireIdEcheanceParDateExigibilite(echeance.getIdEcheancePlan(), echeance.getDateExigibilite()), getBvr().getRefNoSpace());
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
            ArrayList lignes = new ArrayList(); // Lignes factures
            lignes.add(newMap(CADocumentManager.JASP_PROP_BODY_COL_LIBELLE_CACPAIEMENT, new Double(new FWCurrency(
                    echeance.getMontant()).doubleValue())));

            // Prepare la map des lignes de sursis au paiement eBill si propriété eBillOsiris est active et si compte annexe de la facture inscrit à eBill et si eBillPrintable est sélectioné sur le plan
            boolean eBillOsirisActif = CAApplication.getApplicationOsiris().getCAParametres().isEBillOsirisActifEtDansListeCaisses(getSession());
            if (eBillOsirisActif && plan.getEBillPrintable()) {
                if (compteAnnexe != null && !JadeStringUtil.isBlankOrZero(compteAnnexe.getEBillAccountID())) {
                    lignesSursis.put(new PaireIdEcheanceParDateExigibilite(echeance.getIdEcheancePlan(), echeance.getDateExigibilite()), lignes); // EBILL Sursis au paiement - BVR (0043GCA)
                }
            }

            this.setDataSource(lignes);
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

    @Override
    public void afterExecuteReport() {
        if (checkNumberEcheance()) {
            // Effectue le traitement eBill pour les documents concernés et les envoient sur le ftp
            boolean eBillOsirisActif = CAApplication.getApplicationOsiris().getCAParametres().isEBillOsirisActifEtDansListeCaisses(getSession());

            // On imprime eBill si :
            //  - eBillOsiris est actif
            //  - le compte annexe possède un eBillAccountID
            //  - eBillPrintable est sélectioné sur le plan
            if (eBillOsirisActif && plan.getEBillPrintable()) {
                if (getPlanRecouvrement().getCompteAnnexe() != null && !JadeStringUtil.isBlankOrZero(getPlanRecouvrement().getCompteAnnexe().getEBillAccountID())) {
                    try {
                        EBillSftpProcessor.getInstance();
                        traiterSursisEBillOsiris(this);
                        ajouteInfoEBillToEmail();
                    } catch (Exception exception) {
                        LOGGER.error("Impossible de créer les fichiers eBill : " + exception.getMessage(), exception);
                        getMemoryLog().logMessage(getSession().getLabel("BODEMAIL_EBILL_FAILED") + exception.getCause().getMessage(), FWMessage.ERREUR, this.getClass().getName());
                    } finally {
                        EBillSftpProcessor.closeServiceFtp();
                    }
                }
            }
        }else{
            getMemoryLog().logMessage(getSession().getLabel("BODEMAIL_EBILL_ECHEANCE") + getLignesSursis().size(), FWMessage.ERREUR, this.getClass().getName());
            getMemoryLog().logMessage(getSession().getLabel("OBJEMAIL_EBILL_FAELEC") + factureEBill, FWMessage.ERREUR, this.getClass().getName());
            getDocumentInfo().setDocumentNotes(getDocumentInfo().getDocumentNotes() + getMemoryLog().getMessagesInString());
        }
    }
    private boolean checkNumberEcheance(){
        return this.getLignesSursis().size()<NUMBER_MAX_OF_ECHEANCE;
    }

    /**
     * Méthode permettant de traiter les sursis eBill
     * en attente d'être envoyé dans le processus actuel.
     */
    private void traiterSursisEBillOsiris(CAILettrePlanRecouvBVR4 documentBVR) throws Exception {

        List<CASection> sectionsCouvertes = getSectionsCouvertes(documentBVR);
        if (!sectionsCouvertes.isEmpty()) {

            CASection sectionCouvertes = sectionsCouvertes.get(0); // TODO ESVE EBILL UTILISER TOUTES LES SECTIONS COUVERTES
            FAEnteteFacture entete = eBillHelper.generateEnteteFacture(sectionCouvertes, getSession());
            String titreSursis = String.valueOf(documentBVR.getImporter().getParametre().get("P_8"));
            String reference = documentBVR.getReferencesSursis().entrySet().stream().findFirst().get().getValue();
            List<JadePublishDocument> attachedDocuments = eBillHelper.findAndReturnAttachedDocuments(getAttachedDocuments(), CAILettrePlanRecouvBVR4.class.getSimpleName());
            ajouteDecisionFusionee(attachedDocuments);

            if (!attachedDocuments.isEmpty()) {
                creerFichierEBillOsiris(documentBVR.getPlanRecouvrement(), entete, getCumulSoldeFormatee(documentBVR.getCumulSolde()), documentBVR.getLignesSursis(), reference, attachedDocuments, getDateFacturationFromSection(sectionCouvertes), sectionCouvertes, titreSursis);
            }
        }
    }

    private void ajouteDecisionFusionee(List<JadePublishDocument> attachedDocuments) {
        if (decisionFusionee!= null) {
            attachedDocuments.add(decisionFusionee);
        }
    }

    private void ajouteInfoEBillToEmail() {
        getMemoryLog().logMessage(getSession().getLabel("OBJEMAIL_EBILL_FAELEC") + factureEBill, FWMessage.INFORMATION, this.getClass().getName());
        getDocumentInfo().setDocumentNotes(getDocumentInfo().getDocumentNotes() + getMemoryLog().getMessagesInString());
    }

    private List<CASection> getSectionsCouvertes(CAILettrePlanRecouvBVR4 documentBVR) throws Exception {
        // les couvertures
        CACouvertureSectionManager couvertures = documentBVR.getPlanRecouvrement().fetchSectionsCouvertes();

        // les sections couvertes
        ArrayList<CASection> sections = new ArrayList<>();
        for (int i = 0; i < couvertures.size(); i++) {
            CACouvertureSection couverture = (CACouvertureSection) couvertures.getEntity(i);
            CASection tmpSection = new CASection();
            tmpSection.setSession(getSession());
            tmpSection.setIdSection(couverture.getIdSection());
            tmpSection.retrieve();
            // à condition que la section soit pas soldée
            if (Float.parseFloat(tmpSection.getSolde()) != 0f) {
                sections.add(tmpSection);
            }
        }

        return sections;
    }

    private String getCumulSoldeFormatee(double cumulSolde) {
        return JANumberFormatter.fmt(JANumberFormatter.deQuote(new FWCurrency(cumulSolde).toStringFormat()), false, true, false, 2);
    }

    private String getDateFacturationFromSection(CASection section) throws Exception {
        JACalendarGregorian calendar = new JACalendarGregorian();
        JADate dateFacturation = JACalendar.today();
        JADate dateEcheanceSection = new JADate(section.getDateEcheance());
        if (calendar.compare(dateFacturation, dateEcheanceSection) == JACalendar.COMPARE_FIRSTUPPER) {
             return dateFacturation.toStr(".");
        } else {
             return dateEcheanceSection.toStr(".");
        }
    }

    /**
     * Méthode permettant de créer le sursis au paiement eBill,
     * de générer et remplir le fichier puis de l'envoyer sur le ftp.
     *
     * @param planRecouvrement        : le plan de recouvrement
     * @param entete                  : l'entête de la facture
     * @param montantFacture          : contient le montant total de la factures (seulement rempli dans le cas d'un bulletin de soldes ou d'un sursis au paiement)
     * @param lignesSursis            : contient les lignes de sursis au paiement
     * @param reference               : la référence BVR ou QR.
     * @param attachedDocuments       : la liste des fichiers crée par l'impression classique à joindre en base64 dans le fichier eBill
     * @param dateFacturation         : la date de facturation
     * @param section                 : la section
     * @param titreSursis             : le titre de LineItem pour les sursis au paiement
     * @throws Exception
     */
    private void creerFichierEBillOsiris(CAPlanRecouvrement planRecouvrement, FAEnteteFacture entete, String montantFacture, Map<PaireIdEcheanceParDateExigibilite, List<Map>> lignesSursis, String reference, List<JadePublishDocument> attachedDocuments, String dateFacturation, CASection section, String titreSursis) throws Exception {

        // Génère et ajoute un eBillTransactionId dans l'entête de facture eBill
        entete.addEBillTransactionID(getTransaction());

        // Met à jour le flag eBillPrinted dans l'entête de facture eBill
        entete.setEBillPrinted(true);

        // Met à jour le status eBill de la section
        eBillHelper.updateSectionEtatEtTransactionID(section, entete.getEBillTransactionID(), getMemoryLog());

        String dateEcheance = planRecouvrement.getDateEcheance();
        String dateOctroi = planRecouvrement.getDate();
        eBillHelper.creerFichierEBill(planRecouvrement.getCompteAnnexe(), entete, null, montantFacture, null, lignesSursis, reference, attachedDocuments, dateFacturation, dateEcheance, dateOctroi, getSession(), titreSursis);

        factureEBill++;
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

    public Map<PaireIdEcheanceParDateExigibilite, List<Map>> getLignesSursis() {
        return lignesSursis;
    }

    public void setLignesSursis(Map<PaireIdEcheanceParDateExigibilite, List<Map>> lignesSursis) {
        this.lignesSursis = lignesSursis;
    }

    public Map<PaireIdEcheanceParDateExigibilite, String> getReferencesSursis() {
        return referencesSursis;
    }

    public void setReferencesSursis(Map<PaireIdEcheanceParDateExigibilite, String> referencesSursis) {
        this.referencesSursis = referencesSursis;
    }

    public JadePublishDocument getDecisionFusionee() {
        return decisionFusionee;
    }

    public void setDecisionFusionee(JadePublishDocument decisionFusionee) {
        this.decisionFusionee = decisionFusionee;
    }
}
