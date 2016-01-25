package ch.globaz.vulpecula.domain.repositories.postetravail;

import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.postetravail.AdhesionCotisationPosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

public interface AdhesionCotisationPosteTravailRepository {
    /**
     * Recherche des adh�sions aux cotisations auxquels le poste de travail est
     * rattach�es. Cette m�thode ne charge pas le taux de la cotisation (
     * {@link AdhesionCotisationPosteTravail#getTauxContribuable()}) car l'appel au service affecte un
     * nombre important de traitements d�gradant les performances de certains
     * �crans.
     * 
     * @param id
     *            String repr�sentant l'id du poste de travail � partir duquel
     *            les adh�sions aux cotisations seront d�duites
     * @return Liste d'adh�sions aux postes de travail. Vide si aucun �l�ment
     *         pr�sent.
     */
    List<AdhesionCotisationPosteTravail> findByIdPosteTravail(String id);

    /**
     * Recherche des adh�sions aux cotisations auxquels le poste de travail est
     * rattach�es. Cette m�thode ne charge pas le taux de la cotisation (
     * {@link AdhesionCotisationPosteTravail#getTauxContribuable()}) car l'appel au service affecte un
     * nombre important de traitements d�gradant les performances de certains
     * �crans.
     * 
     * @param id
     *            String repr�sentant l'id du poste de travail � partir duquel
     *            les adh�sions aux cotisations seront d�duites
     * @param date
     *            Date repr�sente la date de validit� des cotisations
     * @return Liste d'adh�sions aux postes de travail. Vide si aucun �l�ment
     *         pr�sent.
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
