package globaz.naos.services;

import globaz.globall.db.BSession;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;

public abstract class AFAbstractEventailRegimeParCCVS implements ITISummarizable {
    String element = "";
    String icon = "";
    BSession localSession = null;

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
        AFAffiliationManager manager = new AFAffiliationManager();
        manager.setSession(userSession);
        manager.setForIdTiers(idTiers);
        // manager.setForTypeAffiliation(new String[] {
        // CodeSystem.TYPE_AFFILI_INDEP_EMPLOY, CodeSystem.TYPE_AFFILI_EMPLOY
        // });
        manager.setOrder("MADFIN");
        manager.find();
        setLocalSession(userSession);
        if (manager.size() > 0) {
            return handleAffiliation((AFAffiliation) manager.getFirstEntity());
        } else {
            return null;
        }
    }

    public BSession getLocalSession() {
        return localSession;
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

    public void setLocalSession(BSession localSession) {
        this.localSession = localSession;
    }

    @Override
    public void setStyle(String style) {

    }

    @Override
    public void setTitle(String title) {
    }

}
