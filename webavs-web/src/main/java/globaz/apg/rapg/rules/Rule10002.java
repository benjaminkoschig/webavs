package globaz.apg.rapg.rules;

import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeDateUtil;
import globaz.prestation.utils.DateException;
import globaz.prestation.utils.PRDateUtils;

/**
 * <strong>Règles de validation des plausibilités RAPG</br>
 * Description :</strong></br>
 * Si le champ « serviceType » = 15
 * et/ou 16 et le champ « numberOfDays » est > 42 jours -> erreur </br>
 * <strong>Champs concerné(s) :</strong></br>
 *
 * @author eniv
 */
public class Rule10002 extends Rule {

    private static final int NB_MAX_AGE = 30;

    /**
     * @param errorCode
     */
    public Rule10002(String errorCode) {
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
        String endOfPeriod = champsAnnonce.getEndOfPeriod();
        String insurantBirthDate = champsAnnonce.getInsurantBirthDate();
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        if (serviceType.equals(APGenreServiceAPG.CongeJeunesse.getCodePourAnnonce())) {
            testDateNotEmptyAndValid(insurantBirthDate, "insurantBirthDate");
            testDateNotEmptyAndValid(endOfPeriod, "endOfPeriod");
            try {
                int age = PRDateUtils.getAge(insurantBirthDate, endOfPeriod);
                if (age > NB_MAX_AGE) {
                    return false;
                }
                if (age == NB_MAX_AGE) {
                    String dateAutorise = JadeDateUtil.getLastDateOfMonth(insurantBirthDate).substring(0,
                            insurantBirthDate.length() - 4) + endOfPeriod.substring(6);
                    if (JadeDateUtil.isDateAfter(endOfPeriod, dateAutorise)) {
                        return false;
                    }

                }
            } catch (DateException e) {
                throwRuleExecutionException(e);
            }
        }
        return true;
    }

}
