package ch.globaz.common.domaine;

import org.junit.Assert;
import org.junit.Test;

public class UniqueIDEqualHashCodeTest {

    class Class1 extends UniqueIDEqualHashCode {

        @Override
        public Long getId() {
            return memeID;
        }
    }

    class Class2 extends UniqueIDEqualHashCode {

        @Override
        public Long getId() {
            return memeID;
        }
    }

    final Long memeID = 1l;

    @Test
    public void objetsDifferentsPourLeMemeIDMaisPasLaMemeClasse() {
        Assert.assertNotEquals(new Class1().hashCode(), new Class2().hashCode());
        Assert.assertFalse(new Class1().equals(new Class2()));
    }

    @Test
    public void objetsIdentiquesPourDeuxInstancesDeLaMemeClasseAvecLeMemeID() {
        Assert.assertEquals(new Class1().hashCode(), new Class1().hashCode());
        Assert.assertTrue(new Class1().equals(new Class1()));

        Assert.assertEquals(new Class2().hashCode(), new Class2().hashCode());
        Assert.assertTrue(new Class2().equals(new Class2()));
    }
}
