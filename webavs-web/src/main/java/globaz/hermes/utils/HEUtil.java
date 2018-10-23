package globaz.hermes.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import globaz.commons.nss.NSUtil;
import globaz.framework.controller.FWController;
import globaz.globall.db.BApplication;
import globaz.globall.db.BManager;
import globaz.globall.db.BPreparedStatement;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.db.GlobazServer;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JAStringFormatter;
import globaz.globall.util.JAUtil;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.application.HEApplication;
import globaz.hermes.db.access.HECheckRights;
import globaz.hermes.db.access.HEInfosManager;
import globaz.hermes.db.gestion.HEInputAnnonceViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.services.AFAffiliationServices;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TITiers;
import globaz.webavs.common.CommonProperties;

//
public class HEUtil {
    /***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * @param code
     *            le code pays à contrôler
     * @return true si le code est correct, false sinon
     */
    public static boolean checkCodePays(String code) {
        if ((code == null) || (code.length() != 3)) {
            return false;
        }
        return JadeStringUtil.isDigit(code);
    }

    /***********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************
     * @param field
     *            état nominatif à contrôler
     * @return true si la valeur est correcte, sinon faux
     */

    public static boolean checkEtatNominatif(String field) {
        if (JadeStringUtil.isEmpty(field)) {
            return false;
        } else {
            // l'état nominatif doit contenir une virgule et ne peut pas
            // contenir d'espaces
            if (JadeStringUtil.contains(field, ",")) {
                // Contrôle que le nom comporte bien deux éléments
                StringTokenizer s = new StringTokenizer(field, ",");
                if (s.countTokens() > 1) {
                    while (s.hasMoreTokens()) {
                        if (JadeStringUtil.contains(s.nextToken(), "  ")) {
                            return false;
                        }
                    }
                    // on n'a pas trouvé d'anomalie, c'est ok !
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    /**
     * Cette méthode a été créée pour le mandat Inforom 573, elle permet de contrôler si une affiliation est de type
     * paritaire sans particularité personnelle
     *
     * @param numeroAffilie Un numéro d'affilié
     * @param session Une session de type BSession
     * @return
     * @throws NullPointerException Si la session est null
     * @throws NullPointerException Si le numéro d'affilié est null ou vide
     * @throws Exception
     */
    public static boolean checkAffSansPersoOrAccountAVSZero(String numeroAffilie, String category, BSession session)
            throws Exception {

        boolean hasParticulariteSansPersoActive = false;
        AFAffiliation affiliation = null;

        if (session == null) {
            throw new NullPointerException("Session must be not null !");
        }
        // si on a un numéro d'affilié on va récupérer l'affiliation
        if (!JadeStringUtil.isBlankOrZero(numeroAffilie)) {
            affiliation = AFAffiliationServices.getAffiliationActiveParitaireByNumero(numeroAffilie, session);
        }

        if (affiliation == null) {
            // si il n'y a pas d'affiliation (l'affiliation est radié ou d'un autre type que paritaire) on n'affiche
            // pas de message
            return true;
        }

        // Dans le cas d'un ARC on aura la catégorie, elle doit impérativement être de type employeur
        if (!JadeStringUtil.isBlankOrZero(category)) {
            // si on n'est pas dans la catégorie employeur, on affiche pas le message
            if (!IHEAnnoncesViewBean.CS_CATEGORIE_EMPLOYEUR.equals(category)) {
                return true;
            }
        }

        // Il faut tester la particulartié, s'il en existe une sans date de fin, il faut que le message
        // s'affiche
        hasParticulariteSansPersoActive = AFParticulariteAffiliation.existeParticulariteActive(session,
                affiliation.getAffiliationId(), CodeSystem.PARTIC_AFFILIE_SANS_PERSONNEL);
        if (hasParticulariteSansPersoActive) {
            return false;
        }

        // la cotisation doit être de genre paritaire et AVS active
        AFCotisation cotisation = affiliation._cotisationActive(session.getCurrentThreadTransaction(),
                affiliation.getAffiliationId(), CodeSystem.GENRE_ASS_PARITAIRE, CodeSystem.TYPE_ASS_COTISATION_AVS_AI);

        if (cotisation == null) {
            return false;
        }

        // Si la cotisation est différente de null cela signifie que le plan d'affiliation est actif voir le from de
        // AFCotisationManager dans _cotisationActive

        if (cotisation.getMaisonMere() || affiliation.getReleveParitaire()
                || CodeSystem.CODE_FACTU_MONTANT_LIBRE.equals(affiliation.getCodeFacturation())) {
            return true;
        }

        double masseSalariale;
        try {
            masseSalariale = Double.parseDouble(cotisation.getMasseAnnuelle());
        } catch (NumberFormatException e) {
            masseSalariale = 0;
        }

        if (masseSalariale == 0) {
            return false;
        }

        return true;
    }

    /**
     * Cette méthode permet de retourner le numéro AVS formatté passé en paramètre s'il est existant, sinon la méthode
     * retournera la désignation de l'assuré (nom + prénom)
     *
     * @param numeroAVS
     * @param designation
     * @return
     */
    public static String getNumAVSOrDesignation(String numeroAVS, String designation, BSession session) {

        if (JadeStringUtil.isBlankOrZero(numeroAVS)) {

            return session.getLabel("HERMES_ASSURE_DESIGNATION") + " : " + designation;
        }
        return session.getLabel("LISTE_NSS") + " : " + NSUtil.formatAVSUnknown(numeroAVS);

    }

    public static boolean checkName(String name) {
        int i = -1;
        if (name.indexOf(",") != name.lastIndexOf(",")) {
            // il y a plusieur virgule dans le nom ---> pas permis
            return false;
        }
        if ((i = name.lastIndexOf(",")) == -1) {
            return false; // pas de virgule
        } else {
            if ((i + 1) < name.length()) {
                if ((name.charAt(i - 1) == ' ') || (name.charAt(i + 1) == ' ')) {
                    return false; // espace avant ou après la virgule
                } else {
                    return true; // ok
                }
            } else {
                // il n'y a aucun caractère après la virgule, donc faux
                return false;
            }
        }
    }

    public static String checkReferenceUnique(String s) {
        String[] charOk = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
                "S", "T", "U", "V", "W", "X", "Y", "Z", ".", ",", "-", "/", "0", "1", "2", "3", "4", "5", "6", "7", "8",
                "9" };
        String source = s.toUpperCase();
        String res = "";
        for (int i = 0; i < source.length(); i++) {
            if (Arrays.asList(charOk).contains((source.substring(i, i + 1)))) {
                res += s.substring(i, i + 1);
            } else {
                res += " ";
            }
        }
        return res;
    }

    /**
     * Méthode qui contrôle si un mot qu'on inséré contient des - avec des espaces, si c'est le cas cette méthode va
     * retourner false
     *
     * @param word
     * @return
     */
    public static boolean checkWordWithHyphen(String word) {

        String regex = "\\s*-\\s*";
        String wordToCompare = "";

        if (word != null) {
            wordToCompare = word.replaceAll(regex, "-");
            if (!wordToCompare.equals(word)) {
                return false;
            }
        }
        return true;
    }

    public static void commitTreatReference(ArrayList references, BSession session, BTransaction transaction)
            throws Exception {
        // prepareStatement avec une liste de références mémorisées
        BPreparedStatement terminatingSeries = new BPreparedStatement(transaction);
        try {
            Iterator it = references.iterator();
            int index = 0;
            StringBuffer s = new StringBuffer();
            while (it.hasNext()) {
                s.append("'");
                s.append((String) it.next());
                s.append("'");
                index++;
                // Paginage de la mise à jour
                if ((index < 200) && it.hasNext()) {
                    if (it.hasNext()) {
                        s.append(",");
                    }
                }
                if ((index >= 200) || !it.hasNext()) {
                    terminatingSeries.prepareStatement(HEOutputAnnonceListViewBean.getSqlForSetDateCopy(s.toString()));
                    terminatingSeries.execute();
                    s = new StringBuffer();
                    index = 0;
                }
            }

        } catch (Exception e) {
            throw e;
        } finally {
            terminatingSeries.closeStatement();
        }
    }

    /**
     * Cette méthode a été créée suite au BZ 8457, elle permet de formatter un mot qui contient des espaces avant les
     * traits d'union Exemple : input: Jean -Michel => output: Jean-Michel
     *
     * @param word
     * @return formatedword
     */
    public static String formatWordWithHyphen(String word) {

        String regex = "\\s*-\\s*";

        if (word != null) {
            word = word.replaceAll(regex, "-");
        }
        return word;
    }

    public static int getAutoDigit(BSession session) {
        try {
            BApplication crt = session.getApplication();
            String temp = crt.getProperty(CommonProperties.KEY_AUTO_DIGIT_AFF).trim();
            return Integer.valueOf(temp).intValue();
        } catch (Exception e) {
            return 7;
        }

    }

    /**
     * @param like
     * @param isArchivage
     * @param session
     * @return
     */
    public static String getAvailableARC(String like, String isArchivage, HttpSession session) {
        StringBuffer options = new StringBuffer();
        BSession bSession = HEUtil.getHttpSessionToBSession(session);
        try {
            HEOutputAnnonceListViewBean listViewBean = new HEOutputAnnonceListViewBean();
            listViewBean.setSession(bSession);
            listViewBean.setForCodeApplication("11");
            listViewBean.setCodeApplication3839(false);
            listViewBean.setForCodeEnregistrement("01");
            listViewBean.setForRCIOnly();
            listViewBean.setForNotStatut(IHEAnnoncesViewBean.CS_EN_ATTENTE);
            listViewBean.setForNotStatut2(IHEAnnoncesViewBean.CS_PROBLEME);
            listViewBean.setForNotStatut3(IHEAnnoncesViewBean.CS_ORPHELIN);
            listViewBean.setLikeNumeroAVS(JadeStringUtil.removeChar(like, '.'));
            listViewBean.setIsArchivage(Boolean.valueOf(isArchivage).booleanValue());
            // listViewBean.setFromNumeroAVS(like);
            listViewBean.setOrder("RNIANN");
            listViewBean.find();
            if (listViewBean.size() == 0) {
                // si ne trouve aucune annonce avec le num avs spécifié
                // on met la référence unique à -1
                /**/
                options.append(" refUnique='");
                options.append("-1");
                options.append("'");
            }
            HEOutputAnnonceViewBean arc = null;
            String numAVSAyantDroit = "";
            String dateCloture = "";
            for (int i = 0; i < listViewBean.size(); i++) {
                arc = (HEOutputAnnonceViewBean) listViewBean.getEntity(i);
                String res[] = arc.getNumeroConjointAyantDroit(arc.getRefUnique());
                // modif NNSS
                // numAVSAyantDroit = AVSUtils.formatAVS8Or9(res[0]);
                numAVSAyantDroit = res[0];
                dateCloture = JadeStringUtil.isEmpty(res[1]) ? ""
                        : DateUtils.convertDate(res[1], DateUtils.MMAA, DateUtils.MMAA_DOTS);
                //
                // remplir la liste
                options.append("<option value='");
                // modif NNSS
                // options.append(JAStringFormatter.formatAVS(arc.getNumeroAVS()));
                options.append(globaz.commons.nss.NSUtil.formatWithoutPrefixe(arc.getNumeroAVS(),
                        arc.getNumeroAvsNNSS().equals("true")));
                options.append("'");
                /**/
                options.append(" refUnique='");
                options.append(arc.getRefUnique());
                options.append("'");
                /**/
                options.append(" motif='");
                options.append(arc.getMotif());
                options.append("'");
                /**/
                options.append(" dateCloture='");
                options.append(dateCloture);
                options.append("'");
                /**/
                options.append(" refInterneCaisse='");
                options.append(arc.getField(IHEAnnoncesViewBean.REFERENCE_INTERNE_CAISSE));
                options.append("'");
                /**/
                options.append(" utilisateur='");
                options.append(arc.getUtilisateur());
                options.append("'");
                /**/
                options.append(" numAVSAyantDroit='");
                options.append(numAVSAyantDroit);
                options.append("'");
                /**/
                /**/
                options.append(" dateDebut='");
                options.append(arc.getDateAnnonce());
                options.append("'");
                /**/
                /**/
                options.append(" dateFin='");
                if (IHEAnnoncesViewBean.CS_TERMINE.equals(arc.getStatut())) {
                    options.append(arc.getSpy().getDate());
                } else {
                    options.append("");
                }
                options.append("'");
                /**/
                options.append(">");
                options.append(JAStringFormatter.formatAVS(arc.getNumeroAVS()));
                options.append(" - ");
                options.append(arc.getMotif());
                options.append(" - ");
                options.append(arc.getDateAnnonce());
                options.append("</option>");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            options.append("<option value=\"\" libelle=\"\" caption=\"\" fullLibelle=\"\">Erreur : " + ex.getMessage()
                    + "</option>");
        }
        return options.toString();
    }

    /**
     * Retourne une liste de CI (numéro et nom de l'assuré) en fonction des permiers chiffres du numéro avs donnés en
     * paramètres. Date de création : (25.02.2003 09:05:13)
     *
     * @return la liste d'options (tag select) des CI existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les primers chiffres du numéro avs de l'assuré
     * @param session
     *            la session HTTP actuelle
     */
    public static String getAvailableCI(String like, HttpSession session) {
        StringBuffer options = new StringBuffer();
        BSession bSession = HEUtil.getHttpSessionToBSession(session);
        try {
            globaz.pavo.db.compte.CICompteIndividuelManager mgr = new globaz.pavo.db.compte.CICompteIndividuelManager();
            mgr.setSession(bSession);
            mgr.setLikeNumeroAvs(like);
            mgr.setOrderBy("KANAVS");
            mgr.find();
            for (int i = 0; i < mgr.size(); i++) {
                globaz.pavo.db.compte.CICompteIndividuel ci = (globaz.pavo.db.compte.CICompteIndividuel) mgr
                        .getEntity(i);
                options.append("<option value='");
                options.append(JAUtil.formatAvs(ci.getNumeroAvs()));
                options.append("'>");
                options.append(JAUtil.formatAvs(ci.getNumeroAvs()));
                options.append(" - ");
                options.append(ci.getNomPrenom());
                options.append("</option>");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return options.toString();
    }

    /**
     * @param like
     * @param bSession
     * @return
     */
    public static String getAvailableMotif(String like, BSession bSession) {
        StringBuffer options = new StringBuffer();
        // String[] sTab = new String[2];
        try {
            HEInputAnnonceViewBean viewBean = new HEInputAnnonceViewBean();
            viewBean.setSession(bSession);
            Vector vec = viewBean.getMotifCSListe();
            /** filtre pour les droits de saisie sur les motifs* */
            HECheckRights checkRights = new HECheckRights();
            checkRights.setSession(bSession);

            /***/
            for (Enumeration e = vec.elements(); e.hasMoreElements();) {

                String[] sTab = (String[]) e.nextElement();
                checkRights.setMotif(bSession.getCode(sTab[0]));
                boolean isAllowed = true;
                try {
                    checkRights.checkRole();
                } catch (Exception e1) {
                    isAllowed = false;
                }
                if (!sTab[0].trim().equals("") && sTab[1].startsWith(like) && isAllowed) {
                    options.append("\n<option value=\"" + sTab[1].substring(0, 2) + "\" libelle=\""
                            + sTab[1].substring(5) + "\" caption=\"" + sTab[0] + "\" fullLibelle=\"" + sTab[1] + "\">"
                            + sTab[1] + "</option>");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            options.append("<option value=\"\" libelle=\"\" caption=\"\" fullLibelle=\"\">Erreur : " + ex.getMessage()
                    + "</option>");
        }
        // System.out.println("after build select: "+(System.currentTimeMillis()-time)+"ms");
        return options.toString();
    }

    /**
     * @param like
     * @param httpSession
     * @return
     */
    public static String getAvailableMotif(String like, HttpSession httpSession) {
        return HEUtil.getAvailableMotif(like, HEUtil.getHttpSessionToBSession(httpSession));
    }

    /**
     * Retourne une liste de CI (numéro et nom de l'assuré) en fonction des permiers chiffres du numéro avs donnés en
     * paramètres. Date de création : (25.02.2003 09:05:13)
     *
     * @return la liste d'options (tag select) des CI existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les primers chiffres du numéro avs de l'assuré
     * @param bsession
     *            la session actuelle (provenant du viewBean de la page jsp)
     */
    public static Vector getCharToAscii(String like) {
        Vector v = new Vector();
        String[] list; // = new String[2];
        for (int i = like.charAt(0); i < 256; i++) {
            list = new String[2];
            list[0] = String.valueOf(i);
            list[1] = "Caracter : " + ((char) i) + ", ascii=" + i;
            v.add(list);
        }
        return v;
    }

    public static FWParametersSystemCodeManager getCSMotif(String motif, BSession session) throws Exception {
        FWParametersSystemCodeManager codeListe = new FWParametersSystemCodeManager();
        codeListe.setForIdGroupe("HEMOTIFS");
        codeListe.setForIdTypeCode("11100002");
        codeListe.setForCodeUtilisateur(motif);
        codeListe.setSession(session);
        codeListe.find(BManager.SIZE_NOLIMIT);
        return codeListe;
    }

    /**
     * Retourne une liste de CI (numéro et nom de l'assuré) en fonction des permiers chiffres du numéro avs donnés en
     * paramètres. Date de création : (25.02.2003 09:05:13)
     *
     * @return la liste d'options (tag select) des CI existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les primers chiffres du numéro avs de l'assuré
     * @param session
     *            la session HTTP actuelle
     */
    private static BSession getHttpSessionToBSession(HttpSession session) {
        return (BSession) ((FWController) session.getAttribute("objController")).getSession();
    }

    public static String getModeleCA() {
        try {
            HEApplication application = (HEApplication) GlobazServer.getCurrentSystem()
                    .getApplication(HEApplication.DEFAULT_APPLICATION_HERMES);
            return application.getModeleCA();
        } catch (Exception e) {
            return "HERMES_LETTRE_REMISE_CA_ACC";
        }
    }

    public static String getModeleCAEmp() {
        try {
            HEApplication application = (HEApplication) GlobazServer.getCurrentSystem()
                    .getApplication(HEApplication.DEFAULT_APPLICATION_HERMES);
            return application.getModeleCAEmp();
        } catch (Exception e) {
            return "HERMES_LETTRE_REMISE_CA_ACC_EMP";
        }
    }

    public static int getTailleChampsAffilie(BSession session) {
        try {
            BApplication crt = session.getApplication();
            String temp = crt.getProperty(CommonProperties.KEY_AUTO_DIGIT_AFF).trim();
            return Integer.valueOf(temp).intValue();
        } catch (Exception e) {
            return 11;
        }
    }

    public static String getTimeHHMMSS() {
        GregorianCalendar calendar = new GregorianCalendar();
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        String seconde = String.valueOf(calendar.get(Calendar.SECOND));

        if (hour.length() == 1) {
            hour = "0" + hour;
        }

        if (minute.length() == 1) {
            minute = "0" + minute;
        }

        if (seconde.length() == 1) {
            seconde = "0" + seconde;
        }

        return hour + minute + seconde;
    }

    public static boolean isMotifCert(BSession session, String value) throws Exception {
        if (HEUtil.isNNSSActif(session)) {
            String temp = "";
            try {
                FWFindParameterManager param = new FWFindParameterManager();
                FWFindParameter parametre;
                param.setSession(session);
                param.setIdApplParametre(HEApplication.DEFAULT_APPLICATION_HERMES);
                param.setIdCleDiffere("MOTCERT");
                param.find();
                parametre = (FWFindParameter) param.getFirstEntity();
                temp = parametre.getValeurAlphaParametre();
                return (Arrays.asList(temp.split(",")).contains(value));
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }

    }

    public static boolean isNNSSActif(BSession session) throws Exception {
        String dateNNSS = session.getApplication().getProperty("zas.nnss.dateProduction");
        if (!JadeStringUtil.isEmpty(dateNNSS)) {
            String currentDate = JACalendar.todayJJsMMsAAAA();
            return BSessionUtil.compareDateFirstGreaterOrEqual(session, currentDate, dateNNSS);
        } else {
            return false;
        }
    }

    public static boolean isNNSSdisplay(BSession session) throws Exception {

        String displayNNSS = ((HEApplication) session.getApplication()).getProperty("nsstag.defaultdisplay.newnss");

        if (!JadeStringUtil.isEmpty(displayNNSS)) {
            if (displayNNSS.equals("true")) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    public static boolean isDateNaissanceIncompletAutorisee(BSession session) {
        String isAutorise;
        try {
            isAutorise = ((HEApplication) session.getApplication()).getProperty("autoriseDateNaissanceUniquementAnnee");
            if (!JadeStringUtil.isEmpty(isAutorise)) {
                if (isAutorise.equals("true")) {
                    return true;
                } else {
                    return false;
                }

            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * Permet de commaitre le nombre d'info sotckée selon les critères de sélection passés en paramètre
     *
     * @param session
     * @param typeInfo
     * @param libInfo
     * @param idArc
     * @param idInfoComp
     * @return
     * @throws Exception
     */
    public static int returnNombreInfo(BSession session, String typeInfo, String libInfo, String idArc,
            String idInfoComp) throws Exception {
        // Test paramètre indispensable pour ne pas avoir une requête "trop lourde"
        if (JadeStringUtil.isBlankOrZero(typeInfo) && JadeStringUtil.isBlankOrZero(libInfo)
                && JadeStringUtil.isBlankOrZero(idArc) && JadeStringUtil.isBlankOrZero(idInfoComp)) {
            throw new Exception("Insufficient number of arguments");
        }

        HEInfosManager manager = new HEInfosManager();
        manager.setSession(session);
        manager.setForTypeInfo(typeInfo);
        manager.setForLibInfo(libInfo);
        manager.setForIdArc(idArc);
        manager.setForIdInfoComp(idInfoComp);
        manager.find();
        return manager.getSize();
    }

    /**
     * Commentaire relatif au constructeur HEUtil.
     */
    public HEUtil() {
        super();
    }

    public Vector getCountries(String keyChamp, BSession session) {
        Vector vList = new Vector();
        String[] list = new String[2]; // test que pour HERMES
        if (keyChamp.equals(IHEAnnoncesViewBean.ETAT_ORIGINE) || keyChamp.equals(IHEAnnoncesViewBean.ETAT_ORIGINE_1)) {
            try {
                HEApplication app = (HEApplication) session.getApplication();
                // on récupère les codes systèmes des pays
                FWParametersSystemCodeManager manager = app.getCsPaysListe(session);
                vList = new Vector(manager.size());
                for (int i = 0; i < manager.size(); i++) {
                    list = new String[2];
                    FWParametersSystemCode entity = (FWParametersSystemCode) manager.getEntity(i);
                    String codePays = entity.getCurrentCodeUtilisateur().getCodeUtilisateur();
                    // la valeur du tag OPTION dans le SELECT (ici le code pays)
                    list[0] = codePays;
                    // le libellé
                    list[1] = entity.getCurrentCodeUtilisateur().getLibelle() + " - " + codePays;
                    vList.add(list);
                } // on tri manuellement par ordre alphabétique en utilisant un
                  // tri à bulle
                String[] first = new String[2];
                String[] second = new String[2];
                for (int j = 0; j < vList.size(); j++) {
                    first = (String[]) vList.elementAt(j);
                    for (int k = j + 1; k < vList.size(); k++) {
                        second = (String[]) vList.elementAt(k);
                        if (first[1].compareTo(second[1]) > 0) {
                            vList.setElementAt(second, j);
                            vList.setElementAt(first, k);
                            first = (String[]) vList.elementAt(j);
                        }
                    }
                }
            } catch (Exception e) { // si probleme, retourne list vide.
                e.printStackTrace();
                return new Vector();
            }
            return vList;
        }
        return null;
    } // //////

    public Vector getListeCat(BSession session) {
        Vector vList = new Vector();
        // vide
        /*
         * String[] vide = new String[2]; vide[0]=""; vide[1]=""; vList.add(vide);
         */
        // employeur
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession(session);
        String[] employeur = new String[2];
        cs.getCode(IHEAnnoncesViewBean.CS_CATEGORIE_EMPLOYEUR);
        employeur[0] = IHEAnnoncesViewBean.CS_CATEGORIE_EMPLOYEUR;
        employeur[1] = cs.getCurrentCodeUtilisateur().getLibelle();
        vList.add(employeur);
        // indé
        FWParametersSystemCode cs2 = new FWParametersSystemCode();
        cs2.setSession(session);
        String[] indé = new String[2];
        cs2.getCode(IHEAnnoncesViewBean.CS_CATEGORIE_INDEPENDANT);
        indé[0] = IHEAnnoncesViewBean.CS_CATEGORIE_INDEPENDANT;
        indé[1] = cs2.getCurrentCodeUtilisateur().getLibelle();
        vList.add(indé);
        // rentier
        FWParametersSystemCode cs3 = new FWParametersSystemCode();
        cs3.setSession(session);
        String[] rentier = new String[2];
        cs3.getCode(IHEAnnoncesViewBean.CS_CATEGORIE_RENTIER);
        rentier[0] = IHEAnnoncesViewBean.CS_CATEGORIE_RENTIER;
        rentier[1] = cs3.getCurrentCodeUtilisateur().getLibelle();
        vList.add(rentier);

        return vList;
    }

    /**
     * @param session
     * @return
     */
    public Vector getListeTitre(BSession session) {
        Vector vList = new Vector();
        // vide
        String[] vide = new String[2];
        vide[0] = "";
        vide[1] = "";
        vList.add(vide);
        // Monsieur
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession(session);
        String[] monsieur = new String[2];
        cs.getCode(TITiers.CS_MONSIEUR);
        monsieur[0] = TITiers.CS_MONSIEUR;
        monsieur[1] = cs.getCurrentCodeUtilisateur().getLibelle();
        vList.add(monsieur);
        // Madame
        FWParametersSystemCode cs2 = new FWParametersSystemCode();
        cs2.setSession(session);
        String[] madame = new String[2];
        cs2.getCode(TITiers.CS_MADAME);
        madame[0] = TITiers.CS_MADAME;
        madame[1] = cs2.getCurrentCodeUtilisateur().getLibelle();
        vList.add(madame);
        return vList;
    }

}
