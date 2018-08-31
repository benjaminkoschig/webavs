package ch.globaz.vulpecula.documents.decompte;

import static org.junit.Assert.*;
import org.junit.Test;
import ch.globaz.vulpecula.domain.models.decompte.TypeDecompte;

/**
 * @since WebBMS 1.0
 */
public class DocumentDecompteVidePeriodiqueTest {
    private static final int NB_LINE_MAX = 29;
    DocumentDecompteVidePeriodique document;

    @Test
    public void linesToAdd_give0_return28() {
        assertEquals(28, DocumentDecompteVide.linesToAdd(0, NB_LINE_MAX, 6, TypeDecompte.PERIODIQUE));
    }

    @Test
    public void linesToAdd_give1_return27() {
        assertEquals(27, DocumentDecompteVide.linesToAdd(1, NB_LINE_MAX, 6, TypeDecompte.PERIODIQUE));
    }

    @Test
    public void linesToAdd_give28_return0() {
        assertEquals(0, DocumentDecompteVide.linesToAdd(28, NB_LINE_MAX, 6, TypeDecompte.PERIODIQUE));
    }

    @Test
    public void linesToAdd_give29_return28() {
        assertEquals(27, DocumentDecompteVide.linesToAdd(29, NB_LINE_MAX, 6, TypeDecompte.PERIODIQUE));
    }

    @Test
    public void linesToAdd_give30_return27() {
        assertEquals(27, DocumentDecompteVide.linesToAdd(30, NB_LINE_MAX, 6, TypeDecompte.PERIODIQUE));
    }

    @Test
    public void linesToAdd_give57_return0() {
        assertEquals(0, DocumentDecompteVide.linesToAdd(57, NB_LINE_MAX, 6, TypeDecompte.PERIODIQUE));
    }

    @Test
    public void linesToAdd_give58_return28() {
        assertEquals(27, DocumentDecompteVide.linesToAdd(58, NB_LINE_MAX, 6, TypeDecompte.PERIODIQUE));
    }

    @Test
    public void linesToAdd_give59_return27() {
        assertEquals(27, DocumentDecompteVide.linesToAdd(59, NB_LINE_MAX, 6, TypeDecompte.PERIODIQUE));
    }

    @Test
    public void linesToAdd_give60_return26() {
        assertEquals(26, DocumentDecompteVide.linesToAdd(60, NB_LINE_MAX, 6, TypeDecompte.PERIODIQUE));
    }

    @Test
    public void linesToAdd_give87_return28() {
        assertEquals(27, DocumentDecompteVide.linesToAdd(87, NB_LINE_MAX, 6, TypeDecompte.PERIODIQUE));
    }

    @Test
    public void linesToAdd_give87With8Lignes_27() {
        assertEquals(24, DocumentDecompteVide.linesToAdd(87, NB_LINE_MAX, 7, TypeDecompte.PERIODIQUE));
    }

    @Test
    public void linesToAdd_give87With7LignesAdresse_26() {
        assertEquals(24, DocumentDecompteVide.linesToAdd(87, NB_LINE_MAX, 7, TypeDecompte.COMPLEMENTAIRE));
    }
}
