package ch.globaz.pegasus.businessimpl.services.models.home;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.models.home.SimpleTypeChambre;
import ch.globaz.pegasus.business.models.home.SimpleTypeChambreSearch;
import ch.globaz.pegasus.business.services.models.home.SimpleTypeChambreService;
import ch.globaz.pegasus.businessimpl.checkers.home.SimpleTypeChambreChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

/**
 * @author BSC
 */
public class SimpleTypeChambreServiceImpl extends PegasusAbstractServiceImpl implements SimpleTypeChambreService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.SimpleTypeChambreService
     * #count(ch.globaz.pegasus.business.models.home.SimpleTypeChambreSearch)
     */
    @Override
    public int count(SimpleTypeChambreSearch search) throws TypeChambreException, JadePersistenceException {
        if (search == null) {
            throw new TypeChambreException("Unable to count typeChambres, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.SimpleTypeChambreService
     * #create(ch.globaz.pegasus.business.models.home.SimpleTypeChambre)
     */
    @Override
    public SimpleTypeChambre create(SimpleTypeChambre typeChambre) throws TypeChambreException,
            JadePersistenceException {
        if (typeChambre == null) {
            throw new TypeChambreException("Unable to create typeChambre, the model passed is null!");
        }

        SimpleTypeChambreChecker.checkForCreate(typeChambre);
        return (SimpleTypeChambre) JadePersistenceManager.add(typeChambre);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.SimpleTypeChambreService
     * #delete(ch.globaz.pegasus.business.models.home.SimpleTypeChambre)
     */
    @Override
    public SimpleTypeChambre delete(SimpleTypeChambre typeChambre) throws TypeChambreException,
            JadePersistenceException {
        if (typeChambre == null) {
            throw new TypeChambreException("Unable to delete typeChambre, the model passed is null!");
        }
        if (typeChambre.isNew()) {
            throw new TypeChambreException("Unable to delete typeChambre, the model passed is new!");
        }

        SimpleTypeChambreChecker.checkForDelete(typeChambre);
        return (SimpleTypeChambre) JadePersistenceManager.delete(typeChambre);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.SimpleTypeChambreService #read(java.lang.String)
     */
    @Override
    public SimpleTypeChambre read(String idTypeChambre) throws TypeChambreException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idTypeChambre)) {
            throw new TypeChambreException("Unable to read typeChambre, the id passed is not defined!");
        }
        SimpleTypeChambre home = new SimpleTypeChambre();
        home.setId(idTypeChambre);
        return (SimpleTypeChambre) JadePersistenceManager.read(home);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.SimpleTypeChambreService
     * #update(ch.globaz.pegasus.business.models.home.SimpleTypeChambre)
     */
    @Override
    public SimpleTypeChambre update(SimpleTypeChambre typeChambre) throws TypeChambreException,
            JadePersistenceException {
        if (typeChambre == null) {
            throw new TypeChambreException("Unable to update typeChambre, the model passed is null!");
        }
        if (typeChambre.isNew()) {
            throw new TypeChambreException("Unable to update typeChambre, the model passed is new!");
        }

        SimpleTypeChambreChecker.checkForUpdate(typeChambre);
        return (SimpleTypeChambre) JadePersistenceManager.update(typeChambre);
    }

}
