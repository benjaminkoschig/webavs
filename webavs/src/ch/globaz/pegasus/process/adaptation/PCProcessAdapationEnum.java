package ch.globaz.pegasus.process.adaptation;

import globaz.jade.context.JadeThread;

public enum PCProcessAdapationEnum {
    DATE_ADAPTATION("pegasus.process.adaptation.dateAdaptation"),
    DESC_HOME("pegasus.process.adaptation.libelleHome"),
    DESCRIPTION_CONJOINT("pegasus.process.adaptation.descriptionConjoint"),
    HAS_ADAPTATION_DES_PSAL("pegasus.process.adaptation.adaptationPSAL"),
    HAS_ADAPTATION_DES_RENTE("pegasus.process.adaptation.hasAdaptationRente"),
    HAS_DELETE_VERSION_DROIT("pegasus.process.adaptation.hasDeleteVersionDroit"),
    ID_DECISION_HEADER("pegasus.process.adaptation.idDecisionheader"),
    ID_DECISION_HEADER_CONJOINT("pegasus.process.adaptation.idDecisionHeaderConjoint"),
    ID_HOME("pegasus.process.adaptation.idHome"),
    ID_PLAN_CALCUL_RETENU("pegasus.process.adaptation.idPlanCalculRetenu"),
    ID_PLAN_CALCUL_RETENU_CONJOINT("pegasus.process.adaptation.idPlanCalculRetenuConjoint"),
    ID_PSAL_UPDATED("pegasus.process.adaptation.idPsalUpdated"),
    ID_TIERS_AYANT_DROIT("pegasus.process.adaptation.idTiersAyantDroit"),
    ID_TIERS_CONJOINT("pegasus.process.adaptation.idTiersConjoint"),
    NEW_DONATION("pegasus.process.adaptation.newDonation"),
    NEW_DONATION_CONJOINT("pegasus.process.adaptation.newDonationConjoint"),
    NEW_ETAT_PC("pegasus.process.adaptation.newEtatPc"),
    NEW_ETAT_PC_CONJOINT("pegasus.process.adaptation.newEtatPcConjoint"),
    NEW_NB_ENFANT("pegasus.process.adaptation.newNbEnfant"),
    NEW_PC("pegasus.process.adaptation.newPc"),
    NEW_PC_CONJOINT("pegasus.process.adaptation.newPcConjoint"),
    NEW_PRIX_HOME("pegasus.process.adaptation.newPrixHome"),
    NEW_PRIX_HOME_CONJOINT("pegasus.process.adaptation.newPrixHomeConjoint"),
    NEW_RENTE_AVS_AI("pegasus.process.adaptation.newRenteAvsAi"),
    NEW_RENTE_AVS_AI_CONJOINT("pegasus.process.adaptation.newRenteAvsAiConjoint"),
    OLD_DONATION("pegasus.process.adaptation.oldDonation"),
    OLD_DONATION_CONJOINT("pegasus.process.adaptation.oldDonationConjoint"),
    OLD_ETAT_PC("pegasus.process.adaptation.oldEtatPc"),
    OLD_ETAT_PC_CONJOINT("pegasus.process.adaptation.oldEtatPcConjoint"),
    OLD_NB_ENFANT("pegasus.process.adaptation.oldNbEnfant"),
    OLD_PC("pegasus.process.adaptation.oldPc"),
    OLD_PC_CONJOINT("pegasus.process.adaptation.oldPcConjoint"),
    OLD_PRIX_HOME("pegasus.process.adaptation.oldPrixHome"),
    OLD_PRIX_HOME_CONJOINT("pegasus.process.adaptation.oldPrixHomeConjoint"),
    OLD_RENTE_AVS_AI("pegasus.process.adaptation.oldRenteAvsAi"),
    OLD_RENTE_AVS_AI_CONJOINT("pegasus.process.adaptation.oldRenteAvsAiConjoint"),
    PSAL_MONTANT_ANCIEN("pegasus.process.adaptation.ancienMontantPSAL"),
    PSAL_MONTANT_NOUVEAU("pegasus.process.adaptation.nouveauMontantPSAL"),
    DATE_DOCUMENT_IMPRESSION("pegasus.process.adaptation.dateDocImpression");

    private final String idLabel;

    PCProcessAdapationEnum(String idLabel) {
        this.idLabel = idLabel;
    }

    public String toLabel() {
        return JadeThread.getMessage(idLabel);
    }
}