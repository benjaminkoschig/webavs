package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Le champ « timeStamp » doit être
 * présent et unique pour la caisse ayant fait lannonce si celle-ci est de type 3 ou 4. </br><strong>Champs concerné(s)
 * :</strong></br> timeStamp
 * 
 * @author lga
 */
public class Rule104 extends Rule {

    /**
     * @param errorCode
     */
    public Rule104(String errorCode) {
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
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if ((typeAnnonce == 3) || (typeAnnonce == 4)) {
            String timeStamp = champsAnnonce.getTimeStamp();
            validNotEmpty(timeStamp, "timeStamp");
            return isTimeStampUnique(timeStamp, getSession());
        }
        return true;
    }
}
