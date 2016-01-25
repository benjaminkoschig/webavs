package globaz.phenix.reprise;

import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAException;
import globaz.globall.util.JATime;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CPRepriseSuiviCaisse {

    // Fichier de sortie pour les erreurs warnings
    public static String chemin_Fichier_Sortie = "D:/Temp/echecSuiviCaisse.txt";
    // Info base de données ACCESS
    public static String chemin_Mdb = "d:\\Temp\\LaaLpp.mdb";
    public static String CODE_CAISSE_LAA_INCONNU = "INCONNU";
    public static String CODE_CAISSE_LPP_INCONNU = "INCONNU";

    public static String CODE_CAISSE_SUPPLETIVE_LAA = "SUPPL/LAA";

    // --------------------------------
    // Info donnée de l'appli

    public static final String CS_CAISSE_LAA = "509033";
    public static final String CS_CAISSE_LPP = "509012";
    public static final String CS_LAA_NON_D = "19190006";
    public static final String CS_LAA_NON_P_E_S = "19190005";
    public static final String CS_LPP_NON_C = "19190003";
    public static final String CS_LPP_NON_E_S = "19190002";

    public static final String CS_LPP_NON_P = "19190001";
    public static final String CS_LPP_NON_R = "19190004";

    public static final String CS_LPP_NON_T = "832002";
    public static String idTiersCaisseLaaInconnue = "";
    public static String idTiersCaisseLppInconnue = "";
    public static String idTiersCaisseSupplLaa = "";
    public static String NOM_CAISSE_LAA_INCONNU = "EMPLOYEUR CONTROLE AVANT 2010";
    public static String NOM_CAISSE_LPP_INCONNU = "EMPLOYEUR CONTROLE AVANT 2010";
    public static String NOM_CAISSE_SUPPLETIVE_LAA = "CAISSE SUPPLETIVE LAA et SUVA";

    public static final String PASS_ACCESS = "";
    public static String url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
    public static final String USER_ACCESS = "";

    /**
     * Création d'une administration
     * 
     * @param session
     * @param transaction
     * @param nom
     * @param code
     * @param genreAdmin
     * @throws Exception
     */
    public static String creerAdministration(BSession session, BTransaction transaction, String nom, String code,
            String genreAdmin) throws Exception {

        if (!JadeStringUtil.isBlankOrZero(nom) && (nom.length() > 39)) {

            CPRepriseSuiviCaisse.ecrire("ECRITURE CAISSE" + ";" + nom + ";" + code);

            String nomFormat = nom.substring(0, 37);
            nomFormat += "...";
            nom = nomFormat;

        }

        TIAdministrationViewBean adminBean = new TIAdministrationViewBean();
        adminBean.setSession(session);
        adminBean.setLangue(IConstantes.CS_TIERS_LANGUE_FRANCAIS);
        adminBean.setCanton(IConstantes.CS_LOCALITE_CANTON_VALAIS);
        adminBean.setIdPays(IConstantes.ID_PAYS_SUISSE);
        adminBean.setGenreAdministration(genreAdmin);
        adminBean.setCodeAdministration(code);
        adminBean.setCodeInstitution(code);
        adminBean.setPersonneMorale(new Boolean(true));
        adminBean.setDesignation1(nom);

        adminBean.add(transaction);

        if (adminBean.hasErrors()) {
            throw new Exception(adminBean.getErrors().toString());
        }

        if (adminBean.isNew()) {
            throw new Exception("Creation administration code : " + code + " / nom : " + nom + " impossible");
        }

        return adminBean.getIdTiersAdministration();
    }

    public static void creerSuiviAffiliation(BSession session, BTransaction transaction, AFAffiliation aff,
            String idTiersCaisse, String numContrat, String motif, boolean attestationIP, String genreCaisse)
            throws Exception {

        AFSuiviCaisseAffiliation af = new AFSuiviCaisseAffiliation();
        af.setSession(session);

        af.setAttestationIp(new Boolean(attestationIP));

        if (aff != null) { // L'affilie
            af.setDateDebut(aff.getDateDebut());
            af.setDateFin(aff.getDateFin());

            try {

                BSessionUtil.checkRealDateGregorian(session, aff.getDateDebut());
                BSessionUtil.checkRealDateGregorian(session, aff.getDateFin());

            } catch (JAException ja) {
                CPRepriseSuiviCaisse.ecrire(aff.getAffilieNumero() + ";Probleme de date sur l'affilié;"
                        + aff.getDateDebut() + ";" + aff.getDateFin());
                return;
            }

            if (!JadeStringUtil.isEmpty(aff.getDateDebut()) && !JadeStringUtil.isEmpty(aff.getDateFin())) {
                if (!BSessionUtil.compareDateFirstLowerOrEqual(session, aff.getDateDebut(), aff.getDateFin())) {
                    CPRepriseSuiviCaisse.ecrire(aff.getAffilieNumero()
                            + ";Probleme de date plus grande que la date de debut;" + aff.getDateDebut() + ";"
                            + aff.getDateFin());
                    return;
                }
            }

            af.setAffiliationId(aff.getAffiliationId());
        }
        if (genreCaisse != null) { // Le genre de caisse :
            // CodeSystem.GENRE_CAISSE_LPP ou
            // CodeSystem.GENRE_CAISSE_LAA;
            af.setGenreCaisse(genreCaisse);
        }
        if (!JadeStringUtil.isBlankOrZero(numContrat)) { // Le numero de contrat

            if (numContrat.length() > 14) {
                CPRepriseSuiviCaisse.ecrire(aff.getAffilieNumero() + ";Nom du contrat trop long;" + numContrat + ";");

                String nomFormat = numContrat.substring(0, 12);
                nomFormat += "...";
                numContrat = nomFormat;
            }

            af.setNumeroAffileCaisse(numContrat);
        }
        if (idTiersCaisse != null) { // Id Tiers de la caisse
            af.setIdTiersCaisse(idTiersCaisse);
        }
        if (motif != null) { // Motif
            af.setMotif(motif);
        }

        af.add(transaction);

        if (af.hasErrors()) {
            throw new Exception(af.getErrors().toString());
        }

        if (af.isNew()) {
            throw new Exception("Creation suivi affiliation : impossible");
        }
    }

    public static void creerSuiviAffiliation(BSession session, BTransaction transaction, AFAffiliation aff,
            String idTiersCaisse, String numContrat, String motif, String genreCaisse) throws Exception {
        CPRepriseSuiviCaisse.creerSuiviAffiliation(session, transaction, aff, idTiersCaisse, numContrat, motif, false,
                genreCaisse);
    }

    private static void ecrire(String text) {
        FileWriter ecriTemp = null;
        try {
            if (ecriTemp == null) {
                ecriTemp = new FileWriter(CPRepriseSuiviCaisse.chemin_Fichier_Sortie, true);
            }

            text += "\r\n";
            ecriTemp.write(text, 0, text.length());
            ecriTemp.flush();
        }// try
        catch (NullPointerException a) {
            System.out.println("Erreur : pointeur null");
        } catch (IOException a) {
            System.out.println("Problème d'IO");
        } finally {
            try {
                if (ecriTemp != null) {
                    ecriTemp.close();
                }
            } catch (IOException e) {
                System.out.println("Problème d'IO");
            }
        }
    }

    public static void gestionAffilie(BSession session, BTransaction transaction) throws Exception {
        int count = 0;

        Connection connection = null;

        try {

            connection = DriverManager.getConnection(CPRepriseSuiviCaisse.url, CPRepriseSuiviCaisse.USER_ACCESS,
                    CPRepriseSuiviCaisse.PASS_ACCESS);
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select * from fb2008 where NoAffilie>'067.070.7030'");
            while (rs.next()) {
                String numAffilie = rs.getString("NoAffilie");
                String Adresse1 = rs.getString("Adresse1");
                String Adresse2 = rs.getString("Adresse2");
                String lpp = rs.getString("LppON");
                String codeLpp = rs.getString("CodeLpp");
                String caisseLpp = rs.getString("Caisse LPP");
                String NoContratLpp = rs.getString("NoContratLPP");
                String laa = rs.getString("LAA");
                String codeLaa = rs.getString("LAACode");
                String caisseLaa = rs.getString("Caisse LAA");
                String NoContratLaa = rs.getString("No LAA");

                // Recherche du numero d'affilié
                AFAffiliation affilie = CPRepriseSuiviCaisse.getAfAffiliation(
                        CPRepriseSuiviCaisse.getNumAffilieFormat(numAffilie), session, transaction);

                if (affilie != null) {

                    // --------------------------
                    // GESTION LPP
                    // --------------------------

                    String idTiersCaisseLpp = CPRepriseSuiviCaisse.getIdTiersCaisse(session, transaction, caisseLpp,
                            CPRepriseSuiviCaisse.CS_CAISSE_LPP);

                    if ("OUI".equals(lpp)) {

                        // Si manque d'info : Utiliser caisse fictive
                        if (!JadeStringUtil.isBlankOrZero(codeLpp) && "A".equals(codeLpp)) {

                            if (idTiersCaisseLpp != null) {
                                CPRepriseSuiviCaisse.creerSuiviAffiliation(session, transaction, affilie,
                                        idTiersCaisseLpp, NoContratLpp, null, true, CodeSystem.GENRE_CAISSE_LPP);
                            } else {
                                CPRepriseSuiviCaisse.creerSuiviAffiliation(session, transaction, affilie,
                                        CPRepriseSuiviCaisse.idTiersCaisseLppInconnue, NoContratLpp, null, true,
                                        CodeSystem.GENRE_CAISSE_LPP);
                            }
                        } else if (!JadeStringUtil.isBlankOrZero(codeLpp)) {
                            if (idTiersCaisseLpp != null) {
                                CPRepriseSuiviCaisse.creerSuiviAffiliation(session, transaction, affilie,
                                        idTiersCaisseLpp, NoContratLpp, null, CodeSystem.GENRE_CAISSE_LPP);
                            } else {
                                CPRepriseSuiviCaisse.creerSuiviAffiliation(session, transaction, affilie,
                                        CPRepriseSuiviCaisse.idTiersCaisseLppInconnue, NoContratLpp, null,
                                        CodeSystem.GENRE_CAISSE_LPP);
                            }
                        } else {
                            CPRepriseSuiviCaisse.ecrire(numAffilie + ";CodeLpp non renseigné;");
                        }

                    } else if ("NON".equals(lpp)) {

                        if (!JadeStringUtil.isBlankOrZero(codeLpp)) {
                            if ("P".equals(codeLpp)) {
                                CPRepriseSuiviCaisse.creerSuiviAffiliation(session, transaction, affilie, null,
                                        "NON P", CPRepriseSuiviCaisse.CS_LPP_NON_P, CodeSystem.GENRE_CAISSE_LPP);
                            } else if ("E".equals(codeLpp) || "S".equals(codeLpp)) {
                                CPRepriseSuiviCaisse.creerSuiviAffiliation(session, transaction, affilie, null,
                                        "NON E", CPRepriseSuiviCaisse.CS_LPP_NON_E_S, CodeSystem.GENRE_CAISSE_LPP);
                            } else if ("C".equals(codeLpp)) {
                                CPRepriseSuiviCaisse.creerSuiviAffiliation(session, transaction, affilie, null,
                                        "NON C", CPRepriseSuiviCaisse.CS_LPP_NON_C, CodeSystem.GENRE_CAISSE_LPP);
                            } else if ("T".equals(codeLpp)) {
                                CPRepriseSuiviCaisse.creerSuiviAffiliation(session, transaction, affilie, null,
                                        "NON T", CPRepriseSuiviCaisse.CS_LPP_NON_T, CodeSystem.GENRE_CAISSE_LPP);
                            } else if ("R".equals(codeLpp)) {
                                CPRepriseSuiviCaisse.creerSuiviAffiliation(session, transaction, affilie, null,
                                        "NON R", CPRepriseSuiviCaisse.CS_LPP_NON_R, CodeSystem.GENRE_CAISSE_LPP);
                            } else {
                                CPRepriseSuiviCaisse.ecrire(numAffilie + ";CodeLpp non connu;" + codeLpp);
                            }
                        } else {
                            CPRepriseSuiviCaisse.ecrire(numAffilie + ";CodeLpp non renseigné;");
                        }

                    } else if ((lpp != null) && !"".equals(lpp)) { // SI autre que
                        // OUI et
                        // NON,
                        // message
                        // d'erreur
                        CPRepriseSuiviCaisse.ecrire(numAffilie + ";LppOn n'est pas correct;" + lpp);
                    }

                    // --------------------------
                    // GESTION LAA
                    // --------------------------

                    String idTiersCaisseLaa = CPRepriseSuiviCaisse.getIdTiersCaisse(session, transaction, caisseLaa,
                            CPRepriseSuiviCaisse.CS_CAISSE_LAA);

                    if ("OUI".equals(laa)) {
                        // Si manque d'info : Utiliser caisse fictive
                        if (!JadeStringUtil.isBlankOrZero(codeLaa) && "A".equals(codeLaa)) {

                            if (idTiersCaisseLaa != null) {
                                CPRepriseSuiviCaisse.creerSuiviAffiliation(session, transaction, affilie,
                                        idTiersCaisseLaa, NoContratLaa, null, true, CodeSystem.GENRE_CAISSE_LAA);
                            } else {
                                CPRepriseSuiviCaisse.creerSuiviAffiliation(session, transaction, affilie,
                                        CPRepriseSuiviCaisse.idTiersCaisseLaaInconnue, NoContratLaa, null, true,
                                        CodeSystem.GENRE_CAISSE_LAA);
                            }
                        } else if (!JadeStringUtil.isBlankOrZero(codeLaa)) {
                            if (idTiersCaisseLaa != null) {
                                CPRepriseSuiviCaisse.creerSuiviAffiliation(session, transaction, affilie,
                                        idTiersCaisseLaa, NoContratLaa, null, CodeSystem.GENRE_CAISSE_LAA);
                            } else {
                                CPRepriseSuiviCaisse.creerSuiviAffiliation(session, transaction, affilie,
                                        CPRepriseSuiviCaisse.idTiersCaisseLaaInconnue, NoContratLaa, null,
                                        CodeSystem.GENRE_CAISSE_LAA);
                            }
                        } else {
                            CPRepriseSuiviCaisse.ecrire(numAffilie + ";CodeLaa non renseigné;");
                        }

                    } else if ("NON".equals(laa)) {

                        if (!JadeStringUtil.isBlankOrZero(codeLaa)) {
                            if ("P".equals(codeLaa) || "E".equals(codeLaa) || "S".equals(codeLaa)) {
                                CPRepriseSuiviCaisse.creerSuiviAffiliation(session, transaction, affilie, null,
                                        "NON P", CPRepriseSuiviCaisse.CS_LAA_NON_P_E_S, CodeSystem.GENRE_CAISSE_LAA);
                            } else if ("A".equals(codeLaa)) {
                                CPRepriseSuiviCaisse.creerSuiviAffiliation(session, transaction, affilie,
                                        CPRepriseSuiviCaisse.idTiersCaisseSupplLaa, null, null,
                                        CodeSystem.GENRE_CAISSE_LAA);
                            } else if ("D".equals(codeLaa)) {
                                CPRepriseSuiviCaisse.creerSuiviAffiliation(session, transaction, affilie, null,
                                        "NON D", CPRepriseSuiviCaisse.CS_LAA_NON_D, CodeSystem.GENRE_CAISSE_LAA);
                            } else {
                                CPRepriseSuiviCaisse.ecrire(numAffilie + ";CodeLaa non connu;" + codeLaa);
                            }
                        } else {
                            CPRepriseSuiviCaisse.ecrire(numAffilie + ";CodeLaa non renseigné;");
                        }

                    } else if (JadeStringUtil.isBlankOrZero(laa) && "A".equals(codeLaa)) {
                        // Neant : Caisse suppl/laa
                        CPRepriseSuiviCaisse.creerSuiviAffiliation(session, transaction, affilie,
                                CPRepriseSuiviCaisse.idTiersCaisseSupplLaa, null, null, CodeSystem.GENRE_CAISSE_LAA);

                    } else if ((laa != null) && !"".equals(laa)) { // SI non vide
                        // et autre
                        // que OUI
                        // et NON,
                        // message
                        // d'erreur
                        CPRepriseSuiviCaisse.ecrire(numAffilie + ";Laa n'est pas correct;" + laa);
                    }

                } else {
                    CPRepriseSuiviCaisse.ecrire(numAffilie + ";Erreur numero affilie introuvable;" + Adresse1 + ";"
                            + Adresse2);
                }
                count++;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            System.out.println("count : " + count);
        }

    }

    /**
     * 
     * @param session
     * @param transaction
     */
    public static void gestionLaa(BSession session, BTransaction transaction) {

        int count = 0;
        int count1 = 0;
        int count2 = 0;
        Connection connection = null;

        try {

            connection = DriverManager.getConnection(CPRepriseSuiviCaisse.url, CPRepriseSuiviCaisse.USER_ACCESS,
                    CPRepriseSuiviCaisse.PASS_ACCESS);
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select * from laanew");
            while (rs.next()) {
                String code = rs.getString("Code");
                String nom = rs.getString("Nom1");

                if (CPRepriseSuiviCaisse.getIdTiersCaisse(session, transaction, code,
                        CPRepriseSuiviCaisse.CS_CAISSE_LAA) != null) {
                    count1++;
                } else {
                    CPRepriseSuiviCaisse.creerAdministration(session, transaction, nom, code,
                            CPRepriseSuiviCaisse.CS_CAISSE_LAA);
                    count2++;
                }

                count++;
            }
            System.out.println("Caisse LAA : count " + count + " / Présentes " + count1 + "/ Créées " + count2);

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.close();
                } catch (SQLException sql) {
                    sql.printStackTrace();
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.commit();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static void gestionLpp(BSession session, BTransaction transaction) {

        int count = 0;
        int count1 = 0;
        int count2 = 0;
        Connection connection = null;

        try {

            connection = DriverManager.getConnection(CPRepriseSuiviCaisse.url, CPRepriseSuiviCaisse.USER_ACCESS,
                    CPRepriseSuiviCaisse.PASS_ACCESS);
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select * from lppnew");
            while (rs.next()) {
                String code = rs.getString("Code");
                String nom = rs.getString("Nom1");

                if (CPRepriseSuiviCaisse.getIdTiersCaisse(session, transaction, code,
                        CPRepriseSuiviCaisse.CS_CAISSE_LPP) != null) {
                    count1++;
                } else {
                    CPRepriseSuiviCaisse.creerAdministration(session, transaction, nom, code,
                            CPRepriseSuiviCaisse.CS_CAISSE_LPP);
                    count2++;
                }

                count++;
            }
            System.out.println("Caisse LPP : count " + count + " / Présentes " + count1 + "/ Créées " + count2);

        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.close();
                } catch (SQLException sql) {
                    sql.printStackTrace();
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.commit();
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * Recuperation de l'objet affilié a partir d'un numero d'affilié
     * 
     * @param numero
     * @param session
     * @param transaction
     * @return
     * @throws Exception
     */
    public static AFAffiliation getAfAffiliation(String numero, BSession session, BTransaction transaction)
            throws Exception {

        if (!JadeStringUtil.isBlankOrZero(numero)) {
            AFAffiliationManager affManager = new AFAffiliationManager();

            affManager.setSession(session);
            affManager.setForAffilieNumero(numero);
            affManager.setForTypesAffPersonelles();
            affManager.find(transaction);

            if (affManager.size() > 0) {
                AFAffiliation aff = (AFAffiliation) affManager.getFirstEntity();
                return aff;
            }
        }
        return null;
    }

    public static String getChemin_Fichier_Sortie() {
        return CPRepriseSuiviCaisse.chemin_Fichier_Sortie;
    }

    public static String getChemin_Mdb() {
        return CPRepriseSuiviCaisse.chemin_Mdb;
    }

    public static String getIdTiersCaisse(BSession session, BTransaction transaction, String code,
            String genreAdministration) throws Exception {

        if (!JadeStringUtil.isBlankOrZero(code)) {
            TIAdministrationManager manager = new TIAdministrationManager();
            manager.setSession(session);
            manager.setForCodeAdministration(code);
            if (genreAdministration != null) {
                manager.setForGenreAdministration(genreAdministration);
            }
            manager.find(transaction);

            if (manager.size() > 0) {
                TIAdministrationViewBean administration = (TIAdministrationViewBean) manager.getFirstEntity();
                return administration.getIdTiersAdministration();
            }
        }
        return null;
    }

    public static String getNumAffilieFormat(String num) {
        String retour = "0";
        // "129.513.7001" donne "129.513.007001"
        if ((num != null) && (num.length() == 12)) {

            retour = num.substring(0, 8);
            retour += "00";
            retour += num.substring(8);
        }
        return retour;
    }

    private static String getTimeHHMMSS(long timeMilli) {
        JATime time = new JATime(timeMilli);
        return time.getHour() + "h " + time.getMinute() + "m " + time.getSecond() + "s";
    }

    public static String getUrl() {
        return CPRepriseSuiviCaisse.url;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        long endComputeTime;
        long startComputeTime = System.currentTimeMillis();
        String user = "oca";
        String pwd = "oca";

        BSession session = null;
        BTransaction transaction = null;

        try {
            if (!JadeStringUtil.isEmpty(args[0]) && !JadeStringUtil.isEmpty(args[1])) {
                user = args[0];
                pwd = args[1];
            }
            if (!JadeStringUtil.isEmpty(args[2])) {
                CPRepriseSuiviCaisse.setChemin_Mdb(args[2]);
            }
            if (!JadeStringUtil.isEmpty(args[3])) {
                CPRepriseSuiviCaisse.setChemin_Fichier_Sortie(args[3]);
            }
            CPRepriseSuiviCaisse.setUrl(CPRepriseSuiviCaisse.getUrl() + CPRepriseSuiviCaisse.getChemin_Mdb());
            session = new BSession("NAOS");
            session.connect(user, pwd);
            transaction = new BTransaction(session);

            System.out.println("Debut de mise a jour des suivis ....");

            // ---------------------------------
            // Gestion des caisses
            // ---------------------------------
            // gestionLaa(session, transaction);
            // gestionLpp(session, transaction);
            // ---------------------------------

            // Recuperation des id tiers des caisses inconnues
            CPRepriseSuiviCaisse.recuperationIdCaisseInconnue(session, transaction);

            // traitement des données
            CPRepriseSuiviCaisse.gestionAffilie(session, transaction);

            endComputeTime = System.currentTimeMillis();
            System.out.println("Fin du traitement .... ("
                    + CPRepriseSuiviCaisse.getTimeHHMMSS(endComputeTime - startComputeTime) + ")");

        } catch (Exception e) {
            e.printStackTrace();
            if ((transaction != null)) {
                try {
                    transaction.rollback();
                    transaction.closeTransaction();
                } catch (Exception tr) {
                    tr.printStackTrace();
                }
            }
        } finally {
            if ((transaction != null)) {
                try {
                    // transaction.rollback();
                    transaction.commit();
                    transaction.closeTransaction();
                } catch (Exception tr) {
                    tr.printStackTrace();
                }
            }

            System.exit(0);
        }
    }

    public static void recuperationIdCaisseInconnue(BSession session, BTransaction transaction) throws Exception {

        CPRepriseSuiviCaisse.idTiersCaisseLaaInconnue = CPRepriseSuiviCaisse.getIdTiersCaisse(session, transaction,
                CPRepriseSuiviCaisse.NOM_CAISSE_LPP_INCONNU, CPRepriseSuiviCaisse.CS_CAISSE_LPP);
        if (CPRepriseSuiviCaisse.idTiersCaisseLaaInconnue == null) {
            CPRepriseSuiviCaisse.idTiersCaisseLaaInconnue = CPRepriseSuiviCaisse.creerAdministration(session,
                    transaction, CPRepriseSuiviCaisse.NOM_CAISSE_LPP_INCONNU,
                    CPRepriseSuiviCaisse.CODE_CAISSE_LPP_INCONNU, CPRepriseSuiviCaisse.CS_CAISSE_LPP);
        }
        CPRepriseSuiviCaisse.idTiersCaisseLppInconnue = CPRepriseSuiviCaisse.getIdTiersCaisse(session, transaction,
                CPRepriseSuiviCaisse.NOM_CAISSE_LAA_INCONNU, CPRepriseSuiviCaisse.CS_CAISSE_LAA);
        if (CPRepriseSuiviCaisse.idTiersCaisseLppInconnue == null) {
            CPRepriseSuiviCaisse.idTiersCaisseLppInconnue = CPRepriseSuiviCaisse.creerAdministration(session,
                    transaction, CPRepriseSuiviCaisse.NOM_CAISSE_LAA_INCONNU,
                    CPRepriseSuiviCaisse.CODE_CAISSE_LAA_INCONNU, CPRepriseSuiviCaisse.CS_CAISSE_LAA);
        }

        CPRepriseSuiviCaisse.idTiersCaisseSupplLaa = CPRepriseSuiviCaisse.getIdTiersCaisse(session, transaction,
                CPRepriseSuiviCaisse.NOM_CAISSE_SUPPLETIVE_LAA, CPRepriseSuiviCaisse.CS_CAISSE_LAA);
        if (CPRepriseSuiviCaisse.idTiersCaisseSupplLaa == null) {
            CPRepriseSuiviCaisse.idTiersCaisseSupplLaa = CPRepriseSuiviCaisse.creerAdministration(session, transaction,
                    CPRepriseSuiviCaisse.NOM_CAISSE_SUPPLETIVE_LAA, CPRepriseSuiviCaisse.CODE_CAISSE_SUPPLETIVE_LAA,
                    CPRepriseSuiviCaisse.CS_CAISSE_LAA);
        }
    }

    public static void setChemin_Fichier_Sortie(String chemin_Fichier_Sortie) {
        CPRepriseSuiviCaisse.chemin_Fichier_Sortie = chemin_Fichier_Sortie;
    }

    public static void setChemin_Mdb(String chemin_Mdb) {
        CPRepriseSuiviCaisse.chemin_Mdb = chemin_Mdb;
    }

    public static void setUrl(String url) {
        CPRepriseSuiviCaisse.url = url;
    }
}
