package globaz.hermes.zas;

import globaz.ftp.FtpEnterprisedtWrapper;
import globaz.globall.db.BApplication;
import globaz.globall.db.BSession;
import globaz.globall.db.GlobazServer;
import globaz.hermes.utils.DateUtils;
import globaz.hermes.utils.FTPUtils;
import globaz.hermes.utils.HEEnvoiEmailsGroupe;
import globaz.hermes.utils.StringUtils;
import globaz.jade.common.Jade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * @author ado 18 déc. 03
 */
public class FTPGet {
    public static final int CODE_RETOUR_ERREUR = 200;
    public static final int CODE_RETOUR_OK = 0;
    public static final String ENCODING = "Cp037";

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage : java globaz.hermes.zas.FTPGet <uid> <pwd>");
            System.exit(-1);
        }
        System.out
                .println("**********  FTPGet excuting with user:" + args[0] + " password:" + args[1] + "  **********");
        try {
            BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("HERMES")
                    .newSession(args[0], args[1]);
            FTPGet process = new FTPGet();
            process.setSession(session);
            process.telechargeFichier();
            if ("true".equals(process.getSession().getApplication().getProperty("ftp.file.input.copy"))) {
                process.copyFileToFTP();
            }
            System.exit(CODE_RETOUR_OK);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(CODE_RETOUR_ERREUR);
        }
    }

    private HEEnvoiEmailsGroupe emailResponsable = null;
    private FtpEnterprisedtWrapper ftpWrapper;

    private BSession session;

    /**
     * @param serverFile
     * @param fichierPTZPrest
     * @return
     */
    private void append(File serverFile, File fichierPTZPrest) throws Exception {
        FileInputStream fis = new FileInputStream(fichierPTZPrest);
        FileOutputStream fos = new FileOutputStream(serverFile.getAbsolutePath(), true);
        byte[] buf = new byte[1024];
        int i = 0;
        while ((i = fis.read(buf)) != -1) {
            fos.write(buf, 0, i);
        }
        fis.close();
        fos.close();
    }

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

    /**
	 * 
	 */
    private void copyFileToFTP() throws Exception {
        File serverFile = null;
        try {
            System.out.println(DateUtils.getTimeStamp() + "The copy's file uploading on another ftp server...");
            FtpEnterprisedtWrapper ftpUtils = getFtpWrapper();
            BApplication app = session.getApplication();
            File fichierPTZPrest = new File(Jade.getInstance().getSharedDir()
                    + session.getApplication().getProperty("ftp.copy.file"));
            String ftpHomeDir = app.getProperty("ftp.copy.homeDir");
            File fileToPost;
            String fileName = app.getProperty("ftp.copy.file");
            String fileAck = app.getProperty("ftp.file.ack");
            if (!ftpUtils.isFileExist(ftpHomeDir, fileAck)) {
                System.out.println(DateUtils.getTimeStamp() + "The acknoledge doesn't exist so load existing file...");
                if (ftpUtils.isFileExist(ftpHomeDir, fichierPTZPrest.getName())) {
                    // il n'y a pas un accusé réception sur la destination --->
                    // le système doit maintenir le fichier existant
                    System.out.println(DateUtils.getTimeStamp() + "The existing ftp file and the local's appending...");
                    serverFile = ftpUtils.getFile(ftpHomeDir, fichierPTZPrest.getName(), Jade.getInstance()
                            .getSharedDir(), fichierPTZPrest.getName() + ".serveurFile");
                    append(serverFile, fichierPTZPrest);
                    System.out.println(DateUtils.getTimeStamp() + "The existing ftp file and the local's appended");
                    fileToPost = new File(serverFile.getAbsolutePath());
                } else {
                    System.out.println(DateUtils.getTimeStamp()
                            + "The existing file doesn't exist so take only the local's file (no append)");
                    fileToPost = new File(fichierPTZPrest.getAbsolutePath());
                }
            } else {
                System.out
                        .println(DateUtils.getTimeStamp() + "The acknoledge exist so delete existing file and ack...");
                ftpUtils.deleteFile(ftpHomeDir, fileName);
                ftpUtils.deleteFile(ftpHomeDir, fileAck);
                fileToPost = new File(fichierPTZPrest.getAbsolutePath());
            }
            System.out.println(DateUtils.getTimeStamp() + "The new copy's file uploading ...");
            ftpUtils.putFile(fileToPost, fileName, ftpHomeDir);
            System.out.println(DateUtils.getTimeStamp() + "The new copy's file uploaded");
            if (fichierPTZPrest != null && fichierPTZPrest.exists()) {
                // fichier posté avec succès --> supression remise à zéro de la
                // "pile"
                System.out.println(DateUtils.getTimeStamp() + "Reinit the local file " + fichierPTZPrest.getName());
                fichierPTZPrest.delete();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.out.println(DateUtils.getTimeStamp() + "Error in file copy file to FTP");
        } finally {
            if (serverFile != null && serverFile.exists()) {
                serverFile.delete();
            }
        }
    }

    /**
     * Retourne l'objet CABvrFtpWrapper qui implémente les méthodes FTP.
     * 
     * @return
     */
    public FtpEnterprisedtWrapper getFtpWrapper() throws Exception {
        if (ftpWrapper == null) {
            BApplication app = session.getApplication();
            ftpWrapper = new FtpEnterprisedtWrapper(session, true, app.getProperty("ftp.copy.login"),
                    app.getProperty("ftp.copy.host"), Integer.parseInt(app.getProperty("ftp.copy.port")));
            ftpWrapper.setPassword(app.getProperty("ftp.copy.password"));
        }
        return ftpWrapper;
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
     * Method setOut.
     */
    private void setOut() throws Exception {
        if (getSession().getApplication().getProperty("zas.log").equals("true")) {
            String outLog =
            // getSession().getApplication().getProperty("zas.home.dir")
            // + "/"
            // + getSession().getApplication().getProperty("zas.log.dir")
            // + "/"
            Jade.getInstance().getLogDir() + DateUtils.getMonthYear() + "/FTPGet/out/"
                    + getSession().getApplication().getProperty("zas.log.ftp.out") + DateUtils.getLocaleDateAndTime()
                    + ".log";
            String errLog =
            // getSession().getApplication().getProperty("zas.home.dir")
            // + "/"
            // + getSession().getApplication().getProperty("zas.log.dir")
            // + "/"
            Jade.getInstance().getLogDir() + DateUtils.getMonthYear() + "/FTPGet/err/"
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
        setOut();
        // init le(s) mails responsable(s)
        emailResponsable = new HEEnvoiEmailsGroupe(getSession(), HEEnvoiEmailsGroupe.responsable_ARC);
        System.out.println(DateUtils.getTimeStamp() + emailResponsable.size()
                + " email(s) pour l'envoi des erreurs d'exécution");
        // **
        FTPUtils ftpUtils = new FTPUtils(session);
        // sauvegarde du fichier si présent
        File fichierPTZ = new File(Jade.getInstance().getSharedDir()
                + session.getApplication().getProperty("ftp.file.input"));
        File ancienPTZ = new File(Jade.getInstance().getSharedDir() + fichierPTZ.getName() + ".old");
        // deuxième file d'attente pour prestation
        File fichierPTZPrest = new File(Jade.getInstance().getSharedDir()
                + session.getApplication().getProperty("ftp.copy.file"));
        File ancienPTZPrest = new File(Jade.getInstance().getSharedDir() + fichierPTZPrest.getName() + ".old");
        if (fichierPTZ.exists()) {
            // il exist donc il faut le sauver sous un nom temporaire
            copyFile(fichierPTZ, ancienPTZ);
        }
        if (fichierPTZPrest.exists()) {
            copyFile(fichierPTZPrest, ancienPTZPrest);
        }
        try {
            // *********************************
            System.out.println(DateUtils.getTimeStamp() + "The file downloading...");
            File f = ftpUtils.getReturnFile();
            if (f != null) {
                System.out.println(DateUtils.getTimeStamp() + "File downloaded successfully");
                System.out.println(" - > " + f.getAbsolutePath());
            }
            if ("true".equals(session.getApplication().getProperty("ftp.file.input.copy"))) {
                System.out.println(DateUtils.getTimeStamp() + "The copy's file downloading...");
                File newFile = ftpUtils.getReturnFile(fichierPTZ.getName(), fichierPTZPrest.getName());
                if (newFile != null) {
                    System.out.println(DateUtils.getTimeStamp() + "Copy file downloaded successfully");
                    System.out.println(" - > " + newFile.getAbsolutePath());
                }
            }
            if ("true".equals(getSession().getApplication().getProperty("ftp.file.ack.put"))) {
                System.out.println("**********************************");
                System.out.println("* Acknowledging data to Centrale *");
                System.out.println("**********************************");
                ftpUtils.acknowledgeTransfer();
            }
            System.out.println(DateUtils.getTimeStamp() + "Process realized successfully");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.out.println(DateUtils.getTimeStamp() + "Error in file download");
            System.out.println(DateUtils.getTimeStamp() + "The File downloaded is deleted");
            // erreur de téléchargement, reprendre l'ancien
            if (fichierPTZ.exists()) {
                fichierPTZ.delete();
            }
            if (ancienPTZ.exists()) {
                System.out.println(DateUtils.getTimeStamp() + "The old file is reloaded");
                copyFile(ancienPTZ, fichierPTZ);
            }
            // erreur de téléchargement, reprendre l'ancien de copie
            if (fichierPTZPrest.exists()) {
                fichierPTZPrest.delete();
            }
            if (ancienPTZPrest.exists()) {
                System.out.println(DateUtils.getTimeStamp() + "The old copy's file is reloaded");
                copyFile(ancienPTZPrest, fichierPTZPrest);
            }
            // être sûr que l'acquittement n'a pas été déposé tout de même
            System.out.println(DateUtils.getTimeStamp() + "Be sure that the acknowledge is not posted");
            try {
                ftpUtils.deleteAcknowlegde();
                System.out.println(DateUtils.getTimeStamp() + "acknowledge deletion checking is ok");
            } catch (Exception err) {
                System.err.println(DateUtils.getTimeStamp() + "Error in deleting the acknoledge : " + err.getMessage());
            }
            // envoi de l'erreur au(x) responsable(s)
            // envoi un email
            emailResponsable.sendMail(getSession().getLabel("HERMES_10018"), e.getMessage());
            //
            throw (e);
        } finally {
            if (ancienPTZ.exists()) {
                ancienPTZ.delete();
            }
            if (ancienPTZPrest.exists()) {
                ancienPTZPrest.delete();
            }
        }
    }
}