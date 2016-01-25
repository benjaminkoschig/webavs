package ch.globaz.vulpecula.domain.repositories.registre;

import java.util.List;
import ch.globaz.vulpecula.domain.models.registre.ConventionQualification;
import ch.globaz.vulpecula.domain.repositories.Repository;

/**
 * @author Arnaud Geiser (AGE) | Créé le 15 avr. 2014
 * 
 */
public interface ConventionQualificationRepository extends Repository<ConventionQualification> {
    List<ConventionQualification> findByIdConvention(String idConvention);
}
