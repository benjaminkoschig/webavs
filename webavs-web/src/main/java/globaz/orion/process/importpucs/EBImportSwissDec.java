package globaz.orion.process.importpucs;

import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.fs.message.JadeFsFileInfo;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.orion.business.constantes.EBProperties;
import ch.globaz.orion.business.models.pucs.PucsFile;
import ch.globaz.orion.businessimpl.services.pucs.FindPucsSwissDec;

public class EBImportSwissDec extends ImportPucsPorcess {
    @Override
    public String getKey() {
        return "orion.pucs.import.swissDec";
    }

    @Override
    public String getDescription() {
        return "Process permettant l'importation en DB des fichiers swissDEC";
    }

    @Override
    public String getName() {
        return "PROCESS_IMPORT_PUCSINDB_PUCS_SWISS_DEC";
    }

    @Override
    public List<PucsFile> loadPucs() {
        FindPucsSwissDec swissDec = new FindPucsSwissDec(getSession());
        return swissDec.loadPucsSwissDecATraiter();
    }

    public List<CopyOfPucsSwissDecItem> resolveItems2() {
        String uri;

        try {
            uri = EBProperties.PUCS_SWISS_DEC_DIRECTORY.getValue();
        } catch (PropertiesException e) {
            throw new RuntimeException(e);
        }

        try {
            if (!JadeFsFacade.isFolder(uri)) {
                throw new RuntimeException("This value is not a valid folder: " + uri);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            List<String> listRemotePucsFileUri;
            listRemotePucsFileUri = JadeFsFacade.getFolderChildren(uri);
            List<CopyOfPucsSwissDecItem> list = new ArrayList<CopyOfPucsSwissDecItem>();
            for (String remotePucsFileUri : listRemotePucsFileUri) {
                if (JadeFilenameUtil.extractFilenameExtension(remotePucsFileUri).equalsIgnoreCase("xml")) {
                    JadeFsFileInfo info = JadeFsFacade.getInfo(remotePucsFileUri);
                    if (!info.getIsFolder()) {
                        list.add(new CopyOfPucsSwissDecItem(remotePucsFileUri, getSession(), getJobInfos().getIdJob()));
                    }
                }
            }
            return list;
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }

    }

}
