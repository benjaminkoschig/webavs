package ch.globaz.al.prestations;

import globaz.al.process.traitement.ALStatistiquesOfasProcess;
import globaz.jade.common.Jade;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DoublonPrestationsChecker {

    public static boolean hasDoubleDetailPrestation(List<String> idPrestations) throws SQLException {

        String stringIdsEntetes = idPrestations.toString();
        stringIdsEntetes = stringIdsEntetes.substring(1, stringIdsEntetes.length() - 1);
        String schema = Jade.getInstance().getDefaultJdbcSchema();

        String sql = "SELECT eid, det.MID, FID, NMONT, NVALID, CSTYPE, CSCATA, COUNT(*) AS NB_DOUBLONS " + "FROM "
                + schema + ".ALDETPRE det " + "JOIN " + schema + ".ALENTPRE ent on ent.MID=det.MID "
                + "WHERE det.MID IN (" + stringIdsEntetes + ") "
                + "GROUP BY eid, det.MID, FID, NMONT, NVALID, CSTYPE, CSCATA " + "HAVING COUNT(*) > 1";

        int nbDoublons = countLines(sql);

        return nbDoublons > 0;
    }

    public static boolean hasDoubleEntetePrestation(List<String> idPrestations) throws SQLException {

        String stringIdsEntetes = idPrestations.toString();
        stringIdsEntetes = stringIdsEntetes.substring(1, stringIdsEntetes.length() - 1);
        String schema = Jade.getInstance().getDefaultJdbcSchema();

        String sql = "SELECT MPERD, MPERA, MMONT, COUNT(*) AS NB_DOUBLONS FROM " + schema + ".ALENTPRE WHERE MID IN ("
                + stringIdsEntetes + ") GROUP BY MPERD, MPERA, MMONT HAVING COUNT(*) > 1";

        int nbDoublons = countLines(sql);

        return nbDoublons > 0;
    }

    // public static boolean hasDoubleEntetePrestation(String idDossier) throws SQLException {
    //
    // String schema = Jade.getInstance().getDefaultJdbcSchema();
    //
    // String sql = "SELECT MPERD, MPERA, MMONT, COUNT(*) AS NB_DOUBLONS FROM " + schema + ".ALENTPRE WHERE EID="
    // + idDossier + " GROUP BY MPERD, MPERA, MMONT HAVING COUNT(*) > 1";
    //
    // int nbDoublons = countLines(sql);
    //
    // return nbDoublons > 0;
    // }

    private static int countLines(String sql) throws SQLException {

        Statement statement = null;
        try {
            statement = JadeThread.currentJdbcConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.last();
            int nbLines = resultSet.getRow();
            return nbLines;

        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    JadeLogger.warn(ALStatistiquesOfasProcess.class,
                            "Problem to close statement in ALStatistiquesOfasProcess, reason : " + e.toString());
                }
            }

        }
    }
}
