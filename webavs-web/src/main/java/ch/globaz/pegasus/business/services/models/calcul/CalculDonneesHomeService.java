package ch.globaz.pegasus.business.services.models.calcul;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesHomeSearch;

public interface CalculDonneesHomeService extends JadeApplicationService {

    public CalculDonneesHomeSearch search(CalculDonneesHomeSearch homeSearch) throws CalculException,
            JadePersistenceException;

}
