package ch.globaz.vulpecula.domain.repositories.postetravail;

import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface EmployeurRepository extends Repository<Employeur> {

    /**
     * Retourne tous les employeurs présents
     * 
     * @return Liste d'employeurs
     */
    List<Employeur> findAll();

    /**
     * Retourne l'employeur associé à l'id affilié
     * 
     * @param id
     *            String représentant l'id de l'affilié à rechercher
     * @return {@link Employeur} correspondant à l'id de l'affilié
     */
    Employeur findByIdAffilie(String id);

    Employeur findByNumAffilie(String numAffilie);

    List<Employeur> findByIdConvention(String idConvention, Date dateDebut, Date dateFin,
            Collection<String> inPeriodicite);

    /**
     * Retourne une liste d'employeur relatif à la convention passé en paramètre
     * 
     * @param idConvention
     * @return une liste d'employeur
     */
    List<Employeur> findByIdConvention(final String idConvention);

    /**
     * Retourne une liste d'empoyeurs relatif à la convention et aux périodicités (code système) passés en paramètre.
     * 
     * @param idConvention Id de la convention
     * @param inPeriodicite Liste de périodicité
     * @return Liste d'employeurs
     */
    List<Employeur> findByIdConvention(String idConvention, Collection<String> inPeriodicite);

    /**
     * Cette méthode n'est pas fonctionnelle et ne respecte pas son contrat.
     * Elle est utilisé pour la génération des décomptes en masse. Elle est fonctionnelle uniquement car un filtre est
     * appliqué après la sélection.
     */
    List<Employeur> findByIdAffilie(String idAffilie, Date dateDebut, Date dateFin, Collection<String> inPeriodicite);

    /**
     * Retourne si l'employeur dispose d'une entrée en base de données.
     * 
     * @param employeur Employeur
     * @return true si l'employeur dispose d'une ligne
     */
    boolean hasEntryInDB(Employeur employeur);

    boolean hasEntryInDB(String idEmployeur);

    /**
     * Retourne la liste des employeurs qui possèdent la propriété {@link Employeur#isEditerSansTravailleur()}
     * 
     * @return true si possède la case à cocher
     */
    List<Employeur> findEmployeursWithEdition();

    /**
     * Retourne la liste des employeurs dont l'activité à commencer entre la dateDebut et la dateFin
     * 
     * @param dateDebut Date de début à laquelle déterminer l'activité de début de l'employeur
     * @param dateFin Date de fin à laquelle déterminer l'activité de début de l'employeur
     * @return Liste d'employeurs
     */
    List<Employeur> findByPeriode(Date dateDebut, Date dateFin);
}
