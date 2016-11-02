package ch.globaz.orion.service;

import globaz.globall.db.BSession;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;

public interface EBEbusinessInterface {
    void notifyFinishedPucsFile(String idPucsFile, String type, BSession session) throws Exception;

    void notifyFinishedPucsFile(String idPucsFile, DeclarationSalaireProvenance provenance, BSession session)
            throws Exception;
}
