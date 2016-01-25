package globaz.pegasus.utils;

import globaz.globall.db.BSession;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtenduSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class PCCreancierHandler {

    public static String displayCreancierTiers(TiersSimpleModel simpleTiers) {
        return simpleTiers.getDesignation1() + " " + simpleTiers.getDesignation2();
    }

    // private getRequerant (DroitMembreFamilleEtenduSearch)

    public static String getRequerantDetailByIdDemande(String idDemandem, BSession session) throws CreancierException {
        DroitMembreFamilleEtenduSearch search = new DroitMembreFamilleEtenduSearch();
        search.setForIdDemande(idDemandem);
        List<String> list = new ArrayList<String>();
        list.add(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
        String detail = null;
        search.setForCsRoletMembreFamilleIn(list);
        try {
            if (idDemandem == null) {
                throw new CreancierException(
                        "Unable to getRequerantDetailByIdDemande idDemandem, the model passed is null!");
            }
            search = PegasusServiceLocator.getDroitService().searchDroitMemebreFamilleEtendu(search);
            if (search.getSize() == 1) {
                DroitMembreFamille membreFamille = ((DroitMembreFamilleEtendu) search.getSearchResults()[0])
                        .getDroitMembreFamille();
                detail = PCDroitHandler.getRequerantDetail(session, membreFamille);
            } else {
                throw new CreancierException("Unable to find the requerant");
            }

        } catch (DroitException e) {
            throw new CreancierException("Unable to find the requerant", e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new CreancierException("Unable to find the requerant", e);
        } catch (JadePersistenceException e) {
            throw new CreancierException("Unable to find the requerant", e);
        }
        return detail;
    }
}
