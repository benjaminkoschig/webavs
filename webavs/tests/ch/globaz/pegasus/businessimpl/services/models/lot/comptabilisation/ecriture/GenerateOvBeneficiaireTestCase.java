package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture;

import java.math.BigDecimal;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process.CompteAnnexeFactory;

public class GenerateOvBeneficiaireTestCase {

    private List<OrdreVersementCompta> generateOvs(int dom2rRequerant, int dom2rConjoint, int standardRequerant,
            int standardConjoint) {
        GenerateOvBeneficiaire generate = newGenerateOvBeneficiaire();
        MontantDispo montantDispo = new MontantDispo(dom2rRequerant, dom2rConjoint, standardRequerant, standardConjoint);
        PrestationOvDecompte decompte = DecompteFactory.generateDecompteRequerantConjoint();

        List<OrdreVersementCompta> ovs = generate.generateOvComptaBeneficiare(montantDispo, decompte);
        return ovs;
    }

    private GenerateOvBeneficiaire newGenerateOvBeneficiaire() {
        GenerateOvBeneficiaire generate = new GenerateOvBeneficiaire(new GenerateOvComptaAndGroupe());
        return generate;
    }

    @Test
    public void testGenerateOvComptaBeneficiareConjointDom() {
        List<OrdreVersementCompta> ovs = generateOvs(0, 0, 0, 100);
        Assert.assertEquals(1, ovs.size());
        verifiConjoint(ovs.get(0), 100);
    }

    @Test
    public void testGenerateOvComptaBeneficiareConjointDom2R() {
        List<OrdreVersementCompta> ovs = generateOvs(0, 100, 0, 0);
        Assert.assertEquals(1, ovs.size());
        verifiConjointDom2R(ovs.get(0), 100);
    }

    @Test
    public void testGenerateOvComptaBeneficiareConjointDomAndDom2R() {
        List<OrdreVersementCompta> ovs = generateOvs(0, 100, 0, 100);
        Assert.assertEquals(2, ovs.size());
        verifiConjointDom2R(ovs.get(0), 100);
    }

    @Test
    public void testGenerateOvComptaBeneficiareReqConjDom() {
        List<OrdreVersementCompta> ovs = generateOvs(0, 0, 100, 100);
        Assert.assertEquals(2, ovs.size());
        verifiRequerant(ovs.get(0), 100);
        verifiConjoint(ovs.get(1), 100);
    }

    @Test
    public void testGenerateOvComptaBeneficiareReqConjDom2R() {
        List<OrdreVersementCompta> ovs = generateOvs(100, 100, 0, 0);
        Assert.assertEquals(2, ovs.size());
        verifiRequerant(ovs.get(0), 100);
        verifiConjointDom2R(ovs.get(1), 100);
    }

    @Test
    public void testGenerateOvComptaBeneficiareReqConjDomAndDom2R() {
        List<OrdreVersementCompta> ovs = generateOvs(100, 100, 150, 50);
        Assert.assertEquals(3, ovs.size());
        verifiRequerant(ovs.get(0), 250);
        verifiConjointDom2R(ovs.get(1), 100);
        verifiConjoint(ovs.get(2), 50);
    }

    @Test
    public void testGenerateOvComptaBeneficiareRequerantDom() {
        List<OrdreVersementCompta> ovs = generateOvs(0, 0, 100, 0);
        Assert.assertEquals(1, ovs.size());
        verifiRequerant(ovs.get(0), 100);
    }

    @Test
    public void testGenerateOvComptaBeneficiareRequerantDom2R() {
        List<OrdreVersementCompta> ovs = generateOvs(100, 0, 0, 0);
        Assert.assertEquals(1, ovs.size());
        verifiRequerant(ovs.get(0), 100);
    }

    @Test
    public void testGenerateOvComptaBeneficiareRequerantDomAndDom2R() {
        List<OrdreVersementCompta> ovs = generateOvs(100, 0, 100, 0);
        Assert.assertEquals(1, ovs.size());
        verifiRequerant(ovs.get(0), 200);
    }

    private void verifiConjoint(OrdreVersementCompta ov, int montant) {
        Assert.assertEquals(new BigDecimal(montant), ov.getMontant());
        Assert.assertEquals(CompteAnnexeFactory.ID_TIERS_ADRESSE_PAIEMENT_CONJOINT, ov.getIdTiersAdressePaiement());
        Assert.assertEquals(CompteAnnexeFactory.ID_TIERS_DOMMAINE_APPLICATION_CONJOINT, ov.getIdDomaineApplication());
        Assert.assertEquals(CompteAnnexeFactory.COMPTE_ANNEXE_CONJOINT, ov.getCompteAnnexe().getIdCompteAnnexe());
        Assert.assertEquals(CompteAnnexeFactory.ID_TIERS_CONJOINT, ov.getIdTiers());
    }

    private void verifiConjointDom2R(OrdreVersementCompta ov, int montant) {
        Assert.assertEquals(new BigDecimal(montant), ov.getMontant());
        Assert.assertEquals(CompteAnnexeFactory.ID_TIERS_ADRESSE_PAIEMENT_CONJOINT, ov.getIdTiersAdressePaiement());
        Assert.assertEquals(CompteAnnexeFactory.ID_TIERS_DOMMAINE_APPLICATION_CONJOINT, ov.getIdDomaineApplication());
        Assert.assertEquals(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT, ov.getCompteAnnexe().getIdCompteAnnexe());
        Assert.assertEquals(CompteAnnexeFactory.ID_TIERS_CONJOINT, ov.getIdTiers());
    }

    private void verifiRequerant(OrdreVersementCompta ov, int montant) {
        Assert.assertEquals(new BigDecimal(montant), ov.getMontant());
        Assert.assertEquals(CompteAnnexeFactory.ID_TIERS_ADRESSE_PAIEMENT_REQUERANT, ov.getIdTiersAdressePaiement());
        Assert.assertEquals(CompteAnnexeFactory.ID_TIERS_DOMMAINE_APPLICATION_REQUERANT, ov.getIdDomaineApplication());
        Assert.assertEquals(CompteAnnexeFactory.COMPTE_ANNEXE_REQUERANT, ov.getCompteAnnexe().getIdCompteAnnexe());
        Assert.assertEquals(CompteAnnexeFactory.ID_TIERS_REQUERANT, ov.getIdTiers());
    }
}
