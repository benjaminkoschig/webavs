package ch.globaz.pegasus.business.services.models.home;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.home.PrixChambreException;
import ch.globaz.pegasus.business.models.home.SimplePrixChambre;
import ch.globaz.pegasus.business.models.home.SimplePrixChambreSearch;

public interface SimplePrixChambreService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimplePrixChambreSearch search) throws PrixChambreException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� prixChambre
     * 
     * @param prixChambre
     *            Le prixChambre � cr�er
     * @return Le prixChambre cr��
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePrixChambre create(SimplePrixChambre prixChambre) throws PrixChambreException,
            JadePersistenceException;

    /**
     * Permet la suppression d'une entit� prixChambre
     * 
     * @param prixChambre
     *            Le prisChambre � supprimer
     * @return Le prixChambre supprim�
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePrixChambre delete(SimplePrixChambre prixChambre) throws PrixChambreException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire un prixChambre
     * 
     * @param idPrixChambre
     *            L'identifiant du prixChambre � charger en m�moire
     * @return Le prixChambre charg� en m�moire
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePrixChambre read(String idPrixChambre) throws PrixChambreException, JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� prixChambre
     * 
     * @param prixChambre
     *            Le prixChambre � mettre � jour
     * @return Le prixChambre mis � jour
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimplePrixChambre update(SimplePrixChambre prixChambre) throws PrixChambreException,
            JadePersistenceException;
}
