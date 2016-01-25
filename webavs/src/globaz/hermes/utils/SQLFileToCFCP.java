package globaz.hermes.utils;

import globaz.globall.util.JAUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author ado 23 mars 04
 */
public class SQLFileToCFCP {
    /**
     * Method main.
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage : java globaz.hermes.utils.SQLFileToCFCP <file> <uid> <pwd>");
            System.exit(-1);
        }
        try {
            new SQLFileToCFCP(args).go();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private Connection con;
    private String file;
    private String pwd;
    private Statement statement;

    private String uid;

    /**
     * Method SQLFileToCFCP.
     * 
     * @param args
     */
    public SQLFileToCFCP(String args[]) {
        file = args[0];
        uid = args[1];
        pwd = args[2];
    }

    /**
     * Method go.
     */
    public void go() throws FileNotFoundException, IOException, IllegalAccessException, SQLException,
            ClassNotFoundException, InstantiationException {
        openConnection();
        long time = System.currentTimeMillis();
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        String sqlLine = "";
        int i = 0;
        System.out.println("Starting query execution");
        while ((sqlLine = raf.readLine()) != null) {
            sqlLine = JAUtil.replaceString(sqlLine, " TCTHE", " CFCP.HE");
            sqlLine = sqlLine.replace(';', ' ');
            i++;
            if (i % 1000 == 0) {
                System.out.println(i);
                System.out.println((System.currentTimeMillis() - time) / 1000);
            }
            runQuery(sqlLine);
        }
        System.out.println("...Query execution complete");
        System.out.println("Closing connection and files");
        statement.close();
        con.close();
        raf.close();
        System.out.println("...Closing done");
    }

    /**
     * Method openConnection.
     */
    private void openConnection() throws IllegalAccessException, SQLException, ClassNotFoundException,
            InstantiationException {
        con = DriverManager.getConnection("jdbc:as400://ASGLOB1", uid, pwd);
        statement = con.createStatement();
        System.out.println("...Connection opened");
    }

    /**
     * Method runQuery.
     * 
     * @param sqlLine
     */
    private void runQuery(String sqlLine) throws SQLException {
        statement.executeUpdate(sqlLine);
    }
}
