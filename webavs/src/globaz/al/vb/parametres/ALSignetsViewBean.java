/**
 * 
 */
package globaz.al.vb.parametres;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleList;
import ch.globaz.envoi.business.models.parametrageEnvoi.FormuleListSearch;
import ch.globaz.envoi.business.models.parametrageEnvoi.SignetListModel;
import ch.globaz.envoi.business.models.parametrageEnvoi.SignetListModelSearch;
import ch.globaz.envoi.business.services.ENServiceLocator;

/**
 * ViewBean d'une entité SignetListModel
 * 
 * @author dhi
 * 
 */
public class ALSignetsViewBean extends BJadePersistentObjectViewBean {

    private SignetListModel signetModel = null;

    /**
     * Default constructor
     */
    public ALSignetsViewBean() {
        super();
        signetModel = new SignetListModel();
    }

    /**
     * Constructor called from list view bean
     */
    public ALSignetsViewBean(SignetListModel signetListModel) {
        super();
        signetModel = signetListModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        signetModel = ENServiceLocator.getSignetListModelService().create(signetModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        signetModel = ENServiceLocator.getSignetListModelService().delete(signetModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        if (signetModel != null) {
            return signetModel.getSimpleSignetModel().getId();
        } else {
            return null;
        }
    }

    /**
     * @return the signetModel
     */
    public SignetListModel getSignetModel() {
        return signetModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if (signetModel != null) {
            return new BSpy(signetModel.getSimpleSignetModel().getSpy());
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
        if (signetModel != null) {
            SignetListModelSearch searchModel = new SignetListModelSearch();
            searchModel.setForIdSignet(signetModel.getSimpleSignetModel().getId());
            searchModel = ENServiceLocator.getSignetListModelService().search(searchModel);
            if (searchModel.getSize() == 1) {
                signetModel = (SignetListModel) searchModel.getSearchResults()[0];
            } else {
                signetModel = null;
            }
        }
    }

    public void retrieveFormuleList(String idFormule) throws Exception {
        FormuleListSearch searchModel = new FormuleListSearch();
        searchModel.setForIdFormule(idFormule);
        searchModel = ENServiceLocator.getFormuleListService().search(searchModel);
        if (searchModel.getSize() == 1) {
            if (signetModel == null) {
                signetModel = new SignetListModel();
            }
            signetModel.setFormuleList((FormuleList) searchModel.getSearchResults()[0]);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        if (signetModel != null) {
            signetModel.getSimpleSignetModel().setId(newId);
        }
    }

    /**
     * @param signetModel
     *            the signetModel to set
     */
    public void setSignetModel(SignetListModel signetModel) {
        this.signetModel = signetModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        signetModel = ENServiceLocator.getSignetListModelService().update(signetModel);
    }

}
