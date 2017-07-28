package globaz.osiris.db.ordres.sepa;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.osiris.business.constantes.CAProperties;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public abstract class AbstractSepa implements Serializable {
    private static final long serialVersionUID = 187651144454673616L;

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSepa.class);

    public static final String LEGACY_JADE_CONFIG_FILE = "/JadeFsServer.xml";
    public static final String PROTOCOL_NAME = "JadeFsServiceSftp";
    public static final String PRIVATEKEY_NODENAME_PREFIX = "private.key.";

    public static class SepaException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public SepaException() {
            super();
        }

        public SepaException(String message, Throwable cause) {
            super(message, cause);
        }

        public SepaException(String message) {
            super(message);
        }

        public SepaException(Throwable cause) {
            super(cause);
        }
    }

    private ChannelSftp client = null;

    /**
     * Connecte au serveur SFTP spécifié, et retourne la canal utilisé pour transmettre les informations. Il faut
     * spécifier soit le mot de passe, soit le chemin du fichier de clé.
     * 
     * @param user Utilisateur
     * @param password (optional)
     *            Mot de passe, si authentification par mot de passe.
     * @param keyFiles (optional)
     *            Clé privée, si authentification par clé privée.
     * 
     * @throws SepaException en cas d'erreur de connexion au FTP.
     */
    protected ChannelSftp connect(String server, Integer port, String user, String password, List<String> keyFiles) {
        Validate.notEmpty(server, "server was not specified");
        Validate.notEmpty(user, "you must specify a user, even when using a private key file");
        Validate.isTrue((StringUtils.isBlank(password) || keyFiles.isEmpty())
                && (StringUtils.isNotBlank(password) || !keyFiles.isEmpty()),
                "you must specify a password or a key file, but not both");

        for (String keyFile : keyFiles) {
            if (StringUtils.isNotBlank(keyFile)) {
                Validate.isTrue(new File(keyFile).isFile(), "keyFile does not exist");
            }
        }

        final JSch jsch = new JSch();
        Session session;

        try {
            for (String keyFile : keyFiles) {
                if (StringUtils.isNotBlank(keyFile)) {
                    try {
                        jsch.addIdentity(keyFile);
                        LOG.info("Private key file added: {}", keyFile);
                    } catch (JSchException e) {
                        LOG.warn("Private key file not added ({}): {}", e.toString(), keyFile);
                        LOG.error("getJsch() catch an unrethrowed exception from JSch API", e);
                    }
                }
            }

            session = port == null ? jsch.getSession(user, server) : jsch.getSession(user, server, port);

            if (StringUtils.isNotBlank(password)) {
                session.setPassword(password);
            }

            /**
             * FIXME CRITICAL SECURITY BREACH!!!
             * as per original code in globaz.jade.fs.service.jsch.JadeFsService#~576, we need to explicitly
             * disable strict host key checks, making us vulnerable to man-in-the-middle attacks (ref.
             * http://stackoverflow.com/questions/30178936/jsch-sftp-security-with-session-
             * setconfigstricthostkeychecking-no).
             * And since we are transferring financial informations over the public internet (to PostFinance...), this
             * is terribly embarrassing and should be fixed ASAP: it is blocker, should prevent any deployment to
             * production and send shame on us. But as ever, nobody really cares...
             */
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            LOG.info("successfully connected to {}", server);

            Channel channel = session.openChannel("sftp");
            channel.connect();

            return (ChannelSftp) channel;
        } catch (JSchException e) {
            throw new SepaException("unable to connect to ftp: " + e, e);
        }
    }

    /**
     * @param pathname Une arborescence de chemin terminés par un nom de fichiers, relatifs par rapport à la racine.
     */
    protected void sendData(InputStream source, ChannelSftp client, String pathname) {
        try {
            client.put(source, pathname);
        } catch (SftpException e) {
            throw new SepaException("unable to store file in ftp: " + e, e);
        }
    }

    protected void renameFile(ChannelSftp client, String oldFileName, String newFileName) {
        try {
            client.rename(oldFileName, newFileName);
        } catch (SftpException e) {
            throw new SepaException("unable to rename the file " + oldFileName + " to " + newFileName + ": " + e, e);
        }
    }

    protected void deleteFile(ChannelSftp client, String fileName) {
        try {
            client.rm(fileName);
        } catch (SftpException e) {
            throw new SepaException("unable to delete the file " + fileName + ": " + e, e);
        }
    }

    protected String[] listFiles(ChannelSftp client, String foldername) {
        try {
            final List<String> result = new ArrayList<String>();
            final List<LsEntry> found = new ArrayList<LsEntry>(client.ls(foldername));

            for (LsEntry entry : found) {
                result.add(entry.getFilename());
            }

            return result.toArray(new String[result.size()]);
        } catch (SftpException e) {
            throw new SepaException("unable to list files from ftp: " + e, e);
        }
    }

    protected void retrieveData(ChannelSftp client, String filename, OutputStream target) {
        try {
            client.get(filename, target);
        } catch (SftpException e) {
            throw new SepaException("unable to get file from ftp: " + e, e);
        }
    }

    protected void disconnectQuietly(ChannelSftp ftp) {
        if (ftp != null) {
            ftp.disconnect();
        }
    }

    protected List<String> loadPrivateKeyPathFromJadeConfigFile() {
        /*** try fetching a private ssh key from legacy config files, mimic Jade behavior */
        final InputStream jadeFsServerConfig = getClass().getResourceAsStream(LEGACY_JADE_CONFIG_FILE);

        if (jadeFsServerConfig == null) {
            LOG.info("file {} not found. skipping retrieval of private ssh key for connecting to SFTP",
                    LEGACY_JADE_CONFIG_FILE);
            return new ArrayList<String>();
        }

        final List<String> privateKeyList = new ArrayList<String>();

        final Document doc = CAJaxbUtil.parseDocument(jadeFsServerConfig);
        final NodeList allProtocols = doc.getDocumentElement().getElementsByTagName("protocols");

        for (int i = 0; i < allProtocols.getLength(); i++) {
            Element protocols = (Element) allProtocols.item(i);
            NodeList allProtocol = protocols.getElementsByTagName("protocol");

            for (int j = 0; j < allProtocol.getLength(); j++) {
                Element protocol = (Element) allProtocol.item(j);

                if (PROTOCOL_NAME.equals(protocol.getAttribute("name"))) {
                    NodeList protocolChildren = protocol.getChildNodes();

                    for (int k = 0; k < protocolChildren.getLength(); k++) {
                        Node n = protocolChildren.item(k);

                        if (n instanceof Element && n.getNodeName().startsWith(PRIVATEKEY_NODENAME_PREFIX)) {
                            String privateKey = StringUtils.trimToNull(n.getTextContent());
                            LOG.info("resolved private ssh key to be {}", privateKey);
                            privateKeyList.add(privateKey);
                        }
                    }
                }

                if (!privateKeyList.isEmpty()) {
                    break;
                }
            }

            if (!privateKeyList.isEmpty()) {
                break;
            }
        }

        return privateKeyList;
    }

    /** Connecte sur le ftp cible, dans le folder adapté à l'envoi de messages SEPA. */
    protected ChannelSftp getClient() {
        if (client == null) {
            final List<String> privateKeyList = loadPrivateKeyPathFromJadeConfigFile();

            // try fetching configuration from database
            String login = null;
            String password = null;
            Integer port = null;
            String host = null;
            try {
                host = getValueProperties(getHost());
                String sport = getValueProperties(getPort());

                if (StringUtils.isNotBlank(sport)) {
                    port = Integer.parseInt(sport);
                }

                login = getValueProperties(getUser());
                password = getValueProperties(getPassword());

            } catch (Exception e) {
                throw new SepaException("unable to retrieve ftp config: " + e, e);
            }

            // go connect
            client = connect(host, port, login, password, privateKeyList);

        }
        // go connect
        return client;
    }

    private String getValueProperties(final CAProperties property) throws PropertiesException {
        if (property == null) {
            return "";
        }

        return property.getValue();
    }

    /**
     * Le fils doit définir le host
     * 
     * @return La propriété HOST
     */
    protected abstract CAProperties getHost();

    /**
     * Le fils doit définir le port
     * 
     * @return La propriété PORT
     */
    protected abstract CAProperties getPort();

    /**
     * Le fils doit définir le user
     * 
     * @return La propriété USER
     */
    protected abstract CAProperties getUser();

    /**
     * Le fils peut définir le mot de passe
     * 
     * @return La propriété PASSWORD
     */
    protected abstract CAProperties getPassword();
}
