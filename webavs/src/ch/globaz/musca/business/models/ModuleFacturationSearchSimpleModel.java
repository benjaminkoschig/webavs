package ch.globaz.musca.business.models;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Mod�le de recherche sur les mod�les simples des modules de facturation
 * 
 * @author JPA
 * 
 */
public class ModuleFacturationSearchSimpleModel extends JadeSearchSimpleModel {
    private String forIdModFac;

    public String getForIdModFac() {
        return forIdModFac;
    }

    public void setForIdModFac(String forIdModFac) {
        this.forIdModFac = forIdModFac;
    }

    @Override
    public Class<ModuleFacturationSimpleModel> whichModelClass() {
        return ModuleFacturationSimpleModel.class;
    }

}
