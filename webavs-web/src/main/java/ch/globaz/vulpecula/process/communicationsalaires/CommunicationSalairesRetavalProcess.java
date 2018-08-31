package ch.globaz.vulpecula.process.communicationsalaires;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.common.Annee;
import ch.globaz.vulpecula.domain.models.communicationsalaires.CommunicationSalairesRetaval;

public class CommunicationSalairesRetavalProcess extends AbstractCommunicationSalairesProcess implements Observer {
    private static final long serialVersionUID = -5915275758727848732L;

    private Annee annee;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();

        List<CommunicationSalairesRetaval> listeSalaires = retrieve();

        printListExcel(listeSalaires);

        return true;
    }

    private void printListExcel(List<CommunicationSalairesRetaval> listeSalaires) throws IOException {
        ListSalairesRetavalExcel listeSalairesExcel = new ListSalairesRetavalExcel(getSession(),
                DocumentConstants.LISTES_SALAIRES_RETAVAL_FILE_NAME, DocumentConstants.LISTES_SALAIRES_RETAVAL_DOC_NAME);
        listeSalairesExcel.setListDecompteSalaire(listeSalaires);
        listeSalairesExcel.setAnnee(annee);
        listeSalairesExcel.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), listeSalairesExcel.getOutputFile());
    }

    private List<CommunicationSalairesRetaval> retrieve() {
        return VulpeculaRepositoryLocator.getDecompteSalaireRepository().findSalairesRetavalPourAnnee(annee);
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_SALAIRES_RETAVAL_NAME;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    @Override
    public Annee getAnnee() {
        return annee;
    }

    @Override
    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    @Override
    public void update(Observable o, Object arg) {
        ListSalairesResorExcel listSalairesExcel = (ListSalairesResorExcel) o;
        setProgressCounter(listSalairesExcel.getCurrentElement());
    }
}
