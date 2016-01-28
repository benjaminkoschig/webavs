package globaz.pegasus.vb.revenusdepenses;

import globaz.framework.bean.FWAJAXViewBeanInterface;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.pegasus.vb.droit.PCDonneeFinanciereAjaxViewBean;
import java.util.Iterator;
import ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.revenusdepenses.ContratEntretienViager;
import ch.globaz.pegasus.business.models.revenusdepenses.ContratEntretienViagerSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleLibelleContratEntretienViager;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleLibelleContratEntretienViagerSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCContratEntretienViagerAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements
        FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private ContratEntretienViager ContratEntretienViager = null;

    private Boolean doAddPeriode = null;

    private PCContratEntretienViagerAjaxListViewBean listeContratEntretienViager;
    private SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager = null;

    private SimpleLibelleContratEntretienViagerSearch simpleLibelleContratEntretienViagerSearch = null;

    /**
	 * 
	 */
    public PCContratEntretienViagerAjaxViewBean() {
        super();
        ContratEntretienViager = new ContratEntretienViager();
        simpleLibelleContratEntretienViager = new SimpleLibelleContratEntretienViager();
        simpleLibelleContratEntretienViagerSearch = new SimpleLibelleContratEntretienViagerSearch();
    }

    /**
     * @param ContratEntretienViager
     * 
     */
    public PCContratEntretienViagerAjaxViewBean(ContratEntretienViager ContratEntretienViager) {
        super();
        this.ContratEntretienViager = ContratEntretienViager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {

        // ajout du type de frais
        if (simpleLibelleContratEntretienViager != null) {
            // on decoupe en paquet de 8
            // simpleLibelleContratEntretienViager.getCsFraisObtentionRevenu();
            // -> on
            // ne le fait pas ici
            String libelle = simpleLibelleContratEntretienViager.getCsLibelleContratEntretienViager();
            if (libelle != null) {

                for (int i = 0; i < (libelle.length() / 8); i++) {
                    SimpleLibelleContratEntretienViager libelleContratEntretienViager = new SimpleLibelleContratEntretienViager();
                    libelleContratEntretienViager.setCsLibelleContratEntretienViager(libelle.substring(i * 8,
                            (i + 1) * 8));

                    ContratEntretienViager.getListLiebelleContratEntretienViagers().add(libelleContratEntretienViager);
                    /*
                     * this.simpleLibelleContratEntretienViager = PegasusServiceLocator.getDroitService()
                     * .createSimpleLibelleContratEntretienViager(this.ContratEntretienViager.getId(),
                     * this.simpleLibelleContratEntretienViager);
                     */
                }
            }
        }

        ContratEntretienViager = PegasusServiceLocator.getDroitService().createContratEntretienViager(getDroit(),
                getInstanceDroitMembreFamille(), ContratEntretienViager);

        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = ContratEntretienViager.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille();
        ContratEntretienViager = PegasusServiceLocator.getDroitService().deleteContratEntretienViager(getDroit(),
                ContratEntretienViager);

        // type frais
        if (simpleLibelleContratEntretienViager != null) {
            // on decoupe en paquet de 8
            for (int i = 0; i < simpleLibelleContratEntretienViagerSearch.getSearchResults().length; i++) {

                simpleLibelleContratEntretienViager = (SimpleLibelleContratEntretienViager) simpleLibelleContratEntretienViagerSearch
                        .getSearchResults()[i];
                simpleLibelleContratEntretienViager = PegasusServiceLocator.getDroitService()
                        .deleteSimpleLibelleContratEntretienViager(getDroit(), simpleLibelleContratEntretienViager);
            }
        }

        this.updateListe(idDroitMbrFam);
    }

    /**
     * @return the ContratEntretienViager
     */
    public ContratEntretienViager getContratEntretienViager() {
        return ContratEntretienViager;
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
        return ContratEntretienViager.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeContratEntretienViager;
    }

    protected JadeAbstractSearchModel getManagerModel() {
        return simpleLibelleContratEntretienViagerSearch;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return ContratEntretienViager.getSimpleDonneeFinanciereHeader();
    }

    public SimpleLibelleContratEntretienViager getSimpleLibelleContratEntretienViager() {
        return simpleLibelleContratEntretienViager;
    }

    /**
     * @return the simpleLibelleContratEntretienViagerSearch
     */
    public SimpleLibelleContratEntretienViagerSearch getSimpleLibelleContratEntretienViagerSearch() {
        return simpleLibelleContratEntretienViagerSearch;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (ContratEntretienViager != null) && !ContratEntretienViager.isNew() ? new BSpy(
                ContratEntretienViager.getSpy()) : new BSpy((BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeContratEntretienViager.iterator();
    }

    @Override
    public void retrieve() throws Exception {
        ContratEntretienViager = PegasusServiceLocator.getDroitService().readContratEntretienViager(
                ContratEntretienViager.getId());

        simpleLibelleContratEntretienViagerSearch.setForIdContratEntretienViager(ContratEntretienViager.getId());

        simpleLibelleContratEntretienViagerSearch = PegasusServiceLocator.getDroitService()
                .searchSimpleLibelleContratEntretienViager(simpleLibelleContratEntretienViagerSearch);

    }

    /**
     * @param ContratEntretienViager
     *            the ContratEntretienViager to set
     */
    public void setContratEntretienViager(ContratEntretienViager ContratEntretienViager) {
        this.ContratEntretienViager = ContratEntretienViager;
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
        ContratEntretienViager.setId(newId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeContratEntretienViager = (PCContratEntretienViagerAjaxListViewBean) fwViewBeanInterface;
    }

    public void setMontantContrat(String montantContrat) {
        ContratEntretienViager.getSimpleContratEntretienViager().setMontantContrat(montantContrat);
    }

    public void setSimpleLibelleContratEntretienViager(
            SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager) {
        this.simpleLibelleContratEntretienViager = simpleLibelleContratEntretienViager;
    }

    /**
     * @param homeSearch
     *            the homeSearch to set
     */
    public void setSimpleLibelleContratEntretienViagerSearch(
            SimpleLibelleContratEntretienViagerSearch simpleLibelleContratEntretienViagerSearch) {
        this.simpleLibelleContratEntretienViagerSearch = simpleLibelleContratEntretienViagerSearch;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {

        if (doAddPeriode.booleanValue()) {
            ContratEntretienViager = (PegasusServiceLocator.getDroitService().createAndCloseContratEntretienViager(
                    getDroit(), ContratEntretienViager, false));
        } else if (isForceClorePeriode()) {
            ContratEntretienViager = (PegasusServiceLocator.getDroitService().createAndCloseContratEntretienViager(
                    getDroit(), ContratEntretienViager, true));
        } else {
            ContratEntretienViager = (PegasusServiceLocator.getDroitService().updateContratEntretienViager(getDroit(),
                    getInstanceDroitMembreFamille(), ContratEntretienViager));
        }

        simpleLibelleContratEntretienViagerSearch.setForIdContratEntretienViager(ContratEntretienViager
                .getSimpleContratEntretienViager().getIdContratEntretienViager());
        simpleLibelleContratEntretienViagerSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        simpleLibelleContratEntretienViagerSearch = PegasusServiceLocator.getDroitService()
                .searchSimpleLibelleContratEntretienViager(simpleLibelleContratEntretienViagerSearch);

        // type frais
        // sauvegarde de la liste et de l'id
        String libelle = simpleLibelleContratEntretienViager.getCsLibelleContratEntretienViager();
        String id = simpleLibelleContratEntretienViagerSearch.getForIdContratEntretienViager();

        // on fais un delete
        if (simpleLibelleContratEntretienViager != null) {

            for (int i = 0; i < simpleLibelleContratEntretienViagerSearch.getSearchResults().length; i++) {

                simpleLibelleContratEntretienViager = (SimpleLibelleContratEntretienViager) simpleLibelleContratEntretienViagerSearch
                        .getSearchResults()[i];
                simpleLibelleContratEntretienViager = PegasusServiceLocator.getDroitService()
                        .deleteSimpleLibelleContratEntretienViager(getDroit(), simpleLibelleContratEntretienViager);
            }
        }

        // puis un create
        if (simpleLibelleContratEntretienViager != null) {

            if (libelle != null) {

                for (int i = 0; i < (libelle.length() / 8); i++) {
                    simpleLibelleContratEntretienViager.setCsLibelleContratEntretienViager(libelle.substring(i * 8,
                            (i + 1) * 8));

                    simpleLibelleContratEntretienViager = PegasusServiceLocator.getDroitService()
                            .createSimpleLibelleContratEntretienViager(id, simpleLibelleContratEntretienViager);
                }
            }
        }

        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(ContratEntretienViager.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {

        if (getListe) {
            listeContratEntretienViager = new PCContratEntretienViagerAjaxListViewBean();
            ContratEntretienViagerSearch search = listeContratEntretienViager.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersionedCEV");
            listeContratEntretienViager.find();
        }
    }
}
