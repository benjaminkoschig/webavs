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
 * ViewBean g�rant le mod�le de recherche de dossiers li�s, utilis� dans une recherche ajax pour obtenir les dossiers
 * li�s pour un dossier p�re
 * 
 * @author GMO
 * 
 */
public class ALDossiersLiesListViewBean extends BJadePersistentObjectListViewBean {

    private HashMap<String, String> allCsTypeLien = null;

    /**
     * Mod�le de recherche dossiers li�s
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

        // Recherche de tous les codes syst�me ALLIDOS
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
     * Retourne le mod�le se trouvant � la position idx parmi les r�sultats de la recherche
     * 
     * @param idx
     *            Position du mod�le dans la liste des r�sultats
     * @return le mod�le AllocataireComplexModel voulu ou un mod�le AllocataireComplexModel vide si position non trouv�e
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
