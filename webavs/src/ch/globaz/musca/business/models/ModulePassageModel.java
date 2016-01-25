package ch.globaz.musca.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle de données pour le lien entre un module de facturation et un passage de facturation.
 * Sert uniquement à afficher les données via le modèle complexe <code>PassageModuleComplexModel</code>
 * 
 */
public class ModulePassageModel extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * id du passage de facturation
     */
    private String idPassage = null;
    /**
     * code système représentant l'action exécutée sur le module dans le passage
     */
    private String idAction = null;
    /**
     * id du module de facturation
     */
    private String idModuleFacturation = null;
    /**
     * id du plan de facturation à partir duquel le passage a été créé
     */
    private String idPlan = null;

    @Override
    public String getId() {
        return idPassage;
    }

    @Override
    public void setId(String id) {
        idPassage = id;

    }

    public String getIdAction() {
        return idAction;
    }

    public void setIdAction(String idAction) {
        this.idAction = idAction;
    }

    public String getIdModuleFacturation() {
        return idModuleFacturation;
    }

    public void setIdModuleFacturation(String idModuleFacturation) {
        this.idModuleFacturation = idModuleFacturation;
    }

    public String getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(String idPlan) {
        this.idPlan = idPlan;
    }

}
