package globaz.pegasus.vb.assurancemaladie;

import ch.globaz.pegasus.business.models.assurancemaladie.SubsideAssuranceMaladie;
import ch.globaz.pegasus.business.models.assurancemaladie.SubsideAssuranceMaladieSearch;
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

public class PCSubsideAssuranceMaladieAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private SubsideAssuranceMaladie subsideAssuranceMaladie = null;

    private Boolean doAddPeriode = null;
    private transient  PCSubsideAssuranceMaladieAjaxListViewBean listeSubsideAssuranceMaladie;

    /**
     *
     */
    public PCSubsideAssuranceMaladieAjaxViewBean() {
        super();
        subsideAssuranceMaladie = new SubsideAssuranceMaladie();
    }

    /**
     * @param subsideAssuranceMaladie
     */
    public PCSubsideAssuranceMaladieAjaxViewBean(SubsideAssuranceMaladie subsideAssuranceMaladie) {
        super();
        this.subsideAssuranceMaladie = subsideAssuranceMaladie;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        subsideAssuranceMaladie = PegasusServiceLocator.getDroitService().createSubsideAssuranceMaladie(getDroit(),
                getInstanceDroitMembreFamille(), subsideAssuranceMaladie);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = subsideAssuranceMaladie.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        subsideAssuranceMaladie = PegasusServiceLocator.getDroitService().deleteSubsideAssuranceMaladie(getDroit(), subsideAssuranceMaladie);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the FraisGarde
     */
    public SubsideAssuranceMaladie getSubsideAssuranceMaladie() {
        return subsideAssuranceMaladie;
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
        return subsideAssuranceMaladie.getId();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeSubsideAssuranceMaladie;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return subsideAssuranceMaladie.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (subsideAssuranceMaladie != null) && !subsideAssuranceMaladie.isNew() ? new BSpy(subsideAssuranceMaladie.getSpy()) : new BSpy(
                (BSession) getISession());
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeSubsideAssuranceMaladie.iterator();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        subsideAssuranceMaladie = PegasusServiceLocator.getAssuranceMaladieService().readSubsideAssuranceMaladie(subsideAssuranceMaladie.getId());
    }

    /**
     * @param subsideAssuranceMaladie
     *            the SubsideAssuranceMaladie to set
     */
    public void setSubsideAssuranceMaladie(SubsideAssuranceMaladie subsideAssuranceMaladie) {
        this.subsideAssuranceMaladie = subsideAssuranceMaladie;
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
        subsideAssuranceMaladie.setId(newId);
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeSubsideAssuranceMaladie = (PCSubsideAssuranceMaladieAjaxListViewBean) fwViewBeanInterface;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            subsideAssuranceMaladie = (PegasusServiceLocator.getDroitService().createAndCloseSubsideAssuranceMaladie(getDroit(),
                    subsideAssuranceMaladie, false));
        } else if (isForceClorePeriode()) {
            subsideAssuranceMaladie = (PegasusServiceLocator.getDroitService().createAndCloseSubsideAssuranceMaladie(getDroit(),
                    subsideAssuranceMaladie, true));
        } else {
            subsideAssuranceMaladie = (PegasusServiceLocator.getDroitService().updateSubsideAssuranceMaladie(getDroit(),
                    getInstanceDroitMembreFamille(), subsideAssuranceMaladie));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(subsideAssuranceMaladie.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {

        if (getListe) {
            listeSubsideAssuranceMaladie = new PCSubsideAssuranceMaladieAjaxListViewBean();
            SubsideAssuranceMaladieSearch search = listeSubsideAssuranceMaladie.getSearchModel();
            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersionedSubsideAssuranceMaladie");
            listeSubsideAssuranceMaladie.find();
        }

    }


}
