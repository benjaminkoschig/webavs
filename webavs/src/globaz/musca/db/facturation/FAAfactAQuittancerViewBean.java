/*
 * Cr�� le 17 juin 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.musca.db.facturation;

/**
 * @author mmu Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class FAAfactAQuittancerViewBean extends FAAfactAQuittancer {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String ORDER_MONTANT = " FAAFACP.MONTANTFACTURE DESC";
    public static final String ORDER_NO_DEBITEUR = " FAENTFP.IDEXTERNEROLE";
    public static final String ORDER_NOM = " TITIERP.HTLDE1 DESC, TITIERP.HTLDE2 DESC";

}
