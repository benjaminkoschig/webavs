/**
 * 
 */
package globaz.amal.vb.parametreannuel;

import globaz.framework.bean.FWAJAXFindInterface;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.Iterator;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuel;
import ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuelSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.utils.parametres.ParametresAnnuelsProvider;

/**
 * @author CBU
 * 
 */
public class AMParametreAnnuelAjaxViewBean extends BJadePersistentObjectViewBean implements FWAJAXFindInterface,
        FWAJAXViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected boolean getListe = false;
    private transient AMParametreAnnuelAjaxListViewBean listeParametreAnnuelAjaxListViewBean;
    private SimpleParametreAnnuel simpleParametreAnnuel = null;

    public AMParametreAnnuelAjaxViewBean() {
        super();
        simpleParametreAnnuel = new SimpleParametreAnnuel();
        listeParametreAnnuelAjaxListViewBean = new AMParametreAnnuelAjaxListViewBean();
    }

    public AMParametreAnnuelAjaxViewBean(SimpleParametreAnnuel simpleParametreAnnuel) {
        this();
        this.simpleParametreAnnuel = simpleParametreAnnuel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        simpleParametreAnnuel = AmalServiceLocator.getParametreAnnuelService().create(simpleParametreAnnuel);
        updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        simpleParametreAnnuel = AmalServiceLocator.getParametreAnnuelService().delete(simpleParametreAnnuel);
        updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#find()
     */
    @Override
    public void find() throws Exception {
        getListeParametreAnnuelAjaxListViewBean().find();
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return simpleParametreAnnuel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return getSimpleParametreAnnuel().getId();
    }

    public AMParametreAnnuelAjaxListViewBean getListeParametreAnnuelAjaxListViewBean() {
        return listeParametreAnnuelAjaxListViewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeParametreAnnuelAjaxListViewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#getSearchModel()
     */
    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return getListeParametreAnnuelAjaxListViewBean().getSimpleParametreAnnuelSearch();
    }

    public SimpleParametreAnnuel getSimpleParametreAnnuel() {
        return simpleParametreAnnuel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if (simpleParametreAnnuel != null) {
            return new BSpy(simpleParametreAnnuel.getSpy());
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
        listeParametreAnnuelAjaxListViewBean = new AMParametreAnnuelAjaxListViewBean();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeParametreAnnuelAjaxListViewBean.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        simpleParametreAnnuel = AmalServiceLocator.getParametreAnnuelService().read(simpleParametreAnnuel.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#setDefinedSearchSize(int)
     */
    @Override
    public void setDefinedSearchSize(int definedSearchSize) {
        listeParametreAnnuelAjaxListViewBean.getSimpleParametreAnnuelSearch().setDefinedSearchSize(definedSearchSize);
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
        simpleParametreAnnuel.setId(newId);
    }

    public void setListeParametreAnnuelAjaxListViewBean(
            AMParametreAnnuelAjaxListViewBean listeParametreAnnuelAjaxListViewBean) {
        this.listeParametreAnnuelAjaxListViewBean = listeParametreAnnuelAjaxListViewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXViewBeanInterface#setListViewBean(globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeParametreAnnuelAjaxListViewBean = (AMParametreAnnuelAjaxListViewBean) fwViewBeanInterface;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#setOffset(int)
     */
    @Override
    public void setOffset(int offset) {
        listeParametreAnnuelAjaxListViewBean.getSimpleParametreAnnuelSearch().setOffset(offset);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.framework.bean.FWAJAXFindInterface#setSearchModel(globaz.jade.persistence.model.JadeAbstractSearchModel)
     */
    public void setSearchModel(JadeAbstractSearchModel search) {
        listeParametreAnnuelAjaxListViewBean.setSimpleParametreAnnuelSearch((SimpleParametreAnnuelSearch) search);
    }

    public void setSimpleParametreAnnuel(SimpleParametreAnnuel simpleParametreAnnuel) {
        this.simpleParametreAnnuel = simpleParametreAnnuel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        simpleParametreAnnuel = AmalServiceLocator.getParametreAnnuelService().update(simpleParametreAnnuel);
        updateListe();
    }

    /**
     * @throws Exception
     */
    private void updateListe() throws Exception {
        if (getListe) {
            SimpleParametreAnnuelSearch search = (SimpleParametreAnnuelSearch) listeParametreAnnuelAjaxListViewBean
                    .getManagerModel();

            listeParametreAnnuelAjaxListViewBean.find();
        }
        // On efface le "cache" des paramètres annuels
        ParametresAnnuelsProvider.resetParametersCache();
        // Puis on reconstruit
        ParametresAnnuelsProvider.initParamAnnuels();
    }

}
