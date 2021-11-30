package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    List<String> services = new ArrayList<>(Arrays.asList("90", "91", "92"));

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
        if (getTypeAnnonce(champsAnnonce) == 1) {
            validNotEmpty(serviceType, "serviceType");
        }
        if (!JadeStringUtil.isEmpty(numberOfChildren)) {
            if (services.contains(serviceType)) {
                return JadeStringUtil.isEmpty(numberOfChildren);
            } else {
                return JadeNumericUtil.isZeroValue(numberOfChildren) || JadeNumericUtil.isIntegerPositif(numberOfChildren);
            }
        }
        return true;
    }
}
