package globaz.ij.vb.controleAbsences;

import globaz.framework.bean.JadeAbstractAjaxCrudFindViewBean;
import globaz.jade.service.provider.application.JadeCrudService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.ij.business.models.IJPrononceJointDemande;
import ch.globaz.ij.business.models.IJPrononceJointDemandeSearchModel;
import ch.globaz.ij.business.services.IJServiceLocator;
import ch.globaz.jade.business.models.codesysteme.JadeCodeSysteme;

public class IJPrononceAjaxViewBean extends
        JadeAbstractAjaxCrudFindViewBean<IJPrononceJointDemande, IJPrononceJointDemandeSearchModel> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idPrononceSelectionne;
    private IJPrononceJointDemande prononce;
    private String prononceSelectionne;

    transient private IJPrononceJointDemandeSearchModel searchModel;

    public IJPrononceAjaxViewBean() {
        super();

        searchModel = new IJPrononceJointDemandeSearchModel();
        prononce = new IJPrononceJointDemande();
    }

    @Override
    public IJPrononceJointDemande getCurrentEntity() {
        return prononce;
    }

    public JadeCodeSysteme getGenreReadaptation() {
        return prononce.getGenreReabilitation();
    }

    public final String getIdPrononceSelectionne() {
        return idPrononceSelectionne;
    }

    public final String getPrononceSelectionne() {
        return prononceSelectionne;
    }

    @Override
    public IJPrononceJointDemandeSearchModel getSearchModel() {
        return searchModel;
    }

    @Override
    public JadeCrudService<IJPrononceJointDemande, IJPrononceJointDemandeSearchModel> getService()
            throws JadeApplicationServiceNotAvailableException {
        return IJServiceLocator.getPrononceService();
    }

    @Override
    public void initList() {
        searchModel = new IJPrononceJointDemandeSearchModel();
    }

    @Override
    public void setCurrentEntity(IJPrononceJointDemande entite) {
        prononce = entite;
    }

    public final void setIdPrononceSelectionne(String idPrononceSelectionne) {
        this.idPrononceSelectionne = idPrononceSelectionne;
    }

    public final void setPrononceSelectionne(String prononceSelectionne) {
        this.prononceSelectionne = prononceSelectionne;
    }

    @Override
    public void setSearchModel(IJPrononceJointDemandeSearchModel jadeAbstractSearchModel) {
        searchModel = jadeAbstractSearchModel;
    }
}
