package globaz.apg.rapg.rules;

import java.math.BigDecimal;
import org.safehaus.uuid.Logger;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.properties.APParameter;
import globaz.globall.db.FWFindParameter;

/**
 * <strong>Règles de validation des plausibilités RAPG des jours isolés</br>
 * Description :</strong></br>
 * Si le champ « serviceType » = 501
 * et le champ « numberOfDays » est > la plage de valeur ISOLEDEMEN (normalement = 1) -> erreur </br>
 * <strong>Champs concerné(s) :</strong></br>
 *
 * @author mpe
 */
public class Rule10008 extends Rule {

    /**
     * @param errorCode
     */
    public Rule10008(String errorCode) {
        super(errorCode, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.globaz.apg.businessimpl.plausibilites.Rule#check(ch.globaz.apg.business.models.plausibilites.ChampsAnnonce)
     */
    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {
        String serviceType = champsAnnonce.getServiceType();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        if (serviceType.equals(APGenreServiceAPG.InspectionRecrutementLiberation.getCodePourAnnonce())) {
            try {
                BigDecimal nbJours = new BigDecimal(champsAnnonce.getNumberOfDays());
                BigDecimal valPlage = new BigDecimal(
                        FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(), "1",
                                APParameter.NOMBRE_JOURS_ISOLES_INSPECTION_RECRUTEMENT_LIBERATION.getParameterName(),
                                champsAnnonce.getStartOfPeriod(), "", 0));
                if (nbJours.compareTo(valPlage) >= 1) {
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {
                Logger.logError("Error ");
            }
        }

        return true;
    }

}
