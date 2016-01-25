package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.prestation.utils.DateException;
import globaz.prestation.utils.PRDateUtils;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Si le champ « serviceType » = 14
 * et l'âge calculé à partir de la date de naissance indiquée dans le NAVS13 > 30 ans -> erreur </br><strong>Champs
 * concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule501 extends Rule {

    /**
     * @param errorCode
     */
    public Rule501(String errorCode) {
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
        String startOfPeriod = champsAnnonce.getStartOfPeriod();
        String insurantBirthDate = champsAnnonce.getInsurantBirthDate();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        if ("14".equals(serviceType)) {
            testDateNotEmptyAndValid(insurantBirthDate, "insurantBirthDate");
            testDateNotEmptyAndValid(startOfPeriod, "startOfPeriod");
            try {
                int age = PRDateUtils.getAge(insurantBirthDate, startOfPeriod);
                if (age > 30) {
                    return false;
                }
            } catch (DateException e) {
                throwRuleExecutionException(e);
            }
        }
        return true;
    }

}
