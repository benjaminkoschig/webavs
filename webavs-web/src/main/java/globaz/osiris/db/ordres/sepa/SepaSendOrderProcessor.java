package globaz.osiris.db.ordres.sepa;

import java.io.InputStream;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.osiris.business.constantes.CAProperties;
import com.jcraft.jsch.ChannelSftp;

public class SepaSendOrderProcessor extends AbstractSepa {
    private static final long serialVersionUID = 8134276795047494813L;

    public void sendOrdreGroupeByFtp(InputStream is, String filename, CAProperties propertiesFolder)
            throws PropertiesException {
        ChannelSftp client = null;
        try {
            client = getClient(false);

            sendData(is, client, propertiesFolder.getValue() + filename);
        } finally {
            disconnectQuietly(client);
        }
    }

    @Override
    protected CAProperties getHost() {
        return CAProperties.ISO_SEPA_FTP_HOST;
    }

    @Override
    protected CAProperties getPort() {
        return CAProperties.ISO_SEPA_FTP_PORT;
    }

    @Override
    protected CAProperties getUser() {
        return CAProperties.ISO_SEPA_FTP_USER;
    }

    @Override
    protected CAProperties getPassword() {
        return CAProperties.ISO_SEPA_FTP_PASS;
    }
}
