package globaz.apg.rapg.rules;

import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APTypeProtectionCivile;
import java.util.Arrays;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Période de contrôle : 5 années
 * civiles Si le champ « serviceType » = 22, le champ « referenceNumber » = cas 2 (instruction des cadres, cours de
 * perfectionnement, instruction complémentaire </br><strong>Champs concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule404 extends RuleInterAPG {

    public Rule404(String errorCode) {
        super(errorCode, false, Arrays.asList(APGenreServiceAPG.ProtectionCivileCadreSpecialiste), Arrays
                .asList(APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire), 5, 24);
    }
}
