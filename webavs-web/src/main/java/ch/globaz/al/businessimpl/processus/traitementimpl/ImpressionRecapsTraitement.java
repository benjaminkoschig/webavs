package ch.globaz.al.businessimpl.processus.traitementimpl;

import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.processus.BusinessTraitement;
import ch.globaz.al.utils.ALFileCSVUtils;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.message.JadePublishDocumentMessage;

import java.io.File;
import java.util.*;

/**
 * 
 * Traitement implémentant l'impression des récaps
 * 
 * @author GMO
 * 
 */
public class ImpressionRecapsTraitement extends BusinessTraitement {

    /**
     * Conteneur qui sera copié dans le conteneur B
     */
    private JadePrintDocumentContainer containerA = new JadePrintDocumentContainer();
    /**
     * conteneur pour les protocoles
     */
    private JadePrintDocumentContainer containerB = new JadePrintDocumentContainer();
    private JadePrintDocumentContainer[] containers = new JadePrintDocumentContainer[2];

    /**
     * constructeur du traitement
     */
    public ImpressionRecapsTraitement() {
        super();
    }

    @Override
    protected void execute() {

        ArrayList listConteneurRecap = new ArrayList();
        HashMap recapCSV = new HashMap();
        ArrayList listRecapPdf = new ArrayList();
        ArrayList listRecapCsv = new ArrayList();
        ArrayList listConteneurRecapNonPublicationPdf = new ArrayList();

        // liste du résultats de la recherche
        ArrayList listResultatRecherche = new ArrayList();
        //

        // envoi en ged à true dans le traitement
        boolean isGed = true;

        // modèle de recherche
        RecapitulatifEntrepriseImpressionComplexSearchModel recap = new RecapitulatifEntrepriseImpressionComplexSearchModel();

        recap.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        try {

            if (getProcessusConteneur().isPartiel()) {
                listResultatRecherche = ALServiceLocator.getRecapitulatifEntrepriseBusinessService()
                        .resultSearchRecapNumProcessus(getProcessusConteneur().getProcessusPeriodiqueModel().getId());
            } else {

                listResultatRecherche = ALServiceLocator.getRecapitulatifEntrepriseBusinessService().resultSearchRecap(
                        recap, getProcessusConteneur().getDataCriterias().cotisationCriteria,
                        getProcessusConteneur().getDataCriterias().periodeCriteria, ALCSPrestation.ETAT_TR);

            }

        } catch (Exception e) {
            JadeLogger.error(this, new Exception("Erreur à l'utilisation du service resultSearchRecap", e));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur à l'utilisation du service resultSearchRecap");
            return;
        }
        // ------------- PDF + MAIL + GED ----------------
        try {
            // document en pdf
            listRecapPdf = ALServiceLocator.getRecapitulatifEntrepriseImpressionService().loadArrayListDocData(
                    listResultatRecherche);
            if (listRecapPdf.size() != 0) {
                listConteneurRecap = (ALServiceLocator.getRecapitulatifEntrepriseImpressionService().loadDocuments(
                        listRecapPdf, JadeDateUtil.getGlobazFormattedDate(new Date()), isGed));
            }

        } catch (Exception e) {
            JadeLogger.error(this, new Exception("Erreur à l'utilisation du service loadDocuments", e));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur à l'utilisation du service loadDocuments");
            return;

        }

        // création des pdf dont le doc fusionné part en mail
        Iterator iter = listConteneurRecap.iterator();

        // this.container = new JadePrintDocumentContainer();
        while (iter.hasNext()) {
            JadePrintDocumentContainer containerMail = (JadePrintDocumentContainer) iter.next();
            containerA.setMergedDocDestination(getInfoDocFusionnePDFPublie());

            containerMail.copyDocsTo(containerA);

        }
        // ------------- CSV + MAIL ----------------

        try {
            // document CSV
            listRecapCsv = ALServiceLocator.getRecapitulatifEntrepriseImpressionService().loadArrayListCsv(
                    listResultatRecherche);
            if (listRecapCsv.size() != 0) {
                boolean isCharNssRecap = JadePropertiesService.getInstance().getProperty(ALConstParametres.RECAP_FORMAT_NSS).equals("true");
                recapCSV = ALServiceLocator.getRecapitulatifEntrepriseImpressionService().loadCSVDocument(listRecapCsv, isCharNssRecap);

            }
        } catch (Exception e) {
            JadeLogger.error(this, new Exception("Erreur à l'utilisation du service loadCSVDocument", e));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur à l'utilisation du service loadCSVDocument");
            return;

        }

        Set entrees = recapCSV.entrySet();

        Iterator iterCsv = entrees.iterator();

        // création des fichiers CSV
        while (iterCsv.hasNext()) {
            Map.Entry entree = (Map.Entry) iterCsv.next();
            Object cleIdRecap = entree.getKey();
            Object valeurCsv = entree.getValue();

            JadePublishDocumentInfo docInfoCSVs = new JadePublishDocumentInfo();

            docInfoCSVs.setOwnerEmail(JadeThread.currentUserEmail());
            docInfoCSVs.setOwnerId(JadeThread.currentUserId());
            docInfoCSVs.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
            docInfoCSVs.setDocumentTitle(JadeThread.getMessage("al.recapitulatif.titre.impressionFichierCSV.label")
            /* + nomFichier */);
            docInfoCSVs.setDocumentSubject(JadeThread.getMessage("al.recapitulatif.titre.impressionFichierCSV.label"));

            docInfoCSVs.setPublishDocument(true);
            docInfoCSVs.setArchiveDocument(false);

            // nom du fichier CSV
            String nomFichier = ALFileCSVUtils.nomFichier(cleIdRecap);
            // création du fichier CSV
            try {
                ALFileCSVUtils.createFichier(nomFichier, cleIdRecap, valeurCsv);
            } catch (Exception e) {
                JadeLogger.error(this, new Exception("Erreur à l'utilisation du service ALFileCSVUtils.createFichier",
                        e));
                JadeThread.logError(this.getClass().getName() + ".process()",
                        "Erreur à l'utilisation du service ALFileCSVUtils.createFichier");
                return;

            }
            try {
                JadePublishDocument docInfoCSV = new JadePublishDocument(nomFichier, docInfoCSVs);
                JadePublishServerFacade.publishDocument(new JadePublishDocumentMessage(docInfoCSV));

            } catch (Exception e) {
                JadeLogger.error(this, new Exception(
                        "Erreur à l'utilisation des objets JadePublisDocument/JadePublishServerFacade", e));
                JadeThread.logError(this.getClass().getName() + ".process()",
                        "Erreur à l'utilisation des objets JadePublisDocument/JadePublishServerFacade");

                return;

            }

            // supprimer le fichier CSV

            File file = new File(nomFichier);

            file.delete();

        }

        // ------------- CSV + GED (version PDF) ----------------

        // création des pdf dont le doc fusionné ne part pas en impression mais en ged (csv seulement, mais doc pdf en
        // GED)

        try {
            // création des documents data pour les documents dont la fusion des
            // documents ne doit pas être envoyer en ged
            if (listRecapCsv.size() != 0) {
                listConteneurRecapNonPublicationPdf = (ALServiceLocator.getRecapitulatifEntrepriseImpressionService()
                        .loadDocuments(listRecapCsv, JadeDateUtil.getGlobazFormattedDate(new Date()), isGed));
            }

        } catch (Exception e) {
            JadeLogger.error(this, new Exception("Erreur à l'utilisation du service loadDocuments", e));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur à l'utilisation du service loadDocuments");
            return;

        }

        // création des pdf dont le doc fusionné ne part pas en mail
        Iterator iterNoPublishMail = listConteneurRecapNonPublicationPdf.iterator();

        // this.container = new JadePrintDocumentContainer();
        while (iterNoPublishMail.hasNext()) {
            JadePrintDocumentContainer containerNoMail = (JadePrintDocumentContainer) iterNoPublishMail.next();
            containerB.setMergedDocDestination(getInfoDocFusionnePDFNoPublie());
            containerNoMail.copyDocsTo(containerB);

        }
    }

    @Override
    protected void executeBack() {
        // DO NOTHING
    }

    @Override
    public String getCSTraitement() {
        return ALCSProcessus.NAME_TRAITEMENT_IMPRESSION_RECAP;
    }

    /**
     * Méthode qui retourne les infos de publications pour un document fusionnée qui ne part pas en mail
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
     * Méthode qui retourne les infos de publications pour un document fusionné qui part en mail
     * 
     * @return pubInfo infos de publication du document
     */
    private JadePublishDocumentInfo getInfoDocFusionnePDFPublie() {
        JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();

        pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfo.setOwnerId(JadeThread.currentUserId());

        // Titre du document
        String[] periodes = { getProcessusConteneur().getDataCriterias().periodeCriteria };
        pubInfo.setDocumentTitle(JadeThread.getMessage("al.recapitulatif.titre.impression.label", periodes.clone()));

        // Sujet du document
        pubInfo.setDocumentSubject(JadeThread.getMessage("al.recapitulatif.titre.impression.label", periodes.clone()));
        pubInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        pubInfo.setDocumentType("RecapEntreprise01");

        pubInfo.setDocumentTypeNumber(ALConstDocument.DOC_TYPE_NUMBER_INFOROM_RECAP);
        pubInfo.setArchiveDocument(false);
        pubInfo.setPublishDocument(true);
        return pubInfo;

    }

    @Override
    public JadePrintDocumentContainer[] getProtocole() {
        containers[0] = containerA;
        containers[1] = containerB;
        return containers;
    }

    @Override
    public ProtocoleLogger getProtocoleLogger() {
        // Pas de protocole pour ce traitement
        return null;
    }

    @Override
    public JadePublishDocumentInfo getPubInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }
}
