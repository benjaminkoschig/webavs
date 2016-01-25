package globaz.pegasus.vb.fortuneusuelle;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.fortuneusuelle.AssuranceVie;
import ch.globaz.pegasus.business.models.fortuneusuelle.AssuranceVieSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCAssuranceVieAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private AssuranceVie assuranceVie = null;

    private Boolean doAddPeriode = null;
    private PCAssuranceVieAjaxListViewBean listeAssuranceVie;

    /**
	 * 
	 */
    public PCAssuranceVieAjaxViewBean() {
        super();
        assuranceVie = new AssuranceVie();
    }

    /**
     * @param AssuranceVie
     */
    public PCAssuranceVieAjaxViewBean(AssuranceVie assuranceVie) {
        super();
        this.assuranceVie = assuranceVie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        assuranceVie = PegasusServiceLocator.getDroitService().createAssuranceVie(getDroit(),
                getInstanceDroitMembreFamille(), assuranceVie);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = assuranceVie.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        assuranceVie = PegasusServiceLocator.getDroitService().deleteAssuranceVie(getDroit(), assuranceVie);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the assuranceVie
     */
    public AssuranceVie getAssuranceVie() {
        return assuranceVie;
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
        return assuranceVie.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeAssuranceVie;
    }

    public String getNomCompagnie() {
        if (assuranceVie.getTiersCompagnie() != null) {

            return JadeStringUtil.escapeXML(assuranceVie.getTiersCompagnie().getDesignation1() + " "
                    + assuranceVie.getTiersCompagnie().getDesignation2());
        } else {
            return "";
        }
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return assuranceVie.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (assuranceVie != null) && !assuranceVie.isNew() ? new BSpy(assuranceVie.getSpy()) : new BSpy(
                (BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeAssuranceVie.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        assuranceVie = PegasusServiceLocator.getDroitService().readAssuranceVie(assuranceVie.getId());
    }

    /**
     * @param AssuranceVie
     *            the AssuranceVie to set
     */
    public void setAssuranceVie(AssuranceVie assuranceVie) {
        this.assuranceVie = assuranceVie;
    }

    /*
     * public void setMontantActivite(String montantActiviteLucrative) { this.assuranceVie .getSimpleAssuranceVie()
     * .setMontantActiviteLucrative(montantActiviteLucrative); }
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
        assuranceVie.setId(newId);
    }

    /* nomCompagnie est un id! */
    public void setIdCaisseCompensation(String idCaisseCompensation) {
        assuranceVie.getSimpleAssuranceVie().setNomCompagnie(idCaisseCompensation);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeAssuranceVie = (PCAssuranceVieAjaxListViewBean) fwViewBeanInterface;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            assuranceVie = (PegasusServiceLocator.getDroitService().createAndCloseAssuranceVie(getDroit(),
                    assuranceVie, false));
        } else if (isForceClorePeriode()) {
            assuranceVie = (PegasusServiceLocator.getDroitService().createAndCloseAssuranceVie(getDroit(),
                    assuranceVie, true));
        } else {
            assuranceVie = (PegasusServiceLocator.getDroitService().updateAssuranceVie(getDroit(),
                    getInstanceDroitMembreFamille(), assuranceVie));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(assuranceVie.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {

        if (getListe) {
            listeAssuranceVie = new PCAssuranceVieAjaxListViewBean();
            AssuranceVieSearch search = listeAssuranceVie.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersionedAV");
            listeAssuranceVie.find();
        }

    }

}
