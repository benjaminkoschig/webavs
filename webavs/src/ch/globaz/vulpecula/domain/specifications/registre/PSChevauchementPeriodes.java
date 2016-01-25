package ch.globaz.vulpecula.domain.specifications.registre;

import java.util.List;
import ch.globaz.specifications.AbstractSpecification;
import ch.globaz.specifications.SpecificationMessage;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.syndicat.ParametreSyndicat;

public class PSChevauchementPeriodes extends AbstractSpecification<ParametreSyndicat> {
    private List<ParametreSyndicat> parametresSyndicats;

    public PSChevauchementPeriodes(List<ParametreSyndicat> parametresSyndicats) {
        this.parametresSyndicats = parametresSyndicats;
    }

    @Override
    public boolean isValid(ParametreSyndicat parametreSyndicat) {
        ParametreSyndicat parametreSyndicatChevauchant = parametreSyndicat.chevauche(parametresSyndicats);
        if (parametreSyndicatChevauchant != null) {
            String dateFin;
            if (parametreSyndicatChevauchant.getDateFin() == null) {
                dateFin = Date.DATE_INCONNU;
            } else {
                dateFin = parametreSyndicatChevauchant.getDateFin().toString();
            }
            addMessage(SpecificationMessage.PS_CHEVAUCHEMENT_AUTRE_PARAMETRE_SYNDICAT,
                    parametreSyndicatChevauchant.getId(), parametreSyndicatChevauchant.getDateDebut().toString(),
                    dateFin);
        }
        return true;
    }

}
