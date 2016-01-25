package globaz.corvus.acor.parser.xml.rev10.fcalcul;

/**
 * 
 * @author SCR
 * 
 */
public class RECarriereAssuranceXmlDataStructure {

    private String nss = null;
    private String totRevenusJeunesse = null;
    private String totRevenusPrisEnCompte = null;

    public String getNss() {
        return nss;
    }

    public String getTotRevenusJeunesse() {
        return totRevenusJeunesse;
    }

    public String getTotRevenusPrisEnCompte() {
        return totRevenusPrisEnCompte;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public void setTotRevenusJeunesse(String totRevenusJeunesse) {
        this.totRevenusJeunesse = totRevenusJeunesse;
    }

    public void setTotRevenusPrisEnCompte(String totRevenusPrisEnCompte) {
        this.totRevenusPrisEnCompte = totRevenusPrisEnCompte;
    }

    public String toStringgg() {
        StringBuffer sb = new StringBuffer();
        sb.append("==================================================>>>\n");
        sb.append("\t\tRECarriereAssuranceXmlDataStructure").append("\n");
        sb.append("==================================================>>>\n");
        sb.append("nss = " + nss).append("\n");
        sb.append("totRevenusJeunesse 	  = " + totRevenusJeunesse).append("\n");
        sb.append("totRevenusPrisEnCompte = " + totRevenusPrisEnCompte).append("\n");
        sb.append("==================================================<<<\n\n");
        return sb.toString();

    }
}
