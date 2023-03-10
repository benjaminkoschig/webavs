package ch.globaz.al.businessimpl.processus.traitementimpl;

import ch.globaz.al.business.constantes.*;
import ch.globaz.al.business.loggers.ProtocoleLogger;
import ch.globaz.al.business.models.dossier.AffilieListComplexModel;
import ch.globaz.al.business.models.dossier.AffilieListComplexSearchModel;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.dossier.DossierComplexSearchModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.processus.BusinessTraitement;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.al.properties.ALProperties;
import ch.globaz.al.utils.ALDateUtils;
import ch.globaz.al.utils.ALFileCSVUtils;
import ch.globaz.common.properties.CommonProperties;
import ch.globaz.orion.businessimpl.services.af.AfServiceImpl;
import ch.globaz.orion.businessimpl.services.partnerWeb.PartnerWebServiceImpl;
import ch.globaz.orion.ws.service.AppAffiliationService;
import ch.globaz.xmlns.eb.recapaf.NouvelleLigneRecapAf;
import ch.globaz.xmlns.eb.recapaf.UniteTempsEnum;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.publish.client.JadePublishDocument;
import globaz.jade.publish.client.JadePublishServerFacade;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.publish.message.JadePublishDocumentMessage;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.translation.CodeSystem;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

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
    private List<String> protocolesCSV = new ArrayList<>();
    /**
     * PubInfo pour l'envoi des protocoles
     */
    private JadePublishDocumentInfo pubInfo;

    @Override
    protected void execute() throws JadePersistenceException, JadeApplicationException {

        boolean isGed = false;
        boolean isEbusinessConnected = CommonProperties.EBUSINESS_CONNECTED.getBooleanValue();
        boolean isManagedRecapAfInEbusiness = ALProperties.RECAPAF_MANAGE_RECAP_IN_EBUSINESS.getBooleanValue();

        List listConteneurRecap = new ArrayList();
        Map recapCSV = new HashMap();
        List listRecapPdf = new ArrayList();
        List listRecapCsv = new ArrayList();

        List<String> listDossiersGeneration = new ArrayList<>();
        String periode = getProcessusConteneur().getDataCriterias().periodeCriteria;
        String typeCoti = getProcessusConteneur().getDataCriterias().cotisationCriteria;

        // Recherche des affili?s ? prendre en compte dans la g?n?ration
        AffilieListComplexSearchModel affilies = ALImplServiceLocator.getGenerationService().initSearchAffilies(
                periode, typeCoti);
        affilies.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        affilies = ALImplServiceLocator.getAffilieListComplexModelService().search(affilies);

        DossierComplexSearchModel dossiers = null;

        // r?cup?ration des dossiers de chaque affili?
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

        // pour tous les dossiers concern?s, on les calcule et stockes les r?sultats sous forme de lignes r?caps
        // imprimables via service impression r?caps
        RecapitulatifEntrepriseImpressionComplexSearchModel allPrestations = ALServiceLocator
                .getRecapitulatifEntrepriseImpressionService().calculPrestationsStoreInRecapsDocs(
                        listDossiersGeneration, periode, bonification);

        // liste des r?caps ? imprimer
        List<RecapitulatifEntrepriseImpressionComplexSearchModel> recapToPrint = new ArrayList<>();

        // si l'environnement webAVS est connect? ? EBusiness on cr?? certaines r?caps c?t? EBusiness
        if (isEbusinessConnected && isManagedRecapAfInEbusiness) {
            // r?cup?ration de tous les num?ros d'affili?s actifs dans le portail EBusiness
            List<String> listNumeroAffiliesEbusiness = PartnerWebServiceImpl
                    .listAllActivNumerosAffiliesEbusiness(BSessionUtil.getSessionFromThreadContext());

            List<JadeAbstractModel> allPrestationsList = Arrays.asList(allPrestations.getSearchResults());
            List<JadeAbstractModel> prestationsToPrintList = new ArrayList<>();
            Map<String, List<RecapitulatifEntrepriseImpressionComplexModel>> recapForEbusinessByNumAffilieMap = new HashMap<>();

            for (JadeAbstractModel model : allPrestationsList) {
                RecapitulatifEntrepriseImpressionComplexModel dossier = (RecapitulatifEntrepriseImpressionComplexModel) model;
                String numAffilie = dossier.getRecapEntrepriseModel().getNumeroAffilie();

                // si affili? EBusiness on place dans la map des r?cap EBusiness
                if (listNumeroAffiliesEbusiness.contains(numAffilie)) {
                    if (recapForEbusinessByNumAffilieMap.containsKey(numAffilie)) {
                        recapForEbusinessByNumAffilieMap.get(numAffilie).add(dossier);
                    } else {
                        List<RecapitulatifEntrepriseImpressionComplexModel> listeDossier = new ArrayList<>();
                        listeDossier.add(dossier);
                        recapForEbusinessByNumAffilieMap.put(numAffilie, listeDossier);
                    }
                }
                // sinon on le place dans la liste des r?caps ? imprimer
                else {
                    prestationsToPrintList.add(model);
                }
            }

            // cr?ation des r?caps sur EBusiness
            try {
                createRecapForEbusiness(recapForEbusinessByNumAffilieMap, periode);
            } catch (Exception e1) {
                JadeLogger.error(this, new Exception("Erreur ? l'utilisation du service loadDocuments", e1));
                JadeThread.logError(this.getClass().getName() + ".process()",
                        "Erreur ? l'utilisation du service loadDocuments");
                return;
            }

            // pour les affili?s qui n'ont pas de compte EBusiness on imprime au format papier -> convertir la liste des
            // prestations ? imprimer
            JadeAbstractModel[] prestationsToPrintTab = prestationsToPrintList.toArray(new JadeAbstractModel[0]);
            RecapitulatifEntrepriseImpressionComplexSearchModel prestationToPrintSearchModel = new RecapitulatifEntrepriseImpressionComplexSearchModel();
            prestationToPrintSearchModel.setSearchResults(prestationsToPrintTab);
            recapToPrint.add(prestationToPrintSearchModel);
        } else {
            recapToPrint.add(allPrestations);
        }

        // ------------- PDF + MAIL + GED(ou pas selon param?trage) ----------------
        try {
            // document en pdf
            listRecapPdf = ALServiceLocator.getRecapitulatifEntrepriseImpressionService().loadArrayListDocData(
                    recapToPrint);
            if (listRecapPdf.size() != 0) {
                listConteneurRecap = (ALServiceLocator.getRecapitulatifEntrepriseImpressionService().loadDocuments(
                        listRecapPdf, JadeDateUtil.getGlobazFormattedDate(new Date()), isGed));
            }

        } catch (Exception e) {
            JadeLogger.error(this, new Exception("Erreur ? l'utilisation du service loadDocuments", e));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur ? l'utilisation du service loadDocuments");
            return;

        }

        // cr?ation des pdf dont le doc fusionn? part en mail
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
                boolean isCharNssRecap = JadePropertiesService.getInstance().getProperty(ALConstParametres.RECAP_FORMAT_NSS).equals("true");
                recapCSV = ALServiceLocator.getRecapitulatifEntrepriseImpressionService().loadCSVDocument(listRecapCsv, isCharNssRecap);

            }
        } catch (Exception e) {
            JadeLogger.error(this, new Exception("Erreur ? l'utilisation du service loadCSVDocument", e));
            JadeThread.logError(this.getClass().getName() + ".process()",
                    "Erreur ? l'utilisation du service loadCSVDocument");
            return;

        }

        Set entrees = recapCSV.entrySet();

        Iterator iterCsv = entrees.iterator();

        // cr?ation des fichiers CSV
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
            // cr?ation du fichier CSV
            try {
                ALFileCSVUtils.createFichier(nomFichier, cleIdRecap, valeurCsv);
            } catch (Exception e) {
                JadeLogger.error(this, new Exception("Erreur ? l'utilisation du service ALFileCSVUtils.createFichier",
                        e));
                JadeThread.logError(this.getClass().getName() + ".process()",
                        "Erreur ? l'utilisation du service ALFileCSVUtils.createFichier");
                return;

            }
            try {
                JadePublishDocument docInfoCSV = new JadePublishDocument(nomFichier, docInfoCSVs);
                JadePublishServerFacade.publishDocument(new JadePublishDocumentMessage(docInfoCSV));

            } catch (Exception e) {
                JadeLogger.error(this, new Exception(
                        "Erreur ? l'utilisation des objets JadePublisDocument/JadePublishServerFacade", e));
                JadeThread.logError(this.getClass().getName() + ".process()",
                        "Erreur ? l'utilisation des objets JadePublisDocument/JadePublishServerFacade");

                return;

            }

            // supprimer le fichier CSV

            File file = new File(nomFichier);

            file.delete();

        }

    }

    private void createRecapForEbusiness(Map<String, List<RecapitulatifEntrepriseImpressionComplexModel>> mapDossier,
            String periode) throws DatatypeConfigurationException {
        for (Map.Entry<String, List<RecapitulatifEntrepriseImpressionComplexModel>> entry : mapDossier.entrySet()) {
            String numAffilie = entry.getKey();

            // R?cup?ration de la particularit? si les r?cap doivent ?tre clot?r?es manuellement ou automatiquement
            Boolean isClotureRecapManuelle = false;
            try {
                String idAffilieFromDossier = getIdAffilie(BSessionUtil.getSessionFromThreadContext(), numAffilie);
                if(!JadeStringUtil.isBlankOrZero(idAffilieFromDossier)){
                    isClotureRecapManuelle = AFParticulariteAffiliation.existeParticulariteDateDonnee(BSessionUtil.getSessionFromThreadContext(), idAffilieFromDossier, CodeSystem.PARTIC_AFFILIE_CLOTURE_RECAP_MANUELLE, JACalendar.todayJJsMMsAAAA());
                }
            } catch (Exception e) {
                JadeLogger.error(this, "Impossible de d?terminer si les r?cap pour l'affili? " + numAffilie + " doivent ?tre cl?tur?es manuellement ou automatiquement ("
                        + e.getMessage()+")");
            }

            GregorianCalendar anneeMoiDateCal = new GregorianCalendar();
            GregorianCalendar misADispoCal = new GregorianCalendar();

            String anneeMoisStr = JadeDateUtil.getFirstDateOfMonth(periode);
            Date anneeMoisDate = JadeDateUtil.getGlobazDate(anneeMoisStr);
            Date misADispo = new Date();

            anneeMoiDateCal.setTime(anneeMoisDate);
            misADispoCal.setTime(misADispo);

            XMLGregorianCalendar anneeMoisRecap = null;
            XMLGregorianCalendar miseADispo = null;

            anneeMoisRecap = DatatypeFactory.newInstance().newXMLGregorianCalendar(anneeMoiDateCal);
            miseADispo = DatatypeFactory.newInstance().newXMLGregorianCalendar(misADispoCal);

            List<NouvelleLigneRecapAf> lignesRecap = new ArrayList<NouvelleLigneRecapAf>();

            for (RecapitulatifEntrepriseImpressionComplexModel dossier : entry.getValue()) {
                NouvelleLigneRecapAf ligne = new NouvelleLigneRecapAf();

                ligne.setNss(dossier.getNumNSS());
                ligne.setNumeroDossierAf(Integer.valueOf(dossier.getIdDossier()));
                ligne.setNomAllocataire(dossier.getNomAllocataire());
                ligne.setPrenomAllocataire(dossier.getPrenomAllocataire());
                ligne.setNbEnfant(Integer.valueOf(dossier.getNbrEnfant()));
                ligne.setMontantAllocation(new BigDecimal(dossier.getMontant()));

                String typeUnite = dossier.getTypeUnite();
                if (ALCSDossier.UNITE_CALCUL_MOIS.equals(typeUnite)) {
                    ligne.setUniteTravail(UniteTempsEnum.MOIS);
                    if (!JadeStringUtil.isEmpty(dossier.getNbreUnite())) {
                        ligne.setNbUniteTravail(Integer.valueOf(dossier.getNbreUnite()));
                    }
                } else if (ALCSDossier.UNITE_CALCUL_JOUR.equals(typeUnite)) {
                    ligne.setUniteTravail(UniteTempsEnum.JOUR);
                } else if (ALCSDossier.UNITE_CALCUL_HEURE.equals(typeUnite)) {
                    ligne.setUniteTravail(UniteTempsEnum.HEURE);
                } else {
                    // si unit? sp?ciale ou autre on ne cr?? pas de ligne dans la r?cap
                    continue;
                }

                lignesRecap.add(ligne);
            }

            try {
                AfServiceImpl.createRecapAf(BSessionUtil.getSessionFromThreadContext(), numAffilie, anneeMoisRecap,
                        miseADispo, lignesRecap, isClotureRecapManuelle);
            } catch (Exception e) {
                JadeLogger.error(this, "Erreur lors de la cr?ation de la r?capAf pour l'affili? " + numAffilie + " "
                        + e.getMessage());
            }
        }

    }

    private String getIdAffilie(BSession session, String numAffilie) throws Exception {
            // recherche de l'affiliation
            AFAffiliationManager affiliationManager = new AFAffiliationManager();
            affiliationManager.setSession(session);
            affiliationManager.setForAffilieNumero(numAffilie);

            try {
                affiliationManager.find(BManager.SIZE_NOLIMIT);
                if (affiliationManager.size() > 0) {
                    // r?cup?ration de l'affiliation
                    return ((AFAffiliation) affiliationManager.getFirstEntity()).getAffiliationId();
                } else {
                    JadeLogger.error(AppAffiliationService.class, "affiliation not found : " + numAffilie);
                    throw new Exception("affiliation not found : " + numAffilie);
                }
            } catch (Exception e) {
                JadeLogger.error(AppAffiliationService.class, "technical error when findAffiliation for numeroAffilie : "
                        + numAffilie);
                throw new Exception("technical error when findAffiliation for numeroAffilie : " + numAffilie);
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
     * M?thode qui retourne les infos de publications pour un document fusionn? qui part en mail
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
