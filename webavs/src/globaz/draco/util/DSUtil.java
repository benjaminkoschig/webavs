package globaz.draco.util;

import globaz.draco.application.DSApplication;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.draco.translation.CodeSystem;
import globaz.framework.controller.FWController;
import globaz.globall.api.GlobazSystem;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.FWFindParameter;
import globaz.globall.db.FWFindParameterManager;
import globaz.globall.db.GlobazServer;
import globaz.globall.format.IFormatData;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.pyxis.db.tiers.TITiersViewBean;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.servlet.http.HttpSession;

/**
 * Classe utilitaire pour DRACO. Date de création : (09.01.2003 15:18:41)
 * 
 * @author: Administrator
 */
public class DSUtil {
    /**
     * Retourne une liste des Affilies pour une déclaration, cad les employeurs et indépendants (numéro et nom de
     * l'affilié) en fonction des premiers chiffres du numéro affilié donnés en paramètres. Date de création :
     * (17.09.2003 17:032:13)
     * 
     * @return la liste d'options (tag select) des affiliés existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les premiers chiffres du numéro affilié
     * @param session
     *            la session HTTP actuelle
     */
    public static String getAffiliesForDeclaration(String like, HttpSession session) {
        return DSUtil.getAffilliesForDeclaration(like,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    /**
     * Retourne une liste des Affilies pour une déclaration, cad les employeurs et indépendants (numéro et nom de
     * l'affilié) en fonction des premiers chiffres du numéro affilié donnés en paramètres. Date de création :
     * (17.09.2003 17:032:13)
     * 
     * @return la liste d'options (tag select) des affiliés existants de la caisse actuelle. Peut-être vide si aucune
     *         information n'a été trouvée
     * @param like
     *            les premiers chiffres du numéro affilié
     * @param bsession
     *            la session actuelle (provenant du viewBean de la page jsp)
     */
    public static String getAffilliesForDeclaration(String like, BSession bsession) {
        DSApplication dsApp = null;
        try {
            // ciApp = (CIApplication) bsession.getApplication();
            dsApp = (DSApplication) GlobazServer.getCurrentSystem().getApplication(
                    DSApplication.DEFAULT_APPLICATION_DRACO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuffer options = new StringBuffer();
        try {
            AFAffiliationManager mgr = new AFAffiliationManager();
            mgr.setSession(bsession);
            IFormatData affilieFormater = dsApp.getAffileFormater();
            if (affilieFormater != null) {
                like = affilieFormater.format(like);
            }
            mgr.setLikeAffilieNumero(like);
            mgr.setForTypeFacturation(AFAffiliationManager.PARITAIRE);
            mgr.find(BManager.SIZE_NOLIMIT);
            for (int i = 0; i < mgr.size(); i++) {
                AFAffiliation af = (AFAffiliation) mgr.getEntity(i);
                /**/
                options.append("<option value='");
                options.append(af.getAffilieNumero());
                /**/
                options.append("' nom=\"");
                TITiersViewBean ti = new TITiersViewBean();
                ti.setSession(bsession);
                ti.setIdTiers(af.getIdTiers());
                ti.retrieve();
                options.append(af._getDescriptionTiers());
                /* affiliation Id */
                options.append("\" affiliationId='");
                options.append(af.getAffiliationId());
                /* affilie dès */
                options.append("' affilieDesEcran='");
                options.append(af.getDateDebut());
                /* affilie radié au */
                options.append("' affilieRadieEcran='");
                options.append(af.getDateFin());
                /* Type d'affiliation */
                options.append("' typeAffiliationEcran='");
                options.append(CodeSystem.getLibelle(bsession, af.getTypeAffiliation()));
                options.append("'");
                /**/
                options.append(">");
                options.append(af.getAffilieNumero());
                options.append(" - ");
                options.append(ti.getPrenomNom());
                options.append("</option>");
            }

            if (JadeStringUtil.isBlank(options.toString())) {
                options.append("<OPTION value=\""
                        + like
                        + "\" nom=\"\" affiliationId=\"\" affilieDesEcran=\"\" affilieRadieEcran=\"\" typeAffiliationEcran=\"\">"
                        + like + "</OPTION>");
            }
        } catch (Exception ex) {
        }
        return options.toString();
    }

    public static int getAnneePlausiDecompte18(BSession session) {
        try {
            DSApplication application = (DSApplication) session.getApplication();
            return Integer.parseInt(application.getAnneePlausiDecompte18());
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getAutoDigitAff(BSession session) {

        try {
            DSApplication application = (DSApplication) session.getApplication();
            return application.getAutoDigitAffilie();
        } catch (Exception e) {
            return -1;
        }
    }

    public static int getAutoDigitAff(HttpSession session) {
        BSession bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
        try {
            DSApplication application = (DSApplication) bSession.getApplication();
            return application.getAutoDigitAffilie();
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getBareCode(String bareCode) {
        StringBuffer option = new StringBuffer();
        option.append("<OPTION value=\"" + bareCode + "\">" + bareCode + "</OPTION>");
        return option.toString();
    }

    public static ArrayList<String> getCSBloquage13(BSession session) {
        try {
            DSApplication application = (DSApplication) session.getApplication();
            StringTokenizer token = new StringTokenizer(application.getListeCSBloquage13(), ",");
            ArrayList<String> array = new ArrayList<String>();
            while (token.hasMoreTokens()) {
                array.add(token.nextToken());
            }
            return array;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getDateMiseEnprod() {
        try {
            DSApplication dsApp = (DSApplication) GlobazServer.getCurrentSystem().getApplication(
                    DSApplication.DEFAULT_APPLICATION_DRACO);
            return dsApp.getDateMiseEnProd();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getInformationsDeclaration(String likeNumeroAffilie, String annee, BSession session) {
        DSApplication dsApp = null;
        // annee=String.valueOf(JACalendar.today().getYear()-1);
        try {
            // ciApp = (CIApplication) bsession.getApplication();
            dsApp = (DSApplication) GlobazServer.getCurrentSystem().getApplication(
                    DSApplication.DEFAULT_APPLICATION_DRACO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuffer options = new StringBuffer();
        try {
            AFAffiliationManager mgr = new AFAffiliationManager();
            mgr.setSession(session);
            IFormatData affilieFormater = dsApp.getAffileFormater();
            if (affilieFormater != null) {
                likeNumeroAffilie = affilieFormater.format(likeNumeroAffilie);
            }
            mgr.setLikeAffilieNumero(likeNumeroAffilie);
            mgr.setForTypeFacturation(AFAffiliationManager.PARITAIRE);
            mgr.find(BManager.SIZE_NOLIMIT);
            for (int i = 0; i < mgr.size(); i++) {
                AFAffiliation af = (AFAffiliation) mgr.getEntity(i);
                DSDeclarationListViewBean declListViewBean = new DSDeclarationListViewBean();
                declListViewBean.setSession(session);
                declListViewBean.setForAffiliationId(af.getAffiliationId());
                declListViewBean.setForEtat(DSDeclarationViewBean.CS_OUVERT);
                declListViewBean.setForAnnee(annee);
                declListViewBean.find();
                if (declListViewBean.size() == 0) {
                    /**/
                    options.append("<option value='");
                    options.append(af.getAffilieNumero());
                    /**/
                    options.append("' nom=\"");
                    TITiersViewBean ti = new TITiersViewBean();
                    ti.setSession(session);
                    ti.setIdTiers(af.getIdTiers());
                    ti.retrieve();
                    options.append(af._getDescriptionTiers());
                    /* affiliation Id */
                    options.append("\" affiliationId='");
                    options.append(af.getAffiliationId());
                    /* affilie dès */
                    options.append("' affilieDesEcran='");
                    options.append(af.getDateDebut());
                    /* affilie radié au */
                    options.append("' affilieRadieEcran='");
                    options.append(af.getDateFin());
                    /* Type d'affiliation */
                    options.append("' typeAffiliationEcran='");
                    options.append(CodeSystem.getLibelle(session, af.getTypeAffiliation()));
                    /* Année */
                    options.append("' annee='");
                    options.append(annee);
                    /* Date de réception */
                    options.append("' dateRetourEff='");
                    /* Masse salariale totale */
                    options.append("' masseSalTotal='");
                    /* Masse AC totale */
                    options.append("' masseACTotal='");
                    /* Masse AC2 totale */
                    options.append("' masseAC2Total='");
                    /* Total controle DS */
                    options.append("' totalControleDS='");
                    /* Type de déclaration */
                    options.append("' typeDeclaration='");
                    options.append(DSDeclarationViewBean.CS_PRINCIPALE);
                    options.append("'");
                    /**/
                    options.append(">");
                    options.append(af.getAffilieNumero());
                    options.append(" - ");
                    options.append(ti.getPrenomNom());
                    options.append("</option>");
                } else {
                    DSDeclarationViewBean declViewBean = (DSDeclarationViewBean) declListViewBean.getFirstEntity();
                    /**/
                    options.append("<option value='");
                    options.append(af.getAffilieNumero());
                    /**/
                    options.append("' nom=\"");
                    TITiersViewBean ti = new TITiersViewBean();
                    ti.setSession(session);
                    ti.setIdTiers(af.getIdTiers());
                    ti.retrieve();
                    options.append(af._getDescriptionTiers());
                    /* affiliation Id */
                    options.append("\" affiliationId='");
                    options.append(af.getAffiliationId());
                    /* affilie dès */
                    options.append("' affilieDesEcran='");
                    options.append(af.getDateDebut());
                    /* affilie radié au */
                    options.append("' affilieRadieEcran='");
                    options.append(af.getDateFin());
                    /* Type d'affiliation */
                    options.append("' typeAffiliationEcran='");
                    options.append(CodeSystem.getLibelle(session, af.getTypeAffiliation()));
                    /* Année */
                    options.append("' annee='");
                    options.append(declViewBean.getAnnee());
                    /* Date de réception */
                    options.append("' dateRetourEff='");
                    options.append(declViewBean.getDateRetourEff());
                    /* Masse salariale totale */
                    options.append("' masseSalTotal=\"");
                    options.append(declViewBean.getMasseSalTotal());
                    /* Masse AC totale */
                    options.append("\" masseACTotal=\"");
                    options.append(declViewBean.getMasseACTotal());
                    /* Masse AC2 totale */
                    options.append("\" masseAC2Total=\"");
                    options.append(declViewBean.getMasseAC2Total());
                    /* Total controle DS */
                    options.append("\" totalControleDS=\"");
                    options.append(declViewBean.getTotalControleDS());
                    /* Type de déclaration */
                    options.append("\" typeDeclaration='");
                    options.append(declViewBean.getTypeDeclaration());
                    options.append("'");
                    /**/
                    options.append(">");
                    options.append(af.getAffilieNumero());
                    options.append(" - ");
                    options.append(ti.getPrenomNom());
                    options.append("</option>");
                }
            }

            if (JadeStringUtil.isBlank(options.toString())) {
                options.append("<OPTION value=\""
                        + likeNumeroAffilie
                        + "\" nom=\"\" affiliationId=\"\" affilieDesEcran=\"\" affilieRadieEcran=\"\" typeAffiliationEcran=\"\" annee=\"\" dateRetourEff=\"\" masseSalTotal=\"\" masseACTotal=\"\" masseAC2Total=\"\" totalControleDS=\"\" typeDeclaration=\"\" >"
                        + likeNumeroAffilie + "</OPTION>");
            }
        } catch (Exception ex) {
        }
        return options.toString();
    }

    public static String getInformationsDeclaration(String like, String annee, HttpSession session) {
        return DSUtil.getInformationsDeclaration(like, annee,
                (BSession) ((FWController) session.getAttribute("objController")).getSession());
    }

    public static int getLongueurReception(HttpSession hTsession) {
        try {
            BSession session = (BSession) ((FWController) hTsession.getAttribute("objController")).getSession();
            DSApplication application = (DSApplication) session.getApplication();
            return application.getLongueurReception();
        } catch (Exception e) {
            return 18;
        }

    }

    public static String getNoCaisse(BSession session) {
        try {
            DSApplication application = (DSApplication) session.getApplication();
            return application.getCaisse();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getNoCaisse(HttpSession session) {
        try {
            BSession sessionB = (BSession) ((FWController) session.getAttribute("objController")).getSession();
            DSApplication application = (DSApplication) sessionB.getApplication();
            return application.getCaisse();
        } catch (Exception e) {
            return "";
        }
    }

    public static int getTailleChampsAffilie(HttpSession session) {
        BSession bSession = (BSession) ((FWController) session.getAttribute("objController")).getSession();
        try {
            DSApplication application = (DSApplication) bSession.getApplication();
            return application.getTailleChampsAffilie();
        } catch (Exception e) {
            return 11;
        }
    }

    public static boolean isForceSuiviAttest(BSession session) {
        try {
            FWFindParameterManager param = new FWFindParameterManager();
            FWFindParameter parametre;
            param.setSession(session);
            param.setIdApplParametre(DSApplication.DEFAULT_APPLICATION_DRACO);
            param.setIdCleDiffere(DSApplication.IS_FORCE_SUIVI_ATTEST);
            param.find();
            if ((param.size() > 0) && (param.getFirstEntity() != null)) {
                parametre = (FWFindParameter) param.getFirstEntity();
                String valeur = parametre.getValeurAlphaParametre();
                if (JadeStringUtil.isBoolean(valeur)) {
                    return JadeStringUtil.parseBoolean(valeur, false);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean isNNSSActif(BSession session, String Date) {
        try {
            String dateNNSS = GlobazSystem.getApplication(DSApplication.DEFAULT_APPLICATION_DRACO).getProperty(
                    "nnss.dateProduction");
            if (!JadeStringUtil.isEmpty(dateNNSS)) {
                // String currentDate = JACalendar.todayJJsMMsAAAA();
                String currentDate = Date;
                return BSessionUtil.compareDateFirstGreaterOrEqual(session, currentDate, dateNNSS);
            } else {
                return false;
            }
        } catch (Exception e) {
            // TODO: ajouter l'erreur si la lecture de la la date de production
            // échoue.
            return false;
        }
    }

    public static boolean isNouveauControleEmployeur(BSession session) {
        try {
            DSApplication application = (DSApplication) session.getApplication();
            return application.isNouveauControleEmployeur();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retourne le nombre de déclaration selon les critères renseignés
     * 
     * @param session
     * @param idJournal
     * @param idAffiliation
     * @param annee
     * @param etat
     * @param idControleEmployeur
     * @param typeDeclaration
     * @return
     * @throws Exception
     */
    public static int returnNombreDeclaration(BSession session, String idJournal, String idAffiliation, String annee,
            String etat, String idControleEmployeur, String typeDeclaration) throws Exception {

        if (JadeStringUtil.isBlankOrZero(idJournal) && JadeStringUtil.isBlankOrZero(idAffiliation)
                && JadeStringUtil.isBlankOrZero(annee) && JadeStringUtil.isBlankOrZero(idControleEmployeur)
                && JadeStringUtil.isBlankOrZero(typeDeclaration)) {
            throw new Exception("Insufficient number of arguments");
        }

        DSDeclarationListViewBean mng = new DSDeclarationListViewBean();
        mng.setSession(session);
        mng.setForIdJournal(idJournal);
        mng.setForAffiliationId(idAffiliation);
        mng.setForAnnee(annee);
        mng.setForEtat(etat);
        mng.setForIdControlEmployeur(idControleEmployeur);
        mng.setForTypeDeclaration(typeDeclaration);
        mng.find();
        return mng.getSize();
    }

    public static boolean wantTriAgenceComm() throws Exception {
        try {
            DSApplication application = (DSApplication) GlobazServer.getCurrentSystem().getApplication(
                    DSApplication.DEFAULT_APPLICATION_DRACO);
            return application.wantTriAgenceComm();
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Commentaire relatif au constructeur DSUtil.
     */
    public DSUtil() {
        super();
    }
}
