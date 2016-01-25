/*
 * Créé le 18 oct. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.pavo.db.splitting;

import globaz.jade.client.util.JadeStringUtil;

/**
 * @author dgi
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CICumulsRCI {
    private int annee;
    private CICumulSplitting cumulsAssure;
    private CICumulSplitting cumulsConjoint;
    private boolean isPeriodeEtrangerAssure = false;
    private boolean isPeriodeEtrangerConjoint = false;
    private boolean isPeriodeJeunesseAssure = false;
    private boolean isPeriodeJeunesseConjoint = false;
    private boolean isPeriodeMariage = false;
    private boolean isPeriodeRenteAssure = false;
    private boolean isPeriodeRenteConjoint = false;

    public CICumulsRCI(int anneeCumul) {
        annee = anneeCumul;
    }

    public String getCOL_1() {
        if (cumulsAssure != null) {
            if (JadeStringUtil.isBlankOrZero(cumulsAssure.getCodeADS())) {
                return String.valueOf(cumulsAssure.getRevenu().longValue());
            } else {
                return String.valueOf(cumulsAssure.getRevenu().longValue()) + " " + cumulsAssure.getCodeADS();
            }
        }
        return "0";
    }

    public Boolean getCOL_10() {
        return new Boolean(isPeriodeRenteConjoint);
    }

    public Boolean getCOL_11() {
        // font grisé
        return new Boolean(!isPeriodeMariage || isPeriodeEtrangerAssure || isPeriodeEtrangerConjoint
                || isPeriodeRenteAssure || isPeriodeRenteConjoint);
    }

    public Integer getCOL_2() {
        return new Integer(annee);
    }

    public String getCOL_3() {
        if (cumulsConjoint != null) {
            if (JadeStringUtil.isBlankOrZero(cumulsConjoint.getCodeADS())) {
                return String.valueOf(cumulsConjoint.getRevenu().longValue());
            } else {
                return String.valueOf(cumulsConjoint.getRevenu().longValue()) + " " + cumulsConjoint.getCodeADS();
            }
        }

        return "0";
    }

    public Boolean getCOL_4() {
        return new Boolean(isPeriodeMariage);
    }

    public Boolean getCOL_5() {
        return new Boolean(isPeriodeEtrangerAssure);
    }

    public Boolean getCOL_6() {
        return new Boolean(isPeriodeEtrangerConjoint);
    }

    public Boolean getCOL_7() {
        return new Boolean(isPeriodeJeunesseAssure);
    }

    public Boolean getCOL_8() {
        return new Boolean(isPeriodeJeunesseConjoint);
    }

    public Boolean getCOL_9() {
        return new Boolean(isPeriodeRenteAssure);
    }

    /**
     * @return
     */
    public CICumulSplitting getCumulsAssure() {
        return cumulsAssure;
    }

    /**
     * @return
     */
    public CICumulSplitting getCumulsConjoint() {
        return cumulsConjoint;
    }

    /**
     * @return
     */
    public boolean isPeriodeEtrangerAssure() {
        return isPeriodeEtrangerAssure;
    }

    /**
     * @return
     */
    public boolean isPeriodeEtrangerConjoint() {
        return isPeriodeEtrangerConjoint;
    }

    /**
     * @return
     */
    public boolean isPeriodeJeunesseAssure() {
        return isPeriodeJeunesseAssure;
    }

    /**
     * @return
     */
    public boolean isPeriodeJeunesseConjoint() {
        return isPeriodeJeunesseConjoint;
    }

    /**
     * @return
     */
    public boolean isPeriodeMariage() {
        return isPeriodeMariage;
    }

    /**
     * @return
     */
    public boolean isPeriodeRenteAssure() {
        return isPeriodeRenteAssure;
    }

    /**
     * @return
     */
    public boolean isPeriodeRenteConjoint() {
        return isPeriodeRenteConjoint;
    }

    /**
     * @param splitting
     */
    public void setCumulsAssure(CICumulSplitting splitting) {
        cumulsAssure = splitting;
    }

    /**
     * @param splitting
     */
    public void setCumulsConjoint(CICumulSplitting splitting) {
        cumulsConjoint = splitting;
    }

    /**
     * @param b
     */
    public void setPeriodeEtrangerAssure(boolean b) {
        isPeriodeEtrangerAssure = b;
    }

    /**
     * @param b
     */
    public void setPeriodeEtrangerConjoint(boolean b) {
        isPeriodeEtrangerConjoint = b;
    }

    /**
     * @param b
     */
    public void setPeriodeJeunesseAssure(boolean b) {
        isPeriodeJeunesseAssure = b;
    }

    /**
     * @param b
     */
    public void setPeriodeJeunesseConjoint(boolean b) {
        isPeriodeJeunesseConjoint = b;
    }

    /**
     * @param b
     */
    public void setPeriodeMariage(boolean b) {
        isPeriodeMariage = b;
    }

    /**
     * @param b
     */
    public void setPeriodeRenteAssure(boolean b) {
        isPeriodeRenteAssure = b;
    }

    /**
     * @param b
     */
    public void setPeriodeRenteConjoint(boolean b) {
        isPeriodeRenteConjoint = b;
    }

}
