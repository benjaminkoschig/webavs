package ch.globaz.vulpecula.domain.specifications.parametrecotisationap;

import java.util.Arrays;
import java.util.List;
import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.registre.ParametreCotisationAssociation;
import ch.globaz.vulpecula.domain.models.registre.TypeParamCotisationAP;

public class ParametreCotisationAPMandatoriesSpecification extends
        AbstractSpecification<ParametreCotisationAssociation> {
    // TODO make it param
    private final List<String> listCSMontant = Arrays.asList(TypeParamCotisationAP.MONTANT_MIN.getValue(),
            TypeParamCotisationAP.MONTANT_MAX.getValue(), TypeParamCotisationAP.FORFAIT_FIX.getValue());

    private final List<String> listCSFacteur = Arrays.asList(TypeParamCotisationAP.FACTEUR.getValue());

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
        } else if (listCSFacteur.contains(cotisationAAjouter.getTypeParam().getValue())) {
            if (cotisationAAjouter.getFacteur() == null || cotisationAAjouter.getFacteur() <= 0) {
                addMessage(SpecificationMessage.PARAMETRE_COTISATION_AP_FACTEUR_MANQUANT);
            }
        } else {
            if (cotisationAAjouter.getTaux() == null || cotisationAAjouter.getTaux().isZero()) {
                addMessage(SpecificationMessage.PARAMETRE_COTISATION_AP_TAUX_MANQUANT);
            }
        }

        return true;
    }
}
