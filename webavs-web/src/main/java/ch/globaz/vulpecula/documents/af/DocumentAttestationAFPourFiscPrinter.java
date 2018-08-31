package ch.globaz.vulpecula.documents.af;

import globaz.framework.printing.itext.FWIDocumentManager;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.businessimpl.services.is.PrestationGroupee;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.domain.models.common.Annee;

public class DocumentAttestationAFPourFiscPrinter extends DocumentPrinter<PrestationGroupee> {
    private static final long serialVersionUID = 3721912632239314835L;

    private Annee annee;

    public DocumentAttestationAFPourFiscPrinter() {
        super();
    }

    public DocumentAttestationAFPourFiscPrinter(Annee annee) {
        this.annee = annee;
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.ATTESTATION_AF_FISC_SUBJECT;
    }

    @Override
    public FWIDocumentManager createDocument() throws Exception {
        return new DocumentAttestationAFPourFisc(getCurrentElement(), getAnnee());
    }

    public Annee getAnnee() {
        return annee;
    }

    public void setAnnee(Annee annee) {
        this.annee = annee;
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.ATTESTATION_AF_FISC_TYPE_NUMBER;
    }

    @Override
    public void retrieve() {
        setElements(VulpeculaServiceLocator.getImpotSourceService().getPrestationsForAllocNonIS(annee));
    }
}
