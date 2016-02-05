package globaz.corvus.properties;

import globaz.corvus.application.REApplication;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

public enum REProperties implements IProperties {

    ACOR_CHEMIN_SAUVEGARDE_FEUILLE_CALCUL("calculAcor.path",
            "Chemin vers le répertoire de sauvegarde des feuilles de calcul ACOR"),
    ACOR_FILE_ADAPTER_FACTORY("acor.re.factory.class",
            "Classe Adapter utilisée pour la génération des fichiers plats pour ACOR"),
    ACOR_SAUVEGARDER_FEUILLE_CALCUL_GED("isFeuilleCalculEnGED",
            "Active la sauvegarde de la feuiile de calcul ACOR en GED"),
    ACOR_SAUVER_FEUILLE_CALCUL("calculAcor.save",
            "Active la sauvegarde de la feuille de calcul dans le dossier spécifié par la propriété : calculAcor.path"),
    ACTIVER_CONFIDENTIALITE_DOCUMENT("documents.is.confidentiel",
            "Active la confidentialité des documents. (Description à compléter)"),
    ACTIVER_VALIDATEUR_ANAKIN("activer.anakin.validator",
            "Active la validation de.... par le module Anakin.  (Description à compléter)"),
    AFFICHER_DECISION_TRAITE_PAR("isTraitePar",
            "Doit-on indiquer 'traité par' sur les décisions.  (Description à compléter)"),
    AFFICHER_DOSSIER_TRAITE_PAR("isAfficherDossierTraitePar", "(Description à compléter)"),
    APPLICATION_MAIN_CLASS("applicationClassName", "Nom de la classe principale de l'application Corvus"),
    APPLICATION_NAME("applicationName", "Nom de l'application Coruvs"),
    CLASS_CI_UPLOADER("globaz.corvus.arc.uploader.inscription.ci", "Uploader pour les CI. (Description à compléter)"),
    CLONE_DEMANDE_API_COPIE("clone.corvus.demande.api.copie", "(Description à compléter)"),
    CLONE_DEMANDE_API_CORRECTION("clone.corvus.demande.api.correction", "(Description à compléter)"),
    CLONE_DEMANDE_INVALIDITE_COPIE("clone.corvus.demande.invalidite.copie", "(Description à compléter)"),
    CLONE_DEMANDE_INVALIDITE_CORRECTION("clone.corvus.demande.invalidite.correction", "(Description à compléter)"),
    CLONE_DEMANDE_SURVIVANT("clone.corvus.demande.survivant.correction", "(Description à compléter)"),
    CLONE_DEMANDE_SURVIVANT_COPIE("clone.corvus.demande.survivant.copie", "(Description à compléter)"),
    CLONE_DEMANDE_VIEILLESSE("clone.corvus.definition", "(Description à compléter)"),
    CLONE_DEMANDE_VIEILLESSE_COPIE("clone.corvus.demande.vieillesse.copie", "(Description à compléter)"),
    CLONE_DEMANDE_VIEILLESSE_CORRECTION("clone.corvus.demande.vieillesse.correction", "(Description à compléter)"),
    COPIE_POUR_AGENCE_COMMUNALE("isCopieAgenceCommunale",
            "Doit-on créer une copie de la décision pour l'agence communale du tiers requérant"),
    CREER_COPIE_CAISSE("isCopieCaisse", "Doit-on créer une copie pour la caisse lors de la création de la décision"),
    EMAIL_RESPONSABLE_ENVOI_CI("corvus.user.email", "Le responsable CI. (Description à compléter)"),
    GED_NOM_SERVICE_COTI(
            "ged.service.name",
            "Le nom du service GED des COTISATIONS. Ce nom doit correspondre au nom définit dans le fichier de config JadeClientServiceLocator.xml"),
    GED_NOM_SERVICE_RENTES(
            "domaine.nomService.ged",
            "Le nom du service GED des RENTES. Ce nom doit correspondre au nom définit dans le fichier de config JadeClientServiceLocator.xml"),
    GROUPE_GESTIONNAIRE("groupe.corvus.gestionnaire",
            "Nom du groupe des gestionnaires Corvus (seuls les users associés au groupe apparaissent dans la liste déroulante)"),
    GROUPE_NOTIFICATION("groupeNotification=", "Groupe de notification d'événement. (Description à compléter)"),
    INSERER_ENTETE_COPIE_FISC("isLettreEnTeteCopieFisc",
            "Insérer une lettre d'en-tête pour le fisc lors de la création de la décision"),
    INSERER_ENTETE_COPIE_OAI("isLettreEnTeteCopieOAI",
            "Insérer une lettre d'en-tête pour l'office AI lors de la création de la décision"),
    LETTRE_EN_TETE_DECISION_AJOURNEMENT("isLettreEnTeteCopieAjournement",
            "Chemin vers le répertoire de sauvegarde des feuilles de calcul ACOR"),
    ORGANE_EXECUTION_PAIEMENT("id.orgrane.execution.pmt.rentes",
            "Référence l'organe d'exécution (CCP) duquel seront éxecutés les ordres de versement."),
    ROLE_UTILISE_ENVOI_CI("corvus.role.resp.ci",
            "Rôle pour l'envoi des résultats de l'importation des CI. (Description à compléter)"),
    UTILISER_ADRESSE_DOMICILE(
            "isWantAdresseCourrier",
            "Si la caisse n'utilise pas le genre d'adresse 'domicile', cette propriété permet de rechercher les données du canton de domicile à travers l'adresse de courrier du type 'Rentes'. (Description à compléter)"),
    DNRA_PROCESS_EMAILS("dnra.process.emails",
            "permet de définir les adresses emails à utiliser lors du traitement DNRA, séparer les emails par des virgules");

    private String description;
    private String propertyName;

    REProperties(String propertyName, String description) {
        this.propertyName = propertyName;
        this.description = description;
    }

    @Override
    public String getApplicationName() {
        return REApplication.DEFAULT_APPLICATION_CORVUS;
    }

    @Override
    public Boolean getBooleanValue() throws PropertiesException {
        return CommonPropertiesUtils.getBoolean(this);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public String getValue() throws PropertiesException {
        return CommonPropertiesUtils.getValue(this);
    }

}
