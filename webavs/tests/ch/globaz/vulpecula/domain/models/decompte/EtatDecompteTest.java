package ch.globaz.vulpecula.domain.models.decompte;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class EtatDecompteTest {
    @Test
    public void given0WhenFromValueShouldReturnBusinessException() {
        try {
            EtatDecompte.fromValue("0");
            fail("La valeur 0 est invalide et aucune exception n'a été levée");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void whenGetListShouldReturnAtLeastOneItem() {
        List<String> types = EtatDecompte.getList();

        Assert.assertTrue(types.size() > 0);
    }
}
