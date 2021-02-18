/*
 * Créé le 11 mai 05
 * 
 * Description :
 */
package globaz.apg.module.calcul.rev2005;

import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APCalculException;
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
public class APModuleCalculAllocPaternite extends AAPModuleCalculSalaireJournalier implements IAPModuleCalcul {

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.module.calcul.interfaces.IAPModuleCalcul#calculerMontantAllocation
     * (globaz.apg.module.calcul.APBaseCalcul, globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation)
     */
    @Override
    public APResultatCalcul calculerMontantAllocation(APBaseCalcul baseCalcul, IAPReferenceDataPrestation refData)
            throws Exception {

        if (baseCalcul.isSansActiviteLucrative()) {
            throw new APCalculException("Activité lucrative obligatoire pour bénéficier d'une allocation maternité.");
        }

        APResultatCalcul result = super.calculerMontantAllocation(baseCalcul, refData);
        // Salaire journalier
        FWCurrency TL = result.getTL();

        BigDecimal bTE = new BigDecimal(TL.toString());

        // TL = GE
        if (TL.compareTo(((APReferenceDataAPG) refData).getGE()) >= 0) {
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

        result.setMontantJournalier(new FWCurrency(bTE.toString()));
        return result;

    }

}
