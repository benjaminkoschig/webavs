package ch.globaz.pegasus.rpc.businessImpl.sedex;

import globaz.jade.jaxb.JAXBUtil;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.io.FileUtils;
import ch.globaz.common.exceptions.CommonTechnicalException;
import ch.globaz.common.exceptions.ValidationException;
import ch.globaz.naos.ree.tools.InfoCaisse;

public class AbstractSedex<T> implements Closeable {

    protected final SedexInfo sedexInfo;
    protected final InfoCaisse infoCaisse;
    private final JaxbHandler<T> jaxbHandler;
    private final SedexSender sedexSender;

    /**
     * Postfix des fichiés généré. Identique au postfix Jade mais étant privé...
     */
    protected static final String MARSHALLED_XML_EXT = ".xml";
    protected static final String FILENAME_SEPARATOR = "-";
    protected static final BigInteger IMPLEMENTED_MESSAGE_MINOR_VERSION = BigInteger.valueOf(5);

    protected AbstractSedex(SedexSender sedexSender, InfoCaisse infoCaisse, Class<T> clazz, String xsdFileName,
            SedexInfo sInfo) {
        this.infoCaisse = infoCaisse;
        this.sedexSender = sedexSender;
        jaxbHandler = JaxbHandler.build(xsdFileName, clazz);
        sedexInfo = sInfo;
    }

    protected AbstractSedex(SedexSender sedexSender, String directoryName, InfoCaisse infoCaisse, Class<T> clazz,
            String xsdFileName) {
        this.infoCaisse = infoCaisse;
        this.sedexSender = sedexSender;
        jaxbHandler = JaxbHandler.build(xsdFileName, clazz);
        sedexInfo = sedexSender.buildSedexInfos(directoryName);
    }

    protected File marshallCompleteMessage(T element, String filename) throws ValidationException {
        return jaxbHandler.marshall(element, filename);
    }

    protected void validateByXsd(T element) throws ValidationException {
        jaxbHandler.validate(element);
    }

    protected XMLGregorianCalendar toDay() {
        XMLGregorianCalendar xmlCalendar;
        try {
            xmlCalendar = JAXBUtil.getXmlCalendarTimestamp();
        } catch (DatatypeConfigurationException e) {
            throw new CommonTechnicalException(e);
        }
        xmlCalendar.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
        return xmlCalendar;
    }

    protected void sendMessages(int messageType) {
        this.sedexSender.sendMessages(sedexInfo, messageType);
    }

    @Override
    public void close() throws IOException {
        FileUtils.deleteDirectory(new File(this.sedexInfo.getPersistenceDir()));
    }
}
