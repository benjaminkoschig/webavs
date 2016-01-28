package ch.globaz.vulpecula.business.models.taxationoffice;

import globaz.jade.persistence.model.JadeSimpleModel;

public class TaxationOfficeSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = 1L;

    private String id;
    private String idDecompte;
    private String idPassageFacturation;
    private String etat;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdDecompte() {
        return idDecompte;
    }

    public void setIdDecompte(String idDecompte) {
        this.idDecompte = idDecompte;
    }

    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    public void setIdPassageFacturation(String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}
