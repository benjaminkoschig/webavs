package globaz.al.vb.traitement;

import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import ch.globaz.al.business.models.processus.TemplateTraitementListComplexSearchModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * ViewBean g�rant les processus / traitements
 * 
 * @author GMO
 * 
 */
public class ALProcessusGestionViewBean extends BJadePersistentObjectViewBean {
    /**
     * Identifiant du processus p�riodique
     */
    private String idProcessusPeriodique = null;
    /**
     * identifiant du traitement � ex�cuter
     */
    private String idTraitementToExecute = null;
    /**
     * P�riode � ouvrir
     */
    private String newPeriode = null;

    /**
     * Mod�le de recherche des processus / traitements li�s au template
     */
    private TemplateTraitementListComplexSearchModel searchModel = null;

    /**
     * ann�e � afficher dans l'�cran
     */
    private String yearDisplay = null;

    /**
     * Constructeur du viewBean
     */
    public ALProcessusGestionViewBean() {
        super();
        setSearchModel(new TemplateTraitementListComplexSearchModel());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        throw new Exception(this.getClass() + " - Method called (add) not implemented (might be never called)");

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        throw new Exception(this.getClass() + " - Method called (delete) not implemented (might be never called)");

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return idTraitementToExecute;
    }

    public String getIdProcessusPeriodique() {
        return idProcessusPeriodique;
    }

    /**
     * @return newPeriode
     */
    public String getNewPeriode() {
        return newPeriode;
    }

    /**
     * @return searchModel
     */
    public TemplateTraitementListComplexSearchModel getSearchModel() {
        return searchModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return null;
    }

    /**
     * @return yearDisplay
     */
    public String getYearDisplay() {
        return yearDisplay;
    }

    @Override
    public void retrieve() throws Exception {
        // this.searchModel.setForTemplate(ALServiceLocator.getBusinessProcessusService().getAppliedTemplate());
        searchModel.setForDateDebut("01.".concat(yearDisplay));
        searchModel.setForDateFin("12.".concat(yearDisplay));
        searchModel.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        searchModel = ALServiceLocator.getTemplateTraitementListComplexModelService().search(searchModel);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        idTraitementToExecute = newId;

    }

    public void setIdProcessusPeriodique(String idProcessusPeriodique) {
        this.idProcessusPeriodique = idProcessusPeriodique;
    }

    /**
     * @param newPeriode
     */
    public void setNewPeriode(String newPeriode) {
        this.newPeriode = newPeriode;
    }

    /**
     * @param searchModel
     *            le mod�le de recherche
     */
    public void setSearchModel(TemplateTraitementListComplexSearchModel searchModel) {
        this.searchModel = searchModel;
    }

    /**
     * @param yearDisplay
     *            l'ann�e � afficher
     */
    public void setYearDisplay(String yearDisplay) {
        this.yearDisplay = yearDisplay;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {

        ALServiceLocator.getBusinessProcessusService().initAllBusinessProcessusForPeriode(
                ALServiceLocator.getBusinessProcessusService().getAppliedTemplate(newPeriode), newPeriode);

    }

}
