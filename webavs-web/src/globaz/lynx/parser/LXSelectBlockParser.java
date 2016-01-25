package globaz.lynx.parser;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BManager;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;
import globaz.lynx.db.codetva.LXCodeTva;
import globaz.lynx.db.codetva.LXCodeTvaManager;
import globaz.lynx.db.facture.LXFacture;
import globaz.lynx.db.facture.LXFactureManager;
import globaz.lynx.db.operation.LXOperation;
import globaz.lynx.db.operation.LXOperationManager;
import globaz.lynx.db.organeexecution.LXOrganeExecution;
import globaz.lynx.db.organeexecution.LXOrganeExecutionManager;
import globaz.lynx.db.section.LXSection;
import globaz.lynx.translation.LXCodeSystem;
import globaz.lynx.utils.LXConstants;
import globaz.lynx.utils.LXNoteDeCreditUtil;
import globaz.lynx.utils.LXSectionUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LXSelectBlockParser {

    public static final String LABEL_AUCUN = "AUCUN";
    public static final String LABEL_TOUS = "TOUS";

    /**
     * Si l'opération liée est PREPARE ou SOLDE elle n'apparaîtra pas dans la liste car le manager propose uniquement
     * les opérations COMPTABILISE.
     * 
     * @param session
     * @param idOperationLiee
     * @param select
     * @throws Exception
     */
    private static void addOperationLieeToListe(BSession session, String idOperationLiee, StringBuffer select)
            throws Exception {
        if (!JadeStringUtil.isIntegerEmpty(idOperationLiee)) {
            LXOperation operation = LXNoteDeCreditUtil.getOperationLiee(session, null, idOperationLiee);

            if (LXOperation.CS_ETAT_PREPARE.equals(operation.getCsEtatOperation())
                    || LXOperation.CS_ETAT_SOLDE.equals(operation.getCsEtatOperation())) {
                LXSection section = LXSectionUtil.getSection(session, null, operation.getIdSection());

                FWCurrency valeurFacture = new FWCurrency();
                valeurFacture.add(operation.getMontant());

                FWCurrency valeurNoteDeCreditLiee = LXNoteDeCreditUtil.getMontantFactureDejaUtilise(session,
                        operation.getIdOperation(), operation.getIdSection());
                valeurFacture.add(valeurNoteDeCreditLiee);

                select.append("<OPTION value=\"");
                select.append(operation.getIdOperation());
                select.append("\"  selected=\"selected\"");
                select.append(">");
                select.append(operation.getDateOperation()).append(" - ").append(operation.getDateEcheance())
                        .append(" - ").append(section.getIdExterne()).append(" - ").append(session.getLabel("SOLDE"))
                        .append(" : ").append(valeurFacture.toStringFormat());
                select.append("</OPTION>");
            }
        }
    }

    /**
     * Permet la création d'un select (tag htlm) en ajoutant le label "TOUS" au debut
     * 
     * @param session
     *            Une session
     * @param manager
     *            Un manager
     * @param fieldName
     *            Le nom du field
     * @param selectValue
     *            La valeur du field par defaut
     * @param addLabelTous
     *            True si label TOUS (false sinon)
     * @param addLabelAucun
     *            True si label Aucun (false sinon)
     * @return
     */
    private static String createSelect(BSession session, FWParametersSystemCodeManager manager, String fieldName,
            String selectValue, boolean addLabelTous, boolean addLabelAucun) {
        StringBuffer newSelect = new StringBuffer();

        if (manager.size() > 1) {
            newSelect.append("<SELECT id=\"");
            newSelect.append(fieldName);
            newSelect.append("\" name=\"");
            newSelect.append(fieldName);
            newSelect.append("\" tabindex=\"1\" >");

            if (addLabelTous) {
                newSelect.append("<OPTION selected value=\"\">");
                newSelect.append(session.getLabel(LXSelectBlockParser.LABEL_TOUS));
                newSelect.append("</OPTION>");
            }

            if (addLabelAucun) {
                newSelect.append("<OPTION selected value=\"\">");
                newSelect.append(session.getLabel(LXSelectBlockParser.LABEL_AUCUN));
                newSelect.append("</OPTION>");
            }

            for (int i = 0; i < manager.size(); i++) {
                FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);
                if (!JadeStringUtil.isBlank(code.getLibelle())) {
                    newSelect.append("<OPTION value=\"");
                    newSelect.append(code.getIdCode());
                    newSelect.append("\"");

                    if (code.getIdCode().equals(selectValue)) {
                        newSelect.append(" selected=\"selected\"");
                    }

                    newSelect.append(">");
                    newSelect.append(code.getCurrentCodeUtilisateur().getLibelle());
                    newSelect.append("</OPTION>");
                }
            }

            newSelect.append("</SELECT>");
        }

        return newSelect.toString();
    }

    public static String getCsCodeIsoMonnaie(BSession session, String csCodeIsoMonnaie) throws Exception {
        StringBuffer select = new StringBuffer();
        String codeSelected = (csCodeIsoMonnaie != null) ? csCodeIsoMonnaie : LXConstants.CODE_ISO_CHF;

        try {
            FWParametersSystemCodeManager manager = LXCodeSystem.getCsCodeIsoMonnaie(session);

            if (manager.size() > 1) {
                select.append("<SELECT id=\"csCodeIsoMonnaie\" name=\"csCodeIsoMonnaie\" tabindex=\"-1\">");

                for (int i = 0; i < manager.size(); i++) {
                    FWParametersSystemCode code = (FWParametersSystemCode) manager.getEntity(i);

                    if (!JadeStringUtil.isBlank(code.getLibelle())) {
                        select.append("<OPTION value=\"");
                        select.append(code.getIdCode());
                        select.append("\"");

                        if (code.getIdCode().equals(codeSelected)) {
                            select.append(" selected=\"selected\"");
                        }

                        select.append(">");
                        select.append(code.getCurrentCodeUtilisateur().getCodeUtilisateur());
                        select.append("</OPTION>");
                    }
                }
                select.append("</SELECT>");
            }
        } catch (Exception e) {
            JadeLogger.error(LXSelectBlockParser.class.getName() + "_getCsCodeIsoMonnaie()", e.toString());
        }

        return select.toString();
    }

    public static String getCsCodeTVASelectBlock(BSession session, String fieldName, String selectValue,
            boolean addLabelTous, boolean addLabelAucun) {
        String newSelect = "";

        try {
            FWParametersSystemCodeManager manager = LXCodeSystem.getCsCodeTva(session);
            newSelect = LXSelectBlockParser.createSelect(session, manager, fieldName, selectValue, addLabelTous,
                    addLabelAucun);
        } catch (Exception e) {
            JadeLogger.error(LXSelectBlockParser.class.getName() + "_getCsCodeTVASelectBlock()", e.toString());
        }

        return newSelect;
    }

    public static String getForCsCodeTVASelectBlock(BSession session) {
        String forCsCodeTVASelectBlock = "";

        try {
            FWParametersSystemCodeManager manager = LXCodeSystem.getCsCodeTva(session);
            forCsCodeTVASelectBlock = LXSelectBlockParser.createSelect(session, manager, "forCsCodeTVA", null, true,
                    false);
        } catch (Exception e) {
            JadeLogger.error(LXSelectBlockParser.class.getName() + "_getForCsCodeTVASelectBlock()", e.toString());
        }

        return forCsCodeTVASelectBlock;
    }

    public static String getForCsEtatJournalSelectBlock(BSession session) {
        String forCsEtatSelectBlock = "";

        try {
            FWParametersSystemCodeManager manager = LXCodeSystem.getCsEtatJournal(session);
            forCsEtatSelectBlock = LXSelectBlockParser.createSelect(session, manager, "forCsEtat", null, true, false);
        } catch (Exception e) {
            JadeLogger.error(LXSelectBlockParser.class.getName() + "_getForCsEtatJournalSelectBlock()", e.toString());
        }

        return forCsEtatSelectBlock;
    }

    public static String getForCsEtatOperationSelectBlock(BSession session) {
        String forCsEtatSelectBlock = "";

        try {
            FWParametersSystemCodeManager manager = LXCodeSystem.getCsEtatOperation(session);
            forCsEtatSelectBlock = LXSelectBlockParser.createSelect(session, manager, "forCsEtat", null, true, false);
        } catch (Exception e) {
            JadeLogger.error(LXSelectBlockParser.class.getName() + "_getForCsEtatOperationSelectBlock()", e.toString());
        }

        return forCsEtatSelectBlock;
    }

    public static String getForCsEtatOrdreGroupeSelectBlock(BSession session) {
        String forCsEtatSelectBlock = "";

        try {
            FWParametersSystemCodeManager manager = LXCodeSystem.getCsEtatOrdreGroupe(session);
            forCsEtatSelectBlock = LXSelectBlockParser.createSelect(session, manager, "forCsEtat", null, true, false);
        } catch (Exception e) {
            JadeLogger.error(LXSelectBlockParser.class.getName() + "_getForCsEtatOrdreGroupeSelectBlock()",
                    e.toString());
        }

        return forCsEtatSelectBlock;
    }

    public static String getForIdCategorieSelectBlock(BSession session) {
        String forIdCategorieSelectBlock = "";

        try {
            FWParametersSystemCodeManager manager = LXCodeSystem.getCsCategories(session);
            forIdCategorieSelectBlock = LXSelectBlockParser.createSelect(session, manager, "forCsCategorie", null,
                    true, false);
        } catch (Exception e) {
            JadeLogger.error(LXSelectBlockParser.class.getName() + "_getForIdCategorieSelectBlock()", e.toString());
        }

        return forIdCategorieSelectBlock;
    }

    /**
     * @param session
     * @param idSociete
     * @param idOrganeExecutionValue
     * @return
     */
    public static String getIdOrganeExecutionSelectBlock(BSession session, String idSociete,
            String idOrganeExecutionValue) {
        StringBuffer idorganeSelectBlockSelectBlock = new StringBuffer();

        if (JadeStringUtil.isEmpty(idSociete)) {
            idorganeSelectBlockSelectBlock
                    .append("<SELECT id=\"idOrganeExecution\" name=\"idOrganeExecution\" tabindex=\"1\">");
            idorganeSelectBlockSelectBlock.append("<OPTION selected value=\"\"></OPTION>");
            idorganeSelectBlockSelectBlock.append("</SELECT>");

            return idorganeSelectBlockSelectBlock.toString();
        }

        try {
            LXOrganeExecutionManager manager = new LXOrganeExecutionManager();
            manager.setISession(session);
            manager.setForIdSocieteDebitrice(idSociete);
            manager.find();

            idorganeSelectBlockSelectBlock.append("<SELECT id=\"idOrganeExecution\" name=\"idOrganeExecution\">");
            idorganeSelectBlockSelectBlock.append("<OPTION selected value=\"\"></OPTION>");

            for (int i = 0; i < manager.size(); i++) {
                LXOrganeExecution organe = (LXOrganeExecution) manager.getEntity(i);
                if (!JadeStringUtil.isBlank(organe.getIdOrganeExecution())) {
                    idorganeSelectBlockSelectBlock.append("<OPTION value=\"");
                    idorganeSelectBlockSelectBlock.append(organe.getIdOrganeExecution());
                    idorganeSelectBlockSelectBlock.append("\"");

                    if (organe.getIdOrganeExecution().equals(idOrganeExecutionValue)) {
                        idorganeSelectBlockSelectBlock.append(" selected=\"selected\"");
                    }

                    idorganeSelectBlockSelectBlock.append(">");
                    idorganeSelectBlockSelectBlock.append(organe.getNom());
                    idorganeSelectBlockSelectBlock.append("</OPTION>");
                }
            }

            idorganeSelectBlockSelectBlock.append("</SELECT>");

        } catch (Exception e) {
            JadeLogger.error(LXSelectBlockParser.class.getName() + "_getIdOrganeExecutionSelectBlock()", e.toString());
        }

        return idorganeSelectBlockSelectBlock.toString();
    }

    /**
     * Construit un select avec la liste des Organes d'execution et une option vide suivant un identifiant d'un organe
     * d'execution
     * 
     * @param session
     *            Une session
     * @param idSociete
     *            Un identifiant d'une société débitrice
     * @param idOrganeExecutionValue
     *            Un identifiant d'un organe d'execution (pour la preselection)
     * @return
     */
    public static String getIdOrganeExecutionSelectBlock(BSession session, String idSociete,
            String idOrganeExecutionValue, String type) {
        StringBuffer idorganeSelectBlockSelectBlock = new StringBuffer();

        try {
            LXOrganeExecutionManager manager = new LXOrganeExecutionManager();
            manager.setISession(session);

            idorganeSelectBlockSelectBlock
                    .append("<SELECT id=\"idOrganeExecution\" name=\"idOrganeExecution\" tabindex=\"1\">");
            idorganeSelectBlockSelectBlock.append("<OPTION selected value=\"\"></OPTION>");

            if (!JadeStringUtil.isBlank(idSociete)) {
                manager.setForIdSocieteDebitrice(idSociete);

                if (type != null) {
                    ArrayList<String> csGenre = new ArrayList<String>();

                    if (LXOperation.CS_TYPE_FACTURE_LSV.equals(type)) {
                        csGenre.add(LXOrganeExecution.CS_GENRE_LSV);
                    } else if (LXOperation.CS_TYPE_FACTURE_CAISSE.equals(type)) {
                        csGenre.add(LXOrganeExecution.CS_GENRE_CAISSE);
                    } else {
                        csGenre.add(LXOrganeExecution.CS_GENRE_BANQUE);
                        csGenre.add(LXOrganeExecution.CS_GENRE_POSTE);
                    }

                    manager.setForCsGenreIn(csGenre);
                }

                manager.find();

                for (int i = 0; i < manager.size(); i++) {
                    LXOrganeExecution organe = (LXOrganeExecution) manager.getEntity(i);
                    if (!JadeStringUtil.isBlank(organe.getIdOrganeExecution())) {
                        idorganeSelectBlockSelectBlock.append("<OPTION value=\"");
                        idorganeSelectBlockSelectBlock.append(organe.getIdOrganeExecution());
                        idorganeSelectBlockSelectBlock.append("\"");

                        if (organe.getIdOrganeExecution().equals(idOrganeExecutionValue)) {
                            idorganeSelectBlockSelectBlock.append(" selected=\"selected\"");
                        }

                        idorganeSelectBlockSelectBlock.append(">");
                        idorganeSelectBlockSelectBlock.append(organe.getNom());
                        idorganeSelectBlockSelectBlock.append("</OPTION>");
                    }
                }
            }

            idorganeSelectBlockSelectBlock.append("</SELECT>");

        } catch (Exception e) {
            JadeLogger.error(LXSelectBlockParser.class.getName() + "_getIdOrganeExecutionSelectBlock()", e.toString());
        }

        return idorganeSelectBlockSelectBlock.toString();
    }

    /**
     * Permet de recuperer la liste des factures potentielles pour la liaison des notes de crédits
     * 
     * @param session
     * @param idFournisseur
     * @param idSociete
     * @param idSection
     * @return
     * @throws Exception
     */
    public static String getListeFacture(BSession session, String idFournisseur, String idSociete,
            String idOperationLiee, String idOperationSrc, boolean isMethodAdd) throws Exception {
        StringBuffer select = new StringBuffer();

        LXFactureManager facManager = new LXFactureManager();
        facManager.setSession(session);
        facManager.setForIdFournisseur(idFournisseur);
        facManager.setForIdSociete(idSociete);
        facManager.setForCsEtatOperation(LXOperation.CS_ETAT_COMPTABILISE);
        facManager.find();

        select.append("<SELECT id=\"idOperationLiee\" name=\"idOperationLiee\" tabindex=\"1\">");

        if (facManager.size() < 1) {
            select.append("<OPTION selected value=\"\">").append(session.getLabel(LXSelectBlockParser.LABEL_AUCUN))
                    .append("</OPTION>");
        }

        LXSelectBlockParser.addOperationLieeToListe(session, idOperationLiee, select);

        for (int i = 0; i < facManager.getSize(); i++) {
            LXFacture fac = (LXFacture) facManager.get(i);

            FWCurrency valeurFacture = new FWCurrency();
            valeurFacture.add(fac.getMontant());

            FWCurrency valeurNoteDeCreditLiee = LXNoteDeCreditUtil.getMontantFactureDejaUtilise(session,
                    fac.getIdOperation(), fac.getIdSection());
            valeurFacture.add(valeurNoteDeCreditLiee);

            if ((isMethodAdd && valeurFacture.isPositive() && !LXSelectBlockParser.isFactureAlreadyBinded(session,
                    idOperationSrc, fac)) || (fac.getIdOperation().equals(idOperationLiee) && !isMethodAdd)) {
                select.append("<OPTION value=\"");
                select.append(fac.getIdOperation());
                select.append("\"");

                if (fac.getIdOperation().equals(idOperationLiee)) {
                    select.append(" selected=\"selected\"");
                }

                select.append(">");
                select.append(fac.getDateFacture()).append(" - ").append(fac.getDateEcheance()).append(" - ")
                        .append(fac.getIdExterne()).append(" - ").append(session.getLabel("SOLDE")).append(" : ")
                        .append(valeurFacture.toStringFormat());
                select.append("</OPTION>");
            }
        }

        select.append("</SELECT>");

        return select.toString();
    }

    public static String getListOrganeExecution(BSession session) throws Exception {
        StringBuffer javascript = new StringBuffer();

        LXOrganeExecutionManager manager = new LXOrganeExecutionManager();
        manager.setSession(session);
        manager.find(BManager.SIZE_NOLIMIT);

        for (int i = 0; i < manager.size(); i++) {
            LXOrganeExecution org = (LXOrganeExecution) manager.getEntity(i);

            // if(javascript.length() > 0) javascript.append(" else ");
            javascript.append("if( idSociete == ").append(org.getIdSociete()).append(") {");
            javascript
                    .append("   document.getElementById(\"idOrganeExecution\").options[document.getElementById(\"idOrganeExecution\").options.length]= new Option('")
                    .append(org.getNom()).append("', '").append(org.getIdOrganeExecution()).append("');");
            javascript.append("}");
        }

        return javascript.toString();
    }

    public static String getTauxEtCodeTvaSelectBlock(BSession session, String csCodeTva) throws Exception {
        StringBuffer select = new StringBuffer();
        Map<String, String> mapCodeSystem = new HashMap<String, String>();

        try {
            FWParametersSystemCodeManager managerCodeSystem = LXCodeSystem.getCsCodeTva(session);
            for (int i = 0; i < managerCodeSystem.size(); i++) {
                FWParametersSystemCode code = (FWParametersSystemCode) managerCodeSystem.getEntity(i);
                mapCodeSystem.put(code.getIdCode(), code.getLibelle());
            }

            LXCodeTvaManager manager = new LXCodeTvaManager();
            manager.setSession(session);
            manager.setForDateBetween(JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY));
            manager.find();

            select.append("<SELECT id=\"csCodeTVA\" name=\"csCodeTVA\" tabindex=\"1\">");
            select.append("<OPTION selected taux=\"\" value=\"\">" + session.getLabel(LXSelectBlockParser.LABEL_AUCUN)
                    + "</OPTION>");

            for (int i = 0; i < manager.size(); i++) {
                LXCodeTva codeTva = (LXCodeTva) manager.getEntity(i);
                if (!JadeStringUtil.isBlank(codeTva.getCsCodeTVA())) {
                    select.append("<OPTION value=\"");
                    select.append(codeTva.getCsCodeTVA());
                    select.append("\"");

                    select.append(" taux=\"" + codeTva.getTaux() + "\"");

                    if (codeTva.getCsCodeTVA().equals(csCodeTva)) {
                        select.append(" selected=\"selected\"");
                    }

                    select.append(">");
                    select.append(mapCodeSystem.get(codeTva.getCsCodeTVA()) + " (" + codeTva.getTaux() + " %)");
                    select.append("</OPTION>");
                }
            }
        } catch (Exception e) {
            JadeLogger.error(LXSelectBlockParser.class.getName() + "_getTauxEtCodeTvaSelectBlock()", e.toString());
        }

        return select.toString();
    }

    /**
     * Return true si la facture est déjà liée à la note de crédit de base.
     * 
     * @param session
     * @param idOperationSrc
     * @param fac
     * @return
     * @throws Exception
     */
    private static boolean isFactureAlreadyBinded(BSession session, String idOperationSrc, LXFacture fac)
            throws Exception {
        LXOperationManager manager = new LXOperationManager();
        manager.setSession(session);

        manager.setForIdOperationLiee(fac.getIdOperation());
        manager.setForIdOperationSrc(idOperationSrc);

        manager.find();

        return !manager.isEmpty();
    }

    /**
     * Constructeur
     */
    protected LXSelectBlockParser() {
        throw new UnsupportedOperationException(); // prevents calls from
        // subclass
    }
}
