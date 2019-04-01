package globaz.al.vb.prestation;

import ch.globaz.al.business.constantes.ALConstParametres;
import ch.globaz.al.business.constantes.ALConstRafam;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;
import ch.globaz.al.business.models.prestation.EntetePrestationListRecapComplexSearchModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel;
import ch.globaz.al.business.models.processus.ConfigProcessusModel;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel;
import ch.globaz.al.business.services.ALServiceLocator;
import ch.globaz.naos.business.model.AffiliationSearchSimpleModel;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.service.AFBusinessServiceLocator;
import ch.globaz.param.business.models.ParameterModel;
import ch.globaz.param.business.service.ParamServiceLocator;
import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.util.JANumberFormatter;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.properties.JadePropertiesService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ViewBean gérant le modèle représentant une récap. et ses prestations
 * 
 * @author GMO
 * 
 */
public class ALRecapViewBean extends BJadePersistentObjectViewBean {
    private AffiliationSimpleModel affilie = null;
    /**
     * indique si on doit mettre la récap en GED ou non
     */
    private boolean isPrintGed = false;

    /**
     * indique si la récap est éditable ou non
     */
    private boolean isRecapVerrouillee = false;

    /**
     * indique si le caractère au début du NSS pour les récaps est activé
     */
    private Boolean isCharNssRecap = false ;
    /**
     * Montant total de la récap
     */
    private String montantTotal = null;

    /**
     * Nombre de prestations à 0.- à compléter dans la récap
     */
    private int nbPrestationsASaisir = 0;

    /**
     * Modèle de recherche des entêtes de prestation lié à la récap
     */
    private EntetePrestationListRecapComplexSearchModel prestationSearchModel = null;
    /**
     * Processus lié à la récap
     */
    private ProcessusPeriodiqueModel processusPeriodiqueModel = null;
    private List<ProcessusPeriodiqueModel> processusSelectableList = new ArrayList<ProcessusPeriodiqueModel>();

    /**
     * Modèle de la récap
     */
    private RecapitulatifEntrepriseModel recapModel = null;

    /**
     * Constructeur du viewBean
     */
    public ALRecapViewBean() {
        super();
        prestationSearchModel = new EntetePrestationListRecapComplexSearchModel();
        recapModel = new RecapitulatifEntrepriseModel();
        setAffilie(new AffiliationSimpleModel());
        try {
            this.isCharNssRecap = JadePropertiesService.getInstance().getProperty(ALConstParametres.RECAP_FORMAT_NSS).equals("true");
        } catch (Exception e){
            this.isCharNssRecap = false;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        recapModel = ALServiceLocator.getRecapitulatifEntrepriseModelService().create(recapModel);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        recapModel = ALServiceLocator.getRecapitulatifEntrepriseModelService().delete(recapModel);

    }

    public AffiliationSimpleModel getAffilie() {
        return affilie;
    }

    public String getDescriptionProcessusLie() {

        ProcessusPeriodiqueModel processus = processusPeriodiqueModel;
        PeriodeAFModel period;

        // TODO: avoir service qui récupère le processus principal selon la période et bonif passé en param
        if (JadeNumericUtil.isEmptyOrZero(recapModel.getIdProcessusPeriodique())) {
            return recapModel.getPeriodeA() + "- Processus principal";
        } else {

            try {
                period = ALServiceLocator.getPeriodeAFModelService().read(processus.getIdPeriode());
                ConfigProcessusModel config = ALServiceLocator.getConfigProcessusModelService().read(
                        processus.getIdConfig());
                return period.getDatePeriode() + "-" + processus.getId() + "-"
                        + JadeCodesSystemsUtil.getCodeLibelle(config.getBusinessProcessus());
            } catch (Exception e) {
                return "Unable to get processus lie";
            }
        }

    }

    public String getDescriptionProcessusSelectable(int idx) {

        ProcessusPeriodiqueModel processus = processusSelectableList.get(idx);
        PeriodeAFModel period;
        try {
            period = ALServiceLocator.getPeriodeAFModelService().read(processus.getIdPeriode());
            ConfigProcessusModel config = ALServiceLocator.getConfigProcessusModelService().read(
                    processus.getIdConfig());
            return period.getDatePeriode() + "-" + processus.getId() + "-"
                    + JadeCodesSystemsUtil.getCodeLibelle(config.getBusinessProcessus());
        } catch (Exception e) {
            return "Unable to get processus";
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return recapModel.getId();
    }

    public String getMontantTotal() {
        return montantTotal;
    }

    public String getMontantTotalFormatte() {
        if (montantTotal != null) {
            return JANumberFormatter.fmt(montantTotal, true, true, true, 2);
        }
        return null;
    }

    /**
     * 
     * @return le nombre de prestations à saisir
     */
    public int getNbPrestationsASaisir() {
        return nbPrestationsASaisir;
    }

    public String getNomAffilie() {
        return getAffilie().getRaisonSociale();
    }

    /**
     * Retourne le modèle de recherche des prestations
     * 
     * @return prestationSearchModel
     */
    public EntetePrestationListRecapComplexSearchModel getPrestationSearchModel() {
        return prestationSearchModel;
    }

    public List<ProcessusPeriodiqueModel> getProcessusSelectableList() {
        return processusSelectableList;
    }

    /**
     * Retourne le modèle recap
     * 
     * @return recapModel
     */
    public RecapitulatifEntrepriseModel getRecapModel() {
        return recapModel;
    }

    /**
     * @return the session
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    public int getSizeProcessusSelectableList() {
        return processusSelectableList.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return (recapModel != null) && !recapModel.isNew() ? new BSpy(recapModel.getSpy()) : new BSpy(getSession());
    }

    public boolean isPrintGed() {
        return isPrintGed;
    }

    public boolean isRecapVerrouillee() {
        return isRecapVerrouillee;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        recapModel = ALServiceLocator.getRecapitulatifEntrepriseModelService().read(getId());
        isRecapVerrouillee = ALServiceLocator.getRecapitulatifEntrepriseBusinessService().isRecapVerouillee(recapModel);
        // Besoin pour savoir si on affiche le menu saisie H/J ou pas
        nbPrestationsASaisir = ALServiceLocator.getRecapitulatifEntrepriseBusinessService().getNbPrestationsASaisir(
                getId());

        montantTotal = ALServiceLocator.getRecapitulatifEntrepriseBusinessService().getTotalRecap(recapModel.getId());
        // on charge les processus dans lesquels on pourrait mettre la récap
        processusSelectableList = ALServiceLocator.getBusinessProcessusService().getUnlockProcessusPaiementForPeriode(
                recapModel.getBonification(), recapModel.getGenreAssurance());
        // TODO: avoir service qui récupère le processus principal selon la période et bonif passé en param
        if (!JadeNumericUtil.isEmptyOrZero(recapModel.getIdProcessusPeriodique())) {
            processusPeriodiqueModel = ALServiceLocator.getProcessusPeriodiqueModelService().read(
                    recapModel.getIdProcessusPeriodique());
        }

        AffiliationSearchSimpleModel affiliationSearchModel = new AffiliationSearchSimpleModel();
        affiliationSearchModel.setForNumeroAffilie(recapModel.getNumeroAffilie());
        affiliationSearchModel = AFBusinessServiceLocator.getAffiliationService().find(affiliationSearchModel);
        if (affiliationSearchModel.getSize() > 0) {
            setAffilie((AffiliationSimpleModel) affiliationSearchModel.getSearchResults()[0]);
        }
    }

    public void setAffilie(AffiliationSimpleModel affilie) {
        this.affilie = affilie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        recapModel.setId(newId);

    }

    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

    /**
     * 
     * @param nbPrestationsASaisir
     *            le nombre de prestations à saisir
     */
    public void setNbPrestationsASaisir(int nbPrestationsASaisir) {
        this.nbPrestationsASaisir = nbPrestationsASaisir;
    }

    /**
     * @param prestationSearchModel
     *            le modèle de recherche des entêtes prestations liées à la récap
     */
    public void setPrestationSearchModel(EntetePrestationListRecapComplexSearchModel prestationSearchModel) {
        this.prestationSearchModel = prestationSearchModel;
    }

    public void setPrintGed(boolean isPrintGed) {
        this.isPrintGed = isPrintGed;
    }

    public void setProcessusSelectableList(List<ProcessusPeriodiqueModel> processusSelectableList) {
        this.processusSelectableList = processusSelectableList;
    }

    /**
     * @param recapModel
     *            le modèle représentant la récap
     */
    public void setRecapModel(RecapitulatifEntrepriseModel recapModel) {
        this.recapModel = recapModel;
    }

    public void setRecapVerrouillee(boolean isRecapVerrouillee) {
        this.isRecapVerrouillee = isRecapVerrouillee;
    }
        

    public Boolean getIsCharNssRecap() {
        return this.isCharNssRecap;
    }

    public void setCharNssRecap(Boolean charNssRecap) {
        this.isCharNssRecap = charNssRecap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        recapModel = ALServiceLocator.getRecapitulatifEntrepriseModelService().update(recapModel);

    }
}
