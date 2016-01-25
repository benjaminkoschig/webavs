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
 * Repository des d�comptes
 */

public interface DecompteRepository extends Repository<Decompte> {
    List<Decompte> findByIdInWithDependencies(Collection<String> ids);

    Decompte findByIdWithDependencies(String idDecompte);

    List<Decompte> findDecompteByNoDecompte(String noDecompte);

    Decompte findFirstDecompteByNoDecompte(String noDecompte);

    List<Decompte> findAll();

    List<Decompte> findByIdEmployeur(String idEmployeur);

    /**
     * Recherche tous les d�comptes d'un employeur dont la date de d�but commence apr�s la date pass�e en param�tre.
     * 
     * @param idEmployeur Id de l'employeur (idAffilie) sur lequel rechercher les d�comptes
     * @param date Date � laquelle prendre les d�comptes
     * @return Liste de d�comptes
     */
    List<Decompte> findByIdEmployeur(String idEmployeur, Date date);

    List<Decompte> findDecomptesForFacturation();

    List<Decompte> findDecomptesForIdPassage(String idPassage);

    List<Decompte> findRectificatifs();

    /**
     * Retourne la liste des d�comptes � potentiellement transitionner en �tat SOMMATION.
     */
    List<Decompte> findDecomptesForSommation();

    List<Decompte> findDecomptesForTaxationOffice();

    /**
     * Retourne le d�compte pr�c�dent le d�compte pass� en param�tre.
     * 
     * @param decompte Decompte � partir duquel le d�compte doit �tre d�duit
     * @return Le d�compte pr�c�dent ou null si celui-ci est inexistant
     */
    Decompte findDecomptePrecedent(Decompte decompte);

    Decompte findDecompteIdentiqueGenere(Employeur employeur, PeriodeMensuelle periodeMensuelle, TypeDecompte type);

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

}
