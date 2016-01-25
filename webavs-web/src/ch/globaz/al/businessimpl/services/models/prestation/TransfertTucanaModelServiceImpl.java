package ch.globaz.al.businessimpl.services.models.prestation;

import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.model.dossier.ALDossierModelException;
import ch.globaz.al.business.exceptions.model.droit.ALEnfantModelException;
import ch.globaz.al.business.exceptions.model.prestation.ALTransfertTucanaModelException;
import ch.globaz.al.business.models.prestation.TransfertTucanaModel;
import ch.globaz.al.business.models.prestation.TransfertTucanaSearchModel;
import ch.globaz.al.business.services.models.prestation.TransfertTucanaModelService;
import ch.globaz.al.businessimpl.checker.model.prestation.TransfertTucanaModelChecker;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * classe d'implémentation des services de TransfertTucanaModel
 * 
 * @author PTA
 * 
 */
public class TransfertTucanaModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        TransfertTucanaModelService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation.TransfertTucanaModelService
     * #count(ch.globaz.al.business.models.prestation.TransfertTucanaSearch)
     */
    @Override
    public int count(TransfertTucanaSearchModel tucanaSearch) throws JadeApplicationException, JadePersistenceException {

        if (tucanaSearch == null) {
            throw new ALEnfantModelException("TransfertTucanaModelServiceImpl#count : tucanaSearch is null");
        }

        return JadePersistenceManager.count(tucanaSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.prestation.TransfertTucanaModelService#
     * create(ch.globaz.al.business.model.prestation.TransfertTucanaModel)
     */
    @Override
    public TransfertTucanaModel create(TransfertTucanaModel transTucanaModel) throws JadeApplicationException,
            JadePersistenceException {
        if (transTucanaModel == null) {
            throw new ALTransfertTucanaModelException("Unable to create transfertTucanaModele-the model passed is null");
        }
        TransfertTucanaModelChecker.validate(transTucanaModel);

        return (TransfertTucanaModel) JadePersistenceManager.add(transTucanaModel);
    }

    @Override
    public TransfertTucanaModel delete(TransfertTucanaModel transTucanaModel) throws JadeApplicationException,
            JadePersistenceException {
        if (transTucanaModel == null) {
            throw new ALTransfertTucanaModelException("Unable to create transfertTucanaModele-the model passed is null");
        }
        if (transTucanaModel.isNew()) {
            throw new ALTransfertTucanaModelException("Unable to create transfertTucanaModele-the model passed is new");
        }
        return (TransfertTucanaModel) JadePersistenceManager.delete(transTucanaModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.service.prestation.TransfertTucanaModelService# read(java.lang.String)
     */

    @Override
    public TransfertTucanaModel read(String idtransTucanaModel) throws JadeApplicationException,
            JadePersistenceException {
        if (JadeNumericUtil.isEmptyOrZero(idtransTucanaModel)) {
            throw new ALTransfertTucanaModelException("unable to read the TransfertTucana- the id passed is empty");
        }
        TransfertTucanaModel transTucanaModel = new TransfertTucanaModel();
        transTucanaModel.setId(idtransTucanaModel);
        return (TransfertTucanaModel) JadePersistenceManager.read(transTucanaModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.dossier.DossierModelService#search
     * (ch.globaz.al.business.models.dossier.DossierSearchModel)
     */
    @Override
    public TransfertTucanaSearchModel search(TransfertTucanaSearchModel search) throws JadeApplicationException,
            JadePersistenceException {
        if (search == null) {
            throw new ALDossierModelException("TransfertTucanaModelServiceImpl#search : search is null");
        }

        return (TransfertTucanaSearchModel) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.al.business.services.models.prestation.TransfertTucanaModelService
     * #update(ch.globaz.al.business.models.prestation.TransfertTucanaModel)
     */
    @Override
    public TransfertTucanaModel update(TransfertTucanaModel transTucanaModel) throws JadeApplicationException,
            JadePersistenceException {
        if (transTucanaModel == null) {
            throw new ALTransfertTucanaModelException("Unable to update transfertTucana- the model passed is null");

        }
        if (transTucanaModel.isNew()) {
            throw new ALTransfertTucanaModelException("Unable to update transfertTucana- the model passed is new");

        }
        // lance le contrôle d'intégrité
        TransfertTucanaModelChecker.validate(transTucanaModel);

        return (TransfertTucanaModel) JadePersistenceManager.update(transTucanaModel);
    }
}
