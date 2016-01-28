package ch.globaz.pegasus.business.services.models.pcaccordee;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppointSearch;

public interface SimpleJoursAppointService extends JadeApplicationService {

    /**
     * Permet la cr�ation d'une entit� simpleJoursAppoint
     * 
     * @param simpleJoursAppoint
     *            Le simpleJoursAppoint � cr�er
     * @return Le simpleJoursAppoint cr��
     * @throws PCAccordeeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleJoursAppoint create(SimpleJoursAppoint simpleJoursAppoint) throws PCAccordeeException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entit� simpleJoursAppoint
     * 
     * @param simpleJoursAppoint
     *            Le simpleJoursAppoint � supprimer
     * @return Le simpleJoursAppoint supprim�
     * @throws PCAccordeeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleJoursAppoint delete(SimpleJoursAppoint simpleJoursAppoint) throws PCAccordeeException,
            JadePersistenceException;

    public abstract int delete(SimpleJoursAppointSearch simpleJoursAppointSearch) throws PCAccordeeException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire un simpleJoursAppoint
     * 
     * @param idSimpleJoursAppoint
     *            L'identifiant du simpleJoursAppoint � charger en m�moire
     * @return Le simpleJoursAppoint charg� en m�moire
     * @throws PCAccordeeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleJoursAppoint read(String idSimpleJoursAppoint) throws PCAccordeeException, JadePersistenceException;

    /**
     * Permet de chercher des simpleJoursAppoint selon un mod�le de crit�res.
     * 
     * @param simpleJoursAppointSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PCAccordeeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleJoursAppointSearch search(SimpleJoursAppointSearch simpleJoursAppointSearch)
            throws JadePersistenceException, PCAccordeeException;

    /**
     * Permet la mise � jour d'une entit� simpleJoursAppoint
     * 
     * @param simpleJoursAppoint
     *            Le simpleJoursAppoint � mettre � jour
     * @return Le simpleJoursAppoint mis � jour
     * @throws PCAccordeeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleJoursAppoint update(SimpleJoursAppoint simpleJoursAppoint) throws PCAccordeeException,
            JadePersistenceException;
}
