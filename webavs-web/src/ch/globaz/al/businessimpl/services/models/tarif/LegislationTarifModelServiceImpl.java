/**
 * 
 */
package ch.globaz.al.businessimpl.services.models.tarif;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.tarif.ALLegislationTarifModelException;
import ch.globaz.al.business.exceptions.model.tarif.ALPrestationTarifModelException;
import ch.globaz.al.business.models.tarif.LegislationTarifModel;
import ch.globaz.al.business.models.tarif.LegislationTarifSearchModel;
import ch.globaz.al.business.services.models.tarif.LegislationTarifModelService;
import ch.globaz.al.businessimpl.checker.model.tarif.LegislationTarifModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * classe des services de persistance de legislationTarif
 * 
 * @author PTA
 * 
 */
public class LegislationTarifModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        LegislationTarifModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.tarif.LegislationTarifModelService
     * #count(ch.globaz.al.business.models.tarif.LegislationTarifSearch)
     */
    @Override
    public int count(LegislationTarifSearchModel search) throws JadePersistenceException, JadeApplicationException {

        if (search == null) {
            throw new ALLegislationTarifModelException("Unable to count, the passed model is null");
        }

        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.model.tarif.LegislationTarifModelService
     * #create(ch.globaz.al.business.model.tarif.LegislationTarifModel)
     */
    @Override
    public LegislationTarifModel create(LegislationTarifModel legislationTarifModel) throws JadeApplicationException,
            JadePersistenceException {
        if (legislationTarifModel == null) {
            throw new ALPrestationTarifModelException(
                    "Unable to create LegislationTarifModel - the model passed is null");
        }

        LegislationTarifModelChecker.validate(legislationTarifModel);

        return (LegislationTarifModel) JadePersistenceManager.add(legislationTarifModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.model.tarif.LegislationTarifModelService #read(java.lang.String)
     */
    @Override
    public LegislationTarifModel read(String idLegislationtarifModel) throws JadeApplicationException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idLegislationtarifModel)) {
            throw new ALLegislationTarifModelException("Unable to read LegislationTarifModel - the id passed is empty");
        }
        LegislationTarifModel legislationTarifModel = new LegislationTarifModel();
        legislationTarifModel.setId(idLegislationtarifModel);

        return (LegislationTarifModel) JadePersistenceManager.read(legislationTarifModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.tarif.LegislationTarifModelService
     * #search(ch.globaz.al.business.models.tarif.LegislationTarifSearch)
     */
    @Override
    public LegislationTarifSearchModel search(LegislationTarifSearchModel legislationTarifSearch)
            throws JadeApplicationException, JadePersistenceException {

        if (legislationTarifSearch == null) {
            throw new ALLegislationTarifModelException(
                    "Unable to search LegislationTarifSearch - the model passed is null");
        }

        return (LegislationTarifSearchModel) JadePersistenceManager.search(legislationTarifSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.model.tarif.LegislationTarifModelService
     * #update(ch.globaz.al.business.model.tarif.LegislationTarifModel)
     */
    @Override
    public LegislationTarifModel update(LegislationTarifModel legislationTarifModel) throws JadeApplicationException,
            JadePersistenceException {

        if (legislationTarifModel == null) {
            throw new ALLegislationTarifModelException(
                    "Unable to update LegislationTarifModel - the model passed is null");

        } else if (legislationTarifModel.isNew()) {
            throw new ALLegislationTarifModelException(
                    "Unable to update legislationTarifModel - the model passed is new");
        }

        LegislationTarifModelChecker.validate(legislationTarifModel);

        return (LegislationTarifModel) JadePersistenceManager.update(legislationTarifModel);
    }

}
