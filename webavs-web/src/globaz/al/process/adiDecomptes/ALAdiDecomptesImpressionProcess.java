package globaz.al.process.adiDecomptes;

import globaz.al.process.ALAbsrtactProcess;
import globaz.docinfo.TIDocumentInfoHelper;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.job.common.JadeJobQueueNames;
import globaz.jade.log.JadeLogger;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Date;
import ch.globaz.al.business.constantes.ALCSTiers;
import ch.globaz.al.business.exceptions.adiDecomptes.ALAdiDecomptesException;
import ch.globaz.al.business.models.adi.AdiDecompteComplexModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * Class de gestion des décomptes ADI à imprimer
 * 
 * @author PTA
 * 
 */
public class ALAdiDecomptesImpressionProcess extends ALAbsrtactProcess {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * période(année) pour le décompte
     */
    private String date = null;

    /**
     * Envoi vers la GED ?
     */
    private boolean envoiGED = false;

    /**
     * identifiant du décompte adi
     */
    private String idDecompteAdi = null;
    /**
     * identifiant du dossier
     */
    private String idDossier = null;
    /**
     * type de décompte
     */
    private String typeDecompte = null;

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    @Override
    public String getDescription() {
        return JadeI18n.getInstance().getMessage(getSession().getUserInfo().getLanguage(),
                "globaz.al.process.adiDecomptes.ALAdiDecomptesImpressionProcess.description");
    }

    /**
     * @return the envoiGED
     */
    public boolean getEnvoiGED() {
        return envoiGED;
    }

    /**
     * @return the idDecompteAdi
     */
    public String getIdDecompteAdi() {
        return idDecompteAdi;
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
                "globaz.al.process.adiDecomptes.ALAdiDecomptesImpressionProcess.name");
    }

    /**
     * @return the typeDecompte
     */
    public String getTypeDecompte() {
        return typeDecompte;
    }

    @Override
    public String jobQueueName() {
        return JadeJobQueueNames.SYSTEM_INTER_JOB_QUEUE;
    }

    /**
     * Méthode qui charge les inforamtion de publication du document
     * 
     * @param isGed
     *            booléen
     * @param adiDecompte
     *            décompte adi
     * @return JadePublishDocumentInfo
     * @throws JadeApplicationServiceNotAvailableException
     *             exception levée
     */
    private JadePublishDocumentInfo loadInfoDocAdi(boolean isGed, AdiDecompteComplexModel adiDecompte)
            throws JadeApplicationServiceNotAvailableException {
        JadePublishDocumentInfo docInfo = new JadePublishDocumentInfo();

        // archivage du document
        docInfo.setArchiveDocument(isGed);
        // publication du document
        docInfo.setPublishDocument(true);
        // type de document
        // FIXME (lot 2) devrait contenir le numéro de document InfoRom
        docInfo.setDocumentType("Adi");
        // numéro de type de document
        // FIXME (lot 2) devrait contenir le numéro de document InfoRom
        docInfo.setDocumentTypeNumber("Adi");
        // propriétaire du document
        docInfo.setOwnerId(JadeThread.currentUserId());
        // email du propriétaire du document
        docInfo.setOwnerEmail(JadeThread.currentUserEmail());
        // date document document
        docInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        // titre du document: sans titre pour pendre valeur par défaut de la ged
        // ti
        docInfo.setDocumentTitle("");
        // sujet du document
        docInfo.setDocumentSubject((JadeThread.getMessage("al.adi.decomptes.titre.attestation")));

        String nssAllocataire = adiDecompte.getDossierComplexModel().getAllocataireComplexModel()
                .getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel();
        String numAffilie = adiDecompte.getDossierComplexModel().getDossierModel().getNumeroAffilie();
        String nomPrenom = adiDecompte.getDossierComplexModel().getAllocataireComplexModel()
                .getPersonneEtendueComplexModel().getTiers().getDesignation1()
                + " "
                + adiDecompte.getDossierComplexModel().getAllocataireComplexModel().getPersonneEtendueComplexModel()
                        .getTiers().getDesignation2();
        // FIXME:bz5857
        docInfo.setPublishProperty(TIDocumentInfoHelper.TIERS_ID, adiDecompte.getDossierComplexModel()
                .getAllocataireComplexModel().getPersonneEtendueComplexModel().getId());
        docInfo.setPublishProperty("numero.role.formatte", numAffilie);
        docInfo.setPublishProperty("numero.affilie.formatte", numAffilie);
        docInfo.setPublishProperty("numero.avs.formatte", nssAllocataire);
        docInfo.setPublishProperty("pyxis.tiers.numero.avs.formatte", nssAllocataire);
        docInfo.setPublishProperty("pyxis.tiers.numero.avs.non.formatte",
                JadeStringUtil.removeChar(nssAllocataire, '.'));
        docInfo.setPublishProperty("numero.role.non.formatte", JadeStringUtil.removeChar(numAffilie, '.'));
        docInfo.setPublishProperty("pyxis.tiers.nom.prenom", nomPrenom);
        // setting des type ou type sous dossier?
        docInfo.setPublishProperty(
                "type.dossier",
                ALServiceLocator.getGedBusinessService().getTypeSousDossier(
                        adiDecompte.getDossierComplexModel().getDossierModel()));

        return docInfo;
    }

    @Override
    protected void process() {
        JadePrintDocumentContainer docContainer = new JadePrintDocumentContainer();

        try {

            // docContainer = ALServiceLocator.getAdiDecomptesService()
            // .getAdiDecompteDossier(this.getIdDecompteAdi(),
            // this.getTypeDecompte(), this.isEnvoiGED());

            AdiDecompteComplexModel adiDecompte = ALServiceLocator.getAdiDecompteComplexModelService().read(
                    idDecompteAdi);

            docContainer = ALServiceLocator.getAdiDecomptesService().getDocuments(adiDecompte, getTypeDecompte(),
                    getEnvoiGED());
            // préparation des données de publication pour le document
            JadePublishDocumentInfo docInfo = new JadePublishDocumentInfo();

            docInfo = loadInfoDocAdi(envoiGED, adiDecompte);

            // numéro affilié non formatté
            String numAffilieNonFormatte = JadeStringUtil.removeChar(adiDecompte.getDossierComplexModel()
                    .getDossierModel().getNumeroAffilie(), '.');

            try {
                TIBusinessServiceLocator.getDocInfoService().fill(
                        docInfo,
                        adiDecompte.getDossierComplexModel().getAllocataireComplexModel().getAllocataireModel()
                                .getIdTiersAllocataire(), null, null, ALCSTiers.ROLE_AF,
                        adiDecompte.getDossierComplexModel().getDossierModel().getNumeroAffilie(),
                        numAffilieNonFormatte, null);
            } catch (Exception e) {
                throw new ALAdiDecomptesException(
                        "ALAdiDecompteImpressionProcess#process : unable to fill DocInfoService", e);
            }

            // informations de publication pour le document fusionné
            docContainer.setMergedDocDestination(docInfo);

        } catch (Exception e) {
            JadeLogger.error(this, new Exception("Erreur à l'utilisation du service getAdiDecomptesDossier", e));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur à l'utilisation du service getAdiDecomptesDossier");
            return;
        }

        try {
            this.createDocuments(docContainer);
        } catch (Exception e) {
            JadeLogger.error(this,
                    "Une erreur s'est produite pendant l'impression des décomptes. Raison : " + e.getMessage() + "\n"
                            + e.getCause());
        }

    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @param envoiGED
     *            the envoiGED to set
     */
    public void setEnvoiGED(boolean envoiGED) {
        this.envoiGED = envoiGED;
    }

    /**
     * @param idDecompteAdi
     *            the idDecompteAdi to set
     */
    public void setIdDecompteAdi(String idDecompteAdi) {
        this.idDecompteAdi = idDecompteAdi;
    }

    /**
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param typeDecompte
     *            the typeDecompte to set
     */
    public void setTypeDecompte(String typeDecompte) {
        this.typeDecompte = typeDecompte;
    }
}
