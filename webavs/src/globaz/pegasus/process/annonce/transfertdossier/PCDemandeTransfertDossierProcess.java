package globaz.pegasus.process.annonce.transfertdossier;

import globaz.jade.log.JadeLogger;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.pegasus.process.PCAbstractJob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.transfertDossier.ITransfertDossierBuilder;

public class PCDemandeTransfertDossierProcess extends PCAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String> annexes;
    private ITransfertDossierBuilder builder;
    private List<String> copies;
    private String idDemandePc;
    private Map<String, String> parametres = new HashMap<String, String>();
    private String typeBuilder = null;

    public List<String> getAnnexes() {
        return annexes;
    }

    public ITransfertDossierBuilder getBuilder() {
        return builder;
    }

    public List<String> getCopies() {
        return copies;
    }

    @Override
    public String getDescription() {
        return "Process pour générer une demande de transfert de dossier PC";
    }

    public String getIdDemandePc() {
        return idDemandePc;
    }

    @Override
    public String getName() {
        return "PCDemandeTransfertDossierProcess";
    }

    public Map<String, String> getParametres() {
        return parametres;
    }

    public String getTypeBuilder() {
        return typeBuilder;
    }

    @Override
    protected void process() throws Exception {
        try {
            builder = PegasusServiceLocator.getTransfertDossierPCBuilderProviderService().getBuilderFor(
                    getTypeBuilder());

            builder.loadParameters(parametres, annexes, copies);

            // initialiser le générateur

            // générer le document
            JadePublishDocumentInfo pubInfo = JadePublishDocumentInfoProvider.newInstance(this);
            JadePrintDocumentContainer container = builder.build(pubInfo);

            // Imprimer le document
            this.createDocuments(container);

            // transfert la demande si ce n'est pas encore le cas
            Demande demande = PegasusServiceLocator.getDemandeService().read(idDemandePc);
            if (!IPCDemandes.CS_TRANSFERE.equals(demande.getSimpleDemande().getCsEtatDemande())) {
                demande.getSimpleDemande().setCsEtatDemande(IPCDemandes.CS_TRANSFERE);
                PegasusServiceLocator.getDemandeService().update(demande);
            }

        } catch (Exception e) {
            JadeLogger.error("An error happened while generating the document of a Transfert dossier", e);
        }

    }

    public void setAnnexes(List<String> annexes) {
        this.annexes = annexes;

    }

    public void setBuilder(ITransfertDossierBuilder builder) {
        this.builder = builder;
    }

    public void setCopies(List<String> copies) {
        this.copies = copies;

    }

    public void setIdDemandePc(String idDemandePc) {
        this.idDemandePc = idDemandePc;
    }

    public void setParametres(Map<String, String> parametres) {
        this.parametres = parametres;
    }

    public void setTypeBuilder(String typeBuilder) {
        this.typeBuilder = typeBuilder;
    }

}
