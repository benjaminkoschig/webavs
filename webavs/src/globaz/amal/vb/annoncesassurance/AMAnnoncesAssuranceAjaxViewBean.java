package globaz.amal.vb.annoncesassurance;

import globaz.framework.bean.JadeAbstractAjaxFindViewBean;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.amal.business.models.annonce.AnnoncesCaisse;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMAnnoncesAssuranceAjaxViewBean extends JadeAbstractAjaxFindViewBean {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AnnoncesCaisse annoncesCaisse = null;
    private transient AMAnnoncesAssuranceAjaxListViewBean listeAnnonceAjaxListViewBean;

    public AMAnnoncesAssuranceAjaxViewBean() {
        super();
        listeAnnonceAjaxListViewBean = new AMAnnoncesAssuranceAjaxListViewBean();
        annoncesCaisse = new AnnoncesCaisse();
    }

    public AMAnnoncesAssuranceAjaxViewBean(AnnoncesCaisse annoncesCaisse) {
        this();
        this.annoncesCaisse = annoncesCaisse;
    }

    @Override
    public void add() throws Exception {
        // this.annoncesCaisse = AmalServiceLocator.getSimpleAnnonceService().create(this.annoncesCaisse);
    }

    @Override
    public void delete() throws Exception {
        // this.annoncesCaisse = AmalServiceLocator.getSimpleAnnonceService().delete(this.annoncesCaisse);
    }

    @Override
    public void find() throws Exception {
        listeAnnonceAjaxListViewBean.find();
    }

    public AnnoncesCaisse getAnnoncesCaisse() {
        return annoncesCaisse;
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return annoncesCaisse;
    }

    public AMAnnoncesAssuranceAjaxListViewBean getListeAnnonceAjaxListViewBean() {
        return listeAnnonceAjaxListViewBean;
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return listeAnnonceAjaxListViewBean.getSimpleAnnonceSearch();
    }

    public AnnoncesCaisse getSimpleAnnonce() {
        return annoncesCaisse;
    }

    @Override
    public void initList() {
        listeAnnonceAjaxListViewBean = new AMAnnoncesAssuranceAjaxListViewBean();
    }

    @Override
    public void retrieve() throws Exception {
        annoncesCaisse = AmalServiceLocator.getSimpleAnnonceService().readAnnonce(annoncesCaisse.getId());
    }

    public void setAnnoncesCaisse(AnnoncesCaisse annoncesCaisse) {
        this.annoncesCaisse = annoncesCaisse;
    }

    public void setListeAnnonceAjaxListViewBean(AMAnnoncesAssuranceAjaxListViewBean listeAnnonceAjaxListViewBean) {
        this.listeAnnonceAjaxListViewBean = listeAnnonceAjaxListViewBean;
    }

    public void setSimpleAnnonce(AnnoncesCaisse annoncesCaisse) {
        this.annoncesCaisse = annoncesCaisse;
    }

    @Override
    public void update() throws Exception {
        // this.annoncesCaisse = AmalServiceLocator.getAnnonceService().update(this.annoncesCaisse);
    }

}
