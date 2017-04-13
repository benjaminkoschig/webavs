package globaz.corvus.process;

import static org.junit.Assert.*;
import globaz.corvus.annonce.service.REAnnonces10eXmlService;
import globaz.corvus.annonce.service.REAnnonces9eXmlService;
import globaz.corvus.db.annonces.REAnnoncesAbstractLevel1A;
import globaz.framework.util.FWMemoryLog;
import java.math.BigDecimal;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.Spy;
import ch.admin.zas.rc.DJE9BeschreibungType;
import ch.admin.zas.rc.Gutschriften9Type;
import ch.admin.zas.rc.RRLeistungsberechtigtePersonAuslType;
import ch.admin.zas.rc.RRMeldung9Type;
import ch.admin.zas.rc.SkalaBerechnungType;
import ch.admin.zas.rc.ZuwachsmeldungO9Type;
import ch.globaz.common.exceptions.ValidationException;

public class REEnvoyerAnnoncesXMLProcessTest {

    private String numeroAgence = "";
    private String numeroCaisse = "";

    @Spy
    private REEnvoyerAnnoncesXMLProcess testInstance;
    @Spy
    private FWMemoryLog fwMemory;

    public REEnvoyerAnnoncesXMLProcessTest() {

    }

    @Before
    public void setUp() throws Exception {
        // enregistrement 1
        numeroCaisse = "059";
        numeroAgence = "000";

        testInstance = Mockito.spy(new REEnvoyerAnnoncesXMLProcess());

        // on retourne le numéro de la caisse défini en dur plutôt que d'aller le chercher dans la BApplication
        Mockito.doReturn(numeroCaisse).when(testInstance).getNumeroCaisseFromApplication();
        Mockito.doReturn(numeroAgence).when(testInstance).getNumeroAgenceFromApplication();

        // bypass du logger
        Mockito.doNothing()
                .when(testInstance)
                .logMessageAvecInfos(Matchers.any(REAnnoncesAbstractLevel1A.class), Matchers.anyString(),
                        Matchers.anyString(), Matchers.anyString());
        Mockito.doNothing().when(testInstance).logInMemoryLog(Matchers.anyString(), Matchers.anyString());
    }

    @Test
    public void testServiceAnnonceResolver() {
        REAnnoncesAbstractLevel1A annonceMock = Mockito.spy(new REAnnoncesAbstractLevel1A());
        try {
            Mockito.when(annonceMock.getCodeApplication()).thenReturn("41");
            assertTrue(testInstance.resolveAnnonceVersionService(annonceMock) instanceof REAnnonces9eXmlService);
            Mockito.when(annonceMock.getCodeApplication()).thenReturn("42");
            assertTrue(testInstance.resolveAnnonceVersionService(annonceMock) instanceof REAnnonces9eXmlService);
            Mockito.when(annonceMock.getCodeApplication()).thenReturn("43");
            assertTrue(testInstance.resolveAnnonceVersionService(annonceMock) instanceof REAnnonces9eXmlService);
            Mockito.when(annonceMock.getCodeApplication()).thenReturn("44");
            assertTrue(testInstance.resolveAnnonceVersionService(annonceMock) instanceof REAnnonces10eXmlService);
            Mockito.when(annonceMock.getCodeApplication()).thenReturn("45");
            assertTrue(testInstance.resolveAnnonceVersionService(annonceMock) instanceof REAnnonces10eXmlService);
            Mockito.when(annonceMock.getCodeApplication()).thenReturn("46");
            assertTrue(testInstance.resolveAnnonceVersionService(annonceMock) instanceof REAnnonces10eXmlService);
        } catch (Exception e) {
            Assert.assertEquals(null, e);
        }
        // ne pas tester le cas d'echec sans pouvoir muter le fwMemoryLog
        try {
            Mockito.when(annonceMock.getCodeApplication()).thenReturn("30");
            assertTrue(testInstance.resolveAnnonceVersionService(annonceMock) instanceof REAnnonces9eXmlService);
            fail("ce codeApplication ne doit pas être pris en charge");
        } catch (Exception e) {
            Assert.assertNotNull(e);
        }
    }

    @Test
    public void testValidateUnitMessage() throws Exception {
        // je ne sais pas dans quoi je me lance e je corrige au fur et a mesure que la validation pète pour obtenir un
        // test OK
        RRMeldung9Type rrm9 = mockOrdentlicheZuwachsMeldung();
        try {
            testInstance.validateUnitMessage(rrm9);
        } catch (ValidationException ve) {
            Assert.assertEquals(null, ve);
        }

    }

    @Test
    public void testFailValidateUnitMessage() throws Exception {
        ch.admin.zas.rc.ObjectFactory of = new ch.admin.zas.rc.ObjectFactory();
        // empty object
        RRMeldung9Type rrm9 = of.createRRMeldung9Type();
        try {
            testInstance.validateUnitMessage(rrm9);
            fail("Doit, dans ce cas, lever une exception et ne jamais passer ici");
        } catch (ValidationException ve) {
            assertNotNull(ve);
            System.err.println(ve.getFormattedMessage());
        }

    }

    private RRMeldung9Type mockOrdentlicheZuwachsMeldung() throws DatatypeConfigurationException {
        ch.admin.zas.rc.ObjectFactory of = new ch.admin.zas.rc.ObjectFactory();
        RRMeldung9Type rrm9 = of.createRRMeldung9Type();
        RRMeldung9Type.OrdentlicheRente rrm9o = of.createRRMeldung9TypeOrdentlicheRente();
        ZuwachsmeldungO9Type zmt9 = of.createZuwachsmeldungO9Type();
        // numero de caisse et agence
        zmt9.setKasseZweigstelle("059000");
        // max inclusive
        zmt9.setMeldungsnummer(999999L);
        RRLeistungsberechtigtePersonAuslType lbp = of.createRRLeistungsberechtigtePersonAuslType();
        lbp.setVersichertennummer("7560000000002");
        lbp.setFamilienAngehoerige(of.createFamilienAngehoerigeType());
        // 1 = ?
        lbp.setZivilstand(Short.parseShort("1", 10));
        // 100 = ?
        lbp.setWohnkantonStaat("100");
        zmt9.setLeistungsberechtigtePerson(lbp);
        ZuwachsmeldungO9Type.Leistungsbeschreibung lb = of.createZuwachsmeldungO9TypeLeistungsbeschreibung();
        lb.setLeistungsart("10");
        lb.setAnspruchsbeginn(DatatypeFactory.newInstance().newXMLGregorianCalendar(2017, 01, 01, 01, 01, 01, 01, 01));
        lb.setMonatsbetrag(new BigDecimal(0));
        ZuwachsmeldungO9Type.Leistungsbeschreibung.Berechnungsgrundlagen zlb = of
                .createZuwachsmeldungO9TypeLeistungsbeschreibungBerechnungsgrundlagen();
        zlb.setNiveaujahr(DatatypeFactory.newInstance().newXMLGregorianCalendar(1955, 01, 01, 01, 01, 01, 01, 01));
        SkalaBerechnungType ska = of.createSkalaBerechnungType();
        ska.setBeitragsdauerAb1973(BigDecimal.valueOf(43.00));
        ska.setBeitragsdauerVor1973(BigDecimal.valueOf(20.00));
        ska.setSkala(Short.valueOf("44", 10));
        zlb.setSkalaBerechnung(ska);

        DJE9BeschreibungType dje = of.createDJE9BeschreibungType();
        dje.setDurchschnittlichesJahreseinkommen(BigDecimal.valueOf(1234));
        dje.setBeitragsdauerDurchschnittlichesJahreseinkommen(BigDecimal.valueOf(21));
        dje.setAngerechneteEinkommen(Short.valueOf("1", 10));
        zlb.setDJEBeschreibung(dje);
        Gutschriften9Type gut = of.createGutschriften9Type();
        zlb.setGutschriften(gut);
        lb.setBerechnungsgrundlagen(zlb);
        zmt9.setLeistungsbeschreibung(lb);
        zmt9.setBerichtsmonat(DatatypeFactory.newInstance().newXMLGregorianCalendar(2017, 01, 01, 01, 01, 01, 01, 01));
        rrm9o.setZuwachsmeldung(zmt9);
        rrm9.setOrdentlicheRente(rrm9o);
        return rrm9;
    }
}
