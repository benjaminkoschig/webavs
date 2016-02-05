package ch.globaz.common.process;

import globaz.globall.db.BTransaction;
import globaz.jade.context.JadeThread;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.job.AbstractJadeJob;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.smtp.JadeSmtpClient;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.LabelCommonProvider;
import com.google.common.base.Throwables;
import com.google.gson.Gson;
import com.sun.star.lang.IllegalArgumentException;

public class ProcessMailUtils {

    public static void sendMail(List<String> mailsList, String subject, String body, List<String> joinsFilesPathsList)
            throws Exception {
        if (subject == null) {
            throw new IllegalArgumentException("subject cannot be null");
        }

        if (body == null) {
            throw new IllegalArgumentException("body cannot be null");
        }

        if (mailsList.isEmpty()) {
            throw new IllegalArgumentException("mailsList is empty");
        }
        String[] mailsTab = mailsList.toArray(new String[mailsList.size()]);

        String[] joinsFilesPathsTab = null;
        if (!joinsFilesPathsList.isEmpty()) {
            joinsFilesPathsTab = joinsFilesPathsList.toArray(new String[joinsFilesPathsList.size()]);
        }

        JadeSmtpClient.getInstance().sendMail(mailsTab, subject, body, joinsFilesPathsTab);
    }

    /**
     * Permet d'envoyer une mail d'erreur à une liste d'emails
     * 
     * @param mailsList
     * @param e
     * @param process
     * @param messageInfo
     * @param transaction
     * @param objectsToJson
     * @throws Exception
     */
    public static void sendMailError(List<String> mailsList, Throwable e, AbstractJadeJob process, String messageInfo,
            BTransaction transaction, Object... objectsToJson) {
        String isoLangue = process.getSession().getIdLangueISO();
        String numAffile = "";

        String body = messageInfo + "\n";
        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
            JadeBusinessMessage[] messages = JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR);
            String message = "";
            for (JadeBusinessMessage jadeBusinessMessage : messages) {
                message = message
                        + JadeI18n.getInstance().getMessage(process.getSession().getIdLangueISO(),
                                jadeBusinessMessage.getMessageId()) + "\n";
            }
            body = body + LabelCommonProvider.getLabel("PROCESS_ERROR", isoLangue) + ": " + message;
        } else {
            if (process.getSession() != null) {
                body = body + process.getSession().getErrors();
            }
            if (transaction != null) {
                body = body + process.getSession().getErrors();
            }
            if (e != null) {
                body = body + LabelCommonProvider.getLabel("PROCESS_ERROR", isoLangue) + ": " + e.getMessage();
            }
        }

        body = body + "\n\n" + LabelCommonProvider.getLabel("PROCESS_TEXT_MAIL_ERROR", isoLangue);

        body = body + "\n\n\n********************* "
                + LabelCommonProvider.getLabel("PROCESS_INFORMATION_GLOBAZ", isoLangue) + "*********************\n\n";
        String bodyGlobaz = "";
        if (e != null) {
            bodyGlobaz = bodyGlobaz + "Stack: \t " + Throwables.getStackTraceAsString(e) + "\n\n";
        }
        // new GsonBuilder().setPrettyPrinting().create()

        if (objectsToJson != null) {
            for (Object object : objectsToJson) {
                try {
                    bodyGlobaz = bodyGlobaz + "Object:\t " + new Gson().toJson(object) + "\n\n";
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }

        try {
            bodyGlobaz = bodyGlobaz + "Params:\t " + new Gson().toJson(process) + "\n\n";
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            if (JadeThread.logHasMessagesToLevel(JadeBusinessMessageLevels.ERROR)) {
                bodyGlobaz = bodyGlobaz + "Thread messages: "
                        + new Gson().toJson(JadeThread.logMessagesToLevel(JadeBusinessMessageLevels.ERROR)) + "\n\n";
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        body = body + bodyGlobaz;

        String[] mailsTab = mailsList.toArray(new String[mailsList.size()]);
        try {
            JadeSmtpClient.getInstance().sendMail(
                    mailsTab,
                    process.getName() + " - " + LabelCommonProvider.getLabel("PROCESS_IN_ERROR", isoLangue) + " "
                            + numAffile, body, null);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (e != null) {
            e.printStackTrace();
        }
    }

    /**
     * permet d'envoyer un mail d'erreur à un mail donné
     * 
     * @param mail
     * @param e
     * @param process
     * @param messageInfo
     * @param transaction
     * @param objectsToJson
     * @throws Exception
     */
    public static void sendMailError(String mail, Throwable e, AbstractJadeJob process, String messageInfo,
            BTransaction transaction, Object... objectsToJson) throws Exception {
        List<String> mailsList = new ArrayList<String>();
        mailsList.add(mail);
        sendMailError(mailsList, e, process, messageInfo, transaction, objectsToJson);
    }
}
