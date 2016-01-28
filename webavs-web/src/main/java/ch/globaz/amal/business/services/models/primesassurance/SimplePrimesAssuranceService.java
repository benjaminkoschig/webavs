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
     * Permet la création d'une prime d'assurance
     * 
     * @param simplePrimesAssurance
     *            l'prime d'assurance à créer
     * @return l'prime d'assurance créée
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrimesAssuranceException
     * @throws DetailFamilleException
     */
    public SimplePrimesAssurance create(SimplePrimesAssurance simplePrimesAssurance) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, PrimesAssuranceException, DetailFamilleException;

    /**
     * Permet la suppression d'une prime d'assurance
     * 
     * @param simplePrimesAssurance
     * @return la prime d'assurance supprimée
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrimesAssuranceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DetailFamilleException
     */
    public SimplePrimesAssurance delete(SimplePrimesAssurance simplePrimesAssurance) throws JadePersistenceException,
            PrimesAssuranceException, DetailFamilleException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en mémoire une prime d'assurance
     * 
     * @param idAnnonce
     * @return la prime d'assurance
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrimesAssuranceException
     */
    public SimplePrimesAssurance read(String idPrimesAssurance) throws JadePersistenceException,
            PrimesAssuranceException;

    /**
     * Permet la recherche d'une prime d'assurance simple
     * 
     * @param search
     *            le modèle de recherche
     * @return le modèle renseigné
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrimesAssuranceException
     */
    public SimplePrimesAssuranceSearch search(SimplePrimesAssuranceSearch search) throws JadePersistenceException,
            PrimesAssuranceException;

    /**
     * Permet la mise à jour d'une prime d'assurance
     * 
     * @param simplePrimesAssurance
     * @return la prime d'assurance mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrimesAssuranceException
     * @throws DetailFamilleException
     */
    public SimplePrimesAssurance update(SimplePrimesAssurance simplePrimesAssurance) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, PrimesAssuranceException, DetailFamilleException;

}
