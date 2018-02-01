package globaz.naos.util.ide;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.ech.xmlns.ech_0097._1.UidOrganisationIdCategorieType;
import ch.ech.xmlns.ech_0097._1.UidStructureType;

public class IDESedexDefensivTest {

    private static final int CONTROLE_IDE_MESSAGE_DB_SIZE = 3072;
    private static final int CONTROLE_IDE_STANDARD_COLUMN_DB_SIZE = 255;

    @Test
    public void testDefendMessage() throws Exception {
        final String doNotChange = "' !\"#$%&\\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\\\]^_`abcdefghijklmnopqrstuvwxyz{|}~'";
        assertTrue(doNotChange.equals(IDESedexDefensiv.defendMessage(doNotChange)));
        StringBuilder max = new StringBuilder();
        for (int i = 0; i < CONTROLE_IDE_MESSAGE_DB_SIZE; i++) {
            max.append("a");
        }
        max.append("b");
        assertTrue(max.toString().contains("b"));
        assertFalse(IDESedexDefensiv.defendMessage(max.toString()).contains("b"));
    }

    @Test
    public void testDefendMessageFRDE() throws Exception {
        assertTrue("FR".equals(IDESedexDefensiv.defendMessageFRDE("FR", null)));
        assertTrue("DE".equals(IDESedexDefensiv.defendMessageFRDE(null, "DE")));
        assertTrue("DE".equals(IDESedexDefensiv.defendMessageFRDE("", "DE")));
        assertTrue("DE".equals(IDESedexDefensiv.defendMessageFRDE(" ", "DE")));
        assertFalse("DE".equals(IDESedexDefensiv.defendMessageFRDE("FR", "DE")));
        assertTrue("FR".equals(IDESedexDefensiv.defendMessageFRDE("FR", "")));
        assertTrue("FR".equals(IDESedexDefensiv.defendMessageFRDE("FR", " ")));
        assertTrue("FR / DE".equals(IDESedexDefensiv.defendMessageFRDE("FR", "DE")));
    }

    @Test
    public void testDefendUid() throws Exception {
        ch.ech.xmlns.ech_0097._1.ObjectFactory factory = new ch.ech.xmlns.ech_0097._1.ObjectFactory();
        UidStructureType uidStructureType = factory.createUidStructureType();

        assertTrue(IDESedexDefensiv.defendUid(uidStructureType) != null);
        uidStructureType.setUidOrganisationIdCategorie(UidOrganisationIdCategorieType.CHE);
        uidStructureType.setUidOrganisationId(123456789);
        assertTrue("CHE123456789".equals(IDESedexDefensiv.defendUid(uidStructureType)));
    }

    @Test
    public void testDefendStdString() throws Exception {
        final String doNotChange = "' !\"#$%&\\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\\\]^_`abcdefghijklmnopqrstuvwxyz{|}~'";
        assertTrue(doNotChange.equals(IDESedexDefensiv.defendStdString(doNotChange)));
        StringBuilder max = new StringBuilder();
        for (int i = 0; i < CONTROLE_IDE_STANDARD_COLUMN_DB_SIZE; i++) {
            max.append("a");
        }
        max.append("b");
        assertTrue(max.toString().contains("b"));
        assertFalse(IDESedexDefensiv.defendStdString(max.toString()).contains("b"));
    }

    @Test
    public void testDefendNoga() throws Exception {
        assertTrue(IDESedexDefensiv.defendNoga(null) == null);
    }

    @Test
    public void testDefendRaisonSociale() throws Exception {
        final String doNotChange = "' !\"#$%&\\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\\\]^_`abcdefghijklmnopqrstuvwxyz{|}~'";
        assertTrue(doNotChange.equals(IDESedexDefensiv.defendRaisonSociale(doNotChange, null, null)));
        StringBuilder max = new StringBuilder();
        for (int i = 0; i < CONTROLE_IDE_STANDARD_COLUMN_DB_SIZE; i++) {
            max.append("a");
        }
        max.append("b");
        assertTrue(max.toString().contains("b"));
        assertFalse(IDESedexDefensiv.defendRaisonSociale(max.toString(), null, null).contains("b"));
        assertTrue("Nom Prenom".equals(IDESedexDefensiv.defendRaisonSociale(null, "Nom", "Prenom")));

    }

}
