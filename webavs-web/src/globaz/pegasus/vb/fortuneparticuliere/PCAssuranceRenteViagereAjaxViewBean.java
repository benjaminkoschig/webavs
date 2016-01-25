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
import ch.globaz.pegasus.business.models.fortuneparticuliere.AssuranceRenteViagere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AssuranceRenteViagereSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCAssuranceRenteViagereAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements
        FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private AssuranceRenteViagere assuranceRenteViagere = null;

    private Boolean doAddPeriode = null;

    private PCAssuranceRenteViagereAjaxListViewBean listeAssuranceRenteViagere;

    /**
	 * 
	 */
    public PCAssuranceRenteViagereAjaxViewBean() {
        super();
        assuranceRenteViagere = new AssuranceRenteViagere();
    }

    /**
     * @param assuranceRenteViagere
     */
    public PCAssuranceRenteViagereAjaxViewBean(AssuranceRenteViagere assuranceRenteViagere) {
        super();
        this.assuranceRenteViagere = assuranceRenteViagere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        assuranceRenteViagere = PegasusServiceLocator.getDroitService().createAssuranceRenteViagere(getDroit(),
                getInstanceDroitMembreFamille(), assuranceRenteViagere);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = assuranceRenteViagere.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        assuranceRenteViagere = PegasusServiceLocator.getDroitService().deleteAssuranceRenteViagere(getDroit(),
                assuranceRenteViagere);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the assuranceRenteViagere
     */
    public AssuranceRenteViagere getAssuranceRenteViagere() {
        return assuranceRenteViagere;
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
        return assuranceRenteViagere.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeAssuranceRenteViagere;
    }

    public String getNomCompagnie() {
        if (assuranceRenteViagere.getCompagnie() != null) {
            return assuranceRenteViagere.getCompagnie().getTiers().getDesignation1();
        } else {
            return "";
        }
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return assuranceRenteViagere.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (assuranceRenteViagere != null) && !assuranceRenteViagere.isNew() ? new BSpy(
                assuranceRenteViagere.getSpy()) : new BSpy((BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeAssuranceRenteViagere.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        assuranceRenteViagere = PegasusServiceLocator.getDroitService().readAssuranceRenteViagere(
                assuranceRenteViagere.getId());
    }

    /**
     * @param assuranceRenteViagere
     *            the assuranceRenteViagere to set
     */
    public void setAssuranceRenteViagere(AssuranceRenteViagere assuranceRenteViagere) {
        this.assuranceRenteViagere = assuranceRenteViagere;
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
        assuranceRenteViagere.setId(newId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeAssuranceRenteViagere = (PCAssuranceRenteViagereAjaxListViewBean) fwViewBeanInterface;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            assuranceRenteViagere = (PegasusServiceLocator.getDroitService().createAndCloseAssuranceRenteViagere(
                    getDroit(), assuranceRenteViagere, false));
        } else if (isForceClorePeriode()) {
            assuranceRenteViagere = (PegasusServiceLocator.getDroitService().createAndCloseAssuranceRenteViagere(
                    getDroit(), assuranceRenteViagere, true));
        } else {
            assuranceRenteViagere = (PegasusServiceLocator.getDroitService().updateAssuranceRenteViagere(getDroit(),
                    getInstanceDroitMembreFamille(), assuranceRenteViagere));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(assuranceRenteViagere.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listeAssuranceRenteViagere = new PCAssuranceRenteViagereAjaxListViewBean();
            AssuranceRenteViagereSearch search = listeAssuranceRenteViagere.getSearchModel();
            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersioned");
            listeAssuranceRenteViagere.find();
        }
    }
}
