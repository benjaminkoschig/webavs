package ch.globaz.vulpecula.domain.repositories.decompte;

import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;
import ch.globaz.vulpecula.domain.models.decompte.EtatDecompte;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.repositories.Repository;

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

    /**
     * Recherche tous les décomptes d'un employeur dont la date de début commence après la date passée en paramètre.
     * 
     * @param idEmployeur Id de l'employeur (idAffilie) sur lequel rechercher les décomptes
     * @param date Date à laquelle prendre les décomptes
     * @return Liste de décomptes
     */
    List<Decompte> findByIdEmployeur(String idEmployeur, Date date);

    List<Decompte> findDecomptesForFacturation();

    List<Decompte> findDecomptesForIdPassage(String idPassage);

    List<Decompte> findRectificatifs();

    /**
     * Retourne la liste des décomptes à potentiellement transitionner en état SOMMATION.
     */
    List<Decompte> findDecomptesForSommation();

    List<Decompte> findDecomptesForTaxationOffice();

    /**
     * Retourne le décompte précédent le décompte passé en paramètre.
     * 
     * @param decompte Decompte à partir duquel le décompte doit être déduit
     * @return Le décompte précédent ou null si celui-ci est inexistant
     */
    Decompte findDecomptePrecedent(Decompte decompte);

    Decompte findDecompteIdentiqueGenere(Employeur employeur, PeriodeMensuelle periodeMensuelle, TypeDecompte type);

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

}
