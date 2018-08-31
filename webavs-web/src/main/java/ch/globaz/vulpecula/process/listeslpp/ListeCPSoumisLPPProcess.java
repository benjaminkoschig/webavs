package ch.globaz.vulpecula.process.listeslpp;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;

import java.util.List;

import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.congepaye.CongePaye;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class ListeCPSoumisLPPProcess extends BProcessWithContext {
    private static final long serialVersionUID = 5825307380817735953L;
    private String email;
    private String annee;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        Annee annee = new Annee(this.annee);
        setEMailAddress(email);
        List<CongePaye> list = VulpeculaRepositoryLocator.getCongePayeRepository().findCPSoumisLPP(annee);
        ListeCPsoumisLPPExcel excelDoc = new ListeCPsoumisLPPExcel(getSession(),
                DocumentConstants.LISTES_CP_SOUMIS_LPP_DOC_NAME, DocumentConstants.LISTES_CP_SOUMIS_LPP, list, annee);
        excelDoc.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), excelDoc.getOutputFile());
        return true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_CP_SOUMIS_LPP + " pour " + getAnnee();
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

}
