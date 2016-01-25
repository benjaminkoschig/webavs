package globaz.corvus.annonce;

import static org.junit.Assert.*;
import globaz.corvus.annonce.formatter.RECreationAnnonce10EmeRevisionFormatter;
import org.junit.Test;

public class RECreationAnnonce10EmeFormatterTest {

    @Test
    public void formatCodeCasSpecial() {
        RECreationAnnonce10EmeRevisionFormatter formatter = new RECreationAnnonce10EmeRevisionFormatter();
        assertTrue("".equals(formatter.formatCodeCasSpecial(0)));
        assertTrue("12".equals(formatter.formatCodeCasSpecial(12)));
        assertTrue("08".equals(formatter.formatCodeCasSpecial(8)));
        assertTrue("".equals(formatter.formatCodeCasSpecial(00)));
        assertTrue("".equals(formatter.formatCodeCasSpecial(100)));
        assertTrue("99".equals(formatter.formatCodeCasSpecial(99)));
    }

    @Test
    public void formatDureeAjournement() {
        RECreationAnnonce10EmeRevisionFormatter formatter = new RECreationAnnonce10EmeRevisionFormatter();
        assertTrue("000".equals(formatter.formatDureeAjournement(0, 0)));
        assertTrue("102".equals(formatter.formatDureeAjournement(1, 2)));
        assertTrue("500".equals(formatter.formatDureeAjournement(5, 0)));
        assertTrue("".equals(formatter.formatDureeAjournement(null, 2)));
        assertTrue("".equals(formatter.formatDureeAjournement(1, null)));
        // max 9 année ?? assertTrue("211".equals(formatter.formatDureeAjournement(10,11)));
    }

    @Test
    public void formatNombreAnneeBTA() {
        RECreationAnnonce10EmeRevisionFormatter formatter = new RECreationAnnonce10EmeRevisionFormatter();
        assertTrue("".equals(formatter.formatNombreAnneeBTA(0, 0)));
        assertTrue("".equals(formatter.formatNombreAnneeBTA(null, 0)));
        assertTrue("".equals(formatter.formatNombreAnneeBTA(0, null)));
        assertTrue("".equals(formatter.formatNombreAnneeBTA(null, null)));
        assertTrue("0100".equals(formatter.formatNombreAnneeBTA(1, 0)));
        assertTrue("0100".equals(formatter.formatNombreAnneeBTA(1, null)));
        assertTrue("0001".equals(formatter.formatNombreAnneeBTA(0, 1)));
        assertTrue("0001".equals(formatter.formatNombreAnneeBTA(null, 1)));
        assertTrue("1105".equals(formatter.formatNombreAnneeBTA(11, 5)));
    }

    @Test
    public void formatNombreAnneeBTE() {
        RECreationAnnonce10EmeRevisionFormatter formatter = new RECreationAnnonce10EmeRevisionFormatter();
        assertTrue("".equals(formatter.formatNombreAnneeBTE(0, 0)));
        assertTrue("".equals(formatter.formatNombreAnneeBTE(null, 0)));
        assertTrue("".equals(formatter.formatNombreAnneeBTE(0, null)));
        assertTrue("".equals(formatter.formatNombreAnneeBTE(null, null)));
        assertTrue("0100".equals(formatter.formatNombreAnneeBTE(1, 0)));
        assertTrue("0100".equals(formatter.formatNombreAnneeBTE(1, null)));
        assertTrue("0001".equals(formatter.formatNombreAnneeBTE(0, 1)));
        assertTrue("0001".equals(formatter.formatNombreAnneeBTE(null, 1)));
        assertTrue("1105".equals(formatter.formatNombreAnneeBTE(11, 5)));
    }

    @Test
    public void formatNombreAnneeBonifTrans() {
        RECreationAnnonce10EmeRevisionFormatter formatter = new RECreationAnnonce10EmeRevisionFormatter();
        assertTrue("".equals(formatter.formatNombreAnneeBonifTrans(0, false)));
        assertTrue("".equals(formatter.formatNombreAnneeBonifTrans(null, null)));
        assertTrue("".equals(formatter.formatNombreAnneeBonifTrans(0, null)));
        assertTrue("".equals(formatter.formatNombreAnneeBonifTrans(null, false)));
        assertTrue("05".equals(formatter.formatNombreAnneeBonifTrans(null, true)));
        assertTrue("10".equals(formatter.formatNombreAnneeBonifTrans(1, false)));
        assertTrue("15".equals(formatter.formatNombreAnneeBonifTrans(1, true)));
        assertTrue("05".equals(formatter.formatNombreAnneeBonifTrans(0, true)));
        assertTrue("75".equals(formatter.formatNombreAnneeBonifTrans(7, true)));
    }

    @Test
    public void formatReductionAnticipation() {
        RECreationAnnonce10EmeRevisionFormatter formatter = new RECreationAnnonce10EmeRevisionFormatter();
        assertTrue("".equals(formatter.formatReductionAnticipation(0)));
        assertTrue("".equals(formatter.formatReductionAnticipation(null)));
        assertTrue("00001".equals(formatter.formatReductionAnticipation(1)));
        assertTrue("00956".equals(formatter.formatReductionAnticipation(956)));
        assertTrue("01024".equals(formatter.formatReductionAnticipation(1024)));
    }

    @Test
    public void formatDateDebutAnticipation() throws IllegalArgumentException {

    }

    @Test
    public void formatNombreAnneeAnticipation() {

    }

}
