package ch.globaz.corvus.business.services.models.rentesaccordees;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.corvus.business.exceptions.models.RentesAccordeesException;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleInformationsComptabilite;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleInformationsComptabiliteSearch;

public interface SimpleInformationsComptabiliteService extends JadeApplicationService {

    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws InformationsComptabiliteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleInformationsComptabiliteSearch search) throws RentesAccordeesException,
            JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� InformationsComptabilite. Le InformationsComptabilite doit avoir l'id d'une
     * demande de prestation associ�e qui existe et qui n'est pas d�j� associ�e � un autre InformationsComptabilite,
     * sinon une exception est lev�e.
     * 
     * @param InformationsComptabilite
     *            Le InformationsComptabilite � cr�er
     * @return Le InformationsComptabilite cr��
     * @throws InformationsComptabiliteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleInformationsComptabilite create(SimpleInformationsComptabilite simpleInformationsComptabilite)
            throws RentesAccordeesException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� InformationsComptabilite
     * 
     * @param InformationsComptabilite
     *            Le InformationsComptabilite � supprimer
     * @return Le InformationsComptabilite supprim�
     * @throws InformationsComptabiliteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleInformationsComptabilite delete(SimpleInformationsComptabilite simpleInformationsComptabilite)
            throws RentesAccordeesException, JadePersistenceException;

    /**
     * Permet de charger en m�moire un InformationsComptabilite
     * 
     * @param idInformationsComptabilite
     *            L'identifiant du InformationsComptabilite � charger en m�moire
     * @return Le InformationsComptabilite charg� en m�moire
     * @throws InformationsComptabiliteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleInformationsComptabilite read(String idInformationsComptabilite) throws RentesAccordeesException,
            JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� InformationsComptabilite
     * 
     * @param InformationsComptabilite
     *            Le InformationsComptabilite � mettre � jour
     * @return Le InformationsComptabilite mis � jour
     * @throws InformationsComptabiliteException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleInformationsComptabilite update(SimpleInformationsComptabilite simpleInformationsComptabilite)
            throws RentesAccordeesException, JadePersistenceException;

}
