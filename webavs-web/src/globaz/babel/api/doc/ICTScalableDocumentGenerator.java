/*
 * Cr�� le 7 d�c. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.babel.api.doc;

/**
 * @author bsc
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
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
