package globaz.amal.vb.primeavantageuse;

import globaz.framework.bean.FWAJAXFindInterface;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.Iterator;
import ch.globaz.amal.business.models.primeavantageuse.SimplePrimeAvantageuse;
import ch.globaz.amal.business.models.primeavantageuse.SimplePrimeAvantageuseSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author CBU
 * 
 */
public class AMPrimeAvantageuseAjaxViewBean extends BJadePersistentObjectViewBean implements FWAJAXFindInterface,
        FWAJAXViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected boolean getListe = false;
    private transient AMPrimeAvantageuseAjaxListViewBean listePrimeAvantageuseAjaxListViewBean;
    private SimplePrimeAvantageuse simplePrimeAvantageuse = null;

    public AMPrimeAvantageuseAjaxViewBean() {
        super();
        simplePrimeAvantageuse = new SimplePrimeAvantageuse();
        listePrimeAvantageuseAjaxListViewBean = new AMPrimeAvantageuseAjaxListViewBean();
    }

    public AMPrimeAvantageuseAjaxViewBean(SimplePrimeAvantageuse simplePrimeAvantageuse) {
        this();
        this.simplePrimeAvantageuse = simplePrimeAvantageuse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        simplePrimeAvantageuse = AmalServiceLocator.getSimplePrimeAvantageuseService().create(simplePrimeAvantageuse);
        updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        simplePrimeAvantageuse = AmalServiceLocator.getSimplePrimeAvantageuseService().delete(simplePrimeAvantageuse);
        updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#find()
     */
    @Override
    public void find() throws Exception {
        getListePrimeAvantageuseAjaxListViewBean().find();
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return simplePrimeAvantageuse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return getSimplePrimeAvantageuse().getId();
    }

    public AMPrimeAvantageuseAjaxListViewBean getListePrimeAvantageuseAjaxListViewBean() {
        return listePrimeAvantageuseAjaxListViewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listePrimeAvantageuseAjaxListViewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#getSearchModel()
     */
    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return getListePrimeAvantageuseAjaxListViewBean().getSimplePrimeAvantageuseSearch();
    }

    public SimplePrimeAvantageuse getSimplePrimeAvantageuse() {
        return simplePrimeAvantageuse;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if (simplePrimeAvantageuse != null) {
            return new BSpy(simplePrimeAvantageuse.getSpy());
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
        listePrimeAvantageuseAjaxListViewBean = new AMPrimeAvantageuseAjaxListViewBean();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listePrimeAvantageuseAjaxListViewBean.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        simplePrimeAvantageuse = AmalServiceLocator.getSimplePrimeAvantageuseService().read(
                simplePrimeAvantageuse.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#setDefinedSearchSize(int)
     */
    @Override
    public void setDefinedSearchSize(int definedSearchSize) {
        listePrimeAvantageuseAjaxListViewBean.getSimplePrimeAvantageuseSearch().setDefinedSearchSize(definedSearchSize);
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
        simplePrimeAvantageuse.setId(newId);
    }

    public void setListePrimeAvantageuseAjaxListViewBean(
            AMPrimeAvantageuseAjaxListViewBean listePrimeAvantageuseAjaxListViewBean) {
        this.listePrimeAvantageuseAjaxListViewBean = listePrimeAvantageuseAjaxListViewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXViewBeanInterface#setListViewBean(globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listePrimeAvantageuseAjaxListViewBean = (AMPrimeAvantageuseAjaxListViewBean) fwViewBeanInterface;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#setOffset(int)
     */
    @Override
    public void setOffset(int offset) {
        listePrimeAvantageuseAjaxListViewBean.getSimplePrimeAvantageuseSearch().setOffset(offset);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.framework.bean.FWAJAXFindInterface#setSearchModel(globaz.jade.persistence.model.JadeAbstractSearchModel)
     */
    public void setSearchModel(JadeAbstractSearchModel search) {
        listePrimeAvantageuseAjaxListViewBean.setSimplePrimeAvantageuseSearch((SimplePrimeAvantageuseSearch) search);
    }

    public void setSimplePrimeAvantageuse(SimplePrimeAvantageuse simpleParametreAnnuel) {
        simplePrimeAvantageuse = simpleParametreAnnuel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        simplePrimeAvantageuse = AmalServiceLocator.getSimplePrimeAvantageuseService().update(simplePrimeAvantageuse);
        updateListe();
    }

    private void updateListe() throws Exception {
        if (getListe) {
            SimplePrimeAvantageuseSearch search = (SimplePrimeAvantageuseSearch) listePrimeAvantageuseAjaxListViewBean
                    .getManagerModel();

            listePrimeAvantageuseAjaxListViewBean.find();
        }
    }

}
