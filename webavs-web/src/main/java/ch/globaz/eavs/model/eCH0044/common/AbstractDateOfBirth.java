package ch.globaz.eavs.model.eCH0044.common;

import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.EAVSAbstractModel;
import ch.globaz.eavs.model.EAVSNonFinalNode;
import ch.globaz.eavs.model.eCH0044.v1.Year;
import ch.globaz.eavs.model.eCH0044.v1.YearMonth;
import ch.globaz.eavs.model.eCH0044.v1.YearMonthDay;

public abstract class AbstractDateOfBirth extends Ech0044Model implements EAVSNonFinalNode {
    public abstract Year getYear();

    public abstract YearMonth getYearMonth();

    public abstract YearMonthDay getYearMonthDay();

    public abstract void setYear(EAVSAbstractModel _YearMonthDay);

    public abstract void setYearMonth(EAVSAbstractModel _YearMonthDay);

    public abstract void setYearMonthDay(EAVSAbstractModel _YearMonthDay);

    @Override
    public void validate() throws EAVSInvalidXmlFormatException {

    }

}
