package ch.globaz.amal.business.services.models.detailfamille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.exceptions.models.documents.DocumentException;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;

/**
 * @author CBU
 */
public interface SimpleDetailFamilleService extends JadeApplicationService {

    /**
     * Permet de compter le nombre de subsides
     * 
     * @param detailFamille
     *            le detailFamille a compter
     * @return le nombre de detailFamille
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DetailFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public int count(SimpleDetailFamilleSearch detailFamilleSearch) throws JadePersistenceException,
            DetailFamilleException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la cr�ation d'un detailFamille
     * 
     * @param detailFamille
     *            le detailFamille a cr�er
     * @return le detailFamille cr�e
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DetailFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleDetailFamille create(SimpleDetailFamille detailFamille) throws JadePersistenceException,
            DetailFamilleException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'une entit� detailFamille
     * 
     * @param detailFamille
     *            Le detailFamille � supprimer
     * @return Le detailFamille supprim�
     * @throws DetailFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDetailFamille delete(SimpleDetailFamille detailFamille) throws DetailFamilleException,
            JadePersistenceException, DocumentException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en m�moire un detailFamille
     * 
     * @param idDetailFamille
     *            L'identifiant du detailFamille � charger en m�moire
     * @return Le detailFamille charg� en m�moire
     * @throws DetailFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDetailFamille read(String idDetailFamille) throws DetailFamilleException, JadePersistenceException;

    /**
     * 
     * Permet la recherche de subsides (detailFamille) selon un mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return mod�le de recherche renseign�
     * @throws JadePersistenceException
     * @throws DetailFamilleException
     */
    public SimpleDetailFamilleSearch search(SimpleDetailFamilleSearch search) throws JadePersistenceException,
            DetailFamilleException;

    /**
     * Permet la mise � jour d'une entit� detailFamille
     * 
     * @param detailFamille
     *            Le detailFamille � mettre � jour
     * @return Le detailFamille mis � jour
     * @throws DetailFamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleDetailFamille update(SimpleDetailFamille detailFamille) throws DetailFamilleException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

}
