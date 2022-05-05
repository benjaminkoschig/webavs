/**
 * 
 */
package globaz.apg.services;

import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.log.JadeLogger;
import globaz.jade.sedex.annotation.OnReceive;
import globaz.jade.sedex.annotation.Setup;
import globaz.jade.sedex.message.SimpleSedexMessage;
import globaz.jade.smtp.JadeSmtpClient;
import java.util.Properties;

/**
 * @author dde ATTENTION : CETTE CLASS EST REFERENCE DANS LE FICHIER DE CONFIGURATION JadeSedexService.xml
 */
public class AnnoncesRapgRetourHandler {

    String[] emails;
    private String passSedex = "";
    private BSession session = null;
    private String userSedex = "";

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    public String[] getEmails() {
        return emails;
    }

    public String getPassSedex() {
        return passSedex;
    }

    public BSession getSession() {
        return session;
    }

    public String getUserSedex() {
        return userSedex;
    }

    public void setEmails(String[] emails) {
        this.emails = emails;
    }

    public void setPassSedex(String passSedex) {
        this.passSedex = passSedex;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    public void setUserSedex(String userSedex) {
        this.userSedex = userSedex;
    }

    @OnReceive
    public void onReceive(SimpleSedexMessage message) throws Exception {
        BSession session;
        try {
            if ((!JadeStringUtil.isEmpty(getUserSedex())) && (!JadeStringUtil.isEmpty(getPassSedex()))) {
                session = new BSession("APG");
                session.connect(getUserSedex(), getPassSedex());
                setSession(session);
            } else {
                throw new Exception("Utilisateur Sedex non défini (JadeSedexService)");
            }

            String[] attachements = new String[message.attachments.size()];
            int i = 0;
            for (String o : message.attachments.keySet()) {
                attachements[i] = o.toString();
                i++;
            }
            JadeSmtpClient.getInstance().sendMail(emails, "RAPG - Message", "Nouveau message du RAPG", attachements);
        } catch (Exception e) {
            JadeLogger.error(this, e);
            throw e;
        }

    }

    @Setup
    public void setup(Properties properties) throws Exception {
        try {
            String encryptedUser = properties.getProperty("userSedex");
            if (encryptedUser == null) {
                JadeLogger.error(this, "Réception message 2015/000501: user sedex non renseigné. ");
                throw new IllegalStateException("Réception message 2015/000501: user sedex non renseigné. ");
            }
            String decryptedUser = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedUser);

            String encryptedPass = properties.getProperty("passSedex");
            if (encryptedPass == null) {
                JadeLogger.error(this, "Réception message 2015/000501: mot de passe sedex non renseigné. ");
                throw new IllegalStateException("Réception message 2015/000501: mot de passe sedex non renseigné. ");
            }
            String decryptedPass = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedPass);

            setUserSedex(decryptedUser);
            setPassSedex(decryptedPass);

            String mails = GlobazServer.getCurrentSystem().getApplication("APG")
                    .getProperty("rapg.sedexRecipientGroup");
            emails = mails.split(",");
        } catch (Exception e) {
            JadeLogger.error(this, e);
            throw e;
        }
    }

}
