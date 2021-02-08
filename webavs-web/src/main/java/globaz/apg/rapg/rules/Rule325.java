package globaz.apg.rapg.rules;

import ch.globaz.common.properties.PropertiesException;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.exceptions.APWebserviceException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;

import java.math.BigDecimal;

/**
 * Si le champ « serviceType » =  90, 91 ou 92 et
 * le champ «allowanceFarm» = vrai ou
 * le champ «allowanceCareExpenses» >0
 *                 ? erreur
 */
public class Rule325 extends Rule {
    public Rule325(String errorCode) {
        super(errorCode, false);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException, APWebserviceException, PropertiesException {
        String serviceType = champsAnnonce.getServiceType();
        boolean allowanceFarm = champsAnnonce.getAllowanceFarm();
        BigDecimal aAllowanceCareExpenses = new BigDecimal(champsAnnonce.getAllowanceCareExpenses());
        int typeAnnonce = getTypeAnnonce(champsAnnonce);

        if (typeAnnonce == 1) {
            validNotEmpty(serviceType, "serviceType");
        }

        if ((serviceType.equals(APGenreServiceAPG.Maternite.getCodePourAnnonce())
                || (serviceType.equals(APGenreServiceAPG.Paternite.getCodePourAnnonce()))
                || (serviceType.equals("92")))) {
            if (allowanceFarm == true ||  aAllowanceCareExpenses.doubleValue()>0.0){
                return false;
            }
        }
        return true;

    }
}
