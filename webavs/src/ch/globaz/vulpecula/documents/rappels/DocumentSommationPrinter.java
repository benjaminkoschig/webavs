package ch.globaz.vulpecula.documents.rappels;

import globaz.framework.printing.itext.FWIDocumentManager;
import java.util.Collection;
import java.util.List;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.domain.models.decompte.Decompte;

public class DocumentSommationPrinter extends DocumentPrinter<Decompte> {
    private static final long serialVersionUID = 500258916448358149L;

    private Collection<String> ids;

    public DocumentSommationPrinter() {
        super();
    }

    public DocumentSommationPrinter(Collection<String> ids) {
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
        return DocumentConstants.SOMMATION_TYPE_NUMBER;
    }

    @Override
    public FWIDocumentManager createDocument() throws Exception {
        return new DocumentSommation(getCurrentElement());
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.SOMMATION_SUBJECT;
    }

    @Override
    public void retrieve() {
        List<Decompte> liste = VulpeculaRepositoryLocator.getDecompteRepository().findByIdInWithDependencies(ids);
        for (Decompte decompte : liste) {
            decompte.getEmployeur().setAdressePrincipale(
                    VulpeculaRepositoryLocator.getAdresseRepository().findAdressePrioriteCourrierByIdTiers(
                            decompte.getEmployeur().getIdTiers()));

        }
        setElements(liste);
    }

}
