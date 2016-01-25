package globaz.osiris.services;

import globaz.globall.db.BSession;
import globaz.osiris.application.CAApplication;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;

public abstract class CAAbstractEventailRegime implements ITISummarizable {

    String element = "";
    String icon = "";
    protected String idTiersVG = "";
    protected boolean maxReached = false;
    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.SMALL;

    BSession session = null;
    private String urlTitle = "";

    @Override
    public String getElement() {
        return element;
    }

    protected String getHeadLine(CACompteAnnexe compte) {
        StringBuffer s = new StringBuffer();
        s.append(compte.getIdExterneRole());
        return s.toString();
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public TISummaryInfo[] getInfoForTiers(String idTiers, BSession userSession) throws Exception {
        BSession bSession = new BSession(CAApplication.DEFAULT_APPLICATION_OSIRIS);
        session = userSession;
        idTiersVG = idTiers;

        CACompteAnnexeManager compteM = new CACompteAnnexeManager();
        compteM.setSession(userSession);
        compteM.setForIdTiers(idTiers);
        compteM.find();
        TISummaryInfo[] res = new TISummaryInfo[1];

        res[0] = handleCompteAnnexe(compteM, userSession);

        // int ind = 0;
        // for (int i = 0; i < compteM.size(); i++) {
        // CACompteAnnexe compte = (CACompteAnnexe) compteM.getEntity(i);
        // ind++;
        // boolean maxReached = false;
        // if (compteM.size() > 3) {
        // maxReached = true;
        // }
        // // StringBuffer s = new StringBuffer(compte.getIdExterneRole());
        // // s.append("\t");
        // // s.append(compte.getDescription());
        // // s.append("\t");
        //
        // if (i > 3) {
        // break;
        // }
        //
        // res[i] = this.handleCompteAnnexe(compte, maxReached);
        // }
        return res;
    }

    @Override
    public int getMaxHorizontalItems() {
        if (maxReached) {
            return 1;
        } else {
            return 0;
        }
    };

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
        if (session != null) {
            return session.getLabel("VG_CA_TITLE");
        } else {
            return "";
        }
    }

    @Override
    public String getUrlTitle() {
        return urlTitle;
    }

    protected abstract TISummaryInfo handleCompteAnnexe(CACompteAnnexeManager compte, BSession userSession)
            throws Exception;

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
    }

    @Override
    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

}
