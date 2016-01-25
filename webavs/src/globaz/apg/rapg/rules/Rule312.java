package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Si le champ « serviceType » =
 * 40, 41 (service civil), le champ « controlNumber » doit contenir un nombre à deux chiffres parmi les valeurs
 * suivantes : 11, 12, 13, 14, 15, 16, 17. </br><strong>Champs concerné(s) :</strong></br> serviceType</br>
 * controlNumber
 * 
 * @author lga
 */
public class Rule312 extends Rule {

    /**
     * @param errorCode
     */
    public Rule312(String errorCode) {
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
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        if ("40".equals(serviceType) || "41".equals(serviceType)) {

            // Et que ce n'est pas un duplicata
            if (!champsAnnonce.getBreakRules().contains("505")) {

                String controlNumber = champsAnnonce.getControlNumber();
                if (JadeStringUtil.isEmpty(controlNumber)) {
                    return false;
                }
                try {
                    if (controlNumber.length() < 2) {
                        return false;
                    }

                    int value = Integer.valueOf(controlNumber);
                    if ((value < 11) || (value > 17)) {
                        return false;
                    }
                } catch (NumberFormatException exception) {
                    throw new APRuleExecutionException(exception);
                }
            }
        }
        return true;
    }
}
