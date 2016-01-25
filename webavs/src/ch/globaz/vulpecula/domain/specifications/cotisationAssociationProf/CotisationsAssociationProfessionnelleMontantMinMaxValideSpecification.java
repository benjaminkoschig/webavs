package ch.globaz.vulpecula.domain.specifications.cotisationAssociationProf;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;

/**
 * Specification obligeant l'absence justifi�e de poss�der une p�riode valide (date de d�but et date de fin renseign�e).
 * 
 */
public class CotisationsAssociationProfessionnelleMontantMinMaxValideSpecification extends
        AbstractSpecification<CotisationAssociationProfessionnelle> {

    @Override
    public boolean isValid(CotisationAssociationProfessionnelle cotisationAP) {

        if (cotisationAP.getMontantBase().isNegative() || cotisationAP.getMontantMinimum().isNegative()
                || cotisationAP.getMontantMaximum().isNegative()) {
            addMessage(SpecificationMessage.MONTANT_COTISATION_AP_NEGATIF);
        } else {
            if (cotisationAP.getMontantMinimum().greater(cotisationAP.getMontantMaximum())) {
                addMessage(SpecificationMessage.COTISATION_AP_MONTANT_MIN_MAX);
            }
        }

        return true;
    }
}
