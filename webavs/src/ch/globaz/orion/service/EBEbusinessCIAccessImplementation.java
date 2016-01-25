package ch.globaz.orion.service;

import globaz.globall.db.BSession;
import globaz.pavo.service.ebusiness.CIEbusinessAccessInterface;
import ch.globaz.orion.business.domaine.pucs.DeclarationSalaireProvenance;
import ch.globaz.orion.businessimpl.services.dan.DanServiceImpl;
import ch.globaz.orion.businessimpl.services.pucs.PucsServiceImpl;

public class EBEbusinessCIAccessImplementation implements CIEbusinessAccessInterface {

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.pavo.service.ebusiness.CIEbusinessAccessInterface#
     * notifyFinishedPucsFile(globaz.globall.db.BTransaction, java.lang.String)
     */
    @Override
    public void notifyFinishedPucsFile(String idPucsFile, String type, BSession session) throws Exception {
        DeclarationSalaireProvenance provenance = DeclarationSalaireProvenance.fromValueWithOutException(type);

        if (provenance.isPucs()) {
            PucsServiceImpl.notifyFinished(idPucsFile, session);
        } else if (provenance.isDan()) {
            DanServiceImpl.notifyFinished(idPucsFile, session);
        }
    }
}
