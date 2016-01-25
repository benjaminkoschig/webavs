package ch.globaz.pegasus.businessimpl.services.models.decision.validation;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;

public class EtatDemandeResolverTestCase {

    private DecisionApresCalcul newDecsion(String dateDebut, String dateFin) {
        return ValidationAcFactory.generateDecisionApresCalcul(dateDebut, dateFin, "150");
    }

    private DecisionApresCalcul newDecsion(String dateDebut, String dateFin, String csRoleFamille) {
        DecisionApresCalcul dc = ValidationAcFactory.generateDecisionApresCalcul(dateDebut, dateFin, "150");
        dc.getPcAccordee().getSimplePCAccordee().setCsRoleBeneficiaire(csRoleFamille);
        return dc;
    }

    private DecisionApresCalcul newDecsionSetType(String csTypeDecision) {
        DecisionApresCalcul dc = ValidationAcFactory.generateDecisionApresCalcul("01.2012", "02.2012", "150");
        dc.getPcAccordee().getSimplePCAccordee().setCsRoleBeneficiaire(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
        dc.getDecisionHeader().getSimpleDecisionHeader().setCsTypeDecision(csTypeDecision);
        return dc;
    }

    @Test
    public void testResolveDecionsMostFavorable1ForConjoint() throws Exception {
        DecisionApresCalcul requerant = this.newDecsion("01.2012", "02.2012");
        requerant.getPlanCalcul().setEtatPC(IPCValeursPlanCalcul.STATUS_REFUS);
        DecisionApresCalcul conjoint = this.newDecsion("01.2012", "02.2012");
        EtatDemandeResolver.resolveDecionsMostFavorable(requerant, conjoint);
        Assert.assertEquals(conjoint, EtatDemandeResolver.resolveDecionsMostFavorable(requerant, conjoint));
    }

    @Test
    public void testResolveDecionsMostFavorable2ForConjoint() throws Exception {
        DecisionApresCalcul requerant = this.newDecsion("01.2012", "02.2012");
        requerant.getPlanCalcul().setEtatPC(IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL);
        DecisionApresCalcul conjoint = this.newDecsion("01.2012", "02.2012");
        conjoint.getPlanCalcul().setEtatPC(IPCValeursPlanCalcul.STATUS_OCTROI);
        EtatDemandeResolver.resolveDecionsMostFavorable(requerant, conjoint);
        Assert.assertEquals(conjoint, EtatDemandeResolver.resolveDecionsMostFavorable(requerant, conjoint));
    }

    @Test
    public void testResolveDecionsMostFavorable3ForConjoint() throws Exception {
        DecisionApresCalcul requerant = this.newDecsion("01.2012", "02.2012");
        requerant.getPlanCalcul().setEtatPC(IPCValeursPlanCalcul.STATUS_REFUS);
        DecisionApresCalcul conjoint = this.newDecsion("01.2012", "02.2012");
        conjoint.getPlanCalcul().setEtatPC(IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL);
        EtatDemandeResolver.resolveDecionsMostFavorable(requerant, conjoint);
        Assert.assertEquals(conjoint, EtatDemandeResolver.resolveDecionsMostFavorable(requerant, conjoint));
    }

    @Test
    public void testResolveDecionsMostFavorableForRequerant() throws Exception {
        DecisionApresCalcul requerant = this.newDecsion("01.2012", "02.2012");
        DecisionApresCalcul conjoint = this.newDecsion("01.2012", "02.2012");
        conjoint.getDecisionHeader().getSimpleDecisionHeader().setCsTypeDecision(IPCDecision.CS_TYPE_REFUS_AC);
        EtatDemandeResolver.resolveDecionsMostFavorable(requerant, conjoint);
        Assert.assertEquals(requerant, EtatDemandeResolver.resolveDecionsMostFavorable(requerant, conjoint));
    }

    @Test
    public void testResolveDecionsMostFavorableWithBothSameState() throws Exception {
        DecisionApresCalcul requerant = this.newDecsion("01.2012", "02.2012");
        DecisionApresCalcul conjoint = this.newDecsion("01.2012", "02.2012");
        EtatDemandeResolver.resolveDecionsMostFavorable(requerant, conjoint);
        Assert.assertEquals(requerant, EtatDemandeResolver.resolveDecionsMostFavorable(requerant, conjoint));
    }

    @Test
    public void testResolveDecionsMostFavorableWithOutConjoin() throws Exception {
        DecisionApresCalcul requerant = this.newDecsion("01.2012", "02.2012");
        EtatDemandeResolver.resolveDecionsMostFavorable(requerant, null);
        Assert.assertEquals(requerant, EtatDemandeResolver.resolveDecionsMostFavorable(requerant, null));
    }

    @Test
    public void testResolvedEtatDemande() throws Exception {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();
        decisions.add(this.newDecsion("03.2012", null));
        decisions.get(0).getVersionDroit().getDemande().getSimpleDemande().setDateFin(null);
        Assert.assertEquals(IPCDemandes.CS_OCTROYE, EtatDemandeResolver.resolvedEtatDemande(decisions, false));
    }

    @Test
    public void testResolvedEtatDemandeAvecDateDefin() throws Exception {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();
        decisions.add(this.newDecsion("01.2013", "12.2013"));
        decisions.get(0).getVersionDroit().getDemande().getSimpleDemande().setDateFin("12.2013");

        Assert.assertEquals(IPCDemandes.CS_SUPPRIME, EtatDemandeResolver.resolvedEtatDemande(decisions, false));
    }

    @Test
    public void testResolvedEtatDemandeWithConjoinFavorable() throws Exception {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();
        DecisionApresCalcul requerant = this.newDecsion("01.2012", null);
        requerant.getDecisionHeader().getSimpleDecisionHeader().setCsTypeDecision(IPCDecision.CS_TYPE_REFUS_AC);
        DecisionApresCalcul conjoint = this.newDecsion("01.2012", null);
        conjoint.getPcAccordee().getSimplePCAccordee().setCsRoleBeneficiaire(IPCDroits.CS_ROLE_FAMILLE_CONJOINT);
        decisions.add(requerant);
        decisions.add(conjoint);

        Assert.assertEquals(IPCDemandes.CS_OCTROYE, EtatDemandeResolver.resolvedEtatDemande(decisions, false));
    }

    @Test
    public void testResolveEtatDecisionReOuvert() throws Exception {
        Assert.assertEquals(IPCDemandes.CS_REOUVERT,
                EtatDemandeResolver.resolveEtatDemandeByEtaPca(IPCValeursPlanCalcul.STATUS_REFUS, true, false, true));
        Assert.assertEquals(IPCDemandes.CS_REOUVERT,
                EtatDemandeResolver.resolveEtatDemandeByEtaPca(IPCValeursPlanCalcul.STATUS_REFUS, true, true, true));
    }

    @Test
    public void testResolveEtatDecisionOctroie() throws Exception {
        Assert.assertEquals(IPCDemandes.CS_OCTROYE,
                EtatDemandeResolver.resolveEtatDemandeByEtaPca(IPCValeursPlanCalcul.STATUS_OCTROI, false, false, false));
        Assert.assertEquals(IPCDemandes.CS_OCTROYE,
                EtatDemandeResolver.resolveEtatDemandeByEtaPca(IPCValeursPlanCalcul.STATUS_OCTROI, true, false, false));
    }

    @Test
    public void testResolveEtatDecisionOctroiePartiel() throws Exception {
        Assert.assertEquals(IPCDemandes.CS_OCTROYE, EtatDemandeResolver.resolveEtatDemandeByEtaPca(
                IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL, false, false, false));
        Assert.assertEquals(IPCDemandes.CS_OCTROYE, EtatDemandeResolver.resolveEtatDemandeByEtaPca(
                IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL, true, false, false));
    }

    @Test
    public void testResolveEtatDecisionRefus() throws Exception {
        Assert.assertEquals(IPCDemandes.CS_REFUSE,
                EtatDemandeResolver.resolveEtatDemandeByEtaPca(IPCValeursPlanCalcul.STATUS_REFUS, true, false, false));
        Assert.assertEquals(IPCDemandes.CS_REFUSE,
                EtatDemandeResolver.resolveEtatDemandeByEtaPca(IPCValeursPlanCalcul.STATUS_REFUS, true, true, false));
    }

    @Test
    public void testResolveEtatDecisionSupprimerByRefus() throws Exception {
        Assert.assertEquals(IPCDemandes.CS_SUPPRIME,
                EtatDemandeResolver.resolveEtatDemandeByEtaPca(IPCValeursPlanCalcul.STATUS_REFUS, false, true, false));
        Assert.assertEquals(IPCDemandes.CS_SUPPRIME,
                EtatDemandeResolver.resolveEtatDemandeByEtaPca(IPCValeursPlanCalcul.STATUS_REFUS, false, false, false));
    }

    @Test
    public void testResolveEtatDecisionSupprimer() throws Exception {
        Assert.assertEquals(IPCDemandes.CS_SUPPRIME,
                EtatDemandeResolver.resolveEtatDemandeByEtaPca(IPCValeursPlanCalcul.STATUS_OCTROI, false, true, false));
        Assert.assertEquals(IPCDemandes.CS_SUPPRIME,
                EtatDemandeResolver.resolveEtatDemandeByEtaPca(IPCValeursPlanCalcul.STATUS_OCTROI, true, true, false));
    }

    // @Test
    // public void testHasOnlyPcaRefusTrue() {
    // List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();
    // decisions.add(this.newDecsionSetType(IPCDecision.CS_TYPE_REFUS_AC));
    // decisions.add(this.newDecsionSetType(IPCDecision.CS_TYPE_REFUS_AC));
    // decisions.add(this.newDecsionSetType(IPCDecision.CS_TYPE_REFUS_AC));
    // Assert.assertTrue(EtatDemandeResolver.hasOnlyRefus(decisions));
    // }
    //
    // @Test
    // public void testHasOnlyPcaRefusFalse() {
    // List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();
    // decisions.add(this.newDecsionSetType(IPCDecision.CS_TYPE_REFUS_AC));
    // decisions.add(this.newDecsionSetType(IPCDecision.CS_TYPE_REFUS_AC));
    // decisions.add(this.newDecsionSetType(IPCDecision.CS_TYPE_OCTROI_AC));
    // Assert.assertFalse(EtatDemandeResolver.hasOnlyRefus(decisions));
    // }

    @Test
    public void testSortDecsionByDateDateDebutAvecCoupleSepDesc() throws Exception {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();
        decisions.add(this.newDecsion("01.2012", "02.2012", IPCDroits.CS_ROLE_FAMILLE_CONJOINT));
        decisions.add(this.newDecsion("01.2012", "02.2012", IPCDroits.CS_ROLE_FAMILLE_REQUERANT));
        decisions.add(this.newDecsion("01.2013", null, IPCDroits.CS_ROLE_FAMILLE_REQUERANT));
        decisions.add(this.newDecsion("01.2013", null, IPCDroits.CS_ROLE_FAMILLE_CONJOINT));

        List<DecisionApresCalcul> list = EtatDemandeResolver.sortDecsionByDateDateDebutDesc(decisions);

        Assert.assertEquals("01.2013", list.get(0).getDecisionHeader().getSimpleDecisionHeader().getDateDebutDecision());
        Assert.assertEquals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT, list.get(0).getPcAccordee().getSimplePCAccordee()
                .getCsRoleBeneficiaire());

    }

    @Test
    public void testSortDecsionByDateDateDebutAvecCoupleSepDesc2() throws Exception {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();
        decisions.add(this.newDecsion("01.2012", "02.2012", IPCDroits.CS_ROLE_FAMILLE_CONJOINT));
        decisions.add(this.newDecsion("01.2012", "02.2012", IPCDroits.CS_ROLE_FAMILLE_REQUERANT));
        decisions.add(this.newDecsion("01.2013", null, IPCDroits.CS_ROLE_FAMILLE_CONJOINT));
        decisions.add(this.newDecsion("01.2013", null, IPCDroits.CS_ROLE_FAMILLE_REQUERANT));

        List<DecisionApresCalcul> list = EtatDemandeResolver.sortDecsionByDateDateDebutDesc(decisions);

        Assert.assertEquals("01.2013", list.get(0).getDecisionHeader().getSimpleDecisionHeader().getDateDebutDecision());
        Assert.assertEquals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT, list.get(0).getPcAccordee().getSimplePCAccordee()
                .getCsRoleBeneficiaire());
    }

    @Test
    public void testSortDecsionByDateDateFinDesc() throws Exception {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();
        decisions.add(this.newDecsion("01.2012", "02.2012"));
        decisions.add(this.newDecsion("01.2011", "08.2011"));
        decisions.add(this.newDecsion("09.2011", "12.2011"));
        decisions.add(this.newDecsion("03.2012", "12.2012"));
        decisions.add(this.newDecsion("01.2013", null));

        List<DecisionApresCalcul> list = EtatDemandeResolver.sortDecsionByDateDateDebutDesc(decisions);

        Assert.assertEquals("01.2012", decisions.get(0).getDecisionHeader().getSimpleDecisionHeader()
                .getDateDebutDecision());
        Assert.assertEquals("01.2013", list.get(0).getDecisionHeader().getSimpleDecisionHeader().getDateDebutDecision());
        Assert.assertEquals("01.2011", list.get(decisions.size() - 1).getDecisionHeader().getSimpleDecisionHeader()
                .getDateDebutDecision());

    }
}
