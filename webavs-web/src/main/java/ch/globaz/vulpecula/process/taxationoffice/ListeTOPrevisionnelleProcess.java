package ch.globaz.vulpecula.process.taxationoffice;

import globaz.globall.db.GlobazJobQueue;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.domain.models.taxationoffice.EtatTaxation;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;
import ch.globaz.vulpecula.external.BProcessWithContext;

public class ListeTOPrevisionnelleProcess extends BProcessWithContext {
    private static final long serialVersionUID = -3850771278071874877L;

    private String email;

    @Override
    protected boolean _executeProcess() throws Exception {
        super._executeProcess();
        setEMailAddress(email);
        List<TaxationOffice> tos = VulpeculaRepositoryLocator.getTaxationOfficeRepository().findByEtatIn(
                EtatTaxation.VALIDE, EtatTaxation.SAISI);
        sortByNumeroAffilie(tos);
        ListeTOPrevisionnelleExcel excelDoc = new ListeTOPrevisionnelleExcel(getSession(),
                DocumentConstants.LISTES_TO_PREVISIONNELLE_DOC_NAME, DocumentConstants.LISTES_TO_PREVISIONNELLE, tos);
        excelDoc.create();
        registerAttachedDocument(JadePublishDocumentInfoProvider.newInstance(this), excelDoc.getOutputFile());
        return true;
    }

    private void sortByNumeroAffilie(List<TaxationOffice> tos) {
        Collections.sort(tos, new Comparator<TaxationOffice>() {
            @Override
            public int compare(TaxationOffice to1, TaxationOffice to2) {
                if (EtatTaxation.VALIDE.equals(to1.getEtat()) && EtatTaxation.SAISI.equals(to2.getEtat())) {
                    return -1;
                } else if (EtatTaxation.SAISI.equals(to1.getEtat()) && EtatTaxation.VALIDE.equals(to2.getEtat())) {
                    return 1;
                }
                return to1.getEmployeurAffilieNumero().compareTo(to2.getEmployeurAffilieNumero());
            }
        });
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.LISTES_TO_PREVISIONNELLE;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

}
