package ch.globaz.al.businessimpl.processus.traitementimpl;

import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.processus.BusinessTraitement;
import ch.globaz.al.utils.ALFileCSVUtils;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.message.JadePublishDocumentMessage;

import java.io.File;
import java.util.*;

public class ImpressionRecapsProvisoiresTraitement extends BusinessTraitement {

    private JadePrintDocumentContainer[] containers = new JadePrintDocumentContainer[1];

    @Override
    protected void execute() throws JadePersistenceException, JadeApplicationException {
        ArrayList listConteneurRecap = new ArrayList();
        HashMap recapCSV = new HashMap();
        ArrayList listRecapPdf = new ArrayList();
        ArrayList listRecapCsv = new ArrayList();
        // liste du résultats de la recherche
        ArrayList listResultatRecherche = new ArrayList();
        //

        // envoi en ged à true dans le traitement
        boolean isGed = false;

        // modèle de recherche
        RecapitulatifEntrepriseImpressionComplexSearchModel recap = new RecapitulatifEntrepriseImpressionComplexSearchModel();

        recap.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        try {

            if (getProcessusConteneur().isPartiel()) {
                listResultatRecherche = ALServiceLocator.getRecapitulatifEntrepriseBusinessService()
                        .resultSearchRecapNumProcessus(getProcessusConteneur().getProcessusPeriodiqueModel().getId());
            } else {

                String typeRecap = "";
                if (ALConstPrestations.TYPE_INDIRECT_GROUPE
                        .equals(getProcessusConteneur().getDataCriterias().cotisationCriteria)) {
                    typeRecap = ALConstPrestations.TYPE_INDIRECT_GROUPE;
                }

                if (ALConstPrestations.TYPE_COT_PAR
                        .equals(getProcessusConteneur().getDataCriterias().cotisationCriteria)) {
                    typeRecap = ALConstPrestations.TYPE_COT_PAR;
                }

                if (ALConstPrestations.TYPE_COT_PERS
                        .equals(getProcessusConteneur().getDataCriterias().cotisationCriteria)) {
                    typeRecap = ALConstPrestations.TYPE_COT_PERS;
                }

                if (ALConstPrestations.TYPE_DIRECT
                        .equals(getProcessusConteneur().getDataCriterias().cotisationCriteria)) {
                    typeRecap = ALConstPrestations.TYPE_DIRECT;
                }

                listResultatRecherche = ALServiceLocator.getRecapitulatifEntrepriseBusinessService().resultSearchRecap(
                        recap, typeRecap, getProcessusConteneur().getDataCriterias().periodeCriteria,
                        ALCSPrestation.ETAT_SA);
            }

        } catch (Exception e) {
            JadeLogger.error(this, new Exception("Erreur à l'utilisation du service resultSearchRecap", e));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur à l'utilisation du service resultSearchRecap");
            return;
        }

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

        try {
            // document CSV
            listRecapCsv = ALServiceLocator.getRecapitulatifEntrepriseImpressionService().loadArrayListCsv(
                    listResultatRecherche);
            if (listRecapCsv.size() != 0) {
                recapCSV = ALServiceLocator.getRecapitulatifEntrepriseImpressionService().loadCSVDocument(listRecapCsv, true);

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

        // création des pdf dont le doc fusionné part en mail
        Iterator iter = listConteneurRecap.iterator();
        JadePrintDocumentContainer mainContainer = new JadePrintDocumentContainer();
        // this.container = new JadePrintDocumentContainer();
        while (iter.hasNext()) {
            JadePrintDocumentContainer containerMail = (JadePrintDocumentContainer) iter.next();
            containerMail.copyDocsTo(mainContainer);

        }

        JadePublishDocumentInfo pubInfo = new JadePublishDocumentInfo();

        pubInfo.setOwnerEmail(JadeThread.currentUserEmail());
        pubInfo.setOwnerId(JadeThread.currentUserId());
        pubInfo.setDocumentDate(JadeDateUtil.getGlobazFormattedDate(new Date()));
        // Titre du document
        String[] periodes = { getProcessusConteneur().getDataCriterias().periodeCriteria };
        pubInfo.setDocumentTitle(JadeThread.getMessage("al.recapitulatif.titre.impression.label", periodes.clone()));
        // Sujet du document
        pubInfo.setDocumentSubject(JadeThread.getMessage("al.recapitulatif.titre.impression.label", periodes.clone()));
        pubInfo.setDocumentType("RecapEntreprise01");
        pubInfo.setDocumentTypeNumber(ALConstDocument.DOC_TYPE_NUMBER_INFOROM_RECAP);
        pubInfo.setArchiveDocument(false);
        pubInfo.setPublishDocument(true);

        mainContainer.setMergedDocDestination(pubInfo);
        containers[0] = mainContainer;

    }

    @Override
    protected void executeBack() throws JadeApplicationException, JadePersistenceException {
        // DO NOTHING
    }

    @Override
    public String getCSTraitement() {
        return ALCSProcessus.NAME_TRAITEMENT_IMPRESSION_RECAP_PROV;
    }

    @Override
    public JadePrintDocumentContainer[] getProtocole() {
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
