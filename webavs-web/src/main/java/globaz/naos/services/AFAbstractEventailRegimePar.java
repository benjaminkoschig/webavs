package globaz.naos.services;

import globaz.globall.db.BSession;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;

public abstract class AFAbstractEventailRegimePar implements ITISummarizable {
    String element = "";
    String icon = "";
    BSession session = null;

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

        setSession(userSession);

        AFAffiliationManager manager = new AFAffiliationManager();
        manager.setSession(userSession);
        manager.setForIdTiers(idTiers);
        manager.setForTypeAffiliation(new String[] { CodeSystem.TYPE_AFFILI_INDEP_EMPLOY,
                CodeSystem.TYPE_AFFILI_EMPLOY, CodeSystem.TYPE_AFFILI_EMPLOY_D_F });
        manager.setOrder("MADFIN");
        manager.find();
        if (manager.size() > 0) {
            return handleAffiliation((AFAffiliation) manager.getFirstEntity());
        } else {
            return null;
        }
    }

    public BSession getSession() {
        return session;
    }

    @Override
    public String getStyle() {
        return "";
    }

    public abstract TISummaryInfo[] handleAffiliation(AFAffiliation affiliation) throws Exception;

    @Override
    public void setElement(String element) {
        this.element = element;
    }

    @Override
    public void setIcon(String icone) {
        icon = icone;
    }

    public void setSession(BSession session) {
        this.session = session;
    }

    @Override
    public void setStyle(String style) {

    }

    @Override
    public void setTitle(String title) {
    }

}
