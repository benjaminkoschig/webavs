package globaz.al.process.recapitulatifsEntreprises;

import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.utils.ALFileCSVUtils;
import globaz.al.process.ALAbsrtactProcess;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.job.common.JadeJobQueueNames;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.message.JadePublishDocumentMessage;

import java.io.File;
import java.util.*;

/**
 * Classe de gestion des r�capitulatifs des entreprises (affili�) � imprimer
 * 
 * @author PTA
 * 
 */
public class ALRecapitulatifEntreprisesImprimerProcess extends ALAbsrtactProcess {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * activit� allocataire
     */
    private HashSet activiteAllocSalarie = new HashSet();

    /**
     * date � laquelle il faut imprimer les r�cap
     */
    private String dateImpression = null;
    /**
     * Envoi vers la GED ?
     */
    private boolean envoiGED = false;

    /**
     * �tat de la r�cap
     */
    private String etatRecap = null;

    /**
     * identifiant de la recap
     */
    private String idRecap = null;
    /**
     * type de paiement
     */
    private boolean isPaiementDirect = false;
    /**
     * num�ro de lot pour une r�cap
     */
    private String noLot = null;
    /**
     * p�riode de la recapitulation
     */
    private String periodeRecap = null;

    /**
     * type de traitement recap impression
     * 
     */
    private String typeTraitRecapImpr = null;

    private boolean isCharNssRecap = true;

    /**
     * @return the activiteAllocParitaire
     */
    public HashSet getActiviteAllocParitaire() {
        return activiteAllocSalarie;
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
                "globaz.al.process.recapitulatifsEntreprises.ALRecapitulatifEntreprisesImprimerProcess.description");
    }

    /**
     * @return the envoiGED
     */
    public boolean getEnvoiGED() {
        return envoiGED;
    }

    /**
     * @return the etatRecap
     */
    public String getEtatRecap() {
        return etatRecap;
    }

    /**
     * @return isCharNssRecap
     */
    public boolean getIsCharNssRecap() {
        return this.isCharNssRecap;
    }
    /**
     * @return the idRecap
     */
    public String getIdRecap() {
        return idRecap;
    }

    /**
     * M�thode qui retourne les infos de publications pour un document fusionn�e qui ne part pas en mail
     * 
     * @return pubInfo infos de publication du document
     */

    private JadePublishDocumentInfo getInfoDocFusionnePDFNoPublie() {
        JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();
        pubInfo.setArchiveDocument(false);
        pubInfo.setPublishDocument(false);
        return pubInfo;
    }

    /**
     * M�thode qui retourne les infos de publications pour un document fusionn� qui part en mail
     * 
     * @return pubInfo infos de publication du document
     */
    private JadePublishDocumentInfo getInfoDocFusionnePDFPublie() {
        JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();

        pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfo.setOwnerId(JadeThread.currentUserId());

        String[] periodes = { getPeriodeRecap() };
        // Titre du document
        pubInfo.setDocumentTitle(JadeThread.getMessage("al.recapitulatif.titre.impression.label", periodes));

        // Sujet du document
        pubInfo.setDocumentSubject(JadeThread.getMessage("al.recapitulatif.titre.impression.label", periodes));

        pubInfo.setDocumentType("RecapEntreprise01");
        pubInfo.setDocumentDate(getDateImpression());

        pubInfo.setDocumentTypeNumber(ALConstDocument.DOC_TYPE_NUMBER_INFOROM_RECAP);
        pubInfo.setArchiveDocument(false);
        pubInfo.setPublishDocument(true);
        return pubInfo;

    }

    /**
     * @return the isPaiementDirect
     */
    public boolean getIsPaiementDirect() {
        return isPaiementDirect;
    }

    @Override
    public String getName() {
        return JadeI18n.getInstance().getMessage(getSession().getUserInfo().getLanguage(),
                "globaz.al.process.recapitulatifsEntreprises.ALRecapitulatifEntreprisesImprimerProcess.name");
    }

    /**
     * @return the noLot
     */
    public String getNoLot() {
        return noLot;
    }

    /**
     * @return the periodeRecap
     */
    public String getPeriodeRecap() {
        return periodeRecap;
    }

    /**
     * @return the type of traitement for impression recap
     */
    public String getTypeTraitRecapImpr() {
        return typeTraitRecapImpr;
    }

    @Override
    public String jobQueueName() {
        return JadeJobQueueNames.SYSTEM_INTER_JOB_QUEUE;
    }

    @Override
    protected void process() {
        // liste des conteneurs dont le document fusionn� est publi� par mail
        ArrayList listConteneurRecapPublicationPdf = new ArrayList();
        // liste des conteneurs dont le document fusionn� n'est pas publi� par
        // mail
        ArrayList listConteneurRecapNonPublicationPdf = new ArrayList();
        // liste des recap devant �tre envoy� par mail au format .pdf
        ArrayList listRecapPdf = new ArrayList();
        // liste des recap devatn �tre envoy� par mail au format .csv
        ArrayList listRecapCsv = new ArrayList();

        HashMap recapCSV = new HashMap();

        // liste du r�sultats de la recherche
        ArrayList listResultatRecherche = new ArrayList();

        // mod�le de recherche
        RecapitulatifEntrepriseImpressionComplexSearchModel recap = new RecapitulatifEntrepriseImpressionComplexSearchModel();

        recap.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        try {

            // recherche recap par type
            if (JadeStringUtil.equals(getTypeTraitRecapImpr(), ALConstPrestations.TYPE_COT_PAR, false)
                    || /*
                        * JadeStringUtil.equals(this.getTypeTraitRecapImpr(), ALConstPrestations.TYPE_COT_PERS, false)
                        * ||
                        */JadeStringUtil.equals(getTypeTraitRecapImpr(), ALConstPrestations.TYPE_DIRECT, false)) {
                listResultatRecherche = ALServiceLocator.getRecapitulatifEntrepriseBusinessService().resultSearchRecap(
                        recap, getTypeTraitRecapImpr(), getPeriodeRecap(), getEtatRecap());
            }
            // recherche par un num�ro de lot
            else if (JadeStringUtil.equals(getTypeTraitRecapImpr(), ALConstPrestations.TRAITEMENT_NO_LOT, false)) {
                listResultatRecherche = ALServiceLocator.getRecapitulatifEntrepriseBusinessService().resultSearchRecap(
                        getNoLot());

            }
            // recherche par num�ro de r�cap
            else if (JadeStringUtil.equals(getTypeTraitRecapImpr(), ALConstPrestations.TRAITEMENT_NO_RECAP, false)) {
                listResultatRecherche = ALServiceLocator.getRecapitulatifEntrepriseBusinessService()
                        .resultSearchRecapNumRecap(getIdRecap());
                // pas de type de traitement correspondant
            } else {
                JadeLogger.error(this, new Exception("Erreur: ce type de traitement n'est pas valable"));
                JadeThread.logError(this.getClass().getName() + ".process()",
                        "Erreur: ce type de traitement n'est pas valable");
                return;
            }
        } catch (Exception e) {
            JadeLogger.error(this, new Exception("Erreur � l'utilisation du service resultSearchRecap", e));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur � l'utilisation du service resultSearchRecap");
            return;
        }

        try {
            // documents en pdf qui doivent �tre archiv� en ged et publi� par
            // mail en .pdf
            listRecapPdf = ALServiceLocator.getRecapitulatifEntrepriseImpressionService().loadArrayListDocData(
                    listResultatRecherche);
            if (listRecapPdf.size() != 0) {
                listConteneurRecapPublicationPdf = (ALServiceLocator.getRecapitulatifEntrepriseImpressionService()
                        .loadDocuments(listRecapPdf, getDateImpression(), envoiGED));
            }

        } catch (Exception e) {
            JadeLogger.error(this, new Exception("Erreur � l'utilisation du service loadDocuments", e));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur � l'utilisation du service loadDocuments");
            return;

        }

        // traitement des documents � publier en CSV

        try {
            // publication document CSV
            listRecapCsv = ALServiceLocator.getRecapitulatifEntrepriseImpressionService().loadArrayListCsv(
                    listResultatRecherche);
            if (listRecapCsv.size() != 0) {
                recapCSV = ALServiceLocator.getRecapitulatifEntrepriseImpressionService().loadCSVDocument(listRecapCsv, getIsCharNssRecap());

            }
        } catch (Exception e) {
            JadeLogger.error(this, new Exception("Erreur � l'utilisation du service loadCSVDocument", e));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur � l'utilisation du service loadCSVDocument");
            return;

        }

        Set entrees = recapCSV.entrySet();

        Iterator iterCsv = entrees.iterator();

        // cr�ation des fichiers CSV
        while (iterCsv.hasNext()) {
            Map.Entry entree = (Map.Entry) iterCsv.next();
            Object cleIdRecap = entree.getKey();
            Object valeurCsv = entree.getValue();

            // nom du fichier CSV
            String nomFichier = ALFileCSVUtils.nomFichier(cleIdRecap);

            JadePublishDocumentInfo docInfoCSVs = new JadePublishDocumentInfo();

            docInfoCSVs.setOwnerEmail(JadeThread.currentUserEmail());
            docInfoCSVs.setOwnerId(JadeThread.currentUserId());
            docInfoCSVs.setDocumentTitle(JadeThread.getMessage("al.recapitulatif.titre.impressionFichierCSV.label")
            /* + nomFichier */);
            docInfoCSVs.setDocumentSubject(JadeThread.getMessage("al.recapitulatif.titre.impressionFichierCSV.label")
                    + " NoRecap" + new StringBuffer().append(idRecap));
            docInfoCSVs.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
            docInfoCSVs.setPublishDocument(true);
            docInfoCSVs.setArchiveDocument(false);

            // cr�ation du fichier CSV
            try {
                ALFileCSVUtils.createFichier(nomFichier, cleIdRecap, valeurCsv);
            } catch (Exception e) {
                JadeLogger.error(this, new Exception("Erreur � l'utilisation du service ALFileCSVUtils.createFichier",
                        e));
                JadeThread.logError(this.getClass().getName() + ".process()",
                        "Erreur � l'utilisation du service ALFileCSVUtils.createFichier");
                return;

            }
            try {
                JadePublishDocument docInfoCSV = new JadePublishDocument(nomFichier, docInfoCSVs);
                JadePublishServerFacade.publishDocument(new JadePublishDocumentMessage(docInfoCSV));

            } catch (Exception e) {
                JadeLogger.error(this, new Exception(
                        "Erreur � l'utilisation des objets JadePublisDocument/JadePublishServerFacade", e));
                JadeThread.logError(this.getClass().getName() + ".process()",
                        "Erreur � l'utilisation des objets JadePublisDocument/JadePublishServerFacade");

                return;

            }

            // supprimer le fichier CSV

            File file = new File(nomFichier);

            file.delete();

        }

        // cr�ation des pdf devant �tre publi�s dans un document global
        Iterator iter = listConteneurRecapPublicationPdf.iterator();

        JadePrintDocumentContainer containerMail = new JadePrintDocumentContainer();

        while (iter.hasNext()) {
            /* JadePrintDocumentContainer */containerMail = (JadePrintDocumentContainer) iter.next();
            containerMail.setMergedDocDestination(getInfoDocFusionnePDFPublie());

            try {
                this.createDocuments(containerMail);
            } catch (Exception e) {
                JadeLogger.error(this, new Exception("Erreur � l'utilisation de createDocuments", e));
                JadeThread.logError(this.getClass().getName() + ".process()",
                        "Erreur � l'utilisation de createDocuments");
                return;

            }
        }

        // cr�ation des pdf dont le document global ne doit pas �tre publi�
        // (uniqement archivage des pdf en ged)

        try {
            // documents pdf � archiver(en ged), mais pas � publier cas de tous
            // les
            // documents publi�e en csv
            if (listRecapCsv.size() != 0) {
                listConteneurRecapNonPublicationPdf = ALServiceLocator.getRecapitulatifEntrepriseImpressionService()
                        .loadDocuments(listRecapCsv, dateImpression, envoiGED);
            }

        } catch (Exception e) {
            JadeLogger.error(this, new Exception("Erreur � l'utilisation du service loadDocuments", e));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur � l'utilisation du service loadDocuments");
            return;

        }

        Iterator iterConteneurRecapNonPubliPdf = listConteneurRecapNonPublicationPdf.iterator();

        JadePrintDocumentContainer containerNonMail = new JadePrintDocumentContainer();
        while (iterConteneurRecapNonPubliPdf.hasNext()) {

            /* JadePrintDocumentContainer */containerNonMail = (JadePrintDocumentContainer) iterConteneurRecapNonPubliPdf
                    .next();
            containerNonMail.setMergedDocDestination(getInfoDocFusionnePDFNoPublie());

            try {
                this.createDocuments(containerNonMail);
            } catch (Exception e) {
                JadeLogger.error(this, new Exception("Erreur � l'utilisation de createDocuments"));
                JadeThread.logError(this.getClass().getName() + ".process()",
                        "Erreur � l'utilisation de createDocuments");
                return;

            }
        }

    }

    /**
     * @param activiteAllocParitaire
     *            the activiteAllocParitaire to set
     */
    public void setActiviteAllocParitaire(HashSet activiteAllocParitaire) {
        activiteAllocSalarie = activiteAllocParitaire;
    }

    /**
     * @param dateImpression
     *            the dateImpression to set
     */
    public void setDateImpression(String dateImpression) {
        this.dateImpression = dateImpression;
    }

    /**
     * @param envoiGED
     *            the envoiGED to set
     */
    public void setEnvoiGED(boolean envoiGED) {
        this.envoiGED = envoiGED;
    }

    /**
     * @param etatRecap
     *            the etatRecap to set
     */
    public void setEtatRecap(String etatRecap) {
        this.etatRecap = etatRecap;
    }

    /**
     * @param idRecap
     *            the idRecap to set
     */
    public void setIdRecap(String idRecap) {
        this.idRecap = idRecap;
    }

    /**
     * @param isPaiementDirect
     *            the isPaiementDirect to set
     */
    public void setIsPaiementDirect(boolean isPaiementDirect) {
        this.isPaiementDirect = isPaiementDirect;
    }

    /**
     * @param noLot
     *            the noLot to set
     */
    public void setNoLot(String noLot) {
        this.noLot = noLot;
    }

    /**
     * @param periodeRecap
     *            the periodeRecap to set
     */
    public void setPeriodeRecap(String periodeRecap) {
        this.periodeRecap = periodeRecap;
    }

    /**
     * @param typeTraitRecapImpr
     *            the type of traitement for impression rcap to set
     */
    public void setTypeTraitRecapImpr(String typeTraitRecapImpr) {
        this.typeTraitRecapImpr = typeTraitRecapImpr;
    }

    public void setIsCharNssRecap(boolean isCharNssRecap) {
        // TODO Auto-generated method stub
        this.isCharNssRecap = isCharNssRecap;
        
    }

}
