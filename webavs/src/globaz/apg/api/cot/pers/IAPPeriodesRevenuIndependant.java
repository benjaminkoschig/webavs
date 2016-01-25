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
 *         Si cotisations personnelles prennent une décision définitive pour une année donnée, il faut vérifier si des
 *         APG ont été calculées sur l'ancien montant et si différence entre les montants, créer une liste pour des
 *         corrections APG.
 * 
 */
public interface IAPPeriodesRevenuIndependant {

    public String getDateDebut();

    public String getDateFin();

    public String getRevenuIndependant();

}
