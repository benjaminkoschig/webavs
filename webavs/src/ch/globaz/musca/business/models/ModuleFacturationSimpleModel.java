package ch.globaz.musca.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle représentant un module de facturation
 * 
 * @author JPA
 * 
 */
public class ModuleFacturationSimpleModel extends JadeSimpleModel {
    private String idModFac = "";
    private String idTypeModule = "";
    private String typeFacturation = "";

    public String getTypeFacturation() {
        return typeFacturation;
    }

    public void setTypeFacturation(String typeFacturation) {
        this.typeFacturation = typeFacturation;
    }

    public String getIdTypeModule() {
        return idTypeModule;
    }

    public void setIdTypeModule(String idTypeModule) {
        this.idTypeModule = idTypeModule;
    }

    public String getIdModFac() {
        return idModFac;
    }

    public void setIdModFac(String idModFac) {
        this.idModFac = idModFac;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {
    }
}
