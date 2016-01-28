package globaz.pegasus.process.annonce.communicationocc;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pegasus.process.PCAbstractJob;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Periode;
import ch.globaz.pegasus.business.models.annonce.CommunicationOCC;
import ch.globaz.pegasus.business.models.annonce.CommunicationOCCSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCGenererCommunicationOCCProcess extends PCAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = null;
    private String mailGest = null;
    private String noSemaine = null;
    private String sessionUserName = null;
    private String sessionUserPassword = null;

    public String getAnnee() {
        return annee;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public String getMailGest() {
        return mailGest;
    }

    @Override
    public String getName() {
        return null;
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void process() {

        BSession session = BSessionUtil.getSessionFromThreadContext();
        try {

            Periode periode = Periode.resolvePeriodeByWeek(annee, noSemaine);

            // recherche de toutes les communications en attente pour la semaine donnée
            CommunicationOCCSearch searchModel = new CommunicationOCCSearch();
            searchModel.setForDateDebutRapport(periode.getDateDebut());
            searchModel.setForDateFinRapport(periode.getDateFin());
            searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            searchModel = PegasusServiceLocator.getCommunicationOCCService().search(searchModel);

            if (searchModel.getSize() == 0) {
                JadeSmtpClient.getInstance().sendMail(
                        mailGest,
                        session.getLabel("DOC_COMMUNICATION_OCC_EMAIL_TITRE"),
                        session.getLabel("DOC_COMMUNICATION_OCC_EMAIL_SANS_CONTENU") + getNoSemaine().toString()
                                + getSession().getLabel("GENERATION_ANNONCE_LAPRAMS_ANNEE") + " "
                                + getAnnee().toString(), null);

            } else {

                List<CommunicationOCC> listeOCC = new ArrayList<CommunicationOCC>();
                for (JadeAbstractModel absDonnee : searchModel.getSearchResults()) {
                    listeOCC.add((CommunicationOCC) absDonnee);
                }

                JadePublishDocumentInfo pubInfos = JadePublishDocumentInfoProvider.newInstance(this);

                JadePrintDocumentContainer cont = PegasusServiceLocator.getCommunicationOCCBuilderProviderService()
                        .getCommunicationOCCBuilder().build(listeOCC, pubInfos, mailGest, periode.getDateDebut());

                this.createDocuments(cont);
            }

        } catch (Exception e) {
            JadeLogger.error(session.getLabel("DOC_COMMUNICATION_OCC_ERROR"), e);

        } finally {
            try {

                this.sendMail();

            } catch (Exception e) {
                JadeLogger.error("PCGenererCommunicationOCCProcess", e);
            }
        }

    }

    private void sendMail() {
        ArrayList<String> monMail = new ArrayList<String>();
        monMail.add(mailGest);

        JadeBusinessLogSession logs = getLogSession();

        String logSession = JadeBusinessMessageRenderer.getInstance().getDefaultAdapter()
                .render(logs.getMessages(), getSession().getIdLangueISO());

        String body = JadeBusinessMessageRenderer.getInstance().getDefaultAdapter()
                .render(JadeThread.logMessages(), JadeThread.currentLanguage());

        if ((logs.getMaxLevel() == JadeBusinessMessageLevels.ERROR)
                || JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            body = body + getSession().getLabel("GENERATION_COMMUNICATION_OVAM_ERROR");
            body = body + " \n\r" + logSession;
        } else {
            body = body + getSession().getLabel("GENERATION_COMMUNICATION_OVAM_GOOD");
        }

        String subject = getSession().getLabel("GENERATION_COMMUNICATION_OVAM");

        String[] emailsAsArray = monMail.toArray(new String[monMail.size()]);
        assert emailsAsArray != null : "emailsAsArray is null!";
        for (int i = 0; i < emailsAsArray.length; i++) {
            if (emailsAsArray[i] == null) {
                throw new NullPointerException(
                        "Cannot send completion mails: an email is null in List. No single mail sent!");
            }
        }
        try {
            JadeSmtpClient.getInstance().sendMail(emailsAsArray, subject, body, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setAnnee(String annee) {
        this.annee = annee;
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
