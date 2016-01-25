package globaz.pegasus.process.demanderenseignement;

import globaz.jade.log.JadeLogger;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.document.JadePublishDocumentInfoProvider;
import globaz.pegasus.process.PCAbstractJob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.topazbuilder.demanderenseignement.AbstractDemandeRenseignementBuilder;

public class PCDemandeRenseignementProcess extends PCAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Map<String, List<String>> builderParameters = new HashMap<String, List<String>>();
    private String csTypeDemande = null;

    private String email = null;

    private String idDemandePc = null;
    private String idGestionnaire = null;

    public Map<String, List<String>> getBuilderParameters() {
        return builderParameters;
    }

    public String getCsTypeDemande() {
        return csTypeDemande;
    }

    @Override
    public String getDescription() {
        return "Process pour générer une demande de transfert de rente PC";
    }

    public String getEmail() {
        return email;
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

    @Override
    protected void process() throws Exception {
        try {

            AbstractDemandeRenseignementBuilder demandeBuilder = PegasusServiceLocator
                    .getDemandeRenseignementProviderService().getDemandeBuilder(csTypeDemande);

            // demandeBuilder.loadParameters(this.builderParameters);

            demandeBuilder.loadParameters(builderParameters);
            // générer le document
            JadePublishDocumentInfo pubInfo = JadePublishDocumentInfoProvider.newInstance(this);
            JadePrintDocumentContainer container = demandeBuilder.build(pubInfo, idDemandePc, email, idGestionnaire);

            // Imprimer le document
            this.createDocuments(container);

        } catch (Exception e) {
            JadeLogger.error("An error happened while generating the document of a Demande Renseignement!", e);
        }
        ;
    }

    public void setBuilderParameters(Map<String, List<String>> builderParameters) {
        this.builderParameters = builderParameters;
    }

    public void setCsTypeDemande(String csTypeDemande) {
        this.csTypeDemande = csTypeDemande;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIdDemandePc(String idDemandePc) {
        this.idDemandePc = idDemandePc;
    }

    public void setIdGestionnaire(String idGestionnaire) {
        this.idGestionnaire = idGestionnaire;
    }

}
