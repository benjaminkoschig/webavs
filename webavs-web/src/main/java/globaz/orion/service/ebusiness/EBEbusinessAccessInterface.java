package globaz.orion.service.ebusiness;

import globaz.globall.db.BSession;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;

public interface EBEbusinessAccessInterface {
    public void notifyFinishedPucsFile(String idPucsFile, String type, BSession session) throws Exception;

    public void notifyFinishedPucsFile(String idPucsFile, DeclarationSalaireProvenance provenance, BSession session)
            throws Exception;
}
