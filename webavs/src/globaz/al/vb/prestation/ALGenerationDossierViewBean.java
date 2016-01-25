package globaz.al.vb.prestation;

import globaz.globall.db.BSession;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeCodesSystemsUtil;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.al.business.constantes.ALCSAffilie;
import ch.globaz.al.business.constantes.ALCSDossier;
import ch.globaz.al.business.constantes.ALCSPrestation;
import ch.globaz.al.business.constantes.enumerations.generation.prestations.Bonification;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.al.business.models.droit.DroitComplexModel;
import ch.globaz.al.business.models.periodeAF.PeriodeAFModel;
import ch.globaz.al.business.models.prestation.EntetePrestationModel;
import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseSearchModel;
import ch.globaz.al.business.models.processus.ConfigProcessusModel;
import ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel;
import ch.globaz.al.business.services.ALServiceLocator;

/**
 * 
 * ViewBean gérant la génération de prestations depuis un dossier
 * 
 * @author GMO
 * 
 */
public class ALGenerationDossierViewBean extends BJadePersistentObjectViewBean {
    /**
     * bonification du dossier pour lequel on veut générer
     */
    private String bonification = null;
    /**
     * Le modèle du dossier pour lequel on génère
     */
    private DossierComplexModel dossierComplexModel = null;

    /**
     * Droit. Utilisé dans le cas de génération pour un seul droit
     */
    private DroitComplexModel droitComplexModel;

    /**
     * l'entête de prestation qui sera généré ou compléter sert uniquement à contenir les données
     */
    private EntetePrestationModel entetePrestationModel = null;

    /**
     * Nombre de prestations à 0.- à compléter dans la récap
     */
    private int nbPrestationsASaisir = 0;
    /**
     * Le numéro de facture de la récap dans laquelle mettre la prestation
     */
    private String noFacture = null;

    private String numProcessus = "0";

    private int paramWarnRetroBefore = 0;

    /**
     * La période de traitement sur laquelle on génère la prestation
     */
    private String periodeTraitement = null;
    /**
     * périodicite de l'affilié lié au dossier pour lequel on veut générer
     */
    private String periodicite = null;

    /**
     * Liste des processus sélectionnable
     */
    private List<ProcessusPeriodiqueModel> processusSelectableList = new ArrayList<ProcessusPeriodiqueModel>();

    private RecapitulatifEntrepriseSearchModel searchRecapsExistantesAffilie = new RecapitulatifEntrepriseSearchModel();

    /**
     * Constructeur du viewBean
     */
    public ALGenerationDossierViewBean() {
        super();
        dossierComplexModel = new DossierComplexModel();
        entetePrestationModel = new EntetePrestationModel();
        setDroitComplexModel(new DroitComplexModel());

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {

        String periodeDebutTraitement = periodeTraitement;
        // dans le cas des dossiers indirects, périodes récaps varient selon
        // périodicité
        if (ALCSPrestation.BONI_INDIRECT.equals(bonification)) {
            if (ALCSAffilie.PERIODICITE_TRI.equals(periodicite)) {
                periodeDebutTraitement = ALServiceLocator.getPeriodeAFBusinessService().getPeriodeDebutTrimestre(
                        periodeTraitement);
            }
            if (ALCSAffilie.PERIODICITE_ANN.equals(periodicite)) {
                periodeDebutTraitement = "01".concat(periodeTraitement.substring(2));
            }
        }

        ALServiceLocator
                .getGenerationDossierService()
                .generationDossier(
                        dossierComplexModel,
                        droitComplexModel.getDroitModel().getId(),
                        entetePrestationModel.getPeriodeDe(),
                        entetePrestationModel.getPeriodeA(),
                        periodeDebutTraitement,
                        periodeTraitement,
                        entetePrestationModel.getMontantTotal(),
                        ALCSPrestation.BONI_RESTITUTION.equals(entetePrestationModel.getBonification()) ? Bonification.RESTITUTION
                                : Bonification.AUTO, entetePrestationModel.getNombreUnite(), noFacture, numProcessus);

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

    /**
     * 
     * @return bonification
     */
    public String getBonification() {
        return bonification;
    }

    public String getDescriptionProcessusSelectable(int idx) {

        ProcessusPeriodiqueModel processus = processusSelectableList.get(idx);
        PeriodeAFModel period;
        try {
            period = ALServiceLocator.getPeriodeAFModelService().read(processus.getIdPeriode());

            ConfigProcessusModel config = ALServiceLocator.getConfigProcessusModelService().read(
                    processus.getIdConfig());
            return processus.getIdProcessusPeriodique() + "-" + period.getDatePeriode() + "-"
                    + JadeCodesSystemsUtil.getCodeLibelle(config.getBusinessProcessus());
        } catch (Exception e) {
            return "Unable to get processus";
        }

    }

    /**
     * @return dossierComplexModel
     */
    public DossierComplexModel getDossierComplexModel() {
        return dossierComplexModel;
    }

    public DroitComplexModel getDroitComplexModel() {
        return droitComplexModel;
    }

    /**
     * @return entetePrestationModel
     */
    public EntetePrestationModel getEntetePrestationModel() {
        return entetePrestationModel;
    }

    public String getFormattedParamWarnRetroBefore() {
        String paramStr = Integer.toString(paramWarnRetroBefore);
        return paramStr.length() > 4 ? paramStr.substring(4, 6) + "." + paramStr.substring(0, 4) : paramStr.substring(
                0, 4);
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return dossierComplexModel.getId();
    }

    /**
     * @return nbPrestationsASaisir
     */
    public int getNbPrestationsASaisir() {
        return nbPrestationsASaisir;
    }

    /**
     * @return noFacture
     */
    public String getNoFacture() {
        return noFacture;
    }

    public String getNumProcessus() {
        return numProcessus;
    }

    public int getParamWarnRetroBefore() {
        return paramWarnRetroBefore;
    }

    /**
     * Retourne la période de traitement
     * 
     * @return periodeTraitement
     */
    public String getPeriodeTraitement() {
        return periodeTraitement;
    }

    /**
     * @return periodicite
     */
    public String getPeriodicite() {
        return periodicite;
    }

    /**
     * 
     * @return processusSelectableList
     */
    public List<ProcessusPeriodiqueModel> getProcessusSelectableList() {
        return processusSelectableList;
    }

    public RecapitulatifEntrepriseSearchModel getSearchRecapsExistantesAffilie() {
        return searchRecapsExistantesAffilie;
    }

    /**
     * @return session actuelle
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return (dossierComplexModel != null) && !dossierComplexModel.isNew() ? new BSpy(dossierComplexModel.getSpy())
                : new BSpy(getSession());

    }

    /***
     * Définit si l'unité de calcul du dossier correspond à {@link ALCSDossier#UNITE_CALCUL_HEURE}
     * 
     * @return
     */
    public boolean isUniteHeure() {
        return ALCSDossier.UNITE_CALCUL_HEURE.equals(getDossierComplexModel().getDossierModel().getUniteCalcul());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        throw new Exception(this.getClass() + " - Method called (retrieve) not implemented (might be never called)");
    }

    /**
     * @param bonif
     *            la bonification du dossier à générer
     */
    public void setBonification(String bonif) {
        bonification = bonif;

    }

    /**
     * @param dossierComplexModel
     *            le modèle dossier
     */
    public void setDossierComplexModel(DossierComplexModel dossierComplexModel) {
        this.dossierComplexModel = dossierComplexModel;
    }

    public void setDroitComplexModel(DroitComplexModel droitComplexModel) {
        this.droitComplexModel = droitComplexModel;
    }

    /**
     * @param entetePrestationModel
     *            le modèle prestation
     */
    public void setEntetePrestationModel(EntetePrestationModel entetePrestationModel) {
        this.entetePrestationModel = entetePrestationModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        dossierComplexModel.setId(newId);

    }

    /**
     * @param nbPrestationsASaisir
     *            le nombre de prestations restants à saisir ( prestations à 0 dans une récap)
     */
    public void setNbPrestationsASaisir(int nbPrestationsASaisir) {
        this.nbPrestationsASaisir = nbPrestationsASaisir;
    }

    /**
     * @param noFacture
     *            le numéro de facture de la récap de la prestaion générée
     */
    public void setNoFacture(String noFacture) {
        this.noFacture = noFacture;
    }

    public void setNumProcessus(String numProcessus) {
        this.numProcessus = numProcessus;
    }

    public void setParamWarnRetroBefore(int paramWarnRetroBefore) {
        this.paramWarnRetroBefore = paramWarnRetroBefore;
    }

    /**
     * Définit la période de traitement
     * 
     * @param periodeTraitement
     *            la période de traitement actuel
     */
    public void setPeriodeTraitement(String periodeTraitement) {
        this.periodeTraitement = periodeTraitement;
    }

    /**
     * @param periodicite
     *            périodicité de l'affilié
     */
    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

    /**
     * 
     * @param processusSelectableList
     *            liste des processus sélectionnables pour attacher à la récap
     */
    public void setProcessusSelectableList(List<ProcessusPeriodiqueModel> processusSelectableList) {
        this.processusSelectableList = processusSelectableList;
    }

    public void setSearchRecapsExistantesAffilie(RecapitulatifEntrepriseSearchModel searchRecapsExistantesAffilie) {
        this.searchRecapsExistantesAffilie = searchRecapsExistantesAffilie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        throw new Exception(this.getClass() + " - Method called (update) not implemented (might be never called)");

    }

}
