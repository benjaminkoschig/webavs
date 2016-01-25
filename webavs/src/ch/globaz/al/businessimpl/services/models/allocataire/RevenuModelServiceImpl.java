package ch.globaz.al.businessimpl.services.models.allocataire;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.allocataire.ALAgricoleModelException;
import ch.globaz.al.business.exceptions.model.allocataire.ALRevenuModelException;
import ch.globaz.al.business.models.allocataire.RevenuModel;
import ch.globaz.al.business.models.allocataire.RevenuSearchModel;
import ch.globaz.al.business.services.models.allocataire.RevenuModelService;
import ch.globaz.al.businessimpl.checker.model.allocataire.RevenuModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * classe d'implémentation des services de RevenuModel
 * 
 * @author PTA
 * 
 */
public class RevenuModelServiceImpl extends ALAbstractBusinessServiceImpl implements RevenuModelService {

    /**
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.RevenuModelService#count(ch.globaz.al.business.models.allocataire.RevenuSearchModel)
     */
    @Override
    public int count(RevenuSearchModel revenuSearch) throws JadePersistenceException {
        return JadePersistenceManager.count(revenuSearch);
    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.RevenuModelService#create(ch.globaz.al.business.models.allocataire.RevenuModel)
     */
    @Override
    public RevenuModel create(RevenuModel revenuModel) throws JadeApplicationException, JadePersistenceException {
        if (revenuModel == null) {
            throw new ALRevenuModelException("Unable to create revenu-the model passed is empty");
        }
        RevenuModelChecker.validate(revenuModel);
        return (RevenuModel) JadePersistenceManager.add(revenuModel);
    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.RevenuModelService#delete(ch.globaz.al.business.models.allocataire.RevenuModel)
     */
    @Override
    public RevenuModel delete(RevenuModel revenuModel) throws JadeApplicationException, JadePersistenceException {

        if (revenuModel == null) {
            throw new ALRevenuModelException("Unable to delete revenu-the model passed is null");
        }

        if (revenuModel.isNew()) {
            throw new ALRevenuModelException("unable to delete revenu-the model passed is new");
        }

        return (RevenuModel) JadePersistenceManager.delete(revenuModel);
    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.RevenuModelService#deleteForIdAllocataire(java.lang.String)
     */
    @Override
    public void deleteForIdAllocataire(String idAllocataire) throws JadePersistenceException, JadeApplicationException {

        if (!JadeNumericUtil.isIntegerPositif(idAllocataire)) {
            throw new ALAgricoleModelException(
                    "Unable to delete AgricoleModel - the idAllocataire passed is null or zero");
        }

        RevenuSearchModel rs = new RevenuSearchModel();
        rs.setForIdAllocataire(idAllocataire);
        JadePersistenceManager.delete(rs);
    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.RevenuModelService#initModel(ch.globaz.al.business.models.allocataire.RevenuModel)
     */
    @Override
    public RevenuModel initModel(RevenuModel revenuModel) throws JadeApplicationException, JadePersistenceException {
        if (revenuModel == null) {
            throw new ALRevenuModelException(
                    "RevenuModelServiceImpl#initModel: unable to init model - model passed is null");
        }
        if (!JadeNumericUtil.isEmptyOrZero(revenuModel.getId())) {
            revenuModel = read(revenuModel.getId());
        } else {
            revenuModel.setRevenuConjoint(Boolean.FALSE);
            revenuModel.setRevenuIFD(Boolean.FALSE);
        }

        return revenuModel;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.RevenuModelService#read(java.lang.String)
     */
    @Override
    public RevenuModel read(String idRevenuModel) throws JadeApplicationException, JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idRevenuModel)) {
            throw new ALRevenuModelException("unable to read revenu-the id passed is empty");
        }
        RevenuModel revenuModel = new RevenuModel();
        revenuModel.setId(idRevenuModel);
        return (RevenuModel) JadePersistenceManager.read(revenuModel);
    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.RevenuModelService#search(ch.globaz.al.business.models.allocataire.RevenuSearchModel)
     */
    @Override
    public RevenuSearchModel search(RevenuSearchModel revenuSearchModel) throws JadeApplicationException,
            JadePersistenceException {
        if (revenuSearchModel == null) {
            throw new ALRevenuModelException("unable to search revenuModel - the model passed is null");
        }
        return (RevenuSearchModel) JadePersistenceManager.search(revenuSearchModel);
    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.RevenuModelService#searchDernierRevenu(java.lang.String,
     *      java.lang.String, boolean)
     */
    @Override
    public RevenuModel searchDernierRevenu(String date, String idAllocataire, boolean isConjoint)
            throws JadePersistenceException, JadeApplicationException {
        // si la date n'est pas valide
        if (!JadeDateUtil.isGlobazDate(date)) {
            throw new ALRevenuModelException("'" + date + "' is not a valid date");
        }

        // si l'id de l'allocataire est vide
        if (JadeStringUtil.isEmpty(idAllocataire)) {
            throw new ALRevenuModelException("unable to search revenu - the passed idAllocataire is empty or null");
        }

        // préparation de la recherche
        RevenuSearchModel search = new RevenuSearchModel();
        search.setForBeforeDate(date);
        search.setForIdAllocataire(idAllocataire);
        search.setForIsConjoint(new Boolean(isConjoint));
        // search.setWhereKey("forBeforeDate");
        search.setWhereKey("dateBefore");
        search.setOrderKey("dateDesc");
        search.setDefinedSearchSize(1);

        JadePersistenceManager.search(search);

        // on retourne null si aucun revenu n'a été trouvé. sinon on prend le
        // premier (le plus récent) de la liste
        if (search.getSize() == 0) {
            return null;
        } else {
            return (RevenuModel) search.getSearchResults()[0];
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.allocataire.RevenuModelService#update(ch.globaz.al.business.models.allocataire.RevenuModel)
     */
    @Override
    public RevenuModel update(RevenuModel revenuModel) throws JadeApplicationException, JadePersistenceException {
        if (revenuModel == null) {
            throw new ALRevenuModelException("Unable to update revenu-the model passed is null");
        }
        if (revenuModel.isNew()) {
            throw new ALRevenuModelException("Unable to update revenu-the model passed is new");
        }
        RevenuModelChecker.validate(revenuModel);

        return (RevenuModel) JadePersistenceManager.update(revenuModel);
    }
}
