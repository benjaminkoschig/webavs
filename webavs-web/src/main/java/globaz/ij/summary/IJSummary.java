package globaz.ij.summary;

import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BSession;
import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.application.IJApplication;
import globaz.ij.db.prestations.IJPrestationJointLotPrononce;
import globaz.ij.db.prestations.IJPrestationJointLotPrononceManager;
import globaz.ij.db.prononces.IJPrononce;
import globaz.ij.db.prononces.IJPrononceJointDemande;
import globaz.ij.db.prononces.IJPrononceJointDemandeManager;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;

public class IJSummary implements ITISummarizable {
    String element = "";
    String icon = "";
    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.SMALL;
    String titre = "";

    private String urlTitle = "";

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
            BSession bSession = new BSession(IJApplication.DEFAULT_APPLICATION_IJ);
        } catch (Exception ex) {
            // On ne fait rien, le module n'existe pas
        }
        boolean hasDroitIJPres = userSession.hasRight("ij.prestations.prestationJointLotPrononce",
                FWSecureConstants.READ);
        boolean hasDroitIJPrononces = userSession.hasRight("ij.prononces.prononceJointDemande", FWSecureConstants.READ);

        setTitle(userSession.getLabel("VG_IJ_TITLE"));
        setUrlTitle("");

        IJPrononceJointDemandeManager ijPrononceJointDemandeMgr = new IJPrononceJointDemandeManager();
        ijPrononceJointDemandeMgr.setSession(userSession);
        ijPrononceJointDemandeMgr.setForIdTiers(idTiers);
        ijPrononceJointDemandeMgr.setForCsEtatPrononce(IIJPrononce.CS_COMMUNIQUE);
        ijPrononceJointDemandeMgr.setOrderBy(" CASE " + IJPrononce.FIELDNAME_DATE_FIN_PRONONCE
                + " WHEN 0 THEN 999999 ELSE " + IJPrononce.FIELDNAME_DATE_FIN_PRONONCE + " END DESC");

        ijPrononceJointDemandeMgr.find();

        if ((ijPrononceJointDemandeMgr.size() > 0)) {
            IJPrononceJointDemande ijPrononceJointDem = (IJPrononceJointDemande) ijPrononceJointDemandeMgr
                    .getFirstEntity();
            return handleIJprononce(ijPrononceJointDem, userSession, hasDroitIJPrononces, hasDroitIJPres);
        } else {
            if (hasDroitIJPrononces) {
                setUrlTitle("ij?userAction=ij.prononces.prononceJointDemande.chercher");
            }
            return null;
        }
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
        return "";
    }

    @Override
    public String getTitle() {
        return titre;
    }

    @Override
    public String getUrlTitle() {
        return urlTitle;
    }

    public TISummaryInfo[] handleIJprononce(IJPrononceJointDemande ijPrononceJointDem, BSession userSession,
            boolean hasDroitIJPrononces, boolean hasDroitIJPres) throws Exception {
        StringBuffer title = new StringBuffer();
        TISummaryInfo res = new TISummaryInfo();
        TISummaryInfo[] resTab = null;
        resTab = new TISummaryInfo[1];

        if (null != ijPrononceJointDem) {
            String titrePeriode = userSession.getLabel("JSP_SUMMARY_PERIODE");
            if (hasDroitIJPres) {
                IJPrestationJointLotPrononceManager igm = new IJPrestationJointLotPrononceManager();
                igm.setSession(userSession);
                igm.setForIdPrononce(ijPrononceJointDem.getIdPrononce());
                igm.setOrderBy("XLDDEB DESC");
                igm.find();

                if (igm.getContainerSize() > 0) {
                    IJPrestationJointLotPrononce ij = (IJPrestationJointLotPrononce) igm.getFirstEntity();
                    titrePeriode = "<a href='#' onclick=\"callLocalUrl('/ij?userAction=ij.prestations.prestationJointLotPrononce.chercher&forNoBaseIndemnisation="
                            + ij.getIdBaseIndemnisation()
                            + "&noAVS="
                            + ij.getNoAVS()
                            + "')\">"
                            + userSession.getLabel("JSP_SUMMARY_PERIODE") + "</a>";
                }

            }
            title.append("<table><tr><td>" + titrePeriode);
            title.append(" : ");
            title.append(JadeStringUtil.isBlankOrZero(ijPrononceJointDem.getDateDebutPrononce()) ? "..."
                    : ijPrononceJointDem.getDateDebutPrononce());
            if (!JadeStringUtil.isBlankOrZero(ijPrononceJointDem.getDateFinPrononce())) {
                title.append(" - ");
                title.append(ijPrononceJointDem.getDateFinPrononce());
            } else {
                title.append(" - ...");
            }
            title.append("</td></tr>");

            title.append("</table>");
            res.setText(title.toString());

            if (hasDroitIJPrononces) {

                setUrlTitle("ij?userAction=ij.prononces.prononceJointDemande.chercher&noAVS="
                        + ijPrononceJointDem.getNoAVS());
            }

            resTab[0] = res;
        } else {
            resTab = null;
        }

        return resTab;
    }

    @Override
    public void setElement(String element) {
        this.element = element;
    }

    @Override
    public void setIcon(String icone) {
        icon = icone;
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
        titre = title;
    }

    @Override
    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

}
