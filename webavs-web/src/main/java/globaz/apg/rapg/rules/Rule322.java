package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeDateUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> </br><strong>Champs concerné(s)
 * :</strong></br> Si le champ « serviceType » = 15 et/ou 16 et que l'activité n'est pas Independant et/ou Non actif
 * que la méthode de paiement ne soit pas à l'employeur ni réparti entre assureur et personne assué, qu'il n'y ait 
 * pas d'allocation d'exploitation et qu'il n'y ait pas d'allocation de frais de garde
 * 
 * 
 * @author eniv
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
        String allowanceCareExpenses = champsAnnonce.getAllowanceCareExpenses();
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
            if ((allowanceFarm == true) || !(allowanceCareExpenses.equals("0"))) {
                return false;
            }
        }
        return true;
    }

}
