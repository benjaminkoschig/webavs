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
 * Repository des lignes du décompte Chaque ligne est le décompte pour le
 * travailleur, par rapport au décompte général qui est celui de l'employeur
 */

public interface DecompteSalaireRepository extends Repository<DecompteSalaire> {
    @Override
    void deleteById(String idLigneDecompte);

    /**
     * Retourne les lignes de décomptes pour une année par travailleur
     * 
     * @param string
     * 
     * @param Annee annee
     * 
     * @return Liste de lignes de décompte ou liste vide
     * @throws JadePersistenceException
     */
    Deque<DecompteSalaire> findSalairesPourAnnee(Annee annee, Convention convention, String typeDecompte);

    /**
     * Retourne les lignes de décomptes correspondant à l'id du décompte. Les dépendances avec les absences et les
     * cotisations sont également résolues.
     * 
     * @param idDecompte
     *            String représentnat l'id du décompte
     * @return Liste de lignes de décompte ou liste vide
     */
    List<DecompteSalaire> findByIdDecompteWithDependencies(String idDecompte);

    /**
     * Retourne les décomptes salaires ainsi que leurs dépendances selon les critères ci-dessus.
     * Chaque champ peut être null.
     * 
     * @return Liste de décomptes salaires ou liste vide
     */
    List<DecompteSalaire> findWithDependencies(String idDecompte, String idPosteTravail, String nomTravailleur);

    /**
     * Retourne les lignes de décomptes correspodant à l'id du décompte. Les dépendances 1-n tels que les absences et
     * les cotisations ne sont pas chargées.
     * 
     * @param idDecompte String représentant l'id du décompte
     * @return Liste de lignes de décompte ou liste vide
     */
    List<DecompteSalaire> findByIdDecompte(String idDecompte);

    List<DecompteSalaire> findByIdWithCotisations(String idDecompte, String idPosteTravail);

    List<DecompteSalaire> findByIdsDecomptesInWithDependencies(List<String> idsDecomptes);

    List<DecompteSalaire> findByIdsDecomptesIn(List<String> ids);

    /**
     * Retourne les lignes de décomptes correspondant à l'id du travailleur.
     * 
     * @param idTravailleur
     *            String représentant l'id du travailleur
     * @return Liste de lignes de décompte ou liste vide
     */
    List<DecompteSalaire> findLignesDecomptesByIdTravailleur(String idTravailleur);

    List<DecompteSalaire> findLignesDecomptesSansCotisationsByIdTravailleur(String idTravailleur, String idDecompte,
            String raisonSociale, String numeroDecompte, String type);

    /**
     * @param idDecompte
     * @param sequence
     *            sequence courante du décompte salaire
     * @return le décompte salaire suivant
     */
    DecompteSalaire findNextByIdDecompteAndSequence(String idDecompte, String currentSequence);

    /**
     * @param idDecompte
     * @param sequence
     *            sequence courante du décompte salaire
     * @return le décompte salaire précédent
     */
    DecompteSalaire findPreviousByIdDecompteAndSequence(String idDecompte, String currentSequence);

    /**
     * @param idDecompte
     * @param sequence
     *            sequence courante du décompte salaire
     * @return true if the last or if currentSequence is null or empty
     */
    boolean isLastDecompteSalaire(String idDecompte, String currentSequence);

    /**
     * @param idDecompte
     * @param sequence
     *            sequence courante du décompte salaire
     * @return true if the first
     */
    boolean isFirstDecompteSalaire(String idDecompte, String currentSequence);

    DecompteSalaire findPrecedentComptabilise(String idPosteTravail, Date dateReception);

    List<DecompteSalaire> findByIdPosteTravail(String idPosteTravail);

    /**
     * Retourne les décomptes salaire à partir d'une année en arrière.
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
     * Retourne de tous les décomptes salaires de la même année que le décompte salaire passée en paramètre.
     * 
     * @param idPosteTravail
     * @param anneeDecompte
     * @return Liste de décomptes salaires
     */
    List<DecompteSalaire> findForYear(String idPosteTravail, Annee anneeDecompte);

    List<DecompteSalaire> findByIdAndPeriode(String idTravailleur, Date dateDebut, Date dateFin);

    List<DecompteSalaire> findByIdPosteTravail(Collection<String> idPostes, Date anneeDebut, Date anneeFin);

    DecompteSalaire findAndfetchFirstByIdDecompte(String idDecompte);

    DecompteSalaire findByIdWithoutDependencies(String id);
}
