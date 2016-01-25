package globaz.corvus.acor.parser.xml.rev10.fcalcul;

/**
 * 
 * @author SCR
 * 
 */

public class REBaseEchelleXmlDataStructure {

    private String anneeCotiClasseAge = null;
    private String anneNiveau = null;
    private REDureeCotisationXmlDataStructure[] dureeCotisation = null;

    public String getAnneeCotiClasseAge() {
        return anneeCotiClasseAge;
    }

    public String getAnneNiveau() {
        return anneNiveau;
    }

    public REDureeCotisationXmlDataStructure[] getDureeCotisation() {
        return dureeCotisation;
    }

    public void setAnneeCotiClasseAge(String anneeCotiClasseAge) {
        this.anneeCotiClasseAge = anneeCotiClasseAge;
    }

    public void setAnneNiveau(String anneNiveau) {
        this.anneNiveau = anneNiveau;
    }

    public void setDureeCotisation(REDureeCotisationXmlDataStructure[] dureeCotisation) {
        this.dureeCotisation = dureeCotisation;
    }

    public String toStringgg() {
        StringBuffer sb = new StringBuffer();
        sb.append("==================================================>>>\n");
        sb.append("\t\tREBaseEchelleXmlDataStructure").append("\n");
        sb.append("==================================================>>>\n");
        sb.append("anneNiveau 			= " + anneNiveau).append("\n");
        sb.append("anneeCotiClasseAge 	= " + anneeCotiClasseAge).append("\n");

        if (dureeCotisation != null) {
            sb.append("dureeCotisation 		= \n\n\n");
            for (int i = 0; i < dureeCotisation.length; i++) {
                sb.append(dureeCotisation[i].toStringgg());
            }
        }
        sb.append("==================================================<<<\n\n");
        return sb.toString();

    }

}
