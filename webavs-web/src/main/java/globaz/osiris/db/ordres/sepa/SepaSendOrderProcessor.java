package globaz.osiris.db.ordres.sepa;

import globaz.globall.db.BSession;
import java.io.InputStream;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.osiris.business.constantes.CAProperties;
import com.jcraft.jsch.ChannelSftp;

public class SepaSendOrderProcessor extends AbstractSepa {
    public static final String NAMESPACE_PAIN001 = "http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd";

    public void sendOrdreGroupeByFtp(BSession session, InputStream is, String filename) throws PropertiesException {
        ChannelSftp client = null;
        try {
            client = getClient();

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
