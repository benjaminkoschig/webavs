/*
 * Créé le 7 nov. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.api.doc;

import java.io.Serializable;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public interface ICTScalableDocumentPosition extends Serializable {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Donne la description de la position
     */
    public String getDescription();

    /**
     * Donne l'identifiant de la copie
     */
    public String getKey();

    /**
     * Donne la position de la position
     */
    public String getPosition();

    /**
     * True si la position est selectionnee pour faire partie du document
     */
    public boolean isSelected();

    /**
     * Mise a jour de la description de la position
     * 
     * @param description
     *            la nouvelle description
     */
    public void setDescription(String description);

    /**
     * Mise a jour de la valeur isSelected
     * 
     * @param isSelected
     *            la nouvelle valeur
     */
    public void setIsSelected(boolean isSelected);

    /**
     * Mise a jour de la position de la position
     * 
     * @param position
     *            la nouvelle position
     */
    public void setPosition(String position);
}
