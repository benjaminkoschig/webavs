package ch.globaz.vulpecula.domain.specifications.parametrecotisationap;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.registre.ParametreCotisationAssociation;

/**
 * Specification empêchant le chevauchement de fourchette de paramCotiAP par type
 * 
 */
public class ParametreCotisationAPFourchetteValideSpecification extends
        AbstractSpecification<ParametreCotisationAssociation> {
    private List<ParametreCotisationAssociation> cotisationsList = new ArrayList<ParametreCotisationAssociation>();

    public ParametreCotisationAPFourchetteValideSpecification(List<ParametreCotisationAssociation> cotisationsList) {
        this.cotisationsList = cotisationsList;
    }

    @Override
    public boolean isValid(ParametreCotisationAssociation parametreAAjouter) {
        float fourchetteDebut = parametreAAjouter.getFourchetteDebut().getBigDecimalValue().floatValue();
        float fourchetteFin = parametreAAjouter.getFourchetteFin().getBigDecimalValue().floatValue();

        for (ParametreCotisationAssociation parametreAComparer : cotisationsList) {
            if (!parametreAComparer.getId().equals(parametreAAjouter.getId())) {
                if (!parametreAComparer.getIdCotisationAssociationProfessionnelle().equals(
                        parametreAAjouter.getIdCotisationAssociationProfessionnelle())) {
                    continue;
                }
                if (!parametreAComparer.getTypeParam().getValue().equals(parametreAAjouter.getTypeParam().getValue())) {
                    continue;
                }

                float fDebut = parametreAComparer.getFourchetteDebut().getBigDecimalValue().floatValue();
                float fFin = parametreAComparer.getFourchetteFin().getBigDecimalValue().floatValue();
                if (fourchetteDebut > fourchetteFin) {
                    addMessage(SpecificationMessage.PARAMETRE_COTISATION_AP_FOURCHETTE_INVALIDE);
                } else if (fDebut == fourchetteDebut || fFin == fourchetteFin) {
                    addMessage(SpecificationMessage.PARAMETRE_COTISATION_AP_FOURCHETTE_CHEVAUCHANTE);
                } else if (fDebut < fourchetteDebut && fFin > fourchetteDebut) {
                    addMessage(SpecificationMessage.PARAMETRE_COTISATION_AP_FOURCHETTE_CHEVAUCHANTE);
                } else if (fDebut < fourchetteFin && fFin > fourchetteFin) {
                    addMessage(SpecificationMessage.PARAMETRE_COTISATION_AP_FOURCHETTE_CHEVAUCHANTE);
                } else if (fDebut > fourchetteDebut && fFin < fourchetteFin) {
                    addMessage(SpecificationMessage.PARAMETRE_COTISATION_AP_FOURCHETTE_CHEVAUCHANTE);
                }
            }
        }
        return true;
    }
}
