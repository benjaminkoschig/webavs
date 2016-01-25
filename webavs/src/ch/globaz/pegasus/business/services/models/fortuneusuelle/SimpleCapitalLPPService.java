package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.CapitalLPPException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleCapitalLPP;

public interface SimpleCapitalLPPService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimpleCapitalLPP
     * 
     * @param simpleAssuranceVie
     *            L'entité simpleAssuranceVie à créer
     * @return L'entité simpleCapitalLPP créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CapitalLPPException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleCapitalLPP create(SimpleCapitalLPP simpleCapitalLPP) throws JadePersistenceException,
            CapitalLPPException;

    /**
     * Permet la suppression d'une entité SimpleCapitalLPP
     * 
     * @param SimpleCapitalLPP
     *            L'entité SimpleCapitalLPP à supprimer
     * @return L'entité SimpleCapitalLPP supprimé
     * @throws SimpleCapitalLPPException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleCapitalLPP delete(SimpleCapitalLPP simpleCapitalLPP) throws CapitalLPPException,
            JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité SimpleCapitalLPP
     * 
     * @param idCapitalLPP
     *            L'identifiant de l'entité SimpleCapitalLPP à charger en mémoire
     * @return L'entité SimpleCapitalLPP chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CapitalLPPException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleCapitalLPP read(String idCapitalLPP) throws JadePersistenceException, CapitalLPPException;

    /**
     * 
     * Permet la mise à jour d'une entité SimpleCapitalLPP
     * 
     * @param SimpleCapitalLPP
     *            L'entité SimpleCapitalLPP à mettre à jour
     * @return L'entité SimpleCapitalLPP mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws CapitalLPPException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleCapitalLPP update(SimpleCapitalLPP simpleCapitalLPP) throws JadePersistenceException,
            CapitalLPPException;

}