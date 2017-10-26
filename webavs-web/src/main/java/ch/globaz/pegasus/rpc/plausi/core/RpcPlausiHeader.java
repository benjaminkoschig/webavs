package ch.globaz.pegasus.rpc.plausi.core;

public abstract class RpcPlausiHeader implements PlausiResult {

    private transient RpcPlausi plausi;

    public RpcPlausiHeader(RpcPlausi plausi) {
        this.plausi = plausi;
    }

    @Override
    public RpcPlausi getPlausi() {
        return plausi;
    }

}
