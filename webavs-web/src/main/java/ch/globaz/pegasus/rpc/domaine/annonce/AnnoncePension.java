package ch.globaz.pegasus.rpc.domaine.annonce;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.pca.PcaDecision;
import ch.globaz.pegasus.rpc.domaine.PersonElementsCalcul;

public class AnnoncePension {

    protected AnnonceCompensationOffice compensationOffice;
    protected Montant avsAipension;
    protected Montant disabledAllowance;
    protected Montant dailyAllowance;

    public AnnoncePension(PersonElementsCalcul personData, PersonElementsCalcul requerantData, PcaDecision pcaDecision) {
        compensationOffice = new AnnonceCompensationOffice(personData, requerantData);
        avsAipension = personData.getRenteAvsAi();
        disabledAllowance = pcaDecision.getPca().getCalcul().getRevenusAPIAVSAI();
        dailyAllowance = personData.getRenteIj();
    }

    public AnnonceCompensationOffice getCompensationOffice() {
        return compensationOffice;
    }

    public Montant getAvsAipension() {
        return avsAipension;
    }

    public Montant getDisabledAllowance() {
        return disabledAllowance;
    }

    public Montant getDailyAllowance() {
        return dailyAllowance;
    }

}
