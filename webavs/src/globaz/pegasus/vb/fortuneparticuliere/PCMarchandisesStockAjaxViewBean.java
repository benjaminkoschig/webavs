/**
 * 
 */
package globaz.pegasus.vb.fortuneparticuliere;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.fortuneparticuliere.MarchandisesStock;
import ch.globaz.pegasus.business.models.fortuneparticuliere.MarchandisesStockSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCMarchandisesStockAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean doAddPeriode = null;
    private PCMarchandisesStockAjaxListViewBean listeMarchandisesStock;
    private MarchandisesStock marchandisesStock = null;

    /**
	 * 
	 */
    public PCMarchandisesStockAjaxViewBean() {
        super();
        marchandisesStock = new MarchandisesStock();
    }

    /**
     * @param marchandisesStock
     */
    public PCMarchandisesStockAjaxViewBean(MarchandisesStock marchandisesStock) {
        super();
        this.marchandisesStock = marchandisesStock;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        marchandisesStock = PegasusServiceLocator.getDroitService().createMarchandisesStock(getDroit(),
                getInstanceDroitMembreFamille(), marchandisesStock);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = marchandisesStock.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        marchandisesStock = PegasusServiceLocator.getDroitService().deleteMarchandisesStock(getDroit(),
                marchandisesStock);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the doAddPeriode
     */
    public String getDoAddPeriode() {
        return doAddPeriode.toString();
    }

    public DroitMembreFamilleEtendu getDroitMembreFamille() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return marchandisesStock.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeMarchandisesStock;
    }

    /**
     * @return the marchandisesStock
     */
    public MarchandisesStock getMarchandisesStock() {
        return marchandisesStock;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return marchandisesStock.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (marchandisesStock != null) && !marchandisesStock.isNew() ? new BSpy(marchandisesStock.getSpy())
                : new BSpy((BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeMarchandisesStock.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        marchandisesStock = PegasusServiceLocator.getDroitService().readMarchandisesStock(marchandisesStock.getId());
    }

    /**
     * @param doAddPeriode
     *            the doAddPeriode to set
     */
    public void setDoAddPeriode(String doAddPeriode) {
        this.doAddPeriode = Boolean.valueOf(doAddPeriode);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        marchandisesStock.setId(newId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeMarchandisesStock = (PCMarchandisesStockAjaxListViewBean) fwViewBeanInterface;
    }

    /**
     * @param marchandisesStock
     *            the marchandisesStock to set
     */
    public void setMarchandisesStock(MarchandisesStock marchandisesStock) {
        this.marchandisesStock = marchandisesStock;
    }

    public void setPart(String part) {
        String[] parts = PCDonneeFinanciereAjaxViewBean.splitPart(part);
        marchandisesStock.getSimpleMarchandisesStock().setPartProprieteNumerateur(parts[0]);
        marchandisesStock.getSimpleMarchandisesStock().setPartProprieteDenominateur(parts[1]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            marchandisesStock = (PegasusServiceLocator.getDroitService().createAndCloseMarchandisesStock(getDroit(),
                    marchandisesStock, false));
        } else if (isForceClorePeriode()) {
            marchandisesStock = (PegasusServiceLocator.getDroitService().createAndCloseMarchandisesStock(getDroit(),
                    marchandisesStock, true));
        } else {
            marchandisesStock = (PegasusServiceLocator.getDroitService().updateMarchandisesStock(getDroit(),
                    getInstanceDroitMembreFamille(), marchandisesStock));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(marchandisesStock.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listeMarchandisesStock = new PCMarchandisesStockAjaxListViewBean();
            MarchandisesStockSearch search = listeMarchandisesStock.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersioned");
            listeMarchandisesStock.find();
        }
    }

}
