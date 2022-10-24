package ch.globaz.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class ZipUtils {
    private ZipUtils(){
    }

    public static void zip(Path zipFile, Path zipDir) {
        if (Files.exists(zipFile)) {throw new IllegalArgumentException("file zip already exists.");}
        if (!Files.exists(zipDir) || !Files.isDirectory(zipDir)) {throw new IllegalArgumentException("source dir is not correctly.");}

        try {
            OutputStream os = Files.newOutputStream(zipFile.toFile().toPath());
            ZipOutputStream zipOut = new ZipOutputStream(os);


            try (DirectoryStream<Path> stream = Files.newDirectoryStream(zipDir)) {
                for (Path path : stream) {
                    if (Files.isDirectory(path)) {
                        addDirectory("", path, zipOut);
                    } else {
                        addFile("", path, zipOut);
                    }
                }
            }
            zipOut.close();
            os.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void addFile(String parentPath, Path path, ZipOutputStream zipOut) throws RuntimeException {
        ZipEntry zipEntry;

        try {
            try (FileInputStream fileTarget = new FileInputStream(path.toFile())){
                zipEntry = new ZipEntry(parentPath + path.getFileName().toString());
                zipOut.putNextEntry(zipEntry);
                byte[] bytes = new byte[1024];
                int length;
                while ((length = fileTarget.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void addDirectory(String parentPath, Path path, ZipOutputStream zipOut) throws RuntimeException {
        try {
            String pathDir = parentPath + path.getFileName().toString() + File.separator;
            zipOut.putNextEntry(new ZipEntry(pathDir));
            zipOut.closeEntry();

            try (Stream<Path> stream = Files.list(path)) {
                stream.forEach(subPath -> {
                    if (Files.isDirectory(subPath)) {
                        addDirectory(pathDir + path.getFileName().toString() + File.separator, subPath, zipOut);
                    } else {
                        addFile(pathDir, subPath, zipOut);
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void unZip(Path zipFile, Path destDir) {
        final int bufferSize = 2048;

        try {
            int count;
            BufferedOutputStream bufferedOutputStream;
            FileInputStream fis =new FileInputStream(zipFile.toFile());
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));

            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                //cr�ation des sous-dossiers
                if (entry.getName().contains("/")) {
                    String[] fullDirPart = entry.getName().split("/");
                    String[] dirPart = new String[fullDirPart.length - 1];
                    System.arraycopy(fullDirPart, 0, dirPart, 0, fullDirPart.length - 1);
                    String partDir = String.join(File.separator, dirPart);
                    Path pathDir = Paths.get(destDir + File.separator + partDir);
                    if (!Files.exists(pathDir)) {
                        Files.createDirectories(pathDir);
                    }
                }
                byte[] data = new byte[bufferSize];

                FileOutputStream fos = new FileOutputStream(Paths.get(destDir + File.separator + entry.getName()).toFile());
                bufferedOutputStream = new BufferedOutputStream(fos, bufferSize);
                while ((count = zis.read(data, 0, bufferSize)) != -1) {
                    bufferedOutputStream.write(data, 0, count);
                }
                bufferedOutputStream.flush();
                bufferedOutputStream.close();
            }
            zis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
