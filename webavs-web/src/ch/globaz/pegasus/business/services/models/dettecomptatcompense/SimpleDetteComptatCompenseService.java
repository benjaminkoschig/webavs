package ch.globaz.pegasus.business.services.models.dettecomptatcompense;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeCrudService;
import ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompense;
import ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompenseSearch;

public interface SimpleDetteComptatCompenseService extends
        JadeCrudService<SimpleDetteComptatCompense, SimpleDetteComptatCompenseSearch> {

    public int delete(SimpleDetteComptatCompenseSearch detteComptaCompenseSearch) throws JadeApplicationException,
            JadePersistenceException;

}
