package globaz.pegasus.vb.revenusdepenses;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.revenusdepenses.AutresRevenus;
import ch.globaz.pegasus.business.models.revenusdepenses.AutresRevenusSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCAutresRevenusAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private AutresRevenus autresRevenus = null;

    private Boolean doAddPeriode = null;
    private PCAutresRevenusAjaxListViewBean listeAutresRevenus;

    /**
	 * 
	 */
    public PCAutresRevenusAjaxViewBean() {
        super();
        autresRevenus = new AutresRevenus();
    }

    /**
     * @param AutresRevenus
     */
    public PCAutresRevenusAjaxViewBean(AutresRevenus autresRevenus) {
        super();
        this.autresRevenus = autresRevenus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        autresRevenus = PegasusServiceLocator.getDroitService().createAutresRevenus(getDroit(),
                getInstanceDroitMembreFamille(), autresRevenus);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = autresRevenus.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        autresRevenus = PegasusServiceLocator.getDroitService().deleteAutresRevenus(getDroit(), autresRevenus);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the AutresRevenus
     */
    public AutresRevenus getAutresRevenus() {
        return autresRevenus;
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
        return autresRevenus.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeAutresRevenus;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return autresRevenus.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (autresRevenus != null) && !autresRevenus.isNew() ? new BSpy(autresRevenus.getSpy()) : new BSpy(
                (BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeAutresRevenus.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        autresRevenus = PegasusServiceLocator.getDroitService().readAutresRevenus(autresRevenus.getId());
    }

    /**
     * @param AutresRevenus
     *            the AutresRevenus to set
     */
    public void setAutresRevenus(AutresRevenus autresRevenus) {
        this.autresRevenus = autresRevenus;
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
        autresRevenus.setId(newId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeAutresRevenus = (PCAutresRevenusAjaxListViewBean) fwViewBeanInterface;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            autresRevenus = (PegasusServiceLocator.getDroitService().createAndCloseAutresRevenus(getDroit(),
                    autresRevenus, false));
        } else if (isForceClorePeriode()) {
            autresRevenus = (PegasusServiceLocator.getDroitService().createAndCloseAutresRevenus(getDroit(),
                    autresRevenus, true));
        } else {
            autresRevenus = (PegasusServiceLocator.getDroitService().updateAutresRevenus(getDroit(),
                    getInstanceDroitMembreFamille(), autresRevenus));
        }

        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(autresRevenus.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {

        if (getListe) {
            listeAutresRevenus = new PCAutresRevenusAjaxListViewBean();
            AutresRevenusSearch search = listeAutresRevenus.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersionedAR");
            listeAutresRevenus.find();
        }

    }

}
