package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br> Description :</strong></br> Le code du pays doit
 * correspondre aux nombres-cl�s des Etats. </br><strong>Champs concern�(s) :</strong></br> insurantDomicile</br>country
 * 
 * @author lga
 */
public class Rule201 extends Rule {

    /**
     * @param errorCode
     */
    public Rule201(String errorCode) {
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
        String insurantDomicileCountry = champsAnnonce.getInsurantDomicileCountry();
        if (!JadeStringUtil.isEmpty(insurantDomicileCountry)) {
            return isCodePaysExistant(insurantDomicileCountry, getSession());
        }
        return true;
    }

}
