package globaz.apg.rapg.rules;

import ch.globaz.common.util.Dates;
import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.enums.APTypeProtectionCivile;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Période de contrôle : une année
 * civile Si le champ « serviceType » = 20, 22, 23, le champ « referenceNumber » = cas 5 (interventions en faveur de la
 * collectivité) et le champ « numberOfDays » > 21 jours -> erreur </br><strong>Champs concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule412 extends RuleInterAPG {

    private static LocalDate dateFinRule = Dates.toDate("31.06.2021");

    public Rule412(String errorCode) {
        super(errorCode, false, Arrays.asList(APGenreServiceAPG.ProtectionCivileServiceNormale,
                        APGenreServiceAPG.ProtectionCivileCadreSpecialiste, APGenreServiceAPG.ProtectionCivileCommandant),
                Arrays.asList(APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite), 1, 21);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {
        return JadeStringUtil.isEmpty(champsAnnonce.getEndOfPeriod()) ? true : Dates.toDate(champsAnnonce.getEndOfPeriod()).isAfter(dateFinRule) ? true : super.check(champsAnnonce);
    }

}
