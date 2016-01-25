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
 * Divers service m�tier pour les dossiers.
 * 
 * @author PTA
 * 
 * @see ch.globaz.al.business.models.dossier.DossierModel
 * @see ch.globaz.al.business.models.droit.DroitComplexModel
 */
public interface DossierBusinessService extends JadeApplicationService {

    /**
     * Charge les dossiers actifs pour une date donn�e.
     * 
     * @param date Date � laquelle les dossiers doivent �tre actifs
     * @param idTiers tiers pour lequel les dossiers doivent �tre recherch�s
     * 
     * @return Les dossiers correspondant aux crit�res de recherche
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierComplexSearchModel getDossiersActifs(String date, String idTiersAllocataire)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Compte le nombre de dossiers actifs pour une date et un tiers donn�
     * 
     * @param date Date � laquelle les dossiers doivent �tre actifs
     * @param idTiers tiers pour lequel les dossiers doivent �tre compt�s
     * 
     * @return Nombre de dossiers trouv�s
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public int countDossiersActifs(String date, String idTiersAllocataire) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Ajoute un lien entre 2 dossiers. 2 add DB seront faits, le lien demand� et son inverse
     * 
     * 
     * @param idDossierPere
     *            Le dossier p�re, celui depuis lequel on cr�e le lien
     * 
     * @param idDossierPere
     *            Le dossier fils, celui choisi par l'utilisateur
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void ajouterLien(String idDossierPere, String idDossierFils, String typeLien)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Modifie le type de lien d'un lien selon la nouvelle valeur pass�e en param�tre
     * 
     * 
     * @param idLien
     *            l'identifiant du lien dont il faut modifier le type
     * @param newTypeLien
     *            le nouveau type de lien
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public void changerTypeLien(String idLien, String newTypeLien) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Copie un dossier dans son int�gralit� avec ses commentaires et ses droits. Dossier, commentaires et droits seront
     * des clones ( nouvelles instances)
     * 
     * Enregistre directement le dossier en DB, car il faut li� les copies des droits, commentaires,...
     * 
     * @param dossierComplexModel
     *            Le dossier r�f�rence pour la copie
     * @return le dossier r�sultant de la copie
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierComplexModel copierDossier(DossierComplexModel dossierComplexModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Copie un dossier dans son int�gralit� avec ses commentaires et ses droits. Dossier, commentaires et droits seront
     * des clones ( nouvelles instances)
     * 
     * Enregistre directement le dossier en DB, car il faut li� les copies des droits, commentaires,...
     * 
     * ATTENTION : Ne fait pas une v�ritable copie du dossier, il s'agit d'une copie par r�f�rence.
     * 
     * @param dossierComplexModel
     *            Le dossier r�f�rence pour la copie
     * @param etatNouveauDossier
     *            L'�tat que doit avoir le nouveau dossier
     * @return le dossier r�sultant de la copie
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierComplexModel copierDossier(DossierComplexModel dossierComplexModel, String etatNouveauDossier)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode cr�ant un nouveau dossier et la journalisation correspondante. Pas de gestion d'annonce Rafam car aucun
     * droit cr�� par ce service
     * 
     * @param dossier
     *            Le dossier � cr�er
     * @return Le dossier cr��
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierComplexModel createDossier(DossierComplexModel dossier) throws JadeApplicationException,
            JadePersistenceException;

    public DossierComplexModel createDossier(DossierComplexModel dossier, DossierAgricoleComplexModel dossierAgricole)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode effa�ant le dossier et cr�ant l'annonce rafam en cons�quence
     * 
     * @param dossier
     *            Le dossier � supprimer
     * @return Le dossier supprim�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierComplexModel deleteDossier(DossierComplexModel dossier) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Retourne la liste des activit�s qui sont obligatoirement li� � un affili� de type "Cot. Pers."
     * 
     * @return HashSet contenant les codes syst�mes des activit�s
     */
    public HashSet<String> getActivitesCategorieCotPers();

    /**
     * Retourne la liste des activit�s d'allocataire devant �tre trait� en fonction du type de cotisation
     * 
     * @param typeCoti
     *            type de cotisation trait�. Constante du groupe
     *            {@link ch.globaz.al.business.constantes.ALConstPrestations}
     * @return Liste des activit�s (codes syst�me)
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    public HashSet<String> getActivitesToProcess(String typeCoti) throws JadeApplicationException;

    /**
     * Retourne le mod�le complexe de dossier auquel est li� le <code>droitComplexModel</code>
     * 
     * @param droitComplexModel
     *            permet de r�cup�rer l'identifiant du dossier
     * @return dossierComplex mod�le complexe du dossier
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * Retourne le genre d'assurance en fonction du type d'activit� de l'allocataire
     * 
     * @param activiteAllocataire
     *            Activit� de l'allocataire
     * @return Genre d'assurance
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * 
     * @see ch.globaz.al.business.constantes.ALCSDossier#GROUP_ACTIVITE_ALLOC
     * @see ch.globaz.al.business.constantes.ALCSAffilie#GENRE_ASSURANCE_INDEP
     * @see ch.globaz.al.business.constantes.ALCSAffilie#GENRE_ASSURANCE_PARITAIRE
     */
    public String getGenreAssurance(String activiteAllocataire) throws JadeApplicationException;

    /**
     * R�cup�re la liste des num�ros de dossiers actifs pour un allocataire et un affili�
     * 
     * @param nssAllocataire
     *            NSS de l'allocataire pour lequel rechercher les dossiers
     * @param idAffilie
     *            id de l'affiliation. Param�tre facultatif, s'il n'est pas indiqu�, tous les dossiers actifs de
     *            l'allocataire seront retourn�s
     * @return Liste des num�ros de dossier
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     * 
     */
    public JadeAbstractModel[] getIdDossiersActifs(String nssAllocataire, String idAffilie)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Retourne le lien inverse du lien pass� en param�tre Exemple lien original => dossier p�re : 12451, dossier fils :
     * 521 ==> lien inverse retourn� => dossier p�re : 521, dossier fils: 12451
     * 
     * @param lienOriginal
     * @return lien inverse
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public LienDossierModel getLienInverse(LienDossierModel lienOriginal) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * M�thode permettant de retrouver les dossier en fonction affilie� et allocataire
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
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public String getTypeCotisation(DossierModel dossierModel) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * V�rifie si le dossier dont l'id est pass� en param�tre a des annonces RAFam envoy�es
     * 
     * @param idDossier
     *            dossier pour lequel v�rifier l'existance d'annonces
     * @return <code>true</code> si des annonces ont �t� transmies, <code>false</code> dans le cas contraire
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean hasSentAnnonces(String idDossier) throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode v�rifiant si le tiers correspond � l'affili� d'un dossier
     * 
     * @param dossier
     *            le dossier dont on veut v�rifier l'affili�
     * @param idTiers
     *            l'id du tiers
     * @return vrai si le tiers correspond � l'affili�, faux sinon
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isAffilie(DossierComplexModel dossier, String idTiers) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * V�rifie si <code>idTiers</code> correspond � une agence communale
     * 
     * @param idTiers
     *            id tiers � v�rifier
     * @return <code>true</code> si <code>idTiers</code> est une agence communale
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public boolean isAgenceCommunale(String idTiers) throws JadeApplicationException, JadePersistenceException;

    /**
     * V�rifie si le type d'activit� <code>typeActivite</code> correspond � une activit� agricole
     * 
     * @param typeActivite
     *            Code syst�me de l'activit�
     * @return <code>true</code> si <code>typeActivite</code> correspond a une activit� agricole
     * @throws JadeApplicationException
     *             Exception lev�e si <code>typeActivite</code> est <code>null</code>
     */
    public boolean isAgricole(String typeActivite) throws JadeApplicationException;

    /**
     * M�thode v�rifiant si le tiers correspond � l'allocataire d'un dossier
     * 
     * @param dossier
     *            le dossier dont on veut v�rifier l'allocataire
     * @param idTiers
     *            l'id du tiers
     * @return vrai si le tiers correspond � l'allocataire, faux sinon
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public boolean isAllocataire(DossierComplexModel dossier, String idTiers) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * D�termine si le dossier est en paiement direct.
     * 
     * Un dossier est en paiement direct si
     * {@link ch.globaz.al.business.models.dossier.DossierFkModel#getIdTiersBeneficiaire()} est non <code>null</code> et
     * a une valeur supp�rieure � 0
     * 
     * @param dossier
     *            Le dossier � v�rifier
     * @return <code>true</code> si le dossier est en paiement direct, <code>false</code> sinon
     * @throws JadeApplicationException
     *             Exception lev�e si <code>typeActivite</code> est <code>null</code>
     */
    public boolean isModePaiementDirect(DossierModel dossier) throws JadeApplicationException;

    /**
     * V�rifie si le dossier est dans un contexte paritaire ou personnel
     * 
     * @param typeActivite
     *            type d'activit� de l'allocataire
     * @return true : le dossier est paritaire, sinon personnel
     * @throws JadeApplicationException
     *             Exception lev�e si <code>typeActivite</code> est <code>null</code>
     */
    public boolean isParitaire(String typeActivite) throws JadeApplicationException;

    /**
     * Service retournant les diff�rents valeurs du champ �tat dossier ne bloquant pas la g�n�ration de prestation lors
     * de la g�n�ration globale (selon le param�trage d�fini dans ALPARAM)
     * 
     * @return liste des �tats
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public Set<String> listerEtatsOkGenerationGlobale() throws JadeApplicationException, JadePersistenceException;

    /**
     * Service retournant les �tats d'un dossier permettant la journalisation du changement d'�tat (selon le param�trage
     * d�fini dans ALPARAM)
     * 
     * @return liste des �tats
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public Set<String> listerEtatsOkJournalise() throws JadeApplicationException, JadePersistenceException;

    /**
     * Service retournant les �tats d'un dossier permettant la cr�ation d'annonces RAFAM lors de mutation au sein d'un
     * dossier (selon le param�trage d�fini dans ALPARAM)
     * 
     * @return liste des �tats
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public Set<String> listerEtatsOkRafam() throws JadeApplicationException, JadePersistenceException;

    /**
     * D�finit le nombre de jours de d�but du dossier en fonction d'une date pass�e
     * 
     * @param dateDebutDossier
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public String nbreJourDebutMoisAF(String dateDebutDossier) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * D�finit le nombre de jours de fin du dossier en fonction d'une date pass�e
     * 
     * @param dateFinDossier
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public String nbreJourFinMoisAF(String dateFinDossier) throws JadePersistenceException, JadeApplicationException;

    /**
     * Radie le dossier pass� en param�tre et le met � jour en persistance
     * 
     * @param dossier
     *            le dossier � radier
     * @param dateRadiation
     *            date de radiation
     * @param updateNbJoursFin
     *            indique si il faut mettre � jour le nombre de jours de fin selon date radiation
     * @param remarqueJournalisation
     *            La remarque associ� � la journalisation du changement d'�tat
     * @return le dossier mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierModel radierDossier(DossierModel dossier, String dateRadiation, boolean updateNbJoursFin,
            String remarqueJournalisation) throws JadeApplicationException, JadePersistenceException;

    /**
     * Radie le dossier pass� en param�tre et le met � jour en persistance
     * 
     * @param dossier
     *            le dossier � radier
     * @param dateRadiation
     *            date de radiation
     * @param updateNbJoursFin
     *            indique si il faut mettre � jour le nombre de jours de fin selon date radiation
     * @param remarqueJournalisation
     *            La remarque associ� � la journalisation du changement d'�tat
     * @param reference
     *            Nouvelle personne de r�f�rence
     * @return le dossier mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierModel radierDossier(DossierModel dossier, String dateRadiation, boolean updateNbJoursFin,
            String remarqueJournalisation, String reference) throws JadeApplicationException, JadePersistenceException;

    /**
     * Service �crit pour la radiation des dossier Horloger lors de l'entr�e en vigueur des 30.- horloger. Ne pas
     * utiliser ce service dans d'autres cas
     * 
     * @param dossier
     * @return
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     * @deprecated Service �crit pour la radiation des dossier Horloger lors de l'entr�e en vigueur des 30.- horloger.
     *             Ne pas utiliser ce service dans d'autres cas
     */
    @Deprecated
    public DossierModel radierDossierHorloger(DossierModel dossier, String dateRadiation)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Variante du service <code>nbreJourDebutMoisAF</code> @see nbreJourDebutMoisAF, appelable depuis composant widget
     * 
     * @param values
     * @return la nombre de jour calcul�e (-1 : date entr�e invalide)
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String readWidget_nbreJourDebutMoisAF(HashMap<?, ?> values) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Variante du service <code>nbreJourFinMoisAF</code> @see nbreJourFinMoisAF, appelable depuis composant widget
     * 
     * @param values
     * @return le nombre de jour calcul� (-1 : date entr�e invalide)
     * @throws JadeApplicationException
     * @throws JadePersistenceException
     */
    public String readWidget_nbreJourFinMoisAF(HashMap<?, ?> values) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Retirer le tiers b�n�ficiaire du dossier. Ce service ne g�re (volontairement) pas la persistance, il faudra
     * sauvegarder le dossierComplexModel apr�s coup si n�cessaire
     * 
     * @param dossierComplexModel
     *            Le dossier � modifier
     * @param keepDirect
     *            <code>true</code> : le b�n�ficiaire du dossier devient l'allocataire, <code>false</code> : employeur
     *            (paiement indirect)
     * @return Le dossier modifi�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
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
     *            num�ro de l'affilie, numNss num�ro di nss
     * @return les dossiers trouv�es par la recherche
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierAttestationVersementComplexSearchModel searchDossierAttestation(String numAffilie, String numNss)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode mettant a jour le dossier et cr�ant l'annonce rafam en cons�quence, et la journalisation si changement
     * d'�tat
     * 
     * @param dossier
     *            Le dossier � mettre � jour
     * @param etatDossierAvantModif
     *            L'�tat du dossier avant la modification du mod�le (�tat en DB). si null, journalisation cr��e pour
     *            indiquer changement �tat avec nouvelle valeur (mod�le en param�tre)
     * @param remarqueJournalisation
     *            La remarque associ� � la journalisation du changement d'�tat
     * @return Le dossier mis � jour
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public DossierComplexModel updateDossier(DossierComplexModel dossier, String etatDossierAvantModif,
            String remarqueJournalisation) throws JadeApplicationException, JadePersistenceException;

    public DossierComplexModel updateDossier(DossierComplexModel dossier, String etatDossierAvantModif,
            String remarqueJournalisation, DossierAgricoleComplexModel dossierAgricole)
            throws JadeApplicationException, JadePersistenceException;

}