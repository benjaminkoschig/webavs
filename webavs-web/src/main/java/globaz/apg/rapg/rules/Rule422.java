package globaz.apg.rapg.rules;

import ch.globaz.common.exceptions.Exceptions;
import ch.globaz.common.util.Dates;
import globaz.apg.db.droits.APDroitProcheAidant;
import globaz.apg.enums.APAllPlausibiliteRules;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;

/**
 * Si pour les annonces dont le champ « serviceType » = 92
 * et dont « childInsurantVn » est identique et dont « careLeaveEventID » est identique,
 * la période entre : - la valeur la plus ancienne de «startOfPeriod»
 * et - la valeur la plus récente de « endOfPeriod » > 18 mois ? erreur
 *
 * @author dma
 */
public class Rule422 extends Rule {

    public Rule422(String errorCode) {
        super(errorCode, APAllPlausibiliteRules.R_422.isBreakable());
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException {

        APDroitProcheAidant apDroitProcheAidant = new APDroitProcheAidant();
        apDroitProcheAidant.setIdDroit(champsAnnonce.getIdDroit());
        apDroitProcheAidant.setSession(this.getSession());
        Exceptions.checkedToUnChecked(()->apDroitProcheAidant.retrieve());

        return apDroitProcheAidant.calculerDelai()
                                  .map(delai->delai.isAfter(Dates.toDate(champsAnnonce.getEndOfPeriod())))
                                  .orElse(true);
    }
}
