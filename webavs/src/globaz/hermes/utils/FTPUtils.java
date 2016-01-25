package globaz.hermes.utils;

import globaz.globall.db.BSession;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

// import org.apache.commons.net.ProtocolCommandEvent;
// import org.apache.commons.net.ProtocolCommandListener;
// import org.apache.commons.net.ftp.FTPClient;
/**
 * Insérez la description du type ici. Date de création : (29.04.2003 16:40:40)
 * 
 * @author: Administrator
 */
public class FTPUtils { // implements ProtocolCommandListener {
    public static final String ARCHIVES_FOLDER = "archives";

    public static final String FTP_FILE_ACK = "ftp.file.ack";
    public static final String FTP_FILE_INPUT = "ftp.file.input";
    public static final String FTP_HOST = "ftp.host";
    public static final String FTP_LOGIN = "ftp.login";
    public static final String FTP_PASSWORD = "ftp.password";
    public static final String FTP_PORT = "ftp.port";

    /**
     * Insérez la description de la méthode ici. Date de création : (15.05.2003 13:10:30)
     * 
     * @param args
     *            java.lang.String[]
     */
    public static void main(String[] args) {
        try {
            // new FTPUtils(null).putReturnFile(new File("c:/ftp_log"));
            FTPUtils ftpUtils = new FTPUtils();
            File f = new File("c:/temp/archivetest/filetoarchive.txt");
            if (!f.exists()) {
                f.createNewFile();
            }
            ftpUtils.archiveFile(f, ARCHIVES_FOLDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PrintWriter __writer;
    private String ackFileName;
    private String fileName;
    private FTPWrapper ftp;
    // private FTP ftp;
    private String host;
    private String login;
    private String password;
    private int port;

    private BSession session;

    /**
     * Commentaire relatif au constructeur FTPUtils.
     */
    public FTPUtils() throws Exception {
        __writer = new PrintWriter(System.out);
        ftp = new FTPWrapper();
    }

    public FTPUtils(BSession bSession) throws Exception {
        session = bSession;
        __writer = new PrintWriter(System.out);
        ftp = new FTPWrapper();
        // read values from properties file
        host = session.getApplication().getProperty(FTP_HOST);
        port = Integer.parseInt(session.getApplication().getProperty(FTP_PORT));
        login = session.getApplication().getProperty(FTP_LOGIN);
        password = session.getApplication().getProperty(FTP_PASSWORD);
        fileName = session.getApplication().getProperty(FTP_FILE_INPUT);
        ackFileName = session.getApplication().getProperty(FTP_FILE_ACK);
    }

    /* Spécifier les paramètres du serveur FTP */
    public FTPUtils(BSession lSession, String lHost, String lLogin, String lPassword, String lFile) throws Exception {
        session = lSession;
        __writer = new PrintWriter(System.out);
        ftp = new FTPWrapper();
        // read values from properties file
        host = lHost;
        port = Integer.parseInt(session.getApplication().getProperty(FTP_PORT));
        login = lLogin;
        password = lPassword;
        fileName = lFile;
        ackFileName = session.getApplication().getProperty(FTP_FILE_ACK);

    }

    public void acknowledgeTransfer() throws Exception {
        ftp.openServer(host, port);
        ftp.login(login, password);
        ftp.binary();
        FileInputStream vide = new FileInputStream(File.createTempFile(ackFileName, ""));

        if (!ftp.uploadFile(vide, ackFileName)) {
            vide.close();
            ftp.closeServer();
            throw new Exception("FTPUtils : error in the acknowledge post");
        }
        try {
            vide.close();
            ftp.closeServer();
        } catch (Exception e) {
            // erreur a été produite lors de la déconnexion
            // mais l'acquittement a été tout de même posté
            e.printStackTrace();
        }
    }

    public void archiveFile(File f, String directory) {
        try {
            File archivesDirectory = new File(Jade.getInstance().getSharedDir() + directory);
            if (!archivesDirectory.exists()) {
                archivesDirectory.mkdir();
            }
            File archiveFile = new File(archivesDirectory + "/" + f.getName() + "_" + DateUtils.getLocaleDateAndTime());
            copyFile(f, archiveFile);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void cd(String directory) throws IOException {
        ftp.cd(directory);
    }

    public void copyFile(File source, File dest) {
        try {
            FileReader fr = new FileReader(source);
            FileWriter fw = new FileWriter(dest);
            int car = 0;
            while ((car = fr.read()) != -1) {
                fw.write(car);
            }
            fr.close();
            fw.close();
        } catch (Exception e) {
            System.out.println("Archivage impossible " + e.toString());
        }
    }

    public boolean deleteAcknowlegde() throws Exception {
        ftp.openServer(host, port);
        ftp.login(login, password);
        ftp.binary();
        return ftp.deleteFile(ackFileName);
    }

    /**
     * Returns the __writer.
     * 
     * @return PrintWriter
     */
    public PrintWriter get__writer() {
        return __writer;
    }

    /**
     * Returns the ackFileName.
     * 
     * @return String
     */
    public String getAckFileName() {
        return ackFileName;
    }

    /**
     * Returns the fileName.
     * 
     * @return String
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Returns the host.
     * 
     * @return String
     */
    public String getHost() {
        return host;
    }

    /**
     * Returns the login.
     * 
     * @return String
     */
    public String getLogin() {
        return login;
    }

    /**
     * Returns the password.
     * 
     * @return String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the port.
     * 
     * @return int
     */
    public int getPort() {
        return port;
    }

    public File getReturnFile() throws Exception {
        try {
            ftp.openServer(host, port);
            ftp.login(login, password);
            ftp.binary();
            ftp.downloadFile(fileName, Jade.getInstance().getSharedDir() + fileName);
            ftp.closeServer();
            File returnFile = new File(Jade.getInstance().getSharedDir() + fileName);
            archiveFile(returnFile, ARCHIVES_FOLDER);
            return returnFile;
        } catch (Exception e) {
            throw e;
        }
    }

    public File getReturnFile(String nomFichier) throws Exception {
        return getReturnFile(nomFichier, nomFichier);
    }

    public File getReturnFile(String serverFile, String localFile) throws Exception {
        try {
            ftp.openServer(host, port);
            ftp.login(login, password);
            ftp.binary();
            ftp.downloadFile(serverFile, Jade.getInstance().getSharedDir() + localFile);
            ftp.closeServer();
            File returnFile = new File(Jade.getInstance().getSharedDir() + localFile);
            archiveFile(returnFile, ARCHIVES_FOLDER);
            return returnFile;
        } catch (Exception e) {
            throw e;
        }
    }

    public File getReturnFile(String serverFile, String localFile, String path) throws Exception {
        try {
            ftp.openServer(host, port);
            ftp.login(login, password);
            ftp.binary();
            ftp.cd(path);
            ftp.downloadFile(serverFile, Jade.getInstance().getSharedDir() + localFile);
            ftp.closeServer();
            File returnFile = new File(Jade.getInstance().getSharedDir() + localFile);
            archiveFile(returnFile, ARCHIVES_FOLDER);
            return returnFile;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @return
     */
    public boolean isFileExists(String serverFile) throws Exception {
        ftp.openServer(host, port);
        ftp.login(login, password);
        ftp.binary();
        boolean res = ftp.isFileExist(serverFile);
        ftp.closeServer();
        return res;
    }

    public boolean isFileExists(String serverFile, String path) throws Exception {
        ftp.openServer(host, port);
        ftp.login(login, password);
        ftp.binary();
        ftp.cd(path);
        boolean res = ftp.isFileExist(serverFile);
        ftp.closeServer();
        return res;
    }

    /**
     * Le server FTP est-il un serveur de type UNIX (ou UNIX émulé).
     * 
     * @return
     */
    public boolean isSystemTypeUnix() {
        try {
            openServer();

            ftp.issueRawCommand("SYST");
            String response = ftp.getResponseString();

            return (response.indexOf("UNIX") > -1);
        } catch (IOException e) {
            JadeLogger.error(this, e);

            if (ftp.serverIsOpen()) {
                try {
                    ftp.closeServer();
                } catch (IOException e1) {
                    JadeLogger.error(this, e);
                }
            }
        }

        return false;
    }

    /**
     * Ouvre la connection au serveur FTP en mode binaire.
     * 
     * @throws IOException
     */
    private void openServer() throws IOException {
        ftp.openServer(host, port);
        ftp.login(login, password);
        ftp.binary();
    }

    public void putReturnFile(File f) throws Exception {
        try {
            ftp.openServer(host, port);
            ftp.login(login, password);
            ftp.binary();
            // test si le dernier fichier posté a été traité
            if (!ftp.uploadFile(f.getAbsolutePath(), f.getName())) {
                throw new Exception("Impossible to upload file :" + f.getAbsolutePath());
            }
            ftp.closeServer();
            archiveFile(f, ARCHIVES_FOLDER);
        } catch (Exception e) {
            throw e;
        }
    }

    public void putReturnFile(File f, String path) throws Exception {
        try {
            ftp.openServer(host, port);
            ftp.login(login, password);
            ftp.binary();
            ftp.cd(path);
            // test si le dernier fichier posté a été traité
            if (!ftp.uploadFile(f.getAbsolutePath(), f.getName())) {
                throw new Exception("Impossible to upload file :" + f.getAbsolutePath());
            }
            ftp.closeServer();
            archiveFile(f, ARCHIVES_FOLDER);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Sets the __writer.
     * 
     * @param __writer
     *            The __writer to set
     */
    public void set__writer(PrintWriter __writer) {
        this.__writer = __writer;
    }

    /**
     * Sets the ackFileName.
     * 
     * @param ackFileName
     *            The ackFileName to set
     */
    public void setAckFileName(String ackFileName) {
        this.ackFileName = ackFileName;
    }

    /**
     * Sets the fileName.
     * 
     * @param fileName
     *            The fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Sets the host.
     * 
     * @param host
     *            The host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Sets the login.
     * 
     * @param login
     *            The login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Sets the password.
     * 
     * @param password
     *            The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the port.
     * 
     * @param port
     *            The port to set
     */
    public void setPort(int port) {
        this.port = port;
    }
}
