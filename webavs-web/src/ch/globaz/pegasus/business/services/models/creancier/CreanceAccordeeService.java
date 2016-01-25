package ch.globaz.pegasus.business.services.models.creancier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordee;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordeeSearch;
import ch.globaz.pegasus.business.models.creancier.SimpleCreanceAccordeeSearch;

public interface CreanceAccordeeService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(CreanceAccordeeSearch search) throws CreancierException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� CreanceAccordee
     * 
     * @param CreanceAccordee
     *            La creanceAccordee � cr�er
     * @return creanceAccordee cr��
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public CreanceAccordee create(CreanceAccordee creanceAccordee) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entit� creanceAccordee
     * 
     * @param CreanceAccordee
     *            La creanceAccordee � supprimer
     * @return supprim�
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public CreanceAccordee delete(CreanceAccordee creanceAccordee) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;

    public int deleteWithSearchModele(SimpleCreanceAccordeeSearch simpleCreanceAccordeeSearch)
            throws CreancierException, JadePersistenceException;

    /**
     * Retourne le montant rembourse, pour un creancier, pour une demande Effectue l'addition des crenaces vers� pour la
     * demande(toute les versions de droits)
     * 
     * @param idDemande
     *            , l'id de la demande
     * @param idCreancier
     *            , l'id du creancier
     * @return montantRebourse
     * @throws JadePersistenceException
     */
    public BigDecimal findTotalCreanceVerseByDemandeForCreancier(String idDemande, String idCreancier)
            throws JadePersistenceException, CreancierException;

    /**
     * Retourne le montant rembourse, pour un creancier, pour une demande Effectue l'addition des crenaces vers� pour la
     * demande(toute les versions de droits)
     * 
     * @param idDemande
     *            , l'id de la demande
     * @param idCreancier
     *            , l'id du creancier
     * @return montantRebourse
     * @throws JadePersistenceException
     */
    public BigDecimal findTotalCreanceVerseByVersionDroitForCreancier(String idDemande, String idVersioNDroit,
            String idCreancier) throws JadePersistenceException, CreancierException;

    /**
     * Permet de charger en m�moire une creanceAccordee PC
     * 
     * @param idcreanceAccordee
     *            L'identifiant de creanceAccordee � charger en m�moire
     * @return creanceAccordee charg�e en m�moire
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public CreanceAccordee read(String idCreanceAccordee) throws CreancierException, JadePersistenceException;

    /**
     * Permet de chercher des CreanceAccordee selon un mod�le de crit�res.
     * 
     * @param creanceAccordeeSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public CreanceAccordeeSearch search(CreanceAccordeeSearch creanceAccordeeSearch) throws CreancierException,
            JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� CreanceAccordee
     * 
     * @param CreanceAccordee
     *            Le modele � mettre � jour
     * @return creanceAccordee mis � jour
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public CreanceAccordee update(CreanceAccordee creanceAccordee) throws CreancierException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException;
}
