package globaz.perseus.process.decision;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.perseus.business.constantes.CSTypeLot;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.qd.Facture;
import ch.globaz.perseus.business.models.rentepont.FactureRentePont;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.doc.excel.impl.PFStatsDecisionFactureAgence;
import ch.globaz.perseus.businessimpl.services.facture.ImprimerDecisionFactureBuilder;
import ch.globaz.perseus.businessimpl.services.facture.ImprimerDecisionFactureRPBuilder;
import ch.globaz.perseus.businessimpl.utils.PFTypeImpressionEnum;
import globaz.globall.db.BSessionUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.perseus.process.PFAbstractJob;

public class PFImprimerDecisionFactureProcess extends PFAbstractJob {
    private static final long serialVersionUID = -4606095961880814539L;

    private String adrMail = null;
    private String caisse = null;
    private String dateDocument = null;
    private String idLot = null;
    private List<String> idUserAgence = new ArrayList<String>();
    private boolean isAgence = false;
    private String isSendToGed = "";

    public PFImprimerDecisionFactureProcess() throws Exception {
    }

    private void generateDocument(ImprimerDecisionFactureBuilder builder, PFTypeImpressionEnum typeImpression)
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {
        JadePrintDocumentContainer container = builder.build(idUserAgence, getIsAgence(), typeImpression);
        this.createDocuments(container);
    }

    private void generateDocumentRP(ImprimerDecisionFactureRPBuilder builder, PFTypeImpressionEnum typeImpression)
            throws DecisionException, JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception {
        JadePrintDocumentContainer container = builder.build(typeImpression);
        this.createDocuments(container);
    }

    /**
     * 
     * @param listeDecomptes
     * @throws JadeNoBusinessLogSessionError
     */
    private void genererStatistiquesPourLesAgences(Map<String, List<Facture>> listeDecomptes)
            throws JadeNoBusinessLogSessionError {
        try {
            PFStatsDecisionFactureAgence stats = new PFStatsDecisionFactureAgence();
            stats.setDonnesPourStatsAgence(idUserAgence, listeDecomptes, getLot().getSimpleLot().getDescription(),
                    getDateDocument(), adrMail);

            String body = BSessionUtil.getSessionFromThreadContext().getLabel("PROCESS_STATS_AGENCE_BODY");

            List<String> listeStatistiquesAGenerer = new ArrayList<String>();
            listeStatistiquesAGenerer.add(stats.createGeneralDocAndSave());

            for (String unFichierStatsAgence : stats.createStatsParAgenceAndSave().toArray(new String[0])) {
                listeStatistiquesAGenerer.add(unFichierStatsAgence);
            }

            JadeSmtpClient.getInstance().sendMail(adrMail,
                    BSessionUtil.getSessionFromThreadContext().getLabel("PROCESS_STATS_AGENCE_TITRE_MAIL"), body,
                    listeStatistiquesAGenerer.toArray(new String[0]));
        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(), "perseus.process.decision.PFImprimerDecisionFactureProcess");
            JadeLogger.error(this, e.getMessage());
        }
    }

    public String getAdrMail() {
        return adrMail;
    }

    public String getCaisse() {
        return caisse;
    }

    public String getDateDocument() {
        return dateDocument;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public String getIdLot() {
        return idLot;
    }

    public List<String> getIdUserAgence() {
        return idUserAgence;
    }

    public boolean getIsAgence() {
        return isAgence;
    }

    /**
     * @return the isSendToGed
     */
    public String getIsSendToGed() {
        return isSendToGed;
    }

    // Methode permettant de recupérer le lot d'après son id
    private Lot getLot() throws Exception {
        Lot lot = new Lot();
        lot = PerseusServiceLocator.getLotService().read(idLot);
        return lot;
    }

    @Override
    public String getName() {
        return toString();
    }

    public ImprimerDecisionFactureBuilder newBuilder(Map<String, List<Facture>> listeDecomptes) {
        ImprimerDecisionFactureBuilder builder = new ImprimerDecisionFactureBuilder();
        builder.setAdrMail(adrMail);
        builder.setDateDocument(getDateDocument());
        builder.setCsCaisse(getCaisse());
        builder.setListeDecomptes(listeDecomptes);

        if ("on".equals(getIsSendToGed())) {
            builder.setSendToGed(true);
        }

        return builder;
    }

    public ImprimerDecisionFactureRPBuilder newBuilderRP(Map<String, List<FactureRentePont>> listeDecomptes) {
        ImprimerDecisionFactureRPBuilder builderRP = new ImprimerDecisionFactureRPBuilder();
        builderRP.setAdrMail(adrMail);
        builderRP.setDateDocument(getDateDocument());
        builderRP.setCsCaisse(getCaisse());
        builderRP.setListeDecomptes(listeDecomptes);

        if ("on".equals(getIsSendToGed())) {
            builderRP.setSendToGed(true);
        }

        return builderRP;
    }

    @Override
    protected void process() throws Exception {
        try {
            if (CSTypeLot.LOT_FACTURES.getCodeSystem().equals(getLot().getSimpleLot().getTypeLot())) {
                // Clé pour les décomptes PCF
                Map<String, List<Facture>> listeDecomptes = PerseusServiceLocator.getPmtFactureService()
                        .groupListFactureByMembreFamille(PerseusServiceLocator.getPrestationService(), getLot(),
                                idUserAgence, getIsAgence());

                generateDocument(newBuilder(listeDecomptes), PFTypeImpressionEnum.ORIGINAUX);
                generateDocument(newBuilder(listeDecomptes), PFTypeImpressionEnum.COPIES_AUX_REQUERANTS);

                // Statistiques pour les agences
                if (getIsAgence()) {
                    genererStatistiquesPourLesAgences(listeDecomptes);
                }

            } else {
                // Clé pour les décomptes RP
                Map<String, List<FactureRentePont>> listeDecomptesRP = PerseusServiceLocator
                        .getPmtFactureRentePontService().groupListFactureByDossierRP(getLot());

                generateDocumentRP(newBuilderRP(listeDecomptesRP), PFTypeImpressionEnum.ORIGINAUX);
                generateDocumentRP(newBuilderRP(listeDecomptesRP), PFTypeImpressionEnum.COPIES);
                generateDocumentRP(newBuilderRP(listeDecomptesRP), PFTypeImpressionEnum.COPIES_AUX_REQUERANTS);
            }
        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(),
                    e.getMessage() + System.getProperty("line.separator") + System.getProperty("line.separator")
                            + "Erreur : " + System.getProperty("line.separator") + e.getClass());
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)
                || JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
            JadeBusinessMessage[] messages = JadeThread.logMessages();
            for (JadeBusinessMessage message : messages) {
                getLogSession().addMessage(message);
            }
        }
        if (getLogSession().hasMessages()) {
            List<String> email = new ArrayList<String>();
            email.add(getAdrMail());
            this.sendCompletionMail(email);
        }

    }

    public void setAdrMail(String adrMail) {
        this.adrMail = adrMail;
    }

    public void setCaisse(String caisse) {
        this.caisse = caisse;
    }

    public void setDateDocument(String dateDocument) {
        this.dateDocument = dateDocument;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public void setIdUserAgence(List<String> userAgence) {
        idUserAgence = userAgence;
    }

    public void setIsAgence(boolean isAgence) {
        this.isAgence = isAgence;
    }

    /**
     * @param isSendToGed
     *            the isSendToGed to set
     */
    public void setIsSendToGed(String isSendToGed) {
        this.isSendToGed = isSendToGed;
    }
}
