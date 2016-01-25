package ch.globaz.al.business.models.attribut;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Mod�le de recherche pour les attributs li�s � une entit� pr�cise
 * 
 * @author GMO
 * 
 */
public class AttributEntiteSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Crit�re id de l'entit� des attributs cherch�s
     */
    private String forCleEntite = null;
    /**
     * Crit�re id alphanum�rique de l'entit� des attributs cherch�s
     */
    private String forCleEntiteAlpha = null;
    /**
     * Crit�re nom de l'attribut cherch�
     */
    private String forNomAttribut = null;
    /**
     * Crit�re type de l'entit� des attributs cherch�s
     */
    private String forTypeEntite = null;

    /**
     * @return forCleEntite
     */
    public String getForCleEntite() {
        return forCleEntite;
    }

    public String getForCleEntiteAlpha() {
        return forCleEntiteAlpha;
    }

    /**
     * @return forNomAttribut
     */
    public String getForNomAttribut() {
        return forNomAttribut;
    }

    /**
     * @return forTypeEntite
     */
    public String getForTypeEntite() {
        return forTypeEntite;
    }

    /**
     * @param forCleEntite
     *            le crit�re de recherche cl� entit�
     */
    public void setForCleEntite(String forCleEntite) {
        this.forCleEntite = forCleEntite;
    }

    public void setForCleEntiteAlpha(String forCleEntiteAlpha) {
        this.forCleEntiteAlpha = forCleEntiteAlpha;
    }

    /**
     * @param forNomAttribut
     *            le crit�re de recherche nom de l'attribut
     */
    public void setForNomAttribut(String forNomAttribut) {
        this.forNomAttribut = forNomAttribut;
    }

    /**
     * @param forTypeEntite
     *            le crit�re de recherche type de l'entit�
     */
    public void setForTypeEntite(String forTypeEntite) {
        this.forTypeEntite = forTypeEntite;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<AttributEntiteModel> whichModelClass() {
        return AttributEntiteModel.class;
    }

}
