package ch.globaz.pegasus.business.services.models.home;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.home.ChambreMedicaliseeException;
import ch.globaz.pegasus.business.models.home.ChambreMedicaliseeSearch;

public interface ChambreMedicaliseeService extends JadeApplicationService {

    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws ChambreMedicaliseeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(ChambreMedicaliseeSearch search) throws ChambreMedicaliseeException, JadePersistenceException;

    /**
     * Permet de chercher des prixChambre selon un mod�le de crit�res.
     * 
     * @param ChambreMedicaliseeSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ChambreMedicaliseeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public ChambreMedicaliseeSearch search(ChambreMedicaliseeSearch prixChambreSearch) throws JadePersistenceException,
            ChambreMedicaliseeException;

}
