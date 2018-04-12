package ch.globaz.orion.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class DateConverter {
    private static final String FORMAT_DATE_DD_MM_YYYY = "dd.MM.yyyy";

    /**
     * Parse une date au format String jj.mm.aaaa en un XMLGregorianCalendar
     * 
     * @param dateString
     * @return
     * @throws ParseException
     * @throws DatatypeConfigurationException
     */
    public static XMLGregorianCalendar stringDateToXmlGregorianCalendar(String dateString)
            throws DatatypeConfigurationException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATE_DD_MM_YYYY);
        return toXMLDate(sdf.parse(dateString));
    }

    /**
     * Parse une date au format Date en XMLGregorianCalendar
     * 
     * @param date
     * @return
     * @throws DatatypeConfigurationException
     */
    public static XMLGregorianCalendar toXMLDate(Date date) throws DatatypeConfigurationException {
        if (date == null) {
            return null;
        }
        GregorianCalendar gCalendar = new GregorianCalendar();
        gCalendar.setTime(date);
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
    }
}
