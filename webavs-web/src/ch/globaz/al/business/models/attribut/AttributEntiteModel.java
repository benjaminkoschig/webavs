package ch.globaz.al.business.models.attribut;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Classe mod�le des attributs selon la table des attributs li�s aux affili�s notamment
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
     * la cl� (id) de l'entit� qui b�n�ficie de l'attribut
     */
    private String cleEntite = null;
    /**
     * cl� alphanum�rique de l'entit� b�n�ficiant de l'attribut
     */
    private String cleEntiteAlpha = null;
    /**
     * l'id de l'attribut entit� (cl� primaire)
     */
    private String idAttributEntite = null;
    /**
     * le nom de l'attribut
     */
    private String nomAttribut = null;
    /**
     * Le type d'entit� sur lequel porte l'attribut
     */
    private String typeEntite = null;
    /**
     * la valeur alphanum�rique de l'attribut
     */
    private String valeurAlpha = null;
    /**
     * la valeur num�rique de l'attribut
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
     *            l'id de l'entit� sur laquelle porte l'attribut
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
     *            nom de l'attribut : ex.: destination avis d'�ch�ance
     */
    public void setNomAttribut(String nomAttribut) {
        this.nomAttribut = nomAttribut;
    }

    /**
     * @param typeEntite
     *            : type de l'entit�: les affiliations etc..
     */
    public void setTypeEntite(String typeEntite) {
        this.typeEntite = typeEntite;
    }

    /**
     * @param valeurAlpha
     *            valeur alfa ou num�rique de l'attribut
     */
    public void setValeurAlpha(String valeurAlpha) {
        this.valeurAlpha = valeurAlpha;
    }

    /**
     * @param valeurNum
     *            valeur num�rique de l'attribut
     */
    public void setValeurNum(String valeurNum) {
        this.valeurNum = valeurNum;
    }

}
