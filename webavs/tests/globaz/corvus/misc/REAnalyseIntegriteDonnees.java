package globaz.corvus.misc;

import globaz.corvus.TestUtils;
import globaz.corvus.utils.AbstractBProcessTestCaseWithContext;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazJobQueue;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import java.sql.ResultSet;
import org.junit.Ignore;
import org.junit.Test;

public class REAnalyseIntegriteDonnees extends AbstractBProcessTestCaseWithContext {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public REAnalyseIntegriteDonnees() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void _executeCleanUp() {
        // TODO WTF ???
    }

    @Override
    protected boolean _executeProcess() throws Exception {
        BTransaction transaction = null;
        try {
            transaction = (BTransaction) getSession().newTransaction();
            if (!transaction.isOpened()) {
                transaction.openTransaction();
            }

            logInfo(this.getClass().getSimpleName() + "Démarrage de l'analyse...");
            BStatement statement = null;
            String query = null;
            ResultSet resultSet = null;

            String schema = getDBSchema();
            logInfo(this.getClass().getSimpleName() + "Schéma de base de données utilisé :[" + schema + "]");

            // PRDemande vers tiers
            statement = new BStatement(transaction);
            statement.createStatement();
            query = "SELECT WAIDEM,WAITIE FROM " + schema + "PRDEMAP WHERE (WAITIE is null OR WAITIE = 0)";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String id = resultSet.getString("WAIDEM");
                String extRef = resultSet.getString("WAITIE");
                logWarn("La PRDemande [id:" + id + "] ne possède pas d'ID vers un TIERS. YAIDPA = [" + extRef + "]");
            }
            statement.closeStatement();

            // REDemande vers demande de prestation
            statement = new BStatement(transaction);
            statement.createStatement();
            query = "SELECT YAIDEM,YAIDPA FROM " + schema + "REDEREN WHERE (YAIDPA is null OR YAIDPA = 0)";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String yaidem = resultSet.getString("YAIDEM");
                String yaidpa = resultSet.getString("YAIDPA");
                logWarn("La REDemandeRente [id:" + yaidem + "] ne possède pas d'ID vers une PRDemande. YAIDPA = ["
                        + yaidpa + "]");
            }
            statement.closeStatement();

            // /////////////////////////////////////////////////////////////////////

            if (!transaction.isRollbackOnly()) {
                transaction.commit();
            } else {
                transaction.rollback();
            }
        } catch (Exception exception) {
            transaction.rollback();
        } finally {
            transaction.closeTransaction();
        }
        return false;
    }

    private void addLog(String prefix, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        TestUtils.indent(sb, 10);
        sb.append(": " + message);
        System.out.println(sb.toString());
    }

    private final String getDBSchema() {
        StringBuffer buffer = new StringBuffer();
        String schema = Jade.getInstance().getDefaultJdbcSchema();
        String prefix = Jade.getInstance().getDefaultJdbcTablePrefix();
        if (!JadeStringUtil.isBlank(schema)) {
            buffer.append(schema);
            buffer.append(".");
        }
        if (!JadeStringUtil.isBlank(prefix)) {
            buffer.append(prefix);
        }
        return buffer.toString();
    }

    @Override
    protected String getEMailObject() {
        // TODO
        return "lga@globaz.ch";
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_LONG;
    }

    private void logError(String message, Exception e) {
        addLog("ERROR", message);
    }

    private void logInfo(String message) {
        addLog("INFO", message);
    }

    private void logWarn(String message) {
        addLog("WARN", message);
    }

    @Ignore
    @Test
    public void testProcess() {
        try {
            _executeProcess();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
