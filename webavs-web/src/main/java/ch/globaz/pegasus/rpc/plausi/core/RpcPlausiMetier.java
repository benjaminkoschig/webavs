package ch.globaz.pegasus.rpc.plausi.core;

import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceCase;
import ch.globaz.pegasus.rpc.domaine.annonce.AnnonceDecision;

public interface RpcPlausiMetier<T extends PlausiResult> extends RpcPlausi<T> {

    T buildPlausi(AnnonceDecision decision, AnnonceCase data);

}
