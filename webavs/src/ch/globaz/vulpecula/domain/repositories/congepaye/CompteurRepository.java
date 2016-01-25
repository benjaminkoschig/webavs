package ch.globaz.vulpecula.domain.repositories.congepaye;

import java.util.List;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.congepaye.Compteur;
import ch.globaz.vulpecula.domain.models.registre.Convention;
import ch.globaz.vulpecula.domain.repositories.Repository;

public interface CompteurRepository extends Repository<Compteur> {

    /**
     * Retourne le compteur pour l'id du poste de travail et l'année passée en paramètre.
     * 
     * @param idPosteTravail Id représentant le poste de travail
     * @param annee représentant l'année du compteur à rechercher
     * @return le compteur d'un poste de travail pour une année
     */
    Compteur findByPosteTravailAndAnnee(String idPosteTravail, String annee);

    /**
     * Retourne le compteur pour l'id du poste de travail et l'année passée en paramètre.
     * 
     * @param idPosteTravail Id représentant le poste de travail
     * @param annee représentant l'année du compteur à rechercher
     * @return le compteur d'un poste de travail pour une année
     */
    Compteur findByPosteTravailAndAnnee(String idPosteTravail, Annee annee);

    List<Compteur> findByPosteTravailAndPeriode(String idPosteTravail, String anneeDebut, String anneeFin);

    /**
     * Retourne les compteurs relatifs à un poste de travail.
     * 
     * @param idPosteTravail Id du poste de travail à rechercher
     * @return Liste de compteurs pour un poste de travail spécifique
     */
    List<Compteur> findByIdPosteTravail(String idPosteTravail);

    /**
     * Retourne les compteurs relatifs à un poste de travail avec les dépendances liées.
     * 
     * @param idPosteTravail Id du poste de travail à rechercher
     * @return Liste de compteurs et ses lignes associées pour un poste de travail spécifique
     */
    List<Compteur> findByIdPosteTravailWithDependencies(String idPosteTravail);

    List<Compteur> findCompteursForAnneeMoins5(Convention convention, Annee annee);

}
