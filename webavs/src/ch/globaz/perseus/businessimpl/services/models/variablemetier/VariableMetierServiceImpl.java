package ch.globaz.perseus.businessimpl.services.models.variablemetier;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Date;
import ch.globaz.perseus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.perseus.business.models.variablemetier.VariableMetier;
import ch.globaz.perseus.business.models.variablemetier.VariableMetierSearch;
import ch.globaz.perseus.business.services.models.variablemetier.VariableMetierService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

/**
 * @author DMA
 * 
 */
public class VariableMetierServiceImpl extends PerseusAbstractServiceImpl implements VariableMetierService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.perseus.business.services.models.variableMetier. VariableMetierService#count
     * (ch.globaz.perseus.business.models.variableMetier.VariableMetier)
     */
    @Override
    public int count(VariableMetierSearch search) throws VariableMetierException, JadePersistenceException {
        if (search == null) {
            throw new VariableMetierException("Unable to count variableMetier, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.perseus.business.services.models.variableMetier. VariableMetierService#create
     * (ch.globaz.perseus.business.models.variableMetier.VariableMetier)
     */
    @Override
    public VariableMetier create(VariableMetier variableMetier) throws VariableMetierException,
            JadePersistenceException {
        if (variableMetier == null) {
            throw new VariableMetierException("Unable to creat variableMetier, the model passed is null!");
        }
        // VariableMetierChecker.checkForCreate(variableMetier);
        try {
            variableMetier.setSimpleVariableMetier(PerseusImplServiceLocator.getSimpleVariableMetierService().create(
                    variableMetier.getSimpleVariableMetier()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new VariableMetierException("Service not available - " + e.getMessage());
        }
        return variableMetier;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.perseus.business.services.models.variableMetier. VariableMetierService#delete
     * (ch.globaz.perseus.business.models.variableMetier.VariableMetier)
     */
    @Override
    public VariableMetier delete(VariableMetier variableMetier) throws VariableMetierException,
            JadePersistenceException {
        if (variableMetier == null) {
            throw new VariableMetierException("Unable to delete variableMetier, the model passed is null!");
        }
        try {
            variableMetier.setSimpleVariableMetier(PerseusImplServiceLocator.getSimpleVariableMetierService().delete(
                    variableMetier.getSimpleVariableMetier()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new VariableMetierException("Service not available - " + e.getMessage());
        }
        return null;
    }

    @Override
    public VariableMetier getFromCS(String codeSystem, Date date) throws VariableMetierException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(codeSystem)) {
            throw new VariableMetierException("Unable to read variableMetier, the code system passed is null!");
        }
        VariableMetierSearch searchModel = new VariableMetierSearch();
        searchModel.setForCsTypeVariableMetier(codeSystem);
        searchModel.setForDateValable(JadeDateUtil.getGlobazFormattedDate(date));
        searchModel = (VariableMetierSearch) JadePersistenceManager.search(searchModel);
        if (searchModel.getNbOfResultMatchingQuery() > 0) {
            return (VariableMetier) searchModel.getSearchResults()[0];
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.perseus.business.services.models.variablemetier. VariableMetierService#read(java.lang.String)
     */
    @Override
    public VariableMetier read(String idVariableMetier) throws VariableMetierException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idVariableMetier)) {
            throw new VariableMetierException("Unable to read variableMetier, the id passed is null!");
        }
        VariableMetier variableMetier = new VariableMetier();
        variableMetier.setId(idVariableMetier);
        return (VariableMetier) JadePersistenceManager.read(variableMetier);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.perseus.business.services.models.variablemetier. VariableMetierService
     * #search(ch.globaz.perseus.business.models.variablemetier .VariableMetierSearch)
     */
    @Override
    public VariableMetierSearch search(VariableMetierSearch variableMetierSearch) throws JadePersistenceException,
            VariableMetierException {
        if (variableMetierSearch == null) {
            throw new VariableMetierException("Unable to search variableMetier, the search model passed is null!");
        }

        variableMetierSearch.setWhereKey(JadeStringUtil.isEmpty(variableMetierSearch.getForDateValable()) ? null
                : "withDateValable");
        return (VariableMetierSearch) JadePersistenceManager.search(variableMetierSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.perseus.business.services.models.variablemetier. VariableMetierService
     * #update(ch.globaz.perseus.business.models.variablemetier.VariableMetier)
     */
    @Override
    public VariableMetier update(VariableMetier variableMetier) throws VariableMetierException,
            JadePersistenceException {
        if (variableMetier == null) {
            throw new VariableMetierException("Unable to update variableMetier, the id passed is null!");
        }

        try {
            PerseusImplServiceLocator.getSimpleVariableMetierService().update(variableMetier.getSimpleVariableMetier());
            /*
             * PerseusImplServiceLocator.getVariableMetierService().update(variableMetier );
             */
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new VariableMetierException("Service not available - " + e.getMessage());
        }

        return variableMetier;
    }

}
