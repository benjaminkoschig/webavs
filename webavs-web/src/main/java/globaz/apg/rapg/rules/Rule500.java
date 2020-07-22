package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.prestation.utils.DateException;
import globaz.prestation.utils.PRDateUtils;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br> Description :</strong></br> Si le champ � serviceType � =
 * 11, 13 et l'�ge calcul� � partir de la date de naissance indiqu�e dans le NAVS13 > 26 ans -> erreur
 * </br><strong>Champs concern�(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule500 extends Rule {

    /**
     * @param errorCode
     */
    public Rule500(String errorCode) {
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

        if ("11".equals(serviceType) || "13".equals(serviceType)) {
            testDateNotEmptyAndValid(insurantBirthDate, "insurantBirthDate");
            testDateNotEmptyAndValid(startOfPeriod, "startOfPeriod");
            try {
                int age = PRDateUtils.getAge(insurantBirthDate, startOfPeriod);
                if (age > 25) {
                    return false;
                }
            } catch (DateException e) {
                throwRuleExecutionException(e);
            }
        }
        return true;
    }
}
