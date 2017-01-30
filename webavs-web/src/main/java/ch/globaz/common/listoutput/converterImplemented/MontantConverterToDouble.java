package ch.globaz.common.listoutput.converterImplemented;

import java.util.Locale;
import ch.globaz.common.domaine.Montant;
import ch.globaz.simpleoutputlist.converter.Converter;

public class MontantConverterToDouble implements Converter<Montant, Double> {

    @Override
    public Double getValue(Montant montant, Locale locale) {
        if (montant != null) {
            if (montant.isZero()) {
                return 0.00;
            } else {
                return montant.getValueDouble();
            }
        }
        return null;
    }

    @Override
    public Class<Montant> getType() {
        return Montant.class;
    }

}
