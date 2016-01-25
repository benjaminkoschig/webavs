/*
 * Créé le 27 avr. 05
 */
package globaz.apg.module.calcul.rev1999;

import globaz.apg.module.calcul.APBaseCalcul;
import globaz.apg.module.calcul.APResultatCalcul;
import globaz.apg.module.calcul.interfaces.IAPModuleCalcul;
import globaz.apg.module.calcul.interfaces.IAPReferenceDataPrestation;
import globaz.framework.util.FWCurrency;

/**
 * 
 * Description Calcul de l'allocation pour recrue sans enfants
 * 
 * @author scr
 * 
 */
public class APModuleCalculAllocBaseRecrue extends AAPModuleCalculSalaireJournalier implements IAPModuleCalcul {

    /**
     * Description Constructeur
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

        // Va mettre à jour le RDM.
        APResultatCalcul resultat = super.calculerMontantAllocation(baseCalcul, refData);
        resultat.setMontantJournalier(((APReferenceDataAPG) refData).getAmin());
        // on se fout des frais de garde puisque pour ce cas la prestation
        // journalière est une valeure fixe
        // on met 0 et pas null sinon les exceptions fusent de partout
        resultat.setAllocationJournaliereMaxFraisGarde(new FWCurrency(0));
        if (baseCalcul.isAllocationExploitation()) {
            resultat.setAllocationJournaliereExploitation(((APReferenceDataAPG) refData).getBZ());
        }
        return resultat;

    }

}
