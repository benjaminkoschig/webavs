package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import globaz.jade.exception.JadeApplicationException;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIReferenceRubrique;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ch.globaz.osiris.business.model.SectionSimpleModel;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.CompteAnnexeFactory;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.OrdreVersementFactory;

public class GenerateEcrituresDetteTestCase {
    @BeforeClass
    public static void before() {
        CompteAnnexeResolver.addComptesAnnexes(CompteAnnexeFactory.generateComptesAnnexes());
    }

    private List<Ecriture> generate(PrestationOvDecompte decompte, MontantDispo montantDispo)
            throws JadeApplicationException {
        List<SectionSimpleModel> sections = new ArrayList<SectionSimpleModel>();
        sections.add(SectionFactory.generateSection("555", "15"));
        sections.add(SectionFactory.generateSection("10", "10"));
        sections.add(SectionFactory.generateSection("555", "5"));

        GenerateEcrituresDette generate = new GenerateEcrituresDette(decompte, montantDispo, sections);
        return generate.getEcritures();
    }

    private PrestationOvDecompte generateDecomtpe(List<OrdreVersement> ovs) {
        PrestationOvDecompte decompte = new PrestationOvDecompte();
        decompte.setDettes(ovs);
        decompte.setIdTiersRequerant("1");
        decompte.setIdTiersConjoint("2");
        decompte.setCompteAnnexeRequerant(CompteAnnexeFactory
                .generateCompteAnnexe(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT));
        decompte.setCompteAnnexeConjoint(CompteAnnexeFactory
                .generateCompteAnnexe(CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT));
        return decompte;
    }

    private MontantDispo newMontantDiponible(int dom2rRequerant, int dom2rConjoint, int standardRequerant,
            int statdardConjoint) {
        return new MontantDispo(new BigDecimal(dom2rRequerant), new BigDecimal(dom2rConjoint), new BigDecimal(
                standardRequerant), new BigDecimal(statdardConjoint));
    }

    @Test
    public void testEcritureDette() throws JadeApplicationException {
        List<OrdreVersement> ovs = new ArrayList<OrdreVersement>();

        OrdreVersement ov = OrdreVersementFactory.generateDette("150", "15", "1");
        ovs.add(ov);

        PrestationOvDecompte decompte = generateDecomtpe(ovs);
        MontantDispo montantDispo = newMontantDiponible(0, 0, 200, 200);
        List<Ecriture> ecritures = generate(decompte, montantDispo);
        Assert.assertEquals(2, ecritures.size());

        Ecriture debit = ecritures.get(0);
        Ecriture credit = ecritures.get(1);
        Assert.assertEquals(ov, credit.getOrdreVersement());
        Assert.assertEquals(ov.getMontant(), credit.getMontant());
        Assert.assertEquals(APIReferenceRubrique.COMPENSATION_RENTES, credit.getIdRefRubrique());
        Assert.assertEquals(APIEcriture.CREDIT, credit.getCodeDebitCredit());
        Assert.assertNotNull(credit.getSectionSimple());

        Assert.assertEquals(SectionPegasus.DECISION_PC, debit.getSection());
        Assert.assertEquals(ov, debit.getOrdreVersement());
        Assert.assertEquals(ov.getMontant(), debit.getMontant());
        Assert.assertEquals(APIReferenceRubrique.COMPENSATION_RENTES, debit.getIdRefRubrique());
        Assert.assertEquals(APIEcriture.DEBIT, debit.getCodeDebitCredit());
        Assert.assertNull(debit.getSectionSimple());
    }

    @Test
    public void testResolveSection() throws JadeApplicationException {
        List<OrdreVersement> ovs = new ArrayList<OrdreVersement>();

        OrdreVersement ov = OrdreVersementFactory.generateDette("150", "15", "1");
        ovs.add(ov);

        PrestationOvDecompte decompte = generateDecomtpe(ovs);
        MontantDispo montantDispo = newMontantDiponible(0, 0, 200, 200);
        List<Ecriture> ecritures = generate(decompte, montantDispo);
        Assert.assertEquals(2, ecritures.size());

        Assert.assertNull(ecritures.get(0).getSectionSimple());
        Assert.assertEquals("15", ecritures.get(1).getSectionSimple().getId());

        Assert.assertEquals("10", ecritures.get(0).getIdCompteAnnexe());
        Assert.assertEquals("555", ecritures.get(1).getIdCompteAnnexe());
    }

}
