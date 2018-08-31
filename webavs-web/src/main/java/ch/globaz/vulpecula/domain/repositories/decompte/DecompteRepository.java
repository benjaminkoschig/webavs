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
 * Repository des d�comptes
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
     * Recherche tous les d�comptes d'un employeur avec les param�tres de filtres
     * 
     * @param idEmployeur Id de l'employeur (idAffilie) sur lequel rechercher les d�comptes
     * @param idDecompte -> Id du d�compte sur lequel filtrer
     * @param numeroDecompte -> Num�ro du d�compte sur lequel filtrer
     * @param type -> type de d�compte � prendre en compte
     * @return Liste de d�comptes
     */
    List<Decompte> findByIdEmployeur(String idEmployeur, String idDecompte, String numeroDecompte, String type);

    /**
     * Recherche tous les d�comptes d'un employeur dont la date de d�but commence apr�s la date pass�e en param�tre.
     * 
     * @param idEmployeur Id de l'employeur (idAffilie) sur lequel rechercher les d�comptes
     * @param date Date � laquelle prendre les d�comptes
     * @return Liste de d�comptes
     */
    List<Decompte> findByIdEmployeur(String idEmployeur, Date date);

    List<Decompte> findDecomptesForFacturation(Date dateLimite);

    List<Decompte> findDecomptesForIdPassage(String idPassage);

    List<Decompte> findRectificatifs();

    /**
     * Retourne la liste des d�comptes � potentiellement transitionner en �tat SOMMATION.
     */
    List<Decompte> findDecomptesForSommation();

    List<Decompte> findDecomptesForTaxationOffice();

    /**
     * Retourne le d�compte p�riodique pr�c�dent le d�compte pass� en param�tre.
     * 
     * @param decompte Decompte � partir duquel le d�compte doit �tre d�duit
     * @return Le d�compte pr�c�dent ou null si celui-ci est inexistant
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
     * Retourne les d�comptes ainsi que leurs d�pendances selon les crit�res ci-dessus.
     * Chaque champ peut �tre null.
     * 
     * @return Liste de d�comptes ou liste vide
     */
    List<Decompte> findWithDependencies(String idEmployeur, String idDecompte, String numeroDecompte, String type);

    List<Decompte> findByIdEmployeurAndPeriodeWithDependencies(String idEmployeur, String dateDebut, String dateFin);

    List<Decompte> findByIdEmployeurAndPeriodeWithDependenciesOrderByTravailleur(String idEmployeur, String dateDebut,
            String dateFin);

    List<Decompte> findByIdEmployeurAndPeriodeWithDependencies(String idEmployeur, String dateDebut, String dateFin,
            EtatDecompte etat);

    /**
     * Ignore les d�comptes de type "Sp�ciale Caisse" TypeDecompte.SPECIAL_CAISSE
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
     * Retourne les d�comptes d'un employeur pour une ann�e comptable.
     * 
     * @param idEmployeur String repr�sentant l'id d'un employeur
     * @param anneeComptable Ann�e comptable
     * @return Liste de d�comptes
     */
    List<Decompte> findByIdEmployeurForAnneeComptable(String idEmployeur, Date periodeDebut, Date periodeFin);

    List<Decompte> findByIdEmployeurAndPeriodeForCPP(String idEmployeur, String dateDebut, String dateFin,
            EtatDecompte etat, LoadOptions... options);

    List<Decompte> findByIdEmployeurAndPeriodeForCPPComplementaire(String idEmployeur, String dateDebut,
            String dateFin, EtatDecompte etat, LoadOptions... options);

}
