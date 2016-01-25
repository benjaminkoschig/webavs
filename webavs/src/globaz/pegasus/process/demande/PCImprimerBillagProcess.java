package globaz.pegasus.process.demande;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pegasus.process.PCAbstractJob;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCImprimerBillagProcess extends PCAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut = null;
    private String dateDoc = null;
    private String emailGest = null;
    private String gestionnaire = null;
    private String idDemande = null;

    private String idTiers = null;
    private String nss = null;

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateDoc() {
        return dateDoc;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public String getEmailGest() {
        return emailGest;
    }

    public String getGestionnaire() {
        return gestionnaire;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public String getIdTiers() {
        return idTiers;
    }

    @Override
    public String getName() {
        return null;
    }

    public String getNss() {
        return nss;
    }

    @Override
    protected void process() throws Exception {

        String exceptionMsg = null;

        // Container du document
        try {

            checkInvalidArgument();

            JadePrintDocumentContainer container = PegasusServiceLocator.getDemandeBuilderProvderService()
                    .getBillagBuilder()
                    .buildBillag(idDemande, getDateDoc(), getDateDebut(), getEmailGest(), nss, idTiers, gestionnaire);

            // Appel du service concerné par le type, retourne une instance de JadePrintDocumentContainer
            this.createDocuments(container);

        } catch (Exception ex) {
            this.addError(ex);
            JadeLogger.error(this, ex.getMessage());
            exceptionMsg = ex.getMessage();
        } finally {

            if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
                JadeSmtpClient.getInstance().sendMail(getEmailGest(),
                        getSession().getLabel("IMPRIMER_DECISION_ERREUR"), exceptionMsg, null);
            }

        }

    }

    private void checkInvalidArgument() {
        if (gestionnaire == null || JadeStringUtil.isBlankOrZero(gestionnaire)) {
            throw new IllegalArgumentException("The gestionnaite cannot be null or empty");
        }

    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateDoc(String dateDoc) {
        this.dateDoc = dateDoc;
    }

    public void setEmailGest(String emailGest) {
        this.emailGest = emailGest;
    }

    public void setGestionnaire(String gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

}
