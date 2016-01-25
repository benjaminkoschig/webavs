package globaz.al.vb.ajax;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.HashMap;
import ch.globaz.al.business.models.dossier.DossierLieComplexModel;
import ch.globaz.al.business.models.dossier.DossierLieComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean gérant le modèle de recherche de dossiers liés, utilisé dans une recherche ajax pour obtenir les dossiers
 * liés pour un dossier père
 * 
 * @author GMO
 * 
 */
public class ALDossiersLiesListViewBean extends BJadePersistentObjectListViewBean {

    private HashMap<String, String> allCsTypeLien = null;

    /**
     * Modèle de recherche dossiers liés
     */
    private DossierLieComplexSearchModel dossierLieComplexSearchModel = null;

    private String idDossier = null;

    /**
     * Constructeur du listViewBean
     */
    public ALDossiersLiesListViewBean() {
        super();
        dossierLieComplexSearchModel = new DossierLieComplexSearchModel();

    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        dossierLieComplexSearchModel = ALServiceLocator.getDossierBusinessService().getDossiersLies(idDossier);

        // Recherche de tous les codes système ALLIDOS
        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        FWParametersSystemCodeManager cm = new FWParametersSystemCodeManager();
        cm.setSession(currentSession);
        cm.setForIdGroupe("ALLIDOS");
        cm.setForIdLangue(currentSession.getIdLangue());
        try {
            cm.find();
        } catch (Exception ex) {
            setMsgType(FWViewBeanInterface.ERROR);
            setMessage("Error listing code systems for group ALLIDOS : " + ex.toString());
            // JadeLogger.error(this, "Error listing code systems for group ALLIDOS : " + ex.toString());
        }

        allCsTypeLien = new HashMap<String, String>();
        for (int i = 0; i < cm.getSize(); i++) {

            FWParametersSystemCode cs = (FWParametersSystemCode) cm.get(i);
            allCsTypeLien.put(cs.getIdCode(), cs.getLibelle());

        }

    }

    public HashMap<String, String> getAllCsTypeLien() {
        return allCsTypeLien;
    }

    /**
     * Retourne le modèle se trouvant à la position idx parmi les résultats de la recherche
     * 
     * @param idx
     *            Position du modèle dans la liste des résultats
     * @return le modèle AllocataireComplexModel voulu ou un modèle AllocataireComplexModel vide si position non trouvée
     */
    public DossierLieComplexModel getDossierLieComplexResult(int idx) {
        return idx < getCount() ? (DossierLieComplexModel) getManagerModel().getSearchResults()[idx]
                : new DossierLieComplexModel();
    }

    public DossierLieComplexSearchModel getDossierLieComplexSearchModel() {
        return dossierLieComplexSearchModel;
    }

    public String getIdDossier() {
        return idDossier;
    }

    /**
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return dossierLieComplexSearchModel;
    }

    public void setAllCsTypeLien(HashMap<String, String> allCsTypeLien) {
        this.allCsTypeLien = allCsTypeLien;
    }

    public void setDossierLieComplexSearchModel(DossierLieComplexSearchModel dossierLieComplexSearchModel) {
        this.dossierLieComplexSearchModel = dossierLieComplexSearchModel;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

}
