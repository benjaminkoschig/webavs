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
 * Interface d�finissant les services m�tier impl�ment�s li�s au droit
 * 
 * @author GMO
 * 
 */
public interface DroitBusinessService extends JadeApplicationService {

    /**
     * Ajoute un ou des droit(s) du m�me type selon en fonction des "droits" existants selon l'�ge de l'enfant. Cette
     * m�thode est utilis�e dans les cas des cantons ayant un tarif progressif en fonction de l'�ge (ex : ZH)
     * 
     * @param droitComplexModel
     *            permet de r�cup�rer le droit � ajouter
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    void ajoutDroitMemeType(DroitComplexModel droitComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * @param nssAllocataire
     * @param nssEnfant
     * @return un avertissement d�finit dans <code>ALEnumMsgDroitPC</code> si l'enfant ou lallocataire percoit des
     *         prestations compl�mentaires.
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    ALEnumMsgDroitPC checkPCFamille(String nssAllocataire, String nssEnfant) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Cr�er un droit en copiant le droit correspondant au param�tre idDroitSource avec les dates pass�es en param�tres,
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
     * Cr�er un droit formation � partir d'un droit enfant
     * 
     * @param droitComplexModel
     *            Le droit r�f�rence enfant
     * @return Le droit formation r�sultant
     * @throws JadeApplicationException
     *             Exception lev�e si le droit pass� est <code>null</code> ou si la date n'est pas valide
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    DroitComplexModel copieToFormation(DroitComplexModel droitComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Compte le nombre d'enfants diff�rents (id enfants li�s au droits pass�s en param�tre)
     * 
     * @param listIdDroits
     *            <ArrayList<String>>
     * @return nombre d'enfants <int>
     * @throws JadeApplicationException
     *             Exception lev�e si le droit pass� est <code>null</code> ou si la date n'est pas valide
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    int countEnfantsInDroitsList(List<String> listIdDroits) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * M�thode enregistrant le droit et cr�ant l'annonce rafam en cons�quence
     * 
     * @param droit
     *            Le droit � cr�er
     * @param onlyDroitModel
     *            indique si il faut tenir compte du droitComplex ou seulement du droitModel dans la cr�ation du droit.
     *            true - tient compte seulement du droitModel (simpleModel) (pour g�rer en cas de copie de dossier, si
     *            le m�me enfant se trouve dans plusieurs droits � copier)
     * @return le droit cr��
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    DroitComplexModel createDroitEtEnvoyeAnnoncesRafam(DroitComplexModel droit, boolean onlyDroitModel)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Contr�le si un droit est exportable selon les accords bilat�raux � une date donn�e
     * 
     * @param droit
     *            le droit � contr�ler
     * @param date
     *            la date � laquelle contr�ler le droit (jj.mm.aaaa)
     * @return vrai ou faux
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    boolean ctrlDroitExportabilite(DroitComplexModel droit, String date) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * D�finit la date de d�but du droit en fonction de la naissance du tiers (dans <code>droitComplexModel</code>) et
     * <code>dateDebutDossier</code>
     * 
     * @param droitComplexModel
     *            Le droit pour lequel il faut d�finir la date de d�but
     * @param dateDebutDossier
     *            La date de d�but du dossier
     * @return droitComplexModel Le droit avec la date de d�but d�fini
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    DroitComplexModel defineDebutDroit(DroitComplexModel droitComplexModel, String dateDebutDossier)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode effa�ant le droit et cr�ant l'annonce rafam en cons�quence
     * 
     * @param droit
     *            Le droit � supprimer
     * @return Le droit supprim�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    DroitComplexModel deleteDroit(DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Contr�le si un droit � un chevauchement de dates(m�me type de droit / m�me enfant), au sein du m�me dossier ou
     * non
     * 
     * @param droit
     * @param sameDossier
     *            - indique si le contr�ler doit se cantonner au dossier du droit r�f�rence ou dans tous les dossiers
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    boolean hasChevauchementDate(DroitModel droit, boolean sameDossier) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * V�rifie si le droit pass� en param�tre a des prestations
     * 
     * @param idDroit
     *            id du droit � v�rifier
     * 
     * @return <code>true</code> si le droit a des prestations
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    boolean hasPrestations(String idDroit) throws JadeApplicationException, JadePersistenceException;

    /**
     * Indique si <code>droit</code> est actif
     * 
     * Un droit est actif si :
     * <ul>
     * <li>il a l'�tat A ou G</li>
     * <li>la <code>date</code> est entre le d�but et la fin de validit� du droit ou si la elle est apr�s la date de
     * d�but de validit� et que la date de fin n'est pas renseign�e</li>
     * </ul>
     * 
     * @param droit
     *            Le droit � v�rifier
     * @param date
     *            Date pour laquelle faire la v�rification
     * @return <code>true</code> si le droit est actif, <code>false</code> sinon
     * 
     * @throws JadeApplicationException
     *             Exception lev�e si le droit pass� est <code>null</code> ou si la date n'est pas valide
     * 
     * @see ch.globaz.al.business.models.droit.DroitModel
     * @see ch.globaz.al.business.constantes.ALCSDroit
     */
    boolean isDroitActif(DroitModel droit, String date) throws JadeApplicationException;

    /**
     * V�rifie si le droit est inactif avec calcul du rang
     * 
     * @param droit
     *            Le droit � v�rifier
     * @return <code>true</code> si le droit est inactif avec calcul du rang, <code>false</code> sinon
     * 
     * @throws JadeApplicationException
     *             Exception lev�e si le droit pass� est <code>null</code> ou si la date n'est pas valide
     */
    boolean isDroitInactif(DroitModel droit) throws ALDroitBusinessException, JadeApplicationException;

    /**
     * D�termine si l'�ch�ance du droit d�passe la limite l�gale ou non (limite droit ENF : 16 ans, limite droit FORM :
     * 25 ans)
     * 
     * @param droit
     *            Le droit � contr�ler
     * @return vrai si la limite est d�pass�e, faux sinon
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
     boolean isEcheanceOverLimiteLegale(DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Indique si un droit est en formation anticip�e. Un droit est en formation anticip�e s'il est de type formation (
     * {@link ch.globaz.al.business.constantes.ALCSDroit#TYPE_FORM}) et que la date de d�but de droit est ant�rieur � la
     * date calcul� de fin de droit enfant
     * 
     * @param droit
     *            Droit � v�rifier
     * @return <code>true</code> si le droit est en formation anticip�e, <code>false</code> sinon
     * @throws JadeApplicationException
     */
     boolean isFormationAnticipee(DroitComplexModel droit) throws JadeApplicationException, JadePersistenceException;

    /**
     * V�rifie si le droit pass� en param�tre a un montant forc� � zero
     * 
     * @param droit
     *            Droit � v�rifier
     * @return <code>true</code> si le montant a �t� forc� � zero, <code>false</code> sinon
     */
     boolean isMontantForceZero(DroitModel droit);

    /**
     * V�rifie si un droit est de type m�nage ou non
     * 
     * @param droit
     *            Le droit � v�rifier
     * @return true si le droit est de type m�nage
     * @throws JadeApplicationException
     *             Exception lev�e si <code>droit</code> est null
     * @see ch.globaz.al.business.models.droit.DroitModel
     */
     boolean isTypeMenage(DroitModel droit) throws JadeApplicationException;

    /**
     * Cr�� les annonces temporaires (�tat enregistr�) et les affiche avec les existantes (les plus r�centes)
     * 
     * @param values
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    List<AnnonceRafamModel> readWidget_apercuAnnoncesRafam(Map<?, ?> values)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Supprime les annonces temporaires d'un droit d�fini
     * 
     * @param values
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
     void readWidget_deleteAnnoncesTemporaires(Map<?, ?> values) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Enregistre le recordNumber choisi sur les annonces temporaires (�tat enregistr�) et les met en A_TRANSMETTRE pour
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
     * Remet tous les droits en paiement indirects (plus de b�n�ficiaires par droit)
     * 
     * @param droit
     *            Le droit � modifier
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    void removeBeneficiaire(DroitModel droit) throws JadeApplicationException, JadePersistenceException;

    /**
     * D�finit la date de fin de droit. La date de fin n'est mise � jour que si elle est vide dans le mod�le pass� en
     * param�tres. Aucune mise � jour en persistance n'est faite par cette m�thode
     * 
     * @param model
     *            Mod�le auquel la date de fin doit �tre ajout�e
     * @return le mod�le avec la date de fin de droit.
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @see ch.globaz.al.business.services.echeances.DatesEcheanceService#getDateFinValiditeDroitCalculee
     */
     DroitComplexModel setDateFinDroitForce(DroitComplexModel model) throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode mettant a jour le droit et cr�ant l'annonce rafam en cons�quence
     * 
     * @param droit
     *            Le droit � mettre � jour
     * @return le droit mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
     DroitComplexModel updateDroitEtEnvoyeAnnoncesRafam(DroitComplexModel droit) throws JadeApplicationException,
            JadePersistenceException;
}
