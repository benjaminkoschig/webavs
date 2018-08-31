/**
 *
 */
package ch.globaz.vulpecula.business.models.employeur;

import globaz.jade.persistence.model.JadeSimpleModel;

public class EmployeurSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = 4707206378185394967L;

    private String idEmployeur;
    private String typeFacturation;
    private boolean bvr;

    public EmployeurSimpleModel() {
        super();
    }

    @Override
    public String getId() {
        return idEmployeur;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    @Override
    public void setId(String id) {
        idEmployeur = id;
    }

    public void setIdEmployeur(String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public boolean getBvr() {
        return bvr;
    }

    public void setBvr(boolean bvr) {
        this.bvr = bvr;
    }

    public String getTypeFacturation() {
        return typeFacturation;
    }

    public void setTypeFacturation(String typeFacturation) {
        this.typeFacturation = typeFacturation;
    }
}
