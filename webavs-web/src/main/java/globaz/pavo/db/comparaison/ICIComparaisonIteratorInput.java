/*
 * Cr�� le 9 sept. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.comparaison;

/**
 * @author jmc
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface ICIComparaisonIteratorInput {

    public void close();

    public String getFileName();

    public boolean hasNext() throws Exception;

    public CIEnteteRecord next() throws Exception;

    public void setFileName(String string);

    public int size() throws Exception;
}
