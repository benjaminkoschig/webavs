package ch.globaz.pegasus.rpc.plausi.core;

import java.util.List;

public interface RpcPlausi<T extends PlausiResult> {

    RpcPlausiType getType();

    String getID();

    String getReferance();

    RpcPlausiCategory getCategory();

    List<RpcPlausiApplyToDecision> getApplyTo();

}
