package globaz.osiris.db.ordres.sepa;

import globaz.globall.db.BApplication;
import globaz.globall.db.BSession;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;

public class SepaSendOrderProcessor extends AbstractSepa {
    public static final String NAMESPACE_PAIN001 = "http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd";

    private static final String SEPA_FTP_FOLDER = "sepa.ftp.folder";

    /** Connecte sur le ftp cible, dans le folder adapté à l'envoi de messages SEPA. */
    private FTPClient connect(BSession session) {
        // try fetching configuration from database
        String login = null;
        String password = null;
        String folder = null;
        String host = null;
        Integer port = null;

        try {
            BApplication app = session.getApplication();
            host = app.getProperty(SEPA_FTP_HOST);
            String sport = app.getProperty(SEPA_FTP_PORT);

            if (StringUtils.isNotBlank(sport)) {
                port = Integer.parseInt(sport);
            }

            login = app.getProperty(SEPA_FTP_USER);
            password = app.getProperty(SEPA_FTP_PASS);
            folder = app.getProperty(SEPA_FTP_FOLDER);
        } catch (Exception e) {
            throw new SepaException("unable to retrieve ftp config: " + e, e);
        }

        // go connect
        FTPClient client = connect(host, port, login, password);
        if (StringUtils.isNotBlank(folder)) {
            try {
                if (!client.changeWorkingDirectory(folder)) {
                    throw new SepaException("unable to move to directoy " + folder);
                }
            } catch (IOException e) {
                throw new SepaException("unable to move to directoy " + folder + ": " + e, e);
            }
        }

        return client;
    }

    public void sendOrdreGroupeByFtp(BSession session, InputStream is, String filename) {
        FTPClient client = null;
        try {
            client = connect(session);
            sendData(is, client, filename);
        } finally {
            disconnectQuietly(client);
        }
    }
}
