/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.NumeraireException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleNumeraire;

/**
 * @author BSC
 * 
 */
public interface SimpleNumeraireService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� simpleNumeraire
     * 
     * @param simpleNumeraire
     *            L'entit� simpleNumeraire � cr�er
     * @return L'entit� simpleNumeraire cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws NumeraireException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleNumeraire create(SimpleNumeraire simpleNumeraire) throws JadePersistenceException, NumeraireException;

    /**
     * Permet la suppression d'une entit� simpleNumeraire
     * 
     * @param simpleNumeraire
     *            L'entit� simpleNumeraire � supprimer
     * @return L'entit� simpleNumeraire supprim�
     * @throws NumeraireException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleNumeraire delete(SimpleNumeraire simpleNumeraire) throws NumeraireException, JadePersistenceException;

    /**
     * Permet l'effacement de simpleNumeraire en donnant une liste des id de leurs headerDonneeFinanciere
     * 
     * @param listeIDString
     *            la liste des id des headerDonneeFinanciere pour lesquels il faut effacer les simpleNumeraire
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� simpleNumeraire
     * 
     * @param idNumeraire
     *            L'identifiant de l'entit� simpleNumeraire � charger en m�moire
     * @return L'entit� simpleNumeraire charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws NumeraireException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleNumeraire read(String idNumeraire) throws JadePersistenceException, NumeraireException;

    /**
     * 
     * Permet la mise � jour d'une entit� simpleNumeraire
     * 
     * @param simpleNumeraire
     *            L'entit� simpleNumeraire � mettre � jour
     * @return L'entit� simpleNumeraire mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws NumeraireException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleNumeraire update(SimpleNumeraire simpleNumeraire) throws JadePersistenceException, NumeraireException;
}
