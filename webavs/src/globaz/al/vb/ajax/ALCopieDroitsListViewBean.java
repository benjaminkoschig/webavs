package globaz.al.vb.ajax;

import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.al.business.constantes.ALCSDroit;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean gérant les droits copiables pour un dossier, à partir d'un droit donné Les droits copiables sont actifs en
 * même temps que le droit donné
 * 
 * @author gmo
 * 
 */
public class ALCopieDroitsListViewBean extends BJadePersistentObjectListViewBean {

    private DroitComplexModel droitReference = null;
    private DroitComplexSearchModel searchModel = null;

    public ALCopieDroitsListViewBean() {
        super();
        droitReference = new DroitComplexModel();
        searchModel = new DroitComplexSearchModel();
    }

    @Override
    public void find() throws Exception {
        droitReference = ALServiceLocator.getDroitComplexModelService().read(searchModel.getForIdDroit());
        droitReference.getDroitModel().getDebutDroit();
        droitReference.getDroitModel().getDebutDroit();
        searchModel.setForDebutDroit(droitReference.getDroitModel().getDebutDroit());
        searchModel.setForFinDroitForcee(droitReference.getDroitModel().getFinDroitForcee());
        List<String> types = new ArrayList<String>();
        types.add(ALCSDroit.TYPE_ENF);
        types.add(ALCSDroit.TYPE_FORM);
        searchModel.setInTypeDroit(types);
        searchModel.setForIdDossier(droitReference.getDroitModel().getIdDossier());
        searchModel.setWhereKey(DroitComplexSearchModel.SEARCH_DROIT_PLAGE_INCLUSE);
        searchModel = ALServiceLocator.getDroitComplexModelService().search(searchModel);

    }

    public DroitComplexModel getDroitReference() {
        return droitReference;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    public DroitComplexSearchModel getSearchModel() {
        return searchModel;
    }

    public void setDroitReference(DroitComplexModel droitReference) {
        this.droitReference = droitReference;
    }

    public void setSearchModel(DroitComplexSearchModel searchModel) {
        this.searchModel = searchModel;
    }

}
