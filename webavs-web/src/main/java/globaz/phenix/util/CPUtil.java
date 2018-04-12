package globaz.phenix.util;

import globaz.commons.nss.NSUtil;
import globaz.commons.nss.db.NSSinfo;
import globaz.commons.nss.db.NSSinfoManager;
import globaz.framework.controller.FWController;
import globaz.framework.secure.user.FWSecureGroupUser;
import globaz.framework.secure.user.FWSecureGroupUsers;
import globaz.framework.secure.user.FWSecureUser;
import globaz.framework.secure.user.FWSecureUsers;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BApplication;
import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.format.IFormatData;
import globaz.globall.parameters.FWParameters;
import globaz.globall.parameters.FWParametersManager;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.application.FAApplication;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.parametreAssurance.AFParametreAssurance;
import globaz.naos.translation.CodeSystem;
import globaz.naos.util.AFUtil;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CIEcriture;
import globaz.phenix.application.CPApplication;
import globaz.phenix.db.communications.CPSedexContribuable;
import globaz.phenix.db.principale.CPDecision;
import globaz.phenix.db.principale.CPDecisionAffiliation;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.CommonExcelmlContainer;
import globaz.webavs.common.op.CommonExcelDataContainer.CommonLine;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import ch.globaz.common.domaine.Date;

/**
 * Classe utilitaire pour PAVO. Date de création : (09.01.2003 15:18:41)
 * 
 * @author: Administrator
 */
public class CPUtil {
    /**
     * Commentaire relatif au constructeur CPUtil.
     */
    // Constantes
    // groupe ut NEM et NEK
    public final static String CS_NEKNEM = "10500090";

    /**
     * Permet de créer une partie informations pour globaz afin de mieux debugger le process en cas de probleme
     * 
     * @param message
     */
    public static void addMailInformationsError(FWMemoryLog log, String _message, String source) {

        String message = _message;

        // Parcourir des informations
        StringTokenizer str = new StringTokenizer(message, "\n\r");
        while (str.hasMoreElements()) {
            log.logMessage(str.nextToken(), FWMessage.INFORMATION, source);
        }
    }

    /**
     * @param patternString
     *            La chaîne de charactères à rechercher
     * @param baseString
     *            La chaîne de base dans laquelle rechercher, élément séparé par un espace
     * @return true si le patternString a été trouvé
     * @author BTC
     */
    public static boolean containsString(String patternString, String baseString) {
        StringTokenizer tk = new StringTokenizer(baseString);
        while (tk.hasMoreTokens()) {
            String token = tk.nextToken();
            if (patternString.equalsIgnoreCase(token)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Permet de remplir un PhenixContainer à partir d'une CPLine et de rajouter une erreur dans le container
     * 
     * @param container
     * @param line
     * @param tabNoms
     * @param erreur
     * @param erreurName
     */
    public static void fillPhenixContainerWithCPLine(CommonExcelmlContainer container, CommonLine line,
            String[] tabNoms, String erreur, String erreurName) {
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
     * Retourne une liste des Affilies (numéro et nom de l'affilié) en fonction des permiers chiffres du numéro affilié
     * donnés en paramètres. Date de création : (17.09.2003 17:032:13)
     * 
     * @return la liste d'options (tag select) des affiliés existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les primers chiffres du numéro affilié
     * @param session
     *            la session HTTP actuelle
     */
    public static String getAffilies(String like, HttpSession session) {
        return CPUtil
                .getAffillies(like, (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * Retourne une liste des Affilies (numéro, nom et id de l'affilié) en fonction des permiers chiffres du numéro
     * affilié donnés en paramètres. Date de création : (17.09.2003 17:032:13)
     * 
     * @return la liste d'options (tag select) des affiliés existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les primers chiffres du numéro affilié
     * @param bsession
     *            la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getAffillies(String like, BSession bsession) {
        CPApplication cpApp = null;
        try {
            cpApp = (CPApplication) bsession.getApplication();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (JadeStringUtil.isEmpty(like)) {
            like = "";
        }
        if ((like != null) && (like.indexOf('.') == -1)) {
            try {
                IFormatData affilieFormater = cpApp.getAffileFormater();
                if (affilieFormater != null) {
                    like = affilieFormater.format(like);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            IFormatData affilieFormater = cpApp.getAffileFormater();
            if (affilieFormater != null) {
                like = affilieFormater.format(like);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuffer options = new StringBuffer();
        HashSet<String> affSet = new HashSet<String>();
        try {
            AFAffiliationManager mng = new AFAffiliationManager();
            mng.setSession(bsession);
            mng.setLikeAffilieNumero(like);
            mng.find(BManager.SIZE_NOLIMIT);
            if (mng.size() == 0) {
                /**/
                options.append("<option value='");
                options.append("");
                /**/
                options.append("' nom='");
                options.append("' idTiers='");
                options.append("");
                options.append("'");
                options.append("' idAffiliation='");
                options.append("");
                options.append("'");
                options.append(" numAffilie='");
                options.append(like);
                options.append("'");
                options.append(">");
                options.append("");
                options.append(" - ");
                options.append("");
                options.append(" - ");
                options.append("");
                options.append("</option>");
                affSet.add("");
                return options.toString();
            }
            for (int i = 0; i < mng.size(); i++) {
                AFAffiliation af = (AFAffiliation) mng.getEntity(i);
                if (CodeSystem.TYPE_AFFILI_INDEP.equalsIgnoreCase(af.getTypeAffiliation())
                        || CodeSystem.TYPE_AFFILI_INDEP_EMPLOY.equalsIgnoreCase(af.getTypeAffiliation())
                        || CodeSystem.TYPE_AFFILI_NON_ACTIF.equalsIgnoreCase(af.getTypeAffiliation())
                        || CodeSystem.TYPE_AFFILI_TSE.equalsIgnoreCase(af.getTypeAffiliation())
                        || CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE.equalsIgnoreCase(af.getTypeAffiliation())
                        || CodeSystem.TYPE_AFFILI_SELON_ART_1A.equalsIgnoreCase(af.getTypeAffiliation())) {
                    if (!affSet.contains(af.getAffilieNumero())) {
                        /**/
                        options.append("<option value='");
                        options.append(af.getAffilieNumero());
                        /**/
                        options.append("' nom='");
                        TITiersViewBean ti = new TITiersViewBean();
                        ti.setSession(bsession);
                        ti.setIdTiers(af.getIdTiers());
                        ti.retrieve();
                        options.append(ti.getNom());
                        options.append("' idTiers='");
                        options.append(af.getIdTiers());
                        options.append("'");
                        options.append("' idAffiliation='");
                        options.append(af.getId());
                        options.append("'");
                        options.append(" numAffilie='");
                        options.append(af.getAffilieNumero());
                        options.append("'");
                        options.append(">");
                        options.append(af.getAffilieNumero());
                        options.append(" - ");
                        options.append(ti.getNom());
                        options.append(" - ");
                        options.append(ti.getNumAvsActuel());
                        options.append("</option>");
                        affSet.add(af.getAffilieNumero());
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        return options.toString();
    }

    /**
     * Retourne une liste de numéros et noms d'assurés en fonction des permiers chiffres du numéro avs donnés en
     * paramètres. Date de création : (10.03.2004 09:05:13)
     * 
     * @return le numéro d'affilié des ticket étudiants
     */
    public static int getAutoDigitAff(HttpSession session) {
        BSession bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
        try {
            BApplication application = bSession.getApplication();
            if (application instanceof CPApplication) {
                return ((CPApplication) application).getAutoDigitAffilie();
            } else if (application instanceof AFApplication) {
                return ((AFApplication) application).getAutoDigitAffilie();
            } else if (application instanceof FAApplication) {
                return ((FAApplication) application).getAutoDigitAffilie();
            } else if (application instanceof CIApplication) {
                return ((CIApplication) application).getAutoDigitAffilie();
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getCaisseCompForCI(String like, BSession session) {
        StringBuffer options = new StringBuffer();
        HashSet<String> ccSet = new HashSet<String>();
        try {
            TIAdministrationManager mgr = new TIAdministrationManager();
            mgr.setForCodeAdministrationLike(like);
            mgr.setForGenreAdministration(TIAdministrationViewBean.CS_CAISSE_COMPENSATION);
            mgr.setSession(session);
            mgr.find(BManager.SIZE_NOLIMIT);
            for (int i = 0; i < mgr.size(); i++) {
                TIAdministrationViewBean ti = (TIAdministrationViewBean) mgr.getEntity(i);
                // if(ccSet.contains(ti.getCodeAdministration())){
                options.append("<option value='");
                options.append(ti.getCodeAdministration());
                options.append("' nom=\"");
                options.append(ti.getNom());
                options.append("\"");
                options.append(">");
                options.append(ti.getCodeAdministration());
                options.append("-");
                options.append(ti.getNom());
                options.append("</option>");
                ccSet.add(ti.getCodeAdministration());
                // }
            }
        } catch (Exception e) {
        }
        return options.toString();
    }

    public static String getGenreCI(CPDecisionAffiliation decision, TITiersViewBean tiers) throws Exception {
        String genre = "";
        if ((decision != null) && (tiers != null)) {
            if (decision.isNonActif()) {
                if (tiers.isRentier(Integer.parseInt(decision.getAnneeDecision()))) {
                    genre = CIEcriture.CS_CIGENRE_7;
                } else {
                    genre = CIEcriture.CS_CIGENRE_4;
                }
            } else if (CPDecision.CS_INDEPENDANT.equalsIgnoreCase(decision.getGenreAffilie())) {
                genre = CIEcriture.CS_CIGENRE_3;
            } else if (CPDecision.CS_RENTIER.equalsIgnoreCase(decision.getGenreAffilie())) {
                genre = CIEcriture.CS_CIGENRE_7;
            } else if (CPDecision.CS_AGRICULTEUR.equalsIgnoreCase(decision.getGenreAffilie())) {
                genre = CIEcriture.CS_CIGENRE_9;
            } else if (CPDecision.CS_TSE.equalsIgnoreCase(decision.getGenreAffilie())) {
                genre = CIEcriture.CS_CIGENRE_2;
            }
        }
        return genre;
    }

    public static String getNssOrNavs(String numero, BSession session) {

        NSSinfo info = null;
        try {
            NSSinfoManager mgr = new NSSinfoManager();
            mgr.setSession(session);
            boolean numeroNss = false;
            if (NSUtil.unFormatAVS(numero).trim().length() == 13) {
                numeroNss = true;
                mgr.setForNNSS(numero);
            } else {
                mgr.setForNAVS(numero);
            }

            mgr.setForCodeMutation("0");
            mgr.setForValidite("1");
            mgr.find();

            if (mgr.size() > 0) {
                if (numeroNss) {
                    boolean navsFound = false;
                    for (int i = 0; i < mgr.size(); i++) {
                        if (!navsFound) {
                            info = (NSSinfo) mgr.get(i);
                            if (info.getNAVS().length() == 11) {
                                navsFound = true;
                            } else {
                                info = null;
                            }
                        }
                    }
                } else {
                    info = (NSSinfo) mgr.getFirstEntity();
                }
            }
            if (info == null) {
                mgr.setForValidite("0");
                mgr.find();
                if (mgr.size() > 0) {
                    if (numeroNss) {
                        boolean navsFound = false;
                        for (int i = 0; i < mgr.size(); i++) {
                            if (!navsFound) {
                                info = (NSSinfo) mgr.get(i);
                                if (info.getNAVS().length() == 11) {
                                    navsFound = true;
                                } else {
                                    info = null;
                                }
                            }
                        }
                    }
                } else {
                    info = (NSSinfo) mgr.getFirstEntity();
                }
            }
            if (NSUtil.unFormatAVS(numero).trim().length() == 13) {
                if (info != null) {
                    return info.getNAVS();
                }
                return "";
            } else {
                if (info != null) {
                    return info.getNNSS();
                }
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getRemarkInfo(String idRetour, String champ, BSession session) {
        //
        try {
            CPSedexContribuable contribuable = new CPSedexContribuable();
            contribuable.setSession(session);
            contribuable.setIdRetour(idRetour);
            contribuable.retrieve();
            String remark = contribuable.getRemark();
            int positionChamp = remark.indexOf("|" + champ + "=");
            if (positionChamp > -1) {
                remark = remark.substring(positionChamp + champ.length() + 2);
                int finChamp = remark.indexOf("|");
                return remark.substring(0, finChamp);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static String getRemarkInfoDepuisString(String remark, String champ, BSession session) {
        //
        try {
            int positionChamp = remark.indexOf("|" + champ + "=");
            if (positionChamp > -1) {
                remark = remark.substring(positionChamp + champ.length() + 2);
                int finChamp = remark.indexOf("|");
                return remark.substring(0, finChamp);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static int getTailleChampsAffilie(HttpSession session) {
        BSession bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
        try {
            CIApplication application = (CIApplication) bSession.getApplication();
            return application.getTailleChampsAffilie();
        } catch (Exception e) {
            return 11;
        }
    }

    public static Vector<String[]> getUserList(javax.servlet.http.HttpSession httpSession) {
        Vector<String[]> vList = new Vector<String[]>();
        // ajoute un blanc
        String[] list = new String[15];
        list[0] = "";
        list[1] = "";
        vList.add(list);
        ArrayList<BIPersistentObject> groupVector = new ArrayList<BIPersistentObject>();
        try {
            // charger les users appartenant au groupes suivant: NEK et NEM
            // remarque:pas performant car il faudrait utiliser des jointures à
            // la place
            // -> modif du framework indispensable
            FWSecureGroupUsers groupManager = new FWSecureGroupUsers();
            groupManager.setISession(globaz.phenix.translation.CodeSystem.getSession(httpSession));
            // ON Va charger les groupes depuis la BD.
            FWParametersManager manager = new FWParametersManager();
            FWParameters group;
            manager.setISession(globaz.phenix.translation.CodeSystem.getSession(httpSession));
            manager.setForApplication(globaz.phenix.application.CPApplication.DEFAULT_APPLICATION_PHENIX);
            manager.setForIdCode(CPUtil.CS_NEKNEM);
            manager.find();
            // on ajoute les groupes au vecteur
            for (int i = 0; i < manager.getSize(); i++) {
                group = (FWParameters) manager.get(i);
                groupManager.setForGroup(group.getValeurAlpha()); // BTC
                groupManager.changeManagerSize(0); // recherche illimitée
                groupManager.find();
                for (int j = 0; j < groupManager.getSize(); j++) {
                    groupVector.add(groupManager.get(j));
                }
            }
            // On verifie si c'est un ut autorisé du groupe
            FWSecureUsers managerUser = new FWSecureUsers();
            managerUser.setISession(globaz.phenix.translation.CodeSystem.getSession(httpSession));
            managerUser.changeManagerSize(0);
            managerUser.find();
            for (int i = 0; i < managerUser.size(); i++) {
                list = new String[15];
                FWSecureUser entity = (FWSecureUser) managerUser.getEntity(i);
                // limiter la liste seulement au users qui sont contenus dans le
                // groupVector
                for (int j = 0; j < groupVector.size(); j++) {
                    FWSecureGroupUser secGroupUser = (FWSecureGroupUser) groupVector.get(j);
                    if (secGroupUser.getUser().equalsIgnoreCase((entity.getUser()))) {
                        list[0] = entity.getId();
                        list[1] = entity.getFirstname() + " " + entity.getLastname();
                        vList.add(list);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vList;
    }

    /**
     * return true si la période de décision est comprise dans une période d'affiliation
     * 
     * @param mySession
     * @param myDecision
     * @param myAffiliation
     * @return
     */
    public static boolean isDecisionValid(BSession mySession, CPDecisionAffiliation myDecision,
            AFAffiliation myAffiliation) {
        boolean decisionIsValid = false;
        try {
            if (BSessionUtil.compareDateFirstLowerOrEqual(mySession, myAffiliation.getDateDebut(),
                    myDecision.getDebutDecision())) {
                if (BSessionUtil.compareDateFirstGreaterOrEqual(mySession, myAffiliation.getDateFin(),
                        myDecision.getFinDecision())
                        || JadeStringUtil.isEmpty(myAffiliation.getDateFin())) {
                    decisionIsValid = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decisionIsValid;
    }

    public static String plafonneRevenuDeterminant(String theRevenuDeterminant, String theAssuranceId,
            String theGenreParametreAssurance, String theDate, BSession theSession) throws Exception {

        StringBuffer wrongArgumentBuffer = new StringBuffer();

        if (!JadeNumericUtil.isNumeric(theRevenuDeterminant)) {
            wrongArgumentBuffer.append("theRevenuDeterminant is not a numeric : " + theRevenuDeterminant + " / ");
        }

        if (JadeStringUtil.isBlankOrZero(theAssuranceId)) {
            wrongArgumentBuffer.append("theAssuranceId is blank or zero / ");
        }

        if (JadeStringUtil.isBlankOrZero(theGenreParametreAssurance)) {
            wrongArgumentBuffer.append("theGenreParametreAssurance is blank or zero / ");
        }

        if (!JadeDateUtil.isGlobazDate(theDate)) {
            wrongArgumentBuffer.append("theDate is not a date : " + theDate + " / ");
        }

        if (theSession == null) {
            wrongArgumentBuffer.append("theSession is null");
        }

        if (wrongArgumentBuffer.length() >= 1) {
            throw new Exception("unable to plafonne revenu determinant due to wrong arguments : "
                    + wrongArgumentBuffer.toString());
        }

        AFAssurance entityAssurance = new AFAssurance();
        entityAssurance.setSession(theSession);
        entityAssurance.setAssuranceId(theAssuranceId);
        entityAssurance.retrieve();

        if ((entityAssurance == null) || entityAssurance.isNew()) {
            throw new Exception("unable to plafonne revenu determinant because can't find the assurance");
        }

        AFParametreAssurance theParametreAssurance = AFUtil.giveParametreAssurance(theGenreParametreAssurance,
                theAssuranceId, theDate, theSession);

        if (theParametreAssurance == null) {
            throw new Exception("unable to plafonne revenu determinant because can't find parameter "
                    + theGenreParametreAssurance + " for assurance " + theAssuranceId + " on " + theDate);
        }

        if (Double.valueOf(theRevenuDeterminant).doubleValue() >= Double.valueOf(
                JANumberFormatter.deQuote(theParametreAssurance.getValeurNum())).doubleValue()) {
            return JANumberFormatter.deQuote(theParametreAssurance.getValeurNum());
        }

        return theRevenuDeterminant;
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
     * Method unformat. Oter le formatage du numéro AVS qui contient des "."
     * 
     * @param value
     * @return String
     * @throws Exception
     */
    //
    public static String unformat(String value) throws Exception {
        if (value == null) {
            return "";
        }
        String str = "";
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) != '.') {
                str += value.charAt(i);
            }
        }
        return str;
    }

    /**
     * Détermination du type de décision selon le type de l'ancienne décision
     * 
     * @return
     */
    public static String determinerTypeDecision(Integer anneeDemande, String typeDecisionActuel) {
        Integer anneeCourante = Date.getCurrentYear();
        String typeDecision = CPDecision.CS_PROVISOIRE;

        if (typeDecisionActuel == null) {
            if (anneeCourante.equals(anneeDemande)) {
                typeDecision = CPDecision.CS_ACOMPTE;
            }
        } else {
            if (typeDecisionActuel.equalsIgnoreCase(CPDecision.CS_PROVISOIRE)
                    || typeDecisionActuel.equalsIgnoreCase(CPDecision.CS_CORRECTION)) {
                typeDecision = CPDecision.CS_CORRECTION;
            } else if (anneeCourante.equals(anneeDemande)) {
                typeDecision = CPDecision.CS_ACOMPTE;
            } else {
                typeDecision = CPDecision.CS_PROVISOIRE;
            }
        }
        return typeDecision;
    }

    public CPUtil() {
        super();
    }

}
