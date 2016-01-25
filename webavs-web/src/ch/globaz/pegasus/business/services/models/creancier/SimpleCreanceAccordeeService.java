package ch.globaz.pegasus.business.services.models.creancier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.models.creancier.SimpleCreanceAccordee;
import ch.globaz.pegasus.business.models.creancier.SimpleCreanceAccordeeSearch;

public interface SimpleCreanceAccordeeService extends JadeApplicationService {
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
    public int count(SimpleCreanceAccordeeSearch search) throws CreancierException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� SimpleCreanceAccordee
     * 
     * @param SimpleCreanceAccordee
     *            La simpleCreanceAccordee � cr�er
     * @return simpleCreanceAccordee cr��
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleCreanceAccordee create(SimpleCreanceAccordee simpleCreanceAccordee) throws CreancierException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entit� simpleCreanceAccordee
     * 
     * @param SimpleCreanceAccordee
     *            La simpleCreanceAccordee � supprimer
     * @return supprim�
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleCreanceAccordee delete(SimpleCreanceAccordee simpleCreanceAccordee) throws CreancierException,
            JadePersistenceException;

    /**
     * 
     * Permet de supprimer des creances par une version de droit
     * 
     * @param SimpleCreanceAccordee
     *            Le modele � mettre � jour
     * @return simpleCreanceAccordee mis � jour
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public void deleteByIdVersionDroit(String idVersionDroit) throws CreancierException, JadePersistenceException;

    /**
     * Permet de charger en m�moire une simpleCreanceAccordee PC
     * 
     * @param idsimpleCreanceAccordee
     *            L'identifiant de simpleCreanceAccordee � charger en m�moire
     * @return simpleCreanceAccordee charg�e en m�moire
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleCreanceAccordee read(String idSimpleCreanceAccordee) throws CreancierException,
            JadePersistenceException;

    /**
     * Permet de chercher des SimpleCreanceAccordee selon un mod�le de crit�res.
     * 
     * @param simpleCreanceAccordeeSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleCreanceAccordeeSearch search(SimpleCreanceAccordeeSearch simpleCreanceAccordeeSearch)
            throws CreancierException, JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleCreanceAccordee
     * 
     * @param SimpleCreanceAccordee
     *            Le modele � mettre � jour
     * @return simpleCreanceAccordee mis � jour
     * @throws CreancierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleCreanceAccordee update(SimpleCreanceAccordee simpleCreanceAccordee) throws CreancierException,
            JadePersistenceException;
}