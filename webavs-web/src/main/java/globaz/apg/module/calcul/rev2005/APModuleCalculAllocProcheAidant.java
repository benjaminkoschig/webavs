/*
 * Créé le 11 mai 05
 * 
 * Description :
 */
package globaz.apg.module.calcul.rev2005;

import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APCalculException;
import globaz.apg.module.calcul.wrapper.APMontantJour;
import globaz.apg.module.calcul.APResultatCalcul;
import globaz.apg.module.calcul.interfaces.IAPModuleCalcul;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.framework.util.FWCurrency;
import globaz.globall.util.JANumberFormatter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author scr
 * 
 *         Descpription
 */
public class APModuleCalculAllocProcheAidant extends AAPModuleCalculSalaireJournalier implements IAPModuleCalcul {

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
            throw new APCalculException("Activité lucrative obligatoire pour bénéficier d'une allocation proche aidant.");
        }

        APResultatCalcul result = super.calculerMontantAllocation(baseCalcul, refData);
        // Salaire journalier
        BigDecimal bTE;
        if(result.getEmployeursTL() == null || result.getEmployeursTL().isEmpty()) {
            bTE = getBigDecimal((APReferenceDataAPG) refData, result.getTL());
            result.setMontantJournalier(new FWCurrency(bTE.toString()));
        } else {
            // plusieurs employeurs / plusieurs périodes séparées
            bTE = BigDecimal.ZERO;
            List<APMontantJour> list = result.getEmployeursTL().stream()
                    .map(mj -> APMontantJour.of(getBigDecimal((APReferenceDataAPG) refData, mj.getMontant()), mj.getJours(), mj.getSituationProfessionelle())).collect(Collectors.toList());
            result.setMontantJournalierList(list);
            result.setMontantJournalier(new FWCurrency(list.stream()
                    .map(mj -> mj.calculMontantJournalierParRapportAuTotal(baseCalcul.getNombreJoursSoldes()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add).toString(), 3));

        }
        return result;

    }

    private BigDecimal getBigDecimal(APReferenceDataAPG refData, FWCurrency TL) {
        BigDecimal bTE = new BigDecimal(TL.toString());

        // TL = GE
        if (TL.compareTo(refData.getGE()) >= 0) {
            // TE = Amax
            bTE = refData.getAmax().getBigDecimalValue();
        }
        // TL!=GE
        else {
            // TE = (8 * TL + 0.9)/10
            bTE = bTE.multiply(new BigDecimal(8));
            bTE = bTE.add(new BigDecimal(0.9));
            bTE = bTE.divide(new BigDecimal(10), BigDecimal.ROUND_HALF_DOWN);
            bTE = JANumberFormatter.round(bTE, 0.1, 1, JANumberFormatter.INF);

        }
        return bTE;
    }

}
