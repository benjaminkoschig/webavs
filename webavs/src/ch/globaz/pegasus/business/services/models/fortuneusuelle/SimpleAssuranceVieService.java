package ch.globaz.pegasus.business.services.models.fortuneusuelle;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.AssuranceVieException;
import ch.globaz.pegasus.business.models.fortuneusuelle.SimpleAssuranceVie;

public interface SimpleAssuranceVieService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� SimpleAssuranceVie
     * 
     * @param simpleAssuranceVie
     *            L'entit� simpleAssuranceVie � cr�er
     * @return L'entit� simpleAssuranceVie cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AssuranceVieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAssuranceVie create(SimpleAssuranceVie simpleAssuranceVie) throws JadePersistenceException,
            AssuranceVieException;

    /**
     * Permet la suppression d'une entit� SimpleAssuranceVie
     * 
     * @param SimpleAssuranceVie
     *            L'entit� SimpleAssuranceVie � supprimer
     * @return L'entit� SimpleAssuranceVie supprim�
     * @throws AssuranceVieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleAssuranceVie delete(SimpleAssuranceVie simpleAssuranceVie) throws AssuranceVieException,
            JadePersistenceException;

    public void deleteParListeIdDoFinH(List<String> idsDonneFinanciere) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire d'une entit� SimpleAssuranceVie
     * 
     * @param idAssuranceVie
     *            L'identifiant de l'entit� SimpleAssuranceVie � charger en m�moire
     * @return L'entit� SimpleAssuranceVie charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AssuranceVieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAssuranceVie read(String idAssuranceVie) throws JadePersistenceException, AssuranceVieException;

    /**
     * 
     * Permet la mise � jour d'une entit� SimpleAssuranceVie
     * 
     * @param SimpleAssuranceVie
     *            L'entit� SimpleAssuranceVie � mettre � jour
     * @return L'entit� SimpleAssuranceVie mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AssuranceVieException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleAssuranceVie update(SimpleAssuranceVie simpleAssuranceVie) throws JadePersistenceException,
            AssuranceVieException;

}
