package globaz.osiris.db.ordres.sepa;

import globaz.jade.client.util.JadeListUtil;
import globaz.jade.log.JadeLogger;
import globaz.osiris.db.ordres.sepa.exceptions.CACamt054UnsupportedVersionException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.w3c.dom.Document;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.osiris.business.constantes.CAProperties;
import com.jcraft.jsch.SftpException;

public class CACamt054Processor extends AbstractSepa {

    public List<String> getListFiles() throws PropertiesException {

        List<String> listOriginal = Arrays.asList(listFiles(getClient(), getFolderName()));

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
        getClient().get(getFolderName() + fileName, stream);
    }

    public boolean isCamt054AndWantedType(final CACamt054DefinitionType type, final Document document) {
        boolean isSupportedAndWantedType = false;

        // Savoir si les versions de camt054 sont supportées par l'application
        if (CACamt054DefinitionType.CAMT054_BVR.equals(type)) {
            List<CACamt054Notification> notifications = new ArrayList<CACamt054Notification>();

            try {
                notifications = CACamt054BVRVersionResolver.resolveDocument(document, "");
            } catch (CACamt054UnsupportedVersionException exception) {
                JadeLogger.info(exception, exception.getMessage());
            }

            isSupportedAndWantedType = CACamt054BVRVersionResolver.isSupportedVersion(document.getDocumentElement()
                    .getNamespaceURI()) && checkNotificationsForGoodType(type, notifications);
        }

        return isSupportedAndWantedType;
    }

    public boolean checkNotificationsForGoodType(final CACamt054DefinitionType type,
            final List<CACamt054Notification> notifications) {

        boolean isCamt054Bvr = false;
        for (final CACamt054Notification notification : notifications) {
            for (CACamt054GroupTransaction group : notification.getListGroupTxs()) {
                if (!isCamt054Bvr) {
                    isCamt054Bvr = checkEntryForGoodType(type, group);
                }
            }
        }

        return isCamt054Bvr;
    }

    public boolean checkEntryForGoodType(final CACamt054DefinitionType type, final CACamt054GroupTransaction group) {
        boolean isCamt054Bvr = false;

        // si nous avons trouvé le bon type dans le document pour n'importe quel entry, nous sortons
        if (group != null && type.isGoodType(group.getDomainCode(), group.getFamilyCode(), group.getSubFamilyCode())) {
            isCamt054Bvr = true;
        }

        return isCamt054Bvr;
    }

    private String getFolderName() throws PropertiesException {
        String folder = CAProperties.ISO_SEPA_FTP_002_FOLDER.getValue();
        String foldername = ".";

        if (!folder.isEmpty()) {
            foldername = "./" + folder;
        }

        return foldername;
    }
}
