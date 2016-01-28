package globaz.corvus.acor.parser.xml.rev10.fcalcul;

/**
 * 
 * @author SCR
 * 
 */
public class REVersementXmlDataStructure {

    private String dateDebutVersement = null;
    private String dateFinVersement = null;
    private String montantVerserment = null;

    public String getDateDebutVersement() {
        return dateDebutVersement;
    }

    public String getDateFinVersement() {
        return dateFinVersement;
    }

    public String getMontantVerserment() {
        return montantVerserment;
    }

    public void setDateDebutVersement(String dateDebutVersement) {
        this.dateDebutVersement = dateDebutVersement;
    }

    public void setDateFinVersement(String dateFinVersement) {
        this.dateFinVersement = dateFinVersement;
    }

    public void setMontantVerserment(String montantVerserment) {
        this.montantVerserment = montantVerserment;
    }

    public String toStringgg() {
        StringBuffer sb = new StringBuffer();
        sb.append("==================================================>>>\n");
        sb.append("\t\tREVersementXmlDataStructure").append("\n");
        sb.append("==================================================>>>\n");
        sb.append("dateDebutVersement = " + dateDebutVersement).append("\n");
        sb.append("dateFinVersement = " + dateFinVersement).append("\n");
        sb.append("montantVerserment = " + montantVerserment).append("\n");
        sb.append("==================================================<<<\n\n");
        return sb.toString();

    }

}
