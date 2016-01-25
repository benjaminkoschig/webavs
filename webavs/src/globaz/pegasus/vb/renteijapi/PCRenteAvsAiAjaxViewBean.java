package globaz.pegasus.vb.renteijapi;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.jade.client.util.JadeDateUtil;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Date;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAi;
import ch.globaz.pegasus.business.models.renteijapi.RenteAvsAiSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * ViewBean pour les rentes AvsAi 6.2010
 * 
 * @author SCE
 * 
 */
public class PCRenteAvsAiAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Boolean doAddPeriode = null;

    private PCRenteAvsAiAjaxListViewBean listePretsEnversTiers;

    private RenteAvsAi renteAvsAi = null;

    /**
     * Constructeur
     */
    public PCRenteAvsAiAjaxViewBean() {

        super();
        renteAvsAi = new RenteAvsAi();
    }

    /**
     * Constructeur avec RenteAvsAi
     * 
     * @param renteAvsAi
     *            la Rente
     */
    public PCRenteAvsAiAjaxViewBean(RenteAvsAi renteAvsAi) {
        super();
        this.renteAvsAi = renteAvsAi;
    }

    /**
     * Ajout d'une entité
     */
    @Override
    public void add() throws Exception {
        renteAvsAi = PegasusServiceLocator.getDroitService().createRenteAvsAi(getDroit(),
                getInstanceDroitMembreFamille(), renteAvsAi);
        this.updateListe();
    }

    /**
     * Supression d'une entité
     */
    @Override
    public void delete() throws Exception {
        String idDroitMembreFamille = renteAvsAi.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        renteAvsAi = PegasusServiceLocator.getDroitService().deleteRenteAvsAi(getDroit(), renteAvsAi);
        this.updateListe(idDroitMembreFamille);
    }

    /**
     * @return the doAddPeriode
     */
    public Boolean getDoAddPeriode() {
        return doAddPeriode;
    }

    /**
     * Retourne l'identifiant de l'entité
     */
    @Override
    public String getId() {
        return renteAvsAi.getId();
    }

    /**
     * @return the listePretsEnversTiers
     */
    public PCRenteAvsAiAjaxListViewBean getListePretsEnversTiers() {
        return listePretsEnversTiers;
    }

    /**
     * Retourne le ListViewBean
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listePretsEnversTiers;
    }

    /**
     * @return the renteAvsAi
     */
    public RenteAvsAi getRenteAvsAi() {
        return renteAvsAi;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return renteAvsAi.getSimpleDonneeFinanciereHeader();
    }

    /**
     * Retourne le champ espion
     */
    @Override
    public BSpy getSpy() {
        return (renteAvsAi != null) && !renteAvsAi.isNew() ? new BSpy(renteAvsAi.getSpy()) : new BSpy(
                (BSession) getISession());
    }

    public boolean isPeriodeCourante() {

        String now = JadeDateUtil.getGlobazFormattedDate(new Date());
        if (JadeDateUtil.isDateAfter(now, renteAvsAi.getSimpleDonneeFinanciereHeader().getDateDebut())) {
            return true;
        }
        return false;

    }

    /**
     * Retourne un objet Iterateur pour la liste
     */
    @Override
    public Iterator iterator() {

        return listePretsEnversTiers.iterator();
    }

    /**
     * Récupère l'entité
     */
    @Override
    public void retrieve() throws Exception {
        renteAvsAi = PegasusServiceLocator.getDroitService().readRenteAvsAi(renteAvsAi.getId());
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
        renteAvsAi.setId(newId);

    }

    /**
     * @param listePretsEnversTiers
     *            the listePretsEnversTiers to set
     */
    public void setListePretsEnversTiers(PCRenteAvsAiAjaxListViewBean listePretsEnversTiers) {
        this.listePretsEnversTiers = listePretsEnversTiers;
    }

    /**
     * Set le ListViewBean
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listePretsEnversTiers = (PCRenteAvsAiAjaxListViewBean) fwViewBeanInterface;

    }

    /**
     * @param renteAvsAi
     *            the renteAvsAi to set
     */
    public void setRenteAvsAi(RenteAvsAi renteAvsAi) {
        this.renteAvsAi = renteAvsAi;
    }

    /**
     * Mise à jour de l'entité
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            renteAvsAi = (PegasusServiceLocator.getDroitService().createAndCloseRenteAvsAi(getDroit(), renteAvsAi,
                    false));
        } else if (isForceClorePeriode()) {
            renteAvsAi = (PegasusServiceLocator.getDroitService()
                    .createAndCloseRenteAvsAi(getDroit(), renteAvsAi, true));
        } else {
            renteAvsAi = (PegasusServiceLocator.getDroitService().updateRenteAvsAi(getDroit(),
                    getInstanceDroitMembreFamille(), renteAvsAi));
        }
        this.updateListe();

    }

    /**
     * Mise à jour de la liste
     * 
     * @throws Exception
     */
    private void updateListe() throws Exception {
        this.updateListe(renteAvsAi.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    /**
     * Mise à jour de la liste avec paramètre
     * 
     * @param idDroitMembreFamille
     * @throws Exception
     */
    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listePretsEnversTiers = new PCRenteAvsAiAjaxListViewBean();
            RenteAvsAiSearch search = listePretsEnversTiers.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersioned");
            listePretsEnversTiers.find();
        }
    }

}
