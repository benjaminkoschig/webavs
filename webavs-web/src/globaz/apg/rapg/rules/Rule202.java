package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeNumericUtil;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br> Description :</strong></br> S�il est communiqu�, le revenu
 * journalier moyen doit �tre positif. </br><strong>Champs concern�(s) :</strong></br> averageDailyIncome
 * 
 * @author lga
 */
public class Rule202 extends Rule {

    /**
     * @param errorCode
     */
    public Rule202(String errorCode) {
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
        String averageDailyIncome = champsAnnonce.getAverageDailyIncome();
        if (!JadeNumericUtil.isEmptyOrZero(averageDailyIncome)) {
            if (!JadeNumericUtil.isNumericPositif(averageDailyIncome)
                    && !JadeNumericUtil.isZeroValue(averageDailyIncome)) {
                return false;
            }
        }
        return true;
    }
}
