package ch.globaz.vulpecula.process.decompte;

import org.junit.Assert;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;
import ch.globaz.vulpecula.process.decompte.population.PTProcessDecomptePopulation;

/**
 * @author Arnaud Geiser (AGE) | Créé le 17 mars 2014
 * 
 */
public class PTProcessDecompteInfoTest {
    private PTProcessDecompteInfo decompteInfo;

    private static final String SERIALIZED_DECOMPTE_INFO = "68014001;201403;201403;20140317;0;MENSUELLE_TRIMESTRIELLE";

    @Test
    public void givenValidDecompteInfoShouldBeValid() {
        decompteInfo = new PTProcessDecompteInfo(TypeDecompte.PERIODIQUE.getValue(), "201403", "201403", "17.03.2014",
                0, PTProcessDecomptePopulation.PERIODICITE_MENSUELLE_TRIMESTRIELLE);
    }

    @Test
    public void givenDecompteInfoWhenSerializeShouldReturnCSV() {
        decompteInfo = new PTProcessDecompteInfo(TypeDecompte.PERIODIQUE.getValue(), "201403", "201403", "17.03.2014",
                0, PTProcessDecomptePopulation.PERIODICITE_MENSUELLE_TRIMESTRIELLE);

        Assert.assertEquals(SERIALIZED_DECOMPTE_INFO, decompteInfo.serialize());
    }

    @Test
    public void givenSerializedDecompteInfoWhenCreateFromPersistenceShouldBeValid() {
        decompteInfo = PTProcessDecompteInfo.createFromPersistence(SERIALIZED_DECOMPTE_INFO);
    }
}
