package ch.globaz.pegasus.business.services.models.pcaccordee;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.PersonneDansPlanCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.PersonneDansPlanCalculSearch;

public interface PersonneDansPlanCalculService extends JadeApplicationService {
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
    public int count(PersonneDansPlanCalculSearch search) throws PCAccordeeException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� EnfantDansCalcul
     * 
     * @param enfantDansCalcul
     *            L'enfantDansCalcul � cr�er
     * @return L'enfantDansCalcul cr��
     * @throws PCAccordeeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public PersonneDansPlanCalcul create(PersonneDansPlanCalcul enfantDansCalcul) throws PCAccordeeException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entit� enfantDansCalcul
     * 
     * @param enfantDansCalcul
     *            L'enfantDansCalcul � supprimer
     * @return L'enfantDansCalcul supprim�
     * @throws PCAccordeeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public PersonneDansPlanCalcul delete(PersonneDansPlanCalcul enfantDansCalcul) throws PCAccordeeException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire un enfantDansCalcul
     * 
     * @param idEnfantDansCalcul
     *            L'identifiant de l'enfantDansCalcul � charger en m�moire
     * @return L'enfantDansCalcul charg� en m�moire
     * @throws PCAccordeeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public PersonneDansPlanCalcul read(String idEnfantDansCalcul) throws PCAccordeeException, JadePersistenceException;

    /**
     * Permet de chercher des enfantDansCalcul selon un mod�le de crit�res.
     * 
     * @param enfantDansCalculSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PCAccordeeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PersonneDansPlanCalculSearch search(PersonneDansPlanCalculSearch enfantDansCalculSearch)
            throws JadePersistenceException, PCAccordeeException;

    /**
     * Permet la mise � jour d'une entit� enfantDansCalcul
     * 
     * @param enfantDansCalcul
     *            L'enfantDansCalcul � mettre � jour
     * @return L'enfantDansCalcul mis � jour
     * @throws PCAccordeeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public PersonneDansPlanCalcul update(PersonneDansPlanCalcul enfantDansCalcul) throws PCAccordeeException,
            JadePersistenceException;

}
