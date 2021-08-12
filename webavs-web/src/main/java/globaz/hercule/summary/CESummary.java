package globaz.hercule.summary;

import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BSession;
import globaz.globall.util.JADate;
import globaz.hercule.application.CEApplication;
import globaz.hercule.db.controleEmployeur.*;
import globaz.hercule.db.couverture.CECouverture;
import globaz.hercule.db.couverture.CECouvertureManager;
import globaz.hercule.service.CEAttributionPtsService;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pyxis.db.tiers.TITiersViewBean;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;

public class CESummary implements ITISummarizable {
    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.SMALL;
    String title = "";
    private String urlTitle = "";

    @Override
    public String getElement() {
        return "";
    }

    @Override
    public String getIcon() {
        return "";
    }

    @Override
    public TISummaryInfo[] getInfoForTiers(String idTiers, BSession userSession) throws Exception {
        try {
            BSession bSession = new BSession(CEApplication.DEFAULT_APPLICATION_HERCULE);
        } catch (Exception ex) {
            // On ne fait rien, le module n'existe pas
        }
        setTitle(userSession.getLabel("VG_CE_TITLE"));
        setUrlTitle("");
        // hercule?userAction=hercule.controleEmployeur.listeControleEmployeur.chercher
        boolean hasRightHercule = userSession.hasRight("hercule.controleEmployeur.listeControleEmployeur",
                FWSecureConstants.READ);
        boolean hasRightGestionAttribution = userSession.hasRight("hercule.controleEmployeur.gestionAttributionPts",
                FWSecureConstants.READ);
        try {
            TITiersViewBean pers = new TITiersViewBean();
            pers.setSession(userSession);
            pers.setIdTiers(idTiers);
            pers.retrieve();

            // if (hasRightHercule) {
            if (!JadeStringUtil.isBlankOrZero(pers.getNumAffilieActuel())) {
                CEControleEmployeurManager ceControleEmployeurManager = new CEControleEmployeurManager();
                ceControleEmployeurManager.setSession(userSession);
                ceControleEmployeurManager.setForNumAffilie(pers.getNumAffilieActuel());
                ceControleEmployeurManager.setForActif(true);
                ceControleEmployeurManager.setOrderBy("MDDPRE DESC");
                ceControleEmployeurManager.find();

                String idAttributionPtsActif = null;
                if (ceControleEmployeurManager.getSize() > 0) {
                    if (hasRightHercule) {
                        setUrlTitle("hercule?userAction=hercule.controleEmployeur.listeControleEmployeur.chercher");
                    }
                    CEControleEmployeur ce = (CEControleEmployeur) ceControleEmployeurManager.getEntity(0);

                    String dateProchainControle = "";
                    try {

                        // Rercherche de l'attribution active en fonction du numéro d'affilié
                        idAttributionPtsActif = CEAttributionPtsService.findIdAttributionPtsActifForControle(userSession,
                                pers.getNumAffilieActuel(), ce.getDateDebutControle(), ce.getDateFinControle());

                        boolean isAfficheDateProchainControle = true;

                        if (idAttributionPtsActif != null) {
                            CEGestionAttributionPtsViewBean attributionPtsViewBean = new CEGestionAttributionPtsViewBean();

                            // initialisation nécessaire pour le retrieve
                            attributionPtsViewBean.setSession(userSession);
                            attributionPtsViewBean.setNumAffilie(ce.getNumAffilie());
                            attributionPtsViewBean.setIdAffilie(ce.getAffiliationId());
                            attributionPtsViewBean.setId(idAttributionPtsActif);
                            attributionPtsViewBean.retrieve();

                            // initialisation additionel avant récupération des masses
                            attributionPtsViewBean._getAffilieForAttribution();

                            // si la masse n'est pas supérieur au minimum on cache la date
                            if (!attributionPtsViewBean.isMasseSalarialeSuperieurMin()) {
                                isAfficheDateProchainControle = false;
                            }
                        }

                        if (isAfficheDateProchainControle) {
                            if (JadeStringUtil.isBlankOrZero(ce.getDateEffective())) {
                                int anneeControle = new JADate(ce.getDatePrevue()).getYear();
                                dateProchainControle = ce.getDateDebutControle() + " - " + ce.getDateFinControle() + " / "
                                        + anneeControle;
                            } else {
                                int i_anneeDateDebut = new JADate(ce.getDateFinControle()).getYear() + 1;
                                int i_anneeDateFin = 0;
                                int i_anneeControle = 0;

                                CECouvertureManager ceCouvertureManager = new CECouvertureManager();
                                ceCouvertureManager.setSession(userSession);
                                ceCouvertureManager.setIsActif(true);
                                ceCouvertureManager.setForIdAffilie(ce.getAffiliationId());
                                ceCouvertureManager.find();

                                if (ceCouvertureManager.size() > 0) {
                                    CECouverture couverture = (CECouverture) ceCouvertureManager.getEntity(0);
                                    i_anneeDateFin = Integer.parseInt(couverture.getAnnee());
                                    i_anneeControle = Integer.parseInt(couverture.getAnnee()) + 1;
                                }

                                if ((i_anneeDateFin != 0) && (i_anneeControle != 0)) {
                                    dateProchainControle = "01.01." + i_anneeDateDebut + " - 31.12." + i_anneeDateFin
                                            + " / " + i_anneeControle;
                                } else {
                                    // dateProchainControle = "Erreur recherche prochain contrôle";
                                }
                            }
                        }
                    } catch (Exception e) {
                        dateProchainControle = "Erreur recherche prochain contrôle : " + e.getMessage();
                    }

                    TISummaryInfo[] resTab = new TISummaryInfo[1];
                    StringBuffer s = new StringBuffer();
                    TISummaryInfo content = new TISummaryInfo();

                    s.append("<table>");
                    s.append("<tr>");
                    s.append("<td>");
                    if (!JadeStringUtil.isBlank(dateProchainControle)) {
                        String prochainLibelle = userSession.getLabel("VG_CE_TEXT_PROCHAIN");
                        if (hasRightGestionAttribution && !JadeStringUtil.isBlankOrZero(idAttributionPtsActif)) {
                            prochainLibelle = "<a href='#' onclick=\"callLocalUrl('/hercule?userAction=hercule.controleEmployeur.gestionAttributionPts.afficher&selectedId="
                                    + idAttributionPtsActif + "')\">" + userSession.getLabel("VG_CE_TEXT_PROCHAIN") + "</a>";
                        }
                        s.append(prochainLibelle);
                        s.append(" : ");
                        s.append(dateProchainControle);
                    }
                    s.append("</td>");
                    s.append("</tr>");
                    s.append("</table>");

                    content.setText(s.toString());

                    resTab[0] = content;

                    return resTab;
                }
            }
            if (hasRightHercule) {
                setUrlTitle("hercule?userAction=hercule.controleEmployeur.listeControleEmployeur.chercher");
            }
            // }
        } catch (Exception e) {
            if (hasRightHercule) {
                setUrlTitle("hercule?userAction=hercule.controleEmployeur.listeControleEmployeur.chercher");
            }
            return new TISummaryInfo[] { new TISummaryInfo() };
        }

        return null;
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
        return null;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getUrlTitle() {
        return urlTitle;
    }

    @Override
    public void setElement(String element) {
    }

    @Override
    public void setIcon(String icon) {
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
        this.title = title;

    }

    @Override
    public void setUrlTitle(String urlTitle) {
        this.urlTitle = urlTitle;
    }

}
