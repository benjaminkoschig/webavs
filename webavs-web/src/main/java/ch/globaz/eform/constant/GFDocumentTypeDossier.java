package ch.globaz.eform.constant;

import globaz.globall.db.BSession;
import lombok.Getter;

import java.util.Arrays;

public enum GFDocumentTypeDossier {
    GENERAL("TYPE_GENERAL", "", "01.01", "01.01.01.02"),
    RENTE_AI("TYPE_RENTE_AI", "", "01.03", "01.03.01.02"),
    RENTE_AVS("TYPE_RENTE_AVS", "", "01.04", "01.04.01.02"),
    API_AI("TYPE_API_AI", "", "01.11", "01.11.01.02"),
    API_AVS("TYPE_API_AVS", "", "01.12", "01.12.01.02");

    String label;
    String code;
    String documentType;
    String documentTypeLead;

    GFDocumentTypeDossier(String label, String code, String documentType, String documentTypeLead) {
        this.label = label;
        this.code = code;
        this.documentType = documentType;
        this.documentTypeLead = documentTypeLead;
    }

    public String getLabel(BSession session) {
        return session.getLabel(this.label);
    }

    public String getCode() {
        return code;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getDocumentTypeLead() {
        return documentTypeLead;
    }

    public String getDesignation(BSession session) {
        return session.getLabel(code);
    }

    public static GFDocumentTypeDossier getDocumentTypeDossierByDocumentType(String documentType) {
        return Arrays.stream(GFDocumentTypeDossier.values())
                .filter(type -> type.documentType.equals(documentType))
                .findFirst()
                .orElse(GENERAL);
    }
}
