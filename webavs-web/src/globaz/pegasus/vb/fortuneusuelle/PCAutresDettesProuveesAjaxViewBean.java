/**
 * 
 */
package globaz.pegasus.vb.fortuneusuelle;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.fortuneusuelle.AutresDettesProuvees;
import ch.globaz.pegasus.business.models.fortuneusuelle.AutresDettesProuveesSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCAutresDettesProuveesAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements
        FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AutresDettesProuvees autresDettesProuvees = null;
    private Boolean doAddPeriode = null;
    private PCAutresDettesProuveesAjaxListViewBean listeAutresDettesProuvees;

    /**
	 * 
	 */
    public PCAutresDettesProuveesAjaxViewBean() {
        super();
        autresDettesProuvees = new AutresDettesProuvees();
    }

    /**
     * @param autresDettesProuvees
     */
    public PCAutresDettesProuveesAjaxViewBean(AutresDettesProuvees autresDettesProuvees) {
        super();
        this.autresDettesProuvees = autresDettesProuvees;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        autresDettesProuvees = PegasusServiceLocator.getDroitService().createAutresDettesProuvees(getDroit(),
                getInstanceDroitMembreFamille(), autresDettesProuvees);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = autresDettesProuvees.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        autresDettesProuvees = PegasusServiceLocator.getDroitService().deleteAutresDettesProuvees(getDroit(),
                autresDettesProuvees);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the autresDettesProuvees
     */
    public AutresDettesProuvees getAutresDettesProuvees() {
        return autresDettesProuvees;
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
        return autresDettesProuvees.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeAutresDettesProuvees;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return autresDettesProuvees.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (autresDettesProuvees != null) && !autresDettesProuvees.isNew() ? new BSpy(autresDettesProuvees.getSpy())
                : new BSpy((BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeAutresDettesProuvees.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        autresDettesProuvees = PegasusServiceLocator.getDroitService().readAutresDettesProuvees(
                autresDettesProuvees.getId());
    }

    /**
     * @param AutresDettesProuvees
     *            the AutresDettesProuvees to set
     */
    public void setAutresDettesProuvees(AutresDettesProuvees autresDettesProuvees) {
        this.autresDettesProuvees = autresDettesProuvees;
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
        autresDettesProuvees.setId(newId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeAutresDettesProuvees = (PCAutresDettesProuveesAjaxListViewBean) fwViewBeanInterface;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            autresDettesProuvees = (PegasusServiceLocator.getDroitService().createAndCloseAutresDettesProuvees(
                    getDroit(), autresDettesProuvees, false));
        } else if (isForceClorePeriode()) {
            autresDettesProuvees = (PegasusServiceLocator.getDroitService().createAndCloseAutresDettesProuvees(
                    getDroit(), autresDettesProuvees, true));
        } else {
            autresDettesProuvees = (PegasusServiceLocator.getDroitService().updateAutresDettesProuvees(getDroit(),
                    getInstanceDroitMembreFamille(), autresDettesProuvees));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(autresDettesProuvees.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listeAutresDettesProuvees = new PCAutresDettesProuveesAjaxListViewBean();
            AutresDettesProuveesSearch search = listeAutresDettesProuvees.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersionedADP");
            listeAutresDettesProuvees.find();
        }
    }

}
