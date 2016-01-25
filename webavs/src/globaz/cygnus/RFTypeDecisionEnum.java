package globaz.cygnus;

import globaz.cygnus.api.codesystem.IRFCatalogueTexte;

public enum RFTypeDecisionEnum {

    // Décisions ponctuelle
    RFM_DECISION_PONCTUELLE(true, IRFCatalogueTexte.CS_DECISION_PONCTUELLE, "RFDecisionPonctuelleOO", "idProcess"),

    // Décisions de régime avec excedent (octroi)
    RFM_DECISION_REGIME_AVEC_EXCEDENT_OCTROI(true, IRFCatalogueTexte.CS_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_OCTROI,
            "RFDecisionRegimeAvecExcedentOctroiOO", "idProcess"),

    // Décisions de régime avec excedent (refus)
    RFM_DECISION_REGIME_AVEC_EXCEDENT_REFUS(true, IRFCatalogueTexte.CS_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_REFUS,
            "RFDecisionRegimeAvecExcedentRefusOO", "idProcess"),

    // Décisions de régime sans excedent (octroi)
    RFM_DECISION_REGIME_SANS_EXCEDENT_OCTROI(true, IRFCatalogueTexte.CS_DECISION_MENSUELLE_REGIME_STANDARD_OCTROI,
            "RFDecisionRegimeSansExcedentOctroiOO", "idProcess"),

    // Décisions de régime sans excedent (refus)
    RFM_DECISION_REGIME_SANS_EXCEDENT_REFUS(false, "?", "?", "?"),

    // Décisions de restitution
    RFM_DECISION_RESTITUTION(true, IRFCatalogueTexte.CS_DECISION_RESTITUTION, "RFDecisionRestitutionOO", "idProcess");

    private final String csTypedocument;
    private final Boolean hasModel;
    private final String topazAttribute;
    private final String topazValue;

    // Constructeur
    RFTypeDecisionEnum(Boolean hasModel, String csTypeDocument, String valueTopaz, String attributeTopaz) {
        this.hasModel = hasModel;
        csTypedocument = csTypeDocument;
        topazValue = valueTopaz;
        topazAttribute = attributeTopaz;
    }

    public String getCsTypedocument() {
        return csTypedocument;
    }

    public Boolean getHasModel() {
        return hasModel;
    }

    public String getTopazAttribute() {
        return topazAttribute;
    }

    public String getTopazValue() {
        return topazValue;
    }

}
