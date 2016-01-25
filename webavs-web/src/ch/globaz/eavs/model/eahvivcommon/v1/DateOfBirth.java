package ch.globaz.eavs.model.eahvivcommon.v1;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.eCH0044.common.AbstractYearMonthDay;
import ch.globaz.eavs.model.eCH0044.v1.YearMonthDay;
import ch.globaz.eavs.model.eahvivcommon.common.AbstractDateOfBirth;

public class DateOfBirth extends AbstractDateOfBirth {
    private YearMonthDay yearMonthDay = null;

    @Override
    public List getChildren() {
        List result = new ArrayList();
        result.add(yearMonthDay);
        return result;
    }

    @Override
    public AbstractYearMonthDay getYearMonthDay() {
        if (yearMonthDay == null) {
            yearMonthDay = new YearMonthDay();
        }
        return yearMonthDay;
    }

    public void setYearMonthDay(EAVSAbstractModel _yearMonthDay) {
        yearMonthDay = (YearMonthDay) _yearMonthDay;
    }
}
