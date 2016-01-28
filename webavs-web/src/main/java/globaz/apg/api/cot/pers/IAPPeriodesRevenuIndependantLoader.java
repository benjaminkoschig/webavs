/*
 * Créé le 5 mai 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.api.cot.pers;

/**
 * @author scr
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
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
