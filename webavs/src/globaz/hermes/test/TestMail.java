package globaz.hermes.test;

import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.jade.smtp.JadeSmtpClient;

/**
 * @author ado 28 nov. 03
 */
public class TestMail {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.out.println("Usage globaz.hermes.test.TestMail <uid> <pwd> <email>");
            System.exit(-1);
        }
        new TestMail(args[0], args[1]).go(args[2]);
        System.exit(-1);
    }

    private BSession bSession;

    /**
     * Constructor for TestMail.
     */
    public TestMail(String uid, String pwd) throws Exception {
        bSession = new BSession("HERMES");
        bSession = (BSession) GlobazServer.getCurrentSystem().getApplication("HERMES").newSession(uid, pwd);
    }

    /**
     * Returns the bSession.
     * 
     * @return BSession
     */
    public BSession getSession() {
        return bSession;
    }

    /**
     * Method go.
     * 
     * @param string
     */
    private void go(String eMail) throws Exception {
        // Transmettre le log par email sans pièces jointes
        JadeSmtpClient.getInstance().sendMail("ado@globaz.ch", eMail, "TEST EMAIL",
                "Merci de faire un reply si tu reçois ce mail", null);
    }

    /**
     * Sets the bSession.
     * 
     * @param bSession
     *            The bSession to set
     */
    public void setSession(BSession bSession) {
        this.bSession = bSession;
    }
}
