package globaz.osiris.db.ordres.format.opt;

import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CASQL {

    public interface EachRow {
        public void eachRow(Map<String, String> row, long rowNumber) throws Exception;
    };

    private static long _count(Connection conn, String from) throws Exception {
        Statement stmt = null;
        String sql = "select count(*) as cpt " + from;
        long cpt = 0;
        try {
            stmt = conn.createStatement();
            stmt.executeQuery(sql);
            ResultSet res = stmt.getResultSet();
            conn.commit();
            if (res.next()) {
                cpt = res.getLong("cpt");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return cpt;
    }

    private static long _cursor(Connection conn, String fields, String from, EachRow each) throws Exception {
        Statement stmt = null;
        String sql = "select " + fields + " " + from;

        long rowNumber = 0;
        try {
            stmt = conn.createStatement();
            stmt.executeQuery(sql);
            ResultSet res = stmt.getResultSet();
            List<String> fieldList = JadeStringUtil.tokenize(fields, ",");
            String[] simpleFields = new String[fieldList.size()];
            int ix = 0;
            for (String fieldName : fieldList) {
                fieldName = fieldName.trim();
                fieldName = fieldName.toUpperCase();
                String[] p = fieldName.split(" ");
                fieldName = p[p.length - 1];
                simpleFields[ix] = fieldName;
                ix++;
            }
            int l = simpleFields.length;
            Map<String, String> m = new HashMap<String, String>();

            while (res.next()) {
                for (int i = 0; i < l; i++) {
                    m.put(simpleFields[i], CASQL._prepare(res.getString(simpleFields[i])));
                }
                each.eachRow(m, rowNumber++);
                m.clear();
            }
            conn.commit();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return rowNumber;
    }

    /*
     * return : List[Map[col:value]], liste des resultats
     */
    private static String _prepare(String value) {
        if (value != null) {
            value = (value).trim().replace('¬', '\'').replace('¢', '"');
        }
        return value;
    }

    /*
     * return : le nombre de records
     */
    public static long count(BSession session, String from) throws Exception {
        long res = 0;

        BTransaction trans = null;
        Connection conn = null;
        try {
            trans = new BTransaction(session);
            trans.openTransaction();
            conn = trans.getConnection();
            res = CASQL._count(conn, from);
        } finally {
            if (trans != null) {
                trans.closeTransaction();
            }
        }
        return res;
    }

    public static long cursor(BSession session, String fields, String from, EachRow eachRow) throws Exception {
        BTransaction trans = null;
        Connection conn = null;
        long nb = 0;
        try {
            trans = new BTransaction(session);
            trans.openTransaction();
            conn = trans.getConnection();
            nb = CASQL._cursor(conn, fields, from, eachRow);
        } finally {
            if (trans != null) {
                trans.closeTransaction();
            }
        }
        return nb;
    }

}
