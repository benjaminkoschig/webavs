package globaz.apg.rapg.rules;

import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APTypeProtectionCivile;
import java.util.Arrays;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Période de contrôle : une année
 * civile1 Si le champ « serviceType » = 20, le champ « referenceNumber » = cas 3 (cours de répétition) et le champ «
 * numberOfDays » > 7 jours -> erreur </br><strong>Champs concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule402 extends RuleInterAPG {

    public Rule402(String errorCode) {
        super(errorCode, false, Arrays.asList(APGenreServiceAPG.ProtectionCivileServiceNormale), Arrays
                .asList(APTypeProtectionCivile.CoursDeRepetition), 1, 7);
    }
}