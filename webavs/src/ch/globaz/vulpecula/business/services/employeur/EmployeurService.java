package ch.globaz.vulpecula.business.services.employeur;

import globaz.jade.exception.JadePersistenceException;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.registre.TypeFacturation;

/**
 * @author Arnaud Geiser (AGE) | Créé le 14 mars 2014
 * 
 */
public interface EmployeurService {
    List<Employeur> findByIdConvention(String idConvention, Date dateDebut, Date dateFin,
            Collection<String> inPeriodicite);

    /**
     * Retourne une liste d'employeurs actifs qui ont été actifs au moins un jour pendant la période passée en paramètre
     * (année).
     * 
     * @param idConvention String représentant l'id de la convention (idTiersAdministration)
     * @param dateDebut Date de début de la période
     * @param dateFin Date de fin de la période
     * @param inPeriodicite Liste de périodicités (codes systèmes)
     * @return Liste d'employeurs
     */
    List<Employeur> findByIdConventionInAnnee(String idConvention, Date dateDebut, Date dateFin,
            Collection<String> inPeriodicite);

    List<Employeur> findByIdConventionNonRadieWithParticulariteSansPersonnel(String idConvention)
            throws JadePersistenceException;

    List<Employeur> findByIdConventionNonRadieWithParticulariteSansPersonnelEtActif(String idConvention)
            throws JadePersistenceException;

    List<Employeur> findByIdAffilie(String idAffilie, Date dateDebut, Date dateFin, Collection<String> inPeriodicite);

    /**
     * Retourne si l'employeur possède des postes de travail actifs par rapport
     * à une date.
     * 
     * @param employeur
     *            Employeur sur lequel les postes seront recherchés
     * @param dateActivite
     *            Date à laquelle l'activité doit être vérifiée.
     * @return true si possède au moins un poste actif, false si aucun poste
     *         actif.
     */
    boolean hasPosteTravailActifs(Employeur employeur, Date dateActivite);

    /**
     * Retourne la liste de tous les employeurs actifs par rapport à la date
     * passée en paramètre. Un poste est considéré comme actif si :
     * <ul>
     * <li>La date doit être comprise dans la période d'affiliation
     * <li>L'employeur doit au moins disposer d'un poste actif à cette date
     * 
     * @param date
     *            Date à laquelle l'activité doit être contrôlé
     * @return Liste d'employeurs actifs ou liste vide si inexistant.
     */
    List<Employeur> getEmployeursActifsAvecPostes(List<Employeur> employeurs, Date dateDebut, Date dateFin);

    /**
     * Retourne la liste de tous les employeurs qui ne possèdent pas de particularité par rapport à la date passée en
     * paramètre.
     * 
     * @param employeurs Liste des employeurs à traiter
     * @param date Date à laquelle déterminer si l'employeur possède une particularité
     * @return Liste des employeurs sans particularités
     */
    List<Employeur> getEmployeursSansParticularite(List<Employeur> employeurs, Date date);

    /**
     * Retourne la listes des employeurs actifs qui ne possèdent pas de postes actifs mais qui possède la propriété
     * {@link Employeur#isEditerSansTravailleur()}
     * 
     * @param dateDebut Date de début à laquelle déterminer l'activité
     * @param dateFin Date de fin à laquelle d'éterminer l'activité
     * @return Liste des employeurs sans postes avec propriété {@link Employeur#isEditerSansTravailleur()}
     */
    List<Employeur> findEmployeursSansPostesAvecEdition(Date dateDebut, Date dateFin);

    String changeTypeFacturation(String idEmployeur, TypeFacturation typeFacturation);

    boolean changeEnvoiBVRSansDecompte(String idEmployeur, boolean activated);

    boolean changeEditerSansTravailleur(String idEmployeur, boolean activated);

    List<Employeur> findEmployeurActif(Date periodeDebut, Date periodeFin);
}
