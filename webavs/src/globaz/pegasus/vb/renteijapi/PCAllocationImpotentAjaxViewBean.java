package globaz.pegasus.vb.renteijapi;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.renteijapi.AllocationImpotent;
import ch.globaz.pegasus.business.models.renteijapi.AllocationImpotentSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * Bean ajax pour le modele complexe Allocation Impotent 6.2010
 * 
 * @author SCE
 * 
 */
public class PCAllocationImpotentAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AllocationImpotent allocationImpotent = null;
    private Boolean doAddPeriode = null;
    private PCAllocationImpotentAjaxListViewBean listePretsEnversTiers;

    /**
     * Constructeur
     */
    public PCAllocationImpotentAjaxViewBean() {
        super();
        allocationImpotent = new AllocationImpotent();
    }

    /**
     * Constructeur avec Indemnite journaliera Ai en parametre
     * 
     * @param indemniteJournaliereAi
     */
    public PCAllocationImpotentAjaxViewBean(AllocationImpotent allocationImpotent) {
        super();
        this.allocationImpotent = allocationImpotent;
    }

    /**
     * Aput d'une entité
     */
    @Override
    public void add() throws Exception {
        allocationImpotent = PegasusServiceLocator.getDroitService().createAllocationImpotent(getDroit(),
                getInstanceDroitMembreFamille(), allocationImpotent);
        this.updateListe();

    }

    /**
     * Supression d'une entité
     */
    @Override
    public void delete() throws Exception {
        String idDroitMembreFamille = allocationImpotent.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        allocationImpotent = PegasusServiceLocator.getDroitService().deleteAllocationImpotent(getDroit(),
                allocationImpotent);
        this.updateListe(idDroitMembreFamille);

    }

    /**
     * @return the allocationImpotent
     */
    public AllocationImpotent getAllocationImpotent() {
        return allocationImpotent;
    }

    /**
     * @return the doAddPeriode
     */
    public Boolean getDoAddPeriode() {
        return doAddPeriode;
    }

    @Override
    public String getId() {
        return allocationImpotent.getId();
    }

    /**
     * @return the listePretsEnversTiers
     */
    public PCAllocationImpotentAjaxListViewBean getListePretsEnversTiers() {
        return listePretsEnversTiers;
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listePretsEnversTiers;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return allocationImpotent.getSimpleDonneeFinanciereHeader();
    }

    @Override
    public BSpy getSpy() {
        return (allocationImpotent != null) && !allocationImpotent.isNew() ? new BSpy(allocationImpotent.getSpy())
                : new BSpy((BSession) getISession());
    }

    /**
     * Retourne un objet Iterateur pour la liste
     */
    @Override
    public Iterator iterator() {
        return listePretsEnversTiers.iterator();
    }

    @Override
    public void retrieve() throws Exception {
        allocationImpotent = PegasusServiceLocator.getDroitService().readAllocationImpotent(allocationImpotent.getId());

    }

    /**
     * @param allocationImpotent
     *            the allocationImpotent to set
     */
    public void setAllocationImpotent(AllocationImpotent allocationImpotent) {
        this.allocationImpotent = allocationImpotent;
    }

    /**
     * @param doAddPeriode
     *            the doAddPeriode to set
     */
    public void setDoAddPeriode(Boolean doAddPeriode) {
        this.doAddPeriode = doAddPeriode;
    }

    /**
     * Set l'id
     */
    @Override
    public void setId(String newId) {
        allocationImpotent.setId(newId);

    }

    /**
     * @param listePretsEnversTiers
     *            the listePretsEnversTiers to set
     */
    public void setListePretsEnversTiers(PCAllocationImpotentAjaxListViewBean listePretsEnversTiers) {
        this.listePretsEnversTiers = listePretsEnversTiers;
    }

    /**
     * Set le ListViewBean
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listePretsEnversTiers = (PCAllocationImpotentAjaxListViewBean) fwViewBeanInterface;

    }

    /**
     * Mise à jour
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            allocationImpotent = (PegasusServiceLocator.getDroitService().createAndCloseAllocationImpotent(getDroit(),
                    allocationImpotent, false));
        } else if (isForceClorePeriode()) {
            allocationImpotent = (PegasusServiceLocator.getDroitService().createAndCloseAllocationImpotent(getDroit(),
                    allocationImpotent, true));
        } else {
            allocationImpotent = (PegasusServiceLocator.getDroitService().updateAllocationImpotent(getDroit(),
                    getInstanceDroitMembreFamille(), allocationImpotent));
        }
        this.updateListe();

    }

    /**
     * Mise à jour de la liste
     * 
     * @throws Exception
     */
    private void updateListe() throws Exception {
        this.updateListe(allocationImpotent.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    /**
     * Mise à jour de la liste avec paramètre
     * 
     * @param idDroitMembreFamille
     * @throws Exception
     */
    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listePretsEnversTiers = new PCAllocationImpotentAjaxListViewBean();
            AllocationImpotentSearch search = listePretsEnversTiers.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersioned");
            listePretsEnversTiers.find();
        }
    }
}
