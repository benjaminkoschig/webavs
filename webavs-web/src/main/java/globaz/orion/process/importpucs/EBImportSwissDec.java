package globaz.orion.process.importpucs;

import java.util.List;
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
}
