package ch.globaz.pegasus.businessimpl.services.models.home;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.models.home.TypeChambre;
import ch.globaz.pegasus.business.models.home.TypeChambreSearch;
import ch.globaz.pegasus.business.services.models.home.TypeChambreService;
import ch.globaz.pegasus.businessimpl.checkers.home.TypeChambreChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class TypeChambreServiceImpl extends PegasusAbstractServiceImpl implements TypeChambreService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.TypeChambreService#count
     * (ch.globaz.pegasus.business.models.home.TypeChambreSearch)
     */
    @Override
    public int count(TypeChambreSearch search) throws TypeChambreException, JadePersistenceException {
        if (search == null) {
            throw new TypeChambreException("Unable to count dossiers, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.TypeChambreService#create
     * (ch.globaz.pegasus.business.models.home.TypeChambre)
     */
    @Override
    public TypeChambre create(TypeChambre typeChambre) throws JadePersistenceException, TypeChambreException {

        if (typeChambre == null) {
            throw new TypeChambreException("Unable to create typeChambre, the given model is null!");
        }

        try {

            TypeChambreChecker.checkForCreate(typeChambre);

            typeChambre.setSimpleTypeChambre(PegasusImplServiceLocator.getSimpleTypeChambreService().create(
                    typeChambre.getSimpleTypeChambre()));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TypeChambreException("Service not available - " + e.getMessage());
        }

        return typeChambre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.PrixChambreService#delete
     * (ch.globaz.pegasus.business.models.home.PrixChambre)
     */
    @Override
    public TypeChambre delete(TypeChambre typeChambre) throws TypeChambreException, JadePersistenceException {
        if (typeChambre == null) {
            throw new TypeChambreException("Unable to delete typeChambre, the given model is null!");
        }
        try {

            TypeChambreChecker.checkForDelete(typeChambre);

            typeChambre.setSimpleTypeChambre(PegasusImplServiceLocator.getSimpleTypeChambreService().delete(
                    typeChambre.getSimpleTypeChambre()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TypeChambreException("Service not available - " + e.getMessage());
        }

        return typeChambre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.TypeChambreService#read (java.lang.String)
     */
    @Override
    public TypeChambre read(String idTypeChambre) throws JadePersistenceException, TypeChambreException {
        if (JadeStringUtil.isEmpty(idTypeChambre)) {
            throw new TypeChambreException("Unable to read typeChambre, the id passed is null!");
        }
        TypeChambre typeChambre = new TypeChambre();
        typeChambre.setId(idTypeChambre);
        return (TypeChambre) JadePersistenceManager.read(typeChambre);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.TypeChambreService#search
     * (ch.globaz.pegasus.business.models.home.TypeChambreSearch)
     */
    @Override
    public TypeChambreSearch search(TypeChambreSearch typeChambreSearch) throws JadePersistenceException,
            TypeChambreException {
        if (typeChambreSearch == null) {
            throw new TypeChambreException("Unable to search typeChambre, the search model passed is null!");
        }
        return (TypeChambreSearch) JadePersistenceManager.search(typeChambreSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.TypeChambreService#update
     * (ch.globaz.pegasus.business.models.home.TypeChambre)
     */
    @Override
    public TypeChambre update(TypeChambre typeChambre) throws JadePersistenceException, TypeChambreException {
        if (typeChambre == null) {
            throw new TypeChambreException("Unable to update typeChambre, the given model is null!");
        }

        try {

            TypeChambreChecker.checkForUpdate(typeChambre);

            // la mise a jour ne se fait que sur le simpleTypeChambre
            PegasusImplServiceLocator.getSimpleTypeChambreService().update(typeChambre.getSimpleTypeChambre());

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TypeChambreException("Service not available - " + e.getMessage());
        }

        return typeChambre;
    }

}
