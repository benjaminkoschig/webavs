package globaz.apg.rapg.rules;

import java.math.BigDecimal;
import org.safehaus.uuid.Logger;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.properties.APParameter;
import globaz.globall.db.FWFindParameter;

/**
 * <strong>R?gles de validation des plausibilit?s RAPG des jours isol?s</br>
 * Description :</strong></br>
 * Si le champ ? serviceType ? = 508
 * et le champ ? numberOfDays ? est > la plage de valeur ISOLEDECED (normalement = 1) -> erreur </br>
 * <strong>Champs concern?(s) :</strong></br>
 *
 * @author mpe
 */
public class Rule60 extends Rule {

    /**
     * @param errorCode
     */
    public Rule60(String errorCode) {
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

        if (serviceType.equals(APGenreServiceAPG.DecesDemiJour.getCodePourAnnonce())) {
            try {
                BigDecimal nbJours = new BigDecimal(champsAnnonce.getNumberOfDays());
                BigDecimal valPlage = new BigDecimal(
                        FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(), "1",
                                APParameter.NOMBRE_JOURS_ISOLES_DECES_DEMI_JOUR.getParameterName(),
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
