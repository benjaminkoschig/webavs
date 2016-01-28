package globaz.hermes.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashSet;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author ado 22 mars 04
 */
public class LargeSqlInsert {
    //
    public static void main(String[] args) {
        if (args.length != 5) {
            System.err.println("Usage : java LargeSqlInsert <jdbcDriver> <jdbcURL> <tableFile> <uid> <pwd>");
            System.exit(-1);
        }
        try {
            new LargeSqlInsert(args).go();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Connection connection;
    private String jdbcDriver;
    private String jdbcUrl;
    private String pwd;
    private String tableFile;
    private HashSet tableSet;

    private String uid;

    //
    public LargeSqlInsert(String args[]) {
        jdbcDriver = args[0];
        jdbcUrl = args[1];
        tableFile = args[2];
        uid = args[3];
        pwd = args[4];
    }

    /**
     * Method closeConnection.
     */
    private void closeConnection() throws SQLException {
        System.out.println("Closing connection");
        connection.close();
    }

    protected String doubleSingleQuotes(String aString) {
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
     * Method getConnection.
     * 
     * @return Connection
     */
    private Connection getConnection() throws IllegalAccessException, InstantiationException, SQLException,
            ClassNotFoundException {
        System.out.println("Opening connection for <" + uid + "> on " + jdbcUrl);
        Driver d = (Driver) Class.forName(jdbcDriver).newInstance();
        DriverManager.registerDriver(d);
        Connection con = DriverManager.getConnection(jdbcUrl, uid, pwd);
        return con;
    }

    /**
     * Method getTables.
     * 
     * @return String
     */
    private HashSet getTables() throws FileNotFoundException, IOException {
        System.out.println("Fetching table names");
        RandomAccessFile f = new RandomAccessFile(tableFile, "r");
        HashSet set = new HashSet();
        String s = "";
        while ((s = f.readLine()) != null) {
            set.add(s);
        }
        return set;
    }

    public void go() throws IllegalAccessException, ClassNotFoundException, InstantiationException, SQLException,
            FileNotFoundException, IOException {
        tableSet = getTables();
        connection = getConnection();
        processTables(tableSet);
        closeConnection();
    }

    /**
     * Method processTables.
     * 
     * @param tableSet
     */
    private void processTables(HashSet tableSet) throws SQLException, FileNotFoundException, IOException {
        Iterator it = tableSet.iterator();
        ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream("insertScript.zip"));
        // loops over tables
        while (it.hasNext()) {
            String table = (String) it.next();
            System.out.println("Working table " + table + "(INSERT_" + table + ".sql)");
            // DataOutputStream fos = new DataOutputStream(new
            // FileOutputStream("INSERT_" + table + ".sql"));
            ZipEntry zipEntry = new ZipEntry("INSERT_" + table + ".sql");
            zipFile.putNextEntry(zipEntry);
            String sqlSelect = "SELECT * FROM " + table;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sqlSelect);
            ResultSetMetaData meta = rs.getMetaData();
            StringBuffer sqlBuffer = new StringBuffer();
            // loops over records
            while (rs.next()) {
                sqlBuffer.append("INSERT INTO ");
                sqlBuffer.append(table);
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
                        valuesBuffer.append(doubleSingleQuotes(fieldValue.toString().trim()));
                        if ((columnType == Types.CHAR) || (columnType == Types.VARCHAR)) {
                            valuesBuffer.append("'");
                        }
                    }
                }
                sqlBuffer.append(fieldsBuffer);
                sqlBuffer.append(") VALUES (");
                sqlBuffer.append(valuesBuffer);
                sqlBuffer.append(");\r\n");
                // on écrit la ligne, et on vide le buffer
                // fos.writeBytes(sqlBuffer.toString());
                // fos.flush();
                // fos.close();
                zipFile.write(sqlBuffer.toString().getBytes());
                sqlBuffer = new StringBuffer();
            }
            zipFile.closeEntry();
        }
        zipFile.close();
    }
}
