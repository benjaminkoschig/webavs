package globaz.osiris.db.bvrftp;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.ftp.FtpEnterprisedtWrapper;
import globaz.globall.db.BEntity;
import globaz.globall.db.BManager;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import globaz.osiris.db.bvrftp.log.CABvrFtpLog;
import globaz.osiris.db.bvrftp.log.CABvrFtpLogManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author dda
 */
public class CABvrFtpListViewBean extends BManager implements FWListViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String FTP_READ_LSV_REMOTE_DIRECTORY = "debit-direct-download";

    public static final String FTP_READ_OPAE_REMOTE_DIRECTORY = "esr-bvr-pvr";
    public static final String FTP_READ_OPAE_REMOTE_DIRECTORY_TEST = "esr-bvr-pvr-t";

    public static final String FTP_SEND_LSV_SUB_DIRECTORY = "debit-direct-upload";
    public static final String FTP_SEND_OPAE_SUB_DIRECTORY = "ezag-opae";

    private static final String LABEL_FTP_CONNECTION_AVAILABLE = "FTP_CONNECTION_AVAILABLE";
    private static final String LABEL_FTP_CONNECTION_UNAVAILABLE = "FTP_CONNECTION_UNAVAILABLE";

    private static final int MIN_FILE_LENGTH = 15;
    private static final String PROTOCOL_SFTP = "sftp";

    private String distantDirectoryName = "";

    private ArrayList pvrFiles;

    public CABvrFtpListViewBean() {
        pvrFiles = new ArrayList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BManager#_newEntity()
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return null;
    }

    /**
     * Ajoute un fichier à une arraylist.
     * 
     * @param bvrFtp
     * @param addFirst
     *            A ajouter en début ou fin de l'arraylist
     */
    private void addToPvrFiles(CABvrFtpViewBean bvrFtp, boolean addFirst) {
        if (addFirst) {
            pvrFiles.add(0, bvrFtp);
        } else {
            pvrFiles.add(bvrFtp);
        }
    }

    /**
     * Le nom du répertoire distant à lister.
     * 
     * @return
     */
    public String getDistantDirectoryName() {
        return distantDirectoryName;
    }

    /**
     * Télécharge le fichier présent sur le serveur ftp vers le le serveur applicatif.
     * 
     * @param fileName
     * @return Le nom et chemin du fichier télécharger.
     * @throws Exception
     */
    public String getFileFromFtp(String fileName) throws Exception {
        JadeFsFacade.copyFile(getServerUri() + "/" + fileName, Jade.getInstance().getHomeDir() + "work/" + fileName);

        File returnFile = new File(Jade.getInstance().getHomeDir() + "work/" + fileName);

        CABvrFtpLogManager logManager = new CABvrFtpLogManager();
        logManager.setSession(getSession());
        logManager.setForCodeJournal(CABvrFtpLog.FILE_RECEIVE);
        logManager.setForFileName(fileName);

        logManager.find();

        if (logManager.isEmpty()) {
            CABvrFtpLog log = new CABvrFtpLog();
            log.setSession(getSession());
            log.setFileName(fileName);
            log.setCodeJournal(CABvrFtpLog.FILE_RECEIVE);
            log.setMoreInformations(CABvrFtpLog.MORE_INFOS_FILE_RECEIVE);

            log.add();

            if (log.hasErrors()) {
                throw new Exception(log.getErrors().toString());
            }
        } else {
            CABvrFtpLog log = (CABvrFtpLog) logManager.getFirstEntity();

            log.update();

            if (log.hasErrors()) {
                throw new Exception(log.getErrors().toString());
            }
        }

        return returnFile.getAbsolutePath();
    }

    /**
     * Return la liste des fichiers présents sur le serveurs.
     * 
     * @return
     * @throws Exception
     */
    private List getFilesList() throws Exception {
        return JadeFsFacade.getFolderChildren(getServerUri());
    }

    /**
     * Retourne l'état de la connection au serveur sous forme de texte.
     * 
     * @return
     */
    public String getFtpConnectionState() {
        if (isFtpConnectionAvailable()) {
            return getSession().getLabel(CABvrFtpListViewBean.LABEL_FTP_CONNECTION_AVAILABLE);
        } else {
            return getSession().getLabel(CABvrFtpListViewBean.LABEL_FTP_CONNECTION_UNAVAILABLE);
        }
    }

    /**
     * Renvois l'host ftp défini dans les properties d'Osiris.
     * 
     * @return
     */
    public String getHost() {
        try {
            return getSession().getApplication().getProperty(FtpEnterprisedtWrapper.FTP_HOST);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Renvois le login ftp défini dans les properties d'Osiris.
     * 
     * @return
     */
    public String getLogin() {
        try {
            return getSession().getApplication().getProperty(FtpEnterprisedtWrapper.FTP_LOGIN);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Renvois le port ftp défini dans les properties d'Osiris.
     * 
     * @return
     */
    public String getPort() {
        try {
            return getSession().getApplication().getProperty(FtpEnterprisedtWrapper.FTP_PORT);
        } catch (Exception e) {
            return "";
        }
    }

    public CABvrFtpViewBean getPvrFile(int index) {
        return (CABvrFtpViewBean) getPvrFiles().get(index);
    }

    /**
     * @return
     */
    public ArrayList getPvrFiles() {
        return pvrFiles;
    }

    /**
     * Return l'adresse du serveur formatté.
     * 
     * @return
     */
    private String getServerUri() {
        return CABvrFtpListViewBean.PROTOCOL_SFTP + "://" + getLogin() + "@" + getHost() + ":" + getPort() + "/"
                + getDistantDirectoryName();
    }

    /**
     * Le serveur est-il accessible ? Test la connection ftp. True si la connection peut-être établie.
     * 
     * @return
     */
    public boolean isFtpConnectionAvailable() {
        try {
            return getFilesList() != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Liste les BVR distants. Ajoute un flag si un BVR a déjà été downloadé.
     * 
     * @throws Exception
     */
    public void listDirectoryFiles() throws Exception {
        CABvrFtpLogManager logManager = new CABvrFtpLogManager();
        logManager.setSession(getSession());
        logManager.setForCodeJournal(CABvrFtpLog.FILE_RECEIVE);

        logManager.find(BManager.SIZE_NOLIMIT);

        ArrayList filesAlreadyDownloaded = new ArrayList();
        for (int i = 0; i < logManager.size(); i++) {
            filesAlreadyDownloaded.add(((CABvrFtpLog) logManager.get(i)).getFileName());
        }

        List tmp = getFilesList();
        for (Iterator iter = tmp.iterator(); iter.hasNext();) {
            String fileReceived = ((String) iter.next()).trim();
            System.out.println(fileReceived);
            String file = JadeFilenameUtil.extractFilename(fileReceived);

            if (!JadeStringUtil.isBlank(file) && (file.length() > CABvrFtpListViewBean.MIN_FILE_LENGTH)) {
                String date = file.substring(0, CABvrFtpListViewBean.MIN_FILE_LENGTH).trim();

                if (filesAlreadyDownloaded.contains(file)) {
                    addToPvrFiles(new CABvrFtpViewBean(date, file, getDistantDirectoryName(), true), false);
                } else {
                    addToPvrFiles(new CABvrFtpViewBean(date, file, getDistantDirectoryName()), true);
                }
            }
        }
    }

    /**
     * Dépose un fichier sur le serveur ftp.
     * 
     * @param sourceFileLocation
     * @param destinationFileName
     * @throws Exception
     */
    public void putFileToFtp(String sourceFileLocation, String destinationFileName) throws Exception {
        JadeFsFacade.copyFile(sourceFileLocation, getServerUri() + "/" + destinationFileName);
    }

    /**
     * Défini le nom du répertoire distant à lister.
     * 
     * @value FTP_OPAE_REMOTE_DIRECTORY or FTP_LSV_REMOTE_DIRECTORY
     * @param string
     */
    public void setDistantDirectoryName(String string) {
        distantDirectoryName = string;
    }

    /**
     * @param list
     */
    public void setPvrFiles(ArrayList list) {
        pvrFiles = list;
    }
}
