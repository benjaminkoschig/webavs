package ch.globaz.al.business.models.attribut;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Classe modèle des attributs selon la table des attributs liés aux affiliés notamment
 * 
 * @author GMO/PTA
 * 
 */
public class AttributEntiteModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * la clé (id) de l'entité qui bénéficie de l'attribut
     */
    private String cleEntite = null;
    /**
     * clé alphanumérique de l'entité bénéficiant de l'attribut
     */
    private String cleEntiteAlpha = null;
    /**
     * l'id de l'attribut entité (clé primaire)
     */
    private String idAttributEntite = null;
    /**
     * le nom de l'attribut
     */
    private String nomAttribut = null;
    /**
     * Le type d'entité sur lequel porte l'attribut
     */
    private String typeEntite = null;
    /**
     * la valeur alphanumérique de l'attribut
     */
    private String valeurAlpha = null;
    /**
     * la valeur numérique de l'attribut
     */
    private String valeurNum = null;

    /**
     * @return cleEntite
     */
    public String getCleEntite() {
        return cleEntite;
    }

    public String getCleEntiteAlpha() {
        return cleEntiteAlpha;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idAttributEntite;
    }

    /**
     * @return idAttributEntite
     */
    public String getIdAttributEntite() {
        return idAttributEntite;
    }

    /**
     * @return nomAttribut
     */
    public String getNomAttribut() {
        return nomAttribut;
    }

    /**
     * @return typeEntite
     */
    public String getTypeEntite() {
        return typeEntite;
    }

    /**
     * @return valeurAlpha
     */
    public String getValeurAlpha() {
        return valeurAlpha;
    }

    /**
     * @return valeurNum
     */
    public String getValeurNum() {
        return valeurNum;
    }

    /**
     * @param cleEntite
     *            l'id de l'entité sur laquelle porte l'attribut
     */
    public void setCleEntite(String cleEntite) {
        this.cleEntite = cleEntite;
    }

    public void setCleEntiteAlpha(String cleEntiteAlpha) {
        this.cleEntiteAlpha = cleEntiteAlpha;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idAttributEntite = id;

    }

    /**
     * @param idAttributEntite
     *            identifiant
     */
    public void setIdAttributEntite(String idAttributEntite) {
        this.idAttributEntite = idAttributEntite;
    }

    /**
     * @param nomAttribut
     *            nom de l'attribut : ex.: destination avis d'échéance
     */
    public void setNomAttribut(String nomAttribut) {
        this.nomAttribut = nomAttribut;
    }

    /**
     * @param typeEntite
     *            : type de l'entité: les affiliations etc..
     */
    public void setTypeEntite(String typeEntite) {
        this.typeEntite = typeEntite;
    }

    /**
     * @param valeurAlpha
     *            valeur alfa ou numérique de l'attribut
     */
    public void setValeurAlpha(String valeurAlpha) {
        this.valeurAlpha = valeurAlpha;
    }

    /**
     * @param valeurNum
     *            valeur numérique de l'attribut
     */
    public void setValeurNum(String valeurNum) {
        this.valeurNum = valeurNum;
    }

}
