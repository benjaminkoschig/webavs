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
import ch.globaz.pegasus.business.models.fortuneparticuliere.AutreFortuneMobiliere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AutreFortuneMobiliereSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCAutreFortuneMobiliereAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements
        FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AutreFortuneMobiliere autreFortuneMobiliere = null;
    private Boolean doAddPeriode = null;
    private PCAutreFortuneMobiliereAjaxListViewBean listeAutresFortunesMobiliere;

    /**
	 * 
	 */
    public PCAutreFortuneMobiliereAjaxViewBean() {
        super();
        autreFortuneMobiliere = new AutreFortuneMobiliere();
    }

    /**
     * @param autreFortuneMobiliere
     */
    public PCAutreFortuneMobiliereAjaxViewBean(AutreFortuneMobiliere autreFortuneMobiliere) {
        super();
        this.autreFortuneMobiliere = autreFortuneMobiliere;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        autreFortuneMobiliere = PegasusServiceLocator.getDroitService().createAutreFortuneMobiliere(getDroit(),
                getInstanceDroitMembreFamille(), autreFortuneMobiliere);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = autreFortuneMobiliere.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        autreFortuneMobiliere = PegasusServiceLocator.getDroitService().deleteAutreFortuneMobiliere(getDroit(),
                autreFortuneMobiliere);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the autreFortuneMobiliere
     */
    public AutreFortuneMobiliere getAutreFortuneMobiliere() {
        return autreFortuneMobiliere;
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
        return autreFortuneMobiliere.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeAutresFortunesMobiliere;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return autreFortuneMobiliere.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (autreFortuneMobiliere != null) && !autreFortuneMobiliere.isNew() ? new BSpy(
                autreFortuneMobiliere.getSpy()) : new BSpy((BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeAutresFortunesMobiliere.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        autreFortuneMobiliere = PegasusServiceLocator.getDroitService().readAutreFortuneMobiliere(
                autreFortuneMobiliere.getId());
    }

    /**
     * @param AutreFortuneMobiliere
     *            the AutreFortuneMobiliere to set
     */
    public void setAutreFortuneMobiliere(AutreFortuneMobiliere autreFortuneMobiliere) {
        this.autreFortuneMobiliere = autreFortuneMobiliere;
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
        autreFortuneMobiliere.setId(newId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeAutresFortunesMobiliere = (PCAutreFortuneMobiliereAjaxListViewBean) fwViewBeanInterface;
    }

    public void setPart(String part) {
        String[] parts = PCDonneeFinanciereAjaxViewBean.splitPart(part);
        autreFortuneMobiliere.getSimpleAutreFortuneMobiliere().setPartProprieteNumerateur(parts[0]);
        autreFortuneMobiliere.getSimpleAutreFortuneMobiliere().setPartProprieteDenominateur(parts[1]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            autreFortuneMobiliere = (PegasusServiceLocator.getDroitService().createAndCloseAutreFortuneMobiliere(
                    getDroit(), autreFortuneMobiliere, false));
        } else if (isForceClorePeriode()) {
            autreFortuneMobiliere = (PegasusServiceLocator.getDroitService().createAndCloseAutreFortuneMobiliere(
                    getDroit(), autreFortuneMobiliere, true));
        } else {
            autreFortuneMobiliere = (PegasusServiceLocator.getDroitService().updateAutreFortuneMobiliere(getDroit(),
                    getInstanceDroitMembreFamille(), autreFortuneMobiliere));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(autreFortuneMobiliere.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listeAutresFortunesMobiliere = new PCAutreFortuneMobiliereAjaxListViewBean();
            AutreFortuneMobiliereSearch search = listeAutresFortunesMobiliere.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersioned");
            listeAutresFortunesMobiliere.find();
        }
    }

}
