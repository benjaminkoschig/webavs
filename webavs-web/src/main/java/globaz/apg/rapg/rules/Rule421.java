package globaz.apg.rapg.rules;

import globaz.apg.db.droits.APDroitProcheAidant;
import globaz.apg.enums.APAllPlausibiliteRules;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;

/**
 * Si pour une prise en charge, dont le champ «serviceType» = 92,
 * et toutes les annonces dont «childInsurantVn» est identique,
 * et dont « careLeaveEventID» est identique,
 * le nombre de jours du champ «numberOfDays» > 98 ? erreur
 *
 * @author dma
 */
public class Rule421 extends Rule {

    public Rule421(String errorCode) {
        super(errorCode, APAllPlausibiliteRules.R_421.isBreakable());
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException {
        APDroitProcheAidant apDroitProcheAidant = new APDroitProcheAidant();
        apDroitProcheAidant.setIdDroit(champsAnnonce.getIdDroit());
        apDroitProcheAidant.setSession(this.getSession());

        return apDroitProcheAidant.calculerNbJourDisponible() >= 0;
    }
}
