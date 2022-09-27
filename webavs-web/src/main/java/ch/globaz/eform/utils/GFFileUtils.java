package ch.globaz.eform.utils;

import ch.globaz.common.util.Dates;
import ch.globaz.common.util.NSSUtils;
import ch.globaz.common.util.ZipUtils;
import ch.globaz.eform.business.models.GFFormulaireModel;
import ch.globaz.eform.business.models.sedex.GFSedexModel;
import globaz.eform.vb.envoi.GFEnvoiViewBean;
import globaz.jade.common.Jade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
public class GFFileUtils {
    public final static String WORK_PATH = Jade.getInstance().getHomeDir() + "work/";
    public final static String PERSISTANCE_PATH = Jade.getInstance().getPersistenceDir();
    public final static String FILE_TYPE_PDF = "pdf";
    public final static String FILE_TYPE_TIFF = "tiff";
    public final static String FILE_TYPE_ZIP = "zip";

    public static void downloadFile(HttpServletResponse response, String name, byte[] buf) throws IOException {
        OutputStream os = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename=" + name);
        os.write(buf);
        os.close();
    }

    public static void uploadFile(GFEnvoiViewBean viewBean) throws Exception {
        Path filePath = Paths.get(PERSISTANCE_PATH + viewBean.getFileNamePersistance());
        Path workDir = Paths.get(WORK_PATH + viewBean.getFolderUid());

        Files.createDirectories(workDir);

        if (Files.exists(filePath))
        {
            String extension = FilenameUtils.getExtension(filePath.getFileName().toString());
            if (FILE_TYPE_ZIP.equalsIgnoreCase(extension)) {
                Path zipDir = Paths.get(workDir.toAbsolutePath() + File.separator + FilenameUtils.removeExtension(filePath.getFileName().toString()));

                Files.createDirectories(zipDir);

                ZipUtils.unZip(filePath, zipDir);

                checkFile(viewBean, zipDir);
                injectFiles(viewBean, workDir, zipDir);

                Files.delete(zipDir);
            } else if (extension.equals(FILE_TYPE_PDF) || extension.equals(FILE_TYPE_TIFF)) {
                String originalFilename = Paths.get(viewBean.getFilename()).getFileName().toString();

                if (isContains(viewBean.getFileNameList(), originalFilename)) {
                    List<Integer> allIndex = indexOfAll(viewBean.getFileNameList(), originalFilename);

                    if (allIndex.size() == 1) {
                        String renamedFileName = renameFile(1, viewBean.getFileNameList().get(allIndex.get(0)));

                        Path existantFile = Paths.get(workDir.toAbsolutePath() + File.separator + viewBean.getFileNameList().get(allIndex.get(0)));
                        Path renamedFile = Paths.get(workDir.toAbsolutePath() + File.separator + renamedFileName);

                        Files.move(existantFile, renamedFile);

                        viewBean.getFileNameList().set(allIndex.get(0), renamedFileName);
                    } else {
                        String renamedFileName = renameFile(allIndex.size() + 1, originalFilename);

                        Files.move(filePath, Paths.get(workDir.toAbsolutePath() + File.separator + renamedFileName));
                        viewBean.getFileNameList().add(renamedFileName);
                    };
                } else {
                    Files.move(filePath, Paths.get(workDir.toAbsolutePath() + File.separator + originalFilename));
                    viewBean.getFileNameList().add(originalFilename);
                }
            } else {
                viewBean.getSession().getCurrentThreadTransaction().addErrors("mauvais type de fichier");
            }
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        }
    }

    private static void checkFile(GFEnvoiViewBean viewBean, Path zipDir) {
        try {
            try (Stream<Path> streamZipDir = Files.list(zipDir)) {
                streamZipDir.forEach(extractFilePath -> {
                    String extension = FilenameUtils.getExtension(extractFilePath.getFileName().toString());

                    switch (extension) {
                        case "pdf":
                        case "tiff":
                            break;
                        default:
                            viewBean.getErrorFileNameList().add(extractFilePath.getFileName().toString());
                            try {
                                Files.delete(extractFilePath);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void injectFiles(GFEnvoiViewBean viewBean, Path workDir, Path zipDir) {
        try {
            try (Stream<Path> streamZipDir = Files.list(zipDir)) {
                streamZipDir.forEach(extractFile -> {
                    try {
                        String fileName = extractFile.getFileName().toString();
                        if (isContains(viewBean.getFileNameList(), fileName)) {
                            List<Integer> allIndex = indexOfAll(viewBean.getFileNameList(), fileName);
                            String renamedFileName;

                            //Renommage du fichier
                            if (allIndex.size() == 1) {
                                renamedFileName = renameFile(1, viewBean.getFileNameList().get(allIndex.get(0)));

                                Path existantFile = Paths.get(workDir.toAbsolutePath() + File.separator + viewBean.getFileNameList().get(allIndex.get(0)));
                                Path renamedFile = Paths.get(workDir.toAbsolutePath() + File.separator + renamedFileName);

                                Files.move(existantFile, renamedFile);

                                viewBean.getFileNameList().set(allIndex.get(0), renamedFileName);
                            }
                            renamedFileName = renameFile(allIndex.size() + 1, fileName);
                            viewBean.getFileNameList().add(renamedFileName);

                            Files.move(extractFile, Paths.get(workDir.toAbsolutePath() + File.separator + renamedFileName));
                        } else {
                            viewBean.getFileNameList().add(fileName);
                            Files.move(extractFile, Paths.get(workDir.toAbsolutePath() + File.separator + fileName));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isContains(List<String> fileList, String file) {
        String extention = FilenameUtils.getExtension(file);
        String name = FilenameUtils.removeExtension(file);

        if (fileList.contains(file)) {
            return true;
        } else {
            Pattern pdfFilePattern;
            if ("pdf".equalsIgnoreCase(extention)) {
                pdfFilePattern = Pattern.compile(name + "_([1-9]+)\\.pdf$");
            } else {
                pdfFilePattern = Pattern.compile(name + "_([1-9]+)(_[pP][1-9]+)\\.tiff$");
            }

            return fileList.stream().anyMatch(fileInjected -> {
                Matcher mFileInjected = pdfFilePattern.matcher(fileInjected);
                return mFileInjected.find();
            });
        }
    }

    private static List<Integer> indexOfAll(List<String> fileList, String file){
        List<Integer> index = new ArrayList<>();

        if (fileList.contains(file)) {
            index.add(fileList.indexOf(file));
        } else {
            String extention = FilenameUtils.getExtension(file);
            String name = FilenameUtils.removeExtension(file);

            Pattern pdfFilePattern;
            if ("pdf".equalsIgnoreCase(extention)) {
                pdfFilePattern = Pattern.compile(name + "_([1-9]+)\\.pdf$");
            } else {
                pdfFilePattern = Pattern.compile(name + "_([1-9]+)(_[pP][1-9]+)\\.tiff$");
            }

            fileList.forEach(fileInjected -> {
                Matcher mFileInjected = pdfFilePattern.matcher(fileInjected);

                if (mFileInjected.find()) {
                    index.add(fileList.indexOf(fileInjected));
                }
            });
        }

        return index;
    }

    private static String renameFile(Integer index, String file) {
        String extention = FilenameUtils.getExtension(file);

        Pattern pdfFilePattern;
        if ("pdf".equalsIgnoreCase(extention)) {
            pdfFilePattern = Pattern.compile("(.*)\\.pdf$");
            Matcher mFileInjected = pdfFilePattern.matcher(file);

            if (mFileInjected.find()) {
                return mFileInjected.group(1) + "_" + index + ".pdf";
            }
        } else {
            pdfFilePattern = Pattern.compile("(.*)(_[pP][1-9]+)\\.tiff$");
            Matcher mFileInjected = pdfFilePattern.matcher(file);

            if (mFileInjected.find()) {
                return mFileInjected.group(1) + "_" + index + mFileInjected.group(2) + ".tiff";
            }
        }

        return file;
    }

    public static void deleteFile(GFEnvoiViewBean viewBean, String fileNameToRemove) throws Exception {
        Path deletedFile = Paths.get(GFFileUtils.WORK_PATH + viewBean.getFolderUid() + File.separator + fileNameToRemove);

        if (Files.exists(deletedFile)) {
            Files.delete(deletedFile);
        }

        viewBean.getFileNameList().remove(fileNameToRemove);
    }

    public static String generateEFormFilePath(GFFormulaireModel dbModel) {
        LocalDate date = Dates.toDate(dbModel.getDate());

        return date == null ? "" : date.getYear() + File.separator + date.getMonth().getValue() + File.separator + date.getDayOfMonth() + File.separator;
    }

    public static String generateEFormFilePath(GFSedexModel model) {
        LocalDate date = model.getMessageDate();

        return date == null ? "" : date.getYear() + File.separator + date.getMonth().getValue() + File.separator + date.getDayOfMonth() + File.separator;
    }

    public static String generateDaDossierFilePath(LocalDate date, String nssAffilier) {
        return date.getYear() + File.separator + date.getMonth().getValue() + File.separator + date.getDayOfMonth() + File.separator + NSSUtils.formatNss(nssAffilier) + File.separator;
    }
}
