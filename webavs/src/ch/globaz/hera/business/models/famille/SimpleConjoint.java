package ch.globaz.hera.business.models.famille;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleConjoint extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idConjoint1 = "";
    private String idConjoint2 = "";
    private String idConjoints = "";

    @Override
    public String getId() {
        return idConjoints;
    }

    /**
     * getter pour l'attribut id conjoint1 (idMembreFamilleConjoint1)
     * 
     * @return la valeur courante de l'attribut id conjoint1
     */
    public String getIdConjoint1() {
        return idConjoint1;
    }

    /**
     * getter pour l'attribut id conjoint2 (idMembreFamilleConjoint2)
     * 
     * @return la valeur courante de l'attribut id conjoint2
     */
    public String getIdConjoint2() {
        return idConjoint2;
    }

    /**
     * getter pour l'attribut id conjoints
     * 
     * @return la valeur courante de l'attribut id conjoints
     */
    public String getIdConjoints() {
        return idConjoints;
    }

    @Override
    public void setId(String id) {
        idConjoints = id;
    }

    /**
     * setter pour l'attribut id conjoint1 (idMembreFamilleConjoint1)
     * 
     * @param idConjoint1
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdConjoint1(String idConjoint1) {
        this.idConjoint1 = idConjoint1;
    }

    /**
     * setter pour l'attribut id conjoint2 (idMembreFamilleConjoint1)
     * 
     * @param idConjoint2
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdConjoint2(String idConjoint2) {
        this.idConjoint2 = idConjoint2;
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

}
