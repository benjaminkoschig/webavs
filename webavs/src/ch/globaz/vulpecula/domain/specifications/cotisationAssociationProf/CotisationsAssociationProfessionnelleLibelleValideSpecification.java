package ch.globaz.vulpecula.domain.specifications.cotisationAssociationProf;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.association.CotisationAssociationProfessionnelle;

/**
 * Specification obligeant l'absence justifi�e de poss�der une p�riode valide (date de d�but et date de fin renseign�e).
 * 
 */
public class CotisationsAssociationProfessionnelleLibelleValideSpecification extends
        AbstractSpecification<CotisationAssociationProfessionnelle> {
    private List<CotisationAssociationProfessionnelle> cotisationsList = new ArrayList<CotisationAssociationProfessionnelle>();

    public CotisationsAssociationProfessionnelleLibelleValideSpecification(
            List<CotisationAssociationProfessionnelle> cotisationsList) {
        this.cotisationsList = cotisationsList;
    }

    @Override
    public boolean isValid(CotisationAssociationProfessionnelle cotisationAP) {
        for (CotisationAssociationProfessionnelle cotisation : cotisationsList) {
            if (cotisation.getLibelle().equals(cotisationAP.getLibelle())) {
                if (!cotisation.getId().equals(cotisationAP.getId())) {
                    if (cotisationAP.getIdAssociationProfessionnelle().equals(
                            cotisation.getIdAssociationProfessionnelle())) {
                        addMessage(SpecificationMessage.LIBELLE_COTISATION_AP_EXISTANT);
                    }
                }
            }
        }
        return true;
    }
}
