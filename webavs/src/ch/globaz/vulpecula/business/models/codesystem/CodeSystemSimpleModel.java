package ch.globaz.vulpecula.business.models.codesystem;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author JPA
 * 
 */
public class CodeSystemSimpleModel extends JadeSimpleModel {
    private String groupe;
    private String idCodeSystem;
    private String libelle;

    public CodeSystemSimpleModel() {
        super();
    }

    public String getGroupe() {
        return groupe;
    }

    @Override
    public String getId() {
        return idCodeSystem;
    }

    public String getIdCodeSystem() {
        return idCodeSystem;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setGroupe(String groupe) {
        this.groupe = groupe;
    }

    @Override
    public void setId(String id) {
        idCodeSystem = id;
    }

    public void setIdCodeSystem(String idCodeSystem) {
        this.idCodeSystem = idCodeSystem;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
