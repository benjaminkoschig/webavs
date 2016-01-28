/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.VehiculeException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleVehicule;

/**
 * @author BSC
 * 
 */
public interface SimpleVehiculeService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� simpleVehicule
     * 
     * @param simpleVehicule
     *            L'entit� simpleVehicule � cr�er
     * @return L'entit� simpleVehicule cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws VehiculeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleVehicule create(SimpleVehicule simpleVehicule) throws JadePersistenceException, VehiculeException;

    /**
     * Permet la suppression d'une entit� simpleVehicule
     * 
     * @param simpleVehicule
     *            L'entit� simpleVehicule � supprimer
     * @return L'entit� simpleVehicule supprim�
     * @throws VehiculeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleVehicule delete(SimpleVehicule simpleVehicule) throws VehiculeException, JadePersistenceException;

    /**
     * Permet l'effacement de simpleVehicule en donnant une liste des id de leurs headerDonneeFinanciere
     * 
     * @param listeIDString
     *            la liste des id des headerDonneeFinanciere pour lesquels il faut effacer les simpleVehicule
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� simpleVehicule
     * 
     * @param idVehicule
     *            L'identifiant de l'entit� simpleVehicule � charger en m�moire
     * @return L'entit� simpleVehicule charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws VehiculeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleVehicule read(String idVehicule) throws JadePersistenceException, VehiculeException;

    /**
     * 
     * Permet la mise � jour d'une entit� simpleVehicule
     * 
     * @param simpleVehicule
     *            L'entit� simpleVehicule � mettre � jour
     * @return L'entit� simpleVehicule mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws VehiculeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleVehicule update(SimpleVehicule simpleVehicule) throws JadePersistenceException, VehiculeException;
}
