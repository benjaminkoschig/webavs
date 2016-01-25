package globaz.corvus.acor.parser.xml.rev10.fcalcul;

public class REDecisionXmlDataStructure {

    private REPrestationXmlDataStructure prestation = null;

    public REPrestationXmlDataStructure getPrestation() {
        return prestation;
    }

    public void setPrestation(REPrestationXmlDataStructure prestation) {
        this.prestation = prestation;
    }

    public String toStringgg() {
        StringBuffer sb = new StringBuffer();
        sb.append("==================================================>>>\n");
        sb.append("\t\tREDecisionXmlDataStructure").append("\n");
        sb.append("==================================================>>>\n");

        if (prestation != null) {
            sb.append("prestation = \n" + prestation.toStringgg()).append("\n");
        }

        sb.append("==================================================<<<\n\n");
        return sb.toString();

    }

}
