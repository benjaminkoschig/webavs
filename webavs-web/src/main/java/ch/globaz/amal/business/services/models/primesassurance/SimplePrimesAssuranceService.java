package ch.globaz.amal.business.services.models.primesassurance;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.exceptions.models.primesassurance.PrimesAssuranceException;
import ch.globaz.amal.business.models.primesassurance.SimplePrimesAssurance;
import ch.globaz.amal.business.models.primesassurance.SimplePrimesAssuranceSearch;

public interface SimplePrimesAssuranceService extends JadeApplicationService {
    /**
     * Permet la cr�ation d'une prime d'assurance
     * 
     * @param simplePrimesAssurance
     *            l'prime d'assurance � cr�er
     * @return l'prime d'assurance cr��e
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrimesAssuranceException
     * @throws DetailFamilleException
     */
    public SimplePrimesAssurance create(SimplePrimesAssurance simplePrimesAssurance) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, PrimesAssuranceException, DetailFamilleException;

    /**
     * Permet la suppression d'une prime d'assurance
     * 
     * @param simplePrimesAssurance
     * @return la prime d'assurance supprim�e
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrimesAssuranceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DetailFamilleException
     */
    public SimplePrimesAssurance delete(SimplePrimesAssurance simplePrimesAssurance) throws JadePersistenceException,
            PrimesAssuranceException, DetailFamilleException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en m�moire une prime d'assurance
     * 
     * @param idAnnonce
     * @return la prime d'assurance
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrimesAssuranceException
     */
    public SimplePrimesAssurance read(String idPrimesAssurance) throws JadePersistenceException,
            PrimesAssuranceException;

    /**
     * Permet la recherche d'une prime d'assurance simple
     * 
     * @param search
     *            le mod�le de recherche
     * @return le mod�le renseign�
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrimesAssuranceException
     */
    public SimplePrimesAssuranceSearch search(SimplePrimesAssuranceSearch search) throws JadePersistenceException,
            PrimesAssuranceException;

    /**
     * Permet la mise � jour d'une prime d'assurance
     * 
     * @param simplePrimesAssurance
     * @return la prime d'assurance mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrimesAssuranceException
     * @throws DetailFamilleException
     */
    public SimplePrimesAssurance update(SimplePrimesAssurance simplePrimesAssurance) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, PrimesAssuranceException, DetailFamilleException;

}
