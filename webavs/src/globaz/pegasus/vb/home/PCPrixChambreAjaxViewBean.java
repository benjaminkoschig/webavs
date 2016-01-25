package globaz.pegasus.vb.home;

import globaz.framework.bean.JadeAbstractAjaxFindViewBean;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.pegasus.business.models.home.PrixChambre;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCPrixChambreAjaxViewBean extends JadeAbstractAjaxFindViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private transient PCPrixChambreListViewBean list = null;
    private PrixChambre prixChambre = null;

    public PCPrixChambreAjaxViewBean() {
        super();
        prixChambre = new PrixChambre();
        initList();
    }

    @Override
    public void add() throws Exception {
        prixChambre = PegasusServiceLocator.getHomeService().createPrixChambre(prixChambre);
        // this.find();
    }

    @Override
    public void delete() throws Exception {
        prixChambre = PegasusServiceLocator.getHomeService().deletePrixChambre(prixChambre);
        // this.find();
    }

    @Override
    public void find() throws Exception {
        list.find();
    }

    @Override
    public JadeAbstractModel getCurrentEntity() {
        return prixChambre;
    }

    public PrixChambre getPrixChambre() {
        return prixChambre;
    }

    @Override
    public JadeAbstractSearchModel getSearchModel() {
        return list.getPrixChambreSearch();
    }

    @Override
    public void initList() {
        list = new PCPrixChambreListViewBean();
    }

    @Override
    public void retrieve() throws Exception {
        prixChambre = PegasusServiceLocator.getHomeService().readPrixChambre(prixChambre.getId());
    }

    public void setPrixChambre(PrixChambre prixChambre) {
        this.prixChambre = prixChambre;
    }

    @Override
    public void update() throws Exception {
        prixChambre = PegasusServiceLocator.getHomeService().updatePrixChambre(prixChambre);
        // this.find();
    }

}
