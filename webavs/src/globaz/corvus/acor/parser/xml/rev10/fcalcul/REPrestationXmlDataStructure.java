package globaz.corvus.acor.parser.xml.rev10.fcalcul;

public class REPrestationXmlDataStructure {

    private String nssBeneficiaire = null;
    private RERenteXmlDataStructure rente = null;

    public String getNssBeneficiaire() {
        return nssBeneficiaire;
    }

    public RERenteXmlDataStructure getRente() {
        return rente;
    }

    public void setNssBeneficiaire(String nssBeneficiaire) {
        this.nssBeneficiaire = nssBeneficiaire;
    }

    public void setRente(RERenteXmlDataStructure rente) {
        this.rente = rente;
    }

    public String toStringgg() {
        StringBuffer sb = new StringBuffer();
        sb.append("==================================================>>>\n");
        sb.append("\t\tREAssureXmlDataStructure").append("\n");
        sb.append("==================================================>>>\n");
        sb.append("nssBeneficiaire = " + nssBeneficiaire).append("\n");

        if (rente != null) {
            sb.append("rente = \n" + rente.toStringgg()).append("\n");
        }
        sb.append("==================================================<<<\n\n");
        return sb.toString();

    }

}
