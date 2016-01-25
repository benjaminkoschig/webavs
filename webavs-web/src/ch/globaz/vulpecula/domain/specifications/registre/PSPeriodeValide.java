package ch.globaz.vulpecula.domain.specifications.registre;

import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.common.Periode;
import ch.globaz.vulpecula.domain.models.syndicat.ParametreSyndicat;

public class PSPeriodeValide extends AbstractSpecification<ParametreSyndicat> {

    @Override
    public boolean isValid(ParametreSyndicat parametreSyndicat) {
        if (parametreSyndicat.getDateDebut() == null) {
            addMessage(SpecificationMessage.PS_DATE_DEBUT_NON_VALIDE);
        } else if (!Periode.isValid(parametreSyndicat.getDateDebut(), parametreSyndicat.getDateFin())) {
            addMessage(SpecificationMessage.PS_DATE_FIN_APRES_DATE_DEBUT);
        }
        return true;
    }

}
