package globaz.naos.services;

import globaz.framework.db.postit.FWNoteP;
import globaz.framework.db.postit.FWNotePManager;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BSession;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.assurance.AFAssuranceManager;
import globaz.naos.db.cotisation.AFCotiSearchEntity;
import globaz.naos.db.cotisation.AFCotiSearchManager;
import globaz.naos.db.lienAffiliation.AFLienAffiliation;
import globaz.naos.db.lienAffiliation.AFLienAffiliationManager;
import globaz.naos.db.nombreAssures.AFNombreAssures;
import globaz.naos.db.nombreAssures.AFNombreAssuresManager;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ch.globaz.naos.business.constantes.AFAffiliationType;

/**
 * 
 * @author oca
 * 
 */
public class AFEventailRegimeAff implements ITISummarizable {

    private final static String SEP = "&nbsp;<span style='color:silver'>|</span>&nbsp;";
    private final static String td1 = "style='background:#d8dee4'";
    private final static String td2 = "style='background:white' ";
    private boolean cotisationsVisible = true;

    private String element = "";
    private String icon = "";

    private int maxHorizontalItems = 0;

    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.MEDIUM;
    private final String noteKey = "globaz.naos.db.affiliation.AFAffiliationViewBean";

    private final String noteStyle = "background-color: rgb(255,235,85);"
            + "background-image : url('images/note.png');" + "background-repeat: no-repeat;"
            + "background-position: right bottom;" + "margin-bottom : 0.1cm;" + "padding : 0.1cm 0.1cm 0.1cm 0.1cm;";
    private final String openLinkStyle = "style='color:blue;cursor:hand;text-decoration:none;border-bottom:1px dotted blue; }'";

    private String style = "";
    private String title = "";
    private String urlTitle = "";

    /*
     * rempli une ligne de tableau concerant une cotisation
     */
    private StringBuffer _buildCotisation(AFCotiSearchEntity e, BSession userSession, int index,
            boolean rightForPlanAffiliation) {

        if (JadeStringUtil.isIntegerEmpty(e.getIdCotisation())) {
            return null;
        }

        StringBuffer cot = new StringBuffer();
        String td = "";
        if ((index % 2) == 0) {
            td = AFEventailRegimeAff.td2;
        } else {
            td = AFEventailRegimeAff.td1;
        }
        cot.append("<tr>");
        cot.append("<td " + td + ">" + _dateRange(e.getDateDebutCotisation(), e.getDateFinCotisation(), userSession)
                + "</td>");
        cot.append("<td " + td + ">" + e.getLibelleAssuranceFr() + "</td>");
        cot.append("<td " + td + ">" + userSession.getCodeLibelle(e.getPeriodiciteCotisation()) + "</td>");

        if (CodeSystem.GENRE_ASS_PERSONNEL.equals(e.getGenreAssurance())) {
            // montant personelle
            if (CodeSystem.PERIODICITE_ANNUELLE.equals(e.getPeriodiciteCotisation())) {
                cot.append("<td " + td + ">" + e.getMontantAnnuelCotisation() + "</td>");
            } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equals(e.getPeriodiciteCotisation())) {
                cot.append("<td " + td + ">" + e.getMontantTrimestrielCotisation() + "</td>");
            } else if (CodeSystem.PERIODICITE_MENSUELLE.equals(e.getPeriodiciteCotisation())) {
                cot.append("<td " + td + ">" + e.getMontantMensuelCotisation() + "</td>");
            }
        } else {
            // montant paritaire
            cot.append("<td " + td + ">" + getMassePeriodicite(e) + "</td>");
        }
        cot.append("<td " + td + ">" + userSession.getLabel("VG_TYPE_ASSURANCE") + " "
                + userSession.getCodeLibelle(e.getGenreAssurance()) + ",</td>");

        String planTitle = userSession.getLabel("VG_LIBELLE_FACTURE") + " : " + e.getLibelleFacturePlanAffiliation()
                + "\n" + userSession.getLabel("VG_DOMAINE_COURRIER") + " : "
                + userSession.getCodeLibelle(e.getDomaineCourrierPlanAffiliation()) + "\n"
                + userSession.getLabel("VG_DOMAINE_RECOUVREMENT") + " : "
                + userSession.getCodeLibelle(e.getDomaineRecouvrementPlanAffiliation()) + "\n"
                + userSession.getLabel("VG_DOMAINE_REMBOURSEMENT") + " : "
                + userSession.getCodeLibelle(e.getDomaineRemboursementPlanAffiliation());
        String planLink = e.getLibellePlanAffiliation();
        if (rightForPlanAffiliation) {
            planLink = "<a title='"
                    + planTitle
                    + "' href='#' onclick=\"callLocalUrl('/naos?userAction=naos.planAffiliation.planAffiliation.afficher&selectedId="
                    + e.getIdPlanAffiliation() + "')\">" + e.getLibellePlanAffiliation() + "</a>";
        }

        cot.append("<td " + td + ">" + userSession.getLabel("VG_PLAN_ASSURANCE") + " " + planLink);
        if (e.getBlocageEnvoiPlanAffiliation().booleanValue()) {
            cot.append("<span style='color:red'> (" + userSession.getLabel("VG_BLOCAGE_ENVOI") + ") </span>");
        }
        if (e.getPlanAffiliationInactif().booleanValue()) {
            cot.append("<span style='color:red'> (" + userSession.getLabel("VG_PLAN_INACTIF") + ") </span>");
        }
        cot.append("</td>");
        cot.append("<td " + td + ">" + userSession.getCodeLibelle(e.getMotifFinCotisation()) + "</td>");

        if (!JadeStringUtil.isIntegerEmpty(e.getTauxAssuranceIdCotisation())) {
            cot.append("<td " + td + ">" + userSession.getLabel("VG_TAUX_FORCE") + "</td>");
        }

        cot.append("</tr>");
        return cot;
    }

    /*
     * permet de faire un toggle sur un element à afficher
     */
    private String _buildOpenCloseLink(String elemId, String text) {
        return "<a " + openLinkStyle + " onclick=\"if (document.getElementById('" + elemId
                + "').style.display!='block') {document.getElementById('" + elemId
                + "').style.display='block'} else {document.getElementById('" + elemId + "').style.display='none'}\">"
                + text + "</a>";
    }

    /*
     * format date range like ... - ...
     */
    private String _dateRange(String dd, String df, BSession session) {
        if (JadeStringUtil.isEmpty(df)) {
            df = "...";
        }
        if (JadeStringUtil.isEmpty(dd)) {
            dd = "...";
        }
        return session.getLabel("VG_DU") + " " + dd + " " + session.getLabel("VG_AU") + " " + df;
    }

    /*
     * Ligne affichée à la fin de chaque affiliation active, contenant les liens, notes etc...
     */
    private void fillEndBar(StringBuffer content, BSession session, String idAffiliation, AFCotiSearchEntity e)
            throws Exception {
        // _____________________________________________________________________________________
        // Divers
        //
        boolean rightForParticularite = session.hasRight("naos.particulariteAffiliation.particulariteAffiliation",
                FWSecureConstants.READ);
        boolean rightForLienAffiliation = session.hasRight("naos.lienAffiliation.lienAffiliation",
                FWSecureConstants.READ);
        boolean rightForGestionEnvoi = session.hasRight("leo.envoi.envoi", FWSecureConstants.READ);
        boolean rightForCompteAnnexe = session.hasRight("osiris.comptes.apercuComptes", FWSecureConstants.READ);
        boolean rightForDossierCap = session.hasRight("auriga.decisioncap", FWSecureConstants.READ);
        boolean rightForDossierCgas = session.hasRight("aries.decisioncgas", FWSecureConstants.READ);

        // LIENS
        AFLienAffiliationManager lienMgr = new AFLienAffiliationManager();
        lienMgr.setSession(session);
        lienMgr.setForAffiliationId(idAffiliation);
        lienMgr.find();
        String lienTitle = "";
        for (int i = 0; i < lienMgr.size(); i++) {
            AFLienAffiliation lien = (AFLienAffiliation) lienMgr.getEntity(i);
            String dfp = lien.getDateFin();
            String ddp = lien.getDateDebut();
            if (JadeStringUtil.isEmpty(dfp)) {
                dfp = "...";
            }
            if (JadeStringUtil.isEmpty(ddp)) {
                ddp = "...";
            }

            lienTitle += ddp + " - " + dfp + ", " + session.getCodeLibelle(lien.getTypeLien()) + "\n";
        }

        // PARTICULARITES
        AFParticulariteAffiliationManager parMgr = new AFParticulariteAffiliationManager();
        parMgr.setSession(session);
        parMgr.setForAffiliationId(idAffiliation);
        parMgr.find();
        String parTitle = "";
        for (int i = 0; i < parMgr.size(); i++) {
            AFParticulariteAffiliation par = (AFParticulariteAffiliation) parMgr.getEntity(i);
            String dfp = par.getDateFin();
            String ddp = par.getDateDebut();
            if (JadeStringUtil.isEmpty(dfp)) {
                dfp = "...";
            }
            if (JadeStringUtil.isEmpty(ddp)) {
                ddp = "...";
            }

            parTitle += ddp + " - " + dfp + ", " + session.getCodeLibelle(par.getParticularite()) + "\n";
        }

        // NOTES
        List infos = new ArrayList();
        FWNotePManager mgrNotes = new FWNotePManager();
        mgrNotes.setWithMemo(true);
        mgrNotes.setSession(session);
        mgrNotes.setForTableSource(noteKey);
        mgrNotes.setForSourceId(idAffiliation);
        mgrNotes.find();
        int nbNotes = mgrNotes.size();

        /*
         * Affichage de la bar
         */
        content.append("<table><tr>");

        if (rightForParticularite) {
            content.append("<td><li><a href='#' title=\""
                    + parTitle
                    + "\" onclick=\"callLocalUrl('/naos?userAction=naos.particulariteAffiliation.particulariteAffiliation.chercher&affiliationId="
                    + idAffiliation + "')\">" + parMgr.size() + " " + session.getLabel("VG_PARTICULARITES")
                    + "</a></td>");
        } else {
            content.append("<td><li>" + parMgr.size() + " " + session.getLabel("VG_PARTICULARITES") + "</td>");
        }
        content.append("<td>" + AFEventailRegimeAff.SEP + "</td>");

        if (rightForLienAffiliation) {
            content.append("<td><a href='#' title=\""
                    + lienTitle
                    + "\" onclick=\"callLocalUrl('/naos?userAction=naos.lienAffiliation.lienAffiliation.chercher&affiliationId="
                    + idAffiliation + "')\">" + lienMgr.size() + " " + session.getLabel("VG_LIENS_AFF") + "</a></td>");
        } else {
            content.append("<td>" + lienMgr.size() + " " + session.getLabel("VG_LIENS_AFF") + "</td>");
        }
        content.append("<td>" + AFEventailRegimeAff.SEP + "</td>");

        if (rightForGestionEnvoi) {
            String suiviLink = "<a href='#' onclick=\"callLocalUrl('/naos?userAction=naos.affiliation.affiliation.gestionEnvois&affiliationId="
                    + idAffiliation + "')\">" + session.getLabel("VG_GESTION_ENVOIS") + "</a>";
            content.append("<td>" + suiviLink + "</td>");
            content.append("<td>" + AFEventailRegimeAff.SEP + "</td>");
        }

        if (rightForCompteAnnexe) {
            String comptaLink = "<a href='#' onclick=\"callLocalUrl('/osiris?userAction=osiris.comptes.apercuComptes.chercher&likeNumNomPrint="
                    + e.getNumeroAffilie() + "')\">" + session.getLabel("VG_COMPTES_ANNEXES") + "</a>";
            content.append("<td>" + comptaLink + "</td>");
            content.append("<td>" + AFEventailRegimeAff.SEP + "</td>");
        }

        if (AFAffiliationType.isTypeCAP(e.getTypeAffiliation()) && rightForDossierCap) {
            String suiviLink = "<a href='#' onclick=\"callLocalUrl('/auriga?userAction=auriga.decisioncap.decisionCapSearch.afficherCapSearch&idAffilie="
                    + idAffiliation + "')\">" + session.getLabel("VG_CAP_LINK") + "</a>";
            content.append("<td>" + suiviLink + "</td>");
            content.append("<td>" + AFEventailRegimeAff.SEP + "</td>");
        }

        if (AFAffiliationType.isTypeCGAS(e.getTypeAffiliation()) && rightForDossierCgas) {
            String suiviLink = "<a href='#' onclick=\"callLocalUrl('/aries?userAction=aries.decisioncgas.decisionCgasSearch.afficherCgasSearch&idAffilie="
                    + idAffiliation + "')\">" + session.getLabel("VG_CGAS_LINK") + "</a>";
            content.append("<td>" + suiviLink + "</td>");
            content.append("<td>" + AFEventailRegimeAff.SEP + "</td>");
        }

        if (nbNotes > 0) {
            content.append("<td><a " + openLinkStyle + " onclick=\"if (document.getElementById('idNotes" + noteKey
                    + idAffiliation + "').style.display!='block') {document.getElementById('idNotes" + noteKey
                    + idAffiliation + "').style.display='block'} else {document.getElementById('idNotes" + noteKey
                    + idAffiliation + "').style.display='none'}\"> <img src='images/icon_postit_ok.gif'> [" + nbNotes
                    + " " + session.getLabel("VG_NOTES") + "]</a></td>");
        } else {
            content.append("<td>" + nbNotes + " " + session.getLabel("VG_NOTES") + "</td>");
        }

        content.append("</tr></table>");

        /*
         * Affichage des notes
         */
        if (mgrNotes.size() > 0) {
            String div = "";
            for (Iterator it = mgrNotes.iterator(); it.hasNext();) {
                FWNoteP note = (FWNoteP) it.next();
                String noteTitle = note.getDescription();
                String noteMessage = note.getMemo();
                String noteDate = note.getLastModifiedDate();
                String noteUser = note.getLastModifiedUser();
                noteMessage = JadeStringUtil.change(noteMessage, "\n", "<br>");
                div += "<div style=\"" + noteStyle + "\">" + "<div  style='border-bottom : solid 1px black'><i>"
                        + noteDate + " - " + noteUser + "</i></div>" + "<b>" + noteTitle + "</b> <br>" + noteMessage
                        + "</div>";
            }
            content.append("<div id='idNotes" + noteKey + idAffiliation + "' style='display:none'><br>" + div
                    + "</div>");
        }

    }

    @Override
    public String getElement() {
        return element;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public TISummaryInfo[] getInfoForTiers(String idTiers, BSession userSession) throws Exception {

        setTitle(userSession.getLabel("VG_AFF_TITLE"));
        setUrlTitle("");

        TISummaryInfo info = new TISummaryInfo();

        boolean rightForAffiliation = userSession.hasRight("naos.affiliation.affiliation", FWSecureConstants.READ);
        boolean rightForCotisation = userSession.hasRight("naos.cotisation.cotisation", FWSecureConstants.READ);
        boolean rightForPlanAffiliation = userSession.hasRight("naos.planAffiliation.planAffiliation",
                FWSecureConstants.READ);
        boolean rightForReleves = userSession.hasRight("naos.releve.apercuReleve", FWSecureConstants.READ);
        boolean rightForNbrAssures = userSession.hasRight("naos.nombreAssures.nombreAssures", FWSecureConstants.READ);
        /*
         * Effectue les deux recherches principales qui permettrent de connaitre toutes les adhesions et les cotisations
         */
        AFCotiSearchManager mgrAdh = new AFCotiSearchManager();
        mgrAdh.setSession(userSession);
        mgrAdh.setForCotiActiveTodayJoin(true);
        mgrAdh.setPath(AFCotiSearchManager.PATH_ADHESION);
        mgrAdh.setForIdTiers(idTiers);
        mgrAdh.changeManagerSize(0);
        mgrAdh.setOrder(AFCotiSearchManager.TRI_PAR_AFFIE_ADHESION);
        mgrAdh.find();
        // AFCotiSearchManager mgrCoti = new AFCotiSearchManager();
        // mgrCoti.setSession(userSession);
        // mgrCoti.setForCotiActiveTodayJoin(true);
        // mgrCoti.setPath(AFCotiSearchManager.PATH_PLAN_AFFILIATION);
        // mgrCoti.setForIdTiers(idTiers);
        // mgrCoti.changeManagerSize(0);
        // mgrCoti.setOrder(AFCotiSearchManager.TRI_PAR_AFFILIE_GENRE_ASSURANCE);
        // mgrCoti.setForCotisationSansAdhesion(new Boolean(true));
        // mgrCoti.find();

        //

        String prevIdAffiliation = "";
        String prevIdAdhesion = "";
        boolean affActiveShown = false;
        boolean affInActiveShown = false;
        int nbAffInactif = 0;
        int nbCotisations = 0;

        StringBuffer content = null;
        StringBuffer contentAffActifs = new StringBuffer(); // buffer
        StringBuffer contentAffInactifs = new StringBuffer(); // buffer
        StringBuffer cotisationsAdhesion = new StringBuffer("<table>");// buffer

        // trouve l'id de l'assurance AVS
        AFAssuranceManager ass = new AFAssuranceManager();
        ass.setForTypeAssurance(CodeSystem.TYPE_ASS_COTISATION_AVS_AI);
        ass.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
        ass.setSession(userSession);
        ass.find();
        String idAssuranceAvs = null;
        if (ass.size() == 1) {
            idAssuranceAvs = ((AFAssurance) ass.getFirstEntity()).getAssuranceId();
        }

        for (int i = 0; i < mgrAdh.size(); i++) {
            AFCotiSearchEntity e = (AFCotiSearchEntity) mgrAdh.getEntity(i);

            /*
             * Pour chaque affiliation
             */
            if (!prevIdAffiliation.equals(e.getIdAffiliation())) {

                /*
                 * QQ flags pour pouvoir regrouper les affiliations actives et celles qui sont radiées
                 */
                if ((affActiveShown == false) && (JadeStringUtil.isIntegerEmpty(e.getDateFinAffiliation()))) {
                    content = contentAffActifs;
                    affActiveShown = true;
                }
                if ((affInActiveShown == false) && (!JadeStringUtil.isIntegerEmpty(e.getDateFinAffiliation()))) {
                    content = contentAffInactifs;
                    affInActiveShown = true;
                }
                if (affInActiveShown == true) {
                    nbAffInactif++; // compte les affiliation inactives
                }
                prevIdAffiliation = e.getIdAffiliation();

                /*
                 * Infos concernant l'affiliation elle-même
                 */
                String releveLink = "<b>" + userSession.getLabel("VG_RELEVES") + "</b>";
                if (rightForReleves) {
                    releveLink = "<a href='#' onclick=\"callLocalUrl('/naos?userAction=naos.releve.apercuReleve.chercher&affiliationId="
                            + e.getIdAffiliation() + "')\">" + userSession.getLabel("VG_RELEVES") + "</a>";
                }
                String affLink = "<b>" + e.getNumeroAffilie() + "</b>";
                if (rightForAffiliation) {
                    affLink = "<a href='#' onclick=\"callLocalUrl('/naos?userAction=naos.affiliation.affiliation.afficher&selectedId="
                            + e.getIdAffiliation()
                            + "&idTiers="
                            + idTiers
                            + "')\"><b>"
                            + e.getNumeroAffilie()
                            + "</b></a>";
                }

                if (i > 0) {
                    content.append("<br>");
                }
                content.append(affLink + AFEventailRegimeAff.SEP);
                if (!JadeStringUtil.isEmpty(e.getRaisonSociale())) {
                    content.append("<span title=\"" + e.getDesignation1() + " " + e.getDesignation2() + " "
                            + e.getDesignation3() + " " + e.getDesignation4() + "\">" + e.getRaisonSociale()
                            + "</span>");
                } else {
                    content.append(e.getDesignation1() + " " + e.getDesignation2() + e.getDesignation3() + " "
                            + e.getDesignation4());
                }

                content.append(AFEventailRegimeAff.SEP + userSession.getCodeLibelle(e.getBrancheEconomique()));

                if (!JadeStringUtil.isIntegerEmpty(e.getMotifFinAffiliation())) {
                    content.append(AFEventailRegimeAff.SEP + userSession.getLabel("VG_MOTIF_FIN_AFFILIATION")
                            + " : <b style='color:red'>" + userSession.getCodeLibelle(e.getMotifFinAffiliation())
                            + "</b>");
                }

                content.append("<br>");

                content.append(userSession.getCodeLibelle(e.getTypeAffiliation()));
                content.append(" " + _dateRange(e.getDateDebutAffiliation(), e.getDateFinAffiliation(), userSession));
                content.append(AFEventailRegimeAff.SEP);
                content.append(userSession.getCodeLibelle(e.getPeriodiciteAffiliation()));

                content.append((e.getReleve().booleanValue() == true) ? AFEventailRegimeAff.SEP + " "
                        + userSession.getLabel("VG_FACTURATION_PAR") + " " + releveLink + "" : "");
                content.append((e.getImpressionReleve().booleanValue() == true) ? " ("
                        + userSession.getLabel("VG_ENVOI_AUTO_RELEVES_BLANC") + ")" : "");

                // nbr d'assurés (pour la dernière année connue):
                int resultAnnee = 0;
                String resultNbAssure = null;
                if (!JadeStringUtil.isIntegerEmpty(idAssuranceAvs)) {
                    AFNombreAssuresManager mgr = new AFNombreAssuresManager();
                    mgr.setForAffiliationId(e.getIdAffiliation());
                    mgr.setSession(userSession);
                    mgr.setForAssuranceId(idAssuranceAvs);
                    mgr.find();
                    for (int nbAssInc = 0; nbAssInc < mgr.size(); nbAssInc++) {
                        AFNombreAssures nbAss = (AFNombreAssures) mgr.getEntity(nbAssInc);
                        int annee = Integer.parseInt(nbAss.getAnnee());
                        if (annee > resultAnnee) {
                            resultAnnee = annee;
                            resultNbAssure = nbAss.getNbrAssures();
                        }
                    }
                }
                if (resultNbAssure != null) {
                    String nbrAssuresLink = userSession.getLabel("VG_NB_ASSURES");
                    if (rightForNbrAssures) {
                        nbrAssuresLink = "<a href='#' onclick=\"callLocalUrl('/naos?userAction=naos.nombreAssures.nombreAssures.chercher&affiliationId="
                                + e.getIdAffiliation() + "')\">" + userSession.getLabel("VG_NB_ASSURES") + "</a>";
                    }
                    content.append(AFEventailRegimeAff.SEP + nbrAssuresLink + " (" + resultAnnee + ") : "
                            + resultNbAssure);
                }

                content.append("<br>");
            }

            /*
             * Adhésions
             */
            if (!prevIdAdhesion.equals(e.getIdAdhesion())) {
                prevIdAdhesion = e.getIdAdhesion();
                if (JadeStringUtil.isIntegerEmpty(e.getIdAdhesion())) {
                    content.append("<b>" + userSession.getLabel("VG_AUCUNE_ADHESION") + "</b>");
                } else {
                    content.append("<br><b");
                    if (!JadeStringUtil.isIntegerEmpty(e.getDateFinAdhesion())) {
                        content.append(" style='color :  gray'");
                    }
                    content.append("><span title=\"" + userSession.getLabel("VG_CAISSE") + e.getCodeCaisseMetier()
                            + " - " + e.getDesignation1CaisseMetier() + " " + e.getDesignation2CaisseMetier() + " "
                            + e.getDesignation3CaisseMetier() + " " + e.getDesignation4CaisseMetier() + "\">"
                            + userSession.getCodeLibelle(e.getTypeAdhesion()) + " " + e.getCodeCaisseMetier()
                            + "</span>, " + _dateRange(e.getDateDebutAdhesion(), e.getDateFinAdhesion(), userSession));

                    content.append("</b>");
                }
            }

            /*
             * Cotisations liées à l'adhésion
             */
            StringBuffer buf = _buildCotisation(e, userSession, nbCotisations, rightForPlanAffiliation);
            if (buf != null) {
                cotisationsAdhesion.append(buf.toString());
                nbCotisations++;
            }
            /*
             * Fin des cotisations pour cette adhésion, on affiches toutes les cotisations
             */
            if ((i == (mgrAdh.size() - 1))
                    || (!e.getIdAdhesion().equals(((AFCotiSearchEntity) mgrAdh.getEntity(i + 1)).getIdAdhesion()))) {
                cotisationsAdhesion.append("</table>");
                if (nbCotisations == 0) {
                    if (!JadeStringUtil.isIntegerEmpty(e.getIdAdhesion())) {
                        content.append(" - " + userSession.getLabel("VG_AUCUNE_COTI_ACTIVE"));
                    }
                } else {
                    String cotisationLink = userSession.getLabel("VG_COTISATIONS");
                    if (rightForCotisation) {
                        cotisationLink = "<a href='#' onclick=\"callLocalUrl('/naos?userAction=naos.cotisation.cotisation.chercher&affiliationId="
                                + e.getIdAffiliation() + "')\">" + userSession.getLabel("VG_COTISATIONS") + "</a>";
                    }

                    content.append(" - "
                            + nbCotisations
                            + " "
                            + cotisationLink
                            + ", "
                            + _buildOpenCloseLink("evrAdh" + e.getIdAdhesion(),
                                    userSession.getLabel("VG_AFFICHER_CACHER")));
                    content.append("<div id='evrAdh" + e.getIdAdhesion() + "' style='display:"
                            + (isCotisationsVisible() ? "block" : "none") + "'>" + cotisationsAdhesion + "</div>");

                }
                cotisationsAdhesion = new StringBuffer("<table>");
                nbCotisations = 0;

            }

            /*
             * Info à affichier a la fin de chaque affiliation actives :
             */
            if ((i == (mgrAdh.size() - 1))
                    || (!e.getIdAffiliation().equals(((AFCotiSearchEntity) mgrAdh.getEntity(i + 1)).getIdAffiliation()))) {

                /*
                 * Cotisations sans adhésion
                 */
                // if (mgrCoti.size() > 0) {
                // int nbCotisationSansAdhesion = 0;
                // StringBuffer cotiSansAdhesion = new StringBuffer();
                // for (int j = 0; j<mgrCoti.size();j++) {
                // AFCotiSearchEntity e2 = (AFCotiSearchEntity)
                // mgrCoti.getEntity(j);
                // /*
                // * concerne l'affilié en cours d'affichage ?
                // */
                // if (e.getIdAffiliation().equals(e2.getIdAffiliation())) {
                // buf =
                // _buildCotisation(e2,userSession,nbCotisationSansAdhesion,rightForPlanAffiliation);
                // if (buf != null) {
                // cotiSansAdhesion.append(buf.toString());
                // nbCotisationSansAdhesion ++;
                // }
                // }
                // }
                // if (cotiSansAdhesion.length() > 0) {
                // content.append("<br><br><b>Cotisations sans adhésion :</b><br><table>");
                // content.append(cotiSansAdhesion);
                // content.append("</table>");
                // nbCotisationSansAdhesion = 0;
                // }
                // }

                fillEndBar(content, userSession, e.getIdAffiliation(), e);
                if (i < (mgrAdh.size() - 1)) {
                    content.append("<hr>");
                }
            }
        }

        /*
         * Composition final du contenu du module
         */
        StringBuffer textFinal = new StringBuffer(contentAffActifs.toString());
        if (!JadeStringUtil.isEmpty(contentAffInactifs.toString())) {
            textFinal.append(_buildOpenCloseLink("evrAffRadiee",
                    "[" + userSession.getLabel("VG_AFFICHER_AFFILIATIONS_RADIEES") + " (" + nbAffInactif + ")]"));
            textFinal
                    .append("<div id='evrAffRadiee' style='display:none;border : solid 2px gray;padding :0.2cm 0.2cm 0.2cm 0.2cm'>");
            textFinal.append(contentAffInactifs.toString() + "</div>");
        }

        info.setText(textFinal.toString());

        if (rightForAffiliation) {
            setUrlTitle("naos?userAction=naos.affiliation.affiliation.chercher&idTiers=" + idTiers);
        }
        return new TISummaryInfo[] { info };
    }

    private String getMassePeriodicite(AFCotiSearchEntity e) {

        if (!JadeStringUtil.isEmpty(e.getMasseAnnuelleCotisation())) {
            double massePeriodicite = Double.parseDouble(e.getMasseAnnuelleCotisation());

            if (CodeSystem.PERIODICITE_ANNUELLE.equals(e.getPeriodiciteCotisation())
                    || CodeSystem.PERIODICITE_ANNUELLE_31_MARS.equals(e.getPeriodiciteCotisation())
                    || CodeSystem.PERIODICITE_ANNUELLE_30_JUIN.equals(e.getPeriodiciteCotisation())
                    || CodeSystem.PERIODICITE_ANNUELLE_30_SEPT.equals(e.getPeriodiciteCotisation())) {
                // Déjà le montant annuel
            } else if (CodeSystem.PERIODICITE_TRIMESTRIELLE.equals(e.getPeriodiciteCotisation())) {
                massePeriodicite = massePeriodicite / 4;
            } else if (CodeSystem.PERIODICITE_MENSUELLE.equals(e.getPeriodiciteCotisation())) {
                massePeriodicite = massePeriodicite / 12;
            }
            return JANumberFormatter.round(Double.toString(massePeriodicite), 0.05, 2, JANumberFormatter.NEAR);
        } else {

        }
        return "";
    }

    @Override
    public int getMaxHorizontalItems() {
        return maxHorizontalItems;
    }

    @Override
    public PY_VG_MODULE_SIZE getModuleSize() {
        return moduleSize;
    }

    @Override
    public String getStyle() {
        return style;
    }

    /*
     * Getter et Setter
     */
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getUrlTitle() {
        return urlTitle;
    }

    public boolean isCotisationsVisible() {
        return cotisationsVisible;
    }

    public void setCotisationsVisible(boolean cotisationVisible) {
        cotisationsVisible = cotisationVisible;
    }

    @Override
    public void setElement(String element) {
        this.element = element;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setMaxHorizontalItems(int maxHorizontalItems) {
        this.maxHorizontalItems = maxHorizontalItems;
    }

    @Override
    public void setModuleSize(PY_VG_MODULE_SIZE moduleSize) {
        this.moduleSize = moduleSize;
    }

    @Override
    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }
}
