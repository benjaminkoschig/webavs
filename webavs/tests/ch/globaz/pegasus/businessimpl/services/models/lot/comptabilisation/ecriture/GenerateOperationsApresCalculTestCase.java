package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.jade.client.util.JadeListUtil;
import globaz.jade.exception.JadeApplicationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.pegasus.business.models.lot.OrdreVersementForList;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.ComptabilisationTreatTestCase;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.CompteAnnexeFactory;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.OrdreVersementFactory;

public class GenerateOperationsApresCalculTestCase {

    @BeforeClass
    public static void before() {
        CompteAnnexeResolver.addComptesAnnexes(CompteAnnexeFactory.generateComptesAnnexes());
    }

    private Operations generateOperations(List<OrdreVersementForList> ovs) throws JadeApplicationException {
        List<SectionSimpleModel> sections = new ArrayList<SectionSimpleModel>();
        sections.add(new SectionSimpleModel());
        sections.get(0).setIdSection("2020");
        sections.get(0).setIdCompteAnnexe("555");
        GenerateOperations generateOperations = newGenerateOperations();
        Operations operations = generateOperations.generateAllOperations(ovs, sections, "01.01.2012");
        return operations;
    }

    private GenerateEcrituresResitutionBeneficiareForDecisionAc newGenerateEcritures() throws JadeApplicationException {
        GenerateEcrituresResitutionBeneficiareForDecisionAc generate = new GenerateEcrituresResitutionBeneficiareForDecisionAc();
        GenerateEcrituresResitutionBeneficiareForDecisionAc spy = Mockito.spy(generate);
        Mockito.doReturn(ComptabilisationTreatTestCase.ID_REF_RUBRIQUE).when(spy)
                .resolveIdRefRubrique(Matchers.any(OrdreVersement.class));
        return spy;
    }

    private GenerateOperations newGenerateOperations() throws JadeApplicationException {
        GenerateOperationsApresCalcul generate = new GenerateOperationsApresCalcul();
        GenerateOperationsApresCalcul spy = Mockito.spy(generate);
        Mockito.doReturn(newGenerateEcritures()).when(spy).newEcritureBasic();
        return spy;
    }

    @Test
    public void testControleAmount() throws JadeApplicationException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("1", "2000"));
        ovs.add(OrdreVersementFactory.generateOvListRestitution("1", "1500"));
        for (OrdreVersementForList ov : ovs) {
            ov.setMontantPresation("500");
        }
        Operations operations = generateOperations(ovs);
        Assert.assertEquals(new BigDecimal("500"), operations.getControlAmount());
    }

    @Test
    public void testControleAmountWithDetteAndCreancier() throws JadeApplicationException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("1", "2000"));
        ovs.add(OrdreVersementFactory.generateOvListRestitution("1", "1500"));
        ovs.add(OrdreVersementFactory.generateOvListCreancier("150", "1245", "1"));
        ovs.add(OrdreVersementFactory.generateOvListDette("100", "2020", "1"));
        for (OrdreVersementForList ov : ovs) {
            ov.setMontantPresation("500");
        }
        Operations operations = generateOperations(ovs);
        Assert.assertEquals(new BigDecimal("500"), operations.getControlAmount());
    }

    @Test
    public void testGenerateAllOperationBasiqueCasSimple() throws JadeApplicationException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("1", "2000"));
        ovs.add(OrdreVersementFactory.generateOvListRestitution("1", "1500"));
        Operations operations = generateOperations(ovs);
        List<Ecriture> ecritures = operations.getEcritures();
        List<OrdreVersementCompta> ovsCompta = operations.getOrdresVersements();
        Map<String, List<Ecriture>> map = JadeListUtil.groupBy(ecritures, new JadeListUtil.Key<Ecriture>() {
            @Override
            public String exec(Ecriture e) {
                return e.getTypeEcriture().toString();
            }
        });
        Assert.assertEquals(1, ovsCompta.size());
        Assert.assertEquals(new BigDecimal(500), ovsCompta.get(0).getMontant());
        Assert.assertEquals(4, ecritures.size());
        Assert.assertEquals(2, map.get(TypeEcriture.STANDARD.toString()).size());
        Assert.assertEquals(2, map.get(TypeEcriture.COMPENSATION_INTER_PERIODE.toString()).size());
    }

    @Test
    public void testGenerateAllOperationBasiqueCasSimpleWithConjoint() throws JadeApplicationException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("1", "2000"));
        ovs.add(OrdreVersementFactory.generateOvListRestitution("1", "1500"));
        ovs.add(OrdreVersementFactory.generateOvListConjointBeneficiaire("1", "500"));
        ovs.add(OrdreVersementFactory.generateOvListConjointRestitution("1", "450"));
        Operations operations = generateOperations(ovs);
        List<Ecriture> ecritures = operations.getEcritures();
        List<OrdreVersementCompta> ovsCompta = operations.getOrdresVersements();
        Map<String, List<Ecriture>> map = JadeListUtil.groupBy(ecritures, new JadeListUtil.Key<Ecriture>() {
            @Override
            public String exec(Ecriture e) {
                return e.getTypeEcriture().toString();
            }
        });
        Assert.assertEquals(2, ovsCompta.size());
        Assert.assertEquals(new BigDecimal(500), ovsCompta.get(0).getMontant());
        Assert.assertEquals("10", ovsCompta.get(0).getCompteAnnexe().getIdCompteAnnexe());
        Assert.assertEquals(new BigDecimal(50), ovsCompta.get(1).getMontant());
        Assert.assertEquals("20", ovsCompta.get(1).getCompteAnnexe().getIdCompteAnnexe());

        Assert.assertEquals(8, ecritures.size());
        Assert.assertEquals(4, map.get(TypeEcriture.STANDARD.toString()).size());
        Assert.assertEquals(4, map.get(TypeEcriture.COMPENSATION_INTER_PERIODE.toString()).size());
    }

    @Test
    public void testGenerateAllOperationWithDetteAndCreancier() throws JadeApplicationException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("1", "2000"));
        ovs.add(OrdreVersementFactory.generateOvListRestitution("1", "1500"));
        ovs.add(OrdreVersementFactory.generateOvListCreancier("150", "1245", "1"));
        ovs.add(OrdreVersementFactory.generateOvListDette("100", "2020", "1"));
        Operations operations = generateOperations(ovs);
        List<Ecriture> ecritures = operations.getEcritures();
        List<OrdreVersementCompta> ovsCompta = operations.getOrdresVersements();
        Map<String, List<Ecriture>> map = JadeListUtil.groupBy(ecritures, new JadeListUtil.Key<Ecriture>() {
            @Override
            public String exec(Ecriture e) {
                return e.getTypeEcriture().toString();
            }
        });
        Assert.assertEquals(2, ovsCompta.size());
        Assert.assertEquals(6, ecritures.size());
        Assert.assertEquals(new BigDecimal(150), ovsCompta.get(0).getMontant());
        Assert.assertEquals(new BigDecimal(250), ovsCompta.get(1).getMontant());

        Assert.assertEquals(2, map.get(TypeEcriture.STANDARD.toString()).size());
        Assert.assertEquals(2, map.get(TypeEcriture.COMPENSATION_INTER_PERIODE.toString()).size());
        Assert.assertEquals(2, map.get(TypeEcriture.DETTE.toString()).size());
    }

    @Test
    public void testGenerateOnlyWihtRestitution() throws JadeApplicationException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvListRestitution("1", "1500"));
        Operations operations = generateOperations(ovs);
        Assert.assertEquals(0, operations.getOrdresVersements().size());
        Assert.assertEquals(1, operations.getEcritures().size());
    }

    @Test
    public void testGenerateOvForDom2R() throws JadeApplicationException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("1", "105"));
        ovs.add(OrdreVersementFactory.generateOvListRestitution("1", "50"));
        for (OrdreVersementForList ov : ovs) {
            ov.getSimpleOrdreVersement().setIdTiersAdressePaiementConjoint("20");
        }
        Operations operations = generateOperations(ovs);
        List<Ecriture> ecritures = operations.getEcritures();
        List<OrdreVersementCompta> ovsCompta = operations.getOrdresVersements();
        Map<String, List<Ecriture>> map = JadeListUtil.groupBy(ecritures, new JadeListUtil.Key<Ecriture>() {
            @Override
            public String exec(Ecriture e) {
                return e.getTypeEcriture().toString();
            }
        });
        Assert.assertEquals(2, ovsCompta.size());
        Assert.assertEquals(8, ecritures.size());
        Assert.assertEquals(new BigDecimal(28), ovsCompta.get(0).getMontant());
        Assert.assertEquals(new BigDecimal(27), ovsCompta.get(1).getMontant());

        Assert.assertEquals("10", ovsCompta.get(0).getCompteAnnexe().getIdCompteAnnexe());
        Assert.assertEquals("10", ovsCompta.get(1).getCompteAnnexe().getIdCompteAnnexe());

        Assert.assertEquals(4, map.get(TypeEcriture.STANDARD.toString()).size());
        Assert.assertEquals(4, map.get(TypeEcriture.COMPENSATION_INTER_PERIODE.toString()).size());
    }

    @Test
    public void testRegroupementOv() throws JadeApplicationException {
        List<OrdreVersementForList> ovs = new ArrayList<OrdreVersementForList>();
        ovs.add(OrdreVersementFactory.generateOvListBeneficiaireDom2R("1", "550"));
        ovs.add(OrdreVersementFactory.generateOvListRestitutionDom2R("1", "330"));

        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("2", "800"));
        ovs.add(OrdreVersementFactory.generateOvListRestitutionDom2R("2", "750"));

        ovs.add(OrdreVersementFactory.generateOvListBeneficiaire("3", "150"));
        ovs.add(OrdreVersementFactory.generateOvListRestitution("3", "200"));
        //
        // SimpleLot lot = this.createLot();
        // SimplePrestation prestation = this.createPresation(lot, "220");
        // // 220
        // this.createOvBeneficiaireDom2R(prestation, "550", "1");
        // this.createOvRestitutionDom2R(prestation, "330", "1");
        // // 50
        // this.createOvBeneficiaire(prestation, "800", "2");
        // this.createOvRestitutionDom2R(prestation, "750", "2");
        // // -50
        // this.createOvBeneficiaire(prestation, "150", "3");
        // this.createOvRestitution(prestation, "200", "3");

        Operations operations = generateOperations(ovs);
        List<OrdreVersementCompta> ovsCompta = operations.getOrdresVersements();
        Assert.assertEquals(1, ovsCompta.size());
        Assert.assertEquals(new BigDecimal(220), ovsCompta.get(0).getMontant());
        Assert.assertEquals(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT, ovsCompta.get(0).getCompteAnnexe()
                .getIdCompteAnnexe());
    }

}
