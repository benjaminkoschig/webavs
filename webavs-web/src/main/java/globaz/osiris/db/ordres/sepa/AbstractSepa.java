package globaz.osiris.db.ordres.sepa;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.crypto.JadeDefaultEncrypters;
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
    public static final int MAX_TRY = 2;

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
     * Essaye de se connecter au serveur SFTP sp?cifi? 1 ou plusieurs fois si une exception est remont?.
     *
     * @param user Utilisateur
     * @param password (optional)
     *            Mot de passe, si authentification par mot de passe.
     * @param keyFiles (optional)
     *            Cl? priv?e, si authentification par cl? priv?e.
     *
     * @throws SepaException en cas d'erreur de connexion au FTP.
     */
    protected ChannelSftp connectWithRetry(String server, Integer port, String user, String password, HashMap<String, Boolean> keyFiles, String keyPassphrase, String knownHosts) {
        // On essaye de se connecter jusqu'? que le nombre de retry maximum soit atteint
        for (int i = 0; i < MAX_TRY; i++) {
            try {
                return connect(server, port, user, password, keyFiles, keyPassphrase, knownHosts);
            } catch (SepaException e) {
                // On remonte l'exception seulement si la limite de retry est atteinte.
                if (i == MAX_TRY - 1) {
                    throw e;
                }
            }
        }

        return null;
    }

    /**
     * Connecte au serveur SFTP sp?cifi?, et retourne la canal utilis? pour transmettre les informations. Il faut
     * sp?cifier soit le mot de passe, soit le chemin du fichier de cl?.
     * 
     * @param user Utilisateur
     * @param password (optional)
     *            Mot de passe, si authentification par mot de passe.
     * @param keyFiles (optional)
     *            Cl? priv?e, si authentification par cl? priv?e.
     * 
     * @throws SepaException en cas d'erreur de connexion au FTP.
     */
    protected ChannelSftp connect(String server, Integer port, String user, String password, HashMap<String, Boolean> keyFiles, String keyPassphrase, String knownHosts) {
        Validate.notEmpty(server, "server was not specified");
        Validate.notEmpty(user, "you must specify a user, even when using a private key file");
        Validate.isTrue((StringUtils.isBlank(password) || keyFiles.isEmpty())
                && (StringUtils.isNotBlank(password) || !keyFiles.isEmpty()),
                "you must specify a password or a key file, but not both");

        for (Map.Entry<String, Boolean> keyFile : keyFiles.entrySet()) {
            if (StringUtils.isNotBlank(keyFile.getKey())) {
                Validate.isTrue(new File(keyFile.getKey()).isFile(), "keyFile does not exist");
            }
        }

        final JSch jsch = new JSch();
        Session session;

        try {
            for (Map.Entry<String, Boolean> keyFile : keyFiles.entrySet()) {
                if (StringUtils.isNotBlank(keyFile.getKey())) {
                    try {
                        if (StringUtils.isNotBlank(keyPassphrase) && keyFile.getValue()) {
                            jsch.addIdentity(keyFile.getKey(), keyPassphrase);
                            LOG.info("Passphrase set to the key file: {}", keyFile);
                        } else {
                            jsch.addIdentity(keyFile.getKey());
                        }
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

            if (StringUtils.isNotBlank(knownHosts)) {
                 jsch.setKnownHosts(knownHosts);
            } else {
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
            }
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
     * @param pathname Une arborescence de chemin termin?s par un nom de fichiers, relatifs par rapport ? la racine.
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

    public void disconnectQuietly() {
        if (client != null) {
            client.disconnect();
        }
    }

    protected HashMap<String, Boolean> loadPrivateKeyPathFromJadeConfigFile() {
        /*** try fetching a private ssh key from legacy config files, mimic Jade behavior */
        final InputStream jadeFsServerConfig = getClass().getResourceAsStream(LEGACY_JADE_CONFIG_FILE);

        if (jadeFsServerConfig == null) {
            LOG.info("file {} not found. skipping retrieval of private ssh key for connecting to SFTP",
                    LEGACY_JADE_CONFIG_FILE);
            return new HashMap<String, Boolean>();
        }

        final HashMap<String, Boolean> privateKeyList = new HashMap<String, Boolean>();

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
                            Boolean hasPassphrase = Boolean.parseBoolean(((Element) n).getAttribute("passphrase"));
                            LOG.info("resolved private ssh key to be {}", privateKey);
                            privateKeyList.put(privateKey, hasPassphrase);
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

    /** Connecte sur le ftp cible, dans le folder adapt? ? l'envoi de messages SEPA. */
    protected ChannelSftp getClient(boolean isRetry) {
        if (client == null) {
            final HashMap<String, Boolean> privateKeyList = loadPrivateKeyPathFromJadeConfigFile();

            // try fetching configuration from database
            String login = null;
            String password = null;
            Integer port = null;
            String host = null;
            String keyPassphrase = null;
            String knownHosts = null;
            try {
                host = getValueProperties(getHost());
                String sport = getValueProperties(getPort());

                if (StringUtils.isNotBlank(sport)) {
                    port = Integer.parseInt(sport);
                }

                login = getValueProperties(getUser());
                password = getValueProperties(getPassword());
                keyPassphrase = getValuePropertiesEncrypted(getKeyPassphrase());
                knownHosts = getValueProperties(getKnownHosts());

            } catch (Exception e) {
                throw new SepaException("unable to retrieve ftp config: " + e, e);
            }

            // go connect
            if (isRetry) {
                client = connectWithRetry(host, port, login, password, privateKeyList, keyPassphrase, knownHosts);
            } else {
                client = connect(host, port, login, password, privateKeyList, keyPassphrase, knownHosts);
            }

        }
        // go connect
        return client;
    }

    private String getValuePropertiesEncrypted(final CAProperties property) throws Exception {
        String encryptedProperty = getValueProperties(property);
        if (JadeStringUtil.isEmpty(encryptedProperty)) {
            return "";
        }

        return JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(encryptedProperty);
    }

    private String getValueProperties(final CAProperties property) throws PropertiesException {
        if (property == null) {
            return "";
        }

        return property.getValue();
    }

    /**
     * Le fils doit d?finir le host
     * 
     * @return La propri?t? HOST
     */
    protected abstract CAProperties getHost();

    /**
     * Le fils doit d?finir le port
     * 
     * @return La propri?t? PORT
     */
    protected abstract CAProperties getPort();

    /**
     * Le fils doit d?finir le user
     * 
     * @return La propri?t? USER
     */
    protected abstract CAProperties getUser();

    /**
     * Le fils peut d?finir le mot de passe
     * 
     * @return La propri?t? PASSWORD
     */
    protected abstract CAProperties getPassword();

    /**
     * Le fils peut d?finir la passphrase du fichier
     *
     * @return La propri?t? KEY_PASSPHRASE
     */
    protected CAProperties getKeyPassphrase() {
       return null;
    }

    /**
     * Le fils peut d?finir le fichier known hosts
     *
     * @return La propri?t? KNOWN_HOSTS
     */
    protected CAProperties getKnownHosts() {
       return null;
    }

}
