package globaz.osiris.db.ordres.sepa;

import java.io.InputStream;
import org.apache.commons.lang.StringUtils;
import com.jcraft.jsch.ChannelSftp;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.osiris.business.constantes.CAProperties;
import globaz.globall.db.BApplication;
import globaz.globall.db.BSession;

public class SepaSendOrderProcessor extends AbstractSepa {
    public static final String NAMESPACE_PAIN001 = "http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd";

    /** Connecte sur le ftp cible, dans le folder adapt� � l'envoi de messages SEPA. */
    private ChannelSftp connect(BSession session) {
        // try fetching configuration from database
        String login = null;
        String password = null;

        String host = null;
        Integer port = null;

        try {
            BApplication app = session.getApplication();
            host = CAProperties.ISO_SEPA_FTP_HOST.getValue();
            String sport = CAProperties.ISO_SEPA_FTP_PORT.getValue();

            if (StringUtils.isNotBlank(sport)) {
                port = Integer.parseInt(sport);
            }

            login = CAProperties.ISO_SEPA_FTP_USER.getValue();
            password = CAProperties.ISO_SEPA_FTP_PASS.getValue();

        } catch (Exception e) {
            throw new SepaException("unable to retrieve ftp config: " + e, e);
        }

        // go connect
        ChannelSftp client = connect(host, port, login, password, null);

        return client;
    }

    public void sendOrdreGroupeByFtp(BSession session, InputStream is, String filename) throws PropertiesException {
        ChannelSftp client = null;
        try {
            client = connect(session);

            // prefixpath
            String folder = CAProperties.ISO_SEPA_FTP_001_FOLDER.getValue();
            // make a zip?
            if (CAProperties.ISO_SEPA_FTP_HOST.getValue().startsWith("isotest")) {
                // zip. prefix filename by ???
            }
            sendData(is, client, folder + filename);
        } finally {
            disconnectQuietly(client);
        }
    }
}
