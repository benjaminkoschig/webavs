package ch.globaz.vulpecula.domain.specifications.cotisationAssociationProf;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;

/**
 * Specification obligeant l'absence justifiée de posséder une période valide (date de début et date de fin renseignée).
 * 
 */
public class CotisationsAssociationProfessionnelleMandatoriesSpecification extends
        AbstractSpecification<CotisationAssociationProfessionnelle> {

    @Override
    public boolean isValid(CotisationAssociationProfessionnelle cotisationAP) {
        if (cotisationAP.getLibelle() == null || cotisationAP.getLibelle().isEmpty()) {
            addMessage(SpecificationMessage.LIBELLE_COTISATION_AP_MANQUANT);
        }
        if (cotisationAP.getAssociationProfessionnelle().getId() == null
                || cotisationAP.getAssociationProfessionnelle().getId().isEmpty()) {
            addMessage(SpecificationMessage.ASSOCIATION_PROFESSIONELLE_COTISATION_AP_MANQUANT);
        }
        return true;
    }
}
