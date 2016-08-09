package globaz.osiris.db.ordres.sepa;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

abstract class AbstractSepa {
    public static /* final */ class SepaException extends RuntimeException {
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

    protected static final String SEPA_FTP_HOST = "sepa.ftp.host";
    protected static final String SEPA_FTP_PORT = "sepa.ftp.port";
    protected static final String SEPA_FTP_USER = "sepa.ftp.user";
    protected static final String SEPA_FTP_PASS = "sepa.ftp.pass";

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

    protected Document parseDocument(InputStream source) {
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
    protected <T> T unmarshall(Document doc, Class<? extends T> clazz) {
        try {
            JAXBContext jc = JAXBContext.newInstance(clazz);

            Unmarshaller unmarshaller = jc.createUnmarshaller();
            unmarshaller.setEventHandler(new DefaultValidationEventHandler());

            return unmarshaller.unmarshal(doc, clazz).getValue();
        } catch (JAXBException e) {
            throw new SepaException(
                    "unable to convert xml document to java object of class " + clazz.getName() + ": " + e, e);
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

    /** @throws SepaException en cas d'erreur de connexion au FTP. */
    protected FTPClient connect(String server, String user, String password) {
        FTPClient ftp = new FTPClient();
        FTPClientConfig config = new FTPClientConfig();
        ftp.configure(config);

        try {
            ftp.connect(server);
            LOG.trace("FTP Answer after connecting: {}", ftp.getReplyString());
            dieIfUnsuccessfull(ftp);

            if (!ftp.login(user, password)) {
                throw new IOException("unable to connect with the provided credentials: " + ftp.getReplyString());
            }
            LOG.trace("FTP Answer after login: {}", ftp.getReplyString());
        } catch (IOException e) {
            throw new SepaException("FTP Error: " + e, e);
        }

        return ftp;
    }

    /**
     * @param pathname Une arborescence de chemin terminés par un nom de fichiers, relatifs par rapport à la racine.
     */
    protected void sendData(InputStream source, FTPClient client, String pathname) {
        try {
            String filename = cdToAppropriateFolder(client, pathname);

            if (!client.storeFile(filename, source)) {
                LOG.info("unable to store file in ftp: last reply was: {}", client.getReplyString());
                throw new SepaException("unable to store file in ftp: last reply code was " + client.getReplyCode());
            }
        } catch (IOException e) {
            throw new SepaException("unable to store file in ftp: " + e, e);
        }
    }

    /**
     * Déplace la session ftp dans le répertoire absolu passé en paramètre, et retourne le "restant" du chemin, aka le
     * "nom du fichier".
     *
     * @param client Client
     * @param pathname Chemin (absolu par rapport à la racine du FTP, ou relatif par rapport au répertoire courant) de
     *            la resource référencée. Le dernier segment
     *            contient le nom du fichier.
     * @return le nom du fichier à utiliser, extrait du paramètre pathname. ex. si pathname vaut "/path/to/a/file.xls",
     *         alors cette méthode va retourner "file.xls".
     */
    private String cdToAppropriateFolder(FTPClient client, String pathname) throws IOException {
        String folder = null;
        String name = pathname;

        if (pathname.contains("/")) {
            int lastSlash = pathname.lastIndexOf('/');
            folder = pathname.substring(0, lastSlash);
            name = pathname.substring(lastSlash + 1);
        }

        if (StringUtils.isNotBlank(folder)) {
            // make sure absolute file
            String newFolder = folder;
            if (!folder.startsWith("/")) {
                newFolder = "/" + newFolder;
            }

            if (!client.changeWorkingDirectory(folder)) {
                LOG.info("unable to move to directory " + folder + " in ftp: last reply was: {}",
                        client.getReplyString());
                throw new SepaException("unable to move to directory " + folder + " in ftp: last reply code was "
                        + client.getReplyCode());
            }
        }

        return name;
    }

    protected String[] listFiles(FTPClient client, String foldername) {
        try {
            cdToAppropriateFolder(client, foldername + "/foobar.txt");

            List<String> found = new ArrayList<String>();
            for (FTPFile file : client.listFiles()) {
                found.add(file.getName());
            }

            return found.toArray(new String[found.size()]);
        } catch (IOException e) {
            throw new SepaException("unable to list files from ftp: " + e, e);
        }
    }

    protected void retrieveData(FTPClient client, String filename, OutputStream target) {
        try {
            if (!client.retrieveFile(filename, target)) {
                LOG.info("unable to get file from ftp: last reply was: {}", client.getReplyString());
                throw new SepaException("unable to get file from ftp: last reply code was " + client.getReplyCode());
            }
        } catch (IOException e) {
            throw new SepaException("unable to get file from ftp: " + e, e);
        }
    }

    protected void dieIfUnsuccessfull(FTPClient client) throws IOException {
        int replyCode = client.getReplyCode();

        if (!FTPReply.isPositiveCompletion(replyCode)) {
            disconnectQuietly(client);
            throw new IOException("FTP server replyed with code " + replyCode);
        }
    }

    protected void disconnectQuietly(FTPClient ftp) {
        if (ftp != null) {
            try {
                ftp.disconnect();
            } catch (IOException e) {
                LOG.trace("exception during disconnect: " + e, e);
            }
        }
    }
}
