package ch.globaz.vulpecula.domain.repositories.decompte;

import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.repositories.Repository;
import ch.globaz.vulpecula.repositoriesjade.decompte.DecompteRepositoryJade.LoadOptions;

/**
 * Repository des décomptes
 */

public interface DecompteRepository extends Repository<Decompte> {
    List<Decompte> findByIdInWithDependencies(Collection<String> ids);

    Decompte findByIdWithDependencies(String idDecompte);

    List<Decompte> findDecompteByNoDecompte(String noDecompte);

    Decompte findFirstDecompteByNoDecompte(String noDecompte);

    List<Decompte> findAll();

    List<Decompte> findByIdEmployeur(String idEmployeur);

    List<Decompte> findOuvertGenereATraiterByIdEmployeur(String idEmployeur, Date date);

    List<Decompte> findOuvertGenereATraiterByIdEmployeurPeriode(String idEmployeur, PeriodeMensuelle periode);

    List<Decompte> findByIdEmployeurOrderByPeriodeDebutDesc(final String idEmployeur, String idDecompte,
            String numeroDecompte, String type);

    /**
     * Recherche tous les décomptes d'un employeur avec les paramètres de filtres
     * 
     * @param idEmployeur Id de l'employeur (idAffilie) sur lequel rechercher les décomptes
     * @param idDecompte -> Id du décompte sur lequel filtrer
     * @param numeroDecompte -> Numéro du décompte sur lequel filtrer
     * @param type -> type de décompte à prendre en compte
     * @return Liste de décomptes
     */
    List<Decompte> findByIdEmployeur(String idEmployeur, String idDecompte, String numeroDecompte, String type);

    /**
     * Recherche tous les décomptes d'un employeur dont la date de début commence après la date passée en paramètre.
     * 
     * @param idEmployeur Id de l'employeur (idAffilie) sur lequel rechercher les décomptes
     * @param date Date à laquelle prendre les décomptes
     * @return Liste de décomptes
     */
    List<Decompte> findByIdEmployeur(String idEmployeur, Date date);

    List<Decompte> findDecomptesForFacturation(Date dateLimite);

    List<Decompte> findDecomptesForIdPassage(String idPassage);

    List<Decompte> findRectificatifs();

    /**
     * Retourne la liste des décomptes à potentiellement transitionner en état SOMMATION.
     */
    List<Decompte> findDecomptesForSommation();

    List<Decompte> findDecomptesForTaxationOffice();

    /**
     * Retourne le décompte périodique précédent le décompte passé en paramètre.
     * 
     * @param decompte Decompte à partir duquel le décompte doit être déduit
     * @return Le décompte précédent ou null si celui-ci est inexistant
     */
    Decompte findDecomptePrecedent(Decompte decompte);

    Decompte findDecompteForAnnee(Employeur employeur, Annee annee, TypeDecompte type);

    Decompte findDecompteIdentiqueGenere(Employeur employeur, PeriodeMensuelle periodeMensuelle, TypeDecompte type);

    Decompte findDecompteIdentiqueEBusiness(Employeur employeur, PeriodeMensuelle periodeMensuelle, TypeDecompte type);

    List<Decompte> findByIdEmployeurWithDependencies(String idEmployeur);

    List<Decompte> findBy(String idDecompte, String numeroDecompte, String noAffilie, String idPassage,
            EtatDecompte etatDecompte);

    List<Decompte> findBy(String idDecompte, String numeroDecompte, String noAffilie, String idPassage,
            String etatsDecomptesSeparatedByComma);

    /**
     * Retourne les décomptes ainsi que leurs dépendances selon les critères ci-dessus.
     * Chaque champ peut être null.
     * 
     * @return Liste de décomptes ou liste vide
     */
    List<Decompte> findWithDependencies(String idEmployeur, String idDecompte, String numeroDecompte, String type);

    List<Decompte> findByIdEmployeurAndPeriodeWithDependencies(String idEmployeur, String dateDebut, String dateFin);

    List<Decompte> findByIdEmployeurAndPeriodeWithDependenciesOrderByTravailleur(String idEmployeur, String dateDebut,
            String dateFin);

    List<Decompte> findByIdEmployeurAndPeriodeWithDependencies(String idEmployeur, String dateDebut, String dateFin,
            EtatDecompte etat);

    /**
     * Ignore les décomptes de type "Spéciale Caisse" TypeDecompte.SPECIAL_CAISSE
     * 
     * @param idEmployeur
     * @param dateDebut
     * @param dateFin
     * @param etat
     * @param options
     * @return
     */
    List<Decompte> findByIdEmployeurAndPeriode(String idEmployeur, String dateDebut, String dateFin, EtatDecompte etat,
            LoadOptions... options);

    List<Decompte> findComplementaireByIdEmployeurAndPeriode(String idEmployeur, Annee annee);

    List<Decompte> findComplementaireByIdEmployeurAndPeriodeWithDependencies(String idEmployeur, Annee annee);

    List<Decompte> findByIdEmployeurForPeriode(String idEmployeur, PeriodeMensuelle periode);

    /**
     * Retourne les décomptes d'un employeur pour une année comptable.
     * 
     * @param idEmployeur String représentant l'id d'un employeur
     * @param anneeComptable Année comptable
     * @return Liste de décomptes
     */
    List<Decompte> findByIdEmployeurForAnneeComptable(String idEmployeur, Date periodeDebut, Date periodeFin);

    List<Decompte> findByIdEmployeurAndPeriodeForCPP(String idEmployeur, String dateDebut, String dateFin,
            EtatDecompte etat, LoadOptions... options);

    List<Decompte> findByIdEmployeurAndPeriodeForCPPComplementaire(String idEmployeur, String dateDebut,
            String dateFin, EtatDecompte etat, LoadOptions... options);

}
