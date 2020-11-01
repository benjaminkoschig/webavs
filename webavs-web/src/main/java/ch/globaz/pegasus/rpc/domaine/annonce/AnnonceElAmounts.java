package ch.globaz.pegasus.rpc.domaine.annonce;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.domaine.RpcCalcul;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionAnnonceComplete;

public class AnnonceElAmounts {

    protected Montant amountNoHC;
    protected Montant amountWithHC;
    protected int elLimit;

    protected RpcDecisionAnnonceComplete annonce;

    public AnnonceElAmounts(RpcDecisionAnnonceComplete annonce) {
        this.annonce = annonce;
        RpcCalcul convertPca = annonce.getRpcCalcul();
        boolean isReforme = annonce.getPcaDecision().getPca().getReformePC();
        amountNoHC = annonce.isPcaOctroiPartiel() ? Montant.ZERO_ANNUEL : convertPca.getMontantSansAssuranceMaladie();
        amountWithHC = annonce.isRefusRaisonEco() ? Montant.ZERO_ANNUEL : convertPca.getMontantAvecAssuranceMaladie(isReforme);
        elLimit = convertPca.getPlafonnementDesPC();
    }

    public Montant getAmountNoHC() {
        return amountNoHC;
    }

    public Montant getAmountWithHC() {
        return amountWithHC;
    }

    public int getElLimit() {
        return elLimit;
    }

}
