package ch.globaz.pegasus.business.domaine;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class ListeTotalisableTest {

    class EntityForTest implements Totalisable {

        public EntityForTest(BigDecimal valeur) {
            this.valeur = valeur;
        }

        BigDecimal valeur;

        @Override
        public BigDecimal getMontant() {
            return valeur;
        }

    }

    @Test
    public void testListeStandard() {

        ListeTotalisable<EntityForTest> entities = new ListeTotalisable<ListeTotalisableTest.EntityForTest>();

        entities.addElement(new EntityForTest(new BigDecimal(12.5)));
        entities.addElement(new EntityForTest(new BigDecimal("22.05")));
        entities.addElement(new EntityForTest(new BigDecimal(10)));
        entities.addElement(new EntityForTest(new BigDecimal(12)));
        entities.addElement(new EntityForTest(new BigDecimal("12.5")));

        assertTrue(entities.size() == 5);
        assertTrue(entities.total().equals(new BigDecimal("69.05")));

        try {
            entities.elements().add(new EntityForTest(new BigDecimal(10)));
            fail();
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testListeConstructorWithOtherList() {

        List<EntityForTest> entitiesList = new ArrayList<EntityForTest>();

        entitiesList.add(new EntityForTest(new BigDecimal(12.5)));
        entitiesList.add(new EntityForTest(new BigDecimal("12.55")));
        entitiesList.add(new EntityForTest(new BigDecimal(125)));
        entitiesList.add(new EntityForTest(new BigDecimal("125")));
        entitiesList.add(new EntityForTest(new BigDecimal(12.55)));
        entitiesList.add(new EntityForTest(new BigDecimal(100.0f)));

        ListeTotalisable<EntityForTest> entities = new ListeTotalisable<ListeTotalisableTest.EntityForTest>(
                entitiesList);

        assertTrue(entities.size() == 6);
        assertTrue(entities.total().equals(new BigDecimal("387.60")));

        try {
            entities.elements().add(new EntityForTest(new BigDecimal(10)));
            fail();
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

}
