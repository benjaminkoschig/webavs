package globaz.pegasus.vb.revenusdepenses;

import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.revenusdepenses.FraisGarde;
import ch.globaz.pegasus.business.models.revenusdepenses.FraisGardeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.vulpecula.web.views.prestations.PrestationsViewService;
import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.jade.context.JadeThread;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.renderer.JadeBusinessMessageRendererDefaultStringAdapter;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PCFraisGardeAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final int INFO = 0;
    private static final int WARNING = 0;
    private static final int ERROR = 0;


    private FraisGarde fraisGarde = null;

    private Boolean doAddPeriode = null;
    private transient  PCFraisGardeAjaxListViewBean listeFraisGarde;
    private String logInfos = "";

    /**
     *
     */
    public PCFraisGardeAjaxViewBean() {
        super();
        fraisGarde = new FraisGarde();
    }

    /**
     * @param fraisGarde
     */
    public PCFraisGardeAjaxViewBean(FraisGarde fraisGarde) {
        super();
        this.fraisGarde = fraisGarde;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        fraisGarde = PegasusServiceLocator.getDroitService().createFraisGarde(getDroit(),
                getInstanceDroitMembreFamille(), fraisGarde);
        this.updateListe();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = fraisGarde.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        fraisGarde = PegasusServiceLocator.getDroitService().deleteFraisGarde(getDroit(), fraisGarde);
        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the FraisGarde
     */
    public FraisGarde getFraisGarde() {
        return fraisGarde;
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
        return fraisGarde.getId();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeFraisGarde;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return fraisGarde.getSimpleDonneeFinanciereHeader();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (fraisGarde != null) && !fraisGarde.isNew() ? new BSpy(fraisGarde.getSpy()) : new BSpy(
                (BSession) getISession());
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeFraisGarde.iterator();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        fraisGarde = PegasusServiceLocator.getDroitService().readFraisGarde(fraisGarde.getId());
    }

    /**
     * @param fraisGarde
     *            the FraisGarde to set
     */
    public void setFraisGarde(FraisGarde fraisGarde) {
        this.fraisGarde = fraisGarde;
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
        fraisGarde.setId(newId);
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeFraisGarde = (PCFraisGardeAjaxListViewBean) fwViewBeanInterface;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            fraisGarde = (PegasusServiceLocator.getDroitService().createAndCloseFraisGarde(getDroit(),
                    fraisGarde, false));
        } else if (isForceClorePeriode()) {
            fraisGarde = (PegasusServiceLocator.getDroitService().createAndCloseFraisGarde(getDroit(),
                    fraisGarde, true));
        } else {
            fraisGarde = (PegasusServiceLocator.getDroitService().updateFraisGarde(getDroit(),
                    getInstanceDroitMembreFamille(), fraisGarde));
        }

        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(fraisGarde.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {

        if (getListe) {
            listeFraisGarde = new PCFraisGardeAjaxListViewBean();
            FraisGardeSearch search = listeFraisGarde.getSearchModel();
            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersionedFraisGarde");
            listeFraisGarde.find();
        }

    }



}
