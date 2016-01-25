package ch.globaz.pegasus.businessimpl.services.models.calcul;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesHomeSearch;
import ch.globaz.pegasus.business.services.models.calcul.CalculDonneesHomeService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class CalculDonneesHomeServiceImpl extends PegasusAbstractServiceImpl implements CalculDonneesHomeService {

    @Override
    public CalculDonneesHomeSearch search(CalculDonneesHomeSearch homeSearch) throws CalculException,
            JadePersistenceException {
        if (homeSearch == null) {
            throw new CalculException("Unable to read calculDonneesHome, the id passed is null!");
        }
        return (CalculDonneesHomeSearch) JadePersistenceManager.search(homeSearch);
    }

}
