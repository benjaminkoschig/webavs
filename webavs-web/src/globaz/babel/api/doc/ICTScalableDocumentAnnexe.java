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
public interface ICTScalableDocumentAnnexe extends Serializable {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Donne l'identifiant de la copie
     */
    public String getKey();

    /**
     * Donne le libelle de l'annexe
     */
    public String getLibelle();

    /**
     * Mise a jour du libelle de l'annexe
     * 
     * @param libelle
     *            le nouveau libelle
     */
    public void setLibelle(String libelle);
}
