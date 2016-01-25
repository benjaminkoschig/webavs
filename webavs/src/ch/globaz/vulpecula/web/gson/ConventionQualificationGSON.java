package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;
import ch.globaz.vulpecula.domain.models.postetravail.Qualification;
import ch.globaz.vulpecula.domain.models.registre.ConventionQualification;
import ch.globaz.vulpecula.domain.models.registre.Personnel;
import ch.globaz.vulpecula.domain.models.registre.TypeQualification;

/**
 * @author Arnaud Geiser (AGE) | Créé le 16 avr. 2014
 * 
 */
public class ConventionQualificationGSON implements Serializable {
    public String id;
    public String idTiersAdministration;
    public String qualification;
    public String typeQualification;
    public String personnel;
    public String statut;

    public ConventionQualification convertToDomain() {
        ConventionQualification conventionQualification = new ConventionQualification();
        conventionQualification.setId(id);
        conventionQualification.setIdConvention(idTiersAdministration);
        conventionQualification.setQualification(Qualification.fromValue(qualification));
        conventionQualification.setTypeQualification(TypeQualification.fromValue(typeQualification));
        conventionQualification.setPersonnel(Personnel.fromValue(personnel));
        return conventionQualification;
    }
}
