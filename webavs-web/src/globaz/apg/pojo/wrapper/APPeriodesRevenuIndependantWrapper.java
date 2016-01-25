/*
 * Cr�� le 5 mai 06
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.pojo.wrapper;

import globaz.apg.api.cot.pers.IAPPeriodesRevenuIndependant;

/**
 * @author scr
 * 
 *         Si cotisations personnelles prennent une d�cision d�finitive pour une ann�e donn�e, il faut v�rifier si des
 *         APG ont �t� calcul�es sur l'ancien montant et si diff�rence entre les montants, cr�er une liste pour des
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
