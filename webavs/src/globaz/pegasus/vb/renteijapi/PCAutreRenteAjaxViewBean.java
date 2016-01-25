package globaz.pegasus.vb.renteijapi;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.renteijapi.AutreRente;
import ch.globaz.pegasus.business.models.renteijapi.AutreRenteSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author DMA
 * @date 24 juin 2010
 */
public class PCAutreRenteAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AutreRente autreRente = null;
    private Boolean doAddPeriode = null;
    private PCAutreRenteAjaxListViewBean listeAutreRente;

    public PCAutreRenteAjaxViewBean() {
        super();
        autreRente = new AutreRente();
    }

    public PCAutreRenteAjaxViewBean(AutreRente autreRente) {
        super();
        this.autreRente = autreRente;
    }

    @Override
    public void add() throws Exception {

        autreRente = PegasusServiceLocator.getDroitService().createAutreRente(getDroit(),
                getInstanceDroitMembreFamille(), autreRente);
        this.updateListe();

    }

    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = autreRente.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        autreRente = PegasusServiceLocator.getDroitService().deleteAutreRente(getDroit(), autreRente);

        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the autreRente
     */
    public AutreRente getAutreRente() {
        return autreRente;
    }

    /**
     * @return the doAddPeriode
     */
    public Boolean getDoAddPeriode() {
        return doAddPeriode;
    }

    @Override
    public String getId() {
        return autreRente.getId();
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeAutreRente;
    }

    public String getPays(BSession objSession) {
        String libelle = "";
        if ("fr".equals(objSession.getIdLangueISO())) {
            libelle = getAutreRente().getSimplePays().getLibelleFr();
        } else if ("de".equals(objSession.getIdLangueISO())) {
            libelle = getAutreRente().getSimplePays().getLibelleAl();
        } else {
            libelle = getAutreRente().getSimplePays().getLibelleIt();
        }
        return libelle;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return autreRente.getSimpleDonneeFinanciereHeader();
    }

    @Override
    public BSpy getSpy() {
        return (autreRente != null) && !autreRente.isNew() ? new BSpy(autreRente.getSpy()) : new BSpy(
                (BSession) getISession());
    }

    @Override
    public Iterator iterator() {
        return listeAutreRente.iterator();
    }

    @Override
    public void retrieve() throws Exception {
        autreRente = PegasusServiceLocator.getDroitService().readAutreRente(autreRente.getId());

    }

    /**
     * @param autreRente
     *            the autreRente to set
     */
    public void setAutreRente(AutreRente autreRente) {
        this.autreRente = autreRente;
    }

    /**
     * @param doAddPeriode
     *            the doAddPeriode to set
     */
    public void setDoAddPeriode(Boolean doAddPeriode) {
        this.doAddPeriode = doAddPeriode;
    }

    @Override
    public void setId(String newId) {
        autreRente.setId(newId);
    }

    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeAutreRente = (PCAutreRenteAjaxListViewBean) fwViewBeanInterface;

    }

    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            autreRente = (PegasusServiceLocator.getDroitService().createAndCloseAutreRente(getDroit(), autreRente,
                    false));
        } else if (isForceClorePeriode()) {
            autreRente = (PegasusServiceLocator.getDroitService()
                    .createAndCloseAutreRente(getDroit(), autreRente, true));
        } else {
            autreRente = (PegasusServiceLocator.getDroitService().updateAutreRente(getDroit(),
                    getInstanceDroitMembreFamille(), autreRente));
        }
        this.updateListe();

    }

    private void updateListe() throws Exception {
        this.updateListe(autreRente.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listeAutreRente = new PCAutreRenteAjaxListViewBean();
            AutreRenteSearch search = listeAutreRente.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersioned");
            listeAutreRente.find();
        }
    }

}
