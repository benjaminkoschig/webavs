package ch.globaz.pegasus.business.services.models.home;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.models.home.TypeChambre;
import ch.globaz.pegasus.business.models.home.TypeChambreSearch;

public interface TypeChambreService extends JadeApplicationService {

    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws TypeChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(TypeChambreSearch search) throws TypeChambreException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� typeChambre
     * 
     * @param typeChambre
     *            Le typeChambre � cr�er
     * @return Le typeChambre cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TypeChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public TypeChambre create(TypeChambre typeChambre) throws JadePersistenceException, TypeChambreException;

    /**
     * Permet la suppression d'une entit� typeChambre
     * 
     * @param typeChambre
     *            Le typeChambre � supprimer
     * @return Le typeChambre supprim�
     * @throws TypeChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public TypeChambre delete(TypeChambre typeChambre) throws TypeChambreException, JadePersistenceException;

    /**
     * Permet de charger en m�moire un typeChambre
     * 
     * @param idTypeChambre
     *            L'identifiant du typeChambre � charger en m�moire
     * @return Le typeChambre charg� en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TypeChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public TypeChambre read(String idTypeChambre) throws JadePersistenceException, TypeChambreException;

    /**
     * Permet de chercher des typeChambre selon un mod�le de crit�res.
     * 
     * @param typeChambreSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TypeChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public TypeChambreSearch search(TypeChambreSearch typeChambreSearch) throws JadePersistenceException,
            TypeChambreException;

    /**
     * 
     * Permet la mise � jour d'une entit� typeChambre
     * 
     * @param typeChambre
     *            Le typeChambre � mettre � jour
     * @return Le typeChambre mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws TypeChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public TypeChambre update(TypeChambre typeChambre) throws JadePersistenceException, TypeChambreException;
}
