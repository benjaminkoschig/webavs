package globaz.test.osiris.solde;

import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.helios.application.CGApplication;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.jdbc.JadeJdbcDatasource;
import globaz.jade.jdbc.JadeJdbcDatasourceDirect;
import globaz.jade.jdbc.JadeJdbcDriver;
import globaz.osiris.api.APIOperation;
import globaz.osiris.db.journal.operation.CASumOperationManager;
import java.sql.ResultSet;
import org.junit.Ignore;
import org.junit.Test;

public class CACheckCompteCourantTest {

    private static final String IDENTIFY_DB2_DATASOURCE = ":db2:";
    private static final String KEY_PROPERTY_USER = "user";
    private static final String KEY_PROPERTY_PASSWORD = "password";

    private static final String DEFAULT_USER = "ssii";
    private static final String DEFAULT_PASSWORD = "ssiiadm";

    private String user = DEFAULT_USER;
    private String password = DEFAULT_PASSWORD;

    private static final String IDENTIFIANT_COMPTE_COURANT = "%.110%";

    /**
     * Si les propriétés user et password sont donnés à la JVM les utilier pour
     * creer la session dans les tests.
     */

    protected void setUp() throws Exception {
        if (System.getProperty(KEY_PROPERTY_USER) != null) {
            user = System.getProperty(KEY_PROPERTY_USER);
        }

        if (System.getProperty(KEY_PROPERTY_PASSWORD) != null) {
            password = System.getProperty(KEY_PROPERTY_PASSWORD);
        }
    }

    /**
     * Le test compare les soldes par compte courant de comptabilité auxiliaire
     * avec leurs soldes correspondants en comptabilité générale.
     * 
     * @throws Exception
     */
    @Test
    @Ignore
    public void testTableSolde() throws Exception {
        BTransaction transaction = null;

        try {
            BSession session = (BSession) GlobazSystem.getApplication(CGApplication.DEFAULT_APPLICATION_HELIOS)
                    .newSession(user, password);

            transaction = (BTransaction) session.newTransaction();
            transaction.openTransaction();

            String sql = new String();

            String collection = new CASumOperationManager().getCollection();

            sql += "select * from ( ";

            sql += "select sum(a.montant) as montant, b.idexterne, c.referenceexterne from ";
            sql += collection + "cgecrip a, " + collection + "cgplanp b, " + collection + "cgjourp c where ";
            sql += "a.estactive = '1' and a.idcompte = b.idcompte and ";
            sql += "b.idexterne like '" + IDENTIFIANT_COMPTE_COURANT + "' and a.idjournal = c.idjournal ";
            sql += "and b.idexercomptable = c.idexercomptable ";
            sql += "and c.referenceexterne is not null and c.referenceexterne > '' ";
            sql += "group by b.idexterne, c.referenceexterne ";
            sql += "order by c.referenceexterne asc ";

            sql += ") a, ( ";

            sql += "select sum(a.montant) as montant, b.idexterne, a.idjournal from ";
            sql += collection + "caoperp a, " + collection + "cacptcp b where ";
            sql += "a.etat = " + APIOperation.ETAT_COMPTABILISE + " and a.idcomptecourant = b.idcomptecourant ";
            sql += "group by b.idexterne, a.idjournal ";
            sql += "order by a.idjournal asc ";

            sql += ") b where ";

            String jdbcDriverUrl = getJdbDriverUrl();

            if (jdbcDriverUrl != null && jdbcDriverUrl.indexOf(IDENTIFY_DB2_DATASOURCE) > -1) {
                sql += "CAST (a.referenceexterne as NUMERIC) = b.idjournal and ";
            } else {
                sql += "a.referenceexterne = b.idjournal and ";
            }

            sql += "a.idexterne = b.idexterne and ";
            sql += "a.montant <> b.montant ";

            BStatement s = new BStatement(transaction);
            s.createStatement();

            ResultSet test = s.executeQuery(sql);

            String errorMessage = new String();
            while (test.next()) {
                String montant = test.getString(1).trim();
                String idexterne = test.getString(2).trim();
                String referenceexterne = test.getString(3).trim();

                String montantOsiris = test.getString(4).trim();

                errorMessage += "[journal " + referenceexterne + ", compte " + idexterne + "] " + montant + " <> "
                        + montantOsiris + "\n";
            }

            s.closeStatement();

            if (!JadeStringUtil.isBlank(errorMessage)) {
                throw new Exception(errorMessage);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }

        }
    }

    /**
     * Return l'url de connexion à la bd. Si DB2, la jointure sur l'idjournal et
     * la referenceexterne de la query sql devra être castée.
     * 
     * @return
     */
    private String getJdbDriverUrl() {
        try {
            String jdbcUrl = Jade.getInstance().getDefaultJdbcUrl();
            JadeJdbcDatasource datasource = null;

            if ((jdbcUrl != null) && (jdbcUrl.toLowerCase().startsWith("jdbc:jade:"))) {
                datasource = JadeJdbcDriver.getInstance().getDatasource(jdbcUrl.substring(10));
            }

            JadeJdbcDatasourceDirect dsDirect = (JadeJdbcDatasourceDirect) datasource;
            return dsDirect.getUrl();
        } catch (Exception e) {
            return null;
        }
    }
}
