package ch.globaz.common.util.prestations;

import static org.junit.Assert.*;
import org.junit.Test;

public class MotifVersementUtilTest {

    @Test
    public void testFormatPaiementMensuel() throws Exception {
        String formatAttendu = "756.0000.0000.00 nomPrenom refPaiement genrePrest pP2";
        assertTrue(formatAttendu.equals(MotifVersementUtil.formatPaiementMensuel("756.0000.0000.00", "nomPrenom",
                "refPaiement", "genrePrest", "pP2")));

        formatAttendu = "756.0000.0000.00 nomPrenomQuiAdditionn�AuNSSFaitDePlusDe35Caract�reMaisNeFaitPasDepasserLeMotifDes140Caract�res refPaiement genrePrest pP2";
        assertTrue(formatAttendu.equals(MotifVersementUtil.formatPaiementMensuel("756.0000.0000.00",
                "nomPrenomQuiAdditionn�AuNSSFaitDePlusDe35Caract�reMaisNeFaitPasDepasserLeMotifDes140Caract�res",
                "refPaiement", "genrePrest", "pP2")));
        // Si depassement:
        formatAttendu = "756.0000.0000.00 nomPrenomQuiAdditionn�AuNSSFaitDePlusDe35Caract�reEtSurtoutFaitPasDepasserLeMotifDes140Caract�res refPaiement genrePrest pP2";
        assertFalse(formatAttendu.equals(MotifVersementUtil.formatPaiementMensuel("756.0000.0000.00",
                "nomPrenomQuiAdditionn�AuNSSFaitDePlusDe35Caract�reEtSurtoutFaitPasDepasserLeMotifDes140Caract�res",
                "refPaiement", "genrePrest", "pP2")));
        formatAttendu = "756.0000.0000.00 nomPrenomQuiAddit refPaiement genrePrest pP2";
        assertTrue(formatAttendu.equals(MotifVersementUtil.formatPaiementMensuel("756.0000.0000.00",
                "nomPrenomQuiAdditionn�AuNSSFaitDePlusDe35Caract�reEtSurtoutFaitPasDepasserLeMotifDes140Caract�res",
                "refPaiement", "genrePrest", "pP2")));
    }

}
