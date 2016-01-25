/**
 * 
 */
package ch.globaz.al.businessimpl.services.models.adi;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.adi.ALAdiEnfantMoisModelException;
import ch.globaz.al.business.models.adi.AdiEnfantMoisModel;
import ch.globaz.al.business.models.adi.AdiEnfantMoisSearchModel;
import ch.globaz.al.business.services.models.adi.AdiEnfantMoisModelService;
import ch.globaz.al.businessimpl.checker.model.adi.AdiEnfantMoisModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * classe implémentation des services de AdiEnfantMoisModel
 * 
 * @author PTA
 * 
 */
public class AdiEnfantMoisModelServiceImpl extends ALAbstractBusinessServiceImpl implements AdiEnfantMoisModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.adi.AdiEnfantMoisModelService#create(ch
     * .globaz.al.business.model.adi.AdiEnfantMoisModel)
     */
    @Override
    public AdiEnfantMoisModel create(AdiEnfantMoisModel adiEnfantMoisModel) throws JadeApplicationException,
            JadePersistenceException {
        if (adiEnfantMoisModel == null) {
            throw new ALAdiEnfantMoisModelException("Unable to create AdiEnfantMois - the model passed is null");
        }
        // lance le contrôle de validité des données de AdiEnfantMoisModel
        AdiEnfantMoisModelChecker.validate(adiEnfantMoisModel);
        // ajoute l'adiEnfantMoisModel dans la persistence et le retourne
        return (AdiEnfantMoisModel) JadePersistenceManager.add(adiEnfantMoisModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.adi.AdiEnfantMoisModelService#delete(ch
     * .globaz.al.business.model.adi.AdiEnfantMoisModel)
     */
    @Override
    public AdiEnfantMoisModel delete(AdiEnfantMoisModel adiEnfantMoisModel) throws JadeApplicationException,
            JadePersistenceException {
        if (adiEnfantMoisModel == null) {
            throw new ALAdiEnfantMoisModelException("Unable to delete AdiEnfantMoisModel- the model passed is null");
        } else if (adiEnfantMoisModel.isNew()) {
            throw new ALAdiEnfantMoisModelException("Unable to delete AdiEnfantMoisModel -the model passed is new");
        }
        // ajoute l'adiEnfantMoisModel et le supprime
        return (AdiEnfantMoisModel) JadePersistenceManager.delete(adiEnfantMoisModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.adi.AdiEnfantMoisModelService#read(java .lang.String)
     */
    @Override
    public AdiEnfantMoisModel read(String idAdiEnfantMoisModel) throws JadeApplicationException,
            JadePersistenceException {
        if (JadeStringUtil.isEmpty(idAdiEnfantMoisModel)) {
            throw new ALAdiEnfantMoisModelException("Unable to read AdiEnfantMoisModel- the id passed is null");
        }
        AdiEnfantMoisModel adiEnfantMoisModel = new AdiEnfantMoisModel();
        adiEnfantMoisModel.setId(idAdiEnfantMoisModel);
        return (AdiEnfantMoisModel) JadePersistenceManager.read(adiEnfantMoisModel);
    }

    @Override
    public AdiEnfantMoisSearchModel search(AdiEnfantMoisSearchModel adiEnfantMoisSearch)
            throws JadeApplicationException, JadePersistenceException {
        if (adiEnfantMoisSearch == null) {
            throw new ALAdiEnfantMoisModelException("unable to search adiEnfantMoisSearch - the model passed is null");
        }
        return (AdiEnfantMoisSearchModel) JadePersistenceManager.search(adiEnfantMoisSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.adi.AdiEnfantMoisModelService#update(ch
     * .globaz.al.business.model.adi.AdiEnfantMoisModel)
     */
    @Override
    public AdiEnfantMoisModel update(AdiEnfantMoisModel adiEnfantMoisModel) throws JadeApplicationException,
            JadePersistenceException {

        if (adiEnfantMoisModel == null) {
            throw new ALAdiEnfantMoisModelException("Unable to update AdiEnfantMoisModel - the model passed is null");
        } else if (adiEnfantMoisModel.isNew()) {
            throw new ALAdiEnfantMoisModelException("Unable to update AdiEnfantMoisModel - the model passed is new");
        }
        AdiEnfantMoisModelChecker.validate(adiEnfantMoisModel);
        return (AdiEnfantMoisModel) JadePersistenceManager.update(adiEnfantMoisModel);
    }

}
