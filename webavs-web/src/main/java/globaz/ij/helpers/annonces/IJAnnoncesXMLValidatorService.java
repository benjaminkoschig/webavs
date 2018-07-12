package globaz.ij.helpers.annonces;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import ch.admin.zas.pool.PoolMeldungZurZAS;
import ch.admin.zas.rc.IVTaggelderMeldungType;
import ch.admin.zas.rc.PoolFussType;
import ch.admin.zas.rc.PoolKopfType;
import ch.globaz.common.exceptions.ValidationException;
import ch.globaz.common.properties.PropertiesException;
import globaz.globall.util.JACalendar;
import globaz.ij.properties.IJProperties;
import globaz.jade.client.util.JadeFilenameUtil;
import globaz.jade.common.Jade;

/**
 * @author ebko
 * 
 */

public class IJAnnoncesXMLValidatorService {
    
    private static final Logger LOG = LoggerFactory.getLogger(IJAnnoncesXMLValidatorService.class);
    
    private static final String XSD_FOLDER = "/xsd/P2020/annoncesRC/";
    private static final String XSD_NAME = "MeldungZurZas.xsd";
    
    private static IJAnnoncesXMLValidatorService instance = new IJAnnoncesXMLValidatorService();

    public static IJAnnoncesXMLValidatorService getInstance() {

        return instance;
    }
    
    private transient Marshaller marshaller;
    
    private Marshaller initMarshaller(Object element) throws SAXException, JAXBException {
        if (marshaller == null) {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            URL url = getClass().getResource(XSD_FOLDER + XSD_NAME);
            Schema schema = sf.newSchema(url);

            JAXBContext jc = JAXBContext.newInstance(element.getClass());

            marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setSchema(schema);
        }
        return marshaller;
    }
    
    /**
     * Méthode qui retourne un nom de fichier basé sur le timestamp
     * 
     * @return
     * @throws Exception
     */
    private String getFileNameTimeStamp() throws Exception {
        String fileName = "M_" + IJProperties.RACINE_NOM_FICHIER_OUTPUT_ZAS.getValue();
        fileName = JadeFilenameUtil.addFilenameSuffixDateTimeDecimals(fileName);
        fileName = StringUtils.left(fileName, fileName.length() - 7) + "_" + StringUtils.right(fileName, 7);
        fileName = fileName + ".xml";
        return fileName;
    }
    
    /**
     * 
     * @param poolKopfTest if you need to set TEST flag into header
     * @param poolKopfSender
     * @return
     * @throws PropertiesException
     * @throws ParseException
     * @throws DatatypeConfigurationException
     */
    private PoolMeldungZurZAS.Lot initPoolMeldungZurZASLot(boolean poolKopfTest, String poolKopfSender)
            throws PropertiesException, ParseException, DatatypeConfigurationException {
        ch.admin.zas.pool.ObjectFactory factoryPool = new ch.admin.zas.pool.ObjectFactory();
        ch.admin.zas.rc.ObjectFactory factoryType = new ch.admin.zas.rc.ObjectFactory();
        ch.admin.zas.pool.PoolMeldungZurZAS.Lot lot = factoryPool.createPoolMeldungZurZASLot();
        PoolKopfType poolKopf = factoryType.createPoolKopfType();
        if (poolKopfTest) {
            poolKopf.setTest("TEST");
        }
        poolKopf.setSender(poolKopfSender);

        final DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        final String dateStr = JACalendar.todayJJsMMsAAAA();
        final java.util.Date dDate = format.parse(dateStr);

        GregorianCalendar gregory = new GregorianCalendar();
        gregory.setTime(dDate);

        XMLGregorianCalendar dealCloseDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
        poolKopf.setErstellungsdatum(dealCloseDate);
        lot.setPoolKopf(poolKopf);

        PoolFussType poolFuss = factoryType.createPoolFussType();
        poolFuss.setEintragungengesamtzahl(0);
        lot.setPoolFuss(poolFuss);

        return lot;
    }
    
    /**
     * CAUTION, only element choice of a PoolMeldungZurZAS.Lot
     * 
     * possible object are element of Lot {@link PoolMeldungZurZAS.Lot }
     * 
     * @param element : must be an element to put on a PoolMeldungZurZAS.Lot
     * @throws ValidationException
     * @throws SAXException
     * @throws JAXBException
     */
    public void validateUnitMessage(IVTaggelderMeldungType element) throws ValidationException, SAXException, JAXBException {
        PoolMeldungZurZAS pool;
        final List<String> validationErrors = new LinkedList<String>();
        try {
            ch.admin.zas.pool.ObjectFactory factoryPool = new ch.admin.zas.pool.ObjectFactory();
            pool = factoryPool.createPoolMeldungZurZAS();
            PoolMeldungZurZAS.Lot lot = initPoolMeldungZurZASLot(true, "validateUnitMessage");
            lot.getVAIKMeldungNeuerVersicherterOrVAIKMeldungAenderungVersichertenDatenOrVAIKMeldungVerkettungVersichertenNr()
                    .add(element);
            pool.getLot().add(lot);
            initMarshaller(pool);

            marshaller.setEventHandler(new ValidationEventHandler() {

                @Override
                public boolean handleEvent(ValidationEvent event) {
                    LOG.warn("JAXB validation error : " + event.getMessage(), this);
                    validationErrors.add(event.getMessage());
                    return true;
                }

            });

            marshaller.marshal(pool, new ByteArrayOutputStream());
            
        } catch (JAXBException exception) {
            LOG.error("JAXB validation has thrown a JAXBException : " + exception.toString(), exception);
            throw exception;
        } catch (Exception e) {
            LOG.error("impossible d'initialier un PoolMeldungZurZAS", e);
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

    }
    
    /**
     * Méthode qui génère le fichier en fonction d'un lot d'annonces en input
     * 
     * @return l'uri du fichier généré
     * @throws Exception
     */
    public String genereFichier(PoolMeldungZurZAS.Lot lotAnnonce, int size) throws Exception {
        String fileName;
        ch.admin.zas.pool.ObjectFactory factoryPool = new ch.admin.zas.pool.ObjectFactory();
        PoolMeldungZurZAS pool = factoryPool.createPoolMeldungZurZAS();
        pool.getLot().add(lotAnnonce);
        lotAnnonce.getPoolFuss().setEintragungengesamtzahl(size);
        initMarshaller(pool);
        fileName = Jade.getInstance().getSharedDir() + getFileNameTimeStamp();

        File f = new File(fileName);
        boolean canCreateFile = f.createNewFile();
        if (!canCreateFile) {
            throw new Exception("Unable to create file : " + fileName);
        }

        try {
            marshaller.setEventHandler(new ValidationEventHandler() {

                @Override
                public boolean handleEvent(ValidationEvent event) {
                    LOG.warn("JAXB validation error : " + event.getMessage(), this);
                    return false;
                }
            });
            marshaller.marshal(pool, f);

        } catch (JAXBException exception) {
            LOG.error("JAXB validation has thrown a JAXBException : " + exception.toString(), exception);
            throw exception;

        }
        return fileName;

    }

}
