package ch.globaz.pegasus.rpc.domaine;


public class LotRpcWithNbAnnonces {
    private LotAnnonceRpc lot;
    private long nbAnnonces;

    public LotRpcWithNbAnnonces(final LotAnnonceRpc lot, final long nbAnnonces) {
        this.lot = lot;
        this.nbAnnonces = nbAnnonces;
    }

    public LotAnnonceRpc getLot() {
        return lot;
    }

    public long getNbAnnonces() {
        return nbAnnonces;
    }
}
