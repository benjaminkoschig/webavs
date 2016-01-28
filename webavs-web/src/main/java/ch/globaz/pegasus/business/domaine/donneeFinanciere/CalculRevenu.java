package ch.globaz.pegasus.business.domaine.donneeFinanciere;

import ch.globaz.common.domaine.Montant;
import ch.globaz.common.domaine.Part;

public final class CalculRevenu {

    private CalculRevenu() {

    }

    public static Montant calcul(ProprieteType proprieteType, Part part, Montant montant) {
        if (proprieteType.isProprietaire()) {
            return montant.multiply(part);
        }
        return Montant.ZERO_ANNUEL;
    }
}
