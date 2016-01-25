package globaz.pavo.db.comparaison;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JAUtil;
import globaz.pavo.util.CIUtil;

public class CIEnteteRecord {

    private String agence = "";
    private String agenceCommettante = "";
    private String anneeOuverture = "";
    private String caisse = "";
    private String caisseCommettante = "";
    private String dateCloture = "";
    private String motifOuverture = "";
    private String motifRci = "";
    private String nomPrenom = "";
    private String numeroAssure = "";
    private String numeroAssureAnterieur = "";

    private String pays = "";

    /**
	 * 
	 */
    public CIEnteteRecord() {
        super();
    }

    /**
     * @return
     */
    public String getAgence() {
        return agence;
    }

    /**
     * @return
     */
    public String getAgenceCommettante() {
        return agenceCommettante;
    }

    /**
     * @return
     */
    public String getAnneeOuverture() {
        try {
            if (!JAUtil.isStringEmpty(anneeOuverture)) {
                if (Integer.parseInt(anneeOuverture) > JACalendar.getYear(JACalendar.todayJJsMMsAAAA()) - 2000) {
                    return "19" + anneeOuverture;
                } else {
                    return "20" + anneeOuverture;
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @return
     */
    public String getCaisse() {
        return caisse;
    }

    public String getCaisseClotureFormatee() {
        if (!JAUtil.isIntegerEmpty(caisseCommettante)) {
            if (!JAUtil.isIntegerEmpty(agenceCommettante)) {
                return CIUtil.unPadAdmin(caisseCommettante) + "." + CIUtil.unPadAdmin(agenceCommettante);
            } else {
                return CIUtil.unPadAdmin(caisseCommettante);
            }

        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getCaisseCommettante() {
        return caisseCommettante;
    }

    /**
     * @return
     */
    public String getDateCloture() {
        return dateCloture;
    }

    public String getDateClotureFormatee() {
        if (dateCloture.trim().length() == 4 && !JAUtil.isIntegerEmpty(dateCloture)) {
            String anneeCloture = "";
            if (dateCloture.substring(2, 4).compareTo("48") >= 0) {
                anneeCloture = "19" + dateCloture.substring(2, 4);
            } else {
                anneeCloture = "20" + dateCloture.substring(2, 4);
            }

            return "01." + dateCloture.substring(0, 2) + "." + anneeCloture;
        } else {
            return "";
        }
    }

    /**
     * @return
     */
    public String getMotifOuverture() {
        return motifOuverture;
    }

    /**
     * @return
     */
    public String getMotifRci() {
        return motifRci;
    }

    /**
     * @return
     */
    public String getNomPrenom() {
        return nomPrenom;
    }

    /**
     * @return
     */
    public String getNumeroAssure() {
        return numeroAssure;
    }

    /**
     * @return
     */
    public String getNumeroAssureAnterieur() {
        return numeroAssureAnterieur;
    }

    /**
     * @return
     */
    public String getPays() {
        return pays;
    }

    /**
     * @param string
     */
    public void setAgence(String string) {
        agence = string;
    }

    /**
     * @param string
     */
    public void setAgenceCommettante(String string) {
        agenceCommettante = string;
    }

    /**
     * @param string
     */
    public void setAnneeOuverture(String string) {
        anneeOuverture = string;
    }

    /**
     * @param string
     */
    public void setCaisse(String string) {
        caisse = string;
    }

    /**
     * @param string
     */
    public void setCaisseCommettante(String string) {
        caisseCommettante = string;
    }

    /**
     * @param string
     */
    public void setDateCloture(String string) {
        dateCloture = string;
    }

    /**
     * @param string
     */
    public void setMotifOuverture(String string) {
        motifOuverture = string;
    }

    /**
     * @param string
     */
    public void setMotifRci(String string) {
        motifRci = string;
    }

    /**
     * @param string
     */
    public void setNomPrenom(String string) {
        nomPrenom = string;
    }

    /**
     * @param string
     */
    public void setNumeroAssure(String string) {
        numeroAssure = string;
    }

    /**
     * @param string
     */
    public void setNumeroAssureAnterieur(String string) {
        numeroAssureAnterieur = string;
    }

    /**
     * @param string
     */
    public void setPays(String string) {
        pays = string;
    }

}
