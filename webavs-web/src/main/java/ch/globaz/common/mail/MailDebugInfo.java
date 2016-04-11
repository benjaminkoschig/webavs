package ch.globaz.common.mail;

import globaz.globall.db.BSessionUtil;
import globaz.jade.common.JadeCodingUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.JadeThreadContextPool;
import globaz.jade.smtp.JadeSmtpClient;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import ch.globaz.common.LabelCommonProvider;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;
import com.google.gson.Gson;

public class MailDebugInfo {

    public static void sendMail(List<String> to, String Subject, String body) {

        body += "\n\n\n********************* " + LabelCommonProvider.getLabel("PROCESS_INFORMATION_GLOBAZ")
                + " *********************";

        final Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("Session ID", BSessionUtil.getSessionFromThreadContext().getId());
        map.put("Session", BSessionUtil.getSessionFromThreadContext());
        map.put("CurrentThread", JadeThread.currentContext());
        map.put("Pool", JadeThreadContextPool.getInstance());

        body += valuesToJson(map);

        try {
            String[] emails = to.toArray(new String[to.size()]);
            JadeSmtpClient.getInstance().sendMail(emails, Subject, body, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendMail(IProperties mailsProperties, String subject, String body) {
        try {
            String mailsSparateByComma = mailsProperties.getValue().trim();
            sendMail(mailsSparateByComma, subject, body);
        } catch (PropertiesException e) {
            JadeCodingUtil.catchException(MailDebugInfo.class, "sendMail", e);
        }
    }

    public static void sendMail(String mailsSparateByComma, String subject, String body) {
        List<String> mails;
        if (!mailsSparateByComma.isEmpty()) {
            mails = Arrays.asList(mailsSparateByComma.split(","));
            if (mails != null && !mails.isEmpty()) {
                sendMail(mails, subject, body);
            }
        }
    }

    public static String toJson(Object object) {
        final Gson gson = new Gson();
        return "\n" + gson.toJson(object);
    }

    private static String valuesToJson(final Map<String, Object> map) {
        final Gson gson = new Gson();
        String body = "";

        for (Entry<String, Object> entry : map.entrySet()) {
            try {
                body += "\n\n" + entry.getKey() + "\n" + gson.toJson(entry.getValue());
            } catch (Exception e) {
                body += "Unabled to jsonify the " + entry.getKey() + ". Cause :" + e.toString();
            }
        }
        body = replacePassWord(body);
        return body;
    }

    static String replacePassWord(String body) {
        body = Pattern.compile("\"password\":\"(.*)\"").matcher(body).replaceAll("\"password\":\"********\"");
        return body;
    }

}
