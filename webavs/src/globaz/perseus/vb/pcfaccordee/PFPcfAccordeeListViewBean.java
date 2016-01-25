/**
 * 
 */
package globaz.perseus.vb.pcfaccordee;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordeeSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

/**
 * @author DDE
 * 
 */
public class PFPcfAccordeeListViewBean extends BJadePersistentObjectListViewBean {

    private String moisCourant = null;
    private PCFAccordeeSearchModel searchModel = null;

    /**
	 * 
	 */
    public PFPcfAccordeeListViewBean() {
        super();
        searchModel = new PCFAccordeeSearchModel();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObjectList#find()
     */
    @Override
    public void find() throws Exception {
        searchModel = PerseusServiceLocator.getPCFAccordeeService().search(searchModel);
        moisCourant = PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt();

        getISession().setAttribute("likeNss", searchModel.getLikeNss());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getEntity(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        try {
            if (idx < searchModel.getSize()) {
                if (!JadeStringUtil.isEmpty(moisCourant)) {
                    return new PFPcfAccordeeViewBean((PCFAccordee) searchModel.getSearchResults()[idx], moisCourant);
                } else {
                    return new PFPcfAccordeeViewBean((PCFAccordee) searchModel.getSearchResults()[idx]);
                }
            } else {
                return new PFPcfAccordeeViewBean();
            }
        } catch (Exception e) {
            JadeThread.logError(this.getClass().getName(),
                    "Erreur technique : envoyez un printScreen à Globaz : " + e.toString());
        }
        return new PFPcfAccordeeViewBean();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getManagerModel()
     */
    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        // TODO Auto-generated method stub
        return searchModel;
    }

    /**
     * @return the searchModel
     */
    public PCFAccordeeSearchModel getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel
     *            the searchModel to set
     */
    public void setSearchModel(PCFAccordeeSearchModel searchModel) {
        this.searchModel = searchModel;
    }

}
