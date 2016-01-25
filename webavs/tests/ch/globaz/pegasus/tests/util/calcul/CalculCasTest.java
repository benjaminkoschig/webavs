package ch.globaz.pegasus.tests.util.calcul;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.fortuneparticuliere.Vehicule;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAi;
import ch.globaz.pegasus.tests.util.CasTest;

public class CalculCasTest extends CasTest {

    private ArrayList<DecisionApresCalcul> decisionsForCas = null;
    private ArrayList<PCAccordee> pcasForCas = null;
    private RenteAvsAi renteAvsAi = null;
    private Vehicule vehicule = null;

    public CalculCasTest(String nss, String identifiantAsString, String description, String dateProchainPaiement)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        super(nss, identifiantAsString, description, dateProchainPaiement);
        decisionsForCas = new ArrayList<DecisionApresCalcul>();
        pcasForCas = new ArrayList<PCAccordee>();
    }

    public ArrayList<DecisionApresCalcul> getDecisionsForCas() {
        return decisionsForCas;
    }

    public ArrayList<PCAccordee> getPcasForCas() {
        return pcasForCas;
    }

    public RenteAvsAi getRenteAvsAi() {
        return renteAvsAi;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setDecisionsForCas(ArrayList<DecisionApresCalcul> decisionsForCas) {
        this.decisionsForCas = decisionsForCas;
    }

    public void setPcasForCas(ArrayList<PCAccordee> pcasForCas) {
        this.pcasForCas = pcasForCas;
    }

    public void setRenteAvsAi(RenteAvsAi renteAvsAi) {
        this.renteAvsAi = renteAvsAi;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

}
