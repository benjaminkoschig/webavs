package ch.globaz.eavs.model.eCH0044.v1;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.eCH0044.common.AbstractDateOfDeath;

public class DateOfDeath extends AbstractDateOfDeath {
    private YearMonthDay yearMonthDay = null;
    private YearMonth yearMonth = null;
    private Year year = null;

    public List getChildren() {
        List result = new ArrayList();
        result.add(yearMonthDay);
        result.add(yearMonth);
        result.add(year);
        return result;
    }

    @Override
    public void setYearMonthDay(EAVSAbstractModel yearMonthDay) {
        this.yearMonthDay = (YearMonthDay) yearMonthDay;

    }

    @Override
    public YearMonthDay getYearMonthDay() {
        if (yearMonthDay == null) {
            yearMonthDay = new YearMonthDay();
        }
        return yearMonthDay;
    }

    @Override
    public void setYearMonth(EAVSAbstractModel yearMonth) {
        this.yearMonth = (YearMonth) yearMonth;

    }

    @Override
    public YearMonth getYearMonth() {
        if (yearMonth == null) {
            yearMonth = new YearMonth();
        }
        return yearMonth;
    }

    @Override
    public void setYear(EAVSAbstractModel year) {
        this.year = (Year) year;

    }

    @Override
    public Year getYear() {
        if (year == null) {
            year = new Year();
        }
        return year;
    }
}
