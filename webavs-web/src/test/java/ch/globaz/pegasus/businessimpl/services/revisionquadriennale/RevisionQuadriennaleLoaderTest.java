package ch.globaz.pegasus.businessimpl.services.revisionquadriennale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import ch.globaz.pegasus.business.domaine.pca.Pca;
import ch.globaz.pegasus.business.domaine.pca.PcaRequerantConjoint;
import ch.globaz.pegasus.business.domaine.pca.PcaSitutation;
import ch.globaz.pyxis.domaine.PersonneAVS;

public class RevisionQuadriennaleLoaderTest {

    RevisionQuadriennaleLoader loader = new RevisionQuadriennaleLoader();

    @Test
    public void testResolveIdTierToUsedForAdresseDomicile() throws Exception {
        PcaRequerantConjoint pcas = buildPca(PcaSitutation.DOMICILE);
        assertEquals("1", loader.resolveIdTierToUsedForAdresse(pcas));
    }

    @Test
    public void testResolveIdTierToUsedForAdresseHome() throws Exception {
        PcaRequerantConjoint pcas = buildPca(PcaSitutation.HOME);
        assertEquals("1", loader.resolveIdTierToUsedForAdresse(pcas));
    }

    @Test
    public void testResolveIdTierToUsedForAdresseDom2R() throws Exception {
        PcaRequerantConjoint pcas = buildPca(PcaSitutation.DOM2R);
        assertEquals("1", loader.resolveIdTierToUsedForAdresse(pcas));
    }

    @Test
    public void testResolveIdTierToUsedForAdresseCoupleSepareConjoint() throws Exception {
        PcaRequerantConjoint pcas = buildPca(PcaSitutation.COUPLE_SEPARE_CONJOINT_HOME);
        assertEquals("1", loader.resolveIdTierToUsedForAdresse(pcas));
    }

    @Test
    public void testResolveIdTierToUsedForAdresseCoupleSepareRequerant() throws Exception {
        PcaRequerantConjoint pcas = buildPca(PcaSitutation.COUPLE_SEPARE_REQUERANT_HOME);
        assertEquals("2", loader.resolveIdTierToUsedForAdresse(pcas));
    }

    @Test
    public void testResolveIdTierToUsedForAdresseCoupleSepareLes2() throws Exception {
        PcaRequerantConjoint pcas = buildPca(PcaSitutation.COUPLE_SEPARE_DEUX_EN_HOME);
        assertEquals("1", loader.resolveIdTierToUsedForAdresse(pcas));
    }

    private PcaRequerantConjoint buildPca(PcaSitutation pcaCas) {
        PcaRequerantConjoint pcas = new PcaRequerantConjoint();
        pcas.setRequerant(new Pca());
        pcas.setConjoint(new Pca());
        pcas.getRequerant().setBeneficiaire(new PersonneAVS());
        pcas.getRequerant().getBeneficiaire().setId(new Long(1));
        pcas.getConjoint().setBeneficiaire(new PersonneAVS());
        pcas.getConjoint().getBeneficiaire().setId(new Long(2));
        pcas = spy(pcas);
        doReturn(pcaCas).when(pcas).resolveCasPca();
        return pcas;
    }
}
