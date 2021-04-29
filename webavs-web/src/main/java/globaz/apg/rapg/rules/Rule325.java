package globaz.apg.rapg.rules;

import ch.globaz.common.properties.PropertiesException;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.exceptions.APWebserviceException;
import globaz.apg.pojo.APChampsAnnonce;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Si le champ « serviceType » =  90, 91 ou 92 et
 * le champ «allowanceFarm» = vrai ou
 * le champ «allowanceCareExpenses» >0
 *                 ? erreur
 */
public class Rule325 extends Rule {

    private static final List<String> SERVICES_TO_APPLY_RULE = Stream.of(
            APGenreServiceAPG.Maternite.getCodePourAnnonce(),
            APGenreServiceAPG.Paternite.getCodePourAnnonce(),
            APGenreServiceAPG.ProcheAidant.getCodePourAnnonce()
    ).collect(Collectors.toList());

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

        if (SERVICES_TO_APPLY_RULE.contains(serviceType)) {
            if (allowanceFarm ||  aAllowanceCareExpenses.doubleValue() > 0.0){
                return false;
            }
        }
        return true;
    }
}
