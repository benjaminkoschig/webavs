package globaz.pavo.util;

import globaz.caisse.helper.CaisseHelperFactory;
import globaz.commons.nss.NSUtil;
import globaz.framework.controller.FWController;
import globaz.framework.secure.user.FWSecureUser;
import globaz.framework.secure.user.FWSecureUserDetail;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JADate;
import globaz.globall.util.JAUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CACompteur;
import globaz.osiris.db.comptes.CACompteurManager;
import globaz.pavo.application.CIApplication;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.pavo.db.compte.CIEcriture;
import globaz.pavo.db.compte.CIEcritureManager;
import globaz.pavo.db.compte.CIEcrituresSomme;
import globaz.pavo.db.compte.CIInscriptionsManuellesManager;
import globaz.pavo.db.compte.CIRassemblementOuverture;
import globaz.pavo.translation.CodeSystem;
import globaz.pyxis.db.tiers.TIAdministrationManager;
import globaz.pyxis.db.tiers.TIAdministrationViewBean;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;
import javax.servlet.http.HttpSession;

/**
 * Classe utilitaire pour PAVO. Date de création : (09.01.2003 15:18:41)
 * 
 * @author: Administrator
 */
public class CIUtil {
    public static String SPECIALIST_CI = "SpecialisteCI";

    /**
     * Renvoie le code système asssocié à un code utilisateur et un groupe.
     * 
     * @param Un
     *            object BTransaction.
     * @param Le
     *            code utilisateur.
     * @param Le
     *            groupe.
     * @return Le code système asssocié à un code utilisateur et un groupe
     */
    public static String codeUtilisateurToCodeSysteme(BTransaction transaction, String code, String groupe,
            BSession session) throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(code.trim())) {
            try {
                int valeurCode = new Integer(code).intValue();
                // En dessous de 300000, c'est une code utilisateur (pour PAVO
                // en tout cas)
                if (valeurCode < 300000) {
                    throw new Exception();
                }
                // code est déjà un code système
                return code;
            } catch (Exception e) {
                // C'est un code utilisateur. Il faut obtenir le code système
                FWParametersSystemCodeManager systemCodeMng = new FWParametersSystemCodeManager();
                systemCodeMng.setSession(session);
                systemCodeMng.setForIdGroupe(groupe);
                systemCodeMng.setForCodeUtilisateur(code);
                systemCodeMng.find(transaction);
                if (!systemCodeMng.hasErrors()) {
                    if (systemCodeMng.getSize() > 0) {
                        return ((FWParametersSystemCode) systemCodeMng.getEntity(0)).getIdCode();
                    } else {
                        // Pas de code système pour le code utilisateur
                        return "";
                    }
                } else {
                    return "";
                }
            }
        } else {
            return "";
        }
    }

    public static String fillWith(String text, char fill, int lenght) {
        StringBuffer buf = new StringBuffer(text);
        while (buf.length() < lenght) {
            buf.append(fill);
        }
        return buf.toString();
    }

    /**
     * Renvoie une date aaaammjj en jj.mm.aaaa ou null si format incorrect. Date de création : (14.03.2003 10:38:25)
     * 
     * @return java.lang.String
     * @param date
     *            java.lang.String
     */
    public static String formatDate(String date) {
        if (!JAUtil.isStringEmpty(date) && (date.length() == 8)) {
            return date.substring(6, 8) + "." + date.substring(4, 6) + "." + date.substring(0, 4);
        }
        return "";
    }

    /**
     * Renvoie une date jj.mm.aaaa en aaammjj ou null si format incorrecte. Date de création : (14.03.2003 10:38:25)
     * 
     * @return java.lang.String
     * @param date
     *            java.lang.String
     */
    public static String formatDateAMJ(String date) {
        if (!JAUtil.isStringEmpty(date)) {
            int firstDot = date.indexOf('.');
            int secondDot = date.substring(firstDot + 1).indexOf('.') + firstDot + 1;
            String jour = "00";
            String mois = "00";
            String annee = "";
            if (firstDot != -1) {
                if (secondDot != firstDot) {
                    jour = date.substring(0, firstDot);
                    mois = date.substring(firstDot + 1, secondDot);
                    annee = date.substring(secondDot + 1);
                } else {
                    mois = date.substring(0, firstDot);
                    annee = date.substring(firstDot + 1);
                }
                if (jour.length() > 2) {
                    return null;
                }
                if (mois.length() > 2) {
                    return null;
                }
                if (annee.length() != 4) {
                    return null;
                }
            } else {
                if (date.length() > 6) {
                    jour = date.substring(0, date.length() - 6);
                    if (jour.length() > 2) {
                        return null;
                    }
                    mois = date.substring(date.length() - 6, date.length() - 4);
                } else if (date.length() > 4) {
                    mois = date.substring(0, date.length() - 4);
                }
                if (date.length() >= 4) {
                    annee = date.substring(date.length() - 4);
                } else {
                    return null;
                }
            }
            return annee + JadeStringUtil.rightJustifyInteger(mois, 2) + JadeStringUtil.rightJustifyInteger(jour, 2);
        }
        return null;
    }

    /**
     * Renvoie une date jj.mm.aaaa en aaammjj ou null si format incorrecte et ajout 1 à la date donnée Date de création
     * : (14.03.2003 10:38:25)
     * 
     * @return java.lang.String
     * @param date
     *            java.lang.String
     */
    public static String formatDateAMJInc(String date) {
        try {
            if (!JAUtil.isStringEmpty(date)) {
                int firstDot = date.indexOf('.');
                int secondDot = date.substring(firstDot + 1).indexOf('.') + firstDot + 1;
                String jour = "00";
                String mois = "00";
                String annee = "";
                if (firstDot != -1) {
                    if (secondDot != firstDot) {
                        jour = String.valueOf(Integer.parseInt(date.substring(0, firstDot)) + 1);
                        mois = date.substring(firstDot + 1, secondDot);
                        annee = date.substring(secondDot + 1);
                    } else {
                        mois = String.valueOf(Integer.parseInt(date.substring(0, firstDot)) + 1);
                        annee = date.substring(firstDot + 1);
                    }
                    if (jour.length() > 2) {
                        return null;
                    }
                    if (mois.length() > 2) {
                        return null;
                    }
                    if (annee.length() != 4) {
                        return null;
                    }
                } else {
                    if (date.length() > 6) {
                        jour = String.valueOf(Integer.parseInt(date.substring(0, date.length() - 6)) + 1);
                        if (jour.length() > 2) {
                            return null;
                        }
                        mois = date.substring(date.length() - 6, date.length() - 4);
                        annee = date.substring(date.length() - 4);
                    } else if (date.length() > 4) {
                        mois = String.valueOf(Integer.parseInt(date.substring(0, date.length() - 4)) + 1);
                        annee = date.substring(date.length() - 4);
                    } else if (date.length() == 4) {
                        annee = String.valueOf(Integer.parseInt(date.substring(date.length() - 4)) + 1);
                    } else {
                        return null;
                    }
                }
                return annee + JadeStringUtil.rightJustifyInteger(mois, 2)
                        + JadeStringUtil.rightJustifyInteger(jour, 2);
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }

    /**
     * On met la date début affiliation si celle ci est plus grande que la date défaut
     * Si pas de date début affiliation (ça devrait pas arrivé), alors on met la date défaut
     * Exemple 1 : début affiliation : 15.02.2016 est plus grande que 01.01.2016, > on met la date affiliation
     * Exemple 2 : début affiliation : 10.01.2015 est plus petite que 01.01.2016, > on met la date défaut
     * 
     * @param dateDebutDefaut La date de fin par défaut à comparer.
     * @param dateDebutAffiliation Date de début de l'affiliation.
     * @return La date affiliation si plus grande que date par défaut, sinon la date défaut.
     * @throws Exception Une exception.
     */
    public static String giveDateDebutGreater(BSession session, final String dateDebutDefaut,
            final String dateDebutAffiliation) throws Exception {

        String dateDebut = dateDebutDefaut;

        if (!JadeStringUtil.isBlankOrZero(dateDebutAffiliation)) {
            boolean isDateDebutAffGreater = BSessionUtil.compareDateFirstGreater(session, dateDebutAffiliation,
                    dateDebutDefaut);
            dateDebut = (isDateDebutAffGreater) ? dateDebutAffiliation : dateDebutDefaut;
        }

        return dateDebut;
    }

    /**
     * On met la date fin affiliation si celle ci est plus grande que la date défaut
     * Si pas de date fin affiliation (ça devrait pas arrivé), alors on met la date défaut
     * Exemple 1 : fin affiliation : 20.11.2016 est plus petite que 31.12.2016, > on met la date affiliation
     * Exemple 2 : fin affiliation : 10.01.2017 est plus grande que 31.12.2016, > on met la date défaut
     * 
     * @param dateFinDefaut La date de fin par défaut à comparer.
     * @param dateFinAffiliation Date de fin de l'affiliation.
     * @return La date affiliation si plus petite que date par défaut, sinon la date défaut.
     * @throws Exception Une exception.
     */
    public static String giveDateFinLower(BSession session, final String dateFinDefaut, final String dateFinAffiliation)
            throws Exception {

        String dateFin = dateFinDefaut;

        if (!JadeStringUtil.isBlankOrZero(dateFinAffiliation)) {
            boolean isDateFinAffLower = BSessionUtil.compareDateFirstLower(session, dateFinAffiliation, dateFinDefaut);
            dateFin = (isDateFinAffLower) ? dateFinAffiliation : dateFinDefaut;
        }

        return dateFin;
    }

    /**
     * Renvoie une date jj.mm.aaaa en jjmmaa. Date de création : (14.03.2003 10:38:25)
     * 
     * @return java.lang.String
     * @param date
     *            java.lang.String
     */
    public static String formatDateJJMMAA(String date) {
        if (!JAUtil.isStringEmpty(date) && (date.length() >= 10)) {
            return date.substring(0, 2) + date.substring(3, 5) + date.substring(8, 10);
        }
        return date;
    }

    /**
     * Renvoie une date jj.mm.aaaa en mmaa. Date de création : (14.03.2003 10:38:25)
     * 
     * @return java.lang.String
     * @param date
     *            java.lang.String
     */
    public static String formatDateMMAA(String date) {
        if (!JAUtil.isStringEmpty(date) && (date.length() >= 10)) {
            return date.substring(3, 5) + date.substring(8, 10);
        }
        return date;
    }

    public static String formatNumeroAffilie(BSession session, String numeroAffilie) throws Exception {
        CIApplication app = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                CIApplication.DEFAULT_APPLICATION_PAVO);
        IFormatData affilieFormater = app.getAffileFormater();
        return affilieFormater.format(numeroAffilie);

    }

    /**
     * ATTENTION uniquement pour les affiliés de type Paritaire
     * 
     * @param session
     * @param annee
     * @param idAffiliation
     * @return
     */
    public static ArrayList getAffilieByJournal(HttpSession session, String annee, String idAffiliation) {

        ArrayList list = new ArrayList();
        // idAffiliation = "22980";

        try {
            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            AFAffiliation aff = new AFAffiliation();
            aff = application.getAffilieByNo(
                    (BSession) ((FWController) session.getAttribute("objController")).getSession(), idAffiliation,
                    true, false, "1", "12", annee, "", "");
            CIEcritureIdJournalManager idJournalMgr = new CIEcritureIdJournalManager();
            idJournalMgr.setForEmployeur(aff.getAffiliationId());
            idJournalMgr.setForAnnee(annee);
            idJournalMgr.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
            idJournalMgr.changeManagerSize(BManager.SIZE_NOLIMIT);
            try {
                idJournalMgr.find();
            } catch (Exception e) {
            }
            AFAffiliationManager affMgr = new AFAffiliationManager();
            affMgr.setForAffilieNumero(idAffiliation);
            affMgr.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
            try {
                affMgr.find();
            } catch (Exception e) {
            }
            for (int i = 0; i < idJournalMgr.size(); i++) {
                ArrayList line = new ArrayList();
                String idJournal = ((CIEcritureIdJournal) idJournalMgr.getEntity(i)).getIdJournal();
                line.add(idJournal);
                BigDecimal result = new BigDecimal("0");
                CIEcrituresSomme somme = new CIEcrituresSomme();
                somme.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
                somme.setForEmployeur(aff.getAffiliationId());
                somme.setForAnnee(annee);
                somme.setForMasseSalariale("true");
                somme.setForIdJournal(idJournal);
                try {
                    result = result.add(somme.getSum("KBMMON"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                line.add(new FWCurrency(String.valueOf(result)).toStringFormat());
                list.add(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
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
    public static String getAffilies(String like, boolean forActif, boolean forParitaire, HttpSession session) {
        return CIUtil.getAffilliesActif(like, forActif, forParitaire,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
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
    public static String getAffilies(String like, boolean forActif, boolean forParitaire, String forIndependant,
            HttpSession session) {
        return CIUtil.getAffilliesInd(like, forActif, forParitaire, forIndependant,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
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
        return CIUtil
                .getAffillies(like, (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * Retourne une liste des Affilies (dernier employeur) pour un compte individuel : numéro, nom et adresses de
     * l'affilié en fonction des premiers chiffres du numéro affilié donnés en paramètres. Date de création :
     * (17.09.2003 17:032:13)
     * 
     * @return la liste d'options (tag select) des affiliés existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les primers chiffres du numéro affilié
     * @param session
     *            la session HTTP actuelle
     */
    public static String getAffiliesForCompteIndividuel(String like, HttpSession session) {
        return CIUtil.getAffilliesForCompteIndividuel(like,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * Retourne une liste des Affilies pour un journal, cad les employeurs et indépendants (numéro et nom de l'affilié)
     * en fonction des premiers chiffres du numéro affilié donnés en paramètres. Date de création : (17.09.2003
     * 17:032:13)
     * 
     * @return la liste d'options (tag select) des affiliés existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les primers chiffres du numéro affilié
     * @param session
     *            la session HTTP actuelle
     */
    public static String getAffiliesForJournal(String like, boolean afficherTypeAff, HttpSession session) {
        return CIUtil.getAffilliesForJournal(like, afficherTypeAff,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * Retourne un employeur,un partenaire ou un organisme spécial (nom, prenom ou libellé) en fonction du numéro
     * d'affilié ou du numéro AVS du partenaire Date de création : (17.09.2003 17:032:13)
     * 
     * @return une string contenant un nom,prenom ou libellé
     * @param like
     *            les primers chiffres du numéro affilié
     * @param session
     *            la session HTTP actuelle
     */
    public static String getAffiliesNom(String like, HttpSession session) {
        return CIUtil.getAffilliesNom(like,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
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
        return CIUtil.getAffilliesActif(like, false, false, bsession);
    }

    /**
     * Retourne une liste des Affilies (numéro et nom de l'affilié) en fonction des permiers chiffres du numéro affilié
     * donnés en paramètres. Date de création : (17.09.2003 17:032:13)
     * 
     * @return la liste d'options (tag select) des affiliés existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les primers chiffres du numéro affilié
     * @param forActif
     *            sélectionne que les affiliations actives
     * @param bsession
     *            la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getAffilliesActif(String like, boolean forActif, boolean forParitaire, BSession bsession) {
        if (like != null) {
            // Si c'est inférieur à 3 => exception
            if (like.length() < 3) {
                throw new RuntimeException("Recherche pas assez discriminante");
            }

        }
        CIApplication ciApp = null;
        try {
            // ciApp = (CIApplication) bsession.getApplication();
            ciApp = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((like != null) && (like.indexOf('.') == -1)) {
            try {
                IFormatData affilieFormater = ciApp.getAffileFormater();
                if (affilieFormater != null) {
                    like = affilieFormater.format(like);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        StringBuffer options = new StringBuffer();
        HashSet affSet = new HashSet();
        try {
            CIAffilieManager mgr = new CIAffilieManager();
            mgr.setSession(bsession);
            mgr.setLikeAffilieNumero(like);
            mgr.setForActif(forActif);
            mgr.setForParitaire(forParitaire);
            mgr.find(BManager.SIZE_NOLIMIT);
            for (int i = 0; i < mgr.size(); i++) {
                CIAffilie af = (CIAffilie) mgr.getEntity(i);
                if (!affSet.contains(af.getAffilieNumero())) {
                    /**/
                    options.append("<option value='");
                    options.append(af.getAffilieNumero());
                    /**/
                    options.append("' nom=\"");
                    TITiersViewBean ti = new TITiersViewBean();
                    ti.setSession(bsession);
                    ti.setIdTiers(af.getTiersId());
                    ti.retrieve();
                    options.append(ti.getNom());
                    options.append("\" idAffiliation='");
                    options.append(af.getAffiliationId());
                    options.append("' brancheeco='");
                    options.append(globaz.pavo.translation.CodeSystem.getCodeUtilisateur(af.getBrancheEconomique(),
                            bsession));
                    options.append("'");
                    options.append("brancheecoCode='");
                    options.append(af.getBrancheEconomique());
                    options.append("'");
                    /**/
                    /* ALD : Ajout : 2.04.2004 */// ///////
                    options.append(" lieu=\"");
                    options.append(ti.getLocaliteLong());
                    /* HNA Ajout de la langue */
                    options.append("\" ");
                    options.append(" langue=");
                    options.append("\"");
                    options.append(ti.getLangue());
                    options.append("\" ");

                    /**/
                    options.append(" typeAffiliation=");
                    options.append("\"");
                    options.append(af.getTypeAffiliation());
                    options.append("\" ");
                    /*****************************************************************
                     * ajouter le numéro AVS du tiers si c'est un indépendant-employeur besoin dans ecriture_de pour
                     * déterminer si gre 03 ou 01
                     *****************************************************************/
                    options.append(" numAvsTiers=");
                    options.append("\"");
                    options.append(ti.getNumAvsActuel());
                    options.append("\" ");
                    options.append(" valueEmp=");
                    options.append("\"");
                    options.append(af.getAffilieNumero());
                    options.append("\" ");
                    options.append(">");
                    options.append(af.getAffilieNumero());
                    options.append(" - ");
                    options.append(ti.getNom());
                    options.append("</option>");
                    affSet.add(af.getAffilieNumero());
                }
            }
        } catch (Exception ex) {
        }
        return options.toString();
    }

    /**
     * Retourne une liste des Affilies (dernier employeur) pour un compte individuel : numéro, nom et adresses de
     * l'affilié en fonction des premiers chiffres du numéro affilié donnés en paramètres. Date de création :
     * (17.09.2003 17:032:13)
     * 
     * @return la liste d'options (tag select) des affiliés existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les primers chiffres du numéro affilié
     * @param bsession
     *            la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getAffilliesForCompteIndividuel(String like, BSession bsession) {
        StringBuffer options = new StringBuffer();
        HashSet affSet = new HashSet();
        try {
            CIAffilieManager mgr = new CIAffilieManager();
            mgr.setSession(bsession);
            mgr.setLikeAffilieNumero(like);
            mgr.setForTypeFacturation(CIAffilieManager.PARITAIRE);
            mgr.find(BManager.SIZE_NOLIMIT);
            for (int i = 0; i < mgr.size(); i++) {
                CIAffilie af = (CIAffilie) mgr.getEntity(i);
                if (!affSet.contains(af.getAffilieNumero())) {
                    /**/
                    options.append("<option value='");
                    options.append(af.getAffilieNumero());
                    /**/
                    options.append("' idAffiliation='");
                    options.append(af.getAffiliationId());
                    options.append("'");
                    /**/
                    options.append("' nom=\"");
                    TITiersViewBean ti = new TITiersViewBean();
                    ti.setSession(bsession);
                    ti.setIdTiers(af.getTiersId());
                    ti.retrieve();
                    options.append(ti.getNom());
                    options.append("\"");
                    /**/
                    options.append("' adresse=\"");
                    options.append(ti.getLocaliteLong());
                    options.append("\"");
                    /**/
                    options.append(">");
                    options.append(af.getAffilieNumero());
                    options.append(" - ");
                    options.append(ti.getPrenomNom());
                    options.append("</option>");
                    affSet.add(af.getAffilieNumero());
                }
            }
        } catch (Exception ex) {
        }
        return options.toString();
    }

    /**
     * Retourne une liste des Affilies pour un journal, cad les employeurs et indépendants (numéro et nom de l'affilié)
     * en fonction des premiers chiffres du numéro affilié donnés en paramètres. Date de création : (17.09.2003
     * 17:032:13)
     * 
     * @return la liste d'options (tag select) des affiliés existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les primers chiffres du numéro affilié
     * @param bsession
     *            la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getAffilliesForJournal(String like, boolean afficherTypeAff, BSession bsession) {

        CIApplication ciApp = null;
        try {
            ciApp = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((like != null) && (like.indexOf('.') == -1)) {
            try {
                IFormatData affilieFormater = ciApp.getAffileFormater();
                if (affilieFormater != null) {
                    like = affilieFormater.format(like);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        StringBuffer options = new StringBuffer();
        HashSet affSet = new HashSet();
        try {

            CIAffilieManager mgr = new CIAffilieManager();
            mgr.setSession(bsession);
            mgr.setLikeAffilieNumero(like);
            mgr.setForTypeFacturation(CIAffilieManager.PARITAIRE);
            mgr.find(BManager.SIZE_NOLIMIT);
            for (int i = 0; i < mgr.size(); i++) {
                CIAffilie af = (CIAffilie) mgr.getEntity(i);
                if (!affSet.contains(af.getAffilieNumero())) {
                    /**/
                    options.append("<option value='");
                    options.append(af.getAffilieNumero());
                    /**/
                    options.append("' nom=\"");
                    TITiersViewBean ti = new TITiersViewBean();
                    ti.setSession(bsession);
                    ti.setIdTiers(af.getTiersId());
                    ti.retrieve();
                    options.append(ti.getNom());
                    options.append("\"");
                    /**/
                    options.append(" typeAffiliation=");
                    options.append("\"");
                    options.append(af.getTypeAffiliation());
                    options.append("\" ");
                    /**/
                    options.append(">");
                    options.append(af.getAffilieNumero());
                    options.append(" - ");
                    options.append(ti.getPrenomNom());
                    if (afficherTypeAff) {
                        options.append(" (");
                        options.append(CodeSystem.getLibelle(af.getTypeAffiliation(), bsession));
                        options.append(")");
                    }
                    options.append("</option>");

                    affSet.add(af.getAffilieNumero());
                }
            }
        } catch (Exception ex) {
        }
        return options.toString();
    }

    /**
     * Retourne une liste des Affilies (numéro et nom de l'affilié) en fonction des permiers chiffres du numéro affilié
     * donnés en paramètres. Date de création : (17.09.2003 17:032:13)
     * 
     * @return la liste d'options (tag select) des affiliés existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les primers chiffres du numéro affilié
     * @param forActif
     *            sélectionne que les affiliations actives
     * @param bsession
     *            la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getAffilliesInd(String like, boolean forActif, boolean forParitaire, String forIndependant,
            BSession bsession) {

        if (like != null) {
            // Si c'est inférieur à 3 => exception
            if (like.length() < 3) {
                throw new RuntimeException("Recherche pas assez discriminante");
            }

        }

        CIApplication ciApp = null;
        try {
            // ciApp = (CIApplication) bsession.getApplication();
            ciApp = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((like != null) && (like.indexOf('.') == -1)) {
            try {
                IFormatData affilieFormater = ciApp.getAffileFormater();
                if (affilieFormater != null) {
                    like = affilieFormater.format(like);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        StringBuffer options = new StringBuffer();
        HashSet affSet = new HashSet();
        try {
            CIAffilieManager mgr = new CIAffilieManager();
            mgr.setSession(bsession);
            mgr.setLikeAffilieNumero(like);
            mgr.setForActif(forActif);
            mgr.setForParitaire(forParitaire);
            mgr.setForIndependant(forIndependant);
            mgr.find(BManager.SIZE_NOLIMIT);
            for (int i = 0; i < mgr.size(); i++) {
                CIAffilie af = (CIAffilie) mgr.getEntity(i);
                if (!affSet.contains(af.getAffilieNumero() + "_" + af.getTypeAffiliation())) {
                    /**/
                    options.append("<option value='");
                    options.append(af.getAffilieNumero());
                    /**/
                    options.append("' nom=\"");
                    TITiersViewBean ti = new TITiersViewBean();
                    ti.setSession(bsession);
                    ti.setIdTiers(af.getTiersId());
                    ti.retrieve();
                    options.append(ti.getNom());
                    options.append("\" idAffiliation='");
                    options.append(af.getAffiliationId());
                    options.append("' brancheeco='");
                    options.append(globaz.pavo.translation.CodeSystem.getCodeUtilisateur(af.getBrancheEconomique(),
                            bsession));
                    options.append("'");
                    options.append("brancheecoCode='");
                    options.append(af.getBrancheEconomique());
                    options.append("'");
                    /**/
                    /* ALD : Ajout : 2.04.2004 */// ///////
                    options.append(" lieu=\"");
                    options.append(ti.getLocaliteLong());
                    options.append("\"");
                    /**/
                    options.append(" typeAffiliation=");
                    options.append("\"");
                    options.append(af.getTypeAffiliation());
                    options.append("\" ");
                    /*****************************************************************
                     * ajouter le numéro AVS du tiers si c'est un indépendant-employeur besoin dans ecriture_de pour
                     * déterminer si gre 03 ou 01
                     *****************************************************************/
                    options.append(" numAvsTiers=");
                    options.append("\"");
                    options.append(ti.getNumAvsActuel());
                    options.append("\" ");
                    options.append(" valueEmp=");
                    options.append("\"");
                    options.append(af.getAffilieNumero());
                    options.append("\" ");
                    options.append(" langue=");
                    options.append("\"");
                    options.append(ti.getLangue());
                    options.append("\" ");
                    options.append(">");
                    options.append(af.getAffilieNumero());
                    options.append(" - ");
                    options.append(ti.getNom());
                    options.append(" (");
                    options.append(CodeSystem.getLibelle(af.getTypeAffiliation(), bsession));
                    options.append(")");
                    options.append("</option>");
                    affSet.add(af.getAffilieNumero() + "_" + af.getTypeAffiliation());
                }
            }
        } catch (Exception ex) {
        }
        return options.toString();
    }

    /**
     * Retourne un employeur,un partenaire ou un organisme spécial (nom, prenom ou libellé) en fonction du numéro
     * d'affilié ou du numéro AVS du partenaire Date de création : (09.02.2005 15:023:15)
     * 
     * @return une string contenant un nom,prenom ou libellé
     * @param like
     *            les primers chiffres du numéro affilié
     * @param bsession
     *            la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getAffilliesNom(String like, BSession bsession) {

        if (JAUtil.isStringEmpty(like)) {
            return "";
        }
        StringBuffer options = new StringBuffer();
        /* Teste si la valeur de like est un numéro d'affilié */
        CIAffilieManager mgr = new CIAffilieManager();
        mgr.setSession(bsession);
        mgr.setLikeAffilieNumero(like);
        try {
            mgr.find(BManager.SIZE_NOLIMIT);
        } catch (Exception e1) {
            // TODO Bloc catch auto-généré
            e1.printStackTrace();
        }

        if (mgr.size() != 0) {

            CIAffilie af = (CIAffilie) mgr.getEntity(0);

            TITiersViewBean ti = new TITiersViewBean();
            ti.setSession(bsession);
            ti.setIdTiers(af.getTiersId());
            try {
                ti.retrieve();
            } catch (Exception e) {
                // TODO Bloc catch auto-généré
                e.printStackTrace();
            }
            options.append(ti.getNom());

        }
        /* Teste si la valeur de like correspond à un organisme spécial */
        if ("666.66.666.666".equals(like)) {
            options.append(bsession.getLabel("MSG_ECRITURE_MIL"));
        }

        if ("777.77.777.777".equals(like)) {
            options.append(bsession.getLabel("MSG_ECRITURE_APG"));
        }

        if ("888.88.888.888".equals(like)) {
            options.append(bsession.getLabel("MSG_ECRITURE_AI"));
        }

        if (like.startsWith("999.99.9")) {
            options.append(bsession.getLabel("MSG_ECRITURE_NOM_AC"));
        }
        /*
         * Si options est vide à ce point, c'est que la valeur de like correspond à un numéro AVS
         */
        if (JAUtil.isStringEmpty(options.toString())) {

            try {
                globaz.pavo.db.compte.CICompteIndividuelManager mgr1 = new globaz.pavo.db.compte.CICompteIndividuelManager();
                mgr1.setSession(bsession);
                mgr1.setLikeNumeroAvs(like);
                mgr1.find();

                globaz.pavo.db.compte.CICompteIndividuel ci = (globaz.pavo.db.compte.CICompteIndividuel) mgr1
                        .getEntity(0);

                options.append(ci.getNomPrenom());

            } catch (Exception ex) {
            }
        }

        /* Teste si la chaîne contient un guillemet */

        int pos = options.toString().indexOf("'");
        if (pos >= 0) {

            String res;
            res = options.toString().substring(0, pos);
            res = res.concat("&#146");
            res = res.concat(options.toString().substring(pos + 1));
            options.delete(0, options.length());
            options.append(res);

        }
        // return options.toString().toUpperCase();
        return options.toString();
    }

    public static ArrayList getAnnoncesABloquer() {

        try {

            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            return application.getAnnoncesABloquer();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public static int getAutoDigitAff(BSession session) {

        try {
            CIApplication application = (CIApplication) session.getApplication();
            return application.getAutoDigitAffilie();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Retourne une liste de CI (numéro et nom de l'assuré) en fonction des permiers chiffres du numéro avs donnés en
     * paramètres. Date de création : (10.03.2004 09:05:13)
     * 
     * @return le numéro d'affilié des ticket étudiants
     */
    public static int getAutoDigitAff(HttpSession session) {
        BSession bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
        try {
            CIApplication application = (CIApplication) bSession.getApplication();
            return application.getAutoDigitAffilie();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Retourne une liste de CI (numéro et nom de l'assuré) en fonction des permiers chiffres du numéro avs donnés en
     * paramètres. Date de création : (25.02.2003 09:05:13)=
     * 
     * @return la liste d'options (tag select) des CI existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les primers chiffres du numéro avs de l'assuré
     * @param bsession
     *            la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getAvailableCI(String like, String isNNSS, BSession bsession) {
        long time = System.currentTimeMillis();
        StringBuffer options = new StringBuffer();
        try {
            globaz.pavo.db.compte.CICompteIndividuelManager mgr = new globaz.pavo.db.compte.CICompteIndividuelManager();
            mgr.setSession(bsession);
            mgr.setQuickSearch(true);

            /*
             * mgr.setFromNumeroAvs(like); StringBuffer until = new StringBuffer(mgr.getFromNumeroAvs()); for (int i =
             * until.length(); i < 11; i++) { until.append('9'); } mgr.setUntilNumeroAvs(until.toString());
             */

            mgr.setLikeNumeroAvs(like);
            mgr.setLikeNumeroAvsNNSS(isNNSS);
            mgr.setForRegistre(globaz.pavo.db.compte.CICompteIndividuel.CS_REGISTRE_ASSURES);
            // mgr.setOrderBy("KANAVS");
            mgr.changeManagerSize(10);
            mgr.find();
            // System.out.println("after find: "+(System.currentTimeMillis()-time)+"ms");
            for (int i = 0; (i < mgr.size()) && (i < 10); i++) {
                globaz.pavo.db.compte.CICompteIndividuel ci = (globaz.pavo.db.compte.CICompteIndividuel) mgr
                        .getEntity(i);
                /**/
                options.append("<option value='");
                options.append(NSUtil.formatWithoutPrefixe(ci.getNumeroAvs(), ci.getNnss().booleanValue()));
                /**/
                options.append("' nom=\"");
                options.append(ci.getNomPrenom());
                options.append("\"");
                /**/
                options.append(" idci='");
                options.append(ci.getCompteIndividuelId());
                options.append("'");
                /**/
                options.append(" valueEmp='");
                options.append(ci.getNssFormate());
                options.append("'");
                /**/
                options.append(" sexe='");
                options.append(ci.getSexe());
                options.append("'");
                /**/
                options.append(" date='");
                options.append(ci.getDateNaissance());
                options.append("'");
                /**/
                options.append(" sexeFormate='");
                options.append(ci.getSexeLibelle());
                options.append("'");
                /**/
                options.append(" paysFormate='");
                options.append(ci.getPaysFormate());
                options.append("'");
                /**/
                options.append(" etatFormate='");
                options.append(ci.getEtatFormate());
                options.append("'");
                /**/
                options.append(" paysCode='");
                options.append(globaz.pavo.translation.CodeSystem.getCodeUtilisateur(ci.getPaysOrigineId(), bsession));
                options.append("'");
                /**/
                options.append(" paysLibelle=\"");
                options.append(globaz.pavo.translation.CodeSystem.getLibelle(ci.getPaysOrigineId(), bsession));
                options.append("\"");
                /**/
                options.append(" ecritures=\"");
                options.append(ci.getDernieresEcritures());
                options.append("\"");
                /**/
                options.append(">");

                options.append(ci.getNssFormate());
                options.append(" - ");
                options.append(ci.getNomPrenom() + " " + ci.getEtatFormate());
                options.append("</option>");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        // System.out.println("after build select: "+(System.currentTimeMillis()-time)+"ms");
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
    public static String getAvailableCI(String like, String isNNSS, HttpSession session) {
        return CIUtil.getAvailableCI(like, isNNSS,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * Retourne une liste de CI (numéro et nom de l'assuré) en fonction des permiers chiffres du numéro avs donnés en
     * paramètres, sans le CI précisé dans except et sans les CI possédant une référence vers un autre CI. Date de
     * création : (12.08.2003 16:00:00)=
     * 
     * @return la liste d'options (tag select) des CI existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les primers chiffres du numéro avs de l'assuré
     * @param except
     *            id d'un CI à ignorer
     * @param bsession
     *            la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getAvailableCIForRef(String like, String except, String isNNSS, BSession bsession) {
        long time = System.currentTimeMillis();
        StringBuffer options = new StringBuffer();
        try {
            globaz.pavo.db.compte.CICompteIndividuelManager mgr = new globaz.pavo.db.compte.CICompteIndividuelManager();
            mgr.setSession(bsession);
            mgr.setQuickSearch(true);
            /*
             * mgr.setFromNumeroAvs(like); StringBuffer until = new StringBuffer(mgr.getFromNumeroAvs()); for (int i =
             * until.length(); i < 11; i++) { until.append('9'); } mgr.setUntilNumeroAvs(until.toString());
             */
            mgr.setLikeNumeroAvs(like);
            mgr.setForNotCompteIndividuelId(except);
            // mgr.setForCompteIndividuelReferenceMaitre(new Boolean(true));
            // mgr.setForCompteIndividuelReferenceId("0");
            mgr.setForRegistre(globaz.pavo.db.compte.CICompteIndividuel.CS_REGISTRE_ASSURES);
            mgr.setLikeNumeroAvsNNSS(isNNSS);
            // mgr.setOrderBy("KANAVS");
            // mgr.changeManagerSize(20);
            mgr.find();
            // System.out.println("after find: "+(System.currentTimeMillis()-time)+"ms");
            for (int i = 0; i < mgr.size(); i++) {
                globaz.pavo.db.compte.CICompteIndividuel ci = (globaz.pavo.db.compte.CICompteIndividuel) mgr
                        .getEntity(i);
                /**/
                options.append("<option value='");
                options.append(NSUtil.formatWithoutPrefixe(ci.getNumeroAvs(), ci.getNnss().booleanValue()));
                /**/
                options.append("' nom=\"");
                options.append(ci.getNomPrenom());
                options.append("\"");
                /**/
                options.append(" idci='");
                options.append(ci.getCompteIndividuelId());
                options.append("'");
                /**/
                options.append(" sexe='");
                options.append(ci.getSexe());
                options.append("'");
                /**/
                options.append(" date='");
                options.append(ci.getDateNaissance());
                options.append("'");
                /**/
                options.append(" paysCode='");
                options.append(globaz.pavo.translation.CodeSystem.getCodeUtilisateur(ci.getPaysOrigineId(), bsession));
                options.append("'");
                /**/
                options.append(" paysLibelle=\"");
                options.append(globaz.pavo.translation.CodeSystem.getLibelle(ci.getPaysOrigineId(), bsession));
                options.append("\"");
                /**/
                options.append(" ecritures=\"");
                options.append(ci.getDernieresEcritures());
                options.append("\"");
                /**/
                options.append(">");
                options.append(ci.getNssFormate());
                options.append(" - ");
                options.append(ci.getNomPrenom());
                options.append("</option>");
            }
        } catch (Exception ex) {
        }
        // System.out.println("after build select: "+(System.currentTimeMillis()-time)+"ms");
        return options.toString();
    }

    /**
     * Retourne une liste de CI (numéro et nom de l'assuré) en fonction des permiers chiffres du numéro avs donnés en
     * paramètres, sans le CI précisé dans except et sans les CI possédant une référence vers un autre CI. Date de
     * création : (12.08.2003 16:00:00)=
     * 
     * @return la liste d'options (tag select) des CI existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les primers chiffres du numéro avs de l'assuré
     * @param except
     *            id d'un CI à ignorer
     * @param session
     *            la session HTTP actuelle
     */
    public static String getAvailableCIForRef(String like, String except, String isNNSS, HttpSession session) {
        return CIUtil.getAvailableCIForRef(like, except, isNNSS,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    public static String getAvailableCIWithRegistre(String registre, String like, String isNNSS, BSession bsession) {
        long time = System.currentTimeMillis();
        StringBuffer options = new StringBuffer();
        try {
            globaz.pavo.db.compte.CICompteIndividuelManager mgr = new globaz.pavo.db.compte.CICompteIndividuelManager();
            mgr.setSession(bsession);
            mgr.setQuickSearch(true);
            /*
             * mgr.setFromNumeroAvs(like); StringBuffer until = new StringBuffer(mgr.getFromNumeroAvs()); for (int i =
             * until.length(); i < 11; i++) { until.append('9'); } mgr.setUntilNumeroAvs(until.toString());
             */
            mgr.setLikeNumeroAvs(like);
            mgr.setLikeNumeroAvsNNSS(isNNSS);
            mgr.setForRegistre(registre);
            // mgr.setOrderBy("KANAVS");
            mgr.changeManagerSize(10);
            mgr.find();
            // System.out.println("after find: "+(System.currentTimeMillis()-time)+"ms");
            for (int i = 0; (i < mgr.size()) && (i < 10); i++) {
                globaz.pavo.db.compte.CICompteIndividuel ci = (globaz.pavo.db.compte.CICompteIndividuel) mgr
                        .getEntity(i);
                /**/
                options.append("<option value='");
                options.append(NSUtil.formatWithoutPrefixe(ci.getNumeroAvs(), ci.getNnss().booleanValue()));
                /**/
                options.append("' idci='");
                options.append(ci.getCompteIndividuelId());
                options.append("'");
                /**/
                options.append(">");
                options.append(ci.getNssFormate());
                options.append(" - ");
                options.append(ci.getNomPrenom() + " - " + ci.getEtatFormate());
                options.append("</option>");
            }
        } catch (Exception ex) {
        }
        // System.out.println("after build select: "+(System.currentTimeMillis()-time)+"ms");
        return options.toString();
    }

    public static int returnNombreEcitureByIdAffiliation(String idAffiliation, BSession session) throws Exception {
        if (JadeStringUtil.isBlankOrZero(idAffiliation)) {
            throw new Exception("Insufficient number of arguments");
        }

        CIEcritureManager manager = new CIEcritureManager();
        manager.setSession(session);
        manager.setForEmployeur(idAffiliation);
        return manager.getCount();

    }

    public static String getAvailableCIWithRegistre(String registre, String like, String isNNSS, HttpSession session) {
        return CIUtil.getAvailableCIWithRegistre(registre, like, isNNSS,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    public static String getCaisseCompForCI(String like, BSession session) {
        StringBuffer options = new StringBuffer();
        HashSet ccSet = new HashSet();
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

    public static String getCaisseCompForCI(String like, HttpSession session) {
        return CIUtil.getCaisseCompForCI(like,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * Retourne la caisse du numéro avs donnés en paramètres. Date de création : (14.062004 09:05:13)
     * 
     * @return la caisse
     */
    public static String getCaisseInterne(BSession bSession) {

        try {
            CIApplication application = (CIApplication) bSession.getApplication();
            return application.getCaisseInterne();
        } catch (Exception e) {
            return new String();
        }

    }

    public static String getCaisseInterneEcran() {
        try {
            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            return application.getCaisseInterne();
        } catch (Exception e) {
            return "";
        }

    }

    public static ArrayList getChampsAffiche(HttpSession session) {
        BSession bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
        try {
            CIApplication application = (CIApplication) bSession.getApplication();
            return application.getChampsAffiche();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    /**
     * méthode qui retourne une ArrayList contenant les CS des type d'anomalies à comparer
     * 
     * @return
     */
    public static ArrayList getCodesAComparer() {

        try {
            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            return application.getCodesComparaisonAComparer();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    /**
     * méthode qui retourne une ArrayList contenant les CS des type d'anomalies à corriger
     * 
     * @return
     */
    public static ArrayList getCodesACorriger() {

        try {
            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            return application.getCodesComparaisonAcorriger();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public static ArrayList getColonnesAMasquer(HttpSession session) {
        BSession bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
        try {
            CIApplication application = (CIApplication) bSession.getApplication();
            return application.getColonneAMasquer();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    /**
     * Renvoie les pays triés pour le taglib
     */
    public static Vector getCountries(BSession session) {
        try {
            Vector vList = new Vector();
            String[] list = new String[2];
            // on récupère les codes systèmes des pays
            FWParametersSystemCodeManager csPaysListe = new FWParametersSystemCodeManager();
            csPaysListe.setForIdGroupe("CIPAYORI");
            csPaysListe.setForIdTypeCode("10300015");
            csPaysListe.setSession(session);
            csPaysListe.find(BManager.SIZE_NOLIMIT);
            vList = new Vector(csPaysListe.size());
            for (int i = 0; i < csPaysListe.size(); i++) {
                list = new String[2];
                FWParametersSystemCode entity = (FWParametersSystemCode) csPaysListe.getEntity(i);
                String codePays = entity.getCurrentCodeUtilisateur().getCodeUtilisateur();
                // la valeur du tag OPTION dans le SELECT (ici le code pays)
                list[0] = entity.getIdCode();
                // le libellé
                list[1] = entity.getCurrentCodeUtilisateur().getLibelle() + " (" + codePays + ")";
                vList.add(list);
            }
            // on tri manuellement par ordre alphabétique en utilisant un tri à
            // bulle
            String[] first = new String[2];
            String[] second = new String[2];
            for (int j = 0; j < vList.size(); j++) {
                first = (String[]) vList.elementAt(j);
                for (int k = j + 1; k < vList.size(); k++) {
                    second = (String[]) vList.elementAt(k);
                    if ((first[1]).compareTo(second[1]) > 0) {
                        vList.setElementAt(second, j);
                        vList.setElementAt(first, k);
                        first = (String[]) vList.elementAt(j);
                    }
                }
            }
            return vList;
        } catch (Exception e) {
            // si probleme, retourne list vide.
            return new Vector();
        }
    }

    public static String getCSDefaultDS(HttpSession HtSession) {
        try {
            BSession session = (BSession) ((FWController) HtSession.getAttribute("objController")).getSession();
            CIApplication application = (CIApplication) session.getApplication();
            return application.getDefaultCSForDS();
        } catch (Exception e) {
            return "327007";
        }
    }

    public static String getDateRetraiteAc(String noAVS, int annee, BSession session) {
        if (JadeStringUtil.isBlankOrZero(noAVS)) {
            return "31.12.2999";

        }
        CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
        ciMgr.setSession(session);
        ciMgr.setForNumeroAvs(noAVS);
        ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
        try {
            ciMgr.find();

            if (ciMgr.size() <= 0) {
                ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
                ciMgr.find();
            }
        } catch (Exception e) {
            return "0";
        }
        if (ciMgr.size() > 0) {
            CICompteIndividuel ci = (CICompteIndividuel) ciMgr.getFirstEntity();
            if (!ci.getNnss().booleanValue()) {
                ci.calculSexe();
            }
            int ageRetraite;
            if (CICompteIndividuel.CS_FEMME.equals(ci.getSexe())) {
                // femme
                if (annee <= 2000) {
                    ageRetraite = 62;
                } else if (annee < 2005) {
                    ageRetraite = 63;
                } else {
                    ageRetraite = 64;
                }
            } else {
                // homme
                ageRetraite = 65;
            }
            int anneeRetraite = 0;
            String dateNaissance = ci.getDateNaissance();
            if (JadeStringUtil.isIntegerEmpty(dateNaissance)) {
                if (!ci.getNnss().booleanValue()) {
                    ci.calculDateNaissance();
                } else if (ci.getNnss().booleanValue()) {
                    // Si c'est un nouveau numéro et qu'on ne connaît pas la
                    // date de naissance, on soumet au chômage
                    return "31.12.2999";
                }
            }
            dateNaissance = ci.getDateNaissance();
            if (!JadeStringUtil.isBlankOrZero(dateNaissance)) {
                try {
                    JADate cal = new JADate(dateNaissance);
                    // String dateRet = cal.toStrAMJ();
                    anneeRetraite = cal.getYear() + ageRetraite;
                    // Retraite début du mois d'après
                    String dateRetraite = "";
                    if ("12".equals(JadeStringUtil.rightJustifyInteger(String.valueOf(cal.getMonth()), 2))) {
                        dateRetraite = "01.01." + String.valueOf(anneeRetraite + 1);
                    } else {
                        dateRetraite = "01" + "."
                                + JadeStringUtil.rightJustifyInteger(String.valueOf(cal.getMonth() + 1), 2) + "."
                                + String.valueOf(anneeRetraite);
                    }
                    return dateRetraite;
                } catch (Exception e) {
                }

            }

            // anneeRetraite
            return "0";
        } else {
            return "31.12.2099";
        }

    }

    public static ArrayList getDernieresEcrituresList(HttpSession session, String compteIndividuelId) {
        ArrayList list = new ArrayList();
        if ((compteIndividuelId != null) && (compteIndividuelId.length() != 0)
                && !JAUtil.isStringEmpty(compteIndividuelId)) {
            try {
                CIEcritureManager ecrMgr = new CIEcritureManager();
                ecrMgr.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
                ecrMgr.setForIdTypeCompteCompta("true");
                ecrMgr.setForCompteIndividuelId(compteIndividuelId);
                ecrMgr.setOrder("KBNANN DESC, KBNMOF DESC");
                ecrMgr.changeManagerSize(20);
                ecrMgr.setCacherEcritureProtege(1);
                ecrMgr.find();
                for (int i = 0; i < ecrMgr.size(); i++) {
                    ArrayList line = new ArrayList();
                    CIEcriture ecr = (CIEcriture) ecrMgr.getEntity(i);
                    line.add(ecr.getEmployeurPartenaireForDisplay());
                    line.add(ecr.getGreFormat());
                    line.add(JadeStringUtil.rightJustifyInteger(ecr.getMoisDebut(), 2) + "-"
                            + JadeStringUtil.rightJustifyInteger(ecr.getMoisFin(), 2) + "." + ecr.getAnnee());
                    line.add(ecr.getMontantSigne());
                    line.add(CodeSystem.getCodeUtilisateur(ecr.getCode(), session));
                    if (!JadeStringUtil.isIntegerEmpty(ecr.getRassemblementOuvertureId())) {
                        CIRassemblementOuverture rass = new CIRassemblementOuverture();
                        rass.setRassemblementOuvertureId(ecr.getRassemblementOuvertureId());
                        rass.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
                        rass.retrieve();
                        line.add(rass.getMotifArc());
                    } else {
                        line.add("");
                    }
                    list.add(line);
                }
            } catch (Exception ex) {
                // laisser le buffer vide
            }
        }
        return list;
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
            CIApplication application = (CIApplication) bSession.getApplication();

            numeroAffFormater = application.getAffileFormater();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numeroAffFormater;
    }

    public static String getIdRubrique(BSession session) {
        try {
            CIApplication application = (CIApplication) session.getApplication();
            return application.getIdRubrique();
        } catch (Exception e) {
            return "18";
        }
    }

    public static ArrayList getMasseSalarialePaAnnee(BSession session, String numeroAffilie, String anneeDebut,
            String anneeFin) {

        int fromAnneeBoucle;
        int untilAnneeBoucle;

        if (JadeStringUtil.isBlank(anneeDebut)) {
            fromAnneeBoucle = Calendar.getInstance().get(java.util.Calendar.YEAR) - 2;
        } else {
            fromAnneeBoucle = Integer.parseInt(anneeDebut);
        }
        if (JadeStringUtil.isBlank(anneeFin)) {
            untilAnneeBoucle = Calendar.getInstance().get(java.util.Calendar.YEAR) - 1;
        } else {
            untilAnneeBoucle = Integer.parseInt(anneeFin);
        }
        ArrayList list = new ArrayList();
        for (int annee = fromAnneeBoucle; annee <= untilAnneeBoucle; annee++) {
            /*
             * CIAffilieManager affMgr= new CIAffilieManager(); affMgr.setSession((BSession) ((FWController)
             * session.getAttribute("objController")).getSession());
             * 
             * ArrayList line = new ArrayList(); CIEcritureManager mgrEcrituresPositives = new CIEcritureManager();
             * CIEcritureManager mgrEcrituresNegatives = new CIEcritureManager();
             * mgrEcrituresPositives.setSession((BSession) ((FWController)
             * session.getAttribute("objController")).getSession());
             * mgrEcrituresPositives.setForEmployeur(idAffiliation);
             * mgrEcrituresPositives.setForAnnee(String.valueOf(annee)); mgrEcrituresPositives.setForExtourne("0");
             * mgrEcrituresPositives.setForMasseSalariale("True"); BigDecimal sommePos= new BigDecimal("0"); try{
             * sommePos=mgrEcrituresPositives.getSum("KBMMON"); }catch(Exception e){ e.printStackTrace(); }
             * mgrEcrituresNegatives.setSession((BSession) ((FWController)
             * session.getAttribute("objController")).getSession());
             * mgrEcrituresNegatives.setForEmployeur(idAffiliation);
             * mgrEcrituresNegatives.setForAnnee(String.valueOf(annee)); mgrEcrituresNegatives.setForNotExtourne("0");
             * mgrEcrituresNegatives.setForMasseSalariale("True"); BigDecimal sommeNeg= new BigDecimal("0"); try{
             * sommeNeg=mgrEcrituresNegatives.getSum("KBMMON"); }catch(Exception e){ e.printStackTrace(); }
             */
            String resultCA = "0";
            CACompteAnnexeManager mgrMasse = new CACompteAnnexeManager();
            // CACompteur cptr = new CACompteur();
            CACompteurManager mgrCompteur = new CACompteurManager();
            mgrMasse.setSession(session);
            mgrMasse.setForIdExterneRole(numeroAffilie);
            try {
                CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                        CIApplication.DEFAULT_APPLICATION_PAVO);
                mgrMasse.setForIdRole(CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(application));

                mgrMasse.find();

                if (mgrMasse.size() != 0) {
                    CACompteAnnexe compte = new CACompteAnnexe();
                    compte = (CACompteAnnexe) mgrMasse.getFirstEntity();
                    mgrCompteur.setSession(session);
                    mgrCompteur.setForAnnee(String.valueOf(annee));
                    mgrCompteur.setForIdCompteAnnexe(compte.getIdCompteAnnexe());
                    mgrCompteur.setForIdRubriqueIn((CIUtil.getIdRubrique(session)));
                    try {
                        resultCA = mgrCompteur.getSum("CUMULMASSE").toString();
                    } catch (Exception e) {
                        resultCA = "0";
                    }
                }

            } catch (Exception e) {
                System.out.println(e.toString());
            }
            // maintentant on regarde la somme inscrite aux CI
            // On doit retrouver la bonne afffiliation pour l'année en cours
            // String idAffiliation = new String();
            try {
                // BSession bSession = new BSession();
                // bSession = session;
                // CIApplication application = (CIApplication)
                // bSession.getApplication();
                AFAffiliationManager affMgr = new AFAffiliationManager();
                affMgr.setForAffilieNumero(numeroAffilie);
                affMgr.setSession(session);
                affMgr.find();

                BigDecimal result = new BigDecimal("0");
                for (int i = 0; i < affMgr.size(); i++) {
                    AFAffiliation aff = new AFAffiliation();
                    aff = (AFAffiliation) affMgr.getEntity(i);
                    // si l'affiliation est nulle, on laisse 0 comme valeur pour
                    // le cumul
                    if (aff != null) {
                        // idAffiliation = aff.getAffiliationId();
                        CIEcrituresSomme mgrSomme = new CIEcrituresSomme();
                        mgrSomme.setForEmployeur(aff.getAffiliationId());
                        mgrSomme.setForAnnee(String.valueOf(annee));
                        mgrSomme.setForMasseSalariale("true");
                        mgrSomme.setSession(session);
                        mgrSomme.changeManagerSize(BManager.SIZE_NOLIMIT);
                        result = result.add(mgrSomme.getSum("KBMMON", null));
                    }
                }
                CIInscriptionsManuellesManager correction = new CIInscriptionsManuellesManager();
                correction.setSession(session);
                correction.setForAffilie(affMgr.getForAffilieNumero());
                correction.setForAnnee(String.valueOf(annee));
                correction.find();

                result = result.add(correction.getSum("KSMMON"));
                ArrayList line = new ArrayList();
                line.add(String.valueOf(annee));
                line.add(new FWCurrency(String.valueOf(result)).toStringFormat());
                line.add(new FWCurrency(String.valueOf(resultCA)).toStringFormat());
                // line.add();

                line.add(new FWCurrency(String.valueOf(result.subtract(new BigDecimal(resultCA)))).toStringFormat());
                list.add(line);

            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println(e.toString());
            }

        }
        return list;

    }

    /**
     * Method getMasseSalarialePaAnnée.
     * 
     * @param session
     * @param idAffiliation
     * @return ArrayList
     */
    public static ArrayList getMasseSalarialePaAnnee(HttpSession session, String numeroAffilie, String anneeDebut,
            String anneeFin) {

        int fromAnneeBoucle;
        int untilAnneeBoucle;

        if (JAUtil.isStringEmpty(anneeDebut)) {
            fromAnneeBoucle = Calendar.getInstance().get(java.util.Calendar.YEAR) - 2;
        } else {
            fromAnneeBoucle = Integer.parseInt(anneeDebut);
        }
        if (JAUtil.isStringEmpty(anneeFin)) {
            untilAnneeBoucle = Calendar.getInstance().get(java.util.Calendar.YEAR) - 1;
        } else {
            untilAnneeBoucle = Integer.parseInt(anneeFin);
        }
        ArrayList list = new ArrayList();
        for (int annee = fromAnneeBoucle; annee <= untilAnneeBoucle; annee++) {
            /*
             * CIAffilieManager affMgr= new CIAffilieManager(); affMgr.setSession((BSession) ((FWController)
             * session.getAttribute("objController")).getSession());
             * 
             * ArrayList line = new ArrayList(); CIEcritureManager mgrEcrituresPositives = new CIEcritureManager();
             * CIEcritureManager mgrEcrituresNegatives = new CIEcritureManager();
             * mgrEcrituresPositives.setSession((BSession) ((FWController)
             * session.getAttribute("objController")).getSession());
             * mgrEcrituresPositives.setForEmployeur(idAffiliation);
             * mgrEcrituresPositives.setForAnnee(String.valueOf(annee)); mgrEcrituresPositives.setForExtourne("0");
             * mgrEcrituresPositives.setForMasseSalariale("True"); BigDecimal sommePos= new BigDecimal("0"); try{
             * sommePos=mgrEcrituresPositives.getSum("KBMMON"); }catch(Exception e){ e.printStackTrace(); }
             * mgrEcrituresNegatives.setSession((BSession) ((FWController)
             * session.getAttribute("objController")).getSession());
             * mgrEcrituresNegatives.setForEmployeur(idAffiliation);
             * mgrEcrituresNegatives.setForAnnee(String.valueOf(annee)); mgrEcrituresNegatives.setForNotExtourne("0");
             * mgrEcrituresNegatives.setForMasseSalariale("True"); BigDecimal sommeNeg= new BigDecimal("0"); try{
             * sommeNeg=mgrEcrituresNegatives.getSum("KBMMON"); }catch(Exception e){ e.printStackTrace(); }
             */
            String resultCA = new String();
            CACompteAnnexeManager mgrMasse = new CACompteAnnexeManager();
            CACompteur cptr = new CACompteur();
            mgrMasse.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
            mgrMasse.setLikeNumNom(numeroAffilie);
            try {
                CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                        CIApplication.DEFAULT_APPLICATION_PAVO);
                mgrMasse.setForIdRole(CaisseHelperFactory.getInstance().getRoleForAffilieParitaire(application));

                mgrMasse.find();
            } catch (Exception e) {
            }
            if (mgrMasse == null) {
                resultCA = "0";
            } else {
                try {

                    CACompteAnnexe compte = new CACompteAnnexe();
                    compte = (CACompteAnnexe) mgrMasse.getFirstEntity();
                    CACompteurManager mgrCompteur = new CACompteurManager();
                    mgrCompteur.setSession((BSession) ((FWController) session.getAttribute("objController"))
                            .getSession());
                    mgrCompteur.setForAnnee(String.valueOf(annee));
                    mgrCompteur.setForIdCompteAnnexe(compte.getIdCompteAnnexe());
                    mgrCompteur.setForIdRubriqueIn((CIUtil.getIdRubrique(mgrCompteur.getSession())));
                    try {
                        resultCA = mgrCompteur.getSum("CUMULMASSE").toString();

                    } catch (Exception e) {
                        resultCA = "0";
                    }
                    /*
                     * try { mgrCompteur.find(); } catch (Exception e) {
                     * 
                     * } cptr = (CACompteur)mgrCompteur.getFirstEntity(); if(cptr!=null){ resultCA =
                     * cptr.getCumulMasse(); }else{ resultCA="0"; }
                     */
                } catch (Exception e) {
                    resultCA = "0";
                }

            }
            // maintentant on regarde la somme inscrite aux CI
            // On doit retrouver la bonne afffiliation pour l'année en cours
            String idAffiliation = new String();
            try {
                BSession bSession = new BSession();
                bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
                // CIApplication application = (CIApplication)
                // bSession.getApplication();
                AFAffiliationManager affMgr = new AFAffiliationManager();
                affMgr.setForAffilieNumero(numeroAffilie);
                affMgr.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
                affMgr.setForTypesAffParitaires();
                affMgr.find();

                BigDecimal result = new BigDecimal("0");
                for (int i = 0; i < affMgr.size(); i++) {
                    AFAffiliation aff = new AFAffiliation();
                    aff = (AFAffiliation) affMgr.getEntity(i);
                    // si l'affiliation est nulle, on laisse 0 comme valeur pour
                    // le cumul
                    if (aff != null) {
                        idAffiliation = aff.getAffiliationId();
                        CIEcrituresSomme mgrSomme = new CIEcrituresSomme();
                        mgrSomme.setForEmployeur(aff.getAffiliationId());
                        mgrSomme.setForAnnee(String.valueOf(annee));
                        mgrSomme.setForMasseSalariale("true");
                        mgrSomme.setSession((BSession) ((FWController) session.getAttribute("objController"))
                                .getSession());
                        mgrSomme.changeManagerSize(BManager.SIZE_NOLIMIT);
                        result = result.add(mgrSomme.getSum("KBMMON", null));
                    }
                }
                CIInscriptionsManuellesManager correction = new CIInscriptionsManuellesManager();
                correction.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
                correction.setForAffilie(affMgr.getForAffilieNumero());
                correction.setForAnnee(String.valueOf(annee));
                correction.find();

                result = result.add(correction.getSum("KSMMON"));

                ArrayList line = new ArrayList();
                line.add(String.valueOf(annee));
                line.add(new FWCurrency(String.valueOf(result)).toStringFormat());
                line.add(new FWCurrency(String.valueOf(resultCA)).toStringFormat());
                // line.add();

                line.add(new FWCurrency(String.valueOf(result.subtract(new BigDecimal(resultCA)))).toStringFormat());
                list.add(line);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return list;

    }

    public static int getNbAssuresNNSS(BSession session) {

        try {
            CIApplication application = (CIApplication) session.getApplication();
            return application.getNbAssuresNNSS();
        } catch (Exception e) {
            return 20;
        }
    }

    /**
     * Retourne une liste de CI (numéro et nom de l'assuré) en fonction des permiers chiffres du numéro avs donnés en
     * paramètres. Date de création : (10.03.2004 09:05:13)
     * 
     * @return le numéro d'affilié des ticket étudiants
     */
    public static String getNumeroTicketEtudiants(HttpSession session) {
        BSession bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
        try {
            CIApplication application = (CIApplication) bSession.getApplication();
            return application.getNumeroTicketsEtuditants();
        } catch (Exception e) {
            return new String();
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

    public static ArrayList getToutesEcrituresList(HttpSession session, String compteIndividuelId) {
        ArrayList list = new ArrayList();
        if ((compteIndividuelId != null) && (compteIndividuelId.length() != 0)) {
            try {
                CIEcritureManager ecrMgr = new CIEcritureManager();
                ecrMgr.setSession((BSession) ((FWController) session.getAttribute("objController")).getSession());
                ecrMgr.setForIdTypeCompteCompta("true");
                ecrMgr.setForCompteIndividuelId(compteIndividuelId);
                ecrMgr.setOrder("KBNANN DESC, KBNMOF DESC");
                ecrMgr.setCacherEcritureProtege(1);
                ecrMgr.find(BManager.SIZE_NOLIMIT);
                for (int i = 0; i < ecrMgr.size(); i++) {
                    ArrayList line = new ArrayList();
                    CIEcriture ecr = (CIEcriture) ecrMgr.getEntity(i);
                    line.add(ecr.getEmployeurPartenaireForDisplay());
                    line.add(ecr.getGreFormat());
                    line.add(JadeStringUtil.rightJustifyInteger(ecr.getMoisDebut(), 2) + "-"
                            + JadeStringUtil.rightJustifyInteger(ecr.getMoisFin(), 2) + "." + ecr.getAnnee());
                    line.add(ecr.getMontantSigne());
                    line.add(CodeSystem.getCodeUtilisateur(ecr.getCode(), session));
                    list.add(line);
                }
            } catch (Exception ex) {
                // laisser le buffer vide
            }
        }
        return list;
    }

    public static int getUserAccessLevel(BSession session) {
        try {
            FWSecureUserDetail user = new FWSecureUserDetail();
            user.setSession(session);
            user.setUser(session.getUserId());
            user.setLabel(CICompteIndividuel.SECURITE_LABEL);
            user.retrieve();
            if (!user.isNew()) {
                return Integer.parseInt(user.getData());
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getUserAccessLevel(HttpSession session) {
        try {
            BSession ses = (BSession) ((FWController) session.getAttribute("objController")).getSession();
            return CIUtil.getUserAccessLevel(ses);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Retourne le nom complet d'un utilisateur en fonction d'une abbréviation. Date de création : (09.01.2003 15:20:45)
     * 
     * @return le nom complet
     * @param abr
     *            l'abbréviation
     * @param transaction
     *            la transaction à utiliser.
     * @see FWSecureUser
     */
    public static String getUserNomComplet(String abr, BTransaction transaction) {
        String result = "";
        if ((abr != null) && !JAUtil.isStringEmpty(abr)) {
            // non vide -> recherche
            FWSecureUser user = new FWSecureUser();
            user.setSession(transaction.getSession());
            user.setUser(abr);
            try {
                user.retrieve(transaction);
                // si transaction pas null
                if (transaction != null) {
                    if (!transaction.hasErrors() && !user.isNew()) {
                        result = user.getFirstname() + " " + user.getLastname();
                    } else {
                        if (JAUtil.isStringEmpty(result)) {
                            // si utilisateur trouvé mais sans nom -> id
                            result = abr;
                        }

                    }
                } else {
                    // sinon (modifications car des fois la transaction comporte
                    // des erreurs et du coup on ne reprenait jamais le nom du
                    // user
                    if (!user.hasErrors()) {
                        result = user.getFirstname() + " " + user.getLastname();
                    } else {

                        if (JAUtil.isStringEmpty(result)) {
                            // si utilisateur trouvé mais sans nom -> id
                            result = abr;
                        }
                    }
                }
            } catch (Exception e) {
                // laisser userid
            }
        }
        return result;
    }

    public static String inc(String st) {
        st = st.trim();
        try {
            int intStr = Integer.parseInt(st);
            return String.valueOf(intStr + 1);
        } catch (Exception e) {
            return st;
        }
    }

    public static String inc(String st, int inc) {
        st = st.trim();
        try {
            int intStr = Integer.parseInt(st);
            return String.valueOf(intStr + inc);
        } catch (Exception e) {
            return st;
        }
    }

    /**
     * Teste si la date est située plus de 30 ans en arrière<br>
     * 
     * @param date
     *            la date testée (JJ.MM.AAAA)
     * @return true si y'a plus de 30 ans, false sinon
     * @throws IllegalStateException
     *             si la date est mal formée
     * @throws NullPointerException
     *             : cette exception est levée si la date est nulle
     */
    public static boolean is30YearsAgo(String date) {

        if (date == null) {
            throw new NullPointerException("la date ne doit pas être nulle");
        }

        // la date à partir de JJ.MM.AAAA
        String simpleDateFormat = "dd.MM.yyyy";
        SimpleDateFormat from = new SimpleDateFormat(simpleDateFormat);
        from.setLenient(false);
        Date d;
        try {
            d = from.parse(date);
        } catch (ParseException e) {
            throw new IllegalStateException("Date de naissance invalide :" + date
                    + ". Format à respecter : (JJ.MM.AAAA) ", e);
        }

        Calendar birthDate = Calendar.getInstance();
        birthDate.setTime(d);
        // il y a 30 ans
        Calendar thirtyYearsAgo = Calendar.getInstance();
        thirtyYearsAgo.set(Calendar.YEAR, thirtyYearsAgo.get(Calendar.YEAR) - 31);
        // la différence
        if (birthDate.before(thirtyYearsAgo)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Renvoie true si les journaux d'assurance facultative doivent être affichés Date de création : (31.05.2005)
     * 
     * @return true si les journaux d'assurance facultative doivent être affichés
     */
    public static boolean isAfficheJournalAssuranceFac(BSession bSession) {
        try {
            CIApplication application = (CIApplication) bSession.getApplication();
            return application.isAfficheJournalAssuranceFac();
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Teste si l'assuré peut utiliser le splitting (21 ans). Date de création : (16.05.2003 11:50:40)
     * 
     * @return boolean
     */
    public static boolean isAgeSplitting(JADate date, int annee) {
        int dateNaissance = date.getYear();
        int ageSplitting = 21;
        if ((dateNaissance + ageSplitting) <= annee) {
            return true;
        }
        return false;
    }

    private static boolean isAnneeRetraite(int annee, int sexe, int dateNaissance) {
        int ageRetraite;
        if (sexe > 4) {
            // femme
            if (annee <= 2000) {
                ageRetraite = 62;
            } else if (annee < 2005) {
                ageRetraite = 63;
            } else {
                ageRetraite = 64;
            }
        } else {
            // homme
            ageRetraite = 65;
        }
        if ((dateNaissance + ageRetraite) == annee) {
            return true;
        }
        return false;
    }

    public static boolean isAnneeRetraite(int anneeNaissance, String sexeCS, int anneeReference) {
        int sexe = 5;
        if (CICompteIndividuel.CS_HOMME.equals(sexeCS)) {
            sexe = 1;
        }
        return CIUtil.isAnneeRetraite(anneeReference, sexe, anneeNaissance);
    }

    public static boolean isAnneeRetraite(JADate date, String sexeCS, int annee) {
        int sexe = 5;
        if (CICompteIndividuel.CS_HOMME.equals(sexeCS)) {
            sexe = 1;
        }
        int dateNaissance = date.getYear();
        return CIUtil.isAnneeRetraite(annee, sexe, dateNaissance);
    }

    /**
     * Méthode qui nous dit si les caisses sont différentes en plus des agences en cas de fusion
     * 
     * @return
     */
    public static boolean isCaisseDifferente(BSession session) {
        try {
            CIApplication application = (CIApplication) session.getApplication();
            return application.isCaisseDifferente();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isCaisseFusion() {
        try {
            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            return application.isCaisseFusion();
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean isCIAddionelNonConforme(BSession session) {
        // BSession bSession = (BSession) ((FWController)
        // session.getAttribute("objController")).getSession();
        try {
            CIApplication application = (CIApplication) session.getApplication();
            return application.isCIAddNonConforme();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isCIAddionelNonConforme(HttpSession session) {
        BSession bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
        try {
            CIApplication application = (CIApplication) bSession.getApplication();
            return application.isCIAddNonConforme();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param session
     * @return
     */
    public static boolean isCIGenreZeroPossible(BSession session) {
        // BSession bSession = (BSession) ((FWController)
        // session.getAttribute("objController")).getSession();
        try {
            CIApplication application = (CIApplication) session.getApplication();
            return application.isGenreZeroPossible();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isCodeSpecialIndiv(HttpSession session) {
        BSession bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
        try {
            CIApplication application = (CIApplication) bSession.getApplication();
            return application.isCodeSpecialIndiv();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isComparaisonEBCDIC(BSession session) {
        try {
            CIApplication application = (CIApplication) session.getApplication();
            return application.isComparaisonEBCDIC();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isAnnonceXML(BSession session) {
        try {
            CIApplication application = (CIApplication) session.getApplication();
            return application.isAnnonceXML();
        } catch (Exception e) {
            return false;
        }
    }

    public static String getPathFTP(BSession session) {
        try {
            CIApplication application = (CIApplication) session.getApplication();
            return application.getPathFTP();
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean isTestXML(BSession session) {
        try {
            CIApplication application = (CIApplication) session.getApplication();
            return application.isAnnonceXML();
        } catch (Exception e) {
            return true;
        }
    }

    public static boolean isAnnonceInscriptionCIXML(BSession session) {
        try {
            CIApplication application = (CIApplication) session.getApplication();
            return application.isAnnonceInscriptionXML();
        } catch (Exception e) {
            return false;
        }
    }

    public static String getNumFichierEnteteCI(BSession session) {
        try {
            CIApplication application = (CIApplication) session.getApplication();
            return application.getNumFichierEnteteCI();
        } catch (Exception e) {
            return "000000";
        }
    }

    public static boolean isGenreSeptSiCloture(BSession session, String noAvs, String moisDebut, String moisFin,
            String annee) {
        try {
            /*
             * ***************Si modif, mettre à jour aussi ciutil pour datenTrager
             */
            if (JAUtil.isStringEmpty(noAvs)) {
                return false;
            }
            String clo = "";
            CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
            ciMgr.setSession(session);
            ciMgr.setForNumeroAvs(CIUtil.unFormatAVS(noAvs));
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            ciMgr.find();
            if (ciMgr.size() != 0) {

                CICompteIndividuel ciClo = new CICompteIndividuel();
                ciClo = (CICompteIndividuel) ciMgr.getFirstEntity();
                clo = ciClo.getDerniereCloture(false);

                if (JAUtil.isStringEmpty(clo)) {
                    // pas possible si pas clôture
                    return false;
                }
                // String mot = getCI(null, false).getDernierMotifCloture();
                String mot = ciClo.getDernierMotifCloture(false);
                if (!"71".equals(mot) && !"81".equals(mot)) {
                    // clôturé mais pas pour AVS -> pas possible
                    return false;
                }
                String moisFinTest = JadeStringUtil.isIntegerEmpty(moisFin) ? "12" : moisFin;
                if (CIUtil.isMoisSpeciaux(moisFin)) {
                    moisFinTest = "01";
                }
                try {
                    if (!BSessionUtil.compareDateFirstLower(session, clo,
                            "01." + JadeStringUtil.rightJustifyInteger(moisFinTest, 2) + "." + annee)) {
                        // écriture avant la clôture -> pas genre 7
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;

            }
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Test si le mois de fin et de début sont égal à 66,77 ou 99
     * 
     * @return true si les mois de début et de fin sont spéciaux (66,77,99)
     */
    public static boolean isMoisSpeciaux(String moisFin) {
        return "66".equals(moisFin) || "77".equals(moisFin) || "99".equals(moisFin);
    }

    public static boolean isNNSSlengthOrNegate(String nss) {
        if (NSUtil.unFormatAVS(nss).trim().length() == 13) {
            return true;
        } else if (nss.startsWith("-")) {
            return true;
        }
        return false;
    }

    private static boolean isRetraite(int annee, int sexe, int dateNaissance) {
        int ageRetraite;
        if (sexe > 4) {
            // femme
            if (annee <= 2000) {
                ageRetraite = 62;
            } else if (annee < 2005) {
                ageRetraite = 63;
            } else {
                ageRetraite = 64;
            }
        } else {
            // homme
            ageRetraite = 65;
        }
        if ((dateNaissance + ageRetraite) <= annee) {
            return true;
        }
        return false;
    }

    public static boolean isRetraite(int anneeNaissance, String sexeCS, int anneeReference) {
        int sexe = 5;
        if (CICompteIndividuel.CS_HOMME.equals(sexeCS)) {
            sexe = 1;
        }
        return CIUtil.isRetraite(anneeReference, sexe, anneeNaissance);
    }

    public static boolean isRetraite(JADate date, String sexeCS, int annee) {
        int sexe = 5;
        if (CICompteIndividuel.CS_HOMME.equals(sexeCS)) {
            sexe = 1;
        }
        int dateNaissance = date.getYear();
        return CIUtil.isRetraite(annee, sexe, dateNaissance);
    }

    /**
     * Teste si l'assuré à l'age de la retraite. Date de création : (16.05.2003 11:50:40)
     * 
     * @return boolean
     */
    public static boolean isRetraite(String noAvs, int annee, String noAvsNNSS, BSession session) {
        // Modif NNSS
        // Si c'est un NNSS, on regarde la date dans l'entête dans le CI
        if (NSUtil.unFormatAVS(noAvs).trim().length() == 13) {
            noAvsNNSS = "true";
        }
        if ("true".equalsIgnoreCase(noAvsNNSS) && !JadeStringUtil.isBlankOrZero(noAvs)) {
            CICompteIndividuelManager ciMgr = new CICompteIndividuelManager();
            ciMgr.setSession(session);
            ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            ciMgr.setForNumeroAvs(noAvs);
            try {
                ciMgr.find();
                if (ciMgr.size() > 0) {
                    CICompteIndividuel ciRetraite = (CICompteIndividuel) ciMgr.getFirstEntity();
                    return CIUtil.isRetraite(new JADate(ciRetraite.getDateNaissance()), ciRetraite.getSexe(), annee);
                } else {
                    ciMgr.setForRegistre(CICompteIndividuel.CS_REGISTRE_PROVISOIRE);
                    ciMgr.find();
                    if (ciMgr.size() > 0) {
                        CICompteIndividuel ciRetraite = (CICompteIndividuel) ciMgr.getFirstEntity();
                        if (JadeStringUtil.isBlankOrZero(ciRetraite.getDateNaissance())) {
                            return false;
                        }
                        return CIUtil
                                .isRetraite(new JADate(ciRetraite.getDateNaissance()), ciRetraite.getSexe(), annee);
                    }
                    return false;
                }
            } catch (Exception e) {
                return false;

            }

        }
        if (!JAUtil.isStringEmpty(noAvs) && (noAvs.length() > 5) && !noAvs.startsWith("00000")
                && !"true".equalsIgnoreCase(noAvsNNSS)) {
            String avs = CIUtil.unFormatAVS(noAvs);
            int sexe = Character.digit(avs.charAt(5), 10);
            int dateNaissance = Integer.parseInt("19" + avs.substring(3, 5));
            return CIUtil.isRetraite(annee, sexe, dateNaissance);
        }
        return false;
    }

    public static boolean isSpecialist(BSession session) {
        try {
            try {
                FWSecureUserDetail user = new FWSecureUserDetail();
                user.setSession(session);
                user.setUser(session.getUserId());
                user.setLabel(CIUtil.SPECIALIST_CI);
                user.retrieve();
                if (!user.isNew()) {
                    return "true".equalsIgnoreCase(user.getData());
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
            // niveau suffisant
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean isSpecialist(HttpSession session) {
        try {
            BSession bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
            return CIUtil.isSpecialist(bSession);
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Ajoute des zéro au jour et mois d'une date au format dd.mm.yyyy. Date de création : (27.01.2003 08:44:58)
     * 
     * @date la date à formater.
     * @return la date fotmatée
     */
    public static String padDate(String date) {
        if ((date == null) || (date.length() < 8)) {
            return date;
        }
        StringBuffer buf = new StringBuffer(date);
        if (buf.charAt(2) != '.') {
            buf.insert(0, '0');
        }
        if (buf.charAt(5) != '.') {
            buf.insert(3, '0');
        }
        return buf.toString();
    }

    /**
     * Renvoie le numéro avs sans les zéro superflus
     * 
     * @param noAvs
     *            le numéro avs à convertir
     * @return le numéro avs sans zéro
     */
    public static String removeZeroFromAvs(String noAvs) {
        int pos = noAvs.length() - 1;
        while (noAvs.charAt(pos) == '0') {
            noAvs = noAvs.substring(0, pos--);
        }
        return noAvs;
    }

    /**
     * Sépare un texte avec la première occurence d'un charactère blanc. Date de création : (21.03.2003 13:32:32)
     * 
     * @return java.lang.String[]
     * @param text
     *            java.lang.String
     */
    public static String[] splitWithBlank(String text) {
        String[] result = new String[] { "", "" };
        if ((text != null) && (text.length() != 0)) {
            int pos = text.indexOf(' ');
            if (pos == -1) {
                result[0] = text;
            } else {
                result[0] = text.substring(0, pos);
                result[1] = text.substring(pos + 1);
            }
        }
        return result;
    }

    /**
     * Enlève les . de séparation du numéro avs. Date de création : (09.01.2003 10:07:14)
     * 
     * @return le numéro avs formatté
     * @param avs
     *            le numéro avs à fomatter
     */
    public static String unFormatAVS(String avs) {
        if (!JAUtil.isStringEmpty(avs)) {
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < avs.length(); i++) {
                if (avs.charAt(i) != '.') {
                    buf.append(avs.charAt(i));
                }
            }
            return buf.toString();
        }
        return avs;
    }

    public static String UnFormatNumeroAffilie(BSession session, String numeroAffilie) throws Exception {
        CIApplication app = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                CIApplication.DEFAULT_APPLICATION_PAVO);
        IFormatData affilieFormater = app.getAffileFormater();
        return affilieFormater.unformat(numeroAffilie);
    }

    public static String unPad(String st) {
        if (st == null) {
            return "";
        }
        st = st.trim();
        try {
            int intStr = Integer.parseInt(st);
            if (intStr == 0) {
                return "0";
            }
            return String.valueOf(intStr);
        } catch (Exception e) {
            return st;
        }
    }

    /**
     * Enlèves les 0 de la caisse pour recherche dans tiers Date de création : (09.01.2003 10:07:14)
     * 
     * @return le numéro avs formatté
     * @param avs
     *            le numéro avs à fomatter
     */
    public static String unPadAdmin(String st) {
        if (st == null) {
            return "";
        }
        st = st.trim();
        try {
            int intStr = Integer.parseInt(st);
            if (intStr == 0) {
                return "";
            }
            return String.valueOf(intStr);
        } catch (Exception e) {
            return st;
        }
    }

    public static boolean wantSyncroNRa() {
        try {
            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            return application.wantSyncroNra();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Commentaire relatif au constructeur CIUtil.
     */
    public CIUtil() {
        super();
    }

    public static String getRacineNom() {
        try {
            CIApplication application = (CIApplication) GlobazServer.getCurrentSystem().getApplication(
                    CIApplication.DEFAULT_APPLICATION_PAVO);
            return application.getRacineNom();
        } catch (Exception e) {
            return "";
        }
    }

}
