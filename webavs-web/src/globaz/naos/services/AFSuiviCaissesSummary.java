package globaz.naos.services;

import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.api.IAFSuiviCaisseAffiliation;
import globaz.naos.application.AFApplication;
import globaz.naos.db.cotisation.AFCotiSearchEntity;
import globaz.naos.db.cotisation.AFCotiSearchManager;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliation;
import globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationManager;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;
import java.util.ArrayList;
import java.util.Iterator;

public class AFSuiviCaissesSummary implements ITISummarizable {

    private boolean cotisationsVisible = true;

    private String element = "";

    private String icon = "";
    private int maxHorizontalItems = 0;

    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.MEDIUM;
    private String style = "";

    private String title = "";

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
            new BSession(AFApplication.DEFAULT_APPLICATION_NAOS);
        } catch (Exception ex) {
            // On ne fait rien, le module n'existe pas
        }
        TISummaryInfo info = new TISummaryInfo();
        StringBuffer content = new StringBuffer();
        boolean rightForSuiviCaisses = userSession.hasRight("naos.suiviCaisseAffiliation.suiviCaisseAffiliation",
                FWSecureConstants.READ);

        setTitle(userSession.getLabel("VG_SUIVI_TITLE"));
        setUrlTitle("");

        AFCotiSearchManager mgrAdh = new AFCotiSearchManager();
        mgrAdh.setSession(userSession);
        mgrAdh.setForCotiActiveTodayJoin(true);
        mgrAdh.setPath(AFCotiSearchManager.PATH_ADHESION);
        mgrAdh.setForIdTiers(idTiers);
        mgrAdh.changeManagerSize(0);
        mgrAdh.setOrder(AFCotiSearchManager.TRI_PAR_AFFIE_ADHESION);
        mgrAdh.find();
        ArrayList<String> arrayIdAffiliations = new ArrayList<String>();

        for (Iterator<?> it = mgrAdh.iterator(); it.hasNext();) {
            AFCotiSearchEntity e = (AFCotiSearchEntity) it.next();
            if (!arrayIdAffiliations.contains(e.getIdAffiliation())
                    && JadeStringUtil.isBlankOrZero(e.getDateFinAffiliation())) {
                arrayIdAffiliations.add(e.getIdAffiliation());
            }
        }

        if (arrayIdAffiliations.size() > 0) {
            AFSuiviCaisseAffiliationManager afSuiviCaisseAffiliationManager = new AFSuiviCaisseAffiliationManager();
            afSuiviCaisseAffiliationManager.setSession(userSession);
            afSuiviCaisseAffiliationManager.setInAffiliationId(arrayIdAffiliations);
            afSuiviCaisseAffiliationManager.find();

            int nbItems = 0;
            String currentAffiliationId = "";
            for (int i = 0; i < afSuiviCaisseAffiliationManager.size(); i++) {
                AFSuiviCaisseAffiliation affiliation = new AFSuiviCaisseAffiliation();
                affiliation = (AFSuiviCaisseAffiliation) afSuiviCaisseAffiliationManager.get(i);
                if (!JadeStringUtil.isBlankOrZero(affiliation.getDateFin())) {
                    continue;
                }
                currentAffiliationId = affiliation.getAffiliationId();

                if (!IAFSuiviCaisseAffiliation.GENRE_CAISSE_AVS.equals(affiliation.getGenreCaisse())
                        && !IAFSuiviCaisseAffiliation.GENRE_CAISSE_AF.equals(affiliation.getGenreCaisse())
                        && !"19190040".equals(affiliation.getGenreCaisse())) {
                    // TODO Type 19190040 == AF (Personnel), uniquement à la CCVS?
                    continue;
                }

                String intituleCaisse = "";
                if (JadeStringUtil.isBlankOrZero(affiliation.getDateFin())) {
                    if (IAFSuiviCaisseAffiliation.GENRE_CAISSE_AVS.equals(affiliation.getGenreCaisse())) {
                        intituleCaisse = userSession.getLabel("VG_SUIVI_TYPE_CAISSE_AVS");// "Caisse AVS";
                    } else if (IAFSuiviCaisseAffiliation.GENRE_CAISSE_AF.equals(affiliation.getGenreCaisse())) {
                        intituleCaisse = userSession.getLabel("VG_SUIVI_TYPE_CAISSE_AF");// "Caisse AF";
                    } else if ("19190040".equals(affiliation.getGenreCaisse())) {
                        intituleCaisse = userSession.getLabel("VG_SUIVI_TYPE_CAISSE_AF_PERSONNELLE");
                    }
                    nbItems++;

                    if (nbItems > 3) {
                        content.append("...");
                        break;
                    }

                    String caisseInfos = affiliation.getAdministration().getCodeAdministration() + " " + intituleCaisse
                            + " " + affiliation.getDateDebut() + "-";

                    String title = affiliation.getAdministration().getNom();
                    if (!JadeStringUtil.isBlankOrZero(affiliation.getNumeroAffileCaisse())) {
                        title = affiliation.getNumeroAffileCaisse() + " - " + affiliation.getAdministration().getNom();
                    }

                    content.append("<span title=\"" + title + "\">" + caisseInfos + "</span>");
                    content.append("<br>");
                }
            }
            if (rightForSuiviCaisses) {
                if (!JadeStringUtil.isBlankOrZero(currentAffiliationId)) {
                    setUrlTitle("naos?userAction=naos.suiviCaisseAffiliation.suiviCaisseAffiliation.chercher&affiliationId="
                            + currentAffiliationId);
                } else {
                    setUrlTitle("naos?userAction=naos.affiliation.affiliation.chercher&idTiers=" + idTiers);
                }
            }
        }
        info.setText(content.toString());
        return new TISummaryInfo[] { info };
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
