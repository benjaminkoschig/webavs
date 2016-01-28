package ch.globaz.vulpecula.domain.repositories.postetravail;

import java.util.List;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;

/**
 * Définition des méthodes pour le repository relatives à la classe {@link Qualification}
 * 
 * @author Arnaud Geiser (AGE) | Créé le 17 janv. 2014
 * 
 */
public interface QualificationRepository {
    /**
     * Retourne les qualifications propres à la convention
     * 
     * @return Liste de {@link Qualification}
     */
    List<Qualification> findByIdConvention(String idConvention);

    /**
     * 
     * @param idConvention
     * @return
     */
    List<String> findByIdConventionAsString(String idConvention);
}
