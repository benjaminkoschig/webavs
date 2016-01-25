package ch.globaz.musca.business.models;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.musca.business.services.models.passage.PassageModuleComplexModelService;

/**
 * 
 * Modèle complexe présentant les données d'un passage de facturation avec module de type AF.
 * Sert uniquement à afficher les données ( pas utilisé via service create,update ou delete)
 * 
 * @see PassageModuleComplexModelService
 * 
 */
public class PassageModuleComplexModel extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * id du passage facturation
     */
    private String idPassage = null;
    /**
     * libellé du passage facturation
     */
    private String libellePassage = null;
    /**
     * type de facturation du passage
     */
    private String typeFacturation = null;
    /**
     * date de la période du passage
     */
    private String datePeriode = null;
    /**
     * date de facturation du passage
     */
    private String dateFacturation = null;
    /**
     * status du passage
     */
    private String status = null;
    /**
     * id du module de facturation lié au passage
     */
    private String idModuleFacturation = null;
    /**
     * (cs) type du module de facturation lié au passage
     */
    private String idTypeModule = null;
    /**
     * (cs) action actuelle du module
     */
    private String idAction = null;
    /**
     * id du plan lié au passage
     */
    private String idPlan = null;

    @Override
    public String getId() {
        return idPassage;
    }

    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIdPassage() {
        return idPassage;
    }

    public void setIdPassage(String idPassage) {
        this.idPassage = idPassage;
    }

    public String getLibellePassage() {
        return libellePassage;
    }

    public void setLibellePassage(String libellePassage) {
        this.libellePassage = libellePassage;
    }

    public String getTypeFacturation() {
        return typeFacturation;
    }

    public void setTypeFacturation(String typeFacturation) {
        this.typeFacturation = typeFacturation;
    }

    public String getDatePeriode() {
        return datePeriode;
    }

    public void setDatePeriode(String datePeriode) {
        this.datePeriode = datePeriode;
    }

    public String getDateFacturation() {
        return dateFacturation;
    }

    public void setDateFacturation(String dateFacturation) {
        this.dateFacturation = dateFacturation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    public void setIdModuleFacturation(String idModuleFacturation) {
        this.idModuleFacturation = idModuleFacturation;
    }

    public String getIdTypeModule() {
        return idTypeModule;
    }

    public void setIdTypeModule(String idTypeModule) {
        this.idTypeModule = idTypeModule;
    }

    public String getIdAction() {
        return idAction;
    }

    public void setIdAction(String idAction) {
        this.idAction = idAction;
    }

    public String getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(String idPlan) {
        this.idPlan = idPlan;
    }

    @Override
    public void setId(String id) {
        idPassage = id;

    }

    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }

}
