package globaz.apg.rapg.rules;

import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APTypeProtectionCivile;
import java.util.Arrays;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Période de contrôle : une année
 * civile Si le champ « serviceType » = 23, le champ « referenceNumber » = cas 3 (cours de répétition) et le champ «
 * numberOfDays » > 26 jours -> erreur </br><strong>Champs concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule409 extends RuleInterAPG {

    public Rule409(String errorCode) {
        super(errorCode, false, Arrays.asList(APGenreServiceAPG.ProtectionCivileCommandant), Arrays
                .asList(APTypeProtectionCivile.CoursDeRepetition), 1, 26);
    }
}
