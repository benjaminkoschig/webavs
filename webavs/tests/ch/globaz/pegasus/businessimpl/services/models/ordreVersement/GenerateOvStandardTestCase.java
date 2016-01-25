package ch.globaz.pegasus.businessimpl.services.models.ordreVersement;

import globaz.corvus.api.ordresversements.IREOrdresVersements;
import globaz.externe.IPRConstantesExternes;
import globaz.jade.client.util.JadeListUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.pegasus.business.constantes.IPCCreancier;
import ch.globaz.pegasus.business.constantes.IPCOrdresVersements;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordee;
import ch.globaz.pegasus.business.models.dettecomptatcompense.DetteCompenseCompteAnnexe;
import ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompense;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.business.vo.pcaccordee.PcaDecompte;
import ch.globaz.pegasus.businessimpl.services.models.lot.ordreVersement.DataForGenerateOvs;
import ch.globaz.pegasus.businessimpl.services.models.lot.ordreVersement.GenerateOrdversement;
import ch.globaz.pegasus.businessimpl.services.pca.PeriodePca;
import ch.globaz.pegasus.businessimpl.tests.factory.PegasusTestFactory;

public class GenerateOvStandardTestCase {

    @Test
    public void addIdPrestation() {

        List<SimpleOrdreVersement> ovs = new ArrayList<SimpleOrdreVersement>();
        for (int i = 0; i < 10; i++) {
            SimpleOrdreVersement ov1 = new SimpleOrdreVersement();
            ovs.add(ov1);
        }

        GenerateOrdversement.addIdPrestationInOv(ovs, "21");

        Map<String, List<SimpleOrdreVersement>> map = JadeListUtil.groupBy(ovs,
                new JadeListUtil.Key<SimpleOrdreVersement>() {
                    @Override
                    public String exec(SimpleOrdreVersement e) {
                        return e.getIdPrestation();
                    }
                });
        Assert.assertEquals(10, map.get("21").size());
    }

    @Test
    public void creancierDetermineCsOvAssuranceSociale() throws OrdreVersementException {
        String csType = GenerateOrdversement
                .determineTypeOvForCreancier(IPCCreancier.CS_TYPE_CREANCE_ASSURANCER_SOCIALE);
        Assert.assertEquals(IREOrdresVersements.CS_TYPE_ASSURANCE_SOCIALE, csType);
    }

    @Test(expected = OrdreVersementException.class)
    public void creancierDetermineCsOvError() throws OrdreVersementException {
        GenerateOrdversement.determineTypeOvForCreancier("11");
    }

    @Test
    public void creancierDetermineCsOvImpot() throws OrdreVersementException {
        String csType = GenerateOrdversement.determineTypeOvForCreancier(IPCCreancier.CS_TYPE_CREANCE_IMPOT);
        Assert.assertEquals(IREOrdresVersements.CS_TYPE_IMPOT_SOURCE, csType);
    }

    @Test
    public void creancierDetermineCsOvTiers() throws OrdreVersementException {
        String csType = GenerateOrdversement.determineTypeOvForCreancier(IPCCreancier.CS_TYPE_CREANCE_TIERS);
        Assert.assertEquals(IREOrdresVersements.CS_TYPE_TIERS, csType);
    }

    @Test
    public void creancierUnOv() throws OrdreVersementException {
        List<CreanceAccordee> creanciers = new ArrayList<CreanceAccordee>();
        CreanceAccordee creanceAccordee = new CreanceAccordee();
        creanceAccordee.getSimpleCreancier().setMontant("200");
        creanceAccordee.getSimpleCreancier().setCsTypeCreance(IPCCreancier.CS_TYPE_CREANCE_TIERS);
        creanceAccordee.getSimpleCreancier().setReferencePaiement("Test ref");
        creanceAccordee.getSimpleCreancier().setIdTiers("33");
        creanceAccordee.getSimpleCreancier().setIdAffilieAdressePaiment("20");
        creanceAccordee.getSimpleCreancier().setIdDomaineApplicatif("1");
        creanceAccordee.getSimpleCreanceAccordee().setMontant("100");
        creanciers.add(creanceAccordee);
        List<SimpleOrdreVersement> ovs = GenerateOrdversement.generateOvsCreancier(creanciers);
        SimpleOrdreVersement ov = ovs.get(0);
        Assert.assertEquals("Nb ov", 1, ovs.size());
        Assert.assertEquals(creanceAccordee.getSimpleCreancier().getIdTiersAdressePaiement(),
                ov.getIdTiersAdressePaiement());
        Assert.assertEquals(creanceAccordee.getSimpleCreancier().getIdTiers(), ov.getIdTiers());
        Assert.assertEquals(creanceAccordee.getSimpleCreancier().getIdDomaineApplicatif(), ov.getIdDomaineApplication());
        Assert.assertEquals(creanceAccordee.getSimpleCreanceAccordee().getMontant(), ov.getMontant());
        Assert.assertEquals(IREOrdresVersements.CS_TYPE_TIERS, ov.getCsType());
    }

    @Test
    public void creancierZeroOv() throws OrdreVersementException {
        List<CreanceAccordee> creanciers = new ArrayList<CreanceAccordee>();
        List<SimpleOrdreVersement> ovs = GenerateOrdversement.generateOvsCreancier(creanciers);
        Assert.assertEquals("Nb ov", 0, ovs.size());
    }

    private SimpleJoursAppoint createJourAppoint(String idPCAccordee) {
        SimpleJoursAppoint joursAppoint = new SimpleJoursAppoint();
        joursAppoint.setIdPCAccordee(idPCAccordee);
        joursAppoint.setMontantJournalier("180");
        joursAppoint.setNbrJoursAppoint("20");
        return joursAppoint;
    }

    private PcaDecompte createPcaConjointAvs(String montant) {
        return PegasusTestFactory.createPcaConjointAvs(null, null, montant);
    }

    private PcaDecompte createPcaRequerantAvs(String montant) {
        return PegasusTestFactory.createPcaRequerantAvs(null, null, montant);
    }

    public void csDomaineAi() throws OrdreVersementException {
        Assert.assertEquals("Test domaine avs", IPCOrdresVersements.CS_DOMAINE_AVS,
                GenerateOrdversement.dettermineCsDomaineOv(IPCPCAccordee.CS_TYPE_PC_VIELLESSE));
    }

    public void csDomaineAvs() throws OrdreVersementException {
        Assert.assertEquals("Test domaine avs", IPCOrdresVersements.CS_DOMAINE_AVS,
                GenerateOrdversement.dettermineCsDomaineOv(IPCPCAccordee.CS_TYPE_PC_VIELLESSE));
        Assert.assertEquals("Test domaine avs", IPCOrdresVersements.CS_DOMAINE_AVS,
                GenerateOrdversement.dettermineCsDomaineOv(IPCPCAccordee.CS_TYPE_PC_SURVIVANT));
    }

    @Test(expected = OrdreVersementException.class)
    public void csDomaineErreur() throws OrdreVersementException {
        Assert.assertEquals("Test domaine avs", IPCOrdresVersements.CS_DOMAINE_AVS,
                GenerateOrdversement.dettermineCsDomaineOv("2"));
    }

    @Test
    public void detteMontantAzero() throws OrdreVersementException {
        List<DetteCompenseCompteAnnexe> dettes = new ArrayList<DetteCompenseCompteAnnexe>();
        SimpleDetteComptatCompense simpleDetteComptatCompense = new SimpleDetteComptatCompense();
        simpleDetteComptatCompense.setIdSectionDetteEnCompta("2");
        simpleDetteComptatCompense.setMontant("0");
        simpleDetteComptatCompense.setMontantModifie("0");
        DetteCompenseCompteAnnexe dette = new DetteCompenseCompteAnnexe();
        dette.setSimpleDetteComptatCompense(simpleDetteComptatCompense);
        dettes.add(dette);
        List<SimpleOrdreVersement> ovs = GenerateOrdversement.generateOvsDetteCompta(dettes);
        Assert.assertEquals("Nb ov", 0, ovs.size());
    }

    @Test
    public void detteMontantModifie() throws OrdreVersementException {
        List<DetteCompenseCompteAnnexe> dettes = new ArrayList<DetteCompenseCompteAnnexe>();
        SimpleDetteComptatCompense simeSimpleDetteComptatCompense = new SimpleDetteComptatCompense();
        simeSimpleDetteComptatCompense.setIdSectionDetteEnCompta("2");
        simeSimpleDetteComptatCompense.setMontant("200");
        simeSimpleDetteComptatCompense.setMontantModifie("100");
        DetteCompenseCompteAnnexe dette = new DetteCompenseCompteAnnexe();
        dette.setSimpleDetteComptatCompense(simeSimpleDetteComptatCompense);
        dettes.add(dette);
        List<SimpleOrdreVersement> ovs = GenerateOrdversement.generateOvsDetteCompta(dettes);
        SimpleOrdreVersement ov = ovs.get(0);
        Assert.assertEquals("idSectionDetteEnCompta",
                dette.getSimpleDetteComptatCompense().getIdSectionDetteEnCompta(), ov.getIdSectionDetteEnCompta());
        Assert.assertEquals("montant", dette.getSimpleDetteComptatCompense().getMontant(), ov.getMontant());
        Assert.assertEquals("montant modifie", dette.getSimpleDetteComptatCompense().getMontantModifie(),
                ov.getMontantDetteModifier());
        Assert.assertEquals("Type dette", IREOrdresVersements.CS_TYPE_DETTE, ov.getCsType());
        Assert.assertTrue(ov.getIsCompense());
        Assert.assertEquals("Nb ov", 1, ovs.size());
    }

    @Test
    public void detteUnOv() throws OrdreVersementException {
        List<DetteCompenseCompteAnnexe> dettes = new ArrayList<DetteCompenseCompteAnnexe>();
        SimpleDetteComptatCompense simpleDetteComptatCompense = new SimpleDetteComptatCompense();
        simpleDetteComptatCompense.setIdSectionDetteEnCompta("2");
        simpleDetteComptatCompense.setMontant("200");
        DetteCompenseCompteAnnexe dette = new DetteCompenseCompteAnnexe();
        dette.setSimpleDetteComptatCompense(simpleDetteComptatCompense);
        dettes.add(dette);
        List<SimpleOrdreVersement> ovs = GenerateOrdversement.generateOvsDetteCompta(dettes);
        SimpleOrdreVersement ov = ovs.get(0);
        Assert.assertEquals("idSectionDetteEnCompta",
                dette.getSimpleDetteComptatCompense().getIdSectionDetteEnCompta(), ov.getIdSectionDetteEnCompta());
        Assert.assertEquals("montant", dette.getSimpleDetteComptatCompense().getMontant(), ov.getMontant());
        Assert.assertEquals("montant modifie", dette.getSimpleDetteComptatCompense().getMontantModifie(),
                ov.getMontantDetteModifier());
        Assert.assertEquals("Type dette", IREOrdresVersements.CS_TYPE_DETTE, ov.getCsType());
        Assert.assertTrue(ov.getIsCompense());
        Assert.assertEquals("Nb ov", 1, ovs.size());
    }

    @Test
    public void detteZeroOv() throws OrdreVersementException {
        List<DetteCompenseCompteAnnexe> dettes = new ArrayList<DetteCompenseCompteAnnexe>();
        List<SimpleOrdreVersement> ovs = GenerateOrdversement.generateOvsDetteCompta(dettes);
        Assert.assertEquals("Nb ov", 0, ovs.size());
    }

    @Test
    public void generateOvWithAllocationNoel() throws PegasusException {
        List<PeriodePca> periodesPca = new ArrayList<PeriodePca>();
        PeriodePca periodePca = new PeriodePca();

        periodePca.setNbMont(5);
        periodePca.setPcaRequerantNew(createPcaRequerantAvs("300"));
        periodePca.setPcaConjointNew(createPcaConjointAvs("1500"));
        periodePca.setPcaRequeranReplaced(createPcaRequerantAvs("250"));
        periodePca.setPcaConjointReplaced(createPcaConjointAvs("1200"));
        periodesPca.add(periodePca);

        List<CreanceAccordee> creanciers = new ArrayList<CreanceAccordee>();
        CreanceAccordee creanceAccordee = new CreanceAccordee();
        creanceAccordee.getSimpleCreancier().setMontant("200");
        creanceAccordee.getSimpleCreancier().setCsTypeCreance(IPCCreancier.CS_TYPE_CREANCE_TIERS);
        creanceAccordee.getSimpleCreancier().setReferencePaiement("Test ref");
        creanceAccordee.getSimpleCreancier().setIdTiers("33");
        creanceAccordee.getSimpleCreancier().setIdAffilieAdressePaiment("20");
        creanceAccordee.getSimpleCreancier().setIdDomaineApplicatif("1");
        creanceAccordee.getSimpleCreanceAccordee().setMontant("100");
        creanciers.add(creanceAccordee);

        List<DetteCompenseCompteAnnexe> dettes = new ArrayList<DetteCompenseCompteAnnexe>();
        SimpleDetteComptatCompense simDetteComptatCompense = new SimpleDetteComptatCompense();
        simDetteComptatCompense.setIdSectionDetteEnCompta("2");
        simDetteComptatCompense.setMontant("200");
        simDetteComptatCompense.setMontantModifie("100");
        DetteCompenseCompteAnnexe dette = new DetteCompenseCompteAnnexe();
        dette.setSimpleDetteComptatCompense(simDetteComptatCompense);
        dettes.add(dette);

        List<SimpleAllocationNoel> allocationsNoel = new ArrayList<SimpleAllocationNoel>();
        SimpleAllocationNoel allocationNoel = new SimpleAllocationNoel();
        allocationNoel.setMontantAllocation("100");
        allocationNoel.setIdPCAccordee(periodePca.getPcaRequerantNew().getIdPCAccordee());
        allocationsNoel.add(allocationNoel);

        DataForGenerateOvs data = new DataForGenerateOvs();
        data.setPeriodesPca(periodesPca);
        data.setAllocationsNoel(allocationsNoel);
        data.setCreanciers(creanciers);
        data.setDettes(dettes);
        data.setUseAllocationNoel(true);
        data.setUseJourAppoints(false);
        data.setJoursAppointNew(null);
        data.setJoursAppointReplaced(null);

        List<SimpleOrdreVersement> ovs = GenerateOrdversement.generateOvs(data);
        Assert.assertEquals("Nb ov", 7, ovs.size());
    }

    @Test
    public void generateOvWithJourAppoint() throws PegasusException {
        List<PeriodePca> periodesPca = new ArrayList<PeriodePca>();
        PeriodePca periodePca = new PeriodePca();

        periodePca.setNbMont(5);
        periodePca.setPcaRequerantNew(PegasusTestFactory.createPcaRequerantAvs("03.2013", null, "1500", "10"));
        // periodePca.setPcaConjointNew(PegasusTestFactory.createPcaConjointAvs("03.2013", null,"1200", "11"));
        periodePca.setPcaRequeranReplaced(PegasusTestFactory.createPcaRequerantAvs("01.2013", null, "300", "1"));
        // periodePca.setPcaConjointReplaced(PegasusTestFactory.createPcaConjointAvs("01.2013", null,"300", "2"));
        periodesPca.add(periodePca);

        List<SimpleJoursAppoint> joursAppointNew = new ArrayList<SimpleJoursAppoint>();
        joursAppointNew.add(createJourAppoint("10"));

        DataForGenerateOvs data = new DataForGenerateOvs();
        data.setPeriodesPca(periodesPca);
        data.setAllocationsNoel(new ArrayList<SimpleAllocationNoel>());
        data.setCreanciers(new ArrayList<CreanceAccordee>());
        data.setDettes(new ArrayList<DetteCompenseCompteAnnexe>());
        data.setUseAllocationNoel(false);
        data.setUseJourAppoints(true);
        data.setJoursAppointNew(joursAppointNew);
        data.setJoursAppointReplaced(null);

        List<SimpleOrdreVersement> ovs = GenerateOrdversement.generateOvs(data);
        Assert.assertEquals("Nb ov", 3, ovs.size());
    }

    @Test
    public void generateOvWithOutAllocationNoel() throws PegasusException {
        List<PeriodePca> periodesPca = new ArrayList<PeriodePca>();
        PeriodePca periodePca = new PeriodePca();

        periodePca.setNbMont(5);
        periodePca.setPcaRequerantNew(createPcaRequerantAvs("300"));
        periodePca.setPcaConjointNew(createPcaConjointAvs("1500"));
        periodePca.setPcaRequeranReplaced(createPcaRequerantAvs("250"));
        periodePca.setPcaConjointReplaced(createPcaConjointAvs("1200"));
        periodesPca.add(periodePca);

        List<CreanceAccordee> creanciers = new ArrayList<CreanceAccordee>();
        CreanceAccordee creanceAccordee = new CreanceAccordee();
        creanceAccordee.getSimpleCreancier().setMontant("200");
        creanceAccordee.getSimpleCreancier().setCsTypeCreance(IPCCreancier.CS_TYPE_CREANCE_TIERS);
        creanceAccordee.getSimpleCreancier().setReferencePaiement("Test ref");
        creanceAccordee.getSimpleCreancier().setIdTiers("33");
        creanceAccordee.getSimpleCreancier().setIdAffilieAdressePaiment("20");
        creanceAccordee.getSimpleCreancier().setIdDomaineApplicatif("1");
        creanceAccordee.getSimpleCreanceAccordee().setMontant("100");
        creanciers.add(creanceAccordee);

        List<DetteCompenseCompteAnnexe> dettes = new ArrayList<DetteCompenseCompteAnnexe>();
        SimpleDetteComptatCompense simpleDette = new SimpleDetteComptatCompense();
        simpleDette.setIdSectionDetteEnCompta("2");
        simpleDette.setMontant("200");
        simpleDette.setMontantModifie("100");
        DetteCompenseCompteAnnexe dette = new DetteCompenseCompteAnnexe();
        dette.setSimpleDetteComptatCompense(simpleDette);
        dettes.add(dette);

        List<SimpleAllocationNoel> allocationsNoel = new ArrayList<SimpleAllocationNoel>();
        SimpleAllocationNoel allocationNoel = new SimpleAllocationNoel();
        allocationNoel.setMontantAllocation("100");
        allocationNoel.setIdPCAccordee(periodePca.getPcaRequerantNew().getIdPCAccordee());
        allocationsNoel.add(allocationNoel);

        DataForGenerateOvs data = new DataForGenerateOvs();
        data.setPeriodesPca(periodesPca);
        data.setAllocationsNoel(allocationsNoel);
        data.setCreanciers(creanciers);
        data.setDettes(dettes);
        data.setUseAllocationNoel(false);
        data.setUseJourAppoints(false);
        data.setJoursAppointNew(null);
        data.setJoursAppointReplaced(null);

        List<SimpleOrdreVersement> ovs = GenerateOrdversement.generateOvs(data);
        Assert.assertEquals("Nb ov", 6, ovs.size());
    }

    private Map<String, List<SimpleOrdreVersement>> groupByPca(List<SimpleOrdreVersement> ovs) {
        return JadeListUtil.groupBy(ovs, new JadeListUtil.Key<SimpleOrdreVersement>() {
            @Override
            public String exec(SimpleOrdreVersement e) {
                return e.getIdPca();
            }
        });
    }

    private Map<String, List<SimpleOrdreVersement>> groupByTypeOv(List<SimpleOrdreVersement> ovs) {
        return JadeListUtil.groupBy(ovs, new JadeListUtil.Key<SimpleOrdreVersement>() {
            @Override
            public String exec(SimpleOrdreVersement e) {
                return e.getCsType();
            }
        });
    }

    @Test
    public void testCasInitialAvecPlusieursPeriode() throws PegasusException {
        List<PeriodePca> periodesPca = new ArrayList<PeriodePca>();
        PeriodePca periodePca = new PeriodePca();
        periodePca.setNbMont(5);
        periodePca.setPcaRequerantNew(createPcaRequerantAvs("300"));
        periodesPca.add(periodePca);
        PeriodePca periodePca2 = new PeriodePca();
        periodePca2.setNbMont(2);
        periodePca2.setPcaRequerantNew(createPcaRequerantAvs("100"));
        periodesPca.add(periodePca2);

        PeriodePca periodePca3 = new PeriodePca();
        periodePca3.setNbMont(4);
        periodePca3.setPcaRequerantNew(createPcaRequerantAvs("501"));
        periodesPca.add(periodePca3);

        List<SimpleOrdreVersement> ovs = GenerateOrdversement.generateOvsStandard(periodesPca);
        Assert.assertEquals("Nb ov", 3, ovs.size());
        Assert.assertEquals("No groupe période ov", "1", ovs.get(0).getNoGroupePeriode());
        Assert.assertEquals("Montant ov", "1500", ovs.get(0).getMontant());
        Assert.assertEquals("Type ov", IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, ovs.get(0).getCsType());
        Assert.assertEquals("No groupe période ov", "2", ovs.get(1).getNoGroupePeriode());
        Assert.assertEquals("Montant ov", "200", ovs.get(1).getMontant());
        Assert.assertEquals("Type ov", IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, ovs.get(1).getCsType());
        Assert.assertEquals("No groupe période ov", "3", ovs.get(2).getNoGroupePeriode());
        Assert.assertEquals("Montant ov", "2004", ovs.get(2).getMontant());
        Assert.assertEquals("Type ov", IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, ovs.get(2).getCsType());
    }

    @Test
    public void testTypeOvs() throws PegasusException {
        List<PeriodePca> periodesPca = new ArrayList<PeriodePca>();
        PeriodePca periodePca = new PeriodePca();

        periodePca.setNbMont(5);
        periodePca.setPcaRequerantNew(createPcaRequerantAvs("300"));
        periodePca.setPcaConjointNew(createPcaConjointAvs("1500"));
        periodePca.setPcaRequeranReplaced(createPcaRequerantAvs("250"));
        periodePca.setPcaConjointReplaced(createPcaConjointAvs("1200"));
        periodesPca.add(periodePca);

        List<SimpleOrdreVersement> ovs = GenerateOrdversement.generateOvsStandard(periodesPca);

        Map<String, List<SimpleOrdreVersement>> mapOv = groupByTypeOv(ovs);

        Map<String, List<SimpleOrdreVersement>> mapBeneficiaire = groupByPca(mapOv
                .get(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL));

        Map<String, List<SimpleOrdreVersement>> mapRestitution = groupByPca(mapOv
                .get(IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION));

        Assert.assertEquals("Nb ov", 4, ovs.size());
        Assert.assertEquals("Nb ov beneficaire", 2, mapOv.get(IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL)
                .size());
        Assert.assertEquals("Nb ov restitution", 2, mapOv.get(IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION)
                .size());
        Assert.assertEquals("Type ov requerant(beneficiaire)", IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL,
                mapBeneficiaire.get(periodePca.getPcaRequerantNew().getIdPCAccordee()).get(0).getCsType());
        Assert.assertEquals("Type ov requerant(restitution)", IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION,
                mapRestitution.get(periodePca.getPcaRequeranReplaced().getIdPCAccordee()).get(0).getCsType());
        Assert.assertEquals("Type ov conjoint(beneficiaire)", IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL,
                mapBeneficiaire.get(periodePca.getPcaConjointNew().getIdPCAccordee()).get(0).getCsType());
        Assert.assertEquals("Type ov conjoint(restitution)", IREOrdresVersements.CS_TYPE_DETTE_RENTE_RESTITUTION,
                mapRestitution.get(periodePca.getPcaConjointReplaced().getIdPCAccordee()).get(0).getCsType());

    }

    @Test
    public void testValueDom2RInOv() throws PegasusException {
        List<PeriodePca> periodesPca = new ArrayList<PeriodePca>();
        PeriodePca periodePca = new PeriodePca();
        periodePca.setNbMont(2);
        periodePca.setPcaRequerantNew(PegasusTestFactory.createPcaDom2RAvs("01.2013", null, "100"));
        periodesPca.add(periodePca);

        List<SimpleOrdreVersement> ovs = GenerateOrdversement.generateOvsStandard(periodesPca);
        Assert.assertEquals("Nb ov", 1, ovs.size());
        Assert.assertEquals("Montant ov", "200", ovs.get(0).getMontant());
        Assert.assertEquals("Domaine comptat", IPCOrdresVersements.CS_DOMAINE_AVS, ovs.get(0).getCsTypeDomaine());
        Assert.assertEquals("Type ov", IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, ovs.get(0).getCsType());
        Assert.assertEquals("Type domaineApplication", IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, ovs
                .get(0).getIdDomaineApplication());
        Assert.assertEquals("Test idPca", periodePca.getPcaRequerantNew().getIdPCAccordee(), ovs.get(0).getIdPca());
        Assert.assertEquals("Test idTiersAdressePaiement", periodePca.getPcaRequerantNew().getIdTiersAdressePaiement(),
                ovs.get(0).getIdTiersAdressePaiement());
        Assert.assertEquals("Test idTiersAdressePaiementConjoint", periodePca.getPcaRequerantNew()
                .getIdTiersAdressePaiementConjoint(), ovs.get(0).getIdTiersAdressePaiement());
        Assert.assertEquals("Test idTiersBeneficiaire", periodePca.getPcaRequerantNew().getIdTiersBeneficiaire(), ovs
                .get(0).getIdTiers());
    }

    @Test
    public void testValueInOv() throws PegasusException {
        List<PeriodePca> periodesPca = new ArrayList<PeriodePca>();
        PeriodePca periodePca = new PeriodePca();
        periodePca.setNbMont(5);
        periodePca.setPcaRequerantNew(PegasusTestFactory.createPcaRequerantAvs("01.2013", null, "300"));
        periodesPca.add(periodePca);

        List<SimpleOrdreVersement> ovs = GenerateOrdversement.generateOvsStandard(periodesPca);
        Assert.assertEquals("Nb ov", 1, ovs.size());
        Assert.assertEquals("Montant ov", "1500", ovs.get(0).getMontant());
        Assert.assertEquals("Domaine comptat", IPCOrdresVersements.CS_DOMAINE_AVS, ovs.get(0).getCsTypeDomaine());
        Assert.assertEquals("Type ov", IREOrdresVersements.CS_TYPE_BENEFICIAIRE_PRINCIPAL, ovs.get(0).getCsType());
        Assert.assertEquals("Type domaineApplication", IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, ovs
                .get(0).getIdDomaineApplication());
        Assert.assertEquals("Test idPca", periodePca.getPcaRequerantNew().getIdPCAccordee(), ovs.get(0).getIdPca());
        Assert.assertEquals("Test idTiersAdressePaiement", periodePca.getPcaRequerantNew().getIdTiersAdressePaiement(),
                ovs.get(0).getIdTiersAdressePaiement());
        Assert.assertNull("L'id du conjoint doit être vide", periodePca.getPcaRequerantNew()
                .getIdTiersAdressePaiementConjoint());
        Assert.assertEquals("Test idTiersBeneficiaire", periodePca.getPcaRequerantNew().getIdTiersBeneficiaire(), ovs
                .get(0).getIdTiers());
    }
}
