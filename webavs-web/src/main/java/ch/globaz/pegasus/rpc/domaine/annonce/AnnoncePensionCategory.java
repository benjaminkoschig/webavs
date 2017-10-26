package ch.globaz.pegasus.rpc.domaine.annonce;

import ch.globaz.pegasus.business.domaine.pca.PcaDecision;
import ch.globaz.pegasus.rpc.domaine.PersonElementsCalcul;

public class AnnoncePensionCategory {

    protected AnnoncePension pension;

    public AnnoncePensionCategory(PersonElementsCalcul personData, PersonElementsCalcul requerantData,
            PcaDecision pcaDecision) {

        if (personData.hasPension()) {
            pension = new AnnoncePension(personData, requerantData, pcaDecision);
        }

    }

    public AnnoncePension getPension() {
        return pension;
    }

    public void setPension(AnnoncePension pension) {
        this.pension = pension;
    }

}
