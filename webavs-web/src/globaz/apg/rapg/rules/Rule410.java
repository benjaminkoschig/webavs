package globaz.apg.rapg.rules;

import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APTypeProtectionCivile;
import java.util.Arrays;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br> Description :</strong></br> P�riode de contr�le : 5 ann�es
 * civiles Si le champ � serviceType � = 23, le champ � referenceNumber � = cas 2 (instruction des cadres,
 * perfectionnement) et le champ � numberOfDays � > 36 jours -> erreur </br><strong>Champs concern�(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule410 extends RuleInterAPG {

    public Rule410(String errorCode) {
        super(errorCode, false, Arrays.asList(APGenreServiceAPG.ProtectionCivileCommandant), Arrays
                .asList(APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire), 5, 36);
    }
}
