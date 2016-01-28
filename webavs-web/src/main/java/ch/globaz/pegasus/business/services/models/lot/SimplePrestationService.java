package ch.globaz.pegasus.business.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.lot.SimplePrestation;
import ch.globaz.pegasus.business.models.lot.SimplePrestationSearch;

public interface SimplePrestationService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param SimplePrestationSearch
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws PrestationException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimplePrestationSearch search) throws PrestationException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� Prestation
     * 
     * @param SimplePrestation
     *            La simplePresation � cr�er
     * @return simplePresation cr��
     * @throws PrestationException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws JadeApplicationServiceNotAvailableException
     */
    public SimplePrestation create(SimplePrestation simplePrestation) throws PrestationException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entit� simplePresation
     * 
     * @param SimplePrestation
     *            La simplePresation � supprimer
     * @return supprim�
     * @throws PrestationException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePrestation delete(SimplePrestation simplePrestation) throws PrestationException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire une simplePresation PC
     * 
     * @param idSimplePrestation
     *            L'identifiant de simplePresation � charger en m�moire
     * @return simplePresation charg�e en m�moire
     * @throws PrestationException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePrestation read(String idPrestation) throws PrestationException, JadePersistenceException;

    /**
     * Permet de chercher des Prestation selon un mod�le de crit�res.
     * 
     * @param simplePresationSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws PrestationException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePrestationSearch search(SimplePrestationSearch simplePresationSearch) throws PrestationException,
            JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� Prestation
     * 
     * @param SimplePrestation
     *            Le modele � mettre � jour
     * @return simplePresation mis � jour
     * @throws PrestationException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePrestation update(SimplePrestation simplePrestation) throws PrestationException,
            JadePersistenceException;

    public abstract int delete(SimplePrestationSearch simplePrestationSearch) throws PrestationException,
            JadePersistenceException;

    ArrayList<String> getIdsPrestationsByLot(String idLot) throws PrestationException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

}