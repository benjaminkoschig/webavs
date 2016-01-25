/**
 * 
 * @author DHI
 * 
 */
package globaz.amal.vb.primemoyenne;

import globaz.framework.bean.FWAJAXFindInterface;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.Iterator;
import ch.globaz.amal.business.models.primemoyenne.SimplePrimeMoyenne;
import ch.globaz.amal.business.models.primemoyenne.SimplePrimeMoyenneSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author DHI
 * 
 */
public class AMPrimeMoyenneAjaxViewBean extends BJadePersistentObjectViewBean implements FWAJAXFindInterface,
        FWAJAXViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected boolean getListe = false;
    private transient AMPrimeMoyenneAjaxListViewBean listePrimeMoyenneAjaxListViewBean;
    private SimplePrimeMoyenne simplePrimeMoyenne = null;

    public AMPrimeMoyenneAjaxViewBean() {
        super();
        simplePrimeMoyenne = new SimplePrimeMoyenne();
        listePrimeMoyenneAjaxListViewBean = new AMPrimeMoyenneAjaxListViewBean();
    }

    public AMPrimeMoyenneAjaxViewBean(SimplePrimeMoyenne simplePrimeMoyenne) {
        this();
        this.simplePrimeMoyenne = simplePrimeMoyenne;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        simplePrimeMoyenne = AmalServiceLocator.getSimplePrimeMoyenneService().create(simplePrimeMoyenne);
        updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        simplePrimeMoyenne = AmalServiceLocator.getSimplePrimeMoyenneService().delete(simplePrimeMoyenne);
        updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#find()
     */
    @Override
    public void find() throws Exception {
        getListePrimeMoyenneAjaxListViewBean().find();
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return simplePrimeMoyenne;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return getSimplePrimeMoyenne().getId();
    }

    public AMPrimeMoyenneAjaxListViewBean getListePrimeMoyenneAjaxListViewBean() {
        return listePrimeMoyenneAjaxListViewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listePrimeMoyenneAjaxListViewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#getSearchModel()
     */
    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return getListePrimeMoyenneAjaxListViewBean().getSimplePrimeMoyenneSearch();
    }

    public SimplePrimeMoyenne getSimplePrimeMoyenne() {
        return simplePrimeMoyenne;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if (simplePrimeMoyenne != null) {
            return new BSpy(simplePrimeMoyenne.getSpy());
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXViewBeanInterface#hasList()
     */
    @Override
    public boolean hasList() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#initList()
     */
    @Override
    public void initList() {
        listePrimeMoyenneAjaxListViewBean = new AMPrimeMoyenneAjaxListViewBean();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listePrimeMoyenneAjaxListViewBean.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        simplePrimeMoyenne = AmalServiceLocator.getSimplePrimeMoyenneService().read(simplePrimeMoyenne.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#setDefinedSearchSize(int)
     */
    @Override
    public void setDefinedSearchSize(int definedSearchSize) {
        listePrimeMoyenneAjaxListViewBean.getSimplePrimeMoyenneSearch().setDefinedSearchSize(definedSearchSize);
    }

    @Override
    public void setGetListe(boolean getListe) {
        this.getListe = getListe;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        simplePrimeMoyenne.setId(newId);
    }

    public void setListePrimeMoyenneAjaxListViewBean(AMPrimeMoyenneAjaxListViewBean listePrimeMoyenneAjaxListViewBean) {
        this.listePrimeMoyenneAjaxListViewBean = listePrimeMoyenneAjaxListViewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXViewBeanInterface#setListViewBean(globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listePrimeMoyenneAjaxListViewBean = (AMPrimeMoyenneAjaxListViewBean) fwViewBeanInterface;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#setOffset(int)
     */
    @Override
    public void setOffset(int offset) {
        listePrimeMoyenneAjaxListViewBean.getSimplePrimeMoyenneSearch().setOffset(offset);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.framework.bean.FWAJAXFindInterface#setSearchModel(globaz.jade.persistence.model.JadeAbstractSearchModel)
     */
    public void setSearchModel(JadeAbstractSearchModel search) {
        listePrimeMoyenneAjaxListViewBean.setSimplePrimeMoyenneSearch((SimplePrimeMoyenneSearch) search);
    }

    public void setSimplePrimeMoyenne(SimplePrimeMoyenne simpleParametreAnnuel) {
        simplePrimeMoyenne = simpleParametreAnnuel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        simplePrimeMoyenne = AmalServiceLocator.getSimplePrimeMoyenneService().update(simplePrimeMoyenne);
        updateListe();
    }

    private void updateListe() throws Exception {
        if (getListe) {
            SimplePrimeMoyenneSearch search = (SimplePrimeMoyenneSearch) listePrimeMoyenneAjaxListViewBean
                    .getManagerModel();

            listePrimeMoyenneAjaxListViewBean.find();
        }
    }

}
