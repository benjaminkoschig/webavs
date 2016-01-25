/*
 * Cr�� le 7 nov. 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.babel.api.doc;

import java.io.Serializable;

/**
 * @author bsc
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
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
