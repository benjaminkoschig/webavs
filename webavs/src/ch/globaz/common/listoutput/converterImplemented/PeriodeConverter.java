package ch.globaz.common.listoutput.converterImplemented;

import java.util.Locale;
import ch.globaz.common.domaine.Periode;
import ch.globaz.simpleoutputlist.converter.Converter;

public class PeriodeConverter implements Converter<Periode, String> {

    @Override
    public String getValue(Periode periode, Locale local) {
        if (periode != null) {
            return periode.getDateDebut() + " - " + periode.getDateFin();
        }
        return null;
    }

    @Override
    public Class<Periode> getType() {
        return Periode.class;
    }

}
