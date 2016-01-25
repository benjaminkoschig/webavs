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
import ch.globaz.pegasus.business.models.fortuneparticuliere.Vehicule;
import ch.globaz.pegasus.business.models.fortuneparticuliere.VehiculeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCVehiculeAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean doAddPeriode = null;
    private PCVehiculeAjaxListViewBean listePretsEnversTiers;
    private Vehicule vehicule = null;

    /**
	 * 
	 */
    public PCVehiculeAjaxViewBean() {
        super();
        vehicule = new Vehicule();
    }

    /**
     * @param vehicule
     */
    public PCVehiculeAjaxViewBean(Vehicule vehicule) {
        super();
        this.vehicule = vehicule;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        vehicule = PegasusServiceLocator.getDroitService().createVehicule(getDroit(), getInstanceDroitMembreFamille(),
                vehicule);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = vehicule.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        vehicule = PegasusServiceLocator.getDroitService().deleteVehicule(getDroit(), vehicule);
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
        return vehicule.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listePretsEnversTiers;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return vehicule.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (vehicule != null) && !vehicule.isNew() ? new BSpy(vehicule.getSpy()) : new BSpy(
                (BSession) getISession());
    }

    /**
     * @return the vehicule
     */
    public Vehicule getVehicule() {
        return vehicule;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listePretsEnversTiers.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        vehicule = PegasusServiceLocator.getDroitService().readVehicule(vehicule.getId());
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
        vehicule.setId(newId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listePretsEnversTiers = (PCVehiculeAjaxListViewBean) fwViewBeanInterface;
    }

    public void setPart(String part) {
        String[] parts = PCDonneeFinanciereAjaxViewBean.splitPart(part);
        vehicule.getSimpleVehicule().setPartProprieteNumerateur(parts[0]);
        vehicule.getSimpleVehicule().setPartProprieteDenominateur(parts[1]);
    }

    /**
     * @param Vehicule
     *            the Vehicule to set
     */
    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            vehicule = (PegasusServiceLocator.getDroitService().createAndCloseVehicule(getDroit(), vehicule, false));
        } else if (isForceClorePeriode()) {
            vehicule = (PegasusServiceLocator.getDroitService().createAndCloseVehicule(getDroit(), vehicule, true));
        } else {
            vehicule = (PegasusServiceLocator.getDroitService().updateVehicule(getDroit(),
                    getInstanceDroitMembreFamille(), vehicule));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(vehicule.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listePretsEnversTiers = new PCVehiculeAjaxListViewBean();
            VehiculeSearch search = listePretsEnversTiers.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersioned");
            listePretsEnversTiers.find();
        }
    }

}
