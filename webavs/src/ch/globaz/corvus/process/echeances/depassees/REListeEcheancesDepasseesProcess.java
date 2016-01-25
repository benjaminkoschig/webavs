package ch.globaz.corvus.process.echeances.depassees;

import ch.globaz.corvus.process.echeances.REListeEcheanceProcess;

public class REListeEcheancesDepasseesProcess extends
        REListeEcheanceProcess<REListeEcheancesDepasseesDocumentGenerator> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public REListeEcheancesDepasseesProcess() {
        super();
    }

    @Override
    protected REListeEcheancesDepasseesDocumentGenerator buildListGenerator() throws Exception {
        return new REListeEcheancesDepasseesDocumentGenerator(getSession(), getMoisTraitement());
    }

    @Override
    public String getName() {
        return REListeEcheancesDepasseesProcess.class.getName();
    }

    @Override
    protected void preparerListGenerator(REListeEcheancesDepasseesDocumentGenerator listGenerator) throws Exception {
        // rien
    }
}
