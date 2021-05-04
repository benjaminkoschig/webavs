package globaz.osiris.process.ebill;

import ch.globaz.common.properties.PropertiesException;
import ch.globaz.osiris.business.constantes.CAProperties;
import com.jcraft.jsch.SftpException;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.common.Jade;
import globaz.osiris.db.ordres.sepa.AbstractSepa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class EBillSftpProcessor extends AbstractSepa {
    private static final Logger LOG = LoggerFactory.getLogger(EBillSftpProcessor.class);

    public EBillSftpProcessor() throws PropertiesException {
        String localInDirPath = Jade.getInstance().getPersistenceDir() + getFolderInName();
        File localInDir = new File(localInDirPath);
        if (!localInDir.exists()) {
            localInDir.mkdir();
        }
        String localOutDirPath = Jade.getInstance().getPersistenceDir() + getFolderOutName();
        File localOutDir = new File(localOutDirPath);
        if (!localOutDir.exists()) {
            localOutDir.mkdir();
        }
    }

    public String getFolderInName() throws PropertiesException {
        String folder = CAProperties.EBILL_FTP_IN.getValue();

        if (!folder.endsWith("/")) {
                folder = folder + "/";
        }

        return folder;
    }

    public String getFolderOutName() throws PropertiesException {
        String folder = CAProperties.EBILL_FTP_OUT.getValue();

        if (!folder.endsWith("/")) {
                folder = folder + "/";
        }

        return folder;
    }

    private String getInFolderNameFtp() throws PropertiesException {
        String folder = getFolderInName();
        String foldername = ".";

        if (!folder.isEmpty()) {
            foldername = "./" + folder;
        }

        return foldername;
    }

    private String getOutFolderNameFtp() throws PropertiesException {
        String folder = getFolderOutName();
        String foldername = ".";

        if (!folder.isEmpty()) {
            foldername = "./" + folder;
        }

        return foldername;
    }

    public List<String> getListFiles(String extension) throws PropertiesException {

        List<String> listOriginal = Arrays.asList(listFiles(getClient(), getInFolderNameFtp()));

        // Ne prendre en compte que les fichiers ayant l'extension passé en paramètre
        listOriginal = JadeListUtil.filter(listOriginal, new JadeListUtil.Each<String>() {
            @Override
            public boolean eval(final String fichier) {
                return fichier.toLowerCase(Locale.ROOT).endsWith(extension.toLowerCase(Locale.ROOT));
            }
        });

        return listOriginal;
    }

    public void sendFile(InputStream is, String filename) throws PropertiesException, SftpException {
        //sendData(is, getClient(), getOutFolderNameFtp() + filename);
        getClient().put(is, getOutFolderNameFtp() + filename);
    }

    public void retrieveFile(final String fileName, final OutputStream stream) throws PropertiesException, SftpException {
        //retrieveData(getClient(),getInFolderNameFtp() + fileName, stream);
        getClient().get(getInFolderNameFtp() + fileName, stream);
    }

    public void deleteFile(final String fileName) throws PropertiesException, SftpException {
        //deleteFile(getClient(), getInFolderNameFtp() + fileName);
        getClient().rm(getInFolderNameFtp() + fileName);
    }

    @Override
    protected CAProperties getHost() {
        return CAProperties.EBILL_FTP_HOST;
    }

    @Override
    protected CAProperties getPort() {
        return CAProperties.EBILL_FTP_PORT;
    }

    @Override
    protected CAProperties getUser() {
        return CAProperties.EBILL_FTP_USER;
    }

    @Override
    protected CAProperties getPassword() {
        return null;
    }

    @Override
    protected CAProperties getKeyPassphrase() {
        return CAProperties.EBILL_FTP_KEY_PASSPHRASE;
    }

    @Override
    protected CAProperties getKnownHosts() {
        return CAProperties.EBILL_FTP_KNOWN_HOSTS;
    }

}
