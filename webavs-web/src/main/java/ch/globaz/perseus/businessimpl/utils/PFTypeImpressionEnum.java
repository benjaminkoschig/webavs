package ch.globaz.perseus.businessimpl.utils;

/**
 * Enum permettant de d�finir le type d'impression que l'on d�sire.
 * 
 * @author dcl
 * 
 */
public enum PFTypeImpressionEnum {
    COPIES("PDF_PF_FACTURE_COPIES"),
    COPIES_AUX_REQUERANTS("PDF_PF_FACTURE_COPIES_AUX_REQUERANTS"),
    ORIGINAUX("PDF_PF_FACTURE_ORIGINAUX");

    // Contient la chaine pass� en param�tre lors de la cr�ation d'un enum
    private String name = "";

    PFTypeImpressionEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
