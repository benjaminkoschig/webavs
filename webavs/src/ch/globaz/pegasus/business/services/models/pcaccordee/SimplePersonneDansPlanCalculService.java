package ch.globaz.pegasus.business.services.models.pcaccordee;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PersonneDansPlanCalculException;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePersonneDansPlanCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePersonneDansPlanCalculSearch;

public interface SimplePersonneDansPlanCalculService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws PersonneDansPlanCalculException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PCAccordeeException
     */
    public int count(SimplePersonneDansPlanCalculSearch search) throws JadePersistenceException, PCAccordeeException;

    /**
     * Permet la cr�ation d'une entit� simpleEnfantDansCalcul
     * 
     * @param simplePersonneDansPlanCalcul
     *            Le simpleEnfantDansCalcul � cr�er
     * @return Le simpleEnfantDansCalcul cr��
     * @throws PersonneDansPlanCalculException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PCAccordeeException
     */
    public SimplePersonneDansPlanCalcul create(SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul)
            throws JadePersistenceException, PCAccordeeException;

    /**
     * Permet la suppression d'une entit� simpleEnfantDansCalcul
     * 
     * @param simplePersonneDansPlanCalcul
     *            Le simpleEnfantDansCalcul � supprimer
     * @return Le simpleEnfantDansCalcul supprim�
     * @throws PersonneDansPlanCalculException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PCAccordeeException
     */
    public SimplePersonneDansPlanCalcul delete(SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul)
            throws JadePersistenceException, PCAccordeeException;

    public void delete(SimplePersonneDansPlanCalculSearch personneSearch) throws JadePersistenceException,
            PCAccordeeException;

    /**
     * Permet de charger en m�moire un simpleEnfantDansCalcul
     * 
     * @param idSimplePersonneDansPlanCalcul
     *            L'identifiant du simpleEnfantDansCalcul � charger en m�moire
     * @return Le simpleEnfantDansCalcul charg� en m�moire
     * @throws PersonneDansPlanCalculException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PCAccordeeException
     */
    public SimplePersonneDansPlanCalcul read(String idSimplePersonneDansPlanCalcul) throws JadePersistenceException,
            PCAccordeeException;

    /**
     * Permet de chercher des simpleEnfantDansCalcul selon un mod�le de crit�res.
     * 
     * @param simplePersonneDansPlanCalculSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PersonneDansPlanCalculException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws PCAccordeeException
     */
    public SimplePersonneDansPlanCalculSearch search(
            SimplePersonneDansPlanCalculSearch simplePersonneDansPlanCalculSearch) throws JadePersistenceException,
            PCAccordeeException;

    /**
     * Permet la mise � jour d'une entit� simpleEnfantDansCalcul
     * 
     * @param simplePersonneDansPlanCalcul
     *            Le simpleEnfantDansCalcul � mettre � jour
     * @return Le simpleEnfantDansCalcul mis � jour
     * @throws PersonneDansPlanCalculException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PCAccordeeException
     */
    public SimplePersonneDansPlanCalcul update(SimplePersonneDansPlanCalcul simplePersonneDansPlanCalcul)
            throws JadePersistenceException, PCAccordeeException;

}
