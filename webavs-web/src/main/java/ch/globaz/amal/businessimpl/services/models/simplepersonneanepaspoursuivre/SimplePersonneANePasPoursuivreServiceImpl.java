package ch.globaz.amal.businessimpl.services.models.simplepersonneanepaspoursuivre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import FamilleException.SimplePersonneANePasPoursuivreException;
import ch.globaz.amal.business.models.simplepersonneanepaspoursuivre.SimplePersonneANePasPoursuivre;
import ch.globaz.amal.business.models.simplepersonneanepaspoursuivre.SimplePersonneANePasPoursuivreSearch;
import ch.globaz.amal.business.services.models.simplepersonneanepaspoursuivre.SimplePersonneANePasPoursuivreService;

public class SimplePersonneANePasPoursuivreServiceImpl implements SimplePersonneANePasPoursuivreService {

    @Override
    public SimplePersonneANePasPoursuivre create(SimplePersonneANePasPoursuivre simplePersonneANePasPoursuivre)
            throws JadePersistenceException, SimplePersonneANePasPoursuivreException {
        if (simplePersonneANePasPoursuivre == null) {
            throw new SimplePersonneANePasPoursuivreException(
                    "Unable to create simplePersonneANePasPoursuivre, the model passed is null!");
        }
        return (SimplePersonneANePasPoursuivre) JadePersistenceManager.add(simplePersonneANePasPoursuivre);
    }

    @Override
    public int count(SimplePersonneANePasPoursuivreSearch simplePersonneANePasPoursuivreSearch)
            throws JadePersistenceException, SimplePersonneANePasPoursuivreException {
        return JadePersistenceManager.count(simplePersonneANePasPoursuivreSearch);
    }

    @Override
    public SimplePersonneANePasPoursuivre delete(SimplePersonneANePasPoursuivre simplePersonneANePasPoursuivre)
            throws JadePersistenceException, SimplePersonneANePasPoursuivreException {
        if (simplePersonneANePasPoursuivre == null) {
            throw new SimplePersonneANePasPoursuivreException(
                    "Unable to delete simplePersonneANePasPoursuivre, the model passed is null!");
        }
        return (SimplePersonneANePasPoursuivre) JadePersistenceManager.delete(simplePersonneANePasPoursuivre);
    }

    @Override
    public SimplePersonneANePasPoursuivre update(SimplePersonneANePasPoursuivre simplePersonneANePasPoursuivre)
            throws JadePersistenceException, SimplePersonneANePasPoursuivreException {
        if (simplePersonneANePasPoursuivre == null) {
            throw new SimplePersonneANePasPoursuivreException(
                    "Unable to update simplePersonneANePasPoursuivre, the model passed is null!");
        }
        return (SimplePersonneANePasPoursuivre) JadePersistenceManager.update(simplePersonneANePasPoursuivre);
    }

    @Override
    public SimplePersonneANePasPoursuivreSearch search(
            SimplePersonneANePasPoursuivreSearch simplePersonneANePasPoursuivreSearch) throws JadePersistenceException,
            SimplePersonneANePasPoursuivreException {
        if (simplePersonneANePasPoursuivreSearch == null) {
            throw new SimplePersonneANePasPoursuivreException(
                    "Unable to search simplePersonneANePasPoursuivre, the model passed is null!");
        }
        return (SimplePersonneANePasPoursuivreSearch) JadePersistenceManager
                .search(simplePersonneANePasPoursuivreSearch);
    }

}
