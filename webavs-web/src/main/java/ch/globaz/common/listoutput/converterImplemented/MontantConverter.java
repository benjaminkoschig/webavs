package ch.globaz.common.listoutput.converterImplemented;

import java.util.Locale;
import ch.globaz.common.domaine.Montant;
import ch.globaz.simpleoutputlist.converter.Converter;

public class MontantConverter implements Converter<Montant, String> {

    @Override
    public String getValue(Montant montant, Locale local) {
        if (montant != null) {
            if (montant.isZero()) {
                return "0";
            } else {
                return montant.toStringFormat();
            }
        }
        return null;
    }

    @Override
    public Class<Montant> getType() {
        return Montant.class;
    }

}
