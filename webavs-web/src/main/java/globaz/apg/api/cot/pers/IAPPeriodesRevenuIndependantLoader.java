/*
 * Cr�� le 5 mai 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.api.cot.pers;

/**
 * @author scr
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface IAPPeriodesRevenuIndependantLoader {

    /*
     * (non-Javadoc)
     * 
     * @see globaz.apg.api.cot.pers.IAPPeriodesRevenuIndependant#load(java.lang.String , java.lang.String,
     * java.lang.String)
     */
    public IAPPeriodesRevenuIndependant[] load(String idTiers, String dateDebut, String dateFin) throws Exception;

}
