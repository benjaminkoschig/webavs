package globaz.al.process.decision;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstDecisions;
import ch.globaz.al.business.constantes.ALConstJournalisation;
import ch.globaz.al.business.constantes.enumerations.RafamEvDeclencheur;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierDecisionComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.libra.business.services.LibraServiceLocator;
import ch.globaz.libra.constantes.ILIConstantesExternes;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;
import globaz.al.process.ALAbsrtactProcess;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.job.common.JadeJobQueueNames;
import globaz.jade.log.JadeLogger;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;

/**
 * Permet d'exécuter le process d'impression des décisions
 *
 * @author JER/PTA/JTS
 */
public class ALDecisionProcess extends ALAbsrtactProcess {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Date d'impression
     */
    private String dateImpression = null;

    /**
     * Envoi vers la GED ?
     */
    private boolean envoiGED = false;

    /**
     * ID du dossier d'allocation
     */
    private String idDossier = null;

    private boolean isPreview = true;

    /**
     * Méthode qui fournit les information de publication pour les copies de décisions
     *
     * @param containerDecision
     *            JadePrintDocumentCotnaienr
     * @param docData
     *            DocumentData
     */
    private void addCopyInContainer(JadePrintDocumentContainer containerDecision, DocumentData docData) {
        JadePublishDocumentInfo archiveInfo = new JadePublishDocumentInfo();
        archiveInfo.setArchiveDocument(false);
        archiveInfo.setPublishDocument(false);
        containerDecision.addDocument(docData, archiveInfo);
    }

    /**
     * Méthode qui fournit les information de publication pour les décision originale
     *
     * @param dossier
     *            Dossier pour laquelle la décision est faite
     * @param docData
     *            DocumentData
     * @param periode
     *            période
     * @param isGed
     *            <code>true</code> si la décision doit être mise en GED
     * @param containerDecision
     *            JadePrintDocumentContainer
     * @throws Exception
     */
    private void getArchiveIntoDecisionOriginale(DossierComplexModel dossier, DocumentData docData, boolean isGed,
            JadePrintDocumentContainer containerDecision) throws Exception {
        JadePublishDocumentInfo archiveInfo = new JadePublishDocumentInfo();

        archiveInfo.setArchiveDocument(isGed);
        archiveInfo.setPublishDocument(false);
        archiveInfo.setDocumentType("DecisionAF");
        archiveInfo.setDocumentTypeNumber("DecisionAF");
        archiveInfo.setOwnerId(JadeThread.currentUserId());
        archiveInfo.setOwnerEmail(JadeThread.currentUserEmail());
        archiveInfo.setDocumentDate(getDateImpression());
        archiveInfo.setDocumentTitle("");
        archiveInfo.setDocumentSubject(getDocumentSubject(dossier));

        String nssAllocataire = dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                .getPersonneEtendue().getNumAvsActuel();
        String numAffilie = dossier.getDossierModel().getNumeroAffilie();

        String nomPrenom = getNomAllocataire(dossier);
        archiveInfo.setPublishProperty("numero.role.formatte", numAffilie);
        archiveInfo.setPublishProperty("numero.affilie.formatte", numAffilie);
        archiveInfo.setPublishProperty("numero.avs.formatte", nssAllocataire);
        archiveInfo.setPublishProperty("numero.role.non.formatte", JadeStringUtil.removeChar(numAffilie, '.'));
        archiveInfo.setPublishProperty("pyxis.tiers.numero.avs.formatte", nssAllocataire);
        archiveInfo.setPublishProperty("pyxis.tiers.numero.avs.non.formatte",
                JadeStringUtil.removeChar(nssAllocataire, '.'));

        archiveInfo.setPublishProperty("pyxis.tiers.nom.prenom", nomPrenom);
        archiveInfo.setPublishProperty("type.dossier",
                ALServiceLocator.getGedBusinessService().getTypeSousDossier(dossier.getDossierModel()));
        TIBusinessServiceLocator.getDocInfoService().fill(archiveInfo,
                dossier.getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(), ALCSTiers.ROLE_AF,
                dossier.getDossierModel().getNumeroAffilie(),
                JadeStringUtil.removeChar(dossier.getDossierModel().getNumeroAffilie(), '.'));
        // stockage de la décision dans la liste des décisions à
        // imprimer
        containerDecision.addDocument(docData, archiveInfo);

    }

    /**
     * @return La date d'impression du document
     */
    public String getDateImpression() {
        return dateImpression;
    }

    @Override
    public String getDescription() {
        return JadeI18n.getInstance().getMessage(getSession().getUserInfo().getLanguage(),
                "globaz.al.process.decision.ALDecisionProcess.description");
    }

    private String getDocumentSubject(DossierComplexModel dossier) {

        String subject = null;

        if (isPreview) {
            subject = JadeThread.getMessage("al.decision.publication.titre.apercu");
        } else {
            subject = JadeThread.getMessage("al.decision.publication.titre.definitif");
        }

        return subject + getNomAllocataire(dossier);
    }

    /**
     * @return true si les décisions doivent être envoyées vers la GED, false sinon
     */
    public boolean getEnvoiGED() {
        return envoiGED;
    }

    /**
     * @return String
     */
    public String getIdDossier() {
        return idDossier;
    }

    @Override
    public String getName() {
        return JadeI18n.getInstance().getMessage(getSession().getUserInfo().getLanguage(),
                "globaz.al.process.decision.ALDecisionProcess.name");
    }

    private String getNomAllocataire(DossierComplexModel dossier) {
        return dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1() + " "
                + dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2();
    }

    public boolean getPreview() {
        return isPreview;
    }

    @Override
    public String jobQueueName() {
        return JadeJobQueueNames.SYSTEM_INTER_JOB_QUEUE;
    }

    @Override
    public void process() {

        boolean errorInProcess = false;
        try {

            // lecture du dossier
            DossierDecisionComplexModel dossier = ALServiceLocator.getDossierDecisionComplexeModelService()
                    .read(getIdDossier());
            // si le dossier est en suspendu et qu'on envoie en GEd, on (ré)active le dossier
            if (ALCSDossier.ETAT_SUSPENDU.equals(dossier.getDossierModel().getEtatDossier()) && envoiGED) {
                // FIXME vérifier pourquoi seulement modif sur modèle simple. Si possible passer par
                // DossierBusinessService#updateDossierEtEnvoyeAnnoncesRafam
                DossierModel dossierModel = dossier.getDossierModel();
                dossierModel.setEtatDossier(ALCSDossier.ETAT_ACTIF);
                dossier.setDossierModel(ALServiceLocator.getDossierModelService().update(dossierModel));

                ALServiceLocator.getAnnonceRafamCreationService().creerAnnonces(RafamEvDeclencheur.MODIF_DOSSIER,
                        dossier);
            }

            // récupération des info de l'utilsateurs
            HashMap<String, String> userInfo = getUserInfo();
            Class serviceClass = ALServiceLocator.getDecisionProviderService().getDecisionService(dossier);

            // langue pour les documents
            String langueDocument = null;
            // Si langue reprise langue affilié
            if (dossier.getAllocataireComplexModel().getAllocataireModel().getLangueAffilie()) {
                langueDocument = ALServiceLocator.getLangueAllocAffilieService()
                        .langueTiersAffilie(dossier.getDossierModel().getNumeroAffilie());
            }// si reprise langue allocataire
            else {
                langueDocument = ALServiceLocator.getLangueAllocAffilieService().langueTiersAlloc(
                        dossier.getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(),
                        dossier.getDossierModel().getNumeroAffilie());
            }

            // récupération des décisions (original & copies + lettres
            // d'accompagnement)
            HashMap<String, ArrayList<DocumentData>> listesDocument = ALServiceLocator.getDecisionService(serviceClass)
                    .loadData(dossier,
                            JadeStringUtil.isEmpty(getDateImpression())
                                    ? JadeDateUtil.getGlobazFormattedDate(new Date()) : getDateImpression(),
                            langueDocument, userInfo);

            JadePrintDocumentContainer containerDecision = new JadePrintDocumentContainer();

            // décision originale
            getArchiveIntoDecisionOriginale(dossier, listesDocument.get(ALConstDecisions.DECISION_ORIGINALE).get(0),
                    envoiGED, containerDecision);

            // traitement des copies
            for (int i = 0; i < listesDocument.get(ALConstDecisions.DECISION_COPIES).size(); i++) {
                addCopyInContainer(containerDecision, listesDocument.get(ALConstDecisions.DECISION_COPIES).get(i));
            }

            // préparation des données de publication pour le document fusionné
            JadePublishDocumentInfo publishInfo = new JadePublishDocumentInfo();
            publishInfo.setArchiveDocument(false);
            publishInfo.setPublishDocument(true);
            publishInfo.setDocumentType("DecisionAF");
            publishInfo.setDocumentTypeNumber("DecisionAF");
            publishInfo.setDocumentDate(getDateImpression());

            String[] date = { JadeStringUtil.isEmpty(getDateImpression())
                    ? JadeDateUtil.getGlobazFormattedDate(new Date()) : getDateImpression() };
            publishInfo.setDocumentTitle(getDocumentSubject(dossier));
            publishInfo.setDocumentSubject(getDocumentSubject(dossier));
            publishInfo.setOwnerId(JadeThread.currentUserId());
            publishInfo.setOwnerEmail(JadeThread.currentUserEmail());

            // informations de publication pour le document fusionné
            containerDecision.setMergedDocDestination(publishInfo);

            this.createDocuments(containerDecision);

            // Rest du flag IDGEST dans ALDOSSIER seulement si ce n'est pas un aperçu
            if (!isPreview) {
                dossier.getDossierModel().setIdGestionnaire(null);
                ALServiceLocator.getDossierModelService().update(dossier.getDossierModel());
            }

            // journalisation d'une décision si envoi en GED est à true
            if (getEnvoiGED()) {

                try {

                    LibraServiceLocator.getJournalisationService().createJournalisationAvecRemarqueWithTestDossier(
                            idDossier, ALConstJournalisation.DECISION_MOTIF_JOURNALISATION, "Décision mise en ged",
                            dossier.getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(),
                            ILIConstantesExternes.CS_DOMAINE_AF, true);

                } catch (Exception e) {
                    errorInProcess = true;
                    e.printStackTrace();
                    getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                            new String[] { e.getMessage() });
                }
            }
        } catch (Exception e) {
            errorInProcess = true;
            e.printStackTrace();
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                    new String[] { e.getMessage() });
        }

        // Envoie d'un mail si problème pour lancer le traitement
        ArrayList<String> emails = new ArrayList<String>();
        emails.add(JadeThread.currentUserEmail());
        if (errorInProcess) {
            try {
                sendCompletionMail(emails);
            } catch (Exception e1) {
                JadeLogger.error(this, "Impossible d'envoyer le mail de résultat du traitement. Raison : "
                        + e1.getMessage() + ", " + e1.getCause());
            }
        }
    }

    /**
     * @param dateImpression
     *            La date d'impression du document
     */
    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    /**
     * @param envoiGED
     *            true si les décisions doivent être envoyées vers la GED, false sinon
     */
    public void setEnvoiGED(boolean envoiGED) {
        this.envoiGED = envoiGED;
    }

    /**
     * @param idDossier
     *            ID du dossier d'allocation
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setPreview(boolean preview) {
        isPreview = preview;
    }

}