package globaz.corvus.annonce.reader;

import static org.junit.Assert.*;
import globaz.globall.util.JADate;
import org.junit.Test;

/**
 * S'occupe de tester les méthodes de la classe abstraite REAbstractDataBaseValueReader
 * 
 * @author lga
 * 
 */
public class REAbstractDataBaseValueReaderTest {

    /**
     * Simple class qui étends les méthodes de la classe abstraite afin de pouvoir les tester
     * 
     * @author lga
     * 
     */
    class REBentityValueReader extends REAbstractBEntityValueReader {
        public JADate toJADate(String value) {
            return this.convertToJADate(value);
        }

        public Boolean toBoolean(String value) {
            return this.convertToBoolean(value);
        }

        public Integer toDate(String value) {
            return this.convertToInteger(value);
        }
    }

    @Test
    public void convertToInteger() {
        REBentityValueReader reader = new REBentityValueReader();
        assertTrue(null == reader.convertToInteger(null));
        assertTrue(null == reader.convertToInteger(""));
        assertTrue(null == reader.convertToInteger("A"));
        assertTrue(null == reader.convertToInteger(" "));
        assertTrue(0 == reader.convertToInteger("0"));
        assertTrue(1 == reader.convertToInteger("1"));
        assertTrue(695478 == reader.convertToInteger("695478"));
    }

    @Test
    public void convertToBoolean() {
        REBentityValueReader reader = new REBentityValueReader();
        assertTrue(null == reader.convertToBoolean(null));
        assertTrue(null == reader.convertToBoolean(""));
        assertTrue(null == reader.convertToBoolean("A"));
        assertTrue(null == reader.convertToBoolean(" "));
        assertTrue(Boolean.FALSE == reader.convertToBoolean("0"));
        assertTrue(Boolean.TRUE == reader.convertToBoolean("1"));
    }

    /**
     * Formats de date acceptés
     * dd.mm.yyyy
     * ddmmyyyy
     * mm.yyyy
     * mmyyyy
     * yyyy
     * 
     */
    @Test
    public void convertToJADate() {
        REBentityValueReader reader = new REBentityValueReader();
        assertTrue(null == reader.convertToJADate(null));
        assertTrue(null != reader.convertToJADate("2012"));
        assertTrue(null != reader.convertToJADate("112012"));
        assertTrue(null != reader.convertToJADate("11.2012"));
        assertTrue(null != reader.convertToJADate("01012013"));
        assertTrue(null != reader.convertToJADate("01.01.2013"));
        try {
            reader.convertToJADate("A");
        } catch (IllegalArgumentException e) {

        }
        try {
            reader.convertToJADate("010120133");
        } catch (IllegalArgumentException e) {

        }
        try {
            reader.convertToJADate("215");
        } catch (IllegalArgumentException e) {

        }
        try {
            reader.convertToJADate("salut");
        } catch (IllegalArgumentException e) {

        }
    }
}
