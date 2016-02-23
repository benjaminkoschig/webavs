package ch.globaz.vulpecula.documents.af;

import globaz.framework.printing.itext.FWIDocumentManager;
import ch.globaz.vulpecula.business.services.VulpeculaServiceLocator;
import ch.globaz.vulpecula.businessimpl.services.is.PrestationGroupee;
import ch.globaz.vulpecula.documents.DocumentConstants;
import ch.globaz.vulpecula.documents.catalog.DocumentPrinter;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.is.TauxImpositionNotFoundException;
import ch.globaz.vulpecula.util.ExceptionsUtil;

public class DocumentAttestationAFPrinter extends DocumentPrinter<PrestationGroupee> {
    private static final long serialVersionUID = 3721912632239314835L;

    private String idAllocataire;
    private String dateDebut;
    private String dateFin;

    public DocumentAttestationAFPrinter() {
        super();
    }

    public DocumentAttestationAFPrinter(String idAllocataire, String dateDebut, String dateFin) {
        this.idAllocataire = idAllocataire;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public String getIdAllocataire() {
        return idAllocataire;
    }

    public void setIdAllocataire(String idAllocataire) {
        this.idAllocataire = idAllocataire;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public FWIDocumentManager createDocument() throws Exception {
        return new DocumentAttestationAF(getCurrentElement());
    }

    @Override
    protected String getEMailObject() {
        return DocumentConstants.ATTESTATION_AF_SUBJECT;
    }

    @Override
    public String getNumeroInforom() {
        return DocumentConstants.ATTESTATION_AF_TYPE_NUMBER;
    }

    @Override
    public void retrieve() {
        Date dateFinLastDay = new Date(dateFin);

        try {
            setElements(VulpeculaServiceLocator.getImpotSourceService().getPrestationsForAllocIS(idAllocataire,
                    new Date(dateDebut), dateFinLastDay.getLastDayOfMonth()));
        } catch (TauxImpositionNotFoundException e) {
            getTransaction().addErrors(ExceptionsUtil.translateException(e));
        }
    }
}
