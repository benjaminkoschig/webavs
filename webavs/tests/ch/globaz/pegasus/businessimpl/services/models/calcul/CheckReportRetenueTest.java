package ch.globaz.pegasus.businessimpl.services.models.calcul;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.math.BigDecimal;
import junit.framework.Assert;
import org.junit.Test;
import ch.globaz.pegasus.business.models.calcul.CalculPcaReplace;
import ch.globaz.pegasus.business.models.pcaccordee.PcaRetenue;
import ch.globaz.pegasus.business.models.pcaccordee.PcaRetenueSearch;
import ch.globaz.pegasus.businessimpl.utils.RequerantConjoint;
import ch.globaz.pegasus.factory.pca.CalculPcaReplaceFactory;
import ch.globaz.perseus.business.exceptions.models.retenue.RetenueException;

public class CheckReportRetenueTest {

    @Test
    public void testIsMontantPresationSuffisant_true() throws Exception {
        Assert.assertTrue(CheckReportRetenue.isMontantPresationSuffisant(new BigDecimal(2), new BigDecimal(1)));
        Assert.assertTrue(CheckReportRetenue.isMontantPresationSuffisant(new BigDecimal(2), new BigDecimal(2)));
    }

    @Test
    public void testIsMontantPresationSuffisant_false() throws Exception {
        Assert.assertFalse(CheckReportRetenue.isMontantPresationSuffisant(new BigDecimal(1), new BigDecimal(2)));
    }

    @Test
    public void testIsReporteRetenuePossibleRequerant() throws Exception {

        CalculPcaReplace anciennePCACourante = genreateAnciennePcaRequerant();

        JadeAbstractModel[] t = new PcaRetenue[1];
        PcaRetenue retenue = new PcaRetenue();
        retenue.setIdPCAccordee("1");
        retenue.getSimpleRetenue().setMontantRetenuMensuel("1000");
        retenue.getSimpleRetenue().setIdRenteAccordee("30");
        t[0] = retenue;
        PcaRetenueSearch search = new PcaRetenueSearch();
        search.setSearchResults(t);
        BigDecimal montantNewPca = new BigDecimal(1200);
        CheckReportRetenue checkReportRetenue = new CheckReportRetenue(anciennePCACourante, montantNewPca, search,
                false);
        Assert.assertTrue(checkReportRetenue.isReporteRetenuePossible());

    }

    @Test
    public void testIsReporteRetenuePossibleDom2RTrue() throws Exception {

        CalculPcaReplace anciennePCACourante = CalculPcaReplaceFactory.generateDom2R("01.2014", null, "1", "1");
        anciennePCACourante.getSimplePrestationsAccordees().setIsRetenues(true);
        anciennePCACourante.getSimplePrestationsAccordees().setId("30");
        anciennePCACourante.getSimplePrestationsAccordeesConjoint().setId("40");

        JadeAbstractModel[] t = new PcaRetenue[2];
        PcaRetenue retenueR = new PcaRetenue();
        retenueR.getSimpleRetenue().setMontantRetenuMensuel("500");
        retenueR.getSimpleRetenue().setIdRenteAccordee("30");
        t[0] = retenueR;

        PcaRetenue retenueC = new PcaRetenue();
        retenueC.getSimpleRetenue().setMontantRetenuMensuel("500");
        retenueC.getSimpleRetenue().setIdRenteAccordee("40");
        t[1] = retenueC;
        PcaRetenueSearch search = new PcaRetenueSearch();
        search.setSearchResults(t);
        CheckReportRetenue checkReportRetenue = buildCheckReportRetenue(anciennePCACourante, search, true);
        Assert.assertTrue(checkReportRetenue.isReporteRetenuePossible());

    }

    @Test
    public void testIsReporteRetenuePossibleDom2RFalse() throws Exception {

        CalculPcaReplace anciennePCACourante = CalculPcaReplaceFactory.generateDom2R("01.2014", null, "1", "1");
        anciennePCACourante.getSimplePrestationsAccordees().setIsRetenues(true);
        anciennePCACourante.getSimplePrestationsAccordees().setId("30");
        anciennePCACourante.getSimplePrestationsAccordeesConjoint().setId("40");

        JadeAbstractModel[] t = new PcaRetenue[2];
        PcaRetenue retenueR = new PcaRetenue();
        retenueR.getSimpleRetenue().setMontantRetenuMensuel("1000");
        retenueR.getSimpleRetenue().setIdRenteAccordee("30");
        t[0] = retenueR;

        PcaRetenue retenueC = new PcaRetenue();
        retenueC.getSimpleRetenue().setMontantRetenuMensuel("1000");
        retenueC.getSimpleRetenue().setIdRenteAccordee("40");
        t[1] = retenueC;
        PcaRetenueSearch search = new PcaRetenueSearch();
        search.setSearchResults(t);

        CheckReportRetenue checkReportRetenue = buildCheckReportRetenue(anciennePCACourante, search, true);
        Assert.assertFalse(checkReportRetenue.isReporteRetenuePossible());
    }

    @Test
    public void testIsReporteRetenuePossibleDom2RToStandard() throws Exception {

        CalculPcaReplace anciennePCACourante = CalculPcaReplaceFactory.generateDom2R("01.2014", null, "1", "1");
        anciennePCACourante.getSimplePrestationsAccordees().setIsRetenues(true);
        anciennePCACourante.getSimplePrestationsAccordees().setId("30");
        anciennePCACourante.getSimplePrestationsAccordeesConjoint().setId("40");

        JadeAbstractModel[] t = new PcaRetenue[2];
        PcaRetenue retenueR = new PcaRetenue();
        retenueR.getSimpleRetenue().setMontantRetenuMensuel("1000");
        retenueR.getSimpleRetenue().setIdRenteAccordee("30");
        t[0] = retenueR;

        PcaRetenue retenueC = new PcaRetenue();
        retenueC.getSimpleRetenue().setMontantRetenuMensuel("1000");
        retenueC.getSimpleRetenue().setIdRenteAccordee("40");
        t[1] = retenueC;
        PcaRetenueSearch search = new PcaRetenueSearch();
        search.setSearchResults(t);

        CheckReportRetenue checkReportRetenue = buildCheckReportRetenue(anciennePCACourante, search, false);
        Assert.assertFalse(checkReportRetenue.isReporteRetenuePossible());
    }

    @Test
    public void testHasRetenueRequerant() throws Exception {
        CalculPcaReplace anciennePCACourante = genreateAnciennePcaRequerant();
        anciennePCACourante.setSimplePrestationsAccordeesConjoint(null);
        CheckReportRetenue checkReportRetenue = new CheckReportRetenue(anciennePCACourante, mock(BigDecimal.class),
                mock(PcaRetenueSearch.class), true);

        Assert.assertTrue(checkReportRetenue.hasRetenue());
    }

    @Test
    public void testHasRetenueConjoint() throws Exception {
        CalculPcaReplace anciennePCACourante = genreateAnciennePcaRequerant();
        anciennePCACourante.getSimplePrestationsAccordees().setIsRetenues(false);
        anciennePCACourante.getSimplePrestationsAccordeesConjoint().setIsRetenues(true);
        CheckReportRetenue checkReportRetenue = new CheckReportRetenue(anciennePCACourante, mock(BigDecimal.class),
                mock(PcaRetenueSearch.class), true);

        Assert.assertTrue(checkReportRetenue.hasRetenue());
    }

    private CalculPcaReplace genreateAnciennePcaRequerant() {
        CalculPcaReplace anciennePCACourante = CalculPcaReplaceFactory.generateForRequerant("01.2013", null, "1");
        anciennePCACourante.getSimplePrestationsAccordees().setIsRetenues(true);
        anciennePCACourante.getSimplePrestationsAccordees().setId("30");
        return anciennePCACourante;
    }

    @Test
    public void testSumRetenueDom2R() throws Exception {
        CalculPcaReplace anciennePCACourante = CalculPcaReplaceFactory.generateDom2R("01.2014", null, "1", "1");
        anciennePCACourante.getSimplePrestationsAccordees().setIsRetenues(true);
        anciennePCACourante.getSimplePrestationsAccordees().setId("30");
        anciennePCACourante.getSimplePrestationsAccordeesConjoint().setId("40");

        JadeAbstractModel[] t = new PcaRetenue[4];
        PcaRetenue retenueR = new PcaRetenue();
        retenueR.getSimpleRetenue().setMontantRetenuMensuel("500");
        retenueR.getSimpleRetenue().setIdRenteAccordee("30");
        t[0] = retenueR;

        PcaRetenue retenueC = new PcaRetenue();
        retenueC.getSimpleRetenue().setMontantRetenuMensuel("500");
        retenueC.getSimpleRetenue().setIdRenteAccordee("40");
        t[1] = retenueC;

        PcaRetenue retenueC2 = new PcaRetenue();
        retenueC2.getSimpleRetenue().setMontantRetenuMensuel("100");
        retenueC2.getSimpleRetenue().setIdRenteAccordee("40");
        t[2] = retenueC2;

        PcaRetenue retenueC3 = new PcaRetenue();
        retenueC3.getSimpleRetenue().setMontantRetenuMensuel("200");
        retenueC3.getSimpleRetenue().setIdRenteAccordee("40");
        t[3] = retenueC3;

        PcaRetenueSearch search = new PcaRetenueSearch();
        search.setSearchResults(t);
        CheckReportRetenue checkReportRetenue = buildCheckReportRetenue(anciennePCACourante, search, true);
        RequerantConjoint<BigDecimal> sumRetenue = checkReportRetenue.sumRetenue();
        assertEquals(new BigDecimal(500), sumRetenue.getRequerant());
        assertEquals(new BigDecimal(800), sumRetenue.getConjoint());

    }

    @Test
    public void testSumRetenueRequerant() throws Exception {
        CalculPcaReplace anciennePCACourante = CalculPcaReplaceFactory.generateDom2R("01.2014", null, "1", "1");
        anciennePCACourante.getSimplePrestationsAccordees().setIsRetenues(true);
        anciennePCACourante.getSimplePrestationsAccordees().setId("30");
        anciennePCACourante.getSimplePrestationsAccordeesConjoint().setId("40");

        JadeAbstractModel[] t = new PcaRetenue[2];
        PcaRetenue retenueR = new PcaRetenue();
        retenueR.getSimpleRetenue().setMontantRetenuMensuel("500");
        retenueR.getSimpleRetenue().setIdRenteAccordee("30");
        t[0] = retenueR;

        PcaRetenue retenueR2 = new PcaRetenue();
        retenueR2.getSimpleRetenue().setMontantRetenuMensuel("200");
        retenueR2.getSimpleRetenue().setIdRenteAccordee("30");
        t[1] = retenueR2;

        PcaRetenueSearch search = new PcaRetenueSearch();
        search.setSearchResults(t);
        CheckReportRetenue checkReportRetenue = buildCheckReportRetenue(anciennePCACourante, search, false);

        RequerantConjoint<BigDecimal> sumRetenue = checkReportRetenue.sumRetenue();
        assertEquals(new BigDecimal(700), sumRetenue.getRequerant());
        assertEquals(new BigDecimal(0), sumRetenue.getConjoint());
    }

    private CheckReportRetenue buildCheckReportRetenue(CalculPcaReplace anciennePCACourante, PcaRetenueSearch search,
            boolean isForDom2R) throws RetenueException {
        BigDecimal montantNewPca = new BigDecimal(1200);
        CheckReportRetenue checkReportRetenue = new CheckReportRetenue(anciennePCACourante, montantNewPca, search,
                isForDom2R);
        return checkReportRetenue;
    }

    @Test
    public void testIsReportRetenuePossibleWihtRetenue() throws Exception {
        BigDecimal montantPca = new BigDecimal(100);
        RequerantConjoint<BigDecimal> sumRetenues = new RequerantConjoint<BigDecimal>();
        sumRetenues.setRequerant(new BigDecimal(10));
        sumRetenues.setConjoint(new BigDecimal(20));

        assertTrue(CheckReportRetenue.isReportRetenuePossibleWihtRetenue(montantPca, sumRetenues, true));
        assertTrue(CheckReportRetenue.isReportRetenuePossibleWihtRetenue(montantPca, sumRetenues, false));
        montantPca = new BigDecimal(5);
        assertFalse(CheckReportRetenue.isReportRetenuePossibleWihtRetenue(montantPca, sumRetenues, true));
        assertFalse(CheckReportRetenue.isReportRetenuePossibleWihtRetenue(montantPca, sumRetenues, false));
    }

    // @Test
    // public void test() throws Exception {
    // buildCheckReportRetenue(null, null, false);
    // }
}
