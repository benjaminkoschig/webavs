package globaz.pavo.db.compte;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class H51CIMapper {

    public static void main(String[] args) throws RemoteException, Exception {

        Connection con = null;

        String schema = args[0]; // schema de la BDD
        String driver = args[1];
        String url = args[2];
        String user = args[3];
        String password = args[4];

        if (!(args.length >= 4)) {
            System.out
                    .println("Arguments : 1) schema de la BDD, 2) driver connection BDD, 3) url BDD, 4) user, 5) password");
            System.exit(1);
        } else {

            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url, user, password);

            String requete = "SELECT KANAVS FROM " + schema
                    + ".CIINDIP WHERE KAIREG=309001 GROUP BY KANAVS HAVING COUNT(*) > 1";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(requete);

            List listDesAVS = new ArrayList();

            while (rs.next()) {
                listDesAVS.add(rs.getString("KANAVS")); // je crée une liste
                // avec les navs à
                // double
            }

            rs.close();

            String requete2 = "SELECT * FROM " + schema + ".CIINDIP WHERE KAIREG=309001 AND KANAVS = '";

            for (Iterator iter = listDesAVS.iterator(); iter.hasNext();) { // je
                // recherche
                // toute
                // les
                // lignes
                // pour
                // chaque
                // numéro
                // avs
                String kanavs = (String) iter.next();
                kanavs = kanavs.trim();
                ResultSet rs2 = stmt.executeQuery(requete2 + kanavs + "'");

                BigDecimal kaiindAModifier = null;
                BigDecimal kaiinrAModifier = null;
                BigDecimal kaiindFinal = null;
                BigDecimal kaiinrFinal = null;

                while (rs2.next()) {
                    if (Integer.parseInt(rs2.getBigDecimal("KAIIND").toString()) < 1000000) { // si
                        // pour
                        // la
                        // ligne
                        // le
                        // kaiind
                        // <
                        // 1'000'000
                        // alors
                        // il
                        // faut
                        // mettre
                        // à
                        // jour
                        kaiindAModifier = rs.getBigDecimal("KAIIND");
                        kaiinrAModifier = rs.getBigDecimal("KAIINR");
                    } else {
                        kaiindFinal = rs.getBigDecimal("KAIIND");
                        kaiinrFinal = rs.getBigDecimal("KAIINR");
                    }
                }
                rs2.close();

                stmt.executeUpdate("INSERT INTO " + schema
                        + ".CICORESP(ID_CI_MODIF, ID_CI_COMPL_MODIF, ID_CI_FINAL, ID_CI_COMPL_FINAL) VALUES" + // on
                        // sauve
                        // les
                        // données
                        // dans
                        // une
                        // table
                        // de
                        // sauvegarde
                        // CICORESP
                        "(" + kaiindAModifier + ", " + kaiinrAModifier + ", " + kaiindFinal + ", " + kaiinrFinal + ")");

                stmt.executeUpdate("UPDATE " + schema + ".CIECRIP SET KAIIND =" + kaiindFinal + " WHERE KAIIND = "
                        + kaiindAModifier); // MAJ de la table CIECRIP
                stmt.executeUpdate("UPDATE " + schema + ".CIRAOUP SET KAIIND=" + kaiindFinal + " WHERE KAIIND = "
                        + kaiindAModifier); // MAJ de la table CIRAOUP
                stmt.executeUpdate("UPDATE " + schema + ".CISPLIP SET KAIIND =" + kaiindFinal + " WHERE KAIIND = "
                        + kaiindAModifier); // MAJ de la table CISPLIP
                stmt.executeUpdate("UPDATE " + schema + ".CIEXCP SET KAIIND =" + kaiindFinal + " WHERE KAIIND = "
                        + kaiindAModifier); // MAJ de la table CIEXCP
                stmt.executeUpdate("DELETE FROM " + schema + ".CIINDIP WHERE KAIIND=" + kaiindAModifier); // DELETE
                // du
                // vieux
                // kaiind
                // dans
                // la
                // table
                // CIINDID

            }
            stmt.close();

            Statement stmt2 = con.createStatement();
            String requetePartie2 = "SELECT * FROM " + schema + ".CIINDIP CI INNER JOIN " + schema
                    + ".CICORESP COR ON CI.KAIINR = COR.ID_CI_MODIF "
                    + "WHERE KAIINR <> 0 AND KAIINR  NOT IN (SELECT KAIIND FROM " + schema + ".CIINDIP)";
            ResultSet rsx = stmt2.executeQuery(requetePartie2);
            Map corresp = new HashMap();

            while (rsx.next()) {
                BigDecimal kaiinr = rsx.getBigDecimal("KAIINR");
                BigDecimal kaiinrFinal = rsx.getBigDecimal("ID_CI_FINAL");
                corresp.put(kaiinr, kaiinrFinal);
            }
            rsx.close();

            for (Iterator it = corresp.keySet().iterator(); it.hasNext();) {
                BigDecimal kaiinr = (BigDecimal) it.next();
                BigDecimal kaiinrFinal = (BigDecimal) corresp.get(kaiinr);
                stmt2.executeUpdate("UPDATE " + schema + ".CIINDIP SET KAIINR=" + kaiinrFinal + " WHERE KAIINR="
                        + kaiinr);
            }
            stmt2.close();
            /*
             * **************************************************
             * MIse à jour des liaisons 2 ****************************************************
             */
            Statement stmt3 = con.createStatement();
            String requetePartie3 = "select * from "
                    + schema
                    + ".ciindip CI INNER JOIN "
                    + schema
                    + ".CICORESP COR ON CI.KAIINR = COR.ID_CI_COMPL_MODIF where kaiind = kaiinr and kaiind not in (select kaiinr from "
                    + schema + ".ciindip where kaiind <> kaiinr)";
            ResultSet rs3 = stmt3.executeQuery(requetePartie3);
            Map corresp2 = new HashMap();

            while (rs3.next()) {
                BigDecimal kaiind = rs3.getBigDecimal("KAIIND");
                BigDecimal kaiinrCompl = rs3.getBigDecimal("ID_CI_FINAL");
                corresp2.put(kaiind, kaiinrCompl);
            }
            rs3.close();

            for (Iterator it = corresp2.keySet().iterator(); it.hasNext();) {
                BigDecimal kaiind = (BigDecimal) it.next();
                BigDecimal kaiinrCompl = (BigDecimal) corresp2.get(kaiind);
                stmt3.executeUpdate("UPDATE " + schema + ".CIINDIP SET KAIINR=" + kaiind + " WHERE KAIIND="
                        + kaiinrCompl);
            }
            stmt3.close();
            /*
             * **************************************************
             * MIse à jour des administration pour les entetes. ****************************************************
             */
            Statement stmt4 = con.createStatement();
            String requetePartie4 = "select * from " + schema + ".ciindip CI INNER JOIN " + schema
                    + ".FUSEMAP FU ON CI.KAICAI = FU.SRC";
            ResultSet rs4 = stmt4.executeQuery(requetePartie4);
            Map corresp3 = new HashMap();

            while (rs4.next()) {
                BigDecimal idCI = rs4.getBigDecimal("KAIIND");
                BigDecimal adminDest = rs4.getBigDecimal("DEST");
                corresp3.put(idCI, adminDest);
            }
            rs4.close();

            for (Iterator it = corresp3.keySet().iterator(); it.hasNext();) {
                BigDecimal idCI = (BigDecimal) it.next();
                BigDecimal adminDest = (BigDecimal) corresp3.get(idCI);
                stmt4.executeUpdate("UPDATE " + schema + ".CIINDIP SET KAICAI=" + adminDest + " WHERE KAIIND=" + idCI);
            }
            stmt4.close();
            /*
             * **************************************************
             * MIse à jour des administration pour les RASS. ****************************************************
             */
            Statement stmt5 = con.createStatement();
            String requetePartie5 = "select * from " + schema + ".CIRAOUP RA INNER JOIN " + schema
                    + ".FUSEMAP FU ON RA.KKICCO = FU.SRC";
            ResultSet rs5 = stmt5.executeQuery(requetePartie5);
            Map corresp4 = new HashMap();

            while (rs5.next()) {
                BigDecimal idRaou = rs5.getBigDecimal("KKIRAO");
                BigDecimal kkicco = rs5.getBigDecimal("DEST");
                corresp4.put(idRaou, kkicco);
            }
            rs5.close();

            for (Iterator it = corresp4.keySet().iterator(); it.hasNext();) {
                BigDecimal kkirao = (BigDecimal) it.next();
                BigDecimal kkicco = (BigDecimal) corresp4.get(kkirao);
                stmt5.executeUpdate("UPDATE " + schema + ".CIRAOUP SET KKICCO=" + kkicco + " WHERE KKIRAO=" + kkirao);
            }
            stmt5.close();

            /*
             * **************************************************************
             * MIse à jour des administration pour les périodes de splitting.
             * **************************************************************
             */
            Statement stmt6 = con.createStatement();
            String requetePartie6 = "select * from " + schema + ".CISPLIP SPL INNER JOIN " + schema
                    + ".FUSEMAP FU ON SPL.KLICCO = FU.SRC";
            ResultSet rs6 = stmt6.executeQuery(requetePartie6);
            Map corresp5 = new HashMap();

            while (rs6.next()) {
                BigDecimal idSpli = rs6.getBigDecimal("KLIPSP");
                BigDecimal klicco = rs6.getBigDecimal("DEST");
                corresp5.put(idSpli, klicco);
            }
            rs3.close();

            for (Iterator it = corresp5.keySet().iterator(); it.hasNext();) {
                BigDecimal idSpli = (BigDecimal) it.next();
                BigDecimal klicco = (BigDecimal) corresp5.get(idSpli);
                stmt6.executeUpdate("UPDATE " + schema + ".CISPLIP SET KLICCO=" + klicco + " WHERE KLIPSP=" + idSpli);
            }
            stmt6.close();

            con.close();
        }
    }
}
