package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AutresRevenusException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.ContratEntretienViagerException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleContratEntretienViager;

public interface SimpleContratEntretienViagerService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleContratEntretienViager
     * 
     * @param SimpleContratEntretienViager
     *            L'entité SimpleAutresRevenus à créer
     * @return L'entité SimpleContratEntretienViager créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ContratEntretienViagerException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleContratEntretienViager create(SimpleContratEntretienViager simpleContratEntretienViager)
            throws JadePersistenceException, ContratEntretienViagerException;

    /**
     * Permet la suppression d'une entité SimpleContratEntretienViager
     * 
     * @param SimpleContratEntretienViager
     *            L'entité SimpleAutresRevenus à supprimer
     * @return L'entité SimpleContratEntretienViager supprimé
     * @throws ContratEntretienViagerException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleContratEntretienViager delete(SimpleContratEntretienViager simpleContratEntretienViager)
            throws ContratEntretienViagerException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité SimpleContratEntretienViager
     * 
     * @param idContratEntretienViager
     *            L'identifiant de l'entité SimpleContratEntretienViager à charger en mémoire
     * @return L'entité SimpleContratEntretienViager chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ContratEntretienViagerException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleContratEntretienViager read(String idContratEntretienViager) throws JadePersistenceException,
            ContratEntretienViagerException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleContratEntretienViager
     * 
     * @param SimpleContratEntretienViager
     *            L'entité SimpleContratEntretienViager à mettre à jour
     * @return L'entité SimpleContratEntretienViager mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutresRevenusException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleContratEntretienViager update(SimpleContratEntretienViager simpleContratEntretienViager)
            throws JadePersistenceException, ContratEntretienViagerException;

}
