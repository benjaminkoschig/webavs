package ch.globaz.eform.hosting;

import ch.globaz.common.file.JadeFsServer;
import ch.globaz.common.sftp.JadeSFtpServer;
import ch.globaz.eform.exception.ConfigurationFileNotFoundException;
import globaz.jade.common.Jade;
import globaz.osiris.db.ordres.sepa.CAJaxbUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class EFormFileService {
    public static final String LEGACY_JADE_CONFIG_FILE = "/JadeFsServer.xml";

    private String type;
    private String server;
    private String pathRoot;

    private String nameConfiguration;

    private JadeSFtpServer ftpServer;
    private JadeFsServer fileService;

    public EFormFileService(String nameConfiguration) {
        this.nameConfiguration = nameConfiguration;
        loadConfigurationFile();
        if ("sftp".equals(type)) {
            ftpServer = JadeSFtpServer.client(server);
            ftpServer.connect();
        }
        if ("filesystem".equals(type)) {
            fileService = new JadeFsServer();
        }
    }

    private void loadConfigurationFile() {
        /* try fetching a private ssh key from legacy config files, mimic Jade behavior */
        final InputStream jadeFsServerConfig = getClass().getResourceAsStream(LEGACY_JADE_CONFIG_FILE);

        if (jadeFsServerConfig == null) {
            LOG.error("config file {} not found.",
                    LEGACY_JADE_CONFIG_FILE);
            throw new ConfigurationFileNotFoundException();
        }

        final Document doc = CAJaxbUtil.parseDocument(jadeFsServerConfig);
        final NodeList allProtocols = doc.getDocumentElement().getElementsByTagName("protocols");

        for (int i = 0; i < allProtocols.getLength(); i++) {
            Element protocols = (Element) allProtocols.item(i);
            NodeList allProtocol = protocols.getElementsByTagName("protocol");

            for (int j = 0; j < allProtocol.getLength(); j++) {
                Element protocol = (Element) allProtocol.item(j);

                if (nameConfiguration.equals(protocol.getAttribute("name"))) {
                    this.type = protocol.getAttribute("type");

                    NodeList protocolChildren = protocol.getChildNodes();

                    for (int k = 0; k < protocolChildren.getLength(); k++) {
                        Node n = protocolChildren.item(k);

                        if (n instanceof Element) {
                            if ("server".equals(n.getNodeName())) {
                                this.server = n.getTextContent();
                            }
                            if ("pathroot".equals((n.getNodeName()))) {
                                this.pathRoot = n.getTextContent().replaceAll("[\\\\/]+", StringEscapeUtils.escapeJava(File.separator));
                            }
                        }
                    }
                }
            }
        }
    }

    public File retrieve(String path, String filename){
        if (ftpServer == null) {
            return fileService.read(getPathRoot(path), filename);
        }
        return ftpServer.retrieve(getPathRoot(path), filename, Jade.getInstance().getPersistenceDir());
    }

    public void send(String path, String pathFile){
        send(Paths.get(path), pathFile);
    }

    public void send(Path file, String pathFile){
        if (ftpServer == null) {
            fileService.write(file, getPathRoot(pathFile));
        } else {
            ftpServer.send(file, getPathRoot(pathFile));
        }
    }

    public void remove(String path) {
        if (ftpServer == null) {
            fileService.delete(Paths.get(getPathRoot(path)));
        }
        ftpServer.delete(getPathRoot(path));
    }

    public boolean exist(String path) {
        if (ftpServer == null) {
            return fileService.exist(Paths.get(getPathRoot(path)));
        } else {
            return ftpServer.exist(getPathRoot(path));
        }
    }

    public void createFolder(String path) {
        if (ftpServer == null) {
            fileService.createDirectories(Paths.get(getPathRoot(path)));
        } else {
            ftpServer.createFolder(getPathRoot(path));
        }
    }

    private String getPathRoot(String path) {
        String pathRoot = "";

        if (!StringUtils.isEmpty(this.pathRoot)) {
            pathRoot = this.pathRoot.endsWith(File.separator) ? this.pathRoot + path : this.pathRoot + File.separator + path;
        }

        if (!path.endsWith(File.separator)) {
            pathRoot = pathRoot + File.separator;
        }

        return  pathRoot;
    }
}

