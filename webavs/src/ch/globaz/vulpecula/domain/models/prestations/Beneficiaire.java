package ch.globaz.vulpecula.domain.models.prestations;

import java.util.Arrays;
import java.util.List;
import ch.globaz.exceptions.ExceptionMessage;
import ch.globaz.exceptions.GlobazTechnicalException;

public enum Beneficiaire {
    EMPLOYEUR(68006001),
    NOTE_CREDIT(68006002),
    TRAVAILLEUR(68006003),
    AUCUN_VERSEMENT(68006004),
    PRESTATION_VERSEE_PAR_CAISSE(68006005),
    VERSEMEMENT_A_UN_TIERS(68006006),
    INCONNU(68006007);

    private int codeSysteme;

    private Beneficiaire(int codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * Construit une instance valide de <code>Beneficiaire</code> associ�e au code syst�me pass� en
     * param�tre.
     * 
     * @param value un code syst�me valide repr�sentant le b�n�ficiaire du cong� pay�
     * @return une instance valide de <code>Beneficiaire</code>
     * @throws GlobazTechnicalException si le code syst�me pass� en param�tre n'est pas valide
     */
    public static Beneficiaire fromValue(String value) {
        Integer intValue;
        try {
            intValue = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new GlobazTechnicalException(ExceptionMessage.INVALID_VALUE_CODE_SYSTEME, e);
        }

        for (Beneficiaire beneficiaire : Beneficiaire.values()) {
            if (beneficiaire.codeSysteme == intValue) {
                return beneficiaire;
            }
        }

        throw new IllegalArgumentException("La valeur " + value + " ne correspond � aucun type de b�n�ficiaire connu");
    }

    /**
     * Retourne un <code>String</code> repr�sentant le code syst�me associ�.
     * 
     * @return un <code>String</code> repr�sentant le code syst�me associ�.
     */
    public String getValue() {
        return String.valueOf(codeSysteme);
    }

    public static List<String> getBeneficiairesForAJ() {
        return Arrays.asList(EMPLOYEUR.getValue(), NOTE_CREDIT.getValue());
    }

    public static List<String> getBeneficiairesForCP() {
        return Arrays.asList(EMPLOYEUR.getValue(), TRAVAILLEUR.getValue(), NOTE_CREDIT.getValue());
    }

    public static List<String> getBeneficiairesForSM() {
        return Arrays.asList(EMPLOYEUR.getValue(), TRAVAILLEUR.getValue(), NOTE_CREDIT.getValue());
    }
}
