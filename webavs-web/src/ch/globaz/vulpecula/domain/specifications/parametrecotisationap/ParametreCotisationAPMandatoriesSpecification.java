package ch.globaz.vulpecula.domain.specifications.parametrecotisationap;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.registre.ParametreCotisationAssociation;

public class ParametreCotisationAPMandatoriesSpecification extends
        AbstractSpecification<ParametreCotisationAssociation> {
    @Override
    public boolean isValid(ParametreCotisationAssociation cotisationAAjouter) {
        if (cotisationAAjouter.getFourchetteDebut() == null) {
            addMessage(SpecificationMessage.PARAMETRE_COTISATION_AP_FOURCHETTE_MANQUANTE);
        }
        if (cotisationAAjouter.getFourchetteFin() == null) {
            addMessage(SpecificationMessage.PARAMETRE_COTISATION_AP_FOURCHETTE_MANQUANTE);
        }
        if (cotisationAAjouter.getTaux() == null || cotisationAAjouter.getTaux().isZero()) {
            addMessage(SpecificationMessage.PARAMETRE_COTISATION_AP_TAUX_MANQUANT);
        }
        return true;
    }
}
