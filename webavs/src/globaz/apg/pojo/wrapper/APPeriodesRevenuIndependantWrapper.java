/*
 * Créé le 5 mai 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.pojo.wrapper;

import globaz.apg.api.cot.pers.IAPPeriodesRevenuIndependant;

/**
 * @author scr
 * 
 *         Si cotisations personnelles prennent une décision définitive pour une année donnée, il faut vérifier si des
 *         APG ont été calculées sur l'ancien montant et si différence entre les montants, créer une liste pour des
 *         corrections APG.
 * 
 */
public class APPeriodesRevenuIndependantWrapper implements IAPPeriodesRevenuIndependant {

    public String dateDebut = "";
    public String dateFin = "";
    public String revenuIndependant = "";

    /**
     * @return
     */
    @Override
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return
     */
    @Override
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return
     */
    @Override
    public String getRevenuIndependant() {
        return revenuIndependant;
    }

    /**
     * @param string
     */
    public void setDateDebut(String string) {
        dateDebut = string;
    }

    /**
     * @param string
     */
    public void setDateFin(String string) {
        dateFin = string;
    }

    /**
     * @param string
     */
    public void setRevenuIndependant(String string) {
        revenuIndependant = string;
    }

}
