/**
 * 
 */
package globaz.al.vb.envois;

import globaz.al.process.envois.ALEnvoisProcess;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.parameters.FWParametersCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.Iterator;
import ch.globaz.al.business.constantes.ALCSEnvoi;
import ch.globaz.al.business.exceptions.model.envoi.ALEnvoiJobException;
import ch.globaz.al.business.models.envoi.EnvoiComplexModel;
import ch.globaz.al.business.models.envoi.EnvoiComplexModelSearch;
import ch.globaz.al.business.models.envoi.EnvoiParametresSimpleModel;
import ch.globaz.al.business.models.envoi.EnvoiParametresSimpleModelSearch;
import ch.globaz.al.business.models.envoi.EnvoiTemplateComplexModel;
import ch.globaz.al.business.models.envoi.EnvoiTemplateComplexModelSearch;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;

/**
 * @author dhi
 * 
 */
public class ALEnvoisViewBean extends BJadePersistentObjectViewBean {

    private EnvoiComplexModel envoiComplexModel = null;
    private EnvoiComplexModelSearch envoiComplexSearch = null;
    private String filePath = null;
    private String idToDelete = null;
    private Boolean jobIsSelected = null;
    private String newStatus = null;

    /**
     * Default constructor
     */
    public ALEnvoisViewBean() {
        super();
        setJobIsSelected(true);
        envoiComplexModel = new EnvoiComplexModel();
        envoiComplexSearch = new EnvoiComplexModelSearch();
        retrieveFilePath();
    }

    /**
     * Default constructor called from list
     * 
     * @param _envoiComplex
     */
    public ALEnvoisViewBean(EnvoiComplexModel _envoiComplex) {
        super();
        setJobIsSelected(true);
        envoiComplexModel = _envoiComplex;
        retrieveFilePath();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        // double check si nous avons uniquement 1 élément dans le job, suppression du job entier
        if ((getEnvoiComplexSearch().getSize() == 1) && !getJobIsSelected()) {
            setJobIsSelected(true);
            setIdToDelete(getEnvoiComplexModel().getEnvoiJobSimpleModel().getId());
        }
        // Application de la suppression
        if (getJobIsSelected()) {
            EnvoiComplexModelSearch searchEnvois = new EnvoiComplexModelSearch();
            searchEnvois.setForIdJob(getIdToDelete());
            searchEnvois = ALServiceLocator.getEnvoiComplexService().search(searchEnvois);
            // Suppression des envois
            for (int iEnvoi = 0; iEnvoi < searchEnvois.getSize(); iEnvoi++) {
                EnvoiComplexModel currentEnvoi = (EnvoiComplexModel) searchEnvois.getSearchResults()[iEnvoi];
                ALImplServiceLocator.getEnvoiItemSimpleModelService().delete(currentEnvoi.getEnvoiItemSimpleModel());
            }
            // Suppression du job
            ALImplServiceLocator.getEnvoiJobSimpleModelService()
                    .delete(getEnvoiComplexModel().getEnvoiJobSimpleModel());
        } else {
            // --------------------------------------------------------------------------------------
            // Recherche de l'envoi sélectionné
            // --------------------------------------------------------------------------------------
            EnvoiComplexModelSearch searchEnvois = new EnvoiComplexModelSearch();
            searchEnvois.setForIdEnvoi(getIdToDelete());
            searchEnvois = ALServiceLocator.getEnvoiComplexService().search(searchEnvois);
            if (searchEnvois.getSize() == 1) {
                // --------------------------------------------------------------------------------------
                // Effacement effectif D'UN ENVOI
                // --------------------------------------------------------------------------------------
                EnvoiComplexModel currentEnvoi = (EnvoiComplexModel) searchEnvois.getSearchResults()[0];
                ALImplServiceLocator.getEnvoiItemSimpleModelService().delete(currentEnvoi.getEnvoiItemSimpleModel());
                // --------------------------------------------------------------------------------------
                // relecture pour la suite SET ID VIEWBEAN
                // --------------------------------------------------------------------------------------
                EnvoiComplexModelSearch newSearchEnvois = new EnvoiComplexModelSearch();
                newSearchEnvois.setForIdJob(getEnvoiComplexModel().getEnvoiJobSimpleModel().getId());
                newSearchEnvois = ALServiceLocator.getEnvoiComplexService().search(newSearchEnvois);
                if (newSearchEnvois.getSize() > 0) {
                    EnvoiComplexModel newCurrentEnvoi = (EnvoiComplexModel) newSearchEnvois.getSearchResults()[0];
                    setId(newCurrentEnvoi.getId());
                }
            }
        }
    }

    /**
     * Récupération d'un libelle genre DOC001 - Lettre neutre en fonction d'un id formule
     * 
     * @param idFormule
     * @return
     */
    public String getDocumentLibelle(String idFormule) {

        String returnLibelle = "";

        if (getEnvoiComplexModel() == null) {
            return returnLibelle;
        }

        // Recherche du modèle en fonction de l'idFormule
        EnvoiTemplateComplexModelSearch searchModel = new EnvoiTemplateComplexModelSearch();
        searchModel.setForIdFormule(idFormule);
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        try {
            searchModel = ALServiceLocator.getEnvoiTemplateComplexService().search(searchModel);
        } catch (Exception ex) {
            JadeLogger.error(this,
                    "Error retrieving information for formule id : " + idFormule + " >> " + ex.toString());
            return returnLibelle;
        }
        if (searchModel.getSize() != 1) {
            return returnLibelle;
        }
        EnvoiTemplateComplexModel currentModel = (EnvoiTemplateComplexModel) searchModel.getSearchResults()[0];
        // Recherche de tous les codes système alenvoidoc
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        FWParametersSystemCodeManager cm = new FWParametersSystemCodeManager();
        cm.setSession(currentSession);
        cm.setForIdGroupe("ALENVOIDOC");
        cm.setForIdLangue(currentSession.getIdLangue());
        try {
            cm.find();
        } catch (Exception ex) {
            JadeLogger.error(this,
                    "Error searching code systeme for formule id : " + idFormule + " >> " + ex.toString());
        }
        if ((cm == null) || (cm.getContainer() == null)) {
            return returnLibelle;
        }
        for (Iterator it = cm.getContainer().iterator(); it.hasNext();) {
            FWParametersCode code = (FWParametersCode) it.next();
            if (code.getId().equals(currentModel.getFormuleList().getDefinitionformule().getCsDocument())) {
                returnLibelle = code.getLibelle();
                returnLibelle += " ";
                returnLibelle += code.getCodeUtilisateur(currentSession.getIdLangue()).getLibelle();
                break;
            }
        }
        return returnLibelle;
    }

    /**
     * @return the envoiComplexModel
     */
    public EnvoiComplexModel getEnvoiComplexModel() {
        return envoiComplexModel;
    }

    /**
     * @return the envoiComplexSearch
     */
    public EnvoiComplexModelSearch getEnvoiComplexSearch() {
        return envoiComplexSearch;
    }

    /**
     * Gets the filepath complet d'un document en édition
     * 
     * @param fileName
     * @return
     */
    public String getFilePathDocument(String fileName) {
        if ((filePath != null) && (filePath.length() > 0)) {
            String originalPath = filePath + fileName;
            String finalPath = originalPath.replace("\\", "\\\\");
            return finalPath;
        } else {
            return "";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        if (envoiComplexModel != null) {
            return envoiComplexModel.getId();
        } else {
            return null;
        }
    }

    /**
     * @return the idToDelete
     */
    public String getIdToDelete() {
        return idToDelete;
    }

    /**
     * @return the jobIsSelected
     */
    public Boolean getJobIsSelected() {
        return jobIsSelected;
    }

    /**
     * Récupération du nombre de documents en fonction du job
     * 
     * @return
     */
    public String getNbDocuments() {

        EnvoiComplexModelSearch searchModel = new EnvoiComplexModelSearch();
        searchModel.setForIdJob(envoiComplexModel.getEnvoiJobSimpleModel().getIdJob());
        String nbDocuments = "0";
        try {
            nbDocuments = "" + ALServiceLocator.getEnvoiComplexService().count(searchModel);
        } catch (Exception ex) {
            JadeLogger.error(this, "Error counting element for EnvoiComplexModelSearch");
            nbDocuments = "0";
        }
        return nbDocuments;
    }

    /**
     * @return the newStatus
     */
    public String getNewStatus() {
        return newStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if (envoiComplexModel != null) {
            return new BSpy(envoiComplexModel.getEnvoiJobSimpleModel().getSpy());
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        if (!JadeStringUtil.isEmpty(getId())) {
            envoiComplexModel = ALServiceLocator.getEnvoiComplexService().read(getId());
            envoiComplexSearch = new EnvoiComplexModelSearch();
            envoiComplexSearch.setForIdJob(envoiComplexModel.getEnvoiJobSimpleModel().getId());
            envoiComplexSearch = ALServiceLocator.getEnvoiComplexService().search(envoiComplexSearch);
        }
    }

    public void retrieveFilePath() {
        EnvoiParametresSimpleModelSearch searchModel = new EnvoiParametresSimpleModelSearch();
        searchModel.setForCsTypeParametre(ALCSEnvoi.SHARED_PATH_FROM_CLIENT);
        try {
            searchModel = ALImplServiceLocator.getEnvoiParametresSimpleModelService().search(searchModel);
        } catch (Exception ex) {
            JadeLogger.error(this, "Path from Client to the shared directory not found. Exception : " + ex.toString());
        }
        if (searchModel.getSize() == 1) {
            EnvoiParametresSimpleModel currentParametres = (EnvoiParametresSimpleModel) searchModel.getSearchResults()[0];
            filePath = currentParametres.getValeurParametre();
        } else {
            JadeLogger.error(this, "Path from Client to the shared directory not found.");
        }
    }

    /**
     * @param envoiComplexModel
     *            the envoiComplexModel to set
     */
    public void setEnvoiComplexModel(EnvoiComplexModel envoiComplexModel) {
        this.envoiComplexModel = envoiComplexModel;
    }

    /**
     * @param envoiComplexSearch
     *            the envoiComplexSearch to set
     */
    public void setEnvoiComplexSearch(EnvoiComplexModelSearch envoiComplexSearch) {
        this.envoiComplexSearch = envoiComplexSearch;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        if (envoiComplexModel != null) {
            envoiComplexModel.setId(newId);
        }
    }

    /**
     * @param idToDelete
     *            the idToDelete to set
     */
    public void setIdToDelete(String idToDelete) {
        this.idToDelete = idToDelete;
    }

    /**
     * @param jobIsSelected
     *            the jobIsSelected to set
     */
    public void setJobIsSelected(Boolean jobIsSelected) {
        this.jobIsSelected = jobIsSelected;
    }

    /**
     * @param newStatus
     *            the newStatus to set
     */
    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {

        ALEnvoisProcess currentProcess = new ALEnvoisProcess();
        Boolean bError = currentProcess.initializeProcess(getEnvoiComplexModel().getEnvoiJobSimpleModel().getId(),
                getNewStatus());
        if (!bError) {
            BProcessLauncher.start(currentProcess, false);
        } else {
            throw new ALEnvoiJobException("Error initialising the envoi process, rollback will be done.");
        }
    }

}
