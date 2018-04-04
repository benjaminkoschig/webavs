package ch.globaz.pegasus.business.services.models.demande;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.dossiers.DossierException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.demande.DemandeSearch;
import ch.globaz.pegasus.business.models.demande.ListDemandes;
import ch.globaz.pegasus.business.models.demande.ListDemandesSearch;
import ch.globaz.pegasus.business.models.revisionquadriennale.ListRevisionsSearch;
import ch.globaz.prestation.business.exceptions.models.DemandePrestationException;

public interface DemandeService extends JadeApplicationService {

    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws DemandeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(DemandeSearch search) throws DemandeException, JadePersistenceException;

    /**
     * Permet la création d'une entité demande
     * 
     * @param demande
     *            La demande à créer
     * @return La demande créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DemandeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DossierException
     */
    public Demande create(Demande demande) throws JadePersistenceException, DemandeException,
            DemandePrestationException, DossierException;

    /**
     * Permet la suppression d'une entité demande PC
     * 
     * @param demande
     *            La demande PC à supprimer
     * @return La demande PC supprimé
     * @throws DemandeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public Demande delete(Demande demande) throws DemandeException, JadePersistenceException;

    /**
     * Attention un order by fix est fait pour cette fonction
     * 
     * @param searchModel
     * @return
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public ListDemandesSearch findDemandeForList(ListDemandesSearch searchModel) throws DecisionException,
            JadePersistenceException;

    /**
     * Retourn true si le tiers donne possede une Demande PC en premiere instruction. Les demandes dans les etats
     * "En attente justificatifs" et "En attente calcul" sont considerees comme etant en premiere instruction.
     * 
     * @param idTiers
     * @return
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws DemandeException
     */
    public Boolean hasDemandePCEnPremiereInstruction(String idTiers)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, DemandeException;

    /**
     * Permet de charger en mémoire une demande PC
     * 
     * @param idDemande
     *            L'identifiant de la demande à charger en mémoire
     * @return La demande chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DemandeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Demande read(String idDemande) throws JadePersistenceException, DemandeException;

    /**
     * Permet le chargement d'une entité listDemandes pour la liste
     * 
     * @param idDemande
     * @return
     * @throws JadePersistenceException
     * @throws DemandeException
     */
    public ListDemandes readDemande(String idDemande) throws JadePersistenceException, DemandeException;

    /**
     * Permet de chercher des demandes selon un modèle de critères.
     * 
     * @param demandeSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DemandeException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public DemandeSearch search(DemandeSearch demandeSearch) throws JadePersistenceException, DemandeException;

    /**
     * Permet la recherche des listes de demandes pour le rcListe
     * 
     * @param listDemandesSearch
     * @return
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    public ListDemandesSearch searchDemandes(ListDemandesSearch listDemandesSearch) throws DecisionException,
            JadePersistenceException;

    /**
     * Permet la recheche des demandes devant etre revisiees (Utilise pour generer la liste des revisions)
     * 
     * @param listRevisionsSearch
     * @return
     * @throws DemandeException
     * @throws JadePersistenceException
     */
    public ListRevisionsSearch searchRevisions(ListRevisionsSearch listRevisionsSearch) throws DemandeException,
            JadePersistenceException;

    /**
     * Permet la mise à jour d'une entité demande
     * 
     * @param demande
     *            La demande PC à mettre à jour
     * @return La demande PC mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws DemandeException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DossierException
     */
    public Demande update(Demande demande) throws JadePersistenceException, DemandeException, DossierException;

    /**
     * La réouverture de la demande consite à : 1. Enlever la date de fin de la demande PC. 2. Passer l’état de la
     * demande PC en « Octroyé » 3. Supprimer la date de fin des PCA en refus (attention 2 PCA si le couple est séparé
     * par la maladie)
     * 
     * @param demande
     * @return demande
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public Demande rouvrir(Demande demande) throws JadeApplicationException, JadePersistenceException;

    /**
     * Peremete de savoir s'il est possible de rouvrir la demande.
     * 
     * @param demande
     * @return
     * @throws DemandeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public boolean isLastDemande(Demande demande) throws DemandeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException;

    /**
     * Permet de refermer la demande si la demande a été rouverte
     * 
     * @param demande
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public Demande reFermer(Demande demande) throws JadePersistenceException, JadeApplicationException;

    /**
     * Service qui permet de savoir si il est possible de rouvri la demande
     * 
     * @param demande
     * @return true s'il est possible d'ouvrir la demande
     * @throws PegasusException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    public boolean isDemandeReouvrable(Demande demande) throws PegasusException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Annule la demande
     * 
     * @param demande
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public Demande annuler(Demande demande, Boolean comptabilisationAuto) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Revient en arrière sur l'annulation d'une demande
     * 
     * @param demande
     * @return
     * @throws JadePersistenceException
     * @throws DemandeException
     * @throws DossierException
     * @throws DroitException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public Demande retourArriereAnnuler(Demande demande) throws JadePersistenceException, DemandeException,
            DossierException, DroitException, JadeApplicationServiceNotAvailableException, DecisionException,
            DonneeFinanciereException, PCAccordeeException;

    public Demande dateReduction(Demande demande, Boolean comptabilisationAuto) throws JadePersistenceException,
    JadeApplicationException;
}
