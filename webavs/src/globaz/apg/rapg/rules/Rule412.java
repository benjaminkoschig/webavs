package globaz.apg.rapg.rules;

import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APTypeProtectionCivile;
import java.util.Arrays;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Période de contrôle : une année
 * civile Si le champ « serviceType » = 20, 22, 23, le champ « referenceNumber » = cas 5 (interventions en faveur de la
 * collectivité) et le champ « numberOfDays » > 21 jours -> erreur </br><strong>Champs concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule412 extends RuleInterAPG {

    public Rule412(String errorCode) {
        super(errorCode, false, Arrays.asList(APGenreServiceAPG.ProtectionCivileServiceNormale,
                APGenreServiceAPG.ProtectionCivileCadreSpecialiste, APGenreServiceAPG.ProtectionCivileCommandant),
                Arrays.asList(APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite), 1, 21);
    }
}
