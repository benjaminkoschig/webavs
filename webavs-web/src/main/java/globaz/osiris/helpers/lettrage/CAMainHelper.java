package globaz.osiris.helpers.lettrage;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.IFWHelper;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BApplication;
import globaz.globall.db.BConstants;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.GlobazServer;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.musca.application.FAApplication;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.osiris.api.APIEcriture;
import globaz.osiris.api.APIOperation;
import globaz.osiris.api.APIOperationOrdreVersement;
import globaz.osiris.api.APISection;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CAEcriture;
import globaz.osiris.db.comptes.CAEcritureManager;
import globaz.osiris.db.comptes.CAJournal;
import globaz.osiris.db.comptes.CAOperation;
import globaz.osiris.db.comptes.CAOperationManager;
import globaz.osiris.db.comptes.CAOperationOrdreVersement;
import globaz.osiris.db.comptes.CAOperationOrdreVersementLettrageManager;
import globaz.osiris.db.comptes.CASection;
import globaz.osiris.db.comptes.extrait.CAExtraitCompteManager;
import globaz.osiris.db.contentieux.CAExtraitCompteListViewBean;
import globaz.osiris.db.contentieux.CALigneExtraitCompte;
import globaz.osiris.db.contentieux.CAMotifContentieux;
import globaz.osiris.db.contentieux.CAMotifContentieuxManager;
import globaz.osiris.db.lettrage.CAExclusionSection;
import globaz.osiris.db.ordres.CAOrdreGroupe;
import globaz.osiris.db.utils.CARemarque;
import globaz.osiris.helpers.lettrage.rules.CARulesCouvertureSectionDebitrices;
import globaz.osiris.print.itext.CAImpressionBulletinsSoldes_Doc;
import globaz.osiris.vb.lettrage.CALettrageViewBean;
import globaz.pyxis.constantes.IConstantes;
import globaz.pyxis.db.adressepaiement.TIAvoirPaiementManager;
import globaz.pyxis.util.TISQL;
import globaz.pyxis.util.TIToolBox;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CAMainHelper implements IFWHelper {

    private static final int ETAPE_AUCUNE = 5200030;
    private static final int ETAPE_DECISION = 5200044;
    private static final int ETAPE_SOMMATION = 5200034;
    private static List<String> ETAPES_AVANT_RP = new ArrayList<String>();

    private static final String FILTER_AVANT_REQ_POURSUITE = "1";
    private static final String FILTER_AVEC_SURSIS_PMT = "4";
    private static final String FILTER_DES_REQ_POURSUITE = "2";
    private static final String FILTER_EXCLUSION_MANUEL = "5";
    private static final String FILTER_SANS_SURSIS_PMT = "3";

    static {
        CAMainHelper.ETAPES_AVANT_RP.add("5200029"); // ARD créé
        CAMainHelper.ETAPES_AVANT_RP.add("5200030"); // Aucune
        CAMainHelper.ETAPES_AVANT_RP.add("5200035"); // Contentieux créé
        CAMainHelper.ETAPES_AVANT_RP.add("5200044"); // Décision envoyée
        CAMainHelper.ETAPES_AVANT_RP.add("5200032"); // Premier rappel envoyé
        CAMainHelper.ETAPES_AVANT_RP.add("5200034"); // Sommation envoyée
        // TODO statuer sur "Poursuite classé" ?
        // TODO statuer sur "Poursuite radiée" ?
    }

    private final String DIV_RULES = "<div name='pri' style='display:none' >";

    private final String STYLE_SECTION_CTX = " style='background:#FFD0D0' ";

    private static String _date(String date) {
        return date.substring(6) + "." + date.substring(4, 6) + "." + date.substring(0, 4);
    }

    /*
     * Some utils
     */
    private static String _join(Collection<String> col) {
        StringBuffer buf = new StringBuffer();
        for (Iterator<String> it = col.iterator(); it.hasNext();) {
            buf.append(it.next());
            if (it.hasNext()) {
                buf.append(",");
            }
        }
        return buf.toString();
    }

    /*
     * Renvoie la requête principale
     */
    public static String mainQuery(String role) {
        String col = TIToolBox.getCollection();

        String req = " from " + col + "CASECTP a " + " inner join " + col
                + "CACPTAP b on (a.IDCOMPTEANNEXE = b.IDCOMPTEANNEXE) " + " inner join " + col
                + "titierp t on ( b.IDTIERS = t.htitie) " + " left outer join " + col
                + "CAEXLEP e on (a.IDSECTION = e.IDSECTION)" + " left outer join " + col
                + "CAPLARP pl on (a.IDPLANRECOUVREMENT = pl.IDPLANRECOUVREMENT) left outer join " + col
                + "CAREMAP re on (re.idremarque = a.idremarque)" + " where a.SOLDE <> 0";
        if (!JadeStringUtil.isEmpty(role)) {
            req += "  and b.IDROLE IN (" + role + ")";
        }
        req += " and a.CATEGORIESECTION <> 227096 ";
        return req;
    }

    private void _actionAnnulerVersementSection(BSession session, CALettrageViewBean viewBean,
            final List<String> catCreditriceExclues) throws Exception {
        String idCompteAnnexe = viewBean.getIdCompteAnnexe();

        String role = viewBean.getRole();
        String idOrdreEnAttente = viewBean.getIdOrdreEnAttente();
        CAOperationOrdreVersement ov = new CAOperationOrdreVersement();
        ov.setSession(session);
        ov.setIdOperation(idOrdreEnAttente);
        ov.retrieve();
        ov.delete();

        if (session.hasErrors()) {
            throw new Exception("KO," + session.getErrors().toString());
        }

        String result = "NM,"
                + _renderQuerySection(session, viewBean.getContextPath(), CAMainHelper.mainQuery(role), idCompteAnnexe,
                        catCreditriceExclues);
        viewBean.getResponse().append(result);

    }

    /*
     * Exclure une section creditrice
     */
    private void _actionExclureSection(BSession session, CALettrageViewBean viewBean,
            final List<String> catCreditriceExclues) throws Exception {

        String idCompteAnnexe = viewBean.getIdCompteAnnexe();
        String idSection = viewBean.getIdSection();
        String role = viewBean.getRole();

        /*
         * Exlusion de la section
         */
        CAExclusionSection ex = new CAExclusionSection();
        ex.setSession(session);
        ex.setIdCompteAnnexe(idCompteAnnexe);
        ex.setIdSection(idSection);
        ex.add();
        if (session.hasErrors()) {
            throw new Exception("KO," + session.getErrors().toString());
        }

        String result = "NM,"
                + _renderQuerySection(session, viewBean.getContextPath(), CAMainHelper.mainQuery(role), idCompteAnnexe,
                        catCreditriceExclues);
        viewBean.getResponse().append(result);
    }

    /*
     * Inclure une section creditrice
     */
    private void _actionInclureSection(BSession session, CALettrageViewBean viewBean,
            final List<String> catCreditriceExclues) throws Exception {
        String idCompteAnnexe = viewBean.getIdCompteAnnexe();
        String idExclusion = viewBean.getIdExclusion();
        String role = viewBean.getRole();

        /*
         * Exclusion de la section
         */
        CAExclusionSection ex = new CAExclusionSection();
        ex.setSession(session);
        ex.setIdExculsion(idExclusion);
        ex.retrieve();
        ex.delete();
        if (session.hasErrors()) {
            throw new Exception("KO," + session.getErrors().toString());
        }

        String result = "NM,"
                + _renderQuerySection(session, viewBean.getContextPath(), CAMainHelper.mainQuery(role), idCompteAnnexe,
                        catCreditriceExclues);
        viewBean.getResponse().append(result);
    }

    private void _actionInfoSection(BSession session, CALettrageViewBean viewBean,
            final List<String> catCreditriceExclues) throws Exception {
        String idCompteAnnexe = viewBean.getIdCompteAnnexe();
        String idSection = viewBean.getIdSection();

        CASection section = new CASection();
        section.setSession(session);
        section.setIdSection(idSection);
        section.retrieve();

        /*
         * Idem info bulletin de solde
         */
        CAExtraitCompteListViewBean mgr = new CAExtraitCompteListViewBean();
        mgr.setSession(session);
        mgr.setForSelectionTri(CAExtraitCompteManager.ORDER_BY_DATE_COMPTABLE);
        mgr.setForIdSection(idSection);
        mgr.setIdCompteAnnexe(idCompteAnnexe);

        mgr.setPrintLanguage(session.getIdLangueISO());
        mgr.find();

        if (session.hasErrors()) {
            throw new Exception("KO," + session.getErrors().toString());
        }

        String langue = session.getIdLangueISO();
        BApplication app = (BApplication) GlobazServer.getCurrentSystem().getApplication("OSIRIS");

        String libelleDateComptable = app.getLabel("IMPR_EXTR_CPT_ANNEXE_COL_8", langue);
        String libelleDateValeur = app.getLabel("IMPR_EXTR_CPT_ANNEXE_COL_1", langue);
        String libDescription = app.getLabel("LIBELLE", langue);
        String libRubrique = app.getLabel("RUBRIQUE", langue);
        String libCC = app.getLabel("COMPTECOURANT", langue);

        String libMontant = app.getLabel("MONTANT", langue);

        StringBuffer buffer = new StringBuffer();
        buffer.append("<hr> <h3>" + section.getIdExterne() + " - " + section.getDescription(langue) + "</h3>");

        buffer.append("<div id='infoTabs'>");
        buffer.append("<ul><li><a href='#vueBS'>"
                + session.getApplication().getLabel("GCA70002_APERCU_SECTION", session.getIdLangueISO())
                + "</a></li><li><a href='#vueOP'>" + app.getLabel("IMPR_EXTR_CPT_ANNEXE_COL_8", langue)
                + session.getApplication().getLabel("GCA70002_DETAIL_OPERATION", session.getIdLangueISO())
                + "</a></li></ul> ");

        /*
         * Tab "Extrait de la section" comme bulletin de solde
         */
        buffer.append("<div id='vueBS'>");
        buffer.append("<table border=1 cellpading=0 cellspacing=0>");
        buffer.append("<tr><th>" + libelleDateComptable + "</th><th>" + libelleDateValeur + "</th><th>"
                + libDescription + "</th><th>" + libMontant + "</th></tr>");
        for (Iterator it = mgr.iterator(); it.hasNext();) {
            CALigneExtraitCompte e = (CALigneExtraitCompte) it.next();
            if (JadeStringUtil.isEmpty(e.getHorsCompteAnnexe())) {
                buffer.append("<tr><td>" + e.getDateJournal() + "</td><td>" + e.getDate() + "</td><td>"
                        + e.getDescription() + "</td><td align='right'>"
                        + new FWCurrency(e.getTotal()).toStringFormat() + "</td>" + "</tr>");
            }

        }
        buffer.append("</table>");
        buffer.append("</div>");

        /*
         * Tab "Vue par opérations de la sections"
         */
        buffer.append("<div id='vueOP'>");
        buffer.append("<table border=1 cellpading=0 cellspacing=0>");
        buffer.append("<tr>");
        buffer.append("<th>" + libCC + "</th>");
        buffer.append("<th>" + libelleDateValeur + "</th>");
        buffer.append("<th>" + libRubrique + "</th>");
        buffer.append("<th>" + libDescription + "</th>");
        buffer.append("<th>" + libMontant + "</th>");
        buffer.append("</tr>");

        CAEcritureManager ecrituresMgr = new CAEcritureManager();
        ecrituresMgr.setSession(session);
        ecrituresMgr.setForIdSection(idSection);
        ecrituresMgr.setForEtat(APIOperation.ETAT_COMPTABILISE);
        ecrituresMgr.setVueOperationRubCC("true");
        ecrituresMgr.setOrderBy(CAOperationManager.ORDER_CC_DATE);
        ecrituresMgr.find(0);

        for (CAEcriture ecr : (Iterable<CAEcriture>) ecrituresMgr) {
            buffer.append("<tr>");
            buffer.append("<td>" + ecr.getIdExterneCompteCourantEcran() + "</td>");
            buffer.append("<td>" + ecr.getDate() + "</td>");
            buffer.append("<td>" + ecr.getIdExterneRubriqueEcran() + "</td>");
            buffer.append("<td>" + ecr.getDescription(langue) + "</td>");
            buffer.append("<td align='right'>" + ecr.getMontant() + "</td>");
            buffer.append("</tr>");
        }
        buffer.append("</table>");
        buffer.append("</div>");
        /*
         * Fin du tab
         */
        buffer.append("</div>");

        buffer.append("<hr>");
        String result = buffer.toString();
        viewBean.getResponse().append(result);

    }

    /*
     * Traitement du lettrage des sections d'un compte annexe
     */
    private void _actionLettrage(BSession session, CALettrageViewBean viewBean) throws Exception {
        String errorMessages = "";
        String params = viewBean.getParams();
        int posSep = params.lastIndexOf("-");
        String idCompteAnnexe = params.substring(0, posSep);
        String[] all = params.substring(posSep + 1).split(";");
        // retrieve du compte annexe
        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(session);
        compteAnnexe.setIdCompteAnnexe(idCompteAnnexe);
        compteAnnexe.retrieve();
        CAJournal journal = null;

        String lettrageNonEffectue = "";
        Map<String, FWCurrency> sectionPosMap = new HashMap<String, FWCurrency>();
        // =====================DEBUT DU LETTRAGE ===========================
        for (int i = 0; i < all.length; i++) {
            List<String> parts = JadeStringUtil.tokenize(all[i], (","));
            String idSectionPos = parts.get(0);
            String idSectionNeg = parts.get(1);
            String montant = parts.get(2);
            FWCurrency mon = new FWCurrency(montant);
            if (!mon.isZero()) {
                CASection sectionNeg = new CASection();
                sectionNeg.setSession(session);
                sectionNeg.setIdSection(idSectionNeg);
                sectionNeg.retrieve();
                CASection sectionPos = new CASection();
                sectionPos.setSession(session);
                sectionPos.setIdSection(idSectionPos);
                sectionPos.retrieve();

                FWCurrency sum = sectionPosMap.get(idSectionPos);
                if (sum == null) {
                    sectionPosMap.put(idSectionPos, new FWCurrency(montant));
                } else {
                    sum.add(new FWCurrency(montant));
                    sectionPosMap.put(idSectionPos, sum);
                }

                /*
                 * APPEL DE L'OPERATION LETTRAGE
                 */
                boolean atLeatOneLettrage = sectionNeg.manualLettrage(null, compteAnnexe, sectionPos, journal, montant);

                if (!atLeatOneLettrage) {
                    sectionPosMap.remove(sectionPos.getIdSection()); // pour ne pas sortir de bulletin de solde....
                    lettrageNonEffectue += "<li>" + sectionPos.getIdExterne();
                }
                if (sectionNeg.getSession().hasErrors()) {
                    errorMessages += "<li>" + sectionNeg.getSession().getErrors().toString();
                }
            }
        }
        // =====================FIN DU LETTRAGE ===========================

        if (JadeStringUtil.isEmpty(errorMessages)) {

            /*
             * Bulletin de soldes
             */

            for (String idSection : sectionPosMap.keySet()) {

                /*
                 * Si l'utilisateur n'est pas manuellement bloqué la sortie de ce bulletin de solde (bsParama contient
                 * les id de sections pour lequelles on veut un bulletin de solde, sous la forme : BS_1;BS_2;BS_3;etc...
                 * ou 1,2 et 3 sont les id de sections
                 */
                if (viewBean.getBsParams().indexOf("BS_" + idSection + ";") != -1) {

                    FWCurrency montant = sectionPosMap.get(idSection);

                    /*
                     * Si montant a lettrer n'est pas 0 chf
                     */
                    if (!montant.isZero()) {
                        CASection sectionPos = new CASection();
                        sectionPos.setSession(session);
                        sectionPos.setIdSection(idSection);
                        sectionPos.retrieve();
                        _sortBulletinDeSoldes(session, idSection);
                    }
                }
            }

        }
        String status = "";
        if (JadeStringUtil.isEmpty(errorMessages)) {
            // KO et OK sont également utilisé côté client par le javascript.
            if (!JadeStringUtil.isEmpty(lettrageNonEffectue)) {
                // Pas d'erreurs à proprement parler, mais certaines sections ont étés écartés de la procédure ,
                // par exemple si ordre de remboursement en attente sur section créditrice... voir inforom 146
                // (CASection.doLettrageSectionParDate(...))
                // IN -> INFO
                status = "IN,Information : "
                        + session.getApplication().getLabel("GCA70002_LETTRAGE_PAS_EFFECTUE_1",
                                session.getIdLangueISO())
                        + compteAnnexe.getIdExterneRole()
                        + ", "
                        + session.getApplication().getLabel("GCA70002_LETTRAGE_PAS_EFFECTUE_2",
                                session.getIdLangueISO()) + " :" + lettrageNonEffectue;
            } else {
                String afficheSection = "javascript:getSectionsForCompte(" + compteAnnexe.getIdRole() + ",\""
                        + compteAnnexe.getIdExterneRole() + "\"," + compteAnnexe.getIdCompteAnnexe() + ")";
                status = "OK, "
                        + session.getApplication().getLabel("GCA70002_LETTRAGE_COMPTE", session.getIdLangueISO())
                        + " : <a href='#' onclick='" + afficheSection + "'>" + compteAnnexe.getIdExterneRole()
                        + "</a> "
                        + session.getApplication().getLabel("GCA70002_LETTRAGE_EFFECTUE", session.getIdLangueISO());
            }
        } else {
            status = "KO, " + session.getApplication().getLabel("GCA70002_LETTRAGE_COMPTE", session.getIdLangueISO())
                    + " : " + compteAnnexe.getIdExterneRole() + " "
                    + session.getApplication().getLabel("GCA70002_LETTRAGE_PAS_EFFECTUE_3", session.getIdLangueISO())
                    + " :" + errorMessages;
        }

        viewBean.getResponse().append(status);
    }

    /*
     * Gère le report et les commentaires sur une section
     */
    private void _actionReportSection(BSession session, CALettrageViewBean viewBean, List<String> catCreditriceExclues)
            throws Exception {
        String idCompteAnnexe = viewBean.getIdCompteAnnexe();
        String idSection = viewBean.getIdSection();
        String role = viewBean.getRole();
        String comment = viewBean.getComment();

        CASection section = new CASection();
        section.setSession(session);
        section.setIdSection(idSection);
        section.retrieve();
        if (session.hasErrors()) {
            throw new Exception("KO," + session.getErrors().toString());
        }
        section.setIdModeCompensation(viewBean.getIdModeCompensation());

        /*
         * Remarques sur la section
         */
        CARemarque rem = section.getRemarque();

        BITransaction trans = session.newTransaction();
        trans.openTransaction();

        try {

            // il y a trois cas de figure pour transfèrer le commentaire vers une remarque de la section :
            if ((rem != null) && !JadeStringUtil.isIntegerEmpty(rem.getIdRemarque())) {
                rem.setSession(session);
                if (!JadeStringUtil.isEmpty(comment)) {
                    // 1) une remarque existe déjà, et j'ai un commentaire -> je mes a jour la remarque
                    rem.retrieve();
                    rem.setTexte(comment);
                    rem.update(trans);
                    if (session.hasErrors()) {
                        throw new Exception("KO," + session.getErrors().toString());
                    }
                } else {
                    // 2) une remarque existe , mais je n'ai plus de commentaire -> je supprime la remarque
                    rem.delete(trans);
                    section.setIdRemarque("0");
                    if (session.hasErrors()) {
                        throw new Exception("KO," + session.getErrors().toString());
                    }
                }
            } else if (!JadeStringUtil.isEmpty(comment)) {
                // 3) il n'y a pas de remarque, et j'ai un commenatire ->
                // je crée une remarque
                rem = new CARemarque();
                rem.setSession(session);
                rem.setTexte(comment);
                rem.add(trans);
                if (session.hasErrors()) {
                    throw new Exception("KO," + session.getErrors().toString());
                }
                section.setIdRemarque(rem.getIdRemarque());

            }

            section.update(trans);
            if (session.hasErrors()) {
                throw new Exception("KO," + session.getErrors().toString());
            }
            trans.commit();
        } finally {
            System.out.println(">" + idSection);
            trans.closeTransaction();
        }

        /*
         * Navigation
         * 
         * NM -> No Message -> Ok mais pas de message particulier à l'urilisateur (il y a quand même un message par
         * défaut qui dit que l'opération a été éffectuée avec succes)
         */
        String result = "NM,"
                + _renderQuerySection(session, viewBean.getContextPath(), CAMainHelper.mainQuery(role), idCompteAnnexe,
                        catCreditriceExclues);
        viewBean.getResponse().append(result);

    }

    /*
     * --------------------------------------------------------------------------------------------
     * 
     * Méthodes utilitaires
     * 
     * --------------------------------------------------------------------------------------------
     */

    private void _actionVersementSection(BSession session, CALettrageViewBean viewBean,
            final List<String> catCreditriceExclues) throws Exception {

        String idCompteAnnexe = viewBean.getIdCompteAnnexe();
        String idSection = viewBean.getIdSection();
        String role = viewBean.getRole();
        FWCurrency cMontant = new FWCurrency(viewBean.getMontantVersement());
        cMontant.abs();
        String montant = cMontant.toString();
        String category = viewBean.getCategory();

        String date = JACalendar.todayJJsMMsAAAA();
        String motif = "";

        String nature = CAOrdreGroupe.NATURE_VERSEMENT_REMBOURSEMENT_COT; // par defaut
        if (APISection.ID_CATEGORIE_SECTION_ALLOCATIONS_FAMILIALES.equals(category)) {
            nature = CAOrdreGroupe.NATURE_VERSEMENT_AF;
        } else if (APISection.ID_CATEGORIE_SECTION_APG.equals(category)) {
            nature = CAOrdreGroupe.NATURE_VERSEMENT_APG;
        } else if (StringUtils.equals(APISection.ID_CATEGORIE_SECTION_PANDEMIE,category)) {
            nature= CAOrdreGroupe.NATURE_VERSEMENT_PANDEMIE;
        }

        String type = APIOperationOrdreVersement.VIREMENT;

        CAJournal journal = CAJournal.fetchJournalJournalier(session, null);
        String CHF = session.getCode("215001");

        CAOperationOrdreVersement ordreVersement = new CAOperationOrdreVersement();
        ordreVersement.setIdJournal(journal.getIdJournal());
        ordreVersement.setSession(session);
        ordreVersement.setIdCompteAnnexe(idCompteAnnexe);
        ordreVersement.setIdSection(idSection);
        // ordreVersement.setIdAdressePaiement(idAdressePaiement);
        ordreVersement.setDefaultIdAdressePaiement();
        ordreVersement.setDate(date);
        ordreVersement.setMontant(montant);
        ordreVersement.setCodeISOMonnaieBonification(CHF);
        ordreVersement.setCodeISOMonnaieDepot(CHF);
        ordreVersement.setTypeVirement(type);
        ordreVersement.setNatureOrdre(nature);
        ordreVersement.setMotif(motif);
        ordreVersement.setCodeDebitCredit(APIEcriture.DEBIT);
        ordreVersement.add();

        if (session.hasErrors()) {
            throw new Exception("KO," + session.getErrors().toString());
        }

        String result = "NM,"
                + _renderQuerySection(session, viewBean.getContextPath(), CAMainHelper.mainQuery(role), idCompteAnnexe,
                        catCreditriceExclues);
        viewBean.getResponse().append(result);
    }

    /*
     * les flags pour une sections sont : icone lsv si lsv ou dd en attente Plan de pmt : A CTX => Aucun -> Vide
     * Sommation -> S Décision -> D sinon -> P
     */
    private String _buildFlags(BSession session, String contextPath, String idCompteAnnexe, String idSection,
            String idExterne, String comment, String lastCsEtapeCtx, String etatPlanPmt, String modeCompensation,
            String attenteLSVDD, Map<String, List<Map<String, String>>> sectionOrdreEnAttente) throws Exception {
        String flags = "";
        if (attenteLSVDD.equals(BConstants.DB_BOOLEAN_TRUE) || sectionOrdreEnAttente.containsKey(idSection)) {
            flags += "<img src='images/pending.gif' label>";
        }

        if (!JadeStringUtil.isIntegerEmpty(etatPlanPmt)) {
            flags += "A";
        }
        if (!JadeStringUtil.isIntegerEmpty(lastCsEtapeCtx)) {

            if (!JadeStringUtil.isEmpty(flags)) {
                flags += ", ";
            }

            switch (Integer.parseInt(lastCsEtapeCtx)) {
                case ETAPE_AUCUNE:
                    break;
                case ETAPE_SOMMATION:
                    flags += "S";
                    break;
                case ETAPE_DECISION:
                    flags += "D";
                    break;
                default:
                    flags += "P";
            }
        }
        /*
         * Mode de compensation de la section
         */
        if (!JadeStringUtil.isEmpty(flags)) {
            flags += ", ";
        }
        comment = comment.replace("\r", "");
        comment = comment.replace("\n", "\\n").trim();
        comment = JadeStringUtil.change(comment, "¬", "\\x27"); // '
        comment = JadeStringUtil.change(comment, "¢", "\\\"");

        if (session.hasRight("osiris.lettrage.main.reportSection", FWSecureConstants.UPDATE)) {
            if (!JadeStringUtil.isIntegerEmpty(modeCompensation)) {
                flags += "<b style='color:red'><a href='#' onclick='javascript:showDialogReport(this," + idCompteAnnexe
                        + "," + idSection + "," + idExterne + "," + modeCompensation + ",\"" + comment + "\")'>"
                        + session.getCodeLibelle(modeCompensation) + "</a></b>";
            } else {
                flags += "<a href='#' onclick='javascript:showDialogReport(this," + idCompteAnnexe + "," + idSection
                        + "," + idExterne + "," + modeCompensation + ",\"" + comment + "\")'>"
                        + session.getApplication().getLabel("GCA70002_STANDARD", session.getIdLangueISO()) + "</a>";
            }
        }

        /*
         * Commentaires
         */
        flags += (!JadeStringUtil.isEmpty(comment)) ? "<img src='" + contextPath + "/images/attach.png'>" : "";
        return flags;
    }

    /*
     * Permet de savoir si un affilié est radié ou nom
     */
    private boolean _isRadie(BSession session, String numAff) throws Exception {
        AFAffiliationManager mgr = new AFAffiliationManager();
        mgr.setSession(session);
        mgr.setForAffilieNumero(numAff);
        mgr.find();
        if (mgr.size() == 0) {
            return false; // pas d'affiliation, mais pas radié...
        }
        for (AFAffiliation aff : (Iterable<AFAffiliation>) mgr) {

            String debPeriode = aff.getDateDebut();
            String finPeriode = aff.getDateFin();
            if (JadeStringUtil.isEmpty(finPeriode)) {
                finPeriode = "31.12.9999";
            }
            if (BSessionUtil.compareDateBetween(session, debPeriode, finPeriode, JACalendar.todayJJsMMsAAAA()) == true) {
                return false;
            }
        }
        return true;
    }

    /*
     * ------------------------------------------------------------------------ Action de recherche des compte annexes
     * pour une plage le flux est retourné sous forme d'un <select>...</select>
     */
    private void _query(final BSession session, CALettrageViewBean viewBean, final List<String> catCreditriceExclues)
            throws Exception {

        String role = viewBean.getRole();
        String from = viewBean.getFromCompteAnnexe();
        String to = viewBean.getToCompteAnnexe();
        String filter = viewBean.getFilter();

        StringBuffer result = new StringBuffer("");
        String test = "";
        if (!JadeStringUtil.isEmpty(from)) {
            test += " and IDEXTERNEROLE >= '" + from + "'";
        }
        if (!JadeStringUtil.isEmpty(to)) {
            test += " and IDEXTERNEROLE <= '" + to + "'";
        }

        List<Map<String, String>> res = TISQL
                .query(session,
                        "IDEXTERNEROLE,a.IDCOMPTEANNEXE IDCOMPTEANNEXE, a.IDLASTETATAQUILA IDLASTETATAQUILA,"
                                + " a.CATEGORIESECTION CATEGORIESECTION, a.SOLDE SOLDE,a.IDSECTION IDSECTION, e.IDEXCL IDEXCL,  pl.IDETAT IDETAT",
                        CAMainHelper.mainQuery(role) + test + " order by IDEXTERNEROLE");

        Map<String, String> compteDebitCredit = new LinkedHashMap<String, String>(); // section créditrice non exclue et
                                                                                     // section débitrice
        Map<String, String> compteCreditSeul = new LinkedHashMap<String, String>(); // section créditrice non exclue
                                                                                    // mais pas de section débitrice
        Map<String, String> compteInfo = new LinkedHashMap<String, String>(); // contient les comptes n'ayant que des
                                                                              // sections créditrices exclues

        /*
         * Construit une "liste de liste de Map" à partir du resultat de la requête dans ce cas, une liste de liste de
         * resultats par IDEXTERNEROLE ( donc par compte annexe)
         * 
         * compte annexe A compte annexe B [ [Map123, Map456, ...] , [Map789], ... ]
         */
        List<List<Map<String, String>>> rList = JadeListUtil.splitList(res,
                new JadeListUtil.Splitter<Map<String, String>>() {
                    @Override
                    public boolean splitWhen(Map<String, String> a, Map<String, String> b) {
                        String c1 = a.get("IDEXTERNEROLE");
                        String c2 = b.get("IDEXTERNEROLE");
                        return !c1.equals(c2);
                    }
                });

        // ne prend que les CA ayant au moins une section créditrice
        rList = JadeListUtil.filter(rList, new JadeListUtil.Each<List<Map<String, String>>>() {
            @Override
            public boolean eval(List<Map<String, String>> e) {
                return JadeListUtil.any(e, new JadeListUtil.Each<Map<String, String>>() {
                    @Override
                    public boolean eval(Map<String, String> m) {
                        String solde = m.get("SOLDE");
                        solde = JadeStringUtil.change(solde, ",", ".");
                        boolean creditrice = Double.parseDouble(solde) < 0;
                        return creditrice;
                    }
                });
            }
        });

        /*
         * Filtres optionels
         */
        if (CAMainHelper.FILTER_AVANT_REQ_POURSUITE.equals(filter)) {
            rList = JadeListUtil.filter(rList, new JadeListUtil.Each<List<Map<String, String>>>() {
                @Override
                public boolean eval(List<Map<String, String>> e) {
                    return JadeListUtil.all(e, new JadeListUtil.Each<Map<String, String>>() {
                        @Override
                        public boolean eval(Map<String, String> e) {
                            String lastCsEtapeCtx = e.get("IDLASTETATAQUILA");

                            /*
                             * Aucune étape ctx n'existe, ou si il y en a une, elle est avant la rp on GARDE le cas dans
                             * la liste
                             */
                            return JadeStringUtil.isIntegerEmpty(lastCsEtapeCtx)
                                    || CAMainHelper.ETAPES_AVANT_RP.contains(lastCsEtapeCtx);
                        }
                    });
                }
            });
        }
        if (CAMainHelper.FILTER_DES_REQ_POURSUITE.equals(filter)) {
            rList = JadeListUtil.filter(rList, new JadeListUtil.Each<List<Map<String, String>>>() {
                @Override
                public boolean eval(List<Map<String, String>> e) {
                    return JadeListUtil.any(e, new JadeListUtil.Each<Map<String, String>>() {
                        @Override
                        public boolean eval(Map<String, String> e) {
                            String lastCsEtapeCtx = e.get("IDLASTETATAQUILA");

                            /*
                             * Une étape de ctx existe, et elle n'est pas avant la rp on SUPPRIME le cas dans la liste
                             */
                            return !JadeStringUtil.isIntegerEmpty(lastCsEtapeCtx)
                                    && !CAMainHelper.ETAPES_AVANT_RP.contains(lastCsEtapeCtx);
                        }
                    });
                }
            });
        }

        if (CAMainHelper.FILTER_EXCLUSION_MANUEL.equals(filter)) {
            rList = JadeListUtil.filter(rList, new JadeListUtil.Each<List<Map<String, String>>>() {
                @Override
                public boolean eval(List<Map<String, String>> e) {
                    return JadeListUtil.any(e, new JadeListUtil.Each<Map<String, String>>() {
                        @Override
                        public boolean eval(Map<String, String> e) {
                            String idExclusion = e.get("IDEXCL");
                            return !JadeStringUtil.isIntegerEmpty(idExclusion);
                        }
                    });
                }
            });
        }

        if (CAMainHelper.FILTER_AVEC_SURSIS_PMT.equals(filter)) {
            rList = JadeListUtil.filter(rList, new JadeListUtil.Each<List<Map<String, String>>>() {
                @Override
                public boolean eval(List<Map<String, String>> e) {
                    return JadeListUtil.any(e, new JadeListUtil.Each<Map<String, String>>() {
                        @Override
                        public boolean eval(Map<String, String> e) {
                            String etatPlanPmt = e.get("IDETAT");
                            return !JadeStringUtil.isIntegerEmpty(etatPlanPmt);
                        }
                    });
                }
            });
        }
        if (CAMainHelper.FILTER_SANS_SURSIS_PMT.equals(filter)) {
            rList = JadeListUtil.filter(rList, new JadeListUtil.Each<List<Map<String, String>>>() {
                @Override
                public boolean eval(List<Map<String, String>> e) {
                    return JadeListUtil.all(e, new JadeListUtil.Each<Map<String, String>>() {
                        @Override
                        public boolean eval(Map<String, String> e) {
                            String etatPlanPmt = e.get("IDETAT");
                            return JadeStringUtil.isIntegerEmpty(etatPlanPmt);
                        }
                    });
                }
            });
        }

        /*
         * Repartition des comptes annexes en 3 groupes :
         * 
         * 1) ceux qui ont des sections créditrices (non excules) et des sections débitrices 2) ceux qui ont des
         * sections créditrices (non excules) mais pas de sections débitrices 3) ceux qui n'ont que des section
         * créditrices excules
         */

        for (Iterator<List<Map<String, String>>> it = rList.iterator(); it.hasNext();) {
            List<Map<String, String>> ca = it.next();
            if (!ca.isEmpty()) {
                String id = ca.get(0).get("IDCOMPTEANNEXE");
                String val = ca.get(0).get("IDEXTERNEROLE");

                /*
                 * Est-ce que ce compte annexe a au moin une section céditrice utilisabale ? donc au moins une section
                 * créditrice qui n'est pas exclue systématiquement et qui n'est pas exclue manuellement du lettrage
                 * 
                 * Si oui le compte annexe ira dans un liste "compteALettrer" sinon, dans une liste "compteInfo" de
                 * compte annexe figurant pour information mais n'étant pas lettrable puisque pas de sections
                 * créditrices utilisable...
                 */
                boolean asDebitrice = JadeListUtil.any(ca, new JadeListUtil.Each<Map<String, String>>() {
                    @Override
                    public boolean eval(Map<String, String> m) {
                        String solde = m.get("SOLDE");
                        solde = JadeStringUtil.change(solde, ",", ".");
                        boolean debitrice = Double.parseDouble(solde) > 0;
                        if (debitrice) {
                            return true; // sections débitrices
                        }
                        return false;
                    }
                });

                boolean hasCreditriceNonExclue = JadeListUtil.any(ca, new JadeListUtil.Each<Map<String, String>>() {
                    @Override
                    public boolean eval(Map<String, String> m) {

                        String cat = m.get("CATEGORIESECTION");
                        String solde = m.get("SOLDE");

                        solde = JadeStringUtil.change(solde, ",", ".");
                        boolean debitrice = Double.parseDouble(solde) > 0;
                        if (debitrice) {
                            return false; // sections débitrices
                        }
                        if (!JadeStringUtil.isIntegerEmpty(m.get("IDEXCL"))) {
                            return false; // sections exclues
                        }

                        boolean res = !(catCreditriceExclues.contains(cat)); // exclure systematiquement
                                                                             // certaines
                                                                             // section créditrice du
                                                                             // lettrage

                        return res;
                    }
                });
                boolean hasOVComptabilise = JadeListUtil.all(ca, new JadeListUtil.Each<Map<String, String>>() {
                    @Override
                    public boolean eval(Map<String, String> m) {
                        ArrayList<String> liste = new ArrayList<String>();
                        String solde = m.get("SOLDE");
                        solde = JadeStringUtil.change(solde, ",", ".");
                        liste.add(APIOperation.ETAT_COMPTABILISE);
                        // liste.add(APIOperation.ETAT_PROVISOIRE);
                        // liste.add(APIOperation.ETAT_OUVERT);
                        ArrayList<String> listeJournal = new ArrayList<String>();
                        listeJournal.add(CAJournal.ANNULE);
                        String idSection = m.get("IDSECTION");
                        CAOperationOrdreVersementLettrageManager ovManager = new CAOperationOrdreVersementLettrageManager();
                        ovManager.setSession(session);
                        ovManager.setForIdSection(idSection);
                        ovManager.setForEtatIn(liste);
                        ovManager.wantForEstBloque(false);
                        ovManager.setForEtatJournalNotIn(listeJournal);
                        try {
                            ovManager.find();
                            FWCurrency fMontant = new FWCurrency("0");
                            for (int i = 0; i < ovManager.size(); i++) {
                                CAOperation op = (CAOperation) ovManager.getEntity(i);
                                fMontant.add(op.getMontant());

                            }
                            FWCurrency fSolde = new FWCurrency("0");

                            fSolde.add(solde);
                            fSolde.add(fMontant);
                            if (fSolde.isZero()) {
                                return true;
                            } else {
                                return false;
                            }

                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        return false;
                    }
                });

                if (asDebitrice && hasCreditriceNonExclue) {
                    compteDebitCredit.put(id, val);
                } else if (hasCreditriceNonExclue && !hasOVComptabilise) {
                    // } else if (hasCreditriceNonExclue) {
                    compteCreditSeul.put(id, val);
                } else {
                    compteInfo.put(id, val);
                }
            }
        }

        /*
         * Construction de la liste "final" des compte annexe, avec deux groupes : les compte annexe lettrable, et ceux
         * n'ayant que des sections créditices exclues
         */
        result.append("<input style='width:100%' type='button' value='"
                + session.getApplication().getLabel("GCA70002_LETTRAGE", session.getIdLangueISO()) + " - "
                + compteDebitCredit.size() + "' onclick='javascript:" + "$(\"#resultListDiv\").show();"
                + "$(\"#resultListCreditDiv\").hide();" + "$(\"#resultListExcluDiv\").hide();" + "'><br>");
        result.append("<div id='resultListDiv' ><select style='width:100%' size=20 id='resultList' >");
        for (String idCompteAnnexeList : compteDebitCredit.keySet()) {
            String numeroCompteAnnexeList = compteDebitCredit.get(idCompteAnnexeList);
            result.append("<option value='" + idCompteAnnexeList + "'>" + numeroCompteAnnexeList + "</option>");
        }
        result.append("</select></div>");

        result.append("<input style='width:100%' type='button' value='"
                + session.getApplication().getLabel("GCA70002_CREDIT_SEUL", session.getIdLangueISO()) + " - "
                + compteCreditSeul.size() + "' onclick='javascript:" + "$(\"#resultListDiv\").hide();"
                + "$(\"#resultListCreditDiv\").show();" + "$(\"#resultListExcluDiv\").hide();" + "'><br>");
        result.append("<div id='resultListCreditDiv' style='display:none' ><select style='width:100%' size=20 id='resultListCredit'>");
        for (String idCompteAnnexeList : compteCreditSeul.keySet()) {
            String numeroCompteAnnexeList = compteCreditSeul.get(idCompteAnnexeList);
            result.append("<option value='" + idCompteAnnexeList + "'>" + numeroCompteAnnexeList + "</option>");
        }
        result.append("</select></div>");

        result.append("<input style='width:100%' type='button' value='Info - " + compteInfo.size()
                + "' onclick='javascript:" + "$(\"#resultListDiv\").hide();" + "$(\"#resultListCreditDiv\").hide();"
                + "$(\"#resultListExcluDiv\").show();" + "'><br>");
        result.append("<div id='resultListExcluDiv' style='display:none' ><select style='width:100%' size=20 id='resultListExclu'>");
        for (String idCompteAnnexeList : compteInfo.keySet()) {
            String numeroCompteAnnexeList = compteInfo.get(idCompteAnnexeList);
            result.append("<option value='" + idCompteAnnexeList + "'>" + numeroCompteAnnexeList + "</option>");
        }
        result.append("</select></div>");

        viewBean.getResponse().append(result);
    }

    /*
     * ------------------------------------------------------------------------ Action de lecture des sections pour un
     * compte annexe
     */
    private void _querySection(BSession session, CALettrageViewBean viewBean, final List<String> catCreditriceExclues)
            throws Exception {
        String role = viewBean.getRole();
        String idCompteAnnexe = viewBean.getIdCompteAnnexe();
        String result = _renderQuerySection(session, viewBean.getContextPath(), CAMainHelper.mainQuery(role),
                idCompteAnnexe, catCreditriceExclues);
        viewBean.getResponse().append(result);
    }

    /*
     * Réponse à la requête "pyxis.test.lettrage.querySection" Contient les sections liées à un compte annexe sous forme
     * d'un tableau croisé ayant sur un axe les section positive et sur l'autre les sections négatives
     */
    private String _renderQuerySection(BSession session, String contextPath, String query, String idCompteAnnexe,
            final List catCreditriceExclues) throws Exception {
        String col = TIToolBox.getCollection();
        /*
         * -------------------------------------------------------------- Zone de requête Afin de garder sous controle
         * le nombre de requête sql executer pour chaque lecture de compte annexe, toutes les requête doivent se faire
         * dans cette zone.
         * 
         * --------------------------------------------------------------
         */
        // Requête principale pour trouver les sections du compte annexe
        List<Map<String, String>> rList = TISQL
                .query(session,
                        "IDEXTERNEROLE,a.IDSECTION IDSECTION,IDTIERS ,t.htlde1 HTLDE1,t.htlde2 HTLDE2,a.IDEXTERNE IDEXTERNE,DATESECTION,a.DATEECHEANCE DATEECHEANCE,"
                                + "a.IDMODECOMPENS IDMODECOMPENS,a.SOLDE SOLDE, a.IDLASTETATAQUILA IDLASTETATAQUILA, a.CATEGORIESECTION CATEGORIESECTION,"
                                + " a.ATTENTELSVDD ATTENTELSVDD, e.IDEXCL IDEXCL, pl.IDETAT IDETAT, re.TEXTE REMARQUE",
                        query + " and a.IDCOMPTEANNEXE=" + idCompteAnnexe
                                + " order by IDEXTERNEROLE, a.idexterne, a.DATEECHEANCE");

        // construit la liste des id sections concernées pour aller chercher de l'info complémentaire (plan de pmt)
        Set<String> allSections = new HashSet<String>();
        for (Map<String, String> m : rList) {
            allSections.add(m.get("IDSECTION"));
        }

        // Recherche des motifs de blocage sur le compte annexe
        CAMotifContentieuxManager motifCtxCAMgr = new CAMotifContentieuxManager();
        motifCtxCAMgr.setSession(session);
        motifCtxCAMgr.setForIdCompteAnnexe(idCompteAnnexe);
        motifCtxCAMgr.setFromDateBetweenDebutFin(JACalendar.todayJJsMMsAAAA());
        motifCtxCAMgr.find();

        Map<String, String> sectionPlanPmt = null;
        List<Map<String, String>> sectionMotifBlocage = null;
        if (allSections.size() > 0) {
            // Recherche l'etat d'un plan de paiement lié aux sections (si il y en a un...)
            sectionPlanPmt = TISQL.queryMap(session, "s.IDSECTION IDSECTION", "p.IDETAT IDETAT",
                    "from " + col + "CAPLARP p, " + col
                            + "CASECTP s where s.IDPLANRECOUVREMENT = p.IDPLANRECOUVREMENT and s.IDSECTION in ("
                            + CAMainHelper._join(allSections) + ")");
            // Recherche des motifs de blocage sur les sections
            sectionMotifBlocage = TISQL.query(session,
                    "s.IDSECTION IDSECTION,m.IDMOTIFBLOCAGE IDMOTIFBLOCAGE,s.IDEXTERNE IDEXTERNE ", "from " + col
                            + "CAMOCOP m, " + col + "CASECTP s   where s.IDSECTION = m.IDSECTION AND "
                            + JACalendar.today().toStrAMJ() + " BETWEEN DATEDEBUT AND DATEFIN AND s.IDSECTION in ("
                            + CAMainHelper._join(allSections) + ") order by IDSECTION");
        }
        String compteAnnexe = (String) ((Map) rList.get(0)).get("IDEXTERNEROLE");
        // l'affilié lié au compte annexe est il radié ? (information util pour la rêgle des sections créditrice de
        // catégorie 20)
        boolean isRadie = _isRadie(session, compteAnnexe);

        /*
         * Recherche des ordres en attente pour un compte annexe, dans une map ayant comme clé les id de section ayant
         * un ordre en attente.
         * 
         * Attention, il se put qu'un ordre de versement n'aie pas encore d'ordre groupé associé... d'ou le left join.
         * De plus, si le journal (CAJOURNAL) associé a l'og est traité (ie Comptabilisé ou annulé), ses ordre de
         * versement sont considéré comme n'étant plus en attente (nécessaire pour le cas ou un ov est refusé et quie le
         * montant créditeur reste malgré l'ov...)
         */

        List<Map<String, String>> sectionOrdreEnAttenteRs = TISQL.query(session,
                "o.IDSECTION IDSECTION, v.idordre IDORDRE, v.estbloque ESTBLOQUE, o.ETAT ETAT", " from " + col
                        + "caoperp o" + " inner join " + col + "caopovp v on (v.idordre = o.idoperation)"
                        + " where o.idcompteannexe=" + idCompteAnnexe + " and (" + "	(v.idordregroupe = 0) or ("
                        + "v.idordregroupe in (select idordregroupe from " + col + "CAORGRP og inner join " + col
                        + "CAJOURP j on (og.idjournal = j.idjournal and j.ETAT not in (202002,202005) )" + ")))"

        );

        Map<String, List<Map<String, String>>> sectionOrdreEnAttente = JadeListUtil.groupBy(sectionOrdreEnAttenteRs,
                new JadeListUtil.Key<Map<String, String>>() {
                    @Override
                    public String exec(Map<String, String> m) {

                        return m.get("IDSECTION");
                    }
                });

        /*
         * adresse de pmt disponible (pour remboursement)
         */
        String idTiers = rList.get(0).get("IDTIERS");
        TIAvoirPaiementManager ap = new TIAvoirPaiementManager();
        ap.setSession(session);
        ap.setForDateEntreDebutEtFin(JACalendar.todayJJsMMsAAAA());
        ap.setForIdApplication("519010"); // remboursement
        ap.setForIdTiers(idTiers);
        int nbAdrPmtDefautOrRemb = ap.getCount();
        ap.setForIdApplication(IConstantes.CS_APPLICATION_DEFAUT);
        nbAdrPmtDefautOrRemb += ap.getCount(); // optimization possible : faire un setForIdApplicationIn ds tiers...
        boolean hasAdressePmt = (nbAdrPmtDefautOrRemb > 0);

        /*
         * ----------------------------------------------------------------------------------------------------------
         * Fin zone des Requêtes
         * ----------------------------------------------------------------------------------------------------------
         */

        String designation = rList.get(0).get("HTLDE1") + "<br>" + rList.get(0).get("HTLDE2");
        designation = JadeStringUtil.change(designation, "¬", "'");
        designation = JadeStringUtil.change(designation, "¢", "\"");

        /*
         * A partir du resultat de ma requête principale, contenu dans rList, Je vais construire 2 liste pour la suite
         * du traitement. pos sera une liste contenant les sections positive dîtes "débitrices" neg sera une liste
         * contenant les sections négative dîtes "créditrices"
         * 
         * neg sera ensuite re-filtrée pour pouvoir traiter séparement les sections créditrices filtrées () et les
         * sections créditrices excluent (manuellement par l'utilisateur)
         * 
         * les sections créditrices filtrées et excluent sont traitées séparamenent car les sections créditrices
         * excluent manuellement par l'utilisateur peuvent être re-inclue dans le lettrage alors que ce n'est pas le cas
         * pour les sections créditrices filtées
         */
        List<Map<String, String>> neg = new ArrayList<Map<String, String>>();
        List<Map<String, String>> pos = new ArrayList<Map<String, String>>();
        List<Map<String, String>> negFiltred = new ArrayList<Map<String, String>>();
        List<Map<String, String>> excluFiltred = new ArrayList<Map<String, String>>();
        List<Map<String, String>> sectionsCreditriceAvecOrdreEnAttente = new ArrayList<Map<String, String>>();

        for (Map<String, String> m : rList) {

            String solde = m.get("SOLDE");
            solde = JadeStringUtil.change(solde, ",", ".");
            double d = Double.parseDouble(solde);
            boolean hasOrdreEnAttente = false;

            if (sectionOrdreEnAttente.get(m.get("IDSECTION")) != null) {
                ArrayList<String> liste = new ArrayList<String>();
                liste.add(APIOperation.ETAT_COMPTABILISE);
                liste.add(APIOperation.ETAT_PROVISOIRE);
                liste.add(APIOperation.ETAT_OUVERT);
                ArrayList<String> listeEtat = new ArrayList<String>();
                listeEtat.add(CAJournal.ANNULE);
                String idSection = m.get("IDSECTION");
                CAOperationOrdreVersementLettrageManager ma = new CAOperationOrdreVersementLettrageManager();
                ma.setSession(session);
                ma.setForIdSection(idSection);
                ma.setForEtatIn(liste);
                ma.wantForEstBloque(false);
                ma.setForEtatJournalNotIn(listeEtat);
                try {
                    if (ma.getCount() > 0) {
                        hasOrdreEnAttente = true;
                    }

                } catch (Exception e) {
                    hasOrdreEnAttente = false;// TODO: handle exception
                }

            }
            // boolean hasOrdreEnAttente = (sectionOrdreEnAttente.get(m.get("IDSECTION")) != null);
            /*
             * Séparation des sections ayant un montant positif de celles ayant un montant négatif
             */
            if (d < 0) {
                /*
                 * filtre des sections créditrices exclues qui sont affichée en dehors du tableau (pas de lettrage
                 * possible pour ces sections)
                 */
                if (catCreditriceExclues.contains(m.get("CATEGORIESECTION"))) {
                    negFiltred.add(m);
                } else if (!JadeStringUtil.isIntegerEmpty(m.get("IDEXCL"))) {
                    /*
                     * Cette section a étée exclue du lettrage
                     */
                    excluFiltred.add(m);
                } else if (hasOrdreEnAttente) {
                    sectionsCreditriceAvecOrdreEnAttente.add(m);

                } else {
                    neg.add(m); // sections créditrices "normales" apparaissant dans la grille de lettrage
                }
            } else {
                pos.add(m); // sections débitrices apparaissant dans la grille de lettrage
            }

        }
        // fin de la separation des sections en listes de travail.

        /*
         * Comme certaines sections créditrices peuvent être exclues ou filtrées, il se peut qu'il n'y ai plus aucune
         * section créditrice disponible pour le lettrage. le bouton "Lettrer" ne doit donc être afficher que si il y a
         * bien des sections créditrices (et débitrices)
         */
        String btLettrer = "";
        if ((neg.size() > 0) && (pos.size() > 0)
                && session.hasRight("osiris.lettrage.main.doLettrage", FWSecureConstants.UPDATE)) {

            btLettrer = "<input id ='btLettrer' type='button' value='"
                    + session.getApplication().getLabel("GCA70002_LETTRER", session.getIdLangueISO())
                    + "' onclick='doLettrage()' style='display:inline' >"
                    + "<div id='msgLettrer' style='display:none;color=red' >"
                    + session.getApplication().getLabel("GCA70002_ERREUR_MONTANT", session.getIdLangueISO()) + "</div>";
        }

        /*
         * Construction du titre du tableau contenu
         */
        String aff = "\t\n<span id='numeroCompteAnnexe' style='font-size:20pt'>" + compteAnnexe + "</span>"
                + "<input id='idCompteAnnexe' type='hidden' value='" + idCompteAnnexe + "'>"
                + "<input id='nbDebitrices' type='hidden' value='" + pos.size() + "'>"
                + "<input id='nbCreditrices' type='hidden' value='" + neg.size() + "'>" + "&nbsp;&nbsp;" + btLettrer
                + "<br><b>" + designation + "</b>";

        /*
         * Affichage des éventuels motifs de blocage sur le compte annexe
         */
        if (motifCtxCAMgr.size() > 0) {
            aff += "<br><br><b>"
                    + session.getApplication().getLabel("GCA70002_BLOQUER_CONTENTIEUX", session.getIdLangueISO())
                    + " </b>";

            for (int i = 0; i < motifCtxCAMgr.size(); i++) {
                CAMotifContentieux motif = (CAMotifContentieux) motifCtxCAMgr.get(i);
                aff += "<li>" + session.getCodeLibelle(motif.getIdMotifBlocage());
            }
            aff += "<br><br>";
        }

        /*
         * Construction des propositions de lettrage
         */
        PropositionModel model = null;
        try {
            model = CARulesCouvertureSectionDebitrices.buildPropositions(isRadie, pos, neg);
        } catch (Exception e) {
            /*
             * Si on n'arrive pas à créer les proposition (parce que les ids externe de section ne contient pas le bon
             * format par exemple...) on ne fait pas de propositions.
             */
            model = CARulesCouvertureSectionDebitrices.buildBlankPropositions(pos, neg);
        }

        String cols = "\t<td class='h' colspan='3' rowspan='3'>" + aff + "</td>\n";
        String colsSum = "";
        String colsMontant = "";
        StringBuffer data = new StringBuffer("");
        StringBuffer html = new StringBuffer("");
        int x = 0;
        int y = 0;

        /*
         * Axe X - Entête horizontales - sections débitrices ( montants positifs , donc sections en faveur de la caisse)
         */
        for (Map<String, String> m : pos) {

            String datesection = CAMainHelper._date(m.get("DATESECTION"));
            String dateEcheanceSection = CAMainHelper._date(m.get("DATEECHEANCE"));
            String modeCompensation = m.get("IDMODECOMPENS");

            /*
             * ! aussi modifier javascript pour que le ' soit traité
             */
            // String montant = JANumberFormatter.format(new BigDecimal( (String)m.get("SOLDE")));
            String montant = m.get("SOLDE");
            montant = JadeStringUtil.change(montant, ",", ".");
            String lastCsEtapeCtx = m.get("IDLASTETATAQUILA");
            String numSection = m.get("IDEXTERNE");
            String idSection = m.get("IDSECTION");
            String comment = m.get("REMARQUE");
            String attenteLSVDD = m.get("ATTENTELSVDD");
            String etatPlanPmt = sectionPlanPmt.get(idSection);
            String flags = _buildFlags(session, contextPath, idCompteAnnexe, idSection, numSection, comment,
                    lastCsEtapeCtx, etatPlanPmt, modeCompensation, attenteLSVDD, sectionOrdreEnAttente); // A: si plan
                                                                                                         // de pmt,
                                                                                                         // Vide si
                                                                                                         // étape
                                                                                                         // aucune,
                                                                                                         // S=
                                                                                                         // Sommation
                                                                                                         // , D =
                                                                                                         // Décision,
                                                                                                         // P pour
                                                                                                         // les
                                                                                                         // autres
                                                                                                         // étapes

            String ctxStyle = "";
            if (!JadeStringUtil.isIntegerEmpty(lastCsEtapeCtx)) {
                ctxStyle = STYLE_SECTION_CTX;
            }
            String message = "<b>" + numSection + "</b> : <ul>";
            if (!JadeStringUtil.isIntegerEmpty(lastCsEtapeCtx)) {
                message += "<li>" + session.getApplication().getLabel("GCA70002_CONTENTIEUX", session.getIdLangueISO())
                        + " : " + session.getCodeLibelle(lastCsEtapeCtx);
            }
            if (!JadeStringUtil.isIntegerEmpty(etatPlanPmt)) {
                message += "<li>"
                        + session.getApplication().getLabel("GCA70002_PLAN_PAIEMENT", session.getIdLangueISO()) + " : "
                        + session.getCodeLibelle(etatPlanPmt);
            }

            message += "</ul>";
            message = JadeStringUtil.change(message, "\'", "\\\'");

            String numSectionLink = "<a href='#' onclick='javascript:infoSection(" + idSection + "," + idCompteAnnexe
                    + ")'>" + numSection + "</a>";

            /*
             * Affichage des motifs de blocages sur les sections
             */
            cols += "\t<td class='h' " + ctxStyle + " onmouseover=\"showMessage('" + message + "')\">" + datesection
                    + "<br>" + dateEcheanceSection + "<br>" + flags + "<br>"
                    + "<input type='checkbox' checked name='BS_' id='BS_" + idSection + "'>"
                    + session.getApplication().getLabel("GCA70002_BS", session.getIdLangueISO()) + "<br><b>"
                    + numSectionLink + "</b>" + "<input type='hidden' id='x" + x + "' value='" + idSection
                    + "'></td>\n";
            colsMontant += "\t<td class='vh' id='sx" + x + "a0' ><b style='font-family : courier'>" + montant
                    + "</b></td>\n";
            colsSum += "\t<td class='vsum' id='px" + x + "a0'>" + model.soldesPos[x] + "</td>\n";
            x += 1;
        }

        /*
         * Axe Y - sections créditrices ( montants négatifs, donc en faveur de l'affilié)
         */
        for (Map<String, String> m : neg) {
            String datesection = CAMainHelper._date(m.get("DATESECTION"));
            String modeCompensation = m.get("IDMODECOMPENS");

            /*
             * ! aussi modifier javascript pour que le ' soit traité
             */
            // String montant = JANumberFormatter.format(new BigDecimal( (String)m.get("SOLDE")));
            String montant = m.get("SOLDE");
            montant = JadeStringUtil.change(montant, ",", ".");
            String lastCsEtapeCtx = m.get("IDLASTETATAQUILA");
            String numSection = m.get("IDEXTERNE");
            String idSection = m.get("IDSECTION");
            String comment = m.get("REMARQUE");
            String attenteLSVDD = m.get("ATTENTELSVDD");
            String category = m.get("CATEGORIESECTION");
            String etatPlanPmt = sectionPlanPmt.get(idSection);
            String flags = _buildFlags(session, contextPath, idCompteAnnexe, idSection, numSection, comment,
                    lastCsEtapeCtx, etatPlanPmt, modeCompensation, attenteLSVDD, sectionOrdreEnAttente); // A: si plan
                                                                                                         // de pmt,
                                                                                                         // Vide si
                                                                                                         // aucune,
                                                                                                         // S=
                                                                                                         // Sommation
                                                                                                         // , D =
                                                                                                         // Décision,
                                                                                                         // P pour
                                                                                                         // les
                                                                                                         // autres
                                                                                                         // étapes

            // boolean hasOrdreEnAttente = (sectionOrdreEnAttente.get(idSection) != null);
            boolean hasOrdreEnAttente = false;

            if (sectionOrdreEnAttente.get(m.get("IDSECTION")) != null) {
                ArrayList<String> liste = new ArrayList<String>();
                liste.add(APIOperation.ETAT_COMPTABILISE);
                liste.add(APIOperation.ETAT_PROVISOIRE);
                // liste.add(APIOperation.ETAT_OUVERT);
                ArrayList<String> listeJournal = new ArrayList<String>();
                idSection = m.get("IDSECTION");
                CAOperationOrdreVersementLettrageManager ma = new CAOperationOrdreVersementLettrageManager();
                ma.setSession(session);
                ma.setForIdSection(idSection);
                ma.setForEtatIn(liste);
                ma.wantForEstBloque(false);
                ma.setForEtatJournalNotIn(listeJournal);
                try {
                    if (ma.getCount() > 0) {
                        hasOrdreEnAttente = true;
                    }

                } catch (Exception e) {
                    hasOrdreEnAttente = false;// TODO: handle exception
                }

            }

            String ctxStyle = "";
            if (!JadeStringUtil.isIntegerEmpty(lastCsEtapeCtx)) {
                ctxStyle = STYLE_SECTION_CTX;
            }
            String message = "<b>" + numSection + "</b> : <ul>";
            if (!JadeStringUtil.isIntegerEmpty(lastCsEtapeCtx)) {
                message += "<li>" + session.getApplication().getLabel("GCA70002_CONTENTIEUX", session.getIdLangueISO())
                        + " : " + session.getCodeLibelle(lastCsEtapeCtx);
            }
            if (!JadeStringUtil.isIntegerEmpty(etatPlanPmt)) {
                message += "<li>Plan de paiement : " + session.getCodeLibelle(etatPlanPmt);
            }
            message += "</ul>";
            message = JadeStringUtil.change(message, "\'", "\\\'");
            String btVersement = "";
            if (hasAdressePmt && (!hasOrdreEnAttente)
                    && session.hasRight("osiris.lettrage.main.versementSection", FWSecureConstants.UPDATE)) {
                btVersement = "<input type='button' value='"
                        + session.getApplication().getLabel("GCA70002_OV", session.getIdLangueISO())
                        + "' onclick='versement(" + idCompteAnnexe + "," + idSection + "," + montant + "," + category
                        + ")'>";

            }

            String numSectionLink = "<a href='#' onclick='javascript:infoSection(" + idSection + "," + idCompteAnnexe
                    + ")'>" + numSection + "</a>";

            data.append("<tr>\n");
            data.append("\t<td class='h' " + ctxStyle + " nowrap onmouseover=\"showMessage('" + message + "')\">");
            if (session.hasRight("osiris.lettrage.main.exlureSection", FWSecureConstants.UPDATE)) {
                data.append("<input type='button' onclick='exclure(" + idCompteAnnexe + "," + idSection
                        + ")' value='X'>");
            }
            data.append("&nbsp;" + btVersement + "&nbsp;" + datesection + " - <b>" + numSectionLink + "</b>" + "<br>"
                    + flags + DIV_RULES + "<b>"
                    + session.getApplication().getLabel("GCA70002_REGLE", session.getIdLangueISO()) + " "
                    + model.rule[y] + "</b></div>");
            data.append("<input type='hidden' id='y" + y + "' value='" + idSection + "'></td>\n"
                    + "\t<td class='h' nowrap><b style='font-family : courier'>" + montant + "</b></td>\n"
                    + "\t<td class='hsum' nowrap id='n" + y + "'>" + model.soldesNeg[y] + "</td>\n");
            x = 0;

            /*
             * data cells
             */
            for (int i = 0; i < pos.size(); i++) {
                data.append("\t<td class='mtd'>");
                data.append("<input type='text' class='i' " + "id='x" + x + "y" + y + "' " + "onblur='_b(this)' "
                        + "onfocus='_f(this)' " + "onkeyDown='hkd(event,this)' " + "onkeyUp='hku(this)' "
                        + "name='a0' " + "value='" + model.propositions[x][y] + // debug : pour afficher l'odre des
                                                                                // propositions de letrage :
                                                                                // model.ordre[x][y]
                        "'>");
                data.append(DIV_RULES + "<center>" + model.ordre[x][y] + "</center></div>");
                data.append("</td>\n");

                x += 1;
            }
            data.append("</tr>\n");
            y += 1;
        }

        html.append("" + "<tr>" + cols + "</tr>\n" + "<tr>" + colsMontant + "</tr>\n" + "<tr>" + colsSum + "</tr>\n");

        html.append(data);
        html.append("<tr>");

        for (int i = 0; i < (pos.size() + 3); i++) {
            html.append("\t<td class='lr'>&nbsp;</td>\n");
        }
        html.append("</tr>\n");
        String rep = session.getApplication().getLabel("GCA70002_PRIORITE", session.getIdLangueISO())
                + "<a href='#' onclick='showPriorities()'>"
                + session.getApplication().getLabel("GCA70002_AFFICHER", session.getIdLangueISO()) + "</a> / "
                + "<a href='#' onclick='hidePriorities()'>"
                + session.getApplication().getLabel("GCA70002_MASQUER", session.getIdLangueISO()) + "</a>" + "<br><br>"
                +

                "<table class='mtable' cellpading=0 cellspacing=0>" + html.toString() + "</table>";

        /*
         * Affichage des règles
         */
        Map<String, String> desc = null;
        if ("DE".equalsIgnoreCase(session.getIdLangueISO())) {
            desc = CARulesCouvertureSectionDebitrices.ruleDescriptionDE;
        } else if ("IT".equalsIgnoreCase(session.getIdLangueISO())) {
            desc = CARulesCouvertureSectionDebitrices.ruleDescriptionIT;
        } else {
            desc = CARulesCouvertureSectionDebitrices.ruleDescriptionFR;
        }

        rep += DIV_RULES + "<b>Règle " + CARulesCouvertureSectionDebitrices.RULE_ANTE + " :</b> "
                + desc.get(CARulesCouvertureSectionDebitrices.RULE_ANTE) + "</div><br>" + DIV_RULES + "<b>Règle "
                + CARulesCouvertureSectionDebitrices.RULE_17 + " :</b> "
                + desc.get(CARulesCouvertureSectionDebitrices.RULE_17) + "</div><br>" + DIV_RULES + "<b>Règle "
                + CARulesCouvertureSectionDebitrices.RULE_CHRONO + " :</b> "
                + desc.get(CARulesCouvertureSectionDebitrices.RULE_CHRONO) + "</div><br>";

        rep += "<hr>";

        /*
         * Affichage des sections créditrices exclues
         */
        if (negFiltred.size() > 0) {
            rep += "<b>"
                    + session.getApplication().getLabel("GCA70002_SECTION_CREDITRICE_EXCLUE", session.getIdLangueISO())
                    + " : </b>";
            rep += "<table>";
            for (Map<String, String> m : negFiltred) {
                String solde = m.get("SOLDE");
                solde = JadeStringUtil.change(solde, ",", ".");
                rep += "<tr>" + "<td>" + CAMainHelper._date(m.get("DATESECTION")) + " - <b>" + m.get("IDEXTERNE")
                        + "</b></td>" + "<td>" + solde + "</td>" + "</tr>";
            }
            rep += "</table>";
            rep += "<hr>";
        }

        /*
         * Affichage des sections créditrices avec ordre en attente
         */
        if (sectionsCreditriceAvecOrdreEnAttente.size() > 0) {
            rep += "<b>" + session.getApplication().getLabel("GCA70002_SECTION_CREDIT_ORDRE", session.getIdLangueISO())
                    + " : </b>";
            rep += "<table>";
            for (Map<String, String> m : sectionsCreditriceAvecOrdreEnAttente) {

                String solde = m.get("SOLDE");
                String idSection = m.get("IDSECTION");
                Map<String, String> tmp = sectionOrdreEnAttente.get(idSection).get(0);// il n'y a qu'un seul
                // ordre en attente
                // possible par section
                String idOrdre = tmp.get("IDORDRE");
                String etat = tmp.get("ETAT"); // operation comptabilisé ?
                String action = "";
                if (APIOperation.ETAT_OUVERT.equals(etat)) {
                    if (session.hasRight("osiris.lettrage.main.annulerVersementSection", FWSecureConstants.UPDATE)) {
                        action = "<input type='button' value='"
                                + session.getApplication()
                                        .getLabel("GCA70002_SUPPRESSION_OV", session.getIdLangueISO())
                                + "' onclick='annulerVersement(" + idCompteAnnexe + "," + idSection + "," + idOrdre
                                + ")' > ";
                    }

                } else {

                    action = "<i>"
                            + session.getApplication().getLabel("GCA70002_SECTION_SUPPR_IMPOSSIBLE_1",
                                    session.getIdLangueISO())
                            + "'"
                            + session.getCodeLibelle(etat)
                            + "'"
                            + session.getApplication().getLabel("GCA70002_SECTION_SUPPR_IMPOSSIBLE_2",
                                    session.getIdLangueISO()) + "</i>";

                }
                solde = JadeStringUtil.change(solde, ",", ".");
                rep += "<tr>" + "<td>" + CAMainHelper._date(m.get("DATESECTION")) + " - <b>" + m.get("IDEXTERNE")
                        + "</b></td>" + "<td>" + solde + "</td><td>" + action + "</td></tr>";
            }
            rep += "</table>";
            rep += "<hr>";
        }

        /*
         * Affichage des sections filtées
         */
        if (excluFiltred.size() > 0) {
            rep += "<b>" + session.getApplication().getLabel("GCA70002_SECTION_EXCLUE", session.getIdLangueISO())
                    + "</b>";
            rep += "<table>";
            for (Map<String, String> m : excluFiltred) {
                String solde = m.get("SOLDE");
                solde = JadeStringUtil.change(solde, ",", ".");
                rep += "<tr>" + "<td>" + CAMainHelper._date(m.get("DATESECTION")) + " - <b>" + m.get("IDEXTERNE")
                        + "</b></td>" + "<td>" + solde + "</td>" + "<td>";
                if (session.hasRight("osiris.lettrage.main.inclureSection", FWSecureConstants.UPDATE)) {
                    rep += "<input type='button' onclick='inclure(" + m.get("IDEXCL") + "," + idCompteAnnexe + ","
                            + m.get("IDEXTERNE") + ")' value='"
                            + session.getApplication().getLabel("GCA70002_INCLURE", session.getIdLangueISO()) + "'>";
                }
                rep += "</td>" + "</tr>";
            }
            rep += "</table>";
            rep += "<hr>";
        }

        /*
         * Affichage des motifs de blocage sur les sections
         */
        if (sectionMotifBlocage.size() > 0) {
            rep += "<b>"
                    + session.getApplication().getLabel("GCA70002_SECTION_MOTIF_BLOCAGE", session.getIdLangueISO())
                    + " : </b>";
            rep += "<table>";
            for (Map<String, String> m : sectionMotifBlocage) {
                rep += "<tr>" + "<td>" + m.get("IDEXTERNE") + "</td><td>"
                        + session.getCodeLibelle(m.get("IDMOTIFBLOCAGE")) + "</td>" + "</tr>";
            }
            rep += "</table><hr>";
        }

        return rep;
    }

    /*
     * Sort un bulletin de solde
     */
    private void _sortBulletinDeSoldes(BSession session, String idSection) throws Exception {
        // String montantMinimeBulletinSolde = CAParametres.getMontantMinimeBulletinSolde(new BTransaction(session));
        BSession sessionMusca = new BSession("MUSCA");
        session.connectSession(sessionMusca);
        String montantMinimeNeg = sessionMusca.getApplication().getProperty(FAApplication.MONTANT_MINIMENEG);
        String montantMinimePos = sessionMusca.getApplication().getProperty(FAApplication.MONTANT_MINIMEPOS);

        CAImpressionBulletinsSoldes_Doc doc = new CAImpressionBulletinsSoldes_Doc();
        doc.setIdSection(idSection);
        doc.setMontantMinimePos(montantMinimePos);
        doc.setMontantMinimeNeg(montantMinimeNeg);
        doc.setNumeroReferenceInforom(CAImpressionBulletinsSoldes_Doc.NUM_REF_INFOROM_BVR_SOLDE);
        BSession sessionOsiris = (BSession) GlobazServer.getCurrentSystem().getApplication("OSIRIS")
                .newSession(session);
        doc.setSession(sessionOsiris);
        BProcessLauncher.start(doc);
    }

    /*
     * Pas utilisé
     */
    @Override
    public void afterExecute(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
    }

    @Override
    public void beforeExecute(FWViewBeanInterface viewBean, FWAction action, BISession session) throws Exception {
    }

    /*
	 *
	 *
	 */
    @Override
    public FWViewBeanInterface invoke(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        String actionPart = action.getActionPart();
        String exclure = CAApplication.getApplicationOsiris().getProperty("lettrage_exclure_sections_creditrices");
        List<String> catCreditriceExclues = JadeStringUtil.tokenize(exclure, ",");

        try {
            if ("display".equals(actionPart)) {
                // Rien à faire, mais c'est une action légale
            } else if ("query".equals(actionPart)) {
                _query((BSession) session, (CALettrageViewBean) viewBean, catCreditriceExclues);
            } else if ("querySection".equals(actionPart)) {
                _querySection((BSession) session, (CALettrageViewBean) viewBean, catCreditriceExclues);
            } else if ("doLettrage".equals(actionPart)) {
                _actionLettrage((BSession) session, (CALettrageViewBean) viewBean);
            } else if ("exlureSection".equals(actionPart)) {
                _actionExclureSection((BSession) session, (CALettrageViewBean) viewBean, catCreditriceExclues);
            } else if ("inclureSection".equals(actionPart)) {
                _actionInclureSection((BSession) session, (CALettrageViewBean) viewBean, catCreditriceExclues);
            } else if ("reportSection".equals(actionPart)) {
                _actionReportSection((BSession) session, (CALettrageViewBean) viewBean, catCreditriceExclues);
            } else if ("versementSection".equals(actionPart)) {
                _actionVersementSection((BSession) session, (CALettrageViewBean) viewBean, catCreditriceExclues);
            } else if ("annulerVersementSection".equals(actionPart)) {
                _actionAnnulerVersementSection((BSession) session, (CALettrageViewBean) viewBean, catCreditriceExclues);
            } else if ("infoSection".equals(actionPart)) {
                _actionInfoSection((BSession) session, (CALettrageViewBean) viewBean, catCreditriceExclues);
            } else {
                // préventif
                throw new Exception("CAMainHelper : Unknown action [" + action.toString() + "]");
            }
        } catch (Exception e) {
            viewBean.setMsgType(FWViewBeanInterface.ERROR);
            viewBean.setMessage(e.getMessage());
        }
        return viewBean;
    }

}
