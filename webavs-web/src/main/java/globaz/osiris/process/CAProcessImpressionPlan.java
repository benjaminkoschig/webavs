package globaz.osiris.process;

import globaz.docinfo.TIDocumentInfoHelper;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.printing.itext.exception.FWIException;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMessage;
import globaz.globall.db.*;
import globaz.globall.format.IFormatData;
import globaz.globall.util.*;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.musca.api.musca.PaireIdEcheanceIdPlanRecouvrementEBill;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.osiris.process.ebill.EBillFichier;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.access.recouvrement.CAPlanRecouvrement;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.ebill.enums.CATraitementEtatEBillEnum;
import globaz.osiris.print.itext.list.CAILettrePlanRecouvBVR4;
import globaz.osiris.print.itext.list.CAILettrePlanRecouvDecision;
import globaz.osiris.print.itext.list.CAILettrePlanRecouvEcheancier;
import globaz.osiris.process.ebill.EBillSftpProcessor;
import globaz.osiris.utils.CASursisPaiement;
import globaz.pyxis.api.ITIRole;
import globaz.pyxis.application.TIApplication;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.globaz.common.properties.PropertiesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kurkus, 27 mai 05
 * @author sel, 18 juin 2007
 */
public class CAProcessImpressionPlan extends BProcess {

    private static final long serialVersionUID = -7417065395365529318L;

    private static final String PROPERTIES_LIGNE_TECH = "common.use.ligneTechnique";

    private String dateRef = "";

    private EBillSftpProcessor serviceFtp;
    private static final Logger LOGGER = LoggerFactory.getLogger(CAProcessImpressionPlan.class);
    private CAILettrePlanRecouvDecision document = null;
    private String idDocument = "";
    private String idPlanRecouvrement = "";
    private Boolean impAvecBVR = new Boolean(false);
    private String modele = "";
    private String observation = "";

    /**
     * Constructor for CAProcessImpressionPlan.
     */
    public CAProcessImpressionPlan() throws Exception {
        this(new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS));
    }

    /**
     * Constructor for CAProcessImpressionPlan.
     * 
     * @param parent
     */
    public CAProcessImpressionPlan(BProcess parent) throws Exception {
        super(parent);
    }

    /**
     * Constructor for CAProcessImpressionPlan.
     * 
     * @param session
     */
    public CAProcessImpressionPlan(BSession session) throws Exception {
        super(session);
    }

    @Override
    protected void _executeCleanUp() {
    }

    @Override
    protected boolean _executeProcess() {
        try {
            // Création des documents
            document = createDecision();
            // CAILettrePlanRecouvVoiesDroit documentVD = this.createVoiesDroit();
            CAPlanRecouvrement plan = (CAPlanRecouvrement) document.currentEntity();
            CAILettrePlanRecouvEcheancier documentE = CASursisPaiement.createEcheancier(this, getTransaction(), plan);
            // Fusionne les documents ci-dessus (Décision, voies de droit et échéancier)
            fusionneDocuments(plan);

            List echeances = (ArrayList) documentE.currentEntity();
            if (!JadeStringUtil.isBlank(documentE.getPlanRecouvrement().getId()) && getImpAvecBVR().booleanValue()
                    && (echeances != null)) {

                createBVR(plan, echeances, documentE);
            }

            // Tester si abort
            if (isAborted()) {
                return false;
            }
        } catch (Exception e) {
            JadeLogger.error(this, e);
            this._addError(getSession().getLabel("OSIRIS_IMPRESSION_LETTRE_DECISION") + " : " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    protected void _validate() throws Exception {
    }

    /**
     * Prépare et retourne le document "BVR"
     * 
     * @author: sel Créé le : 16 nov. 06
     * @param plan
     * @param echeances
     * @return le document "BVR"
     * @throws FWIException
     * @throws Exception
     */
    private CAILettrePlanRecouvBVR4 createBVR(CAPlanRecouvrement plan, List echeances,
            CAILettrePlanRecouvEcheancier echeancier) throws FWIException, Exception {
        // Instancie le document : BVR
        CAILettrePlanRecouvBVR4 documentBVR = new CAILettrePlanRecouvBVR4(this);
        documentBVR.setSession(getSession());
        documentBVR.setDateRef(getDateRef());
        documentBVR.addAllEntities(echeances);
        documentBVR.setPlanRecouvrement(plan);
        documentBVR.setCumulSolde(echeancier.getCumulSolde());
        documentBVR.setImpressionParLot(true);
        documentBVR.setTailleLot(500);

        // Demander le traitement du document
        documentBVR.setEMailAddress(getEMailAddress());
        documentBVR.executeProcess();

        // Effectue le traitement eBill pour les documents concernés et les envoient sur le ftp
        boolean eBillActif = CAApplication.getApplicationOsiris().getCAParametres().iseBillActifEtDansListeCaisses(getSession());
        boolean eBillOsirisActif = CAApplication.getApplicationOsiris().getCAParametres().iseBillOsirisActif();

        // On imprime les factures eBill si :
        //  - eBill est actif
        //  - eBillOsiris est actif
        //  - eBillPrintable est sélectioné sur le plan
        if (eBillActif && eBillOsirisActif && plan.geteBillPrintable()) {
            try {
                initServiceFtp();
                traiterSursisEBillOsiris(documentBVR);
            } catch (Exception exception) {
                LOGGER.error("Impossible de créer les fichiers eBill : " + exception.getMessage(), exception);
                getMemoryLog().logMessage(getSession().getLabel("BODEMAIL_EBILL_FAILED") + exception.getCause().getMessage(), FWMessage.ERREUR, this.getClass().getName());
            } finally {
                closeServiceFtp();
            }
        }

        return documentBVR;
    }

    /**
     * Méthode permettant de traiter les sursis eBill
     * en attente d'être envoyé dans le processus actuel.
     */
    private void traiterSursisEBillOsiris(CAILettrePlanRecouvBVR4 documentBVR) throws Exception {

        for (Map.Entry<PaireIdEcheanceIdPlanRecouvrementEBill, List<Map>> lignes : documentBVR.getLignesSursis().entrySet()) {

            CACompteAnnexe compteAnnexe = documentBVR.getPlanRecouvrement().getCompteAnnexe();
            if (compteAnnexe != null && !JadeStringUtil.isBlankOrZero(compteAnnexe.geteBillAccountID())) {

                // Init spécifique aux Sursis au paiement
                CASection section = new CASection();
                section.setSession(getSession());
                section.setIdSection(documentBVR.getPlanRecouvrement().getIdSection());
                section.retrieve();

                FAEnteteFacture entete = new FAEnteteFacture();
                entete.setSession(getSession());
                entete.setIdModeRecouvrement(CodeSystem.MODE_RECOUV_AUTOMATIQUE);
                entete.setIdTiers(section.getCompteAnnexe().getIdTiers());
                entete.setIdTypeCourrier(section.getTypeAdresse());
                entete.setIdDomaineCourrier(section.getDomaine());
                entete.setIdExterneRole(section.getCompteAnnexe().getIdExterneRole());
                entete.setIdExterneFacture(section.getIdExterne());

                String reference = documentBVR.getReferencesSursis().get(lignes.getKey());
                JadePublishDocument attachedDocument = findAndReturnAttachedDocument();
                creerFichierEBillOsiris(compteAnnexe, entete, null, getCumulSoldeFormatee(documentBVR.getCumulSolde()), lignes.getValue(), reference, attachedDocument, getDateFacturationFromSection(section), section);
            }
        }
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

    private JadePublishDocument findAndReturnAttachedDocument() {
        List<JadePublishDocument> jadePublishDocuments = getAttachedDocuments();
        for(JadePublishDocument jadePublishDocument : jadePublishDocuments) {
            if (jadePublishDocument.getPublishJobDefinition().getDocumentInfo().getDocumentType().equals(CAILettrePlanRecouvBVR4.class.getSimpleName())) {
                return jadePublishDocument;
            }
        }
        return null;
    }

    private String getCumulSoldeFormatee(double cumulSolde) {
        FWCurrency montant = new FWCurrency(cumulSolde);
        return JANumberFormatter.fmt(JANumberFormatter.deQuote(montant.toStringFormat()), false, true, false, 2);
    }

    /**
     * Méthode permettant de créer le sursis au paiement eBill,
     * de générer et remplir le fichier puis de l'envoyer sur le ftp.
     *
     * @param compteAnnexe            : le compte annexe
     * @param entete                  : l'entête de la facture
     * @param enteteReference         : l'entête de référence pour les bulletin de soldes (vide dans le cas d'une facture bvr)
     * @param montantSursis           : contient le montant total du sursis au paiement (seulement rempli dans le case d'un sursis au paiement)
     * @param lignes                  : contient les lignes de sursis au paiement
     * @param reference               : la référence BVR ou QR.
     * @param attachedDocument        : le fichier crée par l'impression classique à joindre en base64 dans le fichier eBill
     * @param dateFacturation         : la date de facturation
     * @param section                 : la section
     * @throws Exception
     */
    private void creerFichierEBillOsiris(CACompteAnnexe compteAnnexe, FAEnteteFacture entete, FAEnteteFacture enteteReference, String montantSursis, List<Map> lignes, String reference, JadePublishDocument attachedDocument, String dateFacturation, CASection section) throws Exception {

        String billerId = CAApplication.getApplicationOsiris().getCAParametres().geteBillBillerId();

        // Génère et ajoute un eBillTransactionId dans l'entête de facture eBill
        entete.addeBillTransactionID(getTransaction());

        // Met à jour le flag eBillPrinted dans l'entête de facture eBill
        entete.seteBillPrinted(true);

        // met à jour le status eBill de la section
        updateSectionEtatEtTransactionID(section, entete.geteBillTransactionID());

        String dateEcheance = dateFacturation;
        EBillFichier.creerFichierEBill(compteAnnexe, entete, enteteReference, null, montantSursis, lignes, reference, attachedDocument, dateFacturation, dateEcheance, billerId, getSession(), serviceFtp);
    }

    /**
     * Mise à jour du statut eBill de la section et de son transactionID eBill
     * passe l'état à pending.
     *
     * @param section  la section à mettre à jour.
     * @param transactionId l'id de transaction lié au traitement.
     */
    public void updateSectionEtatEtTransactionID(final CASection section, final String transactionId) {
        try {
            section.seteBillEtat(CATraitementEtatEBillEnum.NUMERO_ETAT_REJECTED_OR_PENDING);
            section.seteBillErreur("");
            section.seteBillTransactionID(transactionId);
            section.update();
        } catch (Exception e) {
            getMemoryLog().logMessage("Impossible de mettre à jour la section avec l'id : " + section.getIdSection() + " : " + e.getMessage(), FWViewBeanInterface.WARNING, this.getClass().getName());
        }
    }

    /**
     * Fermeture du service ftp.
     */
    private void closeServiceFtp() {
        if (serviceFtp != null) {
            serviceFtp.disconnectQuietly();
        }
    }

    /**
     * Initialisation du service ftp.
     */
    private void initServiceFtp() throws PropertiesException {
        if (serviceFtp == null) {
            serviceFtp = new EBillSftpProcessor();
        }
    }

    /**
     * Prépare et retourne le document "Décision"
     * 
     * @author: sel Créé le : 16 nov. 06
     * @return le document "Décision"
     * @throws FWIException
     * @throws Exception
     */
    private CAILettrePlanRecouvDecision createDecision() throws FWIException, Exception {
        // Instancie le document du plan de recouvrement : Décision
        CAILettrePlanRecouvDecision document = new CAILettrePlanRecouvDecision(this);
        document.setSession(getSession());
        document.setDateRef(getDateRef());
        document.setIdDocument(getIdDocument());
        document.setJoindreBVR(getImpAvecBVR());
        document.setObservation(getObservation());
        // Demander le traitement du document
        document.setEMailAddress(getEMailAddress());

        if (JadeStringUtil.isBlank(getIdPlanRecouvrement())) {
            super._addError(getSession().getLabel("OSIRIS_ERR_PLAN_ID_IS_NULL"));
            throw new Exception(getSession().getLabel("OSIRIS_ERR_PLAN_ID_IS_NULL"));
        }
        document.setIdPlanRecouvrement(getIdPlanRecouvrement());
        document.executeProcess();

        if (document.getDocumentList().size() <= 0) {
            throw new Exception(this.getClass().getName() + "._executeProcess() : Error, document "
                    + document.getImporter().getDocumentTemplate() + " can not be created !");
        }

        return document;
    }

    /**
     * Fusionne les documents (Décision, voies de droit et échéancier). <br>
     * Envoie un e-mail avec les pdf fusionnés. <br>
     * 
     * @author: sel Créé le : 16 nov. 06
     * @throws Exception
     */
    private void fusionneDocuments(CAPlanRecouvrement plan) throws Exception {
        // Fusionne les documents (Décision, voies de droit et échéancier)
        // Les documents fusionnés sont effacés

        // GED
        JadePublishDocumentInfo info = null;
        if (getAttachedDocuments().size() > 0) {

            JadePublishDocument doc = (JadePublishDocument) getAttachedDocuments().get(0);
            info = doc.getPublishJobDefinition().getDocumentInfo().createCopy();

        } else {
            info = super.createDocumentInfo();
            info.setDocumentTypeNumber(CAILettrePlanRecouvDecision.NUMERO_REFERENCE_INFOROM);
            IFormatData affilieFormater = ((TIApplication) GlobazServer.getCurrentSystem().getApplication(
                    TIApplication.DEFAULT_APPLICATION_PYXIS)).getAffileFormater();
            TIDocumentInfoHelper.fill(info, plan.getCompteAnnexe().getIdTiers(), getSession(), ITIRole.CS_AFFILIE, plan
                    .getCompteAnnexe().getIdExterneRole(), affilieFormater.unformat(plan.getCompteAnnexe()
                    .getIdExterneRole()));
        }

        if (isUseLigneTechnique()) {
            // Envoie un e-mail avec les pdf fusionnés
            info.setPublishDocument(true);
            info.setArchiveDocument(false);
            this.mergePDF(info, false, 500, false, null);

        } else {
            // Envoie un e-mail avec les pdf fusionnés
            info.setPublishDocument(true);
            info.setArchiveDocument(false);
            info.setDuplex(true);
            this.mergePDF(info, true, 500, false, null);

            // On refait le docinfo du document généré
            if (getAttachedDocuments().size() > 0) {
                JadePublishDocument doc = (JadePublishDocument) getAttachedDocuments().get(0);
                JadePublishDocumentInfo infoDoc = doc.getPublishJobDefinition().getDocumentInfo();
                infoDoc.setChildren(null);
            }
        }
    }

    /**
     * Récupération de la propriété pour savoir si on est en fonctionnement ligne technique
     * 
     * @return la valeur <code>boolean</code> de la propriété.
     * @throws PropertiesException Si la propriété est absente ou incorrecte
     */
    private boolean isUseLigneTechnique() throws PropertiesException {

        String value = JadePropertiesService.getInstance().getProperty(PROPERTIES_LIGNE_TECH);
        if (value == null) {
            throw new PropertiesException("The properties [" + PROPERTIES_LIGNE_TECH + "] doesn't exist.");
        }

        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return Boolean.parseBoolean(value);
        } else {
            throw new PropertiesException("The value (" + value + ") for the properties " + PROPERTIES_LIGNE_TECH
                    + " is not a boolean value");
        }
    }

    /**
     * @return the dateRef
     */
    public String getDateRef() {
        return dateRef;
    }

    @Override
    protected String getEMailObject() {
        // Sujet du mail
        return document.getDocumentTitle();
    }

    /**
     * @return the idDocument
     */
    public String getIdDocument() {
        return idDocument;
    }

    public String getIdPlanRecouvrement() {
        return idPlanRecouvrement;
    }

    public Boolean getImpAvecBVR() {
        return impAvecBVR;
    }

    /**
     * @return the modele
     */
    public String getModele() {
        return modele;
    }

    /**
     * @return the observation
     */
    public String getObservation() {
        return observation;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    /**
     * @param dateRef
     *            the dateRef to set
     */
    public void setDateRef(String dateRef) {
        this.dateRef = dateRef;
    }

    /**
     * @param idDocument
     *            the idDocument to set
     */
    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
    }

    public void setIdPlanRecouvrement(String string) {
        idPlanRecouvrement = string;
    }

    public void setImpAvecBVR(Boolean newImpAvecBVR) {
        impAvecBVR = newImpAvecBVR;
    }

    /**
     * @param modele
     *            the modele to set
     */
    public void setModele(String modele) {
        this.modele = modele;
    }

    /**
     * @param observation
     *            the observation to set
     */
    public void setObservation(String observation) {
        this.observation = observation;
    }
}
