package globaz.pegasus.vb.revenusdepenses;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.revenusdepenses.PensionAlimentaire;
import ch.globaz.pegasus.business.models.revenusdepenses.PensionAlimentaireSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCPensionAlimentaireAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Boolean doAddPeriode = null;

    private PCPensionAlimentaireAjaxListViewBean listePensionAlimentaire;
    private PensionAlimentaire pensionAlimentaire = null;

    /**
	 * 
	 */
    public PCPensionAlimentaireAjaxViewBean() {
        super();
        pensionAlimentaire = new PensionAlimentaire();
    }

    /**
     * @param PensionAlimentaire
     */
    public PCPensionAlimentaireAjaxViewBean(PensionAlimentaire pensionAlimentaire) {
        super();
        this.pensionAlimentaire = pensionAlimentaire;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        pensionAlimentaire = PegasusServiceLocator.getDroitService().createPensionAlimentaire(getDroit(),
                getInstanceDroitMembreFamille(), pensionAlimentaire);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = pensionAlimentaire.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        pensionAlimentaire = PegasusServiceLocator.getDroitService().deletePensionAlimentaire(getDroit(),
                pensionAlimentaire);
        this.updateListe(idDroitMbrFam);
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
        return pensionAlimentaire.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listePensionAlimentaire;
    }

    public String getNomTiers() {
        if (pensionAlimentaire.getTiers() != null) {
            return pensionAlimentaire.getTiers().getDesignation1() + " "
                    + pensionAlimentaire.getTiers().getDesignation2();
        } else {
            return "";
        }
    }

    /**
     * @return the pensionAlimentaire
     */
    public PensionAlimentaire getPensionAlimentaire() {
        return pensionAlimentaire;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return pensionAlimentaire.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (pensionAlimentaire != null) && !pensionAlimentaire.isNew() ? new BSpy(pensionAlimentaire.getSpy())
                : new BSpy((BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listePensionAlimentaire.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        pensionAlimentaire = PegasusServiceLocator.getDroitService().readPensionAlimentaire(pensionAlimentaire.getId());
    }

    public void setDoAddPeriode(String doAddPeriode) {
        this.doAddPeriode = Boolean.valueOf(doAddPeriode);
    }

    /*
     * public void setMontantActivite(String montantActiviteLucrative) { this.pensionAlimentaire
     * .getSimplePensionAlimentaire() .setMontantActiviteLucrative(montantActiviteLucrative); }
     */

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        pensionAlimentaire.setId(newId);
    }

    public void setIdTiers(String idTiers) {
        pensionAlimentaire.getSimplePensionAlimentaire().setIdTiers(idTiers);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listePensionAlimentaire = (PCPensionAlimentaireAjaxListViewBean) fwViewBeanInterface;
    }

    /**
     * @param PensionAlimentaire
     *            the PensionAlimentaire to set
     */
    public void setPensionAlimentaire(PensionAlimentaire pensionAlimentaire) {
        this.pensionAlimentaire = pensionAlimentaire;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            pensionAlimentaire = (PegasusServiceLocator.getDroitService().createAndClosePensionAlimentaire(getDroit(),
                    pensionAlimentaire, false));
        } else if (isForceClorePeriode()) {
            pensionAlimentaire = (PegasusServiceLocator.getDroitService().createAndClosePensionAlimentaire(getDroit(),
                    pensionAlimentaire, true));
        } else {
            pensionAlimentaire = (PegasusServiceLocator.getDroitService().updatePensionAlimentaire(getDroit(),
                    getInstanceDroitMembreFamille(), pensionAlimentaire));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(pensionAlimentaire.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listePensionAlimentaire = new PCPensionAlimentaireAjaxListViewBean();
            PensionAlimentaireSearch search = listePensionAlimentaire.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersionedPA");
            listePensionAlimentaire.find();
        }
    }

}
