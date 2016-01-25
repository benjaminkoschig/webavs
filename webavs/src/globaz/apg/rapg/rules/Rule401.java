/**
 * 
 */
package globaz.apg.rapg.rules;

import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APTypeProtectionCivile;
import java.util.Arrays;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Si le champ « serviceType » = 20
 * ou 21, le champ « referenceNumber » = cas 1 (instruction de base) et le champ « numberOfDays » > 19 jours -> erreur
 * </br><strong>Champs concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule401 extends RuleInterAPG {

    public Rule401(String errorCode) {
        super(errorCode, false, Arrays.asList(APGenreServiceAPG.ProtectionCivileServiceNormale,
                APGenreServiceAPG.ProtectionCivileFormationDeBase), Arrays
                .asList(APTypeProtectionCivile.InstructionDeBase), 5, 19);
    }
}
