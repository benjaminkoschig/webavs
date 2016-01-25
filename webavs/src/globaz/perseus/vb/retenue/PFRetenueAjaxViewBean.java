/**
 * 
 */
package globaz.perseus.vb.retenue;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.bean.JadeAbstractAjaxFindViewBean;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.perseus.utils.PFUserHelper;
import ch.globaz.perseus.business.models.retenue.SimpleRetenue;
import ch.globaz.perseus.business.models.retenue.SimpleRetenueSearchModel;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;

/**
 * @author LFO
 * 
 */
public class PFRetenueAjaxViewBean extends JadeAbstractAjaxFindViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // private String ERROR_ADRESS_MESSAGE = null;
    private String adressePaiement;
    private transient PFRetenueAjaxListViewBean listeSimpleRetenueAjaxListViewBean;
    private SimpleRetenue simpleRetenue = null;

    public PFRetenueAjaxViewBean() {
        super();
        simpleRetenue = new SimpleRetenue();
        listeSimpleRetenueAjaxListViewBean = new PFRetenueAjaxListViewBean();
    }

    public PFRetenueAjaxViewBean(SimpleRetenue simpleRetenue) {
        this();
        this.simpleRetenue = simpleRetenue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        simpleRetenue = PerseusImplServiceLocator.getSimpleRetenueService().create(simpleRetenue);
        listeSimpleRetenueAjaxListViewBean.find();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        simpleRetenue = PerseusImplServiceLocator.getSimpleRetenueService().delete(simpleRetenue);
        listeSimpleRetenueAjaxListViewBean.find();
    }

    @Override
    public void find() throws Exception {
        getListeSimpleRetenueAjaxListViewBean().find();
    }

    /**
     * @return the adressePaiement
     */
    public String getAdressePaiement() {
        return adressePaiement;
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return simpleRetenue;
    }

    public PFRetenueAjaxListViewBean getListeSimpleRetenueAjaxListViewBean() {
        return listeSimpleRetenueAjaxListViewBean;
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeSimpleRetenueAjaxListViewBean;
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return getListeSimpleRetenueAjaxListViewBean().getSimpleRetenueSearchModel();
    }

    public SimpleRetenue getSimpleRetenue() {
        return simpleRetenue;
    }

    @Override
    public void initList() {
        listeSimpleRetenueAjaxListViewBean = new PFRetenueAjaxListViewBean();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        simpleRetenue = PerseusImplServiceLocator.getSimpleRetenueService().read(simpleRetenue.getId());

        AdresseTiersDetail detailTiers = null;
        if (!JadeStringUtil.isEmpty(simpleRetenue.getIdTiersAdressePmt())) {
            detailTiers = PFUserHelper.getAdressePaiementAssure(simpleRetenue.getIdTiersAdressePmt(),
                    simpleRetenue.getIdDomaineApplicatif(), JACalendar.todayJJsMMsAAAA());
        }
        adressePaiement = (detailTiers != null) ? detailTiers.getAdresseFormate() : "";
    }

    /**
     * @param adressePaiement
     *            the adressePaiement to set
     */
    public void setAdressePaiement(String adressePaiement) {
        this.adressePaiement = adressePaiement;
    }

    /**
     * @param listeSimpleRetenueAjaxListViewBean
     *            the listeSimpleRetenueAjaxListViewBean to set
     */
    public void setListeSimpleRetenueAjaxListViewBean(PFRetenueAjaxListViewBean listeSimpleRetenueAjaxListViewBean) {
        this.listeSimpleRetenueAjaxListViewBean = listeSimpleRetenueAjaxListViewBean;
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeSimpleRetenueAjaxListViewBean = (PFRetenueAjaxListViewBean) fwViewBeanInterface;
    }

    public void setSearchModel(JadeAbstractSearchModel search) {
        getListeSimpleRetenueAjaxListViewBean().setSimpleRetenueSearchModel((SimpleRetenueSearchModel) search);

    }

    /**
     * @param simpleRetenue
     *            the simpleRetenue to set
     */
    public void setSimpleRetenue(SimpleRetenue simpleRetenue) {
        this.simpleRetenue = simpleRetenue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        simpleRetenue = PerseusImplServiceLocator.getSimpleRetenueService().update(simpleRetenue);
        listeSimpleRetenueAjaxListViewBean.find();

    }
}
