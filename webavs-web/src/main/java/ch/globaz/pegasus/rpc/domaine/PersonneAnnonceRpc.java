package ch.globaz.pegasus.rpc.domaine;

import ch.globaz.common.domaine.repository.DomainEntity;
import ch.globaz.pyxis.domaine.PersonneAVS;

public interface PersonneAnnonceRpc extends DomainEntity {
    AnnonceRpc getAnnonceRpc();

    PersonneAVS getPersonne();
}
