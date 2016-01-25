package ch.globaz.pegasus.businessimpl.services.models.lot;

/**
 * Enum d�finissant les types de comptabilisation de lots pour les PC.
 * 
 * @author sce
 * 
 * @version 1.11.12(CCJU), 1.12 Inforom
 */
public enum PCTypeLot {

    DECISION("52833001"),
    DEBLOCAGE("52833004");

    private String codeSysteme = null;

    PCTypeLot(String codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    public String getCodeSysteme() {
        return codeSysteme;
    }

    /**
     * Retourne le Type de Lot en fonction du code syst�me pass� en param�tre
     * 
     * @param cs, le codeSyts�me a mapper avec le type
     * @return un instance de PCTypeLot
     */
    static PCTypeLot getTypeByCs(String cs) {

        if (cs == null) {
            throw new IllegalArgumentException("The cs passed to map the enum PCTypeLot cannot be null");
        }

        for (PCTypeLot type : PCTypeLot.values()) {
            if (type.getCodeSysteme().equals(cs)) {
                return type;
            }
        }
        throw new IllegalArgumentException(
                "The cs passed to map the enum PCTypeLot doesn't exist in the value of the enum [cs: " + cs + "]");
    }

}
