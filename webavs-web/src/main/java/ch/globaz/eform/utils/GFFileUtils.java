package ch.globaz.eform.utils;

import ch.globaz.common.util.Dates;
import ch.globaz.common.util.NSSUtils;
import ch.globaz.common.util.ZipUtils;
import ch.globaz.eform.business.models.GFDaDossierModel;
import ch.globaz.eform.business.models.GFFormulaireModel;
import ch.globaz.eform.business.models.sedex.GFSedexModel;

import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.Message;
import globaz.eform.vb.envoi.GFEnvoiViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.common.JadeClassCastException;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.service.exception.JadeServiceActivatorException;
import globaz.jade.service.exception.JadeServiceLocatorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public class GFFileUtils {
    public final static String WORK_PATH = Jade.getInstance().getHomeDir() + "work/";
    public final static String PERSISTANCE_PATH = Jade.getInstance().getPersistenceDir();
    public final static String FILE_TYPE_PDF = "pdf";
    public final static String FILE_TYPE_TIFF = "tiff";
    public final static String FILE_TYPE_ZIP = "zip";
    public static Map<String, Integer> counterMap = new HashMap<>();

    public static void downloadFile(HttpServletResponse response, String name, byte[] buf) throws IOException {
        OutputStream os = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename=" + name);
        os.write(buf);
        os.close();
    }

    public static void uploadFile(GFEnvoiViewBean viewBean) throws Exception {
        String path = viewBean.getFilename();
        Files.createDirectories(Paths.get(WORK_PATH + viewBean.getFolderUid()));
        String filename = path.substring(path.lastIndexOf("\\") + 1);
        String destDir = WORK_PATH + viewBean.getFolderUid();
        if (!JadeStringUtil.isNull(filename)) {
            String extension = FilenameUtils.getExtension(filename);
            if (extension.equals(FILE_TYPE_ZIP)) {
                JadeFsFacade.copyFile(viewBean.getFilename(), PERSISTANCE_PATH + viewBean.getFileNamePersistance());
                viewBean.getFileNameList().addAll(unZipFile(viewBean));
                checkUnZippedFiles(viewBean);
            } else if (extension.equals(FILE_TYPE_PDF) || extension.equals(FILE_TYPE_TIFF)) {
                if (isFileExist(filename, viewBean.getFileNameList())) {
                    filename = renameFileWithSameName(filename);
                }
                JadeFsFacade.copyFile(path, destDir + File.separator + filename);
                viewBean.getFileNameList().add(filename);
            } else {
                viewBean.getSession().getCurrentThreadTransaction().addErrors("mauvais type de fichier");
                return;
            }
            // suppression des doublons
            viewBean.setFileNameList(viewBean.getFileNameList().stream().distinct().collect(Collectors.toList()));
        }
    }

    public static List<String> unZipFile(GFEnvoiViewBean viewBean) throws Exception {
        String filename = viewBean.getFileNamePersistance();
        String pathWork = PERSISTANCE_PATH + filename;
        List<String> fileNameList = new LinkedList<>();
        if (!JadeStringUtil.isNull(pathWork)) {
            //creation des directories pour extraire les fichiers contenus dans le zip
            Files.createDirectories(Paths.get(WORK_PATH + viewBean.getFolderUid()));
            Files.createDirectories(Paths.get(WORK_PATH + viewBean.getFolderUid() + "/" + FilenameUtils.removeExtension(filename)));
            File destDir = new File(WORK_PATH + viewBean.getFolderUid() + "/" + FilenameUtils.removeExtension(filename));
            fileNameList = unzip(new File(pathWork), destDir, viewBean);
        }
        return fileNameList;
    }

    public static List<String> unzip(File srcZipFile, File destDir, GFEnvoiViewBean viewBean) {
        final int bufferSize = 2048;
        List<String> unzipFiles = new ArrayList<>();

        try {
            BufferedOutputStream bufferedOutputStream;
            FileInputStream fis = new FileInputStream(srcZipFile);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            File newFile;
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                int count;
                byte[] data = new byte[bufferSize];
                if (isFileExist(entry.getName(), viewBean.getFileNameList())) {
                    newFile = renameFileWithSameNameInZip(entry.getName(), destDir);
                } else {
                    newFile = new File(destDir + File.separator + entry.getName());
                }
                // cr�ation des r�pertoires parents du fichier si ceux-ci n'existent pas
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

    public static boolean isFileExist(String fileName, List<String> fileNamesList) {
        return fileNamesList.contains(fileName);
    }

    public static void deleteFile(GFEnvoiViewBean viewBean, String fileNameToRemove) throws JadeServiceActivatorException, JadeClassCastException, JadeServiceLocatorException {
        String pathFileNameToRemove = null;
        Collection<File> files = listFile(GFFileUtils.WORK_PATH + viewBean.getFolderUid());

        for (Iterator iterator = files.iterator(); iterator.hasNext(); ) {
            File file = (File) iterator.next();
            if (file.getName().equals(fileNameToRemove)) {
                pathFileNameToRemove = file.getAbsolutePath();
                JadeFsFacade.delete(pathFileNameToRemove);
                if (file.getParentFile().listFiles().length == 0) {
                    JadeFsFacade.delete(file.getParentFile().toString());
                }
            }
        }
        viewBean.getFileNameList().remove(fileNameToRemove);
    }

    public static Collection<File> listFile(String path) {
        File directory = new File(String.valueOf(Paths.get(path)));
        Collection<File> files = FileUtils.listFiles(directory, null, true);
        return files;
    }

    public static void checkUnZippedFiles(GFEnvoiViewBean viewBean) throws JadeServiceActivatorException, JadeClassCastException, JadeServiceLocatorException {
        List<String> fileNames = viewBean.getFileNameList();
        for (String fileName : fileNames) {
            String extension = FilenameUtils.getExtension(fileName);
            if (!extension.equals(FILE_TYPE_PDF) && !extension.equals(FILE_TYPE_TIFF)) {
                viewBean.getErrorFileNameList().add(fileName);
                viewBean.getFileNameList().remove(fileName);
                deleteFile(viewBean, fileName);
            }
        }
        // suppression des doublons
        viewBean.setErrorFileNameList(viewBean.getErrorFileNameList().stream().distinct().collect(Collectors.toList()));
        if (viewBean.getErrorFileNameList().size() > 0) {
            viewBean.getSession().getCurrentThreadTransaction().addErrors(("les documents en rouges ne respectent pas le format requis et ne seront pas pris en compte pour l'envoi."));
        }
    }

    public static void createSedexXml(Message message, String sedexOutPut) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Message.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // Chemin en dur � changer
            File file = new File(sedexOutPut + "/message_00102.xml");
            jaxbMarshaller.marshal(message, file);

        } catch (JAXBException e) {
//            LOGGER.error("Error : impossible de cr�er le fichier xml.");
        }
    }


    public static void zipSedexFile(Path destDir) {
        ZipUtils.zip(Paths.get(destDir + ".zip"), destDir);
    }

    public static String generateEFormFilePath(GFFormulaireModel dbModel) {
        LocalDate date = Dates.toDate(dbModel.getDate());

        return date == null ? "" : date.getYear() + File.separator + date.getMonth().getValue() + File.separator + date.getDayOfMonth() + File.separator;
    }

    public static String generateEFormFilePath(GFSedexModel model) {
        LocalDate date = model.getMessageDate();

        return date == null ? "" : date.getYear() + File.separator + date.getMonth().getValue() + File.separator + date.getDayOfMonth() + File.separator;
    }

    public static String generateDaDossierFilePath(GFDaDossierModel dbModel) {
        LocalDate date = Dates.extractSpyDate(dbModel.getCreationSpy());

        return date.getYear() + File.separator + date.getMonth().getValue() + File.separator + date.getDayOfMonth() + File.separator + NSSUtils.formatNss(dbModel.getNssAffilier()) + File.separator;
    }

    public static String generateDaDossierFilePath(GFSedexModel model) {
        LocalDate date = model.getMessageDate();

        return date == null ? "" : date.getYear() + File.separator + date.getMonth().getValue() + File.separator + date.getDayOfMonth() + File.separator + NSSUtils.formatNss(model.getNssBeneficiaire()) + File.separator;
    }

    public static File renameFileWithSameNameInZip(String fileName, File destDir) {
        counterMap.merge(fileName, 1, Integer::sum);
        File newFile = new File(destDir + File.separator + FilenameUtils.removeExtension(fileName) + "_" + counterMap.get(fileName) + "." + FilenameUtils.getExtension(fileName));
        return newFile;
    }

    public static String renameFileWithSameName(String fileName) {
        counterMap.merge(fileName, 1, Integer::sum);
        return FilenameUtils.removeExtension(fileName) + "_" + counterMap.get(fileName) + "." + FilenameUtils.getExtension(fileName);
    }
}
