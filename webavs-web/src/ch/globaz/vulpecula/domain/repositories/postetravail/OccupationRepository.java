/**
 * 
 */
package ch.globaz.vulpecula.domain.repositories.postetravail;

import java.util.List;
import ch.globaz.vulpecula.domain.models.postetravail.Occupation;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;

/**
 * D�finition des m�thodes pour le repository relatif � la classe {@link Occupation}
 * 
 */
public interface OccupationRepository {
    /**
     * Retourne la liste des occupations pour un poste de travail sp�cifique
     * 
     * @param id
     *            correspondant au poste de travail depuis lequel les
     *            occupations seront recherch�es
     * @return Une liste contenant les occupations pour un poste de travail
     */
    List<Occupation> findOccupationsByIdPosteTravail(String id);

    /**
     * Retourne la liste des occupations pour un poste de travail sp�cifique
     * 
     * @param posteTravail correspond au poste de travail depuis lequel les occupations seront recherch�es.
     * @return Une liste contenant les occupations pour un poste de travail
     */
    List<Occupation> findOccupationsByPosteTravail(PosteTravail posteTravail);
}
