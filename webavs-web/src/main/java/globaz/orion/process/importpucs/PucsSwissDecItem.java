package globaz.orion.process.importpucs;

import globaz.globall.db.BSession;
import globaz.jade.fs.JadeFsFacade;
import globaz.naos.db.affiliation.AFAffiliation;
import java.util.List;
import ch.globaz.orion.business.constantes.EBProperties;
import ch.globaz.orion.business.models.pucs.PucsFile;

public class PucsSwissDecItem extends PucsItem {

    public PucsSwissDecItem(PucsFile pucsFile, List<AFAffiliation> affiliations, BSession session, String idJob) {
        super(pucsFile, affiliations, session, idJob);
    }

    @Override
    public void treat() throws Exception {
        try {
            super.treat();
        } catch (Exception e) {
            addException(e);
        } finally {
            String src = EBProperties.PUCS_SWISS_DEC_DIRECTORY.getValue() + pucsFile.getId() + ".xml";
            String path = EBProperties.PUCS_SWISS_DEC_DIRECTORY_OK.getValue();
            if (hasErrorOrException()) {
                path = EBProperties.PUCS_SWISS_DEC_DIRECTORY_KO.getValue();
            }
            JadeFsFacade.copyFile(src, path + pucsFile.getId().replace(".", "") + ".xml");
            JadeFsFacade.delete(src);
        }
    }

}
