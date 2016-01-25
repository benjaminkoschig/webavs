/*
 * Cr�� le 27 avr. 05
 */
package globaz.apg.module.calcul.rev2005;

import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APResultatCalcul;
import globaz.apg.module.calcul.interfaces.IAPModuleCalcul;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.framework.util.FWCurrency;

/**
 * @author scr
 * 
 *         Calcul de l'allocation pour recrue sans enfants
 */
public class APModuleCalculAllocBaseRecrue extends AAPModuleCalculSalaireJournalier implements IAPModuleCalcul {

    /**
	 * 
	 */
    public APModuleCalculAllocBaseRecrue() {
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
        // Va mettre � jour le RDM.
        APResultatCalcul resultat = super.calculerMontantAllocation(baseCalcul, refData);
        resultat.setMontantJournalier(((APReferenceDataAPG) refData).getKA0min());
        // on se fout des frais de garde puisque pour ce cas la prestation
        // journali�re est une valeure fixe
        // on met 0 et pas null sinon les exceptions fusent de partout
        resultat.setAllocationJournaliereMaxFraisGarde(new FWCurrency(0));
        if (baseCalcul.isAllocationExploitation()) {
            resultat.setAllocationJournaliereExploitation(((APReferenceDataAPG) refData).getBZ());
        }
        return resultat;
    }

}
