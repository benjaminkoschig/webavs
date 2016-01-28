package ch.globaz.pegasus.businessimpl.services.models.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierNonHabitableException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierNonHabitable;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierNonHabitableSearch;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.SimpleBienImmobilierNonHabitableService;
import ch.globaz.pegasus.businessimpl.checkers.fortuneusuelle.SimpleBienImmobilierNonHabitableChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleBienImmobilierNonHabitableServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleBienImmobilierNonHabitableService {

    @Override
    public SimpleBienImmobilierNonHabitable create(SimpleBienImmobilierNonHabitable simpleBienImmobilierNonHabitable)
            throws JadePersistenceException, BienImmobilierNonHabitableException {
        if (simpleBienImmobilierNonHabitable == null) {
            throw new BienImmobilierNonHabitableException(
                    "Unable to create simpleBienImmobilierNonHabitable, the model passed is null!");
        }
        SimpleBienImmobilierNonHabitableChecker.checkForCreate(simpleBienImmobilierNonHabitable);
        return (SimpleBienImmobilierNonHabitable) JadePersistenceManager.add(simpleBienImmobilierNonHabitable);
    }

    @Override
    public SimpleBienImmobilierNonHabitable delete(SimpleBienImmobilierNonHabitable simpleBienImmobilierNonHabitable)
            throws BienImmobilierNonHabitableException, JadePersistenceException {
        if (simpleBienImmobilierNonHabitable == null) {
            throw new BienImmobilierNonHabitableException(
                    "Unable to delete SimpleBienImmobilierNonHabitableChecker, the model passed is null!");
        }
        if (simpleBienImmobilierNonHabitable.isNew()) {
            throw new BienImmobilierNonHabitableException(
                    "Unable to delete SimpleBienImmobilierNonHabitableChecker, the model passed is new!");
        }
        SimpleBienImmobilierNonHabitableChecker.checkForDelete(simpleBienImmobilierNonHabitable);
        return (SimpleBienImmobilierNonHabitable) JadePersistenceManager.delete(simpleBienImmobilierNonHabitable);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleBienImmobilierNonHabitableSearch search = new SimpleBienImmobilierNonHabitableSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    @Override
    public SimpleBienImmobilierNonHabitable read(String idBienImmobilierNonHabitable) throws JadePersistenceException,
            BienImmobilierNonHabitableException {
        if (JadeStringUtil.isEmpty(idBienImmobilierNonHabitable)) {
            throw new BienImmobilierNonHabitableException(
                    "Unable to read SimpleBienImmobilierNonHabitableChecker, the id passed is not defined!");
        }
        SimpleBienImmobilierNonHabitable simpleBienImmobilierNonHabitable = new SimpleBienImmobilierNonHabitable();
        simpleBienImmobilierNonHabitable.setId(idBienImmobilierNonHabitable);
        return (SimpleBienImmobilierNonHabitable) JadePersistenceManager.read(simpleBienImmobilierNonHabitable);
    }

    @Override
    public SimpleBienImmobilierNonHabitable update(SimpleBienImmobilierNonHabitable simpleBienImmobilierNonHabitable)
            throws JadePersistenceException, BienImmobilierNonHabitableException {
        if (simpleBienImmobilierNonHabitable == null) {
            throw new BienImmobilierNonHabitableException(
                    "Unable to update SimpleBienImmobilierNonHabitableChecker, the model passed is null!");
        }
        if (simpleBienImmobilierNonHabitable.isNew()) {
            throw new BienImmobilierNonHabitableException(
                    "Unable to update SimpleBienImmobilierNonHabitableChecker, the model passed is new!");
        }
        SimpleBienImmobilierNonHabitableChecker.checkForUpdate(simpleBienImmobilierNonHabitable);
        return (SimpleBienImmobilierNonHabitable) JadePersistenceManager.update(simpleBienImmobilierNonHabitable);
    }

}