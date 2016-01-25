package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.PensionAlimentaireException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimplePensionAlimentaire;

public interface SimplePensionAlimentaireService extends JadeApplicationService {

    /**
     * Permet la création d'une entité SimplePensionAlimentaire
     * 
     * @param SimplePensionAlimentaire
     *            L'entité SimplePensionAlimentaire à créer
     * @return L'entité SimplePensionAlimentaire créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PensionAlimentaireException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimplePensionAlimentaire create(SimplePensionAlimentaire simplePensionAlimentaire)
            throws JadePersistenceException, PensionAlimentaireException;

    /**
     * Permet la suppression d'une entité SimplePensionAlimentaire
     * 
     * @param SimplePensionAlimentaire
     *            L'entité SimplePensionAlimentaire à supprimer
     * @return L'entité SimplePensionAlimentaire supprimé
     * @throws CotisationsPsalException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws PensionAlimentaireException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimplePensionAlimentaire delete(SimplePensionAlimentaire simplePensionAlimentaire)
            throws PensionAlimentaireException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité SimplePensionAlimentaire
     * 
     * @param idPensionAlimentaire
     *            L'identifiant de l'entité SimplePensionAlimentaire à charger en mémoire
     * @return L'entité SimplePensionAlimentaire chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PensionAlimentaireException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimplePensionAlimentaire read(String idPensionAlimentaire) throws JadePersistenceException,
            PensionAlimentaireException;

    /**
     * 
     * Permet la mise à jour d'une entité SimplePensionAlimentaire
     * 
     * @param SimplePensionAlimentaire
     *            L'entité SimplePensionAlimentaire à mettre à jour
     * @return L'entité SimplePensionAlimentaire mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PensionAlimentaireException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimplePensionAlimentaire update(SimplePensionAlimentaire simplePensionAlimentaire)
            throws JadePersistenceException, PensionAlimentaireException;

}
