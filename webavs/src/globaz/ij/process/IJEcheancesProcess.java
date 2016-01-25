package globaz.ij.process;

import globaz.globall.db.BSessionUtil;
import globaz.ij.application.IJApplication;
import globaz.ij.echeances.IJListerEcheancesIJProcess;
import globaz.lyra.process.LYAbstractDocumentEcheanceProcess;
import ch.globaz.common.properties.CommonProperties;

public class IJEcheancesProcess extends LYAbstractDocumentEcheanceProcess<IJListerEcheancesIJProcess> {

    private static final long serialVersionUID = 1L;

    public IJEcheancesProcess() {
        super();
    }

    @Override
    protected IJListerEcheancesIJProcess buildDocumentGenerator() throws Exception {
        IJListerEcheancesIJProcess proc = new IJListerEcheancesIJProcess(getSession());
        proc.setAjouterCommunePolitique(CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue());
        return proc;
    }

    @Override
    public String getDescription() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("TITRE_DOCUMENT_1825_ANS");
    }

    @Override
    public String getName() {
        return IJEcheancesProcess.class.getName();
    }

    @Override
    protected String getSessionApplicationName() {
        return IJApplication.DEFAULT_APPLICATION_IJ;
    }

    @Override
    protected void preparerDocumentGenerator(IJListerEcheancesIJProcess documentGenerator) throws Exception {
        documentGenerator.setTailleLot(1);
    }
}
