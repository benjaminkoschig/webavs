package globaz.orion.process.adi;

public enum ADIControlesEnum {
    CONTROLE_DIFFERENCE_25_POURCENT("ADI_CONTROLE_DIFFERENCE_25_POURCENT", false),
    CONTROLE_ACTIVITE_ACCESSOIRE("ADI_CONTROLE_ACTIVITE_ACCESSOIRE", false),
    CONTROLE_CONTENTIEUX_EXISTANT("ADI_CONTROLE_CONTENTIEUX_EXISTANT", false),
    CONTROLE_IRRECOUVRABLE("ADI_CONTROLE_IRRECOUVRABLE", false),
    CONTROLE_PERTE_COMMERCIALE("ADI_CONTROLE_PERTE_COMMERCIALE", false),
    CONTROLE_TAXATION_EN_COURS("ADI_CONTROLE_TAXATION_EN_COURS", false),
    CONTROLE_COMMUNICATION_FISCALE_RECUE("ADI_CONTROLE_COMMUNICATION_FISCALE_RECUE", false),
    CONTROLE_PAS_DE_COTISATION_AVS("ADI_CONTROLE_PAS_DE_COTISATION_AVS", false),
    CONTROLE_DIN_1181("ADI_CONTROLE_DIN_1181", false),
    CONTROLE_REDUCTION("ADI_CONTROLE_REDUCTION", false),
    CONTROLE_REMISE("ADI_CONTROLE_REMISE", false),
    CONTROLE_PLUSIEURS_PERIODES_DANS_ANNEE("ADI_CONTROLE_PLUSIEURS_PERIODES_DANS_ANNEE", false),
    CONTROLE_REMARQUE_TRANSMISE("ADI_CONTROLE_REMARQUE_TRANSMISE", false),
    CONTROLE_ERREUR_INCONNUE("ADI_CONTROLE_ERREUR_INCONNUE", false);

    private ADIControlesEnum(String label, Boolean isCritical) {
        this.label = label;
        this.isCritical = isCritical;
    }

    private String label;
    private Boolean isCritical;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean isCritical() {
        return isCritical;
    }

    public void setIsCritical(Boolean isCritical) {
        this.isCritical = isCritical;
    }

}
