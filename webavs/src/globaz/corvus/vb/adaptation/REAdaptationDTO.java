package globaz.corvus.vb.adaptation;

import java.io.Serializable;

public class REAdaptationDTO implements Serializable {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeAug = "";
    private String codePrestation = "";
    private String montantAdapte = "";
    private String typeAdaptation = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REAdaptationDTO.
     */
    public REAdaptationDTO() {
    }

    /**
     * Crée une nouvelle instance de la classe REAdaptationDTO.
     * 
     * @param paramDTO
     */
    public REAdaptationDTO(REAdaptationDTO paramDTO) {
        typeAdaptation = paramDTO.typeAdaptation;
        codePrestation = paramDTO.codePrestation;
        anneeAug = paramDTO.anneeAug;
        montantAdapte = paramDTO.montantAdapte;

    }

    // ~ Methods
    // -------------------------------------------------------------------------------------------------------

    public String getAnneeAug() {
        return anneeAug;
    }

    public String getCodePrestation() {
        return codePrestation;
    }

    public String getMontantAdapte() {
        return montantAdapte;
    }

    public String getTypeAdaptation() {
        return typeAdaptation;
    }

    public void setAnneeAug(String anneeAug) {
        this.anneeAug = anneeAug;
    }

    public void setCodePrestation(String codePrestation) {
        this.codePrestation = codePrestation;
    }

    public void setMontantAdapte(String montantAdapte) {
        this.montantAdapte = montantAdapte;
    }

    public void setTypeAdaptation(String typeAdaptation) {
        this.typeAdaptation = typeAdaptation;
    }

}
