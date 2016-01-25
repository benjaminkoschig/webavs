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
     * Permet la cr�ation d'une annonce
     * 
     * @param simpleAnnonce
     *            l'annonce � cr�er
     * @return l'annonce cr��e
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AnnonceException
     * @throws DetailFamilleException
     */
    public SimpleAnnonce create(SimpleAnnonce simpleAnnonce) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, AnnonceException, DetailFamilleException;

    /**
     * Permet la suppression d'une annonce
     * 
     * @param simpleAnnonce
     * @return l'annonce supprim�e
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AnnonceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DetailFamilleException
     */
    public SimpleAnnonce delete(SimpleAnnonce simpleAnnonce) throws JadePersistenceException, AnnonceException,
            DetailFamilleException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en m�moire une annonce
     * 
     * @param idAnnonce
     * @return l'annonce
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AnnonceException
     */
    public SimpleAnnonce read(String idAnnonce) throws JadePersistenceException, AnnonceException;

    /**
     * Permet de charger en m�moire une annonce group�e
     * 
     * @param idAnnonce
     * @return l'annonce
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AnnonceException
     */
    public AnnoncesCaisse readAnnonce(String idAnnonce) throws JadePersistenceException, AnnonceException;

    /**
     * Permet la recherche d'une annonce group�e
     * 
     * @param search
     *            le mod�le de recherche
     * @return le mod�le renseign�
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AnnonceException
     */
    public AnnoncesCaisseSearch search(AnnoncesCaisseSearch search) throws JadePersistenceException, AnnonceException;

    /**
     * Permet la recherche d'une annonce simple
     * 
     * @param search
     *            le mod�le de recherche
     * @return le mod�le renseign�
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AnnonceException
     */
    public SimpleAnnonceSearch search(SimpleAnnonceSearch search) throws JadePersistenceException, AnnonceException;

    /**
     * Permet la mise � jour d'une annonce
     * 
     * @param simpleAnnonce
     * @return l'annonce mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AnnonceException
     * @throws DetailFamilleException
     */
    public SimpleAnnonce update(SimpleAnnonce simpleAnnonce) throws JadePersistenceException,
            JadeApplicationServiceNotAvailableException, AnnonceException, DetailFamilleException;

}
