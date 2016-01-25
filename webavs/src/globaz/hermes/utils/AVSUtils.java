package globaz.hermes.utils;

import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JAException;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.TreeMap;

/**
 * @author ado 22 oct. 03
 */
public class AVSUtils {
    public static int NBRE_MAX_LETTRES_NOM_PARTICULE = 3;
    public static String SEXE_AVS_FEMININ = "2";
    public static String SEXE_AVS_MASCULIN = "1";

    /**
     * Method formatAVS8Or9. Formatte un num�ro AVS Si � 11, renvoit avec des points Si � 9 ou 8, renvoit sans les z�ros
     * 
     * @param numAVS
     *            , num�ro AVS, avec ou sans points, avec ou sans z�ros
     * @return String, le num�ro AVS formatt�
     */
    public static String formatAVS8Or9(String numAVS) {
        return formatAVS8Or9(numAVS, false);
    }

    /**
     * Method formatAVS8Or9. Formatte un num�ro AVS Si � 11, renvoit avec des points Si � 9 ou 8, renvoit sans les z�ros
     * Si num�ro bizarre, renvoit tel que re�u
     * 
     * @param numAVS
     *            , num�ro AVS, avec ou sans points, avec ou sans z�ros
     * @param wantZeros
     *            si on veut les z�ros significatifs
     * @return String, le num�ro AVS formatt�
     */
    public static String formatAVS8Or9(String numAVS, boolean wantZeros) {
        return formatAVS8Or9(numAVS, wantZeros, true);
    }

    /**
     * Method formatAVS8Or9. Formatte un num�ro AVS
     * 
     * @param numAVS
     *            , num�ro AVS, avec ou sans points, avec ou sans z�ros
     * @param wantZeros
     *            si on veut les z�ros significatifs
     * @param wantZeros
     *            si on veut les points
     * @return String, le num�ro AVS formatt�
     */
    public static String formatAVS8Or9(String numAVS, boolean wantZeros, boolean wantDots) {
        // on enl�ve les points
        numAVS = StringUtils.removeDots(numAVS);
        // on compl�te � 11 si n�cessaire
        numAVS = StringUtils.padAfterString(numAVS, "0", 11);
        numAVS = JAUtil.formatAvs(numAVS);
        if (wantZeros) {
            return numAVS;
        } else {
            if (numAVS.endsWith(".000")) {
                return numAVS.substring(0, 10);
            } else if (numAVS.endsWith("00")) {
                return numAVS.substring(0, 12);
            }
        }
        if (wantZeros) {
            return JAUtil.formatAvs(numAVS);
        } else {
            return numAVS;
        }
    }

    /**
     * Method getBirthDateFromAVS. renvoit la date de naissance sous forme JJ.MM.AAAA
     * 
     * @param numAVS
     *            le num�ro d'assur�
     * @return String la date de naissance sous forme JJ.MM.AAAA
     * @throws JAException
     *             si le numero AVS est invalide
     */
    public static String getBirthDateFromAVS(String numAVS) throws JAException, ParseException {
        // String numAVS = "28977443018";
        // String numAVS = "12541721213";
        // numAVS = globaz.hermes.utils.StringUtils.removeDots(numAVS);
        // while (numAVS.endsWith("0") && numAVS.length() > 8) {
        // numAVS = numAVS.substring(0, numAVS.length() - 1);
        // }
        numAVS = StringUtils.removeDots(numAVS);
        globaz.globall.util.JAUtil.checkAvs(numAVS);
        int aa = Integer.parseInt(numAVS.substring(3, 5)); // 77
        int dayAndMonth = Integer.parseInt(numAVS.substring(5, 8)); // 443
        if (dayAndMonth > 500) {
            dayAndMonth -= 400;
        }
        int trimester = Integer.parseInt(("" + dayAndMonth).substring(0, 1));
        // 4
        int month = (trimester * 3) - 2; // 10
        int dayOfTrimester = dayAndMonth - (trimester * 100); // 43
        // le mois
        do {
            dayOfTrimester = dayOfTrimester - 31;
            month++;
        } while (dayOfTrimester > 0);
        month--;
        dayOfTrimester += 31;
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.MONTH, month - 1); // 10
        cal.set(java.util.Calendar.DAY_OF_MONTH, dayOfTrimester); // 1
        // TODO teste l'ann�e � deux chiffres avec l'ann�e courante
        // et d�termine l'ann�e sur 4 digits
        cal.set(java.util.Calendar.YEAR, DateUtils.enlargeYear(aa));
        // cal.set(java.util.Calendar.YEAR, Integer.parseInt("19" + aa));
        // int dayOfYear = cal.get(java.util.Calendar.DAY_OF_YEAR); //
        // cal.set(java.util.Calendar.DAY_OF_YEAR, (dayOfYear +
        // dayOfTrimester));
        return StringUtils.padBeforeString("" + cal.get(java.util.Calendar.DAY_OF_MONTH), "0", 2) + "."
                + StringUtils.padBeforeString("" + (cal.get(java.util.Calendar.MONTH) + 1), "0", 2) + "."
                + cal.get(java.util.Calendar.YEAR);
    }

    /**
     * public String getFullNumeroAvs() { if (getFullNumeroAvsSet().size() > 0) { return (String)
     * getFullNumeroAvsSet().iterator().next(); } else { return ""; } } * Method getMonthOfTrimester.
     * 
     * @param i
     * @return int
     */
    private static int getMonthOfTrimester(int i) {
        return (3 - ((getTrimester(i) * 3) - i));
    }

    /**
     * Method getSexFromAVS. renvoit 1 pour un homme, 2 pour une femme, vide quand le num�ro est incorrect
     * 
     * @param numAVS
     * @return String
     * @throws JAException
     */
    public static String getSexFromAVS(String numAVS) {
        // 123.45.678.123
        // 12345678123
        numAVS = StringUtils.removeDots(numAVS);
        try {
            JAUtil.checkAvs(numAVS);
        } catch (JAException e) {
            // e.printStackTrace();
            return "";
        }
        int sex = Integer.parseInt(String.valueOf(numAVS.charAt(5)));
        if (sex <= 4) {
            return "1";
        } else {
            return "2";
        }
    }

    /**
     * Method getTrimester.
     * 
     * @param i
     */
    private static int getTrimester(int i) {
        return (int) Math.ceil(Double.parseDouble("" + (i / 3d)));
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

    public static void main(String[] args) {
        BTransaction transaction = null;
        try {
            BSession session = (BSession) GlobazServer.getCurrentSystem().getApplication("PAVO")
                    .newSession("globazf", "ssiiadm");
            // set dans un fichier de sortie
            PrintStream streamOut = new PrintStream(new FileOutputStream("C:/temp/AVSTest.out"));
            System.setOut(streamOut);
            PrintStream streamErr = new PrintStream(new FileOutputStream("C:/temp/AVSTest.err"));
            System.setErr(streamErr);
            // ********************
            CICompteIndividuelManager c = new CICompteIndividuelManager();
            c.setSession(session);
            c.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            // // c.setLikeNumeroAvs(args[0]);
            transaction = new BTransaction(session);
            // Ouverture de la transaction
            transaction.openTransaction();
            BStatement statement = c.cursorOpen(transaction);
            System.out.println("D�but de la recherche");
            CICompteIndividuel crt = null;
            int i = 0;
            while ((crt = (CICompteIndividuel) c.cursorReadNext(statement)) != null) {
                try {
                    AVSUtils a = new AVSUtils(crt.getNomPrenom(), crt.getDateNaissance(), Integer.parseInt(crt
                            .getSexe()));
                    if (!crt.getNumeroAvs().substring(0, 2).equals(a.getNumeroAvs().substring(0, 2))) {
                        System.out.println("Num�ro pas �gal : calcul�=" + a.getNumeroAvs() + " contre num�ro ci="
                                + AVSUtils.formatAVS8Or9(crt.getNumeroAvs()));
                    }
                    if (i % 100 == 0) {
                        System.out.print(".");
                    }
                    if (i % 10000 == 0) {
                        System.out.println(".");
                    }
                    i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Fin du test");
            transaction.commit();
            transaction.closeTransaction();
            c.cursorClose(statement);
        } catch (Exception err) {
            err.printStackTrace();
        } finally {
            if (transaction != null) {
                try {
                    transaction.closeTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.exit(0);
    }

    private TreeMap alphabetMap = new TreeMap();

    private String dateNaissance = "";

    private String exceptionNum = "";

    private TreeMap exceptionsEVoyelleMap = new TreeMap();

    /*
     * public static void main(String[] args) { try { AVSUtils avs = new AVSUtils("SCOTT, JONATHAN ANDREW",
     * "08.12.1969", 2); /*System.out.println(avs.getName() + "," + avs.getNumeroAvs()); avs = new
     * AVSUtils("D'AMENDOLA, CLAUDIO", "08.12.1969", 2); System.out.println(avs.getName() + "," + avs.getNumeroAvs());
     * avs = new AVSUtils("D ALESSIO, GINO", "08.12.1969", 2); System.out.println(avs.getName() + "," +
     * avs.getNumeroAvs()); avs = new AVSUtils("D-AMICO, ANTONIO", "08.12.1969", 2); System.out.println(avs.getName() +
     * "," + avs.getNumeroAvs()); avs = new AVSUtils("MULLER, CARL", "08.12.1969", 2); System.out.println(avs.getName()
     * + "," + avs.getNumeroAvs()); avs = new AVSUtils("MUELLER, CARL", "08.12.1969", 2);
     * System.out.println(avs.getName() + "," + avs.getNumeroAvs()); avs = new AVSUtils("OEBERG, ROGER", "08.12.1969",
     * 2); System.out.println(avs.getName() + "," + avs.getNumeroAvs()); avs = new AVSUtils("SCHAPPAT, INGRID",
     * "08.12.1969", 2); System.out.println(avs.getName() + "," + avs.getNumeroAvs()); avs = new
     * AVSUtils("D-AGOSTINO-MONTICELLI,FILOMENA, JONATHAN ANDREW", "08.12.1969", 2); System.out.println(avs.getName() +
     * "," + avs.getNumeroAvs()); avs = new AVSUtils("SCHWARZENBERGER, COLIN", "08.12.1969", 2);
     * System.out.println(avs.getName() + "," + avs.getNumeroAvs()); avs = new AVSUtils("ZYSSET,EMIL", "08.12.2003", 2);
     * System.out.println(avs.getName() + "," + avs.getNumeroAvs()); avs = new AVSUtils("ZZWTSRA,EMIL", "08.12.2003",
     * 2); System.out.println(avs.getName() + "," + avs.getNumeroAvs()); avs = new AVSUtils("AARTS,EMIL", "08.12.2003",
     * 2); System.out.println(avs.getName() + "," + avs.getNumeroAvs()); avs = new AVSUtils("D ELIA COSIMO",
     * "08.12.2003", 2); System.out.println(avs.getName() + "," + avs.getNumeroAvs()); avs = new
     * AVSUtils("D ORTA-SALERNO,ELVIRA", "08.12.2003", 2); System.out.println(avs.getName() + "," + avs.getNumeroAvs());
     * System.out.println("");
     */

    /*
     * avs = new AVSUtils("AEBI,ELVIRA", "08.12.2003", 2); System.out.println(avs.getName() + "," + avs.getNumeroAvs());
     * avs = new AVSUtils("GUENIN,ELVIRA", "08.12.2003", 2); System.out.println(avs.getName() + "," +
     * avs.getNumeroAvs()); avs = new AVSUtils("RUEEGG,ELVIRA", "08.12.2003", 2); System.out.println(avs.getName() + ","
     * + avs.getNumeroAvs()); avs = new AVSUtils("LEUENBERGER,ELVIRA", "08.12.2003", 2);
     * System.out.println(avs.getName() + "," + avs.getNumeroAvs()); avs = new AVSUtils("AL RAMELELELE,SIDI",
     * "08.12.2003", 2); System.out.println(avs.getName() + "," + avs.getNumeroAvs()); avs = new
     * AVSUtils("HAUESERMANN,MICHAEL", "08.12.2003", 2); System.out.println(avs.getName() + "," + avs.getNumeroAvs());
     * avs = new AVSUtils("BA CHEIKHE,MOHAMED FADAL", "08.12.2003", 2); System.out.println(avs.getName() + "," +
     * avs.getNumeroAvs()); avs = new AVSUtils("SCHMID EGGEN,GEORGES", "08.12.2003", 2);
     * System.out.println(avs.getName() + "," + avs.getNumeroAvs()); avs = new AVSUtils("VON
     * GUNTEN,ALBERT", "08.12.2003", 2); System.out.println(avs.getName() + "
     * ," + avs.getNumeroAvs()); avs = new AVSUtils("
     * SCHWAB,ALPHONSE", "08.12.2003", 2); System.out.println(avs.getName() + ",
     * " + avs.getNumeroAvs()); avs = new AVSUtils(" SCHWEIZ,RODOLPHE", "08.12.2003
     * ", 2); System.out.println(avs.getName() + ", " + avs.getNumeroAvs()); System.out.println("
     * "); avs = new AVSUtils("AMBAUEN ,ALPHONSE", "08.12.2003", 2); System.out.println(avs.getName() + "
     * ," + avs.getNumeroAvs()); avs = new AVSUtils("A
     * MARCA,ALPHONSE", "08.12.2003", 2); System.out.println(avs.getName() + ",
     * " + avs.getNumeroAvs()); avs = new AVSUtils("A
     * PORTA,ALPHONSE", "08.12.2003", 2); System.out.println(avs.getName() + ",
     * " + avs.getNumeroAvs()); avs = new AVSUtils(" BLAUENSTEIN,ALPHONSE", "08.12
     * .2003", 2); System.out.println(avs.getName() + " ," + avs.getNumeroAvs()); avs = new AVSUtils("DE
     * LILLO,ALPHONSE", "08.12.2003", 2); System.out.println(avs.getName() + ",
     * " + avs.getNumeroAvs()); avs = new AVSUtils("DA
     * MOTA,ALPHONSE", "08.12.2003", 2); System.out.println(avs.getName() + ",
     * " + avs.getNumeroAvs()); avs = new AVSUtils(" GEBAUER,ALPHONSE", "08.12.2003
     * ", 2); System.out.println(avs.getName() + ",
     * " + avs.getNumeroAvs()); System.out.println(""); avs = new AVSUtils("HA
     * VINH,ALPHONSE", "08.12.2003", 2); System.out.println(avs.getName() + ",
     * " + avs.getNumeroAvs()); avs = new AVSUtils("
     * FAE,ALPHONSE", "08.12.2003", 2); System.out.println(avs.getName() + "
     * ," + avs.getNumeroAvs()); avs = new AVSUtils("BUR MONNIER,ALPHONSE", "08.12
     * .2003", 2); System.out.println(avs.getName() + ", " + avs.getNumeroAvs()); avs = new AVSUtils("
     * BEINER,ALPHONSE", "08.12.2003 ", 2); System.out.println(avs.getName() + "," + avs.getNumeroAvs()); } catch
     * (Exception err) { err.printStackTrace(); } }
     */

    private String format = DateUtils.JJMMAAAA_DOTS;

    private String name = "";

    private int nameGroup = 0;

    private String[] particules = { "AL", "AM", "DA", "DE", "DI", "DO", "DU", "LA", "LE", "LI", "LO", "VON" };

    private int sexe = 0;

    /**
     * Method AVSUtils.
     * 
     * @param nameGroup
     *            le groupe du nom (123.xx.xxx.xxx) fix� manuellement
     * @param dateNaissance
     *            la date de naissance JJ.MM.AAAA
     * @param sexe
     *            le sexe (1 pour un homme, 2 pour une femme)
     */
    public AVSUtils(int nameGroup, String dateNaissance, int sexe) {
        this.nameGroup = nameGroup;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        format = DateUtils.JJMMAAAA_DOTS;
        initTableRelation(name);
        initExceptionsEVoyelleMap();
    }

    /**
     * Method AVSUtils.
     * 
     * @param name
     *            le nom de famille de la personne
     * @param dateNaissance
     *            la date de naissance JJ.MM.AAAA
     * @param sexe
     *            le sexe (1 pour un homme, 2 pour une femme)
     */
    public AVSUtils(String name, String dateNaissance, int sexe) {
        this.name = name;
        this.dateNaissance = dateNaissance;
        format = DateUtils.JJMMAAAA_DOTS;
        this.sexe = sexe;
        initTableRelation(name);
        initExceptionsEVoyelleMap();
    }

    /**
     * Returns the dateNaissance.
     * 
     * @return String
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return
     */
    public String getExceptionNum() {
        return exceptionNum;
    }

    /**
	 * 
	 */
    public HashSet getFullNumeroAvsSet() throws ParseException {
        HashSet set = new HashSet();
        for (int i = 0; i < 999; i++) {
            try {
                String avs11 = getNumeroAvs() + "." + StringUtils.padBeforeString(i + "", "0", 3);
                JAUtil.checkAvs(avs11);
                set.add(avs11);
            } catch (JAException e) {
            }
        }
        return set;
    }

    /**
     * Returns the name.
     * 
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Method getNameGroup.
     * 
     * @param nom
     * @return String le chiffre correspondant
     */
    public String getNameGroup(String nom) {
        boolean exception = false;
        String key = "";

        // supprimer la virgule
        if (nom.indexOf(",") > 0) {
            nom = nom.substring(0, nom.indexOf(","));
        }
        // traite correctement l'espace dans un nom
        nom = traiteEspace(nom);

        // on ne garde que le premier nom si le nom comporte un tiret (-)
        if (nom.indexOf("-") > 0) {
            nom = nom.substring(0, nom.indexOf("-"));
        }
        // on remplace les � � et � par des diphtongues
        nom = nom.toLowerCase();
        nom = JadeStringUtil.change(nom, "'", "");
        // nom = JadeStringUtil.change(nom, "-", "");
        nom = JadeStringUtil.change(nom, "�", "ae");
        nom = JadeStringUtil.change(nom, "�", "oe");
        nom = JadeStringUtil.change(nom, "�", "ue");

        // v�rifier si le nom correspond aux exceptions
        Object it[] = exceptionsEVoyelleMap.keySet().toArray();
        for (int i = 0; i < it.length; i++) {
            key = (String) it[i];
            // setExceptionNum((String)exceptionsEVoyelleMap.get(it[i]));
            if (nom.toUpperCase().startsWith(key)) {
                setExceptionNum((String) exceptionsEVoyelleMap.get(it[i]));
                exception = true;
                break;
            }
        }

        nom = "   " + nom;
        // si on n'est pas dans le cas d'une exception, application de
        // "la r�gle du e"
        if (!exception) {
            for (int i = 0; i < nom.length(); i++) {
                if (nom.charAt(i) == 'e') {
                    if (isSpecialVoyelle(nom.charAt(i - 1))) {
                        nom = StringUtils.removeCharAt(nom, i);
                        i--;
                    }
                }
            }
        }

        return nameToCode(nom.toUpperCase().trim());
    }

    public String getNumeroAvs() throws JAException, ParseException {
        String avs = "";
        if (nameGroup != 0) {
            avs += nameGroup + ".";
        } else {
            avs += getNameGroup(name);
        }
        avs += ".";
        // ajout de l'ann�e de naissance
        String annee = DateUtils.getYear(dateNaissance, format);
        if (annee.length() == 4) {
            annee = annee.substring(2, 4);
        }
        avs += annee + ".";
        // calcul du sexe, jour et mois
        // / d'abord le numero du trimestre
        Calendar cal = DateUtils.getCalendarFromDate(dateNaissance, format);
        int month = cal.get(Calendar.MONTH) + 1;
        int trimester = getTrimester(month);
        if (sexe == 2) {
            trimester += 4;
        }
        avs += "" + trimester;
        //
        avs += StringUtils.padBeforeString(
                "" + (((getMonthOfTrimester(month) - 1) * 31) + cal.get(Calendar.DAY_OF_MONTH)), "0", 2);
        // JAUtil.checkAvs(avs, sexe);
        return avs;
    }

    private void initExceptionsEVoyelleMap() {
        exceptionsEVoyelleMap.put("BAUE", "143");
        exceptionsEVoyelleMap.put("LEUE", "596");
        exceptionsEVoyelleMap.put("LAUE", "583");
        exceptionsEVoyelleMap.put("SUEU", "798");
        exceptionsEVoyelleMap.put("BAEUE", "143");
        exceptionsEVoyelleMap.put("HAUET", "445");
    }

    /**
     * Method initMap.
     */
    private void initMap() {
        alphabetMap = new TreeMap();
        alphabetMap.put("A", "100");
        alphabetMap.put("ABI", "101");
        alphabetMap.put("ABL", "102");
        alphabetMap.put("AC", "103");
        alphabetMap.put("AF", "104");
        alphabetMap.put("AG", "105");
        alphabetMap.put("AL", "106");
        alphabetMap.put("ALD", "107");
        alphabetMap.put("ALL", "108");
        alphabetMap.put("ALM", "109");
        alphabetMap.put("AM", "110");
        alphabetMap.put("AMD", "111");
        alphabetMap.put("AMM", "112");
        alphabetMap.put("AMO", "113");
        alphabetMap.put("AMS", "114");
        alphabetMap.put("AN", "115");
        alphabetMap.put("ANDO", "116");
        alphabetMap.put("ANE", "117");
        alphabetMap.put("ANN", "118");
        alphabetMap.put("AO", "119");
        alphabetMap.put("AR", "120");
        alphabetMap.put("ARNO", "121");
        alphabetMap.put("ARO", "122");
        alphabetMap.put("AS", "123");
        alphabetMap.put("AU", "124");
        alphabetMap.put("B", "125");
        alphabetMap.put("BACH", "126");
        alphabetMap.put("BACHM", "127");
        alphabetMap.put("BAD", "128");
        alphabetMap.put("BAG", "129");
        alphabetMap.put("BAL", "130");
        alphabetMap.put("BALL", "131");
        alphabetMap.put("BALM", "132");
        alphabetMap.put("BALS", "133");
        alphabetMap.put("BAM", "134");
        alphabetMap.put("BAN", "135");
        alphabetMap.put("BAR", "136");
        alphabetMap.put("BARC", "137");
        alphabetMap.put("BARM", "138");
        alphabetMap.put("BART", "139");
        alphabetMap.put("BAS", "140");
        alphabetMap.put("BAT", "141");
        alphabetMap.put("BAU", "142");
        alphabetMap.put("BAUE", "143");
        alphabetMap.put("BAUM", "144");
        alphabetMap.put("BAUMB", "145");
        alphabetMap.put("BAUMG", "146");
        alphabetMap.put("BAUN", "147");
        alphabetMap.put("BAV", "148");
        alphabetMap.put("BAY", "149");
        alphabetMap.put("BE", "150");
        alphabetMap.put("BED", "151");
        alphabetMap.put("BEF", "152");
        alphabetMap.put("BEL", "153");
        alphabetMap.put("BEN", "154");
        alphabetMap.put("BER", "155");
        alphabetMap.put("BERG", "156");
        alphabetMap.put("BERH", "157");
        alphabetMap.put("BERN", "158");
        alphabetMap.put("BERNE", "159");
        alphabetMap.put("BERO", "160");
        alphabetMap.put("BERT", "161");
        alphabetMap.put("BES", "162");
        alphabetMap.put("BEU", "163");
        alphabetMap.put("BF", "164");
        alphabetMap.put("BI", "165");
        alphabetMap.put("BIE", "166");
        alphabetMap.put("BIER", "167");
        alphabetMap.put("BIF", "168");
        alphabetMap.put("BIL", "169");
        alphabetMap.put("BIN", "170");
        alphabetMap.put("BIO", "171");
        alphabetMap.put("BIS", "172");
        alphabetMap.put("BISE", "173");
        alphabetMap.put("BIT", "174");
        alphabetMap.put("BL", "175");
        alphabetMap.put("BLAS", "176");
        alphabetMap.put("BLAT", "177");
        alphabetMap.put("BLE", "178");
        alphabetMap.put("BLU", "179");
        alphabetMap.put("BO", "180");
        alphabetMap.put("BOD", "181");
        alphabetMap.put("BOF", "182");
        alphabetMap.put("BOH", "183");
        alphabetMap.put("BOI", "184");
        alphabetMap.put("BOL", "185");
        alphabetMap.put("BOLL", "186");
        alphabetMap.put("BOLLI", "187");
        alphabetMap.put("BOLM", "188");
        alphabetMap.put("BOM", "189");
        alphabetMap.put("BON", "190");
        alphabetMap.put("BOO", "191");
        alphabetMap.put("BOR", "192");
        alphabetMap.put("BORG", "193");
        alphabetMap.put("BORN", "194");
        alphabetMap.put("BOS", "195");
        alphabetMap.put("BOSS", "196");
        alphabetMap.put("BOSSH", "197");
        alphabetMap.put("BOT", "198");
        alphabetMap.put("BOV", "199");
        alphabetMap.put("BR", "200");
        alphabetMap.put("BRAG", "201");
        alphabetMap.put("BRAN", "202");
        alphabetMap.put("BRAP", "203");
        alphabetMap.put("BRAU", "204");
        alphabetMap.put("BRE", "205");
        alphabetMap.put("BREG", "206");
        alphabetMap.put("BREL", "207");
        alphabetMap.put("BRES", "208");
        alphabetMap.put("BRI", "209");
        alphabetMap.put("BRO", "210");
        alphabetMap.put("BROF", "211");
        alphabetMap.put("BRON", "212");
        alphabetMap.put("BRONN", "213");
        alphabetMap.put("BROO", "214");
        alphabetMap.put("BRU", "215");
        alphabetMap.put("BRUD", "216");
        alphabetMap.put("BRUG", "217");
        alphabetMap.put("BRUH", "218");
        alphabetMap.put("BRUL", "219");
        alphabetMap.put("BRUN", "220");
        alphabetMap.put("BRUND", "221");
        alphabetMap.put("BRUNN", "222");
        alphabetMap.put("BRUNO", "223");
        alphabetMap.put("BRUO", "224");
        alphabetMap.put("BU", "225");
        alphabetMap.put("BUCH", "226");
        alphabetMap.put("BUCHI", "227");
        alphabetMap.put("BUCHO", "228");
        alphabetMap.put("BUCI", "229");
        alphabetMap.put("BUD", "230");
        alphabetMap.put("BUH", "231");
        alphabetMap.put("BUHLM", "232");
        alphabetMap.put("BUHM", "233");
        alphabetMap.put("BUI", "234");
        alphabetMap.put("BUR", "235");
        alphabetMap.put("BURG", "236");
        alphabetMap.put("BURGI", "237");
        alphabetMap.put("BURH", "238");
        alphabetMap.put("BURK", "239");
        alphabetMap.put("BURKH", "240");
        alphabetMap.put("BURKI", "241");
        alphabetMap.put("BURL", "242");
        alphabetMap.put("BURR", "243");
        alphabetMap.put("BURS", "244");
        alphabetMap.put("BUS", "245");
        alphabetMap.put("BUSS", "246");
        alphabetMap.put("BUT", "247");
        alphabetMap.put("BUTT", "248");
        alphabetMap.put("BY", "249");
        alphabetMap.put("C", "250");
        alphabetMap.put("CAI", "251");
        alphabetMap.put("CAM", "252");
        alphabetMap.put("CAN", "253");
        alphabetMap.put("CAR", "254");
        alphabetMap.put("CAS", "255");
        alphabetMap.put("CAST", "256");
        alphabetMap.put("CAT", "257");
        alphabetMap.put("CAV", "258");
        alphabetMap.put("CE", "259");
        alphabetMap.put("CH", "260");
        alphabetMap.put("CHAR", "261");
        alphabetMap.put("CHE", "262");
        alphabetMap.put("CHI", "263");
        alphabetMap.put("CHR", "264");
        alphabetMap.put("CI", "265");
        alphabetMap.put("CL", "266");
        alphabetMap.put("CO", "267");
        alphabetMap.put("COM", "268");
        alphabetMap.put("COR", "269");
        alphabetMap.put("CORN", "270");
        alphabetMap.put("COS", "271");
        alphabetMap.put("CR", "272");
        alphabetMap.put("CRI", "273");
        alphabetMap.put("CU", "274");
        alphabetMap.put("D", "275");
        alphabetMap.put("DAM", "276");
        alphabetMap.put("DAS", "277");
        alphabetMap.put("DE", "278");
        alphabetMap.put("DEG", "279");
        alphabetMap.put("DEL", "280");
        alphabetMap.put("DEM", "281");
        alphabetMap.put("DEP", "282");
        alphabetMap.put("DES", "283");
        alphabetMap.put("DET", "284");
        alphabetMap.put("DI", "285");
        alphabetMap.put("DIE", "286");
        alphabetMap.put("DIG", "287");
        alphabetMap.put("DO", "288");
        alphabetMap.put("DOR", "289");
        alphabetMap.put("DR", "290");
        alphabetMap.put("DU", "291");
        alphabetMap.put("DUB", "292");
        alphabetMap.put("DUC", "293");
        alphabetMap.put("DUD", "294");
        alphabetMap.put("DUM", "295");
        alphabetMap.put("DUP", "296");
        alphabetMap.put("DUR", "297");
        alphabetMap.put("DUS", "298");
        alphabetMap.put("DUV", "299");
        alphabetMap.put("E", "300");
        alphabetMap.put("EBI", "301");
        alphabetMap.put("EC", "302");
        alphabetMap.put("EG", "303");
        alphabetMap.put("EGGEN", "304");
        alphabetMap.put("EGGER", "305");
        alphabetMap.put("EGGF", "306");
        alphabetMap.put("EGH", "307");
        alphabetMap.put("EH", "308");
        alphabetMap.put("EHRE", "309");
        alphabetMap.put("EI", "310");
        alphabetMap.put("EICHF", "311");
        alphabetMap.put("EID", "312");
        alphabetMap.put("EIN", "313");
        alphabetMap.put("EK", "314");
        alphabetMap.put("EM", "315");
        alphabetMap.put("EN", "316");
        alphabetMap.put("ENG", "317");
        alphabetMap.put("ENI", "318");
        alphabetMap.put("EO", "319");
        alphabetMap.put("ER", "320");
        alphabetMap.put("ERN", "321");
        alphabetMap.put("ES", "322");
        alphabetMap.put("ET", "323");
        alphabetMap.put("EU", "324");
        alphabetMap.put("F", "325");
        alphabetMap.put("FAH", "326");
        alphabetMap.put("FAI", "327");
        alphabetMap.put("FAN", "328");
        alphabetMap.put("FAR", "329");
        alphabetMap.put("FAS", "330");
        alphabetMap.put("FASS", "331");
        alphabetMap.put("FAT", "332");
        alphabetMap.put("FAV", "333");
        alphabetMap.put("FAW", "334");
        alphabetMap.put("FE", "335");
        alphabetMap.put("FEL", "336");
        alphabetMap.put("FELI", "337");
        alphabetMap.put("FEM", "338");
        alphabetMap.put("FET", "339");
        alphabetMap.put("FI", "340");
        alphabetMap.put("FIN", "341");
        alphabetMap.put("FISCH", "342");
        alphabetMap.put("FISD", "343");
        alphabetMap.put("FIT", "344");
        alphabetMap.put("FL", "345");
        alphabetMap.put("FLE", "346");
        alphabetMap.put("FLI", "347");
        alphabetMap.put("FLU", "348");
        alphabetMap.put("FLUD", "349");
        alphabetMap.put("FO", "350");
        alphabetMap.put("FON", "351");
        alphabetMap.put("FOR", "352");
        alphabetMap.put("FORR", "353");
        alphabetMap.put("FOS", "354");
        alphabetMap.put("FR", "355");
        alphabetMap.put("FRAN", "356");
        alphabetMap.put("FRAP", "357");
        alphabetMap.put("FRE", "358");
        alphabetMap.put("FREI", "359");
        alphabetMap.put("FREK", "360");
        alphabetMap.put("FREY", "361");
        alphabetMap.put("FRI", "362");
        alphabetMap.put("FRIE", "363");
        alphabetMap.put("FRIF", "364");
        alphabetMap.put("FRIS", "365");
        alphabetMap.put("FRIT", "366");
        alphabetMap.put("FRO", "367");
        alphabetMap.put("FROI", "368");
        alphabetMap.put("FRU", "369");
        alphabetMap.put("FU", "370");
        alphabetMap.put("FUD", "371");
        alphabetMap.put("FUI", "372");
        alphabetMap.put("FUR", "373");
        alphabetMap.put("FUS", "374");
        alphabetMap.put("G", "375");
        alphabetMap.put("GAF", "376");
        alphabetMap.put("GAL", "377");
        alphabetMap.put("GAM", "378");
        alphabetMap.put("GAN", "379");
        alphabetMap.put("GAP", "380");
        alphabetMap.put("GAS", "381");
        alphabetMap.put("GASS", "382");
        alphabetMap.put("GAT", "383");
        alphabetMap.put("GAV", "384");
        alphabetMap.put("GE", "385");
        alphabetMap.put("GEH", "386");
        alphabetMap.put("GEI", "387");
        alphabetMap.put("GEL", "388");
        alphabetMap.put("GEO", "389");
        alphabetMap.put("GER", "390");
        alphabetMap.put("GERD", "391");
        alphabetMap.put("GES", "392");
        alphabetMap.put("GF", "393");
        alphabetMap.put("GI", "394");
        alphabetMap.put("GIG", "395");
        alphabetMap.put("GIL", "396");
        alphabetMap.put("GIM", "397");
        alphabetMap.put("GIR", "398");
        alphabetMap.put("GIS", "399");
        alphabetMap.put("GL", "400");
        alphabetMap.put("GLE", "401");
        alphabetMap.put("GM", "402");
        alphabetMap.put("GO", "403");
        alphabetMap.put("GOM", "404");
        alphabetMap.put("GR", "405");
        alphabetMap.put("GRAF", "406");
        alphabetMap.put("GRAG", "407");
        alphabetMap.put("GRE", "408");
        alphabetMap.put("GRES", "409");
        alphabetMap.put("GRI", "410");
        alphabetMap.put("GRIM", "411");
        alphabetMap.put("GRO", "412");
        alphabetMap.put("GROS", "413");
        alphabetMap.put("GROT", "414");
        alphabetMap.put("GRU", "415");
        alphabetMap.put("GRUP", "416");
        alphabetMap.put("GS", "417");
        alphabetMap.put("GU", "418");
        alphabetMap.put("GUD", "419");
        alphabetMap.put("GUG", "420");
        alphabetMap.put("GUI", "421");
        alphabetMap.put("GUN", "422");
        alphabetMap.put("GUT", "423");
        alphabetMap.put("GW", "424");
        alphabetMap.put("H", "425");
        alphabetMap.put("HAB", "426");
        alphabetMap.put("HABI", "427");
        alphabetMap.put("HAC", "428");
        alphabetMap.put("HAD", "429");
        alphabetMap.put("HAF", "430");
        alphabetMap.put("HAFL", "431");
        alphabetMap.put("HAFN", "432");
        alphabetMap.put("HAG", "433");
        alphabetMap.put("HAH", "434");
        alphabetMap.put("HAL", "435");
        alphabetMap.put("HALL", "436");
        alphabetMap.put("HAM", "437");
        alphabetMap.put("HAN", "438");
        alphabetMap.put("HANN", "439");
        alphabetMap.put("HAO", "440");
        alphabetMap.put("HAR", "441");
        alphabetMap.put("HART", "442");
        alphabetMap.put("HAS", "443");
        alphabetMap.put("HAT", "444");
        alphabetMap.put("HAU", "445");
        alphabetMap.put("HAUS", "446");
        alphabetMap.put("HAUSF", "447");
        alphabetMap.put("HAUT", "448");
        alphabetMap.put("HAV", "449");
        alphabetMap.put("HE", "450");
        alphabetMap.put("HEE", "451");
        alphabetMap.put("HEG", "452");
        alphabetMap.put("HEI", "453");
        alphabetMap.put("HEIN", "454");
        alphabetMap.put("HEL", "455");
        alphabetMap.put("HELF", "456");
        alphabetMap.put("HELL", "457");
        alphabetMap.put("HEM", "458");
        alphabetMap.put("HEN", "459");
        alphabetMap.put("HENG", "460");
        alphabetMap.put("HENO", "461");
        alphabetMap.put("HEO", "462");
        alphabetMap.put("HER", "463");
        alphabetMap.put("HERM", "464");
        alphabetMap.put("HERR", "465");
        alphabetMap.put("HERS", "466");
        alphabetMap.put("HERZ", "467");
        alphabetMap.put("HES", "468");
        alphabetMap.put("HET", "469");
        alphabetMap.put("HI", "470");
        alphabetMap.put("HIM", "471");
        alphabetMap.put("HIR", "472");
        alphabetMap.put("HIRT", "473");
        alphabetMap.put("HIS", "474");
        alphabetMap.put("HO", "475");
        alphabetMap.put("HOF", "476");
        alphabetMap.put("HOFF", "477");
        alphabetMap.put("HOFM", "478");
        alphabetMap.put("HOFN", "479");
        alphabetMap.put("HOG", "480");
        alphabetMap.put("HOL", "481");
        alphabetMap.put("HOM", "482");
        alphabetMap.put("HOR", "483");
        alphabetMap.put("HOT", "484");
        alphabetMap.put("HU", "485");
        alphabetMap.put("HUBF", "486");
        alphabetMap.put("HUC", "487");
        alphabetMap.put("HUG", "488");
        alphabetMap.put("HUGI", "489");
        alphabetMap.put("HUH", "490");
        alphabetMap.put("HUN", "491");
        alphabetMap.put("HUNI", "492");
        alphabetMap.put("HUNZ", "493");
        alphabetMap.put("HUO", "494");
        alphabetMap.put("HUR", "495");
        alphabetMap.put("HURN", "496");
        alphabetMap.put("HUS", "497");
        alphabetMap.put("HUT", "498");
        alphabetMap.put("HUW", "499");
        alphabetMap.put("I", "500");
        alphabetMap.put("IM", "501");
        alphabetMap.put("IN", "502");
        alphabetMap.put("IS", "503");
        alphabetMap.put("IT", "504");
        alphabetMap.put("J", "505");
        alphabetMap.put("JAD", "506");
        alphabetMap.put("JAH", "507");
        alphabetMap.put("JAN", "508");
        alphabetMap.put("JAR", "509");
        alphabetMap.put("JE", "510");
        alphabetMap.put("JEC", "511");
        alphabetMap.put("JEH", "512");
        alphabetMap.put("JEN", "513");
        alphabetMap.put("JEP", "514");
        alphabetMap.put("JO", "515");
        alphabetMap.put("JOL", "516");
        alphabetMap.put("JOR", "517");
        alphabetMap.put("JOS", "518");
        alphabetMap.put("JOT", "519");
        alphabetMap.put("JU", "520");
        alphabetMap.put("JUF", "521");
        alphabetMap.put("JUN", "522");
        alphabetMap.put("JUNK", "523");
        alphabetMap.put("JUO", "524");
        alphabetMap.put("K", "525");
        alphabetMap.put("KAH", "526");
        alphabetMap.put("KAL", "527");
        alphabetMap.put("KAM", "528");
        alphabetMap.put("KAN", "529");
        alphabetMap.put("KAR", "530");
        alphabetMap.put("KAS", "531");
        alphabetMap.put("KASP", "532");
        alphabetMap.put("KAT", "533");
        alphabetMap.put("KAU", "534");
        alphabetMap.put("KE", "535");
        alphabetMap.put("KEL", "536");
        alphabetMap.put("KEM", "537");
        alphabetMap.put("KES", "538");
        alphabetMap.put("KET", "539");
        alphabetMap.put("KI", "540");
        alphabetMap.put("KIF", "541");
        alphabetMap.put("KIM", "542");
        alphabetMap.put("KIR", "543");
        alphabetMap.put("KIS", "544");
        alphabetMap.put("KL", "545");
        alphabetMap.put("KLE", "546");
        alphabetMap.put("KLI", "547");
        alphabetMap.put("KN", "548");
        alphabetMap.put("KNO", "549");
        alphabetMap.put("KO", "550");
        alphabetMap.put("KOC", "551");
        alphabetMap.put("KOD", "552");
        alphabetMap.put("KOH", "553");
        alphabetMap.put("KOL", "554");
        alphabetMap.put("KOLL", "555");
        alphabetMap.put("KOM", "556");
        alphabetMap.put("KOP", "557");
        alphabetMap.put("KOR", "558");
        alphabetMap.put("KOT", "559");
        alphabetMap.put("KR", "560");
        alphabetMap.put("KRAP", "561");
        alphabetMap.put("KRE", "562");
        alphabetMap.put("KRI", "563");
        alphabetMap.put("KRU", "564");
        alphabetMap.put("KU", "565");
        alphabetMap.put("KUC", "566");
        alphabetMap.put("KUH", "567");
        alphabetMap.put("KUI", "568");
        alphabetMap.put("KUN", "569");
        alphabetMap.put("KUNZ", "570");
        alphabetMap.put("KUNZI", "571");
        alphabetMap.put("KUO", "572");
        alphabetMap.put("KUR", "573");
        alphabetMap.put("KUS", "574");
        alphabetMap.put("L", "575");
        alphabetMap.put("LAF", "576");
        alphabetMap.put("LAN", "577");
        alphabetMap.put("LANG", "578");
        alphabetMap.put("LANH", "579");
        alphabetMap.put("LAO", "580");
        alphabetMap.put("LAT", "581");
        alphabetMap.put("LAU", "582");
        alphabetMap.put("LAUD", "583");
        alphabetMap.put("LAV", "584");
        alphabetMap.put("LE", "585");
        alphabetMap.put("LED", "586");
        alphabetMap.put("LEE", "587");
        alphabetMap.put("LEH", "588");
        alphabetMap.put("LEHN", "589");
        alphabetMap.put("LEI", "590");
        alphabetMap.put("LEIP", "591");
        alphabetMap.put("LEK", "592");
        alphabetMap.put("LEO", "593");
        alphabetMap.put("LER", "594");
        alphabetMap.put("LEU", "595");
        alphabetMap.put("LEUE", "596");
        alphabetMap.put("LEUF", "597");
        alphabetMap.put("LEUT", "598");
        alphabetMap.put("LEV", "599");
        alphabetMap.put("LI", "600");
        alphabetMap.put("LIE", "601");
        alphabetMap.put("LIEC", "602");
        alphabetMap.put("LIED", "603");
        alphabetMap.put("LIF", "604");
        alphabetMap.put("LIN", "605");
        alphabetMap.put("LIND", "606");
        alphabetMap.put("LINE", "607");
        alphabetMap.put("LIO", "608");
        alphabetMap.put("LIS", "609");
        alphabetMap.put("LO", "610");
        alphabetMap.put("LOF", "611");
        alphabetMap.put("LOO", "612");
        alphabetMap.put("LOR", "613");
        alphabetMap.put("LOT", "614");
        alphabetMap.put("LU", "615");
        alphabetMap.put("LUD", "616");
        alphabetMap.put("LUG", "617");
        alphabetMap.put("LUI", "618");
        alphabetMap.put("LUS", "619");
        alphabetMap.put("LUT", "620");
        alphabetMap.put("LUTH", "621");
        alphabetMap.put("LUTI", "622");
        alphabetMap.put("LUU", "623");
        alphabetMap.put("LY", "624");
        alphabetMap.put("M", "625");
        alphabetMap.put("MAD", "626");
        alphabetMap.put("MAG", "627");
        alphabetMap.put("MAI", "628");
        alphabetMap.put("MAK", "629");
        alphabetMap.put("MAR", "630");
        alphabetMap.put("MARG", "631");
        alphabetMap.put("MART", "632");
        alphabetMap.put("MARTIN", "633");
        alphabetMap.put("MARU", "634");
        alphabetMap.put("MAS", "635");
        alphabetMap.put("MAT", "636");
        alphabetMap.put("MATT", "637");
        alphabetMap.put("MAU", "638");
        alphabetMap.put("MAY", "639");
        alphabetMap.put("ME", "640");
        alphabetMap.put("MEIER", "641");
        alphabetMap.put("MEIF", "642");
        alphabetMap.put("MEK", "643");
        alphabetMap.put("MER", "644");
        alphabetMap.put("MES", "645");
        alphabetMap.put("MET", "646");
        alphabetMap.put("MEY", "647");
        alphabetMap.put("MEYER", "648");
        alphabetMap.put("MEYF", "649");
        alphabetMap.put("MI", "650");
        alphabetMap.put("MIC", "651");
        alphabetMap.put("MID", "652");
        alphabetMap.put("MIN", "653");
        alphabetMap.put("MIR", "654");
        alphabetMap.put("MO", "655");
        alphabetMap.put("MOI", "656");
        alphabetMap.put("MON", "657");
        alphabetMap.put("MONN", "658");
        alphabetMap.put("MONO", "659");
        alphabetMap.put("MOO", "660");
        alphabetMap.put("MOR", "661");
        alphabetMap.put("MORE", "662");
        alphabetMap.put("MORG", "663");
        alphabetMap.put("MOS", "664");
        alphabetMap.put("MOSE", "665");
        alphabetMap.put("MOSI", "666");
        alphabetMap.put("MOT", "667");
        alphabetMap.put("MU", "668");
        alphabetMap.put("MUH", "669");
        alphabetMap.put("MUL", "670");
        alphabetMap.put("MULL", "671");
        alphabetMap.put("MUM", "672");
        alphabetMap.put("MUR", "673");
        alphabetMap.put("MUS", "674");
        alphabetMap.put("N", "675");
        alphabetMap.put("NAG", "676");
        alphabetMap.put("NE", "677");
        alphabetMap.put("NEG", "678");
        alphabetMap.put("NEU", "679");
        alphabetMap.put("NI", "680");
        alphabetMap.put("NIE", "681");
        alphabetMap.put("NIEDERG", "682");
        alphabetMap.put("NIEF", "683");
        alphabetMap.put("NIF", "684");
        alphabetMap.put("NO", "685");
        alphabetMap.put("NOT", "686");
        alphabetMap.put("NU", "687");
        alphabetMap.put("NY", "688");
        alphabetMap.put("NYF", "689");
        alphabetMap.put("O", "690");
        alphabetMap.put("OBI", "691");
        alphabetMap.put("OC", "692");
        alphabetMap.put("OD", "693");
        alphabetMap.put("OF", "694");
        alphabetMap.put("OK", "695");
        alphabetMap.put("OP", "696");
        alphabetMap.put("OS", "697");
        alphabetMap.put("OT", "698");
        alphabetMap.put("OU", "699");
        alphabetMap.put("P", "700");
        alphabetMap.put("PAG", "701");
        alphabetMap.put("PAN", "702");
        alphabetMap.put("PAR", "703");
        alphabetMap.put("PAT", "704");
        alphabetMap.put("PE", "705");
        alphabetMap.put("PEL", "706");
        alphabetMap.put("PER", "707");
        alphabetMap.put("PERRI", "708");
        alphabetMap.put("PERS", "709");
        alphabetMap.put("PES", "710");
        alphabetMap.put("PET", "711");
        alphabetMap.put("PEU", "712");
        alphabetMap.put("PF", "713");
        alphabetMap.put("PFI", "714");
        alphabetMap.put("PH", "715");
        alphabetMap.put("PI", "716");
        alphabetMap.put("PIG", "717");
        alphabetMap.put("PIR", "718");
        alphabetMap.put("PL", "719");
        alphabetMap.put("PO", "720");
        alphabetMap.put("POR", "721");
        alphabetMap.put("PR", "722");
        alphabetMap.put("PU", "723");
        alphabetMap.put("Q", "724");
        alphabetMap.put("R", "725");
        alphabetMap.put("RAM", "726");
        alphabetMap.put("RAN", "727");
        alphabetMap.put("RAS", "728");
        alphabetMap.put("RAU", "729");
        alphabetMap.put("RE", "730");
        alphabetMap.put("REC", "731");
        alphabetMap.put("REI", "732");
        alphabetMap.put("REIN", "733");
        alphabetMap.put("REK", "734");
        alphabetMap.put("REN", "735");
        alphabetMap.put("RENI", "736");
        alphabetMap.put("REO", "737");
        alphabetMap.put("REY", "738");
        alphabetMap.put("RH", "739");
        alphabetMap.put("RI", "740");
        alphabetMap.put("RIC", "741");
        alphabetMap.put("RID", "742");
        alphabetMap.put("RIE", "743");
        alphabetMap.put("RIEG", "744");
        alphabetMap.put("RIF", "745");
        alphabetMap.put("RIN", "746");
        alphabetMap.put("RIO", "747");
        alphabetMap.put("RIT", "748");
        alphabetMap.put("RIV", "749");
        alphabetMap.put("RO", "750");
        alphabetMap.put("ROC", "751");
        alphabetMap.put("ROD", "752");
        alphabetMap.put("ROH", "753");
        alphabetMap.put("ROHR", "754");
        alphabetMap.put("ROI", "755");
        alphabetMap.put("ROM", "756");
        alphabetMap.put("ROO", "757");
        alphabetMap.put("ROS", "758");
        alphabetMap.put("ROSS", "759");
        alphabetMap.put("ROT", "760");
        alphabetMap.put("ROTH", "761");
        alphabetMap.put("ROTHA", "762");
        alphabetMap.put("ROTI", "763");
        alphabetMap.put("ROU", "764");
        alphabetMap.put("RU", "765");
        alphabetMap.put("RUC", "766");
        alphabetMap.put("RUD", "767");
        alphabetMap.put("RUF", "768");
        alphabetMap.put("RUG", "769");
        alphabetMap.put("RUH", "770");
        alphabetMap.put("RUP", "771");
        alphabetMap.put("RUS", "772");
        alphabetMap.put("RUT", "773");
        alphabetMap.put("RY", "774");
        alphabetMap.put("S", "775");
        alphabetMap.put("SAL", "776");
        alphabetMap.put("SAM", "777");
        alphabetMap.put("SAR", "778");
        alphabetMap.put("SAV", "779");
        alphabetMap.put("SB", "780");
        alphabetMap.put("SE", "781");
        alphabetMap.put("SEI", "782");
        alphabetMap.put("SEN", "783");
        alphabetMap.put("SEO", "784");
        alphabetMap.put("SF", "785");
        alphabetMap.put("SI", "786");
        alphabetMap.put("SIEG", "787");
        alphabetMap.put("SIF", "788");
        alphabetMap.put("SIM", "789");
        alphabetMap.put("SK", "790");
        alphabetMap.put("SO", "791");
        alphabetMap.put("SOM", "792");
        alphabetMap.put("SP", "793");
        alphabetMap.put("SPI", "794");
        alphabetMap.put("SPO", "795");
        alphabetMap.put("SQ", "796");
        alphabetMap.put("SU", "797");
        alphabetMap.put("SUT", "798");
        alphabetMap.put("SV", "799");
        alphabetMap.put("T", "875");
        alphabetMap.put("TAN", "876");
        alphabetMap.put("TAO", "877");
        alphabetMap.put("TE", "878");
        alphabetMap.put("TER", "879");
        alphabetMap.put("TH", "880");
        alphabetMap.put("THE", "881");
        alphabetMap.put("THO", "882");
        alphabetMap.put("THON", "883");
        alphabetMap.put("THR", "884");
        alphabetMap.put("TI", "885");
        alphabetMap.put("TIR", "886");
        alphabetMap.put("TO", "887");
        alphabetMap.put("TOG", "888");
        alphabetMap.put("TOR", "889");
        alphabetMap.put("TR", "890");
        alphabetMap.put("TRE", "891");
        alphabetMap.put("TRI", "892");
        alphabetMap.put("TRO", "893");
        alphabetMap.put("TRU", "894");
        alphabetMap.put("TS", "895");
        alphabetMap.put("TSCHA", "896");
        alphabetMap.put("TSCHE", "897");
        alphabetMap.put("TSCHU", "898");
        alphabetMap.put("TU", "899");
        alphabetMap.put("U", "900");
        alphabetMap.put("UF", "901");
        alphabetMap.put("UL", "902");
        alphabetMap.put("UM", "903");
        alphabetMap.put("UR", "904");
        alphabetMap.put("V", "905");
        alphabetMap.put("VAL", "906");
        alphabetMap.put("VAM", "907");
        alphabetMap.put("VAR", "908");
        alphabetMap.put("VAU", "909");
        alphabetMap.put("VE", "910");
        alphabetMap.put("VET", "911");
        alphabetMap.put("VI", "912");
        alphabetMap.put("VIL", "913");
        alphabetMap.put("VIO", "914");
        alphabetMap.put("VO", "915");
        alphabetMap.put("VOG", "916");
        alphabetMap.put("VOGT", "917");
        alphabetMap.put("VOH", "918");
        alphabetMap.put("VOL", "919");
        alphabetMap.put("VON", "920");
        alphabetMap.put("VONG", "921");
        alphabetMap.put("VONO", "922");
        alphabetMap.put("VOO", "923");
        alphabetMap.put("VU", "924");
        alphabetMap.put("W", "925");
        alphabetMap.put("WAG", "926");
        alphabetMap.put("WAH", "927");
        alphabetMap.put("WAL", "928");
        alphabetMap.put("WALD", "929");
        alphabetMap.put("WALE", "930");
        alphabetMap.put("WALL", "931");
        alphabetMap.put("WALT", "932");
        alphabetMap.put("WAM", "933");
        alphabetMap.put("WAP", "934");
        alphabetMap.put("WAS", "935");
        alphabetMap.put("WAT", "936");
        alphabetMap.put("WE", "937");
        alphabetMap.put("WEC", "938");
        alphabetMap.put("WEH", "939");
        alphabetMap.put("WEI", "940");
        alphabetMap.put("WEIF", "941");
        alphabetMap.put("WEIS", "942");
        alphabetMap.put("WEIT", "943");
        alphabetMap.put("WEK", "944");
        alphabetMap.put("WEN", "945");
        alphabetMap.put("WER", "946");
        alphabetMap.put("WERN", "947");
        alphabetMap.put("WES", "948");
        alphabetMap.put("WEU", "949");
        alphabetMap.put("WI", "950");
        alphabetMap.put("WID", "951");
        alphabetMap.put("WIE", "952");
        alphabetMap.put("WIEL", "953");
        alphabetMap.put("WIF", "954");
        alphabetMap.put("WIL", "955");
        alphabetMap.put("WILE", "956");
        alphabetMap.put("WIM", "957");
        alphabetMap.put("WIP", "958");
        alphabetMap.put("WIR", "959");
        alphabetMap.put("WIS", "960");
        alphabetMap.put("WIT", "961");
        alphabetMap.put("WITT", "962");
        alphabetMap.put("WO", "963");
        alphabetMap.put("WOL", "964");
        alphabetMap.put("WU", "965");
        alphabetMap.put("WUI", "966");
        alphabetMap.put("WUN", "967");
        alphabetMap.put("WUR", "968");
        alphabetMap.put("WUT", "969");
        alphabetMap.put("WY", "970");
        alphabetMap.put("WYM", "971");
        alphabetMap.put("WYS", "972");
        alphabetMap.put("WYSS", "973");
        alphabetMap.put("WYT", "974");
        alphabetMap.put("X-Y", "975");
        alphabetMap.put("Z", "976");
        alphabetMap.put("ZAM", "977");
        alphabetMap.put("ZAU", "978");
        alphabetMap.put("ZB", "979");
        alphabetMap.put("ZE", "980");
        alphabetMap.put("ZEH", "981");
        alphabetMap.put("ZEI", "982");
        alphabetMap.put("ZEM", "983");
        alphabetMap.put("ZF", "984");
        alphabetMap.put("ZI", "985");
        alphabetMap.put("ZIM", "986");
        alphabetMap.put("ZIN", "987");
        alphabetMap.put("ZK", "988");
        alphabetMap.put("ZO", "989");
        alphabetMap.put("ZU", "990");
        alphabetMap.put("ZUC", "991");
        alphabetMap.put("ZUL", "992");
        alphabetMap.put("ZUM", "993");
        alphabetMap.put("ZUN", "994");
        alphabetMap.put("ZUR", "995");
        alphabetMap.put("ZUS", "996");
        alphabetMap.put("ZW", "997");
        alphabetMap.put("ZWE", "998");
        alphabetMap.put("ZY", "999");
        alphabetMap.put("ZZZZZZZ", "");
    }

    private void initSchMap() {
        alphabetMap.put("SCH", "800");
        alphabetMap.put("SCHAD", "801");
        alphabetMap.put("SCHAF", "802");
        alphabetMap.put("SCHAL", "803");
        alphabetMap.put("SCHAR", "804");
        alphabetMap.put("SCHARB", "805");
        alphabetMap.put("SCHARL", "806");
        alphabetMap.put("SCHAS", "807");
        alphabetMap.put("SCHAU", "808");
        alphabetMap.put("SCHAV", "809");
        alphabetMap.put("SCHE", "810");
        alphabetMap.put("SCHEL", "811");
        alphabetMap.put("SCHEN", "812");
        alphabetMap.put("SCHER", "813");
        alphabetMap.put("SCHEU", "814");
        alphabetMap.put("SCHI", "815");
        alphabetMap.put("SCHIL", "816");
        alphabetMap.put("SCHL", "817");
        alphabetMap.put("SCHLE", "818");
        alphabetMap.put("SCHLU", "819");
        alphabetMap.put("SCHM", "820");
        alphabetMap.put("SCHMID", "821");
        alphabetMap.put("SCHMIDA", "822");
        alphabetMap.put("SCHMIE", "823");
        alphabetMap.put("SCHMO", "824");
        alphabetMap.put("SCHN", "825");
        alphabetMap.put("SCHNEI", "826");
        alphabetMap.put("SCHNEK", "827");
        alphabetMap.put("SCHNI", "828");
        alphabetMap.put("SCHNY", "829");
        alphabetMap.put("SCHO", "830");
        alphabetMap.put("SCHON", "831");
        alphabetMap.put("SCHOO", "832");
        alphabetMap.put("SCHOR", "833");
        alphabetMap.put("SCHR", "834");
        alphabetMap.put("SCHU", "835");
        alphabetMap.put("SCHUL", "836");
        alphabetMap.put("SCHUM", "837");
        alphabetMap.put("SCHUR", "838");
        alphabetMap.put("SCHUS", "839");
        alphabetMap.put("SCHW", "840");
        alphabetMap.put("SCHWAB", "841");
        alphabetMap.put("SCHWAC", "842");
        alphabetMap.put("SCHWAR", "843");
        alphabetMap.put("SCHWARZ", "844");
        alphabetMap.put("SCHWE", "845");
        alphabetMap.put("SCHWEI", "846");
        alphabetMap.put("SCHWEIZ", "847");
        alphabetMap.put("SCHWEK", "848");
        alphabetMap.put("SCHWI", "849");
        alphabetMap.put("SCHZZZZ", "");
    }

    private void initStMap() {
        alphabetMap.put("ST", "850");
        alphabetMap.put("STAH", "851");
        alphabetMap.put("STAL", "852");
        alphabetMap.put("STAM", "853");
        alphabetMap.put("STAU", "854");
        alphabetMap.put("STE", "855");
        alphabetMap.put("STEF", "856");
        alphabetMap.put("STEI", "857");
        alphabetMap.put("STEIN", "858");
        alphabetMap.put("STEK", "859");
        alphabetMap.put("STI", "860");
        alphabetMap.put("STO", "861");
        alphabetMap.put("STOD", "862");
        alphabetMap.put("STOL", "863");
        alphabetMap.put("STOO", "864");
        alphabetMap.put("STR", "865");
        alphabetMap.put("STRAU", "866");
        alphabetMap.put("STRE", "867");
        alphabetMap.put("STRI", "868");
        alphabetMap.put("STRU", "869");
        alphabetMap.put("STU", "870");
        alphabetMap.put("STUC", "871");
        alphabetMap.put("STUD", "872");
        alphabetMap.put("STUF", "873");
        alphabetMap.put("STUS", "874");
        alphabetMap.put("STZZZZ", "");
    }

    private void initTableRelation(String name) {
        if (name.startsWith("SCH")) {
            initSchMap();
        } else if (name.startsWith("ST")) {
            initStMap();
        } else {
            initMap();
        }
    }

    /**
     * Method nameToCode. transform Abi en 101
     * 
     * @param str
     * @return String
     */
    private String nameToCode(String str) {
        String key = "";

        if (getExceptionNum().length() > 0) {
            return getExceptionNum();
        } else {
            Object it[] = alphabetMap.keySet().toArray();
            for (int i = 0; i < it.length; i++) {
                key = (String) it[i];
                if (key.compareToIgnoreCase(str) > 0) {
                    return (String) alphabetMap.get(it[i - 1]);
                }
            }
            return "";
        }
    }

    /**
     * @param string
     */
    public void setExceptionNum(String string) {
        exceptionNum = string;
    }

    /**
     * Method setFormat. le format par d�faut est JJ.MM.AAAA si vous voulez changer le format depuis
     * globaz.hermes.utils.DateUtils
     * 
     * @param format
     */
    public void setFormat(String format) {
        dateNaissance = DateUtils.convertDate(dateNaissance, this.format, format);
        this.format = format;
    }

    /**
     * @param nom
     * @return
     */
    private String traiteEspace(String nom) {
        String partieAvantEspace = "";
        // tester si le nom contient un espace en 3e position au moins
        if (nom.indexOf(" ") > 1) {
            // v�rifier si le nom commence par une particule de la liste
            partieAvantEspace = nom.substring(0, nom.indexOf(" "));
            for (int i = 0; i < particules.length; i++) {
                if (partieAvantEspace.equals(particules[i])) {
                    return StringUtils.removeChar(nom, ' ');
                }
            }

            // si le nom ne commence pas par une particule de la liste
            return nom.substring(0, nom.indexOf(" "));

        } else {
            return StringUtils.removeChar(nom, ' ');
        }
    }

}
