package globaz.osiris.services;

import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BSession;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.db.comptes.CASection;
import globaz.pyxis.summary.TISummaryInfo;

public class CAEventailRegimeFac extends CAAbstractEventailRegime {

    @Override
    protected TISummaryInfo handleCompteAnnexe(CACompteAnnexeManager compte, BSession userSession) throws Exception {
        // CASectionManager sectionM = new CASectionManager();
        // sectionM.setSession(compte.getSession());
        // sectionM.setForIdCompteAnnexe(compte.getIdCompteAnnexe());
        // sectionM.setOrderBy(CASectionManager.ORDER_DATE_DESCEND);
        // sectionM.find();
        // if (sectionM.size() == 0) {
        // TISummaryInfo crtSum = new TISummaryInfo();
        // StringBuffer s = new StringBuffer(this.getHeadLine(compte));
        // s.append("<table><tr><td><li></td><td>");
        // s.append(compte.getSession().getLabel("EVENTAIL_REGIME_AUCUNE_SECTION"));
        // s.append(this.getEndLine());
        // crtSum.setText(s.toString());
        // return crtSum;
        // } else {
        // // on prend la première section trouvée, la dernière en fait...
        // return this.handleSection(compte, (CASection) sectionM.getFirstEntity());
        // }
        return new TISummaryInfo();
    }

    private TISummaryInfo handleSection(CACompteAnnexe compte, CASection section) throws Exception {
        TISummaryInfo crtSum = new TISummaryInfo();
        StringBuffer s = new StringBuffer();
        s.append("<table><tr><td><li></td><td>");
        s.append(section.getDescription());
        s.append("</td><td>");
        s.append(section.getDateDebutPeriode());
        s.append("</td><td>");
        s.append(section.getDateFinPeriode());
        s.append("</td><td>");
        s.append(section.getDateSection());
        s.append("</td><td>");
        s.append(section.getBaseFormate());
        // s.append(this.getEndLine());
        crtSum.setText(s.toString());
        crtSum.setTitle(getHeadLine(compte), compte.getSession());
        crtSum.setUrl("/osiris?userAction=osiris.comptes.apercuSectionDetaille.chercher&id=" + section.getIdSection(),
                "osiris.comptes.apercuSectionDetaille", FWSecureConstants.READ);
        return crtSum;
    }

}
