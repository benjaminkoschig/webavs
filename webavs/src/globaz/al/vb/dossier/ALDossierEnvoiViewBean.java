/**
 * 
 */
package globaz.al.vb.dossier;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.parameters.FWParametersCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.log.JadeLogger;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.Iterator;
import ch.globaz.al.business.constantes.ALCSEnvoi;
import ch.globaz.al.business.models.envoi.EnvoiComplexModel;
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
public class ALDossierEnvoiViewBean extends BJadePersistentObjectViewBean {

    private EnvoiComplexModel envoiComplex = null;
    private String filePath = null;

    /**
	 * 
	 */
    public ALDossierEnvoiViewBean() {
        super();
        retrieveFilePath();
    }

    /**
	 * 
	 */
    public ALDossierEnvoiViewBean(EnvoiComplexModel envoiComplex) {
        super();
        setEnvoiComplex(envoiComplex);
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
        // TODO Auto-generated method stub

    }

    /**
     * Récupération d'un libelle genre DOC001 - Lettre neutre en fonction d'un id formule
     * 
     * @param idFormule
     * @return
     */
    public String getDocumentLibelle(String idFormule) {

        String returnLibelle = "";

        if (getEnvoiComplex() == null) {
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
     * @return the envoiComplex
     */
    public EnvoiComplexModel getEnvoiComplex() {
        return envoiComplex;
    }

    /**
     * Gets the filepaht complet d'un document en édition
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
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        // TODO Auto-generated method stub

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
     * @param envoiComplex
     *            the envoiComplex to set
     */
    public void setEnvoiComplex(EnvoiComplexModel envoiComplex) {
        this.envoiComplex = envoiComplex;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        // TODO Auto-generated method stub

    }

}
