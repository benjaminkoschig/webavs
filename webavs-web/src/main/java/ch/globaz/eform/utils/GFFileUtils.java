package ch.globaz.eform.utils;

import globaz.eform.vb.envoi.GFEnvoiViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public class GFFileUtils {

    public static void downloadFile(HttpServletResponse response, String name, byte buf[]) throws IOException {
        OutputStream os = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename=" + name);
        os.write(buf);
        os.close();
    }

    public static void uploadFile(GFEnvoiViewBean viewBean) throws Exception {
        String path = viewBean.getFilename();
        String filename = path.substring(path.lastIndexOf("\\") + 1);
        if (!JadeStringUtil.isNull(filename)) {
            String extension = FilenameUtils.getExtension(filename);
            if (extension.equals("zip")) {
                JadeFsFacade.copyFile(viewBean.getFilename(), Jade.getInstance().getPersistenceDir() + viewBean.getFileNamePersistance());
                viewBean.getFileNameList().addAll(unZipFile(viewBean.getFileNamePersistance()));
            } else if (extension.equals("pdf") || extension.equals("tiff")) {
                JadeFsFacade.copyFile(path, Jade.getInstance().getHomeDir() + "work/" + filename);
                viewBean.getFileNameList().add(filename);
            } else {
                viewBean.getSession().addError("mauvais type de fichier");
                return;
            }
            // suppression des doublons
            viewBean.setFileNameList(viewBean.getFileNameList().stream().distinct().collect(Collectors.toList()));
        }
    }

    public static List<String> unZipFile(String filename) throws Exception {
        String pathWork = Jade.getInstance().getPersistenceDir() + filename;
        List<String> fileNameList = new LinkedList<>();
        if (!JadeStringUtil.isNull(pathWork)) {
            FileInputStream fis = null;
            ZipInputStream zipIs = null;
            try {
                fis = new FileInputStream(pathWork);
                zipIs = new ZipInputStream(new BufferedInputStream(fis));
                File destDir = new File(Jade.getInstance().getHomeDir() + "work/");
                fileNameList = unzip(new File(pathWork), destDir);
            } finally {
                fis.close();
                zipIs.close();
            }
        }
        return fileNameList;
    }

    public static List<String> unzip(File srcZipFile, File destDir) {
        final int bufferSize = 2048;
        List<String> unzipFiles = new ArrayList<>();

        try {
            BufferedOutputStream bufferedOutputStream = null;
            FileInputStream fis = new FileInputStream(srcZipFile);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                int count;
                byte data[] = new byte[bufferSize];

                File newFile = new File(destDir + File.separator + entry.getName());
                // création des répertoires parents du fichier si ceux-ci n'existent pas
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);
                bufferedOutputStream = new BufferedOutputStream(fos, bufferSize);
                while ((count = zis.read(data, 0, bufferSize)) != -1) {
                    bufferedOutputStream.write(data, 0, count);
                }

                bufferedOutputStream.flush();
                bufferedOutputStream.close();
                unzipFiles.add(newFile.getName());
            }
            zis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return unzipFiles;
    }

    public static void deleteFile(GFEnvoiViewBean viewBean, String fileNameToRemove) throws JadeServiceActivatorException, JadeClassCastException, JadeServiceLocatorException {
        String pathFileNameToRemove = Jade.getInstance().getHomeDir() + "work/" + fileNameToRemove;
        viewBean.getFileNameList().remove(fileNameToRemove);
        JadeFsFacade.delete(pathFileNameToRemove);
    }

}
