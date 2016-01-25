package ch.globaz.perseus.businessimpl.services.models.situationfamille;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.situationfamille.SituationFamilleException;
import ch.globaz.perseus.business.models.situationfamille.SimpleSituationFamiliale;
import ch.globaz.perseus.business.services.models.situationfamille.SimpleSituationFamilialeService;
import ch.globaz.perseus.businessimpl.checkers.situationfamille.SimpleSituationFamilialeChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * TODO Javadoc
 * 
 * @author vyj
 */
public class SimpleSituationFamilialeServiceImpl extends PerseusAbstractServiceImpl implements
        SimpleSituationFamilialeService {
    @Override
    public SimpleSituationFamiliale create(SimpleSituationFamiliale situationFamiliale)
            throws JadePersistenceException, SituationFamilleException {
        if (situationFamiliale == null) {
            throw new SituationFamilleException(
                    "Unable to create a simple situationFamiliale, the model passed is null!");
        }
        SimpleSituationFamilialeChecker.checkForCreate(situationFamiliale);
        return (SimpleSituationFamiliale) JadePersistenceManager.add(situationFamiliale);
    }

    @Override
    public SimpleSituationFamiliale delete(SimpleSituationFamiliale situationFamiliale)
            throws JadePersistenceException, SituationFamilleException {
        if (situationFamiliale == null) {
            throw new SituationFamilleException(
                    "Unable to delete a simple situationFamiliale, the model passed is null!");
        } else if (situationFamiliale.isNew()) {
            throw new SituationFamilleException(
                    "Unable to delete a simple situationFamiliale, the model passed is new!");
        }
        SimpleSituationFamilialeChecker.checkForDelete(situationFamiliale);
        return (SimpleSituationFamiliale) JadePersistenceManager.delete(situationFamiliale);
    }

    @Override
    public SimpleSituationFamiliale read(String idSituationFamiliale) throws JadePersistenceException,
            SituationFamilleException {
        if (JadeStringUtil.isEmpty(idSituationFamiliale)) {
            throw new SituationFamilleException("Unable to read a simple situationFamiliale, the id passed is null!");
        }
        SimpleSituationFamiliale situationFamiliale = new SimpleSituationFamiliale();
        situationFamiliale.setId(idSituationFamiliale);
        return (SimpleSituationFamiliale) JadePersistenceManager.read(situationFamiliale);
    }

    @Override
    public SimpleSituationFamiliale update(SimpleSituationFamiliale situationFamiliale)
            throws JadePersistenceException, SituationFamilleException {
        if (situationFamiliale == null) {
            throw new SituationFamilleException(
                    "Unable to update a simple situationFamiliale, the model passed is null!");
        } else if (situationFamiliale.isNew()) {
            throw new SituationFamilleException(
                    "Unable to update a simple situationFamiliale, the model passed is new!");
        }
        SimpleSituationFamilialeChecker.checkForUpdate(situationFamiliale);
        return (SimpleSituationFamiliale) JadePersistenceManager.update(situationFamiliale);
    }

}
