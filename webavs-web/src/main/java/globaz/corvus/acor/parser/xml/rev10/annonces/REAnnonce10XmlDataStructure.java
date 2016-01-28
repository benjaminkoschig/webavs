package globaz.corvus.acor.parser.xml.rev10.annonces;

/**
 * 
 * @author SCR
 * 
 */
public class REAnnonce10XmlDataStructure extends REAnnonceXmlDataStructure {

    private REAyantDroitXmlDataStructure infoAyantDroit = null;
    private REPrestation10XmlDataStructure infoPrestation = null;
    private String moisRapport = null;

    public REAyantDroitXmlDataStructure getInfoAyantDroit() {
        return infoAyantDroit;
    }

    public REPrestation10XmlDataStructure getInfoPrestation() {
        return infoPrestation;
    }

    public String getMoisRapport() {
        return moisRapport;
    }

    public void setInfoAyantDroit(REAyantDroitXmlDataStructure ayantDroit) {
        infoAyantDroit = ayantDroit;
    }

    public void setInfoPrestation(REPrestation10XmlDataStructure prestation) {
        infoPrestation = prestation;
    }

    public void setMoisRapport(String moisRapport) {
        this.moisRapport = moisRapport;
    }

}
