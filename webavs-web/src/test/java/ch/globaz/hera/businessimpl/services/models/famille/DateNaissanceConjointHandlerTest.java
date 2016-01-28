package ch.globaz.hera.businessimpl.services.models.famille;

import static org.junit.Assert.*;
import org.junit.Test;

public class DateNaissanceConjointHandlerTest {

    @Test
    public void testDateNaissanceConjointInstanciationException() {

        try {
            new DateNaissanceConjointHandler(null, null, null, null);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                assertTrue(true);
                System.out.println(e.getMessage());
            } else {
                fail();
            }
        }

    }

}
