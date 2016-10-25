package ch.globaz.orion.ws.cotisation;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import ch.globaz.orion.ws.service.manager.AFReleveForSdd;

public class WebAvsCotisationsServiceImplTest {

    private DecompteMensuel createDecompteMensuel() {

        List<DecompteMensuelLine> linesDecompte = new ArrayList<DecompteMensuelLine>();
        linesDecompte.add(new DecompteMensuelLineBuilder().withIdCotisation("10").withIdRubrique(45)
                .withLibelleDe("Nein").withLibelleFr("Non").withLibelleIt("Non").withMasse(BigDecimal.ZERO)
                .withTypeCotisation("AVS").build());

        linesDecompte.add(new DecompteMensuelLineBuilder().withIdCotisation("20").withIdRubrique(100)
                .withLibelleDe("Ya").withLibelleFr("Oui").withLibelleIt("Si").withMasse(BigDecimal.ZERO)
                .withTypeCotisation("AI").build());

        DecompteMensuel decompte = new DecompteMensuelBuilder().withIdAffilie("3445").withAnneeDecompte("2015")
                .withMoisDecompte("1").withNumeroAffilie("401.1004").withLinesDecompte(linesDecompte).build();

        return decompte;

    }

    @Test
    public void testFillDecompteMensuelWithReleve() throws Exception {

        AFReleveForSdd sdd = new AFReleveForSdd();
        sdd.setIdRubrique("45");
        sdd.setMasseFacture(new BigDecimal(69999.45));

        DecompteMensuel decompte = WebAvsCotisationsServiceImpl.fillDecompteMensuelWithReleve(createDecompteMensuel(),
                sdd);

        sdd = new AFReleveForSdd();
        sdd.setIdRubrique("100");
        sdd.setMasseFacture(new BigDecimal(0.9999));

        decompte = WebAvsCotisationsServiceImpl.fillDecompteMensuelWithReleve(decompte, sdd);

        sdd = new AFReleveForSdd();
        sdd.setIdRubrique("145");
        sdd.setMasseFacture(new BigDecimal(1650.99));

        decompte = WebAvsCotisationsServiceImpl.fillDecompteMensuelWithReleve(decompte, sdd);

        for (DecompteMensuelLine line : decompte.getLinesDecompte()) {
            if (line.getIdRubrique() == 45) {
                assertEquals(line.getMasse(), new BigDecimal(69999.45));
            }
            if (line.getIdRubrique() == 100) {
                assertEquals(line.getMasse(), new BigDecimal(0.9999));
            }
        }
    }

}
