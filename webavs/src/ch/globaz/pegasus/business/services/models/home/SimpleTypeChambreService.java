package ch.globaz.pegasus.business.services.models.home;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.home.TypeChambreException;
import ch.globaz.pegasus.business.models.home.SimpleTypeChambre;
import ch.globaz.pegasus.business.models.home.SimpleTypeChambreSearch;

public interface SimpleTypeChambreService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws TypeChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleTypeChambreSearch search) throws TypeChambreException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� typeChambre
     * 
     * @param typeChambre
     *            Le typeChambre � cr�er
     * @return Le typeChambre cr��
     * @throws TypeChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleTypeChambre create(SimpleTypeChambre typeChambre) throws TypeChambreException,
            JadePersistenceException;

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
    public SimpleTypeChambre delete(SimpleTypeChambre typeChambre) throws TypeChambreException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire un typeChambre
     * 
     * @param idTypeChambre
     *            L'identifiant du typeChambre � charger en m�moire
     * @return Le typeChambre charg� en m�moire
     * @throws TypeChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleTypeChambre read(String idTypeChambre) throws TypeChambreException, JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� typeChambre
     * 
     * @param typeChambre
     *            Le typeChambre � mettre � jour
     * @return Le typeChambre mis � jour
     * @throws TypeChambreException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleTypeChambre update(SimpleTypeChambre typeChambre) throws TypeChambreException,
            JadePersistenceException;
}
