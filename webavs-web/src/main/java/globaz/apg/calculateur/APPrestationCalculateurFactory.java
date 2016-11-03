package globaz.apg.calculateur;

import globaz.apg.calculateur.acm.ne.APCalculateurAcmNe;
import globaz.apg.calculateur.maternite.acm2.APcalculateurACM2;
import globaz.apg.enums.APTypeDePrestation;
import globaz.apg.module.calcul.standard.APCalculateurPrestationStandardLamatAcmAlpha;

/**
 * Factory dont le but est de retourner un calculateur de prestations en fonction du type de prestation � calculer
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
            default:
                /*
                 * On ne g�re pas le default, une exception sera lanc�e si aucune instance de calculateur n'a pu �tre
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
