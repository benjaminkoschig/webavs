package globaz.musca.util;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.framework.controller.FWController;
import globaz.framework.secure.user.FWSecureUserDetail;
import globaz.globall.api.BISession;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BTransaction;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.musca.application.FAApplication;
import globaz.musca.db.facturation.FAEnteteFacture;
import globaz.musca.db.facturation.FAEnteteFactureManager;
import globaz.musca.db.facturation.FAModuleFacturation;
import globaz.musca.db.facturation.FAModuleFacturationManager;
import globaz.musca.db.facturation.FAModulePassageManager;
import globaz.musca.db.facturation.FAPassage;
import globaz.musca.db.facturation.FAPlanFacturation;
import globaz.musca.db.facturation.FAPlanFacturationManager;
import globaz.musca.external.ServicesFacturation;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.processFacturation.AFProcessFacturationViewBean;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.adressecourrier.TIAdresse;
import globaz.pyxis.db.adressepaiement.TIAdressePaiement;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiement;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.HashSet;
import java.util.Vector;
import javax.servlet.http.HttpSession;

/**
 * Classe utilitaire pour PAVO. Date de création : (09.01.2003 15:18:41)
 * 
 * @author: Administrator
 */
public class FAUtil {
    public static final String SPECIALISTE_FACTURATION = "SpecialisteFacturation";

    public static String determineCodePeriodicite(AFProcessFacturationViewBean donneesFacturation) {
        if (donneesFacturation != null) {
            return donneesFacturation.getPeriodiciteCoti();
        }
        return null;
    }

    /**
     * Retourne une liste de numéros et noms d'assurés en fonction des permiers chiffres du numéro avs donnés en
     * paramètres. Date de création : (10.03.2004 09:05:13)
     * 
     * @return le numéro d'affilié des ticket étudiants
     */
    public static int fetchAutoDigitAff(javax.servlet.http.HttpSession session) {
        BSession bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
        try {
            FAApplication application = (FAApplication) bSession.getApplication();
            return application.getAutoDigitAffilie();
        } catch (Exception e) {
            return -1;
        }
    }

    public static void fillDocInfoWithPassageInfo(JadePublishDocumentInfo theDocInfo, FAPassage thePassage) {

        if ((theDocInfo != null) && (thePassage != null) && (!thePassage.isNew())) {
            theDocInfo.setDocumentProperty("id.passage", thePassage.getIdPassage());
            theDocInfo.setDocumentProperty("id.plan.facturation", thePassage.getIdPlanFacturation());
        }
    }

    /**
     * Return le role de l'affilié
     * 
     * @param session
     * @param donneesFacturation
     * @return
     * @throws Exception
     */
    public static String findRoleAffilie(BSession session, AFProcessFacturationViewBean donneesFacturation)
            throws Exception {
        String roleCoti = CodeSystem.ROLE_AFFILIE;
        if (CodeSystem.GENRE_ASS_PERSONNEL.equals(donneesFacturation.getGenreAssurance())) {
            // assurance personnelle
            roleCoti = CaisseHelperFactory.getInstance().getRoleForAffiliePersonnel(session.getApplication());
        }
        if (CodeSystem.GENRE_ASS_PARITAIRE.equals(donneesFacturation.getGenreAssurance())) {
            // assurance personnelle
            roleCoti = CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(session.getApplication());
        }
        return roleCoti;
    }

    /**
     * Retourne l'adresse de courrier du tiers ou une chaine vide Ex: Soit le n° avs, le n° affilié ou le n° de
     * contribuable
     */
    public static java.lang.String getAdressePrincipaleCourrier(javax.servlet.http.HttpSession httpSession,
            String idAdresse) throws Exception {
        if (!globaz.jade.client.util.JadeStringUtil.isBlank(idAdresse)) {
            BISession bsession = globaz.musca.translation.CodeSystem.getSession(httpSession);
            TIAdresse adresse = new TIAdresse();
            adresse.setSession((BSession) bsession);
            try {
                return adresse.getDetailAdresse(idAdresse);
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public static java.lang.String getAdressePrincipalePaiement(javax.servlet.http.HttpSession httpSession,
            String idAdressePaiement) throws Exception {
        if (!globaz.jade.client.util.JadeStringUtil.isBlank(idAdressePaiement)) {
            BISession bsession = globaz.musca.translation.CodeSystem.getSession(httpSession);
            TIAdressePaiement adresse = new TIAdressePaiement();
            adresse.setSession((BSession) bsession);
            try {
                adresse = adresse.getAdressePaiement(adresse.getSession(), idAdressePaiement);
                return adresse.getDetailPaiement(adresse.getSession());
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }

    }

    public static java.lang.String getAdressePrincipalePaiementFromLinkPmt(BSession session, String idAdressePaiement)
            throws Exception {

        if (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(idAdressePaiement)) {
            // BISession bsession =
            // globaz.musca.translation.CodeSystem.getSession(httpSession);

            TIAvoirPaiement lienAdresse = new TIAvoirPaiement();
            lienAdresse.setSession(session);
            lienAdresse.setIdAdrPmtIntUnique(idAdressePaiement);
            lienAdresse.retrieve();

            // Récupérer l'adresse de paiement
            TIAdressePaiement adresse = new TIAdressePaiement();
            adresse.setSession(session);
            if (!lienAdresse.isNew()) {
                adresse.setIdAdressePmtUnique(lienAdresse.getIdAdressePaiement());
                adresse.retrieve();
            }
            return adresse.getDetailPaiement(adresse.getSession());

        } else {
            return "";
        }

    }

    public static java.lang.String getAdressePrincipalePaiementFromLinkPmt(javax.servlet.http.HttpSession httpSession,
            String idAdressePaiement) throws Exception {

        if (!globaz.jade.client.util.JadeStringUtil.isBlank(idAdressePaiement)) {
            BISession bsession = globaz.musca.translation.CodeSystem.getSession(httpSession);

            TIAvoirPaiement lienAdresse = new TIAvoirPaiement();
            lienAdresse.setSession((BSession) bsession);
            lienAdresse.setIdAdrPmtIntUnique(idAdressePaiement);
            lienAdresse.retrieve();

            // Récupérer l'adresse de paiement
            TIAdressePaiement adresse = new TIAdressePaiement();
            adresse.setSession((BSession) bsession);
            if (!lienAdresse.isNew()) {
                adresse.setIdAdressePmtUnique(lienAdresse.getIdAdressePaiement());
                adresse.retrieve();
            }
            return adresse.getDetailPaiement(adresse.getSession());

        } else {
            return "";
        }

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
    public static String getAffilies(String like, javax.servlet.http.HttpSession session) {
        return FAUtil
                .getAffillies(like, (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * Retourne une liste des Affilies (numéro et nom de l'affilié) en fonction des permiers chiffres du numéro affilié
     * donnés en paramètres. Date de création : (17.09.2003 17:032:13)
     * 
     * @return la liste d'options (tag select) des affiliés existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les primers chiffres du numéro affilié
     * @param bsession
     *            la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getAffillies(String like, BSession bsession) {
        if (JadeStringUtil.isBlank(like)) {
            like = "";
        }
        FAApplication faApp = null;
        try {
            faApp = (FAApplication) bsession.getApplication();
        } catch (Exception e) {
            JadeLogger.error(bsession, e);
        }
        try {
            IFormatData affilieFormater = faApp.getAffileFormater();
            if (affilieFormater != null) {
                like = affilieFormater.format(like);
            }
        } catch (Exception e) {
            JadeLogger.error(bsession, e);
        }

        StringBuffer options = new StringBuffer();
        HashSet<String> affSet = new HashSet<String>();
        AFAffiliationManager mng = new AFAffiliationManager();
        try {
            mng.setSession(bsession);
            mng.setLikeAffilieNumero(like);
            mng.find(BManager.SIZE_NOLIMIT);
            for (int i = 0; i < mng.size(); i++) {
                AFAffiliation af = (AFAffiliation) mng.getEntity(i);
                if (!affSet.contains(af.getIdTiers())) {
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
                    options.append("' numAffilieActuel='");
                    options.append(af.getAffilieNumero());
                    options.append("' idAffiliationActuel='");
                    options.append(af.getAffiliationId());
                    options.append("'");
                    options.append(">");
                    options.append(af.getAffilieNumero());
                    options.append(" - ");
                    options.append(ti.getNom());
                    options.append(" - ");
                    options.append(af.getAffilieNumero());
                    options.append("</option>");
                    affSet.add(af.getIdTiers());
                }
            }
        } catch (Exception ex) {
            JadeLogger.error(mng, ex);
        }
        return options.toString();
    }

    /**
     * Renvoye une en-tête de facture selon un n° de passage, un type et un n° de débiteur ainsi qu'un type et n° de
     * facture. Ces cinq paramètres forment une clef unique pour l'en-tête. Si celle-ci n'existe pas encore elle est
     * crée et retournée, sinon l'existante est retournée. Une session et une transaction sont également nécessaires.
     * 
     * @author dch
     * @param numeroPassage
     *            Le numero de passage
     * @param typeDebiteur
     *            Le type de débiteur
     * @param numeroDebiteur
     *            Le numero du débiteur
     * @param typeFacture
     *            Le type de facture
     * @param numeroFacture
     *            Le numero de facture
     * @param session
     *            La session(devrait être une session MUSCA)
     * @param transaction
     *            La transaction
     * @return L'en-tête de facture de type FAEnteteFacture
     * @throws Renvoye
     *             une exception de type Exception si l'entête de facture ne peut pas être retournée
     */
    public static FAEnteteFacture getEnteteFacture(String numeroPassage, String idTiers, String typeDebiteur,
            String numeroDebiteur, String typeFacture, String numeroFacture, BSession session, BTransaction transaction)
            throws Exception {
        // manager pour vérifier et év. charger l'en-tête si elle existe déjà
        FAEnteteFactureManager manager = new FAEnteteFactureManager();

        manager.setSession(session);
        manager.setForIdPassage(numeroPassage);
        manager.setForIdExterneFacture(numeroFacture);
        manager.setForIdExterneRole(numeroDebiteur);
        manager.setForIdTiers(idTiers);
        try {
            // on charge les deux premières en-têtes trouvées selon les critères
            manager.find(transaction, 2);

            // erreur dans la transaction?
            if (transaction.hasErrors()) {
                throw new Exception(transaction.getErrors().toString());
            }
        } catch (Exception e) {
            throw new Exception("Erreur au chargement de l'en-tête: " + e.getMessage());
        }

        // traîtement selon le nombre d'en-têtes trouvées
        switch (manager.size()) {
        // si on a pas encore l'en-tête, il faut la créer puis la retourner
            case 0: {
                // on crée l'en-tête...
                FAEnteteFacture facture = new FAEnteteFacture();

                // ...puis on la paramètre...
                facture.setIdPassage(numeroPassage);
                facture.setIdRole(typeDebiteur);
                facture.setIdExterneRole(numeroDebiteur);
                facture.setIdTypeFacture(typeFacture);
                facture.setIdExterneFacture(numeroFacture);
                facture.setSession(session);
                // DGI init plan
                facture.initDefaultPlanValue(facture.getIdRole());

                // ...et enfin on l'enregistre
                try {
                    facture.add(transaction);

                    // erreur dans la transaction?
                    if (transaction.hasErrors()) {
                        throw new Exception(transaction.getErrors().toString());
                    }
                } catch (Exception e) {
                    throw new Exception("Erreur à l'enregristrement de l'en-tête: " + e.getMessage());
                }

                return facture;
            }

            // si on a une en-tête unique, on la retourne
            case 1:
                return (FAEnteteFacture) manager.getFirstEntity();

                // sinon on a plusieurs en-têtes répondant aux critères, on envoye
                // une erreur
            default:
                throw new Exception("Erreur: l'en-tête n'est pas unique.");
        }
    }

    /**
     * Renvoie idModuleFacturation pour les relevés TODO: créer un module pour les relevés
     */
    public static final String getIdModuleFacuration(BSession session, String typeModule) throws Exception {
        BTransaction transaction = null;
        try {
            transaction = new BTransaction(session);
            transaction.openTransaction();
            return ServicesFacturation.getIdModFacturationByType(session, transaction, typeModule);
        } catch (Exception e) {
            return "";
        } finally {
            if (transaction != null) {
                transaction.closeTransaction();
            }
        }

    }

    public static java.lang.String getLibelleModule(javax.servlet.http.HttpSession httpSession, String id)
            throws Exception {
        if ((!JadeStringUtil.isBlank(id)) && (!id.equalsIgnoreCase("0"))) {
            FAModuleFacturation module = new FAModuleFacturation();
            globaz.globall.api.BISession bsession = globaz.musca.translation.CodeSystem.getSession(httpSession);
            module.setSession((BSession) bsession);
            module.setIdModuleFacturation(id);
            try {
                module.retrieve();
                return module.getLibelle();
            } catch (Exception e) {
                return "Erreur extraction libellé module: " + e.getMessage();
            }
        } else {
            return "";
        }
    }

    /*
     * Retourne le libellé dans la langue de l'utilisateur pour un id.
     */
    public static java.lang.String getLibellePassage(String idPassage, javax.servlet.http.HttpSession httpSession)
            throws Exception {
        if (!JadeStringUtil.isBlank(idPassage)) {
            // Passage de la session Http
            BISession bsession = globaz.musca.translation.CodeSystem.getSession(httpSession);
            return FAPassage.getLibellePassage(idPassage, (BSession) bsession);
        } else {
            return "";
        }
    }

    /*
     * Retourne le libellé dans la langue de l'utilisateur pour un id.
     */
    public static java.lang.String getLibellePlan(javax.servlet.http.HttpSession httpSession, String id)
            throws Exception {
        if (!JadeStringUtil.isBlank(id)) {
            FAPlanFacturation plan = new FAPlanFacturation();
            // Passage de la session Http
            globaz.globall.api.BISession bsession = globaz.musca.translation.CodeSystem.getSession(httpSession);
            plan.setSession((BSession) bsession);
            plan.setIdPlanFacturation(id);
            try {
                plan.retrieve();
                return plan.getLibelle();
            } catch (Exception e) {
                return "Erreur lors de l'extraction du libellé du plan";
            }
        } else {
            return "";
        }
    }

    public static Vector<String[]> getModuleList(javax.servlet.http.HttpSession httpSession, String condition) {
        Vector<String[]> vList = new Vector<String[]>();
        // ajoute un blanc
        String[] list = new String[2];
        list[0] = "";
        list[1] = "";
        vList.add(list);
        FAModuleFacturationManager manager = new FAModuleFacturationManager();
        try {
            manager.setISession(globaz.musca.translation.CodeSystem.getSession(httpSession));
            manager.orderByLibelleLangueSession();
            manager.find();
        } catch (Exception e) {
            return null;
        }
        for (int i = 0; i < manager.size(); i++) {
            list = new String[2];
            FAModuleFacturation entity = (FAModuleFacturation) manager.getEntity(i);

            // Logique d'omission de certains types
            if (condition.equalsIgnoreCase("ALL")) {
                list[0] = entity.getIdModuleFacturation();
                list[1] = entity.getLibelle();
                vList.add(list);
            }
            if ((condition.equalsIgnoreCase("EXCEPT_LISTE"))
                    && (!entity.getIdTypeModule().equalsIgnoreCase(FAModuleFacturation.CS_MODULE_LISTE))) {
                list[0] = entity.getIdModuleFacturation();
                list[1] = entity.getLibelle();
                vList.add(list);
            }
            if ((condition.equalsIgnoreCase("EXCEPT_STANDARD"))
                    && (!entity.getIdTypeModule().equalsIgnoreCase(FAModuleFacturation.CS_MODULE_STANDARD))) {
                list[0] = entity.getIdModuleFacturation();
                list[1] = entity.getLibelle();
                vList.add(list);
            }
            if (condition.equalsIgnoreCase("EXCEPT_STANDARD_LISTE")) {
                if ((!entity.getIdTypeModule().equalsIgnoreCase(FAModuleFacturation.CS_MODULE_LISTE))
                        && (!entity.getIdTypeModule().equalsIgnoreCase(FAModuleFacturation.CS_MODULE_STANDARD))) {
                    list[0] = entity.getIdModuleFacturation();
                    list[1] = entity.getLibelle();
                    vList.add(list);
                }
            }

            // Logique d'acceptation de certains types
            if (condition.equalsIgnoreCase("ACCEPT_AFACT")) {
                if (entity.getIdTypeModule().equalsIgnoreCase(FAModuleFacturation.CS_MODULE_AFACT)) {
                    list[0] = entity.getIdModuleFacturation();
                    list[1] = entity.getLibelle();
                    vList.add(list);
                }
            } else if (condition.equalsIgnoreCase("ACCEPT_PCOMP")) {
                if (entity.getIdTypeModule().equalsIgnoreCase(FAModuleFacturation.CS_MODULE_COMPENSATION)) {
                    list[0] = entity.getIdModuleFacturation();
                    list[1] = entity.getLibelle();
                    vList.add(list);
                }
            }
        }
        return vList;
    }

    /**
     * Permet de savoir si un Passage est verrouillé depuis toutes les pages JSP
     * 
     * @author rri Date de création : (07.03.2005 11:35:23)
     * @return boolean
     */
    public static Boolean getPassageLock(String idPassage, javax.servlet.http.HttpSession httpSession) throws Exception {
        if (!JadeStringUtil.isBlank(idPassage)) {
            FAPassage passage = new FAPassage();
            // Passage de la session Http
            BISession bsession = globaz.musca.translation.CodeSystem.getSession(httpSession);
            passage.setSession((BSession) bsession);
            passage.setIdPassage(idPassage);
            try {
                passage.retrieve();
            } catch (Exception e) {
                JadeLogger.error(passage, e);
            }
            return passage.isEstVerrouille();

        } else {
            return new Boolean(false);
        }
    }

    /*
     * Retourne le status du passage.
     */
    public static java.lang.String getPassageStatus(String idPassage, javax.servlet.http.HttpSession httpSession)
            throws Exception {
        if (!JadeStringUtil.isBlank(idPassage)) {
            FAPassage passage = new FAPassage();
            // Passage de la session Http
            BISession bsession = globaz.musca.translation.CodeSystem.getSession(httpSession);
            passage.setSession((BSession) bsession);
            passage.setIdPassage(idPassage);
            try {
                passage.retrieve();
                return passage.getStatus();
            } catch (Exception e) {
                return "Error status";
            }
        } else {
            return "";
        }
    }

    /**
     * Liste les plans de passage
     */
    public static Vector<String[]> getPlanList(javax.servlet.http.HttpSession httpSession) {
        Vector<String[]> vList = new Vector<String[]>();
        // ajoute un blanc
        String[] list = new String[2];
        list[0] = "";
        list[1] = "";
        vList.add(list);

        try {
            FAPlanFacturationManager manager = new FAPlanFacturationManager();
            manager.setISession(globaz.musca.translation.CodeSystem.getSession(httpSession));
            manager.setWantFilterByPlanFacturation(new Boolean(true));
            manager.orderByLibelleLangueSession();
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                list = new String[2];
                FAPlanFacturation entity = (FAPlanFacturation) manager.getEntity(i);
                list[0] = entity.getIdPlanFacturation();
                list[1] = entity.getLibelle();
                vList.add(list);
            }
        } catch (Exception e) {
            // si probleme, retourne list vide.
        }
        return vList;
    }

    public static boolean hasCurrentUserComplement(BSession theSession, String theComplement) throws Exception {

        StringBuffer theWrongArgument = new StringBuffer();
        if (theSession == null) {
            theWrongArgument.append(" theSession is null ");
        }

        if (JadeStringUtil.isBlankOrZero(theComplement)) {
            theWrongArgument.append(" theComplement is empty ");
        }

        if (theWrongArgument.length() > 0) {
            throw new Exception("Can't check current user has complement " + theComplement
                    + " due to wrong arguments : " + theWrongArgument.toString());
        }

        FWSecureUserDetail user = new FWSecureUserDetail();
        user.setSession(theSession);
        user.setUser(theSession.getUserId());
        user.setLabel(theComplement);

        user.retrieve();

        return "true".equalsIgnoreCase(user.getData());

    }

    public final static boolean isModFacInPassage(BSession session, String idTypeModule, String idPassage)
            throws Exception {

        StringBuffer wrongArguments = new StringBuffer();

        if (session == null) {
            wrongArguments.append("session : " + session + " ");
        }

        if (JadeStringUtil.isBlankOrZero(idTypeModule)) {
            wrongArguments.append("idTypeModule : " + idTypeModule + " ");
        }

        if (JadeStringUtil.isBlankOrZero(idPassage)) {
            wrongArguments.append("idPassage : " + idPassage);
        }

        if (!JadeStringUtil.isEmpty(wrongArguments.toString())) {
            throw new Exception("Can't check if module is in passage wrong arguments (" + wrongArguments.toString()
                    + ")");
        }

        try {
            FAModulePassageManager modFacManager = new FAModulePassageManager();
            modFacManager.setSession(session);
            modFacManager.setForIdTypeModule(idTypeModule);
            modFacManager.setForIdPassage(idPassage);

            return modFacManager.getCount() >= 1;

        } catch (Exception e) {
            throw new Exception("Can't check if module is in passage : " + e.toString(), e);
        }

    }

    /**
     * Permet de savoir si on utilise le nouveau ou l'ancien controle employeur
     * 
     * @param session
     * @return
     */
    public static boolean isNouveauControleEmployeur(BSession session) {
        try {
            FAApplication application = (FAApplication) session.getApplication();
            return application.isNouveauControleEmployeur();
        } catch (Exception e) {
            return false;
        }
    }

    public static final boolean isSpecialisteFacturation(HttpSession httpSession) {
        try {
            BSession session = (BSession) ((FWController) httpSession.getAttribute("objController")).getSession();

            FWSecureUserDetail user = new FWSecureUserDetail();
            user.setSession(session);
            user.setUser(session.getUserId());
            user.setLabel(FAUtil.SPECIALISTE_FACTURATION);

            user.retrieve();

            return "true".equalsIgnoreCase(user.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static FAPassage loadPassage(String idPassage, BSession session) throws Exception {

        if (JadeStringUtil.isBlankOrZero(idPassage)) {
            throw new Exception(session.getLabel("ERREUR_AUCUN_ID_PASSAGE"));
        }

        FAPassage thePassage = new FAPassage();
        thePassage.setSession(session);
        thePassage.setIdPassage(idPassage);
        thePassage.retrieve();

        return thePassage;
    }

    /**
     * Double les caractères <code>&#39;</code> (apostrophe) d'un message. Il est possible de ne pas doubler les paires
     * d'apostrophes qui se suivent.
     * 
     * @param originalMessage
     *            le message dont on veut "préparer" les apostrophes.
     * @param doubleDQuotesToo
     *            indique s'il faut doubler les paires d'apostrophes également.
     * @return le message original, avec éventuellement les apostrophes doublées.
     */
    public static String prepareQuotes(String originalMessage, boolean doubleDQuotesToo) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < originalMessage.length(); i++) {
            char tmpChar = originalMessage.charAt(i);
            result.append(tmpChar);
            if (tmpChar == '\'') {
                if (doubleDQuotesToo) {
                    result.append('\'');
                } else {
                    boolean hasNextChar = (i + 1) < originalMessage.length();
                    if (hasNextChar) { // s'il y a un prochain char et que c'est
                        // un quote, on oublie
                        char nextChar = originalMessage.charAt(i + 1);
                        if (nextChar == '\'') {
                            i++;
                        }
                    }
                    result.append('\'');
                }
            }
        }
        return result.toString();

    }

    /**
     * Return le nombre d'entête selon les critères renseignées
     * 
     * @param session
     * @param idPassage
     * @param idExterneFacture
     * @param idExterneRole
     * @param idTiers
     * @return
     * @throws Exception
     */
    public static int returnNombreEnteteFacture(BSession session, String idPassage, String idExterneFacture,
            String idExterneRole, String idTiers) throws Exception {
        if (JadeStringUtil.isBlankOrZero(idPassage) && JadeStringUtil.isBlankOrZero(idExterneFacture)
                && JadeStringUtil.isBlankOrZero(idExterneRole) && JadeStringUtil.isBlankOrZero(idTiers)) {
            throw new Exception("Insufficient number of arguments");
        }

        FAEnteteFactureManager manager = new FAEnteteFactureManager();
        manager.setSession(session);
        manager.setForIdPassage(idPassage);
        manager.setForIdExterneFacture(idExterneFacture);
        manager.setForIdExterneRole(idExterneRole);
        manager.setForIdTiers(idTiers);
        manager.find();
        return manager.getSize();

    }

    /**
     * Commentaire relatif au constructeur CPUtil.
     */
    public FAUtil() {
        super();
    }

}
