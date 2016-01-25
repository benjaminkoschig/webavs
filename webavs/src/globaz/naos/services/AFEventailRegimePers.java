package globaz.naos.services;

import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;

public class AFEventailRegimePers implements ITISummarizable {

    String element = "";

    String icon = "";
    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.SMALL;

    private String urlTitle = "";

    @Override
    public String getElement() {
        return element;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    private TISummaryInfo GetInfo(String idTiers, BSession userSession, String[] forGenreAff) throws Exception {
        TISummaryInfo crtSum = new TISummaryInfo();

        // recherche de l'affiliation en ordrant sur la date de fin
        AFAffiliationManager manager = new AFAffiliationManager();
        manager.setSession(userSession);
        manager.setForIdTiers(idTiers);
        manager.setForTypeAffiliation(forGenreAff);
        manager.setOrder("MADFIN");
        manager.find();
        if (manager.size() > 0) {
            StringBuffer text = new StringBuffer();
            text.append("<table><tr><td><li></td><td>");
            // prendre le premier trouvé = le dernier actif
            AFAffiliation crt = (AFAffiliation) manager.getFirstEntity();
            text.append(CodeSystem.getLibelle(userSession, crt.getTypeAffiliation()));
            text.append("</td><td>");
            text.append(crt.getDateDebut());
            text.append("-");
            text.append(crt.getDateFin());
            text.append("</td><td>");
            text.append(CodeSystem.getLibelle(userSession, crt.getPeriodicite()));
            text.append("</td><td>");
            crtSum.setUrl(
                    "/naos?userAction=naos.affiliation.affiliation.afficher&selectedId=" + crt.getAffiliationId(),
                    "naos.affiliation.affiliation", FWSecureConstants.READ);
            // charger également le montant périodique
            AFCotisationManager cotisations = new AFCotisationManager();
            cotisations.setSession(userSession);
            cotisations.setForAffiliationId(crt.getAffiliationId());
            cotisations.find();
            double montantTotPer = 0;
            try {
                for (int i = 0; i < cotisations.size(); i++) {
                    String montant = globaz.globall.util.JANumberFormatter.deQuote(((AFCotisation) cotisations
                            .getEntity(i)).getMontantPeriodicite());
                    if (!JadeStringUtil.isEmpty(montant)) {
                        montantTotPer += Double.parseDouble(montant);
                    }

                }
            } catch (Exception e) {
                // TODO : gérer le montant de coti..
            }

            text.append(globaz.globall.util.JANumberFormatter.fmt(
                    java.text.NumberFormat.getInstance(java.util.Locale.getDefault())
                            .parse(Double.toString(montantTotPer)).toString(), true, true, true, 2));
            if (cotisations.size() == 0) {
                text.append(userSession.getLabel("EVENTAIL_REGIME_AUCUNE_COTI"));
            }
            text.append("</td></tr></table>");
            crtSum.setText(text.toString());
            crtSum.setTitle(crt.getAffilieNumero());
        }
        return crtSum;
    }

    @Override
    public TISummaryInfo[] getInfoForTiers(String idTiers, BSession userSession) throws Exception {
        TISummaryInfo[] res = new TISummaryInfo[4];
        res[0] = GetInfo(idTiers, userSession, new String[] { CodeSystem.TYPE_AFFILI_INDEP,
                CodeSystem.TYPE_AFFILI_INDEP_EMPLOY });
        res[1] = GetInfo(idTiers, userSession, new String[] { CodeSystem.TYPE_AFFILI_NON_ACTIF });
        res[2] = GetInfo(idTiers, userSession, new String[] { CodeSystem.TYPE_AFFILI_TSE });
        res[3] = GetInfo(idTiers, userSession, new String[] { CodeSystem.TYPE_AFFILI_TSE_VOLONTAIRE });
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
        return "";
    }

    @Override
    public String getTitle() {
        return "Affiliation personnelle";
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
