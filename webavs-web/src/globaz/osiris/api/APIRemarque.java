/*
 * Cr�� le Aug 17, 2005
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.osiris.api;

import globaz.globall.api.BIEntity;

/**
 * @author dda Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface APIRemarque extends BIEntity {

    public String getIdRemarque();

    public String getTexte();

    public void setIdRemarque(String newIdRemarque);

    public void setTexte(String newTexte);

}
