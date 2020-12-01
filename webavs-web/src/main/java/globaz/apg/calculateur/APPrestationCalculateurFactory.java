package globaz.apg.calculateur;

import globaz.apg.calculateur.acm.ne.APCalculateurAcmNe;
import globaz.apg.calculateur.complement.APCalculateurComplement;
import globaz.apg.calculateur.complement.APCalculateurComplementMATCIAB1;import globaz.apg.calculateur.maternite.acm2.APcalculateurACM2;
import globaz.apg.calculateur.maternite.acm2.APcalculateurMATCIAB2;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.module.calcul.standard.APCalculateurPrestationStandardLamatAcmAlpha;

/**
 * Factory dont le but est de retourner un calculateur de prestations en fonction du type de prestation à calculer
 *
 * @author lga
 */
public class APPrestationCalculateurFactory {

    public static IAPPrestationCalculateur getCalculateurInstance(final APTypeDePrestation typeDePrestationACalculer) {
        IAPPrestationCalculateur calculateurInstance = null;

        switch (typeDePrestationACalculer) {
            case STANDARD:
                calculateurInstance = new APCalculateurPrestationStandardLamatAcmAlpha();
                break;

            // LAMAT, ACM_ALPHA compris dans calculateur standard
            /*
             * case LAMAT: // TODO // calculateurInstance = new APCalculateurInterface() {
             *
             * // }; break;
             *
             * case ACM_ALFA: // TODO // calculateurInstance = new APCalculateurInterface() {
             *
             * // }; break;
             */

            case ACM_NE:
                calculateurInstance = new APCalculateurAcmNe();
                break;
            case ACM2_ALFA:
                calculateurInstance = new APcalculateurACM2();
                break;
            case COMPCIAB:
                calculateurInstance = new APCalculateurComplement();
                break;
            case MATCIAB1:
                calculateurInstance = new APCalculateurComplementMATCIAB1();
                break;            case MATCIAB2:
                calculateurInstance = new APcalculateurMATCIAB2();
                break;
            default:
                /*
                 * On ne gère pas le default, une exception sera lancée si aucune instance de calculateur n'a pu être
                 * construite
                 */
                break;
        }

        if (calculateurInstance == null) {
            throw new IllegalArgumentException(
                    "APCalculateurFactory can not get IAPPrestationCalculateur instance for APTypeDePrestation ["
                            + typeDePrestationACalculer + "]");
        }

        return calculateurInstance;
    }
}
