package globaz.babel.api;

import globaz.globall.api.BIEntity;

/**
 * @see globaz.babel.api.ICTDocument
 * @author vre
 */
public interface ICTTexte extends BIEntity {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut code iso langue
     * 
     * @return la valeur courante de l'attribut code iso langue
     */
    public String getCodeIsoLangue();

    /**
     * getter pour l'attribut description
     * 
     * 
     * @return la valeur courante de l'attribut description
     */
    public String getDescription();

    /**
     * getter pour l'attribut descriptionBrut
     * 
     * 
     * @return la valeur courante de l'attribut descriptionBrut
     */
    public String getDescriptionBrut();

    /**
     * getter pour l'attribut niveau
     * 
     * @return la valeur courante de l'attribut niveau
     */
    public String getNiveau();

    /**
     * getter pour l'attribut position
     * 
     * @return la valeur courante de l'attribut position
     */
    public String getPosition();
}
