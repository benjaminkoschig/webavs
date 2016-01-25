package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.globall.db.FWFindParameter;
import java.math.BigDecimal;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br>
 * Pour les recrues (et les services avec taux recrue), le registre vérifiera que l'allocation journalière de base
 * sélève à 25% du montant maximal de l'allocation total</br><strong>Champs concerné(s)
 * :</strong></br>
 * numberOfDays<br>
 * basicDailyAmount<br>
 * aAllowanceCareExpenses<br>
 * totalAPG<br>
 * allowanceFarm<br>
 * dailyIndemnityGuaranteeAI
 * 
 * @author sco
 * 
 */
public class Rule307 extends Rule {

    /**
     * @param errorCode
     */
    public Rule307(String errorCode) {
        super(errorCode, false);
    }

    protected boolean check(APChampsAnnonce champsAnnonce, BigDecimal montantMinime) throws APRuleExecutionException {
        boolean dailyIndemnityGuaranteeAI = champsAnnonce.getDailyIndemnityGuaranteeAI();
        boolean allowanceFarm = champsAnnonce.getAllowanceFarm();
        try {
            BigDecimal numberOfDays = new BigDecimal(champsAnnonce.getNumberOfDays());
            BigDecimal basicDailyAmount = new BigDecimal(champsAnnonce.getBasicDailyAmount());
            BigDecimal aAllowanceCareExpenses = new BigDecimal(champsAnnonce.getAllowanceCareExpenses());
            BigDecimal totalAPG = new BigDecimal(champsAnnonce.getTotalAPG());

            if (allowanceFarm) {
                BigDecimal calcul = (numberOfDays.multiply(basicDailyAmount.add(montantMinime)))
                        .add(aAllowanceCareExpenses);
                if (calcul.compareTo(totalAPG.subtract(new BigDecimal(1))) >= 0
                        && calcul.compareTo(totalAPG.add(new BigDecimal(1))) <= 0) {
                    return true;
                }
            }

            if (dailyIndemnityGuaranteeAI) {

                BigDecimal calcul = (numberOfDays.multiply(basicDailyAmount)).add(aAllowanceCareExpenses);
                if (calcul.compareTo(totalAPG.subtract(new BigDecimal(1))) >= 0
                        && calcul.compareTo(totalAPG.add(new BigDecimal(1))) <= 0) {
                    return true;
                }

            } else {

                BigDecimal calcul = (numberOfDays.multiply(basicDailyAmount)).add(aAllowanceCareExpenses);
                if (calcul.compareTo(totalAPG) == 0) {
                    return true;
                }

            }
        } catch (Exception exception) {
            throw new APRuleExecutionException(exception);
        }
        return false;
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {
        BigDecimal montantMinime;

        try {
            montantMinime = new BigDecimal(FWFindParameter.findParameter(getSession().getCurrentThreadTransaction(),
                    "1", "APGMINALEX", "", "", 0));
        } catch (Exception e) {
            throw new APRuleExecutionException(e);
        }

        return check(champsAnnonce, montantMinime);
    }
}
