package ch.globaz.pegasus.businessimpl.services.models.decision;

import java.io.FileInputStream;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;

public class testCase {

    public testCase(String name) {

        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "com.ibm.db2.jcc.DB2Driver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
                "jdbc:db2://sglobdev5.ju.globaz.ch:52027/dev5prod");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "dev5idb2");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "dev5idb2");

        // _customerFactory = CustomerFacto.getInstance();
    }

    protected IDataSet getDataSet() throws Exception {
        return null;
    }

    public void t() throws Exception {
        // get the actual table values
        // IDatabaseConnection connection = getConnection();
        // IDataSet databaseDataSet = connection.createDataSet();
        // ITable actualTable = databaseDataSet.getTable("PCVERDRO");

        // get the expected table values
        IDataSet expectedDataSet = new FlatXmlDataSet(new FileInputStream("customer-expected.xml"));
        ITable expectedTable = expectedDataSet.getTable("PCVERDRO");

    }

}
