package globaz.apg.rapg.rules;

import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.enums.PRCanton;
import globaz.pyxis.constantes.IConstantes;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> </br><strong>Champs concerné(s)
 * :</strong></br> Si l'assuré est domicilié en Suisse, le canton doit être communiqué.</br>
 * 
 * @author lga
 */
public class Rule319 extends Rule {

    /**
     * @param errorCode
     */
    public Rule319(String errorCode) {
        super(errorCode, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.apg.businessimpl.plausibilites.Rule#check(ch.globaz.apg.business.models.plausibilites.ChampsAnnonce)
     */
    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws IllegalArgumentException, APRuleExecutionException {
        if (IConstantes.ID_PAYS_SUISSE.equals(champsAnnonce.getInsurantDomicileCountry())) {
            if (JadeStringUtil.isEmpty(champsAnnonce.getInsurantDomicileCanton())) {
                return false;
            }
            // Le passage sous forme de 'int' est fait pour supprimer les 0 à gauche
            int codeCanton = 0;
            try {
                codeCanton = Integer.valueOf(champsAnnonce.getInsurantDomicileCanton());
            } catch (NumberFormatException exception) {
                throw new APRuleExecutionException(exception);
            }
            if (PRCanton.getCanton(String.valueOf(codeCanton)) == null) {
                return false;
            }
        }
        return true;
    }

}
