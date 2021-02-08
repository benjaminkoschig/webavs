package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Si le champ « serviceType »
 * (genre de service) <> 90 et 30, la date indiquée dans le champ « startOfPeriod » (date de début de la période de
 * service) doit être postérieure de 18 ans à la date de naissance de la personne assurée. </br><strong>Champs
 * concerné(s) :</strong></br> serviceType</br> startOfPeriod
 * 
 * @author lga
 */
public class Rule310 extends Rule {

    /**
     * @param errorCode
     */
    public Rule310(String errorCode) {
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
        String startOfPeriod = champsAnnonce.getStartOfPeriod();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
            validNotEmpty(startOfPeriod, "startOfPeriod");
        }

        if ("30".equals(serviceType) || "90".equals(serviceType) || "91".equals(serviceType)) {
            return true;
        }
        if (!JadeStringUtil.isEmpty(champsAnnonce.getInsurantBirthDate()) && !JadeStringUtil.isEmpty(startOfPeriod)) {
            if (JadeDateUtil.getNbYearsBetween(champsAnnonce.getInsurantBirthDate(), startOfPeriod) < 18) {
                return false;
            }
        }
        return true;
    }
}
