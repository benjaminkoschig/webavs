package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeDateUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br> Description :</strong></br> </br><strong>Champs concern�(s)
 * :</strong></br> Si le champ � serviceType � = 10, 11, 12, 13, 14, 40, 41 ou 50 et la date de naissance issue du
 * NAVS13
 * indique un �ge < 18 ans -> erreur.</br>
 * 
 * @author lga
 */
public class Rule315 extends Rule {

    /**
     * @param errorCode
     */
    public Rule315(String errorCode) {
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
        /**
         * Breakrule plus utilis�
         */
//        String serviceType = champsAnnonce.getServiceType();
//        String startOfPeriod = champsAnnonce.getStartOfPeriod();
//        String insurantBirthDate = champsAnnonce.getInsurantBirthDate();
//        int typeAnnonce = getTypeAnnonce(champsAnnonce);
//        if (typeAnnonce == 1) {
//            validNotEmpty(serviceType, "serviceType");
//            validNotEmpty(startOfPeriod, "startOfPeriod");
//        }
//
//        List<String> services = new ArrayList<String>();
//        services.add("10");
//        services.add("11");
//        services.add("12");
//        services.add("13");
//        services.add("14");
//        services.add("15");
//        services.add("16");
//        services.add("40");
//        services.add("41");
//        services.add("50");
//        if (services.contains(serviceType)) {
//            validNotEmpty(insurantBirthDate, "insurantBirthDate");
//            if (JadeDateUtil.getNbYearsBetween(insurantBirthDate, startOfPeriod) < 18) {
//                return false;
//            }
//        }

        return true;
    }

}
