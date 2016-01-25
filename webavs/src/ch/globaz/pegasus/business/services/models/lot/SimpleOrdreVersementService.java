package ch.globaz.pegasus.business.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersementSearch;

public interface SimpleOrdreVersementService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws OrdreVersementException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleOrdreVersementSearch search) throws OrdreVersementException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� SimpleOrdreVersement
     * 
     * @param SimpleOrdreVersement
     *            La simpleOrdreVersement � cr�er
     * @return simpleOrdreVersement cr��
     * @throws OrdreVersementException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleOrdreVersement create(SimpleOrdreVersement simpleOrdreVersement) throws OrdreVersementException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entit� simpleOrdreVersement
     * 
     * @param SimpleOrdreVersement
     *            La simpleOrdreVersement � supprimer
     * @return supprim�
     * @throws OrdreVersementException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleOrdreVersement delete(SimpleOrdreVersement simpleOrdreVersement) throws OrdreVersementException,
            JadePersistenceException;

    /**
     * Permet la suppression de plusieurs entit�s simpleOrdreVersement
     * 
     * @param SimpleOrdreVersementSearch
     *            Les crit�res des simpleOrdreVersements � supprimer
     * @return nombre d'�l�ments supprim�s
     * @throws OrdreVersementException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int delete(SimpleOrdreVersementSearch simpleOrdreVersementSearch) throws OrdreVersementException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire une simpleOrdreVersement PC
     * 
     * @param idsimpleOrdreVersement
     *            L'identifiant de simpleOrdreVersement � charger en m�moire
     * @return simpleOrdreVersement charg�e en m�moire
     * @throws OrdreVersementException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleOrdreVersement read(String idSimpleOrdreVersement) throws OrdreVersementException,
            JadePersistenceException;

    /**
     * Permet de chercher des SimpleOrdreVersement selon un mod�le de crit�res.
     * 
     * @param simpleOrdreVersementSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws OrdreVersementException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleOrdreVersementSearch search(SimpleOrdreVersementSearch simpleOrdreVersementSearch)
            throws OrdreVersementException, JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleOrdreVersement
     * 
     * @param SimpleOrdreVersement
     *            Le modele � mettre � jour
     * @return simpleOrdreVersement mis � jour
     * @throws OrdreVersementException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleOrdreVersement update(SimpleOrdreVersement simpleOrdreVersement) throws OrdreVersementException,
            JadePersistenceException;

}