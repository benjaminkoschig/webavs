package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br> Description : Si le champ � serviceType � (genre de service)
 * = 13, le champ � numberOfDays � (nombre de jours de solde) ne peut pas �tre sup�rieur � 5.</strong></br>
 * </br><strong>Champs concern�(s) :</strong></br> serviceType</br> numberOfDays
 * 
 * @author lga
 */
public class Rule405 extends Rule {

    /**
     * @param errorCode
     */
    public Rule405(String errorCode) {
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

        if (!JadeStringUtil.isEmpty(serviceType) && "13".equals(serviceType)) {
            if (!JadeStringUtil.isEmpty(champsAnnonce.getNumberOfDays())
                    && ((new Integer(champsAnnonce.getNumberOfDays())) > 5)) {
                return false;
            }
        }
        return true;
    }
}
