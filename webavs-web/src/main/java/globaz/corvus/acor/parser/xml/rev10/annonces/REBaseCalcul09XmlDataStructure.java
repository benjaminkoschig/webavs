package globaz.corvus.acor.parser.xml.rev10.annonces;

/**
 * 
 * @author SCR
 * 
 */
public class REBaseCalcul09XmlDataStructure {

    private String anneeNiveau = null;
    private REAIXmlDataStructure infoAIEpouse = null;
    private REAIXmlDataStructure infoAIEpoux = null;
    private REBonification09XmlDataStructure infoBonification = null;
    private REEchelleXmlDataStructure infoEchelle = null;
    private RERetraiteFlexibleXmlDataStructure infoRetraiteFlexible = null;
    private RERevAnnuelMoyenXmlDataStructure infoRevAnnuelMoyen = null;

    private String limiteRevenu = null;
    private String minimumGaranti = null;

    public String getAnneeNiveau() {
        return anneeNiveau;
    }

    public REAIXmlDataStructure getInfoAIEpouse() {
        return infoAIEpouse;
    }

    public REAIXmlDataStructure getInfoAIEpoux() {
        return infoAIEpoux;
    }

    public REBonification09XmlDataStructure getInfoBonification() {
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

    public String getLimiteRevenu() {
        return limiteRevenu;
    }

    public String getMinimumGaranti() {
        return minimumGaranti;
    }

    public void setAnneeNiveau(String anneeNiveau) {
        this.anneeNiveau = anneeNiveau;
    }

    public void setInfoAIEpouse(REAIXmlDataStructure infoAIEpouse) {
        this.infoAIEpouse = infoAIEpouse;
    }

    public void setInfoAIEpoux(REAIXmlDataStructure infoAIEpoux) {
        this.infoAIEpoux = infoAIEpoux;
    }

    public void setInfoBonification(REBonification09XmlDataStructure infoBonification) {
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

    public void setLimiteRevenu(String limiteRevenu) {
        this.limiteRevenu = limiteRevenu;
    }

    public void setMinimumGaranti(String minimumGaranti) {
        this.minimumGaranti = minimumGaranti;
    }

}
