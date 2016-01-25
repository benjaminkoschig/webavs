package ch.globaz.cygnus.process.document;

import globaz.cygnus.application.RFApplication;
import globaz.globall.api.GlobazSystem;
import globaz.jade.ged.client.JadeGedFacade;
import globaz.jade.log.JadeLogger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public enum RFDocumentEnum {

    // Lettre type : ménage
    RFM_LETTRE_TYPE_AIDE_AU_MENAGE_PRIVE_DEMANDE_EVALUATION_DES_BESOINS_AU_CMS("7039PRF", "?", null),
    RFM_LETTRE_TYPE_AIDE_AU_MENAGE_PRIVE_DEMANDE_REEVALUATION_DES_BESOINS_AU_CMS("7040PRF", "?", null),
    RFM_LETTRE_TYPE_AIDE_AU_MENAGE_PRIVE_RAPPEL_DEMANDE_EVALUATION_DES_BESOINS_AU_CMS("7041PRF", "?", null),

    // Lettre type : dentiste
    RFM_LETTRE_TYPE_DENTISTE_BIS_DEMANDE_DOCUMENTS_MANQUANTS_AU_DENTISTE("7031PRF", "?", null),
    RFM_LETTRE_TYPE_DENTISTE_DEMANDE_DOCUMENTS_MANQUANTS_ET_QMD_AU_DENTISTE("7032PRF", "?", null),
    RFM_LETTRE_TYPE_DENTISTE_DEMANDE_PRECISION_TRAITEMENT_ORTHODONTIQUE_ET_QMD_AU_DENTISTE("7037PRF", "?", null),
    RFM_LETTRE_TYPE_DENTISTE_ENVOIS_DEVIS_AU_MEDECIN_CONSEIL("7028PRF", "?", null),
    RFM_LETTRE_TYPE_DENTISTE_ENVOIS_DEVIS_TRAITEMENT_ORTHODONTIQUE_AU_MEDECIN_CONSEIL("7038PRF", "?", null),
    RFM_LETTRE_TYPE_DENTISTE_ENVOIS_DOSSIER_COMPLET_AU_MEDECIN_CONSEIL("7029PRF", "?", null),
    RFM_LETTRE_TYPE_DENTISTE_ENVOIS_FACTURE_AU_MEDECIN_CONSEIL("7027PRF", "?", null),
    RFM_LETTRE_TYPE_DENTISTE_ENVOIS_FACTURE_MEDECIN_CONSEIL_DE_LA_FACTURE_TRAITEMENT_ETRANGER("7035PRF", "?", null),
    RFM_LETTRE_TYPE_DENTISTE_ENVOIS_RAPPEL_AU_MEDECIN_CONSEIL("7030PRF", "?", null),
    RFM_LETTRE_TYPE_DENTISTE_LETTRE_ASSURE_SUITE_AU_DEVIS_ETRANGER("7036PRF", "?", null),
    RFM_LETTRE_TYPE_DENTISTE_LETTRE_ASSURE_SUITE_REPONSE_MEDECIN_CONSEIL_DEVIS("7034PRF", "?", null),
    RFM_LETTRE_TYPE_DENTISTE_RAPPEL_DEMANDE_DOCUMENTS_MANQUANTS_ET_QMD_AU_DENTISTE("7033PRF", "?", null),

    // Lettres type : lit électrique
    RFM_LETTRE_TYPE_LIT_ELECTRIQUE_BON_DE_LIVRAISON("7022PRF", "?", null),
    RFM_LETTRE_TYPE_LIT_ELECTRIQUE_QUESTIONNAIRE_CCJU("7021PRF", "?", null),

    // Lettre type : moyens auxiliaires
    RFM_LETTRE_TYPE_MOYENS_AUXILIAIRES_ANNEXE_BON_ACCUSE_DE_RECEPTION_BENEFICIARE_PC("7024PRF", "?", null),
    RFM_LETTRE_TYPE_MOYENS_AUXILIAIRES_ANNEXE_BON_ACHAT_ACCUSE_DE_RECPETION_BENEFICIAIRE_PC("7026PRF", "?", null),
    RFM_LETTRE_TYPE_MOYENS_AUXILIAIRES_BON_ACHAT_POUR_REMISE_EN_PRET("7025PRF", "?", null),
    RFM_LETTRE_TYPE_MOYENS_AUXILIAIRES_BON_REMISE_EN_PRET("7023PRF", "?", null),

    // Lettres type : régime
    RFM_LETTRE_TYPE_REGIME_DECISION_OCTROI("7005PRF", "66001407", null),
    RFM_LETTRE_TYPE_REGIME_DECISION_REFUS_1("7006PRF", "66001402", null),
    RFM_LETTRE_TYPE_REGIME_DECISION_REFUS_2("7007PRF", "66001408", null),
    RFM_LETTRE_TYPE_REGIME_DECISION_REFUS_3("7008PRF", "66001414", null),
    RFM_LETTRE_TYPE_REGIME_DECISION_SUPPRESSION("7009PRF", "66001412", null),
    RFM_LETTRE_TYPE_REGIME_DEMANDE_EVALUATION_AU_CMS("7016PRF", "66001405", null),
    RFM_LETTRE_TYPE_REGIME_DROIT_MAINTENU_SUITE_A_REVISION("7018PRF", "66001411", null),
    RFM_LETTRE_TYPE_REGIME_FORMULAIRE_EVALUATION_AU_CMS("7017PRF", "66001406", null),
    RFM_LETTRE_TYPE_REGIME_PRESCRIPTION_DIETETIQUE_FORMULAIRE("7015PRF", "66001404", null),
    RFM_LETTRE_TYPE_REGIME_PRESCRIPTION_DIETETIQUE_MEDECIN("7014PRF", "66001403", null),
    RFM_LETTRE_TYPE_REGIME_QUESTIONNAIRE("7010PRF", "66001401", null),
    RFM_LETTRE_TYPE_REGIME_QUESTIONNAIRE_REVISION("7012PRF", "66001409", null),
    RFM_LETTRE_TYPE_REGIME_RAPPEL_QUESTIONNAIRE("7011PRF", "66001413", null),
    RFM_LETTRE_TYPE_REGIME_RAPPEL_QUESTIONNAIRE_REVISION("7013PRF", "66001410", null),

    // Lettres-type : restitution
    RFM_LETTRE_TYPE_RESTITUTION_SUITE_A_UN_NOUVEAU_CALCUL_RETROACTIF("7019PRF", "?", null),
    RFM_LETTRE_TYPE_RESTITUTION_SUITE_AU_DECES_DU_BENEFICIAIRE("7020PRF", "?", null);

    private final String csDocument;
    private Boolean isGed = null;
    private final String noInforom;

    RFDocumentEnum(String noInforom, String csDocument, Boolean isGed) {
        this.noInforom = noInforom;
        this.csDocument = csDocument;
        this.isGed = isDocumentForGed();
    }

    public String getCsDocument() {
        return csDocument;
    }

    public Boolean getIsGed() {
        return isGed;
    }

    public String getNoInforom() {
        return noInforom;
    }

    Boolean isDocumentForGed() {

        Boolean isDocumentForGed = false;

        try {
            String target = GlobazSystem.getApplication(RFApplication.DEFAULT_APPLICATION_CYGNUS).getProperty(
                    RFApplication.PROPERTY_GED_TARGET_NAME);

            List<String> liste = new ArrayList<String>();
            if (target != null) {
                liste = JadeGedFacade.getDocumentNamesList(target);
            } else {
                liste = JadeGedFacade.getDocumentNamesList();
            }

            Iterator iter = liste.iterator();

            while (iter.hasNext()) {
                String s = (String) iter.next();
                if (noInforom.equals(s)) {
                    isDocumentForGed = true;
                    break;
                }
            }
        } catch (Exception e) {
            JadeLogger.warn(RFDocumentEnum.class,
                    "The jadeGedFacade throw an exception, the ged boolean value for RFDocumentEnum cannot be defined");
            isDocumentForGed = null;
        }
        return isDocumentForGed;
    }
}
