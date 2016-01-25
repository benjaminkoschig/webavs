package ch.globaz.pegasus.business.services.recap;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.MutationException;
import ch.globaz.pegasus.business.models.recap.Recap;

public interface RecapService extends JadeApplicationService {

    /**
     * Permet de cr�er une reap avec tout les informations d�j� calcul� et pret � �tre affich�.
     * 
     * @param dateMonth
     *            (01.2012)
     * @return un Objet complex repr�sentant la reacp. Contient tout le calcule et toute les liste.
     * @throws MutationException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public Recap createRecap(String dateMonth) throws MutationException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException;
}
