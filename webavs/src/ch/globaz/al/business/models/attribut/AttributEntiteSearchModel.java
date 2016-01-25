package ch.globaz.al.business.models.attribut;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modèle de recherche pour les attributs liés à une entité précise
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
     * Critère id de l'entité des attributs cherchés
     */
    private String forCleEntite = null;
    /**
     * Critère id alphanumérique de l'entité des attributs cherchés
     */
    private String forCleEntiteAlpha = null;
    /**
     * Critère nom de l'attribut cherché
     */
    private String forNomAttribut = null;
    /**
     * Critère type de l'entité des attributs cherchés
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
     *            le critère de recherche clé entité
     */
    public void setForCleEntite(String forCleEntite) {
        this.forCleEntite = forCleEntite;
    }

    public void setForCleEntiteAlpha(String forCleEntiteAlpha) {
        this.forCleEntiteAlpha = forCleEntiteAlpha;
    }

    /**
     * @param forNomAttribut
     *            le critère de recherche nom de l'attribut
     */
    public void setForNomAttribut(String forNomAttribut) {
        this.forNomAttribut = forNomAttribut;
    }

    /**
     * @param forTypeEntite
     *            le critère de recherche type de l'entité
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
