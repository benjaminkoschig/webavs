/**
 * 
 */
package globaz.amal.vb.subsideannee;

import globaz.framework.bean.FWAJAXFindInterface;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.Iterator;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnnee;
import ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnneeSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

/**
 * @author CBU
 * 
 */
public class AMSubsideanneeAjaxViewBean extends BJadePersistentObjectViewBean implements FWAJAXViewBeanInterface,
        FWAJAXFindInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected boolean getListe = false;
    private transient AMSubsideanneeAjaxListViewBean listeSubsideanneeAjaxListViewBean;
    private SimpleSubsideAnnee simpleSubsideAnnee = null;

    public AMSubsideanneeAjaxViewBean() {
        super();
        simpleSubsideAnnee = new SimpleSubsideAnnee();
        listeSubsideanneeAjaxListViewBean = new AMSubsideanneeAjaxListViewBean();
    }

    public AMSubsideanneeAjaxViewBean(SimpleSubsideAnnee simpleSubsideAnnee) {
        this();
        this.simpleSubsideAnnee = simpleSubsideAnnee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        simpleSubsideAnnee = AmalServiceLocator.getSimpleSubsideAnneeService().create(simpleSubsideAnnee);
        updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        simpleSubsideAnnee = AmalServiceLocator.getSimpleSubsideAnneeService().delete(simpleSubsideAnnee);
        updateListe();
    }

    @Override
    public void find() throws Exception {
        getListeSubsideanneeAjaxListViewBean().find();
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return simpleSubsideAnnee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return simpleSubsideAnnee.getId();
    }

    public AMSubsideanneeAjaxListViewBean getListeSubsideanneeAjaxListViewBean() {
        return listeSubsideanneeAjaxListViewBean;
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeSubsideanneeAjaxListViewBean;
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return getListeSubsideanneeAjaxListViewBean().getSimpleSubsideAnneeSearch();
    }

    public SimpleSubsideAnnee getSimpleSubsideAnnee() {
        return simpleSubsideAnnee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        if (simpleSubsideAnnee != null) {
            return new BSpy(simpleSubsideAnnee.getSpy());
        } else {
            return null;
        }
    }

    @Override
    public boolean hasList() {
        return true;
    }

    @Override
    public void initList() {
        listeSubsideanneeAjaxListViewBean = new AMSubsideanneeAjaxListViewBean();
    }

    @Override
    public Iterator iterator() {
        return listeSubsideanneeAjaxListViewBean.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        simpleSubsideAnnee = AmalServiceLocator.getSimpleSubsideAnneeService().read(simpleSubsideAnnee.getId());
    }

    @Override
    public void setDefinedSearchSize(int definedSearchSize) {
        listeSubsideanneeAjaxListViewBean.getSimpleSubsideAnneeSearch().setDefinedSearchSize(definedSearchSize);
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
        simpleSubsideAnnee.setId(newId);
    }

    public void setListeSubsideanneeAjaxListViewBean(AMSubsideanneeAjaxListViewBean listeSubsideanneeAjaxListViewBean) {
        this.listeSubsideanneeAjaxListViewBean = listeSubsideanneeAjaxListViewBean;
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeSubsideanneeAjaxListViewBean = (AMSubsideanneeAjaxListViewBean) fwViewBeanInterface;
    }

    @Override
    public void setOffset(int offset) {
        listeSubsideanneeAjaxListViewBean.getSimpleSubsideAnneeSearch().setOffset(offset);
    }

    public void setSearchModel(JadeAbstractSearchModel search) {
        getListeSubsideanneeAjaxListViewBean().setSimpleSubsideAnneeSearch((SimpleSubsideAnneeSearch) search);
    }

    public void setSimpleSubsideAnnee(SimpleSubsideAnnee simpleSubsideAnnee) {
        this.simpleSubsideAnnee = simpleSubsideAnnee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        simpleSubsideAnnee = AmalServiceLocator.getSimpleSubsideAnneeService().update(simpleSubsideAnnee);
        updateListe();
    }

    /**
     * Mise à jour de la liste
     * 
     * @throws Exception
     */
    private void updateListe() throws Exception {
        if (getListe) {
            SimpleSubsideAnneeSearch search = (SimpleSubsideAnneeSearch) listeSubsideanneeAjaxListViewBean
                    .getManagerModel();

            listeSubsideanneeAjaxListViewBean.find();
        }
    }

}
