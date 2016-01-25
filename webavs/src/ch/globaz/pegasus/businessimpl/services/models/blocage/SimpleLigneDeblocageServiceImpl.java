package ch.globaz.pegasus.businessimpl.services.models.blocage;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.EPCTypeDeblocage;
import ch.globaz.pegasus.business.exceptions.models.blocage.BlocageException;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocage;
import ch.globaz.pegasus.business.models.blocage.SimpleLigneDeblocageSearch;
import ch.globaz.pegasus.business.services.models.blocage.SimpleLigneDeblocageService;
import ch.globaz.pegasus.businessimpl.checkers.blocage.SimpleLigneDeblocageChecker;

public class SimpleLigneDeblocageServiceImpl implements SimpleLigneDeblocageService {

    @Override
    public int count(SimpleLigneDeblocageSearch search) throws BlocageException, JadePersistenceException {
        if (search == null) {
            throw new BlocageException("Unable to count search, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public SimpleLigneDeblocage create(SimpleLigneDeblocage SimpleDeblocage) throws JadePersistenceException,
            JadeApplicationException {
        if (SimpleDeblocage == null) {
            throw new BlocageException("Unable to create SimpleDeblocage, the model passed is null!");
        }
        SimpleLigneDeblocageChecker.checkForCreate(SimpleDeblocage);
        return (SimpleLigneDeblocage) JadePersistenceManager.add(SimpleDeblocage);
    }

    @Override
    public SimpleLigneDeblocage delete(SimpleLigneDeblocage SimpleDeblocage) throws BlocageException,
            JadePersistenceException {
        if (SimpleDeblocage == null) {
            throw new BlocageException("Unable to delete SimpleDeblocage, the model passed is null!");
        }
        SimpleLigneDeblocageChecker.checkForDelete(SimpleDeblocage);
        return (SimpleLigneDeblocage) JadePersistenceManager.delete(SimpleDeblocage);
    }

    @Override
    public SimpleLigneDeblocage read(String idSimpleDeblocage) throws BlocageException, JadePersistenceException {
        if (idSimpleDeblocage == null) {
            throw new BlocageException("Unable to read idSimpleDeblocage, the model passed is null!");
        }
        SimpleLigneDeblocage SimpleDeblocage = new SimpleLigneDeblocage();
        SimpleDeblocage.setId(idSimpleDeblocage);
        return (SimpleLigneDeblocage) JadePersistenceManager.read(SimpleDeblocage);
    }

    @Override
    public SimpleLigneDeblocageSearch search(SimpleLigneDeblocageSearch simpleDeblocageSearch) throws BlocageException,
            JadePersistenceException {
        if (simpleDeblocageSearch == null) {
            throw new BlocageException("Unable to search SimpleDeblocageSearch, the model passed is null!");
        }
        return (SimpleLigneDeblocageSearch) JadePersistenceManager.search(simpleDeblocageSearch);
    }

    @Override
    public Map<EPCTypeDeblocage, List<SimpleLigneDeblocage>> searchAndGroupByCsTypeDeblocage(
            SimpleLigneDeblocageSearch simpleDeblocageSearch) throws BlocageException, JadePersistenceException {
        simpleDeblocageSearch = search(simpleDeblocageSearch);

        Map<EPCTypeDeblocage, List<SimpleLigneDeblocage>> map = new LinkedHashMap<EPCTypeDeblocage, List<SimpleLigneDeblocage>>();
        for (JadeAbstractModel model : simpleDeblocageSearch.getSearchResults()) {
            SimpleLigneDeblocage deblocage = (SimpleLigneDeblocage) model;
            EPCTypeDeblocage key = EPCTypeDeblocage.getEnumByCsCode(deblocage.getCsTypeDeblocage());
            if (map.get(key) == null) {
                map.put(key, new ArrayList<SimpleLigneDeblocage>());
            }
            map.get(key).add(deblocage);
        }
        return map;
    }

    @Override
    public SimpleLigneDeblocage update(SimpleLigneDeblocage SimpleDeblocage) throws JadePersistenceException,
            JadeApplicationException {

        if (SimpleDeblocage == null) {
            throw new BlocageException("Unable to update SimpleDeblocage, the model passed is null!");
        }
        SimpleLigneDeblocageChecker.checkForUpdate(SimpleDeblocage);
        return (SimpleLigneDeblocage) JadePersistenceManager.update(SimpleDeblocage);
    }
}
