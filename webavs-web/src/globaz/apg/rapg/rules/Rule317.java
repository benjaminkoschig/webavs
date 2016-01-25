package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeDateUtil;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> </br><strong>Champs concerné(s)
 * :</strong></br> Si le champ « serviceType » = 30 et la date de naissance issue du NAVS13 indique un âge < 17 ans ->
 * erreur.</br>
 * 
 * @author lga
 */
public class Rule317 extends Rule {

    /**
     * @param errorCode
     */
    public Rule317(String errorCode) {
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
        String insurantBirthDate = champsAnnonce.getInsurantBirthDate();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
            validNotEmpty(startOfPeriod, "startOfPeriod");
        }

        if ("30".equals(serviceType)) {
            validNotEmpty(insurantBirthDate, "insurantBirthDate");
            if (JadeDateUtil.getNbYearsBetween(insurantBirthDate, startOfPeriod) < 17) {
                return false;
            }
        }

        return true;
    }

}
