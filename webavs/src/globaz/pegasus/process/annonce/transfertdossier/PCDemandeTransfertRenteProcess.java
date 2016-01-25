package globaz.pegasus.process.annonce.transfertdossier;

import globaz.jade.log.JadeLogger;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.pegasus.process.PCAbstractJob;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCDemandeTransfertRenteProcess extends PCAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateAnnonce = null;
    private String dateTransfert = null;
    private String email = null;
    private String idAgence = null;
    private String idDemandePc = null;
    private String idGestionnaire = null;
    private String noAgence = null;

    public String getDateAnnonce() {
        return dateAnnonce;
    }

    public String getDateTransfert() {
        return dateTransfert;
    }

    @Override
    public String getDescription() {
        return "Process pour générer une demande de transfert de rente PC";
    }

    public String getEmail() {
        return email;
    }

    public String getIdAgence() {
        return idAgence;
    }

    public String getIdDemandePc() {
        return idDemandePc;
    }

    public String getIdGestionnaire() {
        return idGestionnaire;
    }

    @Override
    public String getName() {
        return "PCDemandeTransfertRenteProcess";
    }

    public String getNoAgence() {
        return noAgence;
    }

    @Override
    protected void process() throws Exception {
        try {

            // générer le document
            JadePublishDocumentInfo pubInfo = JadePublishDocumentInfoProvider.newInstance(this);
            JadePrintDocumentContainer container = PegasusServiceLocator.getTransfertRentePCProviderService()
                    .getTransfertBuilder()
                    .build(pubInfo, idDemandePc, email, idGestionnaire, idAgence, noAgence, dateTransfert, dateAnnonce);

            // Imprimer le document
            this.createDocuments(container);

        } catch (Exception e) {
            JadeLogger.error("An error happened while generating the document of a Transfert dossier", e);
        }
        ;
    }

    public void setDateAnnonce(String dateAnnonce) {
        this.dateAnnonce = dateAnnonce;
    }

    public void setDateTransfert(String dateTransfert) {
        this.dateTransfert = dateTransfert;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIdAgence(String idAgence) {
        this.idAgence = idAgence;
    }

    public void setIdDemandePc(String idDemandePc) {
        this.idDemandePc = idDemandePc;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

    public void setNoAgence(String noAgence) {
        this.noAgence = noAgence;
    }

}
