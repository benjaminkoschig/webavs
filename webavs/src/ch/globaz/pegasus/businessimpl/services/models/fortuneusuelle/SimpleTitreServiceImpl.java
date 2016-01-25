package ch.globaz.pegasus.businessimpl.services.models.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.TitreException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleTitre;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleTitreSearch;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.SimpleTitreService;
import ch.globaz.pegasus.businessimpl.checkers.fortuneusuelle.SimpleTitreChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleTitreServiceImpl extends PegasusAbstractServiceImpl implements SimpleTitreService {

    @Override
    public SimpleTitre create(SimpleTitre simpleTitre) throws JadePersistenceException, TitreException {
        if (simpleTitre == null) {
            throw new TitreException("Unable to create simpleTitre, the model passed is null!");
        }
        SimpleTitreChecker.checkForCreate(simpleTitre);
        return (SimpleTitre) JadePersistenceManager.add(simpleTitre);
    }

    @Override
    public SimpleTitre delete(SimpleTitre simpleTitre) throws TitreException, JadePersistenceException {
        if (simpleTitre == null) {
            throw new TitreException("Unable to delete SimpleTitreChecker, the model passed is null!");
        }
        if (simpleTitre.isNew()) {
            throw new TitreException("Unable to delete SimpleTitreChecker, the model passed is new!");
        }
        SimpleTitreChecker.checkForDelete(simpleTitre);
        return (SimpleTitre) JadePersistenceManager.delete(simpleTitre);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleTitreSearch search = new SimpleTitreSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    @Override
    public SimpleTitre read(String idTitre) throws JadePersistenceException, TitreException {
        if (JadeStringUtil.isEmpty(idTitre)) {
            throw new TitreException("Unable to read SimpleTitreChecker, the id passed is not defined!");
        }
        SimpleTitre simpleTitre = new SimpleTitre();
        simpleTitre.setId(idTitre);
        return (SimpleTitre) JadePersistenceManager.read(simpleTitre);
    }

    @Override
    public SimpleTitre update(SimpleTitre simpleTitre) throws JadePersistenceException, TitreException {
        if (simpleTitre == null) {
            throw new TitreException("Unable to update SimpleTitreChecker, the model passed is null!");
        }
        if (simpleTitre.isNew()) {
            throw new TitreException("Unable to update SimpleTitreChecker, the model passed is new!");
        }
        SimpleTitreChecker.checkForUpdate(simpleTitre);
        return (SimpleTitre) JadePersistenceManager.update(simpleTitre);
    }

}