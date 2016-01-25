package globaz.phenix.db.listes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.external.ServicesFacturation;
import globaz.phenix.application.CPApplication;
import globaz.phenix.process.listes.CPProcessListeDecisionsPrincipale;

public class CPListeDecisionsViewBean extends CPProcessListeDecisionsPrincipale implements BIPersistentObject,
        FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id = null;
    private String libellePassage = "";

    // Constructeur
    public CPListeDecisionsViewBean() throws Exception {
        super(new BSession(CPApplication.DEFAULT_APPLICATION_PHENIX));
    }

    public void _init() {
        try {
            globaz.musca.api.IFAPassage passage = null;
            // Recherche si séparation indépendant et non-actif - Inforom 314s
            Boolean isSeprationIndNac = false;
            try {
                isSeprationIndNac = new Boolean(GlobazSystem.getApplication(FAApplication.DEFAULT_APPLICATION_MUSCA)
                        .getProperty(FAApplication.SEPARATION_IND_NA));
            } catch (Exception e) {
                isSeprationIndNac = Boolean.FALSE;
            }
            if (isSeprationIndNac) {
                passage = ServicesFacturation.getDernierPassageFacturation(getSession(), getTransaction(),
                        FAModuleFacturation.CS_MODULE_COT_PERS_IND + ", " + FAModuleFacturation.CS_MODULE_COT_PERS_NAC);
            } else {
                passage = ServicesFacturation.getDernierPassageFacturation(getSession(), getTransaction(),
                        globaz.musca.db.facturation.FAModuleFacturation.CS_MODULE_COT_PERS);
            }
            if (passage != null) {
                setIdPassage(passage.getIdPassage());
                setLibellePassage(passage.getLibelle());
            } else {
                setIdPassage("");
                setLibellePassage("");
            }
            setSendMailOnError(true);
            setControleTransaction(true);
        } catch (Exception e) {
            this._addError(getTransaction(), e.getMessage());
            setIdPassage("");
            setLibellePassage("");
        }
    }

    @Override
    protected void _validate() throws Exception {
        if (JadeStringUtil.isEmpty(getEMailAddress())) {
            this._addError(getSession().getLabel("CP_MSG_0145"));
        }
    }

    @Override
    public void add() throws Exception {
    }

    @Override
    public void delete() throws Exception {
    }

    @Override
    public String getId() {
        return id;
    }

    // Getter
    public String getLibellePassage() {
        return libellePassage;
    }

    @Override
    public void retrieve() throws Exception {
    }

    @Override
    public void setId(String newId) {
        id = newId;
    }

    // Setter
    public void setLibellePassage(String newLibellePassage) {
        libellePassage = newLibellePassage;
    }

    @Override
    public void update() throws Exception {
    }
}
