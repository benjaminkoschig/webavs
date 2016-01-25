/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.AssuranceRenteViagereException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleAssuranceRenteViagere;

/**
 * @author BSC
 * 
 */
public interface SimpleAssuranceRenteViagereService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� simpleAssuranceRenteViagere
     * 
     * @param simpleAssuranceRenteViagere
     *            L'entit� simpleAssuranceRenteViagere � cr�er
     * @return L'entit� simpleAssuranceRenteViagere cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAssuranceRenteViagere create(SimpleAssuranceRenteViagere simpleAssuranceRenteViagere)
            throws JadePersistenceException, AssuranceRenteViagereException;

    /**
     * Permet la suppression d'une entit� simpleAssuranceRenteViagere
     * 
     * @param simpleAssuranceRenteViagere
     *            L'entit� simpleAssuranceRenteViagere � supprimer
     * @return L'entit� simpleAssuranceRenteViagere supprim�
     * @throws AssuranceRenteViagereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleAssuranceRenteViagere delete(SimpleAssuranceRenteViagere simpleAssuranceRenteViagere)
            throws AssuranceRenteViagereException, JadePersistenceException;

    /**
     * Permet l'effacement de simpleAssuranceRenteViagere en donnant une liste des id de leurs headerDonneeFinanciere
     * 
     * @param listeIDString
     *            la liste des id des headerDonneeFinanciere pour lesquels il faut effacer les
     *            simpleAssuranceREnteViagere
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� simpleAssuranceRenteViagere
     * 
     * @param idAssuranceRenteViagere
     *            L'identifiant de l'entit� simpleAssuranceRenteViagere � charger en m�moire
     * @return L'entit� simpleAssuranceRenteViagere charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAssuranceRenteViagere read(String idAssuranceRenteViagere) throws JadePersistenceException,
            AssuranceRenteViagereException;

    /**
     * 
     * Permet la mise � jour d'une entit� simpleAssuranceRenteViagere
     * 
     * @param simpleAssuranceRenteViagere
     *            L'entit� simpleAssuranceRenteViagere � mettre � jour
     * @return L'entit� simpleAssuranceRenteViagere mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAssuranceRenteViagere update(SimpleAssuranceRenteViagere simpleAssuranceRenteViagere)
            throws JadePersistenceException, AssuranceRenteViagereException;
}
