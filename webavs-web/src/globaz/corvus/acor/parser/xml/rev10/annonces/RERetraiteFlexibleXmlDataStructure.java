package globaz.corvus.acor.parser.xml.rev10.annonces;

/**
 * 
 * @author SCR
 * 
 */
public class RERetraiteFlexibleXmlDataStructure {

    private String dateDebutAnticipation = null;
    private String dateRevocationAjournement = null;
    private String dureeAjournement = null;

    private String nombreAnneeAnticipation = null;
    private String reductionAnticipation = null;
    private String supplementAjournement = null;

    public String getDateDebutAnticipation() {
        return dateDebutAnticipation;
    }

    public String getDateRevocationAjournement() {
        return dateRevocationAjournement;
    }

    public String getDureeAjournement() {
        return dureeAjournement;
    }

    public String getNombreAnneeAnticipation() {
        return nombreAnneeAnticipation;
    }

    public String getReductionAnticipation() {
        return reductionAnticipation;
    }

    public String getSupplementAjournement() {
        return supplementAjournement;
    }

    public void setDateDebutAnticipation(String dateDebutAnticipation) {
        this.dateDebutAnticipation = dateDebutAnticipation;
    }

    public void setDateRevocationAjournement(String dateRevocationAjournement) {
        this.dateRevocationAjournement = dateRevocationAjournement;
    }

    public void setDureeAjournement(String dureeAjournement) {
        this.dureeAjournement = dureeAjournement;
    }

    public void setNombreAnneeAnticipation(String nombreAnneeAnticipation) {
        this.nombreAnneeAnticipation = nombreAnneeAnticipation;
    }

    public void setReductionAnticipation(String reductionAnticipation) {
        this.reductionAnticipation = reductionAnticipation;
    }

    public void setSupplementAjournement(String supplementAjournement) {
        this.supplementAjournement = supplementAjournement;
    }

}
