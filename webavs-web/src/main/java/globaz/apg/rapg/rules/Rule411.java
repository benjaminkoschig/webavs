package globaz.apg.rapg.rules;

import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APTypeProtectionCivile;
import java.util.Arrays;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br> Description :</strong></br> P�riode de contr�le : une ann�e
 * civile Si le champ � serviceType � = 20, 21, 22, 23, le champ � referenceNumber � != cas 4 et le champ � numberOfDays
 * � > 40 jours -> erreur </br><strong>Champs concern�(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule411 extends RuleInterAPG {

    public Rule411(String errorCode) {
        super(errorCode, false, Arrays.asList(APGenreServiceAPG.ProtectionCivileFormationDeBase,
                APGenreServiceAPG.ProtectionCivileServiceNormale, APGenreServiceAPG.ProtectionCivileCadreSpecialiste,
                APGenreServiceAPG.ProtectionCivileCommandant), Arrays.asList(APTypeProtectionCivile.InstructionDeBase,
                APTypeProtectionCivile.CoursDeRepetition,
                APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire,
                APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite,
                APTypeProtectionCivile.ServiceAccompliDansAdministrationProtectionCivile), 1, 40);
    }
}
