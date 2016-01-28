package globaz.naos.services;

import globaz.framework.db.postit.FWNoteP;
import globaz.framework.db.postit.FWNotePManager;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.application.AFApplication;
import globaz.naos.db.cotisation.AFCotiSearchEntity;
import globaz.naos.db.cotisation.AFCotiSearchManager;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author oca
 * 
 */
public class AFSummary implements ITISummarizable {

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

    private FWNotePManager countNotes(BSession session, String idAffiliation) throws Exception {
        // NOTES
        List infos = new ArrayList();
        FWNotePManager mgrNotes = new FWNotePManager();
        mgrNotes.setWithMemo(true);
        mgrNotes.setSession(session);
        mgrNotes.setForTableSource(noteKey);
        mgrNotes.setForSourceId(idAffiliation);
        mgrNotes.find();

        return mgrNotes;
    }

    /*
     * Ligne affichée à la fin de chaque affiliation active, contenant les liens, notes etc...
     */
    private void fillEndBar(StringBuffer content, BSession session, String idAffiliation, AFCotiSearchEntity e)
            throws Exception {
        // FWNotePManager mgrNotes = this.getNotes(content, session, idAffiliation);
        FWNotePManager mgrNotes = countNotes(session, idAffiliation);

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

        /*
         * Effectue les deux recherches principales qui permettrent de connaitre toutes les adhesions et les cotisations
         */
        AFCotiSearchManager mgrAdh = new AFCotiSearchManager();
        mgrAdh.setSession(userSession);
        mgrAdh.setForCotiActiveTodayJoin(true);
        mgrAdh.setPath(AFCotiSearchManager.PATH_PLAN_AFFILIATION);
        mgrAdh.setForIdTiers(idTiers);

        mgrAdh.changeManagerSize(0);
        mgrAdh.setOrder(AFCotiSearchManager.TRI_PAR_AFFIE_ADHESION);
        mgrAdh.find();

        if (rightForAffiliation) {
            setUrlTitle("naos?userAction=naos.affiliation.affiliation.chercher&idTiers=" + idTiers);
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

                    content.append(affLink + AFSummary.SEP);
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

                    StringBuffer link_notes = getNotes(userSession, e.getIdAffiliation());
                    if (link_notes.length() > 0) {
                        content.append(AFSummary.SEP + link_notes);
                    }

                    content.append("</br>");
                    content.append("<span style='padding-left:10px'>");
                    content.append(userSession.getCodeLibelle(e.getTypeAffiliation()));
                    content.append(" "
                            + _dateRange(e.getDateDebutAffiliation(), e.getDateFinAffiliation(), userSession));
                    content.append("</span>");
                    content.append("</br>");
                }
                fillEndBar(content, userSession, e.getIdAffiliation(), e);
            }
        }

        if (nbAffiliationsAffichees != nbAffiliationsTotal) {
            maxReached = true;
        }
        info.setText(content.toString());
        return new TISummaryInfo[] { info };
    }

    @Override
    public int getMaxHorizontalItems() {
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

    private StringBuffer getNotes(BSession session, String idAffiliation) throws Exception {
        // NOTES
        StringBuffer content = new StringBuffer();
        List infos = new ArrayList();
        FWNotePManager mgrNotes = new FWNotePManager();
        mgrNotes.setWithMemo(true);
        mgrNotes.setSession(session);
        mgrNotes.setForTableSource(noteKey);
        mgrNotes.setForSourceId(idAffiliation);
        mgrNotes.find();
        int nbNotes = mgrNotes.size();

        if (nbNotes > 0) {
            content.append("<a " + openLinkStyle + " onclick=\"if (document.getElementById('idNotes" + noteKey
                    + idAffiliation + "').style.display!='block') {document.getElementById('idNotes" + noteKey
                    + idAffiliation + "').style.display='block'} else {document.getElementById('idNotes" + noteKey
                    + idAffiliation + "').style.display='none'}\"> <img src='images/icon_postit_ok.gif'> [" + nbNotes
                    + " " + session.getLabel("VG_NOTES") + "]</a>");
            /*
             * Affichage de la bar
             */
            // content.append("<table><tr>");
            // content.append("<td><a " + this.openLinkStyle + " onclick=\"if (document.getElementById('idNotes"
            // + this.noteKey + idAffiliation + "').style.display!='block') {document.getElementById('idNotes"
            // + this.noteKey + idAffiliation + "').style.display='block'} else {document.getElementById('idNotes"
            // + this.noteKey + idAffiliation
            // + "').style.display='none'}\"> <img src='images/icon_postit_ok.gif'> [" + nbNotes + " "
            // + session.getLabel("VG_NOTES") + "]</a></td>");
            // content.append("</tr></table>");
        } else {
            // content.append("<td>" + nbNotes + " " + session.getLabel("VG_NOTES") + "</td>");
        }
        return content;
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
