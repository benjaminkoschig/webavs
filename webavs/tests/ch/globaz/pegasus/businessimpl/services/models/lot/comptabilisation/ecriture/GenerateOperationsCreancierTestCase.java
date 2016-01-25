package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.jade.exception.JadeApplicationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.CompteAnnexeFactory;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.OrdreVersementFactory;

public class GenerateOperationsCreancierTestCase {

    @BeforeClass
    public static void before() {
        CompteAnnexeResolver.addComptesAnnexes(CompteAnnexeFactory.generateComptesAnnexes());
    }

    private List<OrdreVersementCompta> generate(PrestationOvDecompte decompte, MontantDispo montantDispo)
            throws JadeApplicationException {
        GenerateOperationsCreancier generate = newGenerateCreancier(decompte, montantDispo);
        return generate.getOrdresVersementCompta();
    }

    private PrestationOvDecompte generateDecomtpe(List<OrdreVersement> ovs) {
        PrestationOvDecompte decompte = new PrestationOvDecompte();
        decompte.setCreanciers(ovs);
        decompte.setIdTiersRequerant("1");
        decompte.setIdTiersConjoint("2");
        decompte.setCompteAnnexeRequerant(CompteAnnexeFactory.generateCompteAnnexe("10"));
        decompte.setCompteAnnexeConjoint(CompteAnnexeFactory.generateCompteAnnexe("20"));
        return decompte;
    }

    private GenerateOperationsCreancier newGenerateCreancier(PrestationOvDecompte decompte, MontantDispo montantDispo)
            throws JadeApplicationException {
        return new GenerateOperationsCreancier(decompte, montantDispo, new GenerateOvComptaAndGroupe());
    }

    private MontantDispo newMontantDiponible(int dom2rRequerant, int dom2rConjoint, int standardRequerant,
            int statdardConjoint) {
        return new MontantDispo(new BigDecimal(dom2rRequerant), new BigDecimal(dom2rConjoint), new BigDecimal(
                standardRequerant), new BigDecimal(statdardConjoint));
    }

    @Test
    public void testOvRequerantDeuxEcritureUnOv() throws JadeApplicationException {

        List<OrdreVersement> ovs = new ArrayList<OrdreVersement>();
        OrdreVersement ov = OrdreVersementFactory.generateCreancier("150", "2542", "1");
        ovs.add(ov);

        PrestationOvDecompte decompte = generateDecomtpe(ovs);
        MontantDispo montantDispo = newMontantDiponible(100, 100, 0, 0);
        GenerateOperationsCreancier generate = newGenerateCreancier(decompte, montantDispo);

        Assert.assertEquals(0, generate.getEcritures().size());
        Assert.assertEquals(1, generate.getOrdresVersementCompta().size());
        Assert.assertEquals(new BigDecimal(150), generate.getOrdresVersementCompta().get(0).getMontant());
    }

    @Test
    public void testOvRequerantQuatresEcritureDeuxOv() throws JadeApplicationException {

        List<OrdreVersement> ovs = new ArrayList<OrdreVersement>();
        OrdreVersement ov = OrdreVersementFactory.generateCreancier("200", "2542", "1");
        ovs.add(ov);

        PrestationOvDecompte decompte = generateDecomtpe(ovs);
        MontantDispo montantDispo = newMontantDiponible(50, 50, 70, 100);
        GenerateOperationsCreancier generate = newGenerateCreancier(decompte, montantDispo);

        Assert.assertEquals(0, generate.getEcritures().size());
        Assert.assertEquals(2, generate.getOrdresVersementCompta().size());

        Assert.assertEquals(decompte.getIdCompteAnnexeRequerant(), generate.getOrdresVersementCompta().get(0)
                .getCompteAnnexe().getIdCompteAnnexe());
        Assert.assertEquals(new BigDecimal(170), generate.getOrdresVersementCompta().get(0).getMontant());

        Assert.assertEquals(decompte.getIdCompteAnnexeConjoint(), generate.getOrdresVersementCompta().get(1)
                .getCompteAnnexe().getIdCompteAnnexe());
        Assert.assertEquals(new BigDecimal(30), generate.getOrdresVersementCompta().get(1).getMontant());

    }

    @Test
    public void testOvRequerantTroisEcritureUnOv() throws JadeApplicationException {

        List<OrdreVersement> ovs = new ArrayList<OrdreVersement>();

        OrdreVersement ov = OrdreVersementFactory.generateCreancier("150", "2542", "1");
        ovs.add(ov);

        PrestationOvDecompte decompte = generateDecomtpe(ovs);
        MontantDispo montantDispo = newMontantDiponible(50, 50, 70, 0);
        GenerateOperationsCreancier generate = newGenerateCreancier(decompte, montantDispo);

        Assert.assertEquals(0, generate.getEcritures().size());
        Assert.assertEquals(1, generate.getOrdresVersementCompta().size());
        Assert.assertEquals(new BigDecimal(150), generate.getOrdresVersementCompta().get(0).getMontant());
        Assert.assertEquals(decompte.getIdCompteAnnexeRequerant(), generate.getOrdresVersementCompta().get(0)
                .getCompteAnnexe().getIdCompteAnnexe());

    }

    @Test
    public void testOvRequerantUneEcritureUnOv() throws JadeApplicationException {

        List<OrdreVersement> ovs = new ArrayList<OrdreVersement>();

        OrdreVersement ov = OrdreVersementFactory.generateCreancier("150", "2542", "1");
        ovs.add(ov);

        PrestationOvDecompte decompte = generateDecomtpe(ovs);
        MontantDispo montantDispo = newMontantDiponible(0, 0, 200, 0);
        GenerateOperationsCreancier generate = newGenerateCreancier(decompte, montantDispo);

        Assert.assertEquals(0, generate.getEcritures().size());
        Assert.assertEquals(1, generate.getOrdresVersementCompta().size());
        Assert.assertEquals(new BigDecimal(150), generate.getOrdresVersementCompta().get(0).getMontant());
        Assert.assertEquals(decompte.getIdCompteAnnexeRequerant(), generate.getOrdresVersementCompta().get(0)
                .getCompteAnnexe().getIdCompteAnnexe());
    }

    @Test
    public void testOwnerConjoint() throws JadeApplicationException {

        List<OrdreVersement> ovs = new ArrayList<OrdreVersement>();
        OrdreVersement ov = OrdreVersementFactory.generateCreancier("150", "2542", "2");
        ovs.add(ov);

        PrestationOvDecompte decompte = generateDecomtpe(ovs);

        MontantDispo montantDispo = newMontantDiponible(0, 0, 200, 250);

        List<OrdreVersementCompta> ovc = generate(decompte, montantDispo);

        Assert.assertEquals(1, ovc.size());

        Assert.assertEquals(decompte.getIdCompteAnnexeConjoint(), ovc.get(0).getCompteAnnexe().getIdCompteAnnexe());
    }

    @Test
    public void testOwnerConjointPrendsArgentAussiChezLeRequerant() throws JadeApplicationException {

        List<OrdreVersement> ovs = new ArrayList<OrdreVersement>();
        OrdreVersement ov = OrdreVersementFactory.generateCreancier("250", "2542", "2");
        ovs.add(ov);

        PrestationOvDecompte decompte = generateDecomtpe(ovs);
        MontantDispo montantDispo = newMontantDiponible(0, 0, 200, 200);
        List<OrdreVersementCompta> ovc = generate(decompte, montantDispo);

        Assert.assertEquals(2, ovc.size());
        Assert.assertEquals(new BigDecimal(200), ovc.get(0).getMontant());
        Assert.assertEquals(decompte.getIdCompteAnnexeConjoint(), ovc.get(0).getCompteAnnexe().getIdCompteAnnexe());
        Assert.assertEquals(new BigDecimal(50), ovc.get(1).getMontant());
        Assert.assertEquals(decompte.getIdCompteAnnexeRequerant(), ovc.get(1).getCompteAnnexe().getIdCompteAnnexe());
    }

    @Test
    public void testOwnerNonSpicifier() throws JadeApplicationException {

        List<OrdreVersement> ovs = new ArrayList<OrdreVersement>();
        OrdreVersement ov = OrdreVersementFactory.generateCreancier("150", "2542", null);
        ovs.add(ov);

        PrestationOvDecompte decompte = generateDecomtpe(ovs);
        MontantDispo montantDispo = newMontantDiponible(0, 0, 100, 100);
        List<OrdreVersementCompta> ovc = generate(decompte, montantDispo);

        Assert.assertEquals(2, ovc.size());
        Assert.assertEquals(new BigDecimal(75), ovc.get(0).getMontant());
        Assert.assertEquals(new BigDecimal(75), ovc.get(0).getMontant());
        Assert.assertEquals(decompte.getIdCompteAnnexeRequerant(), ovc.get(0).getCompteAnnexe().getIdCompteAnnexe());
        Assert.assertEquals(decompte.getIdCompteAnnexeConjoint(), ovc.get(1).getCompteAnnexe().getIdCompteAnnexe());

    }

    @Test
    public void testOwnerRequerant() throws JadeApplicationException {

        List<OrdreVersement> ovs = new ArrayList<OrdreVersement>();

        OrdreVersement ov = OrdreVersementFactory.generateCreancier("150", "2542", "1");
        ovs.add(ov);

        PrestationOvDecompte decompte = generateDecomtpe(ovs);
        MontantDispo montantDispo = newMontantDiponible(0, 0, 200, 0);
        List<OrdreVersementCompta> ovc = generate(decompte, montantDispo);

        Assert.assertEquals(1, ovc.size());

        Assert.assertEquals(decompte.getIdCompteAnnexeRequerant(), ovc.get(0).getCompteAnnexe().getIdCompteAnnexe());
        Assert.assertEquals(decompte.getIdCompteAnnexeRequerant(), ovc.get(0).getCompteAnnexe().getIdCompteAnnexe());

    }

    @Test
    public void testOwnerRequerantPrendsArgentAussiChezLeConjoint() throws JadeApplicationException {

        List<OrdreVersement> ovs = new ArrayList<OrdreVersement>();
        OrdreVersement ov = OrdreVersementFactory.generateCreancier("250", "2542", "1");
        ovs.add(ov);

        PrestationOvDecompte decompte = generateDecomtpe(ovs);
        MontantDispo montantDispo = newMontantDiponible(0, 0, 200, 200);
        List<OrdreVersementCompta> ovc = generate(decompte, montantDispo);

        Assert.assertEquals(2, ovc.size());
        Assert.assertEquals(new BigDecimal(200), ovc.get(0).getMontant());
        Assert.assertEquals(decompte.getIdCompteAnnexeRequerant(), ovc.get(0).getCompteAnnexe().getIdCompteAnnexe());
        Assert.assertEquals(new BigDecimal(50), ovc.get(1).getMontant());
        Assert.assertEquals(decompte.getIdCompteAnnexeConjoint(), ovc.get(1).getCompteAnnexe().getIdCompteAnnexe());
    }

    @Test
    public void testSplitWihtConjointDom2R() throws JadeApplicationException {
        List<OrdreVersement> ovs = new ArrayList<OrdreVersement>();
        OrdreVersement ov = OrdreVersementFactory.generateCreancier("70", "2542", "1");
        ovs.add(ov);

        PrestationOvDecompte decompte = generateDecomtpe(ovs);
        MontantDispo montantDispo = newMontantDiponible(100, 100, 0, 0);
        GenerateOperationsCreancier generate = newGenerateCreancier(decompte, montantDispo);

        Assert.assertEquals(1, generate.getOrdresVersementCompta().size());
        Assert.assertEquals(new BigDecimal(30), generate.getMontantsDisponible().getAllMontantRequerant());
        Assert.assertEquals(new BigDecimal(100), generate.getMontantsDisponible().getAllMontantConjoint());
    }

    // private void verifEcritureCreancier(OrdreVersement ovCerancier, Ecriture ecriture) {
    // Assert.assertEquals(ovCerancier, ecriture.getOrdreVersement());
    // Assert.assertEquals(ovCerancier.getMontant(), ecriture.getMontant());
    // Assert.assertEquals(APIReferenceRubrique.COMPENSATION_RENTES, ecriture.getIdRefRubrique());
    // Assert.assertEquals(APIEcriture.CREDIT, ecriture.getCodeDebitCredit());
    // Assert.assertEquals(TypeEcriture.CREANCIER, ecriture.getTypeEcriture());
    // Assert.assertEquals(SectionPegasus.DECISION_PC, ecriture.getSection());
    // Assert.assertNull(ecriture.getSectionDette());
    // }

}
