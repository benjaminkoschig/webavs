package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Si le champ « accountingMonth »
 * est présent, le champ « totalAPG » doit être <> 0. </br><strong>Champs concerné(s) :</strong></br>
 * accountingMonth</br>totalAPG
 * 
 * @author lga
 */
public class Rule211 extends Rule {

    /**
     * @param errorCode
     */
    public Rule211(String errorCode) {
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
        if (!JadeStringUtil.isEmpty(champsAnnonce.getAccountingMonth())) {
            if (JadeStringUtil.isEmpty(champsAnnonce.getTotalAPG())
                    || JadeNumericUtil.isZeroValue(champsAnnonce.getTotalAPG())) {
                return false;
            }
        }
        return true;
    }
}
