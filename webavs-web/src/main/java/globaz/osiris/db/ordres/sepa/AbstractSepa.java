package globaz.osiris.db.ordres.sepa;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ch.globaz.osiris.business.constantes.CAProperties;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public abstract class AbstractSepa {
    /** Where to look to find a ssh private key (legacy file from Jade) */
    public static final String LEGACY_JADE_CONFIG_FILE = "/JadeFsServer.xml";
    public static final String PROTOCOL_NAME = "JadeFsServiceSftp";
    public static final String PRIVATEKEY_NODENAME_PREFIX = "private.key.";

    public static/* final */class SepaException extends RuntimeException {
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

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSepa.class);
    ChannelSftp client = null;

    // XML ---------------------------------------------------------

    protected InputStream toInputStream(Document doc) {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            Source xmlSource = new DOMSource(doc);
            Result outputTarget = new StreamResult(outputStream);
            TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (TransformerConfigurationException e) {
            throw new SepaException(e);
        } catch (TransformerException e) {
            throw new SepaException(e);
        } catch (TransformerFactoryConfigurationError e) {
            throw new SepaException(e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    public static Document parseDocument(InputStream source) {
        Document doc;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            doc = documentBuilder.parse(source);
        } catch (ParserConfigurationException e) {
            throw new SepaException("XML Parser error: " + e, e);
        } catch (SAXException e) {
            throw new SepaException("XML Error: " + e, e);
        } catch (IOException e) {
            throw new SepaException("IO error trying to parse source stream: " + e, e);
        }
        return doc;
    }

    // how to convert xml document/node/elements into java objects
    public static <T> T unmarshall(Document doc, Class<? extends T> clazz) {
        try {
            JAXBContext jc = JAXBContext.newInstance(clazz);

            Unmarshaller unmarshaller = jc.createUnmarshaller();
            unmarshaller.setEventHandler(new DefaultValidationEventHandler());

            return unmarshaller.unmarshal(doc, clazz).getValue();
        } catch (JAXBException e) {
            throw new SepaException("unable to convert xml document to java object of class " + clazz.getName() + ": "
                    + e, e);
        }
    }

    // how to convert xml document/node/elements into java objects
    protected Document marshall(Object input) {
        try {
            JAXBContext jc = JAXBContext.newInstance(input.getClass());

            Marshaller marshaller = jc.createMarshaller();
            marshaller.setEventHandler(new DefaultValidationEventHandler());

            DOMResult result = new DOMResult();
            marshaller.marshal(input, result);
            return result.getNode().getOwnerDocument();
        } catch (JAXBException e) {
            throw new SepaException("unable to convert java object of class " + input.getClass().getName()
                    + " to an xml document: " + e, e);
        }
    }

    // FTP -------------------------------------------

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
        JSch jsch = new JSch();

        Session session;
        try {
            // if specified, set an identity (yet a private ssh key file)
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

            // if specified, set the password
            if (StringUtils.isNotBlank(password)) {
                session.setPassword(password);
            }

            // XXX FIXME CRITICAL SECURITY BREACH!!!
            // as per original code in globaz.jade.fs.service.jsch.JadeFsService#~576, we need to explicitly
            // disable strict host key checks, making us vulnerable to man-in-the-middle attacks (ref.
            // http://stackoverflow.com/questions/30178936/jsch-sftp-security-with-session-setconfigstricthostkeychecking-no).
            // And since we are transferring financial informations over the public internet (to PostFinance...), this
            // is terribly embarrassing and should be fixed ASAP: it is blocker, should prevent any deployment to
            // production and send shame on us. But as ever, nobody really cares...
            session.setConfig("StrictHostKeyChecking", "no");

            // pfiou we are connected, pop the champaign
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
            List<String> result = new ArrayList<String>();
            List<LsEntry> found = new ArrayList<LsEntry>(client.ls(foldername));
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
        // try fetching a private ssh key from legacy config files, mimic Jade behavior
        InputStream jadeFsServerConfig = getClass().getResourceAsStream(LEGACY_JADE_CONFIG_FILE);

        if (jadeFsServerConfig == null) {
            LOG.info("file {} not found. skipping retrieval of private ssh key for connecting to SFTP",
                    LEGACY_JADE_CONFIG_FILE);
            return null;
        }
        List<String> privateKeyList = new ArrayList<String>();

        Document doc = parseDocument(jadeFsServerConfig);
        NodeList allProtocols = doc.getDocumentElement().getElementsByTagName("protocols");

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
            List<String> privateKeyList = loadPrivateKeyPathFromJadeConfigFile();

            // try fetching configuration from database
            String login = null;
            String password = null;
            Integer port = null;
            String host = null;
            try {
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
            client = connect(host, port, login, password, privateKeyList);

        }
        // go connect
        return client;
    }
}
