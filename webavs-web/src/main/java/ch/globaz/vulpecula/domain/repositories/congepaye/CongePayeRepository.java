package ch.globaz.vulpecula.domain.repositories.congepaye;

import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.domain.repositories.Repository;

/**
 * Repository des congés payés
 * 
 * @since WebBMS 0.01.04
 */
public interface CongePayeRepository extends Repository<CongePaye> {
    /**
     * Suppression d'un congé payé par sa clé primaire
     */
    @Override
    void deleteById(String id);

    /**
     * Recherche les congés payés associées à un travailleur.
     * 
     * @param id : l'id du travailleur pour lequel on souhaite connaître les congés payés
     * @return la liste des congés payés
     */
    List<CongePaye> findByIdTravailleur(String idTravailleur);

    /**
     * Recherche les congés payés associées à un travailleur.
     * Trie la liste par lot (idPassage)
     * 
     * @param id : l'id du travailleur pour lequel on souhaite connaître les congés payés
     * @return la liste des congés payés
     */
    List<CongePaye> findByIdTravailleurOrderByIdpassage(String idTravailleur);

    /**
     * Recherche des congés payés associés au passage de facturation passé en paramètre.
     * 
     * @param id Id du passage de facturation
     * @return Liste de congés pays
     */
    List<CongePaye> findForFacturation(String idPassage);

    /**
     * Recherche des congés payés associés au passage de facturation passé en paramètre.
     * 
     * @param idPassage Id du passage de facturation
     * @return Liste de congés payés associés au passage de facturation
     */
    List<CongePaye> findByIdPassage(String idPassage);

    List<CongePaye> findByIdPassage(String idPassage, String idEmployeur);

    CongePaye findByIdWithDependancies(String idCongePaye);

    List<CongePaye> findBy(String idPassage, String idEmployeur, String idTravailleur, String idConvention,
            String orderBy);

    List<CongePaye> findBy(String idPassage, String idEmployeur, String idTravailleur, String idConvention);

    List<CongePaye> findBy(String idPassageFacturation, String idEmployeur, String idTravailleur, String idConvention,
            Periode periode);

    List<CongePaye> findByIdTravailleurAndPeriod(String idTravailleur, String dateDebut, String dateFin);

    List<CongePaye> findByIdTravailleurAndDatePassageFacturation(String idTravailleur, String dateDebut, String dateFin);

    List<CongePaye> findByIdTravailleurForDateVersement(String idTravailleur, String dateDebut, String dateFin);

    List<CongePaye> findSalairesPourAnnee(Annee annee, String idConvention);

    /**
     * Retourne les congés payés dont la date de versement est contenu dans l'année pour un employeur spécifique.
     * 
     * @param idEmployeur String représentant un id employeur
     * @param annee Année déterminant la date de versement du début à la fin de l'année
     * @return Liste d'absences justifiées
     */
    List<CongePaye> findByIdEmployeurForDateVersementInAnnee(String idEmployeur, Annee annee);

    List<CongePaye> findByIdEmployeurForDateVersementInAnnee(String idEmployeur, Date dateDebut, Date dateFin);

    List<CongePaye> findByIdTravailleurAndDatePassageFacturationAndIdEmployeur(String idTravailleur, String dateDebut,
            String dateFin, String idEmployeur);

    List<CongePaye> findByIdTravailleurForDateVersementAndIdEmployeur(String idTravailleur, String dateDebut,
            String dateFin, String idEmployeur);

    List<CongePaye> findCPSoumisLPP(Annee annee);
}
