package globaz.cygnus.tests;

import globaz.cygnus.application.RFApplication;
import globaz.cygnus.process.RFAdaptationsJournalieresProcess;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import java.rmi.RemoteException;

/**
 * @author jje
 */
public class testAdaptationsJournalieres {

    /**
     * @param args
     */
    public static void main(String[] args) throws RemoteException, Exception {

        BISession session;
        session = GlobazSystem.getApplication(RFApplication.DEFAULT_APPLICATION_CYGNUS).newSession("jje", "glob4az");

        System.out.println("START ************************************************");

        // BITransaction transactionAdaptation = new BTransaction((BSession) session);
        // transactionAdaptation.openTransaction();

        RFAdaptationsJournalieresProcess adaptationProcess = new RFAdaptationsJournalieresProcess();
        adaptationProcess.setSession((BSession) session);
        // adaptationProcess.setTransaction(transactionAdaptation);
        adaptationProcess.setEMailAddress("jje@globaz.ch");
        adaptationProcess.setIdGestionnaire("jje");
        adaptationProcess.setSendCompletionMail(true);
        adaptationProcess.setSendMailOnError(true);

        adaptationProcess.start();

        System.out.println("STOP ************************************************");

        Thread.sleep(1000 * 60 * 60);

        System.exit(-1);

    }

}
