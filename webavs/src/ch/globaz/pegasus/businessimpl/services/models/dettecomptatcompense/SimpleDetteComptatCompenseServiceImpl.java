package ch.globaz.pegasus.businessimpl.services.models.dettecomptatcompense;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import ch.globaz.pegasus.business.exceptions.models.dettecomptatcompense.DetteComptatCompenseException;
import ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompense;
import ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompenseSearch;
import ch.globaz.pegasus.business.services.models.dettecomptatcompense.SimpleDetteComptatCompenseService;

public class SimpleDetteComptatCompenseServiceImpl implements SimpleDetteComptatCompenseService {

    @Override
    public int count(SimpleDetteComptatCompenseSearch search) throws JadeApplicationException, JadePersistenceException {
        if (search == null) {
            throw new DetteComptatCompenseException("Unable to count search, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public SimpleDetteComptatCompense create(SimpleDetteComptatCompense entity) throws JadeApplicationException,
            JadePersistenceException {
        if (entity == null) {
            throw new DetteComptatCompenseException("Unable to create entity, the model passed is null!");
        }
        return (SimpleDetteComptatCompense) JadePersistenceManager.add(entity);
    }

    @Override
    public SimpleDetteComptatCompense delete(SimpleDetteComptatCompense entity) throws JadeApplicationException,
            JadePersistenceException {
        if (entity == null) {
            throw new DetteComptatCompenseException("Unable to delete entity, the model passed is null!");
        }
        return (SimpleDetteComptatCompense) JadePersistenceManager.delete(entity);
    }

    @Override
    public int delete(SimpleDetteComptatCompenseSearch detteComptaCompenseSearch) throws JadeApplicationException,
            JadePersistenceException {
        if (detteComptaCompenseSearch == null) {
            throw new DetteComptatCompenseException("Unable to delete entity, the model passed is null!");
        }

        detteComptaCompenseSearch = search(detteComptaCompenseSearch);
        for (JadeAbstractModel absDonnee : detteComptaCompenseSearch.getSearchResults()) {
            JadePersistenceManager.delete((SimpleDetteComptatCompense) absDonnee);
        }

        return detteComptaCompenseSearch.getSize();
    }

    @Override
    public SimpleDetteComptatCompense read(String idEntity) throws JadeApplicationException, JadePersistenceException {
        if (idEntity == null) {
            throw new DetteComptatCompenseException("Unable to read idEntity, the model passed is null!");
        }
        SimpleDetteComptatCompense model = new SimpleDetteComptatCompense();
        model.setId(idEntity);
        return (SimpleDetteComptatCompense) JadePersistenceManager.read(model);
    }

    @Override
    public SimpleDetteComptatCompenseSearch search(SimpleDetteComptatCompenseSearch search)
            throws JadeApplicationException, JadePersistenceException {
        if (search == null) {
            throw new DetteComptatCompenseException("Unable to search search, the model passed is null!");
        }
        return (SimpleDetteComptatCompenseSearch) JadePersistenceManager.search(search);
    }

    @Override
    public SimpleDetteComptatCompense update(SimpleDetteComptatCompense entity) throws JadeApplicationException,
            JadePersistenceException {
        if (entity == null) {
            throw new DetteComptatCompenseException("Unable to delete entities, the searchmodel passed is null!");
        }
        return (SimpleDetteComptatCompense) JadePersistenceManager.update(entity);
    }
}
