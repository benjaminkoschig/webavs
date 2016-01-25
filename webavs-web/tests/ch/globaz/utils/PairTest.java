package ch.globaz.utils;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public class PairTest {
    private Administration admin;
    private Administration admin2;

    @Before
    public void setUp() {
        admin = new Administration();
        admin.setId("1");
        admin2 = new Administration();
        admin2.setId("2");
    }

    @Test
    public void equals_TwoSamePair_ShouldReturnTrue() {
        Pair<Administration, Administration> pair = new Pair<Administration, Administration>(admin, admin2);
        Pair<Administration, Administration> pair2 = new Pair<Administration, Administration>(admin, admin2);
        assertTrue(pair.equals(pair2));
    }
}
