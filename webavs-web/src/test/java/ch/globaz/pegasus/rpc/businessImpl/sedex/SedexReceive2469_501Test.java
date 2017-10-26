package ch.globaz.pegasus.rpc.businessImpl.sedex;

import static org.fest.assertions.api.Assertions.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.pegasus.rpc.domaine.RetourAnnonce;
import ch.globaz.pegasus.rpc.domaine.plausi.AnnoncePlausiRetour;
import ch.globaz.pegasus.rpc.domaine.plausi.PlausiRetour;
import ch.globaz.pegasus.rpc.plausi.PlausiBeanRetour;
import ch.globaz.pegasus.rpc.plausi.PlausiBeanSummary;
import ch.globaz.simpleoutputlist.outimpl.SimpleOutputListBuilder;

public class SedexReceive2469_501Test {

    @Ignore("permet de générer la liste des retour (utilitaire de dev/test, ne pas tourner en intégration continue)")
    @Test
    public void testLoad() throws Exception {
        SedexReceive2469_501 sedex = new SedexReceive2469_501();
        List<AnnoncePlausiRetour> messages = sedex.load();
        List<PlausiBeanRetour> beans = new ArrayList<PlausiBeanRetour>();
        Map<String, RetourAnnonce> mapPlausi = new HashMap<String, RetourAnnonce>();
        Map<String, Integer> mapCount = new HashMap<String, Integer>();

        for (AnnoncePlausiRetour annoncePlausiRetour : messages) {
            for (PlausiRetour retour : annoncePlausiRetour.getPlausiRetours()) {
                for (RetourAnnonce plausi : retour.getRetours()) {
                    beans.add(new PlausiBeanRetour(annoncePlausiRetour.getBusinessCaseIdRPC(), retour.getNss(), retour
                            .getIdDecision(), plausi));
                    if (!mapPlausi.containsKey(plausi.getId())) {
                        mapCount.put(plausi.getId(), 1);
                        mapPlausi.put(plausi.getId(), plausi);
                    } else {
                        mapCount.put(plausi.getId(), mapCount.get(plausi.getId()) + 1);
                    }
                }
            }
        }

        System.out.println("Nb annonce avec une plausi en retour: " + messages.size());

        List<PlausiBeanSummary> beanSummaries = new ArrayList<PlausiBeanSummary>();
        for (Entry<String, Integer> entry : mapCount.entrySet()) {
            beanSummaries.add(new PlausiBeanSummary(mapPlausi.get(entry.getKey()), entry.getValue()));
        }

        File file = SimpleOutputListBuilder.newInstance().addList(beans).classElementList(PlausiBeanRetour.class)
                .addList(beanSummaries).classElementList(PlausiBeanSummary.class).outputName("C:\\plaussi").asXls()
                .build();

        file = SimpleOutputListBuilder.newInstance().addList(beanSummaries).classElementList(PlausiBeanSummary.class)
                .addSubTitle("plausis").outputName("C:\\summary").asXls().build();

        assertThat(messages).isNotEmpty();
    }
}
