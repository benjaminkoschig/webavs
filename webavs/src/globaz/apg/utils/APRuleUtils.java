package globaz.apg.utils;

import globaz.apg.enums.APTypeProtectionCivile;
import globaz.jade.client.util.JadeStringUtil;

public class APRuleUtils {

    /**
     * Retourne le type de protection civil en fonction du refNumber
     * 
     * @param refNumber
     *            Le refNumber en question
     * @return
     */
    public static APTypeProtectionCivile getTypePCenFonctionDuRefNumber(String refNumber) {
        // TODO à étudier....
        if (JadeStringUtil.isBlankOrZero(refNumber)) {
            return APTypeProtectionCivile.Indefini;
        }

        if (refNumber.length() > 5) {
            return APTypeProtectionCivile.CoursDeRepetition;
        }

        int chiffresCentraux = -1, dernierChiffre = -1;

        if (refNumber.length() == 4) {
            chiffresCentraux = Integer.parseInt(refNumber.substring(1, 3));
            dernierChiffre = Integer.parseInt(refNumber.substring(3));
        } else {
            chiffresCentraux = Integer.parseInt(refNumber.substring(2, 4));
            dernierChiffre = Integer.parseInt(refNumber.substring(4));
        }

        switch (chiffresCentraux) {
            case -1:
                return APTypeProtectionCivile.Indefini;
            case 99:
                switch (dernierChiffre) {
                    case 1:
                        return APTypeProtectionCivile.CatastropheSituationUrgenceEtRemiseEnEtat;
                    case 2:
                        return APTypeProtectionCivile.InterventionEnFaveurDeLaCollectivite;
                }
                break;
            case 00:
                switch (dernierChiffre) {
                    case 1:
                        return APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire;
                    case 2:
                        return APTypeProtectionCivile.ServiceAccompliDansAdministrationProtectionCivile;
                }
            default:
                switch (dernierChiffre) {
                    case 1:
                        return APTypeProtectionCivile.InstructionDeBase;
                    case 2:
                        return APTypeProtectionCivile.InstructionDesCadresPerfectionnementEtInstructionSupplementaire;
                }
                break;
        }

        return APTypeProtectionCivile.Indefini;
    }
}
