package ch.globaz.vulpecula.domain.models.common;

import static org.junit.Assert.*;
import org.junit.Test;

public class AgeTest {
    @Test
    public void given65AlphaNumericShouldReturn65Int() {
        Age age = new Age("65.00");
        assertTrue(age.getValue() == 65);
    }
}
