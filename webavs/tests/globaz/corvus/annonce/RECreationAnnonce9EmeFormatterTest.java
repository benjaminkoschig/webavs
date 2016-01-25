package globaz.corvus.annonce;

import static org.junit.Assert.*;
import globaz.corvus.annonce.formatter.REAbstractCreationAnnonceFormatter;
import globaz.corvus.annonce.formatter.RECreationAnnonce9EmeRevisionFormatter;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import org.junit.Test;

public class RECreationAnnonce9EmeFormatterTest {

    @Test
    public void formatAnneeNiveau() {
        RECreationAnnonce9EmeRevisionFormatter formatter = new RECreationAnnonce9EmeRevisionFormatter();
        assertTrue("22".equals(formatter.formatAnneeNiveau(2022)));
        assertTrue("12".equals(formatter.formatAnneeNiveau(2012)));
        assertTrue("98".equals(formatter.formatAnneeNiveau(1998)));
        assertTrue("00".equals(formatter.formatAnneeNiveau(2000)));
        assertTrue("01".equals(formatter.formatAnneeNiveau(2001)));

        formatter.formatAnneeNiveau(REAbstractCreationAnnonceFormatter.MIN_YEAR_FOR_VALIDATION);
        try {
            formatter.formatAnneeNiveau(REAbstractCreationAnnonceFormatter.MIN_YEAR_FOR_VALIDATION - 1);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void formatMoisRapport() throws JAException {
        RECreationAnnonce9EmeRevisionFormatter formatter = new RECreationAnnonce9EmeRevisionFormatter();
        assertTrue("1012".equals(formatter.formatMoisRapport(new JADate(0, 10, 2012))));
    }

    @Test
    public void formatDebutDroit() throws JAException {
        RECreationAnnonce9EmeRevisionFormatter formatter = new RECreationAnnonce9EmeRevisionFormatter();
        assertTrue("1012".equals(formatter.formatDebutDroit(new JADate(0, 10, 2012))));
    }

    @Test
    public void formatReferenceCaisseInterne() {
        RECreationAnnonce9EmeRevisionFormatter formatter = new RECreationAnnonce9EmeRevisionFormatter();
        String result = formatter.formatReferenceCaisseInterne(new REPrefixPourReferenceInterneCaisseProvider() {

            @Override
            public String getPrefixPourReferenceInterneCaisse() {
                return "AUG";
            }
        }, "abcdefghijklmnopqurst");
        assertTrue(result.length() == 20);
    }
}
