package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.apg.pojo.APReferenceNumber;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import java.util.ArrayList;
import java.util.List;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Le début de service concernant
 * le service de troupe et le formation de base dans la protection civile (types de service 20 et 21) ne peut pas
 * intervenir après l'age de 30
 * ans</br><strong>Champs concerné(s) :</strong></br>
 * 
 * @author sco
 * 
 */
public class Rule302 extends Rule {

    /**
     * @param errorCode
     */
    public Rule302(String errorCode) {
        super(errorCode, false);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {

        String serviceType = champsAnnonce.getServiceType();

        List<String> serviceCivils = new ArrayList<String>();
        serviceCivils.add("20");
        serviceCivils.add("21");

        validNotEmpty(serviceType, "serviceType");

        if (serviceCivils.contains(serviceType)) {

            String referenceNumber = champsAnnonce.getReferenceNumber();
            APReferenceNumber refNumber = new APReferenceNumber();
            try {
                refNumber.setReferenceNumber(referenceNumber);
            } catch (Exception exception) {
                throw new APRuleExecutionException("Reference number seems invalid", exception);
            }

            // referenceNumber = cas 1
            if (refNumber.getCas() == 1) {

                String startOfPeriod = champsAnnonce.getStartOfPeriod();
                String insurantBirthDate = champsAnnonce.getInsurantBirthDate();

                testDateNotEmptyAndValid(insurantBirthDate, "insurantBirthDate");
                testDateNotEmptyAndValid(startOfPeriod, "startOfPeriod");

                try {
                    int startOfPeriodYear = JACalendar.getYear(startOfPeriod);
                    int insurantBirthDateYear = JACalendar.getYear(insurantBirthDate);
                    int age = startOfPeriodYear - insurantBirthDateYear;

                    if (age > 30) {
                        return false;
                    }
                } catch (JAException e) {
                    throwRuleExecutionException(e);
                }

            }

        }

        return true;
    }

}
