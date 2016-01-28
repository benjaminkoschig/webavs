package globaz.corvus.annonce;

import static org.junit.Assert.*;
import globaz.corvus.annonce.reader.REBaseDeCalcul10EmeRevisionReader;
import org.junit.Test;

public class REBaseDeCalcul10EmeRevisionReaderTest {

    @Test
    public void readNombreAnneeBTE_valeurEntiere() {
        REBaseDeCalcul10EmeRevisionReader reader = new REBaseDeCalcul10EmeRevisionReader();
        assertTrue(4 == reader.readNombreAnneeBTE_valeurEntiere("04.66"));
        assertTrue(20 == reader.readNombreAnneeBTE_valeurEntiere("20.60"));
        assertTrue(12 == reader.readNombreAnneeBTE_valeurEntiere("12.00"));
        assertTrue(null == reader.readNombreAnneeBTE_valeurEntiere("00.75"));
        assertTrue(null == reader.readNombreAnneeBTE_valeurEntiere("101"));
        assertTrue(null == reader.readNombreAnneeBTE_valeurEntiere("4"));
    }

    @Test
    public void readNombreAnneeBTE_valeurDecimal() {
        REBaseDeCalcul10EmeRevisionReader reader = new REBaseDeCalcul10EmeRevisionReader();
        assertTrue(66 == reader.readNombreAnneeBTE_valeurDecimal("04.66"));
        assertTrue(60 == reader.readNombreAnneeBTE_valeurDecimal("20.60"));
        assertTrue(null == reader.readNombreAnneeBTE_valeurDecimal("12.00"));
        assertTrue(75 == reader.readNombreAnneeBTE_valeurDecimal("00.75"));
        assertTrue(null == reader.readNombreAnneeBTE_valeurDecimal("101"));
        assertTrue(null == reader.readNombreAnneeBTE_valeurEntiere("4"));
    }

}
