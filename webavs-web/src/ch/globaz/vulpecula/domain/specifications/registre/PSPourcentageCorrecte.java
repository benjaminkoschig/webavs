package ch.globaz.vulpecula.domain.specifications.registre;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.syndicat.ParametreSyndicat;

public class PSPourcentageCorrecte extends AbstractSpecification<ParametreSyndicat> {

    @Override
    public boolean isValid(ParametreSyndicat parametreSyndicat) {
        if (parametreSyndicat.getPourcentage() == null || parametreSyndicat.getPourcentage().greaterThan(100)) {
            addMessage(SpecificationMessage.PS_POURCENTAGE_NON_VALIDE);
        }
        return true;
    }

}
