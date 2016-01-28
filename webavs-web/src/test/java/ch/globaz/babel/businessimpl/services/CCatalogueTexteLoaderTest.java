package ch.globaz.babel.businessimpl.services;

import static org.junit.Assert.*;
import globaz.babel.api.ICTDocument;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import ch.globaz.jade.business.models.Langues;

public class CCatalogueTexteLoaderTest {

    @Test
    public void testLoadStringStringString() throws Exception {
        ICTDocument document = new Document();

        CCatalogueTexteLoader loader = new CCatalogueTexteLoader(document);
        Map<Langues, List<ICTDocument>> map = loader.load("domaine", "csTypeDoc", "nom");

        assertEquals(1000, map.get(Langues.Francais).size());
        assertEquals(0, map.get(Langues.Allemand).size());
        assertEquals(0, map.get(Langues.Anglais).size());
        assertEquals(0, map.get(Langues.Romanche).size());

    }
}
