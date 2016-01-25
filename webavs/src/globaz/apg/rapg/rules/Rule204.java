package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import java.math.BigDecimal;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Le taux journalier doit être >=
 * 0 pour une annonce de type 1. </br><strong>Champs concerné(s) :</strong></br> basicDailyAmount
 * 
 * @author lga
 */
public class Rule204 extends Rule {

    /**
     * @param errorCode
     */
    public Rule204(String errorCode) {
        super(errorCode, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.apg.businessimpl.plausibilites.Rule#check(ch.globaz.apg.business.models.plausibilites.ChampsAnnonce)
     */
    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            try {
                BigDecimal value = new BigDecimal(champsAnnonce.getBasicDailyAmount());
                if (value.compareTo(new BigDecimal("0")) == -1) {
                    return false;
                }
            } catch (Exception exception) {
                throw new APRuleExecutionException(exception);
            }
        }
        return true;
    }
}
