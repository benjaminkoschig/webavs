package globaz.draco.services;

import globaz.draco.application.DSApplication;
import globaz.draco.db.declaration.DSDeclarationListViewBean;
import globaz.draco.db.declaration.DSDeclarationViewBean;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DSEventailRegimePar implements ITISummarizable {
    String element = "";
    private boolean hasRightDraco = false;

    String icon = "";

    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.SMALL;

    BSession session = null;
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
            BSession bSession = new BSession(DSApplication.DEFAULT_APPLICATION_DRACO);
        } catch (Exception ex) {
            // On ne fait rien, le module n'existe pas
        }
        setSession(userSession);
        hasRightDraco = session.hasRight("draco.declaration.declaration", FWSecureConstants.READ);

        setUrlTitle("");

        // if (hasRightDraco) {

        AFAffiliationManager manager = new AFAffiliationManager();
        manager.setSession(userSession);
        manager.setForIdTiers(idTiers);
        manager.setForTypeAffiliation(new String[] { CodeSystem.TYPE_AFFILI_INDEP_EMPLOY,
                CodeSystem.TYPE_AFFILI_EMPLOY, CodeSystem.TYPE_AFFILI_EMPLOY_D_F });
        manager.setForDateFin("0");
        manager.setOrder("MADFIN");
        manager.find();
        if (manager.size() > 0) {
            return handleAffiliation(manager);
        } else {
            if (hasRightDraco) {
                setUrlTitle("draco?userAction=draco.declaration.declaration.chercher");
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

    public BSession getSession() {
        return session;
    }

    @Override
    public String getStyle() {
        return "";
    }

    @Override
    public String getTitle() {
        return getSession().getLabel("VG_DS_TITLE");
    }

    @Override
    public String getUrlTitle() {
        return urlTitle;
    }

    public TISummaryInfo[] handleAffiliation(AFAffiliationManager manager) throws Exception {
        if (manager != null) {
            // recherche des déclarations de salaires

            ArrayList<DSDeclarationViewBean> arrayDS = new ArrayList<DSDeclarationViewBean>();
            Map<String, DSDeclarationViewBean> mapDS = new HashMap<String, DSDeclarationViewBean>();
            for (Iterator it = manager.getContainer().iterator(); it.hasNext();) {
                AFAffiliation affiliation = (AFAffiliation) it.next();

                DSDeclarationListViewBean listeDS = new DSDeclarationListViewBean();
                listeDS.setSession(manager.getSession());
                listeDS.setForSelectionTri("1");
                listeDS.setLikeNumeroAffilie(affiliation.getAffilieNumero());
                listeDS.setOrder("TAANNE DESC");
                listeDS.find();
                if (listeDS.size() == 0) {
                    TISummaryInfo sum = new TISummaryInfo();
                    setUrlTitle("draco?userAction=draco.declaration.declaration.chercher");
                    return new TISummaryInfo[] { sum };
                } else {
                    DSDeclarationViewBean ds = (DSDeclarationViewBean) listeDS.getEntity(0);
                    mapDS.put(ds.getAnnee(), ds);
                }
            }

            TISummaryInfo[] res = new TISummaryInfo[1];
            res[0] = handline(mapDS);
            return res;
        } else {
            return new TISummaryInfo[] {};
        }
    }

    private TISummaryInfo handline(Map<String, DSDeclarationViewBean> mapDS) throws Exception {
        TISummaryInfo sum = new TISummaryInfo();
        boolean hasRightLeo = getSession().hasRight("leo.envoi.envoi", FWSecureConstants.READ);

        if (hasRightLeo) {
            setUrlTitle("leo?userAction=leo.envoi.envoi.chercher&forTypeDocument=6700003");
        }

        StringBuffer s = new StringBuffer();
        s.append("<table>");
        for (String year : mapDS.keySet()) {
            DSDeclarationViewBean ds = mapDS.get(year);

            String annee = ds.getAnnee();
            String urlAnnee = annee;
            if (hasRightDraco) {
                urlAnnee = "<a href='#' onclick=\"callLocalUrl('/draco?userAction=draco.declaration.declaration.afficher&selectedId="
                        + ds.getIdDeclaration()
                        + "&idTiersVueGlobale="
                        + ds.getIdTiers()
                        + "')\" title='"
                        + ds.getAffilieNumero() + "'>" + annee + "</a>";
            }

            String dateEnvoi = ds.findDateEnvoi();
            String dateReception = ds.getDateRetourEff();

            String etat = "";
            if (!JadeStringUtil.isBlankOrZero(ds.getEtat())) {
                etat = CodeSystem.getLibelle(session, ds.getEtat());
            }
            s.append("	<tr>");
            s.append("		<td>");
            s.append(urlAnnee + " : ");
            s.append("		</td>");
            s.append("		<td>");
            s.append(ds.getSession().getLabel("VG_DS_TEXT_ENVOI") + " " + dateEnvoi);
            s.append("		</td>");
            s.append("	</tr>");
            s.append("	<tr>");
            s.append("		<td>");
            s.append("			&nbsp");
            s.append("		</td>");
            s.append("		<td>");
            s.append(ds.getSession().getLabel("VG_DS_TEXT_RECEPTION") + " " + dateReception
                    + (JadeStringUtil.isBlankOrZero(etat) ? "" : " - " + etat));
            s.append("		</td>");
            s.append("	</tr>");
        }

        s.append("</table>");
        sum.setText(s.toString());
        return sum;
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

    public void setSession(BSession session) {
        this.session = session;
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
