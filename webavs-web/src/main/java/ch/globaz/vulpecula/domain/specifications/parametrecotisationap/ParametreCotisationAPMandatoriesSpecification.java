package ch.globaz.vulpecula.domain.specifications.parametrecotisationap;

import java.util.Arrays;
import java.util.List;
import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.registre.ParametreCotisationAssociation;

public class ParametreCotisationAPMandatoriesSpecification extends
        AbstractSpecification<ParametreCotisationAssociation> {
    //TODO make it param
    private final List<String> listCSMontant = Arrays
            .asList("68030001", "68030002", "68030005", "68030006", "68030007");

    @Override
    public boolean isValid(ParametreCotisationAssociation cotisationAAjouter) {
        if (cotisationAAjouter.getFourchetteDebut() == null) {
            addMessage(SpecificationMessage.PARAMETRE_COTISATION_AP_FOURCHETTE_MANQUANTE);
        }
        if (cotisationAAjouter.getFourchetteFin() == null) {
            addMessage(SpecificationMessage.PARAMETRE_COTISATION_AP_FOURCHETTE_MANQUANTE);
        }
        if (listCSMontant.contains(cotisationAAjouter.getTypeParam().getValue())) {
            if (cotisationAAjouter.getMontant() == null || cotisationAAjouter.getMontant().isZero()) {
                addMessage(SpecificationMessage.PARAMETRE_COTISATION_AP_MONTANT_MANQUANT);
            }
        } else {
            if (cotisationAAjouter.getTaux() == null || cotisationAAjouter.getTaux().isZero()) {
                addMessage(SpecificationMessage.PARAMETRE_COTISATION_AP_TAUX_MANQUANT);
            }
        }

        return true;
    }
}
