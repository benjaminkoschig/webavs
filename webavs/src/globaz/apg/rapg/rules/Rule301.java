package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Le champ « numberOfChildren »
 * doit être vide (ou pas annoncé) si le serviceType = 90 ; sinon, il doit être >= 0. </br> <strong>Champs concerné(s)
 * :</strong></br> serviceType</br> numberOfChildren
 * 
 * @author lga
 */
public class Rule301 extends Rule {

    /**
     * @param errorCode
     */
    public Rule301(String errorCode) {
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
        String serviceType = champsAnnonce.getServiceType();
        String numberOfChildren = champsAnnonce.getNumberOfChildren();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }
        if (!JadeStringUtil.isEmpty(numberOfChildren)) {
            if ("90".equals(champsAnnonce.getServiceType())) {
                if (!JadeStringUtil.isEmpty(numberOfChildren)) {
                    return false;
                } else {
                    return true;
                }
            } else {
                if (JadeNumericUtil.isZeroValue(numberOfChildren)) {
                    return true;
                } else if (JadeNumericUtil.isIntegerPositif(numberOfChildren)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }
}
