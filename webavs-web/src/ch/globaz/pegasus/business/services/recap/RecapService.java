package ch.globaz.pegasus.business.services.recap;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.MutationException;
import ch.globaz.pegasus.business.models.recap.Recap;

public interface RecapService extends JadeApplicationService {

    /**
     * Permet de créer une reap avec tout les informations déjà calculé et pret à être affiché.
     * 
     * @param dateMonth
     *            (01.2012)
     * @return un Objet complex représentant la reacp. Contient tout le calcule et toute les liste.
     * @throws MutationException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public Recap createRecap(String dateMonth) throws MutationException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException;
}
