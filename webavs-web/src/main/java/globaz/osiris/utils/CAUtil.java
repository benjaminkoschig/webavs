package globaz.osiris.utils;

import globaz.framework.controller.FWController;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGMandatManager;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.lynx.parser.LXAutoComplete;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.api.APISection;
import globaz.osiris.api.APIUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.db.comptes.CACompteCourantManager;
import globaz.osiris.db.comptes.CACompteur;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CARubrique;
import globaz.osiris.db.comptes.CARubriqueManager;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.CASectionManager;
import globaz.osiris.db.ventilation.CAVPTypeDeProcedureOrdre;
import globaz.osiris.db.ventilation.CAVPTypeDeProcedureOrdreManager;
import globaz.osiris.exceptions.CATechnicalException;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.http.HttpSession;
import ch.globaz.osiris.business.exception.OsirisException;
import ch.globaz.osiris.business.model.JournalSimpleModel;

/**
 * Classe utilitaire pour OSIRIS.
 * 
 * @author: Sébastien Chappatte
 */
public class CAUtil implements APIUtil {
    private static final String END_ID_EXTERNE_SECTION_899 = "899";
    private static final String END_ID_EXTERNE_SECTION_999 = "999";

    public static final String ID_MENU_NODE_CA_ORDRES_GROUPES_ANNULER = "CA-OrdresGroupesAnnuler";
    public static final String ID_MENU_NODE_CA_ORDRES_GROUPES_EXECUTION = "CA-OrdresGroupesExecution";
    public static final String ID_MENU_NODE_CA_ORDRES_REJETER_IMPRIMER = "CA-OrdresRejeteImprimer";

    public static final String ID_MENU_NODE_CA_ORDRES_GROUPES_PREPARATION = "CA-OrdresGroupesPreparation";
    public static final int SECTION_7_LONGUEUR = 7;
    public static final int SECTION_9_LONGUEUR = 9;

    /**
     * Création d'un numéro de section unique pour un compte annexe identifié par idRole,idExterneRole, sur la base de
     * la racine du numéro de section idTypeFacture, année et idSousType.
     * 
     * @param session
     *            la session
     * @param transaction
     *            la transaction
     * @param idRole
     *            le role
     * @param idExterneRole
     *            le numéro externe
     * @param idTypeSection
     *            identifiant du type de section
     * @param annee
     *            l'année
     * @param idSousType
     *            le sous type
     * @return String le numéro de section
     * @throws Exception
     *             en cas d'erreur
     */
    public static String creerNumeroSectionUnique(BISession session, BITransaction transaction, String idRole,
            String idExterneRole, String idTypeSection, String annee, String categorieSection) throws Exception {
        CAUtil.validateCreerNumeroSectionParameters(session, transaction, idRole, idExterneRole, idTypeSection, annee,
                categorieSection);

        // Préparer le numéro de la section Annee + pos.5 et 6 du sous type
        String numeroSection = annee + categorieSection.substring(4, 6) + "000";
        String numeroSectionMax = annee + categorieSection.substring(4, 6) + APISection.SECTION_900_INTERET_MORATOIRE;

        // Vérifier la longeur du no de section
        if (numeroSection.length() != 9) {
            throw new Exception("creerNumeroSectionUnique: wrong section number :" + numeroSection);
        }

        // Récupérer le compte annexe
        CACompteAnnexe ca = new CACompteAnnexe();
        ca.setISession(session);
        ca.setIdRole(idRole);
        ca.setIdExterneRole(idExterneRole);
        ca.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
        ca.retrieve(transaction);

        if (!ca.isNew()) {
            // Si le compte existe
            CASectionManager mgr = new CASectionManager();
            mgr.setISession(session);
            mgr.setForIdCompteAnnexe(ca.getIdCompteAnnexe());
            mgr.setForIdTypeSection(idTypeSection);
            mgr.setForCategorieSection(categorieSection);
            mgr.setUntilIdExterne(numeroSectionMax);
            mgr.setOrderBy("IDEXTERNE DESC");
            mgr.find(transaction);

            // S'il y a qqch, on ajoute 10
            if (!mgr.isEmpty()) {
                CASection section = (CASection) mgr.getFirstEntity();

                // On contrôle l'année de la dernière section trouvée avec
                // l'année donnée en paramètre
                // si les années sont identiques, on incrémente, sinon, on sort
                long oldAnnee = Long.parseLong(section.getIdExterne().substring(0, 4));
                long newAnnee = Long.parseLong(annee);
                if (newAnnee == oldAnnee) {
                    long max = Long.parseLong(numeroSection) + 900;
                    long newId = Long.parseLong(section.getIdExterne());
                    newId += 1;

                    // Test plafond ......900
                    if (newId >= max) {
                        numeroSection = null;
                        throw new CASectionMaxException(
                                "Maximum section number reached, over xxxxx890. Section can not be created");
                    } else {
                        numeroSection = String.valueOf(newId);
                    }
                }
            }
        }

        // Retourne le numéro de la section
        return numeroSection;
    }

    /**
     * Méthode permettant de transformer un objet CAJournal en JournalSimpleModel.
     * 
     * @param journal Le CAJournal.
     * @return Un JournalSimpleModel.
     * @throws OsirisException Une exception.
     */
    public static JournalSimpleModel parseJournal(CAJournal journal) throws OsirisException {
        if (journal == null) {
            throw new OsirisException("Unable to parse journal, the entity journal is null");
        }

        if (journal.isNew()) {
            return new JournalSimpleModel();
        }

        JournalSimpleModel journalModel = new JournalSimpleModel();

        journalModel.setId(journal.getIdJournal());
        journalModel.setLibelle(journal.getLibelle());
        journalModel.setProprietaire(journal.getProprietaire());
        journalModel.setEtat(journal.getEtat());
        journalModel.setDate(journal.getDate());
        journalModel.setDateValeurCG(journal.getDateValeurCG());
        journalModel.setTypeJournal(journal.getTypeJournal());
        journalModel.setSpy(journal.getSpy().getFullData());

        return journalModel;
    }

    /**
     * Créer un numéro de section unique pour les intérêts moratoires. Incrémente le denier numéro de section par pas de
     * 1.
     * 
     * @param session
     * @param transaction
     * @param ca
     * @param idTypeSection
     * @param annee
     * @param categorieSection
     * @param idExterneSection
     * @return
     * @throws Exception
     */
    public static String creerNumeroSectionUniquePourInteretMoratoire(BISession session, BITransaction transaction,
            CACompteAnnexe ca, String idTypeSection, String annee, String categorieSection, String idExterneSection)
            throws Exception {

        if (JadeStringUtil.isBlankOrZero(categorieSection)
                || APISection.ID_CATEGORIE_SECTION_DECOMPTE_TRANSFERT.equalsIgnoreCase(categorieSection)) {
            categorieSection = APISection.ID_CATEGORIE_SECTION_INTERET_SUR_COTISATIONS_ARRIEREES_SUR_SECTION_SEPAREE;
        }

        CASectionManager manager = new CASectionManager();
        manager.setISession(session);
        manager.setForIdCompteAnnexe(ca.getIdCompteAnnexe());
        manager.setForIdTypeSection(idTypeSection);
        manager.setForCategorieSection(categorieSection);

        manager.setFromIdExterne(annee + categorieSection.substring(4, 6) + CAUtil.END_ID_EXTERNE_SECTION_899);
        manager.setUntilIdExterne(annee + categorieSection.substring(4, 6) + CAUtil.END_ID_EXTERNE_SECTION_999);

        manager.setOrderBy("IDEXTERNE DESC");
        manager.find(transaction);

        if (manager.isEmpty()) {
            return annee + categorieSection.substring(4, 6) + APISection.SECTION_900_INTERET_MORATOIRE;
        } else {
            CASection section = (CASection) manager.getFirstEntity();

            if (CAUtil.END_ID_EXTERNE_SECTION_999.equals(section.getIdExterne().substring(6))) {
                return section.getIdExterne();
            } else {
                long numeroSection = Long.parseLong(section.getIdExterne());
                numeroSection++;

                return "" + numeroSection;
            }
        }
    }

    /**
     * Créer un numéro de section unique pour les intérêts moratoires. Incrémente le denier numéro de section par pas de
     * 1.
     * 
     * @param session
     * @param transaction
     * @param idRole
     * @param idExterneRole
     * @param idTypeSection
     * @param annee
     * @param categorieSection
     * @param idExterneSection
     * @return
     * @throws Exception
     */
    public static String creerNumeroSectionUniquePourInteretMoratoire(BISession session, BITransaction transaction,
            String idRole, String idExterneRole, String idTypeSection, String annee, String categorieSection,
            String idExterneSection) throws Exception {
        CAUtil.validateCreerNumeroSectionParameters(session, transaction, idRole, idExterneRole, idTypeSection, annee,
                categorieSection);

        CACompteAnnexe ca = new CACompteAnnexe();
        ca.setISession(session);
        ca.setIdRole(idRole);
        ca.setIdExterneRole(idExterneRole);
        ca.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
        ca.retrieve(transaction);

        if (ca.isNew()) {
            throw new Exception(((BSession) session).getLabel("5106"));
        }

        return CAUtil.creerNumeroSectionUniquePourInteretMoratoire(session, transaction, ca, idTypeSection, annee,
                categorieSection, idExterneSection);
    }

    /**
     * Cette méthode retourne le compteur en fonction des paramètres passés
     * 
     * @param idCompteAnnexe
     * @param anneeCotisation
     * @param idRubrique
     * @return Si le compteur n'est pas trouvé, retourne null
     * @throws CATechnicalException
     * @throws Exception
     */
    public static CACompteur getCompteur(String idCompteAnnexe, Integer anneeCotisation, String idRubrique,
            BSession session) throws CATechnicalException, Exception {
        if (JadeStringUtil.isBlankOrZero(idCompteAnnexe) || JadeStringUtil.isBlankOrZero(idRubrique)
                || anneeCotisation == null) {
            throw new CATechnicalException("Parameter from Method getCompteur are not correct !");
        }

        CACompteur compteur = new CACompteur();
        compteur.setIdCompteAnnexe(idCompteAnnexe);
        compteur.setAnnee(anneeCotisation.toString());
        compteur.setIdRubrique(idRubrique);
        compteur.setAlternateKey(CACompteur.AK_CPTA_RUB_ANNEE);
        compteur.setSession(session);
        compteur.retrieve();

        if (!compteur.isNew()) {
            return compteur;
        } else {
            return null;
        }
    }

    public static boolean existeSection(BSession session, String idCompteAnnexe, String categorieSection, String annee)
            throws Exception {
        StringBuffer wrongArgument = new StringBuffer();

        if (session == null) {
            wrongArgument.append("session is null / ");
        }

        if (JadeStringUtil.isEmpty(idCompteAnnexe)) {
            wrongArgument.append("idCompteAnnexe is empty / ");
        }

        if (JadeStringUtil.isEmpty(categorieSection)) {
            wrongArgument.append("categorieSection is empty / ");
        }

        if (!JadeDateUtil.isGlobazDateYear(annee)) {
            wrongArgument.append("annee is empty or not valid / ");
        }

        if (wrongArgument.length() >= 1) {
            throw new Exception("unable to determine if section exist due to wrong arguments : "
                    + wrongArgument.toString());
        }

        CASectionManager sectionMgr = new CASectionManager();
        sectionMgr.setSession(session);
        sectionMgr.setForIdCompteAnnexe(idCompteAnnexe);
        sectionMgr.setForCategorieSection(categorieSection);
        sectionMgr.setLikeIdExterne(annee);

        return sectionMgr.getCount() >= 1;
    }

    /**
     * Retourne une liste des comptes courants (numéro et descritption) en fonction des premiers chiffres du numéro de
     * compte introduit en paramètres.
     * 
     * @return la liste d'options (tag select) des comptes courants existants de la caisse actuelle. Peut-être vide si
     *         aucune information n'a été trouvée
     * @param like
     *            les premiers chiffres du numéro affilié
     * @param bsession
     *            la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getForCompteCourant(String like, BSession bsession) {
        StringBuffer options = new StringBuffer();
        try {
            CACompteCourantManager mgr = new CACompteCourantManager();
            mgr.setSession(bsession);
            mgr.setBeginWithIdExterne(like);
            mgr.find(BManager.SIZE_NOLIMIT);
            for (int i = 0; i < mgr.size(); i++) {
                CAUtil.writeOption(options, mgr, i);
            }
        } catch (Exception ex) {
        }
        return options.toString();
    }

    /**
     * Retourne une liste des comptes courants (numéro et description) en fonction des premiers chiffres du numéro de
     * compte introduit en paramètres.
     * 
     * @return la liste d'options (tag select) des comptes courants existants de la caisse actuelle. Peut-être vide si
     *         aucune information n'a été trouvée
     * @param like
     *            les premiers chiffres du numéro affilié
     * @param session
     *            la session HTTP actuelle
     */
    public static String getForCompteCourant(String like, HttpSession session) {
        return CAUtil.getForCompteCourant(like,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * @param like
     * @param bsession
     * @return
     */
    public static String getForCompteCourantForDebitJournal(String like, BSession bsession) {
        StringBuffer options = new StringBuffer();
        try {
            CACompteCourantManager mgr = new CACompteCourantManager();
            mgr.setSession(bsession);
            mgr.setBeginWithIdExterne(like);
            mgr.setForJournalDebit(true);
            mgr.find(BManager.SIZE_NOLIMIT);
            for (int i = 0; i < mgr.size(); i++) {
                CAUtil.writeOption(options, mgr, i);
            }
        } catch (Exception ex) {
        }
        return options.toString();
    }

    /**
     * Retourne une liste des rubriques (numéro et description) en fonction d'une plage de NATURERUBRIQUE
     * 
     * @return la liste d'options (tag select) des rubriques existantes de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les premiers chiffres du numéro affilié
     * @param session
     *            la session HTTP actuelle
     */
    public static String getForNatureRubriqueFraisIn(List listNatureRubrique, BSession bsession) {
        StringBuffer options = new StringBuffer();
        try {
            CARubriqueManager mgr = new CARubriqueManager();
            mgr.setSession(bsession);
            mgr.setForNatureRubriqueIn(listNatureRubrique);
            mgr.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < mgr.size(); i++) {
                CARubrique rub = (CARubrique) mgr.getEntity(i);
                /**/
                options.append("<option value='");
                options.append(rub.getIdExterne());
                options.append(";");
                options.append(rub.getDescription());
                /* Identifiant de la rubrique */
                options.append("' idCompte='");
                options.append(rub.getIdRubrique());
                /* Identifiant externe de la rubrique */
                options.append("' idExterne='");
                options.append(rub.getIdExterne());
                /* Description de la rubrique */
                options.append("' rubriqueDescription=\"");
                options.append(rub.getDescription());
                options.append("\"");
                /**/
                options.append(">");
                options.append(rub.getIdExterne());
                options.append(" - ");
                options.append(rub.getDescription());
                options.append("</option>");
            }
        } catch (Exception ex) {
        }
        return options.toString();
    }

    /**
     * Retourne une liste des rubriques (numéro et description) en fonction d'une plage de NATURERUBRIQUE
     * 
     * @return la liste d'options (tag select) des rubriques existantes de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les premiers chiffres du numéro affilié
     * @param session
     *            la session HTTP actuelle
     */
    public static String getForNatureRubriqueIn(List listNatureRubrique, BSession bsession) {
        StringBuffer options = new StringBuffer();
        try {
            CARubriqueManager mgr = new CARubriqueManager();
            mgr.setSession(bsession);
            mgr.setForNatureRubriqueIn(listNatureRubrique);
            mgr.find(BManager.SIZE_NOLIMIT);

            for (int i = 0; i < mgr.size(); i++) {
                CARubrique rub = (CARubrique) mgr.getEntity(i);
                /**/
                options.append("<option value='");
                options.append(rub.getIdExterne());
                /* Identifiant de la rubrique */
                options.append("' idCompte='");
                options.append(rub.getIdRubrique());
                /* Identifiant externe de la rubrique */
                options.append("' idExterne='");
                options.append(rub.getIdExterne());
                /* Description de la rubrique */
                options.append("' rubriqueDescription=\"");
                options.append(rub.getDescription());
                options.append("\"");
                /**/
                options.append(">");
                options.append(rub.getIdExterne());
                options.append(" - ");
                options.append(rub.getDescription());
                options.append("</option>");
            }
        } catch (Exception ex) {
        }
        return options.toString();
    }

    /**
     * Retourne une liste des rubriques (numéro et description) en fonction d'une plage de NATURERUBRIQUE
     * 
     * @return la liste d'options (tag select) des rubriques existantes de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les premiers chiffres du numéro affilié
     * @param session
     *            la session HTTP actuelle
     */
    public static String getForNatureRubriqueIn(List listNatureRubrique, HttpSession session) {
        return CAUtil.getForNatureRubriqueIn(listNatureRubrique,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * Retourne une liste des rubriques (numéro et descritption) en fonction des premiers chiffres du numéro de compte
     * introduit en paramètres.
     * 
     * @return la liste d'options (tag select) des rubriques existantes de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les premiers chiffres du numéro affilié
     * @param bsession
     *            la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getForRubrique(String like, BSession bsession) {
        StringBuffer options = new StringBuffer();
        try {
            CARubriqueManager mgr = new CARubriqueManager();
            mgr.setSession(bsession);
            mgr.setBeginWithIdExterne(like);
            mgr.find(BManager.SIZE_NOLIMIT);
            for (int i = 0; i < mgr.size(); i++) {
                CARubrique rub = (CARubrique) mgr.getEntity(i);
                /**/
                options.append("<option value='");
                options.append(rub.getIdExterne());
                /* Identifiant de la rubrique */
                options.append("' idCompte='");
                options.append(rub.getIdRubrique());

                options.append("' idRubrique='");
                options.append(rub.getIdRubrique());
                /* Identifiant externe de la rubrique */
                options.append("' idExterneRubriqueEcran='");
                options.append(rub.getIdExterne());
                /* Description de la rubrique */
                options.append("' rubriqueDescription=\"");
                options.append(rub.getDescription());
                options.append("\"");
                /**/
                options.append(">");
                options.append(rub.getIdExterne());
                options.append(" - ");
                options.append(rub.getDescription());
                options.append("</option>");
            }
        } catch (Exception ex) {
        }
        return options.toString();
    }

    /**
     * Retourne une liste des rubriques (numéro et description) en fonction des premiers chiffres du numéro de compte
     * introduit en paramètres.
     * 
     * @return la liste d'options (tag select) des rubriques existantes de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les premiers chiffres du numéro affilié
     * @param session
     *            la session HTTP actuelle
     */
    public static String getForRubrique(String like, HttpSession session) {
        return CAUtil.getForRubrique(like,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * Retourne une liste des rubriques de type 'compte financier'(numéro et descritption) en fonction des premiers
     * chiffres du numéro de compte introduit en paramètres.
     * 
     * @return la liste d'options (tag select) des rubriques existantes de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les premiers chiffres du numéro affilié
     * @param bsession
     *            la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getForRubriqueFinanciere(String like, BSession bsession) {
        StringBuffer options = new StringBuffer();
        try {
            CARubriqueManager mgr = new CARubriqueManager();
            mgr.setSession(bsession);
            mgr.setBeginWithIdExterne(like);
            mgr.setForNatureRubrique(globaz.osiris.api.APIRubrique.COMPTE_FINANCIER);
            mgr.find(BManager.SIZE_NOLIMIT);
            for (int i = 0; i < mgr.size(); i++) {
                CARubrique rub = (CARubrique) mgr.getEntity(i);
                /**/
                options.append("<option value='");
                options.append(rub.getIdExterne());
                /* Identifiant de la rubrique */
                options.append("' idCompte='");
                options.append(rub.getIdRubrique());
                /* Identifiant externe de la rubrique */
                options.append("' idExterneRubriqueEcran='");
                options.append(rub.getIdExterne());
                /* Description de la rubrique */
                options.append("' rubriqueDescription=\"");
                options.append(rub.getDescription());
                options.append("\"");
                /**/
                options.append(">");
                options.append(rub.getIdExterne());
                options.append(" - ");
                options.append(rub.getDescription());
                options.append("</option>");
            }
        } catch (Exception ex) {
        }
        return options.toString();
    }

    /**
     * Retourne une liste des rubriques de type 'compte financier' (numéro et description) en fonction des premiers
     * chiffres du numéro de compte introduit en paramètres.
     * 
     * @return la liste d'options (tag select) des rubriques existantes de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les premiers chiffres du numéro affilié
     * @param session
     *            la session HTTP actuelle
     */
    public static String getForRubriqueFinanciere(String like, HttpSession session) {
        return CAUtil.getForRubriqueFinanciere(like,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * Retourne la liste des mandats.
     * 
     * @param session
     * @param like
     * @return
     */
    public static String getMandat(BSession session, String like) {
        StringBuffer select = new StringBuffer("<select size=\"5\" style=\"width:16cm\">");

        try {
            CGMandatManager manager = new CGMandatManager();
            manager.setForNotComptabiliteAVS(true);
            manager.setSession(session);

            if (like != null) {
                try {
                    Integer.parseInt(like.substring(0, 1));
                    manager.setFromIdMandat(like);
                } catch (Exception e) {
                    if ("IT".equalsIgnoreCase(session.getIdLangueISO())) {
                        manager.setForLibelleItLike(like);
                    } else if ("DE".equalsIgnoreCase(session.getIdLangueISO())) {
                        manager.setForLibelleDeLike(like);
                    } else {
                        manager.setForLibelleFrLike(like);
                    }
                }
            }

            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                CGMandat mandat = (CGMandat) manager.getEntity(i);

                select.append("<option libelleMandat=\"").append(mandat.getLibelle()).append("\" value=\"")
                        .append(mandat.getIdMandat()).append("\">").append(mandat.getIdMandat()).append(" - ")
                        .append(mandat.getLibelle()).append("</option>");
            }

            select.append("</select>");

        } catch (Exception e) {
            JadeLogger.error(LXAutoComplete.class.getName() + "_getMandat()", e.toString());

            select = new StringBuffer("<select size=\"2\" style=\"width:8cm; color:red; \"><option>");
            select.append(session.getLabel("ERREUR_TECHNIQUE"));
            select.append("</option><option>&nbsp;</option></select>");
            return select.toString();
        }

        return select.toString();
    }

    /**
     * Cette méthode vérifie que pour un type de procédure il n'y ait pas, dans le paramétrage, une rubrique
     * irrécouvrable qui soit à la fois de type "simple" et "salarié" ou "employeur"
     * 
     * @param typeDeProcedure
     * @param session
     * @return false si le paramétrage ne correspond pas ou en cas d'erreurs
     */
    public static boolean isParametrageVentilationCorrect(CAVPTypeDeProcedureOrdre typeDeProcedure, BSession session) {
        boolean isParamRubriqueIrrecSimpleSalEmplOk = false;
        boolean isParamRubriqueTypeOrdreOk = false;

        // contrôle qu'il n'y ait qu'un seul type d'ordre existant pour une même rubrique irrécouvrable, soit simple,
        // soit salarié / employeur
        isParamRubriqueIrrecSimpleSalEmplOk = CAUtil.isParamRubriqueIrrecTypeOrdreOk(typeDeProcedure, session);

        // contrôle que pour une rubrique il y ait soit le type d'ordre simple, soit la paire salarié / employeur qui
        // soit existante.
        isParamRubriqueTypeOrdreOk = CAUtil.isParamRubriqueTypeOrdreOk(typeDeProcedure, session);

        if (isParamRubriqueIrrecSimpleSalEmplOk && isParamRubriqueTypeOrdreOk) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Cette méthode vérifie que pour un type de procédure et une rubrique irrécouvrable donnée, il n'existe pas un
     * paramétrage qui ait à la fois un type "simple" et un type "salarié" et/ou "employeur" dans le paramétrage.
     * 
     * @param typeDeProcedure
     * @param session
     * @return false si le paramétrage ne correspond pas ou en cas d'erreurs
     */
    public static boolean isParamRubriqueIrrecTypeOrdreOk(CAVPTypeDeProcedureOrdre typeDeProcedure, BSession session) {
        boolean paramRubriqueIrrecOk = false;
        if ((typeDeProcedure == null)) {
            return paramRubriqueIrrecOk;
        }

        try {
            CAVPTypeDeProcedureOrdreManager typeDeProcedureManager = new CAVPTypeDeProcedureOrdreManager();
            typeDeProcedureManager.setSession(session);
            typeDeProcedureManager.setForIdRubriqueIrrecouvrable(typeDeProcedure.getIdRubriqueIrrecouvrable());
            typeDeProcedureManager.setForTypeProcedure(typeDeProcedure.getTypeProcedure());
            typeDeProcedureManager.changeManagerSize(BManager.SIZE_NOLIMIT);
            typeDeProcedureManager.find();

            boolean modeSimpleTrouve = false;
            boolean modeSalarieEmployeurTrouve = false;
            for (int i = 0; i < typeDeProcedureManager.size(); i++) {
                CAVPTypeDeProcedureOrdre procedure = (CAVPTypeDeProcedureOrdre) typeDeProcedureManager.getEntity(i);
                if (CAVPTypeDeProcedureOrdre.TYPE_PROCEDURE_MONTANT_SIMPLE.equals(procedure.getTypeOrdre())) {
                    modeSimpleTrouve = true;
                }
                if (CAVPTypeDeProcedureOrdre.TYPE_PROCEDURE_PART_EMPLOYEUR.equals(procedure.getTypeOrdre())
                        || CAVPTypeDeProcedureOrdre.TYPE_PROCEDURE_PART_SALARIE.equals(procedure.getTypeOrdre())) {
                    modeSalarieEmployeurTrouve = true;
                }
            }

            if (modeSalarieEmployeurTrouve && modeSimpleTrouve) {
                paramRubriqueIrrecOk = false;
            } else if (modeSalarieEmployeurTrouve
                    && CAVPTypeDeProcedureOrdre.TYPE_PROCEDURE_MONTANT_SIMPLE.equals(typeDeProcedure.getTypeOrdre())) {
                paramRubriqueIrrecOk = false;
            } else if (modeSimpleTrouve
                    && (CAVPTypeDeProcedureOrdre.TYPE_PROCEDURE_PART_EMPLOYEUR.equals(typeDeProcedure.getTypeOrdre()) || CAVPTypeDeProcedureOrdre.TYPE_PROCEDURE_PART_SALARIE
                            .equals(typeDeProcedure.getTypeOrdre()))) {
                paramRubriqueIrrecOk = false;
            } else {
                paramRubriqueIrrecOk = true;
            }
        } catch (Exception e) {
            return paramRubriqueIrrecOk;
        }

        return paramRubriqueIrrecOk;
    }

    /**
     * Cette méthode retourne true si le compteur est coché pour la rubrique passée en paramètre.
     * 
     * @param idRubrique
     * @param session
     * @return True s'il faut tenir un compteur pour la rubrique, non si pas de compteur
     * @throws Exception Retourne une exception si les paramètres ne sont pas corrects.
     */
    public static boolean isRubriqueAvecCompteur(String idRubrique, BSession session) throws CATechnicalException,
            Exception {
        boolean rubriqueAvecCompteur = false;
        if (!JadeStringUtil.isBlank(idRubrique) && session != null) {
            CARubrique rubrique = new CARubrique();
            rubrique.setSession(session);
            rubrique.setIdRubrique(idRubrique);
            rubrique.retrieve();
            if (rubrique.getTenirCompteur()) {
                return true;
            }
        } else {
            throw new CATechnicalException("Les paramètres de la méthode isRubriqueAvecCompteur ne sont pas corrects ");
        }
        return rubriqueAvecCompteur;
    }

    /**
     * Cette méthode vérifie que pour un type de procédure et une rubrique donnée, il existe soit un type d'ordre
     * "simple" ou une paire "salarié" et "employeur" dans le paramétrage.
     * 
     * @param typeDeProcedure
     * @param session
     * @return false si le paramétrage ne correspond pas ou en cas d'erreurs
     */
    public static boolean isParamRubriqueTypeOrdreOk(CAVPTypeDeProcedureOrdre typeDeProcedure, BSession session) {
        boolean paramRubriqueTypeOrdreOk = false;
        if ((typeDeProcedure == null)) {
            return paramRubriqueTypeOrdreOk;
        }

        try {
            CAVPTypeDeProcedureOrdreManager typeDeProcedureManager = new CAVPTypeDeProcedureOrdreManager();
            typeDeProcedureManager.setSession(session);
            typeDeProcedureManager.setForIdRubrique(typeDeProcedure.getIdRubrique());
            typeDeProcedureManager.setForTypeProcedure(typeDeProcedure.getTypeProcedure());
            typeDeProcedureManager.changeManagerSize(BManager.SIZE_NOLIMIT);
            typeDeProcedureManager.find();

            if (typeDeProcedureManager.size() == 1) {
                CAVPTypeDeProcedureOrdre procedure = (CAVPTypeDeProcedureOrdre) typeDeProcedureManager.getFirstEntity();
                if (CAVPTypeDeProcedureOrdre.TYPE_PROCEDURE_MONTANT_SIMPLE.equals(procedure.getTypeOrdre())) {
                    return true;
                }
            } else if (typeDeProcedureManager.size() == 2) {
                boolean typeSalarie = false;
                boolean typeEmployeur = false;
                for (int i = 0; i < typeDeProcedureManager.size(); i++) {
                    CAVPTypeDeProcedureOrdre procedure = (CAVPTypeDeProcedureOrdre) typeDeProcedureManager.getEntity(i);
                    if (CAVPTypeDeProcedureOrdre.TYPE_PROCEDURE_PART_EMPLOYEUR.equals(procedure.getTypeOrdre())) {
                        typeEmployeur = true;
                    }
                    if (CAVPTypeDeProcedureOrdre.TYPE_PROCEDURE_PART_SALARIE.equals(procedure.getTypeOrdre())) {
                        typeSalarie = true;
                    }
                }
                if (typeSalarie && typeEmployeur) {
                    return true;
                }
            }

        } catch (Exception e) {
            return paramRubriqueTypeOrdreOk;
        }

        return paramRubriqueTypeOrdreOk;
    }

    /**
     * Règles permettant de vérifier si le solde restant dans la section est inférieur ou égal aux montants des taxes.
     * 
     * @param soldeSection Le solde de la section en FWCurrency.
     * @param sumTaxes Le total des taxes en FWCurrency.
     * @return Vrai si le solde de la section est (<=) aux montants des taxes.
     */
    public static boolean isSoldeSectionLessOrEqualTaxes(FWCurrency soldeSection, FWCurrency sumTaxes) {
        if ((soldeSection == null) || (sumTaxes == null)) {
            return false;
        }
        // Partie métier, vérifiant si le solde est inférieure ou égal au montant des taxes.
        return (soldeSection.doubleValue() <= sumTaxes.doubleValue()) && (soldeSection.isPositive());
    }

    /**
     * Règles permettant de vérifier si le solde restant dans la section est inférieur ou égal aux montants des taxes.
     * 
     * @param soldeSection Le solde de la section en String.
     * @param sumTaxes Le total des taxes en String.
     * @return Vrai si le solde de la section est (<=) aux montants des taxes.
     */
    public static boolean isSoldeSectionLessOrEqualTaxes(String soldeSection, String sumTaxes) {
        if ((soldeSection == null) || (sumTaxes == null)) {
            return false;
        }
        return CAUtil.isSoldeSectionLessOrEqualTaxes(new FWCurrency(soldeSection), new FWCurrency(sumTaxes));
    }

    public static boolean montantDiffPlusOuMoins5(BigDecimal montant) {

        if (montant.signum() > 0) {
            if (montant.compareTo(new BigDecimal(5)) < 0) {
                return true;
            } else {
                return false;
            }
        } else if (montant.signum() < 0) {
            if (montant.compareTo(new BigDecimal(-5)) > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Reourne le premier compte annexe selon les critères passés en paramètre
     * 
     * @param session
     * @param idExterne
     * @param idRole
     * @param idTiers
     * @param genreCompte
     * @param selectionSigne
     * @param idGenreCompte
     * @param motifContentieux
     * @return
     * @throws Exception
     */
    public static CACompteAnnexe returnCompteAnnexe(BSession session, String idExterne, String idRole, String idTiers,
            String genreCompte, String selectionSigne, String idGenreCompte, String motifContentieux) throws Exception {
        /* Tester les paramètres indispensables (évite ainsi d'avoir une trop grande sélection) */
        if (JadeStringUtil.isBlankOrZero(idExterne) && JadeStringUtil.isBlankOrZero(idTiers)) {
            throw new Exception("Insufficient number of arguments");
        }
        CACompteAnnexeManager manager = new CACompteAnnexeManager();
        manager.setSession(session);
        manager.setForIdTiers(idTiers);
        manager.setForIdExterneRole(idExterne);
        manager.setForIdRole(idRole);
        manager.setForIdGenreCompte(genreCompte);
        manager.setForSelectionSigne(selectionSigne);
        manager.setForIdGenreCompte(idGenreCompte);
        manager.setForIdContMotifBloque(motifContentieux);
        manager.find();
        if (manager.getSize() > 0) {
            return null;
        } else {
            return (CACompteAnnexe) manager.getFirstEntity();
        }
    }

    /**
     * Vérifier les paramètres.
     * 
     * @param session
     * @param transaction
     * @param idRole
     * @param idExterneRole
     * @param idTypeSection
     * @param annee
     * @param categorieSection
     * @throws Exception
     */
    private static void validateCreerNumeroSectionParameters(BISession session, BITransaction transaction,
            String idRole, String idExterneRole, String idTypeSection, String annee, String categorieSection)
            throws Exception {
        if (session == null) {
            throw new Exception("creerNumeroSectionUnique: session required");
        }

        if (transaction == null) {
            throw new Exception("creerNumeroSectionUnique: transaction required");
        }

        if (JadeStringUtil.isBlank(idRole)) {
            throw new Exception("creerNumeroSectionUnique: idRole required");
        }

        if (JadeStringUtil.isBlank(idExterneRole)) {
            throw new Exception("creerNumeroSectionUnique: idExterneRole required");
        }

        if (JadeStringUtil.isBlank(idTypeSection)) {
            throw new Exception("creerNumeroSectionUnique: idTypeSection required");
        }

        if (JadeStringUtil.isBlank(annee)) {
            throw new Exception("creerNumeroSectionUnique: annee required");
        }

        if (JadeStringUtil.isBlank(categorieSection)) {
            throw new Exception("creerNumeroSectionUnique: categorieSection required");
        }

        if (categorieSection.length() != 6) {
            throw new Exception("creerNumeroSectionUnique: wrong categorieSection :" + categorieSection
                    + " for affiliation : " + idExterneRole);
        }
    }

    /**
     * @param options
     * @param mgr
     * @param i
     */
    private static void writeOption(StringBuffer options, CACompteCourantManager mgr, int i) {
        CACompteCourant cc = (CACompteCourant) mgr.getEntity(i);
        /**/
        options.append("<option value='");
        options.append(cc.getIdExterne());
        /* Identifiant du compte courant */
        options.append("' idCompteCourant='");
        options.append(cc.getIdCompteCourant());
        /* Identifiant externe du compte courant */
        options.append("' idExterneCompteCourantEcran='");
        options.append(cc.getIdExterne());
        /* Description du compte courant */
        options.append("' CCEcran=\"");
        options.append(cc.getRubrique().getDescription());
        options.append("\"");
        /**/
        options.append(">");
        options.append(cc.getIdExterne());
        options.append(" - ");
        options.append(cc.getRubrique().getDescription());
        options.append("</option>");
    }

    /**
     * Commentaire relatif au constructeur CAUtil.
     */
    public CAUtil() {
        super();
    }

    /**
     * Création d'un numéro de section unique utilisé par le module sunanda.
     */
    @Override
    public String creerNumeroSectionUniquePourDossierAf(BISession session, BITransaction transaction, String idRole,
            String idExterneRole, String idTypeSection, String annee, String categorieSection) throws Exception {
        return CAUtil.creerNumeroSectionUnique(session, transaction, idRole, idExterneRole, idTypeSection, annee,
                categorieSection);
    }

}
