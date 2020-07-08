package globaz.apg.rapg.rules;

import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> </br><strong>Champs concerné(s)
 * :</strong></br> Si la caisse fait une annonce de type 1, elle ne peut pas communiquer en même temps le breakRuleCode
 * 509 (double paiement).</br>
 * 
 * @author lga
 */
public class Rule318 extends Rule {

    private static final String RULE_509 = "509";

    /**
     * @param errorCode
     */
    public Rule318(String errorCode) {
        super(errorCode, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.apg.businessimpl.plausibilites.Rule#check(ch.globaz.apg.business.models.plausibilites.ChampsAnnonce)
     */
    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws IllegalArgumentException {
        int typeAnnonce = getTypeAnnonce(champsAnnonce);
        if (typeAnnonce == 1) {
            if (!JadeStringUtil.isEmpty(champsAnnonce.getBreakRules()) && hasPeriodesChevauvent(champsAnnonce.getBreakRules())) {
                return false;
            }
        }
        return true;
    }

    private boolean hasPeriodesChevauvent(String breakrules){
        String[] values = breakrules.split(",");
        for (String breakrule : values) {
            if(RULE_509.equals(breakrule)){
                return true;
            }
        }
        return false;
    }

}
