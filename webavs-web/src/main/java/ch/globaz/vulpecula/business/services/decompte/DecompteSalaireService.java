package ch.globaz.vulpecula.business.services.decompte;

import java.util.Deque;
import java.util.List;
import java.util.Map;
import ch.globaz.exceptions.GlobazBusinessException;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.businessimpl.services.decompte.DifferenceCotisationSalaire;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.external.models.pyxis.Administration;
import ch.globaz.vulpecula.web.views.decomptesalaire.DecompteSalaireDetailsAnnuelView;
import globaz.jade.exception.JadePersistenceException;

public interface DecompteSalaireService {
    /**
     * Mise � jour du d�compte salaire et de ses absences associ�es.
     * Dans le cas o� le d�compte associ� � la ligne de salaire est en contr�le AC2, on va r�cup�rer le total des masses
     * AC et regarder si l'on d�passe le plafond. Dans le cas o� il est d�pass�, on va placer la diff�rence sur l'AC2.
     *
     * @param decompteSalaire D�compte salaire � mettre � jour
     * @return DecompteSalaire avec son spy mis � jour
     * @throws UnsatisfiedSpecificationException
     */
    DecompteSalaire update(DecompteSalaire decompteSalaire)
            throws UnsatisfiedSpecificationException, JadePersistenceException;

    DecompteSalaire update(DecompteSalaire decompteSalaire, Montant montantAC2)
            throws UnsatisfiedSpecificationException, JadePersistenceException;

    /**
     * Cr�ation d'un nouveau d�compte salaire et de ses absences associ�es.
     *
     * @param decompteSalaire Decompte salaire � cr�er
     * @return DecompteSalaire avec son id et son spy
     * @throws UnsatisfiedSpecificationException
     * @throws GlobazBusinessException
     */
    DecompteSalaire create(DecompteSalaire decompteSalaire) throws UnsatisfiedSpecificationException;

    /**
     * Cr�ation d'une ligne de d�compte sans poste de travail.
     * Cas de figure : nouveau d�compte en provenance du portail avec un nouveau travailleur pas encore renseign� dans
     * WebM�tier
     *
     * @param decompteSalaire
     * @return
     * @throws UnsatisfiedSpecificationException
     */
    DecompteSalaire createWithoutPosteTravail(DecompteSalaire decompteSalaire) throws UnsatisfiedSpecificationException;

    DecompteSalaire create(DecompteSalaire decompteSalaire, Montant montantAC2)
            throws UnsatisfiedSpecificationException;

    /**
     * Recherche du pr�c�dent d�compte salaire comptabilis� pour l'id d�compte pass� en param�tre. Le dernier d�compte
     * comptabilis� sera recherch� avant la date de recherche.
     *
     * @param idPosteTravail Id du poste de travail
     * @param dateRecherche Date � partir de laquelle rechercher
     * @return Le dernier d�compte salaire pour le poste de travail en prenant en compte la date de rechercher, ou null
     *         si inexistant.
     */
    DecompteSalaire findPrecedentComptabilise(String idPosteTravail, Date dateRecherche);

    /**
     * Recherche du pr�c�dent d�compte salaire valid� (�tat : comptabilis�, valide, rectifi�, factur�) pour l'id
     * d�compte pass� en param�tre. Le dernier d�compte
     * comptabilis� sera recherch� avant la date de recherche.
     *
     * @param idPosteTravail
     * @param dateRecherche
     * @return
     */
    DecompteSalaire findPrecedentValide(String idPosteTravail, Date dateRecherche);

    /**
     * Suppression du d�compte salaire et de ses absences associ�es.
     *
     * @param decompteSalaire Decompte salaire � supprimer
     */
    void delete(DecompteSalaire decompteSalaire) throws UnsatisfiedSpecificationException;

    /**
     * Maj du champ date annonce d'une liste de d�compte salaire.
     * utilis� lors de la communication des salaires � la MEROBA
     */
    void majDateAnnonceSalaire(Deque<DecompteSalaire> listeSalaires);

    /**
     * D�fini si le poste de travail du d�compte salaire est rentier AVS
     *
     * @param idDecompteLigne, Date date : la date � laquelle ont veut tester si le travailleur est rentier
     * @throws Exception
     */
    boolean isPosteTravailRentier(String idDecompteLigne, Date date) throws Exception;

    boolean isPosteTravailRentier(DecompteSalaire decompteSalaire);

    /**
     * Retourne le cumul des salaires entre deux p�riodes pour un travailleur.
     * Seuls les d�comptes salaires correspondant � l'id de la caisse m�tier pass� en param�tre sont pris en compte pour
     * �laborer le cumul.
     *
     * @param idTravailleur id du travailleur sur lequel calculer le cumul salaires
     * @param idCaisseMetier id de la caisse m�tier correspondant en r�alit� � l'id du tiers administration
     * @param dateDebut Date de d�but
     * @param dateFin Date de fin
     * @return Cumul des salaires
     */
    Montant cumulSalaire(String idTravailleur, String idCaisseMetier, Date dateDebut, Date dateFin);

    /**
     * Retourne le cumul des salaires entre deux p�riodes pour un travailleur.
     *
     * @param idTravailleur id du travailleur sur lequel calculer le cumul salaires
     * @param dateDebut Date de d�but
     * @param dateFin Date de fin
     * @return Cumul des salaires
     */
    Montant cumulSalaire(String idTravailleur, Date dateDebut, Date dateFin);

    /**
     * Retourne le cumul des salaires qui ont des cotisations CPR entre deux p�riodes pour un travailleur.
     *
     * @param idTravailleur id du travailleur sur lequel calculer le cumul salaires
     * @param dateDebut Date de d�but
     * @param dateFin Date de fin
     * @return Cumul des salaires
     */
    Montant cumulSalaireSyndicatWithCotisationsCPR(String idTravailleur, Date dateDebut, Date dateFin);

    /**
     * Retourne le cumul des montants sur une p�riode allant de dateDebut � dateFin pour un travailleur.
     *
     * @param idTravailleur
     * @param dateDebut
     * @param dateFin
     * @param isListeTravailleurPaiementSyndicat
     * @return Map contenant un caisse m�tier et son montant
     */
    Map<Administration, Montant> findMontantsByCaisseMetier(String idTravailleur, Date dateDebut, Date dateFin);

    /**
     * Va rechercher la 1�re ligne de salaire du d�compte, afin de pouvoir l'afficher, en cas d'arrivage par l'�cran des
     * d�comptes
     * (�cran de saisie rapide des d�comptes)
     */
    String findFirstRowOfLigneForDecompte(String idDecompte);

    /**
     * @param idDecompteSalaire String repr�sentant l'id d'une cotisation
     * @param annee Ann�e � laquelle prendre en compte les cotisations (optionel)
     * @return DifferenceCotisationSalaire si les cotisations du d�compte salaire correspondent � celles du poste de
     *         travail
     */
    DifferenceCotisationSalaire checkCotisationDecompte(String idDecompteSalaire, Annee annee);

    /**
     * Retourne un d�compte salaire selon son id et applique les adaptations sur ses cotisations :
     * <ul>
     * <li>Gestion de l'AC/AC2
     * <li>Plafonnement pour les rentiers (application de la franchise sur certaines cotisations)
     * </ul>
     *
     * @param idDecompteSalaire id du d�compte salaire sur lequel effectu� les adaptations
     * @param handleAC d�finit si l'on doit simuler l'AC2 sur ce d�compte
     * @param masseSalariale la masse salariale sur lequel les cotisations seront calcul�es
     * @param masseAC2 masse AC2 forc�e, ind�pendant du param�tre handleAC. Si null, on n'effectue pas d'adaptation sur
     *            l'AC2
     * @return
     */
    DecompteSalaire findByIdForSimulation(String idDecompteSalaire, boolean handleAC, Montant masseSalariale,
            Montant masseAC2, Montant masseFranchise);

    DecompteSalaire findById(String idDecompteSalaire);

    DecompteSalaire createWithoutCotisations(DecompteSalaire decompteSalaire) throws UnsatisfiedSpecificationException;

    DecompteSalaire createWithoutCotisations(DecompteSalaire decompteSalaire, Montant montantAC2)
            throws UnsatisfiedSpecificationException;

    /**
     * Retourne les d�tails salariales d'un travailleur pour une p�riode donn�es avec ses cotisations pour un syndicat
     * Seules les d�comptes salaires comptabilis�es sont prises en compte.
     *
     * @param idTravailleur
     * @param idSyndicat
     * @param dateDebut Date de d�but de la p�riode d'affiliation syndicat
     * @param dateFin Date de fin de la p�riode d'affiliation syndicat (date d�fini ou date d'aujourd'hui)
     * @return List<DecompteSalaireDetailsAnnuelView> Liste contenant les d�tails salariales par ann�e
     */
    List<DecompteSalaireDetailsAnnuelView> calculDetailsDecompteSalaire(String id, String idSyndicat, Date date,
            Date dateFin);

    /**
     * Retourne les d�comptes salaires qui poss�dent des cotisations CPR bas� sur la p�riode des d�comptes salaires
     *
     * @param decomptes
     * @param dateDebut
     * @param dateFin
     * @return List<DecompteSalaire>
     */
    List<DecompteSalaire> findDecomptesInPeriodeWithCotisationCPR(List<DecompteSalaire> decomptes, Date dateDebut,
            Date dateFin);

    /**
     * Retourne les d�comptes salaires qui poss�dent des cotisations CPR bas� sur la p�riode des cotisations
     *
     * @param decomptes
     * @param dateDebut
     * @param dateFin
     * @return List<DecompteSalaire>
     */
    List<DecompteSalaire> findDecomptesWithCotisationCPRInPeriode(List<DecompteSalaire> decomptesSalaire,
            Date dateDebut, Date dateFin);

}
