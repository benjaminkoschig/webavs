package globaz.corvus.summary;

import globaz.corvus.api.basescalcul.IREPrestationAccordee;
import globaz.corvus.application.REApplication;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemRenteManager;
import globaz.corvus.db.rentesaccordees.RERenteAccJoinTblTiersJoinDemandeRente;
import globaz.corvus.utils.codeprestation.enums.RECodePrestationResolver;
import globaz.framework.secure.FWSecureConstants;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.enums.codeprestation.IPRCodePrestationEnum;
import globaz.prestation.enums.codeprestation.type.PRCodePrestationAPI;
import globaz.pyxis.summary.ITISummarizable;
import globaz.pyxis.summary.TISummaryInfo;

public class RESummary implements ITISummarizable {

    String element = "";
    boolean hasDroitDemandeRente = false;
    boolean hasDroitRenteAccordee = false;
    boolean hasDroitRenteLiee = false;
    String icon = "";
    private PY_VG_MODULE_SIZE moduleSize = PY_VG_MODULE_SIZE.SMALL;
    String titre = "";

    private String urlTitle = "";

    private String buildUrlParameters(RERenteAccJoinTblTiersJoinDemandeRente rente, String label, boolean hasright) {
        StringBuilder paameters = new StringBuilder();
        if (hasright) {
            paameters.append("&noDemandeRente=");
            paameters.append(rente.getNoDemandeRente());
            paameters.append("&idTierRequerant=");
            paameters.append(rente.getIdTierRequerant());
            paameters.append("&idTiersVueGlobale=");
            paameters.append(rente.getIdTierRequerant());
            paameters.append("')\">");
            paameters.append(label);
            paameters.append("</a> : ");
        } else {
            paameters.append(label + " : ");
        }
        return paameters.toString();
    }

    @Override
    public String getElement() {
        return element;
    }

    /**
     * @param codePrestation
     * @return
     */
    private String getGenrePrestation(String codePrestation) {
        String typeDemande = "";
        if (!JadeStringUtil.isEmpty(codePrestation)) {

            try {
                IPRCodePrestationEnum genrePrestation = RECodePrestationResolver
                        .getEnumAssocieAuCodePrestation(codePrestation);
                if (genrePrestation != null) {
                    switch (genrePrestation.getTypeDePrestation()) {
                        case INVALIDITE:
                            typeDemande = "AI";
                            break;
                        case API:
                            typeDemande = "API";
                            break;
                        case SURVIVANT:
                            typeDemande = "SURV";
                            break;
                        case VIEILLESSE:
                            typeDemande = "AVS";
                            break;
                        default:
                            break;
                    }
                }
            } catch (NullPointerException npe) {
                return typeDemande;
            }
        }
        return typeDemande;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public TISummaryInfo[] getInfoForTiers(String idTiers, BSession userSession) throws Exception {
        try {
            BSession bSession = new BSession(REApplication.DEFAULT_APPLICATION_CORVUS);
        } catch (Exception ex) {
            // On ne fait rien, le module n'existe pas
        }
        hasDroitDemandeRente = userSession.hasRight("corvus.demandes.demandeRenteJointPrestationAccordee",
                FWSecureConstants.READ);
        hasDroitRenteLiee = userSession.hasRight("corvus.rentesaccordees.renteLieeJointRenteAccordee",
                FWSecureConstants.READ);
        hasDroitRenteAccordee = userSession.hasRight("corvus.rentesaccordees.renteAccordeeJointDemandeRente",
                FWSecureConstants.READ);

        setTitle(userSession.getLabel("VG_RENTES_TITLE"));
        setUrlTitle("");

        // Recherche des infos
        RERenteAccJoinTblTiersJoinDemandeRente firstRA = null;
        RERenteAccJoinTblTiersJoinDemandeRente secondRA = null;
        RERenteAccJoinTblTiersJoinDemandeRente renteAPI = null;

        StringBuilder codesPrestationAPI = new StringBuilder();
        PRCodePrestationAPI[] codesAPI = PRCodePrestationAPI.values();
        for (int ctr = 0; ctr < codesAPI.length; ctr++) {
            codesPrestationAPI.append("'");
            codesPrestationAPI.append(codesAPI[ctr].getCodePrestation());
            codesPrestationAPI.append("'");
            if ((ctr + 1) < (codesAPI.length)) {
                codesPrestationAPI.append(", ");
            }
        }

        RERenteAccJoinTblTiersJoinDemRenteManager manager = new RERenteAccJoinTblTiersJoinDemRenteManager();
        manager.setSession(userSession);
        manager.setEscapeString(false);
        manager.setForGenrePrestationNotIn(codesPrestationAPI.toString());
        manager.setForIdTiersBeneficiaire(idTiers);
        manager.setForCsEtatDifferentDe(IREPrestationAccordee.CS_ETAT_CALCULE);
        manager.setOrderBy(" CASE " + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " WHEN 0 THEN 999999 ELSE "
                + REPrestationsAccordees.FIELDNAME_DATE_FIN_DROIT + " END DESC");
        manager.find();

        if (manager.size() > 0) {
            firstRA = (RERenteAccJoinTblTiersJoinDemandeRente) manager.getFirstEntity();
            if ((manager.size() > 1)
                    && firstRA.getDateFinDroit().equals(
                            ((RERenteAccJoinTblTiersJoinDemandeRente) manager.getEntity(1)).getDateFinDroit())) {

                secondRA = (RERenteAccJoinTblTiersJoinDemandeRente) manager.getEntity(1);
            }
        }
        // Recherche d'une rente API
        manager.setForGenrePrestationNotIn(null);
        manager.setForGenrePrestationIn(codesPrestationAPI.toString());
        manager.find();
        if (manager.getContainer().size() > 0) {
            renteAPI = (RERenteAccJoinTblTiersJoinDemandeRente) manager.get(0);
        }

        return handleRenteAcc(firstRA, secondRA, renteAPI, userSession);
    }

    /**
     * @param firstRA
     * @param secondRA
     * @param renteAPI
     * @param textGenre
     */
    private String getLigneGenre(BSession session, RERenteAccJoinTblTiersJoinDemandeRente firstRA,
            RERenteAccJoinTblTiersJoinDemandeRente secondRA, RERenteAccJoinTblTiersJoinDemandeRente renteAPI) {
        StringBuilder textGenre = new StringBuilder();
        if ((firstRA != null) || (renteAPI != null)) {
            String labelGenre = session.getLabel("JSP_SUMMARY_GENRE");
            if (hasDroitRenteAccordee) {
                textGenre
                        .append("<a href='#' onclick=\"callLocalUrl('/corvus?userAction=corvus.rentesaccordees.renteAccordeeJointDemandeRente.chercher");
            }

            if (firstRA != null) {
                textGenre.append(buildUrlParameters(firstRA, labelGenre, hasDroitRenteAccordee));
            } else if (renteAPI != null) {
                textGenre.append(buildUrlParameters(renteAPI, labelGenre, hasDroitRenteAccordee));
            }

            boolean doAddVirgule = false;
            if (firstRA != null) {
                if ((firstRA != null) && !JadeStringUtil.isEmpty(firstRA.getCodePrestation())) {
                    doAddVirgule = true;
                    String genrePrestation = getGenrePrestation(firstRA.getCodePrestation());
                    if (!JadeStringUtil.isEmpty(genrePrestation)) {
                        textGenre.append(genrePrestation);
                        textGenre.append(" - ");
                    }
                    textGenre.append(firstRA.getCodePrestation());

                    if ((secondRA != null) && !JadeStringUtil.isEmpty(secondRA.getCodePrestation())) {
                        String genrePrestation2 = getGenrePrestation(secondRA.getCodePrestation());
                        // Si c'est le même genre de prestation, on met une virgule et le code prestation
                        if (!JadeStringUtil.isEmpty(genrePrestation) && genrePrestation.equals(genrePrestation2)) {
                            // TODO vérifier le comportement voulu

                            textGenre.append(", " + secondRA.getCodePrestation());
                        } else {
                            textGenre.append(", " + genrePrestation2 + " - " + secondRA.getCodePrestation());
                        }
                    }
                }
            }
            if ((renteAPI != null) && !JadeStringUtil.isEmpty(renteAPI.getCodePrestation())) {
                if (doAddVirgule) {
                    textGenre.append(", ");
                }
                textGenre.append("API - " + renteAPI.getCodePrestation());
            }
        }
        return textGenre.toString();
    }

    /**
     * @param firstRA
     * @return
     */
    private String getLignePeriode(BSession session, RERenteAccJoinTblTiersJoinDemandeRente firstRA,
            RERenteAccJoinTblTiersJoinDemandeRente renteAPI) {
        StringBuilder lignePeriode = new StringBuilder();
        if ((firstRA != null) || (renteAPI != null)) {
            String labelPeriode = session.getLabel("JSP_SUMMARY_PERIODE");
            if (hasDroitRenteLiee) {
                lignePeriode
                        .append("<a href='#' onclick=\"callLocalUrl('/corvus?userAction=corvus.rentesaccordees.renteLieeJointRenteAccordee.chercher");
            }
            if (firstRA != null) {

                lignePeriode.append(buildUrlParameters(firstRA, labelPeriode, hasDroitRenteLiee));
                lignePeriode.append(firstRA.getDateDebutDroit());
                lignePeriode.append(" - ");
                lignePeriode.append(firstRA.getDateFinDroit());
            } else if (renteAPI != null) {
                lignePeriode.append(buildUrlParameters(renteAPI, labelPeriode, hasDroitRenteLiee));
                lignePeriode.append(renteAPI.getDateDebutDroit());
                lignePeriode.append(" - ");
                lignePeriode.append(renteAPI.getDateFinDroit());
            }
        }
        return lignePeriode.toString();
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

    /**
     * @param firstRA
     * @param renteAPI
     * @return
     */
    private String getUrl(RERenteAccJoinTblTiersJoinDemandeRente firstRA,
            RERenteAccJoinTblTiersJoinDemandeRente renteAPI) {
        String url = "";
        if (firstRA != null) {
            url = "/corvus?userAction=corvus.rentesaccordees.renteAccordeeJointDemandeRente.afficher&selectedId="
                    + firstRA.getIdPrestationAccordee() + "&idTierRequerant=" + firstRA.getIdTiersBeneficiaire();

        } else if (renteAPI != null) {
            url = "/corvus?userAction=corvus.rentesaccordees.renteAccordeeJointDemandeRente.afficher&selectedId="
                    + renteAPI.getIdPrestationAccordee() + "&idTierRequerant=" + renteAPI.getIdTiersBeneficiaire();
        }
        return url;
    }

    @Override
    public String getUrlTitle() {
        return urlTitle;
    }

    public TISummaryInfo[] handleRenteAcc(RERenteAccJoinTblTiersJoinDemandeRente firstRA,
            RERenteAccJoinTblTiersJoinDemandeRente secondRA, RERenteAccJoinTblTiersJoinDemandeRente renteAPI,
            BSession userSession) throws Exception {

        StringBuffer title = new StringBuffer();
        TISummaryInfo res = new TISummaryInfo();
        TISummaryInfo[] resTab = new TISummaryInfo[1];

        title.append("<table>");

        // Période
        title.append("<tr><td>");
        title.append(getLignePeriode(userSession, firstRA, renteAPI));
        title.append("</tr></td>");

        // Genre
        title.append("<tr><td>");
        title.append(getLigneGenre(userSession, firstRA, secondRA, renteAPI));
        title.append("</tr></td>");

        title.append("</table>");
        res.setText(title.toString());

        res.setUrl(getUrl(firstRA, renteAPI), "corvus.rentesaccordees.renteAccordeeJointDemandeRente",
                FWSecureConstants.READ);

        String idTiersRequerant = null;
        if (firstRA != null) {
            idTiersRequerant = firstRA.getIdTierRequerant();
        } else if (renteAPI != null) {
            idTiersRequerant = renteAPI.getIdTierRequerant();
        }
        String urlTitle = "corvus?userAction=corvus.demandes.demandeRenteJointPrestationAccordee.chercher";
        if (!JadeStringUtil.isEmpty(idTiersRequerant)) {
            urlTitle += "&idTierRequerant=" + idTiersRequerant;
        }
        if (hasDroitDemandeRente) {
            setUrlTitle(urlTitle);
        }
        resTab[0] = res;
        return resTab;
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
