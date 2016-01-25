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
import ch.globaz.pegasus.business.models.fortuneparticuliere.Numeraire;
import ch.globaz.pegasus.business.models.fortuneparticuliere.NumeraireSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCNumeraireAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean doAddPeriode = null;
    private PCNumeraireAjaxListViewBean listePretsEnversTiers;
    private Numeraire numeraire = null;

    /**
	 * 
	 */
    public PCNumeraireAjaxViewBean() {
        super();
        numeraire = new Numeraire();
    }

    /**
     * @param numeraire
     */
    public PCNumeraireAjaxViewBean(Numeraire numeraire) {
        super();
        this.numeraire = numeraire;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        numeraire = PegasusServiceLocator.getDroitService().createNumeraire(getDroit(),
                getInstanceDroitMembreFamille(), numeraire);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = numeraire.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        numeraire = PegasusServiceLocator.getDroitService().deleteNumeraire(getDroit(), numeraire);
        this.updateListe(idDroitMbrFam);
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
        return numeraire.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listePretsEnversTiers;
    }

    /**
     * @return the numeraire
     */
    public Numeraire getNumeraire() {
        return numeraire;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return numeraire.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (numeraire != null) && !numeraire.isNew() ? new BSpy(numeraire.getSpy()) : new BSpy(
                (BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listePretsEnversTiers.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        numeraire = PegasusServiceLocator.getDroitService().readNumeraire(numeraire.getId());
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
        numeraire.setId(newId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listePretsEnversTiers = (PCNumeraireAjaxListViewBean) fwViewBeanInterface;
    }

    /**
     * @param Numeraire
     *            the Numeraire to set
     */
    public void setNumeraire(Numeraire numeraire) {
        this.numeraire = numeraire;
    }

    public void setPart(String part) {
        String[] parts = PCDonneeFinanciereAjaxViewBean.splitPart(part);
        numeraire.getSimpleNumeraire().setPartProprieteNumerateur(parts[0]);
        numeraire.getSimpleNumeraire().setPartProprieteDenominateur(parts[1]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            numeraire = (PegasusServiceLocator.getDroitService().createAndCloseNumeraire(getDroit(), numeraire, false));
        } else if (isForceClorePeriode()) {
            numeraire = (PegasusServiceLocator.getDroitService().createAndCloseNumeraire(getDroit(), numeraire, true));
        } else {
            numeraire = (PegasusServiceLocator.getDroitService().updateNumeraire(getDroit(),
                    getInstanceDroitMembreFamille(), numeraire));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(numeraire.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listePretsEnversTiers = new PCNumeraireAjaxListViewBean();
            NumeraireSearch search = listePretsEnversTiers.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersioned");
            listePretsEnversTiers.find();
        }
    }

}
