/*
 * Cr?? le 27 avr. 05
 */
package globaz.apg.module.calcul.rev2005;

import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APResultatCalcul;
import globaz.apg.module.calcul.interfaces.IAPModuleCalcul;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JANumberFormatter;
import java.math.BigDecimal;

/**
 * @author scr
 * 
 *         Calcul du service normal
 */
public class APModuleCalculAllocServiceNormal extends AAPModuleCalculSalaireJournalier implements IAPModuleCalcul {

    /**
	 * 
	 */
    public APModuleCalculAllocServiceNormal() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.module.calcul.interfaces.IAPModuleCalcul#calculerMontantAllocation
     * (globaz.apg.module.calcul.APBaseCalcul, globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation)
     */
    @Override
    public APResultatCalcul calculerMontantAllocation(APBaseCalcul baseCalcul, IAPReferenceDataPrestation refData)
            throws Exception {
        APResultatCalcul result = super.calculerMontantAllocation(baseCalcul, refData);

        // Salaire journalier
        FWCurrency TL = result.getTL();

        BigDecimal bTE = new BigDecimal(TL.toString());

        // TL = GE
        if (TL.compareTo(((APReferenceDataAPG) refData).getGE()) == 0) {
            // TE = Amax
            bTE = ((APReferenceDataAPG) refData).getAmax().getBigDecimalValue();
        }
        // TL!=GE
        else {
            // TE = (8 * TL + 0.9)/10
            bTE = bTE.multiply(new BigDecimal(8));
            bTE = bTE.add(new BigDecimal(0.9));
            bTE = bTE.divide(new BigDecimal(10), BigDecimal.ROUND_HALF_DOWN);
            bTE = JANumberFormatter.round(bTE, 0.1, 1, JANumberFormatter.INF);
        }

        // TE=TE + K*KZ
        BigDecimal tmp = new BigDecimal(baseCalcul.getNombreEnfants());
        tmp = tmp.multiply(((APReferenceDataAPG) refData).getKZ().getBigDecimalValue());
        bTE = bTE.add(tmp);

        // Sans enfants
        if (baseCalcul.getNombreEnfants() == 0) {
            // TE <= KA0min
            if (bTE.compareTo(((APReferenceDataAPG) refData).getKA0min().getBigDecimalValue()) < 1) {
                // TE == KA0min
                bTE = ((APReferenceDataAPG) refData).getKA0min().getBigDecimalValue();
            }
        }

        // 1 enfant
        else if (baseCalcul.getNombreEnfants() == 1) {
            // TE>KA1min
            if (bTE.compareTo(((APReferenceDataAPG) refData).getKA1min().getBigDecimalValue()) == 1) {
                // TE > TL
                if (bTE.compareTo(TL.getBigDecimalValue()) == 1) {
                    // TL>KA1min
                    if (TL.compareTo(((APReferenceDataAPG) refData).getKA1min()) == 1) {
                        // TE = TL
                        bTE = TL.getBigDecimalValue();
                    } else {
                        // TE = KA1min
                        bTE = ((APReferenceDataAPG) refData).getKA1min().getBigDecimalValue();
                    }
                }
            }
            // TE<=KA1min
            else {
                // TE == KA1min
                bTE = ((APReferenceDataAPG) refData).getKA1min().getBigDecimalValue();
            }
        }

        // 2 enfants ou plus
        else if (baseCalcul.getNombreEnfants() > 1) {
            // TE>KA2min
            if (bTE.compareTo(((APReferenceDataAPG) refData).getKA2min().getBigDecimalValue()) == 1) {
                // TE > TL
                if (bTE.compareTo(TL.getBigDecimalValue()) == 1) {
                    // TL>KA2min
                    if (TL.compareTo(((APReferenceDataAPG) refData).getKA2min()) == 1) {
                        // TE = TL
                        bTE = TL.getBigDecimalValue();
                    } else {
                        // TE = KA2min
                        bTE = ((APReferenceDataAPG) refData).getKA2min().getBigDecimalValue();
                    }
                }
            }
            // TE<=KA2min
            else {
                // TE == KA2min
                bTE = ((APReferenceDataAPG) refData).getKA2min().getBigDecimalValue();
            }

        }
        result.setMontantJournalier(new FWCurrency(bTE.toString()));
        return result;
    }

}
