/*
 * Créé le 11 mai 05
 * 
 * Description :
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
 *         Descpription
 */
public class APModuleCalculAllocCadreServiceLong extends AAPModuleCalculSalaireJournalier implements IAPModuleCalcul {

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
            // TE <= KC0min
            if (bTE.compareTo(((APReferenceDataAPG) refData).getKC0min().getBigDecimalValue()) < 1) {
                // TE == KC0min
                bTE = ((APReferenceDataAPG) refData).getKC0min().getBigDecimalValue();
            }
        }

        // 1 enfant
        else if (baseCalcul.getNombreEnfants() == 1) {
            // TE<=KC1min
            if (bTE.compareTo(((APReferenceDataAPG) refData).getKC1min().getBigDecimalValue()) < 1) {
                // TE == KC1min
                bTE = ((APReferenceDataAPG) refData).getKC1min().getBigDecimalValue();
            }
        }

        // 2 enfants ou plus
        else if (baseCalcul.getNombreEnfants() > 1) {
            // TE>KC2min
            if (bTE.compareTo(((APReferenceDataAPG) refData).getKC2min().getBigDecimalValue()) == 1) {
                // TE > TL
                if (bTE.compareTo(TL.getBigDecimalValue()) == 1) {
                    // TL>KC2min
                    if (TL.compareTo(((APReferenceDataAPG) refData).getKC2min()) == 1) {
                        // TE = TL
                        bTE = TL.getBigDecimalValue();
                    } else {
                        // TE = KC2min
                        bTE = ((APReferenceDataAPG) refData).getKC2min().getBigDecimalValue();
                    }
                }
            }
            // TE<=KC2min
            else {
                // TE == KC2min
                bTE = ((APReferenceDataAPG) refData).getKC2min().getBigDecimalValue();
            }

        }
        result.setMontantJournalier(new FWCurrency(bTE.toString()));
        return result;

    }

}
