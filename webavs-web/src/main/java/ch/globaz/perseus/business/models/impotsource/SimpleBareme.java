package ch.globaz.perseus.business.models.impotsource;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleBareme extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeDebut = null;
    private String anneeFin = null;
    private String csTypeBareme = null;
    private String idBareme = null;
    private String nombrePersonne = null;
    private String nomCategorie = null;

    /**
     * @return the anneeDebut
     */
    public String getAnneeDebut() {
        return anneeDebut;
    }

    /**
     * @return the anneeFin
     */
    public String getAnneeFin() {
        return anneeFin;
    }

    public String getCsTypeBareme() {
        return csTypeBareme;
    }

    @Override
    public String getId() {
        return getIdBareme();
    }

    /**
     * @return the idBareme
     */
    public String getIdBareme() {
        return idBareme;
    }

    /**
     * @return the nombrePersonne
     */
    public String getNombrePersonne() {
        return nombrePersonne;
    }

    /**
     * @return the nomCategorie
     */
    public String getNomCategorie() {
        return nomCategorie;
    }

    /**
     * @param anneeDebut
     *            the anneeDebut to set
     */
    public void setAnneeDebut(String anneeDebut) {
        this.anneeDebut = anneeDebut;
    }

    /**
     * @param anneeFin
     *            the anneeFin to set
     */
    public void setAnneeFin(String anneeFin) {
        this.anneeFin = anneeFin;
    }

    /**
     * @param csTypeBareme
     *            the csTypeBareme to set
     */
    public void setCsTypeBareme(String csTypeBareme) {
        this.csTypeBareme = csTypeBareme;
    }

    @Override
    public void setId(String id) {
        setIdBareme(id);

    }

    /**
     * @param idBareme
     *            the idBareme to set
     */
    public void setIdBareme(String idBareme) {
        this.idBareme = idBareme;
    }

    /**
     * @param nombrePersonne
     *            the nombrePersonne to set
     */
    public void setNombrePersonne(String nombrePersonne) {
        this.nombrePersonne = nombrePersonne;
    }

    /**
     * @param nomCategorie
     *            the nomCategorie to set
     */
    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

}
