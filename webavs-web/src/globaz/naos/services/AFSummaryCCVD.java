package globaz.naos.services;

import globaz.framework.db.postit.FWNoteP;
import globaz.framework.db.postit.FWNotePManager;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.cotisation.AFCotiSearchEntity;
import globaz.naos.db.cotisation.AFCotiSearchManager;
import globaz.naos.db.lienAffiliation.AFLienAffiliation;
import globaz.naos.db.lienAffiliation.AFLienAffiliationManager;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliation;
import globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliationManager;
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
public class AFSummaryCCVD implements ITISummarizable {

    private final static String SEP = "&nbsp;<span style='color:silver'>|</span>&nbsp;";
    private final static String td1 = "style='background:#d8dee4'";
    private final static String td2 = "style='background:white' ";
    private boolean cotisationsVisible = true;

    private String element = "";

    private String icon = "";
    private int maxHorizontalItems = 0;

    private boolean maxReached = false;
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
        try {
            BSession bSession = new BSession(AFApplication.DEFAULT_APPLICATION_NAOS);
        } catch (Exception ex) {
            // On ne fait rien, le module n'existe pas
        }
        TISummaryInfo info = new TISummaryInfo();

        boolean rightForAffiliation = userSession.hasRight("naos.affiliation.affiliation", FWSecureConstants.READ);
        boolean rightForReleves = userSession.hasRight("naos.releve.apercuReleve", FWSecureConstants.READ);
        maxReached = false;

        setTitle(userSession.getLabel("VG_AFF_TITLE"));
        setUrlTitle("");

        // if (rightForAffiliation) {
        /*
         * Effectue les deux recherches principales qui permettrent de connaitre toutes les adhesions et les cotisations
         */
        AFCotiSearchManager mgrAdh = new AFCotiSearchManager();
        mgrAdh.setSession(userSession);
        mgrAdh.setForCotiActiveTodayJoin(true);
        mgrAdh.setPath(AFCotiSearchManager.PATH_PLAN_AFFILIATION);
        mgrAdh.setForIdTiers(idTiers);

        // int year_today = globaz.globall.util.JACalendar.today().getYear();
        // mgrAdh.setForAnneeActive(String.valueOf(year_today));

        mgrAdh.changeManagerSize(0);
        mgrAdh.setOrder(AFCotiSearchManager.TRI_PAR_AFFIE_ADHESION);
        mgrAdh.find();

        if (rightForAffiliation) {
            setUrlTitle("/naos?userAction=naos.affiliation.affiliation.chercher&idTiers=" + idTiers);
        }

        String prevIdAffiliation = "";
        StringBuffer content = new StringBuffer();
        int nbAffiliationsAffichees = 0;
        int nbAffiliationsTotal = 0;

        for (int i = 0; i < mgrAdh.size(); i++) {
            AFCotiSearchEntity e = (AFCotiSearchEntity) mgrAdh.getEntity(i);

            /*
             * Pour chaque affiliation
             */
            if (!prevIdAffiliation.equals(e.getIdAffiliation())) {
                nbAffiliationsTotal++;
                if (JadeStringUtil.isBlankOrZero(e.getDateFinAffiliation())) {

                    prevIdAffiliation = e.getIdAffiliation();
                    nbAffiliationsAffichees++;
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

                    content.append(affLink + AFSummaryCCVD.SEP);
                    if (!JadeStringUtil.isEmpty(e.getRaisonSociale())) {
                        content.append("<span title=\"" + JadeStringUtil.escapeXML(e.getDesignation1()) + " "
                                + JadeStringUtil.escapeXML(e.getDesignation2()) + " "
                                + JadeStringUtil.escapeXML(e.getDesignation3()) + " "
                                + JadeStringUtil.escapeXML(e.getDesignation4()) + "\">"
                                + JadeStringUtil.escapeXML(e.getRaisonSociale()) + "</span>");
                    } else {
                        content.append(e.getDesignation1() + " " + e.getDesignation2() + e.getDesignation3() + " "
                                + e.getDesignation4());
                    }

                    content.append("<span style='padding-left:10px'>");
                    content.append(userSession.getCodeLibelle(e.getTypeAffiliation()));
                    content.append(" "
                            + _dateRange(e.getDateDebutAffiliation(), e.getDateFinAffiliation(), userSession));
                    content.append("</span>");
                    content.append("</br>");
                    String linkBar = getLinkBar(e.getIdAffiliation(), e, userSession);
                    content.append(linkBar);
                    content.append("</br>");
                }
            }
        }

        if (nbAffiliationsAffichees != nbAffiliationsTotal) {
            maxReached = true;
        }
        info.setText(content.toString());
        return new TISummaryInfo[] { info };
        // } else {
        // return null;
        // }
    }

    private String getLinkBar(String idAffiliation, AFCotiSearchEntity e, BSession session) throws Exception {
        StringBuffer content = new StringBuffer();
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
            content.append("<td><a href='#' title=\""
                    + parTitle
                    + "\" onclick=\"callLocalUrl('/naos?userAction=naos.particulariteAffiliation.particulariteAffiliation.chercher&affiliationId="
                    + idAffiliation + "')\">" + parMgr.size() + " " + session.getLabel("VG_PARTICULARITES")
                    + "</a></td>");
        } else {
            content.append("<td>" + parMgr.size() + " " + session.getLabel("VG_PARTICULARITES") + "</td>");
        }
        content.append("<td>" + AFSummaryCCVD.SEP + "</td>");

        if (rightForLienAffiliation) {
            content.append("<td><a href='#' title=\""
                    + lienTitle
                    + "\" onclick=\"callLocalUrl('/naos?userAction=naos.lienAffiliation.lienAffiliation.chercher&affiliationId="
                    + idAffiliation + "')\">" + lienMgr.size() + " " + session.getLabel("VG_LIENS_AFF") + "</a></td>");
        } else {
            content.append("<td>" + lienMgr.size() + " " + session.getLabel("VG_LIENS_AFF") + "</td>");
        }
        content.append("<td>" + AFSummaryCCVD.SEP + "</td>");

        if (rightForGestionEnvoi) {
            String suiviLink = "<a href='#' onclick=\"callLocalUrl('/naos?userAction=naos.affiliation.affiliation.gestionEnvois&affiliationId="
                    + idAffiliation + "')\">" + session.getLabel("VG_GESTION_ENVOIS") + "</a>";
            content.append("<td>" + suiviLink + "</td>");
            content.append("<td>" + AFSummaryCCVD.SEP + "</td>");
        }

        if (rightForCompteAnnexe) {
            String comptaLink = "<a href='#' onclick=\"callLocalUrl('/osiris?userAction=osiris.comptes.apercuComptes.chercher&likeNumNomPrint="
                    + e.getNumeroAffilie() + "')\">" + session.getLabel("VG_COMPTES_ANNEXES") + "</a>";
            content.append("<td>" + comptaLink + "</td>");
            content.append("<td>" + AFSummaryCCVD.SEP + "</td>");
        }

        if (AFAffiliationType.isTypeCAP(e.getTypeAffiliation()) && rightForDossierCap) {
            String suiviLink = "<a href='#' onclick=\"callLocalUrl('/auriga?userAction=auriga.decisioncap.decisionCapSearch.afficherCapSearch&idAffilie="
                    + idAffiliation + "')\">" + session.getLabel("VG_CAP_LINK") + "</a>";
            content.append("<td>" + suiviLink + "</td>");
            content.append("<td>" + AFSummaryCCVD.SEP + "</td>");
        }

        if (AFAffiliationType.isTypeCGAS(e.getTypeAffiliation()) && rightForDossierCgas) {
            String suiviLink = "<a href='#' onclick=\"callLocalUrl('/aries?userAction=aries.decisioncgas.decisionCgasSearch.afficherCgasSearch&idAffilie="
                    + idAffiliation + "')\">" + session.getLabel("VG_CGAS_LINK") + "</a>";
            content.append("<td>" + suiviLink + "</td>");
            content.append("<td>" + AFSummaryCCVD.SEP + "</td>");
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

        return content.toString();

    }

    @Override
    public int getMaxHorizontalItems() {
        // return this.maxHorizontalItems;
        if (maxReached) {
            return 1;
        } else {
            return 0;
        }
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
