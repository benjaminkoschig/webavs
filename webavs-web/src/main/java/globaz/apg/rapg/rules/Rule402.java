package globaz.apg.rapg.rules;

import ch.globaz.common.util.Dates;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APTypeProtectionCivile;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br> Description :</strong></br> P�riode de contr�le : une ann�e
 * civile1 (Si startOfPrediod < 01.01.2021, Si le champ � serviceType � = 20, le champ � referenceNumber � = cas 3 (cours de r�p�tition) et le champ �
 * numberOfDays � > 7 jours) ou (Si startOfPrediod > 31.12.2020, Si le champ � serviceType � = 20, le champ � referenceNumber � = cas 3 (cours de r�p�tition) et le champ �
 *  * numberOfDays � > 21 jours)  -> erreur </br><strong>Champs concern�(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule402 extends RuleInterAPG {

    public Rule402(String errorCode) {
        super(errorCode, false, Arrays.asList(APGenreServiceAPG.ProtectionCivileServiceNormale), Arrays
                .asList(APTypeProtectionCivile.CoursDeRepetition), 1, 7);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {
        try {
            if(Dates.toDate(champsAnnonce.getStartOfPeriod()).isAfter(LocalDate.of(2020, 12,31))){
                setNombreJoursSoldesMax(21);
                addGenresServicesVoulus(APGenreServiceAPG.ProtectionCivileCadreSpecialiste);
            }
            return super.check(champsAnnonce);
        }
        catch (Exception exception) {
            throw new APRuleExecutionException(exception);
        }
    }
}