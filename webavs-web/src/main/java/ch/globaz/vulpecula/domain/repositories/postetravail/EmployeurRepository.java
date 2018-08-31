package ch.globaz.vulpecula.domain.repositories.postetravail;

import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface EmployeurRepository extends Repository<Employeur> {

    /**
     * Retourne tous les employeurs pr�sents
     * 
     * @return Liste d'employeurs
     */
    List<Employeur> findAll();

    /**
     * Retourne l'employeur associ� � l'id affili�
     * 
     * @param id
     *            String repr�sentant l'id de l'affili� � rechercher
     * @return {@link Employeur} correspondant � l'id de l'affili�
     */
    Employeur findByIdAffilie(String id);

    Employeur findByNumAffilie(String numAffilie);

    List<Employeur> findByIdConvention(String idConvention, Date dateDebut, Date dateFin,
            Collection<String> inPeriodicite);

    /**
     * Retourne une liste d'employeur relatif � la convention pass� en param�tre
     * 
     * @param idConvention
     * @return une liste d'employeur
     */
    List<Employeur> findByIdConvention(final String idConvention);

    /**
     * Retourne une liste d'empoyeurs relatif � la convention et aux p�riodicit�s (code syst�me) pass�s en param�tre.
     * 
     * @param idConvention Id de la convention
     * @param inPeriodicite Liste de p�riodicit�
     * @return Liste d'employeurs
     */
    List<Employeur> findByIdConvention(String idConvention, Collection<String> inPeriodicite);

    /**
     * Cette m�thode n'est pas fonctionnelle et ne respecte pas son contrat.
     * Elle est utilis� pour la g�n�ration des d�comptes en masse. Elle est fonctionnelle uniquement car un filtre est
     * appliqu� apr�s la s�lection.
     */
    List<Employeur> findByIdAffilie(String idAffilie, Date dateDebut, Date dateFin, Collection<String> inPeriodicite);

    /**
     * Retourne si l'employeur dispose d'une entr�e en base de donn�es.
     * 
     * @param employeur Employeur
     * @return true si l'employeur dispose d'une ligne
     */
    boolean hasEntryInDB(Employeur employeur);

    boolean hasEntryInDB(String idEmployeur);

    /**
     * Retourne la liste des employeurs qui poss�dent la propri�t� {@link Employeur#isEditerSansTravailleur()}
     * 
     * @return true si poss�de la case � cocher
     */
    List<Employeur> findEmployeursWithEdition();

    /**
     * Retourne la liste des employeurs dont l'activit� � commencer entre la dateDebut et la dateFin
     * 
     * @param dateDebut Date de d�but � laquelle d�terminer l'activit� de d�but de l'employeur
     * @param dateFin Date de fin � laquelle d�terminer l'activit� de d�but de l'employeur
     * @return Liste d'employeurs
     */
    List<Employeur> findByPeriode(Date dateDebut, Date dateFin);
}
