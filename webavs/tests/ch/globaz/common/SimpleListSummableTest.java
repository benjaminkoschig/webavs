package ch.globaz.common;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class SimpleListSummableTest {
    @Test
    public void testListeStandard() {

        SimpleListSummable<Float> list = new SimpleListSummable<Float>();

        list.add(new Float(12.5));
        list.add(new Float("22.05"));
        list.add(new Float(10));
        list.add(new Float(12));
        list.add(new Float("12.5"));

        assertTrue(list.size() == 5);
        assertTrue(list.sum().equals(new BigDecimal("69.05")));

        try {
            list.elements().add(new Float(10));
            fail();
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testListEmpty() {

        SimpleListSummable<Float> list = new SimpleListSummable<Float>();
        assertEquals(BigDecimal.ZERO.setScale(2), list.sum());
    }

    @Test
    public void testListeConstructorWithOtherList() {

        List<Float> list = new ArrayList<Float>();

        list.add(new Float(12.5));
        list.add(new Float("12.55"));
        list.add(new Float(125));
        list.add(new Float("125"));
        list.add(new Float(12.55));
        list.add(new Float(100.0f));
        SimpleListSummable<Float> entities = new SimpleListSummable<Float>(list);

        assertTrue(entities.size() == 6);
        assertTrue(entities.sum().equals(new BigDecimal("387.60")));

        try {
            entities.elements().add(new Float(10));
            fail();
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }
}
