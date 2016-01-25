/**
 * 
 */
package ch.globaz.al.business.constantes.enumerations.droit;

/**
 * @author sel
 * 
 */
public enum ALEnumMsgDroitPC {

    allocataireBeneficiePC("WARNING_ALLOCATAIRE_BENEFICIE_PC"),
    enfantBeneficiePC("WARNING_ENFANT_BENEFICIE_PC");

    // id label des messages à afficher
    protected String message;

    /** Constructeur */
    ALEnumMsgDroitPC(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
