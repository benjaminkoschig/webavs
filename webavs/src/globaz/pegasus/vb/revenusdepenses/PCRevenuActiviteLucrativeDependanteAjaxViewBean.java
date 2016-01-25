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
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeDependante;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeDependanteSearch;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenu;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenuSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCRevenuActiviteLucrativeDependanteAjaxViewBean extends PCDonneeFinanciereAjaxViewBean implements
        FWAJAXViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Boolean doAddPeriode = null;

    private PCRevenuActiviteLucrativeDependanteAjaxListViewBean listeRevenuActiviteLucrativeDependante;

    private RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante = null;

    private SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu = null;

    private SimpleTypeFraisObtentionRevenuSearch simpleTypeFraisObtentionRevenuSearch = null;

    /**
	 * 
	 */
    public PCRevenuActiviteLucrativeDependanteAjaxViewBean() {
        super();
        revenuActiviteLucrativeDependante = new RevenuActiviteLucrativeDependante();
        simpleTypeFraisObtentionRevenu = new SimpleTypeFraisObtentionRevenu();
        simpleTypeFraisObtentionRevenuSearch = new SimpleTypeFraisObtentionRevenuSearch();
    }

    /**
     * @param RevenuActiviteLucrativeDependante
     * 
     */
    public PCRevenuActiviteLucrativeDependanteAjaxViewBean(
            RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante) {
        super();
        this.revenuActiviteLucrativeDependante = revenuActiviteLucrativeDependante;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {

        // ajout du type de frais

        if (simpleTypeFraisObtentionRevenu != null) {
            // on decoupe en paquet de 8. Et la marmotte ?
            // simpleTypeFraisObtentionRevenu.getCsFraisObtentionRevenu(); -> on
            // ne le fait pas ici
            String fraisObtention = simpleTypeFraisObtentionRevenu.getCsFraisObtentionRevenu();
            if (fraisObtention != null) {

                for (int i = 0; i < (fraisObtention.length() / 8); i++) {
                    SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu = new SimpleTypeFraisObtentionRevenu();
                    simpleTypeFraisObtentionRevenu.setCsFraisObtentionRevenu(fraisObtention.substring(i * 8,
                            (i + 1) * 8));
                    revenuActiviteLucrativeDependante.getListTypeDeFrais().add(simpleTypeFraisObtentionRevenu);
                    // this.simpleTypeFraisObtentionRevenu = PegasusServiceLocator.getDroitService()
                    // .createSimpleTypeFraisObtentionRevenu(this.revenuActiviteLucrativeDependante.getId(),
                    // this.simpleTypeFraisObtentionRevenu);
                }
            }
        }

        revenuActiviteLucrativeDependante = PegasusServiceLocator.getDroitService()
                .createRevenuActiviteLucrativeDependante(getDroit(), getInstanceDroitMembreFamille(),
                        revenuActiviteLucrativeDependante);

        simpleTypeFraisObtentionRevenuSearch
                .setForIdRevenuActiviteLucrativeDependante(revenuActiviteLucrativeDependante.getId());

        simpleTypeFraisObtentionRevenuSearch = PegasusServiceLocator.getDroitService()
                .searchSimpleTypeFraisObtentionRevenu(simpleTypeFraisObtentionRevenuSearch);

        this.updateListe();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        String idDroitMbrFam = revenuActiviteLucrativeDependante.getSimpleDonneeFinanciereHeader()
                .getIdDroitMembreFamille();
        revenuActiviteLucrativeDependante = PegasusServiceLocator.getDroitService()
                .deleteRevenuActiviteLucrativeDependante(getDroit(), revenuActiviteLucrativeDependante);

        // type frais
        if (simpleTypeFraisObtentionRevenu != null) {
            // on decoupe en paquet de 8
            // simpleTypeFraisObtentionRevenu.getCsFraisObtentionRevenu(); -> on
            // ne le fait pas ici

            for (int i = 0; i < simpleTypeFraisObtentionRevenuSearch.getSearchResults().length; i++) {

                simpleTypeFraisObtentionRevenu = (SimpleTypeFraisObtentionRevenu) simpleTypeFraisObtentionRevenuSearch
                        .getSearchResults()[i];
                simpleTypeFraisObtentionRevenu = PegasusServiceLocator.getDroitService()
                        .deleteSimpleTypeFraisObtentionRevenu(getDroit(), simpleTypeFraisObtentionRevenu);
            }
        }

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
        return revenuActiviteLucrativeDependante.getId();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#getListViewBean()
     */
    @Override
    public FWListViewBeanInterface getListViewBean() {
        return listeRevenuActiviteLucrativeDependante;
    }

    protected JadeAbstractSearchModel getManagerModel() {
        return simpleTypeFraisObtentionRevenuSearch;
    }

    public String getNoAffilie() {
        return revenuActiviteLucrativeDependante.getSimpleAffiliation().getAffilieNumero();
    }

    public String getNomEmployeur() {
        if (revenuActiviteLucrativeDependante.getEmployeur() != null) {
            return revenuActiviteLucrativeDependante.getEmployeur().getDesignation1() + " "
                    + revenuActiviteLucrativeDependante.getEmployeur().getDesignation2();
        } else {
            return "";
        }
    }

    /**
     * @return the revenuActiviteLucrativeDependante
     */
    public RevenuActiviteLucrativeDependante getRevenuActiviteLucrativeDependante() {
        return revenuActiviteLucrativeDependante;
    }

    @Override
    public SimpleDonneeFinanciereHeader getSimpleDonneeFinanciereHeader() {
        return revenuActiviteLucrativeDependante.getSimpleDonneeFinanciereHeader();
    }

    public SimpleTypeFraisObtentionRevenu getSimpleTypeFraisObtentionRevenu() {
        return simpleTypeFraisObtentionRevenu;
    }

    /**
     * @return the homeSearch
     */
    public SimpleTypeFraisObtentionRevenuSearch getSimpleTypeFraisObtentionRevenuSearch() {
        return simpleTypeFraisObtentionRevenuSearch;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (revenuActiviteLucrativeDependante != null) && !revenuActiviteLucrativeDependante.isNew() ? new BSpy(
                revenuActiviteLucrativeDependante.getSpy()) : new BSpy((BSession) getISession());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#iterator()
     */
    @Override
    public Iterator iterator() {
        return listeRevenuActiviteLucrativeDependante.iterator();
    }

    @Override
    public void retrieve() throws Exception {
        revenuActiviteLucrativeDependante = PegasusServiceLocator.getDroitService()
                .readRevenuActiviteLucrativeDependante(revenuActiviteLucrativeDependante.getId());

        // type frais
        // il nous faut tout les champs csFraisObtentionRevenu qui ont pour
        // id=idRevenuActiviteLucrativeDependante
        // modele complexe qui est la jointure des 2 tables?

        //

        simpleTypeFraisObtentionRevenuSearch
                .setForIdRevenuActiviteLucrativeDependante(revenuActiviteLucrativeDependante.getId());

        simpleTypeFraisObtentionRevenuSearch = PegasusServiceLocator.getDroitService()
                .searchSimpleTypeFraisObtentionRevenu(simpleTypeFraisObtentionRevenuSearch);

        /*
         * this.simpleTypeFraisObtentionRevenu = PegasusServiceLocator
         * .getDroitService().searchSimpleTypeFraisObtentionRevenu (this.simpleTypeFraisObtentionRevenuSearch);
         */
        /*
         * this.simpleTypeFraisObtentionRevenuSearch = PegasusServiceLocator
         * .getDroitService().searchSimpleTypeFraisObtentionRevenu( this.simpleTypeFraisObtentionRevenuSearch);
         */

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    public void setCsGenreRevenu(String csGenreRevenu) {
        revenuActiviteLucrativeDependante.getSimpleRevenuActiviteLucrativeDependante().setCsGenreRevenu(csGenreRevenu);
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
        revenuActiviteLucrativeDependante.setId(newId);
    }

    public void setIdEmployeur(String idEmployeur) {
        revenuActiviteLucrativeDependante.getSimpleRevenuActiviteLucrativeDependante().setIdEmployeur(idEmployeur);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.ajax.FWAJAXViewBeanInterface#setListViewBean(globaz.framework .bean.FWViewBeanInterface)
     */
    @Override
    public void setListViewBean(FWViewBeanInterface fwViewBeanInterface) {
        listeRevenuActiviteLucrativeDependante = (PCRevenuActiviteLucrativeDependanteAjaxListViewBean) fwViewBeanInterface;
    }

    public void setMontantActivite(String montantActiviteLucrative) {
        revenuActiviteLucrativeDependante.getSimpleRevenuActiviteLucrativeDependante().setMontantActiviteLucrative(
                montantActiviteLucrative);
    }

    /**
     * @param RevenuActiviteLucrativeDependante
     *            the RevenuActiviteLucrativeDependante to set
     */
    public void setRevenuActiviteLucrativeDependante(RevenuActiviteLucrativeDependante revenuActiviteLucrativeDependante) {
        this.revenuActiviteLucrativeDependante = revenuActiviteLucrativeDependante;
    }

    public void setSimpleTypeFraisObtentionRevenu(SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu) {
        this.simpleTypeFraisObtentionRevenu = simpleTypeFraisObtentionRevenu;
    }

    /**
     * @param homeSearch
     *            the homeSearch to set
     */
    public void setSimpleTypeFraisObtentionRevenuSearch(
            SimpleTypeFraisObtentionRevenuSearch simpleTypeFraisObtentionRevenuSearch) {
        this.simpleTypeFraisObtentionRevenuSearch = simpleTypeFraisObtentionRevenuSearch;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        if (doAddPeriode.booleanValue()) {
            revenuActiviteLucrativeDependante = (PegasusServiceLocator.getDroitService()
                    .createAndCloseRevenuActiviteLucrativeDependante(getDroit(), revenuActiviteLucrativeDependante,
                            false));

        } else if (isForceClorePeriode()) {
            revenuActiviteLucrativeDependante = (PegasusServiceLocator.getDroitService()
                    .createAndCloseRevenuActiviteLucrativeDependante(getDroit(), revenuActiviteLucrativeDependante,
                            true));
        } else {
            revenuActiviteLucrativeDependante = (PegasusServiceLocator.getDroitService()
                    .updateRevenuActiviteLucrativeDependante(getDroit(), getInstanceDroitMembreFamille(),
                            revenuActiviteLucrativeDependante));
        }

        simpleTypeFraisObtentionRevenuSearch
                .setForIdRevenuActiviteLucrativeDependante(revenuActiviteLucrativeDependante
                        .getSimpleRevenuActiviteLucrativeDependante().getIdRevenuActiviteLucrativeDependante());
        simpleTypeFraisObtentionRevenuSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        simpleTypeFraisObtentionRevenuSearch = PegasusServiceLocator.getDroitService()
                .searchSimpleTypeFraisObtentionRevenu(simpleTypeFraisObtentionRevenuSearch);

        // type frais
        // sauvegarde de la liste et de l'id
        String fraisObtention = simpleTypeFraisObtentionRevenu.getCsFraisObtentionRevenu();
        String id = simpleTypeFraisObtentionRevenuSearch.getForIdRevenuActiviteLucrativeDependante();

        // on fais un delete
        if (simpleTypeFraisObtentionRevenu != null) {
            // on decoupe en paquet de 8
            // simpleTypeFraisObtentionRevenu.getCsFraisObtentionRevenu(); -> on
            // ne le fait pas ici

            for (int i = 0; i < simpleTypeFraisObtentionRevenuSearch.getSearchResults().length; i++) {

                simpleTypeFraisObtentionRevenu = (SimpleTypeFraisObtentionRevenu) simpleTypeFraisObtentionRevenuSearch
                        .getSearchResults()[i];
                simpleTypeFraisObtentionRevenu = PegasusServiceLocator.getDroitService()
                        .deleteSimpleTypeFraisObtentionRevenu(getDroit(), simpleTypeFraisObtentionRevenu);
            }
        }

        // puis un create
        if (simpleTypeFraisObtentionRevenu != null) {
            // on decoupe en paquet de 8
            // simpleTypeFraisObtentionRevenu.getCsFraisObtentionRevenu(); -> on
            // ne le fait pas ici
            if (fraisObtention != null) {

                for (int i = 0; i < (fraisObtention.length() / 8); i++) {
                    simpleTypeFraisObtentionRevenu.setCsFraisObtentionRevenu(fraisObtention.substring(i * 8,
                            (i + 1) * 8));

                    simpleTypeFraisObtentionRevenu = PegasusServiceLocator.getDroitService()
                            .createSimpleTypeFraisObtentionRevenu(id, simpleTypeFraisObtentionRevenu);
                }
            }
        }

        /*
         * if (this.simpleTypeFraisObtentionRevenu != null) {
         * 
         * String fraisObtention = this.simpleTypeFraisObtentionRevenu .getCsFraisObtentionRevenu();
         * 
         * for (int i = 0; i < this.simpleTypeFraisObtentionRevenuSearch .getSearchResults().length; i++) {
         * 
         * this.simpleTypeFraisObtentionRevenu = (SimpleTypeFraisObtentionRevenu)
         * this.simpleTypeFraisObtentionRevenuSearch .getSearchResults()[i];
         * 
         * this.simpleTypeFraisObtentionRevenu .setCsFraisObtentionRevenu(fraisObtention.substring( i * 8, (i + 1) *
         * 8));
         * 
         * // this.simpleTypeFraisObtentionRevenu.setCsFraisObtentionRevenu();
         * 
         * this.simpleTypeFraisObtentionRevenu = PegasusServiceLocator .getDroitService()
         * .updateSimpleTypeFraisObtentionRevenu(this.getDroit(), this.getInstanceDroitMembreFamille(),
         * this.simpleTypeFraisObtentionRevenu); } }
         */

        this.updateListe();
    }

    private void updateListe() throws Exception {
        this.updateListe(revenuActiviteLucrativeDependante.getSimpleDonneeFinanciereHeader().getIdDroitMembreFamille());
    }

    private void updateListe(String idDroitMembreFamille) throws Exception {

        if (getListe) {
            listeRevenuActiviteLucrativeDependante = new PCRevenuActiviteLucrativeDependanteAjaxListViewBean();
            RevenuActiviteLucrativeDependanteSearch search = listeRevenuActiviteLucrativeDependante.getSearchModel();

            search.setIdDroitMembreFamille(idDroitMembreFamille);
            search.setForNumeroVersion(getNoVersion());
            search.setWhereKey("forVersionedRALD");
            listeRevenuActiviteLucrativeDependante.find();
        }

    }

}
