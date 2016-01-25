package globaz.pegasus.vb.revenusdepenses;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependante;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependanteSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCRevenuActiviteLucrativeIndependanteAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements
        FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Boolean doAddPeriode = null;

    private PCRevenuActiviteLucrativeIndependanteAjaxListViewBean listeRevenuActiviteLucrativeIndependante;
    private RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante = null;

    /**
	 * 
	 */
    public PCRevenuActiviteLucrativeIndependanteAjaxViewBean() {
        super();
        revenuActiviteLucrativeIndependante = new RevenuActiviteLucrativeIndependante();
    }

    /**
     * @param RevenuActiviteLucrativeIndependante
     */
    public PCRevenuActiviteLucrativeIndependanteAjaxViewBean(
            RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante) {
        super();
        this.revenuActiviteLucrativeIndependante = revenuActiviteLucrativeIndependante;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        revenuActiviteLucrativeIndependante = PegasusServiceLocator.getDroitService()
                .createRevenuActiviteLucrativeIndependante(getDroit(), getInstanceDroitMembreFamille(),
                        revenuActiviteLucrativeIndependante);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = revenuActiviteLucrativeIndependante.getSimpleDonneeFinanciereHeader()
                .getIdDroitMembreFamille();
        revenuActiviteLucrativeIndependante = PegasusServiceLocator.getDroitService()
                .deleteRevenuActiviteLucrativeIndependante(getDroit(), revenuActiviteLucrativeIndependante);
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
        return revenuActiviteLucrativeIndependante.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeRevenuActiviteLucrativeIndependante;
    }

    public String getNoAffilie() {
        return revenuActiviteLucrativeIndependante.getSimpleAffiliation().getAffilieNumero();
    }

    public String getNomAffilie() {
        if (revenuActiviteLucrativeIndependante.getTiersAffilie() != null) {
            return revenuActiviteLucrativeIndependante.getTiersAffilie().getDesignation1() + " "
                    + revenuActiviteLucrativeIndependante.getTiersAffilie().getDesignation2();
        } else {
            return "";
        }
    }

    public String getNomCaisse() {
        if (revenuActiviteLucrativeIndependante.getCaisse() != null) {
            return revenuActiviteLucrativeIndependante.getCaisse().getTiers().getDesignation1();
        } else {
            return "";
        }
    }

    /**
     * @return the revenuActiviteLucrativeIndependante
     */
    public RevenuActiviteLucrativeIndependante getRevenuActiviteLucrativeIndependante() {
        return revenuActiviteLucrativeIndependante;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return revenuActiviteLucrativeIndependante.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (revenuActiviteLucrativeIndependante != null) && !revenuActiviteLucrativeIndependante.isNew() ? new BSpy(
                revenuActiviteLucrativeIndependante.getSpy()) : new BSpy((BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeRevenuActiviteLucrativeIndependante.iterator();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        revenuActiviteLucrativeIndependante = PegasusServiceLocator.getDroitService()
                .readRevenuActiviteLucrativeIndependante(revenuActiviteLucrativeIndependante.getId());
    }

    public void setCsGenreRevenu(String csGenreRevenu) {
        revenuActiviteLucrativeIndependante.getSimpleRevenuActiviteLucrativeIndependante().setCsGenreRevenu(
                csGenreRevenu);
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
        revenuActiviteLucrativeIndependante.setId(newId);
    }

    /*
     * public void setMontantActivite(String montantActiviteLucrative) { this.revenuActiviteLucrativeIndependante
     * .getSimpleRevenuActiviteLucrativeIndependante() .setMontantActiviteLucrative(montantActiviteLucrative); }
     */

    public void setIdCaisseCompensation(String idCaisseCompensation) {
        revenuActiviteLucrativeIndependante.getSimpleRevenuActiviteLucrativeIndependante().setIdCaisseCompensation(
                idCaisseCompensation);
    }

    public void setIdTiersAffilie(String idTiersAffilie) {
        revenuActiviteLucrativeIndependante.getSimpleRevenuActiviteLucrativeIndependante().setIdTiersAffilie(
                idTiersAffilie);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeRevenuActiviteLucrativeIndependante = (PCRevenuActiviteLucrativeIndependanteAjaxListViewBean) fwViewBeanInterface;
    }

    /**
     * @param RevenuActiviteLucrativeIndependante
     *            the RevenuActiviteLucrativeIndependante to set
     */
    public void setRevenuActiviteLucrativeIndependante(
            RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante) {
        this.revenuActiviteLucrativeIndependante = revenuActiviteLucrativeIndependante;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            revenuActiviteLucrativeIndependante = (PegasusServiceLocator.getDroitService()
                    .createAndCloseRevenuActiviteLucrativeIndependante(getDroit(), revenuActiviteLucrativeIndependante,
                            false));
        } else if (isForceClorePeriode()) {
            revenuActiviteLucrativeIndependante = (PegasusServiceLocator.getDroitService()
                    .createAndCloseRevenuActiviteLucrativeIndependante(getDroit(), revenuActiviteLucrativeIndependante,
                            true));
        } else {
            revenuActiviteLucrativeIndependante = (PegasusServiceLocator.getDroitService()
                    .updateRevenuActiviteLucrativeIndependante(getDroit(), getInstanceDroitMembreFamille(),
                            revenuActiviteLucrativeIndependante));
        }
        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(revenuActiviteLucrativeIndependante.getSimpleDonneeFinanciereHeader()
                .getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listeRevenuActiviteLucrativeIndependante = new PCRevenuActiviteLucrativeIndependanteAjaxListViewBean();
            RevenuActiviteLucrativeIndependanteSearch search = listeRevenuActiviteLucrativeIndependante
                    .getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersionedRALI");
            listeRevenuActiviteLucrativeIndependante.find();
        }
    }

}
