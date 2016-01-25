package ch.globaz.al.business.services.models.dossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.exceptions.business.ALDroitBusinessException;
import ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel;
import ch.globaz.al.business.models.dossier.DossierAttestationVersementComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierLieComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierModel;
import ch.globaz.al.business.models.dossier.LienDossierModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;

/**
 * Divers service métier pour les dossiers.
 * 
 * @author PTA
 * 
 * @see ch.globaz.al.business.models.dossier.DossierModel
 * @see ch.globaz.al.business.models.droit.DroitComplexModel
 */
public interface DossierBusinessService extends JadeApplicationService {

    /**
     * Charge les dossiers actifs pour une date donnée.
     * 
     * @param date Date à laquelle les dossiers doivent être actifs
     * @param idTiers tiers pour lequel les dossiers doivent être recherchés
     * 
     * @return Les dossiers correspondant aux critères de recherche
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierComplexSearchModel getDossiersActifs(String date, String idTiersAllocataire)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Compte le nombre de dossiers actifs pour une date et un tiers donné
     * 
     * @param date Date à laquelle les dossiers doivent être actifs
     * @param idTiers tiers pour lequel les dossiers doivent être comptés
     * 
     * @return Nombre de dossiers trouvés
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int countDossiersActifs(String date, String idTiersAllocataire) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Ajoute un lien entre 2 dossiers. 2 add DB seront faits, le lien demandé et son inverse
     * 
     * 
     * @param idDossierPere
     *            Le dossier père, celui depuis lequel on crée le lien
     * 
     * @param idDossierPere
     *            Le dossier fils, celui choisi par l'utilisateur
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void ajouterLien(String idDossierPere, String idDossierFils, String typeLien)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Modifie le type de lien d'un lien selon la nouvelle valeur passée en paramètre
     * 
     * 
     * @param idLien
     *            l'identifiant du lien dont il faut modifier le type
     * @param newTypeLien
     *            le nouveau type de lien
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void changerTypeLien(String idLien, String newTypeLien) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Copie un dossier dans son intégralité avec ses commentaires et ses droits. Dossier, commentaires et droits seront
     * des clones ( nouvelles instances)
     * 
     * Enregistre directement le dossier en DB, car il faut lié les copies des droits, commentaires,...
     * 
     * @param dossierComplexModel
     *            Le dossier référence pour la copie
     * @return le dossier résultant de la copie
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierComplexModel copierDossier(DossierComplexModel dossierComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Copie un dossier dans son intégralité avec ses commentaires et ses droits. Dossier, commentaires et droits seront
     * des clones ( nouvelles instances)
     * 
     * Enregistre directement le dossier en DB, car il faut lié les copies des droits, commentaires,...
     * 
     * ATTENTION : Ne fait pas une véritable copie du dossier, il s'agit d'une copie par référence.
     * 
     * @param dossierComplexModel
     *            Le dossier référence pour la copie
     * @param etatNouveauDossier
     *            L'état que doit avoir le nouveau dossier
     * @return le dossier résultant de la copie
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierComplexModel copierDossier(DossierComplexModel dossierComplexModel, String etatNouveauDossier)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode créant un nouveau dossier et la journalisation correspondante. Pas de gestion d'annonce Rafam car aucun
     * droit créé par ce service
     * 
     * @param dossier
     *            Le dossier à créer
     * @return Le dossier créé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierComplexModel createDossier(DossierComplexModel dossier) throws JadeApplicationException,
            JadePersistenceException;

    public DossierComplexModel createDossier(DossierComplexModel dossier, DossierAgricoleComplexModel dossierAgricole)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode effaçant le dossier et créant l'annonce rafam en conséquence
     * 
     * @param dossier
     *            Le dossier à supprimer
     * @return Le dossier supprimé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierComplexModel deleteDossier(DossierComplexModel dossier) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Retourne la liste des activités qui sont obligatoirement lié à un affilié de type "Cot. Pers."
     * 
     * @return HashSet contenant les codes systèmes des activités
     */
    public HashSet<String> getActivitesCategorieCotPers();

    /**
     * Retourne la liste des activités d'allocataire devant être traité en fonction du type de cotisation
     * 
     * @param typeCoti
     *            type de cotisation traité. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @return Liste des activités (codes système)
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public HashSet<String> getActivitesToProcess(String typeCoti) throws JadeApplicationException;

    /**
     * Retourne le modèle complexe de dossier auquel est lié le <code>droitComplexModel</code>
     * 
     * @param droitComplexModel
     *            permet de récupérer l'identifiant du dossier
     * @return dossierComplex modèle complexe du dossier
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     * @throws ALDroitBusinessException
     *             texte pour l'exception
     */
    public DossierComplexModel getDossierComplexForDroit(DroitComplexModel droitComplexModel)
            throws JadeApplicationException, JadePersistenceException;

    public DossierLieComplexSearchModel getDossiersLies(String idDossierPere) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Retourne le genre d'assurance en fonction du type d'activité de l'allocataire
     * 
     * @param activiteAllocataire
     *            Activité de l'allocataire
     * @return Genre d'assurance
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_ACTIVITE_ALLOC
     * @see ch.globaz.al.business.constantes.ALCSAffilie#GENRE_ASSURANCE_INDEP
     * @see ch.globaz.al.business.constantes.ALCSAffilie#GENRE_ASSURANCE_PARITAIRE
     */
    public String getGenreAssurance(String activiteAllocataire) throws JadeApplicationException;

    /**
     * Récupère la liste des numéros de dossiers actifs pour un allocataire et un affilié
     * 
     * @param nssAllocataire
     *            NSS de l'allocataire pour lequel rechercher les dossiers
     * @param idAffilie
     *            id de l'affiliation. Paramètre facultatif, s'il n'est pas indiqué, tous les dossiers actifs de
     *            l'allocataire seront retournés
     * @return Liste des numéros de dossier
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     */
    public JadeAbstractModel[] getIdDossiersActifs(String nssAllocataire, String idAffilie)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne le lien inverse du lien passé en paramètre Exemple lien original => dossier père : 12451, dossier fils :
     * 521 ==> lien inverse retourné => dossier père : 521, dossier fils: 12451
     * 
     * @param lienOriginal
     * @return lien inverse
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public LienDossierModel getLienInverse(LienDossierModel lienOriginal) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Méthode permettant de retrouver les dossier en fonction affilieé et allocataire
     * 
     * @param numAffilie
     * @param nssAllocataire
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public JadeAbstractModel[] getResultsAffilieAllocDossiers(String numAffilie, String nssAllocataire)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * REtourne le type de cotisation pour le dossier
     * 
     * @param dossierModel
     * @return {@link ALConstPrestations} TYPE_DIRECT, TYPE_INDIRECT_GROUPE, TYPE_COT_PAR, TYPE_COT_PERS
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getTypeCotisation(DossierModel dossierModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Vérifie si le dossier dont l'id est passé en paramètre a des annonces RAFam envoyées
     * 
     * @param idDossier
     *            dossier pour lequel vérifier l'existance d'annonces
     * @return <code>true</code> si des annonces ont été transmies, <code>false</code> dans le cas contraire
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean hasSentAnnonces(String idDossier) throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode vérifiant si le tiers correspond à l'affilié d'un dossier
     * 
     * @param dossier
     *            le dossier dont on veut vérifier l'affilié
     * @param idTiers
     *            l'id du tiers
     * @return vrai si le tiers correspond à l'affilié, faux sinon
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isAffilie(DossierComplexModel dossier, String idTiers) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Vérifie si <code>idTiers</code> correspond à une agence communale
     * 
     * @param idTiers
     *            id tiers à vérifier
     * @return <code>true</code> si <code>idTiers</code> est une agence communale
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isAgenceCommunale(String idTiers) throws JadeApplicationException, JadePersistenceException;

    /**
     * Vérifie si le type d'activité <code>typeActivite</code> correspond à une activité agricole
     * 
     * @param typeActivite
     *            Code système de l'activité
     * @return <code>true</code> si <code>typeActivite</code> correspond a une activité agricole
     * @throws JadeApplicationException
     *             Exception levée si <code>typeActivite</code> est <code>null</code>
     */
    public boolean isAgricole(String typeActivite) throws JadeApplicationException;

    /**
     * Méthode vérifiant si le tiers correspond à l'allocataire d'un dossier
     * 
     * @param dossier
     *            le dossier dont on veut vérifier l'allocataire
     * @param idTiers
     *            l'id du tiers
     * @return vrai si le tiers correspond à l'allocataire, faux sinon
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public boolean isAllocataire(DossierComplexModel dossier, String idTiers) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Détermine si le dossier est en paiement direct.
     * 
     * Un dossier est en paiement direct si
     * {@link ch.globaz.al.business.models.dossier.DossierFkModel#getIdTiersBeneficiaire()} est non <code>null</code> et
     * a une valeur suppérieure à 0
     * 
     * @param dossier
     *            Le dossier à vérifier
     * @return <code>true</code> si le dossier est en paiement direct, <code>false</code> sinon
     * @throws JadeApplicationException
     *             Exception levée si <code>typeActivite</code> est <code>null</code>
     */
    public boolean isModePaiementDirect(DossierModel dossier) throws JadeApplicationException;

    /**
     * Vérifie si le dossier est dans un contexte paritaire ou personnel
     * 
     * @param typeActivite
     *            type d'activité de l'allocataire
     * @return true : le dossier est paritaire, sinon personnel
     * @throws JadeApplicationException
     *             Exception levée si <code>typeActivite</code> est <code>null</code>
     */
    public boolean isParitaire(String typeActivite) throws JadeApplicationException;

    /**
     * Service retournant les différents valeurs du champ état dossier ne bloquant pas la génération de prestation lors
     * de la génération globale (selon le paramétrage défini dans ALPARAM)
     * 
     * @return liste des états
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public Set<String> listerEtatsOkGenerationGlobale() throws JadeApplicationException, JadePersistenceException;

    /**
     * Service retournant les états d'un dossier permettant la journalisation du changement d'état (selon le paramétrage
     * défini dans ALPARAM)
     * 
     * @return liste des états
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public Set<String> listerEtatsOkJournalise() throws JadeApplicationException, JadePersistenceException;

    /**
     * Service retournant les états d'un dossier permettant la création d'annonces RAFAM lors de mutation au sein d'un
     * dossier (selon le paramétrage défini dans ALPARAM)
     * 
     * @return liste des états
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public Set<String> listerEtatsOkRafam() throws JadeApplicationException, JadePersistenceException;

    /**
     * Définit le nombre de jours de début du dossier en fonction d'une date passée
     * 
     * @param dateDebutDossier
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public String nbreJourDebutMoisAF(String dateDebutDossier) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Définit le nombre de jours de fin du dossier en fonction d'une date passée
     * 
     * @param dateFinDossier
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public String nbreJourFinMoisAF(String dateFinDossier) throws JadePersistenceException, JadeApplicationException;

    /**
     * Radie le dossier passé en paramètre et le met à jour en persistance
     * 
     * @param dossier
     *            le dossier à radier
     * @param dateRadiation
     *            date de radiation
     * @param updateNbJoursFin
     *            indique si il faut mettre à jour le nombre de jours de fin selon date radiation
     * @param remarqueJournalisation
     *            La remarque associé à la journalisation du changement d'état
     * @return le dossier mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierModel radierDossier(DossierModel dossier, String dateRadiation, boolean updateNbJoursFin,
            String remarqueJournalisation) throws JadeApplicationException, JadePersistenceException;

    /**
     * Radie le dossier passé en paramètre et le met à jour en persistance
     * 
     * @param dossier
     *            le dossier à radier
     * @param dateRadiation
     *            date de radiation
     * @param updateNbJoursFin
     *            indique si il faut mettre à jour le nombre de jours de fin selon date radiation
     * @param remarqueJournalisation
     *            La remarque associé à la journalisation du changement d'état
     * @param reference
     *            Nouvelle personne de référence
     * @return le dossier mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierModel radierDossier(DossierModel dossier, String dateRadiation, boolean updateNbJoursFin,
            String remarqueJournalisation, String reference) throws JadeApplicationException, JadePersistenceException;

    /**
     * Service écrit pour la radiation des dossier Horloger lors de l'entrée en vigueur des 30.- horloger. Ne pas
     * utiliser ce service dans d'autres cas
     * 
     * @param dossier
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @deprecated Service écrit pour la radiation des dossier Horloger lors de l'entrée en vigueur des 30.- horloger.
     *             Ne pas utiliser ce service dans d'autres cas
     */
    @Deprecated
    public DossierModel radierDossierHorloger(DossierModel dossier, String dateRadiation)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Variante du service <code>nbreJourDebutMoisAF</code> @see nbreJourDebutMoisAF, appelable depuis composant widget
     * 
     * @param values
     * @return la nombre de jour calculée (-1 : date entrée invalide)
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String readWidget_nbreJourDebutMoisAF(HashMap<?, ?> values) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Variante du service <code>nbreJourFinMoisAF</code> @see nbreJourFinMoisAF, appelable depuis composant widget
     * 
     * @param values
     * @return le nombre de jour calculé (-1 : date entrée invalide)
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String readWidget_nbreJourFinMoisAF(HashMap<?, ?> values) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Retirer le tiers bénéficiaire du dossier. Ce service ne gère (volontairement) pas la persistance, il faudra
     * sauvegarder le dossierComplexModel après coup si nécessaire
     * 
     * @param dossierComplexModel
     *            Le dossier à modifier
     * @param keepDirect
     *            <code>true</code> : le bénéficiaire du dossier devient l'allocataire, <code>false</code> : employeur
     *            (paiement indirect)
     * @return Le dossier modifié
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    public DossierComplexModel retirerBeneficiaire(DossierComplexModel dossierComplexModel, boolean keepDirect)
            throws JadeApplicationException;

    /**
     * Retire le lien entre 2 dossiers, ce qui engendre 2 delete en DB ( un lien => 2 enregistrements)
     * 
     * @param idLien
     * 
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public void retirerLien(String idLien) throws JadeApplicationException, JadePersistenceException;

    /**
     * @param numAffilie
     *            numéro de l'affilie, numNss numéro di nss
     * @return les dossiers trouvées par la recherche
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierAttestationVersementComplexSearchModel searchDossierAttestation(String numAffilie, String numNss)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode mettant a jour le dossier et créant l'annonce rafam en conséquence, et la journalisation si changement
     * d'état
     * 
     * @param dossier
     *            Le dossier à mettre à jour
     * @param etatDossierAvantModif
     *            L'état du dossier avant la modification du modèle (état en DB). si null, journalisation créée pour
     *            indiquer changement état avec nouvelle valeur (modèle en paramètre)
     * @param remarqueJournalisation
     *            La remarque associé à la journalisation du changement d'état
     * @return Le dossier mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierComplexModel updateDossier(DossierComplexModel dossier, String etatDossierAvantModif,
            String remarqueJournalisation) throws JadeApplicationException, JadePersistenceException;

    public DossierComplexModel updateDossier(DossierComplexModel dossier, String etatDossierAvantModif,
            String remarqueJournalisation, DossierAgricoleComplexModel dossierAgricole)
            throws JadeApplicationException, JadePersistenceException;

}