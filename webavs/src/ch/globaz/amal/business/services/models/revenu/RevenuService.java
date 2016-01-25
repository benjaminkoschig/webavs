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
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(RevenuSearch search) throws RevenuException, JadePersistenceException;

    /**
     * Permet la cr�ation d'un revenu full complex (=taxation ou donn�es financi�res)
     * 
     * @param revenuFullComplex
     * @return
     * @throws JadePersistenceException
     * @throws RevenuException
     */
    public RevenuFullComplex create(RevenuFullComplex revenuFullComplex) throws JadePersistenceException,
            RevenuException;

    /**
     * Permet la cr�ation d'un revenu historique complet
     * 
     * @param revenuHistoriqueComplex
     * @return
     * @throws JadePersistenceException
     * @throws RevenuException
     */
    public RevenuHistoriqueComplex create(RevenuHistoriqueComplex revenuHistoriqueComplex)
            throws JadePersistenceException, RevenuException;

    /**
     * Permet la suppression d'un revenu full complex (=taxation ou donn�es financi�res)
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
     * R�cup�ration d'un revenu particulier par rapport � un id donn�. Ce revenu est issu de la liste renseign�e par
     * getRevenusForSubsideYear
     * 
     * Revenu utilis� pour une simulation de calcul
     * 
     * @param anneeHistorique
     *            ann�e du subside concern�
     * @param idContribuable
     *            contribuable concern�
     * @param idRevenu
     *            id du revenu recherch�
     * @return RevenuFullComplex si trouv�, null sinon
     * @throws RevenuException
     * @throws JadePersistenceException
     */
    public RevenuHistoriqueComplex getRevenuForSubsideYear(String anneeHistorique, String idContribuable,
            String idRevenu) throws RevenuException, JadePersistenceException;

    /**
     * R�cup�ration d'une liste de revenu qui peuvent �tre pris en compte pour appliquer le calcul d'un subside pour
     * l'anneeHistorique renseign�e
     * 
     * @param anneeHistorique
     *            annee du subside concern�
     * @param idContribuable
     *            contribuable concern�
     * @return ArrayList<RevenuFullComplex>, un �l�ment null indique un revenu assist�
     * @throws RevenuException
     * @throws JadePersistenceException
     */
    public ArrayList<RevenuHistoriqueComplex> getRevenusForSubsideYear(String anneeHistorique, String idContribuable)
            throws RevenuException, JadePersistenceException;

    /**
     * Permet la lecture d'un revenu full (ou donn�es financi�res, taxation)
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
     * Permet de chercher des revenus selon un mod�le de crit�res.
     * 
     * @param revenuSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public RevenuSearch search(RevenuSearch revenuSearch) throws JadePersistenceException, RevenuException;

    /**
     * Mise � jour d'un revenu full (ou donn�es financi�res, taxations)
     * 
     * @param revenuFullComplex
     * @return
     * @throws JadePersistenceException
     * @throws RevenuException
     */
    public RevenuFullComplex update(RevenuFullComplex revenuFullComplex) throws JadePersistenceException,
            RevenuException;

    /**
     * Mise � jour d'un revenu historique complet
     * 
     * @param revenu
     * @return
     * @throws JadePersistenceException
     * @throws RevenuException
     */
    public RevenuHistoriqueComplex update(RevenuHistoriqueComplex revenu) throws JadePersistenceException,
            RevenuException;

}
