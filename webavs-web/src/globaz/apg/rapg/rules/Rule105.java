package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Une annonce rectificative (de
 * type 3/4) avec le champ « totalAPG » présents et <> 0, doit avoir le champ « accounting-Month » renseigné.
 * </br><strong>Champs concerné(s) :</strong></br> accountingMonth
 * 
 * @author lga
 */
public class Rule105 extends Rule {

    /**
     * @param errorCode
     */
    public Rule105(String errorCode) {
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
        if ((typeAnnonce == 3) || (typeAnnonce == 4)) {
            if (!JadeNumericUtil.isEmptyOrZero(champsAnnonce.getTotalAPG())) {
                if (JadeStringUtil.isEmpty(champsAnnonce.getAccountingMonth())) {
                    return false;
                }
            }
        }
        return true;
    }
}
