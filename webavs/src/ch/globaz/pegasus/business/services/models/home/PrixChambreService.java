package ch.globaz.pegasus.business.services.models.home;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.home.PrixChambreException;
import ch.globaz.pegasus.business.models.home.PrixChambre;
import ch.globaz.pegasus.business.models.home.PrixChambreSearch;

public interface PrixChambreService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(PrixChambreSearch search) throws PrixChambreException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� prixChambre
     * 
     * @param prixChambre
     *            Le prixChambre � cr�er
     * @return Le prixChambre cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PrixChambre create(PrixChambre prixChambre) throws JadePersistenceException, PrixChambreException;

    /**
     * Permet la suppression d'une entit� prixChambre
     * 
     * @param prixChambre
     *            Le prixChambre � supprimer
     * @return Le prixChambre supprim�
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public PrixChambre delete(PrixChambre prixChambre) throws PrixChambreException, JadePersistenceException;

    /**
     * Permet de charger en m�moire un prixChambre
     * 
     * @param idPrixChambre
     *            L'identifiant du prixChambre � charger en m�moire
     * @return Le prixChambre charg� en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PrixChambre read(String idPrixChambre) throws JadePersistenceException, PrixChambreException;

    /**
     * Permet de chercher des prixChambre selon un mod�le de crit�res.
     * 
     * @param prixChambreSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PrixChambreSearch search(PrixChambreSearch prixChambreSearch) throws JadePersistenceException,
            PrixChambreException;

    /**
     * 
     * Permet la mise � jour d'une entit� prixChambre
     * 
     * @param prixChambre
     *            Le prixChambre � mettre � jour
     * @return Le prixChambre mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrixChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public PrixChambre update(PrixChambre prixChambre) throws JadePersistenceException, PrixChambreException;

}
