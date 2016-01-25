package ch.globaz.pegasus.business.services.models.calcul;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculMembreFamilleSearch;

public interface CalculMembreFamilleService extends JadeApplicationService {

    public CalculMembreFamilleSearch search(CalculMembreFamilleSearch calculMembreFamilleSearch)
            throws JadePersistenceException, CalculException;
}
