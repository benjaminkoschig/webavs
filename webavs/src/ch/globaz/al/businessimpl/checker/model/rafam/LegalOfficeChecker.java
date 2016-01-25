package ch.globaz.al.businessimpl.checker.model.rafam;

import ch.globaz.al.business.constantes.ALConstRafam;

public class LegalOfficeChecker {

    public static boolean isValid(String legalOffice) {

        boolean isValid = legalOffice.matches(ALConstRafam.PATTERN_LEGAL_OFFICE);

        return isValid;
    }
}
