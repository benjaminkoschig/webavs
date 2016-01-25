package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.SimpleTypeFraisObtentionRevenuException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.TypeFraisObtentionRevenuException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenuSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.TypeFraisObtentionRevenu;
import ch.globaz.pegasus.business.services.models.revenusdepenses.TypeFraisObtentionRevenuService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class TypeFraisObtentionRevenuServiceImpl extends PegasusAbstractServiceImpl implements
        TypeFraisObtentionRevenuService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. TypeFraisObtentionRevenuService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .TypeFraisObtentionRevenuSearch)
     */
    @Override
    public int count(SimpleTypeFraisObtentionRevenuSearch search) throws TypeFraisObtentionRevenuException,
            JadePersistenceException {
        if (search == null) {
            throw new TypeFraisObtentionRevenuException(
                    "Unable to count TypeFraisObtentionRevenu, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. TypeFraisObtentionRevenuService
     * #create(ch.globaz.pegasus.business.models.revenusdepenses .TypeFraisObtentionRevenu)
     */
    @Override
    public TypeFraisObtentionRevenu create(TypeFraisObtentionRevenu typeFraisObtentionRevenu)
            throws JadePersistenceException, TypeFraisObtentionRevenuException {
        if (typeFraisObtentionRevenu == null) {
            throw new TypeFraisObtentionRevenuException(
                    "Unable to create TypeFraisObtentionRevenu, the model passed is null!");
        }

        try {

            try {
                PegasusImplServiceLocator.getSimpleTypeFraisObtentionRevenuService().create(
                        typeFraisObtentionRevenu.getSimpleTypeFraisObtentionRevenu());
            } catch (SimpleTypeFraisObtentionRevenuException e) {
                throw new TypeFraisObtentionRevenuException("Unable to create TypeFraisObtentionRevenu");
            }
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TypeFraisObtentionRevenuException("Service not available - " + e.getMessage());
        }

        return typeFraisObtentionRevenu;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. TypeFraisObtentionRevenuService
     * #delete(ch.globaz.pegasus.business.models.revenusdepenses .TypeFraisObtentionRevenu)
     */
    @Override
    public TypeFraisObtentionRevenu delete(TypeFraisObtentionRevenu typeFraisObtentionRevenu)
            throws TypeFraisObtentionRevenuException, JadePersistenceException {
        if (typeFraisObtentionRevenu == null) {
            throw new TypeFraisObtentionRevenuException(
                    "Unable to delete TypeFraisObtentionRevenu, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleTypeFraisObtentionRevenuService().delete(
                    typeFraisObtentionRevenu.getSimpleTypeFraisObtentionRevenu());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TypeFraisObtentionRevenuException("Service not available - " + e.getMessage());
        } catch (SimpleTypeFraisObtentionRevenuException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return typeFraisObtentionRevenu;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses.
     * TypeFraisObtentionRevenuService#read(java.lang.String)
     */
    @Override
    public TypeFraisObtentionRevenu read(String idTypeFraisObtentionRevenu) throws JadePersistenceException,
            TypeFraisObtentionRevenuException {
        if (JadeStringUtil.isEmpty(idTypeFraisObtentionRevenu)) {
            throw new TypeFraisObtentionRevenuException(
                    "Unable to read TypeFraisObtentionRevenu, the id passed is null!");
        }
        TypeFraisObtentionRevenu TypeFraisObtentionRevenu = new TypeFraisObtentionRevenu();
        TypeFraisObtentionRevenu.setId(idTypeFraisObtentionRevenu);
        return (TypeFraisObtentionRevenu) JadePersistenceManager.read(TypeFraisObtentionRevenu);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. TypeFraisObtentionRevenuService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .TypeFraisObtentionRevenuSearch)
     */
    @Override
    public SimpleTypeFraisObtentionRevenuSearch search(
            SimpleTypeFraisObtentionRevenuSearch typeFraisObtentionRevenuSearch) throws JadePersistenceException,
            TypeFraisObtentionRevenuException {
        if (typeFraisObtentionRevenuSearch == null) {
            throw new TypeFraisObtentionRevenuException(
                    "Unable to search TypeFraisObtentionRevenu, the search model passed is null!");
        }
        return (SimpleTypeFraisObtentionRevenuSearch) JadePersistenceManager.search(typeFraisObtentionRevenuSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. TypeFraisObtentionRevenuService
     * #update(ch.globaz.pegasus.business.models.revenusdepenses .TypeFraisObtentionRevenu)
     */
    @Override
    public TypeFraisObtentionRevenu update(TypeFraisObtentionRevenu typeFraisObtentionRevenu)
            throws JadePersistenceException, TypeFraisObtentionRevenuException, DonneeFinanciereException {
        if (typeFraisObtentionRevenu == null) {
            throw new TypeFraisObtentionRevenuException(
                    "Unable to update TypeFraisObtentionRevenu, the model passed is null!");
        }

        try {
            try {
                typeFraisObtentionRevenu.setSimpleTypeFraisObtentionRevenu(PegasusImplServiceLocator
                        .getSimpleTypeFraisObtentionRevenuService().update(
                                typeFraisObtentionRevenu.getSimpleTypeFraisObtentionRevenu()));
            } catch (SimpleTypeFraisObtentionRevenuException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new TypeFraisObtentionRevenuException("Service not available - " + e.getMessage());
        }

        return typeFraisObtentionRevenu;
    }

}
