package ch.globaz.eavs.model.eahviv2010000101.v3;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.eCH0044.common.AbstractYearMonthDay;
import ch.globaz.eavs.model.eCH0044.v1.YearMonthDay;
import ch.globaz.eavs.model.eahviv2010000101.common.AbstractDateOfBirth;

public class DateOfBirth extends AbstractDateOfBirth {
    private YearMonthDay yearMonthDay = null;

    @Override
    public AbstractYearMonthDay getYearMonthDay() {
        if (yearMonthDay == null) {
            yearMonthDay = new YearMonthDay();
        }
        return yearMonthDay;
    }

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(yearMonthDay);
        return result;
    }
}
