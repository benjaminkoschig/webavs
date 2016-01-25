package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeDateUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> </br><strong>Champs concerné(s)
 * :</strong></br> Si le champ « serviceType » = 20, 21, 22, 23 et la date de naissance issue du NAVS13 indique un âge <
 * 19 ans -> erreur.</br>
 * 
 * @author lga
 */
public class Rule316 extends Rule {

    /**
     * @param errorCode
     */
    public Rule316(String errorCode) {
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

        List<String> services = new ArrayList<String>();
        services.add("20");
        services.add("21");
        services.add("22");
        services.add("23");
        if (services.contains(serviceType)) {
            validNotEmpty(insurantBirthDate, "insurantBirthDate");
            if (JadeDateUtil.getNbYearsBetween(insurantBirthDate, startOfPeriod) < 19) {
                return false;
            }
        }

        return true;
    }

}
