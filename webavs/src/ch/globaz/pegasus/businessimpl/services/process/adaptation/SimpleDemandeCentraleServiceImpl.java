package ch.globaz.pegasus.businessimpl.services.process.adaptation;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.process.RenteAdapationDemandeException;
import ch.globaz.pegasus.business.models.process.adaptation.SimpleDemandeCentrale;
import ch.globaz.pegasus.business.models.process.adaptation.SimpleDemandeCentraleSearch;
import ch.globaz.pegasus.business.services.process.adaptation.SimpleDemandeCentraleService;

public class SimpleDemandeCentraleServiceImpl implements SimpleDemandeCentraleService {

    @Override
    public int count(SimpleDemandeCentraleSearch search) throws RenteAdapationDemandeException,
            JadePersistenceException {
        if (search == null) {
            throw new RenteAdapationDemandeException("Unable to count search, the model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    @Override
    public SimpleDemandeCentrale create(SimpleDemandeCentrale simpleDemandeCentrale)
            throws RenteAdapationDemandeException, JadePersistenceException {
        if (simpleDemandeCentrale == null) {
            throw new RenteAdapationDemandeException(
                    "Unable to create simpleDemandeCentrale, the model passed is null!");
        }
        return (SimpleDemandeCentrale) JadePersistenceManager.add(simpleDemandeCentrale);
    }

    @Override
    public SimpleDemandeCentrale delete(SimpleDemandeCentrale simpleDemandeCentrale)
            throws RenteAdapationDemandeException, JadePersistenceException {
        if (simpleDemandeCentrale == null) {
            throw new RenteAdapationDemandeException(
                    "Unable to delete simpleDemandeCentrale, the model passed is null!");
        }

        return (SimpleDemandeCentrale) JadePersistenceManager.delete(simpleDemandeCentrale);
    }

    @Override
    public SimpleDemandeCentrale read(String idSimpleDemandeCentrale) throws RenteAdapationDemandeException,
            JadePersistenceException {
        if (idSimpleDemandeCentrale == null) {
            throw new RenteAdapationDemandeException(
                    "Unable to read idSimpleDemandeCentrale, the model passed is null!");
        }
        SimpleDemandeCentrale SimpleDemandeCentrale = new SimpleDemandeCentrale();
        SimpleDemandeCentrale.setId(idSimpleDemandeCentrale);
        return (SimpleDemandeCentrale) JadePersistenceManager.read(SimpleDemandeCentrale);

    }

    @Override
    public SimpleDemandeCentraleSearch search(SimpleDemandeCentraleSearch simpleDemandeCentraleSearch)
            throws RenteAdapationDemandeException, JadePersistenceException {
        if (simpleDemandeCentraleSearch == null) {
            throw new RenteAdapationDemandeException(
                    "Unable to search simpleDemandeCentraleSearch, the model passed is null!");
        }
        return (SimpleDemandeCentraleSearch) JadePersistenceManager.search(simpleDemandeCentraleSearch);
    }

    @Override
    public SimpleDemandeCentrale update(SimpleDemandeCentrale simpleDemandeCentrale)
            throws RenteAdapationDemandeException, JadePersistenceException {
        if (simpleDemandeCentrale == null) {
            throw new RenteAdapationDemandeException(
                    "Unable to update simpleDemandeCentrale, the model passed is null!");
        }

        return (SimpleDemandeCentrale) JadePersistenceManager.update(simpleDemandeCentrale);
    }

}
