package ch.globaz.al.businessimpl.services.models.tarif;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.tarif.ALLegislationTarifModelException;
import ch.globaz.al.business.exceptions.model.tarif.ALPrestationTarifModelException;
import ch.globaz.al.business.models.tarif.PrestationTarifModel;
import ch.globaz.al.business.models.tarif.PrestationTarifSearchModel;
import ch.globaz.al.business.services.models.tarif.PrestationTarifModelService;
import ch.globaz.al.businessimpl.checker.model.tarif.PrestationTarifModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * classe d'implémentation des services de persistance de PrestationTarif
 * 
 * @author PTA
 * 
 */
public class PrestationTarifModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        PrestationTarifModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.tarif.PrestationTarifModelService
     * #count(ch.globaz.al.business.models.tarif.PrestationTarifSearch)
     */
    @Override
    public int count(PrestationTarifSearchModel search) throws JadePersistenceException, JadeApplicationException {

        if (search == null) {
            throw new ALPrestationTarifModelException("Unable to count, the passed model is null");
        }

        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.model.tarif.PrestationTarifModelService
     * #create(ch.globaz.al.business.model.tarif.PrestationTarifModel)
     */
    @Override
    public PrestationTarifModel create(PrestationTarifModel prestationTarifModel) throws JadeApplicationException,
            JadePersistenceException {
        if (prestationTarifModel == null) {
            throw new ALPrestationTarifModelException("Unable to create PrestationTarif - the model passed is null");
        }

        PrestationTarifModelChecker.validate(prestationTarifModel);

        return (PrestationTarifModel) JadePersistenceManager.add(prestationTarifModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.model.tarif.PrestationTarifModelService #read(java.lang.String)
     */
    @Override
    public PrestationTarifModel read(String idPrestationTarifModel) throws JadeApplicationException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idPrestationTarifModel)) {
            throw new ALPrestationTarifModelException("Unable to read PrestationTarifModel - the id passed is empty");
        }
        PrestationTarifModel prestationTarifModel = new PrestationTarifModel();
        prestationTarifModel.setId(idPrestationTarifModel);
        return (PrestationTarifModel) JadePersistenceManager.read(prestationTarifModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.tarif.PrestationTarifModelService
     * #search(ch.globaz.al.business.models.tarif.PrestationTarifSearch)
     */
    @Override
    public PrestationTarifSearchModel search(PrestationTarifSearchModel search) throws JadeApplicationException,
            JadePersistenceException {

        if (search == null) {
            throw new ALLegislationTarifModelException(
                    "Unable to search PrestationTarifSearch - the model passed is null");
        }

        return (PrestationTarifSearchModel) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.model.tarif.PrestationTarifModelService
     * #update(ch.globaz.al.business.model.tarif.PrestationTarifModel)
     */
    @Override
    public PrestationTarifModel update(PrestationTarifModel prestationTarifModel) throws JadeApplicationException,
            JadePersistenceException {
        if (prestationTarifModel == null) {
            throw new ALPrestationTarifModelException(
                    "Unable to update PrestationTarifModel - the model passed is null");
        } else if (prestationTarifModel.isNew()) {
            throw new ALPrestationTarifModelException("Unable to update PrestationTarifModel - the model passed is new");
        }

        PrestationTarifModelChecker.validate(prestationTarifModel);

        return (PrestationTarifModel) JadePersistenceManager.update(prestationTarifModel);
    }

}
