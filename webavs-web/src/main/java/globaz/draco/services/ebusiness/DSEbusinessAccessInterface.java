package globaz.draco.services.ebusiness;

import globaz.globall.db.BSession;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;

public interface DSEbusinessAccessInterface {

    public void notifyFinishedPucsFile(String idPucsFile, DeclarationSalaireProvenance provenance, BSession session)
            throws Exception;
}
