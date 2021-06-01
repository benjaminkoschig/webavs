package ch.globaz.pegasus.rpc.businessImpl.repositoriesjade.loader;

import static org.assertj.core.api.Assertions.*;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;

public class IdsContainerTest {
    @Ignore("not complient under IMB JDK")
    @Test
    public void testPartion() throws Exception {
        IdsContainer idsContainer = new IdsContainer();
        for (int i = 1; i < 11; i++) {
            String idPca = String.valueOf(3 * i);
            String idTiersDomicile = String.valueOf(5 * i);
            String idTiersCourrier = String.valueOf(7 * i);
            String idPlanCal = String.valueOf(11 * i);
            String idDroit = String.valueOf(13 * i);
            String idVersionDroit = String.valueOf(i);
            String idPcaOriginale = String.valueOf(19 * i);
            idsContainer.add(idPca, idTiersDomicile, idTiersCourrier, idPlanCal, idDroit, idVersionDroit,
                    idPcaOriginale);
        }

        List<IdsContainer> containers = idsContainer.partion(3);
        assertThat(containers).hasSize(4);
        assertThat(containers.get(0).getIdsPca()).contains("30", "6", "9");
        assertThat(containers.get(0).getIdsTiersDomicile()).contains("50", "10", "15");
        assertThat(containers.get(0).getIdsTiersCourrier()).contains("70", "14", "21");
        assertThat(containers.get(0).getIdsDroit()).contains("130", "26", "39");
        assertThat(containers.get(0).getIdsVersionDroit()).contains("3", "2", "10");

    }
}
