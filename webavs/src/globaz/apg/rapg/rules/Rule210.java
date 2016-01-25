package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Pas de paiement anticipé :</br>
 * [x1] = mois comptable</br> [x2] = fin de la période d’indemnisation</br> [x1] - 5 ans <= [x2] <= [x1]</br>
 * </br><strong>Champs concerné(s) :</strong></br> accountingMonth</br>endOfPeriod
 * 
 * @author lga
 */
public class Rule210 extends Rule {

    /**
     * @param errorCode
     */
    public Rule210(String errorCode) {
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
        String x1 = champsAnnonce.getAccountingMonth();
        String endOfPeriod = champsAnnonce.getEndOfPeriod().substring(3);
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(x1, "accountingMonth");
            validNotEmpty(endOfPeriod, "endOfPeriod");
        }

        if (!JadeStringUtil.isEmpty(x1) && !JadeStringUtil.isEmpty(endOfPeriod)) {
            // String x2 = endOfPeriod.substring(3);
            String x1moins5ans = JadeDateUtil.addYears("01." + x1, -5).substring(3);
            if (JadeDateUtil.isDateMonthYearBefore(endOfPeriod, x1moins5ans)) {
                return false;
            }
        }
        return true;
    }
}
