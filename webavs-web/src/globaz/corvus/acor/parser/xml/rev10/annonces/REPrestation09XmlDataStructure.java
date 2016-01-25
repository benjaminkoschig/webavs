package globaz.corvus.acor.parser.xml.rev10.annonces;

/**
 * 
 * @author SCR
 * 
 */
public class REPrestation09XmlDataStructure {

    private REBaseCalcul09XmlDataStructure baseCalcul = null;
    private String codeMutation = null;
    private String codeSpecial = null;
    private String debutDroit = null;
    private String finDroit = null;
    private String genrePrestation = null;

    private String invalidSurvivant = null;

    private String mensualite = null;
    private String mensualiteRenteOrdinaireRemplacee = null;
    private String reduction = null;

    public REBaseCalcul09XmlDataStructure getBaseCalcul() {
        return baseCalcul;
    }

    public String getCodeMutation() {
        return codeMutation;
    }

    public String getCodeSpecial() {
        return codeSpecial;
    }

    public String getDebutDroit() {
        return debutDroit;
    }

    public String getFinDroit() {
        return finDroit;
    }

    public String getGenrePrestation() {
        return genrePrestation;
    }

    public String getInvalidSurvivant() {
        return invalidSurvivant;
    }

    public String getMensualite() {
        return mensualite;
    }

    public String getMensualiteRenteOrdinaireRemplacee() {
        return mensualiteRenteOrdinaireRemplacee;
    }

    public String getReduction() {
        return reduction;
    }

    public void setBaseCalcul(REBaseCalcul09XmlDataStructure baseCalcul) {
        this.baseCalcul = baseCalcul;
    }

    public void setCodeMutation(String codeMutation) {
        this.codeMutation = codeMutation;
    }

    public void setCodeSpecial(String codeSpecial) {
        this.codeSpecial = codeSpecial;
    }

    public void setDebutDroit(String debutDroit) {
        this.debutDroit = debutDroit;
    }

    public void setFinDroit(String finDroit) {
        this.finDroit = finDroit;
    }

    public void setGenrePrestation(String genrePrestation) {
        this.genrePrestation = genrePrestation;
    }

    public void setInvalidSurvivant(String invalidSurvivant) {
        this.invalidSurvivant = invalidSurvivant;
    }

    public void setMensualite(String mensualite) {
        this.mensualite = mensualite;
    }

    public void setMensualiteRenteOrdinaireRemplacee(String mensualiteRenteOrdinaireRemplacee) {
        this.mensualiteRenteOrdinaireRemplacee = mensualiteRenteOrdinaireRemplacee;
    }

    public void setReduction(String reduction) {
        this.reduction = reduction;
    }

}
