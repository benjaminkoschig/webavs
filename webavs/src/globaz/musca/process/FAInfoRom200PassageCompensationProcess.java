package globaz.musca.process;

import globaz.framework.util.FWCurrency;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.exceptions.CATechnicalException;

public class FAInfoRom200PassageCompensationProcess extends FAPassageCompenserNewProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected Result appliquerRegleCompensation(FAEnteteFacture entFacture, FWCurrency montantFacture,
            CASection section, CACompteAnnexe compteAnnexe) throws CATechnicalException {
        if (montantFacture.isNegative()) {
            return Result.PASCOMPENSATION;
        } else {
            return hasContentieux(section, compteAnnexe) ? Result.PASCOMPENSATION : Result.AQUITTANCERNON;
        }
    }

    @Override
    protected boolean isCompensationPossible(FWCurrency totalFacture, FWCurrency totalCompteAnnexe) {

        return totalFacture.isPositive() && totalCompteAnnexe.isNegative();
    }
}
