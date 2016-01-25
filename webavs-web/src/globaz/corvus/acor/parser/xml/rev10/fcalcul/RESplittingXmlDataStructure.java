package globaz.corvus.acor.parser.xml.rev10.fcalcul;

public class RESplittingXmlDataStructure {

    private boolean isPartageRevenu = false;

    public boolean isPartageRevenu() {
        return isPartageRevenu;
    }

    public void setPartageRevenu(boolean isPartageRevenu) {
        this.isPartageRevenu = isPartageRevenu;
    }

    public String toStringgg() {
        StringBuffer sb = new StringBuffer();
        sb.append("==================================================>>>\n");
        sb.append("\t\tRESplittingXmlDataStructure").append("\n");
        sb.append("==================================================>>>\n");
        sb.append("isPartageRevenu = " + isPartageRevenu).append("\n");
        sb.append("==================================================<<<\n\n");
        return sb.toString();

    }

}
