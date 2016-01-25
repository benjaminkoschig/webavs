/**
 * 
 */
package ch.globaz.amal.businessimpl.services.models.controleurEnvoi;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.parameters.FWParametersCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JAVector;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Iterator;
import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.amal.business.exceptions.models.controleurEnvoi.ControleurEnvoiException;
import ch.globaz.amal.business.exceptions.models.controleurJob.ControleurJobException;
import ch.globaz.amal.business.exceptions.models.detailFamille.DetailFamilleException;
import ch.globaz.amal.business.exceptions.models.documents.DocumentException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.models.annonce.SimpleAnnonce;
import ch.globaz.amal.business.models.annonce.SimpleAnnonceSearch;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatus;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatusSearch;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJob;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJobSearch;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamille;
import ch.globaz.amal.business.models.detailfamille.SimpleDetailFamilleSearch;
import ch.globaz.amal.business.models.documents.SimpleDocument;
import ch.globaz.amal.business.models.documents.SimpleDocumentSearch;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.famille.SimpleFamilleSearch;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplex;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplexSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.business.services.models.controleurEnvoi.SimpleControleurEnvoiStatusService;
import ch.globaz.amal.businessimpl.checkers.controleurEnvoi.SimpleControleurEnvoiStatusChecker;
import ch.globaz.amal.businessimpl.services.AmalImplServiceLocator;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleList;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleListSearch;
import ch.globaz.envoi.business.models.parametrageEnvoi.SimpleRappel;
import ch.globaz.envoi.business.services.ENServiceLocator;
import ch.globaz.libra.business.exceptions.LibraException;
import ch.globaz.libra.business.services.LibraServiceLocator;
import ch.globaz.libra.constantes.ILIConstantesExternes;
import ch.globaz.pyxis.business.model.AdministrationComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author DHI
 * 
 */
public class SimpleControleurEnvoiStatusServiceImpl implements SimpleControleurEnvoiStatusService {

    @Override
    public SimpleControleurEnvoiStatus changeStatus(String idStatus, String newStatus, Boolean generateGlobal,
            String jobError) throws JadePersistenceException, JadeApplicationServiceNotAvailableException,
            ControleurEnvoiException, AnnonceException, DocumentException, ControleurJobException {
        // -------------------------------------------------------------------------
        // 0) Contrôle la validité des inputs
        // -------------------------------------------------------------------------
        if (JadeStringUtil.isEmpty(idStatus)) {
            throw new ControleurEnvoiException("Unable to change the status, the id passed is empty");
        }
        if (JadeStringUtil.isEmpty(newStatus)) {
            throw new ControleurEnvoiException("Unable to change the status, the new status is empty");
        }
        if (generateGlobal == null) {
            throw new ControleurEnvoiException("Unable to change the status, the generatGlobal flag is empty");
        }
        SimpleControleurEnvoiStatus simpleControleurEnvoiStatus = read(idStatus);

        if (simpleControleurEnvoiStatus != null) {
            // --------------------------------------------------------------------------------
            // Mise à jour du champ dans status
            // -----------------------------------------------------------------------
            if (!simpleControleurEnvoiStatus.getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.SENT.getValue())) {
                simpleControleurEnvoiStatus.setStatusEnvoi(newStatus);
                if (!(jobError == null) && (jobError.length() > 255)) {
                    JadeThread.logWarn(this.getClass().getName(),
                            "Warning, jobError message truncated for status (length > 255)'" + idStatus + "' !");
                    jobError = jobError.substring(0, 255);
                }
                simpleControleurEnvoiStatus.setJobError(jobError);
                update(simpleControleurEnvoiStatus);

                // --------------------------------------------------------------------------------
                // check sur le status sent et journalisation libra
                // --------------------------------------------------------------------------------
                if (simpleControleurEnvoiStatus.getStatusEnvoi()
                        .equals(IAMCodeSysteme.AMDocumentStatus.SENT.getValue())) {
                    String idDetailFamille = "";
                    String libelleLibra = "";
                    String remarqueLibra = "Job Amal #" + simpleControleurEnvoiStatus.getIdJob();
                    String dateEnvoi = "";
                    String noModeleSubside = "";
                    // --------------------------------------------------------------------------------
                    // Search idDetailFamille
                    // -------------------------------------------------------------------------------------------
                    if (simpleControleurEnvoiStatus.getTypeEnvoi().equals(
                            IAMCodeSysteme.AMDocumentType.ENVOI.getValue())) {
                        // --------------------------------------------------------------------------------
                        // Find the document
                        // --------------------------------------------------------------------------------
                        SimpleDocumentSearch documentSearch = new SimpleDocumentSearch();
                        documentSearch.setForIdDocument(simpleControleurEnvoiStatus.getIdEnvoi());
                        try {
                            documentSearch = AmalImplServiceLocator.getSimpleDocumentService().search(documentSearch);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        if (documentSearch.getSize() > 0) {
                            SimpleDocument currentDocument = (SimpleDocument) documentSearch.getSearchResults()[0];
                            idDetailFamille = currentDocument.getIdDetailFamille();
                            // Récupération du code système
                            noModeleSubside = currentDocument.getNumModele();
                            // Récupération du code utilisateur
                            BSession currentSession = BSessionUtil.getSessionFromThreadContext();
                            String codeUtilisateur = currentSession.getCode(noModeleSubside);
                            // Set le libelle libra et date envoi libra
                            libelleLibra = getDocumentLibelle(codeUtilisateur, currentSession);
                            dateEnvoi = currentDocument.getDateEnvoi();
                        }
                    } else {
                        // --------------------------------------------------------------------------------
                        // Find the annonce
                        // --------------------------------------------------------------------------------
                        SimpleAnnonceSearch annonceSearch = new SimpleAnnonceSearch();
                        annonceSearch.setForIdDetailAnnonce(simpleControleurEnvoiStatus.getIdAnnonce());
                        try {
                            annonceSearch = AmalImplServiceLocator.getSimpleAnnonceService().search(annonceSearch);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        if (annonceSearch.getSize() > 0) {
                            SimpleAnnonce currentAnnonce = (SimpleAnnonce) annonceSearch.getSearchResults()[0];
                            idDetailFamille = currentAnnonce.getIdDetailFamille();
                            // Set le libelle libra et date envoi
                            libelleLibra = "Annonce Caisse Maladie ";
                            try {
                                AdministrationComplexModel admin = TIBusinessServiceLocator.getAdministrationService()
                                        .read(currentAnnonce.getNoCaisseMaladie());
                                libelleLibra += admin.getTiers().getDesignation1();
                            } catch (Exception e) {
                                JadeLogger.error(
                                        this,
                                        "Error loading administration for annonce" + currentAnnonce.getId() + " - "
                                                + e.getMessage());
                            }
                            dateEnvoi = currentAnnonce.getDateEnvoiAnnonce();
                        }
                    }

                    if (!JadeStringUtil.isBlankOrZero(idDetailFamille)) {
                        // --------------------------------------------------------------------------------
                        // Find the detailFamille pour mise à jour du subside
                        // ------------------------------------------------------------------------------------
                        SimpleDetailFamilleSearch detailFamilleSearch = new SimpleDetailFamilleSearch();
                        detailFamilleSearch.setForIdDetailFamille(idDetailFamille);
                        try {
                            detailFamilleSearch = AmalImplServiceLocator.getSimpleDetailFamilleService().search(
                                    detailFamilleSearch);
                        } catch (DetailFamilleException e1) {
                            e1.printStackTrace();
                        }
                        if (detailFamilleSearch.getSize() > 0) {
                            SimpleDetailFamille currentDetail = (SimpleDetailFamille) detailFamilleSearch
                                    .getSearchResults()[0];
                            // --------------------------------------------------------------------------------
                            // Mise à jour du subside avec document, date envoi et status dossier et message Sedex
                            // --------------------------------------------------------------------------------
                            currentDetail = changeStatus_WriteDocumentInDetailFamille(currentDetail, noModeleSubside,
                                    dateEnvoi);

                            // --------------------------------------------------------------------------------
                            // Find the SimpleFamille pour récupération idTiers
                            // --------------------------------------------------------------------------------
                            String idFamille = currentDetail.getIdFamille();
                            SimpleFamilleSearch familleSearch = new SimpleFamilleSearch();
                            familleSearch.setForIdFamille(idFamille);
                            try {
                                familleSearch = AmalImplServiceLocator.getSimpleFamilleService().search(familleSearch);
                            } catch (FamilleException e1) {
                                e1.printStackTrace();
                            }
                            if (familleSearch.getSize() > 0) {
                                SimpleFamille currentFamille = (SimpleFamille) familleSearch.getSearchResults()[0];
                                String idTiers = currentFamille.getIdTier();
                                // --------------------------------------------------------------------------------
                                // Création de la journalisation LIBRA
                                // --------------------------------------------------------------------------------
                                if (!JadeStringUtil.isBlankOrZero(idTiers)) {
                                    try {
                                        LibraServiceLocator.getJournalisationService()
                                                .createJournalisationAvecRemarqueWithTestDossier(idFamille,
                                                        libelleLibra, remarqueLibra, idTiers,
                                                        ILIConstantesExternes.CS_DOMAINE_AMAL, true);
                                    } catch (LibraException e) {
                                        e.printStackTrace();
                                    }
                                }
                                // --------------------------------------------------------------------------------
                                // Création du rappel LIBRA si nécessaire
                                // --------------------------------------------------------------------------------
                                if (!JadeStringUtil.isBlankOrZero(idTiers)) {
                                    changeStatus_CreateRappelLibra(dateEnvoi, noModeleSubside, idDetailFamille,
                                            idFamille, idTiers);
                                }
                            }

                        }

                    }
                }
            }

            try {
                JadeThread.commitSession();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // regénère le status global si différent de in progress
            // generateGlobal à true
            // change the status of the job
            // -----------------------------------------------------------------------
            // if (!newStatus.equals(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue()) && generateGlobal) {
            if (generateGlobal) {
                SimpleControleurJobSearch jobSearch = new SimpleControleurJobSearch();
                jobSearch.setForIdJob(simpleControleurEnvoiStatus.getIdJob());
                jobSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                jobSearch = AmalImplServiceLocator.getSimpleControleurJobService().search(jobSearch);
                // le status du job est contrôlé lors de la mise à jour
                for (int iJob = 0; iJob < jobSearch.getSize(); iJob++) {
                    SimpleControleurJob job = (SimpleControleurJob) jobSearch.getSearchResults()[iJob];
                    String generatedStatus = AmalServiceLocator.getControleurEnvoiService().generateStatus(
                            job.getIdJob());
                    job.setStatusEnvoi(generatedStatus);
                    job = AmalImplServiceLocator.getSimpleControleurJobService().update(job);
                }
            }

        }
        return simpleControleurEnvoiStatus;
    }

    /**
     * Création du rappel Libra lors du changement de status
     * 
     * @param dateEnvoi
     * @param noModeleSubside
     * @param idDetailFamille
     * @param idFamille
     * @param idTiers
     */
    private void changeStatus_CreateRappelLibra(String dateEnvoi, String noModeleSubside, String idDetailFamille,
            String idFamille, String idTiers) {
        try {
            // création du nouveau rappel si besoin est
            // récupération de la formule de rappel
            // ---------------------------------------------------------------------
            if (!noModeleSubside.equals("")) {
                // --------------------------------------------------------------------
                // Récupération des informations de la formule courante
                // --------------------------------------------------------------------
                FormuleListSearch currentFormuleSearch = new FormuleListSearch();
                currentFormuleSearch.setForlibelle(noModeleSubside);
                try {
                    currentFormuleSearch = ENServiceLocator.getFormuleListService().search(currentFormuleSearch);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    currentFormuleSearch = null;
                }
                // --------------------------------------------------------------------
                // Récupération des informations du rappel lié à la formule courante
                // --------------------------------------------------------------------
                if ((currentFormuleSearch != null) && (currentFormuleSearch.getSize() > 0)) {
                    FormuleList currentFormule = (FormuleList) currentFormuleSearch.getSearchResults()[0];
                    SimpleRappel currentRappel = currentFormule.getRappel();
                    // --------------------------------------------------------------------
                    // rappel trouvé, récupération de la formule du RAPPEL
                    // --------------------------------------------------------------------
                    if ((currentRappel.getIdDefinitionFormule() != null)
                            && !"".equals(currentRappel.getIdDefinitionFormule())) {

                        // Récupération de la formule de rappel
                        FormuleListSearch formuleSearch = new FormuleListSearch();
                        formuleSearch.setForIdDefinitionFormule(currentRappel.getIdDefinitionFormule());
                        try {
                            formuleSearch = ENServiceLocator.getFormuleListService().search(formuleSearch);
                        } catch (Exception e) {
                            e.printStackTrace();
                            formuleSearch = null;
                        }
                        if ((formuleSearch != null) && (formuleSearch.getSize() > 0)) {
                            // --------------------------------------------------------------------
                            // Récupération des informations pour la création
                            // du rappel LIBRA
                            // --------------------------------------------------------------------
                            FormuleList formule = (FormuleList) formuleSearch.getSearchResults()[0];
                            String rappelCSFormule = formule.getDefinitionformule().getCsDocument();

                            // Récupération du code utilisateur
                            BSession currentSession = BSessionUtil.getSessionFromThreadContext();
                            String codeUtilisateur = currentSession.getCode(rappelCSFormule);

                            // Set le libelle libra et date envoi libra
                            String libelleLibra = getDocumentLibelle(codeUtilisateur, currentSession);
                            libelleLibra += " |" + idDetailFamille + "|" + rappelCSFormule + "|0";

                            // rappel.getUnite();

                            String dateRappel = JadeDateUtil.addDays(dateEnvoi,
                                    Integer.parseInt(currentRappel.getTempsAttente()));

                            LibraServiceLocator.getEcheanceService().createRappelWithTestDossier(dateRappel, idFamille,
                                    libelleLibra, idTiers, ILIConstantesExternes.CS_DOMAINE_AMAL, true);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mise à jour du subside lors du passage au status sent (màj dateenvoi, status dosiser et document)
     * 
     * @param currentDetail
     * @param noModeleSubside
     * @param dateEnvoi
     */
    private SimpleDetailFamille changeStatus_WriteDocumentInDetailFamille(SimpleDetailFamille currentDetail,
            String noModeleSubside, String dateEnvoi) {
        // Mise à jour des champs modèle de document et date envoi
        // -------------------------------------------------------------
        if ("".equals(noModeleSubside)) {
            currentDetail.setDateAnnonceCaisseMaladie(dateEnvoi);
            currentDetail.setAnnonceCaisseMaladie(true);
        } else {
            currentDetail.setDateEnvoi(dateEnvoi);
            currentDetail.setNoModeles(noModeleSubside);
            String newStatusDetailFamille = "";
            // Recherche des paramètres de la formule pour adapter le status du dossier
            FormuleListSearch formuleListSearch = new FormuleListSearch();
            // en fait, for libelle is for cs document...
            formuleListSearch.setForlibelle(noModeleSubside);
            formuleListSearch.setDefinedSearchSize(0);
            try {
                formuleListSearch = ENServiceLocator.getFormuleListService().search(formuleListSearch);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((formuleListSearch != null) && (formuleListSearch.getSize() == 1)) {
                FormuleList formule = (FormuleList) formuleListSearch.getSearchResults()[0];
                // Recherche des paramètres
                ParametreModelComplexSearch modelParametreSearch = new ParametreModelComplexSearch();
                modelParametreSearch.setForIdFormule(formule.getId());
                modelParametreSearch.setWhereKey("basic");
                try {
                    modelParametreSearch = AmalServiceLocator.getParametreModelService().search(modelParametreSearch);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (modelParametreSearch.getSize() == 1) {
                    ParametreModelComplex model = (ParametreModelComplex) modelParametreSearch.getSearchResults()[0];
                    newStatusDetailFamille = model.getSimpleParametreModel().getCodeTraitementDossier();
                    currentDetail.setCodeTraitementDossier(newStatusDetailFamille);

                    // --------------------------------------------------------------------------------
                    // Création du message sedex au status initial si le document provoque une annonce
                    // --------------------------------------------------------------------------------
                    if (model.getSimpleParametreModel().getCodeAnnonceCaisse()) {
                        try {
                            AmalImplServiceLocator.getAnnoncesRPService().initAnnonceNouvelleDecision(
                                    currentDetail.getIdContribuable(), currentDetail.getIdDetailFamille());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                }
            }
        }
        try {
            return AmalServiceLocator.getDetailFamilleService().update(currentDetail);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return currentDetail;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.SimpleControleurEnvoiStatusService#count(ch.globaz.amal
     * .business.models.controleurEnvoi.SimpleControleurEnvoiStatus)
     */
    @Override
    public int count(SimpleControleurEnvoiStatusSearch search) throws JadePersistenceException,
            ControleurEnvoiException {
        if (search == null) {
            throw new ControleurEnvoiException(
                    "Unable to count the number of simplecontroleurenvoistatussearch, the model passed is null");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.SimpleControleurEnvoiStatusService#create(ch.globaz.amal
     * .business.models.controleurEnvoi.SimpleControleurEnvoiStatus)
     */
    @Override
    public SimpleControleurEnvoiStatus create(SimpleControleurEnvoiStatus simpleControleurEnvoiStatus)
            throws JadePersistenceException, JadeApplicationServiceNotAvailableException, ControleurEnvoiException,
            AnnonceException, DocumentException, ControleurJobException {
        if (simpleControleurEnvoiStatus == null) {
            throw new ControleurEnvoiException(
                    "Unable to create a simplecontroleurenvoistatus, the model passed is null");
        }
        SimpleControleurEnvoiStatusChecker.checkForCreate(simpleControleurEnvoiStatus);
        return (SimpleControleurEnvoiStatus) JadePersistenceManager.add(simpleControleurEnvoiStatus);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.SimpleControleurEnvoiStatusService#delete(ch.globaz.amal
     * .business.models.controleurEnvoi.SimpleControleurEnvoiStatus)
     */
    @Override
    public SimpleControleurEnvoiStatus delete(SimpleControleurEnvoiStatus simpleControleurEnvoiStatus)
            throws JadePersistenceException, ControleurEnvoiException {
        if (simpleControleurEnvoiStatus == null) {
            throw new ControleurEnvoiException(
                    "Unable to delete a simplecontroleurenvoistatus, the model passed is null");
        }
        // -------------------------------------------------------------------
        // CHECK STATUS
        // -------------------------------------------------------------------
        if (!JadeStringUtil.isEmpty(simpleControleurEnvoiStatus.getStatusEnvoi())
                && !JadeStringUtil.isEmpty(simpleControleurEnvoiStatus.getTypeEnvoi())
                && !JadeStringUtil.isEmpty(simpleControleurEnvoiStatus.getIdEnvoi())
                && !JadeStringUtil.isEmpty(simpleControleurEnvoiStatus.getIdAnnonce())) {
            if (!simpleControleurEnvoiStatus.getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.SENT.getValue())) {
                if (simpleControleurEnvoiStatus.getTypeEnvoi().equals(IAMCodeSysteme.AMDocumentType.ENVOI.getValue())) {
                    // -------------------------------------------------------------------
                    // DELETE DOCUMENT
                    // -------------------------------------------------------------------
                    try {
                        SimpleDocument document = null;
                        try {
                            document = AmalImplServiceLocator.getSimpleDocumentService().read(
                                    simpleControleurEnvoiStatus.getIdEnvoi());
                        } catch (Exception ex) {
                            // TODO : generate exception ?
                        }
                        String idDetailFamille = "";
                        String csDocument = "";
                        if (document != null) {
                            idDetailFamille = document.getIdDetailFamille();
                            csDocument = document.getNumModele();
                        }
                        try {
                            document = AmalImplServiceLocator.getSimpleDocumentService().delete(document);
                        } catch (Exception ex) {
                            // TODO : generate exception ?
                        }
                        // -------------------------------------------------------------------
                        // si document, il peut s'agir d'un rappel >> retour LIBRA !
                        // -------------------------------------------------------------------
                        ArrayList<String> csFormulesRappel = AmalServiceLocator.getControleurRappelService()
                                .getFormulesRappel();
                        if (csFormulesRappel.contains(csDocument)) {
                            AmalServiceLocator.getControleurRappelService().rollbackRappelInProgressToRappel(
                                    idDetailFamille, csDocument);
                        }
                    } catch (JadeApplicationServiceNotAvailableException e) {
                        JadeLogger.error(this, "Error when deleting document " + e.toString());
                    }
                } else {
                    // -------------------------------------------------------------------
                    // DELETE ANNONCE
                    // -------------------------------------------------------------------
                    try {
                        SimpleAnnonce annonce = AmalImplServiceLocator.getSimpleAnnonceService().read(
                                simpleControleurEnvoiStatus.getIdAnnonce());
                        annonce = AmalImplServiceLocator.getSimpleAnnonceService().delete(annonce);
                    } catch (Exception ex) {
                        JadeLogger.error(this, "Error when deleting annonce " + ex.toString());
                    }
                }
                // -------------------------------------------------------------------
                // DELETE STATUS
                // -------------------------------------------------------------------
                SimpleControleurEnvoiStatusChecker.checkForDelete(simpleControleurEnvoiStatus);
                return (SimpleControleurEnvoiStatus) JadePersistenceManager.delete(simpleControleurEnvoiStatus);
            }
            return simpleControleurEnvoiStatus;
        } else {
            return simpleControleurEnvoiStatus;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.SimpleControleurEnvoiStatusService#delete(ch.globaz.amal
     * .business.models.controleurEnvoi.SimpleControleurEnvoiStatus)
     */
    @Override
    public SimpleControleurEnvoiStatus delete(String idStatus) throws JadePersistenceException,
            ControleurEnvoiException {
        if (JadeStringUtil.isEmpty(idStatus)) {
            throw new ControleurEnvoiException("Unable to delete a simplecontroleurenvoistatus, the id passed is null");
        }
        SimpleControleurEnvoiStatus simpleControleurEnvoiStatus = read(idStatus);
        if (simpleControleurEnvoiStatus != null) {
            String idJob = simpleControleurEnvoiStatus.getIdJob();
            simpleControleurEnvoiStatus = this.delete(simpleControleurEnvoiStatus);
            try {
                String newStatus = AmalServiceLocator.getControleurEnvoiService().generateStatus(idJob);
                SimpleControleurJob currentJob = AmalServiceLocator.getControleurEnvoiService()
                        .readSimpleControleurJob(idJob);
                if ((currentJob != null) && (currentJob.getStatusEnvoi() != null)
                        && !newStatus.equals(currentJob.getStatusEnvoi())) {
                    currentJob.setStatusEnvoi(newStatus);
                    AmalImplServiceLocator.getSimpleControleurJobService().update(currentJob);
                }
            } catch (Exception e) {
                throw new ControleurEnvoiException(e.getMessage());
            }
        }
        return simpleControleurEnvoiStatus;
    }

    /**
     * @param id
     *            ID du code système
     * 
     * @return libelle général du code système correspondant
     * 
     */
    @Override
    public String getDocumentLibelle(String csCodeUser, BSession session) {
        FWParametersSystemCodeManager cm = new FWParametersSystemCodeManager();

        String csRetour = "";

        cm.setSession(session);
        cm.setForCodeUtilisateur(csCodeUser);
        cm.setForIdGroupe("AMMODELES");
        cm.setForIdLangue(session.getIdLangue());
        try {
            cm.find();
        } catch (Exception e) {
            e.printStackTrace();
            return csRetour;
        }
        JAVector containerCS = cm.getContainer();
        if ((containerCS == null) || (containerCS.size() > 1)) {
            return csRetour;
        }
        for (Iterator it = containerCS.iterator(); it.hasNext();) {
            FWParametersCode code = (FWParametersCode) it.next();
            // Création de la string visible dans la combobox
            String documentValeur = code.getLibelle().trim();

            // String documentCodeUtilisateur = code.getCurrentCodeUtilisateur().getCodeUtilisateur().trim();
            // String csCodeLibelle = code.getCurrentCodeUtilisateur().getLibelle().trim();

            String csCodeLibelle = code.getCodeUtilisateur(session.getIdLangue()).getLibelle().trim();
            // csRetour += documentCodeUtilisateur + " - ";
            csRetour += documentValeur + " - ";
            csRetour += csCodeLibelle;
        }
        return csRetour;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.SimpleControleurEnvoiStatusService#read(java.lang.String)
     */
    @Override
    public SimpleControleurEnvoiStatus read(String idStatus) throws JadePersistenceException {
        SimpleControleurEnvoiStatus simpleControleurEnvoiStatus = new SimpleControleurEnvoiStatus();
        simpleControleurEnvoiStatus.setId(idStatus);
        return (SimpleControleurEnvoiStatus) JadePersistenceManager.read(simpleControleurEnvoiStatus);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.SimpleControleurEnvoiStatusService#search(ch.globaz.amal
     * .business.models.controleurEnvoi.SimpleControleurEnvoiStatusSearch)
     */
    @Override
    public SimpleControleurEnvoiStatusSearch search(SimpleControleurEnvoiStatusSearch search)
            throws JadePersistenceException, ControleurEnvoiException {
        if (search == null) {
            throw new ControleurEnvoiException("Unable to search a simplecontroleurenvoistatus, the id passed is null");
        }
        return (SimpleControleurEnvoiStatusSearch) JadePersistenceManager.search(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.amal.business.services.models.controleurEnvoi.SimpleControleurEnvoiStatusService#update(ch.globaz.amal
     * .business.models.controleurEnvoi.SimpleControleurEnvoiStatus)
     */
    @Override
    public SimpleControleurEnvoiStatus update(SimpleControleurEnvoiStatus simpleControleurEnvoiStatus)
            throws JadePersistenceException, JadeApplicationServiceNotAvailableException, ControleurEnvoiException,
            AnnonceException, DocumentException, ControleurJobException {
        if (simpleControleurEnvoiStatus == null) {
            throw new ControleurEnvoiException(
                    "Unable to update a simplecontroleurenvoistatus, the model passed is null");
        }
        SimpleControleurEnvoiStatusChecker.checkForUpdate(simpleControleurEnvoiStatus);
        return (SimpleControleurEnvoiStatus) JadePersistenceManager.update(simpleControleurEnvoiStatus);
    }
}
