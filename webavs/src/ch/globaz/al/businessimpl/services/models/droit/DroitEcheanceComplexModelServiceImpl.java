package ch.globaz.al.businessimpl.services.models.droit;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.al.business.exceptions.droitEcheance.ALDroitEcheanceException;
import ch.globaz.al.business.exceptions.model.droit.ALDroitModelException;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexModel;
import ch.globaz.al.business.models.droit.DroitEcheanceComplexSearchModel;
import ch.globaz.al.business.services.models.droit.DroitEcheanceComplexModelService;
import ch.globaz.al.businessimpl.services.ALAbstractBusinessServiceImpl;

/**
 * 
 * Classe d'implémentation des services liés à l'échéance des droits
 * 
 * @author PTA
 */
public class DroitEcheanceComplexModelServiceImpl extends ALAbstractBusinessServiceImpl implements
        DroitEcheanceComplexModelService {

    @Override
    public DroitEcheanceComplexModel read(String idDroit) throws JadeApplicationException, JadePersistenceException {
        if (JadeStringUtil.isEmpty(idDroit)) {
            throw new ALDroitModelException(
                    "DroitEcheanceComplexModelServiceImpl#read: unable to read droitEcheanceComplexModel- the id passed is empty");

        }
        DroitEcheanceComplexModel droitEcheanceComplexModel = new DroitEcheanceComplexModel();
        droitEcheanceComplexModel.setId(idDroit);

        return (DroitEcheanceComplexModel) JadePersistenceManager.read(droitEcheanceComplexModel);
    }

    @Override
    public DroitEcheanceComplexSearchModel search(DroitEcheanceComplexSearchModel droitEcheanceSearch)
            throws JadeApplicationException, JadePersistenceException {
        if (droitEcheanceSearch == null) {
            throw new ALDroitEcheanceException(
                    "DroitEcheanceComplexModelServiceImpl#search: unable to search droitEcheanceSearch- th model passe is null");

        }

        return (DroitEcheanceComplexSearchModel) JadePersistenceManager.search(droitEcheanceSearch);
    }

}
