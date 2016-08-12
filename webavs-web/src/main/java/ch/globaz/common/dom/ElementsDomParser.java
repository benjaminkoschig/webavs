package ch.globaz.common.dom;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import ch.globaz.common.business.exceptions.CommonTechnicalException;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import com.sun.star.uno.RuntimeException;

public class ElementsDomParser {
    private Document document;
    private List<NodeList> result = new ArrayList<NodeList>();
    private Element element;

    private ElementsDomParser(Element element) {
        this.element = element;
    }

    public ElementsDomParser(Document document) {
        this.document = document;
    }

    public ElementsDomParser(String path) {
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            InputSource is = new InputSource(fileInputStream);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(is);
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }
    }

    public ElementsDomParser(FileInputStream fileInputStream) {
        try {
            InputSource is = new InputSource(fileInputStream);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(is);
        } catch (Exception e) {
            throw new CommonTechnicalException(e);
        }
    }

    ElementsDomParser(Document document, List<NodeList> result) {
        this.document = document;
        this.result = result;
    }

    public Document getDocument() {
        return document;
    }

    public ElementsDomParser find(String domPath) {
        String[] paths = domPath.split(" ");
        List<NodeList> result = new ArrayList<NodeList>();
        result.addAll(this.result);
        for (int i = 0; i < paths.length; i++) {
            if (i == 0 && this.result.isEmpty()) {
                NodeList nodeList;
                if (element != null) {
                    nodeList = element.getElementsByTagNameNS("*", paths[i]);
                } else {
                    nodeList = document.getElementsByTagNameNS("*", paths[i]);
                }
                if (nodeList.getLength() > 0) {
                    result.add(nodeList);
                }
            } else {
                List<NodeList> all1 = new ArrayList<NodeList>();
                for (NodeList nodeList : result) {
                    for (int j = 0; j < nodeList.getLength(); j++) {
                        NodeList subNodeList = ((Element) nodeList.item(j)).getElementsByTagNameNS("*", paths[i]);
                        if (subNodeList.getLength() > 0) {
                            all1.add(subNodeList);
                        }
                    }
                }
                result = all1;
            }
        }
        return new ElementsDomParser(document, result);
    }

    public boolean exist() {
        if (result.isEmpty()) {
            return false;
        }
        return true;
    }

    public <T> List<T> createList(Function<T> function) {
        List<T> list = new ArrayList<T>();
        if (!result.isEmpty()) {
            for (NodeList nodeList : result) {
                for (int j = 0; j < nodeList.getLength(); j++) {
                    List<NodeList> l = new ArrayList<NodeList>();
                    l.add(nodeList);

                    ElementsDomParser parser = new ElementsDomParser(((Element) nodeList.item(j)));
                    T t = function.convert(parser);
                    if (t != null) {
                        list.add(t);
                    }
                }
            }
        }
        return list;
    }

    public String getFirstValue() {
        if (!result.isEmpty() && result.get(0).getLength() > 0) {
            if (result.get(0) != null && result.get(0).item(0) != null) {
                Node node = result.get(0).item(0).getFirstChild();
                if (node != null && node.getNodeValue() != null) {
                    return replaceNullStringByNull(node.getNodeValue().trim());
                }
                return null;
            }
        }
        return null;
    }

    public String replaceNullStringByNull(String value) {
        if (value.equalsIgnoreCase("null")) {
            return null;
        }
        return value;
    }

    public Integer getValueAsInteger() {
        String value = getFirstValue();
        return convertToInteger(value);
    }

    static Integer convertToInteger(String value) {
        if (value != null && value.length() > 0) {
            return Integer.valueOf(value);
        }
        return null;
    }

    public Boolean getValueAsBoolean() {
        String value = getFirstValue();
        return convertToBoolean(value);
    }

    static Boolean convertToBoolean(String value) {
        if (value != null) {
            if (value.equalsIgnoreCase("true")) {
                return true;
            } else if (value.equalsIgnoreCase("false")) {
                return false;
            }
            return null;
        }
        return null;
    }

    public Date getValueAsDate() {
        String value = getFirstValue();
        if (value != null && value.trim().length() > 0) {
            try {
                XMLGregorianCalendar dateXml = DatatypeFactory.newInstance().newXMLGregorianCalendar(value);
                return new Date(dateXml.toGregorianCalendar().getTime());
            } catch (DatatypeConfigurationException e) {
                throw new RuntimeException("Error parasing date", e);
            }
        }
        return null;
    }

    /**
     * Corrige les dates au 31.02.2015
     * 
     * @return une date
     */
    public Date getValueAsDateAndCorrectItIfNeed() {
        String value = getFirstValue();
        return convertAsDateAndCorrectItIfNeed(value);
    }

    /**
     * Corrige les dates au 31.02.2015
     * 
     * @return une date
     */
    static Date convertAsDateAndCorrectItIfNeed(String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                if (Date.isGlobazDate(value)) {
                    return new Date(value);
                } else {
                    XMLGregorianCalendar dateXml = DatatypeFactory.newInstance().newXMLGregorianCalendar(value);
                    return new Date(dateXml.toGregorianCalendar().getTime());
                }
            } catch (DatatypeConfigurationException e) {
                throw new RuntimeException("Error parasing date", e);
            } catch (IllegalArgumentException e) {
                String[] s = value.split("-");
                if (s.length >= 3 && "31".equals(s[2]) && "02".equals(s[1])) {
                    Date date = new Date("01." + s[1] + "." + s[0]);
                    return date.getLastDayOfMonth();
                }
            }
        }
        return null;
    }

    public Montant getValueAsMontant() {
        String value = getFirstValue();
        if (value != null && value.trim().length() > 0) {
            return new Montant(value);
        }
        return Montant.ZERO;
    }

    public Node getFirstNode() {
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0).item(0);
    }

    public List<String> getValues() {
        List<String> values = new ArrayList<String>();
        for (NodeList nodeList : result) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                values.add(nodeList.item(i).getFirstChild().getNodeValue());
            }
        }
        return values;
    }

    public List<String> findValues(String domPath) {
        return find(domPath).getValues();
    }

    public String findValue(String domPath) {
        return find(domPath).getFirstValue();
    }

    public Integer findValueAsInteger(String domPath) {
        return find(domPath).getValueAsInteger();
    }

    public Date findValueAsDate(String domPath) {
        return find(domPath).getValueAsDate();
    }

    public Boolean findValueAsBoolean(String domPath) {
        return find(domPath).getValueAsBoolean();
    }

    public Boolean findValueAsBooleanNullToFalse(String domPath) {
        Boolean b = find(domPath).getValueAsBoolean();
        if (b == null) {
            b = false;
        }
        return b;
    }

    public Date findValueAsDateAndCorrectItIfNeed(String domPath) {
        return find(domPath).getValueAsDateAndCorrectItIfNeed();
    }

    public Montant findValueAsMontant(String domPath) {
        return find(domPath).getValueAsMontant();
    }

    public List<NodeList> getResult() {
        return result;
    }

    public void clear() {
        result = new ArrayList<NodeList>();
    }
}
