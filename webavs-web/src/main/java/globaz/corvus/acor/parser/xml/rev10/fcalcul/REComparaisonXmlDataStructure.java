package globaz.corvus.acor.parser.xml.rev10.fcalcul;

public class REComparaisonXmlDataStructure {
    private String resultatComparaison = null;
    private String typeCalculComparatif = null;

    public String getResultatComparaison() {
        return resultatComparaison;
    }

    public String getTypeCalculComparatif() {
        return typeCalculComparatif;
    }

    public void setResultatComparaison(String resultatComparaison) {
        this.resultatComparaison = resultatComparaison;
    }

    public void setTypeCalculComparatif(String typeCalculComparatif) {
        this.typeCalculComparatif = typeCalculComparatif;
    }

    public String toStringgg() {
        StringBuffer sb = new StringBuffer();
        sb.append("==================================================>>>\n");
        sb.append("\t\tREComparaisonXmlDataStructure").append("\n");
        sb.append("==================================================>>>\n");
        sb.append("typeCalculComparatif = " + typeCalculComparatif).append("\n");
        sb.append("resultatComparaison 	= " + resultatComparaison).append("\n");
        sb.append("==================================================<<<\n\n");
        return sb.toString();

    }
}
