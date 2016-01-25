package ch.globaz.amal.business.services.models.revenu;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.revenu.RevenuFullComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplexSearch;
import ch.globaz.amal.business.models.revenu.RevenuSearch;

public interface RevenuService extends JadeApplicationService {

    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(RevenuSearch search) throws RevenuException, JadePersistenceException;

    /**
     * Permet la création d'un revenu full complex (=taxation ou données financières)
     * 
     * @param revenuFullComplex
     * @return
     * @throws JadePersistenceException
     * @throws RevenuException
     */
    public RevenuFullComplex create(RevenuFullComplex revenuFullComplex) throws JadePersistenceException,
            RevenuException;

    /**
     * Permet la création d'un revenu historique complet
     * 
     * @param revenuHistoriqueComplex
     * @return
     * @throws JadePersistenceException
     * @throws RevenuException
     */
    public RevenuHistoriqueComplex create(RevenuHistoriqueComplex revenuHistoriqueComplex)
            throws JadePersistenceException, RevenuException;

    /**
     * Permet la suppression d'un revenu full complex (=taxation ou données financières)
     * 
     * @param revenuFullComplex
     * @return
     * @throws RevenuException
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public RevenuFullComplex delete(RevenuFullComplex revenuFullComplex) throws RevenuException,
            JadePersistenceException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet la suppression d'un revenu historique complet
     * 
     * @param revenuHistoriqueComplex
     * @return
     * @throws RevenuException
     * @throws JadePersistenceException
     */
    public RevenuHistoriqueComplex delete(RevenuHistoriqueComplex revenuHistoriqueComplex) throws RevenuException,
            JadePersistenceException;

    /**
     * Récupération d'un revenu particulier par rapport à un id donné. Ce revenu est issu de la liste renseignée par
     * getRevenusForSubsideYear
     * 
     * Revenu utilisé pour une simulation de calcul
     * 
     * @param anneeHistorique
     *            année du subside concerné
     * @param idContribuable
     *            contribuable concerné
     * @param idRevenu
     *            id du revenu recherché
     * @return RevenuFullComplex si trouvé, null sinon
     * @throws RevenuException
     * @throws JadePersistenceException
     */
    public RevenuHistoriqueComplex getRevenuForSubsideYear(String anneeHistorique, String idContribuable,
            String idRevenu) throws RevenuException, JadePersistenceException;

    /**
     * Récupération d'une liste de revenu qui peuvent être pris en compte pour appliquer le calcul d'un subside pour
     * l'anneeHistorique renseignée
     * 
     * @param anneeHistorique
     *            annee du subside concerné
     * @param idContribuable
     *            contribuable concerné
     * @return ArrayList<RevenuFullComplex>, un élément null indique un revenu assisté
     * @throws RevenuException
     * @throws JadePersistenceException
     */
    public ArrayList<RevenuHistoriqueComplex> getRevenusForSubsideYear(String anneeHistorique, String idContribuable)
            throws RevenuException, JadePersistenceException;

    /**
     * Permet la lecture d'un revenu full (ou données financières, taxation)
     * 
     * @param idRevenuFullComplex
     * @return
     * @throws JadePersistenceException
     * @throws RevenuException
     */
    public RevenuFullComplex readFullComplex(String idRevenuFullComplex) throws JadePersistenceException,
            RevenuException;

    /**
     * Permet la lecture d'un revenu historique complet
     * 
     * @param idRevenuFullComplex
     * @return
     * @throws JadePersistenceException
     * @throws RevenuException
     */
    public RevenuHistoriqueComplex readHistoriqueComplex(String idRevenuHistoriqueComplex)
            throws JadePersistenceException, RevenuException;

    /**
     * @param revenuFullComplexSearch
     * @return
     * @throws JadePersistenceException
     * @throws RevenuException
     */
    public RevenuHistoriqueComplexSearch search(RevenuHistoriqueComplexSearch revenuFullComplexSearch)
            throws JadePersistenceException, RevenuException;

    /**
     * Permet de chercher des revenus selon un modèle de critères.
     * 
     * @param revenuSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RevenuSearch search(RevenuSearch revenuSearch) throws JadePersistenceException, RevenuException;

    /**
     * Mise à jour d'un revenu full (ou données financières, taxations)
     * 
     * @param revenuFullComplex
     * @return
     * @throws JadePersistenceException
     * @throws RevenuException
     */
    public RevenuFullComplex update(RevenuFullComplex revenuFullComplex) throws JadePersistenceException,
            RevenuException;

    /**
     * Mise à jour d'un revenu historique complet
     * 
     * @param revenu
     * @return
     * @throws JadePersistenceException
     * @throws RevenuException
     */
    public RevenuHistoriqueComplex update(RevenuHistoriqueComplex revenu) throws JadePersistenceException,
            RevenuException;

}
