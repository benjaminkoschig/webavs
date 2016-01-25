package globaz.amal.process.sedexconverter;

import globaz.amal.process.AMALabstractProcess;
import globaz.amal.process.sedexconverter.utils.AMZipFileUtil;
import globaz.globall.db.BSessionUtil;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.smtp.JadeSmtpClient;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 * Process permettant de convertir les messages Sedex de la version 2.3 en version 2.2
 * 
 * @author bjo
 * 
 */

public class AMSedexConverterProcess extends AMALabstractProcess {
    private static final long serialVersionUID = 1L;

    private static final String DIR_INBOX_ERROR = "/inbox/error";
    private static final String DIR_TREAT_ORIGINAL = "/treat_by_sedex_converter/original";
    private static final String DIR_TREAT_TO_CONVERT = "/treat_by_sedex_converter/to_convert";
    private static final String DIR_TREAT_CONVERTED = "/treat_by_sedex_converter/converted";
    private String dirOutput;
    private String dirSedexBackup;
    private List<String> fileNameConvertedList;

    public static void main(String[] args) {
        AMSedexConverterProcess amSedexConverterProcess = new AMSedexConverterProcess();
        amSedexConverterProcess.setDirOutput(args[0]);
        amSedexConverterProcess.setDirSedexBackup(args[1]);
        amSedexConverterProcess.process();
    }

    @Override
    public String getDescription() {
        return "Converti les messages Sedex qui sont en version 2.3 en version 2.2";
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    protected void process() {
        System.out.println("SedexConverter - start process");
        System.out.println("********************************");

        if (dirSedexBackup == null) {
            throw new IllegalArgumentException("le param�tre dirSedexBackup n'est pas d�fini");
        }

        if (dirOutput == null) {
            throw new IllegalArgumentException("le param�tre dirOutput n'est pas d�fini");
        }

        // initialisation de la liste des fichiers converti
        fileNameConvertedList = new ArrayList<String>();

        System.out.println("dirSedexBackup = " + dirSedexBackup);
        System.out.println("dirOutput = " + dirOutput);

        // r�pertoire contenant les messages SEDEX en erreurs
        File dirError = new File(dirSedexBackup + DIR_INBOX_ERROR);

        // liste contenant tous les fichiers en erreurs
        List<File> listFilesError = new ArrayList<File>(Arrays.asList(dirError.listFiles()));

        unzipAllFilesAndCopy(dirError, listFilesError);

        // si aucun fichier � traiter on sort
        File dirToConvert = new File(dirSedexBackup + DIR_INBOX_ERROR + DIR_TREAT_TO_CONVERT);
        if (!dirToConvert.exists() || dirToConvert.listFiles().length == 0) {
            System.out.println("Aucun fichier � traiter !!!");
            return;
        }

        // Parcourir tout les r�pertoires contenant des messages SEDEX � convertir
        List<File> listDirMessageSedex = new ArrayList<File>(Arrays.asList(dirToConvert.listFiles()));
        for (File dirMessageSedex : listDirMessageSedex) {
            boolean isZipFileConverted = false;
            List<File> listMessageSedex = new ArrayList<File>(Arrays.asList(dirMessageSedex.listFiles()));
            // Convertion de chaque fichier du r�pertoire courant
            for (File messageSedex : listMessageSedex) {
                AMSedexConverter23To22_2 sedexConverter23To22 = new AMSedexConverter23To22_2(messageSedex,
                        dirSedexBackup + DIR_INBOX_ERROR + DIR_TREAT_CONVERTED + "/" + dirMessageSedex.getName());
                boolean isFileConverted = sedexConverter23To22.convert();
                if (isFileConverted) {
                    fileNameConvertedList.add(dirMessageSedex.getName() + ".zip-->" + messageSedex.getName());
                    isZipFileConverted = true;
                }
            }

            // zipper tout le r�pertoire converti correspondant au r�pertoire courant
            File outZipFile = zipConvertedDirectory(dirMessageSedex);

            // suppression du dossier dans le r�pertoire to_convert
            deleteFile(dirMessageSedex);

            // si le fichier zip a �t� modifi� on le d�pose sur le sm-client
            if (isZipFileConverted) {
                // copie du fichier zip dans le r�pertoire de sortie (ftp ou local)
                if (dirOutput.startsWith("sftp:")) {
                    try {
                        JadeFsFacade.copyFile(outZipFile.getPath(), dirOutput + "/" + outZipFile.getName());
                    } catch (Exception e) {
                        System.out.println("impossible de copier le fichier " + outZipFile.getName() + " sur "
                                + dirOutput);
                        e.printStackTrace();
                    }
                } else {
                    File dest = new File(dirOutput + File.separator + outZipFile.getName());
                    copyFile(outZipFile, dest);
                }
                System.out.println("copie du fichier : " + outZipFile.getName() + " sur le sm-client termin�");
            }
        }

        // suppression des fichiers de base (dans r�pertoire error) si converti
        for (String fileNameConverted : fileNameConvertedList) {
            String split[] = fileNameConverted.split("-->");
            String zipFileName = split[0];
            File fileToDelete = new File(dirError + File.separator + zipFileName);
            deleteFile(fileToDelete);
        }

        if (fileNameConvertedList.size() > 0) {
            for (String fileName : fileNameConvertedList) {
                System.out.println("les fichiers suivants ont �t� modifi�s : \n");
                System.out.println(fileName);
            }
            sendMail();
        }

        System.out.println("********************************");
        System.out.println("SedexConverter - end of process");
    }

    private void sendMail() {
        String message = "les fichiers suivants ont �t� modifi�s : \n\n";
        for (String fileName : fileNameConvertedList) {
            message += "\n" + fileName;
        }

        try {
            String userEmail = BSessionUtil.getSessionFromThreadContext().getUserEMail();
            JadeSmtpClient.getInstance().sendMail(userEmail, "Convertion des fichiers SEDEX 2.3 en 2.2", message, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Zip tout le r�pertoire converti.
     * 
     * @param dirMessageSedex
     * @return
     */
    private File zipConvertedDirectory(File dirMessageSedex) {
        File outZipFile = new File(dirSedexBackup + DIR_INBOX_ERROR + DIR_TREAT_CONVERTED + File.separator
                + dirMessageSedex.getName() + ".zip");
        File srcDir = new File(dirSedexBackup + DIR_INBOX_ERROR + DIR_TREAT_CONVERTED + File.separator
                + dirMessageSedex.getName());
        AMZipFileUtil.zip(srcDir, outZipFile);
        return outZipFile;
    }

    /**
     * Extrait tous les fichiers de tous les fichiers "zip" dans le r�pertoire "to_convert" et copie les
     * fichiers originaux dans le r�pertoire "original"
     * 
     * @param dirError
     * @param listFilesError
     */
    private void unzipAllFilesAndCopy(File dirError, List<File> listFilesError) {
        // parcours des fichiers en erreurs et d�zippage
        for (File fileError : listFilesError) {
            // si il s'agit d'un fichier de message SEDEX (zip)
            if (fileError.getName().endsWith(".zip")) {
                System.out.println("copie et d�zippage du fichier : " + fileError.getName());
                // copie du fichier dans le r�pertoire "original"
                File destFile = new File(dirError + File.separator + DIR_TREAT_ORIGINAL + File.separator
                        + fileError.getName());
                copyFile(fileError, destFile);

                // extraction des fichiers contenu dans le zip dans le r�pertoire "to_convert"
                unzipFile(fileError);
            }
        }
    }

    /**
     * Extrait les fichiers contenu dans le fichier ZIP
     * 
     * @param srcZipFile
     */
    private void unzipFile(File srcZipFile) {
        try {
            String nameWithoutExtension[] = srcZipFile.getName().split("\\.");
            String destDirPath = dirSedexBackup + DIR_INBOX_ERROR + DIR_TREAT_TO_CONVERT + File.separator
                    + nameWithoutExtension[0];
            File destDir = new File(destDirPath);
            AMZipFileUtil.unzip(srcZipFile, destDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Copie le fichier src dans la cible dest
     * 
     * @param srcFile
     * @param destFile
     */
    private void copyFile(File srcFile, File destFile) {
        try {
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Supprime le fichier passs� en param�tre
     * 
     * @param file
     */
    private void deleteFile(File file) {
        System.out.println("Suppression du fichier ou r�pertoire : " + file.getPath());
        FileUtils.deleteQuietly(file);
    }

    public String getDirOutput() {
        return dirOutput;
    }

    public void setDirOutput(String dirOutput) {
        this.dirOutput = dirOutput;
    }

    public String getDirSedexBackup() {
        return dirSedexBackup;
    }

    public void setDirSedexBackup(String dirSedexBackup) {
        this.dirSedexBackup = dirSedexBackup;
    }
}
