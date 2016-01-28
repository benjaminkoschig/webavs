/**
 * 
 */
package ch.globaz.vulpecula.domain.repositories.registre;

import java.util.List;
import ch.globaz.vulpecula.domain.models.registre.Convention;

/**
 * D�finition des m�thodes pour le repository relatif � la classe {@link Convention}
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 18 d�c. 2013
 * 
 */
public interface ConventionRepository {
    /**
     * Retourne la convention coresspondant � l'id pass� en param�tre
     * 
     * @param id
     *            de la convention � rechercher
     * @return {@link Convention} correspondant � l'id, ou <code>null</code> si
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
