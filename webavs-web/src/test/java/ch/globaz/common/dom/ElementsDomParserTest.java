package ch.globaz.common.dom;

import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ElementsDomParserTest {
    private ElementsDomParser domParser;

    @Before
    public void build() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        String xml = "<?xml version='1.0' encoding='UTF-8'?><un><special>&amp;&lt;&gt;</special>valueUn<unUn><unUnUn>Ok</unUnUn><nullBalise>null</nullBalise><unUnUn>Ok2</unUnUn></unUn><date>2013-02-31</date><trim> trim  </trim><listEmpty><empty></empty><empty></empty></listEmpty></un>";
        InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8")));
        Document document = documentBuilder.parse(inputSource);
        domParser = new ElementsDomParser(document);

    }

    @Test
    public void testFind() throws Exception {
        assertEquals(1, domParser.find("un").getResult().size());
        assertEquals(1, domParser.find("un unUn").getResult().size());
        assertEquals(1, domParser.find("un unUn unUnUn").getResult().size());
    }

    @Test
    public void testGetFirstNodNotFound() throws Exception {
        assertNull(domParser.find("uneueue").getFirstNode());
    }

    @Test
    public void testGetValues() throws Exception {
        assertEquals(2, domParser.find("un unUn unUnUn").getValues().size());
    }

    @Test
    public void testGetValueTrim() throws Exception {
        assertEquals("trim", domParser.find("trim").getFirstValue());
    }

    @Test
    public void testGetFirstValue() throws Exception {
        assertEquals("Ok", domParser.find("un unUn unUnUn").getFirstValue());
        assertEquals(null, domParser.find("un unUn nullBalise").getFirstValue());
    }

    @Test
    public void testGetFirstValueSpecialChar() throws Exception {
        assertEquals("&<>", domParser.find("special").getFirstValue());
    }

    @Test
    public void testGetFirstValueNotFound() throws Exception {
        assertEquals(null, domParser.find("un1 unUn unUnUn").getFirstValue());
    }

    @Test
    public void testGetValueAsDateAndCorrectItIfNeed() throws Exception {
        assertEquals("28.02.2013", domParser.find("date").getValueAsDateAndCorrectItIfNeed().getSwissValue());
    }

    @Test
    public void testConvertAsDateAndCorrectItIfNeedGlobazDate() throws Exception {
        assertEquals("18.02.1980", ElementsDomParser.convertAsDateAndCorrectItIfNeed("19800218").getSwissValue());
    }

    @Test
    public void testConvertToBooleanReturnNull() throws Exception {
        assertNull(ElementsDomParser.convertToBoolean("toto"));
        assertNull(ElementsDomParser.convertToBoolean(null));
    }

    @Test
    public void testConvertToBooleanReturnTrue() throws Exception {
        assertTrue(ElementsDomParser.convertToBoolean("true"));
        assertTrue(ElementsDomParser.convertToBoolean("TRUE"));
    }

    @Test
    public void testConvertToBooleanReturnFalse() throws Exception {
        assertFalse(ElementsDomParser.convertToBoolean("false"));
        assertFalse(ElementsDomParser.convertToBoolean("FALSE"));
    }

    @Test
    public void testConvertToInteger() throws Exception {
        assertEquals(new Integer(0), ElementsDomParser.convertToInteger("0"));
    }

    @Test
    public void testConvertToIntegerNull() throws Exception {
        assertNull(ElementsDomParser.convertToInteger(null));
        assertNull(ElementsDomParser.convertToInteger(""));
    }

    @Test
    public void testCreateListReturnEmptyString() throws Exception {
        List<String> list = domParser.find("empty").createList(new Function<String>() {
            @Override
            public String convert(ElementsDomParser parser) {
                return "";
            }
        });
        assertTrue(!list.isEmpty());
    }

    @Test
    public void testCreateReturnEmptyListReturnNull() throws Exception {
        List<String> list = domParser.find("empty").createList(new Function<String>() {
            @Override
            public String convert(ElementsDomParser parser) {
                return null;
            }
        });
        assertTrue(list.isEmpty());
    }
}
