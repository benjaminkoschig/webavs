/**
 * 
 */
package ch.globaz.amal.business.services.models.annoncesedex;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.amal.business.exceptions.models.annoncesedex.AnnonceSedexException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedex;
import ch.globaz.amal.business.models.annoncesedex.SimpleAnnonceSedexSearch;

/**
 * @author dhi
 * 
 */
public interface SimpleAnnonceSedexService extends JadeApplicationService {
    /**
     * Permet la création d'une annonce sedex en DB
     * 
     * @param simpleAnnonceSedex
     *            l'annonce à créer
     * @return l'annonce créée
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AnnonceSedexException
     * @throws DetailFamilleException
     */
    public SimpleAnnonceSedex create(SimpleAnnonceSedex simpleAnnonceSedex) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, AnnonceSedexException, DetailFamilleException;

    /**
     * Permet la suppression d'une annonce sedex en db
     * 
     * @param simpleAnnonceSedex
     * @return l'annonce supprimée
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AnnonceSedexException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DetailFamilleException
     */
    public SimpleAnnonceSedex delete(SimpleAnnonceSedex simpleAnnonceSedex) throws JadePersistenceException,
            AnnonceSedexException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en mémoire une annonce sedex depuis DB
     * 
     * @param idAnnonceSedex
     * @return l'annonce
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AnnonceSedexException
     */
    public SimpleAnnonceSedex read(String idAnnonceSedex) throws JadePersistenceException, AnnonceSedexException;

    /**
     * Permet la recherche d'une annonce sedex simple
     * 
     * @param search
     *            le modèle de recherche
     * @return le modèle renseigné
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AnnonceException
     */
    public SimpleAnnonceSedexSearch search(SimpleAnnonceSedexSearch search) throws JadePersistenceException,
            AnnonceSedexException;

    /**
     * Permet la mise à jour d'une annonce sedex en db
     * 
     * @param simpleAnnonceSedex
     * @return l'annonce mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AnnonceException
     * @throws DetailFamilleException
     */
    public SimpleAnnonceSedex update(SimpleAnnonceSedex simpleAnnonceSedex) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, AnnonceSedexException, DetailFamilleException;

}
