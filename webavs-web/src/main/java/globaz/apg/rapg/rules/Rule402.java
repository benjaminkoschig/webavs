package globaz.apg.rapg.rules;

import ch.globaz.common.util.Dates;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APTypeProtectionCivile;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Période de contrôle : une année
 * civile1 (Si startOfPrediod < 01.01.2021, Si le champ « serviceType » = 20, le champ « referenceNumber » = cas 3 (cours de répétition) et le champ «
 * numberOfDays » > 7 jours) ou (Si startOfPrediod > 31.12.2020, Si le champ « serviceType » = 20 ou 22, le champ « referenceNumber » = cas 3 (cours de répétition) et le champ «
 *  * numberOfDays » > 21 jours)  -> erreur </br><strong>Champs concerné(s) :</strong></br>
 *
 * @author lga
 */
public class Rule402 extends RuleInterAPG {

    private final int NB_JOURS_MAX_DEPUIS_2021 = 21;

    public Rule402(String errorCode) {
        super(errorCode, false, Arrays.asList(APGenreServiceAPG.ProtectionCivileServiceNormale), Arrays
                .asList(APTypeProtectionCivile.CoursDeRepetition), 1, 7);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {
        try {
            if(Dates.toDate(champsAnnonce.getStartOfPeriod()).isAfter(LocalDate.of(2020, 12,31))){
                setNombreJoursSoldesMax(NB_JOURS_MAX_DEPUIS_2021);
                setGenresServicesAdditionnels(Arrays.asList(APGenreServiceAPG.ProtectionCivileCadreSpecialiste));
            }
            return super.check(champsAnnonce);
        }
        catch (Exception exception) {
            throw new APRuleExecutionException(exception);
        }
    }

    @Override
    public String getDetailMessageErreur() {
        String errorMessage = "";
        if(NB_JOURS_MAX_DEPUIS_2021 == getNombreJoursSoldesMax()){
            errorMessage = getSession().getLabel("APG_RULE_402_21");
        }else{
            errorMessage = getSession().getLabel("APG_RULE_402_7");
        }
        return errorMessage;

    }
}