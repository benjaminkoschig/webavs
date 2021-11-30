package globaz.apg.rapg.rules;

import globaz.apg.enums.APGenreServiceAPG;
import java.util.Arrays;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br> Description :</strong></br> P�riode de contr�le : 3 ann�es
 * civiles Si le champ � serviceType � = 11 ou 14 et le champ � numberOf-Days � > 668 jours -> erreur
 * </br><strong>Champs concern�(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule408 extends RuleInterAPG {

    public Rule408(String errorCode) {
        super(errorCode, false, Arrays.asList(APGenreServiceAPG.MilitaireEcoleDeRecrue,
                APGenreServiceAPG.MilitaireSousOfficierEnServiceLong), 3, 668);
    }
}
