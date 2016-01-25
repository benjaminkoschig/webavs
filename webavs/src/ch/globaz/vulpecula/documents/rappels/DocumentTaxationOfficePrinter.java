package ch.globaz.vulpecula.documents.rappels;

import globaz.framework.printing.itext.FWIDocumentManager;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.domain.models.taxationoffice.TaxationOffice;

public class DocumentTaxationOfficePrinter extends DocumentPrinter<TaxationOffice> {
    private static final long serialVersionUID = 7451949163803279932L;

    private Collection<String> ids;

    public DocumentTaxationOfficePrinter() {
        super();
    }

    public DocumentTaxationOfficePrinter(Collection<String> ids) {
        this.ids = ids;
    }

    public Collection<String> getIds() {
        return ids;
    }

    public void setIds(Collection<String> ids) {
        this.ids = ids;
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.TAXATION_OFFICE_TYPE_NUMBER;
    }

    @Override
    public FWIDocumentManager createDocument() throws Exception {
        return new DocumentTaxationOffice(getCurrentElement());
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.TAXATION_OFFICE_SUBJECT;
    }

    @Override
    public void retrieve() {
        List<TaxationOffice> listeTaxation = VulpeculaRepositoryLocator.getTaxationOfficeRepository().findByIdIn(ids);
        for (TaxationOffice taxationOffice : listeTaxation) {
            taxationOffice.setLignes(VulpeculaRepositoryLocator.getLigneTaxationRepository().findByIdTaxationOffice(
                    taxationOffice.getId()));
            taxationOffice.setAdressePrincipale(VulpeculaRepositoryLocator.getAdresseRepository()
                    .findAdressePrioriteCourrierByIdTiers(taxationOffice.getIdTiers()));
        }
        setElements(listeTaxation);
    }

}
