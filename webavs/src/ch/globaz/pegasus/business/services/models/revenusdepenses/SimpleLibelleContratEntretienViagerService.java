package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.SimpleLibelleContratEntretienViagerException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleLibelleContratEntretienViager;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleLibelleContratEntretienViagerSearch;

public interface SimpleLibelleContratEntretienViagerService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleLibelleContratEntretienViager
     * 
     * @param SimpleLibelleContratEntretienViager
     *            L'entité SimpleLibelleContratEntretienViager à créer
     * @return L'entité SimpleLibelleContratEntretienViager créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws SimpleLibelleContratEntretienViagerException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleLibelleContratEntretienViager create(
            SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager) throws JadePersistenceException,
            SimpleLibelleContratEntretienViagerException;

    /**
     * Permet la suppression d'une entité SimpleLibelleContratEntretienViager
     * 
     * @param SimpleLibelleContratEntretienViager
     *            L'entité SimpleLibelleContratEntretienViager à supprimer
     * @return L'entité SimpleLibelleContratEntretienViager supprimé
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws SimpleLibelleContratEntretienViagerException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleLibelleContratEntretienViager delete(
            SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager)
            throws SimpleLibelleContratEntretienViagerException, JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité SimpleLibelleContratEntretienViager
     * 
     * @param idLibelleContratEntretienViager
     *            L'identifiant de l'entité SimpleLibelleContratEntretienViager à charger en mémoire
     * @return L'entité SimpleLibelleContratEntretienViager chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws SimpleLibelleContratEntretienViagerException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleLibelleContratEntretienViager read(String idLibelleContratEntretienViager)
            throws JadePersistenceException, SimpleLibelleContratEntretienViagerException;

    /**
     * 
     * Permet la rechecher des entités SimpleLibelleContratEntretienViager
     * 
     * @param libelleContratEntretienViagerSearch
     * @return
     * @throws SimpleLibelleContratEntretienViagerException
     * @throws JadePersistenceException
     */
    public SimpleLibelleContratEntretienViagerSearch search(
            SimpleLibelleContratEntretienViagerSearch libelleContratEntretienViagerSearch)
            throws SimpleLibelleContratEntretienViagerException, JadePersistenceException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleLibelleContratEntretienViager
     * 
     * @param SimpleLibelleContratEntretienViager
     *            L'entité SimpleLibelleContratEntretienViager à mettre à jour
     * @return L'entité SimpleLibelleContratEntretienViager mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws SimpleLibelleContratEntretienViagerException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleLibelleContratEntretienViager update(
            SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager) throws JadePersistenceException,
            SimpleLibelleContratEntretienViagerException;
}
