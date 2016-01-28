package ch.globaz.pegasus.businessimpl.services.models.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierServantHabitationPrincipaleException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierServantHabitationPrincipale;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierServantHabitationPrincipaleSearch;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.SimpleBienImmobilierServantHabitationPrincipaleService;
import ch.globaz.pegasus.businessimpl.checkers.fortuneusuelle.SimpleBienImmobilierServantHabitationPrincipaleChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleBienImmobilierServantHabitationPrincipaleServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleBienImmobilierServantHabitationPrincipaleService {

    @Override
    public SimpleBienImmobilierServantHabitationPrincipale create(
            SimpleBienImmobilierServantHabitationPrincipale simpleBienImmobilierServantHabitationPrincipale)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException {
        if (simpleBienImmobilierServantHabitationPrincipale == null) {
            throw new BienImmobilierServantHabitationPrincipaleException(
                    "Unable to create simpleBienImmobilierServantHabitationPrincipale, the model passed is null!");
        }
        SimpleBienImmobilierServantHabitationPrincipaleChecker
                .checkForCreate(simpleBienImmobilierServantHabitationPrincipale);
        return (SimpleBienImmobilierServantHabitationPrincipale) JadePersistenceManager
                .add(simpleBienImmobilierServantHabitationPrincipale);
    }

    @Override
    public SimpleBienImmobilierServantHabitationPrincipale delete(
            SimpleBienImmobilierServantHabitationPrincipale simpleBienImmobilierServantHabitationPrincipale)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException {
        if (simpleBienImmobilierServantHabitationPrincipale == null) {
            throw new BienImmobilierServantHabitationPrincipaleException(
                    "Unable to delete SimpleBienImmobilierServantHabitationPrincipaleChecker, the model passed is null!");
        }
        if (simpleBienImmobilierServantHabitationPrincipale.isNew()) {
            throw new BienImmobilierServantHabitationPrincipaleException(
                    "Unable to delete SimpleBienImmobilierServantHabitationPrincipaleChecker, the model passed is new!");
        }
        SimpleBienImmobilierServantHabitationPrincipaleChecker
                .checkForDelete(simpleBienImmobilierServantHabitationPrincipale);
        return (SimpleBienImmobilierServantHabitationPrincipale) JadePersistenceManager
                .delete(simpleBienImmobilierServantHabitationPrincipale);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleBienImmobilierServantHabitationPrincipaleSearch search = new SimpleBienImmobilierServantHabitationPrincipaleSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    @Override
    public SimpleBienImmobilierServantHabitationPrincipale read(String idBienImmobilierServantHabitationPrincipale)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException {
        if (JadeStringUtil.isEmpty(idBienImmobilierServantHabitationPrincipale)) {
            throw new BienImmobilierServantHabitationPrincipaleException(
                    "Unable to read SimpleBienImmobilierServantHabitationPrincipaleChecker, the id passed is not defined!");
        }
        SimpleBienImmobilierServantHabitationPrincipale simpleBienImmobilierServantHabitationPrincipale = new SimpleBienImmobilierServantHabitationPrincipale();
        simpleBienImmobilierServantHabitationPrincipale.setId(idBienImmobilierServantHabitationPrincipale);
        return (SimpleBienImmobilierServantHabitationPrincipale) JadePersistenceManager
                .read(simpleBienImmobilierServantHabitationPrincipale);
    }

    @Override
    public SimpleBienImmobilierServantHabitationPrincipale update(
            SimpleBienImmobilierServantHabitationPrincipale simpleBienImmobilierServantHabitationPrincipale)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException {
        if (simpleBienImmobilierServantHabitationPrincipale == null) {
            throw new BienImmobilierServantHabitationPrincipaleException(
                    "Unable to update SimpleBienImmobilierServantHabitationPrincipaleChecker, the model passed is null!");
        }
        if (simpleBienImmobilierServantHabitationPrincipale.isNew()) {
            throw new BienImmobilierServantHabitationPrincipaleException(
                    "Unable to update SimpleBienImmobilierServantHabitationPrincipaleChecker, the model passed is new!");
        }
        SimpleBienImmobilierServantHabitationPrincipaleChecker
                .checkForUpdate(simpleBienImmobilierServantHabitationPrincipale);
        return (SimpleBienImmobilierServantHabitationPrincipale) JadePersistenceManager
                .update(simpleBienImmobilierServantHabitationPrincipale);
    }

}