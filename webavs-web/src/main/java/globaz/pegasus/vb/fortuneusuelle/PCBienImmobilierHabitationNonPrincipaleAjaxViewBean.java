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
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierHabitationNonPrincipale;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierHabitationNonPrincipaleSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCBienImmobilierHabitationNonPrincipaleAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements
        FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale = null;
    private Boolean doAddPeriode = null;
    private PCBienImmobilierHabitationNonPrincipaleAjaxListViewBean listeBienImmobilierHabitationNonPrincipale;

    /**
	 * 
	 */
    public PCBienImmobilierHabitationNonPrincipaleAjaxViewBean() {
        super();
        bienImmobilierHabitationNonPrincipale = new BienImmobilierHabitationNonPrincipale();
    }

    /**
     * @param bienImmobilierHabitationNonPrincipale
     */
    public PCBienImmobilierHabitationNonPrincipaleAjaxViewBean(
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale) {
        super();
        this.bienImmobilierHabitationNonPrincipale = bienImmobilierHabitationNonPrincipale;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        bienImmobilierHabitationNonPrincipale = PegasusServiceLocator.getDroitService()
                .createBienImmobilierHabitationNonPrincipale(getDroit(), getInstanceDroitMembreFamille(),
                        bienImmobilierHabitationNonPrincipale);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = bienImmobilierHabitationNonPrincipale.getSimpleDonneeFinanciereHeader()
                .getIdDroitMembreFamille();
        bienImmobilierHabitationNonPrincipale = PegasusServiceLocator.getDroitService()
                .deleteBienImmobilierHabitationNonPrincipale(getDroit(), bienImmobilierHabitationNonPrincipale);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the bienImmobilierHabitationNonPrincipale
     */
    public BienImmobilierHabitationNonPrincipale getBienImmobilierHabitationNonPrincipale() {
        return bienImmobilierHabitationNonPrincipale;
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
        return bienImmobilierHabitationNonPrincipale.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeBienImmobilierHabitationNonPrincipale;
    }

    public String getNomCommune() {
        if (bienImmobilierHabitationNonPrincipale.getLocalite() != null) {
            return bienImmobilierHabitationNonPrincipale.getLocalite().getLocalite();
        } else {
            return "";
        }
    }

    public String getNomNomCompagnie() {
        if (bienImmobilierHabitationNonPrincipale.getTiersCompagnie() != null) {
            return bienImmobilierHabitationNonPrincipale.getTiersCompagnie().getDesignation1();
        } else {
            return "";
        }
    }

    public String getPays(BSession objSession) {
        String libelle = "";
        if ("fr".equals(objSession.getIdLangueISO())) {
            libelle = bienImmobilierHabitationNonPrincipale.getSimplePays().getLibelleFr();
        } else if ("de".equals(objSession.getIdLangueISO())) {
            libelle = bienImmobilierHabitationNonPrincipale.getSimplePays().getLibelleAl();
        } else {
            libelle = bienImmobilierHabitationNonPrincipale.getSimplePays().getLibelleIt();
        }
        return libelle;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return bienImmobilierHabitationNonPrincipale.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (bienImmobilierHabitationNonPrincipale != null) && !bienImmobilierHabitationNonPrincipale.isNew() ? new BSpy(
                bienImmobilierHabitationNonPrincipale.getSpy()) : new BSpy((BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeBienImmobilierHabitationNonPrincipale.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        bienImmobilierHabitationNonPrincipale = PegasusServiceLocator.getDroitService()
                .readBienImmobilierHabitationNonPrincipale(bienImmobilierHabitationNonPrincipale.getId());
    }

    /**
     * @param BienImmobilierHabitationNonPrincipale
     *            the BienImmobilierHabitationNonPrincipale to set
     */
    public void setBienImmobilierHabitationNonPrincipale(
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale) {
        this.bienImmobilierHabitationNonPrincipale = bienImmobilierHabitationNonPrincipale;
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
        bienImmobilierHabitationNonPrincipale.setId(newId);
    }

    public void setIdCommune(String idTiers) {
        bienImmobilierHabitationNonPrincipale.getSimpleBienImmobilierHabitationNonPrincipale().setIdCommuneDuBien(
                idTiers);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeBienImmobilierHabitationNonPrincipale = (PCBienImmobilierHabitationNonPrincipaleAjaxListViewBean) fwViewBeanInterface;
    }

    /* nomCompagnie est un id! */
    public void setNomCompagnie(String idTiers) {
        bienImmobilierHabitationNonPrincipale.getSimpleBienImmobilierHabitationNonPrincipale().setNomCompagnie(idTiers);
    }

    public void setPart(String part) {
        String[] parts = PCDonneeFinanciereAjaxViewBean.splitPart(part);
        bienImmobilierHabitationNonPrincipale.getSimpleBienImmobilierHabitationNonPrincipale()
                .setPartProprieteNumerateur(parts[0]);
        bienImmobilierHabitationNonPrincipale.getSimpleBienImmobilierHabitationNonPrincipale()
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
            bienImmobilierHabitationNonPrincipale = (PegasusServiceLocator.getDroitService()
                    .createAndCloseBienImmobilierHabitationNonPrincipale(getDroit(),
                            bienImmobilierHabitationNonPrincipale, false));
        } else if (isForceClorePeriode()) {
            bienImmobilierHabitationNonPrincipale = (PegasusServiceLocator.getDroitService()
                    .createAndCloseBienImmobilierHabitationNonPrincipale(getDroit(),
                            bienImmobilierHabitationNonPrincipale, true));
        } else {
            bienImmobilierHabitationNonPrincipale = (PegasusServiceLocator.getDroitService()
                    .updateBienImmobilierHabitationNonPrincipale(getDroit(), getInstanceDroitMembreFamille(),
                            bienImmobilierHabitationNonPrincipale));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(bienImmobilierHabitationNonPrincipale.getSimpleDonneeFinanciereHeader()
                .getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listeBienImmobilierHabitationNonPrincipale = new PCBienImmobilierHabitationNonPrincipaleAjaxListViewBean();
            BienImmobilierHabitationNonPrincipaleSearch search = listeBienImmobilierHabitationNonPrincipale
                    .getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersionedBIHNP");
            listeBienImmobilierHabitationNonPrincipale.find();
        }
    }

}
