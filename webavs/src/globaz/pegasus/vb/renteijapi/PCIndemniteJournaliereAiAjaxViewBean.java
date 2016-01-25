package globaz.pegasus.vb.renteijapi;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.renteijapi.IndemniteJournaliereAi;
import ch.globaz.pegasus.business.models.renteijapi.IndemniteJournaliereAiSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * Bean ajax pour le modele complexe IndemniteJournaliere 6.2010
 * 
 * @author SCE
 * 
 */
public class PCIndemniteJournaliereAiAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements
        FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean doAddPeriode = null;
    private IndemniteJournaliereAi indemniteJournaliereAi = null;
    private PCIndemniteJournaliereAiAjaxListViewBean listePretsEnversTiers;

    /**
     * Constructeur
     */
    public PCIndemniteJournaliereAiAjaxViewBean() {
        super();
        indemniteJournaliereAi = new IndemniteJournaliereAi();
    }

    /**
     * Constructeur avec Indemnite journaliera Ai en parametre
     * 
     * @param indemniteJournaliereAi
     */
    public PCIndemniteJournaliereAiAjaxViewBean(IndemniteJournaliereAi indemniteJournaliereAi) {
        super();
        this.indemniteJournaliereAi = indemniteJournaliereAi;
    }

    /**
     * Aput d'une entité
     */
    @Override
    public void add() throws Exception {
        indemniteJournaliereAi = PegasusServiceLocator.getDroitService().createIndemniteJournaliereAi(getDroit(),
                getInstanceDroitMembreFamille(), indemniteJournaliereAi);
        this.updateListe();

    }

    /**
     * Supression d'une entité
     */
    @Override
    public void delete() throws Exception {
        String idDroitMembreFamille = indemniteJournaliereAi.getSimpleDonneeFinanciereHeader()
                .getIdDroitMembreFamille();
        indemniteJournaliereAi = PegasusServiceLocator.getDroitService().deleteIndemniteJournaliereAi(getDroit(),
                indemniteJournaliereAi);
        this.updateListe(idDroitMembreFamille);

    }

    /**
     * @return the doAddPeriode
     */
    public Boolean getDoAddPeriode() {
        return doAddPeriode;
    }

    @Override
    public String getId() {
        return indemniteJournaliereAi.getId();
    }

    /**
     * @return the indemniteJournaliereAi
     */
    public IndemniteJournaliereAi getIndemniteJournaliereAi() {
        return indemniteJournaliereAi;
    }

    /**
     * @return the listePretsEnversTiers
     */
    public PCIndemniteJournaliereAiAjaxListViewBean getListePretsEnversTiers() {
        return listePretsEnversTiers;
    }

    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listePretsEnversTiers;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return indemniteJournaliereAi.getSimpleDonneeFinanciereHeader();
    }

    @Override
    public BSpy getSpy() {
        return (indemniteJournaliereAi != null) && !indemniteJournaliereAi.isNew() ? new BSpy(
                indemniteJournaliereAi.getSpy()) : new BSpy((BSession) getISession());
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
        indemniteJournaliereAi = PegasusServiceLocator.getDroitService().readIndemniteJournaliereAi(
                indemniteJournaliereAi.getId());

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
        indemniteJournaliereAi.setId(newId);

    }

    /**
     * @param indemniteJournaliereAi
     *            the indemniteJournaliereAi to set
     */
    public void setIndemniteJournaliereAi(IndemniteJournaliereAi indemniteJournaliereAi) {
        this.indemniteJournaliereAi = indemniteJournaliereAi;
    }

    /**
     * @param listePretsEnversTiers
     *            the listePretsEnversTiers to set
     */
    public void setListePretsEnversTiers(PCIndemniteJournaliereAiAjaxListViewBean listePretsEnversTiers) {
        this.listePretsEnversTiers = listePretsEnversTiers;
    }

    /**
     * Set le ListViewBean
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listePretsEnversTiers = (PCIndemniteJournaliereAiAjaxListViewBean) fwViewBeanInterface;

    }

    /**
     * Mise à jour
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            indemniteJournaliereAi = (PegasusServiceLocator.getDroitService().createAndCloseIndemniteJournaliereAi(
                    getDroit(), indemniteJournaliereAi, false));
        } else if (isForceClorePeriode()) {
            indemniteJournaliereAi = (PegasusServiceLocator.getDroitService().createAndCloseIndemniteJournaliereAi(
                    getDroit(), indemniteJournaliereAi, true));
        } else {
            indemniteJournaliereAi = (PegasusServiceLocator.getDroitService().updateIndemniteJournaliereAi(getDroit(),
                    getInstanceDroitMembreFamille(), indemniteJournaliereAi));
        }
        this.updateListe();

    }

    /**
     * Mise à jour de la liste
     * 
     * @throws Exception
     */
    private void updateListe() throws Exception {
        this.updateListe(indemniteJournaliereAi.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    /**
     * Mise à jour de la liste avec paramètre
     * 
     * @param idDroitMembreFamille
     * @throws Exception
     */
    private void updateListe(String idDroitMembreFamille) throws Exception {
        if (getListe) {
            listePretsEnversTiers = new PCIndemniteJournaliereAiAjaxListViewBean();
            IndemniteJournaliereAiSearch search = listePretsEnversTiers.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersioned");
            listePretsEnversTiers.find();
        }
    }
}
