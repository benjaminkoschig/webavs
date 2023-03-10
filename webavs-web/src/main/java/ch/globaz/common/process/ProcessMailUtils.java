package ch.globaz.common.process;

import ch.globaz.common.exceptions.CommonTechnicalException;
import globaz.globall.db.BProcess;
import globaz.globall.db.BSession;
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
import ch.globaz.common.process.byitem.ProcessItemsHandlerJadeJob;
import com.google.common.base.Throwables;
import com.google.gson.Gson;

public class ProcessMailUtils {

    public static void sendMail(List<String> mailsList, String subject, String body, List<String> joinsFilesPathsList) {
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

        try {
            JadeSmtpClient.getInstance().sendMail(mailsTab, subject, body, joinsFilesPathsTab);
        } catch (Exception e) {
            throw new CommonTechnicalException("Erreur ? l'envoi de l'e-mail", e);
        }
    }

    /**
     * Permet d'envoyer une mail d'erreur ? une liste d'emails
     * 
     * @param mailsList
     * @param e
     * @param process doit ?tre de type AbstractJadeJob ou BProcess
     * @param messageInfo
     * @param transaction
     * @param objectsToJson
     * @throws IllegalArgumentException
     * @throws Exception
     */
    public static void sendMailError(List<String> mailsList, Throwable e, Object processObject, String messageInfo,
            BTransaction transaction, Object... objectsToJson) {
        BSession session;
        String processName;

        if (processObject instanceof ProcessItemsHandlerJadeJob) {
            session = ((ProcessItemsHandlerJadeJob) processObject).getSession();
            processName = session.getLabel(((ProcessItemsHandlerJadeJob) processObject).getName());
        } else if (processObject instanceof AbstractJadeJob) {
            session = ((AbstractJadeJob) processObject).getSession();
            processName = ((AbstractJadeJob) processObject).getName();
        } else if (processObject instanceof BProcess) {
            session = ((BProcess) processObject).getSession();
            processName = ((BProcess) processObject).getName();
        } else {
            throw new java.lang.IllegalArgumentException("The processObject is not an inherited process object.");
        }

        String isoLangue = session.getIdLangueISO();
        String numAffile = "";

        String body = messageInfo + "\n";
        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
            JadeBusinessMessage[] messages = JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.ERROR);
            String message = "";
            for (JadeBusinessMessage jadeBusinessMessage : messages) {
                message = message
                        + JadeI18n.getInstance().getMessage(session.getIdLangueISO(),
                                jadeBusinessMessage.getMessageId()) + "\n";
            }
            body = body + LabelCommonProvider.getLabel("PROCESS_ERROR", isoLangue) + ": " + message;
        } else {
            if (session != null) {
                body = body + session.getErrors();
            }
            if (transaction != null) {
                body = body + transaction.getSession().getErrors();
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
            bodyGlobaz = bodyGlobaz + "Params:\t " + new Gson().toJson(processObject) + "\n\n";
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
            JadeSmtpClient.getInstance()
                    .sendMail(
                            mailsTab,
                            processName + " - " + LabelCommonProvider.getLabel("PROCESS_IN_ERROR", isoLangue) + " "
                                    + numAffile, body, null);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (e != null) {
            e.printStackTrace();
        }
    }

    /**
     * permet d'envoyer un mail d'erreur ? un mail donn?
     * 
     * @param mail
     * @param e
     * @param process
     * @param messageInfo
     * @param transaction
     * @param objectsToJson
     * @throws Exception
     */
    public static void sendMailError(String mail, Throwable e, Object processObject, String messageInfo,
            Object... objectsToJson) throws Exception {
        List<String> mailsList = new ArrayList<String>();
        mailsList.add(mail);
        sendMailError(mailsList, e, processObject, messageInfo, null, objectsToJson);
    }

    /**
     * permet d'envoyer un mail d'erreur ? un mail donn?
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
