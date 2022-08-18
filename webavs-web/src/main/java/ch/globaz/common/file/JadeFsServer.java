package ch.globaz.common.file;

import ch.globaz.common.file.exception.FSOperationException;
import globaz.jade.common.Jade;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JadeFsServer {
    public void write(InputStream source, String filename, String path) {
        try {
            File file = new File(Jade.getInstance().getPersistenceDir() + filename);
            FileUtils.copyInputStreamToFile(source, file);
            write(file, path);
        } catch (Throwable e) {
            throw new FSOperationException("JadeFtpServer#write : error in the write file operation", e);
        }
    }

    public void write(File source, String path) {
        try {
            File dest = new File(path + source.getName());
            FileUtils.copyFile(source, dest);
        } catch (Throwable e) {
            throw new FSOperationException("JadeFtpServer#write : error in the write file operation", e);
        }
    }

    public void write(Path source, String path) {
        try {
            write(source.toFile(), path);
        } catch (Throwable e) {
            throw new FSOperationException("JadeFtpServer#write : error in the write file operation", e);
        }
    }

    public void rename(File oldFile, File newFile) {
        try {
            if (oldFile.exists() && oldFile.isFile()) {
                oldFile.renameTo(newFile);
            }
        } catch (Throwable e) {
            throw new FSOperationException("JadeFsService#rename : Unable to rename the file " + oldFile.getName() + " to " + newFile.getName() + ": ", e);
        }
    }

    public void rename(Path oldFile, Path newFile) {
        try {
            if (Files.exists(oldFile) && Files.isDirectory(oldFile)) {

                oldFile.toFile().renameTo(newFile.toFile());
            }
        } catch (Throwable e) {
            throw new FSOperationException("JadeFsService#rename : Unable to rename the file " + oldFile.getFileName() + " to " + newFile.getFileName() + ": ", e);
        }
    }

    public File read(Path path) {
        if (Files.exists(path) && !Files.isDirectory(path)) {
            return path.toFile();
        }
        throw new FSOperationException("JadeFtpServer#read : Unable to read file");
    }

    public File read(String path, String fileName) {
        return read(path(path, fileName));
    }

    public void delete(Path path) {
        try {
            if (Files.exists(path) && !Files.isDirectory(path)) {
                Files.delete(path);
            }
        } catch (Throwable e) {
            throw new FSOperationException("JadeFtpServer#delete : Unable to delete file");
        }
    }

    public void delete(String path, String fileName) {
        delete(path(path, fileName));
    }

    public boolean exist(Path path) {
        return Files.exists(path);
    }

    public boolean exist(String path) {
        return Files.exists(path(path));
    }

    public void createDirectories(String path) {
        createDirectories(path(path));
    }

    public void createDirectories(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new FSOperationException("JadeFtpServer#createFolder : Unable to create directory", e);
            }
        }
        throw new FSOperationException("JadeFtpServer#createFolder : Unable to create directory");
    }

    @SuppressWarnings("unchecked")
    public void deleteDirectories(Path path, boolean recursive) {
        if (Files.exists(path)) {
            if (Files.isDirectory(path)) {
                try {
                    if (recursive) {
                        try (DirectoryStream<Path> streamPath = Files.newDirectoryStream(path)) {
                            for (Path subPath : streamPath) {
                                if (!Files.isDirectory(subPath)) {
                                    Files.delete(subPath);
                                } else {
                                    deleteDirectories(subPath, true);
                                }
                            }
                        }
                    }
                    Files.deleteIfExists(path);
                    return;
                } catch (IOException e) {
                    throw new FSOperationException("JadeFtpServer#delete : Unable to delete directory", e);
                }
            }
            throw new FSOperationException("JadeFtpServer#delete : this path is not a directory");
        }
        throw new FSOperationException("JadeFtpServer#delete : directory is not found");
    }

    public void deleteDirectories(String path, boolean recursive) {
        deleteDirectories(path(path), recursive);
    }

    public void renameFolder(Path oldName, Path newName) {
        if (Files.exists(oldName)) {
            if (Files.isDirectory(oldName)) {
                if (!Files.exists(newName)) {
                    oldName.toFile().renameTo(newName.toFile());
                    return;
                }
                throw new FSOperationException("JadeFtpServer#renameFolder : the directory already exist");
            }
            throw new FSOperationException("JadeFtpServer#renameFolder : this path is not a directory");
        }
        throw new FSOperationException("JadeFtpServer#renameFolder : directory is not found");
    }

    public void renameFolder(String oldName, String newName) {
        renameFolder(path(oldName), path(newName));
    }

    public boolean existFolder(Path path) {
        return Files.exists(path);
    }

    public boolean existFolder(String path) {
        return existFolder(path(path));
    }

    public List<String> list(Path path) {
        try {
            try (Stream<Path> dirStream = Files.list(path)) {
                return dirStream.map(subPath -> subPath.getFileName().toString()).collect(Collectors.toList());
            }
        } catch (IOException e) {
            throw new FSOperationException("JadeFtpServer#list : directory is not found");
        }
    }

    public List<String> list(String path) {
        return list(path(path));
    }

    private Path path(String path, String filename) {
        String absolutPath = path.endsWith(File.separator) ? path + filename : path + File.separator + filename ;
        return Paths.get(absolutPath);
    }

    private Path path(String path) {
        String absolutPath = path.endsWith(File.separator) ? path : path + File.separator ;
        return Paths.get(absolutPath);
    }
}
