package globaz.hermes.test;

import globaz.framework.security.FWSecurityLoginException;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.hermes.application.HEApplication;
import globaz.hermes.utils.DateUtils;
import globaz.jade.smtp.JadeSmtpClient;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * @author ado 19 févr. 04
 */
public class HECheckUp {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(DateUtils.getTimeStamp() + "Error : Wrong number of arguments");
            System.out.println(DateUtils.getTimeStamp() + "java globaz.hermes.test.HECheckUp <uid> <pwd>");
            System.exit(200);
        }
        System.out.println("**********  HECheckup excuting with user:" + args[0] + " password:" + args[1]
                + "  **********");
        new HECheckUp(args[0], args[1]).go();
        System.exit(0);
    }

    BSession session = null;

    public HECheckUp(String uid, String pwd) {
        super();
        try {
            session = createSession(uid, pwd);
            display("SUCCESS IN SESSION CREATION");
        } catch (Exception e) {
            display("ERROR IN SESSION CREATION");
            e.printStackTrace();
        }
    }

    /**
     * Method createSession.
     * 
     * @return BSession
     */
    private BSession createSession(String user, String pwd) throws Exception, FWSecurityLoginException {
        display("ATTEMPTING TO CREATE SESSION");
        BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("HERMES").newSession(user, pwd);
        // session.connect(user, pwd);
        return session;
    }

    /**
     * Method display.
     * 
     * @param string
     */
    private void display(String msg) {
        System.out.println("**********  " + msg + "  **********");
    }

    /**
     * Method go.
     */
    private void go() {
        // Session
        try {
            testQuery(session);
            display("SUCCESS IN QUERY");
        } catch (Exception e) {
            e.printStackTrace();
            display("ERROR IN QUERY");
        }
        try {
            testMail(session);
            display("SUCCESS IN MAIL SENDING");
        } catch (Exception e) {
            display("ERROR IN MAIL SENDING");
            e.printStackTrace();
        }
        try {
            testEncoding();
            display("SUCCESS IN ENCODING TEST");
        } catch (Exception e) {
            display("ERROR IN ENCODING TEST");
            e.printStackTrace();
        }
        /* TODO reactivate
        try {
            testFTPConnect();
            display("SUCCESS IN FTP CONNECT TEST");
        } catch (Exception e) {
            display("ERROR IN FTP CONNECT TEST");
            e.printStackTrace();
        }*/
    }

    /**
     * Method testEncoding.
     */
    private void testEncoding() throws FileNotFoundException, UnsupportedEncodingException {
        display("ATTEMPTING TO CREATE ENCODED WRITER");
        new OutputStreamWriter(new FileOutputStream("test.txt"), "Cp037");
    }

    /**
	 * 
	 */
    private void testFTPConnect() throws Exception {
        display("ATTEMPTING TO FTP CONNECT ");
        String login = session.getApplication().getProperty("ftp.login");
        String password = session.getApplication().getProperty("ftp.password");
        String host = session.getApplication().getProperty("ftp.host");
        /*FIXME bad class usage FtpClient client = new FtpClient();
        client.openServer(host);
        client.login(login, password);
        client.closeServer();*/
    }

    /**
     * Method testMail.
     */
    private void testMail(BSession session) throws Exception {
        display("ATTEMPTING TO SEND MAIL");
        // HEApplication heApp = (HEApplication) session.getApplication();
        // Transmettre le log par email sans pièces jointes
        String email = session.getApplication().getProperty("zas.user.email");
        System.out.println("An email will be sent to " + email);
        JadeSmtpClient.getInstance().sendMail(email, "TEST EMAIL", "email send test ok !", null);
    }

    /**
     * Method testQuery.
     * 
     * @param session
     */
    private void testQuery(BSession session) throws Exception {
        display("ATTEMPTING TO ACCESS DB");
        // Requête
        if ((((HEApplication) session.getApplication()).getCsPaysListe(session)).size() > 0) {
            System.out.println("OK !");
        } else {
            throw new Exception("ERREUR ! ");
        }
    }
}
