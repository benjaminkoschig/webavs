package globaz.ccvd.services;

import globaz.ccvd.services.db.CCVDEvtRegFGISrc;
import globaz.ccvd.services.db.CCVDEvtRegFGISrcManager;
import globaz.globall.db.BSession;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;

public class CCVDEventailRegimePrest implements ITISummarizable {
    String element = "";
    String icon = "";
    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.SMALL;
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

        setTitle(userSession.getLabel("VG_INFORMATIONS_FGI_TITLE"));
        setUrlTitle("");

        CCVDEvtRegFGISrcManager infos = new CCVDEvtRegFGISrcManager();
        infos.setSession(userSession);
        infos.setForNIP(idTiers);
        infos.find();
        TISummaryInfo[] res = new TISummaryInfo[infos.size()];
        TISummaryInfo crt;
        for (int i = 0; i < infos.size(); i++) {
            crt = new TISummaryInfo();
            crt.setText(infos.getEntity(i) == null ? "" : ((CCVDEvtRegFGISrc) infos.getEntity(i)).getValue());
            res[i] = crt;
        }
        return res;
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
        return style;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getUrlTitle() {
        return urlTitle;
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
