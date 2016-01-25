package ch.globaz.amal.business.services.models.annonce;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.annonce.AnnoncesCaisse;
import ch.globaz.amal.business.models.annonce.AnnoncesCaisseSearch;
import ch.globaz.amal.business.models.annonce.SimpleAnnonce;
import ch.globaz.amal.business.models.annonce.SimpleAnnonceSearch;

/**
 * @author DHI
 * 
 */
public interface SimpleAnnonceService extends JadeApplicationService {

    /**
     * Permet la création d'une annonce
     * 
     * @param simpleAnnonce
     *            l'annonce à créer
     * @return l'annonce créée
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AnnonceException
     * @throws DetailFamilleException
     */
    public SimpleAnnonce create(SimpleAnnonce simpleAnnonce) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, AnnonceException, DetailFamilleException;

    /**
     * Permet la suppression d'une annonce
     * 
     * @param simpleAnnonce
     * @return l'annonce supprimée
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AnnonceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DetailFamilleException
     */
    public SimpleAnnonce delete(SimpleAnnonce simpleAnnonce) throws JadePersistenceException, AnnonceException,
            DetailFamilleException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en mémoire une annonce
     * 
     * @param idAnnonce
     * @return l'annonce
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AnnonceException
     */
    public SimpleAnnonce read(String idAnnonce) throws JadePersistenceException, AnnonceException;

    /**
     * Permet de charger en mémoire une annonce groupée
     * 
     * @param idAnnonce
     * @return l'annonce
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AnnonceException
     */
    public AnnoncesCaisse readAnnonce(String idAnnonce) throws JadePersistenceException, AnnonceException;

    /**
     * Permet la recherche d'une annonce groupée
     * 
     * @param search
     *            le modèle de recherche
     * @return le modèle renseigné
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AnnonceException
     */
    public AnnoncesCaisseSearch search(AnnoncesCaisseSearch search) throws JadePersistenceException, AnnonceException;

    /**
     * Permet la recherche d'une annonce simple
     * 
     * @param search
     *            le modèle de recherche
     * @return le modèle renseigné
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AnnonceException
     */
    public SimpleAnnonceSearch search(SimpleAnnonceSearch search) throws JadePersistenceException, AnnonceException;

    /**
     * Permet la mise à jour d'une annonce
     * 
     * @param simpleAnnonce
     * @return l'annonce mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AnnonceException
     * @throws DetailFamilleException
     */
    public SimpleAnnonce update(SimpleAnnonce simpleAnnonce) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, AnnonceException, DetailFamilleException;

}
