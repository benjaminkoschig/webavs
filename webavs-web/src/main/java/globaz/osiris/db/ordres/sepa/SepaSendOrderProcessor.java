package globaz.osiris.db.ordres.sepa;

import globaz.globall.db.BApplication;
import globaz.globall.db.BSession;
import java.io.InputStream;
import org.apache.commons.lang.StringUtils;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.osiris.business.constantes.CAProperties;
import com.jcraft.jsch.ChannelSftp;

public class SepaSendOrderProcessor extends AbstractSepa {
    public static final String NAMESPACE_PAIN001 = "http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd";

    /** Connecte sur le ftp cible, dans le folder adapté à l'envoi de messages SEPA. */
    private ChannelSftp connect(BSession session) {
        String privateKey = loadPrivateKeyPathFromJadeConfigFile();

        // try fetching configuration from database
        String login = null;
        String password = null;

        String host = null;
        Integer port = null;

        try {
            BApplication app = session.getApplication();

            // FIXME lookuper ces propriétés dans la DB
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
        ChannelSftp client = connect(host, port, login, password, privateKey);

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
