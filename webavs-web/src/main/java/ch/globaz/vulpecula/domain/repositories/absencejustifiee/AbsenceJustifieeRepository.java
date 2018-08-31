package ch.globaz.vulpecula.domain.repositories.absencejustifiee;

import java.util.List;
import ch.globaz.vulpecula.domain.models.absencejustifiee.AbsenceJustifiee;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface AbsenceJustifieeRepository extends Repository<AbsenceJustifiee> {
    /**
     * Suppression d'une absence justifi�e par sa cl� primaire
     */
    @Override
    void deleteById(String id);

    /**
     * Recherche les absences justifi�es associ�es � un travailleur.
     * 
     * @param id l'id du travailleur pour lequel on souhaite conna�tre les absences justifi�es
     * @return la liste des absences justifi�es
     */
    List<AbsenceJustifiee> findByIdTravailleur(String id);

    /**
     * Recherche les absences justifi�es associ�es � un travailleur.
     * Trie la liste par lot (idPassage)
     * 
     * @param id l'id du travailleur pour lequel on souhaite conna�tre les absences justifi�es
     * @return la liste des absences justifi�es
     */
    List<AbsenceJustifiee> findByIdTravailleurOrderByIdpassage(String id);

    List<AbsenceJustifiee> findDecomptesForFacturation(String id);

    /**
     * Retourne les absences justifi�es associ�s au passage de facturation dont l'id est pass� en param�tre.
     * 
     * @param id String repr�sentant l'id d'un passage de facturation
     * @return Liste des absences justifi�es li�es � ce passage
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
     * Retourne les absences justifi�es dont la date de versement est contenu dans l'ann�e pour un employeur sp�cifique.
     * 
     * @param idEmployeur String repr�sentant un id employeur
     * @param annee Ann�e d�terminant la date de versement du d�but � la fin de l'ann�e
     * @return Liste d'absences justifi�es
     */
    List<AbsenceJustifiee> findByIdEmployeurForDateVersementInAnnee(String idEmployeur, Annee annee);

    List<AbsenceJustifiee> findByIdEmployeurForDateVersementInAnnee(String idEmployeur, Date dateDebut, Date dateFin);

    List<AbsenceJustifiee> findByIdTravailleurForDateVersementAndIdEmployeur(String idTravailleur, String dateDebut,
            String dateFin, String idEmployeur);

    List<AbsenceJustifiee> findByIdTravailleurAndDatePassageFacturationAndIdEmployeur(String idTravailleur,
            String dateDebut, String dateFin, String idEmployeur);

}
