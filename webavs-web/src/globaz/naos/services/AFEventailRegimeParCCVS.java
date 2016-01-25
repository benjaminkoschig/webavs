package globaz.naos.services;

import globaz.framework.secure.FWSecureConstants;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.summary.TISummaryInfo;

public class AFEventailRegimeParCCVS extends AFAbstractEventailRegimeParCCVS {

    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.SMALL;

    private String urlTitle = "";

    @Override
    public int getMaxHorizontalItems() {
        return 0;
    }

    @Override
    public PY_VG_MODULE_SIZE getModuleSize() {
        return moduleSize;
    }

    @Override
    public String getTitle() {
        try {
            return CodeSystem.getLibelle(getLocalSession(), CodeSystem.GENRE_ASS_PARITAIRE);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public String getUrlTitle() {
        return urlTitle;
    }

    @Override
    public TISummaryInfo[] handleAffiliation(AFAffiliation crt) throws Exception {
        StringBuffer title = new StringBuffer();
        TISummaryInfo[] resTab = new TISummaryInfo[1];
        TISummaryInfo res = new TISummaryInfo();
        if (crt != null) {
            // charger également le montant périodique
            AFCotisationManager cotisations = new AFCotisationManager();
            cotisations.setSession(crt.getSession());
            cotisations.setForAffiliationId(crt.getAffiliationId());
            cotisations.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
            cotisations.find();
            if (cotisations.size() > 0) {
                if (crt.isTraitement().equals(Boolean.TRUE)) {
                    title.append("<table><tr style=color:#FF0066><td><li></td><td>");
                } else {
                    title.append("<table><tr><td><li></td><td>");
                }
                title.append(CodeSystem.getLibelle(crt.getSession(), crt.getTypeAffiliation()));
                title.append("</td><td>");
                title.append(crt.getDateDebut());
                title.append("-");
                title.append(crt.getDateFin());
                title.append("</td><td>");
                title.append(CodeSystem.getLibelle(crt.getSession(), crt.getPeriodicite()));
                title.append("</td></tr>");
                title.append("<tr><td colspan=\"3\"><table>");
                for (int i = 0; i < cotisations.size(); i++) {
                    title.append("<tr><td>");
                    AFCotisation coti = (AFCotisation) cotisations.getEntity(i);
                    String periodeCoti = coti.getDateDebut();
                    if (!JadeStringUtil.isIntegerEmpty(coti.getDateFin())) {
                        periodeCoti = periodeCoti + " -> " + coti.getDateFin() + " - ";
                    } else {
                        periodeCoti = periodeCoti + " -> 0 - ";
                    }
                    AFAssurance crtAss = ((AFCotisation) cotisations.getEntity(i)).getAssurance();
                    if (crtAss == null) {
                        title.append("Impossible de retrouver l'assurance");
                        title.append("</td><td>");
                        title.append("");
                        title.append("</td><td>");
                    } else {
                        title.append(crtAss.getAssuranceLibelleCourt());
                        title.append("</td><td>");
                        title.append(periodeCoti);
                        title.append("</td><td>");
                        title.append(CodeSystem.getLibelle(crt.getSession(), coti.getPeriodicite()) + " - "
                                + coti.getMasseAnnuelle());
                    }
                    title.append("</td></tr>");
                }
                if (cotisations.size() == 0) {
                    title.append("<tr><td>");
                    title.append(crt.getSession().getLabel("EVENTAIL_REGIME_AUCUNE_COTI"));
                    title.append("</td></tr>");
                }
                title.append("</table></td></tr></table>");
                res.setText(title.toString());
                res.setUrl(
                        "/naos?userAction=naos.affiliation.affiliation.afficher&selectedId=" + crt.getAffiliationId(),
                        "naos.affiliation.affiliation", FWSecureConstants.READ);
                res.setTitle(crt.getAffilieNumero());
                resTab[0] = res;
                return resTab;
            }
        }
        return null;
    }

    @Override
    public void setModuleSize(PY_VG_MODULE_SIZE moduleSize) {
        this.moduleSize = moduleSize;
    }

    @Override
    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }
}
