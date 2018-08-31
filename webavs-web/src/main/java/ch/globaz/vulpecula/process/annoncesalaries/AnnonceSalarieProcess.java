package ch.globaz.vulpecula.process.annoncesalaries;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.io.IOException;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class AnnonceSalarieProcess extends BProcessWithContext {
    private static final long serialVersionUID = -5915275758727848732L;

    private boolean miseAJour = false;
    private String date;

    private List<PosteTravail> postes;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        retrieve();
        print();

        if (miseAJour) {
            updateTravailleurs();
        }
        return true;
    }

    private void retrieve() {
        postes = VulpeculaServiceLocator.getPosteTravailService().findAAnnoncer2(new Date(date));
    }

    private void print() throws IOException {
        AnnonceSalarieExcel annonceSalarieExcel = new AnnonceSalarieExcel(postes, getSession(),
                DocumentConstants.LISTES_ANNONCE_SALARIE_DOC_NAME);
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), annonceSalarieExcel.getOutputFile());
    }

    private void updateTravailleurs() {
        for (PosteTravail posteTravail : postes) {
            Travailleur travailleur = posteTravail.getTravailleur();
            travailleur.setAnnonceMeroba(true);
            travailleur.setDateAnnonceMeroba(new Date(date));
            VulpeculaRepositoryLocator.getTravailleurRepository().update(travailleur);
        }
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_ANNONCE_SALARIE_NAME;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public boolean getMiseAJour() {
        return miseAJour;
    }

    public String getDate() {
        return date;
    }

    public void setMiseAJour(boolean miseAJour) {
        this.miseAJour = miseAJour;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
