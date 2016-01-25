package ch.globaz.vulpecula.domain.repositories.postetravail;

import java.util.List;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;

/**
 * D�finition des m�thodes pour le repository relatives � la classe {@link Qualification}
 * 
 * @author Arnaud Geiser (AGE) | Cr�� le 17 janv. 2014
 * 
 */
public interface QualificationRepository {
    /**
     * Retourne les qualifications propres � la convention
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
