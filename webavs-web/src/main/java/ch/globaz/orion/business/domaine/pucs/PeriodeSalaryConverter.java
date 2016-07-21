package ch.globaz.orion.business.domaine.pucs;

import java.util.Locale;
import ch.globaz.simpleoutputlist.converter.Converter;

public class PeriodeSalaryConverter implements Converter<PeriodeSalary, String> {

    @Override
    public Class<PeriodeSalary> getType() {
        return PeriodeSalary.class;
    }

    @Override
    public String getValue(PeriodeSalary periode, Locale locale) {
        if (periode != null) {
            return periode.getDateDebut().getSwissValue() + " - " + periode.getDateFin().getSwissValue();
        }
        return null;
    }

}
