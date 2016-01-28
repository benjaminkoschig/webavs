package globaz.corvus.acor.parser.xml.rev10.annonces;

/**
 * 
 * @author SCR
 * 
 */
public class REAnnonce09XmlDataStructure extends REAnnonceXmlDataStructure {

    private String codeSpecial = null;
    private REAyantDroitXmlDataStructure infoAyantDroit = null;
    private REPrestation09XmlDataStructure infoPrestation = null;
    private String invalidSurvivant = null;
    private String moisRapport = null;
    private String reduction = null;

    public String getCodeSpecial() {
        return codeSpecial;
    }

    public REAyantDroitXmlDataStructure getInfoAyantDroit() {
        return infoAyantDroit;
    }

    public REPrestation09XmlDataStructure getInfoPrestation() {
        return infoPrestation;
    }

    public String getInvalidSurvivant() {
        return invalidSurvivant;
    }

    public String getMoisRapport() {
        return moisRapport;
    }

    public String getReduction() {
        return reduction;
    }

    public void setCodeSpecial(String codeSpecial) {
        this.codeSpecial = codeSpecial;
    }

    public void setInfoAyantDroit(REAyantDroitXmlDataStructure infoAyantDroit) {
        this.infoAyantDroit = infoAyantDroit;
    }

    public void setInfoPrestation(REPrestation09XmlDataStructure infoPrestation) {
        this.infoPrestation = infoPrestation;
    }

    public void setInvalidSurvivant(String invalidSurvivant) {
        this.invalidSurvivant = invalidSurvivant;
    }

    public void setMoisRapport(String moisRapport) {
        this.moisRapport = moisRapport;
    }

    public void setReduction(String reduction) {
        this.reduction = reduction;
    }

}
