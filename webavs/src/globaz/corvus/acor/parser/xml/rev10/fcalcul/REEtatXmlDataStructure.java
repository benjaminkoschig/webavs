package globaz.corvus.acor.parser.xml.rev10.fcalcul;

public class REEtatXmlDataStructure {

    private String anneeMontantRAM = null;
    private String dateDebut = null;

    private String dateFin = null;
    private String montant = null;
    private String montantReductionAnticipation = null;

    private String ram = null;
    private String supplementAjournement = null;

    public String getAnneeMontantRAM() {
        return anneeMontantRAM;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getMontant() {
        return montant;
    }

    public String getMontantReductionAnticipation() {
        return montantReductionAnticipation;
    }

    public String getRam() {
        return ram;
    }

    public String getSupplementAjournement() {
        return supplementAjournement;
    }

    public void setAnneeMontantRAM(String anneeMontantRAM) {
        this.anneeMontantRAM = anneeMontantRAM;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setMontantReductionAnticipation(String montantReductionAnticipation) {
        this.montantReductionAnticipation = montantReductionAnticipation;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public void setSupplementAjournement(String supplementAjournement) {
        this.supplementAjournement = supplementAjournement;
    }

    public String toStringgg() {
        StringBuffer sb = new StringBuffer();
        sb.append("==================================================>>>\n");
        sb.append("\t\tREEtatXmlDataStructure").append("\n");
        sb.append("==================================================>>>\n");
        sb.append("montant 						= " + montant).append("\n");
        sb.append("anneeMontantRAM 				= " + anneeMontantRAM).append("\n");
        sb.append("dateDebut 					= " + dateDebut).append("\n");
        sb.append("dateFin 						= " + dateFin).append("\n");
        sb.append("ram 							= " + ram).append("\n");
        sb.append("montantReductionAnticipation = " + montantReductionAnticipation).append("\n");
        sb.append("supplementAjournement 		= " + supplementAjournement).append("\n");
        sb.append("==================================================<<<\n\n");
        return sb.toString();

    }

}
