package ch.globaz.pegasus.business.services.models.pcaccordee;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordeeSearch;

public interface SimplePCAccordeeService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws PCAccordeeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimplePCAccordeeSearch search) throws PCAccordeeException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� simplePCAccordees
     * 
     * @param simplePCAccordees
     *            Le simplePCAccordees � cr�er
     * @return Le simplePCAccordees cr��
     * @throws PCAccordeeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePCAccordee create(SimplePCAccordee simplePCAccordees) throws PCAccordeeException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entit� simplePCAccordees
     * 
     * @param simplePCAccordees
     *            Le simplePCAccordees � supprimer
     * @return Le simplePCAccordees supprim�
     * @throws PCAccordeeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePCAccordee delete(SimplePCAccordee simplePCAccordees) throws PCAccordeeException,
            JadePersistenceException;

    /**
     * Permet la suppression de plusieurs simplePCAccordees
     * 
     * @param simplePCAccordees
     *            Le modele de recherche simplePCAccordees � supprimer
     * @return Le nombre d'elements supprim�s
     * @throws PCAccordeeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int delete(SimplePCAccordeeSearch simplePCAccordeesSearch) throws JadePersistenceException,
            PCAccordeeException;

    /**
     * Permet de charger en m�moire un simplePCAccordees
     * 
     * @param idSimplePCAccordee
     *            L'identifiant du simplePCAccordees � charger en m�moire
     * @return Le simplePCAccordees charg� en m�moire
     * @throws PCAccordeeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePCAccordee read(String idSimplePCAccordee) throws PCAccordeeException, JadePersistenceException;

    /**
     * Permet de chercher des simplePCAccordees selon un mod�le de crit�res.
     * 
     * @param simplePCAccordeesSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PCAccordeeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimplePCAccordeeSearch search(SimplePCAccordeeSearch simplePCAccordeesSearch)
            throws JadePersistenceException, PCAccordeeException;

    /**
     * Permet la mise � jour d'une entit� simplePCAccordees
     * 
     * @param simplePCAccordees
     *            Le simplePCAccordees � mettre � jour
     * @return Le simplePCAccordees mis � jour
     * @throws PCAccordeeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePCAccordee update(SimplePCAccordee simplePCAccordees) throws PCAccordeeException,
            JadePersistenceException;

}
