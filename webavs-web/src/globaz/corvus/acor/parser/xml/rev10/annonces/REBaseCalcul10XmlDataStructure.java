package globaz.corvus.acor.parser.xml.rev10.annonces;

/**
 * 
 * @author SCR
 * 
 */
public class REBaseCalcul10XmlDataStructure {

    private String anneeNiveau = null;
    private REAIXmlDataStructure infoAI = null;
    private REBonification10XmlDataStructure infoBonification = null;
    private REEchelleXmlDataStructure infoEchelle = null;
    private RERetraiteFlexibleXmlDataStructure infoRetraiteFlexible = null;
    private RERevAnnuelMoyenXmlDataStructure infoRevAnnuelMoyen = null;

    public String getAnneeNiveau() {
        return anneeNiveau;
    }

    public REAIXmlDataStructure getInfoAI() {
        return infoAI;
    }

    public REBonification10XmlDataStructure getInfoBonification() {
        return infoBonification;
    }

    public REEchelleXmlDataStructure getInfoEchelle() {
        return infoEchelle;
    }

    public RERetraiteFlexibleXmlDataStructure getInfoRetraiteFlexible() {
        return infoRetraiteFlexible;
    }

    public RERevAnnuelMoyenXmlDataStructure getInfoRevAnnuelMoyen() {
        return infoRevAnnuelMoyen;
    }

    public void setAnneeNiveau(String anneeNiveau) {
        this.anneeNiveau = anneeNiveau;
    }

    public void setInfoAI(REAIXmlDataStructure infoAI) {
        this.infoAI = infoAI;
    }

    public void setInfoBonification(REBonification10XmlDataStructure infoBonification) {
        this.infoBonification = infoBonification;
    }

    public void setInfoEchelle(REEchelleXmlDataStructure infoEchelle) {
        this.infoEchelle = infoEchelle;
    }

    public void setInfoRetraiteFlexible(RERetraiteFlexibleXmlDataStructure infoRetraiteFlexible) {
        this.infoRetraiteFlexible = infoRetraiteFlexible;
    }

    public void setInfoRevAnnuelMoyen(RERevAnnuelMoyenXmlDataStructure infoRevAnnuelMoyen) {
        this.infoRevAnnuelMoyen = infoRevAnnuelMoyen;
    }

}
