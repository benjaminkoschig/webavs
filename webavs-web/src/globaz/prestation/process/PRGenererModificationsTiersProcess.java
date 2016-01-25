package globaz.prestation.process;

import globaz.corvus.application.REApplication;
import globaz.globall.db.BProcess;
import globaz.globall.db.GlobazJobQueue;
import globaz.globall.db.GlobazServer;
import globaz.globall.parameters.FWParametersUserCode;
import globaz.jade.admin.user.bean.JadeUser;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.smtp.JadeSmtpClient;
import globaz.prestation.external.AdresseModificationsHandler;
import globaz.prestation.interfaces.fx.PRGestionnaireHelper;
import globaz.pyxis.db.adressecourrier.TIPays;
import java.util.HashMap;

public class PRGenererModificationsTiersProcess extends BProcess {

    public enum Language {
        FR("fr"),
        DE("de"),
        IT("it");

        private String value;

        private Language(String value) {
            this.value = value;
        }
    };

    private static final long serialVersionUID = 1L;

    private TypeModificationsTiers typeModification;

    private String idCantonDomicile = "";
    private String dateDeces = "";
    private String dateModification = "";
    private String dateNaissance = "";
    private String heureModification = "";
    private String idGroupeNotification = "";
    private String idTier = "";
    private HashMap<String, String> modifications;
    private TIPays nationalite = new TIPays();

    private String newIdCantonDomicile = "";
    private String newDateDeces = "";
    private String newDateNaissance = "";
    private TIPays newNationalite = new TIPays();
    private String newNom = "";
    private String newNSS = "";
    private TIPays newPaysDomicile = new TIPays();
    private String newPrenom = "";
    private String newSexe = "";
    private String nom = "";
    private String nss = "";
    private TIPays paysDomicile = new TIPays();
    private String prenom = "";
    private String sexe = "";
    private String user = "";

    private String designation3 = "";
    private String newDesignation3 = "";
    private String designation4 = "";
    private String newDesignation4 = "";

    private String pays = "";
    private String etatCivil = "";
    private String nomJeuneFille = "";
    private String langue = "";
    private String newPays = "";
    private String newEtatCivil = "";
    private String newNomJeuneFille = "";
    private String newLangue = "";

    protected AdresseModificationsHandler containerModificationAdresse = null;
    private boolean isCommunicationOAI = false;

    public AdresseModificationsHandler getContainerModificationAdresse() {
        return containerModificationAdresse;
    }

    @Override
    protected void _executeCleanUp() {
    }

    REApplication app;

    @Override
    protected boolean _executeProcess() throws Exception {

        app = (REApplication) GlobazServer.getCurrentSystem().getApplication("CORVUS");

        JadeSmtpClient mail = JadeSmtpClient.getInstance();

        if (!JadeStringUtil.isBlank(getIdGroupeNotification())) {
            String[] users = PRGestionnaireHelper.getGestionnairesId(getIdGroupeNotification());
            if (users != null) {
                for (String userId : users) {
                    JadeUser jadeUser = PRGestionnaireHelper.getGestionnaire(userId);
                    String sujet = getSujet(jadeUser);
                    String body = getBody(jadeUser.getLanguage());
                    if (jadeUser.getEmail().indexOf("@") > 0) {
                        mail.sendMail(jadeUser.getEmail(), sujet, body, null);
                    }
                }
            }

        }

        return true;
    }

    private String getSujet(JadeUser user) {
        String sujet = "";

        if (isCommunicationOAI) {
            sujet = app.getLabel("COMM_OAI_OBJET_MAIL_PROCESS_MODIFICATION_TIERS", user.getLanguage()) + " "
                    + getNewNSS() + " " + getNewNom() + " " + getNewPrenom();
        } else {

            sujet = app.getLabel("OBJET_MAIL_PROCESS_MODIFICATION_TIERS", user.getLanguage()) + " : " + getNewNSS()
                    + " " + getNewNom() + " " + getNewPrenom();
        }

        return sujet;
    }

    protected String getBody(String langue) {

        StringBuffer mailBody = new StringBuffer();

        addInfosModification(langue, mailBody);

        if (typeModification.equals(TypeModificationsTiers.MODIFICATION_TIERS)) {
            addModificationsTiers(langue, mailBody);
        } else {
            addModificationsAdresse(langue, mailBody);
        }

        // Détails avant modification
        mailBody.append("<b>" + app.getLabel("AVANT_MODIFICATION_TIERS", langue) + "</b>\n");
        addDetailsTiersAvantModification(langue, mailBody);
        mailBody.append("\n");
        if (typeModification.equals(TypeModificationsTiers.MODIFICATION_ADRESSE)
                || typeModification.equals(TypeModificationsTiers.SUPPRESSION_ADRESSE)) {
            addAdresseAvantModification(langue, mailBody);
        }
        mailBody.append("\n");

        // Détails après modification
        mailBody.append("<b>" + app.getLabel("APRES_MODIFICATION_TIERS", langue) + "</b>\n");
        addDetailsTiersApresModification(langue, mailBody);
        mailBody.append("\n");
        if (typeModification.equals(TypeModificationsTiers.MODIFICATION_ADRESSE)
                || typeModification.equals(TypeModificationsTiers.AJOUT_ADRESSE)) {
            addAdresseApresModification(langue, mailBody);
        }
        mailBody.append("\n");

        return mailBody.toString();
    }

    private void addInfosModification(String langue, StringBuffer mailBody) {

        String titreBody = "";
        String modifiePar = "";

        if (typeModification.equals(TypeModificationsTiers.AJOUT_ADRESSE)) {
            titreBody = app.getLabel(("TITRE_AJOUT_ADRESSE_TIERS"), langue) + "\n\n";
            modifiePar = app.getLabel("TITRE_AJOUT_ADRESSE_TIERS_GESTIONNAIRE", langue) + getUser() + "\n";
        } else if (typeModification.equals(TypeModificationsTiers.MODIFICATION_ADRESSE)) {
            titreBody = app.getLabel(("TITRE_MODIFICATION_ADRESSE_TIERS"), langue) + "\n\n";
            modifiePar = app.getLabel("TITRE_MODIFICATION_ADRESSE_TIERS_GESTIONNAIRE", langue) + getUser() + "\n";

        } else if (typeModification.equals(TypeModificationsTiers.SUPPRESSION_ADRESSE)) {
            titreBody = app.getLabel(("TITRE_SUPPRESSION_ADRESSE_TIERS"), langue) + "\n\n";
            modifiePar = app.getLabel("TITRE_SUPPRESSION_ADRESSE_TIERS_GESTIONNAIRE", langue) + getUser() + "\n";
        } else {
            titreBody = app.getLabel(("TITRE_MODIFICATIONS_TIERS"), langue) + "\n\n";
            modifiePar = app.getLabel("TITRE_MODIFICATIONS_TIERS_GESTIONNAIRE", langue) + " " + getUser() + "\n";
        }

        mailBody.append("<b>" + titreBody + "</b>");
        mailBody.append(modifiePar);
        mailBody.append(app.getLabel("DATE_MODIFICATION_TIERS", langue) + " " + getDateModification() + "\n");
        mailBody.append(app.getLabel("HEURE_MODIFICATION_TIERS", langue) + " " + getHeureModification() + "\n" + "\n");
        mailBody.append("<b>" + app.getLabel("DETAILS_MODIFICATIONS_TIERS", langue) + "</b>\n");
    }

    private void addDetailsTiersAvantModification(String langue, StringBuffer mailBody) {
        mailBody.append(app.getLabel("NSS_MODIFICATION_TIERS", langue) + " " + getNSS() + "\n");
        mailBody.append(app.getLabel("ETAT_CIVIL_MODIFICATION", langue) + " " + getUserCode(getEtatCivil(), langue)
                + "\n");
        mailBody.append(app.getLabel("NOM_MODIFICATION_TIERS", langue) + " " + getNom() + "\n");
        mailBody.append(app.getLabel("PRENOM_MODIFICATION_TIERS", langue) + " " + getPrenom() + "\n");
        mailBody.append(app.getLabel("NOM_SUITE_1_MODIFICATION", langue) + " " + getDesignation3() + "\n");
        mailBody.append(app.getLabel("NOM_SUITE_2_MODIFICATION", langue) + " " + getDesignation4() + "\n");
        mailBody.append(app.getLabel("NOM_JEUNE_FILLE_MODIFICATION", langue) + " " + getNomJeuneFille() + "\n");
        mailBody.append(app.getLabel("SEXE_MODIFICATION_TIERS", langue) + " " + app.getLabel(getSexe(), langue) + "\n");
        mailBody.append(app.getLabel("NAISSANCE_MODIFICATION_TIERS", langue) + " " + getDateNaissance() + "\n");
        mailBody.append(app.getLabel("DECES_MODIFICATION_TIERS", langue) + " " + getDateDeces() + "\n");
        mailBody.append(app.getLabel("CANTON_MODIFICATION_TIERS", langue)
                + " "
                + (JadeStringUtil.isBlankOrZero(getIdCantonDomicile()) ? ""
                        : getUserCode(getIdCantonDomicile(), langue)) + "\n");
        mailBody.append(app.getLabel("PAYS_MODIFICATION_TIERS", langue) + " "
                + getLibellePays(getPaysDomicile(), langue) + "\n");
        mailBody.append(app.getLabel("NATIONALITE_MODIFICATION_TIERS", langue) + " "
                + getLibellePays(getNationalite(), langue) + "\n");

        mailBody.append(app.getLabel("LANGUE_MODIFICATION", langue) + " " + getUserCode(getLangue(), langue) + "\n");
    }

    private void addDetailsTiersApresModification(String langue, StringBuffer mailBody) {
        mailBody.append(app.getLabel("NSS_MODIFICATION_TIERS", langue) + " " + getNewNSS() + "\n");
        mailBody.append(app.getLabel("ETAT_CIVIL_MODIFICATION", langue) + " " + getUserCode(getNewEtatCivil(), langue)
                + "\n");
        mailBody.append(app.getLabel("NOM_MODIFICATION_TIERS", langue) + " " + getNewNom() + "\n");
        mailBody.append(app.getLabel("PRENOM_MODIFICATION_TIERS", langue) + " " + getNewPrenom() + "\n");
        mailBody.append(app.getLabel("NOM_SUITE_1_MODIFICATION", langue) + " " + getNewDesignation3() + "\n");
        mailBody.append(app.getLabel("NOM_SUITE_2_MODIFICATION", langue) + " " + getNewDesignation4() + "\n");
        mailBody.append(app.getLabel("NOM_JEUNE_FILLE_MODIFICATION", langue) + " " + getNewNomJeuneFille() + "\n");
        mailBody.append(app.getLabel("SEXE_MODIFICATION_TIERS", langue) + " " + app.getLabel(getNewSexe(), langue)
                + "\n");
        mailBody.append(app.getLabel("NAISSANCE_MODIFICATION_TIERS", langue) + " " + getNewDateNaissance() + "\n");
        mailBody.append(app.getLabel("DECES_MODIFICATION_TIERS", langue) + " " + getNewDateDeces() + "\n");
        mailBody.append(app.getLabel("CANTON_MODIFICATION_TIERS", langue)
                + " "
                + (JadeStringUtil.isBlankOrZero(getNewIdCantonDomicile()) ? "" : getUserCode(getNewIdCantonDomicile(),
                        langue)) + "\n");
        mailBody.append(app.getLabel("PAYS_MODIFICATION_TIERS", langue) + " "
                + getLibellePays(getNewPaysDomicile(), langue) + "\n");
        mailBody.append(app.getLabel("NATIONALITE_MODIFICATION_TIERS", langue) + " "
                + getLibellePays(getNewNationalite(), langue) + "\n");

        mailBody.append(app.getLabel("LANGUE_MODIFICATION", langue) + " " + getUserCode(getNewLangue(), langue) + "\n");
    }

    private String getLibellePays(TIPays pays, String langue) {

        if (JadeStringUtil.isBlank(langue)) {
            langue = "FR";
        }

        Language language = Language.valueOf(langue.toUpperCase());

        switch (language) {
            case DE:
                return pays.getLibelleAl();

            case IT:
                return pays.getLibelleIt();

            default:
                return pays.getLibelleFr();
        }
    }

    public String getUserCode(String idCode, String langue) {

        if (JadeStringUtil.isBlank(langue)) {
            langue = "FR";
        }

        FWParametersUserCode userCode = new FWParametersUserCode();
        userCode.setSession(getSession());
        userCode.setIdCodeSysteme(idCode);

        Language language = Language.valueOf(langue.toUpperCase());

        switch (language) {
            case DE:
                userCode.setIdLangue("D");

            case IT:
                userCode.setIdLangue("I");

            default:
                userCode.setIdLangue("F");
        }

        try {
            userCode.retrieve();
            if (!userCode.isNew()) {
                return userCode.getLibelle();
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    private void addAdresseAvantModification(String langue, StringBuffer mailBody) {

        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getTitreAdresse().getOldValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_TITRE_ADRESSE", langue) + " "
                    + getSession().getCodeLibelle(containerModificationAdresse.getTitreAdresse().getOldValue()) + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getAttention().getOldValue())) {
            mailBody.append(app.getLabel("ATTENTION_MODIFICATION_ADRESSE", langue) + " "
                    + containerModificationAdresse.getAttention().getOldValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getCasePostale().getOldValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_CASE_POSTALE_ADRESSE", langue) + " "
                    + containerModificationAdresse.getCasePostale().getOldValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getLigneAdresse1().getOldValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_LIGNE_ADRESSE_1", langue) + " "
                    + containerModificationAdresse.getLigneAdresse1().getOldValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getLigneAdresse2().getOldValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_LIGNE_ADRESSE_2", langue) + " "
                    + containerModificationAdresse.getLigneAdresse2().getOldValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getLigneAdresse3().getOldValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_LIGNE_ADRESSE_3", langue) + " "
                    + containerModificationAdresse.getLigneAdresse3().getOldValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getLigneAdresse4().getOldValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_LIGNE_ADRESSE_4", langue) + " "
                    + containerModificationAdresse.getLigneAdresse4().getOldValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getRue().getOldValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_RUE", langue) + " "
                    + containerModificationAdresse.getRue().getOldValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getNumeroRue().getOldValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_NUMERO_RUE", langue) + " "
                    + containerModificationAdresse.getNumeroRue().getOldValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getRueRepertoire().getOldValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_RUE_REPERTOIRE", langue) + " "
                    + containerModificationAdresse.getRueRepertoire().getOldValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getNumPostal().getOldValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_NUM_POSTAL", langue) + " "
                    + containerModificationAdresse.getNumPostal().getOldValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getLocalite().getOldValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_LOCALITE", langue) + " "
                    + containerModificationAdresse.getLocalite().getOldValue() + "\n");
        }
    }

    private void addAdresseApresModification(String langue, StringBuffer mailBody) {

        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getTitreAdresse().getNewValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_TITRE_ADRESSE", langue) + " "
                    + getSession().getCodeLibelle(containerModificationAdresse.getTitreAdresse().getNewValue()) + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getAttention().getNewValue())) {
            mailBody.append(app.getLabel("ATTENTION_MODIFICATION_ADRESSE", langue) + " "
                    + containerModificationAdresse.getAttention().getNewValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getCasePostale().getNewValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_CASE_POSTALE_ADRESSE", langue) + " "
                    + containerModificationAdresse.getCasePostale().getNewValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getLigneAdresse1().getNewValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_LIGNE_ADRESSE_1", langue) + " "
                    + containerModificationAdresse.getLigneAdresse1().getNewValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getLigneAdresse2().getNewValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_LIGNE_ADRESSE_2", langue) + " "
                    + containerModificationAdresse.getLigneAdresse2().getNewValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getLigneAdresse3().getNewValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_LIGNE_ADRESSE_3", langue) + " "
                    + containerModificationAdresse.getLigneAdresse3().getNewValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getLigneAdresse4().getNewValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_LIGNE_ADRESSE_4", langue) + " "
                    + containerModificationAdresse.getLigneAdresse4().getNewValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getRue().getNewValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_RUE", langue) + " "
                    + containerModificationAdresse.getRue().getNewValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getNumeroRue().getNewValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_NUMERO_RUE", langue) + " "
                    + containerModificationAdresse.getNumeroRue().getNewValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getRueRepertoire().getNewValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_RUE_REPERTOIRE", langue) + " "
                    + containerModificationAdresse.getRueRepertoire().getNewValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getNumPostal().getNewValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_NUM_POSTAL", langue) + " "
                    + containerModificationAdresse.getNumPostal().getNewValue() + "\n");
        }
        if (!JadeStringUtil.isBlankOrZero(containerModificationAdresse.getLocalite().getNewValue())) {
            mailBody.append(app.getLabel("CHANGEMENT_LOCALITE", langue) + " "
                    + containerModificationAdresse.getLocalite().getNewValue() + "\n");
        }
    }

    private void addModificationsTiers(String langue, StringBuffer mailBody) {

        if ("true".equals(modifications.get("nss"))) {
            mailBody.append(app.getLabel("NSS_MODIFICATION_TIERS", langue) + " " + getNSS() + " "
                    + app.getLabel("REMPLACE_MODIFICATION_TIERS", langue) + " " + getNewNSS() + "\n");
        }

        if ("true".equals(modifications.get("etatCivil"))) {
            mailBody.append(app.getLabel("ETAT_CIVIL_MODIFICATION", langue) + " " + getUserCode(getEtatCivil(), langue)
                    + " " + app.getLabel("REMPLACE_MODIFICATION_TIERS", langue) + " "
                    + getUserCode(getNewEtatCivil(), langue) + "\n");
        }

        if ("true".equals(modifications.get("nom"))) {
            mailBody.append(app.getLabel("NOM_MODIFICATION_TIERS", langue) + " " + getNom() + " "
                    + app.getLabel("REMPLACE_MODIFICATION_TIERS", langue) + " " + getNewNom() + "\n");
        }

        if ("true".equals(modifications.get("prenom"))) {
            mailBody.append(app.getLabel("PRENOM_MODIFICATION_TIERS", langue) + " " + getPrenom() + " "
                    + app.getLabel("REMPLACE_MODIFICATION_TIERS", langue) + " " + getNewPrenom() + "\n");
        }

        if ("true".equals(modifications.get("designation3"))) {
            mailBody.append(app.getLabel("NOM_SUITE_1_MODIFICATION", langue) + " " + getDesignation3() + " "
                    + app.getLabel("REMPLACE_MODIFICATION_TIERS", langue) + " " + getNewDesignation3() + "\n");
        }

        if ("true".equals(modifications.get("designation4"))) {
            mailBody.append(app.getLabel("NOM_SUITE_2_MODIFICATION", langue) + " " + getDesignation4() + " "
                    + app.getLabel("REMPLACE_MODIFICATION_TIERS", langue) + " " + getNewDesignation4() + "\n");
        }

        if ("true".equals(modifications.get("nomJeuneFille"))) {
            mailBody.append(app.getLabel("NOM_JEUNE_FILLE_MODIFICATION", langue) + " " + getNomJeuneFille() + " "
                    + app.getLabel("REMPLACE_MODIFICATION_TIERS", langue) + " " + getNewNomJeuneFille() + "\n");
        }

        if ("true".equals(modifications.get("sexe"))) {
            mailBody.append(app.getLabel("SEXE_MODIFICATION_TIERS", langue) + " " + app.getLabel(getSexe(), langue)
                    + " " + app.getLabel("REMPLACE_MODIFICATION_TIERS", langue) + " "
                    + app.getLabel(getNewSexe(), langue) + "\n");
        }

        if ("true".equals(modifications.get("dnaissance"))) {
            mailBody.append(app.getLabel("NAISSANCE_MODIFICATION_TIERS", langue) + " " + getDateNaissance() + " "
                    + app.getLabel("REMPLACE_MODIFICATION_TIERS", langue) + " " + getNewDateNaissance() + "\n");
        }

        if ("true".equals(modifications.get("ddeces"))) {
            mailBody.append(app.getLabel("DECES_MODIFICATION_TIERS", langue) + " " + getDateDeces() + " "
                    + app.getLabel("REMPLACE_MODIFICATION_TIERS", langue) + " " + getNewDateDeces() + "\n");
        }

        if ("true".equals(modifications.get("canton"))) {
            mailBody.append(app.getLabel("CANTON_MODIFICATION_TIERS", langue) + " "
                    + getUserCode(getIdCantonDomicile(), langue) + " "
                    + app.getLabel("REMPLACE_MODIFICATION_TIERS", langue) + " "
                    + getUserCode(getNewIdCantonDomicile(), langue) + "\n");
        }

        if ("true".equals(modifications.get("pays"))) {
            mailBody.append(app.getLabel("PAYS_MODIFICATION_TIERS", langue) + " "
                    + getLibellePays(getPaysDomicile(), langue) + " "
                    + app.getLabel("REMPLACE_MODIFICATION_TIERS", langue) + " "
                    + getLibellePays(getNewPaysDomicile(), langue) + "\n");
        }

        if ("true".equals(modifications.get("nationalite"))) {
            mailBody.append(app.getLabel("NATIONALITE_MODIFICATION_TIERS", langue) + " "
                    + getLibellePays(getNationalite(), langue) + " "
                    + app.getLabel("REMPLACE_MODIFICATION_TIERS", langue) + " "
                    + getLibellePays(getNewNationalite(), langue) + "\n");
        }

        if ("true".equals(modifications.get("langue"))) {
            mailBody.append(app.getLabel("LANGUE_MODIFICATION", langue) + " " + getUserCode(getLangue(), langue) + " "
                    + app.getLabel("REMPLACE_MODIFICATION_TIERS", langue) + " " + getUserCode(getNewLangue(), langue)
                    + "\n");
        }

        mailBody.append("\n\n");
    }

    private void addModificationsAdresse(String langue, StringBuffer mailBody) {

        if (typeModification.equals(TypeModificationsTiers.SUPPRESSION_ADRESSE)) {
            mailBody.append(app.getLabel("ADRESSE_TIERS_SUPPRIMEE", langue) + "\n");
            // } else if (typeModification.equals(TypeModificationsTiers.AJOUT_ADRESSE)) {
            // mailBody.append(app.getLabel("ADRESSE_TIERS_AJOUTEE", langue) + "\n");
        } else {
            if (containerModificationAdresse.getAttention().isModified()) {
                mailBody.append(app.getLabel("ATTENTION_MODIFICATION_ADRESSE", langue) + " "
                        + containerModificationAdresse.getAttention().getOldValue() + " "
                        + app.getLabel("CHANGEMENT_ADRESSE_TIERS_PAR", langue) + " "
                        + containerModificationAdresse.getAttention().getNewValue() + "\n");
            }

            if (containerModificationAdresse.getCasePostale().isModified()) {
                mailBody.append(app.getLabel("CHANGEMENT_CASE_POSTALE_ADRESSE", langue) + " "
                        + containerModificationAdresse.getCasePostale().getOldValue() + " "
                        + app.getLabel("CHANGEMENT_ADRESSE_TIERS_PAR", langue) + " "
                        + containerModificationAdresse.getCasePostale().getNewValue() + "\n");
            }

            if (containerModificationAdresse.getTitreAdresse().isModified()) {
                mailBody.append(app.getLabel("CHANGEMENT_TITRE_ADRESSE", langue) + " "
                        + getSession().getCodeLibelle(containerModificationAdresse.getTitreAdresse().getOldValue())
                        + " " + app.getLabel("CHANGEMENT_ADRESSE_TIERS_PAR", langue) + " "
                        + getSession().getCodeLibelle(containerModificationAdresse.getTitreAdresse().getNewValue())
                        + "\n");
            }

            if (containerModificationAdresse.getLigneAdresse1().isModified()) {
                mailBody.append(app.getLabel("CHANGEMENT_LIGNE_ADRESSE_1", langue) + " "
                        + containerModificationAdresse.getLigneAdresse1().getOldValue() + " "
                        + app.getLabel("CHANGEMENT_ADRESSE_TIERS_PAR", langue) + " "
                        + containerModificationAdresse.getLigneAdresse1().getNewValue() + "\n");
            }

            if (containerModificationAdresse.getLigneAdresse2().isModified()) {
                mailBody.append(app.getLabel("CHANGEMENT_LIGNE_ADRESSE_2", langue) + " "
                        + containerModificationAdresse.getLigneAdresse2().getOldValue() + " "
                        + app.getLabel("CHANGEMENT_ADRESSE_TIERS_PAR", langue) + " "
                        + containerModificationAdresse.getLigneAdresse2().getNewValue() + "\n");
            }

            if (containerModificationAdresse.getLigneAdresse3().isModified()) {
                mailBody.append(app.getLabel("CHANGEMENT_LIGNE_ADRESSE_3", langue) + " "
                        + containerModificationAdresse.getLigneAdresse3().getOldValue() + " "
                        + app.getLabel("CHANGEMENT_ADRESSE_TIERS_PAR", langue) + " "
                        + containerModificationAdresse.getLigneAdresse3().getNewValue() + "\n");
            }

            if (containerModificationAdresse.getLigneAdresse4().isModified()) {
                mailBody.append(app.getLabel("CHANGEMENT_LIGNE_ADRESSE_4", langue) + " "
                        + containerModificationAdresse.getLigneAdresse4().getOldValue() + " "
                        + app.getLabel("CHANGEMENT_ADRESSE_TIERS_PAR", langue) + " "
                        + containerModificationAdresse.getLigneAdresse4().getNewValue() + "\n");
            }

            if (containerModificationAdresse.getNumeroRue().isModified()) {
                mailBody.append(app.getLabel("CHANGEMENT_NUMERO_RUE", langue) + " "
                        + containerModificationAdresse.getNumeroRue().getOldValue() + " "
                        + app.getLabel("CHANGEMENT_ADRESSE_TIERS_PAR", langue) + " "
                        + containerModificationAdresse.getNumeroRue().getNewValue() + "\n");
            }

            if (containerModificationAdresse.getRue().isModified()) {
                mailBody.append(app.getLabel("CHANGEMENT_RUE", langue) + " "
                        + containerModificationAdresse.getRue().getOldValue() + " "
                        + app.getLabel("CHANGEMENT_ADRESSE_TIERS_PAR", langue) + " "
                        + containerModificationAdresse.getRue().getNewValue() + "\n");
            }

            if (containerModificationAdresse.getRueRepertoire().isModified()) {
                mailBody.append(app.getLabel("CHANGEMENT_RUE_REPERTOIRE", langue) + " "
                        + containerModificationAdresse.getRueRepertoire().getOldValue() + " "
                        + app.getLabel("CHANGEMENT_ADRESSE_TIERS_PAR", langue) + " "
                        + containerModificationAdresse.getRueRepertoire().getNewValue() + "\n");
            }

            if (containerModificationAdresse.getNumPostal().isModified()) {
                mailBody.append(app.getLabel("CHANGEMENT_NUM_POSTAL", langue) + " "
                        + containerModificationAdresse.getNumPostal().getOldValue() + " "
                        + app.getLabel("CHANGEMENT_ADRESSE_TIERS_PAR", langue) + " "
                        + containerModificationAdresse.getNumPostal().getNewValue() + "\n");
            }

            if (containerModificationAdresse.getLocalite().isModified()) {
                mailBody.append(app.getLabel("CHANGEMENT_LOCALITE", langue) + " "
                        + containerModificationAdresse.getLocalite().getOldValue() + " "
                        + app.getLabel("CHANGEMENT_ADRESSE_TIERS_PAR", langue) + " "
                        + containerModificationAdresse.getLocalite().getNewValue() + "\n");
            }
        }

        mailBody.append("\n");
    }

    @Override
    protected void _validate() throws Exception {
        // les emails sont envoyés par un méchanisme spécifique à ce processus (car il y a plusieurs destinataire)
        setSendCompletionMail(false); // donc pas de mail automatique...
    }

    public String getIdCantonDomicile() {
        return idCantonDomicile;
    }

    public String getDateDeces() {
        return dateDeces;
    }

    public String getDateModification() {
        return dateModification;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    @Override
    protected String getEMailObject() {

        return getSession().getLabel("OBJET_MAIL_PROCESS_MODIFICATION_TIERS");

    }

    public String getHeureModification() {
        return heureModification;
    }

    public String getIdGroupeNotification() {
        return idGroupeNotification;
    }

    public String getIdTier() {
        return idTier;
    }

    public HashMap<String, String> getModifications() {
        return modifications;
    }

    public TIPays getNationalite() {
        return nationalite;
    }

    public String getNewIdCantonDomicile() {
        return newIdCantonDomicile;
    }

    public String getNewDateDeces() {
        return newDateDeces;
    }

    public String getNewDateNaissance() {
        return newDateNaissance;
    }

    public TIPays getNewNationalite() {
        return newNationalite;
    }

    public String getNewNom() {
        return newNom;
    }

    public TIPays getNewPaysDomicile() {
        return newPaysDomicile;
    }

    public String getNewPrenom() {
        return newPrenom;
    }

    public String getNewSexe() {
        return newSexe;
    }

    public String getNom() {
        return nom;
    }

    public TIPays getPaysDomicile() {
        return paysDomicile;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public String getUser() {
        return user;
    }

    @Override
    public GlobazJobQueue jobQueue() {
        return GlobazJobQueue.READ_SHORT;
    }

    public void setIdCantonDomicile(String idCantonDomicile) {
        this.idCantonDomicile = idCantonDomicile;
    }

    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    public void setDateModification(String dateModification) {
        this.dateModification = dateModification;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setHeureModification(String heureModification) {
        this.heureModification = heureModification;
    }

    public void setIdGroupeNotification(String idGroupeNotification) {
        this.idGroupeNotification = idGroupeNotification;
    }

    public void setIdTier(String idTier) {
        this.idTier = idTier;
    }

    public void setModifications(HashMap<String, String> modifications) {
        this.modifications = modifications;
    }

    public void setNationalite(TIPays nationalite) {
        this.nationalite = nationalite;
    }

    public void setNewIdCantonDomicile(String newIdCantonDomicile) {
        this.newIdCantonDomicile = newIdCantonDomicile;
    }

    public void setNewDateDeces(String newDateDeces) {
        this.newDateDeces = newDateDeces;
    }

    public void setNewDateNaissance(String newDateNaissance) {
        this.newDateNaissance = newDateNaissance;
    }

    public void setNewNationalite(TIPays newNationalite) {
        this.newNationalite = newNationalite;
    }

    public void setNewNom(String newNom) {
        this.newNom = newNom;
    }

    public void setNewPaysDomicile(TIPays newIdPaysDomicile) {
        newPaysDomicile = newIdPaysDomicile;
    }

    public void setNewPrenom(String newPrenom) {
        this.newPrenom = newPrenom;
    }

    public void setNewSexe(String newSexe) {
        this.newSexe = newSexe;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPaysDomicile(TIPays paysDomicile) {
        this.paysDomicile = paysDomicile;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setContainerModificationAdresse(AdresseModificationsHandler containerModificationAdresse) {
        this.containerModificationAdresse = containerModificationAdresse;
    }

    public boolean getCommunicationOAI() {
        return isCommunicationOAI;
    }

    public void setCommunicationOAI(boolean isCommunicationOAI) {
        this.isCommunicationOAI = isCommunicationOAI;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getEtatCivil() {
        return etatCivil;
    }

    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    public String getNomJeuneFille() {
        return nomJeuneFille;
    }

    public void setNomJeuneFille(String nomJeuneFille) {
        this.nomJeuneFille = nomJeuneFille;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public String getNewPays() {
        return newPays;
    }

    public void setNewPays(String newPays) {
        this.newPays = newPays;
    }

    public String getNewEtatCivil() {
        return newEtatCivil;
    }

    public void setNewEtatCivil(String newEtatCivil) {
        this.newEtatCivil = newEtatCivil;
    }

    public String getNewNomJeuneFille() {
        return newNomJeuneFille;
    }

    public void setNewNomJeuneFille(String newNomJeuneFille) {
        this.newNomJeuneFille = newNomJeuneFille;
    }

    public String getNewLangue() {
        return newLangue;
    }

    public void setNewLangue(String newLangue) {
        this.newLangue = newLangue;
    }

    public String getDesignation3() {
        return designation3;
    }

    public void setDesignation3(String designation3) {
        this.designation3 = designation3;
    }

    public String getNewDesignation3() {
        return newDesignation3;
    }

    public void setNewDesignation3(String newDesignation3) {
        this.newDesignation3 = newDesignation3;
    }

    public String getDesignation4() {
        return designation4;
    }

    public void setDesignation4(String designation4) {
        this.designation4 = designation4;
    }

    public String getNewDesignation4() {
        return newDesignation4;
    }

    public void setNewDesignation4(String newDesignation4) {
        this.newDesignation4 = newDesignation4;
    }

    public TypeModificationsTiers getTypeModification() {
        return typeModification;
    }

    public void setTypeModification(TypeModificationsTiers typeModification) {
        this.typeModification = typeModification;
    }

    public String getNSS() {
        return nss;
    }

    public void setNSS(String nSS) {
        nss = nSS;
    }

    public String getNewNSS() {
        return newNSS;
    }

    public void setNewNSS(String newNss) {
        newNSS = newNss;
    }
}
