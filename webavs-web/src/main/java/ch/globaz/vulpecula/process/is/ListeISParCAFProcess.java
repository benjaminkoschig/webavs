package ch.globaz.vulpecula.process.is;

import globaz.framework.util.FWMessage;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.Map;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.businessimpl.services.is.PrestationGroupee;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class ListeISParCAFProcess extends BProcessWithContext {
    private static final long serialVersionUID = 931442789627393396L;

    private Annee annee;
    private String canton;

    private Map<String, PrestationGroupee> prestationsAImprimer;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        try {
            retrieve();
            print();
        } catch (Exception ex) {
            getMemoryLog().logMessage(ex.toString(), FWMessage.ERREUR, this.getClass().getName());
            return false;
        }
        return true;
    }

    private void retrieve() {
        prestationsAImprimer = VulpeculaServiceLocator.getImpotSourceService().getPrestationsForAllocISGroupByCaisseAF(
                annee, canton);
    }

    private void print() throws Exception {
        ListISParCAFExcel listISParCAFExcel = new ListISParCAFExcel(getSession(),
                DocumentConstants.LISTES_AF_RETENUES_PAR_CAF_DOC_NAME,
                DocumentConstants.LISTES_AF_RETENUES_PAR_CAF_NAME);
        listISParCAFExcel.setPrestationsAImprimer(prestationsAImprimer);
        listISParCAFExcel.setAnnee(annee);
        listISParCAFExcel.setCanton(canton);
        listISParCAFExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), listISParCAFExcel.getOutputFile());
    }

    @Override
    protected String getEMailObject() {
        return getSession().getLabel("IS_CAF_OBJECT");
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    public Annee getAnnee() {
        return annee;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String canton) {
        this.canton = canton;
    }

}
