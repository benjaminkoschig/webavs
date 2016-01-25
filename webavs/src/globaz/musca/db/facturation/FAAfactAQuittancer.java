/*
 * Créé le 17 juin 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.musca.db.facturation;

/**
 * @author mmu Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class FAAfactAQuittancer extends FAAfact {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void _afterUpdate(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // Mise à jour du total dans l'entête
        FAEnteteFacture entete = new FAEnteteFacture();
        FAAfact afact = new FAAfact();
        afact.setIdEnteteFacture(getIdEnteteFacture());
        afact.setMontantFacture(getMontantFacture());
        afact.setAQuittancer(isAQuittancer());
        entete.updateTotal(transaction, afact);
    }

}
