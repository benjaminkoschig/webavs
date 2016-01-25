package globaz.naos.services;

import globaz.framework.secure.FWSecureConstants;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.assurance.AFAssurance;
import globaz.naos.db.cotisation.AFCotisation;
import globaz.naos.db.cotisation.AFCotisationManager;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.summary.TISummaryInfo;

public class AFEventailRegimePar extends AFAbstractEventailRegimePar {

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
        return "Affiliation paritaire";
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
            title.append("<table><tr><td><li></td><td>");
            title.append(CodeSystem.getLibelle(crt.getSession(), crt.getTypeAffiliation()));
            title.append("</td><td>");
            title.append(crt.getDateDebut());
            title.append("-");
            title.append(crt.getDateFin());
            title.append("</td><td>");
            title.append(CodeSystem.getLibelle(crt.getSession(), crt.getPeriodicite()));
            title.append("</td></tr>");
            // charger également le montant périodique
            AFCotisationManager cotisations = new AFCotisationManager();
            cotisations.setSession(crt.getSession());
            cotisations.setForAffiliationId(crt.getAffiliationId());
            cotisations.setForGenreAssurance(CodeSystem.GENRE_ASS_PARITAIRE);
            cotisations.find();
            title.append("<tr><td colspan=\"3\"><table>");
            for (int i = 0; i < cotisations.size(); i++) {
                title.append("<tr><td>");
                AFAssurance crtAss = ((AFCotisation) cotisations.getEntity(i)).getAssurance();
                title.append(crtAss == null ? "Impossible de retrouver l'assurance" : crtAss.getAssuranceLibelle());
                title.append("</td></tr>");
            }
            if (cotisations.size() == 0) {
                title.append("<tr><td>");
                title.append(crt.getSession().getLabel("EVENTAIL_REGIME_AUCUNE_COTI"));
                title.append("</td></tr>");
            }
            title.append("</table></td></tr></table>");
            res.setText(title.toString());
        }
        res.setUrl("/naos?userAction=naos.affiliation.affiliation.afficher&selectedId=" + crt.getAffiliationId(),
                "naos.affiliation.affiliation", FWSecureConstants.READ);
        res.setTitle(crt.getAffilieNumero());
        resTab[0] = res;
        return resTab;
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
