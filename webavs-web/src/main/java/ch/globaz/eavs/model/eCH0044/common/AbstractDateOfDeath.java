package ch.globaz.eavs.model.eCH0044.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSFinalNode;
import ch.globaz.eavs.model.eCH0044.v1.Year;
import ch.globaz.eavs.model.eCH0044.v1.YearMonth;
import ch.globaz.eavs.model.eCH0044.v1.YearMonthDay;

public abstract class AbstractDateOfDeath extends Ech0044Model implements EAVSFinalNode {

    private String value = null;

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    public abstract YearMonthDay getYearMonthDay();

    public abstract void setYearMonthDay(EAVSAbstractModel _YearMonthDay);

    public abstract YearMonth getYearMonth();

    public abstract void setYearMonth(EAVSAbstractModel _YearMonthDay);

    public abstract Year getYear();

    public abstract void setYear(EAVSAbstractModel _YearMonthDay);
}
