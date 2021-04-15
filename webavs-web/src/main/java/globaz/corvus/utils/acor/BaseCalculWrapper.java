package globaz.corvus.utils.acor;

import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.rentesaccordees.RERenteAccordee;

import java.util.List;

public class BaseCalculWrapper extends CleWrapper {
    private REBasesCalcul basesCalcul;
    private List<RERenteAccordee> renteAccordees;

    public BaseCalculWrapper(REBasesCalcul basesCalcul, List<RERenteAccordee> renteAccordees) {
        super(basesCalcul, renteAccordees);
        this.basesCalcul = basesCalcul;
        this.renteAccordees = renteAccordees;
    }

    public REBasesCalcul getBasesCalcul() {
        return basesCalcul;
    }

    public boolean hasCodeCasSpecial08() {
        for (RERenteAccordee ra : renteAccordees) {
            if (hasCodeCasSpecial(ra, "08")) {
                return true;
            }
        }
        return false;
    }
}
