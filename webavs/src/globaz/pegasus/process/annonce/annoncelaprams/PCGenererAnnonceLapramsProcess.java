package globaz.pegasus.process.annonce.annoncelaprams;

import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.job.server.JadeJobServer;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.client.JadePublishDocumentProducer;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.document.JadePublishDocumentContent;
import globaz.jade.publish.message.JadePublishDocumentMessage;
import globaz.jade.smtp.JadeSmtpClient;
import java.io.File;
import java.util.List;
import ch.globaz.common.domaine.Periode;
import ch.globaz.pegasus.business.models.annonce.AnnonceLaprams;
import ch.globaz.pegasus.business.models.annonce.AnnonceLapramsSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

public class PCGenererAnnonceLapramsProcess extends BProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = null;
    private String emailObject = "";
    private boolean forceCompletionMail = false;
    private String isProcessForDaemon = null;
    private String mailGest = null;

    private String message;
    private String noSemaine = null;
    private String sessionUserName = null;

    private String sessionUserPassword = null;

    private String typeMessage;

    @Override
    protected void _executeCleanUp() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean _executeProcess() {

        boolean success = true;
        setForceCompletionMail(true);
        setSendMailOnError(true);

        try {

            BSessionUtil.initContext(getSession(), this);

            Periode periode = Periode.resolvePeriodeByWeek(annee, noSemaine);

            // recherche de toutes les communications en attente pour la semaine donnée
            AnnonceLapramsSearch searchModel = new AnnonceLapramsSearch();
            searchModel.setForDateDebutRapport(periode.getDateDebut());
            searchModel.setForDateFinRapport(periode.getDateFin());
            searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            searchModel = PegasusServiceLocator.getAnnonceLapramsService().search(searchModel);

            if (searchModel.getSize() == 0) {
                message = getSession().getLabel("GENERATION_ANNONCE_LAPRAMS_PAS_DE_DONNES") + " "
                        + getNoSemaine().toString() + getSession().getLabel("GENERATION_ANNONCE_LAPRAMS_ANNEE") + " "
                        + getAnnee().toString();
            } else {
                List<AnnonceLaprams> listeLaprams = PersistenceUtil.typeSearch(searchModel);

                PegasusServiceLocator.getAnnonceLapramsBuilderProviderService().getAnnonceLapramsDefaultBuilder()
                        .build(listeLaprams, mailGest, periode.getDateDebut(), this);

                message = getSession().getLabel("GENERATION_ANNONCE_LAPRAMS_OK");
            }

            typeMessage = FWMessage.INFORMATION;
        } catch (Exception e) {
            success = false;

            message = getSession().getLabel("GENERATION_ANNONCE_LAPRAMS_KO") + "\n" + e.getLocalizedMessage();
            typeMessage = FWMessage.ERREUR;
            e.printStackTrace();

        } finally {
            try {

                String status = (success == true) ? "SUCCES" : "ERREUR";

                emailObject = FWMessageFormat.format(getSession().getLabel("GENERATION_ANNONCE_LAPRAMS"), status, this);

                setEMailAddress(mailGest);
                setSendCompletionMail(true);

                getMemoryLog().logMessage(message, typeMessage, "");

                if (Boolean.valueOf(isProcessForDaemon)) {
                    sendMailForDameon();
                }

                BSessionUtil.stopUsingContext(this);

                return success;
            } catch (Exception e) {
                typeMessage = FWMessage.ERREUR;
                getMemoryLog().logMessage(message + " - " + e.getMessage(), typeMessage, this.getClass().toString());
            }
        }
        return success;
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected void _validate() throws Exception {
        if (getParent() == null) {
            if ((getEMailAddress() == null) || getEMailAddress().equals("")) {
                setSendCompletionMail(false);
            } else {
                setSendCompletionMail(true);
            }

            setControleTransaction(true);

        }

        if (getSession().hasErrors()) {
            abort();
        }
    }

    public String getAnnee() {

        return annee;
    }

    public String getDescription() {
        return null;
    }

    @Override
    protected String getEMailObject() {
        // TODO Auto-generated method stub
        return emailObject;
    }

    @Override
    public boolean getForceCompletionMail() {
        return forceCompletionMail;
    }

    public String getIsProcessForDaemon() {
        return isProcessForDaemon;
    }

    public String getMailGest() {
        return mailGest;
    }

    public String getNoSemaine() {
        return noSemaine;
    }

    public String getSessionUserName() {
        return sessionUserName;
    }

    public String getSessionUserPassword() {
        return sessionUserPassword;
    }

    private void initSession() {

        if (!JadeStringUtil.isBlank(getSessionUserName()) && !JadeStringUtil.isBlank(getSessionUserPassword())) {
            try {
                setSession((BSession) GlobazSystem.getApplication("PEGASUS").newSession(getSessionUserName(),
                        getSessionUserPassword()));
            } catch (Exception e) {
                JadeLogger.error("PCGenererCommunicationOCCProcess", e);
            }
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.UPDATE_SHORT;
    }

    private void sendMailForDameon() throws Exception {
        JadePublishDocumentProducer docProducer = this;
        List<?> publishDocuments = docProducer.getAttachedDocuments();

        for (int i = 0; i < publishDocuments.size(); i++) {
            JadePublishDocument publishDocument = (JadePublishDocument) publishDocuments.get(i);

            if (JadeJobServer.getInstance().isVerbose()) {
                JadeLogger.info(this, "sending document " + publishDocument.getDocumentLocation()
                        + " to JadePublishServer (jobUID="
                        + publishDocument.getPublishJobDefinition().getDocumentInfo().getJobUID() + ")...");
            }

            JadePublishDocumentMessage message = new JadePublishDocumentMessage(publishDocument);
            if (!JadePublishServerFacade.isLocal()) {
                JadePublishDocumentContent contents = new JadePublishDocumentContent(new File(
                        message.getOriginalFilename()));
                message.setDocumentContent(contents);
            }

            JadePublishServerFacade.publishDocument(message);
            if (JadeJobServer.getInstance().isVerbose()) {
                JadeLogger.info(this, "document " + publishDocument.getDocumentLocation()
                        + " sent to JadePublishServer (jobUID="
                        + publishDocument.getPublishJobDefinition().getDocumentInfo().getJobUID() + ")");
            }
        }

        JadeSmtpClient.getInstance().sendMail(docProducer.getEMailAddress(),
                getSession().getLabel("GENERATION_ANNONCE_LAPRAMS"), docProducer.getSubjectDetail(), null);

    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setForceCompletionMail(boolean forceCompletionMail) {
        this.forceCompletionMail = forceCompletionMail;
    }

    public void setIsProcessForDaemon(String isProcessForDaemon) {
        this.isProcessForDaemon = isProcessForDaemon;
    }

    public void setMailGest(String mailGest) {
        this.mailGest = mailGest;
    }

    public void setNoSemaine(String noSemaine) {
        this.noSemaine = noSemaine;
    }

    public void setSessionUserName(String sessionUserName) {
        this.sessionUserName = sessionUserName;
        initSession();
    }

    public void setSessionUserPassword(String sessionUserPassword) {
        this.sessionUserPassword = sessionUserPassword;
        initSession();
    }

}
