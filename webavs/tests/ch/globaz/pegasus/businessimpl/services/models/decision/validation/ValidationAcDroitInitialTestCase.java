package ch.globaz.pegasus.businessimpl.services.models.decision.validation;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.api.prestations.IREPrestations;
import globaz.globall.util.JACalendar;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompte;

public class ValidationAcDroitInitialTestCase {

    public final static String DATE_DECISION = "01.06.2013";

    @Test
    public void droitInitialDemandeOctroye() throws DecisionException, JadePersistenceException,
            JadeApplicationException {
        ValiderDecisionAcData data = executeTreatValidataionInitialOctroye();
        Assert.assertEquals(IPCDemandes.CS_OCTROYE, data.getSimpleDemande().getCsEtatDemande());
        Assert.assertNull(data.getSimpleDemande().getDateFin());
        Assert.assertNotNull(data.getSimpleDemande().getDateProchaineRevision());
        Assert.assertEquals("06.2017", data.getSimpleDemande().getDateProchaineRevision());
    }

    @Ignore
    @Test
    public void droitInitialDemandeRefuse() throws DecisionException, JadePersistenceException,
            JadeApplicationException {
        ValiderDecisionAcData data = executeTreatValidataionInitialRefuse();
        Assert.assertEquals(IPCDemandes.CS_REFUSE, data.getSimpleDemande().getCsEtatDemande());
    }

    @Ignore
    @Test
    public void droitInitialDemandeSupprimer() throws DecisionException, JadePersistenceException,
            JadeApplicationException {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();
        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("01.2013", null, "150",
                ValidationAcDroitInitialTestCase.DATE_DECISION));
        decisions.get(0).getDecisionHeader().getSimpleDecisionHeader().setCsTypeDecision(IPCDecision.CS_TYPE_OCTROI_AC);
        ValiderDecisionAcData data = ValidationAcFactory.validationInitialAcData("01.2013", "05.2013", decisions);
        ValiderDecisionAcTreat treat = new ValiderDecisionAcTreat(data);
        treat.treat();
        Assert.assertEquals(IPCDemandes.CS_SUPPRIME, data.getSimpleDemande().getCsEtatDemande());
    }

    @Test
    public void droitInitialEtatDroitValide() throws DecisionException, JadePersistenceException,
            JadeApplicationException {
        ValiderDecisionAcData data = executeTreatValidataionInitialOctroye();
        Assert.assertEquals(IPCDroits.CS_VALIDE, data.getSimpleVersionDroitNew().getCsEtatDroit());
    }

    @Test
    public void droitInitialVerifiDecisions() throws DecisionException, JadePersistenceException,
            JadeApplicationException {
        ValiderDecisionAcData data = executeTreatValidataionInitialOctroye();
        for (DecisionApresCalcul dc : data.getDecisionsApresCalcul()) {
            SimpleDecisionHeader header = dc.getDecisionHeader().getSimpleDecisionHeader();
            Assert.assertEquals(IPCDecision.CS_ETAT_DECISION_VALIDE, header.getCsEtatDecision());
            Assert.assertEquals(JACalendar.todayJJsMMsAAAA(), header.getDateValidation());
            Assert.assertEquals("dma", header.getValidationPar());
        }
    }

    @Test
    public void droitnInitialVerifiEtatSimplePrestationsAccordeesNew() throws DecisionException,
            JadePersistenceException, JadeApplicationException {
        ValiderDecisionAcData data = executeTreatValidataionInitialOctroye();
        for (PcaForDecompte pca : data.getPcasNew()) {
            Assert.assertNotNull(pca.getEtatPC());
            Assert.assertEquals(IREPrestationAccordee.CS_ETAT_VALIDE, pca.getSimplePrestationsAccordees().getCsEtat());
            Assert.assertEquals(IREPrestationAccordee.CS_ETAT_VALIDE, pca.getSimplePrestationsAccordeesConjoint()
                    .getCsEtat());
        }
    }

    @Test
    public void droitnInitialVerifiEtatSimplePrestationsAccordeesNewRefuse() throws DecisionException,
            JadePersistenceException, JadeApplicationException {
        ValiderDecisionAcData data = executeTreatValidataionInitialRefuse();
        for (PcaForDecompte pca : data.getPcasNew()) {
            Assert.assertEquals(IREPrestationAccordee.CS_ETAT_DIMINUE, pca.getSimplePrestationsAccordees().getCsEtat());
            Assert.assertEquals(IREPrestationAccordee.CS_ETAT_DIMINUE, pca.getSimplePrestationsAccordeesConjoint()
                    .getCsEtat());
            Assert.assertEquals("12.2012", pca.getSimplePrestationsAccordees().getDateFinDroit());
            // Assert.assertEquals("12.2012", pca.getSimplePrestationsAccordeesConjoint().getDateFinDroit());
        }
    }

    @Test
    public void droitnInitialVerifiEtatSimplePrestationsAccordeesReplaced() throws DecisionException,
            JadePersistenceException, JadeApplicationException {
        ValiderDecisionAcData data = executeTreatValidataionInitialOctroye();
        Assert.assertEquals(0, data.getPcasReplaced().size());
    }

    @Test
    public void droitnInitialVerifiEtatSimpleSimplePCAccordeeNew() throws DecisionException, JadePersistenceException,
            JadeApplicationException {
        ValiderDecisionAcData data = executeTreatValidataionInitialOctroye();
        for (PcaForDecompte pca : data.getPcasNew()) {
            Assert.assertEquals(IPCPCAccordee.CS_ETAT_PCA_VALIDE, pca.getSimplePCAccordee().getCsEtatPC());
        }
    }

    @Test
    public void droitnInitialVerifiPcaCopie() throws DecisionException, JadePersistenceException,
            JadeApplicationException {
        ValiderDecisionAcData data = executeTreatValidataionInitialOctroye();
        Assert.assertEquals(0, data.getPcasCopie().size());
    }

    @Test
    public void droitnInitialVerifiPNbOv() throws DecisionException, JadePersistenceException, JadeApplicationException {
        ValiderDecisionAcData data = executeTreatValidataionInitialOctroye();
        Assert.assertEquals(1, data.getOvs().size());
    }

    @Test
    public void droitnInitialVerifiPrestation() throws DecisionException, JadePersistenceException,
            JadeApplicationException {
        ValiderDecisionAcData data = executeTreatValidataionInitialOctroye();
        Assert.assertNotNull(data.getSimplePrestation());
        Assert.assertEquals(IREPrestations.CS_ETAT_PRE_DEFINITIF, data.getSimplePrestation().getCsEtat());
        Assert.assertEquals(data.getSimpleVersionDroitNew().getIdVersionDroit(), data.getSimplePrestation()
                .getIdVersionDroit());
        Assert.assertEquals(data.getRequerant().getPersonneEtendue().getId(), data.getSimplePrestation()
                .getIdTiersBeneficiaire());
        Assert.assertEquals(data.getDateDebut(), data.getSimplePrestation().getDateDebut());
        Assert.assertEquals(ValidationAcFactory.DATE_DERNIER_PAIEMENT, data.getSimplePrestation().getDateFin());
        Assert.assertEquals("600", data.getSimplePrestation().getMontantTotal());
    }

    @Test
    public void droitnInitialVerifiSimpleSimplePCAccordeeReplaced() throws DecisionException, JadePersistenceException,
            JadeApplicationException {
        ValiderDecisionAcData data = executeTreatValidataionInitialOctroye();
        Assert.assertEquals(0, data.getPcasReplaced().size());
    }

    private ValiderDecisionAcData executeTreatValidataionInitialOctroye() throws DecisionException,
            JadePersistenceException, JadeApplicationException {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();
        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("01.2013", null, "150",
                ValidationAcDroitInitialTestCase.DATE_DECISION));
        ValiderDecisionAcData data = ValidationAcFactory.validationInitialAcData("01.2013", null, decisions);
        ValiderDecisionAcTreat treat = new ValiderDecisionAcTreat(data);
        treat.treat();
        return data;
    }

    private ValiderDecisionAcData executeTreatValidataionInitialRefuse() throws DecisionException,
            JadePersistenceException, JadeApplicationException {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();
        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("01.2013", null, "150",
                ValidationAcDroitInitialTestCase.DATE_DECISION));
        decisions.get(0).getDecisionHeader().getSimpleDecisionHeader().setCsTypeDecision(IPCDecision.CS_TYPE_REFUS_AC);
        decisions.get(0).getPlanCalcul().setEtatPC(IPCValeursPlanCalcul.STATUS_REFUS);
        ValiderDecisionAcData data = ValidationAcFactory.validationInitialAcData("01.2013", null, decisions);
        ValiderDecisionAcTreat treat = new ValiderDecisionAcTreat(data);
        treat.treat();
        return data;
    }

}
