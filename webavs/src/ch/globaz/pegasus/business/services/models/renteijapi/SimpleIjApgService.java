package ch.globaz.pegasus.business.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.IjApgException;
import ch.globaz.pegasus.business.models.renteijapi.SimpleIjApg;

public interface SimpleIjApgService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� autreRente
     * 
     * @param SimpleIjApg
     *            SimpleIjApg � cr�er
     * @return SimpleIjApg cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws IjApgException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleIjApg create(SimpleIjApg simpleIjApg) throws IjApgException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� autreRente
     * 
     * @param SimpleIjApg
     *            SimpleIjApg � supprimer
     * @return simpleIjApg supprim�
     * @throws IjApgException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleIjApg delete(SimpleIjApg simpleIjApg) throws IjApgException, JadePersistenceException;

    /**
     * Permet la suppression r�ele de la donn�e financiere
     * 
     * @param listeIDString
     * @throws JadePersistenceException
     */
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException;

    /**
     * Permet de charger en m�moire une autreRente PC
     * 
     * @param idSimpleIjApg
     *            L'identifiant de la simpleIjApg � charger en m�moire
     * @return simpleIjApg charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws IjApgException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleIjApg read(String idSimpleIjApg) throws IjApgException, JadePersistenceException;

    /**
     * 
     * Permet la mise � jour d'une entit� variableMetier
     * 
     * @param SimpleIjApg
     *            SimpleIjApg � mettre � jour
     * @return simpleAutreRente mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws IjApgException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleIjApg update(SimpleIjApg simpleIjApg) throws IjApgException, JadePersistenceException;
}
