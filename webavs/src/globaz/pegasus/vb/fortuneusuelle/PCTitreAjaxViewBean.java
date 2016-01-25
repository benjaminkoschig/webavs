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
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.fortuneusuelle.Titre;
import ch.globaz.pegasus.business.models.fortuneusuelle.TitreSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 * 
 */
public class PCTitreAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean doAddPeriode = null;
    private PCTitreAjaxListViewBean listeTitre;
    private Titre titre = null;

    /**
	 * 
	 */
    public PCTitreAjaxViewBean() {
        super();
        titre = new Titre();
    }

    /**
     * @param titre
     */
    public PCTitreAjaxViewBean(Titre titre) {
        super();
        this.titre = titre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        titre = PegasusServiceLocator.getDroitService().createTitre(getDroit(), getInstanceDroitMembreFamille(), titre);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = titre.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        titre = PegasusServiceLocator.getDroitService().deleteTitre(getDroit(), titre);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the doAddPeriode
     */
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
        return titre.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeTitre;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return titre.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (titre != null) && !titre.isNew() ? new BSpy(titre.getSpy()) : new BSpy((BSession) getISession());
    }

    /**
     * @return the titre
     */
    public Titre getTitre() {
        return titre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeTitre.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        titre = PegasusServiceLocator.getDroitService().readTitre(titre.getId());
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
        titre.setId(newId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeTitre = (PCTitreAjaxListViewBean) fwViewBeanInterface;
    }

    public void setPart(String part) {
        String[] parts = PCDonneeFinanciereAjaxViewBean.splitPart(part);
        titre.getSimpleTitre().setPartProprieteNumerateur(parts[0]);
        titre.getSimpleTitre().setPartProprieteDenominateur(parts[1]);
    }

    /**
     * @param Titre
     *            the Titre to set
     */
    public void setTitre(Titre titre) {
        this.titre = titre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            titre = (PegasusServiceLocator.getDroitService().createAndCloseTitre(getDroit(), titre, false));
        } else if (isForceClorePeriode()) {
            titre = (PegasusServiceLocator.getDroitService().createAndCloseTitre(getDroit(), titre, true));
        } else {
            titre = (PegasusServiceLocator.getDroitService().updateTitre(getDroit(), getInstanceDroitMembreFamille(),
                    titre));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(titre.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listeTitre = new PCTitreAjaxListViewBean();
            TitreSearch search = listeTitre.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersionedT");
            listeTitre.find();
        }
    }

}
