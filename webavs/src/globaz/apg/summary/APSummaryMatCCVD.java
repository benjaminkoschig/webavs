package globaz.apg.summary;

import globaz.apg.api.annonces.IAPAnnonce;
import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitLAPGJointDemande;
import globaz.apg.db.droits.APDroitLAPGJointDemandeManager;
import globaz.apg.db.droits.APEmployeur;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.apg.db.prestation.APPrestation;
import globaz.apg.db.prestation.APPrestationJointLotTiersDroitManager;
import globaz.commons.nss.NSUtil;
import globaz.framework.secure.FWSecureConstants;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class APSummaryMatCCVD implements ITISummarizable {
    String element = "";
    String icon = "";
    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.SMALL;
    String titre = "";

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
        BSession bSession = new BSession(APApplication.DEFAULT_APPLICATION_APG);
        setTitle(userSession.getLabel("SUMMARY_MAT_TITLE"));

        // on cherche la dernier droit Mat pour le tiers
        APDroitLAPGJointDemandeManager manager = new APDroitLAPGJointDemandeManager();
        manager.setSession(userSession);
        manager.setForIdTiers(idTiers);
        manager.setForGenreService(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE);
        manager.setOrderBy(APDroitLAPG.FIELDNAME_DATEDEBUTDROIT + " DESC , " + APDroitLAPG.FIELDNAME_IDDROIT_LAPG
                + " DESC");
        manager.find();

        String lastDateDebut = "";
        List listeDroitsMat = new ArrayList();

        if (manager.size() > 0) {

            Iterator iter = manager.iterator();
            while (iter.hasNext()) {
                APDroitLAPGJointDemande droit = (APDroitLAPGJointDemande) iter.next();

                if (!lastDateDebut.equals(droit.getDateDebutDroit())) {
                    listeDroitsMat.add(droit);
                    lastDateDebut = droit.getDateDebutDroit();
                }
            }

            return handleDroit((APDroitLAPGJointDemande[]) listeDroitsMat.toArray(new APDroitLAPGJointDemande[1]));
        } else {
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

    @Override
    public String getStyle() {
        return "";
    }

    @Override
    public String getTitle() {
        return titre;
    }

    @Override
    public String getUrlTitle() {
        return urlTitle;
    }

    public TISummaryInfo[] handleDroit(APDroitLAPGJointDemande[] droitsMat) throws Exception {

        StringBuffer title = new StringBuffer();
        TISummaryInfo res = new TISummaryInfo();

        TISummaryInfo[] resTab = new TISummaryInfo[droitsMat.length];

        boolean isProtected = false;

        if (resTab.length > 0) {

            // Si le CI ou l'affiliation est protégé, il faut uniquement
            // afficher la mention "Assuré protégé" au lieu du détail
            // BZ 4554
            if (isProtectedCI(droitsMat[0]) || isProtectedAffiliation(droitsMat[0])) {

                resTab = new TISummaryInfo[1];

                title.append("<table>");
                title.append("<tr><td>");
                title.append(droitsMat[0].getSession().getLabel("SUMMARY_PROTEGE"));
                title.append("</td></tr>");
                title.append("</table>");

                res.setText(title.toString());
                res.setTitle(droitsMat[0].getSession().getLabel("SUMMARY_DROIT"), droitsMat[0].getSession());

                resTab[0] = res;

                isProtected = true;

            }
        }

        if (!isProtected) {

            for (int i = 0; i < droitsMat.length; i++) {

                res = new TISummaryInfo();
                title = new StringBuffer();

                title.append("<table>");
                title.append("<tr><td>");
                title.append(droitsMat[i].getSession().getLabel("SUMMARY_PERIODE"));
                title.append(droitsMat[i].getDateDebutDroit());
                title.append("-");
                title.append(droitsMat[i].getDateFinDroit());
                title.append("</td></tr>");
                title.append("<tr><td>");

                if (IAPDroitLAPG.CS_REFUSE.equals(droitsMat[i].getEtatDroit())
                        || IAPDroitLAPG.CS_TANSFERE.equals(droitsMat[i].getEtatDroit())) {

                    if (IAPDroitLAPG.CS_REFUSE.equals(droitsMat[i].getEtatDroit())) {

                        title.append("<table><tr><td colspan=\"2\">");
                        title.append("Cas refusé ");
                        title.append("</td></tr>");

                        title.append("</table>");
                    } else {
                        title.append("<table><tr><td colspan=\"2\">");
                        title.append("Cas transféré autre caisse ");
                        title.append("</td></tr>");
                        title.append("</table>");

                    }

                } else {
                    // la montant brut des prestations que ne sont pas des
                    // restitutions
                    APPrestationJointLotTiersDroitManager prestJoinDroitManager = new APPrestationJointLotTiersDroitManager();
                    prestJoinDroitManager.setSession(droitsMat[i].getSession());
                    prestJoinDroitManager.setForIdDroit(droitsMat[i].getIdDroit());
                    prestJoinDroitManager.setNotForContenuAnnonce(IAPAnnonce.CS_RESTITUTION);
                    BigDecimal montantBrut = prestJoinDroitManager.getSum(APPrestation.FIELDNAME_MONTANTBRUT);

                    // on cherche les destinatires des repartitions par la sit.
                    // prof.
                    APSituationProfessionnelleManager sitProfManager = new APSituationProfessionnelleManager();
                    sitProfManager.setSession(droitsMat[i].getSession());
                    sitProfManager.setForIdDroit(droitsMat[i].getIdDroit());
                    sitProfManager.find();

                    String pmtA = "";

                    boolean hasPmtIndependant = false;
                    boolean hasPmtEmployeur = false;
                    boolean hasPmtAssure = false;

                    Iterator sitProfIter = sitProfManager.iterator();
                    while (sitProfIter.hasNext()) {
                        APSituationProfessionnelle sitProf = (APSituationProfessionnelle) sitProfIter.next();

                        if (sitProf.getIsIndependant().booleanValue()) {
                            hasPmtIndependant = true;
                        } else {

                            if (sitProf.getIsVersementEmployeur().booleanValue()) {
                                hasPmtEmployeur = true;
                            } else {
                                hasPmtAssure = true;
                            }
                        }
                    }

                    if (hasPmtIndependant) {
                        pmtA = droitsMat[i].getSession().getLabel("SUMMARY_PMT_INDEPENDANT");
                    }
                    if (hasPmtEmployeur) {
                        if (pmtA.length() > 0) {
                            pmtA += "/";
                        }
                        pmtA += droitsMat[i].getSession().getLabel("SUMMARY_PMT_EMPLOYEUR");
                    }
                    if (hasPmtAssure) {
                        if (pmtA.length() > 0) {
                            pmtA += "/";
                        }
                        pmtA += droitsMat[i].getSession().getLabel("SUMMARY_PMT_ASSURE");
                    }

                    title.append("<table><tr><td>");
                    title.append(droitsMat[i].getSession().getLabel("SUMMARY_MONTANT_BRUT"));
                    title.append(new FWCurrency(montantBrut.toString()).toStringFormat());
                    title.append("</td><td>");
                    title.append(droitsMat[i].getSession().getLabel("SUMMARY_PMT_A"));
                    title.append(pmtA);
                    title.append("</td></tr>");
                    title.append("</table>");

                }
                title.append("</td></tr>");
                title.append("</table>");

                res.setText(title.toString());
                res.setUrl("/apg?userAction=apg.droits.droitMatP.afficher&selectedId=" + droitsMat[i].getIdDroit(),
                        "apg", FWSecureConstants.READ);
                res.setTitle(droitsMat[i].getSession().getLabel("SUMMARY_DROIT"), droitsMat[i].getSession());

                resTab[i] = res;
            }
        }

        return resTab;
    }

    public boolean isProtectedAffiliation(APDroitLAPGJointDemande droitsMat) throws Exception {

        APSituationProfessionnelleManager sitProMgr = new APSituationProfessionnelleManager();
        sitProMgr.setSession(droitsMat.getSession());
        sitProMgr.setForIdDroit(droitsMat.getIdDroit());
        sitProMgr.find();

        boolean isProtege = false;

        for (Iterator iterator = sitProMgr.iterator(); iterator.hasNext();) {
            APSituationProfessionnelle sitPro = (APSituationProfessionnelle) iterator.next();

            APEmployeur employeur = new APEmployeur();
            employeur.setSession(droitsMat.getSession());
            employeur.setIdEmployeur(sitPro.getIdEmployeur());
            employeur.retrieve(droitsMat.getSession().getCurrentThreadTransaction());

            AFAffiliation affiliation = new AFAffiliation();
            affiliation.setSession(droitsMat.getSession());
            affiliation.setId(employeur.getIdAffilie());
            affiliation.retrieve();

            if (affiliation.isNew()) {
                isProtege = false;
            } else {
                if (affiliation.hasRightAccesSecurity()) {
                    isProtege = false;
                } else {
                    isProtege = true;
                }
            }
        }

        return isProtege;

    }

    public boolean isProtectedCI(APDroitLAPGJointDemande droitsMat) throws Exception {

        PRDemande demande = new PRDemande();
        demande.setSession(droitsMat.getSession());
        demande.setIdDemande(droitsMat.getIdDemande());
        demande.retrieve();

        PRTiersWrapper tiers = PRTiersHelper.getTiersParId(droitsMat.getSession(), demande.getIdTiers());

        if (tiers != null) {
            CICompteIndividuelManager ciManager = new CICompteIndividuelManager();
            ciManager.setSession(droitsMat.getSession());
            ciManager.setForRegistre(CICompteIndividuel.CS_REGISTRE_ASSURES);
            ciManager.setForNumeroAvs(NSUtil.unFormatAVS(tiers.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL)));
            ciManager.wantCallMethodBeforeFind(false);
            ciManager.find();

            if (ciManager.isEmpty()) {
                return false;
            } else {
                CICompteIndividuel ci = (CICompteIndividuel) ciManager.getFirstEntity();
                if (ci.hasUserShowRight(droitsMat.getSession().getCurrentThreadTransaction())) {
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }
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
        titre = title;
    }

    @Override
    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

}
