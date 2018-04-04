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
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws DemandeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(DemandeSearch search) throws DemandeException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� demande
     * 
     * @param demande
     *            La demande � cr�er
     * @return La demande cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DemandeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DossierException
     */
    public Demande create(Demande demande) throws JadePersistenceException, DemandeException,
            DemandePrestationException, DossierException;

    /**
     * Permet la suppression d'une entit� demande PC
     * 
     * @param demande
     *            La demande PC � supprimer
     * @return La demande PC supprim�
     * @throws DemandeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
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
     * Permet de charger en m�moire une demande PC
     * 
     * @param idDemande
     *            L'identifiant de la demande � charger en m�moire
     * @return La demande charg�e en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DemandeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Demande read(String idDemande) throws JadePersistenceException, DemandeException;

    /**
     * Permet le chargement d'une entit� listDemandes pour la liste
     * 
     * @param idDemande
     * @return
     * @throws JadePersistenceException
     * @throws DemandeException
     */
    public ListDemandes readDemande(String idDemande) throws JadePersistenceException, DemandeException;

    /**
     * Permet de chercher des demandes selon un mod�le de crit�res.
     * 
     * @param demandeSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DemandeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
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
     * Permet la mise � jour d'une entit� demande
     * 
     * @param demande
     *            La demande PC � mettre � jour
     * @return La demande PC mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws DemandeException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws DossierException
     */
    public Demande update(Demande demande) throws JadePersistenceException, DemandeException, DossierException;

    /**
     * La r�ouverture de la demande consite � : 1. Enlever la date de fin de la demande PC. 2. Passer l��tat de la
     * demande PC en � Octroy� � 3. Supprimer la date de fin des PCA en refus (attention 2 PCA si le couple est s�par�
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
     * Permet de refermer la demande si la demande a �t� rouverte
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
     * Revient en arri�re sur l'annulation d'une demande
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
