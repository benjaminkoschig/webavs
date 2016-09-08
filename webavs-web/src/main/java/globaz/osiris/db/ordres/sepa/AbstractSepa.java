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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public abstract class AbstractSepa {
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

    /** @throws SepaException en cas d'erreur de connexion au FTP. */
    protected ChannelSftp connect(String server, Integer port, String user, String password) {
        JSch jsch = new JSch();
        Session session;
        try {
            session = jsch.getSession(user, server, port);

            session.setPassword(password);

            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            LOG.info("Connected to {}", server);

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
}
