package ch.globaz.eform.constant;

import globaz.globall.db.BSession;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum GFDocumentTypeDossier {
    GENERAL("Général/Divers", "", "01.01", "01.01.01.02"),
    RENTE_AI("Rente AI", "", "01.03", "01.03.01.02"),
    RENTE_AVS("Rente AVS", "", "01.04", "01.04.01.02"),
    API_AI("API AI", "", "01.11", "01.11.01.02"),
    API_AVS("API AVS", "", "01.12", "01.12.01.02");

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


    public String getDesignation(BSession session) {
        return session.getLabel(code);
    }

    public static GFDocumentTypeDossier getStatusByDocumentType(String documentType) {
        return Arrays.stream(GFDocumentTypeDossier.values())
                .filter(type -> type.documentType.equals(documentType))
                .findFirst()
                .orElse(GENERAL);
    }

    public static GFDocumentTypeDossier getStatusByCode(String code) {
        return Arrays.stream(GFDocumentTypeDossier.values())
                .filter(type -> type.code.equals(code))
                .findFirst()
                .orElse(GENERAL);
    }
}
