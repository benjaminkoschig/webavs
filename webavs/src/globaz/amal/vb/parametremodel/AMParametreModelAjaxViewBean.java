package globaz.amal.vb.parametremodel;

import globaz.framework.bean.FWAJAXFindInterface;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.Iterator;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplex;
import ch.globaz.amal.business.models.parametremodel.ParametreModelComplexSearch;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMParametreModelAjaxViewBean extends BJadePersistentObjectViewBean implements FWAJAXViewBeanInterface,
        FWAJAXFindInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected boolean getListe = false;
    private transient AMParametreModelAjaxListViewBean listeParametremodelAjaxListViewBean;
    private ParametreModelComplex parametreModelComplex = null;

    public AMParametreModelAjaxViewBean() {
        super();
        parametreModelComplex = new ParametreModelComplex();
        listeParametremodelAjaxListViewBean = new AMParametreModelAjaxListViewBean();
    }

    public AMParametreModelAjaxViewBean(ParametreModelComplex parametreModelComplex) {
        this();
        this.parametreModelComplex = parametreModelComplex;
    }

    @Override
    public void add() throws Exception {
        parametreModelComplex = AmalServiceLocator.getParametreModelService().create(parametreModelComplex);
        updateListe();
    }

    @Override
    public void delete() throws Exception {
        parametreModelComplex = AmalServiceLocator.getParametreModelService().delete(parametreModelComplex);
        updateListe();
    }

    @Override
    public void find() throws Exception {
        getListeParametremodelAjaxListViewBean().find();
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return parametreModelComplex;
    }

    @Override
    public String getId() {
        return getParametreModelComplex().getId();
    }

    public AMParametreModelAjaxListViewBean getListeParametremodelAjaxListViewBean() {
        return listeParametremodelAjaxListViewBean;
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeParametremodelAjaxListViewBean;
    }

    public ParametreModelComplex getParametreModelComplex() {
        return parametreModelComplex;
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return getListeParametremodelAjaxListViewBean().getParametreModelComplexSearch();
    }

    @Override
    public BSpy getSpy() {
        if ((parametreModelComplex != null) && (parametreModelComplex.getSimpleParametreModel() != null)) {
            return new BSpy(parametreModelComplex.getSpy());
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
        listeParametremodelAjaxListViewBean = new AMParametreModelAjaxListViewBean();
    }

    @Override
    public Iterator iterator() {
        return listeParametremodelAjaxListViewBean.iterator();
    }

    @Override
    public void retrieve() throws Exception {
        parametreModelComplex = AmalServiceLocator.getParametreModelService().read(parametreModelComplex.getId());
    }

    @Override
    public void setDefinedSearchSize(int definedSearchSize) {
        listeParametremodelAjaxListViewBean.getParametreModelComplexSearch().setDefinedSearchSize(definedSearchSize);
    }

    @Override
    public void setGetListe(boolean getListe) {
        this.getListe = getListe;
    }

    @Override
    public void setId(String newId) {
        parametreModelComplex.setId(newId);
    }

    public void setListeParametremodelAjaxListViewBean(
            AMParametreModelAjaxListViewBean listeParametremodelAjaxListViewBean) {
        this.listeParametremodelAjaxListViewBean = listeParametremodelAjaxListViewBean;
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeParametremodelAjaxListViewBean = (AMParametreModelAjaxListViewBean) fwViewBeanInterface;
    }

    @Override
    public void setOffset(int offset) {
        listeParametremodelAjaxListViewBean.getParametreModelComplexSearch().setOffset(offset);
    }

    public void setParametreModelComplex(ParametreModelComplex parametreModelComplex) {
        this.parametreModelComplex = parametreModelComplex;
    }

    public void setSearchModel(JadeAbstractSearchModel search) {
        getListeParametremodelAjaxListViewBean().setParametreModelComplexSearch((ParametreModelComplexSearch) search);
    }

    @Override
    public void update() throws Exception {
        parametreModelComplex = AmalServiceLocator.getParametreModelService().update(parametreModelComplex);
        updateListe();
    }

    /**
     * Mise à jour de la liste
     * 
     * @throws Exception
     */
    private void updateListe() throws Exception {
        if (getListe) {
            ParametreModelComplexSearch search = (ParametreModelComplexSearch) listeParametremodelAjaxListViewBean
                    .getManagerModel();

            listeParametremodelAjaxListViewBean.find();
        }
    }
}
