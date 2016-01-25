package ch.globaz.pegasus.business.services.models.dossier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.models.dossier.SimpleDossier;
import ch.globaz.pegasus.business.models.dossier.SimpleDossierSearch;

public interface SimpleDossierService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws DossierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleDossierSearch search) throws DossierException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� dossier. Le dossier doit avoir l'id d'une demande de prestation associ�e qui
     * existe et qui n'est pas d�j� associ�e � un autre dossier, sinon une exception est lev�e.
     * 
     * @param dossier
     *            Le dossier � cr�er
     * @return Le dossier cr��
     * @throws DossierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDossier create(SimpleDossier dossier) throws DossierException, JadePersistenceException;

    /**
     * Permet la suppression d'une entit� dossier
     * 
     * @param dossier
     *            Le dossier � supprimer
     * @return Le dossier supprim�
     * @throws DossierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDossier delete(SimpleDossier dossier) throws DossierException, JadePersistenceException;

    /**
     * Permet de charger en m�moire un dossier
     * 
     * @param idDossier
     *            L'identifiant du dossier � charger en m�moire
     * @return Le dossier charg� en m�moire
     * @throws DossierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDossier read(String idDossier) throws DossierException, JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� dossier
     * 
     * @param dossier
     *            Le dossier � mettre � jour
     * @return Le dossier mis � jour
     * @throws DossierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDossier update(SimpleDossier dossier) throws DossierException, JadePersistenceException;
}
