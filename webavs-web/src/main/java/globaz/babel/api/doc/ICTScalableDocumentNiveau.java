/*
 * Créé le 7 nov. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.api.doc;

import java.io.Serializable;
import java.util.Iterator;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public interface ICTScalableDocumentNiveau extends Serializable {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Ajoute une position au niveau
     * 
     * @param position
     */
    public void addPosition(ICTScalableDocumentPosition position);

    /**
     * Donne la description du niveau
     */
    public String getDescription();

    /**
     * Donne l'identifiant du niveau
     */
    public String getKey();

    /**
     * Donne le niveau du niveau
     */
    public String getNiveau();

    /**
     * Donne une position
     */
    public ICTScalableDocumentPosition getPosition(int pos);

    /**
     * Donne un iterator sur les positions du niveau
     */
    public Iterator getPositionIterator();

    /**
     * Donne le nombre de positions du niveau
     */
    public int getPositionSize();

    /**
     * Mise a jour de la description du niveau
     * 
     * @param description
     *            la nouvelle description
     */
    public void setDescription(String description);

    /**
     * Mise a jour du niveau du niveau
     * 
     * @param le
     *            niveau du nouveau niveau
     */
    public void setNiveau(String niveau);
}
