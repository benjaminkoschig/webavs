package globaz.orion.process.importpucs;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.fs.JadeFsFacade;
import globaz.jade.fs.message.JadeFsFileInfo;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.process.byitem.ProcessItemsHandlerJadeJob;
import ch.globaz.orion.business.constantes.EBProperties;

public class EBImportSwissDecProcess extends ProcessItemsHandlerJadeJob<PucsSwissDecItem> {
    public static final String KEY = "orion.pucs.import.swissDec";

    private String uri;
    private String done;
    private String error;

    @Override
    public String getKey() {
        return KEY;
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
    public List<PucsSwissDecItem> resolveItems() {
        try {
            List<String> listRemotePucsFileUri;
            listRemotePucsFileUri = JadeFsFacade.getFolderChildren(uri);
            List<PucsSwissDecItem> list = new ArrayList<PucsSwissDecItem>();
            for (String remotePucsFileUri : listRemotePucsFileUri) {
                if (JadeFilenameUtil.extractFilenameExtension(remotePucsFileUri).equalsIgnoreCase("xml")) {
                    JadeFsFileInfo info = JadeFsFacade.getInfo(remotePucsFileUri);
                    if (!info.getIsFolder()) {
                        list.add(new PucsSwissDecItem(remotePucsFileUri, done, error, getJobInfos().getIdJob(),
                                getSession()));
                    }
                }
            }
            return list;
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
    }

    @Override
    public void before() {
        try {
            uri = EBProperties.PUCS_SWISS_DEC_DIRECTORY.getValue();
            done = EBProperties.PUCS_SWISS_DEC_DIRECTORY_OK.getValue();
            error = EBProperties.PUCS_SWISS_DEC_DIRECTORY_KO.getValue();
            if (!JadeFsFacade.isFolder(uri)) {
                throw new RuntimeException("This value is not a valid folder: " + uri);
            }
            BSessionUtil.initContext(getSession(), this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void after() {
        sendMailIfHasError();
        BSessionUtil.stopUsingContext(this);
    }
}
