package ch.globaz.vulpecula.domain.repositories.absencejustifiee;

import java.util.List;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface AbsenceJustifieeRepository extends Repository<AbsenceJustifiee> {
    /**
     * Suppression d'une absence justifiée par sa clé primaire
     */
    @Override
    void deleteById(String id);

    /**
     * Recherche les absences justifiées associées à un travailleur.
     * 
     * @param id l'id du travailleur pour lequel on souhaite connaître les absences justifiées
     * @return la liste des absences justifiées
     */
    List<AbsenceJustifiee> findByIdTravailleur(String id);

    /**
     * Recherche les absences justifiées associées à un travailleur.
     * Trie la liste par lot (idPassage)
     * 
     * @param id l'id du travailleur pour lequel on souhaite connaître les absences justifiées
     * @return la liste des absences justifiées
     */
    List<AbsenceJustifiee> findByIdTravailleurOrderByIdpassage(String id);

    List<AbsenceJustifiee> findDecomptesForFacturation(String id);

    /**
     * Retourne les absences justifiées associés au passage de facturation dont l'id est passé en paramètre.
     * 
     * @param id String représentant l'id d'un passage de facturation
     * @return Liste des absences justifiées liées à ce passage
     */
    List<AbsenceJustifiee> findByIdPassage(String id);

    List<AbsenceJustifiee> findBy(String idPassage, String idEmployeur, String idTravailleur, String idConvention);

    List<AbsenceJustifiee> findBy(String idPassage, String idEmployeur, String idTravailleur, String idConvention,
            String orderBy);

    List<AbsenceJustifiee> findBy(String idPassage, String idEmployeur, String idTravailleur, String idConvention,
            Periode periode, String orderBy);

    List<AbsenceJustifiee> findByIdPassage(String idPassage, String idEmployeur);

    List<AbsenceJustifiee> findByIdTravailleurAndPeriod(String idTravailleur, String dateDebut, String dateFin);

    List<AbsenceJustifiee> findByIdTravailleurAndDatePassageFacturation(String idTravailleur, String dateDebut,
            String dateFin);

    List<AbsenceJustifiee> findByIdTravailleurForDateVersement(String idTravailleur, String dateDebut, String dateFin);

    List<AbsenceJustifiee> findSalairesPourAnnee(Annee annee, String idConvention);

    /**
     * Retourne les absences justifiées dont la date de versement est contenu dans l'année pour un employeur spécifique.
     * 
     * @param idEmployeur String représentant un id employeur
     * @param annee Année déterminant la date de versement du début à la fin de l'année
     * @return Liste d'absences justifiées
     */
    List<AbsenceJustifiee> findByIdEmployeurForDateVersementInAnnee(String idEmployeur, Annee annee);

    List<AbsenceJustifiee> findByIdEmployeurForDateVersementInAnnee(String idEmployeur, Date dateDebut, Date dateFin);

    List<AbsenceJustifiee> findByIdTravailleurForDateVersementAndIdEmployeur(String idTravailleur, String dateDebut,
            String dateFin, String idEmployeur);

    List<AbsenceJustifiee> findByIdTravailleurAndDatePassageFacturationAndIdEmployeur(String idTravailleur,
            String dateDebut, String dateFin, String idEmployeur);

}
