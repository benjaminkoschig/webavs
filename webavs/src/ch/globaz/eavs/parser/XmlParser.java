package ch.globaz.eavs.parser;

import globaz.jade.sedex.CannotParseSedexMessageException;
import globaz.jade.sedex.JadeSedexMessageParser;
import java.io.StringReader;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;

public class XmlParser implements JadeSedexMessageParser {
    @Override
    public Object parse(String xmlFileContent, Map metadata) throws CannotParseSedexMessageException {
        try {
            EAVSSaxHandler saxHandler = null;
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            saxHandler = new EAVSSaxHandler();
            saxHandler.setMetaData(metadata);
            parser.parse(new InputSource(new StringReader(xmlFileContent)), saxHandler);
            return saxHandler.getContainerElement();
        } catch (final Exception e) {
            throw new CannotParseSedexMessageException(e);
        }
    }
}
