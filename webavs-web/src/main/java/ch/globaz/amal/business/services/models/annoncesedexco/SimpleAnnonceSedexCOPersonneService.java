package ch.globaz.amal.business.services.models.annoncesedexco;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.annoncesedexco.AnnonceSedexCOException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOPersonne;
import ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOPersonneSearch;

public interface SimpleAnnonceSedexCOPersonneService extends JadeApplicationService {
    /**
     * Permet la cr�ation d'une annonce sedexco en DB
     * 
     * @param simpleAnnonceSedexCOPersonne
     *            l'annonce � cr�er
     * @return l'annonce cr��e
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AnnonceSedexCOException
     * @throws DetailFamilleException
     */
    public SimpleAnnonceSedexCOPersonne create(SimpleAnnonceSedexCOPersonne simpleAnnonceSedexCOPersonne)
            throws JadePersistenceException, JadeApplicationServiceNotAvailableException, AnnonceSedexCOException,
            DetailFamilleException;

    /**
     * Permet la suppression d'une annonce sedexco en db
     * 
     * @param simpleAnnonceSedexCOPersonne
     * @return l'annonce supprim�e
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AnnonceSedexCOException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DetailFamilleException
     */
    public SimpleAnnonceSedexCOPersonne delete(SimpleAnnonceSedexCOPersonne simpleAnnonceSedexCOPersonne)
            throws JadePersistenceException, AnnonceSedexCOException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en m�moire une annonce sedexco depuis DB
     * 
     * @param idAnnonceSedex
     * @return l'annonce
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AnnonceSedexCOException
     */
    public SimpleAnnonceSedexCOPersonne read(String idAnnonceSedexCO) throws JadePersistenceException,
            AnnonceSedexCOException;

    /**
     * Permet la recherche d'une annonce sedexco simple
     * 
     * @param search
     *            le mod�le de recherche
     * @return le mod�le renseign�
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AnnonceException
     */
    public SimpleAnnonceSedexCOPersonneSearch search(SimpleAnnonceSedexCOPersonneSearch search)
            throws JadePersistenceException, AnnonceSedexCOException;

    /**
     * Permet la mise � jour d'une annonce sedexco en db
     * 
     * @param simpleAnnonceSedexCOPersonne
     * @return l'annonce mise � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AnnonceException
     * @throws DetailFamilleException
     */
    public SimpleAnnonceSedexCOPersonne update(SimpleAnnonceSedexCOPersonne simpleAnnonceSedexCOPersonne)
            throws JadePersistenceException, JadeApplicationServiceNotAvailableException, AnnonceSedexCOException,
            DetailFamilleException;
}
