package ch.globaz.pegasus.rpc.businessImpl.converter;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ch.globaz.pegasus.rpc.businessImpl.RpcTechnicalException;

public class XmlConverters {
    /**
     * Méthode permettant la conversion une date Globaz dans le format {@link XMLGregorianCalendar }
     * 
     * @param yyyyMMdd Une string représentant la date sous le format yyyyMMdd
     * @return Retourne une date sous le format {@link XMLGregorianCalendar }
     * @throws DatatypeConfigurationException
     * @throws RpcBusinessException
     */
    public static XMLGregorianCalendar convertDateToXMLGregorianCalendar(Date date) {
        XMLGregorianCalendar returnCalendar = null;
        try {
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(date);
            returnCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
            returnCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
        } catch (DatatypeConfigurationException e) {
            throw new RpcTechnicalException(e);
        }
        return returnCalendar;
    }

    /**
     * Méthode permettant la conversion une date Globaz dans le format {@link XMLGregorianCalendar }
     */
    public static XMLGregorianCalendar convertDateToXMLGregorianCalendar(ch.globaz.common.domaine.Date date) {
        return convertDateToXMLGregorianCalendar(date.getDate());
    }
}
