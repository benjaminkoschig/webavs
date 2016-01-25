package globaz.lynx.parser;

import globaz.globall.db.BSession;
import globaz.globall.util.JACalendar;
import globaz.helios.db.comptes.CGExerciceComptable;
import globaz.helios.db.comptes.CGMandat;
import globaz.helios.db.comptes.CGMandatManager;
import globaz.helios.parser.CGAutoComplete;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.lynx.db.canevas.LXCanevas;
import globaz.lynx.db.canevas.LXCanevasManager;
import globaz.lynx.db.facture.LXLectureOptique;
import globaz.lynx.db.facture.LXLectureOptiqueManager;
import globaz.lynx.db.fournisseur.LXFournisseur;
import globaz.lynx.db.fournisseur.LXFournisseurManager;
import globaz.lynx.db.informationcomptable.LXInformationComptableListViewBean;
import globaz.lynx.db.informationcomptable.LXInformationComptableViewBean;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.organeexecution.LXOrganeExecution;
import globaz.lynx.db.organeexecution.LXOrganeExecutionManager;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.db.section.LXSectionManager;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitrice;
import globaz.lynx.db.societesdebitrice.LXSocieteDebitriceManager;
import globaz.lynx.service.helios.LXHeliosService;
import globaz.lynx.service.tiers.LXTiersService;
import globaz.lynx.utils.LXOutilsBVR;
import globaz.lynx.utils.LXUtils;
import globaz.pyxis.adresse.datasource.TIAbstractAdressePaiementDataSource;
import globaz.pyxis.adresse.datasource.TIAdresseDataSource;
import globaz.pyxis.adresse.datasource.TIAdressePaiementDataSource;
import globaz.pyxis.adresse.formater.TIAdresseFormater;
import java.util.ArrayList;

public class LXAutoComplete {

    /**
     * Permet de récupérér les infos d'un canevas pour la création d'une facture
     * 
     * @param session
     *            La session
     * @param like
     *            Le libellé d'un canevas
     * @return
     */
    public static String getCanevas(BSession session, String like, String forIdSociete) {
        StringBuffer select = new StringBuffer("<select size=\"5\" style=\"width:16cm\">");

        try {

            LXCanevasManager manager = new LXCanevasManager();
            manager.setSession(session);
            manager.setForIdSociete(forIdSociete);
            manager.setLikeLibelle(like);
            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                LXCanevas canevas = (LXCanevas) manager.getEntity(i);

                select.append("<option idFournisseur=\"").append(canevas.getIdFournisseur()).append("\"");
                select.append(" libelleFournisseur=\"").append(canevas.getNomFournisseur()).append("\"");
                select.append(" idOperationCanevas=\"").append(canevas.getIdOperationCanevas()).append("\"");
                select.append(" idSectionCanevas=\"").append(canevas.getIdSectionCanevas()).append("\"");
                select.append(" montant=\"").append(canevas.getMontant()).append("\"");
                select.append(" libelle=\"").append(canevas.getLibelle()).append("\"");
                select.append(" value=\"").append(canevas.getLibelle()).append("\"");
                select.append(" idExterne=\"").append(canevas.getIdExterne()).append("\">");

                select.append(canevas.getLibelle()).append(" - (").append(canevas.getIdExterneFournisseur())
                        .append(" - ").append(canevas.getNomFournisseur()).append(")");

                select.append("</option>");
            }

            select.append("</select>");

        } catch (Exception e) {
            JadeLogger.error(LXAutoComplete.class.getName() + "_getSociete()", e.toString());

            select = new StringBuffer("<select size=\"2\" style=\"width:8cm; color:red; \"><option>");
            select.append(session.getLabel("ERREUR_TECHNIQUE"));
            select.append("</option><option>&nbsp;</option></select>");
            return select.toString();
        }

        return select.toString();
    }

    /**
     * Return le select nécessaire à la sélection du compte.
     * 
     * @param session
     * @param like
     * @param idMandat
     * @param forDate
     * @return
     */
    public static String getComptes(BSession session, String like, String idMandat, String forDate) {
        if (JadeStringUtil.isBlank(forDate)) {
            forDate = JACalendar.todayJJsMMsAAAA();
        }

        try {
            CGExerciceComptable exercice = LXHeliosService.getExerciceComptable(session, idMandat, forDate);

            return CGAutoComplete.getComptes(session, like, exercice.getIdExerciceComptable(), exercice.getMandat()
                    .isEstComptabiliteAVS().toString());
        } catch (Exception e) {
            return "<select size=\"5\" style=\"width:16cm\"></select>";
        }

    }

    /**
     * Methode pour la recuperation des informations des différents fournisseur
     * 
     * @param session
     *            Une session
     * @param like
     *            Un numero de référence BVR
     * @param idSociete
     *            Un identifiant de societe
     * @return
     */
    public static String getDecoupageLigneCodage(BSession session, String like, String idSociete) {
        // Exemple d'entrée Banque : like =
        // "0100002186056>327913041130722032212126099+ 230002615>";
        // Exemple d'entrée CCP : like =
        // "0100002186056>327913041130722032212126099+ 800000022>";

        StringBuffer select = new StringBuffer();
        select.append("<select size=\"5\" style=\"width:16cm\">");

        if ((like == null) || ('>' != like.trim().charAt(like.length() - 1))) {
            return select.toString();
        }

        try {
            if (JadeStringUtil.isBlank(like)) {
                select.append("<option value=\"" + like + "\">" + like + "</option>");
            } else {
                LXOutilsBVR outilBvr = new LXOutilsBVR(like);
                if (outilBvr.decouper()) {
                    select.append(LXAutoComplete.getLectureOptiqueInformation(session, idSociete,
                            outilBvr.getNumeroClient(), outilBvr.getNumeroReference(), outilBvr.getMontant(),
                            outilBvr.getCodeGenre(), like));
                }
            }
        } catch (Exception e) {
            JadeLogger.error(LXAutoComplete.class.getName() + "_getDecoupageLigneCodage()", e.toString());

            select = new StringBuffer("<select size=\"2\" style=\"width:8cm; color:red; \"><option>");
            select.append(session.getLabel("ERREUR_TECHNIQUE"));
            select.append("</option><option>&nbsp;</option></select>");
            return select.toString();
        }

        return select.toString();
    }

    /**
     * Methode pour la recuperation des informations des différents fournisseur
     * 
     * @param session
     *            Une session
     * @param like
     *            Un numero de référence BVR
     * @param idSociete
     *            Un identifiant de societe
     * @return
     */
    public static String getDecoupageLigneCodageBVRrouge(BSession session, String like, String idSociete) {
        // Exemple d'entrée : like = "230002615>";

        StringBuffer select = new StringBuffer();
        select.append("<select size=\"5\" style=\"width:16cm\">");

        if ((like == null) || ('>' != like.trim().charAt(like.length() - 1))) {
            return select.toString();
        }

        try {
            if (JadeStringUtil.isBlank(like)) {
                select.append("<option value=\"" + like + "\">" + like + "</option>");
            } else {
                if (like.length() == 10) {
                    like = like.substring(0, 9);
                    select.append(LXAutoComplete.getLectureOptiqueInformation(session, idSociete,
                            LXOutilsBVR.getCcpFormated(like.trim()), null, null, null, like));
                }
            }
        } catch (Exception e) {
            JadeLogger.error(LXAutoComplete.class.getName() + "_getDecoupageLigneCodageBVRrouge()", e.toString());

            select = new StringBuffer("<select size=\"2\" style=\"width:8cm; color:red; \"><option>");
            select.append(session.getLabel("ERREUR_TECHNIQUE"));
            select.append("</option><option>&nbsp;</option></select>");
            return select.toString();
        }

        return select.toString();
    }

    /**
     * Liste auto-complete des fournisseurs.
     * 
     * @param session
     * @param like
     * @param withAdressePaiement
     * @param idSociete
     * @param withInfoComptable
     * @return
     */
    public static String getFournisseur(BSession session, String like, String withAdressePaiement, String idSociete,
            String withInfoComptable, String typeFacture) {
        StringBuffer select = new StringBuffer("<select size=\"5\" style=\"width:16cm\">");

        try {
            LXFournisseurManager manager = new LXFournisseurManager();
            manager.setSession(session);

            if (like != null) {
                try {
                    Integer.parseInt(like.substring(0, 1));
                    manager.setLikeIdExterne(like);
                } catch (Exception e) {
                    manager.setLikeNomFournisseur(like);
                }
            }

            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                LXFournisseur fournisseur = (LXFournisseur) manager.getEntity(i);

                TIAdressePaiementDataSource dataSource = LXTiersService.getAdresseFournisseurPaiementAsDataSource(
                        session, null, fournisseur.getIdTiers());
                String compteFournisseur = null;
                String nomBanque = null;

                if (dataSource != null) {
                    compteFournisseur = dataSource.getData().get(
                            TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_COMPTE);
                    nomBanque = dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_BANQUE_D1);
                }

                if ((!LXOperation.CS_TYPE_FACTURE_VIREMENT.equals(typeFacture) || !JadeStringUtil
                        .isBlank(compteFournisseur)) && (dataSource != null)) {
                    select.append("<option idFournisseur=\"");
                    select.append(fournisseur.getIdFournisseur());
                    select.append("\"  libelleFournisseur=\"");
                    select.append(fournisseur.getNomComplet());
                    select.append("\" value=\"");
                    select.append(fournisseur.getIdExterne());
                    select.append("\"");

                    if (new Boolean(withAdressePaiement).booleanValue()) {

                        String tmpCCP = ""
                                + dataSource.getData().get(TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_CCP);
                        if (JadeStringUtil.isBlank(tmpCCP)
                                && !JadeStringUtil.isBlank(""
                                        + dataSource.getData().get(
                                                TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_BANQUE_CCP))) {
                            tmpCCP = ""
                                    + dataSource.getData().get(
                                            TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_BANQUE_CCP);
                        }

                        select.append(" ccpFournisseur=\"" + tmpCCP);

                        select.append("\"");
                        select.append(" compteFournisseur=\"");
                        select.append(compteFournisseur);
                        select.append("\"");
                        select.append(" clearingFournisseur=\"");
                        select.append(dataSource.getData().get(
                                TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_CLEARING));
                        select.append("\"");
                        select.append(" swiftFournisseur=\"");
                        select.append(dataSource.getData().get(
                                TIAbstractAdressePaiementDataSource.ADRESSEP_VAR_BANQUE_SWIFT));
                        select.append("\"");

                        // Information Banque
                        select.append(" nomBanque=\"");
                        select.append(nomBanque + "\n" + compteFournisseur);
                        select.append("\"");

                        // Récupération de l'adresse
                        TIAdresseFormater formater = new TIAdresseFormater();
                        select.append(" adressePaiementFournisseur=\"");
                        if (LXOperation.CS_TYPE_FACTURE_CAISSE.equals(typeFacture)) {
                            TIAdresseDataSource dataSourceCourrier = LXTiersService
                                    .getAdresseFournisseurCourrierAsDataSource(session, null, fournisseur.getIdTiers());
                            select.append(formater.format(dataSourceCourrier));
                        } else {
                            select.append(formater.format(dataSource));
                        }
                        select.append("\"");
                        // Information adresse banque
                        select.append(" adresseBanque=\"");
                        select.append(LXUtils.formateBanque(dataSource));
                        select.append("\"");
                    }

                    if (new Boolean(withInfoComptable).booleanValue()) {

                        LXInformationComptableListViewBean managerInfo = new LXInformationComptableListViewBean();
                        managerInfo.setSession(session);
                        managerInfo.setForIdFournisseur(fournisseur.getIdFournisseur());
                        managerInfo.setForIdSociete(idSociete);
                        managerInfo.find();

                        if (managerInfo.size() == 1) {
                            LXInformationComptableViewBean infoComptable = (LXInformationComptableViewBean) managerInfo
                                    .getFirstEntity();

                            select.append(LXAutoComplete.getInformationComptable(infoComptable));
                        }

                    }

                    select.append(">");
                    select.append(fournisseur.getIdExterne());
                    select.append(" - ");
                    select.append(fournisseur.getNomComplet());
                    select.append("</option>");
                }
            }

            select.append("</select>");
        } catch (Exception e) {
            JadeLogger.error(LXAutoComplete.class.getName() + "_getFournisseur()", e.toString());

            select = new StringBuffer("<select size=\"2\" style=\"width:16cm; color:red; \"><option>");
            select.append(session.getLabel("ERREUR_TECHNIQUE"));
            select.append("</option><option>&nbsp;</option></select>");
            return select.toString();
        }

        return select.toString();
    }

    /**
     * Retourne les informations comptables pour la réponse.
     * 
     * @param informationComptable
     * @return
     */
    private static String getInformationComptable(LXInformationComptableViewBean informationComptable) throws Exception {
        StringBuffer select = new StringBuffer();

        select.append(" echeance=\"");
        select.append(informationComptable.getEcheance());
        select.append("\"");
        select.append(" libelleFacture=\"");
        select.append(informationComptable.getLibelleFacture());
        select.append("\"");
        select.append(" codeIsoMonnaieDefaut=\"");
        select.append(informationComptable.getCsCodeIsoMonnaie());
        select.append("\"");

        select.append(" idCompteCreance=\"");
        select.append(informationComptable.getIdCompteCreance());
        select.append("\"");
        select.append(" idCompteCharge=\"");
        select.append(informationComptable.getIdCompteCharge());
        select.append("\"");
        select.append(" idCompteTVA=\"");
        select.append(informationComptable.getIdCompteTva());
        select.append("\"");

        select.append(" compteCreance=\"");
        select.append(informationComptable.getIdExterneCompteCredit());
        select.append("\"");
        select.append(" compteCharge=\"");
        select.append(informationComptable.getIdExterneCompteCharge());
        select.append("\"");
        select.append(" compteTVA=\"");
        select.append(informationComptable.getIdExterneCompteTva());
        select.append("\"");

        select.append(" idNatureCompteCreance=\"");
        select.append(informationComptable.getIdNatureCompteCreance());
        select.append("\"");
        select.append(" idNatureCompteCharge=\"");
        select.append(informationComptable.getIdNatureCompteCharge());
        select.append("\"");
        select.append(" idNatureCompteTVA=\"");
        select.append(informationComptable.getIdNatureCompteTVA());
        select.append("\"");

        select.append(" defaultIdCentreChargeCompteCreance=\"");
        select.append(informationComptable.getDefaultIdCentreChargeCompteCreance());
        select.append("\"");
        select.append(" defaultIdCentreChargeCompteCharge=\"");
        select.append(informationComptable.getDefaultIdCentreChargeCompteCharge());
        select.append("\"");
        select.append(" defaultIdCentreChargeCompteTVA=\"");
        select.append(informationComptable.getDefaultIdCentreChargeCompteTVA());
        select.append("\"");

        select.append(" defaultCsCodeTVA=\"");
        select.append(informationComptable.getCsCodeTva());
        select.append("\"");

        return select.toString();
    }

    /**
     * @param session
     * @param idSociete
     * @param ccp
     * @param numeroReference
     * @param montant
     * @param codeGenre
     * @param like
     * @return
     * @throws Exception
     */
    private static String getLectureOptiqueInformation(BSession session, String idSociete, String ccp,
            String numeroReference, String montant, String codeGenre, String like) throws Exception {

        StringBuffer select = new StringBuffer();

        LXLectureOptiqueManager managerLectureOptique = new LXLectureOptiqueManager();
        managerLectureOptique.setSession(session);
        managerLectureOptique.setLikeCcpFournisseur(ccp);
        managerLectureOptique.setForIdSociete(idSociete);
        managerLectureOptique.find();

        if (!managerLectureOptique.isEmpty()) {
            // Sur les resultats, on boucle et on fait ceci :

            for (int i = 0; i < managerLectureOptique.size(); i++) {

                LXLectureOptique lectureOptique = (LXLectureOptique) managerLectureOptique.get(i);

                select.append("<option");
                select.append(" value=\"");
                select.append(like);
                select.append("\"");

                select.append(" libelleFournisseur=\"");
                select.append(lectureOptique.getNomComplet());
                select.append("\"");
                select.append(" idExterneFournisseur=\"");
                select.append(lectureOptique.getIdExterne());
                select.append("\"");
                select.append(" idFournisseur=\"");
                select.append(lectureOptique.getIdFournisseur());
                select.append("\"");
                select.append(" idAdressePmt=\"");
                select.append(lectureOptique.getIdAdressePmt());
                select.append("\"");

                select.append(LXAutoComplete.getInformationComptable(lectureOptique));

                select.append(" numeroReference=\"");
                select.append(numeroReference);
                select.append("\"");
                select.append(" montant=\"");
                select.append(montant);
                select.append("\"");
                select.append(" ccpFournisseur=\"");
                select.append(ccp);
                select.append("\"");
                select.append(" codeGenre=\"");
                select.append(codeGenre);
                select.append("\"");

                select.append(" compteFournisseur=\"");
                select.append(lectureOptique.getBanqueCompte());
                select.append("\"");
                select.append(" clearingFournisseur=\"");
                select.append(lectureOptique.getBanqueClearing());
                select.append("\"");
                select.append(" swiftFournisseur=\"");
                select.append(lectureOptique.getBanqueSwift());
                select.append("\"");

                TIAdressePaiementDataSource dataSource = LXTiersService.getAdresseFournisseurPaiementAsDataSource(
                        session, null, lectureOptique.getIdTiers());
                TIAdresseFormater formater = new TIAdresseFormater();
                select.append(" adressePaiementFournisseur=\"");
                select.append(formater.format(dataSource));
                select.append("\"");

                if (JadeStringUtil.isIntegerEmpty(lectureOptique.getBanqueCompte())) {
                    select.append(" nomBanque=\"");
                    select.append("\"");
                    select.append(">");
                    select.append(lectureOptique.getIdExterne());
                    select.append(" - ");
                    select.append(lectureOptique.getNomComplet());
                } else {
                    select.append(" nomBanque=\"");
                    select.append(lectureOptique.getBanqueDes1() + " \n" + lectureOptique.getBanqueCompte());
                    select.append("\"");
                    select.append(">");
                    select.append(lectureOptique.getIdExterne());
                    select.append(" - ");
                    select.append(lectureOptique.getNomComplet());
                    select.append(" - ");
                    select.append(lectureOptique.getBanqueCompte());
                    select.append(" - ");
                    select.append(lectureOptique.getBanqueDes1());
                    select.append(" ");
                    select.append(lectureOptique.getBanqueDes2());
                }
                select.append("</option>");

            }
            select.append("</select>");
        }

        return select.toString();
    }

    /**
     * Retourne la liste des mandats.
     * 
     * @param session
     * @param like
     * @return
     */
    public static String getMandat(BSession session, String like) {
        StringBuffer select = new StringBuffer("<select size=\"5\" style=\"width:16cm\">");

        try {
            CGMandatManager manager = new CGMandatManager();
            manager.setSession(session);

            if (like != null) {
                try {
                    Integer.parseInt(like.substring(0, 1));
                    manager.setFromIdMandat(like);
                } catch (Exception e) {
                    if ("IT".equalsIgnoreCase(session.getIdLangueISO())) {
                        manager.setForLibelleItLike(like);
                    } else if ("DE".equalsIgnoreCase(session.getIdLangueISO())) {
                        manager.setForLibelleDeLike(like);
                    } else {
                        manager.setForLibelleFrLike(like);
                    }
                }
            }

            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                CGMandat mandat = (CGMandat) manager.getEntity(i);

                select.append("<option libelleMandat=\"").append(mandat.getLibelle()).append("\" value=\"")
                        .append(mandat.getIdMandat()).append("\">").append(mandat.getIdMandat()).append(" - ")
                        .append(mandat.getLibelle()).append("</option>");
            }

            select.append("</select>");

        } catch (Exception e) {
            JadeLogger.error(LXAutoComplete.class.getName() + "_getMandat()", e.toString());

            select = new StringBuffer("<select size=\"2\" style=\"width:8cm; color:red; \"><option>");
            select.append(session.getLabel("ERREUR_TECHNIQUE"));
            select.append("</option><option>&nbsp;</option></select>");
            return select.toString();
        }

        return select.toString();
    }

    /**
     * Liste auto-complete des sections.
     * 
     * @param session
     * @param like
     * @param idSociete
     * @param idFournisseur
     * @return
     */
    public static String getSection(BSession session, String like, String idSociete, String idFournisseur,
            String csTypeSection) {
        StringBuffer select = new StringBuffer("<select size=\"5\" style=\"width:16cm\">");

        try {
            if (JadeStringUtil.isBlank(idFournisseur) || JadeStringUtil.isBlank(idSociete)) {
                select.append("<option idSection=\"\" value=\"").append(like).append("\">").append(like)
                        .append("</option>");
            } else {

                LXSectionManager manager = new LXSectionManager();
                manager.setSession(session);

                manager.setForIdSociete(idSociete);
                manager.setForIdFournisseur(idFournisseur);
                manager.setForCsTypeSection(csTypeSection);

                if (like != null) {
                    manager.setLikeIdExterne(like);
                }

                manager.find();

                for (int i = 0; i < manager.size(); i++) {
                    LXSection section = (LXSection) manager.getEntity(i);

                    select.append("<option idSection=\"").append(section.getIdSection()).append("\" value=\"")
                            .append(section.getIdExterne()).append("\">").append(section.getIdExterne())
                            .append("</option>");
                }

                select.append("</select>");
            }
        } catch (Exception e) {
            JadeLogger.error(LXAutoComplete.class.getName() + "_getSociete()", e.toString());

            select = new StringBuffer("<select size=\"2\" style=\"width:8cm; color:red; \"><option>");
            select.append(session.getLabel("ERREUR_TECHNIQUE"));
            select.append("</option><option>&nbsp;</option></select>");
            return select.toString();
        }

        return select.toString();
    }

    /**
     * Liste auto-complete des sociétés débitrices.
     * 
     * @param session
     * @param like
     * @return
     */
    public static String getSociete(BSession session, String like, String withAdresse) {
        StringBuffer select = new StringBuffer("<select size=\"5\" style=\"width:16cm\">");

        try {
            LXSocieteDebitriceManager manager = new LXSocieteDebitriceManager();
            manager.setSession(session);

            if (like != null) {
                try {
                    Integer.parseInt(like.substring(0, 1));
                    manager.setLikeIdExterne(like);
                } catch (Exception e) {
                    manager.setLikeNomSociete(like);
                }
            }

            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                LXSocieteDebitrice societe = (LXSocieteDebitrice) manager.getEntity(i);

                select.append("<option idSociete=\"").append(societe.getIdSociete()).append("\"");
                select.append(" idMandat=\"").append(societe.getIdMandat()).append("\"");
                select.append(" libelleSociete=\"").append(societe.getNom()).append("\"");

                if (new Boolean(withAdresse).booleanValue()) {
                    String adresse = LXTiersService.getAdresseSocieteAsString(session,
                            session.getCurrentThreadTransaction(), societe.getIdTiers());
                    select.append(" adresseSociete=\"").append(adresse).append("\"");
                }

                select.append(" value=\"").append(societe.getIdExterne()).append("\">").append(societe.getIdExterne())
                        .append(" - ").append(societe.getNom());
                select.append("</option>");
            }

            select.append("</select>");
        } catch (Exception e) {
            JadeLogger.error(LXAutoComplete.class.getName() + "_getSociete()", e.toString());

            select = new StringBuffer("<select size=\"2\" style=\"width:8cm; color:red; \"><option>");
            select.append(session.getLabel("ERREUR_TECHNIQUE"));
            select.append("</option><option>&nbsp;</option></select>");
            return select.toString();
        }

        return select.toString();
    }

    /**
     * Liste auto-complete des sociétés débitrices et rapatriement des infos de l'organe d'execution.
     * 
     * @param session
     * @param like
     * @return
     */
    public static String getSocieteEtOrganeExecution(BSession session, String like, String withAdresse, String csGenre) {
        StringBuffer select = new StringBuffer("<select size=\"5\" style=\"width:16cm\">");

        try {
            LXSocieteDebitriceManager manager = new LXSocieteDebitriceManager();
            manager.setSession(session);

            if (like != null) {
                try {
                    Integer.parseInt(like.substring(0, 1));
                    manager.setLikeIdExterne(like);
                } catch (Exception e) {
                    manager.setLikeNomSociete(like);
                }
            }

            manager.find();

            for (int i = 0; i < manager.size(); i++) {
                LXSocieteDebitrice societe = (LXSocieteDebitrice) manager.getEntity(i);

                select.append("<option idSociete=\"").append(societe.getIdSociete()).append("\"");
                select.append(" idMandat=\"").append(societe.getIdMandat()).append("\"");
                select.append(" libelleSociete=\"").append(societe.getNom()).append("\"");

                if (new Boolean(withAdresse).booleanValue()) {
                    String adresse = LXTiersService.getAdresseSocieteAsString(session,
                            session.getCurrentThreadTransaction(), societe.getIdTiers());
                    select.append(" adresseSociete=\"").append(adresse).append("\"");

                    if (!JadeStringUtil.isBlank(csGenre)) {
                        LXOrganeExecutionManager oeManager = new LXOrganeExecutionManager();
                        oeManager.setSession(session);
                        oeManager.setForIdSocieteDebitrice(societe.getIdSociete());

                        ArrayList<String> forCsGenre = new ArrayList<String>();

                        if (LXOperation.CS_TYPE_FACTURE_LSV.equals(csGenre)) {
                            forCsGenre.add(LXOrganeExecution.CS_GENRE_LSV);
                        } else if (LXOperation.CS_TYPE_FACTURE_CAISSE.equals(csGenre)) {
                            forCsGenre.add(LXOrganeExecution.CS_GENRE_CAISSE);
                        } else if (LXOperation.CS_TYPE_FACTURE_VIREMENT.equals(csGenre)) {
                            forCsGenre.add(LXOrganeExecution.CS_GENRE_BANQUE);
                        } else {
                            forCsGenre.add(LXOrganeExecution.CS_GENRE_BANQUE);
                            forCsGenre.add(LXOrganeExecution.CS_GENRE_POSTE);
                        }
                        oeManager.setForCsGenreIn(forCsGenre);

                        oeManager.find();

                        select.append(" nbOrganeExecution=\"").append(oeManager.size()).append("\"");
                        for (int j = 0; j < oeManager.size(); j++) {
                            LXOrganeExecution oe = (LXOrganeExecution) oeManager.getEntity(j);

                            select.append(" idOrganeExecution").append(j).append("=\"")
                                    .append(oe.getIdOrganeExecution()).append("\"");
                            select.append(" nomOrganeExecution").append(j).append("=\"").append(oe.getNom())
                                    .append("\"");
                        }
                    }
                }

                select.append(" value=\"").append(societe.getIdExterne()).append("\">").append(societe.getIdExterne())
                        .append(" - ").append(societe.getNom());
                select.append("</option>");
            }

            select.append("</select>");
        } catch (Exception e) {
            JadeLogger.error(LXAutoComplete.class.getName() + "_getSocieteEtOrganeExecution()", e.toString());

            select = new StringBuffer("<select size=\"2\" style=\"width:8cm; color:red; \"><option>");
            select.append(session.getLabel("ERREUR_TECHNIQUE"));
            select.append("</option><option>&nbsp;</option></select>");
            return select.toString();
        }

        return select.toString();
    }

    /**
     * Constructeur
     */
    protected LXAutoComplete() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }
}
