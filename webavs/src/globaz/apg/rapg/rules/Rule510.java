package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Si le champ « serviceType » = 40
 * et/ou 41 et le champ « numberOfDays » est > 300 jours -> erreur </br><strong>Champs concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule510 extends Rule {

    /**
     * @param errorCode
     */
    public Rule510(String errorCode) {
        super(errorCode, true);
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

        List<String> services = new ArrayList<String>();
        services.add("40");
        services.add("41");

        if (services.contains(serviceType)) {
            int totalDeJours = 0;
            try {
                if (!JadeStringUtil.isEmpty(champsAnnonce.getNumberOfDays())) {
                    totalDeJours += Integer.valueOf(champsAnnonce.getNumberOfDays());
                }
            } catch (NumberFormatException e) {
                throw new APRuleExecutionException("Exception during reading the number of days in RAPG Rule 510", e);
            }

            if (totalDeJours > 300) {
                return false;
            }
        }
        return true;
    }
}
