/**
 * 
 */
package globaz.amal.vb.documents;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JAVector;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJob;
import ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJobSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author dhi
 * 
 */
public class AMDocumentsListViewBean extends BJadePersistentObjectListViewBean {

    SimpleControleurJobSearch searchModel = null;

    /**
	 * 
	 */
    public AMDocumentsListViewBean() {
        super();
        searchModel = new SimpleControleurJobSearch();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        searchModel = AmalServiceLocator.getControleurEnvoiService().search(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#get(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < searchModel.getSize() ? new AMDocumentsViewBean(
                (SimpleControleurJob) searchModel.getSearchResults()[idx]) : new AMDocumentsViewBean();
    }

    /**
     * @param id
     *            ID du code système
     * 
     * @return libelle général du code système correspondant
     * 
     */
    public JAVector getListeDocuments() {
        FWParametersSystemCodeManager cm = new FWParametersSystemCodeManager();

        BSession currentSession = BSessionUtil.getSessionFromThreadContext();
        cm.setSession(currentSession);
        cm.setForIdGroupe("AMMODELES");
        cm.setForIdLangue(currentSession.getIdLangue());
        try {
            cm.find();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((cm == null) || (cm.getContainer() == null)) {
            return new JAVector();
        }
        JAVector containerCS = cm.getContainer();
        return containerCS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public SimpleControleurJobSearch getSearchModel() {
        return searchModel;
    }

    @Override
    public int getSize() {
        return searchModel.getSize();
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(SimpleControleurJobSearch searchModel) {
        this.searchModel = searchModel;
    }

}
