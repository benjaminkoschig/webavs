package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description : </strong></br>L’année indiquée dans le champ «
 * startOfPeriod » (début de la période de service) doit être postérieure à la date de naissance, obtenue via le champ
 * réservé au NAVS13. </br><strong>Champs concerné(s) :</strong></br> startOfPeriod</br>
 * insurant/personIdentificationType/vn
 * 
 * @author lga
 */
public class Rule303 extends Rule {

    /**
     * @param errorCode
     */
    public Rule303(String errorCode) {
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
        if (!JadeStringUtil.isEmpty(champsAnnonce.getStartOfPeriod())
                && !JadeStringUtil.isEmpty(champsAnnonce.getInsurantBirthDate())) {
            Integer anneeStart = new Integer(champsAnnonce.getStartOfPeriod().substring(6));
            Integer anneeNaissance = new Integer(champsAnnonce.getInsurantBirthDate().substring(6));
            if (!(anneeStart > anneeNaissance)) {
                return false;
            }
        }
        return true;
    }
}
