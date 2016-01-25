package globaz.cygnus.summary;

import globaz.cygnus.application.RFApplication;
import globaz.cygnus.db.decisions.RFDecisionJointTiersManagerSummary;
import globaz.cygnus.db.decisions.RFDecisionJointTiersSummary;
import globaz.cygnus.db.paiement.RFPrestationAccordeeJointREPrestationAccordee;
import globaz.cygnus.db.paiement.RFPrestationAccordeeJointREPrestationAccordeeManager;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RFSummary implements ITISummarizable {
    private BSession aUserSession;
    private String element = "";
    private String icon = "";
    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.SMALL;
    private String title = "";
    private String urlTitle = "";

    public void addInfosInTitle(RFDecisionJointTiersSummary decisionJointTiersSummary) {
        StringBuilder param = new StringBuilder();
        param.append(".chercher&disabledSearchByGestionaire=true&forIdDossier=");
        param.append(decisionJointTiersSummary.getIdDossier());
        title = createHref("cygnus.qds.qdJointDossierJointTiersJointDemande", param, "VG_RFM_TITLE").toString();
    }

    public StringBuilder createHref(String action, StringBuilder param, String content) {
        StringBuilder str = new StringBuilder();
        boolean hasRight = hasReadRight(action);

        if (hasRight) {
            str.append("<a href='#' onclick=\"callLocalUrl('/cygnus?userAction=");
            str.append(action);
            str.append(param);
            str.append("')\" >");
            str.append(aUserSession.getLabel(content));
            str.append("</a>");
        } else {
            str.append("<span>");
            str.append(aUserSession.getLabel(content));
            str.append("</span>");
        }

        return str;
    }

    private RFDecisionJointTiersSummary findDecisionSummary(String idTiers) throws Exception {
        RFDecisionJointTiersManagerSummary managerSummary = new RFDecisionJointTiersManagerSummary();
        managerSummary.setSession(aUserSession);
        managerSummary.setForIdTiers(idTiers);
        managerSummary.find();
        Iterator<RFDecisionJointTiersSummary> it = managerSummary.iterator();

        while (it.hasNext()) {
            RFDecisionJointTiersSummary decisionJointTiersSummary = it.next();
            return decisionJointTiersSummary;
        }
        return null;
    }

    public List<RFPrestationAccordeeJointREPrestationAccordee> findPresation(String idTiers) throws Exception {
        RFPrestationAccordeeJointREPrestationAccordeeManager manager = new RFPrestationAccordeeJointREPrestationAccordeeManager();
        manager.setForEnCoursAtMois(JACalendar.todayJJsMMsAAAA().substring(3));
        manager.setForIdTiersBeneficiaire(idTiers);
        manager.setSession(aUserSession);
        manager.find();

        Iterator<RFPrestationAccordeeJointREPrestationAccordee> it = manager.iterator();
        List<RFPrestationAccordeeJointREPrestationAccordee> list = new ArrayList<RFPrestationAccordeeJointREPrestationAccordee>();
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
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
    public TISummaryInfo[] getInfoForTiers(String idTiers, BSession aUserSession) throws Exception {
        TISummaryInfo info = new TISummaryInfo();
        // Nouvelle session afin de charger les labels de l'application
        try {
            BSession bsession = new BSession(RFApplication.DEFAULT_APPLICATION_CYGNUS);
        } catch (Exception ex) {
            // On ne fait rien, le module n'existe pas
        }
        try {
            this.aUserSession = aUserSession;
            RFDecisionJointTiersSummary decisionJointTiersSummary = findDecisionSummary(idTiers);

            // if ((decisionJointTiersSummary != null)
            // && this.hasReadRight("cygnus.qds.qdJointDossierJointTiersJointDemande")) {
            if (decisionJointTiersSummary != null) {
                List<RFPrestationAccordeeJointREPrestationAccordee> list = findPresation(idTiers);
                addInfosInTitle(decisionJointTiersSummary);
                String box = render(decisionJointTiersSummary, list);
                info.setText(box);
            } else {
                StringBuilder param = new StringBuilder();
                param.append(".chercher");
                title = createHref("cygnus.qds.qdJointDossierJointTiersJointDemande", param, "VG_RFM_TITLE").toString();

            }
        } catch (Exception e) {
            title = aUserSession.getLabel("VG_RFM_TITLE");
        }

        return new TISummaryInfo[] { info };
    }

    @Override
    public int getMaxHorizontalItems() {
        return 0;
    }

    @Override
    public PY_VG_MODULE_SIZE getModuleSize() {
        return moduleSize;
    }

    @Override
    public String getStyle() {
        return null;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getUrlTitle() {
        return urlTitle;
    }

    public boolean hasReadRight(String action) {
        boolean hasRight = aUserSession.hasRight(action, FWSecureConstants.READ);
        return hasRight;
    }

    private String render(RFDecisionJointTiersSummary decisionJointTiersSummary,
            List<RFPrestationAccordeeJointREPrestationAccordee> listPrestation) {
        StringBuilder param = new StringBuilder();

        param.append(".chercher");
        // &forIdDossier=
        // param.append(decisionJointTiersSummary.getIdDossier());
        param.append("&disabledSearchByGestionaire=true&partiallikeNumeroAVS=");
        param.append(decisionJointTiersSummary.getNss().substring(4));

        StringBuilder str = new StringBuilder();
        str.append("<div>");
        str.append(createHref("cygnus.decisions.decisionJointTiers", param, "JSP_SUMMARY_RF_DERNIERE_DECISION"));
        str.append(":<span style='padding-left:5px'>" + decisionJointTiersSummary.getDateValidation() + "</span>");
        str.append("</div>");

        Boolean displayDroit = true;

        for (RFPrestationAccordeeJointREPrestationAccordee item : listPrestation) {
            // if (!JadeStringUtil.isBlankOrZero(item.getDateDebut())) {
            str.append("<div style='margin:5px 0px; position: relative'>");

            str.append("<span style='inline-bloc; width:40px'>");
            if (displayDroit) {
                str.append(createHref("cygnus.prestationsaccordees.prestationsAccordees", param, "JSP_SUMMARY_RF_DROIT"));
                str.append(":");
            }
            str.append("</span>");

            str.append("<span style='padding-left:5px inline-bloc; width:180px'>");
            str.append(item.getDateDebutDroit());
            str.append(" - ");
            str.append(item.getDateFinDroit());
            str.append("</span>");

            str.append("<span style='position:absolute; right:0;  text-align: right; '> Fr.");
            str.append(new FWCurrency(item.getMontantPrestation()).toStringFormat());
            str.append("</span>");
            str.append("</div>");
            displayDroit = false;
            // }
        }

        return str.toString();
    }

    @Override
    public void setElement(String element) {
        this.element = element;
    }

    @Override
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public void setModuleSize(PY_VG_MODULE_SIZE moduleSize) {
        this.moduleSize = moduleSize;

    }

    @Override
    public void setStyle(String style) {
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
