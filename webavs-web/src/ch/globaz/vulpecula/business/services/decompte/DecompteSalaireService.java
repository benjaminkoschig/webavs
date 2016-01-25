package ch.globaz.vulpecula.business.services.decompte;

import java.util.Deque;
import java.util.Map;
import ch.globaz.exceptions.GlobazBusinessException;
import ch.globaz.specifications.UnsatisfiedSpecificationException;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Montant;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public interface DecompteSalaireService {
    /**
     * Mise à jour du décompte salaire et de ses absences associées.
     * Dans le cas où le décompte associé à la ligne de salaire est en contrôle AC2, on va récupérer le total des masses
     * AC et regarder si l'on dépasse le plafond. Dans le cas où il est dépassé, on va placer la différence sur l'AC2.
     * 
     * @param decompteSalaire Décompte salaire à mettre à jour
     * @return DecompteSalaire avec son spy mis à jour
     * @throws UnsatisfiedSpecificationException
     */
    DecompteSalaire update(DecompteSalaire decompteSalaire) throws UnsatisfiedSpecificationException;

    DecompteSalaire update(DecompteSalaire decompteSalaire, Montant montantAC2)
            throws UnsatisfiedSpecificationException;

    /**
     * Création d'un nouveau décompte salaire et de ses absences associées.
     * 
     * @param decompteSalaire Decompte salaire à créer
     * @return DecompteSalaire avec son id et son spy
     * @throws UnsatisfiedSpecificationException
     * @throws GlobazBusinessException
     */
    DecompteSalaire create(DecompteSalaire decompteSalaire) throws UnsatisfiedSpecificationException;

    DecompteSalaire create(DecompteSalaire decompteSalaire, Montant montantAC2)
            throws UnsatisfiedSpecificationException;

    /**
     * Recherche du précédent décompte salaire comptabilisé pour l'id décompte passé en paramètre. Le dernier décompte
     * comptabilisé sera recherché avant la date de recherche.
     * 
     * @param idPosteTravail Id du poste de travail
     * @param dateRecherche Date à partir de laquelle rechercher
     * @return Le dernier décompte salaire pour le poste de travail en prenant en compte la date de rechercher, ou null
     *         si inexistant.
     */
    DecompteSalaire findPrecedentComptabilise(String idPosteTravail, Date dateRecherche);

    /**
     * Suppression du décompte salaire et de ses absences associées.
     * 
     * @param decompteSalaire Decompte salaire à supprimer
     */
    void delete(DecompteSalaire decompteSalaire) throws UnsatisfiedSpecificationException;

    /**
     * Maj du champ date annonce d'une liste de décompte salaire.
     * utilisé lors de la communication des salaires à la MEROBA
     */
    void majDateAnnonceSalaire(Deque<DecompteSalaire> listeDecompteSalaire);

    /**
     * Défini si le poste de travail du décompte salaire est rentier AVS
     * 
     * @param idDecompteLigne, Date date : la date à laquelle ont veut tester si le travailleur est rentier
     * @throws Exception
     */
    boolean isPosteTravailRentier(String idDecompteLigne, Date date) throws Exception;

    boolean isPosteTravailRentier(DecompteSalaire decompteSalaire);

    /**
     * Retourne le cumul des salaires entre deux périodes pour un travailleur.
     * Seuls les décomptes salaires correspondant à l'id de la caisse métier passé en paramètre sont pris en compte pour
     * élaborer le cumul.
     * 
     * @param idTravailleur id du travailleur sur lequel calculer le cumul salaires
     * @param idCaisseMetier id de la caisse métier correspondant en réalité à l'id du tiers administration
     * @param dateDebut Date de début
     * @param dateFin Date de fin
     * @return Cumul des salaires
     */
    Montant cumulSalaire(String idTravailleur, String idCaisseMetier, Date dateDebut, Date dateFin);

    /**
     * Retourne le cumul des salaires entre deux périodes pour un travailleur.
     * 
     * @param idTravailleur id du travailleur sur lequel calculer le cumul salaires
     * @param dateDebut Date de début
     * @param dateFin Date de fin
     * @return Cumul des salaires
     */
    Montant cumulSalaire(String idTravailleur, Date dateDebut, Date dateFin);

    /**
     * Retourne le cumul des montants sur une période allant de dateDebut à dateFin pour un travailleur.
     * 
     * @param idTravailleur
     * @param dateDebut
     * @param dateFin
     * @return Map contenant un caisse métier et son montant
     */
    Map<Administration, Montant> findMontantsByCaisseMetier(String idTravailleur, Date dateDebut, Date dateFin);

    /**
     * Va rechercher la 1ère ligne de salaire du décompte, afin de pouvoir l'afficher, en cas d'arrivage par l'écran des
     * décomptes
     * (écran de saisie rapide des décomptes)
     */
    String findFirstRowOfLigneForDecompte(String idDecompte);

    /**
     * @param idDecompteSalaire
     * @return true si les cotisations du décompte salaire correspondent à celles du poste de travail
     */
    boolean checkCotisationDecompte(String idDecompteSalaire);

    /**
     * Retourne un décompte salaire selon son id et applique les adaptations sur ses cotisations :
     * <ul>
     * <li>Gestion de l'AC/AC2
     * <li>Plafonnement pour les rentiers (application de la franchise sur certaines cotisations)
     * </ul>
     * 
     * @param idDecompteSalaire id du décompte salaire sur lequel effectué les adaptations
     * @param handleAC définit si l'on doit simuler l'AC2 sur ce décompte
     * @param masseSalariale la masse salariale sur lequel les cotisations seront calculées
     * @param masseAC2 masse AC2 forcée, indépendant du paramètre handleAC. Si null, on n'effectue pas d'adaptation sur
     *            l'AC2
     * @return
     */
    DecompteSalaire findByIdForSimulation(String idDecompteSalaire, boolean handleAC, Montant masseSalariale,
            Montant masseAC2, Montant masseFranchise);

    DecompteSalaire findById(String idDecompteSalaire);
}
