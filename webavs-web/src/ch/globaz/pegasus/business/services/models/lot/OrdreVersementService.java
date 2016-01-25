package ch.globaz.pegasus.business.services.models.lot;

import globaz.globall.util.JAException;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.models.lot.OrdreVersement;
import ch.globaz.pegasus.business.models.lot.OrdreVersementForList;
import ch.globaz.pegasus.business.models.lot.OrdreVersementForListSearch;
import ch.globaz.pegasus.business.models.lot.OrdreVersementSearch;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.ComptabilisationData;

/**
 * @author dma
 * 
 */
public interface OrdreVersementService extends JadeApplicationService {

    /**
     * Permet de simuler les ordres de versements qui seront effectuée en compta
     * 
     * @param idPrestation
     * @return
     * @throws JadePersistenceException
     * @throws JAException
     */
    public ComptabilisationData computedOrdrevrsement(String idPrestation) throws JadePersistenceException,
            JadeApplicationException, JAException;

    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws OrdreVersementException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(OrdreVersementSearch search) throws OrdreVersementException, JadePersistenceException;

    /**
     * Permet d'effectuer un regroupement des <code>OrdreVersementForList</code> présent dans une collection. Le
     * regroupement se fait au niveau des type d'ordres de versement. Définit dans l'interace
     * <code>IREOrdresVersements.java</code>
     * 
     * @param listeOVs
     * @return Map d' OV groupé par type
     * @throws JadePersistenceException
     * @throws OrdreVersementException
     */
    public Map<String, List<OrdreVersementForList>> groupByType(List<OrdreVersementForList> listeOVs)
            throws JadePersistenceException, OrdreVersementException;

    /**
     * Permet de charger en mémoire un ordre de versement
     * 
     * @param idOrdreVersement
     *            L'identifiant de l'ordre de versement à charger en mémoire
     * @return L'ordre de versement chargée en mémoire
     * @throws OrdreVersementException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public OrdreVersement read(String idOrdreVersement) throws JadePersistenceException, OrdreVersementException;

    /**
     * Permet de chercher des ordres de versement pour la liste selon un modèle de critères.
     * 
     * @param search
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws OrdreVersementException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public OrdreVersementForListSearch search(OrdreVersementForListSearch search) throws JadePersistenceException,
            OrdreVersementException;

    /**
     * Permet de chercher des ordres de versement selon un modèle de critères.
     * 
     * @param search
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws OrdreVersementException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public OrdreVersementSearch search(OrdreVersementSearch search) throws JadePersistenceException,
            OrdreVersementException;

    /**
     * Peremt de rechercher le ov mais retourne une Map<Type d'ov, list<ov>>
     * 
     * @param search
     * @return
     * @throws JadePersistenceException
     * @throws OrdreVersementException
     */
    public Map<String, List<OrdreVersement>> searchAndGroupByType(OrdreVersementSearch search)
            throws JadePersistenceException, OrdreVersementException;

    public List<OrdreVersementForList> searchOvByLot(String idLot) throws JadePersistenceException,
            OrdreVersementException;

    /**
     * Permet de chercher les ordres de versement qui sont liée à une prestation.
     * 
     * @param idPrestation
     * @return
     * @throws JadePersistenceException
     * @throws OrdreVersementException
     */
    public List<OrdreVersementForList> searchOvByPrestation(String idPrestation) throws JadePersistenceException,
            OrdreVersementException;

    /**
     * Update seulement le simpleOrdreVersement
     * 
     * @param ordreVersement
     * @return
     * @throws JadePersistenceException
     * @throws OrdreVersementException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public OrdreVersement update(OrdreVersement ordreVersement) throws JadePersistenceException,
            OrdreVersementException, JadeApplicationServiceNotAvailableException;

}
