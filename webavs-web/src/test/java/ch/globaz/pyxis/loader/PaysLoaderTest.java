package ch.globaz.pyxis.loader;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pyxis.business.model.PaysSearchSimpleModel;
import ch.globaz.pyxis.business.model.PaysSimpleModel;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.domaine.Pays;
import ch.globaz.pyxis.domaine.constantes.CodeIsoPays;

@RunWith(MockitoJUnitRunner.class)
public class PaysLoaderTest {

    @Mock
    private AdresseService service;

    @Test
    public void testConvertEmpty() throws Exception {
        PaysSimpleModel paysSimpleModel = new PaysSimpleModel();
        paysSimpleModel.setId("1");
        paysSimpleModel.setCodeIso("CH");
        paysSimpleModel.setLibelleAl("Schweiz");
        paysSimpleModel.setLibelleFr("Suisse");
        paysSimpleModel.setLibelleIt("Svizzera");

        Pays paysConverted = PaysLoader.convert(paysSimpleModel);

        assertEquals(CodeIsoPays.SUISSE, paysConverted.getCodeIso());
        assertEquals(3, paysConverted.getTraductionParLangue().size());
        assertEquals("Schweiz", paysConverted.getTraductionParLangue().get(Langues.Allemand));
        assertEquals("Suisse", paysConverted.getTraductionParLangue().get(Langues.Francais));
        assertEquals("Svizzera", paysConverted.getTraductionParLangue().get(Langues.Italien));
        assertEquals(new Long(1), paysConverted.getId());
    }

    @Test
    public void testResolveById() throws Exception {
        PaysSearchSimpleModel search = buildSearchModel();
        when(service.findPays(any(PaysSearchSimpleModel.class))).thenReturn(search);
        PaysLoader loader = new PaysLoader(service);

        Pays pays = buildPaysSuisse();
        assertEquals(pays.getId(), loader.resolveById("1").getId());
    }

    private PaysSearchSimpleModel buildSearchModel() {
        PaysSimpleModel paysSimpleModel = new PaysSimpleModel();
        paysSimpleModel.setId("1");
        paysSimpleModel.setCodeIso("CH");
        paysSimpleModel.setCodeCentrale("100");
        paysSimpleModel.setLibelleFr("Suisse");
        PaysSearchSimpleModel search = new PaysSearchSimpleModel();

        search.setSearchResults(new PaysSimpleModel[] { paysSimpleModel });
        return search;
    }

    @Test
    public void testResolveByCodeCentrale() throws Exception {
        PaysSearchSimpleModel search = buildSearchModel();
        when(service.findPays(any(PaysSearchSimpleModel.class))).thenReturn(search);
        PaysLoader loader = new PaysLoader(service);
        Pays pays = buildPaysSuisse();
        assertEquals(pays.getId(), loader.resolveByCodeCentrale("100").getId());
    }

    private Pays buildPaysSuisse() {
        Pays pays = new Pays();
        pays.setCodeIso(CodeIsoPays.SUISSE);
        pays.setId(new Long(1));
        return pays;
    }

    @Test
    public void testLoadAndGetPaysByIdAnReturnAnyOne() throws Exception {
        PaysSearchSimpleModel search = new PaysSearchSimpleModel();
        when(service.findPays(any(PaysSearchSimpleModel.class))).thenReturn(search);
        PaysLoader loader = new PaysLoader(service);
        assertEquals(new Long(-1), loader.resolveById("1").getId());
    }

}
