/**
 * 
 */
package ch.globaz.vulpecula.domain.repositories.postetravail;

import java.util.List;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

/**
 * Définition des méthodes pour le repository relatif à la classe {@link Occupation}
 * 
 */
public interface OccupationRepository {
    /**
     * Retourne la liste des occupations pour un poste de travail spécifique
     * 
     * @param id
     *            correspondant au poste de travail depuis lequel les
     *            occupations seront recherchées
     * @return Une liste contenant les occupations pour un poste de travail
     */
    List<Occupation> findOccupationsByIdPosteTravail(String id);

    /**
     * Retourne la liste des occupations pour un poste de travail spécifique
     * 
     * @param posteTravail correspond au poste de travail depuis lequel les occupations seront recherchées.
     * @return Une liste contenant les occupations pour un poste de travail
     */
    List<Occupation> findOccupationsByPosteTravail(PosteTravail posteTravail);
}
