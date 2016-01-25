package globaz.corvus.acor.parser.xml.rev10.fcalcul;

/**
 * 
 * @author SCR
 * 
 */

public class REAssureXmlDataStructure {

    private String anticipation = null;
    private String codeRefugie = null;
    private String etatCivil = null;
    private String function = null;
    private String nss = null;

    public String getAnticipation() {
        return anticipation;
    }

    public String getCodeRefugie() {
        return codeRefugie;
    }

    public String getEtatCivil() {
        return etatCivil;
    }

    public String getFunction() {
        return function;
    }

    public String getNss() {
        return nss;
    }

    public void setAnticipation(String anticipation) {
        this.anticipation = anticipation;
    }

    public void setCodeRefugie(String codeRefugie) {
        this.codeRefugie = codeRefugie;
    }

    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String toStringgg() {
        StringBuffer sb = new StringBuffer();
        sb.append("==================================================>>>\n");
        sb.append("\t\tREAssureXmlDataStructure").append("\n");
        sb.append("==================================================>>>\n");
        sb.append("nss 				= " + nss).append("\n");
        sb.append("anticipation 	= " + anticipation).append("\n");
        sb.append("etatCivil 		= " + etatCivil).append("\n");
        sb.append("codeRefugie 		= " + codeRefugie).append("\n");
        sb.append("function 		= " + function).append("\n");
        sb.append("==================================================<<<\n\n");
        return sb.toString();

    }

}
