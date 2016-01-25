package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.CapitalLPPException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleCapitalLPP;

public interface SimpleCapitalLPPService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� SimpleCapitalLPP
     * 
     * @param simpleAssuranceVie
     *            L'entit� simpleAssuranceVie � cr�er
     * @return L'entit� simpleCapitalLPP cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CapitalLPPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleCapitalLPP create(SimpleCapitalLPP simpleCapitalLPP) throws JadePersistenceException,
            CapitalLPPException;

    /**
     * Permet la suppression d'une entit� SimpleCapitalLPP
     * 
     * @param SimpleCapitalLPP
     *            L'entit� SimpleCapitalLPP � supprimer
     * @return L'entit� SimpleCapitalLPP supprim�
     * @throws SimpleCapitalLPPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleCapitalLPP delete(SimpleCapitalLPP simpleCapitalLPP) throws CapitalLPPException,
            JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� SimpleCapitalLPP
     * 
     * @param idCapitalLPP
     *            L'identifiant de l'entit� SimpleCapitalLPP � charger en m�moire
     * @return L'entit� SimpleCapitalLPP charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CapitalLPPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleCapitalLPP read(String idCapitalLPP) throws JadePersistenceException, CapitalLPPException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleCapitalLPP
     * 
     * @param SimpleCapitalLPP
     *            L'entit� SimpleCapitalLPP � mettre � jour
     * @return L'entit� SimpleCapitalLPP mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CapitalLPPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleCapitalLPP update(SimpleCapitalLPP simpleCapitalLPP) throws JadePersistenceException,
            CapitalLPPException;

}