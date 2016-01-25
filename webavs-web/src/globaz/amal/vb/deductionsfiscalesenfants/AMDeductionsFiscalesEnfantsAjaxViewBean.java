/**
 * 
 */
package globaz.amal.vb.deductionsfiscalesenfants;

import globaz.framework.bean.FWAJAXFindInterface;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.Iterator;
import ch.globaz.amal.business.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfants;
import ch.globaz.amal.business.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfantsSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author CBU
 * 
 */
public class AMDeductionsFiscalesEnfantsAjaxViewBean extends BJadePersistentObjectViewBean implements
        FWAJAXFindInterface, FWAJAXViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected boolean getListe = false;
    private transient AMDeductionsFiscalesEnfantsAjaxListViewBean listeDeductionsFiscalesEnfantsAjaxListViewBean;
    private SimpleDeductionsFiscalesEnfants simpleDeductionsFiscalesEnfants = null;

    public AMDeductionsFiscalesEnfantsAjaxViewBean() {
        super();
        simpleDeductionsFiscalesEnfants = new SimpleDeductionsFiscalesEnfants();
        listeDeductionsFiscalesEnfantsAjaxListViewBean = new AMDeductionsFiscalesEnfantsAjaxListViewBean();
    }

    public AMDeductionsFiscalesEnfantsAjaxViewBean(SimpleDeductionsFiscalesEnfants _simpleDeductionsFiscalesEnfants) {
        this();
        simpleDeductionsFiscalesEnfants = _simpleDeductionsFiscalesEnfants;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        simpleDeductionsFiscalesEnfants = AmalServiceLocator.getDeductionsFiscalesEnfantsService().create(
                simpleDeductionsFiscalesEnfants);
        updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        simpleDeductionsFiscalesEnfants = AmalServiceLocator.getDeductionsFiscalesEnfantsService().delete(
                simpleDeductionsFiscalesEnfants);
        updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#find()
     */
    @Override
    public void find() throws Exception {
        getListeDeductionsFiscalesEnfantsAjaxListViewBean().find();
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return simpleDeductionsFiscalesEnfants;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return getSimpleDeductionsFiscalesEnfants().getId();
    }

    public AMDeductionsFiscalesEnfantsAjaxListViewBean getListeDeductionsFiscalesEnfantsAjaxListViewBean() {
        return listeDeductionsFiscalesEnfantsAjaxListViewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeDeductionsFiscalesEnfantsAjaxListViewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#getSearchModel()
     */
    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return getListeDeductionsFiscalesEnfantsAjaxListViewBean().getSimpleDeductionsFiscalesEnfantsSearch();
    }

    /**
     * @return the simpleDeductionsFiscalesEnfants
     */
    public SimpleDeductionsFiscalesEnfants getSimpleDeductionsFiscalesEnfants() {
        return simpleDeductionsFiscalesEnfants;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if (simpleDeductionsFiscalesEnfants != null) {
            return new BSpy(simpleDeductionsFiscalesEnfants.getSpy());
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
        listeDeductionsFiscalesEnfantsAjaxListViewBean = new AMDeductionsFiscalesEnfantsAjaxListViewBean();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeDeductionsFiscalesEnfantsAjaxListViewBean.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        simpleDeductionsFiscalesEnfants = AmalServiceLocator.getDeductionsFiscalesEnfantsService().read(
                simpleDeductionsFiscalesEnfants.getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#setDefinedSearchSize(int)
     */
    @Override
    public void setDefinedSearchSize(int definedSearchSize) {
        listeDeductionsFiscalesEnfantsAjaxListViewBean.getSimpleDeductionsFiscalesEnfantsSearch().setDefinedSearchSize(
                definedSearchSize);
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
        simpleDeductionsFiscalesEnfants.setId(newId);
    }

    public void setListeDeductionsFiscalesEnfantsAjaxListViewBean(
            AMDeductionsFiscalesEnfantsAjaxListViewBean _listeDeductionsFiscalesEnfantsAjaxListViewBean) {
        listeDeductionsFiscalesEnfantsAjaxListViewBean = _listeDeductionsFiscalesEnfantsAjaxListViewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXViewBeanInterface#setListViewBean(globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeDeductionsFiscalesEnfantsAjaxListViewBean = (AMDeductionsFiscalesEnfantsAjaxListViewBean) fwViewBeanInterface;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.bean.FWAJAXFindInterface#setOffset(int)
     */
    @Override
    public void setOffset(int offset) {
        listeDeductionsFiscalesEnfantsAjaxListViewBean.getSimpleDeductionsFiscalesEnfantsSearch().setOffset(offset);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * globaz.framework.bean.FWAJAXFindInterface#setSearchModel(globaz.jade.persistence.model.JadeAbstractSearchModel)
     */
    public void setSearchModel(JadeAbstractSearchModel search) {
        listeDeductionsFiscalesEnfantsAjaxListViewBean
                .setSimpleDeductionsFiscalesEnfantsSearch((SimpleDeductionsFiscalesEnfantsSearch) search);
    }

    /**
     * @param simpleDeductionsFiscalesEnfants
     *            the simpleDeductionsFiscalesEnfants to set
     */
    public void setSimpleDeductionsFiscalesEnfants(SimpleDeductionsFiscalesEnfants simpleDeductionsFiscalesEnfants) {
        this.simpleDeductionsFiscalesEnfants = simpleDeductionsFiscalesEnfants;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        simpleDeductionsFiscalesEnfants = AmalServiceLocator.getDeductionsFiscalesEnfantsService().update(
                simpleDeductionsFiscalesEnfants);
        updateListe();
    }

    private void updateListe() throws Exception {
        if (getListe) {
            SimpleDeductionsFiscalesEnfantsSearch search = (SimpleDeductionsFiscalesEnfantsSearch) listeDeductionsFiscalesEnfantsAjaxListViewBean
                    .getManagerModel();

            listeDeductionsFiscalesEnfantsAjaxListViewBean.find();
        }
    }

}
