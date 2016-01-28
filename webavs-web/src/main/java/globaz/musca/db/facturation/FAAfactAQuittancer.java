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
public class FAAfactAQuittancer extends FAAfact {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void _afterUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Mise � jour du total dans l'ent�te
        FAEnteteFacture entete = new FAEnteteFacture();
        FAAfact afact = new FAAfact();
        afact.setIdEnteteFacture(getIdEnteteFacture());
        afact.setMontantFacture(getMontantFacture());
        afact.setAQuittancer(isAQuittancer());
        entete.updateTotal(transaction, afact);
    }

}
