package ch.globaz.pegasus.rpc.plausi.core;

import ch.globaz.pegasus.business.domaine.pca.PcaSituation;
import ch.globaz.pegasus.rpc.domaine.MembresFamilleWithDonneesFinanciere;
import ch.globaz.pegasus.rpc.domaine.RpcPcaDecisionCalculElementCalcul;

public interface RpcPlausiCouple<T extends PlausiResult> extends RpcPlausi<T> {

    T buildPlausiCouple(RpcPcaDecisionCalculElementCalcul data, RpcPcaDecisionCalculElementCalcul dataPartner,
            MembresFamilleWithDonneesFinanciere membresFamilleWithDonneesFinanciere, PcaSituation situation);

}
