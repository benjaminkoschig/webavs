package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import static org.fest.assertions.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import ch.globaz.pegasus.business.domaine.droit.EtatDroit;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.rpc.business.models.RPCDecionsPriseDansLeMois;

public class RpcDatasFilterTest {

    @Test
    public void testFiltreWith6Correction() throws Exception {
        List<RPCDecionsPriseDansLeMois> rpcDatas = new ArrayList<RPCDecionsPriseDansLeMois>();
        rpcDatas.add(create("1", EtatDroit.HISTORISE, "10"));
        rpcDatas.add(create("6", EtatDroit.VALIDE, "10"));
        rpcDatas.add(create("2", EtatDroit.HISTORISE, "10"));
        rpcDatas.add(create("5", EtatDroit.HISTORISE, "10"));
        rpcDatas.add(create("3", EtatDroit.HISTORISE, "10"));
        RpcDatasFilter filtrer = new RpcDatasFilter();
        Map<String, List<RPCDecionsPriseDansLeMois>> map = filtrer.filtre(rpcDatas);
        assertThat(map).hasSize(1);
        assertThat(map.values()).hasSize(1);
        assertThat(map.values()).hasSize(1);
        assertThat(map.values().iterator().next()).hasSize(1);
        assertThat(map.values().iterator().next().get(0).getSimpleVersionDroit().getNoVersion()).isEqualTo("6");
    }

    @Test
    public void testFiltreWith6CorrectionCoupleSepare() throws Exception {
        List<RPCDecionsPriseDansLeMois> rpcDatas = new ArrayList<RPCDecionsPriseDansLeMois>();
        rpcDatas.add(create("1", EtatDroit.HISTORISE, "10"));
        rpcDatas.add(create("6", EtatDroit.VALIDE, "10"));
        rpcDatas.add(create("2", EtatDroit.HISTORISE, "10"));
        rpcDatas.add(create("5", EtatDroit.HISTORISE, "10"));
        rpcDatas.add(create("3", EtatDroit.HISTORISE, "10"));
        rpcDatas.add(create("6", EtatDroit.VALIDE, "10"));
        rpcDatas.add(create("5", EtatDroit.HISTORISE, "10"));
        RpcDatasFilter filtrer = new RpcDatasFilter();
        Map<String, List<RPCDecionsPriseDansLeMois>> map = filtrer.filtre(rpcDatas);
        assertThat(map).hasSize(1);
        assertThat(map.values()).hasSize(1);
        assertThat(map.values()).hasSize(1);
        assertThat(map.values().iterator().next()).hasSize(2);
        assertThat(map.values().iterator().next().get(0).getSimpleVersionDroit().getNoVersion()).isEqualTo("6");
        assertThat(map.values().iterator().next().get(1).getSimpleVersionDroit().getNoVersion()).isEqualTo("6");
    }

    @Test
    public void testFiltreWith1Correction() throws Exception {
        List<RPCDecionsPriseDansLeMois> rpcDatas = new ArrayList<RPCDecionsPriseDansLeMois>();
        rpcDatas.add(create("6", EtatDroit.VALIDE, "10"));
        RpcDatasFilter filtrer = new RpcDatasFilter();
        assertThat(filtrer.filtre(rpcDatas)).hasSize(1);
    }

    private RPCDecionsPriseDansLeMois create(String noVersion, EtatDroit etat, String idDemande) {
        RPCDecionsPriseDansLeMois decision = new RPCDecionsPriseDansLeMois();
        decision.setIdDemande(idDemande);
        decision.setSimpleVersionDroit(new SimpleVersionDroit());
        decision.getSimpleVersionDroit().setIdVersionDroit(noVersion);
        decision.getSimpleVersionDroit().setNoVersion(noVersion);
        return decision;
    }
}
