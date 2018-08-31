package ch.globaz.vulpecula.domain.repositories.postetravail;

import java.util.List;
import ch.globaz.ij.businessimpl.exception.BusinessException;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.syndicat.ListeTravailleursSansSyndicat;
import ch.globaz.vulpecula.domain.repositories.QueryParameters;
import ch.globaz.vulpecula.domain.repositories.Repository;

/**
 * Fournit un service pour ajouter, supprimer et rechercher des éléments dans un
 * repository contenant des postes de travail.
 * 
 */
public interface PosteTravailRepository extends Repository<PosteTravail> {

    /**
     * Ajoute un poste de travail dans le repository, ses caisses sociales ainsi
     * que ses cotisations seront également persistées.
     * 
     * @param posteTravail
     *            le poste de travail à persister
     * @return le poste de travail passé en paramètre, avec son id et son spy
     * @throws BusinessException
     */
    @Override
    PosteTravail create(PosteTravail posteTravail);

    /**
     * Recherche le poste de travail correspondant à l'id passé en paramètre.
     * 
     * @param id
     *            l'id du poste de travail à rechercher
     * @return le poste de travail correspondant à l'id passé en paramètre ou <code>null</code> s'il n'existe aucun
     *         poste de travail avec cet
     *         id dans le repository
     */
    @Override
    PosteTravail findById(String id);

    /**
     * Recherche des postes de travail associés à l'id de l'employeur passé en
     * paramètre.
     * 
     * @param id
     *            de l'employeur
     * @return List<PosteTravail> associés à l'employeur
     */
    List<PosteTravail> findByIdEmployeur(String id);

    /**
     * Recherche des postes de travail associés à l'id de l'employeur passé en
     * paramètre actif à une date donnée.
     * 
     * @param id de l'employeur
     * @param periode de début
     * @param periode de fin
     * @return List<PosteTravail> associés à l'employeur
     */
    List<PosteTravail> findPosteActif(String id, Date periodeDebut, Date periodeFin);

    /**
     * Recherche des postes de travail associés à l'id de la convention ainsi qu'a une des qualifications de la listes
     * passée en
     * paramètre et actif à une date donnée.
     * 
     * @param id de la convention
     * @param qualifications Liste de qualifications
     * @param periode de début
     * @param periode de fin
     * @return List<PosteTravail> associés à l'employeur
     */
    List<PosteTravail> findPosteActifByConventionAndQualification(Date periodeDebut, Date periodeFin,
            String idConvention, List<Qualification> qualifications);

    /**
     * Recherche des postes de travail auxquels un travailleur est rattaché.
     * 
     * @param id
     *            l'id du travailleur
     * @return les postes de travail auxquels le travailleur est rattaché
     */
    List<PosteTravail> findByIdTravailleur(String id);

    /**
     * Recherche des postes de travail auxquels un travailleur est rattaché.
     * Avec les dépendances
     * 
     * @param id
     *            l'id du travailleur
     * @return les postes de travail auxquels le travailleur est rattaché
     */
    List<PosteTravail> findByIdTravailleurWithDependencies(String id);

    /**
     * Supprime un poste de travail du repository. Afin d'être supprimable, un
     * poste de travail ne doit pas posséder de décomptes salaires
     * 
     * @param posteTravail
     *            le poste de travail à supprimer
     * @throws BusinessException
     *             dans le cas où le poste de travail ne peut être supprimé
     */
    @Override
    void delete(PosteTravail posteTravail);

    @Override
    PosteTravail update(PosteTravail posteTravail);

    PosteTravail updateForEbu(PosteTravail posteTravail, Date oldDateFin);

    PosteTravail findByIdPosteTravailWithDependencies(String idPosteTravail);

    PosteTravail findByIdWithOccupations(String idPosteTravail);

    PosteTravail findByIdWithFullDependecies(String idPosteTravail);

    /**
     * Recherche de tous les postes de travail dont le travailleur a/n'a pas été annoncé à la MEROBA. Attention, cette
     * méthode peut potentiellement retourner plusieurs postes de travail pour un même travailleur.
     * 
     * @param isAnnonceMeroba Si l'on souhaite retournés les postes de travail non annoncés à la MEROBA
     * @return Liste de postes de travail
     */
    List<PosteTravail> findAAnnoncer(Date date, boolean isAnnonceMeroba);

    List<String> findAAnnoncer2(Date date, boolean isAnnonceMeroba);

    List<PosteTravail> findAll();

    List<PosteTravail> findByIdConventionAndQualification(String idConvention, List<Qualification> qualifications);

    List<PosteTravail> findByIdAffilieWithPagination(String idAffilie, QueryParameters extraParams, Date date,
            int offset, int size);

    int countByIdAffilie(String idEmployeur, QueryParameters extraParams, Date date);

    List<PosteTravail> findByIdTravailleurWithPagination(String idTravailleur, QueryParameters extrasParams, Date date,
            int offset, int size);

    int countByIdTravailleur(String idTravailleur, QueryParameters extrasParams, Date date);

    List<PosteTravail> findPosteActifInAnnee(String id, Annee annee);

    PosteTravail findByTravailleurEtEmployeur(String idTravailleur, String idEmployeur);

    PosteTravail findByTravailleurEtEmployeurEtPosteCorrelationId(String idTravailleur, String idEmployeur,
            String posteCorrelationId);

    PosteTravail findByPosteCorrelationId(String posteCorrelationId);

    PosteTravail findByTravailleurEmployeurEtPeriode(String idTravailleur, String idEmployeur, String periodeDebut,
            String periodeFin);

    List<PosteTravail> findListByTravailleurEtEmployeur(String idTravailleur, String idEmployeur);

    PosteTravail findByTravailleurEmployeurEtQualification(String idTravailleur, String idEmployeur,
            List<Qualification> qualifications);

    /**
     * Recherche de tous les postes de travail qui possèdent des cotisations de type CPR et qui n'est pas affilié à un
     * syndicat pour une année donnée
     * 
     * @param idCaisseMetier
     * 
     * @param Annee Année de l'affiliation du poste de travail
     * @return Liste d'ID des postes de travail
     */
    List<ListeTravailleursSansSyndicat> findPostesTravailWithCPRCotiNotInAffiliationSyndicat(Annee annee,
            String idCaisseMetier);
}
