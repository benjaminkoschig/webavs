package globaz.apg.rapg.rules;

import globaz.apg.enums.APGenreServiceAPG;
import java.util.Arrays;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br> Description :</strong></br> Si les champs � serviceType � =
 * 41 et � numberOfDays � > 145 jours -> erreur </br><strong>Champs concern�(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule413 extends RuleInterAPG {

    public Rule413(String errorCode) {
        super(errorCode, false, Arrays.asList(APGenreServiceAPG.ServiceCivilTauxRecrue), 0, 145);
    }
}
