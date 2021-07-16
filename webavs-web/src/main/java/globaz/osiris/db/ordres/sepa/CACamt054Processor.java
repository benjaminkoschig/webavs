package globaz.osiris.db.ordres.sepa;

import globaz.jade.client.util.JadeListUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.ordres.sepa.exceptions.CACamt054UnsupportedVersionException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import org.w3c.dom.Document;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.osiris.business.constantes.CAProperties;
import com.jcraft.jsch.SftpException;

public class CACamt054Processor extends AbstractSepa {

    private static final long serialVersionUID = -3284528414276418236L;

    public List<String> getListFiles() throws PropertiesException {

        List<String> listOriginal = Arrays.asList(listFiles(getClient(false), getFolderName()));

        // Ne prendre en compte que les fichiers ayant le mot-clé XML à la fin
        listOriginal = JadeListUtil.filter(listOriginal, new JadeListUtil.Each<String>() {
            @Override
            public boolean eval(final String fichier) {
                return fichier.endsWith("xml");
            }
        });

        return listOriginal;
    }

    public void retrieveFile(final String fileName, final OutputStream stream) throws SftpException,
            PropertiesException {
        getClient(false).get(getFolderName() + fileName, stream);
    }

    public boolean isCamt054AndWantedType(final CACamt054DefinitionType type, final Document document) {
        boolean isSupportedAndWantedType = true;

        try {
            final String namespace = document.getDocumentElement().getNamespaceURI();
            final List<CACamt054Notification> notifications = CACamt054VersionResolver.resolveDocument(document, "",
                    type);

            isSupportedAndWantedType &= CACamt054VersionResolver.isSupportedVersion(namespace, type);
            isSupportedAndWantedType &= checkNotificationsForGoodType(type, notifications);
        } catch (CACamt054UnsupportedVersionException exception) {
            JadeLogger.info(exception, exception.getMessage());
            isSupportedAndWantedType = false;
        }

        return isSupportedAndWantedType;
    }

    public boolean checkNotificationsForGoodType(final CACamt054DefinitionType type,
            final List<CACamt054Notification> notifications) {

        boolean isCamt054Matching = false;
        for (final CACamt054Notification notification : notifications) {
            for (CACamt054GroupTransaction group : notification.getListGroupTxs()) {
                if (!isCamt054Matching) {
                    isCamt054Matching = checkEntryForGoodType(type, group);
                }
            }
        }

        return isCamt054Matching;
    }

    public boolean checkEntryForGoodType(final CACamt054DefinitionType type, final CACamt054GroupTransaction group) {
        boolean isCamt054Matching = false;

        if (group != null
                && type.isDefinitionMatching(group.getDomainCode(), group.getFamilyCode(), group.getSubFamilyCode())) {
            isCamt054Matching = true;
        }

        return isCamt054Matching;
    }

    private String getFolderName() throws PropertiesException {
        String folder = CAProperties.ISO_SEPA_FTP_CAMT054_FOLDER.getValue();
        String foldername = ".";

        if (!folder.isEmpty()) {
            foldername = "./" + folder;
        }

        return foldername;
    }

    @Override
    protected CAProperties getHost() {
        return CAProperties.ISO_SEPA_FTP_CAMT054_HOST;
    }

    @Override
    protected CAProperties getPort() {
        return CAProperties.ISO_SEPA_FTP_CAMT054_PORT;
    }

    @Override
    protected CAProperties getUser() {
        return CAProperties.ISO_SEPA_FTP_CAMT054_USER;
    }

    @Override
    protected CAProperties getPassword() {
        return CAProperties.ISO_SEPA_FTP_CAMT054_PASS;
    }
}
