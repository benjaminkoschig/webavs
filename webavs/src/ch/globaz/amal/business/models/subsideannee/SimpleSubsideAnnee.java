package ch.globaz.amal.business.models.subsideannee;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleSubsideAnnee extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeSubside = null;
    private String idSubsideAnnee = null;
    private String limiteRevenu = null;
    private String subsideAdo = null;
    private String subsideAdulte = null;
    private String subsideEnfant = null;
    private String subsideSalarie1618 = null;
    private String subsideSalarie1925 = null;

    public String getAnneeSubside() {
        return anneeSubside;
    }

    @Override
    public String getId() {
        return idSubsideAnnee;
    }

    public String getIdSubsideAnnee() {
        return idSubsideAnnee;
    }

    public String getLimiteRevenu() {
        return limiteRevenu;
    }

    public String getSubsideAdo() {
        return subsideAdo;
    }

    public String getSubsideAdulte() {
        return subsideAdulte;
    }

    public String getSubsideEnfant() {
        return subsideEnfant;
    }

    public String getSubsideSalarie1618() {
        return subsideSalarie1618;
    }

    public String getSubsideSalarie1925() {
        return subsideSalarie1925;
    }

    public void setAnneeSubside(String anneeSubside) {
        this.anneeSubside = anneeSubside;
    }

    @Override
    public void setId(String id) {
        idSubsideAnnee = id;
    }

    public void setIdSubsideAnnee(String idSubsideAnnee) {
        this.idSubsideAnnee = idSubsideAnnee;
    }

    public void setLimiteRevenu(String limiteRevenu) {
        this.limiteRevenu = limiteRevenu;
    }

    public void setSubsideAdo(String subsideAdo) {
        this.subsideAdo = subsideAdo;
    }

    public void setSubsideAdulte(String subsideAdulte) {
        this.subsideAdulte = subsideAdulte;
    }

    public void setSubsideEnfant(String subsideEnfant) {
        this.subsideEnfant = subsideEnfant;
    }

    public void setSubsideSalarie1618(String subsideSalarie1618) {
        this.subsideSalarie1618 = subsideSalarie1618;
    }

    public void setSubsideSalarie1925(String subsideSalarie1925) {
        this.subsideSalarie1925 = subsideSalarie1925;
    }

}
