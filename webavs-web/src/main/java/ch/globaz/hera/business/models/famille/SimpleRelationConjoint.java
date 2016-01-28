package ch.globaz.hera.business.models.famille;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleRelationConjoint extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateDebut = "";
    private String dateFin = "";
    private String idConjoints = "";
    private String idRelationConjoint = "";
    private String typeRelation = "";

    /**
     * getter pour l'attribut date debut
     * 
     * @return la valeur courante de l'attribut date debut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * getter pour l'attribut date fin
     * 
     * @return la valeur courante de l'attribut date fin
     */
    public String getDateFin() {
        return dateFin;
    }

    @Override
    public String getId() {
        return idRelationConjoint;
    }

    /**
     * getter pour l'attribut id conjoints
     * 
     * @return la valeur courante de l'attribut id conjoints
     */
    public String getIdConjoints() {
        return idConjoints;
    }

    /**
     * getter pour l'attribut id relation conjoint
     * 
     * @return la valeur courante de l'attribut id relation conjoint
     */
    public String getIdRelationConjoint() {
        return idRelationConjoint;
    }

    /**
     * getter pour l'attribut type relation
     * 
     * @return la valeur courante de l'attribut type relation
     */
    public String getTypeRelation() {
        return typeRelation;
    }

    /**
     * setter pour l'attribut date debut
     * 
     * @param dateDebut
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * setter pour l'attribut date fin
     * 
     * @param dateFin
     *            une nouvelle valeur pour cet attribut
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    @Override
    public void setId(String id) {
        idRelationConjoint = id;
    }

    /**
     * setter pour l'attribut id conjoints
     * 
     * @param idConjoints
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdConjoints(String idConjoints) {
        this.idConjoints = idConjoints;
    }

    /**
     * setter pour l'attribut id relation conjoint
     * 
     * @param idRelationConjoint
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdRelationConjoint(String idRelationConjoint) {
        this.idRelationConjoint = idRelationConjoint;
    }

    /**
     * setter pour l'attribut type relation
     * 
     * @param typeRelation
     *            une nouvelle valeur pour cet attribut
     */
    public void setTypeRelation(String typeRelation) {
        this.typeRelation = typeRelation;
    }

}
