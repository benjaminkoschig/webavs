package globaz.apg.rapg.rules;

import globaz.apg.enums.APGenreServiceAPG;
import globaz.apg.exceptions.APRuleExecutionException;
import globaz.apg.pojo.APChampsAnnonce;
import globaz.jade.client.util.JadeStringUtil;

/**
 * <strong>R�gles de validation des plausibilit�s RAPG</br> Description :</strong></br> P�riode de contr�le : 2 ann�es
 * civiles Si le champ � serviceType � = 11, le champ � numberOfDays � > 173 jours et la p�riode de service ne chevauche
 * pas No�l -> erreur Si le champ � serviceType � = 11, le champ � numberOfDays � > 187 jours et la p�riode de service
 * chevauche No�l -> erreur </br><strong>Champs concern�(s) :</strong></br>
 * 
 * @author lga
 */
public class Rule406 extends Rule {

    public Rule406(String errorCode) {
        super(errorCode, false);
    }

    @Override
    public boolean check(APChampsAnnonce champsAnnonce) throws APRuleExecutionException, IllegalArgumentException {

        // Si �cole de recrue
        if (APGenreServiceAPG.MilitaireEcoleDeRecrue.getCodePourAnnonce().equals(champsAnnonce.getServiceType())) {

            int nbMaxJoursSoldesEcoleRecrue = 173;

            if (isPeriodePendantNoel(champsAnnonce.getStartOfPeriod(), champsAnnonce.getEndOfPeriod())) {
                nbMaxJoursSoldesEcoleRecrue = 187; // possibilit� d'augmenter de 15 jours si �cole durant No�l
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
