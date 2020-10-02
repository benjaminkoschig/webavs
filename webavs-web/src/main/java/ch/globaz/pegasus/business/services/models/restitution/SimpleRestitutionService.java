package ch.globaz.pegasus.business.services.models.restitution;

import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.restitution.PCRestitutionException;
import ch.globaz.pegasus.business.models.restitution.SimpleRestitution;
import ch.globaz.pegasus.business.models.restitution.SimpleRestitutionSearch;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public interface SimpleRestitutionService extends JadeApplicationService {

    /**
     * Méthode permettant de récupérer une SimpleRestitutionSearch.
     * @param simpleRestitutionSearch
     * @return
     */
    SimpleRestitutionSearch search(SimpleRestitutionSearch simpleRestitutionSearch) throws PCRestitutionException, JadePersistenceException;


    /**
     * Méthode permettant de créer une restitution de PC.
     * @param simpleRestitution
     * @throws PCRestitutionException
     * @return
     */
    SimpleRestitution create(SimpleRestitution simpleRestitution) throws PCRestitutionException, JadeApplicationServiceNotAvailableException, DossierException, JadePersistenceException;

    /**
     * Méthode permettant de mettre à jour une restitution de PC.
     * @param simpleRestitution
     * @return
     * @throws PCRestitutionException
     * @throws JadePersistenceException
     */
    SimpleRestitution update(SimpleRestitution simpleRestitution) throws PCRestitutionException, JadePersistenceException;

    /**
     *  Méthode permettant de vérifier qu'il n'y a pas de résultat pour cette requête.
     * @param search
     * @return
     * @throws PCRestitutionException
     * @throws JadePersistenceException
     */
    int count(SimpleRestitutionSearch search) throws PCRestitutionException, JadePersistenceException;
}
