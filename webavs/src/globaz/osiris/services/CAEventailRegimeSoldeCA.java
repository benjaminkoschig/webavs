package globaz.osiris.services;

import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BSession;
import globaz.globall.util.JADate;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.affiliation.AFAffiliationManager;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CACompteAnnexeManager;
import globaz.osiris.translation.CACodeSystem;
import globaz.pyxis.summary.TISummaryInfo;
import java.util.SortedMap;
import java.util.TreeMap;

public class CAEventailRegimeSoldeCA extends CAAbstractEventailRegime {
    private static final int MAX_CPTES_ANNEXES = 3;
    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.SMALL;

    @Override
    public PY_VG_MODULE_SIZE getModuleSize() {
        return moduleSize;
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
    protected TISummaryInfo handleCompteAnnexe(CACompteAnnexeManager compteM, BSession userSession) throws Exception {
        TISummaryInfo crtSum = new TISummaryInfo();
        StringBuffer s = new StringBuffer();

        setUrlTitle("");

        boolean hasRightComptaAux = userSession.hasRight("osiris.comptes.apercuComptes", FWSecureConstants.READ);
        boolean hasRightCompteAnnexeParSection = userSession.hasRight("osiris.comptes.apercuParSection",
                FWSecureConstants.READ);

        String likeNumNomPrint = "";
        // if (hasRightComptaAux) {
        SortedMap<Integer, CACompteAnnexe> mapComptes = new TreeMap<Integer, CACompteAnnexe>();
        Integer dateSansDateFin = new Integer(10000000);
        Integer dateAvecDateFin = new Integer(0);
        Integer dateSansAffiliation = new Integer(0);
        Integer dateSort = new Integer(0);

        for (int i = 0; i < compteM.size(); i++) {
            CACompteAnnexe compte = (CACompteAnnexe) compteM.getEntity(i);
            likeNumNomPrint = compte.getTiers().getDesignation1() + " " + compte.getTiers().getDesignation2();
            AFAffiliationManager afAffiliationManager = new AFAffiliationManager();
            afAffiliationManager.setSession(session);
            afAffiliationManager.setForIdTiers(compte.getIdTiers());
            afAffiliationManager.setForAffilieNumero(compte.getIdExterneRole());
            afAffiliationManager.find();

            if (afAffiliationManager.getSize() > 0) {
                AFAffiliation aff = (AFAffiliation) afAffiliationManager.getContainer().get(0);
                if (JadeStringUtil.isBlankOrZero(aff.getDateFin())) {
                    dateSort = dateSansDateFin;
                    dateSansDateFin++;
                } else {
                    JADate jaDate = new JADate(aff.getDateDebut());
                    dateAvecDateFin = new Integer(jaDate.toStrAMJ());
                    dateSort = dateAvecDateFin;
                    dateAvecDateFin++;
                }
            } else {
                dateSort = dateSansAffiliation;
                dateSansAffiliation++;
            }

            if (mapComptes.containsKey(dateSort)) {
                dateSort++;
            }
            mapComptes.put(dateSort, compte);

        }
        s.append("<table width='100%'>");
        crtSum.setText(s.toString());
        int ind = 0;
        maxReached = false;
        for (CACompteAnnexe compte : mapComptes.values()) {
            ind++;

            if (ind > CAEventailRegimeSoldeCA.MAX_CPTES_ANNEXES) {
                maxReached = true;
                break;
            }

            s.append("<tr>");
            s.append("		<td>");
            String libelleCompte = compte.getIdExterneRole();
            if (hasRightCompteAnnexeParSection) {
                libelleCompte = "<a href='#' onclick=\"callLocalUrl('/osiris?userAction=osiris.comptes.apercuParSection.chercher&id="
                        + compte.getIdCompteAnnexe() + "')\">" + compte.getIdExterneRole() + "</a>";
            }
            s.append(libelleCompte);
            s.append("		</td>");
            s.append("		<td>");
            s.append(CACodeSystem.getLibelle(compte.getSession(), compte.getIdRole()));
            s.append("		</td>");
            s.append("		<td>");
            s.append(userSession.getLabel("VG_CA_TEXT_SOLDE"));
            s.append("		</td>");
            s.append("		<td align='right'>");
            s.append(compte.getSoldeFormate());
            s.append("		</td>");
            s.append("</tr>");
            crtSum.setText(s.toString());
            // crtSum.setTitle(this.getHeadLine(compte), compte.getSession());
            // crtSum.setUrl(
            // "/osiris?userAction=osiris.comptes.apercuParSection.chercher&id=" + compte.getIdCompteAnnexe(),
            // "osiris.comptes.apercuParSection", FWSecureConstants.READ);
        }

        s.append("</table>");
        crtSum.setText(s.toString());

        if (hasRightComptaAux) {
            if (compteM.size() > 0) {
                setUrlTitle("osiris?userAction=osiris.comptes.apercuComptes.chercher&idTiersVG=" + idTiersVG);
            } else {
                setUrlTitle("osiris?userAction=osiris.comptes.apercuComptes.chercher");
            }
        }

        return crtSum;
        // /

        // s.append("<table>");
        // s.append("<tr><td>");
        // s.append(compte.getSession().getLabel("EVENTAIL_REGIME_SOLDE_COMPTE"));
        // s.append("</td><td>");
        // s.append(CACodeSystem.getLibelle(compte.getSession(), compte.getIdRole()));
        // s.append("</td><td>");
        // s.append(compte.getSoldeFormate());
        // s.append(this.getEndLine());
        // crtSum.setText(s.toString());
        // crtSum.setTitle(this.getHeadLine(compte), compte.getSession());
        // crtSum.setUrl("/osiris?userAction=osiris.comptes.apercuParSection.chercher&id=" + compte.getIdCompteAnnexe(),
        // "osiris.comptes.apercuParSection", FWSecureConstants.READ);
        // return crtSum;

        // TISummaryInfo crtSum = new TISummaryInfo();
        // StringBuffer s = new StringBuffer();
        // s.append("<table><tr><td><li></td><td>");
        // s.append(compte.getSession().getLabel("EVENTAIL_REGIME_SOLDE_COMPTE"));
        // s.append("</td><td>");
        // s.append(CACodeSystem.getLibelle(compte.getSession(), compte.getIdRole()));
        // s.append("</td><td>");
        // s.append(compte.getSoldeFormate());
        // s.append(this.getEndLine());
        // crtSum.setText(s.toString());
        // crtSum.setTitle(this.getHeadLine(compte), compte.getSession());
        // crtSum.setUrl("/osiris?userAction=osiris.comptes.apercuParSection.chercher&id=" + compte.getIdCompteAnnexe(),
        // "osiris.comptes.apercuParSection", FWSecureConstants.READ);
        // return crtSum;
    }

    @Override
    public void setModuleSize(PY_VG_MODULE_SIZE moduleSize) {
        this.moduleSize = moduleSize;
    }

}
