package globaz.pavo.util;

import globaz.globall.util.JACalendar;
import globaz.globall.util.JAException;
import globaz.jade.client.util.JadeStringUtil;
import java.util.Arrays;
import java.util.TreeMap;

/**
 * @author jmc
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CIAvsUtil {
    /**
     * Constructor for CIAvsUtil.
     */
    private static TreeMap alphabetMap = new TreeMap();
    private static TreeMap exceptionsEVoyelleMap = new TreeMap();
    private final static int NBRE_MAX_LETTRES_NOM_PARTICULE = 3;

    private static String[] particules = { "AL", "AM", "DA", "DE", "DI", "DO", "DU", "LA", "LE", "LI", "LO", "VON" };

    /**
     * Method checkDeuxiemeGroupe.
     * 
     * @param dateNaissance
     * @param numAvs
     * @return boolean
     */
    public static boolean checkDeuxiemeGroupe(String dateNaissance, String numAvs) {

        try {
            if (JACalendar.getYear(dateNaissance) - 1900 == Integer.parseInt(numAvs.substring(3, 5))
                    || JACalendar.getYear(dateNaissance) - 2000 == Integer.parseInt(numAvs.substring(3, 5))) {
                return true;
            } else {
                return false;
            }
        } catch (JAException e) {
            return false;
        }

    }

    public static String checkNomPrenom(String nomPrenom) {
        try {
            if (" ".equals(nomPrenom.substring(nomPrenom.indexOf(",") + 1, nomPrenom.indexOf(",") + 2))) {
                nomPrenom = nomPrenom.substring(0, nomPrenom.indexOf(",") + 1)
                        + nomPrenom.substring(nomPrenom.indexOf(",") + 2);
            }
            return nomPrenom;
        } catch (Exception e) {
            return nomPrenom;

        }
    }

    /**
     * Method checkPays.
     * 
     * @param codePays
     * @param numAvs
     * @return boolean
     */
    public static boolean checkPays(String codePays, String numAvs) {
        // Extraction du chiffre du pays dans le num avs
        if (numAvs.length() == 11) {
            int paysAvs = Integer.parseInt(numAvs.substring(9, 10));
            if ("315100".equals(codePays)) {
                if (paysAvs > 0 && paysAvs < 5) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (paysAvs > 4 && paysAvs < 9) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return true;
        }

    }

    /**
     * Method checkPremierGroupe.
     * 
     * @param nom
     * @param numAvs
     * @return boolean
     */
    public static boolean checkPremierGroupe(String nom, String numAvs) {
        String key = "";
        initMap();
        initExceptionsEVoyelleMap();
        numAvs = numAvs.substring(0, 3);
        String initial = "";
        String compare = "";
        String comparePlusGrand = "";
        if (nom.indexOf(",") > 0) {
            nom = nom.substring(0, nom.indexOf(","));
        }
        nom = JadeStringUtil.change(nom, "'", "");
        nom = traiteEspace(nom);
        if (nom.indexOf("-") > 0) {
            nom = nom.substring(0, nom.indexOf("-"));
        }

        /*
         * char actuel; char precedent='B'; for(int i=0; i<nom.length();i++){ actuel=nom.charAt(i); if(actuel=='E'){
         * if(precedent=='A' || precedent=='O' || precedent=='U'){ nom = nom.substring(0,i)+nom.substring(i+1); } }
         * precedent = actuel;
         * 
         * }
         */
        nom = nom.toLowerCase();
        nom = JadeStringUtil.change(nom, "'", "");
        // nom = JadeStringUtil.change(nom, "-", "");
        nom = JadeStringUtil.change(nom, "ä", "ae");
        nom = JadeStringUtil.change(nom, "ö", "oe");
        nom = JadeStringUtil.change(nom, "ü", "ue");
        Object it[] = exceptionsEVoyelleMap.keySet().toArray();
        for (int i = 0; i < it.length; i++) {
            key = (String) it[i];
            // pour traiter bae et bauer
            if (numAvs.startsWith("143")) {
                if (nom.toUpperCase().trim().startsWith("BAUE") || nom.toUpperCase().trim().startsWith("BAEUE")) {
                    return true;
                }
            }
            // setExceptionNum((String)exceptionsEVoyelleMap.get(it[i]));
            if (numAvs.startsWith(key)) {
                if (nom.toUpperCase().trim().startsWith((String) exceptionsEVoyelleMap.get(it[i]))) {
                    return true;
                }
            }
        }
        nom = "   " + nom;
        for (int i = 0; i < nom.length(); i++) {
            if (nom.charAt(i) == 'e') {
                if (isSpecialVoyelle(nom.charAt(i - 1))) {
                    // le 'e' est précédé par a o ou u

                    // le a o u est en première position
                    nom = globaz.hermes.utils.StringUtils.removeCharAt(nom, i);
                    i--;

                }
            }
        }
        nom = nom.toUpperCase().trim();
        int numeroCompare = 0;
        initial = (String) alphabetMap.get(numAvs);
        numeroCompare = Integer.parseInt(numAvs);
        numeroCompare = numeroCompare + 1;
        compare = Integer.toString(numeroCompare);
        comparePlusGrand = (String) alphabetMap.get(compare);
        // Gérer spécifiquement le 799 pour les sch
        if (numAvs.startsWith("799")) {
            if (nom.compareToIgnoreCase("SV") >= 0 && nom.compareToIgnoreCase("TAN") < 0) {
                return true;
            }

        }
        if (numAvs.startsWith("999")) {
            if (nom.compareToIgnoreCase("SY") >= 0) {
                return true;
            }

        }

        if (nom.compareToIgnoreCase(initial) >= 0 && nom.compareToIgnoreCase(comparePlusGrand) < 0) {

            return true;
        } else {
            return false;
        }
    }

    /**
     * Method checkSexe.1 pour un homme et 2 pour une femme
     * 
     * @param numeroAvs
     * @param sexe
     */
    public static boolean checkSexe(String numeroAvs, int sexe) {

        try {
            int sexCompare = Integer.parseInt(numeroAvs.substring(5, 6));
            // Pour gérer les xxx.xx.900, on retourne true si sexe = 9
            if (9 == sexCompare) {
                return true;
            }
            if (1 == sexe) {
                if (sexCompare > 0 && sexCompare < 5) {
                    return true;
                } else {
                    return false;
                }
            }
            if (2 == sexe) {
                if (sexCompare > 4 && sexCompare < 9) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            return false;

        }

    }

    /**
     * Method checkTroisiemeGroupe.
     * 
     * @param dateNaissance
     * @param numeroAvs
     * @param sexe
     *            , 1 pour un hommme et 2 pour une femme
     * @return boolean
     */
    public static boolean checkTroisiemeGroupe(String dateNaissance, String numeroAvs, int sexe) {

        int mois = 0;
        try {
            mois = JACalendar.getMonth(dateNaissance);
        } catch (JAException e) {
            return false;
        }
        int dateEtSex = 0;
        /*
         * if(mois>0 && mois<4){ dateEtSex=100; } if(mois>3 && mois<7){ dateEtSex=200; } if(mois>6 && mois<10){
         * dateEtSex=300; } if(mois>9 && mois<3){ dateEtSex=400; }
         */
        switch (mois) {
            case 1:
                dateEtSex = 100;
                break;
            case 2:
                dateEtSex = 131;

                break;
            case 3:
                dateEtSex = 162;
                break;
            case 4:
                dateEtSex = 200;
                break;
            case 5:
                dateEtSex = 231;

                break;
            case 6:
                dateEtSex = 262;

                break;
            case 7:
                dateEtSex = 300;

                break;
            case 8:
                dateEtSex = 331;

                break;
            case 9:
                dateEtSex = 362;

                break;
            case 10:
                dateEtSex = 400;

                break;

            case 11:
                dateEtSex = 431;

                break;
            case 12:
                dateEtSex = 462;
                break;
        }
        if (2 == sexe) {
            dateEtSex = dateEtSex + 400;
        }
        try {
            dateEtSex = dateEtSex + JACalendar.getDay(dateNaissance);
        } catch (JAException e) {
            e.printStackTrace();
        }

        if (dateEtSex == 0 || dateEtSex == 400 || dateEtSex == 101 || dateEtSex == 501) {

            return true;
        }
        if ("900".equals(numeroAvs.substring(5, 8))) {
            return true;
        }
        if (dateEtSex == Integer.parseInt(numeroAvs.substring(5, 8))) {
            return true;
        } else {
            return false;
        }

    }

    private static void initExceptionsEVoyelleMap() {
        exceptionsEVoyelleMap.put("143", "BAUE");
        exceptionsEVoyelleMap.put("596", "LEUE");
        exceptionsEVoyelleMap.put("583", "LAUE");
        exceptionsEVoyelleMap.put("798", "SUEU");
        exceptionsEVoyelleMap.put("445", "HAUET");
    }

    /**
     * Method initMap.
     */
    private static void initMap() {
        alphabetMap = new TreeMap();
        alphabetMap.put("100", "A");
        alphabetMap.put("101", "ABI");
        alphabetMap.put("102", "ABL");
        alphabetMap.put("103", "AC");
        alphabetMap.put("104", "AF");
        alphabetMap.put("105", "AG");
        alphabetMap.put("106", "AL");
        alphabetMap.put("107", "ALD");
        alphabetMap.put("108", "ALL");
        alphabetMap.put("109", "ALM");
        alphabetMap.put("110", "AM");
        alphabetMap.put("111", "AMD");
        alphabetMap.put("112", "AMM");
        alphabetMap.put("113", "AMO");
        alphabetMap.put("114", "AMS");
        alphabetMap.put("115", "AN");
        alphabetMap.put("116", "ANDO");
        alphabetMap.put("117", "ANE");
        alphabetMap.put("118", "ANN");
        alphabetMap.put("119", "AO");
        alphabetMap.put("120", "AR");
        alphabetMap.put("121", "ARNO");
        alphabetMap.put("122", "ARO");
        alphabetMap.put("123", "AS");
        alphabetMap.put("124", "AU");
        alphabetMap.put("125", "B");
        alphabetMap.put("126", "BACH");
        alphabetMap.put("127", "BACHM");
        alphabetMap.put("128", "BAD");
        alphabetMap.put("129", "BAG");
        alphabetMap.put("130", "BAL");
        alphabetMap.put("131", "BALL");
        alphabetMap.put("132", "BALM");
        alphabetMap.put("133", "BALS");
        alphabetMap.put("134", "BAM");
        alphabetMap.put("135", "BAN");
        alphabetMap.put("136", "BAR");
        alphabetMap.put("137", "BARC");
        alphabetMap.put("138", "BARM");
        alphabetMap.put("139", "BART");
        alphabetMap.put("140", "BAS");
        alphabetMap.put("141", "BAT");
        alphabetMap.put("142", "BAU");
        alphabetMap.put("143", "BAUE");
        alphabetMap.put("144", "BAUM");
        alphabetMap.put("145", "BAUMB");
        alphabetMap.put("146", "BAUMG");
        alphabetMap.put("147", "BAUN");
        alphabetMap.put("148", "BAV");
        alphabetMap.put("149", "BAY");
        alphabetMap.put("150", "BE");
        alphabetMap.put("151", "BED");
        alphabetMap.put("152", "BEF");
        alphabetMap.put("153", "BEL");
        alphabetMap.put("154", "BEN");
        alphabetMap.put("155", "BER");
        alphabetMap.put("156", "BERG");
        alphabetMap.put("157", "BERH");
        alphabetMap.put("158", "BERN");
        alphabetMap.put("159", "BERNE");
        alphabetMap.put("160", "BERO");
        alphabetMap.put("161", "BERT");
        alphabetMap.put("162", "BES");
        alphabetMap.put("163", "BEU");
        alphabetMap.put("164", "BF");
        alphabetMap.put("165", "BI");
        alphabetMap.put("166", "BIE");
        alphabetMap.put("167", "BIER");
        alphabetMap.put("168", "BIF");
        alphabetMap.put("169", "BIL");
        alphabetMap.put("170", "BIN");
        alphabetMap.put("171", "BIO");
        alphabetMap.put("172", "BIS");
        alphabetMap.put("173", "BISE");
        alphabetMap.put("174", "BIT");
        alphabetMap.put("175", "BL");
        alphabetMap.put("176", "BLAS");
        alphabetMap.put("177", "BLAT");
        alphabetMap.put("178", "BLE");
        alphabetMap.put("179", "BLU");
        alphabetMap.put("180", "BO");
        alphabetMap.put("181", "BOD");
        alphabetMap.put("182", "BOF");
        alphabetMap.put("183", "BOH");
        alphabetMap.put("184", "BOI");
        alphabetMap.put("185", "BOL");
        alphabetMap.put("186", "BOLL");
        alphabetMap.put("187", "BOLLI");
        alphabetMap.put("188", "BOLM");
        alphabetMap.put("189", "BOM");
        alphabetMap.put("190", "BON");
        alphabetMap.put("191", "BOO");
        alphabetMap.put("192", "BOR");
        alphabetMap.put("193", "BORG");
        alphabetMap.put("194", "BORN");
        alphabetMap.put("195", "BOS");
        alphabetMap.put("196", "BOSS");
        alphabetMap.put("197", "BOSSH");
        alphabetMap.put("198", "BOT");
        alphabetMap.put("199", "BOV");
        alphabetMap.put("200", "BR");
        alphabetMap.put("201", "BRAG");
        alphabetMap.put("202", "BRAN");
        alphabetMap.put("203", "BRAP");
        alphabetMap.put("204", "BRAU");
        alphabetMap.put("205", "BRE");
        alphabetMap.put("206", "BREG");
        alphabetMap.put("207", "BREL");
        alphabetMap.put("208", "BRES");
        alphabetMap.put("209", "BRI");
        alphabetMap.put("210", "BRO");
        alphabetMap.put("211", "BROF");
        alphabetMap.put("212", "BRON");
        alphabetMap.put("213", "BRONN");
        alphabetMap.put("214", "BROO");
        alphabetMap.put("215", "BRU");
        alphabetMap.put("216", "BRUD");
        alphabetMap.put("217", "BRUG");
        alphabetMap.put("218", "BRUH");
        alphabetMap.put("219", "BRUL");
        alphabetMap.put("220", "BRUN");
        alphabetMap.put("221", "BRUND");
        alphabetMap.put("222", "BRUNN");
        alphabetMap.put("223", "BRUNO");
        alphabetMap.put("224", "BRUO");
        alphabetMap.put("225", "BU");
        alphabetMap.put("226", "BUCH");
        alphabetMap.put("227", "BUCHI");
        alphabetMap.put("228", "BUCHO");
        alphabetMap.put("229", "BUCI");
        alphabetMap.put("230", "BUD");
        alphabetMap.put("231", "BUH");
        alphabetMap.put("232", "BUHLM");
        alphabetMap.put("233", "BUHM");
        alphabetMap.put("234", "BUI");
        alphabetMap.put("235", "BUR");
        alphabetMap.put("236", "BURG");
        alphabetMap.put("237", "BURGI");
        alphabetMap.put("238", "BURH");
        alphabetMap.put("239", "BURK");
        alphabetMap.put("240", "BURKH");
        alphabetMap.put("241", "BURKI");
        alphabetMap.put("242", "BURL");
        alphabetMap.put("243", "BURR");
        alphabetMap.put("244", "BURS");
        alphabetMap.put("245", "BUS");
        alphabetMap.put("246", "BUSS");
        alphabetMap.put("247", "BUT");
        alphabetMap.put("248", "BUTT");
        alphabetMap.put("249", "BY");
        alphabetMap.put("250", "C");
        alphabetMap.put("251", "CAI");
        alphabetMap.put("252", "CAM");
        alphabetMap.put("253", "CAN");
        alphabetMap.put("254", "CAR");
        alphabetMap.put("255", "CAS");
        alphabetMap.put("256", "CAST");
        alphabetMap.put("257", "CAT");
        alphabetMap.put("258", "CAV");
        alphabetMap.put("259", "CE");
        alphabetMap.put("260", "CH");
        alphabetMap.put("261", "CHAR");
        alphabetMap.put("262", "CHE");
        alphabetMap.put("263", "CHI");
        alphabetMap.put("264", "CHR");
        alphabetMap.put("265", "CI");
        alphabetMap.put("266", "CL");
        alphabetMap.put("267", "CO");
        alphabetMap.put("268", "COM");
        alphabetMap.put("269", "COR");
        alphabetMap.put("270", "CORN");
        alphabetMap.put("271", "COS");
        alphabetMap.put("272", "CR");
        alphabetMap.put("273", "CRI");
        alphabetMap.put("274", "CU");
        alphabetMap.put("275", "D");
        alphabetMap.put("276", "DAM");
        alphabetMap.put("277", "DAS");
        alphabetMap.put("278", "DE");
        alphabetMap.put("279", "DEG");
        alphabetMap.put("280", "DEL");
        alphabetMap.put("281", "DEM");
        alphabetMap.put("282", "DEP");
        alphabetMap.put("283", "DES");
        alphabetMap.put("284", "DET");
        alphabetMap.put("285", "DI");
        alphabetMap.put("286", "DIE");
        alphabetMap.put("287", "DIG");
        alphabetMap.put("288", "DO");
        alphabetMap.put("289", "DOR");
        alphabetMap.put("290", "DR");
        alphabetMap.put("291", "DU");
        alphabetMap.put("292", "DUB");
        alphabetMap.put("293", "DUC");
        alphabetMap.put("294", "DUD");
        alphabetMap.put("295", "DUM");
        alphabetMap.put("296", "DUP");
        alphabetMap.put("297", "DUR");
        alphabetMap.put("298", "DUS");
        alphabetMap.put("299", "DUV");
        alphabetMap.put("300", "E");
        alphabetMap.put("301", "EBI");
        alphabetMap.put("302", "EC");
        alphabetMap.put("303", "EG");
        alphabetMap.put("304", "EGGEN");
        alphabetMap.put("305", "EGGER");
        alphabetMap.put("306", "EGGF");
        alphabetMap.put("307", "EGH");
        alphabetMap.put("308", "EH");
        alphabetMap.put("309", "EHRE");
        alphabetMap.put("310", "EI");
        alphabetMap.put("311", "EICHF");
        alphabetMap.put("312", "EID");
        alphabetMap.put("313", "EIN");
        alphabetMap.put("314", "EK");
        alphabetMap.put("315", "EM");
        alphabetMap.put("316", "EN");
        alphabetMap.put("317", "ENG");
        alphabetMap.put("318", "ENI");
        alphabetMap.put("319", "EO");
        alphabetMap.put("320", "ER");
        alphabetMap.put("321", "ERN");
        alphabetMap.put("322", "ES");
        alphabetMap.put("323", "ET");
        alphabetMap.put("324", "EU");
        alphabetMap.put("325", "F");
        alphabetMap.put("326", "FAH");
        alphabetMap.put("327", "FAI");
        alphabetMap.put("328", "FAN");
        alphabetMap.put("329", "FAR");
        alphabetMap.put("330", "FAS");
        alphabetMap.put("331", "FASS");
        alphabetMap.put("332", "FAT");
        alphabetMap.put("333", "FAV");
        alphabetMap.put("334", "FAW");
        alphabetMap.put("335", "FE");
        alphabetMap.put("336", "FEL");
        alphabetMap.put("337", "FELI");
        alphabetMap.put("338", "FEM");
        alphabetMap.put("339", "FET");
        alphabetMap.put("340", "FI");
        alphabetMap.put("341", "FIN");
        alphabetMap.put("342", "FISCH");
        alphabetMap.put("343", "FISD");
        alphabetMap.put("344", "FIT");
        alphabetMap.put("345", "FL");
        alphabetMap.put("346", "FLE");
        alphabetMap.put("347", "FLI");
        alphabetMap.put("348", "FLU");
        alphabetMap.put("349", "FLUD");
        alphabetMap.put("350", "FO");
        alphabetMap.put("351", "FON");
        alphabetMap.put("352", "FOR");
        alphabetMap.put("353", "FORR");
        alphabetMap.put("354", "FOS");
        alphabetMap.put("355", "FR");
        alphabetMap.put("356", "FRAN");
        alphabetMap.put("357", "FRAP");
        alphabetMap.put("358", "FRE");
        alphabetMap.put("359", "FREI");
        alphabetMap.put("360", "FREK");
        alphabetMap.put("361", "FREY");
        alphabetMap.put("362", "FRI");
        alphabetMap.put("363", "FRIE");
        alphabetMap.put("364", "FRIF");
        alphabetMap.put("365", "FRIS");
        alphabetMap.put("366", "FRIT");
        alphabetMap.put("367", "FRO");
        alphabetMap.put("368", "FROI");
        alphabetMap.put("369", "FRU");
        alphabetMap.put("370", "FU");
        alphabetMap.put("371", "FUD");
        alphabetMap.put("372", "FUI");
        alphabetMap.put("373", "FUR");
        alphabetMap.put("374", "FUS");
        alphabetMap.put("375", "G");
        alphabetMap.put("376", "GAF");
        alphabetMap.put("377", "GAL");
        alphabetMap.put("378", "GAM");
        alphabetMap.put("379", "GAN");
        alphabetMap.put("380", "GAP");
        alphabetMap.put("381", "GAS");
        alphabetMap.put("382", "GASS");
        alphabetMap.put("383", "GAT");
        alphabetMap.put("384", "GAV");
        alphabetMap.put("385", "GE");
        alphabetMap.put("386", "GEH");
        alphabetMap.put("387", "GEI");
        alphabetMap.put("388", "GEL");
        alphabetMap.put("389", "GEO");
        alphabetMap.put("390", "GER");
        alphabetMap.put("391", "GERD");
        alphabetMap.put("392", "GES");
        alphabetMap.put("393", "GF");
        alphabetMap.put("394", "GI");
        alphabetMap.put("395", "GIG");
        alphabetMap.put("396", "GIL");
        alphabetMap.put("397", "GIM");
        alphabetMap.put("398", "GIR");
        alphabetMap.put("399", "GIS");
        alphabetMap.put("400", "GL");
        alphabetMap.put("401", "GLE");
        alphabetMap.put("402", "GM");
        alphabetMap.put("403", "GO");
        alphabetMap.put("404", "GOM");
        alphabetMap.put("405", "GR");
        alphabetMap.put("406", "GRAF");
        alphabetMap.put("407", "GRAG");
        alphabetMap.put("408", "GRE");
        alphabetMap.put("409", "GRES");
        alphabetMap.put("410", "GRI");
        alphabetMap.put("411", "GRIM");
        alphabetMap.put("412", "GRO");
        alphabetMap.put("413", "GROS");
        alphabetMap.put("414", "GROT");
        alphabetMap.put("415", "GRU");
        alphabetMap.put("416", "GRUP");
        alphabetMap.put("417", "GS");
        alphabetMap.put("418", "GU");
        alphabetMap.put("419", "GUD");
        alphabetMap.put("420", "GUG");
        alphabetMap.put("421", "GUI");
        alphabetMap.put("422", "GUN");
        alphabetMap.put("423", "GUT");
        alphabetMap.put("424", "GW");
        alphabetMap.put("425", "H");
        alphabetMap.put("426", "HAB");
        alphabetMap.put("427", "HABI");
        alphabetMap.put("428", "HAC");
        alphabetMap.put("429", "HAD");
        alphabetMap.put("430", "HAF");
        alphabetMap.put("431", "HAFL");
        alphabetMap.put("432", "HAFN");
        alphabetMap.put("433", "HAG");
        alphabetMap.put("434", "HAH");
        alphabetMap.put("435", "HAL");
        alphabetMap.put("436", "HALL");
        alphabetMap.put("437", "HAM");
        alphabetMap.put("438", "HAN");
        alphabetMap.put("439", "HANN");
        alphabetMap.put("440", "HAO");
        alphabetMap.put("441", "HAR");
        alphabetMap.put("442", "HART");
        alphabetMap.put("443", "HAS");
        alphabetMap.put("444", "HAT");
        alphabetMap.put("445", "HAU");
        alphabetMap.put("446", "HAUS");
        alphabetMap.put("447", "HAUSF");
        alphabetMap.put("448", "HAUT");
        alphabetMap.put("449", "HAV");
        alphabetMap.put("450", "HE");
        alphabetMap.put("451", "HEE");
        alphabetMap.put("452", "HEG");
        alphabetMap.put("453", "HEI");
        alphabetMap.put("454", "HEIN");
        alphabetMap.put("455", "HEL");
        alphabetMap.put("456", "HELF");
        alphabetMap.put("457", "HELL");
        alphabetMap.put("458", "HEM");
        alphabetMap.put("459", "HEN");
        alphabetMap.put("460", "HENG");
        alphabetMap.put("461", "HENO");
        alphabetMap.put("462", "HEO");
        alphabetMap.put("463", "HER");
        alphabetMap.put("464", "HERM");
        alphabetMap.put("465", "HERR");
        alphabetMap.put("466", "HERS");
        alphabetMap.put("467", "HERZ");
        alphabetMap.put("468", "HES");
        alphabetMap.put("469", "HET");
        alphabetMap.put("470", "HI");
        alphabetMap.put("471", "HIM");
        alphabetMap.put("472", "HIR");
        alphabetMap.put("473", "HIRT");
        alphabetMap.put("474", "HIS");
        alphabetMap.put("475", "HO");
        alphabetMap.put("476", "HOF");
        alphabetMap.put("477", "HOFF");
        alphabetMap.put("478", "HOFM");
        alphabetMap.put("479", "HOFN");
        alphabetMap.put("480", "HOG");
        alphabetMap.put("481", "HOL");
        alphabetMap.put("482", "HOM");
        alphabetMap.put("483", "HOR");
        alphabetMap.put("484", "HOT");
        alphabetMap.put("485", "HU");
        alphabetMap.put("486", "HUBF");
        alphabetMap.put("487", "HUC");
        alphabetMap.put("488", "HUG");
        alphabetMap.put("489", "HUGI");
        alphabetMap.put("490", "HUH");
        alphabetMap.put("491", "HUN");
        alphabetMap.put("492", "HUNI");
        alphabetMap.put("493", "HUNZ");
        alphabetMap.put("494", "HUO");
        alphabetMap.put("495", "HUR");
        alphabetMap.put("496", "HURN");
        alphabetMap.put("497", "HUS");
        alphabetMap.put("498", "HUT");
        alphabetMap.put("499", "HUW");
        alphabetMap.put("500", "I");
        alphabetMap.put("501", "IM");
        alphabetMap.put("502", "IN");
        alphabetMap.put("503", "IS");
        alphabetMap.put("504", "IT");
        alphabetMap.put("505", "J");
        alphabetMap.put("506", "JAD");
        alphabetMap.put("507", "JAH");
        alphabetMap.put("508", "JAN");
        alphabetMap.put("509", "JAR");
        alphabetMap.put("510", "JE");
        alphabetMap.put("511", "JEC");
        alphabetMap.put("512", "JEH");
        alphabetMap.put("513", "JEN");
        alphabetMap.put("514", "JEP");
        alphabetMap.put("515", "JO");
        alphabetMap.put("516", "JOL");
        alphabetMap.put("517", "JOR");
        alphabetMap.put("518", "JOS");
        alphabetMap.put("519", "JOT");
        alphabetMap.put("520", "JU");
        alphabetMap.put("521", "JUF");
        alphabetMap.put("522", "JUN");
        alphabetMap.put("523", "JUNK");
        alphabetMap.put("524", "JUO");
        alphabetMap.put("525", "K");
        alphabetMap.put("526", "KAH");
        alphabetMap.put("527", "KAL");
        alphabetMap.put("528", "KAM");
        alphabetMap.put("529", "KAN");
        alphabetMap.put("530", "KAR");
        alphabetMap.put("531", "KAS");
        alphabetMap.put("532", "KASP");
        alphabetMap.put("533", "KAT");
        alphabetMap.put("534", "KAU");
        alphabetMap.put("535", "KE");
        alphabetMap.put("536", "KEL");
        alphabetMap.put("537", "KEM");
        alphabetMap.put("538", "KES");
        alphabetMap.put("539", "KET");
        alphabetMap.put("540", "KI");
        alphabetMap.put("541", "KIF");
        alphabetMap.put("542", "KIM");
        alphabetMap.put("543", "KIR");
        alphabetMap.put("544", "KIS");
        alphabetMap.put("545", "KL");
        alphabetMap.put("546", "KLE");
        alphabetMap.put("547", "KLI");
        alphabetMap.put("548", "KN");
        alphabetMap.put("549", "KNO");
        alphabetMap.put("550", "KO");
        alphabetMap.put("551", "KOC");
        alphabetMap.put("552", "KOD");
        alphabetMap.put("553", "KOH");
        alphabetMap.put("554", "KOL");
        alphabetMap.put("555", "KOLL");
        alphabetMap.put("556", "KOM");
        alphabetMap.put("557", "KOP");
        alphabetMap.put("558", "KOR");
        alphabetMap.put("559", "KOT");
        alphabetMap.put("560", "KR");
        alphabetMap.put("561", "KRAP");
        alphabetMap.put("562", "KRE");
        alphabetMap.put("563", "KRI");
        alphabetMap.put("564", "KRU");
        alphabetMap.put("565", "KU");
        alphabetMap.put("566", "KUC");
        alphabetMap.put("567", "KUH");
        alphabetMap.put("568", "KUI");
        alphabetMap.put("569", "KUN");
        alphabetMap.put("570", "KUNZ");
        alphabetMap.put("571", "KUNZI");
        alphabetMap.put("572", "KUO");
        alphabetMap.put("573", "KUR");
        alphabetMap.put("574", "KUS");
        alphabetMap.put("575", "L");
        alphabetMap.put("576", "LAF");
        alphabetMap.put("577", "LAN");
        alphabetMap.put("578", "LANG");
        alphabetMap.put("579", "LANH");
        alphabetMap.put("580", "LAO");
        alphabetMap.put("581", "LAT");
        alphabetMap.put("582", "LAU");
        alphabetMap.put("583", "LAUD");
        alphabetMap.put("584", "LAV");
        alphabetMap.put("585", "LE");
        alphabetMap.put("586", "LED");
        alphabetMap.put("587", "LEE");
        alphabetMap.put("588", "LEH");
        alphabetMap.put("589", "LEHN");
        alphabetMap.put("590", "LEI");
        alphabetMap.put("591", "LEIP");
        alphabetMap.put("592", "LEK");
        alphabetMap.put("593", "LEO");
        alphabetMap.put("594", "LER");
        alphabetMap.put("595", "LEU");
        alphabetMap.put("596", "LEUE");
        alphabetMap.put("597", "LEUF");
        alphabetMap.put("598", "LEUT");
        alphabetMap.put("599", "LEV");
        alphabetMap.put("600", "LI");
        alphabetMap.put("601", "LIE");
        alphabetMap.put("602", "LIEC");
        alphabetMap.put("603", "LIED");
        alphabetMap.put("604", "LIF");
        alphabetMap.put("605", "LIN");
        alphabetMap.put("606", "LIND");
        alphabetMap.put("607", "LINE");
        alphabetMap.put("608", "LIO");
        alphabetMap.put("609", "LIS");
        alphabetMap.put("610", "LO");
        alphabetMap.put("611", "LOF");
        alphabetMap.put("612", "LOO");
        alphabetMap.put("613", "LOR");
        alphabetMap.put("614", "LOT");
        alphabetMap.put("615", "LU");
        alphabetMap.put("616", "LUD");
        alphabetMap.put("617", "LUG");
        alphabetMap.put("618", "LUI");
        alphabetMap.put("619", "LUS");
        alphabetMap.put("620", "LUT");
        alphabetMap.put("621", "LUTH");
        alphabetMap.put("622", "LUTI");
        alphabetMap.put("623", "LUU");
        alphabetMap.put("624", "LY");
        alphabetMap.put("625", "M");
        alphabetMap.put("626", "MAD");
        alphabetMap.put("627", "MAG");
        alphabetMap.put("628", "MAI");
        alphabetMap.put("629", "MAK");
        alphabetMap.put("630", "MAR");
        alphabetMap.put("631", "MARG");
        alphabetMap.put("632", "MART");
        alphabetMap.put("633", "MARTIN");
        alphabetMap.put("634", "MARU");
        alphabetMap.put("635", "MAS");
        alphabetMap.put("636", "MAT");
        alphabetMap.put("637", "MATT");
        alphabetMap.put("638", "MAU");
        alphabetMap.put("639", "MAY");
        alphabetMap.put("640", "ME");
        alphabetMap.put("641", "MEIER");
        alphabetMap.put("642", "MEIF");
        alphabetMap.put("643", "MEK");
        alphabetMap.put("644", "MER");
        alphabetMap.put("645", "MES");
        alphabetMap.put("646", "MET");
        alphabetMap.put("647", "MEY");
        alphabetMap.put("648", "MEYER");
        alphabetMap.put("649", "MEYF");
        alphabetMap.put("650", "MI");
        alphabetMap.put("651", "MIC");
        alphabetMap.put("652", "MID");
        alphabetMap.put("653", "MIN");
        alphabetMap.put("654", "MIR");
        alphabetMap.put("655", "MO");
        alphabetMap.put("656", "MOI");
        alphabetMap.put("657", "MON");
        alphabetMap.put("658", "MONN");
        alphabetMap.put("659", "MONO");
        alphabetMap.put("660", "MOO");
        alphabetMap.put("661", "MOR");
        alphabetMap.put("662", "MORE");
        alphabetMap.put("663", "MORG");
        alphabetMap.put("664", "MOS");
        alphabetMap.put("665", "MOSE");
        alphabetMap.put("666", "MOSI");
        alphabetMap.put("667", "MOT");
        alphabetMap.put("668", "MU");
        alphabetMap.put("669", "MUH");
        alphabetMap.put("670", "MUL");
        alphabetMap.put("671", "MULL");
        alphabetMap.put("672", "MUM");
        alphabetMap.put("673", "MUR");
        alphabetMap.put("674", "MUS");
        alphabetMap.put("675", "N");
        alphabetMap.put("676", "NAG");
        alphabetMap.put("677", "NE");
        alphabetMap.put("678", "NEG");
        alphabetMap.put("679", "NEU");
        alphabetMap.put("680", "NI");
        alphabetMap.put("681", "NIE");
        alphabetMap.put("682", "NIEDERG");
        alphabetMap.put("683", "NIEF");
        alphabetMap.put("684", "NIF");
        alphabetMap.put("685", "NO");
        alphabetMap.put("686", "NOT");
        alphabetMap.put("687", "NU");
        alphabetMap.put("688", "NY");
        alphabetMap.put("689", "NYF");
        alphabetMap.put("690", "O");
        alphabetMap.put("691", "OBI");
        alphabetMap.put("692", "OC");
        alphabetMap.put("693", "OD");
        alphabetMap.put("694", "OF");
        alphabetMap.put("695", "OK");
        alphabetMap.put("696", "OP");
        alphabetMap.put("697", "OS");
        alphabetMap.put("698", "OT");
        alphabetMap.put("699", "OU");
        alphabetMap.put("700", "P");
        alphabetMap.put("701", "PAG");
        alphabetMap.put("702", "PAN");
        alphabetMap.put("703", "PAR");
        alphabetMap.put("704", "PAT");
        alphabetMap.put("705", "PE");
        alphabetMap.put("706", "PEL");
        alphabetMap.put("707", "PER");
        alphabetMap.put("708", "PERRI");
        alphabetMap.put("709", "PERS");
        alphabetMap.put("710", "PES");
        alphabetMap.put("711", "PET");
        alphabetMap.put("712", "PEU");
        alphabetMap.put("713", "PF");
        alphabetMap.put("714", "PFI");
        alphabetMap.put("715", "PH");
        alphabetMap.put("716", "PI");
        alphabetMap.put("717", "PIG");
        alphabetMap.put("718", "PIR");
        alphabetMap.put("719", "PL");
        alphabetMap.put("720", "PO");
        alphabetMap.put("721", "POR");
        alphabetMap.put("722", "PR");
        alphabetMap.put("723", "PU");
        alphabetMap.put("724", "Q");
        alphabetMap.put("725", "R");
        alphabetMap.put("726", "RAM");
        alphabetMap.put("727", "RAN");
        alphabetMap.put("728", "RAS");
        alphabetMap.put("729", "RAU");
        alphabetMap.put("730", "RE");
        alphabetMap.put("731", "REC");
        alphabetMap.put("732", "REI");
        alphabetMap.put("733", "REIN");
        alphabetMap.put("734", "REK");
        alphabetMap.put("735", "REN");
        alphabetMap.put("736", "RENI");
        alphabetMap.put("737", "REO");
        alphabetMap.put("738", "REY");
        alphabetMap.put("739", "RH");
        alphabetMap.put("740", "RI");
        alphabetMap.put("741", "RIC");
        alphabetMap.put("742", "RID");
        alphabetMap.put("743", "RIE");
        alphabetMap.put("744", "RIEG");
        alphabetMap.put("745", "RIF");
        alphabetMap.put("746", "RIN");
        alphabetMap.put("747", "RIO");
        alphabetMap.put("748", "RIT");
        alphabetMap.put("749", "RIV");
        alphabetMap.put("750", "RO");
        alphabetMap.put("751", "ROC");
        alphabetMap.put("752", "ROD");
        alphabetMap.put("753", "ROH");
        alphabetMap.put("754", "ROHR");
        alphabetMap.put("755", "ROI");
        alphabetMap.put("756", "ROM");
        alphabetMap.put("757", "ROO");
        alphabetMap.put("758", "ROS");
        alphabetMap.put("759", "ROSS");
        alphabetMap.put("760", "ROT");
        alphabetMap.put("761", "ROTH");
        alphabetMap.put("762", "ROTHA");
        alphabetMap.put("763", "ROTI");
        alphabetMap.put("764", "ROU");
        alphabetMap.put("765", "RU");
        alphabetMap.put("766", "RUC");
        alphabetMap.put("767", "RUD");
        alphabetMap.put("768", "RUF");
        alphabetMap.put("769", "RUG");
        alphabetMap.put("770", "RUH");
        alphabetMap.put("771", "RUP");
        alphabetMap.put("772", "RUS");
        alphabetMap.put("773", "RUT");
        alphabetMap.put("774", "RY");
        alphabetMap.put("775", "S");
        alphabetMap.put("776", "SAL");
        alphabetMap.put("777", "SAM");
        alphabetMap.put("778", "SAR");
        alphabetMap.put("779", "SAV");
        alphabetMap.put("780", "SB");
        alphabetMap.put("781", "SE");
        alphabetMap.put("782", "SEI");
        alphabetMap.put("783", "SEN");
        alphabetMap.put("784", "SEO");
        alphabetMap.put("785", "SF");
        alphabetMap.put("786", "SI");
        alphabetMap.put("787", "SIEG");
        alphabetMap.put("788", "SIF");
        alphabetMap.put("789", "SIM");
        alphabetMap.put("790", "SK");
        alphabetMap.put("791", "SO");
        alphabetMap.put("792", "SOM");
        alphabetMap.put("793", "SP");
        alphabetMap.put("794", "SPI");
        alphabetMap.put("795", "SPO");
        alphabetMap.put("796", "SQ");
        alphabetMap.put("797", "SU");
        alphabetMap.put("798", "SUT");
        alphabetMap.put("799", "SV");
        alphabetMap.put("800", "SCH");
        alphabetMap.put("801", "SCHAD");
        alphabetMap.put("802", "SCHAF");
        alphabetMap.put("803", "SCHAL");
        alphabetMap.put("804", "SCHAR");
        alphabetMap.put("805", "SCHARB");
        alphabetMap.put("806", "SCHARL");
        alphabetMap.put("807", "SCHAS");
        alphabetMap.put("808", "SCHAU");
        alphabetMap.put("809", "SCHAV");
        alphabetMap.put("810", "SCHE");
        alphabetMap.put("811", "SCHEL");
        alphabetMap.put("812", "SCHEN");
        alphabetMap.put("813", "SCHER");
        alphabetMap.put("814", "SCHEU");
        alphabetMap.put("815", "SCHI");
        alphabetMap.put("816", "SCHIL");
        alphabetMap.put("817", "SCHL");
        alphabetMap.put("818", "SCHLE");
        alphabetMap.put("819", "SCHLU");
        alphabetMap.put("820", "SCHM");
        alphabetMap.put("821", "SCHMID");
        alphabetMap.put("822", "SCHMIDA");
        alphabetMap.put("823", "SCHMIE");
        alphabetMap.put("824", "SCHMO");
        alphabetMap.put("825", "SCHN");
        alphabetMap.put("826", "SCHNEI");
        alphabetMap.put("827", "SCHNEK");
        alphabetMap.put("828", "SCHNI");
        alphabetMap.put("829", "SCHNY");
        alphabetMap.put("830", "SCHO");
        alphabetMap.put("831", "SCHON");
        alphabetMap.put("832", "SCHOO");
        alphabetMap.put("833", "SCHOR");
        alphabetMap.put("834", "SCHR");
        alphabetMap.put("835", "SCHU");
        alphabetMap.put("836", "SCHUL");
        alphabetMap.put("837", "SCHUM");
        alphabetMap.put("838", "SCHUR");
        alphabetMap.put("839", "SCHUS");
        alphabetMap.put("840", "SCHW");
        alphabetMap.put("841", "SCHWAB");
        alphabetMap.put("842", "SCHWAC");
        alphabetMap.put("843", "SCHWAR");
        alphabetMap.put("844", "SCHWARZ");
        alphabetMap.put("845", "SCHWE");
        alphabetMap.put("846", "SCHWEI");
        alphabetMap.put("847", "SCHWEIZ");
        alphabetMap.put("848", "SCHWEK");
        alphabetMap.put("849", "SCHWI");
        alphabetMap.put("850", "ST");
        alphabetMap.put("851", "STAH");
        alphabetMap.put("852", "STAL");
        alphabetMap.put("853", "STAM");
        alphabetMap.put("854", "STAU");
        alphabetMap.put("855", "STE");
        alphabetMap.put("856", "STEF");
        alphabetMap.put("857", "STEI");
        alphabetMap.put("858", "STEIN");
        alphabetMap.put("859", "STEK");
        alphabetMap.put("860", "STI");
        alphabetMap.put("861", "STO");
        alphabetMap.put("862", "STOD");
        alphabetMap.put("863", "STOL");
        alphabetMap.put("864", "STOO");
        alphabetMap.put("865", "STR");
        alphabetMap.put("866", "STRAU");
        alphabetMap.put("867", "STRE");
        alphabetMap.put("868", "STRI");
        alphabetMap.put("869", "STRU");
        alphabetMap.put("870", "STU");
        alphabetMap.put("871", "STUC");
        alphabetMap.put("872", "STUD");
        alphabetMap.put("873", "STUF");
        alphabetMap.put("874", "STUS");
        alphabetMap.put("875", "T");
        alphabetMap.put("876", "TAN");
        alphabetMap.put("877", "TAO");
        alphabetMap.put("878", "TE");
        alphabetMap.put("879", "TER");
        alphabetMap.put("880", "TH");
        alphabetMap.put("881", "THE");
        alphabetMap.put("882", "THO");
        alphabetMap.put("883", "THON");
        alphabetMap.put("884", "THR");
        alphabetMap.put("885", "TI");
        alphabetMap.put("886", "TIR");
        alphabetMap.put("887", "TO");
        alphabetMap.put("888", "TOG");
        alphabetMap.put("889", "TOR");
        alphabetMap.put("890", "TR");
        alphabetMap.put("891", "TRE");
        alphabetMap.put("892", "TRI");
        alphabetMap.put("893", "TRO");
        alphabetMap.put("894", "TRU");
        alphabetMap.put("895", "TS");
        alphabetMap.put("896", "TSCHA");
        alphabetMap.put("897", "TSCHE");
        alphabetMap.put("898", "TSCHU");
        alphabetMap.put("899", "TU");
        alphabetMap.put("900", "U");
        alphabetMap.put("901", "UF");
        alphabetMap.put("902", "UL");
        alphabetMap.put("903", "UM");
        alphabetMap.put("904", "UR");
        alphabetMap.put("905", "V");
        alphabetMap.put("906", "VAL");
        alphabetMap.put("907", "VAM");
        alphabetMap.put("908", "VAR");
        alphabetMap.put("909", "VAU");
        alphabetMap.put("910", "VE");
        alphabetMap.put("911", "VET");
        alphabetMap.put("912", "VI");
        alphabetMap.put("913", "VIL");
        alphabetMap.put("914", "VIO");
        alphabetMap.put("915", "VO");
        alphabetMap.put("916", "VOG");
        alphabetMap.put("917", "VOGT");
        alphabetMap.put("918", "VOH");
        alphabetMap.put("919", "VOL");
        alphabetMap.put("920", "VON");
        alphabetMap.put("921", "VONG");
        alphabetMap.put("922", "VONO");
        alphabetMap.put("923", "VOO");
        alphabetMap.put("924", "VU");
        alphabetMap.put("925", "W");
        alphabetMap.put("926", "WAG");
        alphabetMap.put("927", "WAH");
        alphabetMap.put("928", "WAL");
        alphabetMap.put("929", "WALD");
        alphabetMap.put("930", "WALE");
        alphabetMap.put("931", "WALL");
        alphabetMap.put("932", "WALT");
        alphabetMap.put("933", "WAM");
        alphabetMap.put("934", "WAP");
        alphabetMap.put("935", "WAS");
        alphabetMap.put("936", "WAT");
        alphabetMap.put("937", "WE");
        alphabetMap.put("938", "WEC");
        alphabetMap.put("939", "WEH");
        alphabetMap.put("940", "WEI");
        alphabetMap.put("941", "WEIF");
        alphabetMap.put("942", "WEIS");
        alphabetMap.put("943", "WEIT");
        alphabetMap.put("944", "WEK");
        alphabetMap.put("945", "WEN");
        alphabetMap.put("946", "WER");
        alphabetMap.put("947", "WERN");
        alphabetMap.put("948", "WES");
        alphabetMap.put("949", "WEU");
        alphabetMap.put("950", "WI");
        alphabetMap.put("951", "WID");
        alphabetMap.put("952", "WIE");
        alphabetMap.put("953", "WIEL");
        alphabetMap.put("954", "WIF");
        alphabetMap.put("955", "WIL");
        alphabetMap.put("956", "WILE");
        alphabetMap.put("957", "WIM");
        alphabetMap.put("958", "WIP");
        alphabetMap.put("959", "WIR");
        alphabetMap.put("960", "WIS");
        alphabetMap.put("961", "WIT");
        alphabetMap.put("962", "WITT");
        alphabetMap.put("963", "WO");
        alphabetMap.put("964", "WOL");
        alphabetMap.put("965", "WU");
        alphabetMap.put("966", "WUI");
        alphabetMap.put("967", "WUN");
        alphabetMap.put("968", "WUR");
        alphabetMap.put("969", "WUT");
        alphabetMap.put("970", "WY");
        alphabetMap.put("971", "WYM");
        alphabetMap.put("972", "WYS");
        alphabetMap.put("973", "WYSS");
        alphabetMap.put("974", "WYT");
        alphabetMap.put("975", "X");
        alphabetMap.put("976", "Z");
        alphabetMap.put("977", "ZAM");
        alphabetMap.put("978", "ZAU");
        alphabetMap.put("979", "ZB");
        alphabetMap.put("980", "ZE");
        alphabetMap.put("981", "ZEH");
        alphabetMap.put("982", "ZEI");
        alphabetMap.put("983", "ZEM");
        alphabetMap.put("984", "ZF");
        alphabetMap.put("985", "ZI");
        alphabetMap.put("986", "ZIM");
        alphabetMap.put("987", "ZIN");
        alphabetMap.put("988", "ZK");
        alphabetMap.put("989", "ZO");
        alphabetMap.put("990", "ZU");
        alphabetMap.put("991", "ZUC");
        alphabetMap.put("992", "ZUL");
        alphabetMap.put("993", "ZUM");
        alphabetMap.put("994", "ZUN");
        alphabetMap.put("995", "ZUR");
        alphabetMap.put("996", "ZUS");
        alphabetMap.put("997", "ZW");
        alphabetMap.put("998", "ZWE");
        alphabetMap.put("999", "ZZZ");
    }

    private static boolean isSpecialVoyelle(char c) {
        Character voyelles[] = { new Character('a'), new Character('o'), new Character('u') };
        return Arrays.asList(voyelles).contains(new Character(c));
    }

    private static boolean isVoyelle(char c) {
        Character voyelles[] = { new Character('a'), new Character('e'), new Character('i'), new Character('o'),
                new Character('u'), new Character('y') };
        return Arrays.asList(voyelles).contains(new Character(c));
    }

    /**
     * @param nom
     * @return
     */
    private static String traiteEspace(String nom) {
        String partieAvantEspace = "";
        // tester si le nom contient un espace en 3e position au moins
        if (nom.indexOf(" ") > 1) {
            // vérifier si le nom commence par une particule de la liste
            partieAvantEspace = nom.substring(0, nom.indexOf(" "));
            for (int i = 0; i < particules.length; i++) {
                if (partieAvantEspace.equals(particules[i])) {
                    return globaz.hermes.utils.StringUtils.removeChar(nom, ' ');
                }
            }

            // si le nom ne commence pas par une particule de la liste
            return nom.substring(0, nom.indexOf(" "));

        } else {
            return globaz.hermes.utils.StringUtils.removeChar(nom, ' ');
        }
    }

    public CIAvsUtil() {
        super();
    }

}
