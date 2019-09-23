/*
 * Created on 25-Jan-05
 */
package globaz.naos.util;

import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.controller.FWController;
import globaz.framework.util.FWCurrency;
import globaz.framework.util.FWMemoryLog;
import globaz.framework.util.FWMessage;
import globaz.framework.util.FWMessageFormat;
import globaz.globall.api.BIApplication;
import globaz.globall.api.BISession;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.util.JACalendar;
import globaz.globall.util.JACalendarGregorian;
import globaz.globall.util.JADate;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeNumericUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.leo.constantes.ILEConstantes;
import globaz.lupus.db.data.LUProvenanceDataSource;
import globaz.lupus.db.journalisation.LUJournalListViewBean;
import globaz.naos.application.AFApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationJTiers;
import globaz.naos.db.affiliation.AFAffiliationJTiersManager;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.db.assurance.AFRechercherAssuranceManager;
import globaz.naos.db.controleEmployeur.AFControleEmployeurManager;
import globaz.naos.db.controleEmployeur.AFReviseur;
import globaz.naos.db.controleEmployeur.AFReviseurManager;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.db.parametreAssurance.AFParametreAssurance;
import globaz.naos.db.parametreAssurance.AFParametreAssuranceManager;
import globaz.naos.db.planAffiliation.AFPlanAffiliation;
import globaz.naos.db.planAffiliation.AFPlanAffiliationManager;
import globaz.naos.db.releve.AFApercuReleveManager;
import globaz.naos.translation.CodeSystem;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteur;
import globaz.osiris.db.comptes.CACompteurManager;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CARubriqueManager;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TIRole;
import globaz.pyxis.db.tiers.TITiers;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.webavs.common.CommonExcelmlContainer;
import globaz.webavs.common.op.CommonExcelDataContainer.CommonLine;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Class Utilitaire pour Affiliation.
 * 
 * @author sau
 */
public class AFUtil {
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

    public static int calculerAgeAvs(String sexe, int anneeNaissance) {
        int ageAvs = 0;
        if (sexe.equalsIgnoreCase(IConstantes.CS_PERSONNE_SEXE_HOMME)) {
            ageAvs = anneeNaissance + 65;
        } else if (anneeNaissance <= 1938) {
            ageAvs = anneeNaissance + 62;
        } else if (anneeNaissance <= 1941) {
            ageAvs = anneeNaissance + 63;
        } else {
            ageAvs = anneeNaissance + 64;
        }
        return ageAvs;
    }

    /**
     * Permet de connaître le nombre de contrôle selon les critères de sélection passés en paramètre
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
            BSession session) throws Exception {
        // Test paramètre indispensable pour ne pas avoir une requête "trop lourde"
        if (JadeStringUtil.isBlankOrZero(annee) && JadeStringUtil.isBlankOrZero(idAffiliation)
                && JadeStringUtil.isBlankOrZero(dateDebut) && JadeStringUtil.isBlankOrZero(dateFin)) {
            throw new Exception("Insufficient number of arguments");
        }
        AFControleEmployeurManager manager = new AFControleEmployeurManager();
        manager.setSession(session);
        manager.setForAffiliationId(idAffiliation);
        manager.setForDateDebutControle(dateDebut);
        manager.setForDateFinControle(dateFin);
        manager.setForAnnee(annee);
        manager.find();
        return manager.getSize();
    }

    /**
     * Permet de remplir un NaosContainer à partir d'une AFLine et de rajouter une erreur dans le container
     * 
     * @param container
     * @param line
     * @param tabNoms
     * @param erreur
     * @param erreurName
     */
    public static void fillNaosContainerWithAFLine(CommonExcelmlContainer container, CommonLine line, String[] tabNoms,
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
     * Retourne le suffix (les trois dernières positions du no de décompte) en fonction du plan d'affiliation à facturer
     * 
     * @param session
     *            la session utlisateur
     * @param affiliationId
     *            l'id affiliation
     * @param planAffiliationId
     *            l'id du plan d'affiliation à facturer
     * @param idPassage
     *            le numéro de passage de facturation (est ignoré pour l'instant)
     * @return le suffix à utiliser pour cette facture
     * @throws Exception
     *             si une exception survient
     */
    public static String genererSuffixDécompte(BSession session, String affiliationId, String planAffiliationId,
            String idPassage) throws Exception {
        // TOCO: recherche du prochain suffix du passage donné en paramètre
        int suffix = 100;

        // recherche des plans de l'affilié
        AFPlanAffiliationManager mgr = new AFPlanAffiliationManager();
        mgr.setSession(session);
        mgr.setForAffiliationId(affiliationId);
        mgr.setOrder("MUIPLA");
        mgr.find();
        for (int i = 0; i < mgr.size(); i++) {
            AFPlanAffiliation plan = (AFPlanAffiliation) mgr.getEntity(i);
            if (plan.getPlanAffiliationId().equals(planAffiliationId)) {
                // c'est le plan concerné, on renvoie la valeur du suffix en
                // cours
                return String.valueOf(suffix);
            }
            suffix++;
        }
        // le plan n'est pas trouvé, ne devrait jamais arrivé -> retourne le
        // suffi en cours
        return String.valueOf(suffix);

    }

    public static String getAffiliationEnCours(HttpSession session, HttpServletRequest request) {
        BSession bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
        // String idAffiliation =
        // (String)session.getAttribute("affiliationPrincipale");
        String idAffiliation = request.getParameter("affiliationId");
        if ((bSession != null) && !JadeStringUtil.isEmpty(idAffiliation)) {
            try {
                AFAffiliation aff = new AFAffiliation();
                aff.setSession(bSession);
                aff.setAffiliationId(idAffiliation);
                aff.retrieve();
                return aff.getAffilieNumero();
            } catch (Exception ex) {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Retourne les affiliations actives par rapport à l'idTiers passé en paramètre.
     * Si aucune affiliation est active, alors on retourne la dernière en date.
     * 
     * @param idTiers String représentant un idTiers
     * @param session BISession
     * @return Liste d'AFAffiliation
     * @throws Exception Exception retournée lors de la recherche via {@link AFAffiliationManager}
     */
    public static List<AFAffiliation> getAffiliationsAAfficherForIdTiers(String idTiers, BISession session)
            throws Exception {
        List<AFAffiliation> affiliations = new ArrayList<AFAffiliation>();
        AFAffiliationManager affManager = new AFAffiliationManager();
        affManager.setISession(session);
        affManager.setForIdTiers(idTiers);
        affManager.setOrder("MADDEB DESC");
        affManager.find();
        if (affManager.size() == 0) {
            // aucune affiliation trouvée pour ce tiers
            return affiliations;
        } else {
            for (int i = 0; i < affManager.size(); i++) {
                AFAffiliation aff = (AFAffiliation) affManager.getEntity(i);
                if (JadeStringUtil.isIntegerEmpty(aff.getDateFin())) {
                    affiliations.add(aff);
                }
            }

            // Si il n'y a pas d'affiliations actives, alors on prend la dernière en date.
            if (affiliations.size() == 0) {
                affiliations.add((AFAffiliation) affManager.get(0));
            }
            return affiliations;
        }
    }

    /**
     * Retourne une liste des Affilies (numéro et nom de l'affilié) en fonction des permiers chiffres du numéro affilié
     * donnés en paramètres.
     * 
     * 
     * @return la liste d'options (tag select) des affiliés existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            - les primers chiffres du numéro affilié
     * @param session
     *            - la session HTTP actuelle
     */
    public static String getAffilies(String like, HttpSession session) {
        return AFUtil
                .getAffillies(like, (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }
    
    /**
     * Retourne une liste des Affilies (numéro et nom de l'affilié) en fonction des permiers chiffres du numéro affilié
     * donnés en paramètres.
     * 
     * 
     * @return la liste d'options (tag select) des affiliés existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            - les primers chiffres du numéro affilié
     * @param session
     *            - la session HTTP actuelle
     */
    public static String getAffilies(String like, String max, HttpSession session) {
        return AFUtil
                .getAffillies(like, max, (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    public static String getAffillies(String like, BSession bsession) {
        return getAffillies(like, null, bsession);
    }

    /**
     * Retourne une liste des Affilies (numéro et nom de l'affilié) en fonction des permiers chiffres du numéro affilié
     * donnés en paramètres.
     * 
     * 
     * @return la liste d'options (tag select) des affiliés existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            - les primers chiffres du numéro affilié
     * @param session
     *            - la session HTTP actuelle
     */
    public static String getAffiliesId(String like, HttpSession session) {
        return AFUtil.getAffilliesId(like,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * Retourne une liste des Affilies (numéro et nom de l'affilié) en fonction des permiers chiffres du numéro affilié
     * donnés en paramètres.
     * 
     * 
     * @return la liste d'options (tag select) des affiliés existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            - les primers chiffres du numéro affilié
     * @param bsession
     *            - la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getAffillies(String like, String max, BSession bsession) {
        if (JadeStringUtil.isBlank(like)) {
            like = "";
        }
        AFApplication afApp = null;
        try {
            afApp = (AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS);
        } catch (Exception e) {
            JadeLogger.error(null, e);
        }
        try {
            IFormatData affilieFormater = afApp.getAffileFormater();
            if (affilieFormater != null) {
                like = affilieFormater.format(like);
            }
        } catch (Exception e) {
            JadeLogger.error(null, e);
        }

        StringBuffer options = new StringBuffer();
        HashSet<String> affSet = new HashSet<String>();
        try {
            // K170119_001, ne pas faire la recherche si le no d'affilié est vide, évite de lancer une recherche sur
            // tout
            // I190125_018 : Modification de l'écran de recherche d'affiliation : paramètre limite + modification de la requête : jointure

            AFAffiliationJTiersManager manager = new AFAffiliationJTiersManager();
            manager.setSession(bsession);
            manager.setLikeAffilieNumero(like);
            if (!JadeStringUtil.isBlank(like)) {
                manager.find(getMaxValue(max));    
            }
            for (int i = 0; i < manager.size(); i++) {
                AFAffiliationJTiers affilie = (AFAffiliationJTiers) manager.getEntity(i);
                String nom = affilie.getNom();
                String designation1 = affilie.getDesignation1();
                String designation2 = affilie.getDesignation2();
                if (!affSet.contains(affilie.getIdTiers() + "-" + affilie.getAffilieNumero())) {
                    options.append("<option value='");
                    options.append(affilie.getAffilieNumero());
                    options.append("' nom='");
//                    TITiers tiers = new TITiers();
//                    tiers.setSession(bsession);
//                    tiers.setIdTiers(affilie.getIdTiers());
//                    tiers.retrieve();
//                    if (!tiers.isNew()) {
//                        nom = tiers.getNom();
//                        designation1 = tiers.getDesignation1();
//                        designation2 = tiers.getDesignation2();
//                    }
                    options.append(nom);
                    options.append("' idTiers='");
                    options.append(affilie.getIdTiers());
                    options.append("' designation1='");
                    options.append(designation1);
                    options.append("' designation2='");
                    options.append(designation2);
                    options.append("'");
                    options.append(">");
                    options.append(affilie.getAffilieNumero());
                    options.append(" - ");
                    options.append(nom);
                    if ((affilie.getTypeAssocie() != null) && (affilie.getTypeAssocie().length() != 0)) {
                        options.append(" - ");
                        options.append(CodeSystem.getCode(affilie.getSession(), affilie.getTypeAssocie()));
                    }
                    options.append("</option>");
                    affSet.add(affilie.getIdTiers() + "-" + affilie.getAffilieNumero());
                }
            }

            if (manager.size() == 0) {
                options.append("<option value='");
                options.append(like);
                options.append("' nom=''");
                options.append("'");
                options.append(">");
                options.append(like);
                options.append("</option>");
            }
        } catch (Exception ex) {
            JadeLogger.error(null, ex);
        }
        return options.toString();
    }
    
    private static Integer getMaxValue(String value) {
        if(value != null) {
            try {
                return Integer.parseInt(value);    
            } catch (NumberFormatException e) {
                return BManager.SIZE_USEDEFAULT;
            }
        }
        return BManager.SIZE_USEDEFAULT;
    }

    /**
     * Retourne une liste des Affilies (numéro et nom de l'affilié) en fonction des permiers chiffres du numéro affilié
     * donnés en paramètres.
     * 
     * 
     * @return la liste d'options (tag select) des affiliés existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            - les primers chiffres du numéro affilié
     * @param bsession
     *            - la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getAffilliesId(String like, BSession bsession) {
        if (JadeStringUtil.isBlank(like)) {
            like = "";
        }
        AFApplication afApp = null;
        try {
            afApp = (AFApplication) bsession.getApplication();
        } catch (Exception e) {
            JadeLogger.error(null, e);
        }
        try {
            IFormatData affilieFormater = afApp.getAffileFormater();
            if (affilieFormater != null) {
                like = affilieFormater.format(like);
            }
        } catch (Exception e) {
            JadeLogger.error(null, e);
        }

        StringBuffer options = new StringBuffer();
        HashSet<String> affSet = new HashSet<String>();
        try {
            AFAffiliationManager manager = new AFAffiliationManager();
            manager.setSession(bsession);
            manager.setLikeAffilieNumero(like);
            manager.setForTypeFacturation(AFAffiliationManager.PARITAIRE);
            manager.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < manager.size(); i++) {
                AFAffiliation affilie = (AFAffiliation) manager.getEntity(i);

                options.append("<option value='");
                options.append(affilie.getAffilieNumero());
                options.append("' nom='");
                TITiers tiers = new TITiers();
                tiers.setSession(bsession);
                tiers.setIdTiers(affilie.getIdTiers());
                tiers.retrieve();
                options.append(tiers.getNom());
                options.append("' idTiers='");
                options.append(tiers.getIdTiers());
                options.append("' affiliationId='");
                options.append(affilie.getAffiliationId());
                options.append("' designation1='");
                options.append(tiers.getDesignation1());
                options.append("' designation2='");
                options.append(tiers.getDesignation2());
                options.append("'");
                options.append(">");
                options.append(affilie.getAffilieNumero());
                options.append(" - ");
                options.append(tiers.getNom());
                if ((affilie.getTypeAssocie() != null) && (affilie.getTypeAssocie().length() != 0)) {
                    options.append(" - ");
                    options.append(CodeSystem.getCode(affilie.getSession(), affilie.getTypeAssocie()));
                }
                options.append(" (");
                options.append(affilie.getDateDebut());
                options.append(" - ");
                if (JadeStringUtil.isEmpty(affilie.getDateFin())) {
                    options.append("0");
                } else {
                    options.append(affilie.getDateFin());
                }
                options.append(")");
                options.append("</option>");
                affSet.add(affilie.getIdTiers() + "-" + affilie.getAffilieNumero());
            }
            if (manager.size() == 0) {
                options.append("<option value='");
                options.append(like);
                options.append("' nom=''");
                options.append("'");
                options.append(">");
                options.append(like);
                options.append("</option>");
            }
        } catch (Exception ex) {
            JadeLogger.error(null, ex);
        }
        return options.toString();
    }

    public static String getAncienAffilies(String like, HttpSession session) {
        return AFUtil.getAncienAffillies(like,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * Retourne une liste des Affilies (numéro et nom de l'affilié) en fonction des permiers chiffres de l'ancien numéro
     * affilié donnés en paramètres.
     * 
     * 
     * @return la liste d'options (tag select) des affiliés existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            - les primers chiffres du numéro affilié
     * @param bsession
     *            - la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getAncienAffillies(String like, BSession bsession) {
        if (JadeStringUtil.isBlank(like)) {
            like = "";
        }
        AFApplication afApp = null;
        try {
            afApp = (AFApplication) bsession.getApplication();
        } catch (Exception e) {
            JadeLogger.error(null, e);
        }
        try {
            IFormatData affilieFormater = afApp.getAncienAffileFormater();
            if (affilieFormater != null) {
                like = affilieFormater.format(like);
            }
        } catch (Exception e) {
            JadeLogger.error(null, e);
        }

        StringBuffer options = new StringBuffer();
        HashSet<String> affSet = new HashSet<String>();
        try {
            AFAffiliationManager manager = new AFAffiliationManager();
            manager.setSession(bsession);
            manager.setLikeAncienNumero(like);
            manager.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < manager.size(); i++) {
                AFAffiliation affilie = (AFAffiliation) manager.getEntity(i);

                if (!affSet.contains(affilie.getIdTiers())) {
                    options.append("<option value='");
                    options.append(affilie.getAncienAffilieNumero());
                    options.append("' nom='");
                    TITiers tiers = new TITiers();
                    tiers.setSession(bsession);
                    tiers.setIdTiers(affilie.getIdTiers());
                    tiers.retrieve();
                    options.append(tiers.getNom());
                    options.append("' idTiers='");
                    options.append(tiers.getIdTiers());
                    options.append("' designation1='");
                    options.append(tiers.getDesignation1());
                    options.append("' designation2='");
                    options.append(tiers.getDesignation2());
                    options.append("'");
                    options.append(">");
                    options.append(affilie.getAncienAffilieNumero());
                    options.append(" - ");
                    options.append(tiers.getNom());
                    if ((affilie.getTypeAssocie() != null) && (affilie.getTypeAssocie().length() != 0)) {
                        options.append(" - ");
                        options.append(CodeSystem.getCode(affilie.getSession(), affilie.getTypeAssocie()));
                    }
                    options.append("</option>");
                    affSet.add(affilie.getIdTiers());
                }
            }
            if (manager.size() == 0) {
                options.append("<option value='");
                options.append(like);
                options.append("' nom=''");
                options.append("'");
                options.append(">");
                options.append(like);
                options.append("</option>");
            }
        } catch (Exception ex) {
            JadeLogger.error(null, ex);
        }
        return options.toString();
    }

    /**
     * Retourne une liste d'assurance.
     * 
     * @return la liste d'options (tag select) d'Assurance. Peut-être vide si aucune information n'a été trouvée
     * @param like
     *            - le nom de l'assurance
     * @param bsession
     *            - la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getAssurance(String like, BSession bsession) {
        if (JadeStringUtil.isBlank(like)) {
            like = "";
        }

        StringBuffer options = new StringBuffer();
        HashSet<String> affSet = new HashSet<String>();
        try {
            AFAssuranceManager manager = new AFAssuranceManager();
            manager.setSession(bsession);
            manager.setFromLibelle(like);
            manager.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < manager.size(); i++) {

                AFAssurance admin = (AFAssurance) manager.getEntity(i);
                if (!affSet.contains(admin.getAssuranceId())) {
                    options.append("<option value='");
                    options.append(admin.getAssuranceLibelle());
                    options.append("' assuranceId='");
                    options.append(admin.getAssuranceId());
                    options.append("'");
                    options.append(">");
                    options.append(admin.getAssuranceLibelle());
                    options.append("</option>");
                    affSet.add(admin.getAssuranceId());
                }
            }
        } catch (Exception ex) {
            JadeLogger.error(null, ex);
        }
        return options.toString();
    }

    /**
     * Retourne une liste d'assurance.
     * 
     * @return la liste d'options (tag select) d'Assurance. Peut-être vide si aucune information n'a été trouvée
     * @param like
     *            - le nom de l'assurance
     * @param session
     *            - la session HTTP actuelle
     */
    public static String getAssurance(String like, HttpSession session) {
        return AFUtil
                .getAssurance(like, (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * Retourne une liste d'assurance.
     * 
     * @return la liste d'options (tag select) d'Assurance. Peut-être vide si aucune information n'a été trouvée
     * @param like
     *            - le nom de l'assurance
     * @param bsession
     *            - la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getAssuranceLike(String like, BSession bsession) {
        if (JadeStringUtil.isBlank(like)) {
            like = "";
        }

        StringBuffer options = new StringBuffer();
        HashSet<String> affSet = new HashSet<String>();
        try {
            AFAssuranceManager manager = new AFAssuranceManager();
            manager.setSession(bsession);
            manager.setForLibelleCourtOuLongLike(like);
            manager.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < manager.size(); i++) {

                AFAssurance admin = (AFAssurance) manager.getEntity(i);
                if (!affSet.contains(admin.getAssuranceId())) {
                    options.append("<option value=\"");
                    options.append(admin.getAssuranceLibelle() + " ("
                            + CodeSystem.getLibelle(bsession, admin.getAssuranceGenre()) + ")");
                    options.append("\" assuranceId='");
                    options.append(admin.getAssuranceId());
                    options.append("'");
                    options.append(">");
                    options.append(admin.getAssuranceLibelle());
                    options.append("</option>");
                    affSet.add(admin.getAssuranceId());
                }
            }
        } catch (Exception ex) {
            JadeLogger.error(null, ex);
        }
        return options.toString();
    }

    /**
     * Retourne une liste d'assurance.
     * 
     * @return la liste d'options (tag select) d'Assurance. Peut-être vide si aucune information n'a été trouvée
     * @param like
     *            - le nom de l'assurance
     * @param session
     *            - la session HTTP actuelle
     */
    public static String getAssuranceLike(String like, HttpSession session) {
        return AFUtil.getAssuranceLike(like,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * Retourne une liste de numéros et noms d'assurés en fonction des permiers chiffres du numéro avs donnés en
     * paramètres.
     * 
     * @return le numéro d'affilié des ticket étudiants
     */
    public static int getAutoDigitAff(HttpSession session) {
        BSession bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
        try {
            AFApplication application = (AFApplication) bSession.getApplication();
            return application.getAutoDigitAffilie();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Retour la date pour le debut du mois.
     * 
     * @param date
     *            DD.MM.YYYY
     * @return
     */
    public static String getDateBeginingOfMonth(String date) {

        String result = null;
        if (!JadeStringUtil.isIntegerEmpty(date)) {
            result = "01." + date.substring(3, 5) + "." + date.substring(6);
        } else {
            result = date;
        }
        return result;
    }

    /**
     * Retourne le premier jour du Trimestre en cours.
     * 
     * @param date
     *            DD.MM.YYYY
     * @return
     */
    public static String getDateBeginingOfTrim(String date) {

        String end = AFUtil.getDateEndOfTrim(date);
        String mois = String.valueOf(Integer.parseInt(end.substring(3, 5)) - 2);
        if (mois.length() == 1) {
            mois = "0" + mois;
        }
        return "01." + mois + "." + end.substring(6);
    }

    /**
     * Retour la date pour la fin du mois.
     * 
     * @param date
     *            DD.MM.YYYY
     * @return
     */
    public static String getDateEndOfMonth(String date) {

        String result = null;
        if (!JadeStringUtil.isIntegerEmpty(date)) {

            String jour = "31";
            String mois = date.substring(3, 5);
            String annee = date.substring(6);

            switch (Integer.parseInt(mois)) {

                case 4:
                case 6:
                case 9:
                case 11:
                    jour = "30";
                    break;
                case 2:
                    int iAnnee = Integer.parseInt(annee);
                    if ((((iAnnee % 4) == 0) && !((iAnnee % 100) == 0)) || ((iAnnee % 400) == 0)) {
                        jour = "29";
                    } else {
                        jour = "28";
                    }
                    break;
            }
            result = jour + "." + mois + "." + annee;
        } else {
            result = date;
        }
        return result;
    }

    /**
     * Retourne le dernier jour du mois suivant.
     * 
     * @param date
     *            DD.MM.YYYY
     * @return
     */
    public static String getDateEndOfNextMonth(String date) {

        String result = null;
        if (!JadeStringUtil.isIntegerEmpty(date)) {
            int mois = Integer.parseInt(date.substring(3, 5));
            int annee = Integer.parseInt(date.substring(6));

            mois += 1;
            if (mois == 13) {
                mois = 1;
                annee += 1;
            }
            String sMois = "0" + Integer.toString(mois);
            if (mois >= 10) {
                sMois = Integer.toString(mois);
            }
            result = AFUtil.getDateEndOfMonth("01." + sMois + "." + Integer.toString(annee));
        } else {
            result = date;
        }
        return result;
    }

    /**
     * Retourne le dernier jour du prochain Trimestre.
     * 
     * @param date
     *            DD.MM.YYYY
     * @return
     */
    public static String getDateEndOfNextTrim(String date) {

        String result = null;
        if (!JadeStringUtil.isIntegerEmpty(date)) {
            result = AFUtil.getDateEndOfTrim(date);
            for (int i = 0; i < 3; i++) {
                result = AFUtil.getDateEndOfNextMonth(result);
                int mois = Integer.parseInt(result.substring(3, 5));
                if ((mois == 3) || (mois == 6) || (mois == 9) || (mois == 12)) {
                    break;
                }
            }
        } else {
            result = date;
        }
        return result;
    }

    /**
     * Retourne le dernier jour du Xème mois précédent.
     * 
     * @param date
     *            DD.MM.YYYY
     * @return
     */
    public static String getDateEndOfPreviousMonth(int nbMois, String date) {

        String result = null;
        if (!JadeStringUtil.isIntegerEmpty(date)) {
            result = date;
            for (int i = 0; i < nbMois; i++) {
                result = AFUtil.getDateEndOfPreviousMonth(result);
            }
        } else {
            result = date;
        }
        return result;
    }

    /**
     * Retourne le dernier jour du mois précédent.
     * 
     * @param date
     *            DD.MM.YYYY
     * @return
     */
    public static String getDateEndOfPreviousMonth(String date) {

        String result = null;
        if (!JadeStringUtil.isIntegerEmpty(date)) {
            int mois = Integer.parseInt(date.substring(3, 5));
            int annee = Integer.parseInt(date.substring(6));

            mois -= 1;
            if (mois == 0) {
                mois = 12;
                annee -= 1;
            }
            String sMois = "0" + Integer.toString(mois);
            if (mois >= 10) {
                sMois = Integer.toString(mois);
            }
            result = AFUtil.getDateEndOfMonth("01." + sMois + "." + Integer.toString(annee));
        } else {
            result = date;
        }
        return result;
    }

    /**
     * Retourne le dernier jour du précédent Trimestre.
     * 
     * @param date
     *            DD.MM.YYYY
     * @return
     */
    public static String getDateEndOfPreviousTrim(String date) {

        String result = null;
        if (!JadeStringUtil.isIntegerEmpty(date)) {
            result = date;
            for (int i = 0; i < 3; i++) {
                result = AFUtil.getDateEndOfPreviousMonth(result);
                int mois = Integer.parseInt(result.substring(3, 5));
                if ((mois == 3) || (mois == 6) || (mois == 9) || (mois == 12)) {
                    break;
                }
            }
        } else {
            result = date;
        }
        return result;
    }

    /**
     * Retourne le dernier jour de l'année précédente.
     * 
     * @param date
     *            DD.MM.YYYY
     * @return
     */
    public static String getDateEndOfPreviousYear(String date) {

        String result = null;
        if (!JadeStringUtil.isIntegerEmpty(date)) {

            int annee = Integer.parseInt(date.substring(6));
            result = "31.12." + Integer.toString(annee - 1);
        } else {
            result = date;
        }
        return result;
    }

    /**
     * Retourne le dernier jour du Trimestre en cours.
     * 
     * @param date
     *            DD.MM.YYYY
     * @return
     */
    public static String getDateEndOfTrim(String date) {

        String result = null;
        if (!JadeStringUtil.isIntegerEmpty(date)) {
            result = AFUtil.getDateEndOfMonth(date);
            for (int i = 0; i < 3; i++) {
                int mois = Integer.parseInt(result.substring(3, 5));
                if ((mois == 3) || (mois == 6) || (mois == 9) || (mois == 12)) {
                    break;
                } else {
                    result = AFUtil.getDateEndOfNextMonth(result);
                }
            }
        } else {
            result = date;
        }
        return result;
    }

    /**
     * Retourne le dernier jour de l'année en cours.
     * 
     * @param date
     *            DD.MM.YYYY
     * @return
     */
    public static String getDateEndOfYear(String date) {

        String result = null;
        if (!JadeStringUtil.isIntegerEmpty(date)) {
            result = "31.12." + date.substring(6);
        } else {
            result = date;
        }
        return result;
    }

    /**
     * Retourne le jour précedent.
     * 
     * @param date
     *            DD.MM.YYYY
     * @return la date pour le jour précédent.
     */
    public static String getDatePreviousDay(String date) {

        String result = null;
        if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(date)) {
            int jour = Integer.parseInt(date.substring(0, 2));

            jour -= 1;

            if (jour == 0) {
                result = AFUtil.getDateEndOfPreviousMonth(date);
            } else {
                if (jour < 10) {
                    result = "0" + Integer.toString(jour) + date.substring(2);
                } else {
                    result = Integer.toString(jour) + date.substring(2);
                }
            }
        } else {
            result = date;
        }
        return result;
    }

    /**
     * Calcule une date de retour qui est 14 jours après la date donnée. Si la date calculée est un jour non ouvrable,
     * le prochain jour ouvrable est retourné
     * 
     * @param dateEnvoi
     *            la date de référence
     * @return la date de retour calculée ou null si impossible
     */
    public static String getDateRetour14(String dateEnvoi) {
        try {
            JACalendarGregorian cal = new JACalendarGregorian();
            JADate result = cal.addDays(new JADate(dateEnvoi), 14);
            result = cal.getNextWorkingDay(result);
            return result.toStr(".");
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * Retourne la date de situation à utiliser pour les recherches sur l'affiliation ou ses attributs.
     * 
     * @param affToUse
     *            l'affiliation concernée
     * @return la date du jour ou la date de radiation si affiliation radiée ou la date de début si affiliation créée
     *         dans le futur
     */
    public static String getDateSituation(AFAffiliation affToUse) {
        if (affToUse == null) {
            return "";
        }
        if (!JadeStringUtil.isEmpty(affToUse.getDateFin())) {
            // si radié, utiliser date de fin
            return affToUse.getDateFin();
        }
        // sinon date du jour ou début d'affiliation si > date du jour
        String today = JACalendar.todayJJsMMsAAAA();
        try {
            if (BSessionUtil.compareDateFirstGreater(affToUse.getSession(), affToUse.getDateDebut(), today)) {
                return affToUse.getDateDebut();
            } else {
                return today;
            }
        } catch (Exception ex) {
            return today;
        }
    }

    /**
     * Retour le TAG HTML options avec les Plan d'affiliation possible pour une cotisation, le plan actuelle étant
     * séléctionné.
     * 
     * @param affiliationId
     * @param planAffiliationId
     * @param bsession
     * @return
     */
    public static String getPlanAffiliation(String affiliationId, String planAffiliationId, BSession bsession) {
        return AFUtil.getPlanAffiliation(affiliationId, planAffiliationId, bsession, false);
    }

    /**
     * Retour le TAG HTML options avec les Plan d'affiliation possible pour une cotisation, le plan actuelle étant
     * séléctionné.
     * 
     * @param affiliationId
     * @param planAffiliationId
     * @param bsession
     * @param vide
     * @return
     */
    public static String getPlanAffiliation(String affiliationId, String planAffiliationId, BSession bsession,
            boolean vide) {
        StringBuffer options = new StringBuffer();
        if (!JadeStringUtil.isIntegerEmpty(affiliationId)) {
            try {
                if (vide) {
                    options.append("<option value=\"\"></option> ");
                }
                AFPlanAffiliationManager manager = new AFPlanAffiliationManager();
                manager.setSession(bsession);
                manager.setForAffiliationId(affiliationId);
                manager.find();

                for (int i = 0; i < manager.size(); i++) {
                    AFPlanAffiliation planAffiliation = (AFPlanAffiliation) manager.getEntity(i);

                    options.append("<option ");
                    if (planAffiliation.getPlanAffiliationId().equals(planAffiliationId) || (manager.size() == 1)) {
                        options.append("selected ");
                    }
                    options.append("value=\"" + planAffiliation.getPlanAffiliationId() + "\">");
                    options.append(planAffiliation.getLibelleFactureNotEmpty());

                    options.append("</option>");
                }
            } catch (Exception ex) {
                JadeLogger.error(null, ex);
            }
        }
        return options.toString();
    }

    /**
     * Retour le TAG HTML options avec les Plan d'affiliation possible pour une cotisation, le plan actuelle étant
     * séléctionné.
     * 
     * @param affiliationId
     * @param planAffiliationId
     * @param session
     * @return
     */
    public static String getPlanAffiliation(String affiliationId, String planAffiliationId, HttpSession session) {
        return AFUtil.getPlanAffiliation(affiliationId, planAffiliationId,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * Retour le TAG HTML options avec les Plan d'affiliation possible pour une cotisation, le plan actuelle étant
     * séléctionné.
     * 
     * @param affiliationId
     * @param planAffiliationId
     * @param session
     * @return
     */
    public static String getPlanAffiliation(String affiliationId, String planAffiliationId, HttpSession session,
            boolean vide) {
        return AFUtil.getPlanAffiliation(affiliationId, planAffiliationId,
                (BSession) ((FWController) session.getAttribute("objController")).getSession(), vide);
    }

    public static String getPlanAffiliationInfoRom280(String affiliationId, String planAffiliationId,
                                                      BSession bsession, boolean vide) {
        return getPlanAffiliationInfoRom280(affiliationId, planAffiliationId, bsession, vide, false);
    }

    public static String getPlanAffiliationInfoRom280(String affiliationId, String planAffiliationId,
            BSession bsession, boolean vide, boolean filtreInactif) {
        StringBuffer options = new StringBuffer();
        if (!JadeStringUtil.isIntegerEmpty(affiliationId)) {
            try {
                if (vide) {
                    options.append("<option value=\"\"></option> ");
                }
                AFPlanAffiliationManager manager = new AFPlanAffiliationManager();
                manager.setSession(bsession);
                manager.setForAffiliationId(affiliationId);
                if(filtreInactif) {
                    manager.setForPlanActif(true);
                }
                manager.setOrder("MUIPLA DESC");
                manager.find();

                for (int i = 0; i < manager.size(); i++) {
                    AFPlanAffiliation planAffiliation = (AFPlanAffiliation) manager.getEntity(i);

                    // Recherche de la date début la plus petite de cotisation utilisant ce plan
                    String periode = "(" + bsession.getLabel("EVENTAIL_REGIME_AUCUNE_COTI") + ")";

                    AFCotisationManager cotiMng = new AFCotisationManager();
                    cotiMng.setSession(bsession);
                    cotiMng.setForPlanAffiliationId(planAffiliation.getPlanAffiliationId());
                    cotiMng.setTypeAffichage(AFCotisationManager.AFFICHAGE_RESUME_DATE);
                    cotiMng.setOrder("MEDDEB"); // Passer un order pour ne pas qu'il mette celui par défaut et qui dans
                                                // ce cas de figure fait planté
                    cotiMng.find();
                    // il y a forcement une ligne à cause des min ou max de la requête
                    AFCotisation coti = (AFCotisation) cotiMng.getFirstEntity();
                    // Si il n'y a rien le min ou max retourne une ligne à vide, ne pas prendre (pas de coti)
                    if (!JadeStringUtil.isEmpty(coti.getDateDebut()) || !JadeStringUtil.isEmpty(coti.getDateFin())
                            || !JadeStringUtil.isEmpty(coti.getDateFinMin())) {
                        periode = "(" + coti.getDateDebut() + " - ";
                        if (!JadeStringUtil.isIntegerEmpty(coti.getDateFinMin())) {
                            periode += coti.getDateFin();
                        } else {
                            periode += "0";
                        }
                        periode += ")";
                    }

                    options.append("<option ");
                    if (planAffiliation.getPlanAffiliationId().equals(planAffiliationId) || (manager.size() == 1)) {
                        options.append("selected ");
                    }
                    options.append("value=\"" + planAffiliation.getPlanAffiliationId() + "\">");
                    options.append(planAffiliation.getLibelle() + " " + periode);

                    options.append("</option>");
                }
            } catch (Exception ex) {
                JadeLogger.error(null, ex);
            }
        }
        return options.toString();
    }

    /*
     * Method unformat. Oter le formatage du numéro AVS qui contient des "."
     * 
     * @param value
     * 
     * @return String
     * 
     * @throws Exception
     */
    /*
     * public static String unformat(String value) throws Exception { if (value == null) { return ""; } StringBuffer str
     * = new StringBuffer(value.length());
     * 
     * for (int i = 0;i<value.length();i++) { if (value.charAt(i)!= '.') { str.append(value.charAt(i)); } } return
     * str.toString(); }
     */

    public static String getPlanAffiliationInfoRom280(String affiliationId, String planAffiliationId,
            HttpSession session, boolean vide) {
        return AFUtil.getPlanAffiliationInfoRom280(affiliationId, planAffiliationId,
                (BSession) ((FWController) session.getAttribute("objController")).getSession(), vide);
    }

    public static String getPlanAffiliationInfoRom280FitreInactif(String affiliationId, String planAffiliationId,
                                                      HttpSession session, boolean vide) {
        return AFUtil.getPlanAffiliationInfoRom280(affiliationId, planAffiliationId,
                (BSession) ((FWController) session.getAttribute("objController")).getSession(), vide, true);
    }

    /**
     * Retourne une liste de Tiers en fonction du Code d'administration pour les genres "Caisse Professionnel".
     * 
     * @return la liste d'options (tag select) de Tiers. Peut-être vide si aucune information n'a été trouvée
     * @param like
     *            - les premiers chiffres du numéro du Code d'administration
     * @param bsession
     *            - la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getPlanCaisseTiers(String like, BSession bsession) {
        if (JadeStringUtil.isBlank(like)) {
            like = "";
        }

        StringBuffer options = new StringBuffer();
        HashSet<String> affSet = new HashSet<String>();
        try {
            TIAdministrationManager manager = new TIAdministrationManager();
            manager.setSession(bsession);
            manager.setForCodeAdministrationLike(like);
            manager.setForGenreAdministration(CodeSystem.GENRE_ADMIN_CAISSE_PROF);
            manager.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < manager.size(); i++) {

                TIAdministrationViewBean admin = (TIAdministrationViewBean) manager.getEntity(i);
                if (!affSet.contains(admin.getIdTiers())) {
                    options.append("<option value=\"");
                    options.append(admin.getCodeAdministration());
                    options.append(" - ");
                    options.append(admin.getNom());
                    options.append("\" idTiers='");
                    options.append(admin.getIdTiers());
                    options.append("'");
                    options.append(">");
                    options.append(admin.getCodeAdministration());
                    options.append(" - ");
                    options.append(admin.getNom());
                    options.append("</option>");
                    affSet.add(admin.getIdTiers());
                }
            }
        } catch (Exception ex) {
            JadeLogger.error(null, ex);
        }
        return options.toString();
    }

    /**
     * Retourne une liste de Tiers en fonction du Code d'administration pour les genres "Caisse Professionnel".
     * 
     * @return la liste d'options (tag select) de Tiers. Peut-être vide si aucune information n'a été trouvée
     * @param like
     *            - les premiers chiffres du numéro du Code d'administration
     * @param session
     *            - la session HTTP actuelle
     */
    public static String getPlanCaisseTiers(String like, HttpSession session) {
        return AFUtil.getPlanCaisseTiers(like,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    public static String getReviseur(String like, BSession bsession) {
        if (JadeStringUtil.isBlank(like)) {
            like = "";
        }
        StringBuffer options = new StringBuffer();
        HashSet<String> affSet = new HashSet<String>();
        try {
            AFReviseurManager manager = new AFReviseurManager();
            manager.setSession(bsession);
            manager.setLikeVisa(like);
            manager.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < manager.size(); i++) {
                AFReviseur reviseur = (AFReviseur) manager.getEntity(i);

                if (!affSet.contains(reviseur.getVisa())) {
                    options.append("<option value='");
                    options.append(reviseur.getVisa());
                    options.append("' nomReviseur='");
                    options.append(reviseur.getNomReviseur());
                    options.append("'");
                    options.append(">");
                    options.append(reviseur.getVisa());
                    options.append(" - ");
                    options.append(reviseur.getNomReviseur());
                    options.append("</option>");
                    affSet.add(reviseur.getVisa());
                }
            }
            if (manager.size() == 0) {
                options.append("<option value='");
                options.append(like);
                options.append("' nomReviseur=''");
                options.append("'");
                options.append(">");
                options.append(like);
                options.append("</option>");
            }
        } catch (Exception ex) {
            JadeLogger.error(null, ex);
        }
        return options.toString();
    }

    public static String getReviseur(String like, HttpSession session) {
        return AFUtil.getReviseur(like, (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * Retourne une liste Rubrique Comptable (idExterne et Description) en fonction des permiers chiffres (min 3) de
     * l'idExterne.
     * 
     * @return la liste d'options (tag select) des rubrique comptable. Peut-être vide si aucune information n'a été
     *         trouvée
     * @param like
     *            - les premiers chiffres du numéro de rubrique comptable
     * @param bsession
     *            - la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getRubriqueComptable(String like, BSession bsession) {
        if (JadeStringUtil.isBlank(like)) {
            like = "";
        }

        StringBuffer options = new StringBuffer();
        HashSet<String> affSet = new HashSet<String>();
        try {
            CARubriqueManager manager = new CARubriqueManager();
            manager.setSession(bsession);
            manager.setFromNumero(like);
            manager.setOrderBy(CARubriqueManager.ORDER_IDEXTERNE);
            manager.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < manager.size(); i++) {
                CARubrique rub = (CARubrique) manager.getEntity(i);
                if (!affSet.contains(rub.getIdRubrique())) {

                    options.append("<option value='");
                    options.append(rub.getIdExterne());
                    options.append("' nom='");
                    options.append(rub.getIdExterne());
                    options.append("' idRubrique='");
                    options.append(rub.getIdRubrique());
                    options.append("'");
                    options.append(">");
                    options.append(rub.getIdExterne());
                    options.append(" - ");
                    options.append(rub.getDescription(bsession.getIdLangueISO()));
                    options.append("</option>");
                    affSet.add(rub.getIdRubrique());
                }
            }
        } catch (Exception ex) {
            JadeLogger.error(null, ex);
        }
        return options.toString();
    }

    /**
     * Retourne une liste Rubrique Comptable (idExterne et Description) en fonction des permiers chiffres (min 3) de
     * l'idExterne.
     * 
     * @return la liste d'options (tag select) des rubrique comptable. Peut-être vide si aucune information n'a été
     *         trouvée
     * @param like
     *            - les premiers chiffres du numéro de rubrique comptable
     * @param session
     *            - la session HTTP actuelle
     */
    public static String getRubriqueComptable(String like, HttpSession session) {
        return AFUtil.getRubriqueComptable(like,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * Retourne la plus petite periodicité (mensuelle < trimestrielle < annuelle)
     * 
     * @param la
     *            première périodicité à comparer
     * @param la
     *            deuxième périodicité à comparer
     * @return la périodicité la plus petite ou null si les deux périodicités à comparer sont null
     */
    public static String getSmallerPeriode(String per1, String per2) {
        if (!JadeStringUtil.isIntegerEmpty(per1)) {
            if (!JadeStringUtil.isIntegerEmpty(per2)) {
                if (Integer.parseInt(per1.substring(5)) < Integer.parseInt(per2.substring(5))) {
                    return per1;
                } else {
                    return per2;
                }
            }
            return per1;
        } else if (!JadeStringUtil.isIntegerEmpty(per2)) {
            return per2;
        }
        return null;
    }

    public static String giveNumeroAffilieNonFormate(String theNumeroAffilie) {

        try {
            IFormatData affilieFormater = ((AFApplication) GlobazServer.getCurrentSystem().getApplication(
                    AFApplication.DEFAULT_APPLICATION_NAOS)).getAffileFormater();
            return affilieFormater.unformat(theNumeroAffilie);
        } catch (Exception e) {
            JadeLogger.warn(AFUtil.class.getName(), "Unable to unformat numeroAffilie : " + e.getMessage());
        }

        return theNumeroAffilie;
    }

    public static AFParametreAssurance giveParametreAssurance(String idParametre, String idAssurance, String date,
            BSession session) throws Exception {

        AFParametreAssuranceManager mgrParametreAssurance = new AFParametreAssuranceManager();
        mgrParametreAssurance.setSession(session);
        mgrParametreAssurance.setForIdAssurance(idAssurance);
        mgrParametreAssurance.setForGenre(idParametre);
        mgrParametreAssurance.setForDate(date);
        mgrParametreAssurance.setOrderByDateDebutDesc();
        mgrParametreAssurance.find(BManager.SIZE_NOLIMIT);

        if (mgrParametreAssurance.size() > 0) {
            return (AFParametreAssurance) mgrParametreAssurance.getFirstEntity();
        }

        return null;
    }

    /**
     * Méthode qui retourne une valeur paramètre par rapport au sexe
     * 
     * @param idParametre
     * @param idAssurance
     * @param date
     * @param sexe
     * @param session
     * @return
     * @throws Exception
     */
    public static AFParametreAssurance giveParametreAssurance(String idParametre, String idAssurance, String date,
            String sexe, BSession session) throws Exception {

        AFParametreAssuranceManager mgrParametreAssurance = new AFParametreAssuranceManager();
        mgrParametreAssurance.setSession(session);
        mgrParametreAssurance.setForIdAssurance(idAssurance);
        mgrParametreAssurance.setForGenre(idParametre);
        mgrParametreAssurance.setForDate(date);
        mgrParametreAssurance.setForSexe(sexe);
        mgrParametreAssurance.setOrderByDateDebutDesc();
        mgrParametreAssurance.find(BManager.SIZE_NOLIMIT);

        if (mgrParametreAssurance.size() > 0) {
            return (AFParametreAssurance) mgrParametreAssurance.getFirstEntity();
        }

        return null;
    }

    /**
     * Return true si le type d'affiliation est paritaire (804002,804005,804010,804012)
     * 
     * @param typeAffiliation
     * @return
     */
    public static boolean isAffiliationParitaire(String typeAffiliation) {
        if (typeAffiliation.equals(globaz.naos.translation.CodeSystem.TYPE_AFFILI_EMPLOY)
                || typeAffiliation.equals(globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP_EMPLOY)
                || typeAffiliation.equals(globaz.naos.translation.CodeSystem.TYPE_AFFILI_EMPLOY_D_F)
                || typeAffiliation.equals(globaz.naos.translation.CodeSystem.TYPE_AFFILI_LTN)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retourne true si le type d'affiliation est personnel (804001,804005,804011,804008,804004,804006)
     * 
     * @param typeAffiliation
     * @return
     */
    public static boolean isAffiliationPersonnelle(String typeAffiliation) {
        if (typeAffiliation.equals(globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP)
                || typeAffiliation.equals(globaz.naos.translation.CodeSystem.TYPE_AFFILI_NON_ACTIF)
                || typeAffiliation.equals(globaz.naos.translation.CodeSystem.TYPE_AFFILI_SELON_ART_1A)
                || typeAffiliation.equals(globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP_EMPLOY)
                || typeAffiliation.equals(globaz.naos.translation.CodeSystem.TYPE_AFFILI_TSE)
                || typeAffiliation.equals(globaz.naos.translation.CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAgeAVS(String sexe, String dateNaissance, int annee) throws Exception {
        int anneeNaissance = globaz.globall.util.JACalendar.getYear(dateNaissance);
        int ageAvs = AFUtil.calculerAgeAvs(sexe, anneeNaissance);
        if (ageAvs == annee) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNouveauControleEmployeur(BSession session) {
        try {
            AFApplication application = (AFApplication) session.getApplication();
            return application.isNouveauControleEmployeur();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isRentier(String sexe, String dateNaissance) throws Exception {
        int annee = JACalendar.getYear(JACalendar.todayJJsMMsAAAA());
        return AFUtil.isRentierForAnnee(sexe, dateNaissance, annee);
    }

    public static boolean isRentierForAnnee(String sexe, String dateNaissance, int annee) throws Exception {
        int anneeNaissance = globaz.globall.util.JACalendar.getYear(dateNaissance);
        int ageAvs = AFUtil.calculerAgeAvs(sexe, anneeNaissance);
        if (ageAvs <= annee) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Determine le nombre de mois inclus dans un période donnée
     * 
     * du 01.01.2004 au 02.01.2004 => 1 mois (01) du 01.01.2004 au 31.01.2004 => 1 mois (01) du 31.01.2004 au 01.02.2004
     * => 2 mois (01, 02) du 01.12.2004 au 01.02.2005 => 3 mois (12, 01, 02)
     * 
     * @param session
     * @param dateDebutFact
     *            DD.MM.YYYY
     * @param dateFinFact
     *            DD.MM.YYYY
     * @return le nombre de mois
     * @throws Exception
     */
    public static int nbMoisPeriode(BSession session, String debutPeriode, String finPeriode) throws Exception {

        int nbMois = 0;
        if (BSessionUtil.compareDateFirstLower(session, debutPeriode, finPeriode)) {

            int nbAnnee = Integer.parseInt(finPeriode.substring(6)) - Integer.parseInt(debutPeriode.substring(6));
            nbMois = (Integer.parseInt(finPeriode.substring(3, 5)) - Integer.parseInt(debutPeriode.substring(3, 5)))
                    + 1 + (nbAnnee * 12);
            if (nbMois < 0) {
                nbMois = 0;
            }
        }
        return nbMois;
    }

    private static String plafonneComplement(String theComplement, AFAssurance entityAssurance,
            AFParametreAssurance plafond, String theDate, String theIdCompteAnnexe, BSession session) throws Exception {

        // Récupération des rubriques AVS
        AFAssuranceManager mgrAssuranceAVS = new AFAssuranceManager();
        mgrAssuranceAVS.setSession(session);
        mgrAssuranceAVS.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
        mgrAssuranceAVS.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
        mgrAssuranceAVS.find(BManager.SIZE_NOLIMIT);

        List<String> listRubriqueAVS = new ArrayList<String>();
        for (int i = 0; i < mgrAssuranceAVS.size(); i++) {
            AFAssurance entityAssuranceAVS = (AFAssurance) mgrAssuranceAVS.getEntity(i);
            listRubriqueAVS.add(entityAssuranceAVS.getRubriqueId());
        }

        double masseAVSFromCompteurs = 0;
        if (!JadeStringUtil.isBlankOrZero(theIdCompteAnnexe)) {

            // Récupération des compteurs de l'affilié pour l'année donnée
            CACompteurManager mgrCompteur = new CACompteurManager();
            mgrCompteur.setSession(session);
            mgrCompteur.setForIdCompteAnnexe(theIdCompteAnnexe);
            mgrCompteur.setForAnnee(String.valueOf(JACalendar.getYear(theDate)));
            mgrCompteur.find(BManager.SIZE_NOLIMIT);

            // Calcul de la masse AVS en cumulant la masse des compteurs AVS
            boolean compteurAVSFound = false;
            for (int i = 0; i < mgrCompteur.size(); i++) {
                CACompteur entityCompteur = (CACompteur) mgrCompteur.getEntity(i);

                if (listRubriqueAVS.contains(entityCompteur.getIdRubrique())) {
                    masseAVSFromCompteurs = masseAVSFromCompteurs
                            + Double.valueOf(entityCompteur.getCumulMasse()).doubleValue();
                    compteurAVSFound = true;
                }
            }

            if (!compteurAVSFound) {
                // throw new Exception("unable to plafonne complement because no compteur AVS found");
                masseAVSFromCompteurs = 0;
            }

            if (masseAVSFromCompteurs < 0) {
                throw new Exception("unable to plafonne complement because masse AVS < 0");
            }
        }

        double theComplementDouble = Double.valueOf(JANumberFormatter.deQuote(theComplement)).doubleValue();
        double plafondDouble = Double.valueOf(JANumberFormatter.deQuote(plafond.getValeurNum())).doubleValue();

        if (masseAVSFromCompteurs < plafondDouble) {
            return AFUtil.plafonneComplementWithMasseAVSInferieurPlafond(masseAVSFromCompteurs, theComplementDouble,
                    plafondDouble);
        } else {
            return AFUtil.plafonneComplementWithMasseAVSSuperieurEgalPlafond(masseAVSFromCompteurs,
                    theComplementDouble, plafondDouble);
        }

    }

    private static String plafonneComplementWithMasseAVSInferieurPlafond(double theMasseAVS, double theComplement,
            double thePlafond) throws Exception {

        if ((theComplement <= 0) || ((theMasseAVS + theComplement) <= thePlafond)) {
            return JANumberFormatter.fmt(Double.toString(theComplement), false, false, false, 2);
        } else {
            return JANumberFormatter.fmt(Double.toString(thePlafond - theMasseAVS), false, false, false, 2);
        }
    }

    private static String plafonneComplementWithMasseAVSSuperieurEgalPlafond(double theMasseAVS, double theComplement,
            double thePlafond) throws Exception {

        if ((theComplement >= 0) || ((theMasseAVS + theComplement) >= thePlafond)) {
            return "0";
        } else {
            return JANumberFormatter.fmt(Double.toString((theMasseAVS + theComplement) - thePlafond), false, false,
                    false, 2);
        }
    }

    public static double plafonneMasse(double theMasse, String typeReleve, String assuranceId, String theDate,
            BSession session, String theIdCompteAnnexe) {
        return Double.valueOf(
                AFUtil.plafonneMasse(JANumberFormatter.fmt(Double.toString(theMasse), false, false, false, 2),
                        typeReleve, assuranceId, theDate, session, theIdCompteAnnexe, null)).doubleValue();
    }

    public static String plafonneMasse(String theMasse, String assuranceId, String theDate, BSession session,
            String theIdCompteAnnexe, String typeDS) {
        return AFUtil.plafonneMasse(theMasse, null, assuranceId, theDate, session, theIdCompteAnnexe, typeDS);
    }

    public static String plafonneMasse(String theMasse, String typeReleve, String assuranceId, String theDate,
            BSession session, String theIdCompteAnnexe) {
        return AFUtil.plafonneMasse(theMasse, typeReleve, assuranceId, theDate, session, theIdCompteAnnexe, null);
    }

    public static String plafonneMasse(String theMasse, String typeReleve, String assuranceId, String theDate,
            BSession session, String theIdCompteAnnexe, String typeDS) {

        try {

            theMasse = JANumberFormatter.deQuote(theMasse);

            // validation des arguments reçus en paramètres
            StringBuffer wrongArgumentsBuffer = new StringBuffer();

            if (JadeStringUtil.isBlankOrZero(assuranceId)) {
                wrongArgumentsBuffer.append("assuranceId (" + assuranceId + ") is blank or zero / ");
            }

            if (session == null) {
                wrongArgumentsBuffer.append("session (" + session + ") is null / ");
            }

            if (wrongArgumentsBuffer.length() >= 1) {
                throw new Exception("unable to plafonne masse due to wrong arguments : "
                        + wrongArgumentsBuffer.toString());
            }

            AFAssurance entityAssurance = new AFAssurance();
            entityAssurance.setSession(session);
            entityAssurance.setAssuranceId(assuranceId);
            entityAssurance.retrieve();

            if ((entityAssurance == null) || entityAssurance.isNew()) {
                // Préventif, ne devrait jammais arriver...
                throw new Exception("unable to plafonne masse because can't find assurance");
            }

            if (!JadeNumericUtil.isZeroValue(theMasse) && !JadeStringUtil.isBlank(theMasse)
                    && CodeSystem.GENRE_ASS_PARITAIRE.equalsIgnoreCase(entityAssurance.getAssuranceGenre())
                    && !CodeSystem.TYPE_RELEVE_PERIODIQUE.equalsIgnoreCase(typeReleve)
                    && !CodeSystem.TYPE_RELEVE_COMPLEMENT.equalsIgnoreCase(typeReleve)) {

                if (!new JACalendarGregorian().isValid(theDate)) {
                    wrongArgumentsBuffer.append("theDate (" + theDate + ") is not a date / ");
                }

                if (wrongArgumentsBuffer.length() >= 1) {
                    throw new Exception("unable to plafonne masse due to wrong arguments : "
                            + wrongArgumentsBuffer.toString());
                }

                AFParametreAssurance plafond = AFUtil.giveParametreAssurance(CodeSystem.GEN_PARAM_ASS_PLAFOND,
                        assuranceId, theDate, session);

                if (plafond != null) {

                    if (!JadeNumericUtil.isNumeric(JANumberFormatter.deQuote(theMasse))) {
                        wrongArgumentsBuffer.append("theMasse (" + theMasse + ") is not numeric / ");
                    }

                    if (JadeStringUtil.isBlankOrZero(typeReleve) && JadeStringUtil.isBlankOrZero(typeDS)) {
                        wrongArgumentsBuffer.append("typeReleve and typeDS can't be null both / ");
                    }

                    if (wrongArgumentsBuffer.length() >= 1) {
                        throw new Exception("unable to plafonne masse due to wrong arguments : "
                                + wrongArgumentsBuffer.toString());
                    }

                    if (Double.valueOf(JANumberFormatter.deQuote(plafond.getValeurNum())).doubleValue() < 0) {
                        throw new Exception("unable to plafonne masse because plafond < 0");
                    }

                    if (CodeSystem.TYPE_RELEVE_CONTROL_EMPL.equalsIgnoreCase(typeReleve)
                            || CodeSystem.TYPE_RELEVE_RECTIF.equalsIgnoreCase(typeReleve)
                            || (!JadeStringUtil.isBlankOrZero(typeDS)
                                    && !DSDeclarationViewBean.CS_LTN.equalsIgnoreCase(typeDS)
                                    && !DSDeclarationViewBean.CS_BOUCLEMENT_ACOMPTE.equalsIgnoreCase(typeDS) && !DSDeclarationViewBean.CS_PRINCIPALE
                                        .equalsIgnoreCase(typeDS))) {

                        // if (JadeStringUtil.isBlankOrZero(theIdCompteAnnexe)) {
                        // wrongArgumentsBuffer.append("theIdCompteAnnexe (" + theIdCompteAnnexe
                        // + ") is blank or zero / ");
                        // }

                        if (wrongArgumentsBuffer.length() >= 1) {
                            throw new Exception("unable to plafonne masse due to wrong arguments : "
                                    + wrongArgumentsBuffer.toString());
                        }

                        // Dans le cas d'un relevé de type contrôle d'employeur(17) ou complément(18)
                        // La masse saisie par l'utilisateur dans le relevé correspond en fait à un
                        // complément d'une masse saisie dans un précédent relevé
                        return AFUtil.plafonneComplement(theMasse, entityAssurance, plafond, theDate,
                                theIdCompteAnnexe, session);

                    } else {
                        if (Double.valueOf(theMasse).doubleValue() >= Double.valueOf(
                                JANumberFormatter.deQuote(plafond.getValeurNum())).doubleValue()) {
                            return JANumberFormatter.fmt(JANumberFormatter.deQuote(plafond.getValeurNum()), false,
                                    false, false, 2);
                        }

                    }
                }

            }
        } catch (Exception e) {
            JadeLogger.warn(AFUtil.class.getName(), "unable to plafonne masse : " + e.toString());

            try {
                CACompteAnnexe entityCompteAnnexe = new CACompteAnnexe();
                String theCompteAnnexeDescription = "";

                try {
                    entityCompteAnnexe.setSession(session);
                    entityCompteAnnexe.setIdCompteAnnexe(theIdCompteAnnexe);
                    entityCompteAnnexe.retrieve();

                    if ((entityCompteAnnexe != null) && !entityCompteAnnexe.isNew()) {
                        theCompteAnnexeDescription = entityCompteAnnexe.getDescription();
                    }

                } catch (Exception e2) {
                    theCompteAnnexeDescription = "";
                }

                JadeSmtpClient.getInstance().sendMail(
                        session.getUserEMail(),
                        session.getLabel("ERREUR_PLAFONNE_MASSE_EMAIL_SUBJECT"),
                        FWMessageFormat.format(session.getLabel("ERREUR_PLAFONNE_MASSE_EMAIL_BODY"),
                                theCompteAnnexeDescription), null);
            } catch (Exception e2) {
                JadeLogger.warn(AFUtil.class.getName(),
                        "unable to send mail after a plafonne masse exception : " + e2.toString());
            }

        }

        return theMasse;

    }

    public static FWCurrency plancheMasse(FWCurrency theMasse, String assuranceId, String theDate, BSession session)
            throws Exception {

        AFParametreAssurance plancher = AFUtil.giveParametreAssurance(CodeSystem.GEN_PARAM_ASS_PLANCHER, assuranceId,
                theDate, session);

        if ((plancher != null)
                && (theMasse.doubleValue() < Double.valueOf(JANumberFormatter.deQuote(plancher.getValeurNum()))
                        .doubleValue())) {
            return new FWCurrency("0");
        }

        return theMasse;
    }

    /**
     * Recherche si il existe au moins une assurance correspondant au critères de recherche.
     * 
     * @param session
     * @param forAffilieNumero
     *            Numéro d'affilié, Obligatoire
     * @param forIdTiers
     *            Id Tiers, Obligatoire
     * @param forTypeAssurance
     *            Type d'assurance (AF, AVS, ...voir Code Système), Obligatoire
     * @param forGenreAssurance
     *            Genre d'assurance (paritaire, personnel, ...voir Code Système), Obligatoire
     * @param forDate
     *            La date de référence, Obligatoire
     * @param forCanton
     *            Le Canton, Facultatif
     * @return true, si il existe au moins une assurance - false, si il n'y a aucune assurance
     * @throws Exception
     */
    public static boolean rechercherAssurance(String forAffilieNumero, String forIdTiers, String forTypeAssurance,
            String forGenreAssurance, String forDate, String forCanton) throws Exception {

        BIApplication remoteApplication = GlobazSystem.getApplication("NAOS");
        BISession sessionNaos = remoteApplication.newSession();

        boolean result = false;
        AFRechercherAssuranceManager manager = new AFRechercherAssuranceManager();
        manager.setISession(sessionNaos);
        manager.setForAffilieNumero(forAffilieNumero);
        manager.setForIdTiers(forIdTiers);
        manager.setForTypeAssurance(forTypeAssurance);
        manager.setForGenreAssurance(forGenreAssurance);
        manager.setForDate(forDate);
        manager.setForCanton(forCanton);
        manager.find();

        if (manager.size() > 0) {
            result = true;
        }
        return result;
    }

    /**
     * Permet la récupération d'un tiers par son identifiant (un Tiers de type TITiersViewBean)
     * 
     * @param session
     * @param idTiers
     * @return
     */
    public static TITiersViewBean retrieveTiersViewBean(BSession session, String idTiers) throws Exception {

        if (session == null) {
            throw new Exception("Unabled to retrieve TITiersViewBean, session is null");
        }

        if (JadeStringUtil.isIntegerEmpty(idTiers)) {
            throw new Exception("Unabled to retrieve TITiersViewBean, idTiers is null or empty");
        }

        TITiersViewBean tiers = new TITiersViewBean();
        tiers.setSession(session);
        tiers.setIdTiers(idTiers);
        try {
            tiers.retrieve();
        } catch (Exception e) {
            tiers = null;
            throw new Exception("Technical Exception, Unabled to retrieve the tiers viewBean ( idTiers = " + idTiers
                    + ")", e);
        }

        return tiers;
    }

    /**
     * Retourne le nombre de cotisation ouverte pour une période et un plan d'affiliation
     * 
     * @param session
     * @param idPlanAffiliation
     * @param dateDebut
     * @param dateFin
     * @return
     * @throws Exception
     */
    public static int returnNombreCotisationPourPeriode(BSession session, String idPlanAffiliation, String dateDebut,
            String dateFin) throws Exception {

        if (JadeStringUtil.isBlankOrZero(idPlanAffiliation)) {
            throw new Exception(session.getLabel("1420"));
        }
        AFCotisationManager cotiMng = new AFCotisationManager();
        cotiMng.setSession(session);
        cotiMng.setForPlanAffiliationId(idPlanAffiliation);
        cotiMng.setDateDebutLessEqual(dateDebut);
        cotiMng.setDateFinGreaterEqual(dateFin);
        cotiMng.setForDateDifferente(Boolean.TRUE);
        return cotiMng.getCount();
    }

    /**
     * Permet de retourner le nombre d'envoi effectué pour un tiers ou une affiliation
     * 
     * @param session
     * @param idAffiliation
     * @param idTiers
     * @return
     * @throws Exception
     */
    public static int returnNombreEnvoi(BSession session, String idAffiliation, String idTiers) throws Exception {

        if (JadeStringUtil.isBlankOrZero(idAffiliation) && JadeStringUtil.isBlankOrZero(idTiers)) {
            throw new Exception("Insufficient number of arguments");
        }
        LUProvenanceDataSource provenanceCriteres = new LUProvenanceDataSource();
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_TIERS, idTiers);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ROLE, TIRole.CS_AFFILIE);
        provenanceCriteres.addProvenance(ILEConstantes.CS_PARAM_GEN_ID_AFFILIATION, idAffiliation);
        LUJournalListViewBean viewBean = new LUJournalListViewBean();
        viewBean.setSession(session);
        viewBean.setProvenance(provenanceCriteres);
        viewBean.find();
        return viewBean.getSize();

    }

    /**
     * Retourne le nombre de relevé pour un affilié, une entête, un état
     * 
     * @param session
     * @param idEntete
     * @param numAffilie
     * @param etatReleve
     * @return
     * @throws Exception
     */
    public static int returnNombreReleve(BSession session, String idEntete, String numAffilie, String etatReleve)
            throws Exception {

        if (JadeStringUtil.isBlankOrZero(idEntete) && JadeStringUtil.isBlankOrZero(numAffilie)) {
            throw new Exception(session.getLabel("1420"));
        }
        AFApercuReleveManager releveMng = new AFApercuReleveManager();
        releveMng.setSession(session);
        releveMng.setForIdEnteteFacture(idEntete);
        releveMng.setForEtat(etatReleve);
        releveMng.setForAffilieNumero(numAffilie);

        return releveMng.getCount();
    }

    /**
     * @deprecated Utiliser AFUtil.sqlAddCondition(StringBuilder sqlWhere, String condition)
     */
    @Deprecated
    public static void sqlAddCondition(StringBuffer sqlWhere, String condition) {
        if (sqlWhere.length() != 0) {
            sqlWhere.append(" AND ");
        }
        sqlWhere.append(condition);
    }

    /**
     * Permet de créer une condition sql précédée de "AND" si ce n'est pas la premiere
     * 
     * @param sqlWhere
     * @param condition
     */
    public static void sqlAddCondition(StringBuilder sqlWhere, String condition) {
        if (sqlWhere.length() != 0) {
            sqlWhere.append(" AND ");
        }
        sqlWhere.append(condition);
    }

    /**
     * Permet de créer une condition sql de type "fields in (id, id2, id3)" précédé de "AND"
     * 
     * @param strBuff
     * @param fields
     *            le champ
     * @param list
     *            la liste d'id
     */
    public static void sqlAddConditionIn(StringBuffer strBuff, String fields, Collection<String> list) {

        if (strBuff.length() != 0) {
            strBuff.append(" AND ");
        }

        strBuff.append(" " + fields + " in (");

        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String id = it.next();
            strBuff.append(id);
            if (it.hasNext()) {
                strBuff.append(",");
            }
        }

        strBuff.append(") ");
    }

    /**
     * Permet de créer une condition sql de type "fields in (id, id2, id3)" précédé de "AND"
     * 
     * @param strBuff
     * @param fields
     *            le champ
     * @param list
     *            la liste d'id
     */
    public static void sqlAddConditionInString(StringBuffer strBuff, String fields, Collection<String> list) {

        if (strBuff.length() != 0) {
            strBuff.append(" AND ");
        }

        strBuff.append(" " + fields + " in ('");

        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String id = it.next();
            strBuff.append(id);
            if (it.hasNext()) {
                strBuff.append("','");
            }
        }

        strBuff.append("') ");
    }

    /**
     * @deprecated Utiliser plutot AFUtil.sqlAddField(StringBuilder fields, String columnName)
     */
    @Deprecated
    public static void sqlAddField(StringBuffer fields, String columnName) {
        if (!JadeStringUtil.isBlank(fields.toString())) {
            fields.append(",");
        }
        fields.append(columnName);
    }

    /**
     * Permet d'ajouter un champ (fields) précédé d'une virgule si le buffer n'est pas vide
     * 
     * @param fields
     * @param columnName
     * @return
     */
    public static void sqlAddField(StringBuilder fields, String columnName) {
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

    public static String toLangueIso(String langue) {

        if (IConstantes.CS_TIERS_LANGUE_FRANCAIS.equals(langue)) {
            return "FR";
        } else if (IConstantes.CS_TIERS_LANGUE_ALLEMAND.equals(langue)) {
            return "DE";
        } else if (IConstantes.CS_TIERS_LANGUE_ITALIEN.equals(langue)) {
            return "IT";
        }

        return "FR";
    }

    public static String toLangueSimple(String langue) {

        if (IConstantes.CS_TIERS_LANGUE_FRANCAIS.equals(langue)) {
            return "F";
        } else if (IConstantes.CS_TIERS_LANGUE_ALLEMAND.equals(langue)) {
            return "D";
        } else if (IConstantes.CS_TIERS_LANGUE_ITALIEN.equals(langue)) {
            return "I";
        }

        return "F";
    }

    public static String transformMasseToMasseAnnuelle(String theMasse, String thePeriodiciteMasse) throws Exception {

        theMasse = JANumberFormatter.deQuote(theMasse);

        if (!JadeNumericUtil.isNumeric(theMasse)) {
            throw new Exception("unable to transform masse to masse annuelle due to not numeric masse : " + theMasse);
        }

        if (!CodeSystem.PERIODICITE_ANNUELLE.equalsIgnoreCase(thePeriodiciteMasse)
                && !CodeSystem.PERIODICITE_TRIMESTRIELLE.equalsIgnoreCase(thePeriodiciteMasse)
                && !CodeSystem.PERIODICITE_MENSUELLE.equalsIgnoreCase(thePeriodiciteMasse)) {
            throw new Exception("unable to transform masse to masse annuelle due to invalid periodicite : "
                    + thePeriodiciteMasse);
        }

        double transformedMasse = Double.valueOf(theMasse).doubleValue();

        if (CodeSystem.PERIODICITE_MENSUELLE.equalsIgnoreCase(thePeriodiciteMasse)) {
            transformedMasse = transformedMasse * 12;
        } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equalsIgnoreCase(thePeriodiciteMasse)) {
            transformedMasse = transformedMasse * 4;
        }

        return String.valueOf(transformedMasse);

    }

    /**
     * Constructeur d'AFUtil.
     */
    public AFUtil() {
        super();
    }

}
