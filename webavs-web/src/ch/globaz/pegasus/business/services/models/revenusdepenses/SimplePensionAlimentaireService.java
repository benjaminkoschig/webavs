package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.PensionAlimentaireException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimplePensionAlimentaire;

public interface SimplePensionAlimentaireService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� SimplePensionAlimentaire
     * 
     * @param SimplePensionAlimentaire
     *            L'entit� SimplePensionAlimentaire � cr�er
     * @return L'entit� SimplePensionAlimentaire cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PensionAlimentaireException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimplePensionAlimentaire create(SimplePensionAlimentaire simplePensionAlimentaire)
            throws JadePersistenceException, PensionAlimentaireException;

    /**
     * Permet la suppression d'une entit� SimplePensionAlimentaire
     * 
     * @param SimplePensionAlimentaire
     *            L'entit� SimplePensionAlimentaire � supprimer
     * @return L'entit� SimplePensionAlimentaire supprim�
     * @throws CotisationsPsalException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws PensionAlimentaireException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePensionAlimentaire delete(SimplePensionAlimentaire simplePensionAlimentaire)
            throws PensionAlimentaireException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� SimplePensionAlimentaire
     * 
     * @param idPensionAlimentaire
     *            L'identifiant de l'entit� SimplePensionAlimentaire � charger en m�moire
     * @return L'entit� SimplePensionAlimentaire charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PensionAlimentaireException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimplePensionAlimentaire read(String idPensionAlimentaire) throws JadePersistenceException,
            PensionAlimentaireException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimplePensionAlimentaire
     * 
     * @param SimplePensionAlimentaire
     *            L'entit� SimplePensionAlimentaire � mettre � jour
     * @return L'entit� SimplePensionAlimentaire mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PensionAlimentaireException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimplePensionAlimentaire update(SimplePensionAlimentaire simplePensionAlimentaire)
            throws JadePersistenceException, PensionAlimentaireException;

}
