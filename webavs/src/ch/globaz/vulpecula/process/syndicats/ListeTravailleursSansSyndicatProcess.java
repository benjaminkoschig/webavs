package ch.globaz.vulpecula.process.syndicats;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.io.IOException;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.business.services.syndicat.AffiliationSyndicatService;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class ListeTravailleursSansSyndicatProcess extends BProcessWithContext {
    private static final long serialVersionUID = -3840278440960810742L;

    private AffiliationSyndicatService affiliationSyndicatService = VulpeculaServiceLocator
            .getAffiliationSyndicatService();

    private Annee annee;

    private List<Travailleur> travailleurs;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        retrieve();
        print();
        return true;
    }

    private void retrieve() {
        travailleurs = affiliationSyndicatService.findTravailleursSansSyndicats(annee);
    }

    private void print() throws IOException {
        ListeTravailleursSansSyndicatExcel listeTravailleursSansSyndicatExcel = new ListeTravailleursSansSyndicatExcel(
                getSession(), DocumentConstants.LISTES_TRAVAILLEURS_SANS_SYNDICAT_DOC_NAME,
                DocumentConstants.LISTES_TRAVAILLEURS_SANS_SYNDICAT_NAME);
        listeTravailleursSansSyndicatExcel.setTravailleurs(travailleurs);
        listeTravailleursSansSyndicatExcel.setAnnee(annee);
        listeTravailleursSansSyndicatExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this),
                listeTravailleursSansSyndicatExcel.getOutputFile());
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_TRAVAILLEURS_SANS_SYNDICAT_NAME;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public Annee getAnnee() {
        return annee;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }
}
