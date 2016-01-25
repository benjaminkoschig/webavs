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
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierNonHabitable;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierNonHabitableSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCBienImmobilierNonHabitableAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements
        FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BienImmobilierNonHabitable bienImmobilierNonHabitable = null;
    private Boolean doAddPeriode = null;
    private PCBienImmobilierNonHabitableAjaxListViewBean listeBienImmobilierNonHabitable;

    /**
	 * 
	 */
    public PCBienImmobilierNonHabitableAjaxViewBean() {
        super();
        bienImmobilierNonHabitable = new BienImmobilierNonHabitable();
    }

    /**
     * @param bienImmobilierNonHabitable
     */
    public PCBienImmobilierNonHabitableAjaxViewBean(BienImmobilierNonHabitable bienImmobilierNonHabitable) {
        super();
        this.bienImmobilierNonHabitable = bienImmobilierNonHabitable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        bienImmobilierNonHabitable = PegasusServiceLocator.getDroitService().createBienImmobilierNonHabitable(
                getDroit(), getInstanceDroitMembreFamille(), bienImmobilierNonHabitable);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = bienImmobilierNonHabitable.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        bienImmobilierNonHabitable = PegasusServiceLocator.getDroitService().deleteBienImmobilierNonHabitable(
                getDroit(), bienImmobilierNonHabitable);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the bienImmobilierNonHabitable
     */
    public BienImmobilierNonHabitable getBienImmobilierNonHabitable() {
        return bienImmobilierNonHabitable;
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
        return bienImmobilierNonHabitable.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeBienImmobilierNonHabitable;
    }

    public String getNomCommune() {
        if (bienImmobilierNonHabitable.getLocalite() != null) {
            return bienImmobilierNonHabitable.getLocalite().getLocalite();
        } else {
            return "";
        }
    }

    public String getNomNomCompagnie() {
        if (bienImmobilierNonHabitable.getTiersCompagnie() != null) {
            return bienImmobilierNonHabitable.getTiersCompagnie().getDesignation1();
        } else {
            return "";
        }
    }

    public String getPays(BSession objSession) {
        String libelle = "";
        if ("fr".equals(objSession.getIdLangueISO())) {
            libelle = bienImmobilierNonHabitable.getSimplePays().getLibelleFr();
        } else if ("de".equals(objSession.getIdLangueISO())) {
            libelle = bienImmobilierNonHabitable.getSimplePays().getLibelleAl();
        } else {
            libelle = bienImmobilierNonHabitable.getSimplePays().getLibelleIt();
        }
        return libelle;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return bienImmobilierNonHabitable.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (bienImmobilierNonHabitable != null) && !bienImmobilierNonHabitable.isNew() ? new BSpy(
                bienImmobilierNonHabitable.getSpy()) : new BSpy((BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeBienImmobilierNonHabitable.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        bienImmobilierNonHabitable = PegasusServiceLocator.getDroitService().readBienImmobilierNonHabitable(
                bienImmobilierNonHabitable.getId());
    }

    /**
     * @param BienImmobilierNonHabitable
     *            the BienImmobilierNonHabitable to set
     */
    public void setBienImmobilierNonHabitable(BienImmobilierNonHabitable bienImmobilierNonHabitable) {
        this.bienImmobilierNonHabitable = bienImmobilierNonHabitable;
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
        bienImmobilierNonHabitable.setId(newId);
    }

    public void setIdCommune(String idTiers) {
        bienImmobilierNonHabitable.getSimpleBienImmobilierNonHabitable().setIdCommuneDuBien(idTiers);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeBienImmobilierNonHabitable = (PCBienImmobilierNonHabitableAjaxListViewBean) fwViewBeanInterface;
    }

    /* nomCompagnie est un id! */
    public void setNomCompagnie(String idTiers) {
        bienImmobilierNonHabitable.getSimpleBienImmobilierNonHabitable().setNomCompagnie(idTiers);
    }

    public void setPart(String part) {
        String[] parts = PCDonneeFinanciereAjaxViewBean.splitPart(part);
        bienImmobilierNonHabitable.getSimpleBienImmobilierNonHabitable().setPartProprieteNumerateur(parts[0]);
        bienImmobilierNonHabitable.getSimpleBienImmobilierNonHabitable().setPartProprieteDenominateur(parts[1]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            bienImmobilierNonHabitable = (PegasusServiceLocator.getDroitService()
                    .createAndCloseBienImmobilierNonHabitable(getDroit(), bienImmobilierNonHabitable, false));
        } else if (isForceClorePeriode()) {
            bienImmobilierNonHabitable = (PegasusServiceLocator.getDroitService()
                    .createAndCloseBienImmobilierNonHabitable(getDroit(), bienImmobilierNonHabitable, true));
        } else {
            bienImmobilierNonHabitable = (PegasusServiceLocator.getDroitService().updateBienImmobilierNonHabitable(
                    getDroit(), getInstanceDroitMembreFamille(), bienImmobilierNonHabitable));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(bienImmobilierNonHabitable.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listeBienImmobilierNonHabitable = new PCBienImmobilierNonHabitableAjaxListViewBean();
            BienImmobilierNonHabitableSearch search = listeBienImmobilierNonHabitable.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersionedBINH");
            listeBienImmobilierNonHabitable.find();
        }
    }

}
