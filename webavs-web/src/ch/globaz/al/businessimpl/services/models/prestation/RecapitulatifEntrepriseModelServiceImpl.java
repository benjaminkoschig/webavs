package ch.globaz.al.businessimpl.services.models.prestation;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.prestation.ALRecapitulatifEntrepriseModelException;
import ch.globaz.al.business.models.prestation.EntetePrestationSearchModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseSearchModel;
import ch.globaz.al.business.services.models.prestation.RecapitulatifEntrepriseModelService;
import ch.globaz.al.businessimpl.checker.model.prestation.RecapitulatifEntrepriseModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * classe d'implémentation des services de RecapitulatifEntrepriseModel
 * 
 * @author PTA
 * 
 */
public class RecapitulatifEntrepriseModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        RecapitulatifEntrepriseModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation. RecapitulatifEntrepriseModelService
     * #count(ch.globaz.al.business.models.prestation .RecapitulatifEntrepriseSearchModel)
     */
    @Override
    public int count(RecapitulatifEntrepriseSearchModel recapSearch) throws JadePersistenceException,
            JadeApplicationException {
        if (recapSearch == null) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseModelServiceImpl#count : recapSearch is null");
        }
        return JadePersistenceManager.count(recapSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.prestation.RecapitulatifEntrepriseModelService #
     * create(ch.globaz.al.business.model.prestation.RecapitulatifEntrepriseModel )
     */
    @Override
    public RecapitulatifEntrepriseModel create(RecapitulatifEntrepriseModel recapEntrepriseModel)
            throws JadeApplicationException, JadePersistenceException {
        if (recapEntrepriseModel == null) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "Unable to create recapitulatifEntrepriseModel- the model passed is null");
        }
        RecapitulatifEntrepriseModelChecker.validate(recapEntrepriseModel);
        return (RecapitulatifEntrepriseModel) JadePersistenceManager.add(recapEntrepriseModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.prestation.RecapitulatifEntrepriseModelService #
     * delete(ch.globaz.al.business.model.prestation.RecapitulatifEntrepriseModel )
     */
    @Override
    public RecapitulatifEntrepriseModel delete(RecapitulatifEntrepriseModel recapEntrepriseModel)
            throws JadeApplicationException, JadePersistenceException {
        if (recapEntrepriseModel == null) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "Unable to delete recapitulatifEntrepriseModel- the model passed is null");
        }
        if (recapEntrepriseModel.isNew()) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "Unable to delete recapitulatifEntrepriseModel- the model passed is new");
        }

        RecapitulatifEntrepriseModelChecker.validateForDelete(recapEntrepriseModel);

        // suppression des prestations liées à la récap
        ALImplServiceLocator.getEntetePrestationModelService().deleteForIdRecap(recapEntrepriseModel.getIdRecap());

        return (RecapitulatifEntrepriseModel) JadePersistenceManager.delete(recapEntrepriseModel);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation. RecapitulatifEntrepriseModelService
     * #deleteIfSizeEquals(String, int)
     */
    @Override
    public void deleteIfSizeEquals(String idRecap, int size) throws JadeApplicationException, JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idRecap)) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "unable to delete recapitulatifEntreprise- The id is empty");
        }

        EntetePrestationSearchModel searchEntete = new EntetePrestationSearchModel();
        searchEntete.setForIdRecap(idRecap);
        if (ALImplServiceLocator.getEntetePrestationModelService().count(searchEntete) == size) {

            RecapitulatifEntrepriseSearchModel searchRecap = new RecapitulatifEntrepriseSearchModel();
            searchRecap.setForIdRecap(idRecap);
            JadePersistenceManager.delete(searchRecap);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.prestation.RecapitulatifEntrepriseModelService #read(java.lang.String)
     */
    @Override
    public RecapitulatifEntrepriseModel read(String idRecapEntrepriseModel) throws JadeApplicationException,
            JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idRecapEntrepriseModel)) {
            throw new ALRecapitulatifEntrepriseModelException("unable to read recapitulatifEntreprise- The id is empty");
        }
        RecapitulatifEntrepriseModel recapEntrepriseModel = new RecapitulatifEntrepriseModel();
        recapEntrepriseModel.setId(idRecapEntrepriseModel);
        return (RecapitulatifEntrepriseModel) JadePersistenceManager.read(recapEntrepriseModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation. RecapitulatifEntrepriseModelService
     * #search(ch.globaz.al.business.models.prestation .RecapitulatifEntrepriseSearchModel)
     */
    @Override
    public RecapitulatifEntrepriseSearchModel search(RecapitulatifEntrepriseSearchModel recapSearch)
            throws JadeApplicationException, JadePersistenceException {

        if (recapSearch == null) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseModelServiceImpl#search : unable to search, recapSearch is null");
        }

        return (RecapitulatifEntrepriseSearchModel) JadePersistenceManager.search(recapSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.prestation.RecapitulatifEntrepriseModelService #
     * update(ch.globaz.al.business.model.prestation.RecapitulatifEntrepriseModel )
     */
    @Override
    public RecapitulatifEntrepriseModel update(RecapitulatifEntrepriseModel recapEntrepriseModel)
            throws JadePersistenceException, JadeApplicationException {
        if (recapEntrepriseModel == null) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "unable to update recapitulatifEntreprise- The model passed is null");
        }
        if (recapEntrepriseModel.isNew()) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "unable to update recapitulatifEntreprise- The model passed is new");
        }
        // valide intégrité
        RecapitulatifEntrepriseModelChecker.validate(recapEntrepriseModel);

        return (RecapitulatifEntrepriseModel) JadePersistenceManager.update(recapEntrepriseModel);
    }

    @Override
    public RecapitulatifEntrepriseSearchModel widgetFind(RecapitulatifEntrepriseSearchModel recapSearch)
            throws JadeApplicationException, JadePersistenceException {

        if (recapSearch == null) {
            throw new ALRecapitulatifEntrepriseModelException(
                    "RecapitulatifEntrepriseModelServiceImpl#search : unable to search, recapSearch is null");
        }

        // HACK:pour contourner le "bug" (?) du widget avec fixedValue
        recapSearch.setForNumeroAffilie(JadeStringUtil.removeChar(recapSearch.getForNumeroAffilie(), ','));

        recapSearch.setWhereKey("numFactureSelectable");
        recapSearch.setOrderKey("lastNumFacture");
        return (RecapitulatifEntrepriseSearchModel) JadePersistenceManager.search(recapSearch);

    }
}
