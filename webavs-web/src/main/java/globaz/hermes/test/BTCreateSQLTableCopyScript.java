package globaz.hermes.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Tool permettant de cr�er des scripts SQL pour copier des tables
 * <p>
 * Les arguments de <code>BTCreateSQLTableCopyScript</code> sont:
 * <ul>
 * <li><code>jdbcDriverName</code> nom du driver JDBC � utiliser
 * <li><code>jdbcUrl</code> url de connexion � la base de donn�es
 * <li><code>tableName</code> nom de la table � traiter ou FILE pour utiliser une liste de tables dans un fichier
 * <li><code>tablesFileName</code> nom du fichier contenant les tables � traiter (un nom de table par ligne)
 * </ul>
 * 
 * @author Emmanuel Fleury
 */
public class BTCreateSQLTableCopyScript {
    private final static String ZIP_FILENAME = "insertScript.zip";

    /**
     * Programme qui construit le script SQL pour copier des tables
     * <p>
     * Arguments:
     * <ul>
     * <li>jdbcDriverName nom du driver JDBC � utiliser
     * <li>jdbcUrl url de connexion � la base de donn�es
     * <li>tableName nom de la table � traiter ou FILE pour utiliser une liste de tables dans un fichier
     * <li>tablesFileName nom du fichier contenant les tables � traiter (un nom de table par ligne)
     * </ul>
     * 
     * @param args
     *            les arguments de la ligne de commande
     */
    public static void main(String[] args) {
        try {
            if ((args.length != 3) && ((args.length != 4) || (!args[2].equalsIgnoreCase("file")))) {
                throw new Exception("ERROR: wrong arguments, please see program documentation");
            }
            System.out.println("BTCreateSQLTableCopyScript started...");
            BTCreateSQLTableCopyScript process = new BTCreateSQLTableCopyScript();
            process._setJdbcDriverName(args[0]);
            process._setJdbcUrl(args[1]);
            if (args.length == 3) {
                process._setTableName(args[2]);
            } else { // (args.length == 4)
                process._setTableListFileName(args[3]);
            }
            process._run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            System.out.println("BTCreateSQLTableCopyScript finished processing.");
        }
        System.exit(0);
    }

    private Connection connection = null;
    private Driver jdbcDriver = null;
    //
    private String jdbcDriverName = null;
    private String jdbcUrl = null;
    private String tableListFileName = null;
    //
    private String tableName = null;
    private Vector tables = new Vector();

    private ZipOutputStream zip = null;

    /**
     * Constructeur du type BTCreateSQLTableCopyScript.
     */
    public BTCreateSQLTableCopyScript() {
        super();
    }

    /**
     * Construit la requ�te SQL d'insertion
     * 
     * @return le script SQL pour copier la table
     * @exception java.lang.Exception
     *                en cas d'erreur
     */
    protected final String _constructSqlInsert() throws Exception {
        if (connection == null) {
            return null;
        }
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            if (stmt != null) {
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
                ResultSetMetaData meta = rs.getMetaData();
                StringBuffer sqlBuffer = new StringBuffer();
                while (rs.next()) {
                    System.out.println(".");
                    sqlBuffer.append("INSERT INTO ");
                    sqlBuffer.append(tableName);
                    sqlBuffer.append(" (");
                    StringBuffer fieldsBuffer = new StringBuffer();
                    StringBuffer valuesBuffer = new StringBuffer();
                    int columnCount = meta.getColumnCount();
                    for (int i = 0; i < columnCount; i++) {
                        String fieldName = meta.getColumnName(i + 1);
                        Object fieldValue = rs.getObject(i + 1);
                        if (i > 0) {
                            fieldsBuffer.append(",");
                            valuesBuffer.append(",");
                        }
                        fieldsBuffer.append(fieldName);
                        if (fieldValue == null) {
                            valuesBuffer.append("NULL");
                        } else {
                            int columnType = meta.getColumnType(i + 1);
                            if ((columnType == Types.CHAR) || (columnType == Types.VARCHAR)) {
                                valuesBuffer.append("'");
                            }
                            valuesBuffer.append(_doubleSingleQuotes(fieldValue.toString().trim()));
                            if ((columnType == Types.CHAR) || (columnType == Types.VARCHAR)) {
                                valuesBuffer.append("'");
                            }
                        }
                    }
                    sqlBuffer.append(fieldsBuffer);
                    sqlBuffer.append(") VALUES (");
                    sqlBuffer.append(valuesBuffer);
                    sqlBuffer.append(");\r\n");
                }
                return sqlBuffer.toString();
            } else {
                return null;
            }
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (12.02.2003 12:46:01)
     * 
     * @return java.lang.String
     * @param aString
     *            java.lang.String
     */
    protected String _doubleSingleQuotes(String aString) {
        StringBuffer result = new StringBuffer();
        int length = aString.length();
        for (int i = 0; i < length; i++) {
            char currChar = aString.charAt(i);
            result.append(currChar);
            if (currChar == '\'') {
                result.append(currChar);
            }
        }
        return result.toString();
    }

    /**
     * Cr�e le script pour copier une table
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur
     */
    protected final void _processTable() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(out));
        output.write(_constructSqlInsert());
        out.flush();
        output.close();
        byte[] zipContents = out.toByteArray();
        out.close();
        ByteArrayInputStream in = new ByteArrayInputStream(zipContents);
        zip.putNextEntry(new ZipEntry(tableName.replace('.', '_') + ".sql"));
        int len;
        byte[] buffer = new byte[1024];
        while ((len = in.read(buffer)) > 0) {
            zip.write(buffer, 0, len);
        }
        zip.closeEntry();
        in.close();
    }

    /**
     * Ex�cute le programme
     * 
     * @exception java.lang.Exception
     *                en cas d'erreur
     */
    protected final void _run() throws Exception {
        tables.clear();
        if (tableName == null) {
            if (tableListFileName == null) {
                throw new Exception("Name of the file containing the table list is unknown");
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(new FileInputStream(tableListFileName)));
                String table = null;
                while ((table = in.readLine()) != null) {
                    if (table.trim().length() != 0) {
                        tables.add(table);
                    }
                }
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            tables.add(tableName);
        }
        connection = null;
        zip = null;
        try {
            zip = new ZipOutputStream(new FileOutputStream(ZIP_FILENAME));
            System.out.println("Getting connection");
            connection = DriverManager.getConnection(jdbcUrl, "t75al", "gnrcqiph");
            System.out.println("Connection OK");
            if (connection != null) {
                for (Enumeration enum1 = tables.elements(); enum1.hasMoreElements();) {
                    tableName = (String) enum1.nextElement();
                    System.out.println("Processing table " + tableName);
                    _processTable();
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (zip != null) {
                try {
                    zip.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * D�finit le driver JDBC � utiliser
     * 
     * @param newJdbcDriverName
     *            le nom du driver JDBC � utiliser
     * @exception java.lang.Exception
     *                en cas d'erreur
     */
    protected final void _setJdbcDriverName(String newJdbcDriverName) throws Exception {
        jdbcDriverName = newJdbcDriverName;
        jdbcDriver = (Driver) Class.forName(jdbcDriverName).newInstance();
        DriverManager.registerDriver(jdbcDriver);
    }

    /**
     * D�finit l'URL JDBc � acc�der
     * 
     * @param newJdbcUrl
     *            l'URL JDBc � acc�der
     */
    protected final void _setJdbcUrl(String newJdbcUrl) {
        jdbcUrl = newJdbcUrl;
    }

    /**
     * D�finit le nom du fichier contenant la liste des tables
     * 
     * @param filename
     *            le nom du fichier contenant la liste des tables
     */
    protected final void _setTableListFileName(String filename) {
        tableListFileName = filename;
    }

    /**
     * D�finit le nom de la table � traiter
     * 
     * @param name
     *            le nom de la table � traiter
     */
    protected final void _setTableName(String name) {
        tableName = name;
    }
}
