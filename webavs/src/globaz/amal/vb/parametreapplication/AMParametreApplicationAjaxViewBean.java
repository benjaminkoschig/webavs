/**
 * 
 */
package globaz.amal.vb.parametreapplication;

import globaz.framework.bean.FWAJAXFindInterface;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.Iterator;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplication;
import ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplicationSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author dhi
 * 
 */
public class AMParametreApplicationAjaxViewBean extends BJadePersistentObjectViewBean implements FWAJAXFindInterface,
        FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected boolean getListe = false;
    private transient AMParametreApplicationAjaxListViewBean listeParametreApplicationAjaxListViewBean;
    private SimpleParametreApplication simpleParametreApplication = null;

    /**
     * Default constructor
     */
    public AMParametreApplicationAjaxViewBean() {
        super();
        simpleParametreApplication = new SimpleParametreApplication();
        listeParametreApplicationAjaxListViewBean = new AMParametreApplicationAjaxListViewBean();
    }

    /**
     * Default constructor with parameters
     * 
     * @param paramApplication
     */
    public AMParametreApplicationAjaxViewBean(SimpleParametreApplication paramApplication) {
        this();
        simpleParametreApplication = paramApplication;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        simpleParametreApplication = AmalServiceLocator.getSimpleParametreApplicationService().create(
                simpleParametreApplication);
        updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        simpleParametreApplication = AmalServiceLocator.getSimpleParametreApplicationService().delete(
                simpleParametreApplication);
        updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#find()
     */
    @Override
    public void find() throws Exception {
        listeParametreApplicationAjaxListViewBean.find();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#getCurrentEntity()
     */
    @Override
    public JadeAbstractModel getCurrentEntity() {
        return simpleParametreApplication;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return simpleParametreApplication.getId();
    }

    /**
     * @return the listeParametreApplicationAjaxListViewBean
     */
    public AMParametreApplicationAjaxListViewBean getListeParametreApplicationAjaxListViewBean() {
        return listeParametreApplicationAjaxListViewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeParametreApplicationAjaxListViewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#getSearchModel()
     */
    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return listeParametreApplicationAjaxListViewBean.getSimpleParametreApplicationSearch();
    }

    /**
     * @return the simpleParametreApplication
     */
    public SimpleParametreApplication getSimpleParametreApplication() {
        return simpleParametreApplication;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if (simpleParametreApplication != null) {
            return new BSpy(simpleParametreApplication.getSpy());
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
        listeParametreApplicationAjaxListViewBean = new AMParametreApplicationAjaxListViewBean();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeParametreApplicationAjaxListViewBean.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        simpleParametreApplication = AmalServiceLocator.getSimpleParametreApplicationService().read(
                simpleParametreApplication.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#setDefinedSearchSize(int)
     */
    @Override
    public void setDefinedSearchSize(int definedSearchSize) {
        listeParametreApplicationAjaxListViewBean.getSimpleParametreApplicationSearch().setDefinedSearchSize(
                definedSearchSize);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXViewBeanInterface#setGetListe(boolean)
     */
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
        simpleParametreApplication.setId(newId);
    }

    /**
     * @param listeParametreApplicationAjaxListViewBean
     *            the listeParametreApplicationAjaxListViewBean to set
     */
    public void setListeParametreApplicationAjaxListViewBean(
            AMParametreApplicationAjaxListViewBean listeParametreApplicationAjaxListViewBean) {
        this.listeParametreApplicationAjaxListViewBean = listeParametreApplicationAjaxListViewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXViewBeanInterface#setListViewBean(globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeParametreApplicationAjaxListViewBean = (AMParametreApplicationAjaxListViewBean) fwViewBeanInterface;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#setOffset(int)
     */
    @Override
    public void setOffset(int offset) {
        listeParametreApplicationAjaxListViewBean.getSimpleParametreApplicationSearch().setOffset(offset);
    }

    /**
     * @param simpleParametreApplication
     *            the simpleParametreApplication to set
     */
    public void setSimpleParametreApplication(SimpleParametreApplication simpleParametreApplication) {
        this.simpleParametreApplication = simpleParametreApplication;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        simpleParametreApplication = AmalServiceLocator.getSimpleParametreApplicationService().update(
                simpleParametreApplication);
        updateListe();
    }

    /**
     * Update the liste
     * 
     * @throws Exception
     */
    private void updateListe() throws Exception {
        if (getListe) {
            SimpleParametreApplicationSearch search = (SimpleParametreApplicationSearch) listeParametreApplicationAjaxListViewBean
                    .getManagerModel();

            listeParametreApplicationAjaxListViewBean.find();
        }
    }

}
