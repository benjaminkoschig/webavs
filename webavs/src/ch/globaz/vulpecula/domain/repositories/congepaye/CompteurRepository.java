package ch.globaz.vulpecula.domain.repositories.congepaye;

import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.congepaye.Compteur;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface CompteurRepository extends Repository<Compteur> {

    /**
     * Retourne le compteur pour l'id du poste de travail et l'ann�e pass�e en param�tre.
     * 
     * @param idPosteTravail Id repr�sentant le poste de travail
     * @param annee repr�sentant l'ann�e du compteur � rechercher
     * @return le compteur d'un poste de travail pour une ann�e
     */
    Compteur findByPosteTravailAndAnnee(String idPosteTravail, String annee);

    /**
     * Retourne le compteur pour l'id du poste de travail et l'ann�e pass�e en param�tre.
     * 
     * @param idPosteTravail Id repr�sentant le poste de travail
     * @param annee repr�sentant l'ann�e du compteur � rechercher
     * @return le compteur d'un poste de travail pour une ann�e
     */
    Compteur findByPosteTravailAndAnnee(String idPosteTravail, Annee annee);

    List<Compteur> findByPosteTravailAndPeriode(String idPosteTravail, String anneeDebut, String anneeFin);

    /**
     * Retourne les compteurs relatifs � un poste de travail.
     * 
     * @param idPosteTravail Id du poste de travail � rechercher
     * @return Liste de compteurs pour un poste de travail sp�cifique
     */
    List<Compteur> findByIdPosteTravail(String idPosteTravail);

    /**
     * Retourne les compteurs relatifs � un poste de travail avec les d�pendances li�es.
     * 
     * @param idPosteTravail Id du poste de travail � rechercher
     * @return Liste de compteurs et ses lignes associ�es pour un poste de travail sp�cifique
     */
    List<Compteur> findByIdPosteTravailWithDependencies(String idPosteTravail);

    List<Compteur> findCompteursForAnneeMoins5(Convention convention, Annee annee);

}
