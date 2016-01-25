package ch.globaz.al.business.constantes.enumerations;

import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.i18n.JadeI18n;
import ch.globaz.al.business.exceptions.rafam.ALRafamException;

/**
 * 
 * @author jts
 * @comment si une valeur est modifié ici, reporter cette modification dans droit.js ! (GMO)
 */
public enum RafamFamilyAllowanceType {

    /** Type d'allocation pour une ADC */
    ADC("30"),

    /** Type d'allocation pour une ADI */
    ADI("31"),

    /** Type d'allocation pour une adoption */
    ADOPTION("02"),

    /** Type d'allocation pour un enfant IJAI */
    ALLOCATION_ENFANT_IJAI("32"),

    /** Type d'allocation pour une allocation difference à l'adoption */
    DIFFERENCE_ADOPTION("04"),

    /** Type d'allocation pour une allocation difference à la naissance */
    DIFFERENCE_NAISSANCE("03"),

    /** Type d'allocation pour un enfant */
    ENFANT("10"),

    /** Type d'allocation pour un enfant avec supplément */
    ENFANT_AVEC_SUPPLEMENT("11"),

    /** Type d'allocation pour un enfant en incapacité */
    ENFANT_EN_INCAPACITE("12"),

    ENFANT_EN_INCAPACITE_AVEC_SUPPLEMENT("13"),

    /** Type d'allocation pour une formation */
    FORMATION("20"),

    /** Type d'allocation pour une formation anticipé */
    FORMATION_ANTICIPEE("22"),

    /** Type d'allocation pour une formation anticipé avec supplément */
    FORMATION_ANTICIPEE_AVEC_SUPPLEMENT("23"),

    /** Type d'allocation pour une formation avec supplément */
    FORMATION_AVEC_SUPPLEMENT("21"),

    /** Type d'allocation pour une naissance */
    NAISSANCE("01");

    /**
     * Retourne une instance de l'enum en fonction du type d'allocation
     * 
     * @param familyAllowanceType
     * @return
     * @throws JadeApplicationException
     */
    public static RafamFamilyAllowanceType getFamilyAllowanceType(String code) throws JadeApplicationException {

        switch (Integer.parseInt(code)) {
            case 1:
                return RafamFamilyAllowanceType.NAISSANCE;
            case 2:
                return RafamFamilyAllowanceType.ADOPTION;
            case 3:
                return RafamFamilyAllowanceType.DIFFERENCE_NAISSANCE;
            case 4:
                return RafamFamilyAllowanceType.DIFFERENCE_ADOPTION;
            case 10:
                return RafamFamilyAllowanceType.ENFANT;
            case 11:
                return RafamFamilyAllowanceType.ENFANT_AVEC_SUPPLEMENT;
            case 12:
                return RafamFamilyAllowanceType.ENFANT_EN_INCAPACITE;
            case 13:
                return RafamFamilyAllowanceType.ENFANT_EN_INCAPACITE_AVEC_SUPPLEMENT;
            case 20:
                return RafamFamilyAllowanceType.FORMATION;
            case 21:
                return RafamFamilyAllowanceType.FORMATION_AVEC_SUPPLEMENT;
            case 22:
                return RafamFamilyAllowanceType.FORMATION_ANTICIPEE;
            case 23:
                return RafamFamilyAllowanceType.FORMATION_ANTICIPEE_AVEC_SUPPLEMENT;
            case 30:
                return RafamFamilyAllowanceType.ADC;
            case 31:
                return RafamFamilyAllowanceType.ADI;
            case 32:
                return RafamFamilyAllowanceType.ALLOCATION_ENFANT_IJAI;
            default:
                throw new ALRafamException(
                        "RafamFamilyAllowanceType#getFamilyAllowanceType : this type is not supported");
        }
    }

    /** Code de la centrale */
    private String codeCentrale;

    RafamFamilyAllowanceType(String codeCentrale) {
        this.codeCentrale = codeCentrale;
    }

    public String getCode() {
        return String.valueOf(Integer.parseInt(getCodeCentrale()));
    }

    public String getCodeCentrale() {
        return codeCentrale;
    }

    public String getCodeLibelle() {
        return "al.enum.rafam.familyAllowanceType.code" + getCodeCentrale();
    }

    public String getLibelle() {
        return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), getCodeLibelle());
    }
}
