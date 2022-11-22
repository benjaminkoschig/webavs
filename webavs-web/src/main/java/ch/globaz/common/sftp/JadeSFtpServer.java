package ch.globaz.common.sftp;

import ch.globaz.common.sftp.exception.SFtpConnectionException;
import ch.globaz.common.sftp.exception.SFtpOperationException;
import ch.globaz.common.sftp.exception.SFtpPermissionException;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import globaz.jade.common.Jade;
import globaz.jade.crypto.JadeDefaultEncrypters;
import globaz.jade.log.JadeLogger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.xerces.dom.ElementImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

public class JadeSFtpServer {
    private static final Logger LOG = LoggerFactory.getLogger(JadeSFtpServer.class);

    public static final String LEGACY_JADE_FTP_CONFIG_XML = "/JadeSFtpServer.xml";

    private String protocol; //FTP|SFTP
    private String host;
    private Integer port;
    private String knownHosts;
    private String user;
    private String password;
    private Map<String, String> certificats;

    private ChannelSftp client;

    public static JadeSFtpServer client(String server) {
        return new JadeSFtpServer(server);
    }

    public static JadeSFtpServer autoConnect(String server) {
        JadeSFtpServer ftpServer = new JadeSFtpServer(server);
        ftpServer.connect();
        return ftpServer;
    }

    public static JadeSFtpServer autoConnect(String server, Integer tryConnection) {
        JadeSFtpServer ftpServer = new JadeSFtpServer(server);
        int i = 0;

        do {
            try {
                ftpServer.connect();
            } catch (Exception e) {
                LOG.warn("JadeFtpServer#autoConnect : Failed to connect for try " + i + 1, e);
            }
        } while (!ftpServer.isConnected() && ++i < tryConnection);

        return ftpServer;
    }

    private JadeSFtpServer(String server) {
        loadingConfiguration(server);
    }

    public void connect() {
        try {

            if (!StringUtils.isEmpty(password) && certificats != null && !certificats.isEmpty()) {
                connect(user, password, certificats);
            } else if (!StringUtils.isEmpty(password)) {
                connect(user, password);
            } else {
                connect(user, certificats);
            }
        } catch (Exception e) {
            LOG.error("", e);
            throw new SFtpConnectionException(e);
        }
    }

    public void connect(String user, String password) {
        try {
            connection(user, password);
        } catch (Exception e) {
            LOG.error("", e);
            throw new SFtpConnectionException(e);
        }
    }

    public void connect(String user, Map<String, String> certificats) {
        try {
            connection(user, certificats);
        } catch (Exception e) {
            LOG.error("", e);
            throw new SFtpConnectionException(e);
        }
    }

    public void connect(String user, String password, Map<String, String> certificats) {
        try {
            connection(user, password, certificats);
        } catch (Exception e) {
            LOG.error("", e);
            throw new SFtpConnectionException(e);
        }
    }

    public void disconnect() {
        if (Objects.nonNull(client) && client.isConnected()) {
            client.disconnect();
        }
    }

    public void send(InputStream file, String path) {
        try {
            client.put(file, normalizePath(path));
        } catch (SftpException e) {
            LOG.error("", e);
            throw new SFtpOperationException("JadeFtpServer#send : Unable to store file in " + protocol.toUpperCase() + " : " + e, e);
        }
    }

    public void send(Path file, String sftpPath) {
        send(file.toFile(), sftpPath);
    }

    public void send(File file, String path) {
        try {
            createFolder(path);
            client.put(Files.newInputStream(file.toPath()), normalizePath(path) + file.getName());
        } catch (Exception e) {
            LOG.error("", e);
            throw new SFtpOperationException("JadeFtpServer#send : Unable to store file in " + path + " : ", e);
        }
    }

    public void rename(String oldFileName, String newFileName) {
        try {
            client.rename(normalizePath(oldFileName), normalizePath(newFileName));
        } catch (SftpException e) {
            LOG.error("", e);
            throw new SFtpOperationException("JadeFtpServer#rename : Unable to rename the file " + oldFileName + " to " + newFileName + ": ", e);
        }
    }

    public File retrieve(String path, String fileName, String outputPath) {
        try {
            File fileRetreave = Paths.get(outputPath + File.separator + fileName).toFile();
            FileUtils.copyInputStreamToFile(client.get(normalizePath(path) + fileName), fileRetreave);

            return fileRetreave;
        } catch (Exception e) {
            LOG.error("", e);
            throw new SFtpOperationException("JadeFtpServer#retrieve : Unable to get file from ftp: ", e);
        }
    }

    public void delete(String path) {
        try {
            client.rm(normalizePath(path));
        } catch (SftpException e) {
            LOG.error("", e);
            throw new SFtpOperationException("JadeFtpServer#delete : Unable to delete the file " + path + ": ", e);
        }
    }

    public boolean exist(String path) {
        Vector res = null;
        try {
            res = client.ls(normalizePath(path));
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                return false;
            }
            LOG.error("JadeFtpServer#exist : Unexpected exception during ls files on sftp: [{}:{}]", e.id, e.getMessage());
        }
        return res != null && !res.isEmpty();
    }

    public void createFolder(String path) {
        String normalizePath = normalizePath(path);
        if (!existFolder(normalizePath)) {
            String[] folders = normalizePath.split("/");
            StringBuilder sftpPath = new StringBuilder("");
            Arrays.stream(folders).forEach(folder -> {
                sftpPath.append("/").append(folder);
                if (!existFolder(sftpPath.toString())) {
                    try {
                        client.mkdir(sftpPath.toString());
                    } catch (SftpException e) {
                        LOG.error("", e);
                        throw new SFtpOperationException("JadeFtpServer#createFolder : folder can't be creating!", e);
                    }
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    public void deleteFolder(String path) {
        String normalizePath = normalizePath(path);
        try {
            if (existFolder(normalizePath)) {
                // List source directory structure.
                Vector<LsEntry> fileAndFolderList = client.ls(normalizePath);

                // Iterate objects in the list to get file/folder names.
                for (LsEntry item : fileAndFolderList) {

                    // If it is a file (not a directory).
                    if (!item.getAttrs().isDir()) {
                        client.rm(normalizePath + "/" + item.getFilename()); // Remove file.
                    } else if (!(".".equals(item.getFilename()) || "..".equals(item.getFilename()))) { // If it is a subdir.
                        try {
                            // removing sub directory.
                            client.rmdir(normalizePath + "/" + item.getFilename());
                        } catch (Exception e) { // If subdir is not empty and error occurs.
                            // Do lsFolderRemove on this subdir to enter it and clear its contents.
                            deleteFolder(normalizePath + "/" + item.getFilename());
                        }
                    }
                }
                client.rmdir(normalizePath); // delete the parent directory after empty
            }
        } catch (SftpException e) {
            LOG.error("", e);
            throw new SFtpOperationException("JadeFtpServer#deleteFolder : Remove dir is failed", e);
        }
    }

    public boolean existFolder(String path) {
        boolean exist = false;
        try {
            SftpATTRS attrs = client.stat(normalizePath(path));
            if (attrs == null) {
                return false;
            }
            if ((attrs.getFlags() & SftpATTRS.SSH_FILEXFER_ATTR_PERMISSIONS) == 0) {
                throw new SFtpPermissionException("JadeFtpServer#existFolder : Unknown permissions error!");
            }
            exist = attrs.isDir();
        } catch (Exception e) {
            // Folder can not be found!
        }
        return exist;
    }

    @SuppressWarnings("unchecked")
    public List<String> list(String path) {
        try {
            Vector<LsEntry> ftplist = client.ls(normalizePath(path));
            return ftplist.stream()
                    .map(LsEntry::getFilename)
                    .collect(Collectors.toList());
        } catch (SftpException e) {
            LOG.error("", e);
            throw new SFtpOperationException("JadeFtpServer#list : Unable to list files from ftp: " + e, e);
        }
    }

    public boolean isConnected() {
        return client != null && client.isConnected();
    }

    private void loadingConfiguration(String serverName) {
        //Chargement du fichier de configuration
        final InputStream jadeFtpServerConfig = getClass().getResourceAsStream(LEGACY_JADE_FTP_CONFIG_XML);

        if (jadeFtpServerConfig == null) {
            LOG.error("JadeFtpServer#loadingConfiguration : Configuration FTP server file {} not found.", LEGACY_JADE_FTP_CONFIG_XML);
            throw new SFtpConnectionException("configuration FTP server file not found");
        }

        final Document doc = parseDocument(jadeFtpServerConfig);
        final Element servers = (Element) doc.getDocumentElement().getElementsByTagName("servers").item(0);
        final NodeList allServer = servers.getElementsByTagName("server");

        for (int i = 0; i < allServer.getLength(); i++) {
            Element server = (Element) allServer.item(i);
            if (serverName.equals(server.getAttribute("name"))) {
                for (int j = 0; j < server.getChildNodes().getLength(); j++) {
                    if (server.getChildNodes().item(j) instanceof ElementImpl) {
                        Element parameter = (Element) server.getChildNodes().item(j);

                        switch (parameter.getNodeName()) {
                            case "protocol":
                                this.protocol = StringUtils.trimToNull(parameter.getTextContent());
                                this.port = "SFTP".equals(this.protocol) ? 22 : 21;
                                break;
                            case "host":
                                this.host = StringUtils.trimToNull(parameter.getTextContent());
                                break;
                            case "knownhosts":
                                this.knownHosts = StringUtils.trimToNull(parameter.getTextContent());
                                break;
                            case "port":
                                if (StringUtils.isEmpty(parameter.getTextContent())) {
                                    this.port = Integer.parseInt(parameter.getTextContent().trim());
                                }
                                break;
                            case "user":
                                this.user = StringUtils.trimToNull(parameter.getTextContent());
                                break;
                            case "password":
                                this.password = StringUtils.isEmpty(parameter.getTextContent()) ?
                                        null :
                                        decrypte(parameter.getTextContent().trim());
                                break;
                            case "certificats":
                                this.certificats = new HashMap<>();
                                NodeList certificats = parameter.getElementsByTagName("certificat");
                                for (int k = 0; k < certificats.getLength(); k++) {
                                    Node certificat = certificats.item(k);

                                    String pathCertificat = StringUtils.trimToNull(certificat.getTextContent());
                                    String passphrase = StringUtils.trimToNull(((Element) certificat).getAttribute("passphrase"));
                                    if (!StringUtils.isEmpty(passphrase)) {
                                        passphrase = decrypte(passphrase);
                                    }
                                    this.certificats.put(pathCertificat, passphrase);
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    private void connection(String user, String password) throws Exception {
        JSch jsch = new JSch();
        Session session;

        session = port == null ? jsch.getSession(user, host) : jsch.getSession(user, host, port);

        session.setPassword(password);

        paramSession(jsch, session);
    }

    private void connection(String user, Map<String, String> certificats) throws Exception {
        JSch jsch = new JSch();
        Session session;

        addCertificats(jsch, certificats);

        session = port == null ? jsch.getSession(user, host) : jsch.getSession(user, host, port);

        paramSession(jsch, session);
    }

    private void connection(String user, String password, Map<String, String> certificats) throws Exception {
        JSch jsch = new JSch();
        Session session;

        addCertificats(jsch, certificats);

        session = port == null ? jsch.getSession(user, host) : jsch.getSession(user, host, port);

        session.setPassword(password);

        paramSession(jsch, session);
    }

    private void addCertificats(JSch jsch, Map<String, String> certificats) {
        certificats.forEach((certificat, passphrase) -> {
            try {
                if (passphrase == null) {
                    jsch.addIdentity(certificat);
                } else {
                    jsch.addIdentity(certificat, passphrase);
                }
            } catch (JSchException e) {
                LOG.error("", e);
                throw new RuntimeException(e);
            }
        });
    }

    private void paramSession(JSch jsch, Session session) throws JSchException {
        if (StringUtils.isNotBlank(knownHosts)) {
            jsch.setKnownHosts(knownHosts);
        } else {
            /*
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

        LOG.info("JadeFtpServer#paramSession : Successfully connected to {}", host);

        client = (ChannelSftp) session.openChannel(protocol.toLowerCase());
        client.connect();
    }

    /**
     * Décryptage des informations
     */
    private String decrypte(String pass) {
        String decryptePass;
        try {
            decryptePass = JadeDefaultEncrypters.getJadeDefaultEncrypter().decrypt(pass);
            if (!Jade.isSilent()) {
                JadeLogger.info(this, "JadeFtpServer#decrypte : -> Information decrypted using " + JadeDefaultEncrypters.getJadeDefaultEncrypter().toString());
            }
        } catch (Exception e) {
            decryptePass = pass;
        }

        return decryptePass;
    }

    private Document parseDocument(InputStream source) {
        Document doc;

        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            final DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(source);
        } catch (ParserConfigurationException e) {
            LOG.error("", e);
            throw new SFtpOperationException("XML Parser error: ", e);
        } catch (SAXException e) {
            LOG.error("", e);
            throw new SFtpOperationException("XML Error: ", e);
        } catch (IOException e) {
            LOG.error("", e);
            throw new SFtpOperationException("IO error trying to parse source stream: ", e);
        }

        return doc;
    }

    private String normalizePath(String path) {
        return path.replace('\\', '/');
    }
}
