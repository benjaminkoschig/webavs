package ch.globaz.vulpecula.process.caissemaladie;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.caissemaladie.SuiviCaisseMaladie;
import ch.globaz.vulpecula.external.BProcessWithContext;
import ch.globaz.vulpecula.external.models.pyxis.Administration;

public abstract class SuiviCaisseMaladieAbstractProcess extends BProcessWithContext {
    private static final long serialVersionUID = 8772742516190178078L;
    private String Doc_Name = "";
    private String Standard_Name = "";
    private boolean simulation;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        print();
        if (!simulation) {
            updateEnvoyes();
        }
        return true;
    }

    protected abstract Map<Administration, Collection<SuiviCaisseMaladie>> getSuivis();

    private void print() throws IOException {
        SuiviCaisseMaladieExcel suiviCaisseMaladieExcel = new SuiviCaisseMaladieExcel(getSession(), Doc_Name,
                Standard_Name);
        suiviCaisseMaladieExcel.setSuivis(getSuivis());
        suiviCaisseMaladieExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this),
                suiviCaisseMaladieExcel.getOutputFile());
    }

    private void updateEnvoyes() {
        for (Map.Entry<Administration, Collection<SuiviCaisseMaladie>> entry : getSuivis().entrySet()) {
            for (SuiviCaisseMaladie suivi : entry.getValue()) {
                suivi.setEnvoye(true);
                VulpeculaRepositoryLocator.getSuiviCaisseMaladieRepository().update(suivi);
            }
        }
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public boolean getSimulation() {
        return simulation;
    }

    public boolean isSimulation() {
        return simulation;
    }

    public void setSimulation(boolean simulation) {
        this.simulation = simulation;
    }

    public String getDoc_Name() {
        return Doc_Name;
    }

    public void setDoc_Name(String doc_Name) {
        Doc_Name = doc_Name;
    }

    public String getStandard_Name() {
        return Standard_Name;
    }

    public void setStandard_Name(String standard_Name) {
        Standard_Name = standard_Name;
    }
}
