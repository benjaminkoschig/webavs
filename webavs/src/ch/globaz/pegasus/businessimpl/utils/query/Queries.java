package ch.globaz.pegasus.businessimpl.utils.query;

import globaz.jade.client.xml.JadeXmlReader;
import globaz.jade.client.xml.JadeXmlReaderException;
import globaz.jade.persistence.util.JadePersistenceUtil;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Queries {

    private static final String propFileName = "pegasus/sqlSpecifique/queries.xml";
    private static Map<String, String> queries = new ConcurrentHashMap<String, String>();

    /**
     * This function add the query ton the queries map and change the sql SCHEMA to the current s
     * 
     * @param {Query} The object query to add in the map
     */
    private static void addQueryInMapAndSchemaToSql(Query query) {
        String sql = query.getSql().replaceAll("SCHEMA", JadePersistenceUtil.getDbSchema());
        Queries.queries.put(query.getName(), sql);
    }

    /**
     * @param currentNode
     */
    private static void addQueryInMapFromNode(Node currentNode) {
        switch (currentNode.getNodeType()) {
            case Node.ELEMENT_NODE:
                Element ele = (Element) currentNode;
                Element sql = Queries.getElementByTagName("sql", ele);
                Element comment = Queries.getElementByTagName("comment", ele);
                Query query = new Query();
                query.setComment(comment.getTextContent());
                query.setSql(sql.getTextContent());
                query.setName(ele.getAttribute("name"));
                Queries.addQueryInMapAndSchemaToSql(query);
                break;
            default:
        }
    }

    /**
     * Add the queries form the xml file into the map
     * 
     */
    private static void addQuriesFromFileInMap() {
        try {

            Document t = JadeXmlReader.parseFile(Queries.propFileName);
            Element currentElement = t.getDocumentElement();
            NodeList childs = currentElement.getChildNodes();
            for (int i = 0; i < childs.getLength(); i++) {
                Queries.addQueryInMapFromNode(childs.item(i));
            }
        } catch (JadeXmlReaderException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * @param name
     * @param elementRoot
     * @return
     */
    private static Element getElementByTagName(String name, Element elementRoot) {
        NodeList list = elementRoot.getElementsByTagName("sql");
        for (int i = 0; i < list.getLength(); i++) {
            switch (list.item(i).getNodeType()) {
                case Node.ELEMENT_NODE:
                    return (Element) list.item(i);
                default:
            }
        }
        return null;
    }

    /**
     * @return {Map<String: queryName, String: sql> } Return the map who contain all queries
     * @throws SQLException
     */
    public static Map<String, String> getQueries() throws SQLException {
        return Queries.queries;
    }

    /**
     * @param {String} The query name of the xml attribute who contain the sql wanted
     * @return {String} The sql from the xml
     * @throws SQLException
     */
    public static String getQuery(String queryName) throws SQLException {
        String sql = Queries.queries.get(queryName);
        if (sql == null) {
            sql = Queries.loadQueries().get(queryName);
        }
        return sql;
    }

    /**
     * Load all the query form the file into the static map
     * 
     * @return {Map<String: queryName, String: sql> } Return the map who contain all queries
     * @throws SQLException
     */
    private static Map<String, String> loadQueries() throws SQLException {
        Queries.addQuriesFromFileInMap();
        return Queries.queries;
    }

}