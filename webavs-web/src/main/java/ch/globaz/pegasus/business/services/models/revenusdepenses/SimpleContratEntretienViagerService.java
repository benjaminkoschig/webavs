package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AutresRevenusException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.ContratEntretienViagerException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleContratEntretienViager;

public interface SimpleContratEntretienViagerService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� SimpleContratEntretienViager
     * 
     * @param SimpleContratEntretienViager
     *            L'entit� SimpleAutresRevenus � cr�er
     * @return L'entit� SimpleContratEntretienViager cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ContratEntretienViagerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleContratEntretienViager create(SimpleContratEntretienViager simpleContratEntretienViager)
            throws JadePersistenceException, ContratEntretienViagerException;

    /**
     * Permet la suppression d'une entit� SimpleContratEntretienViager
     * 
     * @param SimpleContratEntretienViager
     *            L'entit� SimpleAutresRevenus � supprimer
     * @return L'entit� SimpleContratEntretienViager supprim�
     * @throws ContratEntretienViagerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleContratEntretienViager delete(SimpleContratEntretienViager simpleContratEntretienViager)
            throws ContratEntretienViagerException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� SimpleContratEntretienViager
     * 
     * @param idContratEntretienViager
     *            L'identifiant de l'entit� SimpleContratEntretienViager � charger en m�moire
     * @return L'entit� SimpleContratEntretienViager charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ContratEntretienViagerException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleContratEntretienViager read(String idContratEntretienViager) throws JadePersistenceException,
            ContratEntretienViagerException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleContratEntretienViager
     * 
     * @param SimpleContratEntretienViager
     *            L'entit� SimpleContratEntretienViager � mettre � jour
     * @return L'entit� SimpleContratEntretienViager mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AutresRevenusException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleContratEntretienViager update(SimpleContratEntretienViager simpleContratEntretienViager)
            throws JadePersistenceException, ContratEntretienViagerException;

}
