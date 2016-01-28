package globaz.pegasus.process.lot;

import globaz.globall.db.BSession;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessLogSession;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;
import globaz.jade.smtp.JadeSmtpClient;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.pegasus.businessimpl.services.models.decision.ged.DACGedHandler;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.ComptabilisationHandler;

public class ComptabiliserProcessMailHandler {

    // type de process compta ou mise en ged
    public enum PROCESS_TYPE {
        COMPTABILISATION("COMPTA"),
        MISE_EN_GED("GED");

        // prefixe du label servant à récupérer le label correct
        private String labelPrefix = null;

        PROCESS_TYPE(String labelPrefix) {
            this.labelPrefix = labelPrefix;
        }
    }

    private final static String CR = " \n\r";
    private final static String DESC_REPLACE = "{desc}";
    private final static String ID_REPLACE = "{id}";
    private DACGedHandler gedHandler = null;
    private JadeBusinessLogSession logs = null;
    private SimpleLot lot = null;
    private StringBuilder mailBody = new StringBuilder("");
    private StringBuilder mailSubject = new StringBuilder("");
    private BSession session = null;

    private PROCESS_TYPE typeProcess = null;

    /**
     * Constructeur
     * 
     * @param process
     *            le type de process @see PROCESS_TYPE
     * @param lot
     *            le lot concerné
     * @param session
     *            la session
     * @param logs
     *            le container de logs
     * @param gedHandler2
     */
    public ComptabiliserProcessMailHandler(PROCESS_TYPE process, SimpleLot lot, BSession session,
            JadeBusinessLogSession logs, DACGedHandler gedHandler) {

        if (session == null) {
            throw new IllegalArgumentException(
                    "The session cannot be null to handle the mail process for compatbilisation process " + "["
                            + this.getClass().getName() + "]");
        }

        this.gedHandler = gedHandler;
        this.session = session;
        this.logs = logs;
        this.lot = lot;
        typeProcess = process;
        initMailBody();
        buildMail();
    }

    /**
     * Constrution du mail pour le processus de comptabilisation
     */
    private void buildComptaProcessMail() {

        // comptabilisation process time
        String time = ComptabilisationHandler.displayTime();

        buildMailContent();

        mailBody.insert(0, time + ComptabiliserProcessMailHandler.CR);
    }

    private void buildMail() {
        switch (typeProcess) {
            case COMPTABILISATION:
                buildComptaProcessMail();
                break;

            case MISE_EN_GED:
                buildMiseEnGedProcessMail();
                break;
        }

    }

    private void buildMailContent() {

        // erreur
        if ((logs.getMaxLevel() == JadeBusinessMessageLevels.ERROR)
                || JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {

            mailSubject.append(session.getLabel("PROCESS_MAIL_" + typeProcess.labelPrefix + "_ERROR")).append("(")
                    .append(lot.getIdLot()).append(") : ").append(lot.getDescription());

            mailBody.append(ComptabiliserProcessMailHandler.CR).append(getLogSession());
        }
        // warning
        else if (logs.getMaxLevel() == JadeBusinessMessageLevels.WARN) {

            String desc = (lot.getDescription() == null) ? "" : lot.getDescription();

            mailSubject.append(session.getLabel("PROCESS_MAIL_" + typeProcess.labelPrefix + "_WARN")).append("(")
                    .append(lot.getIdLot()).append(")  ").append(desc);
        }
        // ok
        else {

            String replaceStr = new String(session.getLabel("PROCESS_MAIL_" + typeProcess.labelPrefix + "_OK"));

            replaceStr = StringUtils.replace(replaceStr, ComptabiliserProcessMailHandler.ID_REPLACE, lot.getIdLot());
            replaceStr = StringUtils.replace(replaceStr, ComptabiliserProcessMailHandler.DESC_REPLACE,
                    lot.getDescription());

            mailSubject.append(replaceStr);
        }

    }

    /**
     * Constrution du mail pour le processus de mise en ged
     */
    private void buildMiseEnGedProcessMail() {

        buildMailContent();
    }

    /**
     * Retourne la log session courante
     * 
     * @return
     */
    private String getLogSession() {
        return JadeBusinessMessageRenderer.getInstance().getDefaultAdapter()
                .render(logs.getMessages(), session.getIdLangueISO());
    }

    /**
     * Initiaisatioon du mail body
     */
    private void initMailBody() {

        switch (typeProcess) {
            case COMPTABILISATION:
                mailBody.append(JadeBusinessMessageRenderer.getInstance().getDefaultAdapter()
                        .render(JadeThread.logMessages(), JadeThread.currentLanguage()));
                break;

            case MISE_EN_GED:
                if (JadeThread.logMessages() == null) {
                    mailBody.append(gedHandler.generateMiseEnGedStatistiques());
                } else {
                    mailBody.append(JadeBusinessMessageRenderer.getInstance().getDefaultAdapter()
                            .render(JadeThread.logMessages(), JadeThread.currentLanguage()));
                }

                break;
        }

    }

    /**
     * Envoi du mail proprement dit
     * 
     * @param emailAdresses
     *            la liste des destinataires
     * @throws Exception
     *             exception succeptible d'être levé
     */
    public void sendMail(List<String> emailAdresses) throws Exception {

        if (emailAdresses == null) {
            throw new NullPointerException("cannot send completion mail: dest list is null");
        }
        if (emailAdresses.isEmpty()) {
            return;
        }
        BSession theSession = session;
        if (theSession == null) {
            throw new IllegalStateException("cannot send completion mail: user session is null.");
        }

        String[] emailsAsArray = emailAdresses.toArray(new String[emailAdresses.size()]);
        assert emailsAsArray != null : "emailsAsArray is null!";
        for (int i = 0; i < emailsAsArray.length; i++) {
            if (emailsAsArray[i] == null) {
                throw new NullPointerException(
                        "Cannot send completion mails: an email is null in List. No single mail sent!");
            }
        }
        JadeSmtpClient.getInstance().sendMail(emailsAsArray, mailSubject.toString(), mailBody.toString(), null);
    }

}
