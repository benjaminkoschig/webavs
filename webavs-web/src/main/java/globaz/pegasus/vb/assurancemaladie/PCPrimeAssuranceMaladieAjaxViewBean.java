package globaz.pegasus.vb.assurancemaladie;

import ch.globaz.pegasus.business.models.assurancemaladie.PrimeAssuranceMaladie;
import ch.globaz.pegasus.business.models.assurancemaladie.PrimeAssuranceMaladieSearch;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;

import java.util.Iterator;

public class PCPrimeAssuranceMaladieAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private PrimeAssuranceMaladie primeAssuranceMaladie = null;

    private Boolean doAddPeriode = null;
    private transient  PCPrimeAssuranceMaladieAjaxListViewBean listePrimeAssuranceMaladie;

    /**
     *
     */
    public PCPrimeAssuranceMaladieAjaxViewBean() {
        super();
        primeAssuranceMaladie = new PrimeAssuranceMaladie();
    }

    /**
     * @param primeAssuranceMaladie
     */
    public PCPrimeAssuranceMaladieAjaxViewBean(PrimeAssuranceMaladie primeAssuranceMaladie) {
        super();
        this.primeAssuranceMaladie = primeAssuranceMaladie;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        primeAssuranceMaladie = PegasusServiceLocator.getDroitService().createPrimeAssuranceMaladie(getDroit(),
                getInstanceDroitMembreFamille(), primeAssuranceMaladie);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = primeAssuranceMaladie.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        primeAssuranceMaladie = PegasusServiceLocator.getDroitService().deletePrimeAssuranceMaladie(getDroit(), primeAssuranceMaladie);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the FraisGarde
     */
    public PrimeAssuranceMaladie getPrimeAssuranceMaladie() {
        return primeAssuranceMaladie;
    }

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
        return primeAssuranceMaladie.getId();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listePrimeAssuranceMaladie;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return primeAssuranceMaladie.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (primeAssuranceMaladie != null) && !primeAssuranceMaladie.isNew() ? new BSpy(primeAssuranceMaladie.getSpy()) : new BSpy(
                (BSession) getISession());
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listePrimeAssuranceMaladie.iterator();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        primeAssuranceMaladie = PegasusServiceLocator.getAssuranceMaladieService().readPrimeAssuranceMaladie(primeAssuranceMaladie.getId());
    }

    /**
     * @param primeAssuranceMaladie
     *            the PrimeAssuranceMaladie to set
     */
    public void setPrimeAssuranceMaladie(PrimeAssuranceMaladie primeAssuranceMaladie) {
        this.primeAssuranceMaladie = primeAssuranceMaladie;
    }

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
        primeAssuranceMaladie.setId(newId);
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listePrimeAssuranceMaladie = (PCPrimeAssuranceMaladieAjaxListViewBean) fwViewBeanInterface;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            primeAssuranceMaladie = (PegasusServiceLocator.getDroitService().createAndClosePrimeAssuranceMaladie(getDroit(),
                    primeAssuranceMaladie, false));
        } else if (isForceClorePeriode()) {
            primeAssuranceMaladie = (PegasusServiceLocator.getDroitService().createAndClosePrimeAssuranceMaladie(getDroit(),
                    primeAssuranceMaladie, true));
        } else {
            primeAssuranceMaladie = (PegasusServiceLocator.getDroitService().updatePrimeAssuranceMaladie(getDroit(),
                    getInstanceDroitMembreFamille(), primeAssuranceMaladie));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(primeAssuranceMaladie.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {

        if (getListe) {
            listePrimeAssuranceMaladie = new PCPrimeAssuranceMaladieAjaxListViewBean();
            PrimeAssuranceMaladieSearch search = listePrimeAssuranceMaladie.getSearchModel();
            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersionedPrimeAssuranceMaladie");
            listePrimeAssuranceMaladie.find();
        }

    }


}
