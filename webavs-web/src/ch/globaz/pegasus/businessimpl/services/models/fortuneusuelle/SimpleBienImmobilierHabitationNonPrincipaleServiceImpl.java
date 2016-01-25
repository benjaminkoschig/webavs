package ch.globaz.pegasus.businessimpl.services.models.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierHabitationNonPrincipaleException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierHabitationNonPrincipale;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierHabitationNonPrincipaleSearch;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.SimpleBienImmobilierHabitationNonPrincipaleService;
import ch.globaz.pegasus.businessimpl.checkers.fortuneusuelle.SimpleBienImmobilierHabitationNonPrincipaleChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleBienImmobilierHabitationNonPrincipaleServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleBienImmobilierHabitationNonPrincipaleService {

    @Override
    public SimpleBienImmobilierHabitationNonPrincipale create(
            SimpleBienImmobilierHabitationNonPrincipale simpleBienImmobilierHabitationNonPrincipale)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException {
        if (simpleBienImmobilierHabitationNonPrincipale == null) {
            throw new BienImmobilierHabitationNonPrincipaleException(
                    "Unable to create simpleBienImmobilierHabitationNonPrincipale, the model passed is null!");
        }
        SimpleBienImmobilierHabitationNonPrincipaleChecker.checkForCreate(simpleBienImmobilierHabitationNonPrincipale);
        return (SimpleBienImmobilierHabitationNonPrincipale) JadePersistenceManager
                .add(simpleBienImmobilierHabitationNonPrincipale);
    }

    @Override
    public SimpleBienImmobilierHabitationNonPrincipale delete(
            SimpleBienImmobilierHabitationNonPrincipale simpleBienImmobilierHabitationNonPrincipale)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException {
        if (simpleBienImmobilierHabitationNonPrincipale == null) {
            throw new BienImmobilierHabitationNonPrincipaleException(
                    "Unable to delete SimpleBienImmobilierHabitationNonPrincipaleChecker, the model passed is null!");
        }
        if (simpleBienImmobilierHabitationNonPrincipale.isNew()) {
            throw new BienImmobilierHabitationNonPrincipaleException(
                    "Unable to delete SimpleBienImmobilierHabitationNonPrincipaleChecker, the model passed is new!");
        }
        SimpleBienImmobilierHabitationNonPrincipaleChecker.checkForDelete(simpleBienImmobilierHabitationNonPrincipale);
        return (SimpleBienImmobilierHabitationNonPrincipale) JadePersistenceManager
                .delete(simpleBienImmobilierHabitationNonPrincipale);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleBienImmobilierHabitationNonPrincipaleSearch search = new SimpleBienImmobilierHabitationNonPrincipaleSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    @Override
    public SimpleBienImmobilierHabitationNonPrincipale read(String idBienImmobilierHabitationNonPrincipale)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException {
        if (JadeStringUtil.isEmpty(idBienImmobilierHabitationNonPrincipale)) {
            throw new BienImmobilierHabitationNonPrincipaleException(
                    "Unable to read SimpleBienImmobilierHabitationNonPrincipaleChecker, the id passed is not defined!");
        }
        SimpleBienImmobilierHabitationNonPrincipale simpleBienImmobilierHabitationNonPrincipale = new SimpleBienImmobilierHabitationNonPrincipale();
        simpleBienImmobilierHabitationNonPrincipale.setId(idBienImmobilierHabitationNonPrincipale);
        return (SimpleBienImmobilierHabitationNonPrincipale) JadePersistenceManager
                .read(simpleBienImmobilierHabitationNonPrincipale);
    }

    @Override
    public SimpleBienImmobilierHabitationNonPrincipale update(
            SimpleBienImmobilierHabitationNonPrincipale simpleBienImmobilierHabitationNonPrincipale)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException {
        if (simpleBienImmobilierHabitationNonPrincipale == null) {
            throw new BienImmobilierHabitationNonPrincipaleException(
                    "Unable to update SimpleBienImmobilierHabitationNonPrincipaleChecker, the model passed is null!");
        }
        if (simpleBienImmobilierHabitationNonPrincipale.isNew()) {
            throw new BienImmobilierHabitationNonPrincipaleException(
                    "Unable to update SimpleBienImmobilierHabitationNonPrincipaleChecker, the model passed is new!");
        }
        SimpleBienImmobilierHabitationNonPrincipaleChecker.checkForUpdate(simpleBienImmobilierHabitationNonPrincipale);
        return (SimpleBienImmobilierHabitationNonPrincipale) JadePersistenceManager
                .update(simpleBienImmobilierHabitationNonPrincipale);
    }

}