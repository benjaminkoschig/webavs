package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeNumericUtil;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Si le champ «
 * allowanceCareExpenses » (montant de l’allocation pour frais de garde) > 0, le champ « numberOf-Children » (nombre
 * d’enfants) ne peut pas être égal à 0.</br><strong>Champs concerné(s) :</strong></br> numberOfChildren</br>
 * allowanceCareExpenses
 * 
 * @author lga
 */
public class Rule309 extends Rule {

    /**
     * @param errorCode
     */
    public Rule309(String errorCode) {
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
        if (!JadeNumericUtil.isEmptyOrZero(champsAnnonce.getAllowanceCareExpenses())) {
            if (JadeNumericUtil.isEmptyOrZero(champsAnnonce.getNumberOfChildren())) {
                return false;
            }
        }
        return true;
    }
}
