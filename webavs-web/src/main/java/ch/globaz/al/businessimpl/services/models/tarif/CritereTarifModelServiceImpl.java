package ch.globaz.al.businessimpl.services.models.tarif;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.tarif.ALCategorieTarifModelException;
import ch.globaz.al.business.exceptions.model.tarif.ALCritereTarifModelException;
import ch.globaz.al.business.models.tarif.CritereTarifModel;
import ch.globaz.al.business.models.tarif.CritereTarifSearchModel;
import ch.globaz.al.business.services.models.tarif.CritereTarifModelService;
import ch.globaz.al.businessimpl.checker.model.tarif.CritereTarifModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * classe d'implémentation des services de persistance de CritereTarif
 * 
 * @author PTA
 * 
 */
public class CritereTarifModelServiceImpl extends ALAbstractBusinessServiceImpl implements CritereTarifModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.tarif.CritereTarifModelService#
     * count(ch.globaz.al.business.models.tarif.CritereTarifSearch)
     */
    @Override
    public int count(CritereTarifSearchModel search) throws JadePersistenceException, JadeApplicationException {

        if (search == null) {
            throw new ALCategorieTarifModelException(
                    "CritereTarifModelServiceImpl#count : Unable to count, search is null");
        }

        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.model.tarif.CritereTarifModelService#create
     * (ch.globaz.al.business.model.tarif.CritereTarifModel)
     */
    @Override
    public CritereTarifModel create(CritereTarifModel critereTarifModel) throws JadeApplicationException,
            JadePersistenceException {
        if (critereTarifModel == null) {
            throw new ALCritereTarifModelException("unable to create CritereTarifModel- the model passed is null");
        }

        CritereTarifModelChecker.validate(critereTarifModel);

        return (CritereTarifModel) JadePersistenceManager.add(critereTarifModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.model.tarif.CritereTarifModelService#read (java.lang.String)
     */
    @Override
    public CritereTarifModel read(String idCritereTarifModel) throws JadeApplicationException, JadePersistenceException {

        if (JadeStringUtil.isEmpty(idCritereTarifModel)) {
            throw new ALCritereTarifModelException(" unable to read CritereTarifModel - the id is empty");
        }
        CritereTarifModel critereTarifModel = new CritereTarifModel();
        critereTarifModel.setId(idCritereTarifModel);

        return (CritereTarifModel) JadePersistenceManager.read(critereTarifModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.tarif.CritereTarifModelService#
     * search(ch.globaz.al.business.models.tarif.CritereTarifSearch)
     */
    @Override
    public CritereTarifSearchModel search(CritereTarifSearchModel critereTarifSearch) throws JadeApplicationException,
            JadePersistenceException {

        return (CritereTarifSearchModel) JadePersistenceManager.search(critereTarifSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.model.tarif.CritereTarifModelService#update
     * (ch.globaz.al.business.model.tarif.CritereTarifModel)
     */
    @Override
    public CritereTarifModel update(CritereTarifModel critereTarifModel) throws JadeApplicationException,
            JadePersistenceException {

        if (critereTarifModel == null) {
            throw new ALCritereTarifModelException("Unable to update CritereTarifModel- the model passed is null");
        } else if (critereTarifModel.isNew()) {
            throw new ALCritereTarifModelException("Unable to update CritereTarifModel - th model passed is new");
        }

        CritereTarifModelChecker.validate(critereTarifModel);

        return (CritereTarifModel) JadePersistenceManager.update(critereTarifModel);
    }

}
