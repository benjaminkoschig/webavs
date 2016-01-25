package globaz.corvus.acor.parser.xml.rev10.fcalcul;

/**
 * 
 * @author SCR
 * 
 */

public class REDureeCotisationXmlDataStructure {

    public final static String ANNEE_JEUNESSE = "5";
    public final static String ANNEE_OUVERTURE = "7";
    public final static String ASSURANCE_ETRANGERE = "8";
    public final static String BTA = "4";
    public final static String BTE = "3";
    public final static String COTISATION_PERSONNELLE = "1";
    public final static String DUREE_TOT_SANS_MOIS_APPOINT = "10";
    public final static String MARIAGE_VEUVAGE_SANS_COTI = "2";
    public final static String MOIS_APPOINT = "6";
    public final static String TOTAL = "9";

    private String ap73Annee = null;

    private String ap73Mois = null;

    private String av73Annee = null;

    private String av73Mois = null;

    private String totalAnnee = null;

    private String totalMois = null;

    private String type = null;

    public String getAp73Annee() {
        return ap73Annee;
    }

    public String getAp73Mois() {
        return ap73Mois;
    }

    public String getAv73Annee() {
        return av73Annee;
    }

    public String getAv73Mois() {
        return av73Mois;
    }

    public String getTotalAnnee() {
        return totalAnnee;
    }

    public String getTotalMois() {
        return totalMois;
    }

    public String getType() {
        return type;
    }

    public void setAp73Annee(String ap73Annee) {
        this.ap73Annee = ap73Annee;
    }

    public void setAp73Mois(String ap73Mois) {
        this.ap73Mois = ap73Mois;
    }

    public void setAv73Annee(String av73Annee) {
        this.av73Annee = av73Annee;
    }

    public void setAv73Mois(String av73Mois) {
        this.av73Mois = av73Mois;
    }

    public void setTotalAnnee(String totalAnnee) {
        this.totalAnnee = totalAnnee;
    }

    public void setTotalMois(String totalMois) {
        this.totalMois = totalMois;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toStringgg() {
        StringBuffer sb = new StringBuffer();
        sb.append("==================================================>>>\n");
        sb.append("\t\tREDureeCotisationXmlDataStructure").append("\n");
        sb.append("==================================================>>>\n");
        sb.append("type = " + type).append("\n");
        sb.append("av73Annee = " + av73Annee).append("\n");
        sb.append("av73Mois = " + av73Mois).append("\n");
        sb.append("ap73Annee = " + ap73Annee).append("\n");
        sb.append("ap73Mois = " + ap73Mois).append("\n");
        sb.append("totalAnnee = " + totalAnnee).append("\n");
        sb.append("totalMois = " + totalMois).append("\n");
        sb.append("==================================================<<<\n\n");
        return sb.toString();

    }

}
