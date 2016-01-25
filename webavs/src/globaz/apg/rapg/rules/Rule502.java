package globaz.apg.rapg.rules;

import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description : </strong></br> Si le champ « serviceType » =
 * 41 et que le début de la période de service « startOfPeriode » est > que l'année des 26 ans -> erreur</br> Si le
 * champ « serviceType » = 40 et que le début de la période de service « startOfPeriode » est > que l'année des 34 ans
 * -> erreur </br>
 * 
 * @author lga
 */
public class Rule502 extends Rule {

    /**
     * @param errorCode
     */
    public Rule502(String errorCode) {
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

        boolean isService41 = APGenreServiceAPG.ServiceCivilTauxRecrue.getCodePourAnnonce().equals(serviceType);
        boolean isService40 = APGenreServiceAPG.ServiceCivilNormal.getCodePourAnnonce().equals(serviceType);

        if (isService41 || isService40) {

            testDateNotEmptyAndValid(insurantBirthDate, "insurantBirthDate");
            testDateNotEmptyAndValid(startOfPeriod, "startOfPeriod");

            try {
                int startOfPeriodYear = JACalendar.getYear(startOfPeriod);
                int insurantBirthDateYear = JACalendar.getYear(insurantBirthDate);
                int age = startOfPeriodYear - insurantBirthDateYear;

                if (isService41 && (age > 26)) {
                    return false;
                } else if (isService40 && (age > 34)) {
                    return false;
                }
            } catch (JAException e) {
                throwRuleExecutionException(e);
            }

        }

        return true;
    }
}
