/**
 * 
 */
package globaz.perseus.process.revisiondossier;

import globaz.docinfo.TIDocumentInfoHelper;
import globaz.externe.IPRConstantesExternes;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.perseus.process.PFAbstractJob;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.dossier.Dossier;
import ch.globaz.perseus.business.models.dossier.DossierSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.services.revisiondossier.RevisionDossierFormulaireBuilder;
import ch.globaz.perseus.businessimpl.services.revisiondossier.RevisionDossierLettreAccompagnementBuilder;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * @author PCA
 * 
 */
public class PFRevisionDossierProcess extends PFAbstractJob {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adresseMail = null;
    private String csCaisse = null;
    private String dateRevision = null;
    private boolean hasRevision = false;
    private String isSendToGed = "";

    public String getAdresseMail() {
        return adresseMail;
    }

    public String getCsCaisse() {
        return csCaisse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.perseus.process.PFAbstractJob#process()
     */
    public String getDateRevision() {
        return dateRevision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getDescription()
     */
    @Override
    public String getDescription() {
        return "Révision dossiers PC Familles";
    }

    public String getIsSendToGed() {
        return isSendToGed;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getName()
     */
    @Override
    public String getName() {
        return "Révision dossiers PC Familles";

    }

    @Override
    protected void process() throws Exception {

        try {
            // TopazSystem.getInstance().getDocBuilder().setOpenedDocumentsVisible(true);
            // Déterminer les dossiers à réviser

            DossierSearchModel dossierSearch = new DossierSearchModel();
            dossierSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

            dossierSearch.setBetweenDateRevisionDebut(getDateRevision());
            dossierSearch.setBetweenDateRevisionFin(getDateRevision());

            dossierSearch = PerseusServiceLocator.getDossierService().search(dossierSearch);
            String caisse = "";

            if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(csCaisse)) {
                caisse = CSCaisse.AGENCE_LAUSANNE.toString();
            } else if (CSCaisse.CCVD.getCodeSystem().equals(csCaisse)) {
                caisse = CSCaisse.CCVD.toString();
            }

            JadePrintDocumentContainer container = new JadePrintDocumentContainer();
            for (JadeAbstractModel abstractModel : dossierSearch.getSearchResults()) {

                DocumentData data = new DocumentData();

                Dossier dossier = (Dossier) abstractModel;
                Demande demande = PerseusServiceLocator.getDemandeService().getDerniereDemande(
                        dossier.getDossier().getIdDossier());

                if (null == demande) {
                    continue;
                }

                DecisionSearchModel decisionSearchMode = new DecisionSearchModel();
                decisionSearchMode.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                decisionSearchMode.setForIdDemande(demande.getSimpleDemande().getIdDemande());
                decisionSearchMode.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
                decisionSearchMode.setForCsTypeDecision(CSTypeDecision.OCTROI_COMPLET.getCodeSystem());
                boolean hasOctroiEnCours = false;

                if ((PerseusServiceLocator.getDecisionService().count(decisionSearchMode) != 0)) {
                    hasOctroiEnCours = true;
                }

                if (hasOctroiEnCours && (JadeStringUtil.isEmpty(demande.getSimpleDemande().getDateFin()))
                        && csCaisse.equals(demande.getSimpleDemande().getCsCaisse())
                        && CSEtatDemande.VALIDE.getCodeSystem().equals(demande.getSimpleDemande().getCsEtatDemande())) {

                    if (CSCaisse.CCVD.getCodeSystem().equals(demande.getSimpleDemande().getCsCaisse())) {
                        RevisionDossierLettreAccompagnementBuilder builderLettreAc = new RevisionDossierLettreAccompagnementBuilder();

                        builderLettreAc.setData(data);
                        builderLettreAc.setCsCaisse(csCaisse);
                        builderLettreAc.setDemande(demande);

                        data = builderLettreAc.build();
                        data.addData("isLettreAccompagnement", "TRUE");
                    } else {
                        data.addData("isLettreAccompagnement", "FALSE");
                    }

                    RevisionDossierFormulaireBuilder builder = new RevisionDossierFormulaireBuilder();

                    builder.setData(data);
                    builder.setCsCaisse(csCaisse);
                    builder.setDemande(demande);

                    data = builder.build();

                    hasRevision = true;

                    // Put data dans container et déclaration du jadepublish doc info
                    JadePublishDocumentInfo pubInfos = new JadePublishDocumentInfo();
                    pubInfos.setDocumentTitle(getSession().getLabel("PDF_PF_REVISION_DOSSIER"));
                    pubInfos.setDocumentSubject(getSession().getLabel("PDF_PF_REVISION_DOSSIER"));
                    pubInfos.setOwnerEmail(adresseMail);
                    pubInfos.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, adresseMail);
                    if ("on".equals(isSendToGed)) {
                        pubInfos.setArchiveDocument(true);
                    } else {
                        pubInfos.setArchiveDocument(false);
                    }
                    pubInfos.setDocumentType(IPRConstantesExternes.PCF_REVISION_DOSSIER);
                    pubInfos.setDocumentTypeNumber(IPRConstantesExternes.PCF_REVISION_DOSSIER);
                    pubInfos.setPublishDocument(false);
                    pubInfos.setDocumentDate(dateRevision);
                    TIDocumentInfoHelper.fill(pubInfos, demande.getDossier().getDemandePrestation()
                            .getPersonneEtendue().getTiers().getIdTiers(), getSession(), null, null, null);
                    container.addDocument(data, pubInfos);

                }

            }

            if (!hasRevision) {
                // Message d'erreur pour indiquer que pas de décompte à généré
                String[] param = new String[1];
                param[0] = caisse;

                JadeThread
                        .logWarn(this.getClass().getName(), "perseus.revision.dossier.pasderevisionpourcaisse", param);
            }

            JadePublishDocumentInfo pubInfoDest = new JadePublishDocumentInfo();
            pubInfoDest.setDocumentTitle(getSession().getLabel("PDF_PF_REVISION_DOSSIER") + " " + getDateRevision());
            pubInfoDest.setDocumentSubject(getSession().getLabel("PDF_PF_REVISION_DOSSIER") + " " + getDateRevision());
            pubInfoDest.setOwnerEmail(adresseMail);
            pubInfoDest.setPublishProperty(JadePublishDocumentInfo.MAIL_TO, adresseMail);
            pubInfoDest.setArchiveDocument(false);
            pubInfoDest.setPublishDocument(true);
            container.setMergedDocDestination(pubInfoDest);

            this.createDocuments(container);

        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(),
                    e.getMessage() + System.getProperty("line.separator") + System.getProperty("line.separator")
                            + "Erreur : " + System.getProperty("line.separator") + e.getClass());
            e.printStackTrace();
        }

        if (JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)
                || JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.WARN)) {
            JadeBusinessMessage[] messages = JadeThread.logMessages();
            for (int i = 0; i < messages.length; i++) {
                getLogSession().addMessage(messages[i]);
            }
        }

        if (getLogSession().hasMessages()) {
            List<String> email = new ArrayList<String>();
            email.add(getAdresseMail());
            this.sendCompletionMail(email);
        }
    }

    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    public void setCsCaisse(String csCaisse) {
        this.csCaisse = csCaisse;
    }

    public void setDateRevision(String dateRevision) {
        this.dateRevision = dateRevision;
    }

    public void setIsSendToGed(String isSendToGed) {
        this.isSendToGed = isSendToGed;
    }
}
