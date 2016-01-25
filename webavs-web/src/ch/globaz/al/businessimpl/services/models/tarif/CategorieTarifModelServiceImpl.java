package ch.globaz.al.businessimpl.services.models.tarif;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.tarif.ALCategorieTarifModelException;
import ch.globaz.al.business.models.tarif.CategorieTarifModel;
import ch.globaz.al.business.models.tarif.CategorieTarifSearchModel;
import ch.globaz.al.business.services.models.tarif.CategorieTarifModelService;
import ch.globaz.al.businessimpl.checker.model.tarif.CategorieTarifModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * classe d'implémentation des services de categorieTarifmodel
 * 
 * @author PTA
 * 
 */
public class CategorieTarifModelServiceImpl extends ALAbstractBusinessServiceImpl implements CategorieTarifModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.tarif.CategorieTarifModelService
     * #count(ch.globaz.al.business.models.tarif.CategorieTarifSearch)
     */
    @Override
    public int count(CategorieTarifSearchModel search) throws JadePersistenceException, JadeApplicationException {

        if (search == null) {
            throw new ALCategorieTarifModelException("Unable to count, the passed model is null");
        }

        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.tarif.CategorieTarifModelService#create
     * (ch.globaz.al.business.model.tarif.CategorieTarifModel)
     */
    @Override
    public CategorieTarifModel create(CategorieTarifModel categorieTarifModel) throws JadePersistenceException,
            JadeApplicationException {
        if (categorieTarifModel == null) {
            throw new ALCategorieTarifModelException("Unable to create categorieTarif-the model passed is null");
        }
        CategorieTarifModelChecker.validate(categorieTarifModel);
        return (CategorieTarifModel) JadePersistenceManager.add(categorieTarifModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.tarif.CategorieTarifModelService#read(java .lang.String)
     */
    @Override
    public CategorieTarifModel read(String idCategorieTarifModel) throws JadeApplicationException,
            JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idCategorieTarifModel)) {
            throw new ALCategorieTarifModelException("unable to read categorieModelException-the id passed is empty");

        }
        CategorieTarifModel categorieTarifModel = new CategorieTarifModel();
        categorieTarifModel.setId(idCategorieTarifModel);
        return (CategorieTarifModel) JadePersistenceManager.read(categorieTarifModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.tarif.CategorieTarifModelService
     * #search(ch.globaz.al.business.models.tarif.CategorieTarifSearch)
     */
    @Override
    public CategorieTarifSearchModel search(CategorieTarifSearchModel categorieTarifSearch)
            throws JadeApplicationException, JadePersistenceException {

        if (categorieTarifSearch == null) {
            throw new ALCategorieTarifModelException(
                    "CategorieTarifModelServiceImpl#search : categorieTarifSearch is null");
        }

        return (CategorieTarifSearchModel) JadePersistenceManager.search(categorieTarifSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.tarif.CategorieTarifModelService#update
     * (ch.globaz.al.business.model.tarif.CategorieTarifModel)
     */
    @Override
    public CategorieTarifModel update(CategorieTarifModel categorieTarifModel) throws JadePersistenceException,
            JadeApplicationException {
        if (categorieTarifModel == null) {
            throw new ALCategorieTarifModelException("unable to update categorieTarif-the model passed is null");

        }
        if (categorieTarifModel.isNew()) {
            throw new ALCategorieTarifModelException("unable to update categorieTarif-the model passed is new");
        }
        // contrôle la validité des données
        CategorieTarifModelChecker.validate(categorieTarifModel);

        // ajoute le categorieTarif dans la persistance et le retourne
        return (CategorieTarifModel) JadePersistenceManager.update(categorieTarifModel);
    }

}
