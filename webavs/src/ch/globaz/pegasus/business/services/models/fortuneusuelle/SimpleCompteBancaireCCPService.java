package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.CompteBancaireCCPException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleCompteBancaireCCP;

public interface SimpleCompteBancaireCCPService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� SimpleCompteBancaireCCP
     * 
     * @param simpleCompteBancaireCCP
     *            L'entit� simpleCompteBancaireCCP � cr�er
     * @return L'entit� simpleCompteBancaireCCP cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CompteBancaireCCPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleCompteBancaireCCP create(SimpleCompteBancaireCCP simpleCompteBancaireCCP)
            throws JadePersistenceException, CompteBancaireCCPException;

    /**
     * Permet la suppression d'une entit� SimpleCompteBancaireCCP
     * 
     * @param SimpleCompteBancaireCCP
     *            L'entit� SimpleCompteBancaireCCP � supprimer
     * @return L'entit� SimpleCompteBancaireCCP supprim�
     * @throws SimpleCompteBancaireCCPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleCompteBancaireCCP delete(SimpleCompteBancaireCCP simpleCompteBancaireCCP)
            throws CompteBancaireCCPException, JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� SimpleCompteBancaireCCP
     * 
     * @param idCompteBancaireCCP
     *            L'identifiant de l'entit� SimpleCompteBancaireCCP � charger en m�moire
     * @return L'entit� SimpleCompteBancaireCCP charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CompteBancaireCCPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleCompteBancaireCCP read(String idCompteBancaireCCP) throws JadePersistenceException,
            CompteBancaireCCPException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleCompteBancaireCCP
     * 
     * @param SimpleCompteBancaireCCP
     *            L'entit� SimpleCompteBancaireCCP � mettre � jour
     * @return L'entit� SimpleCompteBancaireCCP mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws CompteBancaireCCPException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleCompteBancaireCCP update(SimpleCompteBancaireCCP simpleCompteBancaireCCP)
            throws JadePersistenceException, CompteBancaireCCPException;

}