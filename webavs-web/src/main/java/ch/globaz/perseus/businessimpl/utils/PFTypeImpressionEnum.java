package ch.globaz.perseus.businessimpl.utils;

/**
 * Enum permettant de définir le type d'impression que l'on désire.
 * 
 * @author dcl
 * 
 */
public enum PFTypeImpressionEnum {
    COPIES("PDF_PF_FACTURE_COPIES"),
    COPIES_AUX_REQUERANTS("PDF_PF_FACTURE_COPIES_AUX_REQUERANTS"),
    ORIGINAUX("PDF_PF_FACTURE_ORIGINAUX");

    // Contient la chaine passé en paramètre lors de la création d'un enum
    private String name = "";

    PFTypeImpressionEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
