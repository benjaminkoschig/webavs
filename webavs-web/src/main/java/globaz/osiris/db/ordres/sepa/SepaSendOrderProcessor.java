package globaz.osiris.db.ordres.sepa;

import globaz.globall.db.BApplication;
import globaz.globall.db.BSession;
import java.io.InputStream;
import org.apache.commons.lang.StringUtils;
import com.jcraft.jsch.ChannelSftp;

public class SepaSendOrderProcessor extends AbstractSepa {
    public static final String NAMESPACE_PAIN001 = "http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd";

    private static final String SEPA_FTP_FOLDER = "sepa.ftp.folder";

    /** Connecte sur le ftp cible, dans le folder adapté à l'envoi de messages SEPA. */
    private ChannelSftp connect(BSession session) {
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
        ChannelSftp client = connect(host, port, login, password);

        return client;
    }

    public void sendOrdreGroupeByFtp(BSession session, InputStream is, String filename) {
        ChannelSftp client = null;
        try {
            client = connect(session);
            sendData(is, client, filename);
        } finally {
            disconnectQuietly(client);
        }
    }
}
