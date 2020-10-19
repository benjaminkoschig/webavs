package globaz.amal.process.annonce;

import globaz.amal.process.AMALabstractProcess;
import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.job.common.JadeJobQueueNames;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.pyxis.constantes.IConstantes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.amal.business.constantes.AMMessagesTypesAnnonceSedex;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladie;
import ch.globaz.amal.business.models.caissemaladie.CaisseMaladieSearch;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJob;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJobSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.amal.businessimpl.services.sedexRP.builder.AnnonceBuilderReturnInfosContainer;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class AnnonceSedexProcess extends AMALabstractProcess {

    private static final long serialVersionUID = 1L;
    public static final String ATTACHED_FILES = "ATTACHED_FILES";
    public static final String ATTACHED_XML = "ATTACHED_XML";
    public static final String ERROR_CREATION = "ERROR_CREATION";
    public static final String ERROR_ENVOI = "ERROR_ENVOI";
    public static final String MAP_RETURNINFOS = "MAP_RETURNINFOS";
    public static final String NB_ITEMS_CREATED = "NB_ITEMS_CREATED";
    public static final String NB_ITEMS_SEND = "NB_ITEMS_SEND";
    private String anneeHistorique = null;
    private List<String> errorMessages = null;
    private boolean isSimulation = false;
    private Map<String, Object> mapReturn = null;
    private String noGroupe = null;
    private String periodeDecreeInventoryFrom = null;
    private String periodeDecreeInventoryTo = null;
    private String periodeInsuranceQueryFrom = null;
    private String periodeInsuranceQueryTo = null;
    private List<String> selectedIdCaisses = null;
    private String typeMessage = null;

    private String _getReturnsInfos(Map<String, AnnonceBuilderReturnInfosContainer> mapReturnInfos) {
        String message = "";

        int nbElements = 0;
        int nbCreation = 0;
        int nbEnvoi = 0;
        ArrayList<String> nbElementByCaisse = new ArrayList<String>();

        // Comptage des annonces créees et envoyées
        for (String idCaisse : mapReturnInfos.keySet()) {
            AnnonceBuilderReturnInfosContainer container = mapReturnInfos.get(idCaisse);
            nbCreation += container.getNbCreation();
            nbEnvoi += container.getNbEnvoi();

            try {
                CaisseMaladieSearch cmSearch = new CaisseMaladieSearch();
                cmSearch.setForIdTiersCaisse(idCaisse);
                cmSearch.setForTypeAdmin(IConstantes.CS_TIERS_TYPE_ADMINISTRATION);
                cmSearch.setWhereKey("rcListe");
                cmSearch.setDefinedSearchSize(0);
                cmSearch = AmalServiceLocator.getCaisseMaladieService().search(cmSearch);
                if (cmSearch.getSize() > 0) {
                    CaisseMaladie currentCMIndividuel = (CaisseMaladie) cmSearch.getSearchResults()[0];
                    String currentCMNo = currentCMIndividuel.getNumCaisse();
                    String currentCMName = currentCMIndividuel.getNomCaisse();
                    nbElementByCaisse.add(currentCMNo + " - " + currentCMName + " : " + container.getNbElements()
                            + " élément(s)");
                }
            } catch (Exception e) {
                nbElementByCaisse.add("#" + idCaisse + " : " + container.getNbElements() + " élément(s)");
            }
        }

        message += "\nNombre annonce(s) crée(s) : " + nbCreation;
        message += "\nNombre annonce(s) envoyées(s) : " + nbEnvoi;
        message += "\nDétail : ";
        for (String ligneDetail : nbElementByCaisse) {
            message += "\n   - " + ligneDetail;
        }

        return message;
    }

    /**
     * Création du mail à envoyer à l'utilisateur
     */
    private void createMail() {
        // --------------------------------------------------
        // 1) Préparation du message (body and subject)
        // --------------------------------------------------
        String subject = "";
        String message = "";
        String etatProcess = "terminé";
        boolean onError = false;

        if (mapReturn == null) {
            mapReturn = new HashMap<String, Object>();
        }

        if (mapReturn.containsKey(AnnonceSedexProcess.ERROR_CREATION)) {
            ArrayList<?> arrayC = (ArrayList<?>) mapReturn.get(AnnonceSedexProcess.ERROR_CREATION);
            if (arrayC.size() > 0) {
                onError = true;
            }
        }

        if (mapReturn.containsKey(AnnonceSedexProcess.ERROR_ENVOI)) {
            ArrayList<?> arrayE = (ArrayList<?>) mapReturn.get(AnnonceSedexProcess.ERROR_ENVOI);
            if (arrayE.size() > 0) {
                onError = true;
            }
        }

        if (errorMessages.size() > 0) {
            onError = true;
        }

        if (onError) {
            etatProcess = "en erreur";
        }

        String typeProcess = "Processus";
        if (isSimulation) {
            typeProcess = "Simulation";
        }

        try {
            if (AMMessagesTypesAnnonceSedex.NOUVELLE_DECISION.getValue().equals(typeMessage)) {
                subject += "Web@Lamal : " + typeProcess + " SEDEX 'Nouvelles décisions / Interruptions' " + etatProcess;
                message += typeProcess + " de création et d'envoi d'annonces SEDEX RP terminé.\n\n";
            } else if (AMMessagesTypesAnnonceSedex.DEMANDE_RAPPORT_ASSURANCE.getValue().equals(typeMessage)) {
                subject += "Web@Lamal : " + typeProcess + " SEDEX 'Demande du rapport d'assurance' " + etatProcess;
                message += typeProcess + " de création et d'envoi d'annonces SEDEX RP terminé.\n\n";
            } else if (AMMessagesTypesAnnonceSedex.ETAT_DECISIONS.getValue().equals(typeMessage)) {
                subject += "Web@Lamal : " + typeProcess + " SEDEX 'Etat des décisions' " + etatProcess;
                message += typeProcess + " de création et d'envoi d'annonces SEDEX RP terminé.\n\n";
            } else if (AMMessagesTypesAnnonceSedex.DEMANDE_PRIME_TARIFAIRE.getValue().equals(typeMessage)) {
                subject += "Web@Lamal : " + typeProcess + " SEDEX 'Demande prime tarifaire' " + etatProcess;
                message += typeProcess + " de création et d'envoi d'annonces SEDEX PT terminé.\n\n";
            } else {
                subject += "Web@Lamal : " + typeProcess + " SEDEX " + etatProcess;
                message += typeProcess + " de création et d'envoi d'annonces SEDEX RP terminé.\n\n";
            }
            if (JadeStringUtil.isEmpty(getAnneeHistorique())) {
                message += "Année de subside : pas de sélection (toutes)\n\n";
            } else {
                message += "Année de subside : " + getAnneeHistorique() + "\n";
            }
            if (JadeStringUtil.isBlankOrZero(noGroupe)) {
                message += "Caisse(s) : \n";
                for (String idCaisse : selectedIdCaisses) {
                    AdministrationComplexModel admc = TIBusinessServiceLocator.getAdministrationService()
                            .read(idCaisse);
                    if (!admc.isNew() && !JadeStringUtil.isEmpty(admc.getTiers().getDesignation1())) {
                        message += "   " + admc.getTiers().getDesignation1() + "\n";
                    } else {
                        message += "   " + admc.getAdmin().getCodeAdministration() + "\n";
                    }
                }
            } else {
                String currentGroupe = noGroupe;
                String currentNoGroupe = "";
                String currentNomGroupe = "";
                CaisseMaladieSearch cmSearch = new CaisseMaladieSearch();
                cmSearch.setForIdTiersGroupe(currentGroupe);
                cmSearch.setForTypeAdmin(IConstantes.CS_TIERS_TYPE_ADMINISTRATION);
                cmSearch.setWhereKey("rcListeOnlyActifs");
                cmSearch.setDefinedSearchSize(0);
                try {
                    cmSearch = AmalServiceLocator.getCaisseMaladieService().search(cmSearch);
                    if (cmSearch.getSize() > 0) {
                        CaisseMaladie currentCMGroupe = (CaisseMaladie) cmSearch.getSearchResults()[0];
                        currentNoGroupe = currentCMGroupe.getNumGroupe();
                        currentNomGroupe = currentCMGroupe.getNomGroupe();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (JadeStringUtil.isEmpty(currentNomGroupe) && JadeStringUtil.isEmpty(currentNoGroupe)) {
                    message += "Groupe sélectionné : " + currentGroupe + "\n	";
                } else {
                    message += "Groupe sélectionné : " + JadeStringUtil.fillWithZeroes(currentNoGroupe, 4) + " "
                            + currentNomGroupe + "\n";
                }
            }

            if (selectedIdCaisses == null) {
                if (!JadeStringUtil.isBlankOrZero(noGroupe)) {
                    selectedIdCaisses = new ArrayList<String>();
                    CaisseMaladieSearch cmSearch = new CaisseMaladieSearch();
                    cmSearch.setForIdTiersGroupe(noGroupe);
                    cmSearch.setForTypeAdmin(IConstantes.CS_TIERS_TYPE_ADMINISTRATION);
                    cmSearch.setWhereKey("rcListeOnlyActifs");
                    cmSearch.setDefinedSearchSize(0);
                    try {
                        cmSearch = AmalServiceLocator.getCaisseMaladieService().search(cmSearch);
                        for (int iCm = 0; iCm < cmSearch.getSize(); iCm++) {
                            CaisseMaladie currentCM = (CaisseMaladie) cmSearch.getSearchResults()[iCm];
                            if (!selectedIdCaisses.contains(currentCM.getIdTiersCaisse())) {
                                selectedIdCaisses.add(currentCM.getIdTiersCaisse());
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Pour les états de décision on n'affiche que le nombre d'annonces créés qu'on récupère depuis une
        // mapReturnInfos
        if (AMMessagesTypesAnnonceSedex.ETAT_DECISIONS.getValue().equals(typeMessage)) {
            // La map devrait contenir un "mapReturnInfos" qui contient différentes informations pour afficher dans
            // le mail
            if (mapReturn.containsKey(AnnonceSedexProcess.MAP_RETURNINFOS)) {
                @SuppressWarnings("unchecked")
                Map<String, AnnonceBuilderReturnInfosContainer> mapReturnInfos = (Map<String, AnnonceBuilderReturnInfosContainer>) mapReturn
                        .get(AnnonceSedexProcess.MAP_RETURNINFOS);
                message += _getReturnsInfos(mapReturnInfos);
            }
        } else {
            Integer nbItemsCreated = new Integer(0);
            if (mapReturn.containsKey(AnnonceSedexProcess.NB_ITEMS_CREATED)) {
                nbItemsCreated = (Integer) mapReturn.get(AnnonceSedexProcess.NB_ITEMS_CREATED);
                message += "\nNombre annonce(s) crées : ";
                message += nbItemsCreated.toString();
            }

            Integer nbItemsSend = new Integer(0);
            if (mapReturn.containsKey(AnnonceSedexProcess.NB_ITEMS_SEND)) {
                nbItemsSend = (Integer) mapReturn.get(AnnonceSedexProcess.NB_ITEMS_SEND);
                message += "\nNombre annonce(s) envoyées : ";
                message += nbItemsSend.toString();
            }
        }

        String[] files = null;
        if (mapReturn.containsKey(AnnonceSedexProcess.ATTACHED_FILES)) {
            files = (String[]) mapReturn.get(AnnonceSedexProcess.ATTACHED_FILES);
        }

        if (onError) {
            if (mapReturn.containsKey(AnnonceSedexProcess.ERROR_CREATION)) {
                ArrayList<?> arrayErrorsCreation = (ArrayList<?>) mapReturn.get(AnnonceSedexProcess.ERROR_CREATION);
                message += "\nErrors création : ";
                message += arrayErrorsCreation.size();
                for (int i = 0; i < arrayErrorsCreation.size(); i++) {
                    message += "\n" + arrayErrorsCreation.get(i).toString();
                }
            }

            if (mapReturn.containsKey(AnnonceSedexProcess.ERROR_ENVOI)) {
                ArrayList<?> arrayErrorsEnvoi = (ArrayList<?>) mapReturn.get(AnnonceSedexProcess.ERROR_ENVOI);
                message += "\nErrors envois : ";
                message += arrayErrorsEnvoi.size();
                for (int i = 0; i < arrayErrorsEnvoi.size(); i++) {
                    message += "\n" + arrayErrorsEnvoi.get(i).toString();
                }
            }

            if (errorMessages.size() > 0) {
                message += "\nErrors : ";
                message += errorMessages.size();
                for (String errMsg : errorMessages) {
                    message += "\n" + errMsg;
                }
            }
        }
        // --------------------------------------------------
        // 2) Envoi du message
        // --------------------------------------------------
        try {
            JadeSmtpClient.getInstance().sendMail(BSessionUtil.getSessionFromThreadContext().getUserEMail(), subject,
                    message, files);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getAnneeHistorique() {
        return anneeHistorique;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getDescription()
     */
    @Override
    public String getDescription() {
        return "Génération des annonces CM AMAL";
    }

    public boolean getIsSimulation() {
        return isSimulation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.JadeJob#getName()
     */
    @Override
    public String getName() {
        return this.getClass().getName();
    }

    public String getNoGroupe() {
        return noGroupe;
    }

    public String getPeriodeDecreeInventoryFrom() {
        return periodeDecreeInventoryFrom;
    }

    public String getPeriodeDecreeInventoryTo() {
        return periodeDecreeInventoryTo;
    }

    public String getPeriodeInsuranceQueryFrom() {
        return periodeInsuranceQueryFrom;
    }

    public String getPeriodeInsuranceQueryTo() {
        return periodeInsuranceQueryTo;
    }

    public List<String> getSelectedIdCaisses() {
        return selectedIdCaisses;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.job.AbstractJadeJob#jobQueueName()
     */
    @Override
    public String jobQueueName() {
        return JadeJobQueueNames.SYSTEM_INTER_JOB_QUEUE;
    }

    @Override
    protected void process() {
        try {
            if (mapReturn != null) {
                mapReturn.clear();
            }
            mapReturn = AmalImplServiceLocator.getAnnoncesRPService().createAndSendAnnonce(typeMessage,
                    selectedIdCaisses, noGroupe, anneeHistorique, isSimulation);
        } catch (Exception ex) {
            ex.printStackTrace();
            JadeLogger.error(this, "Error launching AnnoncesSedexRP process : " + ex.getMessage());
            JadeThread.logError("AnnonceSedexProcess.process()", ex.toString());
        }

        setJobStatusSent();

        createMail();
    }

    public void setAnneeHistorique(String anneeHistorique) {
        this.anneeHistorique = anneeHistorique;
    }

    public void setIsSimulation(boolean isSimulation) {
        this.isSimulation = isSimulation;
    }

    /**
     * Reset du status du job en cours à sent
     */
    private void setJobStatusSent() {

        if (JadeThread.logHasMessages()) {
            JadeBusinessMessage[] allMessages = JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.WARN);
            for (int iMessage = 0; iMessage < allMessages.length; iMessage++) {
                String currentMessage = allMessages[iMessage].getContents(JadeThread.currentLanguage());
                if (errorMessages == null) {
                    errorMessages = new ArrayList<String>();
                }
                errorMessages.add(currentMessage);
            }
            JadeThread.logClear();
        } else {
            errorMessages = new ArrayList<String>();
        }

        SimpleControleurJobSearch currentJobSearch = new SimpleControleurJobSearch();
        currentJobSearch.setForTypeJob(IAMCodeSysteme.AMJobType.JOBANNONCE.getValue());
        currentJobSearch.setForStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue());
        currentJobSearch.setDefinedSearchSize(0);
        try {
            currentJobSearch = AmalServiceLocator.getControleurEnvoiService().search(currentJobSearch);
            for (int iJob = 0; iJob < currentJobSearch.getSize(); iJob++) {
                SimpleControleurJob currentJob = (SimpleControleurJob) currentJobSearch.getSearchResults()[iJob];
                currentJob.setStatusEnvoi(IAMCodeSysteme.AMDocumentStatus.SENT.getValue());
                currentJob = AmalImplServiceLocator.getSimpleControleurJobService().update(currentJob);
            }
        } catch (Exception ex) {
            JadeLogger.error(null, "Error reseting status job annonce à SENT :" + ex.toString());
            ex.printStackTrace();
        }
        try {
            JadeThread.commitSession();
        } catch (Exception ex) {
            JadeLogger.error(null, "Error commiting session when reseting status job annonce à SENT :" + ex.toString());
            ex.printStackTrace();
        }
    }

    public void setNoGroupe(String noGroupe) {
        this.noGroupe = noGroupe;
    }

    public void setPeriodeDecreeInventoryFrom(String periodeDecreeInventoryFrom) {
        this.periodeDecreeInventoryFrom = periodeDecreeInventoryFrom;
    }

    public void setPeriodeDecreeInventoryTo(String periodeDecreeInventoryTo) {
        this.periodeDecreeInventoryTo = periodeDecreeInventoryTo;
    }

    public void setPeriodeInsuranceQueryFrom(String periodeInsuranceQueryFrom) {
        this.periodeInsuranceQueryFrom = periodeInsuranceQueryFrom;
    }

    public void setPeriodeInsuranceQueryTo(String periodeInsuranceQueryTo) {
        this.periodeInsuranceQueryTo = periodeInsuranceQueryTo;
    }

    public void setSelectedIdCaisses(List<String> selectedIdCaisses) {
        this.selectedIdCaisses = selectedIdCaisses;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }

}
