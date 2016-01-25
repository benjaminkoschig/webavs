package globaz.hermes.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author ado 7 mai 04
 */
public class ExtractSQL {
    /**
     * Method main.
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage : java " + ExtractSQL.class + " \"<sql>\"");
            System.exit(-1);
        }
        try {
            new ExtractSQL(args[0]).go();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    // /////
    // /////
    private String driver = "COM.ibm.db2.jdbc.app.DB2Driver";
    private String pwd = "gnrcqiph";
    private String rSQL = "";
    //
    // private String uid = "ssii";
    // private String pwd = "ssiiadm";
    private String uid = "T75AL";

    private String url = "jdbc:db2:EQQ1";

    /**
     * Method ExtractSQL.
     * 
     * @param rSQL
     */
    //
    public ExtractSQL(String rSQL) {
        this.rSQL = rSQL;
    }

    /**
     * Method executeQuery.
     * 
     * @param rSQL
     */
    private ResultSet executeQuery(Statement s, String rSQL) throws SQLException {
        return s.executeQuery(rSQL);
    }

    /**
     * Method getConnection.
     * 
     * @return Connection
     */
    private Connection getConnection() throws IllegalAccessException, SQLException, InstantiationException,
            ClassNotFoundException {
        Class.forName(driver).newInstance();
        return DriverManager.getConnection(url, uid, pwd);
    }

    /**
     * Method getStatement.
     * 
     * @return Statement
     */
    private Statement getStatement(Connection con) throws SQLException, IllegalAccessException, ClassNotFoundException,
            InstantiationException {
        return con.createStatement();
    }

    /**
     * Method go.
     * 
     * @throws InstantiationException
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public void go() throws InstantiationException, SQLException, IllegalAccessException, ClassNotFoundException,
            FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(new File("c:/out.csv"));
        Connection con = getConnection();
        Statement statement = getStatement(con);
        ResultSet rSet = executeQuery(statement, rSQL);
        ResultSetMetaData metaData = rSet.getMetaData();
        while (rSet.next()) {
            System.out.print(".");
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                fos.write((rSet.getObject(i + 1) + ";").getBytes());
            }
            fos.write("\n".getBytes());
        }
        fos.close();
        con.close();
        System.out.println("\nCiao");
    }

}
