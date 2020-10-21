package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import static org.fest.assertions.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import ch.globaz.common.domaine.Date;
import ch.globaz.pegasus.business.domaine.decision.Decision;
import ch.globaz.pegasus.business.domaine.decision.TypeDecision;
import ch.globaz.pegasus.business.domaine.demande.Demande;
import ch.globaz.pegasus.business.domaine.dossier.Dossier;
import ch.globaz.pegasus.business.domaine.pca.Pca;
import ch.globaz.pegasus.business.domaine.pca.PcaDecision;
import ch.globaz.pegasus.business.domaine.pca.PcaEtatCalcul;
import ch.globaz.pegasus.business.domaine.pca.PcaGenre;
import ch.globaz.pegasus.rpc.domaine.RpcData;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionRequerantConjoint;

public class RpcDataDecisionFilterTest {
    @Test
    public void testFiltre() throws Exception {
        RpcData data = new RpcData(new Dossier(), new Demande());
        RpcDecisionRequerantConjoint r02 = createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20150101", "20151231",
                PcaGenre.DOMICILE, PcaEtatCalcul.OCTROYE);
        RpcDecisionRequerantConjoint r01 = createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20140114", "20140601",
                PcaGenre.DOMICILE, PcaEtatCalcul.OCTROYE);
        RpcDecisionRequerantConjoint r1 = createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20151231", "20151231",
                PcaGenre.DOMICILE, PcaEtatCalcul.REFUSE);
        RpcDecisionRequerantConjoint r2 = createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20160102", "20161231",
                PcaGenre.DOMICILE, PcaEtatCalcul.OCTROYE);
        RpcDecisionRequerantConjoint r3 = createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20170101", "20170101",
                PcaGenre.DOMICILE, PcaEtatCalcul.REFUSE);
        data.add(r1);
        RpcDataDecisionFilter.filtre(data);
        assertThat(data.getRpcDecisionRequerantConjoints().size()).isEqualTo(1);
        assertThat(data.getRpcDecisionRequerantConjoints().get(0)).isEqualTo(r1);
        data.add(r3);
        RpcDataDecisionFilter.filtre(data);
        assertThat(data.getRpcDecisionRequerantConjoints().size()).isEqualTo(1);
        assertThat(data.getRpcDecisionRequerantConjoints().get(0)).isEqualTo(r3);

        data.add(r02);
        data.add(r2);
        data.add(r01);
        assertThat(data.getRpcDecisionRequerantConjoints().size()).isEqualTo(4);
        RpcDataDecisionFilter.filtre(data);
        assertThat(data.getRpcDecisionRequerantConjoints().size()).isEqualTo(1);
        assertThat(data.getRpcDecisionRequerantConjoints().get(0)).isEqualTo(r2);

        assertThat(data.getRpcDecisionRequerantConjoints().get(0).getRequerant().getPca().getDateDebut()).isEqualTo(
                new Date("20160102"));
        assertThat(data.getRpcDecisionRequerantConjoints().get(0).getRequerant().getPca().getDateFin()).isEqualTo(
                new Date("20161231"));

        RpcDecisionRequerantConjoint r4 = createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20170101", null,
                PcaGenre.DOMICILE, PcaEtatCalcul.OCTROYE);
        data.add(r4);
        RpcDataDecisionFilter.filtre(data);
        assertThat(data.getRpcDecisionRequerantConjoints().size()).isEqualTo(1);
        assertThat(data.getRpcDecisionRequerantConjoints().get(0)).isEqualTo(r4);
        assertThat(data.getRpcDecisionRequerantConjoints().get(0).getRequerant().getPca().getDateDebut()).isEqualTo(
                new Date("20170101"));
        assertThat(data.getRpcDecisionRequerantConjoints().get(0).getRequerant().getPca().getDateFin()).isNull();
    }

    @Test
    public void testKeepLast() throws Exception {
        RpcData data = new RpcData(new Dossier(), new Demande());
        RpcDecisionRequerantConjoint r1 = createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20150101", "20150101",
                PcaGenre.DOMICILE, PcaEtatCalcul.REFUSE);
        RpcDecisionRequerantConjoint r2 = createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20160101", "20160101",
                PcaGenre.DOMICILE, PcaEtatCalcul.REFUSE);
        data.add(r2);
        data.add(r1);
        assertThat(data.getRpcDecisionRequerantConjoints().size()).isEqualTo(2);
        RpcDataDecisionFilter.keepLast(data);
        assertThat(data.getRpcDecisionRequerantConjoints().size()).isEqualTo(1);
        assertThat(data.getRpcDecisionRequerantConjoints().get(0)).isEqualTo(r2);
        RpcDecisionRequerantConjoint r4 = createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20170101", "20170101",
                PcaGenre.DOMICILE, PcaEtatCalcul.REFUSE);
        RpcDecisionRequerantConjoint r3 = createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20160101", "20160101",
                PcaGenre.DOMICILE, PcaEtatCalcul.REFUSE);
        data.add(r4);
        data.add(r3);
        assertThat(data.getRpcDecisionRequerantConjoints().size()).isEqualTo(3);
        RpcDataDecisionFilter.keepLast(data);
        assertThat(data.getRpcDecisionRequerantConjoints().size()).isEqualTo(1);
        assertThat(data.getRpcDecisionRequerantConjoints().get(0)).isEqualTo(r4);
    }

    private RpcDecisionRequerantConjoint createRpcDeci(TypeDecision typeDecision, String dateDebutyyyymmdd,
            String dateFinyyyymmdd, PcaGenre genre, PcaEtatCalcul pcaEtatCalcul) {
        Decision decsion = new Decision();

        decsion.setType(typeDecision);
        Pca pca = new Pca();
        pca.setEtatCalcul(pcaEtatCalcul);
        pca.setDateDebut(new Date(dateDebutyyyymmdd));
        decsion.setDateDebut(new Date(dateDebutyyyymmdd));
        if (dateFinyyyymmdd != null) {
            pca.setDateFin(new Date(dateFinyyyymmdd));
            decsion.setDateFin(new Date(dateFinyyyymmdd));
        }
        pca.setGenre(genre);
        return new RpcDecisionRequerantConjoint(new Demande(), new PcaDecision(pca, decsion), null, null, null, null);
    }

    @Test
    public void testKeepLastPositiv() throws Exception {
        RpcData data = new RpcData(new Dossier(), new Demande());
        RpcDecisionRequerantConjoint r1 = createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20150101", "20151231",
                PcaGenre.DOMICILE, PcaEtatCalcul.REFUSE);
        RpcDecisionRequerantConjoint r2 = createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20160101", "20160101",
                PcaGenre.DOMICILE, PcaEtatCalcul.OCTROYE);
        data.add(r2);
        data.add(r1);
        assertThat(data.getRpcDecisionRequerantConjoints().size()).isEqualTo(2);

        RpcDecisionRequerantConjoint r3 = createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20160101", "20161231",
                PcaGenre.DOMICILE, PcaEtatCalcul.REFUSE);
        data.add(r3);
        assertThat(data.getRpcDecisionRequerantConjoints().size()).isEqualTo(3);
        RpcDataDecisionFilter.keepLastPositiv(data);
        assertThat(data.getRpcDecisionRequerantConjoints().size()).isEqualTo(1);
        assertThat(data.getRpcDecisionRequerantConjoints().get(0)).isEqualTo(r2);
        RpcDecisionRequerantConjoint r4 = createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20170101", null,
                PcaGenre.DOMICILE, PcaEtatCalcul.OCTROYE);
        data.add(r4);
        RpcDataDecisionFilter.keepLastPositiv(data);
        assertThat(data.getRpcDecisionRequerantConjoints().size()).isEqualTo(1);
        assertThat(data.getRpcDecisionRequerantConjoints().get(0)).isEqualTo(r4);

    }

    @Test
    public void testKeepCurrent() throws Exception {
        RpcData data = new RpcData(new Dossier(), new Demande());
        RpcDecisionRequerantConjoint r1 = createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20150101", "20151231",
                PcaGenre.DOMICILE, PcaEtatCalcul.REFUSE);
        RpcDecisionRequerantConjoint r2 = createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20160101", "20160101",
                PcaGenre.DOMICILE, PcaEtatCalcul.OCTROYE);
        data.add(r2);
        data.add(r1);
        assertThat(data.getRpcDecisionRequerantConjoints().size()).isEqualTo(2);

        RpcDecisionRequerantConjoint r3 = createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20160101", "20161231",
                PcaGenre.DOMICILE, PcaEtatCalcul.REFUSE);
        data.add(r3);
        assertThat(data.getRpcDecisionRequerantConjoints().size()).isEqualTo(3);
        try {
            RpcDataDecisionFilter.keepCurrent(data);

        } catch (NullPointerException e) {
            assertThat(true);
        }

        RpcDecisionRequerantConjoint r4 = createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20170101", null,
                PcaGenre.DOMICILE, PcaEtatCalcul.OCTROYE);
        data.add(r4);
        RpcDataDecisionFilter.keepCurrent(data);
        assertThat(data.getRpcDecisionRequerantConjoints().size()).isEqualTo(1);
        assertThat(data.getRpcDecisionRequerantConjoints().get(0)).isEqualTo(r4);
        assertThat(data.getRpcDecisionRequerantConjoints().get(0).getRequerant().getPca().getDateDebut()).isEqualTo(
                new Date("20170101"));
    }

    @Test
    public void testIsOnlyNegativ() throws Exception {
        List<RpcDecisionRequerantConjoint> rpcDatas = new ArrayList<RpcDecisionRequerantConjoint>();
        rpcDatas.add(createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20150101", null, PcaGenre.DOMICILE,
                PcaEtatCalcul.REFUSE));
        assertThat(RpcDataDecisionFilter.isOnlyNegativ(rpcDatas)).isTrue();
        rpcDatas.add(createRpcDeci(TypeDecision.OCTROI_APRES_CALCUL, "20150201", null, PcaGenre.DOMICILE,
                PcaEtatCalcul.OCTROYE));
        assertThat(RpcDataDecisionFilter.isOnlyNegativ(rpcDatas)).isFalse();
    }
}
