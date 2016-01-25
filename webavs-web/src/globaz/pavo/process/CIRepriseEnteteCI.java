package globaz.pavo.process;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CIRepriseEnteteCI {

    /**
     * @author MKA
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        if (args.length < 5) {
            System.out.println("Liste des arguments à renseigner :");
            System.out.println("args0 = driver de connection (exemple :com.ibm.as400...)");
            System.out.println("args1 = url de la base de données (exemple : jdbc:/as400://xx.yyy.zz.uu)");
            System.out.println("args2 = schema de la table source");
            System.out.println("args3 = nom d'utilisateur (pour la base de données)");
            System.out.println("args4 = mot de passe (pour la base de données)");
            throw new Exception("Wrong number of arguments");
        }

        Date date = new Date();
        System.out.println("Debut Programme : " + date.toString());

        Connection lecture = null;
        Connection ecriture = null;
        String driver = args[0];
        String url = args[1];
        String schema = args[2];
        String user = args[3];
        String password = args[4];

        Class.forName(driver).newInstance();
        lecture = DriverManager.getConnection(url, user, password);
        ecriture = DriverManager.getConnection(url, user, password);

        Statement liaison_update = ecriture.createStatement();
        try {
            liaison_update.executeUpdate("DROP TABLE " + schema + ".LIAISON_UPD");
            liaison_update.executeUpdate("DROP TABLE " + schema + ".LIAISON_UPD2");
        } catch (Exception e) {
        }
        liaison_update.executeUpdate("CREATE TABLE " + schema
                + ".LIAISON_UPD(ID_FAMILLE NUMERIC(15), KANAVS VARCHAR(24), KAIIND NUMERIC(15), KAIINR NUMERIC(15))");
        liaison_update.close();

        Statement stmt = lecture.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT DISTINCT * FROM " + schema + ".CIINDIP A, " + schema + ".NSSRA B"
                + " WHERE A.KAIREG=309001 AND A.KANAVS LIKE '756%' AND LENGTH(A.KANAVS)>=12"
                + " AND CAST(A.KANAVS AS DECIMAL(20))=B.NSS" + " ORDER BY A.KANAVS");

        PreparedStatement updater = ecriture.prepareStatement("INSERT INTO " + schema
                + ".LIAISON_UPD (ID_FAMILLE, KANAVS, KAIIND, KAIINR) VALUES(?,?,?,?)");
        int insert = 0;
        int numer = 0;
        int idfamily = 0;

        String nssToCheck = "";
        String avsToCheck = "";

        List numCtrl = new ArrayList();

        while (rs.next()) {

            String nss = rs.getString("KANAVS").trim();
            BigDecimal id = rs.getBigDecimal("KAIIND");
            BigDecimal avs = rs.getBigDecimal("NAVS");

            if (nssToCheck.equals(nss)) {
                if (0 != avs.intValue() && !avsToCheck.equals(avs.toString()) && !nssToCheck.equals(avs.toString())) {
                    avsToCheck = avs.toString();

                    if (!numCtrl.contains(avs)) {
                        updater.setInt(1, idfamily);
                        updater.setString(2, avs.toString());
                        updater.setBigDecimal(3, null);
                        updater.setInt(4, 0);
                        updater.addBatch();
                        numCtrl.add(avs);
                        numer++;
                        insert++;
                    }
                }
            } else {
                idfamily++;
                nssToCheck = nss;
                numCtrl.clear();

                if (!numCtrl.contains(nss)) {
                    updater.setInt(1, idfamily);
                    updater.setString(2, nss);
                    updater.setBigDecimal(3, id);
                    updater.setInt(4, 0);
                    updater.addBatch();
                    numCtrl.add(nss);

                    numer++;
                    insert++;
                }

                if (0 != avs.intValue() && !avsToCheck.equals(avs.toString()) && !nssToCheck.equals(avs.toString())) {
                    avsToCheck = avs.toString();
                    if (!numCtrl.contains(avs)) {
                        updater.setInt(1, idfamily);
                        updater.setString(2, avs.toString());
                        updater.setBigDecimal(3, null);
                        updater.setInt(4, 0);
                        updater.addBatch();
                        numCtrl.add(avs);
                        numer++;
                        insert++;
                    }

                }
            }

            if (numer >= 1000) {
                updater.executeBatch();
                updater.clearBatch();
                numer = 0;
            }
        }
        updater.executeBatch();
        updater.close();
        rs.close();
        stmt.close();
        System.out.println("*********************************************************");
        System.out.println("Total lignes insérées : " + insert);
        Date dateFirstEnd = new Date();
        System.out.println("Fin première phase : " + dateFirstEnd.toString());

        Statement stmt2 = ecriture.createStatement();

        stmt2.executeUpdate("UPDATE " + schema + ".LIAISON_UPD A " + "SET A.KAIIND = (SELECT MAX(B.KAIIND) FROM "
                + schema + ".CIINDIP B WHERE B.KAIREG = 309001 AND A.KANAVS = B.KANAVS)" + " WHERE A.KAIIND IS NULL");
        stmt2.close();

        Statement stmt3 = ecriture.createStatement();
        stmt3.executeUpdate("DELETE FROM " + schema + ".LIAISON_UPD WHERE KAIIND IS NULL");
        stmt3.close();

        Date dateSecondEnd = new Date();
        System.out.println("Fin deuxième partie : " + dateSecondEnd.toString());

        Statement reader = lecture.createStatement();
        ResultSet rs2 = reader.executeQuery("SELECT * FROM " + schema + ".LIAISON_UPD" + " WHERE ID_FAMILLE IN ("
                + "SELECT ID_FAMILLE FROM " + schema
                + ".LIAISON_UPD GROUP BY ID_FAMILLE HAVING COUNT(ID_FAMILLE)>1) order by id_famille");

        ecriture.setAutoCommit(false);
        PreparedStatement ps3 = ecriture.prepareStatement("UPDATE " + schema
                + ".CIINDIP SET KAIINR = ? WHERE KANAVS = ? AND KAIREG=309001");

        int fam = 0;
        Map oneFamily = new HashMap();
        Map familyWithoutParent = new HashMap();

        while (rs2.next()) { // essaie sur la table intermédiaire mais a faire
            // sur CIINDIP + Attention prob de deadlock et
            // timeout

            BigDecimal idFamille = rs2.getBigDecimal("ID_FAMILLE");
            String kanavs = rs2.getString("KANAVS");
            BigDecimal kaiind = rs2.getBigDecimal("KAIIND");

            if (fam == idFamille.intValue()) { // si on traite toujours la meme
                // famille on rajoute
                // meme famille
                oneFamily.put(kanavs, kaiind);
                familyWithoutParent.put(kanavs, kaiind);
            } else {
                fam = idFamille.intValue();

                if (oneFamily.isEmpty()) {
                    oneFamily.put(kanavs, kaiind);
                    familyWithoutParent.put(kanavs, kaiind);
                } else {
                    boolean hasNss = false;

                    for (Iterator iterator = oneFamily.keySet().iterator(); iterator.hasNext();) {
                        String key = (String) iterator.next();
                        BigDecimal lien = (BigDecimal) oneFamily.get(key);

                        if (key.startsWith("756") && key.length() >= 12) {
                            hasNss = true;
                            familyWithoutParent.remove(key);
                            ps3.setBigDecimal(1, lien);
                            ps3.setString(2, key);
                            ps3.addBatch();

                            for (Iterator it = familyWithoutParent.keySet().iterator(); it.hasNext();) {
                                String keyEnfant = (String) it.next();
                                BigDecimal lienEnfant = (BigDecimal) familyWithoutParent.get(keyEnfant);

                                ps3.setBigDecimal(1, lien);
                                ps3.setString(2, keyEnfant);
                                ps3.addBatch();

                                lien = lienEnfant;
                            }
                            break;
                        } else {
                            continue;
                        }
                    }
                    BigDecimal lien = null;
                    if (!hasNss) {
                        for (Iterator t = oneFamily.keySet().iterator(); t.hasNext();) {
                            String key = (String) t.next();
                            BigDecimal lien2 = (BigDecimal) oneFamily.get(key);

                            if (lien == null) {
                                lien = lien2;
                            }

                            ps3.setBigDecimal(1, lien);
                            ps3.setString(2, key);
                            ps3.addBatch();

                            lien = lien2;
                        }
                    }
                    ps3.executeBatch();
                    ps3.clearBatch();
                    ecriture.commit();
                    oneFamily.clear();
                    familyWithoutParent.clear();
                    oneFamily.put(kanavs, kaiind);
                    familyWithoutParent.put(kanavs, kaiind);
                }
            }
        }
        rs2.close();
        reader.close();
        ps3.close();
        Date finProg = new Date();
        System.out.println("Fin troisième partie : " + finProg.toString());

        Statement liaison_update2 = ecriture.createStatement();
        liaison_update2.executeUpdate("CREATE TABLE " + schema
                + ".LIAISON_UPD2(ID_FAMILLE NUMERIC(15), KANAVS VARCHAR(24), KAIIND NUMERIC(15), KAIINR NUMERIC(15))");
        liaison_update2.close();
        ecriture.commit();

        Statement stmt5 = lecture.createStatement();
        ResultSet rs5 = stmt5.executeQuery("SELECT * FROM " + schema + ".CIINDIP A INNER JOIN " + schema + ".NSSRA B"
                + " ON CAST(A.KANAVS AS DECIMAL(20)) = B.NAVS" + " AND A.KAIINR = 0 AND KAIREG = 309001"
                + " ORDER BY B.NSS");

        PreparedStatement ps4 = ecriture.prepareStatement("INSERT INTO " + schema
                + ".LIAISON_UPD2 (ID_FAMILLE, KANAVS, KAIIND, KAIINR) VALUES(?,?,?,?)");

        BigDecimal control = null;
        int count = 0;
        int clearer = 0;

        while (rs5.next()) {
            BigDecimal nss = rs5.getBigDecimal("NSS");
            BigDecimal kaiind = rs5.getBigDecimal("KAIIND");
            BigDecimal navs = rs5.getBigDecimal("NAVS");

            if ("0".equals(nss.toString())) {
                Statement p = ecriture.createStatement();
                p.executeUpdate("UPDATE " + schema + ".CIINDIP SET KAIINR = " + kaiind + " WHERE KAIIND = " + kaiind);
                p.close();
                ecriture.commit();
            } else {
                if (nss.equals(control)) {
                    ps4.setInt(1, count);
                    ps4.setString(2, navs.toString());
                    ps4.setBigDecimal(3, kaiind);
                    ps4.setInt(4, 0);
                    ps4.addBatch();
                    clearer++;
                    // meme famille
                } else {
                    count++;
                    control = nss;

                    ps4.setInt(1, count);
                    ps4.setString(2, navs.toString());
                    ps4.setBigDecimal(3, kaiind);
                    ps4.setInt(4, 0);
                    ps4.addBatch();
                    clearer++;
                }
            }

            if (clearer >= 1000) {
                ps4.executeBatch();
                ps4.clearBatch();
                ecriture.commit();
                clearer = 0;
            }
        }
        ps4.executeBatch();
        ecriture.commit();
        rs5.close();
        stmt5.close();
        ps4.close();

        Statement stmtF = lecture.createStatement();
        ResultSet rsF = stmtF.executeQuery("SELECT * FROM " + schema + ".LIAISON_UPD2");
        PreparedStatement psF = ecriture.prepareStatement("UPDATE " + schema
                + ".CIINDIP SET KAIINR = ? WHERE KAIIND = ? AND KAIREG=309001");

        BigDecimal famEnCours = null;
        BigDecimal kaiindParent = null;
        int x = 0;

        while (rsF.next()) {
            BigDecimal kaiind = rsF.getBigDecimal("KAIIND");
            BigDecimal idFam = rsF.getBigDecimal("ID_FAMILLE");

            if (idFam.equals(famEnCours)) {
                psF.setBigDecimal(1, kaiindParent);
                psF.setBigDecimal(2, kaiind);
                psF.addBatch();
                kaiindParent = kaiind;
                x++;
            } else {
                famEnCours = idFam;
                kaiindParent = kaiind;

                psF.setBigDecimal(1, kaiind);
                psF.setBigDecimal(2, kaiind);
                psF.addBatch();
                x++;
            }

            if (x >= 1000) {
                psF.executeBatch();
                psF.clearBatch();
                ecriture.commit();
                x = 0;
            }
        }
        psF.executeBatch();
        ecriture.commit();
        rsF.close();
        stmtF.close();
        psF.close();

        Statement s = lecture.createStatement();
        ResultSet RS = s.executeQuery("SELECT A.KAIIND, A.KAIINR, A.KANAVS, B.NSS, B.NAVS FROM " + schema
                + ".CIINDIP A, " + schema + ".NSSRA B"
                + " WHERE CAST(A.KANAVS AS DECIMAL(20)) = B.NAVS AND A.KAIREG = 309001 AND A.KAIINR = 0"
                + " ORDER BY NSS");
        PreparedStatement p = ecriture.prepareStatement("UPDATE " + schema
                + ".CIINDIP SET KAIINR = ? WHERE KANAVS = ? AND KAIREG=309001");

        BigDecimal nss = null;
        BigDecimal parent = null;
        int y = 0;

        while (RS.next()) {
            BigDecimal kaiind = RS.getBigDecimal("KAIIND");
            BigDecimal ns = RS.getBigDecimal("NSS");
            String kanavs = RS.getString("KANAVS");

            if (ns.equals(nss)) {
                p.setBigDecimal(1, parent);
                p.setString(2, kanavs);
                p.addBatch();
                parent = kaiind;
                y++;
            } else {
                nss = ns;
                parent = kaiind;

                p.setBigDecimal(1, kaiind);
                p.setString(2, kanavs);
                p.addBatch();
                y++;
            }

            if (y >= 2000) {
                p.executeBatch();
                p.clearBatch();
                ecriture.commit();
                y = 0;
            }
        }
        p.executeBatch();
        ecriture.commit();
        RS.close();
        s.close();
        p.close();
        Date f4 = new Date();
        System.out.println("Fin quatrième étape : " + f4.toString());

        // MAJ DU SEXE (kanavs - nss)
        Statement fin = lecture.createStatement();
        ResultSet rsFin = fin.executeQuery("SELECT * FROM " + schema + ".CIINDIP A INNER JOIN " + schema + ".NSSRA B"
                + " ON CAST(A.KANAVS AS DECIMAL(20)) = B.NSS WHERE A.KAIREG=309001 AND B.VALID=1");
        PreparedStatement ps5 = ecriture.prepareStatement("UPDATE " + schema
                + ".CIINDIP SET KATSEX = ? WHERE KANAVS = ? AND KAIREG=309001");
        int num = 0;

        while (rsFin.next()) {
            BigDecimal sexeJuste = rsFin.getBigDecimal("SEXE");
            String kanavs = rsFin.getString("KANAVS");

            if (1 == sexeJuste.intValue()) {
                ps5.setInt(1, 316000);
            } else {
                if (2 == sexeJuste.intValue()) {
                    ps5.setInt(1, 316001);
                }
            }
            ps5.setString(2, kanavs);
            ps5.addBatch();
            num++;

            if (num >= 2000) {
                ps5.executeBatch();
                ps5.clearBatch();
                ecriture.commit();
                num = 0;
            }
        }
        ps5.executeBatch();
        ecriture.commit();
        rsFin.close();
        fin.close();
        ps5.close();
        Date f5 = new Date();
        System.out.println("Fin cinquième partie : " + f5.toString());

        // maj sexe (kanavs - navs)
        Statement stmt6 = lecture.createStatement();
        ResultSet rs6 = stmt6.executeQuery("SELECT * FROM " + schema + ".CIINDIP A INNER JOIN " + schema + ".NSSRA B"
                + " ON CAST(A.KANAVS AS DECIMAL(20)) = B.NAVS WHERE A.KAIREG=309001 AND B.VALID=1");
        PreparedStatement ps6 = ecriture.prepareStatement("UPDATE " + schema
                + ".CIINDIP SET KATSEX = ? WHERE KANAVS = ? AND KAIREG=309001");
        int num2 = 0;

        while (rs6.next()) {
            BigDecimal sexeJuste = rs6.getBigDecimal("SEXE");
            String kanavs = rs6.getString("KANAVS");

            if (1 == sexeJuste.intValue()) {
                ps6.setInt(1, 316000);
            } else {
                if (2 == sexeJuste.intValue()) {
                    ps6.setInt(1, 316001);
                }
            }
            ps6.setString(2, kanavs);
            ps6.addBatch();
            num2++;

            if (num2 >= 2000) {
                ps6.executeBatch();
                ps6.clearBatch();
                ecriture.commit();
                num2 = 0;
            }
        }
        ps6.executeBatch();
        ecriture.commit();
        rs6.close();
        stmt6.close();
        ps6.close();
        Date f6 = new Date();
        System.out.println("Fin sixième partie : " + f6.toString());

        // maj des liens
        Statement stmt7 = ecriture.createStatement();
        stmt7.executeUpdate("UPDATE " + schema + ".CIINDIP SET KAIINR = 0 "
                + "WHERE KAIIND = KAIINR AND KAIREG=309001 " + "AND KAIIND IN (SELECT KAIINR FROM " + schema
                + ".CIINDIP WHERE KAIREG=309001 GROUP BY KAIINR HAVING COUNT(KAIINR)<2)");
        ecriture.commit();
        stmt7.close();
        Date f7 = new Date();
        System.out.println("Fin septième partie : " + f7.toString());

        lecture.close();
        ecriture.close();
        System.out.println("Fin de la reprise");
        System.exit(0);
    }
}
