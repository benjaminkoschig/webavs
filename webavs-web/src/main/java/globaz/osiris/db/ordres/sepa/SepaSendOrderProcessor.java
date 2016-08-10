package globaz.osiris.db.ordres.sepa;

import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import globaz.globall.db.BApplication;
import globaz.globall.db.BSession;
import globaz.osiris.db.ordres.CAOrdreGroupe;

public class SepaSendOrderProcessor extends AbstractSepa {
    public static final String NAMESPACE_PAIN001 = "http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd";

    private static final String SEPA_FTP_FOLDER = "sepa.ftp.folder";

    private static final String STATUS_TRANSMIS = "STATUS_TRANSMIS";

    /** Connecte sur le ftp cible, dans le folder adapté à l'envoi de messages SEPA. */
    private FTPClient connect(BSession session) {
        // try fetching configuration from database
        String login = null;
        String password = null;
        String folder = null;
        String uri = null;

        try {
            BApplication app = session.getApplication();
            String host = app.getProperty(SEPA_FTP_HOST);
            String sport = app.getProperty(SEPA_FTP_PORT);
            Integer port = null;

            if (StringUtils.isNotBlank(sport)) {
                port = Integer.parseInt(sport);
            }

            login = app.getProperty(SEPA_FTP_USER);
            password = app.getProperty(SEPA_FTP_PASS);
            folder = app.getProperty(SEPA_FTP_FOLDER);
            uri = host + (port == null ? "" : ":" + port);
        } catch (Exception e) {
            throw new SepaException("unable to retrieve ftp config: " + e, e);
        }

        // go connect
        FTPClient client = connect(uri, login, password);
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

    public void sendOrdreGroupeByFtp(BSession session, CAOrdreGroupe ordreGroupe) {
        org.w3c.dom.Document xmlDoc = marshall(convertOrdreGroupe(ordreGroupe));

        FTPClient client = null;
        try {
            client = connect(session);
            sendData(toInputStream(xmlDoc), client,
                    /* FIXME faut-il un nom de fichier particulier? */ ordreGroupe.getId() + "_"
                            + System.currentTimeMillis() + ".xml");
            ordreGroupe.setEtat(STATUS_TRANSMIS);
        } finally {
            disconnectQuietly(client);
        }
    }

    private Object convertOrdreGroupe(CAOrdreGroupe ordreGroupe) {
        // TODO Auto-generated method stub
        return null;
    }

    public void sendOrdreGroupeByMail(CAOrdreGroupe ordreGroupe) {
        org.w3c.dom.Document xmlDoc = marshall(convertOrdreGroupe(ordreGroupe));
        throw new AssertionError("not implemented yet...");
        // TODO sendEmail();
    }
}
