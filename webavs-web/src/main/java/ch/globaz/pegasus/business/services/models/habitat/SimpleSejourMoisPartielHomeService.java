package ch.globaz.pegasus.business.services.models.habitat;

import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.exceptions.models.habitat.SejourMoisPartielHomeException;
import ch.globaz.pegasus.business.models.habitat.SimpleSejourMoisPartielHome;
import ch.globaz.pegasus.business.models.habitat.SimpleSejourMoisPartielHomeSearch;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

import java.util.List;

public interface SimpleSejourMoisPartielHomeService extends JadeApplicationService {

    /**
     * Permet la création d'une entité simpleIndemniteJournaliere.
     * 
     * @param simpleSejourMoisPartielHome
     *            La simpleSejourMoisPartielHome à créer
     * @return le simpleSejourMoisPartielHome créé
     * @throws LoyerException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    SimpleSejourMoisPartielHome create(SimpleSejourMoisPartielHome simpleSejourMoisPartielHome)
            throws SejourMoisPartielHomeException, DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité simpleIndemniteJournaliere
     * 
     * @param simpleSejourMoisPartielHome
     *            le simpleSejourMoisPartielHome à supprimer
     * @return le simpleSejourMoisPartielHome supprimé
     * @throws SejourMoisPartielHomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    SimpleSejourMoisPartielHome delete(SimpleSejourMoisPartielHome simpleSejourMoisPartielHome) throws SejourMoisPartielHomeException,
            JadePersistenceException;

    /**
     * Permet l'effacement de simpleSejourMoisPartielHome en donnant une liste des id de leurs headerDonneeFinanciere
     * 
     * @param listeIDString
     *            la liste des id des headerDonneeFinanciere pour lesquels il faut effacer les simpleSejourMoisPartielHome
     * @throws JadePersistenceException
     */
    void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire un versionDroit
     * 
     * @param idSimpleSejourMoisPartielHome
     *            L'identifiant de le simpleSejourMoisPartielHomee à charger en mémoire
     * @return le simpleSejourMoisPartielHome chargé en mémoire
     * @throws SejourMoisPartielHomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    SimpleSejourMoisPartielHome read(String idSimpleSejourMoisPartielHome) throws SejourMoisPartielHomeException,
            JadePersistenceException;

    /**
     * Permet la recherche sur les simpel taxes journaliere Home
     * 
     * @param simpleTaxesJournaliereSearch
     * @return
     * @throws SejourMoisPartielHomeException
     * @throws JadePersistenceException
     */
    SimpleSejourMoisPartielHomeSearch search(SimpleSejourMoisPartielHomeSearch simpleTaxesJournaliereSearch)
            throws SejourMoisPartielHomeException, JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité simpleIndemniteJournaliere
     * 
     * @param simpleSejourMoisPartielHome
     *            le simpleSejourMoisPartielHome à mettre à jour
     * @return le simpleSejourMoisPartielHome mis à jour
     * @throws SejourMoisPartielHomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    SimpleSejourMoisPartielHome update(SimpleSejourMoisPartielHome simpleSejourMoisPartielHome) throws SejourMoisPartielHomeException,
            DonneeFinanciereException, JadePersistenceException;

}
