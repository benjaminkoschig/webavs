package globaz.amal.process.sedexconverter.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class AMZipFileUtil {

    /**
     * Permet de d�zipper un fichier re�u en param�tre. Les fichiers sont d�pos�s dans le r�pertoire de sortie sp�cifi�
     * en param�tre
     * 
     * @param srcZipFile : fichier � d�zipper
     * @param destDir : r�pertoire dans lequel d�poser les fichiers
     */
    public static void unzip(File srcZipFile, File destDir) {
        final int bufferSize = 2048;

        try {
            BufferedOutputStream bufferedOutputStream = null;
            FileInputStream fis = new FileInputStream(srcZipFile);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                // System.out.println("Extraction du fichier : " + entry);
                int count;
                byte data[] = new byte[bufferSize];

                File newFile = new File(destDir + File.separator + entry.getName());
                // cr�ation des r�pertoires parents du fichier si ceux-ci n'existent pas
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);
                bufferedOutputStream = new BufferedOutputStream(fos, bufferSize);
                while ((count = zis.read(data, 0, bufferSize)) != -1) {
                    bufferedOutputStream.write(data, 0, count);
                }

                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            }
            zis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet de zipper un r�pertoire. Attention, les sous dossiers ne sont pas g�r�s
     * 
     * @param srcDir
     * @param destZipFile
     */
    public static void zip(File srcDir, File destZipFile) {
        final int bufferSize = 2048;

        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(destZipFile);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            // out.setMethod(ZipOutputStream.DEFLATED);
            byte data[] = new byte[bufferSize];
            String files[] = srcDir.list();

            for (int i = 0; i < files.length; i++) {
                // System.out.println("Adding: " + srcDir + File.separator + files[i]);
                FileInputStream fi = new FileInputStream(srcDir + File.separator + files[i]);
                origin = new BufferedInputStream(fi, bufferSize);
                ZipEntry entry = new ZipEntry(files[i]);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, bufferSize)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
