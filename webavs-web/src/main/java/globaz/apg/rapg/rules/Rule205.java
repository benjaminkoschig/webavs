package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import java.math.BigDecimal;

/**
 * <strong>R?gles de validation des plausibilit?s RAPG</br> Description :</strong></br> Le montant total de l?APG doit
 * ?tre > 0 pour une annonce de type 1. </br><strong>Champs concern?(s) :</strong></br> totalAPG
 * 
 * @author lga
 */
public class Rule205 extends Rule {

    /**
     * @param errorCode
     */
    public Rule205(String errorCode) {
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
                BigDecimal value = new BigDecimal(champsAnnonce.getTotalAPG());
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
