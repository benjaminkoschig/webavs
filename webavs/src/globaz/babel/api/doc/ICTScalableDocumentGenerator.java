/*
 * Créé le 7 déc. 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.babel.api.doc;

/**
 * @author bsc
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public interface ICTScalableDocumentGenerator {

    /**
     * Donne les proprietes parametrables du document
     * 
     * @return
     */
    public ICTScalableDocumentProperties getScalableDocumentProperties();

    /**
     * Set les proprietes parametrables du document
     * 
     * @param documentProperties
     */
    public void setScalableDocumentProperties(ICTScalableDocumentProperties documentProperties);

}
