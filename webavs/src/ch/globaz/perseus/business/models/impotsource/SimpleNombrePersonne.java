package ch.globaz.perseus.business.models.impotsource;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleNombrePersonne extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeDebut = null;
    private String anneeFin = null;
    private String idNombrePersonne = null;
    private String nombrePersonne = null;
    private String nomCategorieNombrePersonne = null;

    public String getAnneeDebut() {
        return anneeDebut;
    }

    public String getAnneeFin() {
        return anneeFin;
    }

    @Override
    public String getId() {
        return idNombrePersonne;
    }

    public String getIdNombrePersonne() {
        return idNombrePersonne;
    }

    public String getNombrePersonne() {
        return nombrePersonne;
    }

    public String getNomCategorieNombrePersonne() {
        return nomCategorieNombrePersonne;
    }

    public void setAnneeDebut(String anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    public void setAnneeFin(String anneeFin) {
        this.anneeFin = anneeFin;
    }

    @Override
    public void setId(String id) {
        idNombrePersonne = id;

    }

    public void setIdNombrePersonne(String idNombrePersonne) {
        this.idNombrePersonne = idNombrePersonne;
    }

    public void setNombrePersonne(String nombrePersonne) {
        this.nombrePersonne = nombrePersonne;
    }

    public void setNomCategorieNombrePersonne(String nomCategorieNombrePersonne) {
        this.nomCategorieNombrePersonne = nomCategorieNombrePersonne;
    }

}
