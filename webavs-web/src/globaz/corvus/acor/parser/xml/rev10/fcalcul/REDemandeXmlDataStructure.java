package globaz.corvus.acor.parser.xml.rev10.fcalcul;

public class REDemandeXmlDataStructure {

    private String dateTraitement = null;
    private Boolean isProvisoire = null;

    public String getDateTraitement() {
        return dateTraitement;
    }

    public Boolean getIsProvisoire() {
        return isProvisoire;
    }

    public void setDateTraitement(String dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public void setIsProvisoire(Boolean isProvisoire) {
        this.isProvisoire = isProvisoire;
    }

    public String toStringgg() {
        StringBuffer sb = new StringBuffer();
        sb.append("==================================================>>>\n");
        sb.append("\t\tREDemandeXmlDataStructure").append("\n");
        sb.append("==================================================>>>\n");
        sb.append("dateTraitement = " + getDateTraitement()).append("\n");
        sb.append("isProvisoire = " + getIsProvisoire()).append("\n");
        sb.append("==================================================<<<\n\n");
        return sb.toString();

    }
}
