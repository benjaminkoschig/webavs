package ch.globaz.al.business.services.models.droit;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.al.business.constantes.enumerations.droit.ALEnumMsgDroitPC;
import ch.globaz.al.business.exceptions.business.ALDroitBusinessException;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitModel;
import ch.globaz.al.business.models.rafam.AnnonceRafamModel;

import java.util.List;
import java.util.Map;

/**
 * Interface définissant les services métier implémentés liés au droit
 * 
 * @author GMO
 * 
 */
public interface DroitBusinessService extends JadeApplicationService {

    /**
     * Ajoute un ou des droit(s) du même type selon en fonction des "droits" existants selon l'âge de l'enfant. Cette
     * méthode est utilisée dans les cas des cantons ayant un tarif progressif en fonction de l'âge (ex : ZH)
     * 
     * @param droitComplexModel
     *            permet de récupérer le droit à ajouter
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    void ajoutDroitMemeType(DroitComplexModel droitComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * @param nssAllocataire
     * @param nssEnfant
     * @return un avertissement définit dans <code>ALEnumMsgDroitPC</code> si l'enfant ou lallocataire percoit des
     *         prestations complémentaires.
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    ALEnumMsgDroitPC checkPCFamille(String nssAllocataire, String nssEnfant) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Créer un droit en copiant le droit correspondant au paramètre idDroitSource avec les dates passées en paramètres,
     * en tenant compte de l'attestation.
     * 
     * @param idDroitSource
     * @param dateAttestationDroitSource
     * @param newDateDebut
     * @param newDateFin
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    void copierDroitAndUpdateAttestationDate(String idDroitSource, String dateAttestationDroitSource,
            String newDateDebut, String newDateFin) throws JadeApplicationException, JadePersistenceException;

    /**
     * Créer un droit formation à partir d'un droit enfant
     * 
     * @param droitComplexModel
     *            Le droit référence enfant
     * @return Le droit formation résultant
     * @throws JadeApplicationException
     *             Exception levée si le droit passé est <code>null</code> ou si la date n'est pas valide
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    DroitComplexModel copieToFormation(DroitComplexModel droitComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Compte le nombre d'enfants différents (id enfants liés au droits passés en paramètre)
     * 
     * @param listIdDroits
     *            <ArrayList<String>>
     * @return nombre d'enfants <int>
     * @throws JadeApplicationException
     *             Exception levée si le droit passé est <code>null</code> ou si la date n'est pas valide
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    int countEnfantsInDroitsList(List<String> listIdDroits) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Méthode enregistrant le droit et créant l'annonce rafam en conséquence
     * 
     * @param droit
     *            Le droit à créer
     * @param onlyDroitModel
     *            indique si il faut tenir compte du droitComplex ou seulement du droitModel dans la création du droit.
     *            true - tient compte seulement du droitModel (simpleModel) (pour gérer en cas de copie de dossier, si
     *            le même enfant se trouve dans plusieurs droits à copier)
     * @return le droit créé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    DroitComplexModel createDroitEtEnvoyeAnnoncesRafam(DroitComplexModel droit, boolean onlyDroitModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Contrôle si un droit est exportable selon les accords bilatéraux à une date donnée
     * 
     * @param droit
     *            le droit à contrôler
     * @param date
     *            la date à laquelle contrôler le droit (jj.mm.aaaa)
     * @return vrai ou faux
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    boolean ctrlDroitExportabilite(DroitComplexModel droit, String date) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Définit la date de début du droit en fonction de la naissance du tiers (dans <code>droitComplexModel</code>) et
     * <code>dateDebutDossier</code>
     * 
     * @param droitComplexModel
     *            Le droit pour lequel il faut définir la date de début
     * @param dateDebutDossier
     *            La date de début du dossier
     * @return droitComplexModel Le droit avec la date de début défini
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    DroitComplexModel defineDebutDroit(DroitComplexModel droitComplexModel, String dateDebutDossier)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode effaçant le droit et créant l'annonce rafam en conséquence
     * 
     * @param droit
     *            Le droit à supprimer
     * @return Le droit supprimé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    DroitComplexModel deleteDroit(DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Contrôle si un droit à un chevauchement de dates(même type de droit / même enfant), au sein du même dossier ou
     * non
     * 
     * @param droit
     * @param sameDossier
     *            - indique si le contrôler doit se cantonner au dossier du droit référence ou dans tous les dossiers
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    boolean hasChevauchementDate(DroitModel droit, boolean sameDossier) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Vérifie si le droit passé en paramètre a des prestations
     * 
     * @param idDroit
     *            id du droit à vérifier
     * 
     * @return <code>true</code> si le droit a des prestations
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    boolean hasPrestations(String idDroit) throws JadeApplicationException, JadePersistenceException;

    /**
     * Indique si <code>droit</code> est actif
     * 
     * Un droit est actif si :
     * <ul>
     * <li>il a l'état A ou G</li>
     * <li>la <code>date</code> est entre le début et la fin de validité du droit ou si la elle est après la date de
     * début de validité et que la date de fin n'est pas renseignée</li>
     * </ul>
     * 
     * @param droit
     *            Le droit à vérifier
     * @param date
     *            Date pour laquelle faire la vérification
     * @return <code>true</code> si le droit est actif, <code>false</code> sinon
     * 
     * @throws JadeApplicationException
     *             Exception levée si le droit passé est <code>null</code> ou si la date n'est pas valide
     * 
     * @see ch.globaz.al.business.models.droit.DroitModel
     * @see ch.globaz.al.business.constantes.ALCSDroit
     */
    boolean isDroitActif(DroitModel droit, String date) throws JadeApplicationException;

    /**
     * Vérifie si le droit est inactif avec calcul du rang
     * 
     * @param droit
     *            Le droit à vérifier
     * @return <code>true</code> si le droit est inactif avec calcul du rang, <code>false</code> sinon
     * 
     * @throws JadeApplicationException
     *             Exception levée si le droit passé est <code>null</code> ou si la date n'est pas valide
     */
    boolean isDroitInactif(DroitModel droit) throws ALDroitBusinessException, JadeApplicationException;

    /**
     * Détermine si l'échéance du droit dépasse la limite légale ou non (limite droit ENF : 16 ans, limite droit FORM :
     * 25 ans)
     * 
     * @param droit
     *            Le droit à contrôler
     * @return vrai si la limite est dépassée, faux sinon
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
     boolean isEcheanceOverLimiteLegale(DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Indique si un droit est en formation anticipée. Un droit est en formation anticipée s'il est de type formation (
     * {@link ch.globaz.al.business.constantes.ALCSDroit#TYPE_FORM}) et que la date de début de droit est antérieur à la
     * date calculé de fin de droit enfant
     * 
     * @param droit
     *            Droit à vérifier
     * @return <code>true</code> si le droit est en formation anticipée, <code>false</code> sinon
     * @throws JadeApplicationException
     */
     boolean isFormationAnticipee(DroitComplexModel droit) throws JadeApplicationException, JadePersistenceException;

    /**
     * Vérifie si le droit passé en paramètre a un montant forcé à zero
     * 
     * @param droit
     *            Droit à vérifier
     * @return <code>true</code> si le montant a été forcé à zero, <code>false</code> sinon
     */
     boolean isMontantForceZero(DroitModel droit);

    /**
     * Vérifie si un droit est de type ménage ou non
     * 
     * @param droit
     *            Le droit à vérifier
     * @return true si le droit est de type ménage
     * @throws JadeApplicationException
     *             Exception levée si <code>droit</code> est null
     * @see ch.globaz.al.business.models.droit.DroitModel
     */
     boolean isTypeMenage(DroitModel droit) throws JadeApplicationException;

    /**
     * Créé les annonces temporaires (état enregistré) et les affiche avec les existantes (les plus récentes)
     * 
     * @param values
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    List<AnnonceRafamModel> readWidget_apercuAnnoncesRafam(Map<?, ?> values)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime les annonces temporaires d'un droit défini
     * 
     * @param values
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
     void readWidget_deleteAnnoncesTemporaires(Map<?, ?> values) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Enregistre le recordNumber choisi sur les annonces temporaires (état enregistré) et les met en A_TRANSMETTRE pour
     * un droit
     * 
     * @param values
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    void readWidget_validerAnnoncesTemporaires(Map<?, ?> values) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Remet tous les droits en paiement indirects (plus de bénéficiaires par droit)
     * 
     * @param droit
     *            Le droit à modifier
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    void removeBeneficiaire(DroitModel droit) throws JadeApplicationException, JadePersistenceException;

    /**
     * Définit la date de fin de droit. La date de fin n'est mise à jour que si elle est vide dans le modèle passé en
     * paramètres. Aucune mise à jour en persistance n'est faite par cette méthode
     * 
     * @param model
     *            Modèle auquel la date de fin doit être ajoutée
     * @return le modèle avec la date de fin de droit.
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @see ch.globaz.al.business.services.echeances.DatesEcheanceService#getDateFinValiditeDroitCalculee
     */
     DroitComplexModel setDateFinDroitForce(DroitComplexModel model) throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode mettant a jour le droit et créant l'annonce rafam en conséquence
     * 
     * @param droit
     *            Le droit à mettre à jour
     * @return le droit mis à jour
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
     DroitComplexModel updateDroitEtEnvoyeAnnoncesRafam(DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException;
}
