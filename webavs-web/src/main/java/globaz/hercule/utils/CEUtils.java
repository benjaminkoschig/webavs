package globaz.hercule.utils;

import globaz.framework.controller.FWController;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BSession;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JADate;
import globaz.globall.util.JAException;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.CEAttributionPts;
import globaz.hercule.db.controleEmployeur.CEControleEmployeur;
import globaz.hercule.db.controleEmployeur.CEControleEmployeurManager;
import globaz.hercule.service.CEAffiliationService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.log.JadeLogger;
import globaz.naos.translation.CodeSystem;
import globaz.pavo.application.CIApplication;
import globaz.webavs.common.CommonExcelmlContainer;
import globaz.webavs.common.op.CommonExcelDataContainer.CommonLine;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import javax.servlet.http.HttpSession;

/**
 * @author SCO
 * @since SCO 1 juin 2010
 */
public class CEUtils {
    private static final String AND = " AND ";
    public static final String MONTANT_MAX_POUR_CALCUL_5_POUR_CENT = "150000";
    public static final String MONTANT_MIN_POUR_CALCUL_5_POUR_CENT = "100000";

    /**
     * Permet d'ajouter une durée (en année) a une date passée en string
     * 
     * @param _date
     * @param annee
     * @return
     * @throws JAException
     */
    public static String addAnneeToDate(String _date, int annee) throws JAException {
        int resultat = CEUtils.stringDateToAnnee(_date) + annee;
        return "" + resultat;
    }

    /**
     * Permet de créer une partie informations pour globaz afin de mieux debugger le process en cas de probleme
     * 
     * @param message
     */
    public static void addMailInformationsError(FWMemoryLog log, String _message, String source) {

        String message = "\n***** INFORMATIONS POUR GLOBAZ *****\n";
        message += _message;

        // Parcourir des informations
        StringTokenizer str = new StringTokenizer(message, "\n\r");
        while (str.hasMoreElements()) {
            log.logMessage(str.nextToken(), FWMessage.INFORMATION, source);
        }
    }

    /**
     * Permet de remplir un CommonExcelmlContainer à partir d'une CELine et de rajouter une erreur dans le container
     * 
     * @param container
     * @param line
     * @param tabNoms
     * @param erreur
     * @param erreurName
     */
    public static void fillContainerWithCommonLine(CommonExcelmlContainer container, CommonLine line, String[] tabNoms,
            String erreur, String erreurName) {
        HashMap<String, String> map = line.returnLineHashMap();
        Set<String> keySet = map.keySet();
        Iterator<String> colNameIt = keySet.iterator();
        while (colNameIt.hasNext()) {
            String name = colNameIt.next();
            String value = map.get(name);
            if (!JadeStringUtil.isEmpty(value)) {
                container.put(name, value);
            } else {
                container.put(name, "");
            }
        }
        for (int i = 0; i < tabNoms.length; i++) {
            if (!keySet.contains(tabNoms[i])) {
                container.put(tabNoms[i], "");
            }
        }
        container.put(erreurName, erreur.replace('\n', '.'));
    }

    /**
     * Permet de formater le nom d'un tiers depuis sa designation1 et designation2
     * 
     * @param designation1
     * @param designation2
     * @return
     */
    public static String formatNomTiers(String designation1, String designation2) {

        String d1 = designation1;
        String d2 = designation2;

        if (d1 == null) {
            d1 = "";
        } else {
            d1 = d1.trim();
        }

        if (d2 == null) {
            d2 = "";
        } else {
            d2 = d2.trim();
        }

        String tmp = d1;
        if (!JadeStringUtil.isBlank(d2)) {

            if (!JadeStringUtil.isBlank(d1)) {
                tmp += " ";
            }
            tmp += d2;
        }

        return tmp;
    }

    /**
     * Méthode qui formate le numéro d'affilié en fonction de properties
     * 
     * @param session
     * @param numeroAffilie
     * @return
     * @throws Exception
     */
    public static String formatNumeroAffilie(BSession session, String numeroAffilie) {

        try {
            CEApplication app = (CEApplication) GlobazServer.getCurrentSystem().getApplication(
                    CEApplication.DEFAULT_APPLICATION_HERCULE);
            IFormatData affilieFormater = app.getAffileFormater();
            return affilieFormater.format(numeroAffilie);
        } catch (Exception e) {
            JadeLogger.error(CEUtils.class, e);
        }

        return numeroAffilie;

    }

    /**
     * permet de soustraire un certain nombre d'années à une année passée en String.
     * 
     * @param nombreAnneesEnArriere
     * @param annee
     * @return
     */
    public static String getAnneePrecedente(int nombreAnneesEnArriere, String annee) {
        return String.valueOf((Integer.valueOf(annee)).intValue() - nombreAnneesEnArriere);
    }

    /**
     * Permet de retirer une année a l'année passé en parametre<br>
     * 
     * @param _annee
     *            L'année de référence
     * @return l'année - 1 ou null si non renseigné.
     */
    public static String getAnneePrecedente(String _annee) {

        if (JadeStringUtil.isEmpty(_annee)) {
            return null;
        }

        int annee = Integer.parseInt(_annee);
        annee -= 1;

        return Integer.toString(annee);
    }

    /**
     * Retourne la valeur d'un code system d'après son libelle
     * 
     * @param genre
     * @param session
     * @return
     */
    public static String getCsGenreControle(String genre, BSession session) {
        try {
            FWParametersSystemCodeManager manager = new FWParametersSystemCodeManager();
            manager.setSession(session);
            manager.setForLibelleUtilisateurLike(genre);
            manager.setForIdLangue("F");
            manager.find();
            if (!(manager.size() > 0)) {
                manager.setForIdLangue("D");
                manager.find();
            }
            FWParametersSystemCode code = (FWParametersSystemCode) manager.getFirstEntity();
            return code.getIdCode();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Method getFormatAffilie.
     * 
     * @param session
     * @return IFormatData
     */
    public static IFormatData getFormatAffilie(BSession bSession) {

        IFormatData numeroAffFormater = null;
        try {
            CEApplication application = (CEApplication) bSession.getApplication();

            numeroAffFormater = application.getAffileFormater();
        } catch (Exception e) {
            JadeLogger.error(CEUtils.class, e);
        }

        return numeroAffFormater;
    }

    /**
     * Permet la récupération de l'identifiant rubrique
     * 
     * @param session
     * @return
     */
    public static String getIdRubrique(BSession session) {
        String idRubrique = null;

        try {
            BSession sessionCI = (BSession) CEUtils.getSessionCI(session);
            CIApplication application = (CIApplication) sessionCI.getApplication();
            idRubrique = application.getIdRubrique();
        } catch (Exception e) {
            session.addError("unable to load propertie idRubrique");
            idRubrique = null;
        }

        return idRubrique;
    }

    /**
     * Retourne le libellé d'un code système.
     * 
     * @param session
     * @param code
     * @return
     * @throws Exception
     */
    public static String getLibelle(BSession session, String code) throws Exception {
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession(session);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getLibelle();
    }

    /**
     * @param session
     * @param code
     * @param idLangue
     * @return
     * @throws Exception
     */
    public static String getLibelle(BSession session, String code, String idLangue) throws Exception {
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession(session);
        cs.getCode(code);
        return cs.getCodeUtilisateur(idLangue).getLibelle();
    }

    /**
     * Retourne le libellé d'un code système.
     * 
     * @param session
     * @param code
     * @return
     * @throws Exception
     */
    public static String getLibelle(HttpSession session, String code) throws Exception {

        BISession bSession = CEUtils.getSession(session);
        return CEUtils.getLibelle((BSession) bSession, code);
    }

    /**
     * Récupération du libelle ISO suivant la langue et le code
     * 
     * @param session
     * @param code
     * @param isoLangue
     * @return
     * @throws Exception
     */
    public static String getLibelleIso(BSession session, String code, String isoLangue) throws Exception {
        if ("IT".equals(isoLangue) || "it".equals(isoLangue)) {
            return CEUtils.getLibelle(session, code, "I");
        } else if ("DE".equals(isoLangue) || "de".equals(isoLangue)) {
            return CEUtils.getLibelle(session, code, "D");
        } else {
            return CEUtils.getLibelle(session, code, "F");
        }
    }

    /**
     * Return le 5% des employeurs à contrôler
     * 5% ayant une masse pour l'année-1 entre 100'000 inclus et 150'000.-
     * 
     * @param transaction
     * @param anneeCptr
     * @param session
     * @return le 5% des employeurs ayant une masse pour l'année-1 entre 100000 et 150000.-
     */
    public static int getNombre5PourCent(BTransaction transaction, String annee, BSession session) {
        int nombreEmployeur = 0;
        String idRole = CEAffiliationService.getRoleForAffilieParitaire(session);
        String idRubriques = CEUtils.getIdRubrique(session);

        String schema = Jade.getInstance().getDefaultJdbcSchema();

        // Sous select permettant de récupérer la masse salariale
        StringBuilder sousSelectMasse = new StringBuilder("SELECT SUM(CUMULMASSE) ");
        sousSelectMasse.append("FROM ");
        sousSelectMasse.append(schema + ".CACPTRP AS CPTR1 ");
        sousSelectMasse.append("WHERE ");
        sousSelectMasse.append("ANNEE = " + CEUtils.getAnneePrecedente(annee));
        sousSelectMasse.append(" AND CPTR1.IDRUBRIQUE IN(" + idRubriques + ")");
        sousSelectMasse.append(" AND CA.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE");

        StringBuilder sql = new StringBuilder("select count(*) from " + schema + ".afaffip affiliation ");
        sql.append("LEFT OUTER JOIN " + schema + ".CACPTAP AS CA ON ");
        sql.append("(affiliation.MALNAF = CA.IDEXTERNEROLE AND CA.IDTIERS = affiliation.HTITIE  AND CA.IDROLE = "
                + idRole + ") ");
        sql.append("WHERE ");
        sql.append("MATTAF in (" + CodeSystem.TYPE_AFFILI_EMPLOY + ", " + CodeSystem.TYPE_AFFILI_INDEP_EMPLOY + ", "
                + CodeSystem.TYPE_AFFILI_EMPLOY_D_F + ") ");
        sql.append(" AND (").append(sousSelectMasse).append(") >= " + CEUtils.MONTANT_MIN_POUR_CALCUL_5_POUR_CENT);
        sql.append(" AND (").append(sousSelectMasse).append(") < " + CEUtils.MONTANT_MAX_POUR_CALCUL_5_POUR_CENT);

        BStatement statement = new BStatement(transaction);
        try {
            statement.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());
            while (result.next()) {
                nombreEmployeur = result.getInt(1);
            }
            statement.closeStatement();
            statement = null;
        } catch (Exception e) {
            return 0;
        }

        double pourcentage = (nombreEmployeur * 5.0) / 100.0;
        double pourcentageArrondi = Math.rint(pourcentage);
        return (int) pourcentageArrondi;
    }

    public static int getNombreControleNCCCat0Et1(BTransaction transaction, String annee, BSession session) {
        String idRole = CEAffiliationService.getRoleForAffilieParitaire(session);
        String idRubriques = CEUtils.getIdRubrique(session);

        String schema = Jade.getInstance().getDefaultJdbcSchema();

        // Sous select permettant de récupérer la masse salariale
        StringBuilder sousSelectMasse = new StringBuilder("SELECT SUM(CUMULMASSE) ");
        sousSelectMasse.append(" FROM ");
        sousSelectMasse.append(schema + ".CACPTRP AS CPTR1 ");
        sousSelectMasse.append(" WHERE ");
        sousSelectMasse.append(" ANNEE = " + CEUtils.getAnneePrecedente(annee));
        sousSelectMasse.append(" AND CPTR1.IDRUBRIQUE IN(" + idRubriques + ")");
        sousSelectMasse.append(" AND CA.IDCOMPTEANNEXE = CPTR1.IDCOMPTEANNEXE");

        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM " + schema + ".cecontp controle");
        sql.append(" LEFT OUTER JOIN " + schema + ".CACPTAP AS CA ");

        sql.append(" ON (controle.MALNAF = CA.IDEXTERNEROLE AND CA.IDTIERS = controle.HTITIE  AND CA.IDROLE = "
                + idRole + ") ");
        sql.append(" WHERE ");
        sql.append("controle.MDTGEN = " + globaz.hercule.utils.CodeSystem.TYPE_CONTROLE_NON_CERTIFIE_CONFORME);
        sql.append(" AND mdbfdr = '1' ");
        sql.append(" AND SUBSTR(CAST(MDDEFF AS char(8)),1,4) = '" + CEUtils.subAnnee(annee, 1) + "' ");
        sql.append(" AND (").append(sousSelectMasse).append(") >= 0 ");
        sql.append(" AND (").append(sousSelectMasse).append(") < " + CEUtils.MONTANT_MAX_POUR_CALCUL_5_POUR_CENT);

        BStatement statement = new BStatement(transaction);
        int nombreControle = 0;
        try {
            statement.createStatement();
            ResultSet result = statement.executeQuery(sql.toString());
            while (result.next()) {
                nombreControle = result.getInt(1);
            }
            statement.closeStatement();
        } catch (Exception e) {
            return 0;
        }

        return nombreControle;
    }

    /**
     * Extrait la Session de HttpSession.
     * 
     * @param httpSession
     * @return la BISession
     * @throws Exception
     */
    public static BISession getSession(HttpSession httpSession) throws Exception {

        BISession session = null;
        FWController controller = (FWController) httpSession.getAttribute("objController");
        if (controller != null) {
            session = controller.getSession();
        } else {
            throw new Exception("Aucune session connectée");
        }
        return session;
    }

    /**
     * Permet d'obtenir une session pavo depuis n'importe quelle Bsession
     * 
     * @param local
     * @return une BIsesssion pavo
     * @throws Exception
     */
    public static BISession getSessionCI(BSession local) throws Exception {
        BISession remoteSession = (BISession) local.getAttribute("sessionPavo");
        if (remoteSession == null) {
            // pas encore de session pour l'application demandé
            remoteSession = GlobazSystem.getApplication("PAVO").newSession(local);
            local.setAttribute("sessionPavo", remoteSession);
        }
        if (!remoteSession.isConnected()) {
            local.connectSession(remoteSession);
        }
        // vide le buffer d'erreur
        remoteSession.getErrors();
        return remoteSession;
    }

    /**
     * Retourne l'année courante
     * 
     * @return
     */
    public static String giveAnneeCourante() {

        try {
            return String.valueOf(JACalendar.getYear(JACalendar.todayJJsMMsAAAA()));
        } catch (JAException e) {
            return "";
        }
    }

    /**
     * Retourne le jour courant
     * 
     * @return
     */
    public static String giveToday() {
        return JACalendar.todayJJsMMsAAAA();
    }

    /**
     * Recherche d'une attribution de points pour un numéro affilié et une période donnée
     * 
     * @param numAffilie
     * @param periodeDebut
     * @param periodeFin
     * @param session
     * @return
     * @throws Exception
     */
    public static CEAttributionPts rechercheAttributionPts(String numAffilie, String periodeDebut, String periodeFin,
            BSession session) throws Exception {

        CEAttributionPts attribution = new CEAttributionPts();
        attribution.setSession(session);
        attribution.setNumAffilie(numAffilie);
        attribution.setPeriodeDebut(periodeDebut);
        attribution.setPeriodeFin(periodeFin);
        attribution.setAlternateKey(CEAttributionPts.AK_NUMAFFILIE);
        attribution.retrieve();

        if (attribution.isNew()) {
            return null;
        } else {
            return attribution;
        }
    }

    /**
     * Recherche si il existe un controle non effectué prévu pour une autre année
     * 
     * @param idAffiliation
     * @param session
     * @param annee
     * @return
     * @throws Exception
     */
    public static boolean rechercheControleNonEffectuePrevuForAnnee(BSession session, BTransaction transaction,
            String idAffiliation, String annee) throws Exception {

        CEControleEmployeurManager manager = new CEControleEmployeurManager();
        manager.setSession(session);
        manager.setForAffiliationId(idAffiliation);
        manager.setForNotAnneePrevue(annee);
        manager.setForNotDateEffective(true);
        manager.setOrderBy(" MDDCFI DESC ");
        manager.setForActif(true);
        manager.find(transaction);

        if (manager.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Permet la recherche du dernier controle par l'identifiant d'un affilié
     * 
     * @param idAffiliation
     * @param session
     * @return
     * @throws Exception
     */
    public static CEControleEmployeur rechercheDernierControle(String idAffiliation, BSession session) throws Exception {

        CEControleEmployeurManager manager = new CEControleEmployeurManager();
        manager.setSession(session);
        manager.setForAffiliationId(idAffiliation);
        manager.setForDateEffective(true);
        manager.setOrderBy(" MDDCFI DESC ");
        manager.setForActif(true);
        manager.find();

        if (manager.size() > 0) {
            return (CEControleEmployeur) manager.getFirstEntity();
        } else {
            return null;
        }
    }

    /**
     * Recherche le dernier controle qui n'a pas de date effectivité
     * 
     * @param idAffiliation
     * @param session
     * @param annee
     * @return
     * @throws Exception
     */
    public static CEControleEmployeur rechercheDernierControleNonEffectuePrevu(BSession session,
            BTransaction transaction, String idAffiliation, String annee) throws Exception {

        CEControleEmployeurManager manager = new CEControleEmployeurManager();
        manager.setSession(session);
        manager.setForAffiliationId(idAffiliation);
        manager.setForAnnee(annee);
        manager.setForNotDateEffective(true);
        manager.setOrderBy(" MDDCFI DESC ");
        manager.setForActif(true);
        manager.find(transaction);

        if (manager.size() > 0) {
            return (CEControleEmployeur) manager.getFirstEntity();
        } else {
            return null;
        }
    }

    /**
     * Permet de rechercher le controle précédent pour un affilié et par rapport à l'id du controle faisant référence
     * 
     * @param idAffiliation
     * @param annee
     * @param session
     * @return
     * @throws Exception
     */
    public static CEControleEmployeur recherchePrecedentControle(String idAffiliation, String idControl,
            BSession session) throws Exception {

        CEControleEmployeurManager manager = new CEControleEmployeurManager();
        manager.setSession(session);
        manager.setForAffiliationId(idAffiliation);
        manager.setForDateEffective(true);
        manager.setOrderBy(" MDDEFF DESC ");
        manager.setForActif(true);
        manager.find();

        // si on trouve que 1 control c'est qu'il n y a pas de précécdent
        if (manager.size() == 1) {
            return null;
        } else if (manager.size() > 1) {
            boolean returnNext = false;
            for (int i = 0; i < manager.size(); i++) {
                CEControleEmployeur control = (CEControleEmployeur) manager.getEntity(i);
                if (returnNext) {
                    return control;
                } else {
                    if (control.getIdControleEmployeur().equals(idControl)) {
                        returnNext = true;
                    }
                }
            }
            // si on arrive ici c'est qu'on a pas trouvé de control précédent
            return null;
        } else {
            return null;
        }
    }

    /**
     * Permet de commaitre le nombre de controle selon les critères de sélection passés en paramètre
     * 
     * @param idAffiliation
     * @param annee
     * @param dateFin
     * @param dateDebut
     * @param dateEffective
     * @param actif
     * @param session
     * @return
     * @throws Exception
     */
    public static int returnNombreControle(String idAffiliation, String annee, String dateFin, String dateDebut,
            boolean dateEffective, boolean actif, BSession session) throws Exception {
        // Test paramètre indispensable pour ne pas avoir une requête "trop lourde"
        if (JadeStringUtil.isBlankOrZero(annee) && JadeStringUtil.isBlankOrZero(idAffiliation)
                && JadeStringUtil.isBlankOrZero(dateDebut) && JadeStringUtil.isBlankOrZero(dateFin)) {
            throw new Exception("Insufficient number of arguments");
        }

        CEControleEmployeurManager manager = new CEControleEmployeurManager();
        manager.setSession(session);
        manager.setForAffiliationId(idAffiliation);
        manager.setForDateDebutControle(dateDebut);
        manager.setForDateFinControle(dateFin);
        manager.setForAnnee(annee);
        manager.setForDateEffective(dateEffective);
        manager.setForActif(actif);
        manager.find();
        return manager.getSize();
    }

    /**
     * Permet de créer une condition sql précédée de "AND" si ce n'est pas la premiere
     * 
     * @param sqlWhere
     * @param condition
     */
    public static void sqlAddCondition(StringBuffer sqlWhere, String condition) {
        if (sqlWhere.length() != 0) {
            sqlWhere.append(CEUtils.AND);
        }
        sqlWhere.append(condition);
    }

    /**
     * Permet d'ajouter un champ (fields) précédé d'une virgule si le buffer n'est pas vide
     * 
     * @param fields
     * @param columnName
     * @return
     */
    public static void sqlAddField(StringBuffer fields, String columnName) {
        if (!JadeStringUtil.isBlank(fields.toString())) {
            fields.append(",");
        }
        fields.append(columnName);
    }

    /**
     * Convertion d'un "e.printStackTrace" en String
     * 
     * @param e
     * @return
     */
    public static String stack2string(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return "------\r\n" + sw.toString() + "------\r\n";
        } catch (Exception e2) {
            return "bad stack2string";
        }
    }

    /**
     * Permet de récupérer en int l'année d'une date passée en string
     * 
     * @param _date
     * @return
     * @throws JAException
     */
    public static int stringDateToAnnee(String _date) throws JAException {
        JADate date = new JADate(_date);
        return date.getYear();
    }

    /**
     * Permet de soustraire un nombre d'année a un année donnée
     * 
     * @param _annee
     * @param nombreAnneesEnArriere
     * @return
     */
    public static String subAnnee(String _annee, int nombreAnneesEnArriere) {

        String anneeResult = "";

        if (!JadeStringUtil.isBlankOrZero(_annee)) {
            int annee = Integer.parseInt(_annee);
            anneeResult = Integer.toString(annee - nombreAnneesEnArriere);
        }

        return anneeResult;
    }

    /**
     * Retourne la valeur entiere stockée dans la string. Retourne 0 si null ou différent d'un int.
     * 
     * @param _string
     * @return
     */
    public static int transformeStringToInt(String _string) {

        int _int;

        try {
            _int = Double.valueOf(_string).intValue();
        } catch (Exception e) {
            _int = 0;
        }

        return _int;
    }

    /**
     * Permet d'unformater le numero d'affilié (utilisé pour les bar code)
     * 
     * @param session
     * @param numeroAffilie
     * @return
     * @throws Exception
     */
    public static String unFormatNumeroAffilie(BSession session, String numeroAffilie) throws Exception {
        CEApplication app = (CEApplication) GlobazServer.getCurrentSystem().getApplication(
                CEApplication.DEFAULT_APPLICATION_HERCULE);
        IFormatData affilieFormater = app.getAffileFormater();
        return affilieFormater.unformat(numeroAffilie);
    }

    /**
     * Permet de valider une année
     * 
     * @param year
     * @return
     */
    public static boolean validateYear(String year) {

        // Si c'est pas un nombre, c'est pas une date valide
        if (!JadeStringUtil.isDigit(year)) {
            return false;
        }

        try {
            // Si la date n'est pas comprise entre 1900 et 2100, elle n'est pas
            // valide
            int int_year = Integer.parseInt(year);
            if ((int_year > 1900) && (int_year < 2100)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }
}
