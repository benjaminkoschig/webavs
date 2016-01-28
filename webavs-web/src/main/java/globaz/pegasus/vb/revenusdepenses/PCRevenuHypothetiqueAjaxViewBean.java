package globaz.pegasus.vb.revenusdepenses;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuHypothetique;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuHypothetiqueSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCRevenuHypothetiqueAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String autreBrutOuNet = null;

    private Boolean doAddPeriode = null;
    private PCRevenuHypothetiqueAjaxListViewBean listeRevenusHypothetique;
    private RevenuHypothetique revenuHypothetique = null;

    /**
	 * 
	 */
    public PCRevenuHypothetiqueAjaxViewBean() {
        super();
        revenuHypothetique = new RevenuHypothetique();
    }

    /**
     * @param RevenuHypothetique
     */
    public PCRevenuHypothetiqueAjaxViewBean(RevenuHypothetique revenuHypothetique) {
        super();
        this.revenuHypothetique = revenuHypothetique;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        revenuHypothetique = PegasusServiceLocator.getDroitService().createRevenuHypothetique(getDroit(),
                getInstanceDroitMembreFamille(), revenuHypothetique);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = revenuHypothetique.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        revenuHypothetique = PegasusServiceLocator.getDroitService().deleteRevenuHypothetique(getDroit(),
                revenuHypothetique);
        this.updateListe(idDroitMbrFam);
    }

    public String getAutreBrutOuNet() {
        return autreBrutOuNet;
    }

    public String getDoAddPeriode() {
        return doAddPeriode.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return revenuHypothetique.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeRevenusHypothetique;
    }

    /**
     * @return the RevenuHypothetique
     */
    public RevenuHypothetique getRevenuHypothetique() {
        return revenuHypothetique;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return revenuHypothetique.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (revenuHypothetique != null) && !revenuHypothetique.isNew() ? new BSpy(revenuHypothetique.getSpy())
                : new BSpy((BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeRevenusHypothetique.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {

        revenuHypothetique = PegasusServiceLocator.getDroitService().readRevenuHypothetique(revenuHypothetique.getId());

        if (!JadeNumericUtil.isEmptyOrZero(revenuHypothetique.getSimpleRevenuHypothetique()
                .getMontantRevenuHypothetiqueBrut())) {
            autreBrutOuNet = "BRUT";
        } else if (!JadeNumericUtil.isEmptyOrZero(revenuHypothetique.getSimpleRevenuHypothetique()
                .getMontantRevenuHypothetiqueNet())) {
            autreBrutOuNet = "NET";
        } else {
            autreBrutOuNet = "";
        }
    }

    public void setAutreBrutOuNet(String autreBrutOuNet) {
        this.autreBrutOuNet = autreBrutOuNet;
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
        revenuHypothetique.setId(newId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeRevenusHypothetique = (PCRevenuHypothetiqueAjaxListViewBean) fwViewBeanInterface;
    }

    /**
     * @param RevenuHypothetique
     *            the RevenuHypothetique to set
     */
    public void setRevenuHypothetique(RevenuHypothetique revenuHypothetique) {
        this.revenuHypothetique = revenuHypothetique;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            revenuHypothetique = (PegasusServiceLocator.getDroitService().createAndCloseRevenuHypothetique(getDroit(),
                    revenuHypothetique, false));
        } else if (isForceClorePeriode()) {
            revenuHypothetique = (PegasusServiceLocator.getDroitService().createAndCloseRevenuHypothetique(getDroit(),
                    revenuHypothetique, true));
        } else {
            revenuHypothetique = (PegasusServiceLocator.getDroitService().updateRevenuHypothetique(getDroit(),
                    getInstanceDroitMembreFamille(), revenuHypothetique));
        }

        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(revenuHypothetique.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {

        if (getListe) {
            listeRevenusHypothetique = new PCRevenuHypothetiqueAjaxListViewBean();
            RevenuHypothetiqueSearch search = listeRevenusHypothetique.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersionedHypo");
            listeRevenusHypothetique.find();
        }

    }

}
