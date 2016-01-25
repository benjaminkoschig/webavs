package ch.globaz.orion.service;

import globaz.draco.services.ebusiness.DSEbusinessAccessInterface;
import globaz.globall.db.BSession;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.businessimpl.services.dan.DanServiceImpl;
import ch.globaz.orion.businessimpl.services.pucs.PucsServiceImpl;

public class EBEbusinessDSAccessImplementation implements DSEbusinessAccessInterface {

    @Override
    public void notifyFinishedPucsFile(String idPucsFile, DeclarationSalaireProvenance provenance, BSession session)
            throws Exception {

        if (provenance.isPucs()) {
            PucsServiceImpl.notifyFinished(idPucsFile, session);
        } else if (provenance.isDan()) {
            DanServiceImpl.notifyFinished(idPucsFile, session);
        }

    }

}
