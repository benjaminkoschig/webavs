package ch.globaz.pegasus.business.services.models.pcaccordee;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PersonneDansPlanCalculException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.pcaccordee.ListPCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.ListPCAccordeeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAIdMembreFamilleRetenuSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAWithCalculMembreFamilleAndPrestationSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeIdMembresRetenusSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeePlanCalculSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalculSearch;
import ch.globaz.pegasus.business.models.pcaccordee.VersionDroitPCAPlanDeCaculeSearch;
import ch.globaz.pegasus.business.vo.pcaccordee.PCAAccordeePlanClaculeAndMembreFamilleVO;
import ch.globaz.pegasus.business.vo.pcaccordee.PCAccordeePlanCalculRetenuEnfantsDansCalculVO;
import ch.globaz.pegasus.businessimpl.services.models.pcaccordee.CalculMontantRetroActif;

/**
 * @author DMA
 */
public interface PCAccordeeService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(PCAccordeeSearch search) throws PCAccordeeException, JadePersistenceException;

    public List<PCAccordee> findPcaForPeriode(List<String> idsPca) throws PCAccordeeException;

    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(PCAccordeePlanCalculSearch search) throws PCAccordeeException, JadePersistenceException;

    /**
     * PErmet de compter le nombre de membre famille compris dans le plan de calcul passé en paramètre
     * 
     * @param idPcal
     *            , l'i du plan de calcul
     * @return nombre de personnes comprises
     * @throws JadePersistenceException
     * @throws PCAccordeeException
     */
    public int countMembreFamilleForPlanRetenu(String idPcal) throws JadePersistenceException, PCAccordeeException;

    /**
     * Permet la création d'une entité PCAccordee
     * 
     * @param PCAccordee
     *            La PCAccordee à creer
     * @return La PCAccordee créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PCAccordee create(PCAccordee pcAccordee) throws JadePersistenceException, PCAccordeeException;

    /**
     * Permet la suppression d'une entité pcAccordee
     * 
     * @param pcAccordee
     *            La PCAccordee à supprimer
     * @return La PCAccordee supprimé
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws JadeApplicationException
     */
    public PCAccordee delete(PCAccordee pcAccordee) throws PCAccordeeException, JadePersistenceException,
            JadeApplicationException;

    /**
     * Permet la suprime toutes les pcAccordee(et toute ces enfants) liée à une version de droit
     * 
     * @param pcAccordee
     *            La pcAccordee à mettre à jour
     * @return La pcAccordee mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public void deleteByIdVersionDroit(Droit droit) throws JadePersistenceException, JadeApplicationException;

    /**
     * Suppression des pca en passant l''d de la version de droit !!! Attention sous entende que le droit est
     * supprimable, pour les decsup !!
     * 
     * @param idDroit
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public void deleteByIdVersionDroit(String idDroit) throws JadePersistenceException, JadeApplicationException;

    /**
     * Permet de retrouvé le plan de clacule qui est rentenu pour la pc accordé
     * 
     * @param idPCAccordee
     * @return
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     */
    public SimplePlanDeCalcul findSimplePlanCalculeRetenu(String idPCAccordee) throws PCAccordeeException,
            JadePersistenceException;

    /**
     * Retourne une map<idPcAccodee,montantRetroActif> contenant les montants rétroactif brut sans déduction. Si aucun
     * plan de calcule n'est trouvé on retourn null;
     * 
     * @param idDemandePc
     *            l'id de la demande
     * @param noVersionDroit
     *            le numero de la version
     * @return simplePlanDeCalcul le plan de calcul
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     */
    public CalculMontantRetroActif getCalculMontantRetroActif(String idDemande, String noVersionDroit)
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Retourne une map<idPcAccodee,montantRetroActif> contenant les montants rétroactif. Si aucun plan de calcule n'est
     * trouvé on retourn null;
     * 
     * @param idDemandePc
     * @param noVersionDroit
     * @return simplePlanDeCalcul
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     */
    public Map<String, String> getMapMontantRetroActif(PCAccordee pcAccordee) throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Retourne une map<idPcAccodee,montantRetroActif> contenant les montants rétroactif.
     * 
     * @param idDemandePc
     * @param noVersionDroit
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     */
    public Map<String, String> getMapMontantRetroActif(String idDemande, String noVersionDroit)
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException;

    public Map<String, String> getMapMontantRetroActifBrut(String idDemande, String noVersionDroit)
            throws PCAccordeeException, JadeApplicationServiceNotAvailableException, JadePersistenceException;

    public abstract void loadJoursAppoint(PCAccordee pcAccordee) throws JadePersistenceException, PCAccordeeException,
            JadeApplicationServiceNotAvailableException;

    // void searchPlanCalcul(String idTiers) throws JadePersistenceException,
    // PCAccordeeException;

    /**
     * Permet de charger en mémoire une pc accordee pour le listage(réduit)
     * 
     * @param idPCAccordee
     *            L'identifiant de la pc accordee à charger en mémoire
     * @return La pc accordee chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public ListPCAccordee read(String idPCAccordee) throws JadePersistenceException, PCAccordeeException;

    /**
     * Retourne le detail complet d'une pc accordee
     * 
     * @param idPCAccordee
     *            , l'identifinat de la pc a charger
     * @return pcAccordee, le modele chargé
     * @throws JadePersistenceException
     * @throws PCAccordeeException
     */
    public PCAccordee readDetail(String idPCAccordee) throws JadePersistenceException, PCAccordeeException;

    public PCAccordee readDetailwithPcal(String idPCAccordee) throws PCAccordeeException, JadePersistenceException;

    /**
     * @return
     */
    public PCAccordeeIdMembresRetenusSearch search(PCAccordeeIdMembresRetenusSearch search) throws PCAccordeeException,
            JadePersistenceException;

    /**
     * Permet de chercher des pc accordees liée a un plan calcul selon un modèle de critères.
     * 
     * @param pcAccordeePlanCalculSearch
     *            Le modèle de critère avec les résultats
     * @return Le modèle de critère avec les résultats
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     */
    public PCAccordeePlanCalculSearch search(PCAccordeePlanCalculSearch pcAccordeePlanCalculSearch)
            throws PCAccordeeException, JadePersistenceException;

    /**
     * Permet de chercher des pc accordees selon un modèle de critères.
     * 
     * @param pcAccordeeSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PCAccordeeSearch search(PCAccordeeSearch pcAccordeeSearch) throws JadePersistenceException,
            PCAccordeeException;

    /**
     * Permet la rechecher pour trouveé toutes les pcaAccodé lié au prestation et remonte tout les personne comprsise
     * dans le plan de calcule
     * 
     * @param search
     * @return
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     */
    public PCAWithCalculMembreFamilleAndPrestationSearch search(
            PCAWithCalculMembreFamilleAndPrestationSearch pcaWithCalculMembreFamilleAndPrestationSearch)
            throws PCAccordeeException, JadePersistenceException;

    /**
     * Permet de rechercher les personnes liées à un plan de calcule
     * 
     * @param planDeCalculWitMembreFamilleSearch
     * @return planDeCalculWitMembreFamilleSearch mis à jour
     * @throws PersonneDansPlanCalculException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public PlanDeCalculWitMembreFamilleSearch search(
            PlanDeCalculWitMembreFamilleSearch planDeCalculWitMembreFamilleSearch)
            throws PersonneDansPlanCalculException, JadePersistenceException;

    /**
     * Permet de chercher des pc accordees liée a un plan de calcul selon un modèle de critères. Les personneEtentue
     * sont aussi lié ainci que le la version du droit
     * 
     * @param pcAccordeePlanCalculSearch
     *            Le modèle de critère avec les résultats
     * @return Le modèle de critère avec les résultats
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     */
    public VersionDroitPCAPlanDeCaculeSearch search(VersionDroitPCAPlanDeCaculeSearch versionDroitPCAPlanDeCaculeSearch)
            throws PCAccordeeException, JadePersistenceException;

    public ListPCAccordeeSearch searchForList(ListPCAccordeeSearch pcAccordeeSearch) throws JadePersistenceException,
            PCAccordeeException;

    /**
     * Permet de retrouver les pc accordees d'un tiers, pour une periode donnee et un etat donne. Un objet
     * PCAccordeePlanCalculRetenuEnfantsDansCalculVO contient: - la pc accordee - le plan de calcul retenu (il y a
     * plusieurs plans de calcul lors d'un calcul comparatif) - la liste des enfants retenus pour le calcul comparatif
     * 
     * @param idTiers
     *            l'id du tiers pour lequel on cherche les pc accordees
     * @param dateDebut
     *            debut de la periode
     * @param dateFin
     *            fin de la periode
     * @param csEtat
     *            etat de la pc accordee
     * @return les pc accordees correspondant aux criteres
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     * @throws PersonneDansPlanCalculException
     */
    public List<PCAccordeePlanCalculRetenuEnfantsDansCalculVO> searchPCAccordeePlanCalculRetenuEnfants(String idTiers,
            String dateDebut, String dateFin, String csEtat) throws PCAccordeeException, JadePersistenceException,
            PersonneDansPlanCalculException;

    /**
     * Permet de rechercher les PCAccordées validé par l'idTiers du membre de la famille et valable à une date donnée
     * L'objet dans la liste condiadra les montant du plans de calcule retenu et la liste des membre de famille
     * 
     * @param idTiersMembreFamille
     *            l'id tiers d'un des membres de la famille
     * @return Une liste de PCAAccordeePlanClaculeAndMembreFamilleVO
     * @throws JadePersistenceException
     * @throws PCAccordeeException
     */
    public List<PCAAccordeePlanClaculeAndMembreFamilleVO> searchPCAccordeeWithCalculeRetenuVO(
            String idTiersMembreFamille, String dateValable) throws PCAccordeeException, JadePersistenceException;

    /**
     * Retourne les IdMembreFamilleSF de tous les membre de la famille retenu dans le plan de calcul retenu des PCA
     * courante selectionnees
     * 
     * @param search
     * @return
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     */
    public PCAIdMembreFamilleRetenuSearch searchPCAIdMembreFamilleRetenuSearch(
            PCAIdMembreFamilleRetenuSearch pcaIdMembreFamilleRetenuSearch) throws PCAccordeeException,
            JadePersistenceException;

    public ArrayList<SimplePlanDeCalcul> searchPlanCalcul(SimplePlanDeCalculSearch search)
            throws JadePersistenceException, PCAccordeeException;

    /**
     * Permet la mise à jour d'une entité pcAccordee
     * 
     * @param pcAccordee
     *            La pcAccordee à mettre à jour
     * @return La pcAccordee mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public PCAccordee update(PCAccordee pcAccordee) throws JadePersistenceException, JadeApplicationException;

    /**
     * Permet la mise à jour du plan de calcule qui doit être retenu
     * 
     * @param SimplePlanDeCalcul
     * @return La pcAccordee mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PCAccordeeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimplePlanDeCalcul updatePlanDecalculeRetenu(String idPlanDeCalcul) throws JadePersistenceException,
            PCAccordeeException;

    public Map<String, List<Map<String, Object>>> xxVisuel(String idDossier) throws JadePersistenceException,
            PCAccordeeException;

    /**
     * Permet de trouver la dernière PCA (la plus récente) Si aucune PCA n'est trouvée on retourne null
     * 
     * @param idDemande
     * @return la pca le plus récente
     * @throws PCAccordeeException
     * @throws JadePersistenceException
     */
    public PCAccordee findLastestPca(String idDemande) throws PCAccordeeException, JadePersistenceException;

    /**
     * Permet de calculer et retourner le montant de la restitution
     * 
     * @param idDroit
     * @param numVersionDroit
     * @param dateSuppressionDroit
     * @return
     * @throws OrdreVersementException
     * @throws PCAccordeeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws PmtMensuelException
     */
    public BigDecimal calculerMontantRestitution(String idDroit, String numVersionDroit, String dateSuppressionDroit)
            throws OrdreVersementException, PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, PmtMensuelException;
}
