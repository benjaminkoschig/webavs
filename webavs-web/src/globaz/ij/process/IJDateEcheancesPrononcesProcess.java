package globaz.ij.process;

import globaz.globall.db.BSessionUtil;
import globaz.ij.application.IJApplication;
import globaz.ij.itext.IJListeDateEcheancesPrononces;
import globaz.lyra.process.LYAbstractListEcheanceProcess;
import ch.globaz.common.properties.CommonProperties;

public class IJDateEcheancesPrononcesProcess extends LYAbstractListEcheanceProcess<IJListeDateEcheancesPrononces> {

    private static final long serialVersionUID = 1L;

    public IJDateEcheancesPrononcesProcess() {
        super();
    }

    @Override
    protected IJListeDateEcheancesPrononces buildListGenerator() throws Exception {
        IJListeDateEcheancesPrononces proc = new IJListeDateEcheancesPrononces(getSession());
        proc.setAjouterCommunePolitique(CommonProperties.ADD_COMMUNE_POLITIQUE.getBooleanValue());
        return proc;
    }

    @Override
    public String getDescription() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("GENERER_ECHEANCE_ERR");
    }

    @Override
    public String getName() {
        return IJDateEcheancesPrononcesProcess.class.getName();
    }

    @Override
    protected String getSessionApplicationName() {
        return IJApplication.DEFAULT_APPLICATION_IJ;
    }

    @Override
    protected void preparerListGenerator(IJListeDateEcheancesPrononces listGenerator) throws Exception {
        // rien
    }
}
