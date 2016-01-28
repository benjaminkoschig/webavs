package ch.globaz.pegasus.business.services.models.droit;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneesPersonnellesException;
import ch.globaz.pegasus.business.models.droit.SimpleDonneesPersonnelles;
import ch.globaz.pegasus.business.models.droit.SimpleDonneesPersonnellesSearch;

public interface SimpleDonneesPersonnellesService extends JadeApplicationService {

    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws DonneesPersonnellesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleDonneesPersonnellesSearch search) throws DonneesPersonnellesException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� donneesPersonnelles.
     * 
     * @param donneesPersonnelles
     *            L'entit� donneesPersonnelles � cr�er
     * @return L'entit� donneesPersonnelles cr��
     * @throws DonneesPersonnellesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDonneesPersonnelles create(SimpleDonneesPersonnelles donneesPersonnelles)
            throws DonneesPersonnellesException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� donneesPersonnelles
     * 
     * @param donneesPersonnelles
     *            L'entit� donneesPersonnelles � supprimer
     * @return L'entit� donneesPersonnelles supprim�
     * @throws DonneesPersonnellesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDonneesPersonnelles delete(SimpleDonneesPersonnelles donneesPersonnelles)
            throws DonneesPersonnellesException, JadePersistenceException;

    /**
     * Permet de charger en m�moire une entite donneesPersonnelles
     * 
     * @param idDonneesPersonnelles
     *            L'identifiant du donneesPersonnelles � charger en m�moire
     * @throws DonneesPersonnellesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDonneesPersonnelles read(String idDonneesPersonnelles) throws DonneesPersonnellesException,
            JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� donneesPersonnelles
     * 
     * @param donneesPersonnelles
     *            L'entit� donneesPersonnelles � mettre � jour
     * @return L'entit� donneesPersonnelles mis � jour
     * @throws DonneesPersonnellesException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDonneesPersonnelles update(SimpleDonneesPersonnelles donneesPersonnelles)
            throws DonneesPersonnellesException, JadePersistenceException;

}
