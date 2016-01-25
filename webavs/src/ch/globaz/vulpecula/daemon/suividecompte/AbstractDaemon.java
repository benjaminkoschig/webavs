package ch.globaz.vulpecula.daemon.suividecompte;

import globaz.jade.crypto.JadeDecryptionNotSupportedException;
import globaz.jade.crypto.JadeEncrypterNotFoundException;
import globaz.jade.smtp.JadeSmtpClient;

public abstract class AbstractDaemon implements Runnable {

    // username et password avec lesquels on ouvre une session
    private String username;
    private String password;

    public final void setPassword(String password) throws JadeDecryptionNotSupportedException,
            JadeEncrypterNotFoundException, Exception {

        if (password == null) {
            throw new IllegalStateException("user non renseigné. ");
        }
        // this.password = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(passSedex);
        this.password = password;
    }

    public final void setUsername(String username) throws JadeDecryptionNotSupportedException,
            JadeEncrypterNotFoundException, Exception {

        if (username == null) {
            throw new IllegalStateException("password non renseigné. ");
        }
        // this.username = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(userSedex);
        this.username = username;
    }

    @Override
    public void run() {
    }

    protected String getUsername() {
        return username;
    }

    protected String getPassword() {
        return password;
    }

    protected void sendDebugMail(String address, String body) {
        String subject = "debugmail";
        try {
            JadeSmtpClient.getInstance().sendMail(address, subject, body, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
