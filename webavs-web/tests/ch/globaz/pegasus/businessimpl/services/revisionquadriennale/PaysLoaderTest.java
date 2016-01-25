package ch.globaz.pegasus.businessimpl.services.revisionquadriennale;

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

        PaysLoader loader = new PaysLoader();
        Pays paysConverted = loader.convert(paysSimpleModel);

        assertEquals(CodeIsoPays.SUISSE, paysConverted.getCodeIso());
        assertEquals(3, paysConverted.getTraductionParLangue().size());
        assertEquals("Schweiz", paysConverted.getTraductionParLangue().get(Langues.Allemand));
        assertEquals("Suisse", paysConverted.getTraductionParLangue().get(Langues.Francais));
        assertEquals("Svizzera", paysConverted.getTraductionParLangue().get(Langues.Italien));
        assertEquals(new Long(1), paysConverted.getId());
    }

    @Test
    public void testLoadAndGetPaysByIdAnReturnOne() throws Exception {

        PaysSimpleModel paysSimpleModel = new PaysSimpleModel();
        paysSimpleModel.setId("1");
        paysSimpleModel.setCodeIso("CH");
        paysSimpleModel.setLibelleFr("Suisse");
        PaysSearchSimpleModel search = new PaysSearchSimpleModel();

        search.setSearchResults(new PaysSimpleModel[] { paysSimpleModel });

        when(service.findPays(any(PaysSearchSimpleModel.class))).thenReturn(search);
        PaysLoader loader = new PaysLoader(service);
        loader.loadAndGetPaysById("1");

        Pays pays = new Pays();
        pays.setCodeIso(CodeIsoPays.SUISSE);
        pays.setId(new Long(1));
        assertEquals(pays.getId(), loader.loadAndGetPaysById("1").getId());
    }

    @Test
    public void testLoadAndGetPaysByIdAnReturnAnyOne() throws Exception {
        PaysSearchSimpleModel search = new PaysSearchSimpleModel();

        when(service.findPays(any(PaysSearchSimpleModel.class))).thenReturn(search);
        PaysLoader loader = new PaysLoader(service);

        Pays pays = new Pays();
        pays.setCodeIso(CodeIsoPays.SUISSE);
        pays.setId(new Long(1));
        assertEquals(new Long(-1), loader.loadAndGetPaysById("1").getId());
    }
}
