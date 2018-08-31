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
 * Fournit un service pour ajouter, supprimer et rechercher des �l�ments dans un
 * repository contenant des postes de travail.
 * 
 */
public interface PosteTravailRepository extends Repository<PosteTravail> {

    /**
     * Ajoute un poste de travail dans le repository, ses caisses sociales ainsi
     * que ses cotisations seront �galement persist�es.
     * 
     * @param posteTravail
     *            le poste de travail � persister
     * @return le poste de travail pass� en param�tre, avec son id et son spy
     * @throws BusinessException
     */
    @Override
    PosteTravail create(PosteTravail posteTravail);

    /**
     * Recherche le poste de travail correspondant � l'id pass� en param�tre.
     * 
     * @param id
     *            l'id du poste de travail � rechercher
     * @return le poste de travail correspondant � l'id pass� en param�tre ou <code>null</code> s'il n'existe aucun
     *         poste de travail avec cet
     *         id dans le repository
     */
    @Override
    PosteTravail findById(String id);

    /**
     * Recherche des postes de travail associ�s � l'id de l'employeur pass� en
     * param�tre.
     * 
     * @param id
     *            de l'employeur
     * @return List<PosteTravail> associ�s � l'employeur
     */
    List<PosteTravail> findByIdEmployeur(String id);

    /**
     * Recherche des postes de travail associ�s � l'id de l'employeur pass� en
     * param�tre actif � une date donn�e.
     * 
     * @param id de l'employeur
     * @param periode de d�but
     * @param periode de fin
     * @return List<PosteTravail> associ�s � l'employeur
     */
    List<PosteTravail> findPosteActif(String id, Date periodeDebut, Date periodeFin);

    /**
     * Recherche des postes de travail associ�s � l'id de la convention ainsi qu'a une des qualifications de la listes
     * pass�e en
     * param�tre et actif � une date donn�e.
     * 
     * @param id de la convention
     * @param qualifications Liste de qualifications
     * @param periode de d�but
     * @param periode de fin
     * @return List<PosteTravail> associ�s � l'employeur
     */
    List<PosteTravail> findPosteActifByConventionAndQualification(Date periodeDebut, Date periodeFin,
            String idConvention, List<Qualification> qualifications);

    /**
     * Recherche des postes de travail auxquels un travailleur est rattach�.
     * 
     * @param id
     *            l'id du travailleur
     * @return les postes de travail auxquels le travailleur est rattach�
     */
    List<PosteTravail> findByIdTravailleur(String id);

    /**
     * Recherche des postes de travail auxquels un travailleur est rattach�.
     * Avec les d�pendances
     * 
     * @param id
     *            l'id du travailleur
     * @return les postes de travail auxquels le travailleur est rattach�
     */
    List<PosteTravail> findByIdTravailleurWithDependencies(String id);

    /**
     * Supprime un poste de travail du repository. Afin d'�tre supprimable, un
     * poste de travail ne doit pas poss�der de d�comptes salaires
     * 
     * @param posteTravail
     *            le poste de travail � supprimer
     * @throws BusinessException
     *             dans le cas o� le poste de travail ne peut �tre supprim�
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
     * Recherche de tous les postes de travail dont le travailleur a/n'a pas �t� annonc� � la MEROBA. Attention, cette
     * m�thode peut potentiellement retourner plusieurs postes de travail pour un m�me travailleur.
     * 
     * @param isAnnonceMeroba Si l'on souhaite retourn�s les postes de travail non annonc�s � la MEROBA
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
     * Recherche de tous les postes de travail qui poss�dent des cotisations de type CPR et qui n'est pas affili� � un
     * syndicat pour une ann�e donn�e
     * 
     * @param idCaisseMetier
     * 
     * @param Annee Ann�e de l'affiliation du poste de travail
     * @return Liste d'ID des postes de travail
     */
    List<ListeTravailleursSansSyndicat> findPostesTravailWithCPRCotiNotInAffiliationSyndicat(Annee annee,
            String idCaisseMetier);
}
