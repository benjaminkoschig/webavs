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

    List<AdhesionCotisationPosteTravail> findByIdPosteTravailAndPeriode(String id, Periode periode);

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

    List<AdhesionCotisationPosteTravail> findByIdPosteTravailForCP(DecompteSalaire decompteSalaire);

    /**
     * Recherche des adh�sions aux cotisations auxquels le poste de travail est rattach� dans l'ann�e pass�e en
     * param�tre. Cette m�thode ne charge pas le taux de la cotisation (
     * {@link AdhesionCotisationPosteTravail#getTauxContribuable()}) car l'appel au service affecte un
     * nombre important de traitements d�gradant les performances de certains
     * �crans.
     * 
     * @param id String repr�sentant l'id du poste de travail
     * @param annee Ann�e pour laquelle prendre les cotisations
     * @return Liste d'adh�sions aux postes de travail. Vide si aucun �l�ment pr�sent
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
     * Recherche du taux CPR du premier poste de travail qui est rattach� dans l'ann�e pass�e en
     * param�tre.
     * 
     * @param postesTravail List contenant les postes de travail � parcourir
     * @param annee Ann�e pour laquelle prendre les cotisations
     * @return Liste d'adh�sions aux postes de travail. Vide si aucun �l�ment pr�sent
     */
    Taux findTauxCPRTravailleurByAnnee(List<PosteTravail> postesTravail, Annee annee);

    AdhesionCotisationPosteTravail findAdhesionCPRTravailleurByAnnee(PosteTravail posteTravail, Annee annee);
}
