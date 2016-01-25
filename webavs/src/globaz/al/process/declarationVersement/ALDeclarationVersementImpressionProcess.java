package globaz.al.process.declarationVersement;

import globaz.al.process.ALAbsrtactProcess;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.JadeLogger;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Date;
import ch.globaz.al.business.constantes.ALCSDeclarationVersement;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * 
 * @author PTA
 * 
 */
public class ALDeclarationVersementImpressionProcess extends ALAbsrtactProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * date pour le début de la période
     */
    String dateDebut = null;
    /**
     * date de fin pour la période
     */
    String dateFin = null;
    /**
     * date figurant sur le document
     */
    String dateImpression = null;
    /**
     * identifiant du dossier
     */
    String idDossier = null;
    /**
     * numéro de l'affilié
     */
    String numAffilie = null;
    /**
     * Période de la prestation
     */
    private String periodePrestation = null;
    /**
     * texte impôt
     */
    private Boolean textImpot = null;
    /**
     * type de déclaration de versement
     */
    String typeDeclarationVersement = null;

    /**
     * type de document
     */
    String typeDocument = null;

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return the dateImpression
     */
    public String getDateImpression() {
        return dateImpression;
    }

    @Override
    public String getDescription() {
        return JadeI18n.getInstance().getMessage(getSession().getUserInfo().getLanguage(),
                "globaz.al.process.declarationVersement.ALDeclarationVersementImpressionProcess.description");
    }

    /**
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    @Override
    public String getName() {
        return JadeI18n.getInstance().getMessage(getSession().getUserInfo().getLanguage(),
                "globaz.al.process.declarationVersement.ALDeclarationVersementImpressionProcess.name");
    }

    /**
     * @return the numAffilie
     */
    public String getNumAffilie() {
        return numAffilie;
    }

    /**
     * @return the periodePrestation
     */
    public String getPeriodePrestation() {
        return periodePrestation;
    }

    private JadePrintDocumentContainer getSearchDataForType() throws JadeNoBusinessLogSessionError,
            JadeApplicationServiceNotAvailableException, JadeApplicationException, JadePersistenceException {

        JadePrintDocumentContainer docContainer = new JadePrintDocumentContainer();
        // try {
        // traitement déclaration sur demande
        if (JadeStringUtil.equals(getTypeDeclarationVersement(), ALCSDeclarationVersement.DECLA_VERS_DEMANDE, false)) {
            docContainer = ALServiceLocator.getDeclarationsVersementService().getDeclarationsVersementDemande(
                    getIdDossier(), getNumAffilie(), getDateDebut(), getDateFin(), getDateImpression(),
                    getTypeDocument(), textImpot);

        }
        // pour un lot versementDirect, non imposé à la source
        else if (JadeStringUtil.equals(getTypeDeclarationVersement(),
                ALCSDeclarationVersement.DECLA_VERS_DIR_NON_IMP_SOUR, false)) {
            docContainer = ALServiceLocator.getDeclarationsVersementService().getDeclarationsVersementDirect(
                    getDateDebut(), getDateFin(), getDateImpression(), getTypeDocument(), getIdDossier(),
                    getNumAffilie(), getTextImpot());
        }
        // paiements directs imposés à la source
        else if (JadeStringUtil.equals(getTypeDeclarationVersement(),
                ALCSDeclarationVersement.DECLA_VERS_DIR_IMP_SOURCE, false)) {
            docContainer = ALServiceLocator.getDeclarationsVersementService().getDeclarationsImposeSource(
                    getDateDebut(), getDateFin(), getDateImpression(), getTypeDocument(), getIdDossier(),
                    getNumAffilie(), getTextImpot());
        }
        // non actifs à paiement indirects (ccju, ccvd...)
        else if (JadeStringUtil.equals(getTypeDeclarationVersement(), ALCSDeclarationVersement.DECLA_VERS_IND_NON_ACT,
                false)) {
            docContainer = ALServiceLocator.getDeclarationsVersementService()
                    .getDeclarationsNonActifPaiementsIndirects(getDateDebut(), getDateFin(), getDateImpression(),
                            getTypeDocument(), getIdDossier(), getNumAffilie(), getTextImpot());
        }
        // frontaliers à paiements indirect
        else if (JadeStringUtil.equals(getTypeDeclarationVersement(), ALCSDeclarationVersement.DECLA_VERS_IND_FRONT,
                false)) {
            docContainer = ALServiceLocator.getDeclarationsVersementService().getDeclarationsFrontaliers(
                    getDateDebut(), getDateFin(), getDateImpression(), getTypeDocument(), getIdDossier(),
                    getNumAffilie(), getTextImpot());
        }

        return docContainer;
    }

    public Boolean getTextImpot() {
        return textImpot;
    }

    /**
     * @return the typeDeclarationVersement
     */
    public String getTypeDeclarationVersement() {
        return typeDeclarationVersement;
    }

    /**
     * @return the typeDocument
     */
    public String getTypeDocument() {
        return typeDocument;
    }

    @Override
    protected void process() {

        // Envoie d'un mail si problème pour lancer le traitement
        ArrayList<String> emails = new ArrayList<String>();

        JadePrintDocumentContainer docContainer = new JadePrintDocumentContainer();
        boolean errorInProcess = false;

        try {
            docContainer = getSearchDataForType();
        } catch (JadeApplicationServiceNotAvailableException e) {
            errorInProcess = true;
            e.printStackTrace();
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                    new String[] { e.getMessage() });

        } catch (JadeNoBusinessLogSessionError e1) {
            errorInProcess = true;
            e1.printStackTrace();
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                    new String[] { e1.getMessage() });

        } catch (JadeApplicationException e2) {
            errorInProcess = true;
            e2.printStackTrace();
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                    new String[] { e2.getMessage() });

        } catch (JadePersistenceException e3) {
            errorInProcess = true;
            e3.printStackTrace();
            getLogSession().error(this.getClass().getName(), "al.processus.traitement.technical",
                    new String[] { e3.getMessage() });

        }

        JadePublishDocumentInfo docInfo = new JadePublishDocumentInfo();
        docInfo.setOwnerEmail(JadeThread.currentUserEmail());
        docInfo.setOwnerId(JadeThread.currentUserId());
        // titre du document
        docInfo.setDocumentTitle(JadeThread.getMessage("al.declarationVersement.titre.attestation"));
        // Sujet du document
        docInfo.setDocumentSubject(JadeThread.getMessage("al.declarationVersement.titre.attestation"));
        docInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        // pas d'archivage du document
        docInfo.setArchiveDocument(false);
        // publication du document
        docInfo.setPublishDocument(true);

        docContainer.setMergedDocDestination(docInfo);

        try {
            this.createDocuments(docContainer);
        } catch (Exception e) {
            errorInProcess = true;
            JadeLogger.error(this, new Exception("Erreur à l'utilisation de createDocuments"));
            JadeThread.logError(this.getClass().getName() + ".process()", "Erreur à l'utilisation de createDocuments");

            return;

        }

        emails.add(JadeThread.currentUserEmail());
        if (errorInProcess) {
            try {
                sendCompletionMail(emails);
            } catch (Exception e1) {
                JadeLogger.error(this,
                        "Impossible d'envoyer le mail de résultat du traitement. Raison : " + e1.getMessage() + ", "
                                + e1.getCause());
            }
        }

    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @param dateImpression
     *            the dateImpression to set
     */
    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    /**
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param numAffilie
     *            the numAffilie to set
     */
    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    /**
     * @param periodePrestation
     *            the periodePrestation to set
     */
    public void setPeriodePrestation(String periodePrestation) {
        this.periodePrestation = periodePrestation;
    }

    public void setTextImpot(Boolean textImpot) {
        this.textImpot = textImpot;
    }

    /**
     * @param typeDeclarationVersement
     *            the typeDeclarationVersement to set
     */
    public void setTypeDeclarationVersement(String typeDeclarationVersement) {
        this.typeDeclarationVersement = typeDeclarationVersement;
    }

    /**
     * @param typeDocument
     *            the typeDocument to set
     */
    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

}
