package globaz.al.process.decision;

import globaz.al.process.ALAbsrtactProcess;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.job.common.JadeJobQueueNames;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.message.JadePublishDocumentMessage;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import ch.globaz.al.business.constantes.ALCSCopie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.constantes.ALConstDecisions;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierDecisionComplexModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Permet d'ex�cuter le process d'impression des d�cisions en masse NE PAS UTILISER EN PROD
 * 
 * @author PTA
 * @deprecated Process cr�� pour l'impression des d�cisions des caisses horlog�res lors de l'introduction du suppl�ment
 *             de 30.-
 */
@Deprecated
public class ALDecisionLotProcess extends ALAbsrtactProcess {
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

    /**
     * liste des dossiers � imprimer
     */

    private ArrayList<String> listeDecisionDossier = null;

    /**
     * peronne de r�f�rence
     */
    private String perRef = null;

    /**
     * M�thode qui fournit les information de publication pour les copies de d�cisions
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
     * M�thode qui fournit les information de publication pour les d�cision originale
     * 
     * @param dossier
     *            Dossier pour laquelle la d�cision est faite
     * @param docData
     *            DocumentData
     * @param periode
     *            p�riode
     * @param isGed
     *            <code>true</code> si la d�cision doit �tre mise en GED
     * @param containerDecision
     *            JadePrintDocumentContainer
     * @throws Exception
     */
    private void getArchiveIntoDecisionOriginale(DossierComplexModel dossier, DocumentData docData, boolean isGed,
            JadePrintDocumentContainer containerDecision) throws Exception {
        JadePublishDocumentInfo archiveInfo = new JadePublishDocumentInfo();

        // archive � true TODO remettre � isGed
        archiveInfo.setArchiveDocument(true);
        archiveInfo.setPublishDocument(false);
        archiveInfo.setDocumentType("DecisionAF");
        archiveInfo.setDocumentTypeNumber("DecisionAF");
        archiveInfo.setOwnerId(JadeThread.currentUserId());
        archiveInfo.setOwnerEmail(JadeThread.currentUserEmail());
        archiveInfo.setDocumentDate(getDateImpression());
        archiveInfo.setDocumentTitle("");
        archiveInfo.setDocumentSubject(JadeThread.getMessage(
                "al.decision.publication.titre",
                new String[] { JadeStringUtil.isEmpty(getDateImpression()) ? JadeDateUtil
                        .getGlobazFormattedDate(new Date()) : getDateImpression() }));

        String nssAllocataire = dossier.getAllocataireComplexModel().getPersonneEtendueComplexModel()
                .getPersonneEtendue().getNumAvsActuel();
        String numAffilie = dossier.getDossierModel().getNumeroAffilie();

        archiveInfo.setPublishProperty("numero.role.formatte", numAffilie);
        archiveInfo.setPublishProperty("numero.affilie.formatte", numAffilie);
        archiveInfo.setPublishProperty("numero.avs.formatte", nssAllocataire);
        archiveInfo.setPublishProperty("numero.role.non.formatte", JadeStringUtil.removeChar(numAffilie, '.'));
        archiveInfo.setPublishProperty("pyxis.tiers.numero.avs.formatte", nssAllocataire);
        archiveInfo.setPublishProperty("pyxis.tiers.numero.avs.non.formatte",
                JadeStringUtil.removeChar(nssAllocataire, '.'));
        archiveInfo.setPublishProperty("type.dossier",
                ALServiceLocator.getGedBusinessService().getTypeSousDossier(dossier.getDossierModel()));
        TIBusinessServiceLocator.getDocInfoService().fill(archiveInfo,
                dossier.getAllocataireComplexModel().getAllocataireModel().getIdTiersAllocataire(), null, null,
                ALCSTiers.ROLE_AF, dossier.getDossierModel().getNumeroAffilie(),
                JadeStringUtil.removeChar(dossier.getDossierModel().getNumeroAffilie(), '.'), null);
        // stockage de la d�cision dans la liste des d�cisions �
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

    /**
     * @return true si les d�cisions doivent �tre envoy�es vers la GED, false sinon
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

    public ArrayList<String> getListeDecisionDossier() {
        return listeDecisionDossier;
    }

    @Override
    public String getName() {
        return JadeI18n.getInstance().getMessage(getSession().getUserInfo().getLanguage(),
                "globaz.al.process.decision.ALDecisionProcess.name");
    }

    public String getPerRef() {
        return perRef;
    }

    @Override
    public String jobQueueName() {
        return JadeJobQueueNames.SYSTEM_INTER_JOB_QUEUE;
    }

    @Override
    public void process() {

        boolean errorInProcess = false;

        JadePrintDocumentContainer containerDecision = new JadePrintDocumentContainer();

        ProtocoleLogger logger = new ProtocoleLogger();
        DossierDecisionComplexModel dossier = null;

        // creastion de chaque d�c�sion et de ses copies
        for (int j = 0; j < listeDecisionDossier.size(); j++) {
            try {
                // lecture du dossier
                dossier = ALServiceLocator.getDossierDecisionComplexeModelService().read(listeDecisionDossier.get(j));
                // si le dossier est en suspendu et qu'on envoie en GEd, on (r�)active le dossier
                if (ALCSDossier.ETAT_SUSPENDU.equals(dossier.getDossierModel().getEtatDossier()) && envoiGED) {
                    DossierModel dossierModel = dossier.getDossierModel();
                    dossierModel.setEtatDossier(ALCSDossier.ETAT_ACTIF);
                    dossier.setDossierModel(ALServiceLocator.getDossierModelService().update(dossierModel));
                }

                // r�cup�ration des info de l'utilsateurs
                // HashMap<String, String> userInfo = this.getUserInfo();

                HashMap<String, String> userInfo = new HashMap<String, String>();
                userInfo.put(ALConstDocument.USER_MAIL, "valerie.ellenberger@ccih5110.ch");
                userInfo.put(ALConstDocument.USER_NAME, "Ellenberger Val�rie");
                userInfo.put(ALConstDocument.USER_PHONE, "032 344 46 22");
                userInfo.put(ALConstDocument.USER_VISA, "h51xve");
                // userInfo.put(ALConstDocument.USER_NAME, "Ellenberger Val�rie");
                // userInfo.put(ALConstDocument.USER_PHONE, "032 344 46 22");
                // userInfo.put(ALConstDocument.USER_VISA, "h51xve");

                Class serviceClass = ALServiceLocator.getDecisionProviderService().getDecisionService(dossier);
                String langueDocument = ALServiceLocator.getLangueAllocAffilieService().langueTiersAffilie(
                        dossier.getDossierModel().getNumeroAffilie());

                // Cr�ation des copies par d�faut pour les dossier
                ALServiceLocator.getCopiesBusinessService().createDefaultCopies(dossier, ALCSCopie.TYPE_DECISION);

                // r�cup�ration des d�cisions (original & copies + lettres
                // d'accompagnement)
                HashMap<String, ArrayList<DocumentData>> listesDocument = ALServiceLocator.getDecisionService(
                        serviceClass).loadData(
                        dossier,
                        JadeStringUtil.isEmpty(getDateImpression()) ? JadeDateUtil.getGlobazFormattedDate(new Date())
                                : getDateImpression(), langueDocument, userInfo);

                // JadePrintDocumentContainer containerDecision = new JadePrintDocumentContainer();

                // d�cision originale
                getArchiveIntoDecisionOriginale(dossier,
                        listesDocument.get(ALConstDecisions.DECISION_ORIGINALE).get(0), envoiGED, containerDecision);

                // traitement des copies
                for (int i = 0; i < listesDocument.get(ALConstDecisions.DECISION_COPIES).size(); i++) {
                    addCopyInContainer(containerDecision, listesDocument.get(ALConstDecisions.DECISION_COPIES).get(i));
                }
            } catch (Exception e) {

                try {
                    logger.getErrorsLogger(dossier.getId(), dossier.getId()).addMessage(
                            new JadeBusinessMessage(JadeBusinessMessageLevels.WARN, ALDecisionLotProcess.class
                                    .getName(), e.getMessage()));

                } catch (JadeApplicationException e1) {
                    e1.printStackTrace();
                }

                // errorInProcess = true;
                // e.printStackTrace();
                // this.getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                // new String[] { e.getMessage() });
            }
        }

        // pr�paration des donn�es de publication pour le document fusionn� TODO pour les masses � archiver
        JadePublishDocumentInfo publishInfo = new JadePublishDocumentInfo();
        publishInfo.setArchiveDocument(true);
        publishInfo.setPublishDocument(false);
        publishInfo.setDocumentType("DecisionAF");
        publishInfo.setDocumentTypeNumber("DecisionAF");
        publishInfo.setDocumentDate(getDateImpression());

        String[] date = { JadeStringUtil.isEmpty(getDateImpression()) ? JadeDateUtil.getGlobazFormattedDate(new Date())
                : getDateImpression() };
        publishInfo.setDocumentTitle(JadeThread.getMessage("al.decision.publication.titre", date));
        publishInfo.setDocumentSubject(JadeThread.getMessage("al.decision.publication.titre", date));
        publishInfo.setOwnerId(JadeThread.currentUserId());
        publishInfo.setOwnerEmail(JadeThread.currentUserEmail());

        // informations de publication pour le document fusionn�
        containerDecision.setMergedDocDestination(publishInfo);

        try {
            this.createDocuments(containerDecision);
        } catch (JadeServiceLocatorException e) {

            e.printStackTrace();
        } catch (JadeServiceActivatorException e) {

            e.printStackTrace();
        } catch (JadeClassCastException e) {

            e.printStackTrace();
        }

        // Envoie d'un mail si probl�me pour lancer le traitement
        ArrayList<String> emails = new ArrayList<String>();
        emails.add(JadeThread.currentUserEmail());
        if (errorInProcess) {
            try {
                sendCompletionMail(emails);
            } catch (Exception e1) {
                JadeLogger.error(this,
                        "Impossible d'envoyer le mail de r�sultat du traitement. Raison : " + e1.getMessage() + ", "
                                + e1.getCause());
            }
        }

        JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();
        pubInfo.setOwnerId(JadeThread.currentUserId());
        pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfo.setDocumentTitle("Impression de masse de d�cisions");
        pubInfo.setDocumentSubject("Impression de masse de d�cisions");
        pubInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        String fileName = "protocole_csv.csv";

        try {
            if (logger.getErrorsContainer().size() != 0) {
                FileOutputStream fichier = new FileOutputStream(fileName);

                fichier.write(logger.toCSV().getBytes());

                fichier.flush();
                fichier.close();

                JadePublishDocument docInfoCSV = new JadePublishDocument(fileName, pubInfo);

                JadePublishServerFacade.publishDocument(new JadePublishDocumentMessage(docInfoCSV));
            }

        } catch (IOException e) {

            e.printStackTrace();
        } catch (JadeServiceLocatorException e) {

            e.printStackTrace();
        } catch (JadeServiceActivatorException e) {

            e.printStackTrace();
        } catch (NullPointerException e) {

            e.printStackTrace();
        } catch (ClassCastException e) {

            e.printStackTrace();
        } catch (JadeClassCastException e) {

            e.printStackTrace();
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
     *            true si les d�cisions doivent �tre envoy�es vers la GED, false sinon
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

    public void setListeDecisionDossier(ArrayList<String> listeDecisionDossier) {
        this.listeDecisionDossier = listeDecisionDossier;
    }

    public void setPerRef(String perRef) {
        this.perRef = perRef;
    }

}