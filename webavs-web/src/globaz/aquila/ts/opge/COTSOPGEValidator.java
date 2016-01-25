package globaz.aquila.ts.opge;

import globaz.aquila.ts.COAbstractTSValidator;
import globaz.aquila.ts.rules.COTSOPGERule;

public class COTSOPGEValidator extends COAbstractTSValidator {

    public COTSOPGEValidator() {
        addValidationRule(new COTSOPGERule());
    }

}
