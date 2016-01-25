package ch.globaz.vulpecula.domain.specifications.registre;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.syndicat.ParametreSyndicat;

public class PSMontantParTravailleurCorrecte extends AbstractSpecification<ParametreSyndicat> {

    @Override
    public boolean isValid(ParametreSyndicat parametreSyndicat) {
        if (parametreSyndicat.getMontantParTravailleur() == null) {
            addMessage(SpecificationMessage.PS_MONTANT_PAR_TRAVAILLEUR_PRESENT);
        } else if (parametreSyndicat.getMontantParTravailleur().isNegative()) {
            addMessage(SpecificationMessage.PS_MONTANT_PAR_TRAVAILLEUR_NEGATIF);
        }
        return true;
    }

}
