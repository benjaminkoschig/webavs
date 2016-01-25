package ch.globaz.pegasus.business.services.models.habitat;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.LoyerException;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.models.habitat.SimpleTaxeJournaliereHome;
import ch.globaz.pegasus.business.models.habitat.SimpleTaxeJournaliereHomeSearch;

public interface SimpleTaxeJournaliereHomeService extends JadeApplicationService {

    /**
     * Permet la création d'une entité simpleIndemniteJournaliere.
     * 
     * @param simpleTaxeJournaliereHome
     *            La simpleTaxeJournaliereHome à créer
     * @return le simpleTaxeJournaliereHome créé
     * @throws LoyerException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleTaxeJournaliereHome create(SimpleTaxeJournaliereHome simpleTaxeJournaliereHome)
            throws TaxeJournaliereHomeException, DonneeFinanciereException, JadePersistenceException;

    /**
     * Permet la suppression d'une entité simpleIndemniteJournaliere
     * 
     * @param simpleTaxeJournaliereHome
     *            le simpleTaxeJournaliereHome à supprimer
     * @return le simpleTaxeJournaliereHome supprimé
     * @throws TaxeJournaliereHomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleTaxeJournaliereHome delete(SimpleTaxeJournaliereHome simpleLoyer) throws TaxeJournaliereHomeException,
            JadePersistenceException;

    /**
     * Permet l'effacement de simpleTaxeJournaliereHome en donnant une liste des id de leurs headerDonneeFinanciere
     * 
     * @param listeIDString
     *            la liste des id des headerDonneeFinanciere pour lesquels il faut effacer les simpleTaxeJournaliereHome
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire un versionDroit
     * 
     * @param idsimpleTaxeJournaliereHome
     *            L'identifiant de le simpleTaxeJournaliereHomee à charger en mémoire
     * @return le simpleTaxeJournaliereHome chargé en mémoire
     * @throws TaxeJournaliereHomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleTaxeJournaliereHome read(String idSimpleTaxeJournaliereHome) throws TaxeJournaliereHomeException,
            JadePersistenceException;

    /**
     * Permet la recherche sur les simpel taxes journaliere Home
     * 
     * @param simpleTaxesJournaliereSearch
     * @return
     * @throws TaxeJournaliereHomeException
     * @throws JadePersistenceException
     */
    public SimpleTaxeJournaliereHomeSearch search(SimpleTaxeJournaliereHomeSearch simpleTaxesJournaliereSearch)
            throws TaxeJournaliereHomeException, JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité simpleIndemniteJournaliere
     * 
     * @param simpleTaxeJournaliereHome
     *            le simpleTaxeJournaliereHome à mettre à jour
     * @return le simpleTaxeJournaliereHome mis à jour
     * @throws TaxeJournaliereHomeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleTaxeJournaliereHome update(SimpleTaxeJournaliereHome simpleLoyer) throws TaxeJournaliereHomeException,
            DonneeFinanciereException, JadePersistenceException;

}
