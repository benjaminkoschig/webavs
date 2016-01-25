/**
 * 
 */
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
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierServantHabitationPrincipale;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierServantHabitationPrincipaleSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCBienImmobilierServantHabitationPrincipaleAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements
        FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale = null;
    private Boolean doAddPeriode = null;
    private PCBienImmobilierServantHabitationPrincipaleAjaxListViewBean listeBienImmobilierServantHabitationPrincipale;

    /**
	 * 
	 */
    public PCBienImmobilierServantHabitationPrincipaleAjaxViewBean() {
        super();
        bienImmobilierServantHabitationPrincipale = new BienImmobilierServantHabitationPrincipale();
    }

    /**
     * @param bienImmobilierServantHabitationPrincipale
     */
    public PCBienImmobilierServantHabitationPrincipaleAjaxViewBean(
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale) {
        super();
        this.bienImmobilierServantHabitationPrincipale = bienImmobilierServantHabitationPrincipale;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        bienImmobilierServantHabitationPrincipale = PegasusServiceLocator.getDroitService()
                .createBienImmobilierServantHabitationPrincipale(getDroit(), getInstanceDroitMembreFamille(),
                        bienImmobilierServantHabitationPrincipale);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = bienImmobilierServantHabitationPrincipale.getSimpleDonneeFinanciereHeader()
                .getIdDroitMembreFamille();
        bienImmobilierServantHabitationPrincipale = PegasusServiceLocator.getDroitService()
                .deleteBienImmobilierServantHabitationPrincipale(getDroit(), bienImmobilierServantHabitationPrincipale);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the bienImmobilierServantHabitationPrincipale
     */
    public BienImmobilierServantHabitationPrincipale getBienImmobilierServantHabitationPrincipale() {
        return bienImmobilierServantHabitationPrincipale;
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
        return bienImmobilierServantHabitationPrincipale.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeBienImmobilierServantHabitationPrincipale;
    }

    public String getNomCommune() {
        if (bienImmobilierServantHabitationPrincipale.getLocalite() != null) {
            return bienImmobilierServantHabitationPrincipale.getLocalite().getLocalite();
        } else {
            return "";
        }
    }

    public String getNomNomCompagnie() {
        if (bienImmobilierServantHabitationPrincipale.getTiersCompagnie() != null) {
            return JadeStringUtil.escapeXML(bienImmobilierServantHabitationPrincipale.getTiersCompagnie()
                    .getDesignation1()
                    + " "
                    + bienImmobilierServantHabitationPrincipale.getTiersCompagnie().getDesignation2());
        } else {
            return "";
        }
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return bienImmobilierServantHabitationPrincipale.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (bienImmobilierServantHabitationPrincipale != null)
                && !bienImmobilierServantHabitationPrincipale.isNew() ? new BSpy(
                bienImmobilierServantHabitationPrincipale.getSpy()) : new BSpy((BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeBienImmobilierServantHabitationPrincipale.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        bienImmobilierServantHabitationPrincipale = PegasusServiceLocator.getDroitService()
                .readBienImmobilierServantHabitationPrincipale(bienImmobilierServantHabitationPrincipale.getId());
    }

    /**
     * @param BienImmobilierServantHabitationPrincipale
     *            the BienImmobilierServantHabitationPrincipale to set
     */
    public void setBienImmobilierServantHabitationPrincipale(
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale) {
        this.bienImmobilierServantHabitationPrincipale = bienImmobilierServantHabitationPrincipale;
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
        bienImmobilierServantHabitationPrincipale.setId(newId);
    }

    public void setIdCommune(String idTiers) {
        bienImmobilierServantHabitationPrincipale.getSimpleBienImmobilierServantHabitationPrincipale()
                .setIdCommuneDuBien(idTiers);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeBienImmobilierServantHabitationPrincipale = (PCBienImmobilierServantHabitationPrincipaleAjaxListViewBean) fwViewBeanInterface;
    }

    /* nomCompagnie est un id! */
    public void setNomCompagnie(String idTiers) {
        bienImmobilierServantHabitationPrincipale.getSimpleBienImmobilierServantHabitationPrincipale().setNomCompagnie(
                idTiers);
    }

    public void setPart(String part) {
        String[] parts = PCDonneeFinanciereAjaxViewBean.splitPart(part);
        bienImmobilierServantHabitationPrincipale.getSimpleBienImmobilierServantHabitationPrincipale()
                .setPartProprieteNumerateur(parts[0]);
        bienImmobilierServantHabitationPrincipale.getSimpleBienImmobilierServantHabitationPrincipale()
                .setPartProprieteDenominateur(parts[1]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            bienImmobilierServantHabitationPrincipale = (PegasusServiceLocator.getDroitService()
                    .createAndCloseBienImmobilierServantHabitationPrincipale(getDroit(),
                            bienImmobilierServantHabitationPrincipale, false));
        } else if (isForceClorePeriode()) {
            bienImmobilierServantHabitationPrincipale = (PegasusServiceLocator.getDroitService()
                    .createAndCloseBienImmobilierServantHabitationPrincipale(getDroit(),
                            bienImmobilierServantHabitationPrincipale, true));
        } else {
            bienImmobilierServantHabitationPrincipale = (PegasusServiceLocator.getDroitService()
                    .updateBienImmobilierServantHabitationPrincipale(getDroit(), getInstanceDroitMembreFamille(),
                            bienImmobilierServantHabitationPrincipale));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(bienImmobilierServantHabitationPrincipale.getSimpleDonneeFinanciereHeader()
                .getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listeBienImmobilierServantHabitationPrincipale = new PCBienImmobilierServantHabitationPrincipaleAjaxListViewBean();
            BienImmobilierServantHabitationPrincipaleSearch search = listeBienImmobilierServantHabitationPrincipale
                    .getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersionedBISHP");
            listeBienImmobilierServantHabitationPrincipale.find();
        }
    }

}
