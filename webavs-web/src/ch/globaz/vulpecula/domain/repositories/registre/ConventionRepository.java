/**
 * 
 */
package ch.globaz.vulpecula.domain.repositories.registre;

import java.util.List;
import ch.globaz.vulpecula.domain.models.registre.Convention;

/**
 * Définition des méthodes pour le repository relatif à la classe {@link Convention}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 18 déc. 2013
 * 
 */
public interface ConventionRepository {
    /**
     * Retourne la convention coresspondant à l'id passé en paramètre
     * 
     * @param id
     *            de la convention à rechercher
     * @return {@link Convention} correspondant à l'id, ou <code>null</code> si
     *         aucune correspondance n'existe pour cet id
     */
    Convention findById(String id);

    /**
     * Retourne toutes les conventions
     * 
     * @return Liste de conventions
     */
    List<Convention> findAll();

    Convention findByIdWithQualifications(String idConvention);
}
