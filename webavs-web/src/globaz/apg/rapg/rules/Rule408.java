package globaz.apg.rapg.rules;

import globaz.apg.enums.APGenreServiceAPG;
import java.util.Arrays;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Période de contrôle : 3 années
 * civiles Si le champ « serviceType » = 11 ou 14 et le champ « numberOf-Days » > 600 jours -> erreur
 * </br><strong>Champs concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule408 extends RuleInterAPG {

    public Rule408(String errorCode) {
        super(errorCode, false, Arrays.asList(APGenreServiceAPG.MilitaireEcoleDeRecrue,
                APGenreServiceAPG.MilitaireSousOfficierEnServiceLong), 3, 600);
    }
}
