package ch.globaz.vulpecula.business.services.employeur;

import globaz.jade.exception.JadePersistenceException;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.registre.TypeFacturation;

/**
 * @author Arnaud Geiser (AGE) | Cr�� le 14 mars 2014
 * 
 */
public interface EmployeurService {
    List<Employeur> findByIdConvention(String idConvention, Date dateDebut, Date dateFin,
            Collection<String> inPeriodicite);

    /**
     * Retourne une liste d'employeurs actifs qui ont �t� actifs au moins un jour pendant la p�riode pass�e en param�tre
     * (ann�e).
     * 
     * @param idConvention String repr�sentant l'id de la convention (idTiersAdministration)
     * @param dateDebut Date de d�but de la p�riode
     * @param dateFin Date de fin de la p�riode
     * @param inPeriodicite Liste de p�riodicit�s (codes syst�mes)
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
     * Retourne si l'employeur poss�de des postes de travail actifs par rapport
     * � une date.
     * 
     * @param employeur
     *            Employeur sur lequel les postes seront recherch�s
     * @param dateActivite
     *            Date � laquelle l'activit� doit �tre v�rifi�e.
     * @return true si poss�de au moins un poste actif, false si aucun poste
     *         actif.
     */
    boolean hasPosteTravailActifs(Employeur employeur, Date dateActivite);

    /**
     * Retourne la liste de tous les employeurs actifs par rapport � la date
     * pass�e en param�tre. Un poste est consid�r� comme actif si :
     * <ul>
     * <li>La date doit �tre comprise dans la p�riode d'affiliation
     * <li>L'employeur doit au moins disposer d'un poste actif � cette date
     * 
     * @param date
     *            Date � laquelle l'activit� doit �tre contr�l�
     * @return Liste d'employeurs actifs ou liste vide si inexistant.
     */
    List<Employeur> getEmployeursActifsAvecPostes(List<Employeur> employeurs, Date dateDebut, Date dateFin);

    /**
     * Retourne la liste de tous les employeurs qui ne poss�dent pas de particularit� par rapport � la date pass�e en
     * param�tre.
     * 
     * @param employeurs Liste des employeurs � traiter
     * @param date Date � laquelle d�terminer si l'employeur poss�de une particularit�
     * @return Liste des employeurs sans particularit�s
     */
    List<Employeur> getEmployeursSansParticularite(List<Employeur> employeurs, Date date);

    /**
     * Retourne la listes des employeurs actifs qui ne poss�dent pas de postes actifs mais qui poss�de la propri�t�
     * {@link Employeur#isEditerSansTravailleur()}
     * 
     * @param dateDebut Date de d�but � laquelle d�terminer l'activit�
     * @param dateFin Date de fin � laquelle d'�terminer l'activit�
     * @return Liste des employeurs sans postes avec propri�t� {@link Employeur#isEditerSansTravailleur()}
     */
    List<Employeur> findEmployeursSansPostesAvecEdition(Date dateDebut, Date dateFin);

    String changeTypeFacturation(String idEmployeur, TypeFacturation typeFacturation);

    boolean changeEnvoiBVRSansDecompte(String idEmployeur, boolean activated);

    boolean changeEditerSansTravailleur(String idEmployeur, boolean activated);

    List<Employeur> findEmployeurActif(Date periodeDebut, Date periodeFin);
}
