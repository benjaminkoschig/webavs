package globaz.apg.summary;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.application.APApplication;
import globaz.apg.db.droits.APDroitLAPG;
import globaz.apg.db.droits.APDroitLAPGJointDemande;
import globaz.apg.db.droits.APDroitLAPGJointDemandeManager;
import globaz.apg.db.droits.APEmployeur;
import globaz.apg.db.droits.APSituationProfessionnelle;
import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.commons.nss.NSUtil;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BSession;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.pavo.db.compte.CICompteIndividuel;
import globaz.pavo.db.compte.CICompteIndividuelManager;
import globaz.prestation.db.demandes.PRDemande;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;
import java.util.Iterator;

public class APSummaryMat implements ITISummarizable {
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
        // Nouvelle session afin de charger les labels de l'application
        try {
            BSession bSession = new BSession(APApplication.DEFAULT_APPLICATION_APG);
        } catch (Exception ex) {
            // On ne fait rien, le module n'existe pas
        }
        boolean hasDroitMatPrest = userSession.hasRight("apg.prestation.prestationJointLotTiersDroit",
                FWSecureConstants.READ);
        boolean hasDroitMatDroit = userSession.hasRight("apg.droits.droitLAPGJointDemande", FWSecureConstants.READ);

        setTitle(userSession.getLabel("VG_MAT_TITLE"));
        setUrlTitle("");

        // on cherche la dernier droit Mat pour le tiers
        APDroitLAPGJointDemandeManager manager = new APDroitLAPGJointDemandeManager();
        manager.setSession(userSession);
        manager.setForIdTiers(idTiers);
        manager.setForGenreService(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE);
        manager.setOrderBy(APDroitLAPG.FIELDNAME_DATEDEBUTDROIT + " DESC , " + APDroitLAPG.FIELDNAME_IDDROIT_LAPG
                + " DESC");
        manager.find();

        if ((manager.size() > 0)) {
            APDroitLAPGJointDemande lastDroit = (APDroitLAPGJointDemande) manager.getFirstEntity();
            return handleDroit(lastDroit, userSession, hasDroitMatPrest, hasDroitMatDroit);
        } else {
            if (hasDroitMatDroit) {
                setUrlTitle("apg?userAction=apg.droits.droitLAPGJointDemande.chercher&typePrestation=MATERNITE&idTiers="
                        + idTiers);
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

    public TISummaryInfo[] handleDroit(APDroitLAPGJointDemande lastDroit, BSession userSession,
            boolean hasDroitMatPrest, boolean hasDroitMatDroit) throws Exception {
        StringBuffer title = new StringBuffer();
        TISummaryInfo res = new TISummaryInfo();

        TISummaryInfo[] resTab = new TISummaryInfo[1];

        boolean isProtected = false;

        if (resTab.length > 0) {

            resTab = new TISummaryInfo[1];

            // Si le CI ou l'affiliation est protégé, il faut uniquement
            // afficher la mention "Assuré protégé" au lieu du détail
            // BZ 4554
            if (isProtectedCI(lastDroit) || isProtectedAffiliation(lastDroit)) {

                title.append("<table width='100%'>");
                title.append("<tr><td>");
                title.append(lastDroit.getSession().getLabel("SUMMARY_PROTEGE"));
                title.append("</td></tr>");
                title.append("</table>");

                res.setText(title.toString());
                res.setTitle(lastDroit.getSession().getLabel("SUMMARY_DROIT"), lastDroit.getSession());

                resTab[0] = res;

                isProtected = true;

            }
        }

        if (!isProtected) {
            if (lastDroit != null) {
                title.append("<table width='100%'>");
                title.append("<td><tr>");
                // title.append(lastDroit.getSession().getLabel("SUMMARY_PERIODE"));
                String titrePeriode = lastDroit.getSession().getLabel("SUMMARY_PERIODE");
                if (hasDroitMatPrest) {
                    titrePeriode = "<a href='#' onclick=\"callLocalUrl('/apg?userAction=apg.prestation.prestationJointLotTiersDroit.chercher&typePrestation=MATERNITE&forIdDroit="
                            + lastDroit.getIdDroit()
                            + "')\">"
                            + lastDroit.getSession().getLabel("SUMMARY_PERIODE")
                            + "</a>";
                }
                title.append(titrePeriode);
                title.append(" : ");
                title.append(lastDroit.getDateDebutDroit());
                title.append("-");
                title.append(lastDroit.getDateFinDroit());
                title.append("</td></tr>");
                title.append("</table>");

                if (hasDroitMatDroit) {
                    setUrlTitle("apg?userAction=apg.droits.droitLAPGJointDemande.chercher&typePrestation=MATERNITE&idDroit="
                            + lastDroit.getIdDroit()
                            + "&noAVS="
                            + lastDroit.getNoAVS()
                            + "&idTiers="
                            + lastDroit.getIdTiers());
                }
                res.setText(title.toString());

                resTab[0] = res;
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
