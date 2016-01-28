package globaz.ftp;

import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;

public class FtpEnterprisedtWrapper {
    public static final String FTP_HOST = "ftp.host";
    public static final String FTP_LOGIN = "ftp.login";
    public static final String FTP_PORT = "ftp.port";

    private FTPTransferType connectionType = FTPTransferType.ASCII;

    private FTPClient ftp;
    private String login;

    private String password;

    private BSession session;

    public FtpEnterprisedtWrapper(BSession session) throws Exception {
        this(session, true);
    }

    public FtpEnterprisedtWrapper(BSession session, boolean modePassive) throws Exception {
        this(session, modePassive, session.getApplication().getProperty(FTP_LOGIN), session.getApplication()
                .getProperty(FTP_HOST), Integer.parseInt(session.getApplication().getProperty(FTP_PORT)));
    }

    public FtpEnterprisedtWrapper(BSession session, boolean modePassive, String login, String host, int port)
            throws Exception {
        setSession(session);
        setLogin(login);

        ftp = new FTPClient();

        ftp.setRemoteHost(host);
        ftp.setRemotePort(port);

        if (modePassive) {
            ftp.setConnectMode(FTPConnectMode.PASV);
        } else {
            ftp.setConnectMode(FTPConnectMode.ACTIVE);
        }
    }

    /**
     * Change de répertoire sur le serveur FTP.
     * 
     * @param subDirectory
     * @throws FTPException
     * @throws IOException
     */
    private void changeFtpDirectory(String subDirectory) throws IOException, FTPException {
        if (!JadeStringUtil.isBlank(subDirectory)) {
            ftp.chdir(subDirectory);
        }
    }

    public void deleteFile(String directory, String fileName) throws Exception {
        openServer();

        changeFtpDirectory(directory);

        ftp.delete(fileName);

        ftp.quit();
    }

    public FTPTransferType getConnectionType() {
        return connectionType;
    }

    /**
     * Télécharge le fichier présent sur le serveur ftp vers le répertoire "archives".
     * 
     * @param directory
     * @param fileName
     * @return
     * @throws Exception
     */
    public File getFile(String directory, String fileName, String localDirectory) throws Exception {
        return getFile(directory, fileName, localDirectory, fileName);
    }

    public File getFile(String directory, String serverFileName, String localDirectory, String localFileName)
            throws Exception {
        openServer();

        changeFtpDirectory(directory);

        ftp.get(localDirectory + "/" + localFileName, serverFileName);
        ftp.quit();

        return new File(localDirectory + "/" + localFileName);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public BSession getSession() {
        return session;
    }

    /**
     * retourne si un fichier existe sur le serveur ftp .
     * 
     * @param directory
     * @param fileName
     * @return
     * @throws Exception
     */
    public boolean isFileExist(String directory, String fileName) throws IOException, FTPException {
        ArrayList listFile = listDirectoryFiles(directory);
        for (int i = 0; i < listFile.size(); i++) {
            String crt = listFile.get(i).toString();
            int index = crt.indexOf(fileName);
            // on vérifie encore que ce qu'on a trouvé un espace devant et rien
            // après
            if (index > 0 && crt.length() == index + fileName.length()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Liste les fichiers d'un répertoire distant.
     * 
     * @param subDirectory
     *            Dossier distant indexé par rapport au dossier racine.
     * @return
     */
    public ArrayList listDirectoryFiles(String subDirectory) {
        ArrayList result = null;
        try {
            openServer();

            changeFtpDirectory(subDirectory);

            String[] files = ftp.dir(".", true);
            result = new ArrayList();
            for (int i = 0; i < files.length; i++) {
                result.add(files[i]);
            }

            ftp.quit();
        } catch (Exception e) {
            JadeLogger.error(this, e);

            if (ftp.connected()) {
                try {
                    ftp.quit();
                } catch (IOException e1) {
                    JadeLogger.error(this, e1);
                } catch (FTPException e1) {
                    JadeLogger.error(this, e1);
                }
            }
        }

        return result;
    }

    private void openServer() throws IOException, FTPException {
        ftp.connect();
        ftp.login(login, password);
        ftp.setType(getConnectionType());
    }

    /**
     * Créer un fichier dans le sous-répertoire (optionnel) du server ftp à partir d'un flux.
     * 
     * @param in
     * @param subDirectory
     * @param fileName
     * @throws Exception
     */
    public void putFile(ByteArrayInputStream in, String subDirectory, String fileName) throws Exception {
        openServer();

        changeFtpDirectory(subDirectory);

        try {
            ftp.put(in, fileName);
        } catch (Exception e) {
            ftp.quit();
            throw e;
        }

        ftp.quit();
    }

    /**
     * Post un fichier sur le serveur ftp dans un sous-répertoire (optionnel).
     * 
     * @param f
     * @param destinationFileName
     * @param subDirectory
     * @throws Exception
     */
    public void putFile(File f, String destinationFileName, String subDirectory) throws Exception {
        openServer();

        changeFtpDirectory(subDirectory);

        try {
            ftp.put(f.getAbsolutePath(), destinationFileName);
        } catch (Exception e) {
            ftp.quit();
            throw e;
        }

        ftp.quit();
    }

    public void setConnectionType(FTPTransferType connectionType) {
        this.connectionType = connectionType;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    /**
     * Test s'il est possible d'établir la connection avec le serveur ftp défini par les properties du module.
     * 
     * @return
     */
    public boolean testConnectionAvailable() {
        try {
            openServer();

            ftp.quit();
        } catch (FTPException e) {
            JadeLogger.warn(this, e);
            return false;
        } catch (IOException e) {
            JadeLogger.warn(this, e);
            return false;
        }

        return true;
    }
}
