package globaz.ij.process;

import globaz.ij.application.IJApplication;
import globaz.ij.echeances.IJListerEcheancesPrononcesProcess;
import globaz.lyra.process.LYAbstractDocumentEcheanceProcess;

public class IJEcheancesPrononcesProcess extends LYAbstractDocumentEcheanceProcess<IJListerEcheancesPrononcesProcess> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public IJEcheancesPrononcesProcess() {
        super();
    }

    @Override
    protected IJListerEcheancesPrononcesProcess buildDocumentGenerator() throws Exception {
        return new IJListerEcheancesPrononcesProcess(getSession());
    }

    @Override
    public String getDescription() {
        return getSession().getLabel("GENERER_ECHEANCE_ERR");
    }

    @Override
    public String getName() {
        return IJEcheancesPrononcesProcess.class.getName();
    }

    @Override
    protected String getSessionApplicationName() {
        return IJApplication.DEFAULT_APPLICATION_IJ;
    }

    @Override
    protected void preparerDocumentGenerator(IJListerEcheancesPrononcesProcess documentGenerator) throws Exception {
        documentGenerator.setTailleLot(1);
    }
}
