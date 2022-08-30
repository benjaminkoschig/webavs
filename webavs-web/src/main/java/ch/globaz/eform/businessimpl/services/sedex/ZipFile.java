package ch.globaz.eform.businessimpl.services.sedex;

import globaz.common.util.CommonBlobUtils;

import java.io.File;
import java.util.Objects;

public class ZipFile {
    private final File file;
    private byte[] byteFile;

    public ZipFile(String path) {
        file = new File(path);
    }

    public String getPath() {
        return file.getPath();
    }

    public String getName() {
        return file.getName();
    }

    public byte[] getFileToByte() throws Exception {
        if (Objects.isNull(byteFile)) {
            byteFile = CommonBlobUtils.fileToByteArray(file.getPath());
        }

        return byteFile;
    }
}
