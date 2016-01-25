package ch.globaz.vulpecula.business.models.codesystem;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author JPA
 * 
 */
public class CodeSystemLibelleSimpleModel extends JadeSimpleModel {
    private String idCodeSystem;

    private String langue;

    private String libelle;
    private String libelleCourt;

    public CodeSystemLibelleSimpleModel() {
        super();
    }

    @Override
    public String getId() {
        return idCodeSystem;
    }

    public String getIdCodeSystem() {
        return idCodeSystem;
    }

    public String getLangue() {
        return langue;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getLibelleCourt() {
        return libelleCourt;
    }

    @Override
    public void setId(String id) {
        idCodeSystem = id;
    }

    public void setIdCodeSystem(String idCodeSystem) {
        this.idCodeSystem = idCodeSystem;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setLibelleCourt(String libelleCourt) {
        this.libelleCourt = libelleCourt;
    }
}
