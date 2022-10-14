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
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JadeFsServer {
    public void write(InputStream source, String filename, String path) {
        try {
            File file = new File(Jade.getInstance().getPersistenceDir() + filename);
            FileUtils.copyInputStreamToFile(source, file);
            write(file, path);
        } catch (Throwable e) {
            throw new FSOperationException("JadeFsServer#write : error in the write file operation", e);
        }
    }

    public void write(File source, String path) {
        try {
            File dest = new File(path + source.getName());
            FileUtils.copyFile(source, dest);

            Set<PosixFilePermission> perms = new HashSet<>();
            perms.add(PosixFilePermission.OWNER_READ);
            perms.add(PosixFilePermission.OWNER_WRITE);
            perms.add(PosixFilePermission.OWNER_EXECUTE);

            perms.add(PosixFilePermission.OTHERS_READ);
            perms.add(PosixFilePermission.OTHERS_WRITE);
            perms.add(PosixFilePermission.OTHERS_EXECUTE);

            try {
                Files.setPosixFilePermissions(dest.toPath(), perms);
            } catch (Exception e) {
                //System non compatible posix
                dest.setReadable(true, false);
                dest.setWritable(true, false);
                dest.setExecutable(true, false);
            }
        } catch (Throwable e) {
            throw new FSOperationException("JadeFsServer#write : error in the write file operation", e);
        }
    }

    public void write(Path source, String path) {
        try {
            write(source.toFile(), path);
        } catch (Throwable e) {
            throw new FSOperationException("JadeFsServer#write : error in the write file operation", e);
        }
    }

    public void rename(File oldFile, File newFile) {
        try {
            if (oldFile.exists() && oldFile.isFile()) {
                oldFile.renameTo(newFile);
            }
        } catch (Throwable e) {
            throw new FSOperationException("JadeFsServer#rename : Unable to rename the file " + oldFile.getName() + " to " + newFile.getName() + ": ", e);
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
        throw new FSOperationException("JadeFsServer#read : Unable to read file");
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
            throw new FSOperationException("JadeFsServer#delete : Unable to delete file");
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

                Set<PosixFilePermission> perms = new HashSet<>();
                perms.add(PosixFilePermission.OWNER_READ);
                perms.add(PosixFilePermission.OWNER_WRITE);
                perms.add(PosixFilePermission.OWNER_EXECUTE);

                perms.add(PosixFilePermission.OTHERS_READ);
                perms.add(PosixFilePermission.OTHERS_WRITE);
                perms.add(PosixFilePermission.OTHERS_EXECUTE);

                try {
                    Files.setPosixFilePermissions(path, perms);
                } catch (Exception e) {
                    //System non compatible posix
                    File dir = path.toFile();
                    dir.setReadable(true, false);
                    dir.setWritable(true, false);
                    dir.setExecutable(true, false);
                }
            } catch (IOException e) {
                throw new FSOperationException("JadeFsServer#createFolder : Unable to create directory", e);
            }
        }
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
                    throw new FSOperationException("JadeFsServer#delete : Unable to delete directory", e);
                }
            }
            throw new FSOperationException("JadeFsServer#delete : this path is not a directory");
        }
        throw new FSOperationException("JadeFsServer#delete : directory is not found");
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
                throw new FSOperationException("JadeFsServer#renameFolder : the directory already exist");
            }
            throw new FSOperationException("JadeFsServer#renameFolder : this path is not a directory");
        }
        throw new FSOperationException("JadeFsServer#renameFolder : directory is not found");
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
            throw new FSOperationException("JadeFsServer#list : directory is not found");
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
