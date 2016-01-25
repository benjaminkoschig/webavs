package globaz.apg.rapg.rules;

import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <strong>Règles de validation des plausibilités RAPG</br> Description :</strong></br> Période de contrôle : 2 années
 * civiles Si le champ « serviceType » = 11, le champ « numberOfDays » > 173 jours et la période de service ne chevauche
 * pas Noël -> erreur Si le champ « serviceType » = 11, le champ « numberOfDays » > 187 jours et la période de service
 * chevauche Noël -> erreur </br><strong>Champs concerné(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule406 extends Rule {

    public Rule406(String errorCode) {
        super(errorCode, false);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {

        // Si école de recrue
        if (APGenreServiceAPG.MilitaireEcoleDeRecrue.getCodePourAnnonce().equals(champsAnnonce.getServiceType())) {

            int nbMaxJoursSoldesEcoleRecrue = 173;

            if (isPeriodePendantNoel(champsAnnonce.getStartOfPeriod(), champsAnnonce.getEndOfPeriod())) {
                nbMaxJoursSoldesEcoleRecrue = 187; // possibilité d'augmenter de 15 jours si école durant Noël
            }

            if (!JadeStringUtil.isBlank(champsAnnonce.getNumberOfDays())) {
                int nombreDeJoursSoldes = Integer.parseInt(champsAnnonce.getNumberOfDays());
                if (nombreDeJoursSoldes > nbMaxJoursSoldesEcoleRecrue) {
                    return false;
                }
            }
        }

        return true;
    }
}
