/**
 * 
 */
package ch.globaz.pegasus.business.services.models.demande;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.demande.SimpleDemandeSearch;

/**
 * @author ECO
 * 
 */
public interface SimpleDemandeService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws DemandeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleDemandeSearch search) throws DemandeException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� demande
     * 
     * @param demande
     *            La demande PC � cr�er
     * @return La demande PC cr��
     * @throws DemandeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DossierException
     */
    public SimpleDemande create(SimpleDemande demande) throws DemandeException, JadePersistenceException,
            DossierException;

    /**
     * Permet la suppression d'une entit� demande PC
     * 
     * @param demande
     *            La demande PC � supprimer
     * @return La demande PC supprim�
     * @throws DemandeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDemande delete(SimpleDemande demande) throws DemandeException, JadePersistenceException;

    /**
     * Recherche si une demande initial (im�diatement contigue existe pour ce dossier)
     * 
     * @param simpleDemande
     * @return
     * @throws DemandeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public boolean isDemandeInitial(SimpleDemande simpleDemande, String dateDemandeToCheck) throws DemandeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet de charger en m�moire une demande
     * 
     * @param idDemande
     *            L'identifiant de la demande � charger en m�moire
     * @return La demande charg� en m�moire
     * @throws DemandeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDemande read(String idDemande) throws DemandeException, JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� demande
     * 
     * @param demande
     *            La demande PC � mettre � jour
     * @return La demande PC mis � jour
     * @throws DemandeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DossierException
     */
    public SimpleDemande update(SimpleDemande demande) throws DemandeException, JadePersistenceException,
            DossierException;

}
