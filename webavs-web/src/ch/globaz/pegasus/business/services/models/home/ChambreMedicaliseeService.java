package ch.globaz.pegasus.business.services.models.home;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.home.ChambreMedicaliseeException;
import ch.globaz.pegasus.business.models.home.ChambreMedicaliseeSearch;

public interface ChambreMedicaliseeService extends JadeApplicationService {

    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws ChambreMedicaliseeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(ChambreMedicaliseeSearch search) throws ChambreMedicaliseeException, JadePersistenceException;

    /**
     * Permet de chercher des prixChambre selon un modèle de critères.
     * 
     * @param ChambreMedicaliseeSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ChambreMedicaliseeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public ChambreMedicaliseeSearch search(ChambreMedicaliseeSearch prixChambreSearch) throws JadePersistenceException,
            ChambreMedicaliseeException;

}
