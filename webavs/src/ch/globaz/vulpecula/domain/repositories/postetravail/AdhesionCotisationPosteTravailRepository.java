package ch.globaz.vulpecula.domain.repositories.postetravail;

import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Taux;
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

    Taux findTauxCPRTravailleur(PosteTravail posteTravail, Date date);

    List<AdhesionCotisationPosteTravail> findAll();

    AdhesionCotisationPosteTravail create(String idPoste, AdhesionCotisationPosteTravail adhesionCotisationPosteTravail);

    AdhesionCotisationPosteTravail findById(String id);

    AdhesionCotisationPosteTravail update(String idPoste, AdhesionCotisationPosteTravail adhesionCotisationPosteTravail);

    void delete(String idPoste, AdhesionCotisationPosteTravail adhesionCotisationPosteTravail);

    void deleteById(String id);

}
