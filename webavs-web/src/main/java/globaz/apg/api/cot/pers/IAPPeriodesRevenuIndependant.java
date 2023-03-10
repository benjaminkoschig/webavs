/*
 * Cr?? le 5 mai 06
 * 
 * Pour changer le mod?le de ce fichier g?n?r?, allez ? : Fen?tre&gt;Pr?f?rences&gt;Java&gt;G?n?ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.api.cot.pers;

/**
 * @author scr
 * 
 *         Si cotisations personnelles prennent une d?cision d?finitive pour une ann?e donn?e, il faut v?rifier si des
 *         APG ont ?t? calcul?es sur l'ancien montant et si diff?rence entre les montants, cr?er une liste pour des
 *         corrections APG.
 * 
 */
public interface IAPPeriodesRevenuIndependant {

    public String getDateDebut();

    public String getDateFin();

    public String getRevenuIndependant();

}
