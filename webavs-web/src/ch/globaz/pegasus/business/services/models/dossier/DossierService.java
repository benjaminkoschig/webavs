package ch.globaz.pegasus.business.services.models.dossier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.models.dossier.Dossier;
import ch.globaz.pegasus.business.models.dossier.DossierRCListSearch;
import ch.globaz.pegasus.business.models.dossier.DossierSearch;
import ch.globaz.prestation.business.exceptions.models.DemandePrestationException;

public interface DossierService extends JadeApplicationService {

    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws DossierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(DossierSearch search) throws DossierException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� dossier
     * 
     * @param dossier
     *            Le dossier � cr�er
     * @return Le dossier cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DossierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Dossier create(Dossier dossier) throws JadePersistenceException, DossierException,
            DemandePrestationException;

    public Dossier delete(Dossier dossier) throws DossierException, JadePersistenceException;

    /**
     * Permet de charger en m�moire un dossier
     * 
     * @param idDossier
     *            L'identifiant du dossier � charger en m�moire
     * @return Le dossier charg� en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DossierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Dossier read(String idDossier) throws JadePersistenceException, DossierException;

    /**
     * Permet de chercher des dossiers selon un mod�le de crit�res.
     * 
     * @param dossierSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DossierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public DossierSearch search(DossierSearch dossierSearch) throws JadePersistenceException, DossierException;

    /**
     * Permet de chercher des dossiers selon un mod�le de crit�res.
     * 
     * @param dossierSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats (DossierRcList)
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DossierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public DossierRCListSearch searchRCList(DossierRCListSearch dossierSearch) throws JadePersistenceException,
            DossierException;

    /**
     * 
     * Permet la mise � jour d'une entit� dossier
     * 
     * @param dossier
     *            Le dossier � mettre � jour
     * @return Le dossier mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DossierException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Dossier update(Dossier dossier) throws JadePersistenceException, DossierException;
}
