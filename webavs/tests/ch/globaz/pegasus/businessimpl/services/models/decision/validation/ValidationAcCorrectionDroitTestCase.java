package ch.globaz.pegasus.businessimpl.services.models.decision.validation;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.PcaForDecompte;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;

public class ValidationAcCorrectionDroitTestCase {

    public ValiderDecisionAcData executeTreatValidataionOctroye() throws DecisionException, JadePersistenceException,
            JadeApplicationException {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();

        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("06.2012", "06.2012", "100", "06.2013"));
        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("07.2012", "12.2012", "110", "06.2013"));
        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("01.2013", null, "150", "06.2013"));
        List<PcaForDecompte> pcasReplaced = new ArrayList<PcaForDecompte>();
        ValiderDecisionAcData data = ValidationAcFactory.validationCorrectionDroitAcData("01.2013", null, decisions);
        pcasReplaced.add(ValidationAcFactory.generatePcaForDecompte("01.2012", "12.2012", "90"));
        pcasReplaced.add(ValidationAcFactory.generatePcaForDecompte("01.2013", "12.2013", "140"));
        data.setPcasReplaced(pcasReplaced);
        ValiderDecisionAcTreat treat = new ValiderDecisionAcTreat(data);
        treat.treat();
        return data;
    }

    public ValiderDecisionAcData executeTreatValidationWithAllocationNoel() throws DecisionException,
            JadePersistenceException, JadeApplicationException {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();

        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("06.2012", "06.2012", "100", "06.2013"));
        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("07.2012", "12.2012", "140", "06.2013"));
        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("01.2013", null, "150", "06.2013"));
        List<PcaForDecompte> pcasReplaced = new ArrayList<PcaForDecompte>();
        ValiderDecisionAcData data = ValidationAcFactory.validationCorrectionDroitAcData("01.2013", null, decisions);
        pcasReplaced.add(ValidationAcFactory.generatePcaForDecompte("01.2012", "12.2012", "80"));
        pcasReplaced.add(ValidationAcFactory.generatePcaForDecompte("01.2013", null, "140"));
        data.setPcasReplaced(pcasReplaced);
        data.setAllocationsNoel(new ArrayList<SimpleAllocationNoel>());
        data.getAllocationsNoel().add(new SimpleAllocationNoel());
        data.getAllocationsNoel().get(0).setMontantAllocation("200");
        data.getAllocationsNoel().get(0).setNbrePersonnes("2");
        data.getAllocationsNoel().get(0)
                .setIdPCAccordee(decisions.get(0).getPcAccordee().getSimplePCAccordee().getIdPCAccordee());
        data.setHasAllocationNoel(true);
        ValiderDecisionAcTreat treat = new ValiderDecisionAcTreat(data);
        treat.treat();
        return data;
    }

    public ValiderDecisionAcData executeTreatValidationWithAllocationNoelParam() throws DecisionException,
            JadePersistenceException, JadeApplicationException {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();

        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("06.2012", "06.2012", "100"));
        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("07.2012", "12.2012", "140"));
        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("01.2013", null, "150"));
        List<PcaForDecompte> pcasReplaced = new ArrayList<PcaForDecompte>();
        ValiderDecisionAcData data = ValidationAcFactory.validationCorrectionDroitAcData("01.2013", null, decisions);
        pcasReplaced.add(ValidationAcFactory.generatePcaForDecompte("01.2012", "12.2012", "80"));
        pcasReplaced.add(ValidationAcFactory.generatePcaForDecompte("01.2013", null, "140"));
        data.setPcasReplaced(pcasReplaced);
        data.setHasAllocationNoel(true);
        ValiderDecisionAcTreat treat = new ValiderDecisionAcTreat(data);
        treat.treat();
        return data;
    }

    @Test
    public void testValidationAvecPcaQuiCommencALaDateDuProchainPmt() throws DecisionException,
            JadePersistenceException, JadeApplicationException {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();

        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("06.2012", "06.2012", "100"));
        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("07.2012", "12.2012", "110"));
        decisions.add(ValidationAcFactory.generateDecisionApresCalcul("01.2013",
                ValidationAcFactory.DATE_DERNIER_PAIEMENT, "155"));
        decisions.add(ValidationAcFactory.generateDecisionApresCalcul(ValidationAcFactory.DATE_PROCHAIN_PAIEMENT, null,
                "150", "06.2013"));
        List<PcaForDecompte> pcasReplaced = new ArrayList<PcaForDecompte>();
        ValiderDecisionAcData data = ValidationAcFactory.validationCorrectionDroitAcData(
                ValidationAcFactory.DATE_PROCHAIN_PAIEMENT, null, decisions);
        pcasReplaced.add(ValidationAcFactory.generatePcaForDecompte("01.2012", "12.2012", "90"));
        pcasReplaced.add(ValidationAcFactory.generatePcaForDecompte("01.2013", null, "140"));
        data.setPcasReplaced(pcasReplaced);
        ValiderDecisionAcTreat treat = new ValiderDecisionAcTreat(data);
        treat.treat();
        Assert.assertEquals(6, data.getOvs().size());
        Assert.assertEquals("190", data.getSimplePrestation().getMontantTotal());
    }

    @Ignore
    @Test
    public void testValidationDemandeDansEtatRefuseNotChange() throws DecisionException, JadePersistenceException,
            JadeApplicationException {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();

        decisions.add(ValidationAcFactory.generateDecisionApresCalcul(ValidationAcFactory.DATE_PROCHAIN_PAIEMENT, null,
                "150", "06.2013"));
        List<PcaForDecompte> pcasReplaced = new ArrayList<PcaForDecompte>();
        ValiderDecisionAcData data = ValidationAcFactory.validationCorrectionDroitAcData(
                ValidationAcFactory.DATE_PROCHAIN_PAIEMENT, null, decisions, false);
        data.setPcasReplaced(pcasReplaced);
        data.getSimpleDemande().setCsEtatDemande(IPCDemandes.CS_REFUSE);
        ValiderDecisionAcTreat treat = new ValiderDecisionAcTreat(data);
        treat.treat();
        Assert.assertEquals(IPCDemandes.CS_REFUSE, data.getSimpleDemande().getCsEtatDemande());
    }

    @Ignore
    @Test
    public void testValidationDemandeDansEtatSupprimeNotChange() throws DecisionException, JadePersistenceException,
            JadeApplicationException {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();

        decisions.add(ValidationAcFactory.generateDecisionApresCalcul(ValidationAcFactory.DATE_PROCHAIN_PAIEMENT, null,
                "150", "06.2013"));
        List<PcaForDecompte> pcasReplaced = new ArrayList<PcaForDecompte>();
        ValiderDecisionAcData data = ValidationAcFactory.validationCorrectionDroitAcData(
                ValidationAcFactory.DATE_PROCHAIN_PAIEMENT, null, decisions, false);
        data.setPcasReplaced(pcasReplaced);
        data.getSimpleDemande().setCsEtatDemande(IPCDemandes.CS_SUPPRIME);
        ValiderDecisionAcTreat treat = new ValiderDecisionAcTreat(data);
        treat.treat();
        Assert.assertEquals(IPCDemandes.CS_SUPPRIME, data.getSimpleDemande().getCsEtatDemande());
    }

    @Ignore
    @Test
    public void testValidationDemandeDansEtatTransfereNotChange() throws DecisionException, JadePersistenceException,
            JadeApplicationException {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();

        decisions.add(ValidationAcFactory.generateDecisionApresCalcul(ValidationAcFactory.DATE_PROCHAIN_PAIEMENT, null,
                "150", "06.2013"));
        List<PcaForDecompte> pcasReplaced = new ArrayList<PcaForDecompte>();
        ValiderDecisionAcData data = ValidationAcFactory.validationCorrectionDroitAcData(
                ValidationAcFactory.DATE_PROCHAIN_PAIEMENT, null, decisions, false);
        data.setPcasReplaced(pcasReplaced);
        data.getSimpleDemande().setCsEtatDemande(IPCDemandes.CS_TRANSFERE);
        ValiderDecisionAcTreat treat = new ValiderDecisionAcTreat(data);
        treat.treat();
        Assert.assertEquals(IPCDemandes.CS_TRANSFERE, data.getSimpleDemande().getCsEtatDemande());
    }

    @Test
    public void testValidationEffetMoisSuivant() throws DecisionException, JadePersistenceException,
            JadeApplicationException {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();

        decisions.add(ValidationAcFactory.generateDecisionApresCalcul(ValidationAcFactory.DATE_PROCHAIN_PAIEMENT, null,
                "150", "06.2013"));
        List<PcaForDecompte> pcasReplaced = new ArrayList<PcaForDecompte>();
        ValiderDecisionAcData data = ValidationAcFactory.validationCorrectionDroitAcData(
                ValidationAcFactory.DATE_PROCHAIN_PAIEMENT, null, decisions, false);
        data.setPcasReplaced(pcasReplaced);
        ValiderDecisionAcTreat treat = new ValiderDecisionAcTreat(data);
        treat.treat();
        Assert.assertEquals(1, data.getOvs().size());
        Assert.assertEquals("0", data.getSimplePrestation().getMontantTotal());
    }

    @Test
    public void testValidationEffetMoisSuivantCoupleSep() throws DecisionException, JadePersistenceException,
            JadeApplicationException {
        List<DecisionApresCalcul> decisions = new ArrayList<DecisionApresCalcul>();
        String dateDebutPca = ValidationAcFactory.DATE_PROCHAIN_PAIEMENT;
        decisions.add(ValidationAcFactory.generateDecisionApresCalculPourConjointCoupleSep(dateDebutPca, null, "110"));
        decisions.add(ValidationAcFactory.generateDecisionApresCalcul(dateDebutPca, null, "150"));
        List<PcaForDecompte> pcasReplaced = new ArrayList<PcaForDecompte>();
        ValiderDecisionAcData data = ValidationAcFactory.validationCorrectionDroitAcData(dateDebutPca, null, decisions,
                false);
        data.setPcasReplaced(pcasReplaced);
        ValiderDecisionAcTreat treat = new ValiderDecisionAcTreat(data);
        treat.treat();
        Assert.assertEquals(2, data.getOvs().size());
        Assert.assertEquals("0", data.getSimplePrestation().getMontantTotal());
    }

    @Test
    public void verifiEtatDroitReplaced() throws DecisionException, JadePersistenceException, JadeApplicationException {
        ValiderDecisionAcData data = executeTreatValidataionOctroye();
        Assert.assertEquals(IPCDroits.CS_HISTORISE, data.getSimpleVersionDroitReplaced().getCsEtatDroit());
    }

    @Ignore
    @Test
    public void verifiPcaReplaced() throws DecisionException, JadePersistenceException, JadeApplicationException {
        ValiderDecisionAcData data = executeTreatValidataionOctroye();
        Assert.assertEquals(IPCDemandes.CS_OCTROYE, data.getSimpleDemande().getCsEtatDemande());
        for (PcaForDecompte pca : data.getPcasReplaced()) {

            Assert.assertEquals(IREPrestationAccordee.CS_ETAT_DIMINUE, pca.getSimplePrestationsAccordees().getCsEtat());
            Assert.assertEquals(IREPrestationAccordee.CS_ETAT_DIMINUE, pca.getSimplePrestationsAccordeesConjoint()
                    .getCsEtat());
            Assert.assertEquals(IPCPCAccordee.CS_ETAT_PCA_HISTORISEE, pca.getSimplePCAccordee().getCsEtatPC());
        }
        Assert.assertEquals("12.2012", data.getPcasReplaced().get(0).getSimplePrestationsAccordees().getDateFinDroit());
        Assert.assertEquals("12.2013", data.getPcasReplaced().get(1).getSimplePrestationsAccordees().getDateFinDroit());
    }

    @Test
    public void withAllocationNoel() throws DecisionException, JadePersistenceException, JadeApplicationException {
        ValiderDecisionAcData data = executeTreatValidationWithAllocationNoel();
        Assert.assertEquals(7, data.getOvs().size());
        Assert.assertEquals("620", data.getSimplePrestation().getMontantTotal());
    }

    @Test
    public void withAllocationNoelParam() throws DecisionException, JadePersistenceException, JadeApplicationException {
        ValiderDecisionAcData data = executeTreatValidationWithAllocationNoelParam();
        Assert.assertEquals(6, data.getOvs().size());
        Assert.assertEquals("420", data.getSimplePrestation().getMontantTotal());
    }
}
