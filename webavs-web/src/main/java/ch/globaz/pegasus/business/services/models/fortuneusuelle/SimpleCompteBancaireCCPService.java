package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.CompteBancaireCCPException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleCompteBancaireCCP;

public interface SimpleCompteBancaireCCPService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleCompteBancaireCCP
     * 
     * @param simpleCompteBancaireCCP
     *            L'entité simpleCompteBancaireCCP à créer
     * @return L'entité simpleCompteBancaireCCP créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CompteBancaireCCPException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleCompteBancaireCCP create(SimpleCompteBancaireCCP simpleCompteBancaireCCP)
            throws JadePersistenceException, CompteBancaireCCPException;

    /**
     * Permet la suppression d'une entité SimpleCompteBancaireCCP
     * 
     * @param SimpleCompteBancaireCCP
     *            L'entité SimpleCompteBancaireCCP à supprimer
     * @return L'entité SimpleCompteBancaireCCP supprimé
     * @throws SimpleCompteBancaireCCPException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleCompteBancaireCCP delete(SimpleCompteBancaireCCP simpleCompteBancaireCCP)
            throws CompteBancaireCCPException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité SimpleCompteBancaireCCP
     * 
     * @param idCompteBancaireCCP
     *            L'identifiant de l'entité SimpleCompteBancaireCCP à charger en mémoire
     * @return L'entité SimpleCompteBancaireCCP chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CompteBancaireCCPException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleCompteBancaireCCP read(String idCompteBancaireCCP) throws JadePersistenceException,
            CompteBancaireCCPException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleCompteBancaireCCP
     * 
     * @param SimpleCompteBancaireCCP
     *            L'entité SimpleCompteBancaireCCP à mettre à jour
     * @return L'entité SimpleCompteBancaireCCP mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CompteBancaireCCPException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleCompteBancaireCCP update(SimpleCompteBancaireCCP simpleCompteBancaireCCP)
            throws JadePersistenceException, CompteBancaireCCPException;

}