package ch.globaz.corvus.process.dnra;

import static org.fest.assertions.api.Assertions.*;
import org.junit.Test;

public class MutationsContainerTest {

    @Test
    public void testExtractNss() throws Exception {
        MutationsContainer container = new MutationsContainer();
        Mutation mutation = new Mutation();
        mutation.setNss("756.0000.0000.00");
        container.add(mutation);
        assertThat(container.extractNssActuel()).hasSize(1);
    }

}
