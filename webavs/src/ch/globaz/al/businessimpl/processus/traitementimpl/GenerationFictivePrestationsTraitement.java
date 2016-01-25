package ch.globaz.al.businessimpl.processus.traitementimpl;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.ALCSProcessus;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstPrestations;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.dossier.AffilieListComplexModel;
import ch.globaz.al.business.models.dossier.AffilieListComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.processus.BusinessTraitement;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.utils.ALDateUtils;
import ch.globaz.al.utils.ALFileCSVUtils;

public class GenerationFictivePrestationsTraitement extends BusinessTraitement {
    private JadePrintDocumentContainer containerA = new JadePrintDocumentContainer();
    /**
     * Conteneur
     */
    private JadePrintDocumentContainer[] containers = new JadePrintDocumentContainer[1];
    /**
     * Logger contenant les erreurs survenues pendant le traitement
     */
    private ProtocoleLogger protocoleLogger = null;

    /**
     * Conteneur pour les protocoles CSV de la simulation
     */
    private ArrayList<String> protocolesCSV = new ArrayList<String>();
    /**
     * PubInfo pour l'envoi des protocoles
     */
    private JadePublishDocumentInfo pubInfo;

    @Override
    protected void execute() throws JadePersistenceException, JadeApplicationException {

        boolean isGed = false;

        ArrayList listConteneurRecap = new ArrayList();
        HashMap recapCSV = new HashMap();
        ArrayList listRecapPdf = new ArrayList();
        ArrayList listRecapCsv = new ArrayList();

        ArrayList listDossiersGeneration = new ArrayList();
        String periode = getProcessusConteneur().getDataCriterias().periodeCriteria;
        String typeCoti = getProcessusConteneur().getDataCriterias().cotisationCriteria;

        // Recherche des affiliés à prendre en compte dans la génération
        AffilieListComplexSearchModel affilies = ALImplServiceLocator.getGenerationService().initSearchAffilies(
                periode, typeCoti);
        affilies.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        affilies = ALImplServiceLocator.getAffilieListComplexModelService().search(affilies);

        DossierComplexSearchModel dossiers = null;

        // récupération des dossiers de chaque affilié
        for (int i = 0; i < affilies.getSize(); i++) {

            String numAffilie = ((AffilieListComplexModel) affilies.getSearchResults()[i]).getNumeroAffilie();
            String periodicite = ((AffilieListComplexModel) affilies.getSearchResults()[i]).getPeriodicite();
            String debutPeriode = ALConstPrestations.TYPE_DIRECT.equals(typeCoti) ? periode : ALDateUtils
                    .getDebutPeriode(periode, periodicite);

            dossiers = ALImplServiceLocator.getGenerationService().initSearchDossiers(debutPeriode, typeCoti);
            dossiers.setForNumeroAffilie(numAffilie);
            dossiers.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            dossiers.setOrderKey("generationFictive");
            dossiers = ALServiceLocator.getDossierComplexModelService().search(dossiers);

            for (int j = 0; j < dossiers.getSize(); j++) {
                listDossiersGeneration.add(((DossierComplexModel) dossiers.getSearchResults()[j]).getId());
            }

        }

        String bonification = ALConstPrestations.TYPE_DIRECT.equals(typeCoti) ? ALCSPrestation.BONI_DIRECT
                : ALCSPrestation.BONI_INDIRECT;
        // pour tous les dossiers concernés, on les calcule et stockes les résultats sous forme de lignes récaps
        // imprimables via service impression récaps
        RecapitulatifEntrepriseImpressionComplexSearchModel prestationsToPrint = ALServiceLocator
                .getRecapitulatifEntrepriseImpressionService().calculPrestationsStoreInRecapsDocs(
                        listDossiersGeneration, periode, bonification);
        // pour que résultat du calcul soient compatibles avec l'impression des récaps, il faut le mettre dans une
        // ArrayList
        ArrayList<RecapitulatifEntrepriseImpressionComplexSearchModel> recapToPrint = new ArrayList<RecapitulatifEntrepriseImpressionComplexSearchModel>();
        recapToPrint.add(prestationsToPrint);

        // ------------- PDF + MAIL + GED(ou pas selon paramètrage) ----------------
        try {
            // document en pdf
            listRecapPdf = ALServiceLocator.getRecapitulatifEntrepriseImpressionService().loadArrayListDocData(
                    recapToPrint);
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
            listRecapCsv = ALServiceLocator.getRecapitulatifEntrepriseImpressionService()
                    .loadArrayListCsv(recapToPrint);
            if (listRecapCsv.size() != 0) {
                recapCSV = ALServiceLocator.getRecapitulatifEntrepriseImpressionService().loadCSVDocument(listRecapCsv);

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

    }

    @Override
    protected void executeBack() throws JadeApplicationException, JadePersistenceException {
        // TODO Auto-generated method stub

    }

    @Override
    public String getCSTraitement() {
        return ALCSProcessus.NAME_TRAITEMENT_GENERATION_FICTIVE;
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
        return containers;

    }

    @Override
    public ProtocoleLogger getProtocoleLogger() {
        return protocoleLogger;
    }

    @Override
    public JadePublishDocumentInfo getPubInfo() {
        return pubInfo;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }
}
