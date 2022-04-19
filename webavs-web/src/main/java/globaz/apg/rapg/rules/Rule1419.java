package globaz.apg.rapg.rules;

import ch.globaz.common.properties.PropertiesException;
import ch.globaz.common.util.Dates;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.exceptions.APWebserviceException;
import globaz.apg.pojo.APChampsAnnonce;

import java.time.LocalDate;

/**
 * Suite à l'importation d'un formulaire paternité : Si période de droit débutant avant la naissance de l'enfant,
 * afficher breakrule bloquante 1419
 */
public class Rule1419 extends Rule {

    public Rule1419(String errorCode) {
        super(errorCode, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.apg.businessimpl.plausibilites.Rule#check(ch.globaz.apg.business.models.plausibilites.ChampsAnnonce)
     */
    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException, APWebserviceException, PropertiesException {
        if(APGenreServiceAPG.Paternite.getCodePourAnnonce().equals(champsAnnonce.getServiceType())) {
            LocalDate dateNaissance = Dates.toDate(champsAnnonce.getNewbornDateOfBirth());
            LocalDate dateDebut = Dates.toDate(champsAnnonce.getStartOfPeriod());
            return dateNaissance.isBefore(dateDebut);
        }
        return true;
    }


}
