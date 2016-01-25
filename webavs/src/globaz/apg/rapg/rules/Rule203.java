package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeNumericUtil;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br> Description :</strong></br> Le champ � numberOfDays � doit
 * �tre >= 0 pour une annonce de type 1. </br><strong>Champs concern�(s) :</strong></br> numberOfDays
 * 
 * @author lga
 */
public class Rule203 extends Rule {

    /**
     * @param errorCode
     */
    public Rule203(String errorCode) {
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
            String numberOfDays = champsAnnonce.getNumberOfDays();
            if (JadeNumericUtil.isZeroValue(numberOfDays)) {
                return true;
            }
            if (!JadeNumericUtil.isNumericPositif(numberOfDays)) {
                return false;
            }
        }
        return true;
    }
}
