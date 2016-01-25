package ch.globaz.pegasus.businessimpl.services.models.calcul;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculMembreFamilleSearch;
import ch.globaz.pegasus.business.services.models.calcul.CalculMembreFamilleService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class CalculMembreFamilleServiceImpl extends PegasusAbstractServiceImpl implements CalculMembreFamilleService {

    @Override
    public CalculMembreFamilleSearch search(CalculMembreFamilleSearch calculMembreFamilleSearch)
            throws JadePersistenceException, CalculException {
        if (calculMembreFamilleSearch == null) {
            throw new CalculException("Unable to read calculMembreFamille, the id passed is null!");
        }
        return (CalculMembreFamilleSearch) JadePersistenceManager.search(calculMembreFamilleSearch);
    }

}
