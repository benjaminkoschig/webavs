package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce;

import java.util.List;
import ch.globaz.common.domaine.repository.Repository;
import ch.globaz.pegasus.rpc.domaine.AnnonceRpc;
import ch.globaz.pegasus.rpc.domaine.LotAnnonceRpc;
import ch.globaz.pegasus.rpc.domaine.PersonneAnnonceRpc;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionWithIdPlanCal;

public interface AnnonceRpcRepository extends Repository<AnnonceRpc> {

    AnnonceRpc create(AnnonceRpc entity, LotAnnonceRpc lotAnnonce);

    AnnonceRpc update(AnnonceRpc entity, LotAnnonceRpc lotAnnonce);

    List<PersonneAnnonceRpc> findPersonneAnnonce(AnnonceSearch annonceSearch);

    void createLienDecision(AnnonceRpc annonce, RpcDecisionWithIdPlanCal decision);

}