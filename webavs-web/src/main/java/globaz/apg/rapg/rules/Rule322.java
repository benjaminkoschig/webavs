package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeDateUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> </br><strong>Champs concerné(s)
 * :</strong></br> Si le champ « serviceType » = 10, 11, 12, 13, 14, 40, 41 ou 50 et la date de naissance issue du
 * NAVS13
 * indique un âge < 18 ans -> erreur.</br>
 * 
 * @author lga
 */
public class Rule322 extends Rule {

    /**
     * @param errorCode
     */
    public Rule322(String errorCode) {
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
        String activityBeforeService = champsAnnonce.getActivityBeforeService();
        String paymentMethod = champsAnnonce.getPaymentMethod();
        Boolean allowanceFarm = champsAnnonce.getAllowanceFarm();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        List<String> services = new ArrayList<String>();
        services.add("15");
        services.add("16");
        if (services.contains(serviceType)) {
            validNotEmpty(paymentMethod, "paymentMethod");
            validNotEmpty(activityBeforeService, "activityBeforeService");
            if ((activityBeforeService.equals("2")) || (activityBeforeService.equals("3"))) {
                return false;
            }
            if ((paymentMethod.equals("2")) || (paymentMethod.equals("3"))) {
                return false;
            }
            if (allowanceFarm == true) {
                return false;
            }
        }

        return true;
    }

}
