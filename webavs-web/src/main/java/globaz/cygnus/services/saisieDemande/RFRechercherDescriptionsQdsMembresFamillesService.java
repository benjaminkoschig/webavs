package globaz.cygnus.services.saisieDemande;

import globaz.cygnus.api.qds.IRFQd;
import globaz.cygnus.db.qds.RFAssQdDossierJointDossierJointTiers;
import globaz.cygnus.db.qds.RFAssQdDossierJointDossierJointTiersManager;
import globaz.cygnus.db.qds.RFQdAssureJointDossierJointTiers;
import globaz.cygnus.db.qds.RFQdAssureJointDossierJointTiersManager;
import globaz.cygnus.db.qds.RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande;
import globaz.cygnus.db.qds.RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager;
import globaz.cygnus.services.demande.RFDemandeService;
import globaz.cygnus.utils.RFUtils;
import globaz.framework.util.FWCurrency;
import globaz.globall.api.BISession;
import globaz.globall.api.BITransaction;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.tools.PRDateFormater;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import ch.globaz.pegasus.business.constantes.IPCDroits;

public class RFRechercherDescriptionsQdsMembresFamillesService {

    public static final String ACTION_DOSSIER_JOINT_TIERS = "cygnus.dossiers.dossierJointTiers";

    private RFDescriptionsQdsMembresFamillesData buildDescQdInnerHtml(
            RFDescriptionsQdsMembresFamillesData descriptionData,
            RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager rfPerValQdPriMgr,
            String etatFormulaireDemande, String forDateDebutBetweenPeriode, String idTiersRequerant,
            String idAdressePaiement, String codeTypeDeSoinList, String codeSousTypeDeSoinList, BISession session)
            throws Exception {

        StringBuffer mntResiduelQdHtmlBuffer = new StringBuffer();
        String idQdPrincipal = "";

        if (rfPerValQdPriMgr.size() > 0) {

            RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande rfDernierePeriodeValiditeQdPrincipale = (RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande) rfPerValQdPriMgr
                    .getFirstEntity();

            if (null != rfDernierePeriodeValiditeQdPrincipale) {

                descriptionData.setIdQdsPrincipale(rfDernierePeriodeValiditeQdPrincipale.getIdQdPrincipale());

                String mntSoldeExcedent = RFUtils.getSoldeExcedentDeRevenu(
                        rfDernierePeriodeValiditeQdPrincipale.getIdQd(), (BSession) session);

                if (rfDernierePeriodeValiditeQdPrincipale.getIsPlafonnee()) {

                    String mntResiduel = RFUtils.getMntResiduel(rfDernierePeriodeValiditeQdPrincipale
                            .getLimiteAnnuelle(), RFUtils.getAugmentationQd(
                            rfDernierePeriodeValiditeQdPrincipale.getIdQd(), (BSession) session), RFUtils
                            .getSoldeDeCharge(rfDernierePeriodeValiditeQdPrincipale.getIdQd(), (BSession) session),
                            rfDernierePeriodeValiditeQdPrincipale.getMontantChargeRfm());

                    mntResiduelQdHtmlBuffer.append(getDescQdPrincipalePlafonneeHtml(
                            rfDernierePeriodeValiditeQdPrincipale.getIdQdPrincipale(),
                            rfDernierePeriodeValiditeQdPrincipale.getLimiteAnnuelle(), mntResiduel, mntSoldeExcedent,
                            rfPerValQdPriMgr.size(), rfDernierePeriodeValiditeQdPrincipale.getDateDebutPeriode(),
                            rfDernierePeriodeValiditeQdPrincipale.getDateFinPeriode(), (BSession) session));
                } else {

                    mntResiduelQdHtmlBuffer.append(getDescQdPrincipaleNonPlafonneeHtml(
                            rfDernierePeriodeValiditeQdPrincipale.getIdQdPrincipale(), mntSoldeExcedent,
                            rfPerValQdPriMgr.size(), rfDernierePeriodeValiditeQdPrincipale.getDateDebutPeriode(),
                            rfDernierePeriodeValiditeQdPrincipale.getDateFinPeriode(), (BSession) session));

                }

                idQdPrincipal = rfDernierePeriodeValiditeQdPrincipale.getIdQdPrincipale();
            }

        } else {
            mntResiduelQdHtmlBuffer.append(getDescQdPrincipaleNonCreeHtml((BSession) session));
        }

        // Recherche de la petite Qd
        if (!JadeStringUtil.isBlankOrZero(idQdPrincipal)) {

            RFQdAssureJointDossierJointTiersManager rfQdAssureJointPotTypeMgr = RFUtils
                    .getRFQdAssureJointDossierJointTiersManager((BSession) session, null, idTiersRequerant,
                            codeTypeDeSoinList, codeSousTypeDeSoinList, "",
                            PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(forDateDebutBetweenPeriode), "", "",
                            forDateDebutBetweenPeriode, true, "", "");

            if (rfQdAssureJointPotTypeMgr.size() == 1) {

                RFQdAssureJointDossierJointTiers rfQdAssureJointPotType = (RFQdAssureJointDossierJointTiers) rfQdAssureJointPotTypeMgr
                        .getFirstEntity();

                if (null != rfQdAssureJointPotType) {
                    if (rfQdAssureJointPotType.getIsPlafonnee()) {

                        String mntResiduel = RFUtils.getMntResiduel(rfQdAssureJointPotType.getLimiteAnnuelle(),
                                RFUtils.getAugmentationQd(rfQdAssureJointPotType.getIdQd(), (BSession) session),
                                RFUtils.getSoldeDeCharge(rfQdAssureJointPotType.getIdQd(), (BSession) session),
                                rfQdAssureJointPotType.getMontantChargeRfm());

                        mntResiduelQdHtmlBuffer.append(getDescQdAssurePlafonneeHtml(
                                rfQdAssureJointPotType.getIdQdAssure(), rfQdAssureJointPotType.getLimiteAnnuelle(),
                                mntResiduel, rfQdAssureJointPotType.getDateDebut(),
                                rfQdAssureJointPotType.getDateFin(), (BSession) session));
                    } else {

                        mntResiduelQdHtmlBuffer.append(getDescQdAssureNonPlafonneeHtml(
                                rfQdAssureJointPotType.getIdQdAssure(), rfQdAssureJointPotType.getLimiteAnnuelle(),
                                rfQdAssureJointPotType.getDateDebut(), rfQdAssureJointPotType.getDateFin(),
                                (BSession) session));
                    }
                }
            }
        }

        String[] adresseIdTier = RFDemandeService.getDescAdressePaiementWithIdTiers(etatFormulaireDemande,
                idTiersRequerant, idAdressePaiement, idQdPrincipal, (BSession) session);

        descriptionData.setAdressePaiement(adresseIdTier[0]);
        descriptionData.setIdTiers(adresseIdTier[1]);

        descriptionData.setDescriptionQd(mntResiduelQdHtmlBuffer.toString());

        return descriptionData;
    }

    private String getDescMembresFamilleHtml(List<String[]> membresFamilleCcList, String idTiers,
            String servletContext, String mainServletPath, BSession session) {

        StringBuffer descMembresFamilleStrBuffer = new StringBuffer();

        int i = 0;
        for (String[] membreCourant : membresFamilleCcList) {

            descMembresFamilleStrBuffer.append("<TR>");
            descMembresFamilleStrBuffer.append("<TD style=\"border-right=1px solid #C0C0C0\">");
            descMembresFamilleStrBuffer.append("<INPUT type=\"radio\" name=\"membreFamille\" id=\"membreId_" + i
                    + "\" ");
            descMembresFamilleStrBuffer.append("value=\"" + membreCourant[0]
                    + "\" onClick=\"demandeUtils.onClickMembreFamille()\" ");
            descMembresFamilleStrBuffer.append(membreCourant[0].equals(idTiers) ? "CHECKED" : "");
            descMembresFamilleStrBuffer.append("/></TD>");
            descMembresFamilleStrBuffer.append("<TD style=\"border-right=1px solid #C0C0C0\"><i>");

            // Type relation
            descMembresFamilleStrBuffer.append(session.getCodeLibelle(membreCourant[1]) + "</i></TD>");

            // NSS
            descMembresFamilleStrBuffer.append("<TD style=\"border-right=1px solid #C0C0C0\"><b>" + membreCourant[2]
                    + "</b></TD>");

            // Nom
            descMembresFamilleStrBuffer.append("<TD style=\"border-right=1px solid #C0C0C0\">" + membreCourant[3]);

            // Prénom
            descMembresFamilleStrBuffer.append(" / " + membreCourant[4]);

            // Date naissance
            descMembresFamilleStrBuffer.append(" / " + membreCourant[5]);

            // Sexe
            descMembresFamilleStrBuffer.append(" / " + RFUtils.getLibelleCourtSexe(membreCourant[6]));

            // Nationalité
            descMembresFamilleStrBuffer.append(" / " + RFUtils.getLibellePays(membreCourant[7], session) + "</TD>");

            descMembresFamilleStrBuffer.append("<TD style=\"border-right=1px solid #C0C0C0\">");
            /*
             * descMembresFamilleStrBuffer.append("<A href=\"" + servletContext + mainServletPath + "?userAction=");
             * descMembresFamilleStrBuffer
             * .append(RFRechercherDescriptionsQdsMembresFamillesService.ACTION_DOSSIER_JOINT_TIERS);
             * descMembresFamilleStrBuffer.append(".chercher&likeNumeroAVS=" + membreCourant[2].substring(4,
             * membreCourant[2].length() - 1) + "\">");
             * descMembresFamilleStrBuffer.append(session.getLabel("JSP_RF_DEM_S_REF_DOSSIER"));
             */
            descMembresFamilleStrBuffer.append("</TD></TR>");
            // <ct:FWLabel key=\"JSP_RF_DEM_S_REF_DOSSIER\"/>

            i++;
        }

        return descMembresFamilleStrBuffer.toString();

    }

    // private void setDescQdInnerHtml(RFSaisieDemandeViewBean vb) {
    //
    // try {
    //
    // // Recherche de la dernière période de validités
    // RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager rfPerValQdPriMgr = new
    // RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager();
    //
    // rfPerValQdPriMgr.setSession(vb.getSession());
    // rfPerValQdPriMgr.setForCsGenreQd(IRFQd.CS_GRANDE_QD);
    // rfPerValQdPriMgr.setForIdTiers(vb.getIdTiers());
    // rfPerValQdPriMgr.setComprisDansCalcul(true);
    // rfPerValQdPriMgr.setForAnneeQd(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(JACalendar.todayJJsMMsAAAA()));
    // rfPerValQdPriMgr.setForDateDebutBetweenPeriode(JACalendar.todayJJsMMsAAAA());
    // rfPerValQdPriMgr.changeManagerSize(0);
    // rfPerValQdPriMgr.find();
    //
    // vb.setDescQdInnerHtml(this.buildDescQdInnerHtml(rfPerValQdPriMgr, vb, JACalendar.todayJJsMMsAAAA()));
    //
    // } catch (Exception e) {
    // RFUtils.setMsgExceptionErreurViewBean(vb, e.getMessage());
    // }
    // }

    private String getDescQdAssureNonPlafonneeHtml(String idQdAssure, String limiteAnnuelle, String dateDebut,
            String dateFin, BSession session) {

        StringBuffer mntResiduelQdHtmlBuffer = new StringBuffer();

        mntResiduelQdHtmlBuffer.append("<TR><TD>&nbsp;</TD></TR><TR><TD>"
                + session.getLabel("JSP_RF_QD_S_ETAT_PETITE_QD") + "</TD>" + "<TD colspan=\"5\"><i>"
                + session.getLabel("JSP_RF_QD_S_NO_QD") + "&nbsp;<b>" + idQdAssure + "</b>&nbsp;"
                + session.getLabel("JSP_RF_QD_S_LIMITE_ANNUELLE") + "&nbsp;<b>" + " - " + "</b>&nbsp;"
                + session.getLabel("JSP_RF_QD_S_QD_RESIDUELLE") + "&nbsp;<b>" + " - " + "</b>&nbsp;"
                + session.getLabel("JSP_RF_QD_S_DATE_DEBUT") + "&nbsp;<b>" + dateDebut + "</b>&nbsp;"
                + session.getLabel("JSP_RF_QD_S_DATE_FIN") + "&nbsp;<b>"
                + (JadeStringUtil.isBlankOrZero(dateFin) ? " - " : dateFin) + "</b></i></TD></TR>");

        return mntResiduelQdHtmlBuffer.toString();

    }

    private String getDescQdAssurePlafonneeHtml(String idQdAssure, String limiteAnnuelle, String mntResiduel,
            String dateDebut, String dateFin, BSession session) {

        StringBuffer mntResiduelQdHtmlBuffer = new StringBuffer();

        mntResiduelQdHtmlBuffer.append("<TR><TD>&nbsp;</TD></TR><TR><TD>"
                + session.getLabel("JSP_RF_QD_S_ETAT_PETITE_QD") + "</TD>" + "<TD colspan=\"5\"><i>"
                + session.getLabel("JSP_RF_QD_S_NO_QD") + "&nbsp;<b>" + idQdAssure + "</b>&nbsp;"
                + session.getLabel("JSP_RF_QD_S_LIMITE_ANNUELLE") + "&nbsp;<b>"
                + new FWCurrency(limiteAnnuelle).toStringFormat() + "</b>&nbsp;"
                + session.getLabel("JSP_RF_QD_S_QD_RESIDUELLE") + "&nbsp;<b>"
                + new FWCurrency(mntResiduel).toStringFormat() + "</b>&nbsp;"
                + session.getLabel("JSP_RF_QD_S_DATE_DEBUT") + "&nbsp;<b>" + dateDebut + "</b>&nbsp;"
                + session.getLabel("JSP_RF_QD_S_DATE_FIN") + "&nbsp;<b>"
                + (JadeStringUtil.isBlankOrZero(dateFin) ? " - " : dateFin) + "</b></i></TD></TR>");

        return mntResiduelQdHtmlBuffer.toString();
    }

    private String getDescQdPrincipaleNonCreeHtml(BSession session) {
        return "<TD colspan=\"5\"><b>" + session.getLabel("JSP_RF_DEM_S_QD_NON_CREEE") + "</b><TD>";
    }

    private String getDescQdPrincipaleNonPlafonneeHtml(String idQdPrincipale, String mntSoldeExcedent,
            int rfPerValQdPriMgrSize, String dateDebutPeriode, String dateFinPeriode, BSession session) {

        StringBuffer mntResiduelQdHtmlBuffer = new StringBuffer();

        mntResiduelQdHtmlBuffer.append("<TR><TD>" + session.getLabel("JSP_RF_QD_S_ETAT_GRANDE_QD") + "</TD>"
                + "<TD colspan=\"5\"><i>" + session.getLabel("JSP_RF_QD_S_NO_QD") + "<b>&nbsp;" + idQdPrincipale
                + "</b>&nbsp;" + session.getLabel("JSP_RF_QD_S_LIMITE_ANNUELLE") + "&nbsp;<b>" + " - " + "&nbsp;</b>"
                + session.getLabel("JSP_RF_QD_S_QD_RESIDUELLE") + "<b>&nbsp;" + " - " + "</b></i>");

        if (JadeStringUtil.isBlankOrZero(mntSoldeExcedent)) {
            mntResiduelQdHtmlBuffer.append("</i>");
        } else {
            mntResiduelQdHtmlBuffer.append("&nbsp;" + session.getLabel("JSP_RF_QD_S_SOLDE_EX_REV") + "&nbsp;<b>"
                    + "<font color='red'>" + new FWCurrency(mntSoldeExcedent).toStringFormat() + "</font></b></i>");
        }

        if (rfPerValQdPriMgrSize == 1) {
            mntResiduelQdHtmlBuffer.append("&nbsp;<i>" + session.getLabel("JSP_RF_QD_S_DATE_DEBUT") + "&nbsp;<b>"
                    + dateDebutPeriode + "</b>&nbsp;" + session.getLabel("JSP_RF_QD_S_DATE_FIN") + "&nbsp;<b>"
                    + (JadeStringUtil.isBlankOrZero(dateFinPeriode) ? " - " : dateFinPeriode) + "</b></i>");
        }

        mntResiduelQdHtmlBuffer.append("</TD></TR>");

        return mntResiduelQdHtmlBuffer.toString();

    }

    private String getDescQdPrincipalePlafonneeHtml(String idQdPrincipale, String limiteAnnuelle, String mntResiduel,
            String mntSoldeExcedent, int rfPerValQdPriMgrSize, String dateDebutPeriode, String dateFinPeriode,
            BSession session) {

        StringBuffer mntResiduelQdHtmlBuffer = new StringBuffer();

        mntResiduelQdHtmlBuffer.append("<TR><TD>" + session.getLabel("JSP_RF_QD_S_ETAT_GRANDE_QD") + "</TD>"
                + "<TD colspan=\"5\"><i>" + session.getLabel("JSP_RF_QD_S_NO_QD") + "&nbsp;<b>" + idQdPrincipale
                + "</b>&nbsp;" + session.getLabel("JSP_RF_QD_S_LIMITE_ANNUELLE") + "&nbsp;<b>"
                + new FWCurrency(limiteAnnuelle).toStringFormat() + "</b>&nbsp;"
                + session.getLabel("JSP_RF_QD_S_QD_RESIDUELLE") + "&nbsp;<b>"
                + new FWCurrency(mntResiduel).toStringFormat() + "</b>");

        if (JadeStringUtil.isBlankOrZero(mntSoldeExcedent)) {
            mntResiduelQdHtmlBuffer.append("</i>");
        } else {
            mntResiduelQdHtmlBuffer.append("&nbsp;" + session.getLabel("JSP_RF_QD_S_SOLDE_EX_REV") + "&nbsp;<b>"
                    + "<font color='red'>" + new FWCurrency(mntSoldeExcedent).toStringFormat() + "</font></b></i>");
        }

        if (rfPerValQdPriMgrSize == 1) {
            mntResiduelQdHtmlBuffer.append("&nbsp;<i>" + session.getLabel("JSP_RF_QD_S_DATE_DEBUT") + "&nbsp;<b>"
                    + dateDebutPeriode + "</b>&nbsp;" + session.getLabel("JSP_RF_QD_S_DATE_FIN") + "&nbsp;<b>"
                    + (JadeStringUtil.isBlankOrZero(dateFinPeriode) ? " - " : dateFinPeriode) + "</b></i>");
        }

        mntResiduelQdHtmlBuffer.append("</TD></TR>");

        return mntResiduelQdHtmlBuffer.toString();
    }

    /**
     * Méthode remontant les membres famille du tiers bénéficiaire
     * 
     * @param FWViewBeanInterface
     *            , BSession
     * @throws Exception
     */
    public RFDescriptionsQdsMembresFamillesData rechercher(String idTiersRequerant, String dateFactureDemande,
            String dateDebutTraitementDemande, String idQdPrincipale, String codeTypeDeSoinList,
            String codeSousTypeDeSoinList, String servletContext, String mainservletPath, String idAdressePaiement,
            String etatFormulaireDemande) throws Exception {

        BISession session = BSessionUtil.getSessionFromThreadContext();

        RFDescriptionsQdsMembresFamillesData descriptionData = null;

        // Recherche des membres familles compris dans le calcul
        BITransaction transaction = null;
        try {
            transaction = ((BSession) session).newTransaction();
            transaction.openTransaction();

            RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager rfPerValQdPriMgr = new RFQdJointPeriodeValiditeJointDossierJointTiersJointDemandeManager();

            rfPerValQdPriMgr.setSession((BSession) session);
            rfPerValQdPriMgr.setForCsGenreQd(IRFQd.CS_GRANDE_QD);
            rfPerValQdPriMgr.setForIdTiers(idTiersRequerant);
            rfPerValQdPriMgr.setComprisDansCalcul(true);

            String dateFacture = "";

            if (JadeStringUtil.isBlankOrZero(dateDebutTraitementDemande)) {
                if (JadeStringUtil.isBlankOrZero(dateFactureDemande)) {
                    dateFacture = JACalendar.todayJJsMMsAAAA();
                } else {
                    dateFacture = dateFactureDemande;
                }
            } else {

                if (dateDebutTraitementDemande.length() == 7) {
                    dateDebutTraitementDemande = "01." + dateDebutTraitementDemande;
                }

                dateFacture = dateDebutTraitementDemande;
            }

            if (((BSession) session).getApplication().getCalendar().isValid(dateFacture)) {

                boolean idQdPrincipaleDefini = JadeStringUtil.isBlankOrZero(idQdPrincipale);

                if (!idQdPrincipaleDefini || (etatFormulaireDemande.equals("add") && idQdPrincipaleDefini)) {
                    rfPerValQdPriMgr.setForAnneeQd(PRDateFormater.convertDate_JJxMMxAAAA_to_AAAA(dateFacture));
                    rfPerValQdPriMgr.setForDateDebutBetweenPeriode(dateFacture);
                } else {
                    rfPerValQdPriMgr.setForIdQd(idQdPrincipale);
                }
                rfPerValQdPriMgr.changeManagerSize(0);
                rfPerValQdPriMgr.find();

                descriptionData = new RFDescriptionsQdsMembresFamillesData();

                descriptionData = buildDescQdInnerHtml(descriptionData, rfPerValQdPriMgr, etatFormulaireDemande,
                        dateFacture, idTiersRequerant, idAdressePaiement, codeTypeDeSoinList, codeSousTypeDeSoinList,
                        session);

                if (rfPerValQdPriMgr.size() > 0) {

                    RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande rfDernierePeriodeValiditeQdPrincipale = (RFQdJointPeriodeValiditeJointDossierJointTiersJointDemande) rfPerValQdPriMgr
                            .getFirstEntity();

                    if (null != rfDernierePeriodeValiditeQdPrincipale) {

                        RFAssQdDossierJointDossierJointTiersManager rfAssQdDossierJointDossierJointTiersMgr = new RFAssQdDossierJointDossierJointTiersManager();
                        rfAssQdDossierJointDossierJointTiersMgr.setSession((BSession) session);
                        rfAssQdDossierJointDossierJointTiersMgr.setForIdQd(rfDernierePeriodeValiditeQdPrincipale
                                .getIdQdPrincipale());
                        rfAssQdDossierJointDossierJointTiersMgr.changeManagerSize(0);
                        rfAssQdDossierJointDossierJointTiersMgr.find();

                        if (rfAssQdDossierJointDossierJointTiersMgr.size() > 0) {

                            Iterator<RFAssQdDossierJointDossierJointTiers> rfAssQdDossierJointDossierJointTiersIter = rfAssQdDossierJointDossierJointTiersMgr
                                    .iterator();
                            List<String[]> membresFamList = new Vector<String[]>();
                            while (rfAssQdDossierJointDossierJointTiersIter.hasNext()) {

                                RFAssQdDossierJointDossierJointTiers rfAssQdDossierJointDossierJointTiers = rfAssQdDossierJointDossierJointTiersIter
                                        .next();

                                if (null != rfAssQdDossierJointDossierJointTiers) {

                                    if (rfAssQdDossierJointDossierJointTiers.getIsComprisDansCalcul().booleanValue()) {
                                        membresFamList.add(RFUtils.getMembreFamilleTabString(
                                                rfAssQdDossierJointDossierJointTiers.getIdTiers(),
                                                rfAssQdDossierJointDossierJointTiers.getTypeRelation(),
                                                rfAssQdDossierJointDossierJointTiers.getNss(),
                                                rfAssQdDossierJointDossierJointTiers.getNom(),
                                                rfAssQdDossierJointDossierJointTiers.getPrenom(),
                                                rfAssQdDossierJointDossierJointTiers.getDateNaissance(),
                                                rfAssQdDossierJointDossierJointTiers.getCsSexe(),
                                                rfAssQdDossierJointDossierJointTiers.getCsNationalite(),
                                                rfAssQdDossierJointDossierJointTiers.getIsComprisDansCalcul()));
                                    }
                                }
                            }

                            descriptionData.setMembresFamilleCC(getDescMembresFamilleHtml(membresFamList,
                                    idTiersRequerant, servletContext, mainservletPath, (BSession) session));
                        }

                    } else {
                        throw new Exception(
                                "RFDescriptionsQdsMembresFamillesData.rechercher(): Association Qd dossier non trouvée");
                    }

                } else {
                    if (rfPerValQdPriMgr.size() == 0) {
                        List<String[]> membresFamilleList = RFUtils.getMembreFamille((BTransaction) transaction,
                                idTiersRequerant, dateFacture, false, (BSession) session);

                        if (membresFamilleList.size() == 0) {
                            // Ajout du tiers principal
                            PRTiersWrapper prTieWrap = PRTiersHelper.getTiersParId(session, idTiersRequerant);

                            if (null != prTieWrap) {
                                membresFamilleList.add(RFUtils.getMembreFamilleTabString(idTiersRequerant,
                                        IPCDroits.CS_ROLE_FAMILLE_REQUERANT,
                                        prTieWrap.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
                                        prTieWrap.getProperty(PRTiersWrapper.PROPERTY_NOM),
                                        prTieWrap.getProperty(PRTiersWrapper.PROPERTY_PRENOM),
                                        prTieWrap.getProperty(PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
                                        prTieWrap.getProperty(PRTiersWrapper.PROPERTY_SEXE),
                                        prTieWrap.getProperty(PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE), Boolean.TRUE));
                            } else {
                                throw new Exception("Impossible de retrouver le tiers");
                            }
                        }

                        descriptionData.setMembresFamilleCC(getDescMembresFamilleHtml(membresFamilleList,
                                idTiersRequerant, servletContext, mainservletPath, (BSession) session));

                    } else {
                        throw new Exception(((BSession) session).getLabel("Plusieurs grandes Qds trouvées"));
                    }
                }
            } else {
                throw new Exception(((BSession) session).getLabel("ERREUR_RF_DEM_S_DATE_FAMILLE_MAUVAIS_FORMAT"));
            }

        } catch (Exception e) {
            throw new Exception("RFDescriptionsQdsMembresFamillesData.rechercher(): " + e.getMessage());
        } finally {
            transaction.closeTransaction();
        }

        return descriptionData;
    }
}
