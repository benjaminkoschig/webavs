/*
 * Créé le 27 avr. 05
 */
package globaz.apg.module.calcul.rev1999;

import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APResultatCalcul;
import globaz.apg.module.calcul.interfaces.IAPModuleCalcul;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JANumberFormatter;
import java.math.BigDecimal;

/**
 * Description Calcul du service d'avancement
 * 
 * @author scr
 * 
 */
public class APModuleCalculAllocServiceAvancement extends AAPModuleCalculSalaireJournalier implements IAPModuleCalcul {

    public APModuleCalculAllocServiceAvancement() {
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
            // TE = (6.5 * TL + 0.9)/10
            bTE = bTE.multiply(new BigDecimal(6.5));
            bTE = bTE.add(new BigDecimal(0.9));
            bTE = bTE.divide(new BigDecimal(10), BigDecimal.ROUND_HALF_DOWN);
            bTE = JANumberFormatter.round(bTE, 0.1, 1, JANumberFormatter.INF);

            // TE <= Bmin
            if (bTE.compareTo(((APReferenceDataAPG) refData).getBmin().getBigDecimalValue()) < 1) {
                // TE = Bmin
                bTE = ((APReferenceDataAPG) refData).getBmin().getBigDecimalValue();
            }
        }

        // K>0
        if (baseCalcul.getNombreEnfants() > 0) {
            // KZ = KZ1 + (K-1) * KZ2
            BigDecimal kz = new BigDecimal(baseCalcul.getNombreEnfants() - 1);
            kz = kz.multiply(((APReferenceDataAPG) refData).getKZ2().getBigDecimalValue());
            kz = kz.add(((APReferenceDataAPG) refData).getKZ1().getBigDecimalValue());

            // TE= TE + KZ
            bTE = bTE.add(kz);
        }

        // TE>TL
        if (bTE.compareTo(TL.getBigDecimalValue()) == 1) {

            // TL > KBmin
            if (TL.compareTo(((APReferenceDataAPG) refData).getKBmin()) == 1) {
                // TE = TL
                bTE = TL.getBigDecimalValue();
            } else {
                // TE > KBmin
                if (bTE.compareTo(((APReferenceDataAPG) refData).getKBmin().getBigDecimalValue()) == 1) {
                    // TE = KBmin
                    bTE = ((APReferenceDataAPG) refData).getKBmin().getBigDecimalValue();
                }
            }
        }
        result.setMontantJournalier(new FWCurrency(bTE.toString()));
        return result;
    }

}
