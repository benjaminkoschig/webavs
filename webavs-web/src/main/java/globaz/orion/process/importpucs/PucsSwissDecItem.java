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
        super.treat();
        String src = EBProperties.PUCS_SWISS_DEC_DIRECTORY.getValue() + pucsFile.getId() + ".xml";
        String dest = EBProperties.PUCS_SWISS_DEC_DIRECTORY_OK.getValue() + pucsFile.getId().replace(".", "") + ".xml";
        JadeFsFacade.copyFile(src, dest);
    }

}
