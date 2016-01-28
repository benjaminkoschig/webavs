package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.ArrayList;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.pegasus.business.constantes.EPCEtatDeblocage;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocage;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocageSearch;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class AfterPersisterDeblocageOperation implements AfterPersisterOperations {

    @Override
    public void afterPersist(SimpleLot simpleLot) throws JadePersistenceException, JadeApplicationException {

        ArrayList<String> idsPrestationsForLot = PegasusImplServiceLocator.getSimplePrestationService()
                .getIdsPrestationsByLot(simpleLot.getIdLot());

        SimpleLigneDeblocageSearch search = new SimpleLigneDeblocageSearch();
        search.setForInIdPrestation(idsPrestationsForLot);
        PegasusImplServiceLocator.getSimpleLigneDeblocageService().search(search);

        // mise à jour des prestations, etat comptabiliser
        for (JadeAbstractModel ligneDeblocage : search.getSearchResults()) {

            SimpleLigneDeblocage simpleLigneDeblocage = (SimpleLigneDeblocage) ligneDeblocage;

            simpleLigneDeblocage.setCsEtat(EPCEtatDeblocage.COMPTABILISE.getCsCode());
            PegasusImplServiceLocator.getSimpleLigneDeblocageService().update(simpleLigneDeblocage);
        }

    }

}
