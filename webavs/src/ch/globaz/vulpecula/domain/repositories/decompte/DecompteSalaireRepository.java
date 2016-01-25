package ch.globaz.vulpecula.domain.repositories.decompte;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.domain.repositories.Repository;

/**
 * Repository des lignes du d�compte Chaque ligne est le d�compte pour le
 * travailleur, par rapport au d�compte g�n�ral qui est celui de l'employeur
 */

public interface DecompteSalaireRepository extends Repository<DecompteSalaire> {
    @Override
    void deleteById(String idLigneDecompte);

    /**
     * Retourne les lignes de d�comptes pour une ann�e par travailleur
     * 
     * @param string
     * 
     * @param Annee annee
     * 
     * @return Liste de lignes de d�compte ou liste vide
     * @throws JadePersistenceException
     */
    Deque<DecompteSalaire> findSalairesPourAnnee(Annee annee, Convention convention, String typeDecompte);

    /**
     * Retourne les lignes de d�comptes correspondant � l'id du d�compte. Les d�pendances avec les absences et les
     * cotisations sont �galement r�solues.
     * 
     * @param idDecompte
     *            String repr�sentnat l'id du d�compte
     * @return Liste de lignes de d�compte ou liste vide
     */
    List<DecompteSalaire> findByIdDecompteWithDependencies(String idDecompte);

    /**
     * Retourne les d�comptes salaires ainsi que leurs d�pendances selon les crit�res ci-dessus.
     * Chaque champ peut �tre null.
     * 
     * @return Liste de d�comptes salaires ou liste vide
     */
    List<DecompteSalaire> findWithDependencies(String idDecompte, String idPosteTravail, String nomTravailleur);

    /**
     * Retourne les lignes de d�comptes correspodant � l'id du d�compte. Les d�pendances 1-n tels que les absences et
     * les cotisations ne sont pas charg�es.
     * 
     * @param idDecompte String repr�sentant l'id du d�compte
     * @return Liste de lignes de d�compte ou liste vide
     */
    List<DecompteSalaire> findByIdDecompte(String idDecompte);

    List<DecompteSalaire> findByIdWithCotisations(String idDecompte, String idPosteTravail);

    List<DecompteSalaire> findByIdsDecomptesInWithDependencies(List<String> idsDecomptes);

    List<DecompteSalaire> findByIdsDecomptesIn(List<String> ids);

    /**
     * Retourne les lignes de d�comptes correspondant � l'id du travailleur.
     * 
     * @param idTravailleur
     *            String repr�sentant l'id du travailleur
     * @return Liste de lignes de d�compte ou liste vide
     */
    List<DecompteSalaire> findLignesDecomptesByIdTravailleur(String idTravailleur);

    List<DecompteSalaire> findLignesDecomptesSansCotisationsByIdTravailleur(String idTravailleur, String idDecompte,
            String raisonSociale, String numeroDecompte, String type);

    /**
     * @param idDecompte
     * @param sequence
     *            sequence courante du d�compte salaire
     * @return le d�compte salaire suivant
     */
    DecompteSalaire findNextByIdDecompteAndSequence(String idDecompte, String currentSequence);

    /**
     * @param idDecompte
     * @param sequence
     *            sequence courante du d�compte salaire
     * @return le d�compte salaire pr�c�dent
     */
    DecompteSalaire findPreviousByIdDecompteAndSequence(String idDecompte, String currentSequence);

    /**
     * @param idDecompte
     * @param sequence
     *            sequence courante du d�compte salaire
     * @return true if the last or if currentSequence is null or empty
     */
    boolean isLastDecompteSalaire(String idDecompte, String currentSequence);

    /**
     * @param idDecompte
     * @param sequence
     *            sequence courante du d�compte salaire
     * @return true if the first
     */
    boolean isFirstDecompteSalaire(String idDecompte, String currentSequence);

    DecompteSalaire findPrecedentComptabilise(String idPosteTravail, Date dateReception);

    List<DecompteSalaire> findByIdPosteTravail(String idPosteTravail);

    /**
     * Retourne les d�comptes salaire � partir d'une ann�e en arri�re.
     * 
     * @param idPosteTravail
     * @return
     */
    List<DecompteSalaire> findByIdPosteTravailOneYearAgo(String idPosteTravail);

    List<DecompteSalaire> findBy(String idDecompte, String idPosteTravail, Date periodeDebut, Date periodeFin);

    DecompteSalaire updateDateAnnonce(DecompteSalaire decompteSalaire);

    void deleteCotisationsDecompte(DecompteSalaire decompteSalaire);

    void saveCotisationsDecompte(DecompteSalaire decompteSalaire);

    /**
     * Retourne de tous les d�comptes salaires de la m�me ann�e que le d�compte salaire pass�e en param�tre.
     * 
     * @param idPosteTravail
     * @param anneeDecompte
     * @return Liste de d�comptes salaires
     */
    List<DecompteSalaire> findForYear(String idPosteTravail, Annee anneeDecompte);

    List<DecompteSalaire> findByIdAndPeriode(String idTravailleur, Date dateDebut, Date dateFin);

    List<DecompteSalaire> findByIdPosteTravail(Collection<String> idPostes, Date anneeDebut, Date anneeFin);

    DecompteSalaire findAndfetchFirstByIdDecompte(String idDecompte);

    DecompteSalaire findByIdWithoutDependencies(String id);
}
