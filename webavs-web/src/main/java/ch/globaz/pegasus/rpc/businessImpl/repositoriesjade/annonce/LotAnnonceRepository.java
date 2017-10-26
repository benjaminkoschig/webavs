package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.annonce;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.persistence.DomaineConverterJade;
import ch.globaz.common.persistence.RepositoryJade;
import ch.globaz.pegasus.rpc.business.models.SimpleAnnonceSearch;
import ch.globaz.pegasus.rpc.business.models.SimpleLotAnnonce;
import ch.globaz.pegasus.rpc.business.models.SimpleLotAnnonceSearch;
import ch.globaz.pegasus.rpc.domaine.AnnonceRpc;
import ch.globaz.pegasus.rpc.domaine.LotAnnonceRpc;
import ch.globaz.pegasus.rpc.domaine.LotRpcWithNbAnnonces;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionWithIdPlanCal;

public class LotAnnonceRepository extends RepositoryJade<LotAnnonceRpc, SimpleLotAnnonce> {

    @Override
    public LotAnnonceRpc create(LotAnnonceRpc entity) {
        LotAnnonceRpc lot = super.create(entity);
        AnnonceRpcRepository annonceRepositoryJade = new AnnonceRepositoryJade();

        for (AnnonceRpc annonce : entity.getAnnonces()) {
            annonceRepositoryJade.create(annonce, lot);
            for (RpcDecisionWithIdPlanCal decision : annonce.getDecisions()) {
                annonceRepositoryJade.createLienDecision(annonce, decision);
            }
        }
        return lot;
    }

    public List<LotRpcWithNbAnnonces> searchLastsLots(int lastSize) {
        final List<LotRpcWithNbAnnonces> lastsLots = new ArrayList<LotRpcWithNbAnnonces>();

        final SimpleLotAnnonceSearch lotSearchModel = new SimpleLotAnnonceSearch();
        lotSearchModel.setDefinedSearchSize(lastSize);
        lotSearchModel.setOrderKey(SimpleLotAnnonceSearch.ORDER_BY_IDLOT_DESC);

        final List<SimpleLotAnnonce> lots = searchForAndFetch(lotSearchModel);
        int lotCounter = 0;
        for (SimpleLotAnnonce lotDb : lots) {
            if (lotCounter < lastSize) {
                final LotAnnonceRpc lotMetier = getConverter().convertToDomain(lotDb);

                final SimpleAnnonceSearch annonceSearchModel = new SimpleAnnonceSearch();
                annonceSearchModel.setForIdLot(lotMetier.getId());

                int nbAnnonces = count(annonceSearchModel);
                lastsLots.add(new LotRpcWithNbAnnonces(lotMetier, nbAnnonces));
                lotCounter++;
            }
        }

        return lastsLots;
    }

    @Override
    public DomaineConverterJade<LotAnnonceRpc, SimpleLotAnnonce> getConverter() {
        return new LotAnnonceConverter();
    }

}
