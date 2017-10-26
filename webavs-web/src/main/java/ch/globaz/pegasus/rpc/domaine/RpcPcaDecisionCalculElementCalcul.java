package ch.globaz.pegasus.rpc.domaine;

import ch.globaz.pegasus.business.domaine.pca.PcaDecision;

public class RpcPcaDecisionCalculElementCalcul {
    private final PcaDecision pcaDecision;
    private final RpcCalcul calcul;
    private final PersonsElementsCalcul personsElementsCalcul;

    public RpcPcaDecisionCalculElementCalcul(PcaDecision pcaDecision, RpcCalcul calcul,
            PersonsElementsCalcul personsElementsCalcul) {
        this.pcaDecision = pcaDecision;
        this.calcul = calcul;
        this.personsElementsCalcul = personsElementsCalcul;
    }

    public PcaDecision getPcaDecision() {
        return pcaDecision;
    }

    public RpcCalcul getCalcul() {
        return calcul;
    }

    public PersonsElementsCalcul getPersonsElementsCalcul() {
        return personsElementsCalcul;
    }

}
