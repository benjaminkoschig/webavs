package globaz.corvus.properties;

import globaz.corvus.application.REApplication;
import ch.globaz.common.properties.CommonPropertiesUtils;
import ch.globaz.common.properties.IProperties;
import ch.globaz.common.properties.PropertiesException;

public enum REProperties implements IProperties {

    ACOR_CHEMIN_SAUVEGARDE_FEUILLE_CALCUL("calculAcor.path",
            "Chemin vers le r�pertoire de sauvegarde des feuilles de calcul ACOR"),
    ACOR_FILE_ADAPTER_FACTORY("acor.re.factory.class",
            "Classe Adapter utilis�e pour la g�n�ration des fichiers plats pour ACOR"),
    ACOR_SAUVEGARDER_FEUILLE_CALCUL_GED("isFeuilleCalculEnGED",
            "Active la sauvegarde de la feuiile de calcul ACOR en GED"),
    ACOR_SAUVER_FEUILLE_CALCUL("calculAcor.save",
            "Active la sauvegarde de la feuille de calcul dans le dossier sp�cifi� par la propri�t� : calculAcor.path"),
    ACTIVER_CONFIDENTIALITE_DOCUMENT("documents.is.confidentiel",
            "Active la confidentialit� des documents. (Description � compl�ter)"),
    ACTIVER_VALIDATEUR_ANAKIN("activer.anakin.validator",
            "Active la validation de.... par le module Anakin.  (Description � compl�ter)"),
    AFFICHER_DECISION_TRAITE_PAR("isTraitePar",
            "Doit-on indiquer 'trait� par' sur les d�cisions.  (Description � compl�ter)"),
    AFFICHER_DOSSIER_TRAITE_PAR("isAfficherDossierTraitePar", "(Description � compl�ter)"),
    APPLICATION_MAIN_CLASS("applicationClassName", "Nom de la classe principale de l'application Corvus"),
    APPLICATION_NAME("applicationName", "Nom de l'application Coruvs"),
    CLASS_CI_UPLOADER("globaz.corvus.arc.uploader.inscription.ci", "Uploader pour les CI. (Description � compl�ter)"),
    CLONE_DEMANDE_API_COPIE("clone.corvus.demande.api.copie", "(Description � compl�ter)"),
    CLONE_DEMANDE_API_CORRECTION("clone.corvus.demande.api.correction", "(Description � compl�ter)"),
    CLONE_DEMANDE_INVALIDITE_COPIE("clone.corvus.demande.invalidite.copie", "(Description � compl�ter)"),
    CLONE_DEMANDE_INVALIDITE_CORRECTION("clone.corvus.demande.invalidite.correction", "(Description � compl�ter)"),
    CLONE_DEMANDE_SURVIVANT("clone.corvus.demande.survivant.correction", "(Description � compl�ter)"),
    CLONE_DEMANDE_SURVIVANT_COPIE("clone.corvus.demande.survivant.copie", "(Description � compl�ter)"),
    CLONE_DEMANDE_VIEILLESSE("clone.corvus.definition", "(Description � compl�ter)"),
    CLONE_DEMANDE_VIEILLESSE_COPIE("clone.corvus.demande.vieillesse.copie", "(Description � compl�ter)"),
    CLONE_DEMANDE_VIEILLESSE_CORRECTION("clone.corvus.demande.vieillesse.correction", "(Description � compl�ter)"),
    COPIE_POUR_AGENCE_COMMUNALE("isCopieAgenceCommunale",
            "Doit-on cr�er une copie de la d�cision pour l'agence communale du tiers requ�rant"),
    CREER_COPIE_CAISSE("isCopieCaisse", "Doit-on cr�er une copie pour la caisse lors de la cr�ation de la d�cision"),
    EMAIL_RESPONSABLE_ENVOI_CI("corvus.user.email", "Le responsable CI. (Description � compl�ter)"),
    GED_NOM_SERVICE_COTI(
            "ged.service.name",
            "Le nom du service GED des COTISATIONS. Ce nom doit correspondre au nom d�finit dans le fichier de config JadeClientServiceLocator.xml"),
    GED_NOM_SERVICE_RENTES(
            "domaine.nomService.ged",
            "Le nom du service GED des RENTES. Ce nom doit correspondre au nom d�finit dans le fichier de config JadeClientServiceLocator.xml"),
    GROUPE_GESTIONNAIRE("groupe.corvus.gestionnaire",
            "Nom du groupe des gestionnaires Corvus (seuls les users associ�s au groupe apparaissent dans la liste d�roulante)"),
    GROUPE_NOTIFICATION("groupeNotification=", "Groupe de notification d'�v�nement. (Description � compl�ter)"),
    INSERER_ENTETE_COPIE_FISC("isLettreEnTeteCopieFisc",
            "Ins�rer une lettre d'en-t�te pour le fisc lors de la cr�ation de la d�cision"),
    INSERER_ENTETE_COPIE_OAI("isLettreEnTeteCopieOAI",
            "Ins�rer une lettre d'en-t�te pour l'office AI lors de la cr�ation de la d�cision"),
    LETTRE_EN_TETE_DECISION_AJOURNEMENT("isLettreEnTeteCopieAjournement",
            "Chemin vers le r�pertoire de sauvegarde des feuilles de calcul ACOR"),
    ORGANE_EXECUTION_PAIEMENT("id.orgrane.execution.pmt.rentes",
            "R�f�rence l'organe d'ex�cution (CCP) duquel seront �xecut�s les ordres de versement."),
    ROLE_UTILISE_ENVOI_CI("corvus.role.resp.ci",
            "R�le pour l'envoi des r�sultats de l'importation des CI. (Description � compl�ter)"),
    UTILISER_ADRESSE_DOMICILE(
            "isWantAdresseCourrier",
            "Si la caisse n'utilise pas le genre d'adresse 'domicile', cette propri�t� permet de rechercher les donn�es du canton de domicile � travers l'adresse de courrier du type 'Rentes'. (Description � compl�ter)"),
    DNRA_PROCESS_EMAILS("dnra.process.emails",
            "permet de d�finir les adresses emails � utiliser lors du traitement DNRA, s�parer les emails par des virgules"),
    ACTIVER_ANNONCES_XML("isAnnoncesXML", "d�termine si les annonces sont envoy�es au format xml"),
    RACINE_NOM_FICHIER_OUTPUT_ZAS("racine.nom.fichier.centrale",
            "donne la ra�ine nom du fichier � envoyer � la centrale"),
    FTP_CENTRALE_PATH("centrale.url", "donne l'url de la centrale"),
    CENTRALE_TEST("centrale.test",
            "d�finit si nous sommes en mode test pour mettre la balise test dans le fichier output de la centrale"),
    TRANSFERT_ACTIVER_ANNONCES_XML("transfert.isAnnoncesXML","d�termine si les annonces sont envoy�es au format xml"),
    RECAP_ACTIVER_ANNONCES_XML("recapRentes.isAnnoncesXML", "d�termine si les annonces sont envoy�es au format xml"),
    RECAP_RACINE_NOM_FICHIER_OUTPUT_ZAS("recapRentes.racine.nom.fichier.centrale",
            "donne la ra�ine nom du fichier � envoyer � la centrale"),
    RECAP_FTP_CENTRALE_PATH("recapRentes.centrale.url", "donne l'url de la centrale"),
    RECAP_CENTRALE_TEST("recapRentes.centrale.test",
            "d�finit si nous sommes en mode test pour mettre la balise test dans le fichier output de la centrale"),
    TYPE_DE_CAISSE("type_de_caisse", "renseigne le type de caisse (caisse cant ou prof)"),
    ACOR_UTILISER_VERSION_WEB("acor.utiliser.version.web","Boolean, si true, utilisation de la version Web d'ACOR"),
    ACOR_AFFICHER_VERSION_POSTE("acor.utiliser.version.poste.utilisateur", "Boolean, si true, affichage du lien ACORv3");


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
