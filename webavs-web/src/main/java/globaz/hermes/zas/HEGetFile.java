package globaz.hermes.zas;

import globaz.globall.db.BApplication;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.hermes.utils.DateUtils;
import globaz.hermes.utils.HEEnvoiEmailsGroupe;
import globaz.hermes.utils.HEUtil;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.fs.JadeFsFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class HEGetFile {
    public static final String ARCHIVES_FOLDER = "archives";
    public static final int CODE_RETOUR_ERREUR = 200;
    public static final int CODE_RETOUR_OK = 0;
    public static final String ENCODING = "Cp037";
    private static final String FTP_FILE_ACK = "ftp.file.ack";

    private static String getExistCopyFile(BSession session) throws Exception {
        BApplication application;
        try {
            application = session.getApplication();
        } catch (Exception e1) {
            throw new Exception("Impossible de récupérer l'application :" + e1.getMessage());
        }
        String ftpCopyPath = application.getProperty("ftp.copy.path");
        if (JadeStringUtil.isEmpty(ftpCopyPath)) {
            throw new Exception("paramétrage du serveur ftp erroné!");
        }
        String ftpCopyFile = application.getProperty("ftp.copy.file");
        if (JadeStringUtil.isEmpty(ftpCopyFile)) {
            throw new Exception("paramétrage du nom de fichier erroné!");
        }
        String destPath = Jade.getInstance().getSharedDir();
        String ftpUri = JadeFilenameUtil.normalizePathRoot(ftpCopyPath) + ftpCopyFile;
        String destUri = JadeFilenameUtil.normalizePathRoot(destPath) + ftpCopyFile;

        if (JadeFsFacade.exists(ftpUri)) {
            try {
                JadeFsFacade.copyFile(ftpUri, destUri);
            } catch (Exception e) {
                throw new Exception("Echec lors de la réception du fichier du fichier pour prestation :"
                        + e.getMessage());
            }
        }
        return destUri;
    }

    private static String getFile(BSession session) throws Exception {
        BApplication application;
        try {
            application = session.getApplication();
        } catch (Exception e1) {
            throw new Exception("Impossible de récupérer l'application :" + e1.getMessage());
        }
        String ftpPath = application.getProperty("ftp.centrale.path");
        if (JadeStringUtil.isEmpty(ftpPath)) {
            throw new Exception("paramétrage du serveur ftp erroné!");
        }
        String ftpFile = application.getProperty("ftp.file.input");
        if (JadeStringUtil.isEmpty(ftpFile)) {
            throw new Exception("paramétrage du nom de fichier erroné!");
        }
        String destPath = Jade.getInstance().getSharedDir();
        String ftpUri = JadeFilenameUtil.normalizePathRoot(ftpPath) + ftpFile;
        String destUri = JadeFilenameUtil.normalizePathRoot(destPath) + ftpFile;
        String archivesUri = JadeFilenameUtil.normalizePathRoot(JadeFilenameUtil.normalizePathRoot(Jade.getInstance()
                .getSharedDir()) + HEGetFile.ARCHIVES_FOLDER)
                + ftpFile
                + "_"
                + JACalendar.format(JACalendar.today(), JACalendar.FORMAT_YYYYMMDD)
                + HEUtil.getTimeHHMMSS();
        try {
            JadeFsFacade.copyFile(ftpUri, archivesUri);
        } catch (Exception e) {
            throw new Exception("Echec lors de l'archivage du fichier:" + e.getMessage());
        }
        try {
            JadeFsFacade.copyFile(ftpUri, destUri);
        } catch (Exception e) {
            throw new Exception("Echec lors de la réception du fichier:" + e.getMessage());
        }
        return destPath;
    }

    private static String getFileCopy(BSession session) throws Exception {
        BApplication application;
        try {
            application = session.getApplication();
        } catch (Exception e1) {
            throw new Exception("Impossible de récupérer l'application :" + e1.getMessage());
        }
        String ftpPath = application.getProperty("ftp.centrale.path");

        if (JadeStringUtil.isEmpty(ftpPath)) {
            throw new Exception("paramétrage du serveur ftp de copie erroné!");
        }
        String ftpFile = application.getProperty("ftp.file.input");
        String ftpCopyFile = application.getProperty("ftp.copy.file");
        if (JadeStringUtil.isEmpty(ftpCopyFile)) {
            throw new Exception("paramétrage du nom de fichier erroné!");
        }
        String destPath = Jade.getInstance().getSharedDir();
        String ftpUri = JadeFilenameUtil.normalizePathRoot(ftpPath) + ftpFile;
        String destUri = JadeFilenameUtil.normalizePathRoot(destPath) + ftpCopyFile + "_"
                + JACalendar.format(JACalendar.today(), JACalendar.FORMAT_YYYYMMDD);
        try {
            JadeFsFacade.copyFile(ftpUri, destUri);
        } catch (Exception e) {
            throw new Exception("Echec lors de la réception du fichier du fichier pour prestation :" + e.getMessage());
        }
        return destUri;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage : java globaz.hermes.zas.FTPGet <uid> <pwd>");
            System.exit(-1);
        }
        System.out.println("**********  FTPGet executing  **********");

        // démarrage en mode CommandeLineJob pour ne pas exécuter les processus SEDEX
        Jade.getInstanceForCommandLineJob();

        try {
            BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("HERMES")
                    .newSession(args[0], args[1]);
            HEGetFile process = new HEGetFile();
            process.setSession(session);
            // set pour les logs
            process.setOut();
            // avant de lancer le téléchargement du fichier on vérifie si on n'a pas déjà une quittance présente
            if (!process.isAcknowledgementexists()) {
                process.telechargeFichier();
            } else {
                throw new Exception("Un accusé de réception existe déjà!");
            }
            if ("true".equals(process.getSession().getApplication().getProperty("ftp.file.input.copy"))) {
                process.copyFileToFTP();
            }
            System.exit(HEGetFile.CODE_RETOUR_OK);
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            System.exit(HEGetFile.CODE_RETOUR_ERREUR);
        }
        System.exit(HEGetFile.CODE_RETOUR_ERREUR);
    }

    private HEEnvoiEmailsGroupe emailResponsable = null;

    private BSession session;

    public void copyFile(File in, File out) throws Exception {
        FileInputStream fis = new FileInputStream(in);
        FileOutputStream fos = new FileOutputStream(out);
        byte[] buf = new byte[1024];
        int i = 0;
        while ((i = fis.read(buf)) != -1) {
            fos.write(buf, 0, i);
        }
        fis.close();
        fos.close();
    }

    private void copyFileToFTP() throws Exception {

        System.out.println(DateUtils.getTimeStamp() + "The copy's file uploading on another ftp server...");

        try {
            // *********************************
            System.out.println(DateUtils.getTimeStamp() + "The copy's file downloading...");
            String pathFile = HEGetFile.getFileCopy(getSession());
            if (pathFile != null) {
                File fileUploaded = new File(pathFile);
                sendCopyFile(fileUploaded);
                System.out.println(DateUtils.getTimeStamp() + "File downloaded successfully");
            }
            System.out.println(DateUtils.getTimeStamp() + "Copy file realized successfully");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.out.println(DateUtils.getTimeStamp() + "Error in copy's file download");
            // envoi de l'erreur au(x) responsable(s)
            // envoi un email
            emailResponsable.sendMail(getSession().getLabel("HERMES_10018"), e.getMessage());
            //
            throw (e);
        }
    }

    private void deleteAck() throws Exception {
        BApplication application;
        try {
            application = session.getApplication();
        } catch (Exception e1) {
            throw new Exception("Impossible de récupérer l'application :" + e1.getMessage());
        }
        String ftpPath = application.getProperty("ftp.centrale.path");
        if (JadeStringUtil.isEmpty(ftpPath)) {
            throw new Exception("paramétrage du serveur ftp erroné!");
        }
        String ftpFile = application.getProperty("ftp.file.ack");
        if (JadeStringUtil.isEmpty(ftpFile)) {
            throw new Exception("paramétrage du nom de fichier erroné!");
        }
        String ftpUri = JadeFilenameUtil.normalizePathRoot(ftpPath) + ftpFile;
        try {
            JadeFsFacade.delete(ftpUri);
        } catch (Exception e) {
            throw new Exception("Echec lors de l'envoi de l'acquittement :" + e.getMessage());
        }
    }

    /**
     * Returns the session.
     * 
     * @return BSession
     */
    public BSession getSession() {
        return session;
    }

    /**
     * Cette méthode permet de contrôler s'il n'y pas déjà une quittance présente dans le ftp. Si c'est le cas, elle
     * doit retourner true. Sinon false
     * 
     * @return boolean à true ou false
     * @throws Exception
     */
    private boolean isAcknowledgementexists() throws Exception {
        // On check si le fichier de Ack est bien paramétré
        String ftpAck = session.getApplication().getProperty(HEGetFile.FTP_FILE_ACK);
        if (JadeStringUtil.isBlankOrZero(ftpAck)) {
            throw new Exception("paramétrage de l'accusé de réception erroné!");
        }
        // On check si le path du ftp est bien paramétré
        String ftpPath = session.getApplication().getProperty("ftp.centrale.path");
        if (JadeStringUtil.isBlankOrZero(ftpPath)) {
            throw new Exception("paramétrage du chemin FTP erroné!");
        }
        // récupérer le fichier de quittancement et contrôler s'il est existant ou non
        String ftpAckFilePath = JadeFilenameUtil.normalizePathRoot(ftpPath) + ftpAck;

        if (JadeFsFacade.exists(ftpAckFilePath)) {
            return true;
        } else {
            return false;
        }
    }

    private void sendAck() throws Exception {
        BApplication application;
        try {
            application = session.getApplication();
        } catch (Exception e1) {
            throw new Exception("Impossible de récupérer l'application :" + e1.getMessage());
        }
        String ftpPath = application.getProperty("ftp.centrale.path");
        if (JadeStringUtil.isEmpty(ftpPath)) {
            throw new Exception("paramétrage du serveur ftp erroné!");
        }
        String ftpFile = application.getProperty("ftp.file.ack");
        if (JadeStringUtil.isEmpty(ftpFile)) {
            throw new Exception("paramétrage du nom de fichier erroné!");
        }
        File vide = File.createTempFile(ftpFile, "");
        String ftpUri = JadeFilenameUtil.normalizePathRoot(ftpPath) + ftpFile;
        try {
            JadeFsFacade.copyFile(vide.getAbsolutePath(), ftpUri);
        } catch (Exception e) {
            throw new Exception("Echec lors de l'envoi de l'acquittement :" + e.getMessage());
        }
    }

    private void sendCopyFile(File fileUploaded) throws Exception {

        File existingFile = new File(HEGetFile.getExistCopyFile(getSession()));

        File fusionFile = writeFusionFile(existingFile, fileUploaded);

        sendCopyFileToFtp(fusionFile);
        fileUploaded.delete();
        existingFile.delete();
        fusionFile.delete();
    }

    private void sendCopyFileToFtp(File fichierPTZPrest) throws Exception {
        BApplication application;
        try {
            application = session.getApplication();
        } catch (Exception e1) {
            throw new Exception("Impossible de récupérer l'application :" + e1.getMessage());
        }
        String ftpCopyPath = application.getProperty("ftp.copy.path");
        if (JadeStringUtil.isEmpty(ftpCopyPath)) {
            throw new Exception("paramétrage du serveur ftp erroné!");
        }
        String ftpCopyFile = application.getProperty("ftp.copy.file");
        if (JadeStringUtil.isEmpty(ftpCopyFile)) {
            throw new Exception("paramétrage du nom de fichier erroné!");
        }
        String destPath = JadeFilenameUtil.normalizePathRoot(ftpCopyPath) + ftpCopyFile;

        if (JadeFsFacade.exists(destPath)) {
            try {
                JadeFsFacade.delete(destPath);
            } catch (Exception e) {
                throw new Exception("Echec lors du fichier prestation à remplacer :" + e.getMessage());
            }
        }

        try {
            JadeFsFacade.copyFile(fichierPTZPrest.getAbsolutePath(), destPath);
        } catch (Exception e) {
            throw new Exception("Echec lors de la réception du fichier pour prestation :" + e.getMessage());
        }
    }

    /**
     * Method setOut.
     */
    private void setOut() throws Exception {
        if (getSession().getApplication().getProperty("zas.log").equals("true")) {
            String outLog = Jade.getInstance().getLogDir() + DateUtils.getMonthYear() + "/FTPGet/out/"
                    + getSession().getApplication().getProperty("zas.log.ftp.out") + DateUtils.getLocaleDateAndTime()
                    + ".log";
            String errLog = Jade.getInstance().getLogDir() + DateUtils.getMonthYear() + "/FTPGet/err/"
                    + getSession().getApplication().getProperty("zas.log.ftp.err") + DateUtils.getLocaleDateAndTime()
                    + ".log";
            StringUtils.createDirectory(outLog);
            StringUtils.createDirectory(errLog);
            PrintStream streamOut = new PrintStream(new FileOutputStream(outLog));
            System.setOut(streamOut);
            PrintStream streamErr = new PrintStream(new FileOutputStream(errLog));
            System.setErr(streamErr);
        }
    }

    public void setSession(BSession crtSession) {
        session = crtSession;
    }

    /**
     * Method go.
     */
    private void telechargeFichier() throws Exception {
        // init le(s) mails responsable(s)
        emailResponsable = new HEEnvoiEmailsGroupe(getSession(), HEEnvoiEmailsGroupe.responsable_ARC);
        System.out.println(DateUtils.getTimeStamp() + emailResponsable.size()
                + " email(s) pour l'envoi des erreurs d'exécution");
        File fichierPTZ = new File(Jade.getInstance().getSharedDir()
                + session.getApplication().getProperty("ftp.file.input"));

        if (fichierPTZ.exists()) {
            throw new Exception("Echec, le fichier destination " + fichierPTZ.getName() + " existe déjà !");
        }
        try {
            // *********************************
            System.out.println(DateUtils.getTimeStamp() + "The file downloading...");
            String pathFile = HEGetFile.getFile(getSession());
            if (pathFile != null) {
                System.out.println(DateUtils.getTimeStamp() + "File downloaded successfully");
                System.out.println(" - > " + pathFile);
            }
            if ("true".equals(getSession().getApplication().getProperty("ftp.file.ack.put"))) {
                System.out.println("**********************************");
                System.out.println("* Acknowledging data to Centrale *");
                System.out.println("**********************************");
                sendAck();
            }
            System.out.println(DateUtils.getTimeStamp() + "Process realized successfully");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.out.println(DateUtils.getTimeStamp() + "Error in file download");
            // être sûr que l'acquittement n'a pas été déposé tout de même
            System.out.println(DateUtils.getTimeStamp() + "Be sure that the acknowledge is not posted");
            try {
                deleteAck();
                System.out.println(DateUtils.getTimeStamp() + "acknowledge deletion checking is ok");
            } catch (Exception err) {
                System.err.println(DateUtils.getTimeStamp() + "Error in deleting the acknoledge : " + err.getMessage());
            }
            // envoi de l'erreur au(x) responsable(s)
            // envoi un email
            emailResponsable.sendMail(getSession().getLabel("HERMES_10018"), e.getMessage());
            //
            throw (e);
        }
    }

    private File writeFusionFile(File fichierPTZPrest, File fileUploaded) throws Exception {
        // On ouvre (ou on crée) le fichier de destination :
        File res = new File(fichierPTZPrest.getAbsolutePath() + "fusionne");
        OutputStream out = new FileOutputStream(res);
        try {

            byte[] buf = new byte[8192]; // buffer de copie par bloc
            int len; // compteur de byte lu
            if (fichierPTZPrest.exists()) {
                InputStream in1 = new FileInputStream(fichierPTZPrest);
                try {
                    // On lit dans le buffer (bloc de 8192 bytes max)
                    while ((len = in1.read(buf)) >= 0) {
                        out.write(buf, 0, len); // et on copie ce qu'on a lu
                    }
                } finally {
                    in1.close();
                }
            }
            if (fileUploaded.exists()) {
                InputStream in2 = new FileInputStream(fileUploaded);
                try {
                    while ((len = in2.read(buf)) >= 0) {
                        out.write(buf, 0, len); // et on copie ce qu'on a lu
                    }
                } finally {
                    in2.close();
                }
            }
        } finally {
            out.close();
        }
        return res;
    }
}
