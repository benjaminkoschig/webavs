package ch.globaz.vulpecula.domain.repositories.postetravail;

import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

public interface AdhesionCotisationPosteTravailRepository {
    /**
     * Recherche des adhésions aux cotisations auxquels le poste de travail est
     * rattachées. Cette méthode ne charge pas le taux de la cotisation (
     * {@link AdhesionCotisationPosteTravail#getTauxContribuable()}) car l'appel au service affecte un
     * nombre important de traitements dégradant les performances de certains
     * écrans.
     * 
     * @param id
     *            String représentant l'id du poste de travail à partir duquel
     *            les adhésions aux cotisations seront déduites
     * @return Liste d'adhésions aux postes de travail. Vide si aucun élément
     *         présent.
     */
    List<AdhesionCotisationPosteTravail> findByIdPosteTravail(String id);

    List<AdhesionCotisationPosteTravail> findByIdPosteTravailAndPeriode(String id, Periode periode);

    /**
     * Recherche des adhésions aux cotisations auxquels le poste de travail est
     * rattachées. Cette méthode ne charge pas le taux de la cotisation (
     * {@link AdhesionCotisationPosteTravail#getTauxContribuable()}) car l'appel au service affecte un
     * nombre important de traitements dégradant les performances de certains
     * écrans.
     * 
     * @param id
     *            String représentant l'id du poste de travail à partir duquel
     *            les adhésions aux cotisations seront déduites
     * @param date
     *            Date représente la date de validité des cotisations
     * @return Liste d'adhésions aux postes de travail. Vide si aucun élément
     *         présent.
     */
    List<AdhesionCotisationPosteTravail> findByIdPosteTravail(String id, Date date);

    List<AdhesionCotisationPosteTravail> findByIdPosteTravailForCP(DecompteSalaire decompteSalaire);

    /**
     * Recherche des adhésions aux cotisations auxquels le poste de travail est rattaché dans l'année passée en
     * paramètre. Cette méthode ne charge pas le taux de la cotisation (
     * {@link AdhesionCotisationPosteTravail#getTauxContribuable()}) car l'appel au service affecte un
     * nombre important de traitements dégradant les performances de certains
     * écrans.
     * 
     * @param id String représentant l'id du poste de travail
     * @param annee Année pour laquelle prendre les cotisations
     * @return Liste d'adhésions aux postes de travail. Vide si aucun élément présent
     */
    List<AdhesionCotisationPosteTravail> findByIdPosteTravail(String id, Annee annee);

    Taux findTauxCPRTravailleur(PosteTravail posteTravail, Date date);

    List<AdhesionCotisationPosteTravail> findAll();

    AdhesionCotisationPosteTravail create(String idPoste, AdhesionCotisationPosteTravail adhesionCotisationPosteTravail);

    AdhesionCotisationPosteTravail findById(String id);

    AdhesionCotisationPosteTravail update(String idPoste, AdhesionCotisationPosteTravail adhesionCotisationPosteTravail);

    void delete(String idPoste, AdhesionCotisationPosteTravail adhesionCotisationPosteTravail);

    void deleteById(String id);

    List<AdhesionCotisationPosteTravail> findByIdEmployeur(String idEmployeur, String idCotisation);

    /**
     * Recherche du taux CPR du premier poste de travail qui est rattaché dans l'année passée en
     * paramètre.
     * 
     * @param postesTravail List contenant les postes de travail à parcourir
     * @param annee Année pour laquelle prendre les cotisations
     * @return Liste d'adhésions aux postes de travail. Vide si aucun élément présent
     */
    Taux findTauxCPRTravailleurByAnnee(List<PosteTravail> postesTravail, Annee annee);

    AdhesionCotisationPosteTravail findAdhesionCPRTravailleurByAnnee(PosteTravail posteTravail, Annee annee);
}
