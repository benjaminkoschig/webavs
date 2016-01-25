package globaz.cygnus.process;

import globaz.cygnus.application.RFApplication;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.log.JadeLogger;

public class RFAdaptationsJournalieresRunnable implements Runnable {

    private String emailAdress = null;
    private String encryptedIdGestionnaire = null;
    private String encryptedPass = null;

    public String getEmailAdress() {
        return emailAdress;
    }

    public String getEncryptedIdGestionnaire() {
        return encryptedIdGestionnaire;
    }

    public String getEncryptedPass() {
        return encryptedPass;
    }

    @Override
    public void run() {

        try {

            String idGestionnaire = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(
                    getEncryptedIdGestionnaire());
            String pass = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(getEncryptedPass());

            BSession session = (BSession) GlobazSystem.getApplication(RFApplication.DEFAULT_APPLICATION_CYGNUS)
                    .newSession(idGestionnaire, pass);

            RFAdaptationsJournalieresProcess adaptationProcess = new RFAdaptationsJournalieresProcess();
            adaptationProcess.setSession(session);
            // adaptationProcess.setTransaction(transactionAdaptation);
            adaptationProcess.setEMailAddress(getEmailAdress());
            adaptationProcess.setIdGestionnaire(idGestionnaire);
            adaptationProcess.setSendCompletionMail(true);
            adaptationProcess.setSendMailOnError(true);

            BProcessLauncher.start(adaptationProcess, false);

        } catch (Exception e) {
            JadeLogger.error(this, e.toString());
            e.printStackTrace();
        }

    }

    public void setEmailAdress(String emailAdress) {
        this.emailAdress = emailAdress;
    }

    public void setEncryptedIdGestionnaire(String encryptedIdGestionnaire) {
        this.encryptedIdGestionnaire = encryptedIdGestionnaire;
    }

    public void setEncryptedPass(String encryptedPass) {
        this.encryptedPass = encryptedPass;
    }

}
