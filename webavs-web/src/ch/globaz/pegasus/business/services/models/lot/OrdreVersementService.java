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
     * Permet de simuler les ordres de versements qui seront effectu�e en compta
     * 
     * @param idPrestation
     * @return
     * @throws JadePersistenceException
     * @throws JAException
     */
    public ComptabilisationData computedOrdrevrsement(String idPrestation) throws JadePersistenceException,
            JadeApplicationException, JAException;

    /**
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws OrdreVersementException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(OrdreVersementSearch search) throws OrdreVersementException, JadePersistenceException;

    /**
     * Permet d'effectuer un regroupement des <code>OrdreVersementForList</code> pr�sent dans une collection. Le
     * regroupement se fait au niveau des type d'ordres de versement. D�finit dans l'interace
     * <code>IREOrdresVersements.java</code>
     * 
     * @param listeOVs
     * @return Map d' OV group� par type
     * @throws JadePersistenceException
     * @throws OrdreVersementException
     */
    public Map<String, List<OrdreVersementForList>> groupByType(List<OrdreVersementForList> listeOVs)
            throws JadePersistenceException, OrdreVersementException;

    /**
     * Permet de charger en m�moire un ordre de versement
     * 
     * @param idOrdreVersement
     *            L'identifiant de l'ordre de versement � charger en m�moire
     * @return L'ordre de versement charg�e en m�moire
     * @throws OrdreVersementException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public OrdreVersement read(String idOrdreVersement) throws JadePersistenceException, OrdreVersementException;

    /**
     * Permet de chercher des ordres de versement pour la liste selon un mod�le de crit�res.
     * 
     * @param search
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws OrdreVersementException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public OrdreVersementForListSearch search(OrdreVersementForListSearch search) throws JadePersistenceException,
            OrdreVersementException;

    /**
     * Permet de chercher des ordres de versement selon un mod�le de crit�res.
     * 
     * @param search
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws OrdreVersementException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
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
     * Permet de chercher les ordres de versement qui sont li�e � une prestation.
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
